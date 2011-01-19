package frontier.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;
import net.oauth.OAuthProblemException;
import net.oauth.OAuthServiceProvider;
import net.oauth.client.OAuthClient;
import net.oauth.client.OAuthResponseMessage;
import net.oauth.client.httpclient3.HttpClient3;
import net.oauth.server.OAuthServlet;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;



import twitter4j.Paging;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.http.AccessToken;


import frontier.common.RedirectException;
import frontier.common.TwitterUtility;
import frontier.dto.AppDefDto;
import frontier.dto.OauthTokensDto;
import frontier.dto.UserInfoDto;
import frontier.entity.Members;
import frontier.entity.OauthTokensStore;

public class OauthConsumerService {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	public AppDefDto appDefDto;
	@Resource
	protected JdbcManager jdbcManager;
	@Resource
	public UserInfoDto userInfoDto;

	@Resource
	public OauthTokensDto oauthTokensDto;
	
	@Resource
	protected MembersService membersService;
	@Resource
	protected FrontiernetService frontiernetService;

    @Resource
    HttpServletRequest  request;
    @Resource
    HttpServletResponse response;
	    
    public OAuthClient CLIENT = new OAuthClient(new HttpClient3());
 
    /* CallBack  URL */
    public static final String PATH = "/pc/profile1/callBack";
    

    private static final Collection<String> RECOVERABLE_PROBLEMS = new HashSet<String>();
    static {
        RECOVERABLE_PROBLEMS.add("token_revoked");
        RECOVERABLE_PROBLEMS.add("token_expired");
        RECOVERABLE_PROBLEMS.add("permission_unknown");
        // In the case of permission_unknown, getting a fresh token
        // will cause the Service Provider to ask the User to decide.
    }

    /**
     * Twitterアカウントの切り替えを行う
     * @param mid メンバーID
     * @param oldTwitterID 現在使用中のTwitterユーザID
     * @param newTwitterID 切り替え後のTwitterユーザID
     */
    public void changeTwitterAccount(String mid,Integer oldTwitterID,Integer newTwitterID){
    	//現在使用中のTwitterアカウントを未使用にする
    	updUseFlg(mid, oldTwitterID, "0");
    	
    	//切り替え後のTwitterアカウントを有効にする
    	updUseFlg(mid,newTwitterID,"1");
    }

    /**
     * TwitterのアカウントとFrontierに登録してあるアカウントを比較し違いがあれば更新する
     * @param mid
     * @param twitterAccount
     */
	public Integer checkTwitterAccount(String mid,User u){
		OauthTokensStore params = new OauthTokensStore();
		params.mid          = mid;
		params.consumerId   = appDefDto.TWI_PROVIDER;
		params.screenname   = u.getScreenName();
		params.twitname     = u.getName();
		params.pic          = u.getProfileImageURL().toString();
		params.twituserid   = u.getId();
		
		return jdbcManager.updateBySqlFile("data/updTwitterUserInfo.sql", params).execute();
	}

    
    /**
     * 現在使用中のTwitterアカウント名を取得する
     * @param domain frontierドメイン
     * @param fid frontier ID
     * @return　Twitterアカウント名。全て未使用の場合はnullを返す
     */
    public String editMid(String domain,String fid){
    	String accountName = null;
    	//使用しているfrontier固有のmidを取得する
    	List<BeanMap> lbm = frontiernetService.getOpenId(domain, fid);
    	
    	if(lbm.size()!=0){
    		//使用中のTwitterアカウントを検索
    		List<BeanMap> lbm2 = getTokens((String)lbm.get(0).get("mid"),appDefDto.TWI_PROVIDER,"1",null);
    		
    		if(lbm2.size()!=0){
    			accountName = (String)lbm2.get(0).get("screenname");
    		}
    	}
    	
    	return accountName;
    }
    
