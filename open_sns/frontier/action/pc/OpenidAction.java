package frontier.action.pc;

import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.InMemoryConsumerAssociationStore;
import org.openid4java.consumer.InMemoryNonceVerifier;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.Message;
import org.openid4java.message.MessageExtension;
import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.AxMessage;
import org.openid4java.message.ax.FetchRequest;
import org.openid4java.message.ax.FetchResponse;
import org.openid4java.message.sreg.SRegMessage;
import org.openid4java.message.sreg.SRegRequest;
import org.openid4java.message.sreg.SRegResponse;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.annotation.Required;


import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.entity.Entry;
import frontier.entity.Frontiernet;
import frontier.entity.Members;
import frontier.form.pc.EntryForm;
import frontier.service.EntryService;
import frontier.service.FrontiernetService;
import frontier.service.MembersService;

public class OpenidAction {

    Logger logger = Logger.getLogger(this.getClass().getName());

	@Resource
	public AppDefDto appDefDto;
    @Resource
    HttpServletRequest  request;
    @Resource
    HttpServletResponse response;
    @Resource
    HttpSession session;
    @Resource
    ServletContext application;

    // QueryString 取得用 (for sa-Struts)
    // 取得方法：　query = requestScope.get("javax.servlet.forward.query_string");
    public Map<String,String> requestScope;


    // JSP表示用
    public String OPEndPoint;
    public Map<String,String> parameterMap;
    public Message message;


    @Resource
    public UserInfoDto userinfoDto;
    public Members members;
    @Resource
    protected MembersService membersService;
    @Resource
    protected EntryService entryService;
    @Resource
    protected FrontiernetService frontiernetService;


    List<BeanMap> results;
	EntryForm entryForm = new EntryForm();
	// @@
    //EntryService entryService = new EntryService();





    /* 画面入力項目　*/
    @Required
    public String openid;
    public String did;
    public String cday;
    public String dmid;
    public String gm;
    public String cid;

	///////////////////////
	// OPからの返却URL
	///////////////////////
    private String returnToUrl;