    /**
     * 保存されているtoken情報をDBから取得
     * @param mid メンバーID
     * @param consumerId コンシューマID
     * @param useflg 1：使用中、0：未使用、null：検索条件に含めない
     * @param twituserid TwitterでのユーザID。検索条件として使用しない場合はNULLを設定。
     * @return　検索条件で検索したtoken情報
     */
    public List<BeanMap> getTokens(String mid,String consumerId,String useflg,Integer twituserid){
    	List<BeanMap> results = null;
    	//
		Map<String,Object> params = new HashMap<String,Object>();
		
		//パラメータ設定
		params.put("mid"       , mid);
		params.put("consumerid", consumerId);
		params.put("useflg", useflg);
		params.put("twituserid", twituserid);
		
		//SQL実行
		results =  jdbcManager
					.selectBySqlFile(BeanMap.class, "data/selOAuthTokens.sql",params)
					.getResultList();
    	return results;
    }
	
	
	
	public OAuthResponseMessage oauthExecute(String provider){

		OAuthConsumer consumer = null;
		OAuthAccessor accessor = null;
		
		// コンシューマ情報をDBから取得
		List<BeanMap> results = getConsumerInfo(provider);

		consumer = getConsumer(provider,results);
        try{
            accessor = getAccessor(consumer);
        }catch(Exception e){
        	try{
        		handleException(e,  consumer);
        	}catch(Exception ex){
        		ex.printStackTrace();

        		//　session クリア
        		oauthTokensDto.oauthTokens.put((String)consumer.getProperty("name"), null);
        		throw new RuntimeException();

        	}
        }
		return null;
	}

	
	public String callBack(TwitterService twitterService){
		
        OAuthConsumer consumer = null;
        try {
            final OAuthMessage requestMessage = OAuthServlet.getMessage(request, null);
            requestMessage.requireParameters("consumer");
            final String consumerName = requestMessage.getParameter("consumer");
            
    		// コンシューマ情報をDBから取得
    		List<BeanMap> results = getConsumerInfo(consumerName);

            consumer = getConsumer(consumerName,results);
            
            //
    		// sessionに保持しているRequest Tokenを取得。
            OAuthAccessor accessor = newAccessor(consumer);
            final String expectedToken = accessor.requestToken;
            
            //
            // パラメータから取得したRequest Tokenを取得。
            String requestToken = requestMessage.getParameter(OAuth.OAUTH_TOKEN);
            
            //
            // Request Token の有効性チェック
            if (requestToken == null || requestToken.length() <= 0) {
                logger.warn(request.getMethod() + " "
                        + OAuthServlet.getRequestURL(request));
                requestToken = expectedToken;
                if (requestToken == null) {
                    OAuthProblemException problem = new OAuthProblemException(OAuth.Problems.PARAMETER_ABSENT);
                    problem.setParameter(OAuth.Problems.OAUTH_PARAMETERS_ABSENT, OAuth.OAUTH_TOKEN);
                    throw problem;
                }
            } else if (!requestToken.equals(expectedToken)) {
                OAuthProblemException problem = new OAuthProblemException("token_rejected");
                problem.setParameter("oauth_rejected_token", requestToken);
                problem.setParameter("oauth_expected_token", expectedToken);
                throw problem;
            }
            
            // 
            // OAuthの 有効性パラメータが返却されてきた場合、パラメータに設定する。
            List<OAuth.Parameter> parameters = null;
            String verifier = requestMessage.getParameter(OAuth.OAUTH_VERIFIER);
            if (verifier != null) {
                parameters = OAuth.newList(OAuth.OAUTH_VERIFIER, verifier);
            }
            //
            // Access Token取得のため、サービスプロバイダへリクエスト
            OAuthMessage result = CLIENT.getAccessToken(accessor, null, parameters);
            if (accessor.accessToken != null) {
                //String returnTo = requestMessage.getParameter("returnTo");
            	String returnTo = appDefDto.TWI_RETURN_TO;
                if (returnTo == null) {
                    returnTo = request.getContextPath(); // 戻り先が返却されなかった場合のリダイレクト先?
                }

                //
                // Access Token をセッションに保存
                Map <String,String> tokens = new HashMap<String,String>();
                tokens.put("requestToken",null);
                tokens.put("accessToken", accessor.accessToken);
                tokens.put("tokenSecret", accessor.tokenSecret);
                oauthTokensDto.oauthTokens.put(consumerName,tokens);

                //登録済みのデータを論理削除する
                //AccessTokenは「123-xxx」の形で「-」の前がユーザIDなのでそこだけ切り出す。
                String at = (String)tokens.get("accessToken");
                String[] sat = at.split("-");

                TwitterUtility tu = new TwitterUtility();
                //OAuth認証情報を取得
                Twitter twitter = twitterService.setTwitter(userInfoDto.memberId, (String)tokens.get("accessToken"), (String)tokens.get("tokenSecret"), results);

                //Twitterからユーザ情報を取得
                User u = tu.getUserInfo(twitter, Integer.parseInt(sat[0]));
                
                //Access TokenをDBに登録する。
                updateTokens((String)consumer.getProperty("name"),u);
                
                // 
                // APPのURLへリダイレクト
                throw new RedirectException(returnTo);
                
            }
            
            //
            // その他のエラー時
            OAuthProblemException problem = new OAuthProblemException(OAuth.Problems.PARAMETER_ABSENT);
            problem.setParameter(OAuth.Problems.OAUTH_PARAMETERS_ABSENT, OAuth.OAUTH_TOKEN);
            problem.getParameters().putAll(result.getDump());
            throw problem;
        } catch (Exception e) {
        	try{
        		handleException(e,  consumer);
        	}catch(Exception ex){
        		ex.printStackTrace();

        		//　session クリア
        		oauthTokensDto.oauthTokens.put((String)consumer.getProperty("name"), null);            	

        		throw new RuntimeException();
        		
        	}
        }
		
		return null;
	}	
	    
    
    /**
     * コンシューマ情報を取得。
     * @param provider
     * @return
     */
	public OAuthConsumer getConsumer(String provider,List<BeanMap> results){
    
		OAuthConsumer consumer   = null;
		
		if(results.size() > 0 ){			
			consumer = new OAuthConsumer(
					(String)results.get(0).get("callbackurl"),
					(String)results.get(0).get("consumerkey"),
					(String)results.get(0).get("consumersecret"),
					new OAuthServiceProvider(
							(String)results.get(0).get("requesttokenurl"),
							(String)results.get(0).get("userauthrizationurl"),
							(String)results.get(0).get("accesstokenurl")				
							)
								
					);
		}
		consumer.setProperty("name", provider);
		return consumer;
	}
    
    /**
     * トークンをセッションへ取得（作成）する。
     * @param request
     * @param response
     * @param consumer
     * @return
     * @throws OAuthException
     * @throws IOException
     * @throws URISyntaxException
     */
	public OAuthAccessor getAccessor(OAuthConsumer consumer)
	throws OAuthException, IOException, URISyntaxException{
	    
		
		OAuthAccessor accessor = null;

		if(oauthTokensDto.oauthTokens==null){
			oauthTokensDto.oauthTokens = new HashMap<String,Map<String,String>>();
		}

		//
		// セッションに保存しているtokenを取得。
		Map<String,String>tokens = oauthTokensDto.oauthTokens.get((String)consumer.getProperty("name"));
		
		tokens = new HashMap<String,String>();
	   	tokens.put("requestToken", null);
	   	tokens.put("accessToken" , null);
	   	tokens.put("tokenSecret" , null);

	   	// セッションにtoken情報を保存
	   	oauthTokensDto.oauthTokens.put((String)consumer.getProperty("name"), tokens);

		//
		// Access Tokenが設定すみであれば、tokenの情報をそのまま返す。
		// なければ、サービスプロバイダからのtoken取得手続きを実行。
        accessor = newAccessor(consumer);
        if (accessor.accessToken == null) {
            getAccessToken(accessor);
        }
		return accessor;
	}
	