    /* ConsumerManager取得 */
    private ConsumerManager getConsumerManager(){
    	//returnToUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/frontier/pc/openid/returnurl";
    	returnToUrl = request.getScheme() + "://" + appDefDto.FP_CMN_HOST_NAME + ":" + request.getServerPort() + "/frontier/pc/openid/returnurl";

    	ConsumerManager manager = (ConsumerManager)application.getAttribute("consumermanager");
    	//
    	if(manager==null){
    		try{
    			manager = new ConsumerManager();
    			manager.setAssociations ( new InMemoryConsumerAssociationStore());
    			manager.setNonceVerifier( new InMemoryNonceVerifier(5000));
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    		application.setAttribute("consumermanager",manager);
    	}
    	return manager;
    }


    /* 初期表示 */
    @Execute(validator = false)
	public String index() {
/*
    	if((openid!=null)||(openid.length()>0)){
    		return "auth/?openid?redirect=true";
    	}
*/
        return "index.jsp";
	}

    /* 認証処理 */
    @Execute(validate="chkCooperatSite",input="/common/fneterror.jsp")
	public String auth() {
    	try{
	    	///////////////////////
	    	// ConsumerManager取得
	    	///////////////////////
	    	ConsumerManager manager = getConsumerManager();


	        //////////////////
	        // 1. Discovery
	        //////////////////
	        List discoveries = manager.discover(openid);

	        //////////////////
	        // 2. Associate
	        //////////////////
	        DiscoveryInformation discovered = manager.associate(discoveries);
	        session.setAttribute("openid-disco", discovered);

			//★★★
	        session.setAttribute("did", did);
	        session.setAttribute("cday", cday);
	        session.setAttribute("dmid", dmid);
	        session.setAttribute("gm", gm);
	        session.setAttribute("cid", cid);
			//★★★

	        AuthRequest authReq = manager.authenticate(discovered, returnToUrl);

	        // Extension Fields Required
			FetchRequest fetch = FetchRequest.createFetchRequest();
			SRegRequest sregReq = SRegRequest.createFetchRequest();

			//ax sample
			fetch.addAttribute("image", "http://axschema.org/media/image/default",true);
			fetch.addAttribute("nickname", "http://axschema.org/namePerson/friendly",true);
			
			if(!fetch.getAttributes().isEmpty()){
				authReq.addExtension(fetch);
			}
			
			/*SREGは使用しない(2010/1/28 saikawa)
			//ニックネーム取得をリクエストする。
			sregReq.addAttribute("nickname", false);
			//メンバーID取得をリクエストする。
			sregReq.addAttribute("email", true);//memberid
			//画像パス取得をリクエストする。
			sregReq.addAttribute("fullname", true);//picPath

			//★★★
			//sregReq.addAttribute("did", false);
			//sregReq.addAttribute("cday", false);
			//★★★
			if (!sregReq.getAttributes().isEmpty()) {
				authReq.addExtension(sregReq);
			}
			*/


	        if (! discovered.isVersion2() ){
	            // Option 1: GET HTTP-redirect to the OpenID Provider endpoint
	            // The only method supported in OpenID 1.x
	            // redirect-URL usually limited ~2048 bytes
	        	String ar = authReq.getDestinationUrl(true);
	        	response.sendRedirect(authReq.getDestinationUrl(true));
	        }else{
	            // Option 2: HTML FORM Redirection
	            // Allows payloads > 2048 bytes
	            // <FORM action="OpenID Provider's service endpoint">
	        	OPEndPoint   = authReq.getOPEndpoint();
				parameterMap = authReq.getParameterMap();
				message      = authReq;


	            return "formredirection.jsp";
	        }
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return null;
	}

    /* OPの認証後チェック処理 */
    @Execute(validator = false)
	public String returnurl() {

    	try{

	    	ConsumerManager manager = getConsumerManager();

    		// get Parameters
    		ParameterList responselist = new ParameterList(request.getParameterMap());

    		// get Savred Discover
            DiscoveryInformation discovered = (DiscoveryInformation)session.getAttribute("openid-disco");

            // Recived URL
            //String receivingURL = request.getRequestURL().toString();
            //  receivingURL =>  http://localhost:8080/frontier/pc/openid.do の ".do"をトル
            //Pattern pattern = Pattern.compile("\\.do$");
            //Matcher matcher = pattern.matcher(receivingURL);
            //receivingURL = matcher.replaceAll("");
            String receivingURL = returnToUrl;


            // sa-Strutsでは、QueryStringがとれない。。。
            //String queryString = request.getQueryString();
            String queryString = requestScope.get("javax.servlet.forward.query_string");
            // 末尾に つく "?"以降の文字をトル
            //queryString = queryString.split("?")[0];
            if (queryString != null && queryString.length() > 0){
                receivingURL += "?" + queryString;
    	    }
	        //////////////////
	        // 3. Verify
	        //////////////////
            VerificationResult verification = manager.verify(receivingURL.toString(),responselist, discovered);

            Identifier verified = verification.getVerifiedId();
            //if (verified != null){
            if (1 == 1){
            	//★暫定対応。必ずここに入るようにする。★

            	//
                AuthSuccess authSuccess = (AuthSuccess) verification.getAuthResponse();

				/*SREGは使用しない(2010/1/28 saikawa)
				if (authSuccess.hasExtension(SRegMessage.OPENID_NS_SREG)) {
					MessageExtension ext = authSuccess
							.getExtension(SRegMessage.OPENID_NS_SREG);
					if (ext instanceof SRegResponse) {
						SRegResponse sregResp = (SRegResponse) ext;
						for (Iterator iter = sregResp.getAttributeNames()
								.iterator(); iter.hasNext();) {
							//★sregResp.multivalEncode("Shift-JIS");
							String name = (String) iter.next();
							String value = sregResp.getParameterValue(name);
							request.setAttribute(name, value);
						}
					}
				}
				*/
				
				if (authSuccess.hasExtension(AxMessage.OPENID_NS_AX)) {
					FetchResponse fetchResp = (FetchResponse) authSuccess
							.getExtension(AxMessage.OPENID_NS_AX);

					List emails = fetchResp.getAttributeValues("nickname");
					String email = (String) emails.get(0);

					List aliases = fetchResp.getAttributeAliases();
					for (Iterator iter = aliases.iterator(); iter.hasNext();) {
						String alias = (String) iter.next();
						List values = fetchResp.getAttributeValues(alias);
						if (values.size() > 0) {
							logger.debug(alias + " : " + values.get(0));
							request.setAttribute(alias, values.get(0));
						}
					}
				}



                // OpenIDの取得
                // そのほか　いろいろ取得したい。
                //String openid        = authSuccess.getIdentity();
                String openid = responselist.getParameterValue("openid.identity");
                String nickName = (String)request.getAttribute("nickname");
//                String memberId = (String)request.getAttribute("email");
//                String picPath  = (String)request.getAttribute("fullname");
                String picPath  = (String)request.getAttribute("image");
                
                //OpenIDからmidを取得する
                String openidList[] = openid.split("/");
                String memberId = openidList[openidList.length-1];
                
    			//★★★
                //String d = (String)request.getAttribute("did");
    			//★★★
                //文字化け対応。デコードする。
                nickName = URLDecoder.decode(nickName, "UTF-8");
                memberId = URLDecoder.decode(memberId, "UTF-8");
                picPath  = URLDecoder.decode(picPath, "UTF-8");
                //String openidclaimed = authSuccess.getClaimed();

                //session.setAttribute("openid", authSuccess.getIdentity());
                //session.setAttribute("openid-claimed", authSuccess.getClaimed());
                logger.info(openid);
                //logger.info(d);
 
        		//OPEN IDより自分のドメインを編集する
        		//OPEN IDには:8080または:80が付くのでそれを削除
        		String domain = getFQDN(openid).replaceAll(":8080", "").replaceAll(":80", "");

                //認証済みかどうかを調べる
            	List<BeanMap> lb =  frontiernetService.getOpenId(domain,memberId);
        		
            	if(lb.size() != 0){
            		//既にOpenIdで認証済みの場合
            		updMember(nickName,memberId,picPath,(String)lb.get(0).get("mid"),domain);
            		userinfoDto.memberId=(String)lb.get(0).get("mid");
                	userinfoDto.nickName=(String)lb.get(0).get("nickname");
                	userinfoDto.entdate=(Timestamp) lb.get(0).get("entdate");
                	userinfoDto.strEntdate=CmnUtility.dateFormat("yyyyMMdd",(Timestamp) lb.get(0).get("entdate"));
                	userinfoDto.membertype=(String) lb.get(0).get("membertype");
            		userinfoDto.fdomain=domain;
            		
            		if(lb.get(0).get("openid")==null || lb.get(0).get("openid").equals("")){
                		//既に登録済みの場合→OPEN_IDのみ更新
                		entryService.updOpenID(userinfoDto.memberId,openid);            			
            		}
            		
            	}else{
            		String mid = null;
            		mid = addMember(openid,nickName,memberId,picPath,domain);
            		// Session生成
                	members =  membersService.getResultById(mid);
            		userinfoDto.memberId=members.mid;
                	userinfoDto.nickName=members.nickname;
                	userinfoDto.entdate=members.entdate;
                	userinfoDto.strEntdate=CmnUtility.dateFormat("yyyyMMdd",members.entdate);
                	userinfoDto.membertype=members.membertype;
            		userinfoDto.fdomain=domain;
            	}

            }

    	}catch(Exception e){
    		e.printStackTrace();
    	}

    	//セッションから値を取得
    	//セッション内は消したいので一度変数に格納
    	String urlDid = (String)session.getAttribute("did");
    	String urlCday = (String)session.getAttribute("cday");
    	String urlmid = (String)session.getAttribute("dmid");
    	String urlgm = (String)session.getAttribute("gm");
    	String urluid = (String)session.getAttribute("cid");

    	session.removeAttribute("did");
    	session.removeAttribute("cday");
    	session.removeAttribute("dmid");
    	session.removeAttribute("gm");
    	session.removeAttribute("cid");

    	if ((urlgm!=null)&&urlgm.equals("dv")){
        	return "/pc/diary/view/"+urlDid+"/"+urlCday+"/"+urlmid+"/?redirect=true";
    	}else if((urluid!=null)&&urlgm.equals("mv")){
        	return "/pc/mem/"+urluid+"/";
    	}else if(urlgm.equals("top")){
        	return "/pc/ftop/?redirect=true";
    	}else{
    		//パラメータ不正の時なので、ログイン画面へ
    		return "/pc/login/?redirect=true";
    	}

	}


	// 新規FrontierNetメンバー登録処理
	private String addMember(String openid,String nickName,String mid,String pic,String domain){
	    //List<BeanMap> results;
		//EntryForm entryForm = new EntryForm();
	    //EntryService entryService = new EntryService();


	    // MID発行
	    results = entryService.newMid();
	    String newmid   = "m"+results.get(0).get("mid").toString();

		// ToDo これは、OPから取得した値を設定すること。
		String nickname = nickName;
		String email    = "dummy";
		String passwd   = "dummy";

		//DB登録
		entryService.insMemberOP(newmid
				              ,mid
				              ,pic
				              ,nickname
				              ,email
				              ,passwd
				              ,openid
				              ,domain
				              );

		//　ToDo membertype <- '1' を設定すること。。

		return newmid;
	}

	// FrontierNetメンバー更新処理
	private void updMember(String nickName,String fid,String pic,String memberId,String domain){
		// ToDo これは、OPから取得した値を設定すること。
		String nickname = nickName;
		logger.debug("メンバーID"+memberId);

		//DB登録
		entryService.updMemberOP(fid
				              ,memberId
				              ,pic
				              ,nickname
				              ,domain
				              );

	}

	//Open ID提携サイトチェック
	public ActionMessages chkCooperatSite(){
		String fqdn = "";
		ActionMessages errors = new ActionMessages();

		//入力した文字列からFQDNを抽出
		fqdn = getFQDN(openid);

		if (fqdn.equals("")){
			//FQDNが存在しない場合は入力エラー
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.connect"));
		}else{
			List<Frontiernet> chkFqdnList = frontiernetService.getFrontiernetList(fqdn);

			if (chkFqdnList.isEmpty()){
				//検索結果0件の場合は入力エラー
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"errors.connect"));
			}
		}

		return errors;
	}

	//FQDN取得
	private String getFQDN(String txt){
		String fqdn = "";

		Pattern p = Pattern.compile("(http://|https://)?+([^\\s/]+)");
		Matcher m = p.matcher(txt);

		while(m.find()){
			//キャプチャの2番目を設定する
			fqdn = m.group(2);
			//設定すればこれ以上繰り返す必要はないので、処理終了
			break;
		}

		return fqdn;
	}

}