    /**
     * token情報をセッションより設定する。
     * @param consumer
     * @return
     * @throws OAuthException
     */
	public OAuthAccessor newAccessor(OAuthConsumer consumer)
    throws OAuthException {
    	OAuthAccessor accessor = new OAuthAccessor(consumer);
    	//
    	//
    	Map<String,String> tokens = oauthTokensDto.oauthTokens.get(consumer.getProperty("name"));

    	
    	accessor.requestToken = tokens.get("requestToken");
    	accessor.accessToken  = tokens.get("accessToken");
    	accessor.tokenSecret  = tokens.get("tokenSecret");
    	return accessor;
    }

	/**
	 * token情報をサービスプロバイダより取得し設定。
	 * @param request
	 * @param accessor
	 * @throws OAuthException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
    private  void getAccessToken(OAuthAccessor accessor)
        throws OAuthException, IOException, URISyntaxException{
    	

    	//
    	// CallBack URLを取得。
    	String callbackURL = getCallbackURL((String) accessor.consumer.getProperty("name"));
        List<OAuth.Parameter> parameters = OAuth.newList(OAuth.OAUTH_CALLBACK, callbackURL);
        
        // 
        // Google needs to know what you intend to do with the access token:
        // 
        Object scope = accessor.consumer.getProperty("request.scope");
        if (scope != null) {
            parameters.add(new OAuth.Parameter("scope", scope.toString()));
        }
        
        
        //
        // サービスプロバイダへリクエストし、Request Tokenを取得。
        OAuthMessage response = CLIENT.getRequestTokenResponse(accessor, null, parameters);
        
        Map<String,String> tokens = new HashMap<String,String>();
        tokens.put("accessToken" , accessor.accessToken);
        tokens.put("requestToken", accessor.requestToken);
        tokens.put("tokenSecret" , accessor.tokenSecret);

        //セッションへ保存。
        oauthTokensDto.oauthTokens.put((String) accessor.consumer.getProperty("name"),tokens);
        
        //
        // サービスプロバイダの認可処理画面へリダイレクト
        // (authrizationURLにパラメータを設定し、RedirectExceptionをスロー)
        String authorizationURL = accessor.consumer.serviceProvider.userAuthorizationURL;
        if (authorizationURL.startsWith("/")) {
            authorizationURL = (new URL(new URL(request.getScheme() + "://" + appDefDto.FP_CMN_HOST_NAME + "/frontier/pc/profile1"), 
                    request.getContextPath() + authorizationURL))
                    .toString();           
        }
        authorizationURL = OAuth.addParameters(authorizationURL , OAuth.OAUTH_TOKEN, accessor.requestToken);
        if (response.getParameter(OAuth.OAUTH_CALLBACK_CONFIRMED) == null) {
            authorizationURL = OAuth.addParameters(authorizationURL , OAuth.OAUTH_CALLBACK, callbackURL);
        }
        throw new RedirectException(authorizationURL);
    }

	
	/**
	 * OAuthのExceptionハンドリング
	 * (リダイレクト処置を含む)
	 * @param e
	 * @param request
	 * @param response
	 * @param consumer
	 * @throws IOException
	 * @throws ServletException
	 */
	public void handleException(Exception e,OAuthConsumer consumer)
    throws IOException, ServletException {
	    
		//
		// RedirectException の場合、設定されているURLへリダイレクトを実行
		// リダイレクトを実行
        if (e instanceof RedirectException) {
            RedirectException redirect = (RedirectException) e;
            String targetURL = redirect.getTargetURL();
            if (targetURL != null) {
                response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
                response.setHeader("Location", targetURL);
            }
        //
        //　OAuthProblemException
        //　(サービスプロバイダより、問題がかえされた場合)
        //
        } else if (e instanceof OAuthProblemException) {
            OAuthProblemException p = (OAuthProblemException) e;
            String problem = p.getProblem();

            //
            // リカバリ可能な問題？　の場合、
        	//　再度 access Token 取得を実行
            if (consumer != null && RECOVERABLE_PROBLEMS.contains(problem)) {
                try {

                	OAuthAccessor accessor = newAccessor(consumer);
                    getAccessToken(accessor);
            	   	
            	   	
                } catch (Exception e2) {
                    handleException(e2,  null);
                }

           //
           // リカバリ不可能な場合
           // ()
            } else {
                try {
                    StringWriter s = new StringWriter();
                    PrintWriter pw = new PrintWriter(s);
                    e.printStackTrace(pw);
                    pw.flush();
                    p.setParameter("stack trace", s.toString());
                    
                    logger.error(s.toString());
                    
                } catch (Exception rats) {
                }
                /*
                response.setStatus(p.getHttpStatusCode());
                response.resetBuffer();
                request.setAttribute("OAuthProblemException", p);
                request.getRequestDispatcher //
                        ("/OAuthProblemException.jsp").forward(request,
                                response);
                */               
                throw new ServletException(e);
            }
        } else if (e instanceof IOException) {
            throw (IOException) e;
        } else if (e instanceof ServletException) {
            throw (ServletException) e;
        } else if (e instanceof RuntimeException) {
            throw (RuntimeException) e;
        } else {
            throw new ServletException(e);
        }
		
	}

    /**
     * レスポンス結果の出力（テスト用）
     * @param from
     * @param into
     * @throws IOException
     */
	public void copyResponse(OAuthResponseMessage from) throws IOException {
        InputStream in = from.getBodyAsStream();
        OutputStream out = response.getOutputStream();
        response.setContentType(from.getHeader("Content-Type"));
                
        try {
            copyAll(in, out);
        } finally {
            in.close();
        }
    }

    private  void copyAll(InputStream from, OutputStream into) throws IOException {
        byte[] buffer = new byte[1024];
        for (int len; 0 < (len = from.read(buffer, 0, buffer.length));) {
            into.write(buffer, 0, len);
        }
    }
	

    /**
     * CallBack URLの編集
     * @param request
     * @param consumerName
     * @return
     * @throws IOException
     */
    private  String getCallbackURL(String consumerName)
    throws IOException{
        //URL base = new URL(new URL(request.getRequestURL().toString()), request.getContextPath() + PATH);
        URL base = new URL(new URL(request.getScheme() + "://" + appDefDto.FP_CMN_HOST_NAME + "/frontier/pc/profile1"), request.getContextPath() + PATH);
        return OAuth.addParameters(base.toExternalForm(), "consumer", consumerName , "returnTo", getRequestPath() );
    }
	
    /**
     * アプリケーションの実行URLの編集
     * @param request
     * @return
     * @throws MalformedURLException
     */
    private  String getRequestPath() throws MalformedURLException {

    	URL url = new URL(OAuthServlet.getRequestURL(request));
    	StringBuilder path = new StringBuilder(url.getPath());
    	String queryString = url.getQuery();
    	if (queryString != null) {
    		path.append("?").append(queryString);
    	}
    	// ★ コンテキストパスが正しくとれていないので(/frontier/frontierになってしまう・・)　
    	// 　 力ずくで　/frontier を取り除いている。。。
    	return (path.toString()).replaceAll("/frontire", "");
    }


    /**
     * Consumer情報をDBから取得。
     * @param consumerId
     * @return
     */
    public List<BeanMap> getConsumerInfo(String consumerId){
    	List<BeanMap> results = null;
    	//
    	
    	//jdbcManager.from(OauthTokensStore.class)
		//	.where("mid = ?","123" )
		//	.getResultList();
    	
		Map<String,Object> params = new HashMap<String,Object>();
		
		//パラメータ設定
		params.put("consumerid", consumerId);
		
		//SQL実行
		results =  jdbcManager
					.selectBySqlFile(BeanMap.class, "data/selOAuthConsumerInfo.sql",params)
					.getResultList();
    	return results;
    }

    
    
    /**
     * 保存されているtoken情報をDBから取得
     * @param consumerId
     * @return
     */
    public List<BeanMap> getTokens(String consumerId){
    	List<BeanMap> results = null;
    	//
		Map<String,Object> params = new HashMap<String,Object>();
		
		//パラメータ設定
		params.put("mid"       , userInfoDto.memberId);
		params.put("consumerid", consumerId);
		params.put("useflg", null);
		
		//SQL実行
		results =  jdbcManager
					.selectBySqlFile(BeanMap.class, "data/selOAuthTokens.sql",params)
					.getResultList();
    	return results;
    }
    
    /**
     * token情報をDBへ保存。
     * @param consumerId コンシューマID
     * @param u Twitterから取得したユーザ情報を含むUserクラス
     */
    public void updateTokens(String consumerId,User u){	
    	//登録済かどうか確認
    	List<BeanMap> results = getTokens(userInfoDto.memberId,consumerId,null,u.getId());
    	if(results.size() <= 0 ){
    		//
    		//未登録の場合は作成
    		insTokens(consumerId,u);
    	}
    	
    	
    	//パラメータ設定
    	Map <String,String> tokens = oauthTokensDto.oauthTokens.get(consumerId);
    	
		OauthTokensStore params = new OauthTokensStore();
		params.requestToken = tokens.get("requestToken");
		params.accessToken  = tokens.get("accessToken");
		params.tokenSecret  = tokens.get("tokenSecret");
		params.mid          = userInfoDto.memberId;
		params.consumerId   = consumerId;
		params.screenname   = u.getScreenName();
		params.twitname     = u.getName();
		params.pic          = u.getProfileImageURL().toString();
		params.twituserid   = u.getId();
		//SQL実行
		jdbcManager
		.updateBySqlFile("data/updOAuthTokens.sql",
		    	params)
		.execute();		
    }

    /**
     * OAuthトークンストアTBLの利用フラグの更新
     * @param mid (更新条件)メンバーID
     * @param twituserid (更新条件)TwitterでのユーザID
     * @param useflg (更新値)アカウントの利用FLG。1(使用中),0(未使用)
     * @return 更新した行数
     */
    public int updUseFlg(String mid,Integer twituserid,String useflg){
    	OauthTokensStore ots = new OauthTokensStore();
    	
    	//更新条件設定
    	ots.mid = mid;
    	ots.consumerId = appDefDto.ST_PROVIDER;
    	ots.twituserid = twituserid;
    	//更新項目設定
    	ots.useflg = useflg;
    	
    	//更新実行
    	return jdbcManager.updateBySqlFile("data/updUseFlg.sql",ots).execute();
    }
    
    /**
     * token情報をDBへ作成。
     * @param consumerId コンシューマID
     * @param u Twitterから取得したユーザ情報を含むUserクラス
     */   
    public void insTokens(String consumerId,User u){	

    	//パラメータ設定   	
		OauthTokensStore params = new OauthTokensStore();
		params.mid        = userInfoDto.memberId;
		params.consumerId = consumerId;
		params.twituserid = u.getId();
		params.screenname = u.getScreenName();
		params.twitname   = u.getName();
		params.pic        = u.getProfileBackgroundImageUrl();
		//SQL実行
		jdbcManager
		.updateBySqlFile("data/insOAuthTokens.sql",
		    	params)
		.execute();		
    }
        
    /**
     * token情報を削除。
     * @param twituserid 削除対象となるtwitterでのユーザID
     */   
    public void delTokensReal(Integer twituserid){	
        	//パラメータ設定   	
    		OauthTokensStore params = new OauthTokensStore();
		
			//パラメータ設定
			params.mid = userInfoDto.memberId;
			params.twituserid = twituserid;
		
    		//SQL実行
    		jdbcManager
    		.updateBySqlFile("data/delOAuthTokens2.sql",
    		    	params)
    		.execute();		
    }    
    
    public boolean isAuthorized(String provider){
    	boolean ret = false;
    	
        List<BeanMap> results = getTokens(provider);
        if(results.size() >0){
        	if(results.get(0).get("accesstoken")!=null){
        		ret = true;
        	}
        }
    	
        return ret;
    }
    
    /**
     * Twitter利用可能かどうかのチェックをする。
     * @return useTwitterFlg 0:未使用、1:Token削除、2:設定済み
     * @deprecated 削除予定
     */   
    public String checkUseTwitter(){
    	String useTwitterFlg = "0";
    	
    	//Twitterへのアカウント登録があるかをチェック
    	String twitterFlg = getTwitterFlg(userInfoDto.memberId);
    	
    	if(twitterFlg!=null && !twitterFlg.equals("") && !twitterFlg.equals("0")){
    		//Twitterへのユーザ登録済み
    		useTwitterFlg = "2";
    		
        	//利用可能なtokenが存在するかを調べる
        	if(isAuthorized(appDefDto.TWI_PROVIDER)){
        		//FrontierのDBよりtoken情報を取得
        		List<BeanMap> lbm = getTokens(appDefDto.ST_PROVIDER);
        		
        		//認証情報を取得
        		Twitter twitter = null;
        		twitter = authTwitter(getConsumerInfo(appDefDto.ST_PROVIDER),lbm);
        		
        		try {
        			//TwitterよりTLを取得する(取得できれば認証OK)
        			//デフォルトだと20件取得するので1件だけ取得にする
        			Paging paging = new Paging(1, 1);
        			twitter.getFriendsTimeline(paging);
        			User user = null;
        			//Twitterよりユーザ情報を取得する
        			user = twitter.showUser(Integer.parseInt(reTwitterUserId(lbm)));
        			//アカウント更新
        			membersService.checkTwitterAccount(userInfoDto.memberId,user.getScreenName());
        		} catch (TwitterException e) {
        			//Twitter特有のエラーの場合
        			//e.printStackTrace();
        			//List<BeanMap> lbm = getTokens(appDefDto.ST_PROVIDER);
        			
        			//tokenを未使用状態にする。
        			//2010/11/8 一旦修正
        			delTokensReal(Integer.parseInt(reTwitterUserId(lbm)));
        			
        			//F Shout投稿先をF Shoutのみに更新する
        			membersService.updTarget(userInfoDto.memberId, "0");
        			
        			useTwitterFlg = "1";
        		} catch (Exception e){
        			//Twitterとは関係のないエラーなので一旦正常
        		}
        	}else{
        		//twitterflg=1かつTwitter登録を解除した場合はここにくる
        		useTwitterFlg = "1";
        	}
    	}
    	
    	return useTwitterFlg;
    }    
    
    /**
     * Twitterクラスへ認証情報設定。
     * @return twitter 認証情報を設定したTwitterクラス
     */  
    public Twitter authTwitter(List<BeanMap> consumerList,List<BeanMap> lbm){
		
		//Twitterクラスにconsumer key/secret と AccessTokenを設定
		TwitterFactory factory = new TwitterFactory();
		Twitter twitter = factory.getInstance();
		twitter.setOAuthConsumer((String)consumerList.get(0).get("consumerkey"), (String)consumerList.get(0).get("consumersecret"));
		AccessToken accessToken = new AccessToken((String)lbm.get(0).get("accesstoken"), (String)lbm.get(0).get("tokensecret"));
		twitter.setOAuthAccessToken(accessToken);
		
		return twitter;
    }
    
    /**
     * TwitterのユーザID取得。
     * param lbm:FrontierのDBより取得したtoken情報
     * @return ユーザID
     */  
    public String reTwitterUserId(List<BeanMap> lbm){
		//AccessTokenからTwitterでのユーザIDを取得し設定する
		String at = (String)lbm.get(0).get("accesstoken");
		//AccessTokenは「123-xxx」の形で「-」の前がユーザIDなのでそこだけ切り出す。
		String[] sat = at.split("-");
		return sat[0];			
    }
    
    private String getTwitterFlg(String mid){
    	Members members = membersService.getResultById(mid);
    	String twitterFlg = members.twitterflg;
    	return twitterFlg;
    }
    
}
