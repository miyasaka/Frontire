package frontier.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONException;
import net.oauth.client.OAuthResponseMessage;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.BeanMap;

import frontier.common.DecorationUtility;
import frontier.common.TwitterUtility;
import frontier.dto.AppDefDto;
import frontier.entity.OauthTokensStore;
import frontier.entity.TwitterDb;
import frontier.entity.TwitterPostManagement;
import twitter4j.PagableResponseList;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Relationship;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;
import twitter4j.http.AccessToken;
import twitter4j.http.OAuthAuthorization;

/**
 * Twitter関連のDB処理を行うクラス。
 * 
 * @author H.Saikawa
 * @version 1.0
 */
public class TwitterService{
	Logger logger = Logger.getLogger(this.getClass().getName());

	@Resource
	public AppDefDto appDefDto;
	@Resource
	protected OauthConsumerService oauthConsumerService;
	@Resource
	public MembersService membersService;
	//twitter
	private Twitter twitter;

	protected ResponseList<Status> twresults;

	@Resource
	protected JdbcManager jdbcManager;

    /**
     * Twitter利用可能かどうかのチェックをする。
     * @param mid メンバーID
     * @return useTwitterFlg 0:未使用、1:Token削除、2:設定済み、3:Twitterでのエラー(通信障害、API切れなど)
     */   
    public String checkUseTwitter(String mid){
    	String useTwitterFlg = "0";
    	
    	//現在使用中のTwitterアカウントがあるかをチェック
    	List<BeanMap> lbmAccount = oauthConsumerService.getTokens(mid,appDefDto.TWI_PROVIDER,"1",null);
    	
    	if(lbmAccount.size()!=0){
    		//Twitterへのユーザ登録済み
    		useTwitterFlg = "2";
    		
        	//認証情報を取得
        	Twitter twitter = null;
        	twitter = setTwitter(mid, lbmAccount.get(0).get("accesstoken").toString(), lbmAccount.get(0).get("tokensecret").toString(),null);
        		
        	TwitterUtility tu = new TwitterUtility();
        		
        	try {
        		//TwitterよりTLを取得する(取得できれば認証OK)
        		//デフォルトだと20件取得するので1件だけ取得にする
       			tu.getHomeTimeLine(twitter, 1, 1);
       			User user = null;
       			//Twitterよりユーザ情報を取得する
       			user = tu.getUserInfo(twitter,Integer.parseInt(lbmAccount.get(0).get("twituserid").toString()));
       			//アカウント更新
       			oauthConsumerService.checkTwitterAccount(mid,user);
       		} catch (TwitterException e) {
       			useTwitterFlg = "3";
        			
       			String errCode = tu.getErrorTwitter(e);
        			
       			if(errCode == null || errCode.equals("")){
       				//認証エラー
           			//tokenを未使用状態にする。
           			oauthConsumerService.delTokensReal(Integer.parseInt(lbmAccount.get(0).get("twituserid").toString()));
            			
           			//F Shout投稿先をF Shoutのみに更新する
           			membersService.updTarget(mid, "0");
            			
           			useTwitterFlg = "1";
        				
       			}
        			
       		} catch (Exception e){
       			//Twitterとは関係のないエラーなので一旦正常
       		}
    	}
    	
    	return useTwitterFlg;
    }	
	
    /**
     * 文中のFrontierアカウント -> Twitterアカウントへ変換
     * @param getText 変換対象を含む文字列
     * @return Frontierアカウント -> Twitterアカウントへ置換した文字列
     */
	public String chgFidtoTid(String getText){
		// 正規表現の適合調査結果の文字列格納用
		StringBuffer sb = new StringBuffer();

		// 型の定義(正規表現) [@:(Frontierドメイン),(メンバーID)]
		Pattern ptn = Pattern.compile("\\[@:(\\S+)\\,(\\S+)\\]");
		// マッチングクラス生成
		Matcher mt = ptn.matcher(getText);
		// 検索
		while(mt.find()){
			// Twitterアカウント名変数
			String getTwitScreenName = "";
			// マッチした型のFrontierドメイン部分(xxxxxxx.ne.jp部分)
			String mtFDomain = mt.group(1);
			// マッチした型のメンバーID部分(mXXXXXXXXXの'm'なし0トルツメ)
			String mtFid = mt.group(2);
			// mXXXXXXXXX型に変換
			mtFid = new DecorationUtility().stringFormat("m000000000",Integer.valueOf(mtFid).intValue());
			// Twitterアカウント名取得
			getTwitScreenName = oauthConsumerService.editMid(mtFDomain,mtFid);
			if(getTwitScreenName == null){
				// 取得できなければブランクを設定
				getTwitScreenName = "";
			} else {
				// 取得出来れば「@」を付与
				getTwitScreenName = "@"+getTwitScreenName;
			}
			// Twitterアカウントの置換を行う
			mt.appendReplacement(sb, getTwitScreenName);
		}
		// 正規表現の適合調査を行っていない残りの文字シーケンスを引数に指定した文字列バッファに追加
		// ※appendReplacementメソッドと一緒に利用
		mt.appendTail(sb);
		return sb.toString();
	}
    
	/**
	 * 入力した検索条件に該当するツイートの件数を取得
	 * @param mid FrontierのメンバーID
	 * @param searchWord 検索条件(フリーワード)
	 * @param fromYear 年(from)
	 * @param fromMonth 月(from)
	 * @param fromDay 日(from)
	 * @param toYear 年(to)
	 * @param toMonth 月(to)
	 * @param toDay 日(to)
	 * @param userIdList TwitterのユーザIDを含んだリスト
	 * @return 総件数
	 */
	public long cntSearchTwitter(String mid,String searchWord,String fromYear,String fromMonth,String fromDay,String toYear,String toMonth,String toDay,List<String> userIdList){
		Map<String,Object> params = new HashMap<String,Object>();
		params = setSearchConditionTweet(mid,searchWord,fromYear,fromMonth,fromDay,toYear,toMonth,toDay,userIdList);
		
		return jdbcManager.getCountBySqlFile("data/twitter/selSearchTwitter.sql",params);
	}
	
	/**
     * twitter_dbテーブルより発言が登録済かどうかを確認する。
     * @param statusid　検索キーとなる発言毎のステータスID
     * @return 取得できた件数
     */ 	
	public long cntTwitter(long statusid){
		return jdbcManager.from(TwitterDb.class)
							.where("statusid = ?",statusid)
							.getCount();
	}

	/**
     * twitter_post_managementテーブルより条件に一致する件数を取得する。(汎用版)
     * @param statusid　(検索キー)発言毎のステータスID
     * @param twituserid　(検索キー)twitterのユーザIDを指定。<br>　使用しない場合はnullを渡す。<br>　但し、IDは各Frontierで固有。別のIDがDB登録されていることはない。
     * @param readflg (検索キー)既読未読FLGを指定。0：未読、1：既読<br>　使用しない場合はnullを渡す。この場合は全件取得となる。
     * @param favoritflg (検索キー)お気に入りFLGを指定。0：通常、1：お気に入り<br>　使用しない場合はnullを渡す。この場合は全件取得となる。
     * @param postflg　(検索キー)バッチ投稿フラグを指定。0：未取得、1：取得済<br>　使用しない場合はnullを渡す。
     * @param mentionflg　(検索キー)バッチmentionフラグを指定。0：未取得、1：取得済<br>　使用しない場合はnullを渡す。
     * @param batchfavoriteflg　(検索キー)バッチお気に入りフラグを指定。0：未取得、1：取得済<br>　使用しない場合はnullを渡す。
     * @return 検索条件に一致する発言の件数
     */ 	
	public long cntTwitterPostManagement(Object statusid,Integer twituserid,String readflg,String favoriteflg,String postflg,String mentionflg,String batchfavoriteflg){	
		return jdbcManager.from(TwitterPostManagement.class)
							.where(new SimpleWhere()
									.eq("statusid", statusid)
									.eq("twituserid", twituserid)
									.eq("readflg",readflg)
									.eq("favoriteflg",favoriteflg)
									.eq("postflg",postflg)
									.eq("mentionflg",mentionflg)
									.eq("batchfavoriteflg",batchfavoriteflg))
							.getCount();
	}
	
	/**
	 * F Shoutと同時投稿した場合のTwitter側の削除処理
	 * @param statusid 削除対象のステータスID
	 * @param mid 処理を実行するメンバーID
	 * @throws TwitterException
	 */
	public void delTwitterOnFshout(long statusid,String mid) throws TwitterException{
		//FrontierのDBからTwitterの投稿を削除
		updTwitter(statusid, mid);
				
		//OAuth認証情報を取得
		Twitter twitter = setTwitter(mid, null, null, null);

		TwitterUtility tu = new TwitterUtility();

		//Twitterの投稿を削除
		tu.deleteStatus(twitter, statusid);
	}
	
	/**
	 * Ludiaで利用可能なように検索文字列を整形する
	 * @param word 入力した検索文字列
	 * @param searchPattern 選択した検索パターン(AND検索またはOR検索)
	 * @return 整形後の文字列
	 */
	public String fixLudiaWord(String word,String searchPattern){
		String fixWord = "";
		DecorationUtility du = new DecorationUtility();
		
		//Ludiaで利用出来るように整形
		fixWord = du.fixLudiaWord(word);
		
		//AND検索用に整形
		if(searchPattern.equals("0")){
			fixWord = du.fixLudiaAndWord(fixWord);
		}
		
		return fixWord;
	}
	
	/**
	 * 与えられたパラメータから日付を生成する
	 * @param year 年
	 * @param month 月
	 * @param day 日
	 * @param revisionMonth 補正月(fromの場合は01,toの場合は12)
	 * @param revisionDay 補正日(fromの場合は01,toの場合は31)
	 * @return yyyymmddの形式の文字列
	 */
	private String makeSearchYmd(String year,String month,String day,String revisionMonth,String revisionDay){
		String ymd = "";
		
		if(!year.equals("0")){
			//入力した年を設定
			ymd = year;
			
			//月の設定
			if(!month.equals("0")){
				//入力した月を設定
				ymd = ymd + month;
			}else{
				//月が未入力の場合は補正月を設定
				ymd = ymd + revisionMonth;
			}
			
			//日の設定
			if(!day.equals("0")){
				//入力した月を設定
				ymd = ymd + day;
			}else{
				//月が未入力の場合は補正月を設定
				ymd = ymd + revisionDay;
			}
		}
		
		return ymd;
	}
	
	/**
	 * twitter_post_managementから指定したユーザの最小のステータスIDを取得する
	 * @param twituserid (検索キー)twitterのユーザIDを指定。
	 * @return ステータスIDの最小値。検索条件に該当するデータがない場合は-1を返す。
	 */
	public long selMinStatusId(Integer twituserid){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("twituserid", twituserid);
		
		//twitter_post_managementから指定したユーザの最小のステータスIDを取得する
		List<BeanMap> lb = jdbcManager.selectBySqlFile(BeanMap.class,"data/twitter/selMinStatusid.sql",params)
		.getResultList();
		
		return (Long)lb.get(0).get("statudid");
			
	}
	
	/**
	 * 入力した条件に該当するツイートを検索する
	 * @param mid FrontierのメンバーID
	 * @param searchWord 検索条件(フリーワード)
	 * @param fromYear 年(from)
	 * @param fromMonth 月(from)
	 * @param fromDay 日(from)
	 * @param toYear 年(to)
	 * @param toMonth 月(to)
	 * @param toDay 日(to)
	 * @param userIdList TwitterのユーザIDを含んだリスト
	 * @param limit 最大取得件数
	 * @param offset 最初に取得する行の位置
	 * @return 検索結果
	 */
	public List<BeanMap> selSearchTwitter(String mid,String searchWord,String fromYear,String fromMonth,String fromDay,String toYear,String toMonth,String toDay,List<String> userIdList,Integer limit,Integer offset){
		Map<String,Object> params = new HashMap<String,Object>();
		params = setSearchConditionTweet(mid,searchWord,fromYear,fromMonth,fromDay,toYear,toMonth,toDay,userIdList);
		
		return jdbcManager.selectBySqlFile(BeanMap.class,"data/twitter/selSearchTwitter.sql",params).limit(limit).offset(offset).getResultList();
	}
	
	/**
     * twitter_dbテーブルより発言一覧を取得する。
     * @param statusid　検索キーとなる発言毎のステータスID
     * @param limit 一度に取得する最大件数。
     * @param offset 最初に取得する行の位置
     * @return 検索条件に一致する発言一覧
     */ 	
	public List<TwitterDb> selTwitterDb(long statusid,Integer limit,Integer offset){
		return jdbcManager.from(TwitterDb.class)
							.where("statusid = ?",statusid)
							.orderBy("statusid desc")
							.limit(limit)
							.offset(offset)
							.getResultList();
	}
	
	/**
     * twitter_dbテーブルより発言一覧を取得する(汎用版)。
     * @param statusid　(検索キー)発言毎のステータスID
     * @param twituserid　(検索キー)twitterのユーザIDを指定。<br>使用しない場合はnullを渡す。<br>但し、IDは各Frontierで固有。別のIDがDB登録されていることはない。
     * @param screenname (検索キー)twitterでのアカウント名<br>使用しない場合はnullを渡す。この場合は全件取得となる。
     * @param replytwitstatusid　(検索キー)reply元のステータスID<br>使用しない場合はnullを渡す。
     * @param replytwituserid　(検索キー)reply元のユーザID<br>使用しない場合はnullを渡す。
     * @param delflg (検索キー)削除FLGを指定。0：削除済、1：有効<br>使用しない場合はnullを渡す。この場合は全件取得となる。
     * @param sort　(ソートキー)asc又はdescを指定
     * @param limit 一度に取得する最大件数。
     * @param offset 最初に取得する行の位置
     * @return 検索条件に一致する発言一覧
     */ 	
	public List<TwitterDb> selTwitterDbGeneral(Object statusid,Integer twituserid,String screenname,Object replytwitstatusid,Integer replytwituserid,String delflg,String sort,Integer limit,Integer offset){
		return jdbcManager.from(TwitterDb.class)
							.where(new SimpleWhere()
										.eq("statusid", statusid)
										.eq("twituserid", twituserid)
										.eq("screenname", screenname)
										.eq("replytwitstatusid", replytwitstatusid)
										.eq("replytwituserid", replytwituserid)
										.eq("delflg", delflg)
										)
							.orderBy("statusid "+sort)
							.limit(limit)
							.offset(offset)
							.getResultList();
	}
	
	/**
     * twitter_post_managementテーブルより条件に一致する一覧を取得する。
     * @param statusid　(検索キー)発言毎のステータスID
     * @param twituserid　(検索キー)twitterのユーザIDを指定。<br>使用しない場合はnullを渡す。<br>但し、IDは各Frontierで固有。別のIDがDB登録されていることはない。
     * @param readflg (検索キー)既読未読FLGを指定。0：未読、1：既読<br>使用しない場合はnullを渡す。この場合は全件取得となる。
     * @param postflg　(検索キー)バッチ投稿フラグを指定。0：未取得、1：取得済<br>使用しない場合はnullを渡す。
     * @param mentionflg　(検索キー)バッチmentionフラグを指定。0：未取得、1：取得済<br>使用しない場合はnullを渡す。
     * @param batchfavoriteflg　(検索キー)バッチお気に入りフラグを指定。0：未取得、1：取得済<br>使用しない場合はnullを渡す。
     * @param limit 一度に取得する最大件数。
     * @param offset 最初に取得する行の位置
     * @return 検索条件に一致する発言一覧
     */ 	
	public List<TwitterPostManagement> selTwitterPostManagement(Object statusid,Integer twituserid,String readflg,String postflg,String mentionflg,String batchfavoriteflg,Integer limit,Integer offset){
		return jdbcManager.from(TwitterPostManagement.class)
							.where(new SimpleWhere()
									.eq("statusid", statusid)
									.eq("twituserid", twituserid)
									.eq("readflg",readflg)
									.eq("postflg",postflg)
									.eq("mentionflg",mentionflg)
									.eq("batchfavoriteflg",batchfavoriteflg))
							.orderBy("statusid desc,twituserid asc")
							.limit(limit)
							.offset(offset)
							.getResultList();
	}
	

	/**
	 * 検索条件をMapに設定
	 * @param mid FrontierのメンバーID
	 * @param searchWord 検索条件(フリーワード)
	 * @param fromYear 年(from)
	 * @param fromMonth 月(from)
	 * @param fromDay 日(from)
	 * @param toYear 年(to)
	 * @param toMonth 月(to)
	 * @param toDay 日(to)
	 * @param userIdList TwitterのユーザIDを含んだリスト
	 * @return
	 */
	private Map<String,Object> setSearchConditionTweet(String mid,String searchWord,String fromYear,String fromMonth,String fromDay,String toYear,String toMonth,String toDay,List<String> userIdList){
		Map<String,Object> params = new HashMap<String,Object>();
		if(!searchWord.equals("")){
			params.put("searchword", "*E"+appDefDto.FP_CMN_ALL_SEACH_CHANGE_NUM+",1 " + searchWord);	
		}else{
			params.put("searchword",searchWord);
		}

		//Fromの設定
		String searchFromYmd = makeSearchYmd(fromYear,fromMonth,fromDay,"01","01");
		params.put("searchFronYmd", searchFromYmd);			

		//Toの設定
		String searchToYmd = makeSearchYmd(toYear,toMonth,toDay,"12","31");
		params.put("searchToYmd", searchToYmd);			
		
		//ユーザIDの設定
		if(userIdList != null && userIdList.size()!=0){
			//ユーザIDのチェックボックス選択あり
			params.put("searchUserId", userIdList);
		}else{
			//ユーザID未選択の場合
			params.put("searchUserId", "");
			//自分が使用している全ユーザが対象(SQLで検索するときにmidが必要となる。)
			params.put("mid", mid);
		}
		
		return params;
	}
	
	/**
     * twitter_dbテーブルに発言を登録する。
     * @param st　twitterの発言が格納されたStatusクラス
     * @param mid 操作実行者のメンバーID
     * @return 登録した件数
     */ 	
	public int insTwitter(Status st,String mid){
		TwitterDb twitterdb = new TwitterDb();
		DecorationUtility du = new DecorationUtility();
		
		//登録内容をエンティティへ設定
		twitterdb.statusid = st.getId();
		twitterdb.comment = du.replaceCharacter(st.getText());
		twitterdb.twituserid = st.getUser().getId();
		twitterdb.screenname = st.getUser().getScreenName();
		twitterdb.twitname = st.getUser().getName();
		twitterdb.pic = st.getUser().getProfileImageURL().toString();
		
		//Replyが存在しない場合は-1を返すのでその場合は登録しない
		long rtsId = st.getInReplyToStatusId();
		if(rtsId != -1){
			twitterdb.replytwitstatusid = rtsId;
		}
		
		//Replyが存在しない場合は-1を返すのでその場合は登録しない
		int rtuId = st.getInReplyToUserId();
		if(rtsId != -1){		
			twitterdb.replytwituserid = rtuId;
		}
		twitterdb.contributetime = new Timestamp ((st.getCreatedAt()).getTime());
		twitterdb.delflg = "0";
		twitterdb.upddate = new Timestamp ((new java.util.Date()).getTime());
		twitterdb.entid = mid;
		twitterdb.updid = mid;
		twitterdb.entdate = new Timestamp ((new java.util.Date()).getTime());		
		
		//登録
		return jdbcManager.insert(twitterdb).execute();
	}

	/**
     * twitter_dbテーブルに発言を登録する。
     * twitter4jのライブラリが用意されていない場合に使用する。
     * @param st　twitterの発言が格納されたMap
     * @param mid 操作実行者のメンバーID
     * @return 登録した件数
     */ 	
	public int insTwitter(Map<String,String> st,String mid){
		TwitterDb twitterdb = new TwitterDb();
		DecorationUtility du = new DecorationUtility();

		//登録内容をエンティティへ設定
		Object objId = st.get("id");
		twitterdb.statusid = Long.parseLong(objId.toString());
		twitterdb.comment = du.replaceCharacter(st.get("text"));
		
		//User情報を取得
		Object objUser = st.get("user");
		Map<String,String> mss = (Map<String, String>) objUser;
		Object objUserid = mss.get("id");
		twitterdb.twituserid = Integer.parseInt(objUserid.toString());
		twitterdb.screenname = mss.get("screen_name");
		twitterdb.twitname = mss.get("name");
		twitterdb.pic = mss.get("profile_image_url");
		
		//Replyが存在しない場合は登録しない
		Object rtsId = st.get("in_reply_to_status_id");
		if(rtsId != null && !rtsId.equals("")){
			twitterdb.replytwitstatusid = Long.parseLong(rtsId.toString());
		}
		
		//Replyが存在しない場合は登録しない
		Object rtuId = st.get("in_reply_to_user_id");
		if(rtuId != null && !rtuId.equals("")){		
			twitterdb.replytwituserid = Integer.parseInt(rtuId.toString());
		}
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);

		try {
			twitterdb.contributetime = new Timestamp (sdf.parse(st.get("created_at")).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		twitterdb.delflg = "0";
		twitterdb.upddate = new Timestamp ((new java.util.Date()).getTime());
		twitterdb.entid = mid;
		twitterdb.updid = mid;
		twitterdb.entdate = new Timestamp ((new java.util.Date()).getTime());		
		
		//登録
		return jdbcManager.insert(twitterdb).execute();
	}
	
	
	/**
     * twitter_post_managementテーブルより発言が登録済かどうかを確認する。
     * @param statusid　検索キーとなる発言毎のステータスID
     * @param userid　検索キーとなる操作実行者のtwitterでのユーザID<br>
     * 　　　　　　　　　　　　バッチの場合は現在処理対象となっているユーザID
     * @return 取得できた件数
     */ 
	public long cntTwitterPostManagement(long statusid,int userid){
		return jdbcManager.from(TwitterPostManagement.class)
							.where("statusid = ? and twituserid = ?",statusid,userid)
							.getCount();
	}

	/**
     * twitter_post_managementテーブルに発言を登録する。
     * @param statusid　発言毎のステータスID
     * @param userid　操作実行者のtwitterでのユーザID。バッチの場合は現在処理対象となっているユーザID
     * @param readflg 既読：１、未読：０を設定。画面系は常に既読。バッチは常に未読。
     * @param　favoriteflg お気に入り：１、その他：０を設定。お気に入り設定時以外は常に０。
     * @param postflg 自分宛の発言をバッチ取得：１、バッチ未取得：０。画面系は常に０。
     * @param mentionflg メンションをバッチ取得：１、バッチ未取得：０。画面系は常に０。　　　　　　　　　　　 
     * @param batchfavoriteflg お気に入りをバッチ取得：１、バッチ未取得：０。画面系は常に０。　　　　　　　　　　　 
     * @param mid 操作実行者のメンバーID
	 * @param realmentionflg 発言がメンションの場合：１、それ以外：０を設定
	 * @return 登録した件数
	 */
	public int insTwitterPostManagement(long statusid,int userid,String readflg,String favoriteflg,String postflg,String mentionflg,String batchfavoriteflg,String mid,String realmentionflg){
		TwitterPostManagement tpm = new TwitterPostManagement();
		//登録内容をエンティティへ設定
		tpm.statusid = statusid;
		tpm.twituserid = userid;
		tpm.readflg = readflg;
		tpm.favoriteflg = favoriteflg;
		tpm.postflg = postflg;
		tpm.mentionflg = mentionflg;
		tpm.batchfavoriteflg = batchfavoriteflg;
		tpm.upddate = new Timestamp ((new java.util.Date()).getTime());
		tpm.entid = mid;
		tpm.updid = mid;
		tpm.entdate = new Timestamp ((new java.util.Date()).getTime());
		tpm.realmentionflg = realmentionflg;
		
		//登録
		return jdbcManager.insert(tpm).execute();
	}
	
	/**
     * twitter_post_managementテーブルに発言を登録する。
     * @deprecated 2010/12/01 メンションの管理方法変更により削除
     * @param statusid　発言毎のステータスID
     * @param userid　操作実行者のtwitterでのユーザID<br>バッチの場合は現在処理対象となっているユーザID
     * @param readflg 既読：１、未読：０を設定<br>画面系は常に既読。バッチは常に未読。
     * @param　favoriteflg お気に入り：１、その他：０を設定<br>お気に入り設定時以外は常に０。
     * @param  postflg 自分宛の発言をバッチ取得：１、バッチ未取得：０<br>画面系は常に０。
     * @param  mentionflg メンションをバッチ取得：１、バッチ未取得：０<br>画面系は常に０。　　　　　　　　　　　 
     * @param  batchfavoriteflg お気に入りをバッチ取得：１、バッチ未取得：０<br>画面系は常に０。　　　　　　　　　　　 
     * @param mid 操作実行者のメンバーID
     * @return 登録した件数
     */ 	
	public int insTwitterPostManagement(long statusid,int userid,String readflg,String favoriteflg,String postflg,String mentionflg,String batchfavoriteflg,String mid){
		TwitterPostManagement tpm = new TwitterPostManagement();
		//登録内容をエンティティへ設定
		tpm.statusid = statusid;
		tpm.twituserid = userid;
		tpm.readflg = readflg;
		tpm.favoriteflg = favoriteflg;
		tpm.postflg = postflg;
		tpm.mentionflg = mentionflg;
		tpm.batchfavoriteflg = batchfavoriteflg;
		tpm.upddate = new Timestamp ((new java.util.Date()).getTime());
		tpm.entid = mid;
		tpm.updid = mid;
		tpm.entdate = new Timestamp ((new java.util.Date()).getTime());		
		
		//登録
		return jdbcManager.insert(tpm).execute();
	}	

	/**
     * twitter_db及びtwitter_post_managementテーブルから発言一覧を取得する。
     * @param statusid　(検索キー)指定したID以下の発言毎のステータスIDを取得する。<br>使用しない場合はnullを渡す。
     * @param userid　(検索キー)取得したい発言を行ったユーザのtwitterでのユーザID
     * @param limit 一度に取得する最大件数。
     * @return 発言一覧
     */ 	
	public List<BeanMap> selTwitterList(Object statusid,int userid,Integer limit){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("twituserid", userid);
		params.put("statusid", statusid);
		params.put("statusidfrom", null);
		params.put("limit", limit);
		
		return jdbcManager.selectBySqlFile(BeanMap.class,"data/twitter/selTwitterList.sql",params)
		.getResultList();
	}
	
	/**
     * twitter_db及びtwitter_post_managementテーブルから発言一覧を取得する。
     * ステータスIDのFromからToで検索を実行可能
     * @param statusid　(検索キー)指定したID以下の発言毎のステータスIDを取得する。<br>使用しない場合はnullを渡す。
     * @param statusidto (検索キー)ここで指定したステータスIDより大きい発言を検索する。
     * @param userid　(検索キー)取得したい発言を行ったユーザのtwitterでのユーザID
     * @param limit 一度に取得する最大件数。
     * @return 発言一覧
     */ 	
	public List<BeanMap> selTwitterList(Object statusid,Object statusidfrom,int userid,Integer limit){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("twituserid", userid);
		params.put("statusid", statusid);
		params.put("statusidfrom", statusidfrom);
		params.put("limit", limit);
		
		return jdbcManager.selectBySqlFile(BeanMap.class,"data/twitter/selTwitterList.sql",params)
		.getResultList();
	}
	
	/**
     * twitter_dbテーブルを更新する。
     * 削除フラグ以外は更新することはないので実質は削除の場合のみ実行される
     * @param statusId   削除対象となるステータスID。
     * @param mid 操作実行者のメンバーID
     * @return 更新した件数
     */ 	
	public int updTwitter(long statusid,String mid){
		TwitterDb twitterdb = new TwitterDb();
		//更新内容をエンティティへ設定
		twitterdb.statusid = statusid;
		twitterdb.delflg = "1";
		twitterdb.upddate = new Timestamp ((new java.util.Date()).getTime());
		twitterdb.updid = mid;
		
		//更新
		return jdbcManager.update(twitterdb).includes("delflg","upddate","updid").execute();
	}

	/**
     * OauthTokensStoreテーブルからtwitterユーザ一覧を取得する。
     * @param mid　(検索キー)検索したいユーザのメンバーIDを指定。<br>使用しない場合はnullを渡す。
     * @param consumerId　(検索キー)コンシューマIDを指定。<br>使用しない場合はnullを渡す。<br>但し、IDは各Frontierで固有。別のIDがDB登録されていることはない。
     * @param delflg (検索キー)削除FLGを指定。0：有効、1：削除済<br>使用しない場合はnullを渡す。この場合は全件取得となる。
     * @param twituserid　(検索キー)検索したいtwitterでのユーザIDを指定。<br>使用しない場合はnullを渡す。
     * @param screenname　(検索キー)検索したいtwitterでのユーザ名を指定。<br>使用しない場合はnullを渡す。
     * @param useflg (検索キー)1:アカウント使用中、0:アカウント未使用。<br>使用しない場合はnullを渡す。
     * @return twitterユーザ一覧
     */ 	
	public List<OauthTokensStore> selAllTwitterUser(String mid,String consumerId,String delflg,String twituserid,String screenname,String useflg){
		return jdbcManager.from(OauthTokensStore.class).where(
														new SimpleWhere()
															.eq("mid", mid)
															.eq("consumerId",consumerId)
															.eq("delflg", delflg)
															.eq("twituserid", twituserid)
															.eq("screenname", screenname)
															.eq("useflg", useflg))
														.getResultList();
	}
	
	/**
     * twitter_dbテーブルを更新する。
     * @param statusid　発言毎のステータスID
     * @param twituserid　操作実行者のtwitterでのユーザID。バッチの場合は現在処理対象となっているユーザID
     * @param readflg 既読：１、未読：０を設定。画面系は常に既読にのみ更新。バッチでは更新しない。
     * @param　favoriteflg お気に入り：１、その他：０を設定。バッチでは更新しない。
     * @param  postflg 自分宛の発言をバッチ取得：１、バッチ未取得：０。画面系では更新しない。バッチでは常に１で更新。
     * @param  mentionflg メンションをバッチ取得：１、バッチ未取得：０。画面系では更新しない。バッチでは常に１で更新。　　　　　　　　　　　 
     * @param batchfavoriteflg お気に入りをバッチ取得：１、バッチ未取得：０。画面系では更新しない。バッチでは常に１で更新。　　　　　　　　　　　 
     * @param mid 操作実行者のメンバーID
     * @param realmentionflg メンションFLG。メンションの場合は１を設定。それ以外は０を設定。
     * @param limit 未使用。
     * @param offset 未使用
	 * @return
	 */
	public int updTwitterPostManagement(long statusid,Integer twituserid,String readflg,String favoriteflg,String postflg,String mentionflg,String batchfavoriteflg,String mid,String realmentionflg,int limit,int offset){
		//変更前の状態を取得
		//List<TwitterPostManagement> ltpm = selTwitterPostManagement(statusid,twituserid,readflg,postflg,mentionflg,batchfavoriteflg,limit,offset);
		
		TwitterPostManagement oldtpm = new TwitterPostManagement();
		//oldtpm = ltpm.get(0);
		
		TwitterPostManagement tpm = new TwitterPostManagement();
		//更新内容をエンティティへ設定
		tpm.statusid = statusid;
		tpm.twituserid = twituserid;
		tpm.readflg = readflg;
		tpm.favoriteflg = favoriteflg;
		tpm.postflg = postflg;
		tpm.mentionflg = mentionflg;
		tpm.batchfavoriteflg = batchfavoriteflg;
		tpm.upddate = new Timestamp ((new java.util.Date()).getTime());
		tpm.updid = mid;
		tpm.realmentionflg = realmentionflg;
		
		//更新
		return jdbcManager.update(tpm).changedFrom(oldtpm).excludesNull().execute();
	}
	
	/**
     * twitter_dbテーブルを更新する。
     * @deprecated 2010/12/01 メンションの管理方法変更に伴い削除。
     * @param statusid　発言毎のステータスID
     * @param twituserid　操作実行者のtwitterでのユーザID<br>バッチの場合は現在処理対象となっているユーザID
     * @param readflg 既読：１、未読：０を設定<br>画面系は常に既読にのみ更新。バッチでは更新しない。
     * @param　favoriteflg お気に入り：１、その他：０を設定<br>バッチでは更新しない。
     * @param  postflg 自分宛の発言をバッチ取得：１、バッチ未取得：０<br>画面系では更新しない。バッチでは常に１で更新。
     * @param  mentionflg メンションをバッチ取得：１、バッチ未取得：０<br>画面系では更新しない。バッチでは常に１で更新。　　　　　　　　　　　 
     * @param  batchfavoriteflg お気に入りをバッチ取得：１、バッチ未取得：０<br>画面系では更新しない。バッチでは常に１で更新。　　　　　　　　　　　 
     * @param mid 操作実行者のメンバーID
     * @param limit 一度に取得する最大件数。
     * @param offset 最初に取得する行の位置
     * @return 更新した件数
     */ 	
	public int updTwitterPostManagement(long statusid,Integer twituserid,String readflg,String favoriteflg,String postflg,String mentionflg,String batchfavoriteflg,String mid,int limit,int offset){
		//変更前の状態を取得
		//List<TwitterPostManagement> ltpm = selTwitterPostManagement(statusid,twituserid,readflg,postflg,mentionflg,batchfavoriteflg,limit,offset);
		
		TwitterPostManagement oldtpm = new TwitterPostManagement();
		//oldtpm = ltpm.get(0);
		
		TwitterPostManagement tpm = new TwitterPostManagement();
		//更新内容をエンティティへ設定
		tpm.statusid = statusid;
		tpm.twituserid = twituserid;
		tpm.readflg = readflg;
		tpm.favoriteflg = favoriteflg;
		tpm.postflg = postflg;
		tpm.mentionflg = mentionflg;
		tpm.batchfavoriteflg = batchfavoriteflg;
		tpm.readflg = "1";
		tpm.upddate = new Timestamp ((new java.util.Date()).getTime());
		tpm.updid = mid;
		
		//更新
		return jdbcManager.update(tpm).changedFrom(oldtpm).excludesNull().execute();
	}
	
	/**
     * twitterクラスへ認証情報を設定する。
     * @param mid　twitterへ接続を行ないたいユーザのメンバーID。画面は必須、バッチは不要。
     * @param accessToken　twitterのユーザ毎に固有のアクセストークン。
     * 　　　　　　　　　　　　　　　　tokenSecretと併せて指定をすると本メソッドでFrontierのDBより認証情報を取得する。
     * 　　　　　　　　　　　　　　　　画面は指定不要、バッチは必須。
     * @param tokenSecret twitterのユーザ毎に固有のトークンシークレット。
     * 　　　　　　　　　　　　　　　　accessTokenと併せて指定をすると本メソッドでFrontierのDBより認証情報を取得する。
     * 　　　　　　　　　　　　　　　　画面は指定不要、バッチは必須。
     * @return 認証情報を設定したtwitterクラス
     * @deprecated
     */ 
	public Twitter setTwitter(String mid,String accessToken,String tokenSecret){
		//初期化
		List<OauthTokensStore> lots = new ArrayList<OauthTokensStore>();
		String token = accessToken;
		String secret = tokenSecret;
		
		if(accessToken==null && tokenSecret==null){
			//画面系の場合
			//Frontierに登録されているtwitterのユーザ情報を取得する
			lots = selAllTwitterUser(mid,appDefDto.TWI_PROVIDER,"0",null,null,"1");
			token = lots.get(0).accessToken;
			secret = lots.get(0).tokenSecret;
		}
		
		//consumerkeyとconsumersecretをFrontierのDBより取得
		List<BeanMap> consumerList = oauthConsumerService.getConsumerInfo(appDefDto.ST_PROVIDER);
		
		Configuration conf = ConfigurationContext.getInstance();
		//AccessTokenの設定
		AccessToken localAccessToken = new AccessToken(token, secret);
		//consumer key/secretの設定
		OAuthAuthorization auth = new OAuthAuthorization(conf,(String)consumerList.get(0).get("consumerkey"),(String)consumerList.get(0).get("consumersecret"),localAccessToken);
		Twitter twitter = new TwitterFactory().getInstance(auth);	

		
/*		
		TwitterFactory factory = new TwitterFactory();
		twitter = factory.getInstance();
		
		//consumer key/secretの設定
		twitter.setOAuthConsumer((String)consumerList.get(0).get("consumerkey"), (String)consumerList.get(0).get("consumersecret"));
		//AccessTokenの設定
		AccessToken at = loadAccessToken(token, secret);
		//認証情報設定
		twitter.setOAuthAccessToken(at);
*/				
		return twitter;
	}
	
	/**
     * twitterクラスへ認証情報を設定する。
     * @param mid　twitterへ接続を行ないたいユーザのメンバーID。画面は必須、バッチは不要。
     * @param accessToken　twitterのユーザ毎に固有のアクセストークン。
     * 　　　　　　　　　　　　　　　　tokenSecretと併せて指定をすると本メソッドでFrontierのDBより認証情報を取得する。
     * 　　　　　　　　　　　　　　　　画面は指定不要、バッチは必須。
     * @param tokenSecret twitterのユーザ毎に固有のトークンシークレット。
     * 　　　　　　　　　　　　　　　　accessTokenと併せて指定をすると本メソッドでFrontierのDBより認証情報を取得する。
     * 　　　　　　　　　　　　　　　　画面は指定不要、バッチは必須。
     * @param consumerList DBより取得したコンシューマ情報の格納されたリスト。必要のない場合はNULLを指定する。
     * @return 認証情報を設定したtwitterクラス
     */ 
	public Twitter setTwitter(String mid,String accessToken,String tokenSecret,List<BeanMap> consumerList){
		//初期化
		List<OauthTokensStore> lots = new ArrayList<OauthTokensStore>();
		String token = accessToken;
		String secret = tokenSecret;
		
		if(accessToken==null && tokenSecret==null){
			//画面系の場合
			//Frontierに登録されているtwitterのユーザ情報を取得する
			lots = selAllTwitterUser(mid,appDefDto.TWI_PROVIDER,"0",null,null,"1");
			if(lots.size()!=0){
				token = lots.get(0).accessToken;
				secret = lots.get(0).tokenSecret;
			}
		}
		
		try{
			//第４引数が設定されているかどうかの判定するために実行
			consumerList.size();
		}catch(NullPointerException e){
			//consumerkeyとconsumersecretをFrontierのDBより取得
			consumerList = oauthConsumerService.getConsumerInfo(appDefDto.TWI_PROVIDER);			
		}
			
		Configuration conf = ConfigurationContext.getInstance();
		//AccessTokenの設定
		AccessToken localAccessToken = new AccessToken(token, secret);
		//consumer key/secretの設定
		OAuthAuthorization auth = new OAuthAuthorization(conf,(String)consumerList.get(0).get("consumerkey"),(String)consumerList.get(0).get("consumersecret"),localAccessToken);
		Twitter twitter = new TwitterFactory().getInstance(auth);	
		
		return twitter;
	}
	
	/**
     * twitter_db及びtwitter_post_managementテーブルからmention又はお気に入り一覧を取得する。
     * @param statusidTo　(検索キー)指定したIDより大きいMentionを取得する。使用しない場合はnullを渡す。
     * @param statusidFrom　(検索キー)指定したID以下のMentionを取得する。使用しない場合はnullを渡す。
     * @param userid　(検索キー)ここで指定したtwitterでのユーザIDのMentionを検索する
     * @param realmentionflg　(検索キー)Mentionフラグ。Mentionを取得したい場合は１を設定。使用しない場合はnullを渡す。
     * @param favoriteflg (検索キー)１を指定するとお気に入りを検索する。使用しない場合はnullを渡す。
     * @param limit 一度に取得する最大件数。
     * @param offset 最初に取得する行の位置     
     * @return mentionまたはお気に入り一覧
     */ 	
	public List<BeanMap> selMentionOrFavoriteList(Object statusidTo,Object statusidFrom,int userid,String realmentionflg,String favoriteflg,Integer limit,int offset){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("twituserid", userid);
		params.put("statusidFrom", statusidTo);
		params.put("statusidTo", statusidFrom);		
		params.put("realmentionflg", realmentionflg);
		params.put("favoriteflg", favoriteflg);
		params.put("limit", limit);
		params.put("offset", offset);
			
		return jdbcManager.selectBySqlFile(BeanMap.class,"data/twitter/selMentionList.sql",params)
		.getResultList();
	}

	/**
     * twitter_post_managementテーブルの既読未読フラグを既読へと一括更新する。
     * @param lb   更新対象となる発言を含んだList。List中のものが全て更新対象となる。
     * @param mid 操作実行者のメンバーID
     * @return 更新した件数の配列
     */ 	
	public int[] updPackageReadflg(List<BeanMap> lb,String mid){
		List<TwitterPostManagement> l = new ArrayList<TwitterPostManagement>();
		
		for(int i=0;i<lb.size();i++){
			TwitterPostManagement tpm = new TwitterPostManagement();
			//更新内容をエンティティへ設定
			tpm.statusid = (Long)lb.get(i).get("statusid");
			tpm.twituserid = (Integer)lb.get(i).get("mytwituserid");
			tpm.readflg = "1";
			tpm.upddate = new Timestamp ((new java.util.Date()).getTime());
			tpm.updid = mid;
			//更新対象のリストへ設定
			l.add(tpm);
		}
				
		//一括更新
		return jdbcManager.updateBatch(l).includes("readflg","upddate","updid").execute();
	}
	
	/**
	 * @deprecated
	 * @param userId
	 * @return
	 */
	//ユーザ情報取得（userIdをキーにした場合）
	public User getUserInfo(String userId){
		User user = null;

		//Twitterクラスに認証情報設定
		twitter = oauthConsumerService.authTwitter(oauthConsumerService.getConsumerInfo(appDefDto.ST_PROVIDER),oauthConsumerService.getTokens(appDefDto.ST_PROVIDER));

		try {
			//int型に変換
			int intUserid = Integer.parseInt(userId);

			//ユーザ情報取得(userIdを利用した場合：ログインユーザの場合を想定)
			user = twitter.showUser(intUserid);
		} catch (TwitterException e2){
			e2.printStackTrace();
		}

		return user;
	}

	/**
     * 自分と自分のfriendのTL取得（もっと見るの場合を想定）。
     * 指定したステータスID以下の値のIDのステータスのみ取得する。
     * @deprecated
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param maxId    ステータスID 
     * @return TL
     * @throws TwitterException 
     */  
	public List<Status> getHomeTimeLineOld(Twitter pTwitter,long maxId) throws TwitterException{
		twitter = pTwitter;
		//指定したステータスIDも含めてしますのでIDをマイナス１する
		return twitter.getHomeTimeline(setPagingMax(checkLongId(maxId)));
	}
	
	/**
     * 自分と自分のfriendのTL取得（更新の場合を想定）。
     * 指定したステータスIDより大きい値のIDのステータスのみ取得する。
     * @deprecated
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param sinceId    ステータスID 
     * @return TL
     * @throws TwitterException 
     */  
	public List<Status> getHomeTimeLineNew(Twitter pTwitter,long sinceId) throws TwitterException{
		twitter = pTwitter;
		return twitter.getHomeTimeline(setPagingNew(sinceId));
	}

	/**
     * 自分と自分のfriendのTL取得（次ページの判断に使用）。
     * @deprecated
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param pls    取得したTL 
     * @return ture:次ページあり、false:次ページなし
     * @throws TwitterException 
     */  
	public boolean getHomeTimeLineNext(Twitter pTwitter,List<Status> pls) throws TwitterException{
		boolean flg = false;
		
		twitter = pTwitter;
		if(pls!=null && pls.size()!=0){
			//取得したTLの最も古いID-1を利用して、それより古いTLを取得する
			List<Status> ls = twitter.getHomeTimeline(setPagingMax(pls.get(pls.size()-1).getId()-1));
		
			if(ls.size()>0){
				//次ページあり
				flg = true;
			}
		}
		
		return flg;
	}

	/**
     * Pagingクラス初期化。
     * ステータスIDの最小値で初期化
     * @param maxId    ステータスID 
     * @return ページングクラス
     */  
	private Paging setPagingMax(long maxId){
		Paging paging = new Paging();
		paging.setMaxId(maxId);
		return paging;
	}

	/**
     * Pagingクラス初期化。
     * ステータスIDの最大値とTL取得件数（max２００件固定）で初期化
     * @param sinceId    ステータスID 
     * @return ページングクラス
     */  
	private Paging setPagingNew(long sinceId){
		Paging paging = new Paging(sinceId);
		paging.setCount(200);
		return paging;
	}

	
	
	////他ユーザTL取得
	//public List<Status> getUTimeline(String id,Integer page,Integer cnt) throws TwitterException{
	//	return super.getUserTimeline(id,setPaging(page, cnt));
	//}

	/**
     * 検索結果取得。
     * 指定したワードで検索を実行し該当したTLを取得する。
     * @deprecated
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param word    検索語
     * @return TL
     * @throws TwitterException 
     */ 
	public List<Tweet> getSearchList(String word) throws TwitterException{
		twitter = new TwitterFactory().getInstance();
		//検索実行
		QueryResult result = twitter.search(setQueryNow(word));

		//取得結果返却
		return result.getTweets();
	}
	
	/**
     * 検索結果取得（更新の場合を想定）。
     * 指定したワードで検索を実行し該当したTLを取得する。
     * @deprecated
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param word    検索語
     * @param sinceId    ステータスID 
     * @return TL
     * @throws TwitterException 
     */ 
	public List<Tweet> getSearchListNew(String word,long sinceId) throws TwitterException{
		twitter = new TwitterFactory().getInstance();
		//検索実行
		QueryResult result = twitter.search(setQueryNew(word, sinceId));

		//取得結果返却
		return result.getTweets();
	}

	
	/**
     * 検索結果取得（もっと見るの場合を想定）。
     * 指定したワードで検索を実行し該当したTLを取得する。
     * javaのライブラリが用意されていないので、APIを直接呼ぶ方法で作成した。
     * @deprecated
     * @param word    検索語
     * @param maxId    ステータスID 
     * @return TL
     * @throws TwitterException 
	 * @throws IOException 
	 * @throws JSONException 
	 * @throws IOException 
     */ 
	public List<Map<String,String>> getSearchListOld(String word,long maxId) throws TwitterException, JSONException, IOException{		
		//検索パラメータの設定
		Map<String,String> params = new HashMap<String,String>();
		params.put("q", word);
		params.put("rpp", "20");
		params.put("max_id", String.valueOf(checkLongId(maxId)));
		
		//取得結果返却
		return exeSearch(params,word);
	}

	/**
     * 検索結果取得（次ページの判断に使用）。
     * 指定したワードで検索を実行し該当したTLを取得する。
     * javaのライブラリが用意されていないので、APIを直接呼ぶ方法で作成した。
     * @deprecated
     * @param word    検索語
     * @param pls    もっとみる実行後に取得したTL 
     * @param plt    初期表示の取得したTL 
     * @return ture:次ページあり、false:次ページなし
     * @throws TwitterException 
	 * @throws IOException 
	 * @throws JSONException 
	 * @throws IOException 
     */ 
	public boolean getSearchListNext(String word,List<Map<String,String>> pls,List<Tweet> plt) throws TwitterException, JSONException, IOException{		
		boolean flg = false;
		//検索パラメータの設定
		Map<String,String> params = new HashMap<String,String>();

		if((pls!=null && pls.size()!=0) || (plt!=null && plt.size()!=0)){
			params.put("q", word);
			params.put("rpp", "20");
			
			
			if(pls!=null && pls.size()!=0){
				//もっとみる実行後の場合
				Object obj = pls.get(pls.size()-1).get("id");
				String str = obj.toString();
				long l = Long.parseLong(str);
				l = l -1;
				params.put("max_id", String.valueOf(l));				
			}else{
				//初期表示の場合（もっと見るを未実行）
				params.put("max_id", String.valueOf(plt.get(plt.size()-1).getId()-1));
			}
			//取得結果返却
			List<Map<String,String>> ls = exeSearch(params,word);

			if(ls.size()>0){
				//次ページあり
				flg = true;
			}
		}
		
		//取得結果返却
		return flg;
	}

	/**
	 * ！！！！削除予定！！！！
     * 検索実行。
     * 指定したワードで検索を実行し該当したTLを取得する。
     * javaのライブラリが用意されていないので、APIを直接呼ぶ方法で作成した。
     * @param params  検索パラメータ
     * @param word    検索語 
     * @return TL
     * @throws TwitterException 
	 * @throws IOException 
	 * @throws JSONException 
	 * @throws IOException 
	 * @deprecated 削除予定
     */ 	
	private List<Map<String,String>> exeSearch(Map<String,String> params,String word) throws IOException{
		OAuthResponseMessage result = null;
		List<Map<String,String>> searchList = null;
		
		//APIで検索の実行
		//result = oauthConsumerService.executeGet(appDefDto.ST_PROVIDER, "http://search.twitter.com/search.json",params);

		InputStream in = null;
		try {
			//取得結果をJSONに変更する
			in = result.getBodyAsStream();
			Map<String,List<Map<String,String>>> m = null;
			m = (Map)JSON.decode(in);
			
			//JSONから必要な情報のみを設定する。
			searchList = m.get("results");
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			in.close();			
		}
		
		return searchList;
	}
	
	
	//Pagingクラス初期化
	private Paging setPaging(Integer page,Integer cnt){
		Paging paging = new Paging(page, cnt);		
		return paging;
	}
	
	// Twitterへの登録処理
	public void insTwitter(String comment){
		List<BeanMap> consumerInfo = oauthConsumerService.getConsumerInfo(appDefDto.ST_PROVIDER);
		List<BeanMap> tokens = oauthConsumerService.getTokens(appDefDto.ST_PROVIDER);
		twitter = oauthConsumerService.authTwitter(consumerInfo,tokens);
		// 投稿内容をTwitterの制限文字数でカット(140文字)
		if(comment.length() > appDefDto.TWI_MAXCOMMENT){
			comment = comment.substring(0,appDefDto.TWI_MAXCOMMENT);
		}
		//Twitterへ投稿
		try {
			twitter.updateStatus(comment);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}
	
	//他ユーザTL取得
	public List<Status> getUTimeline(String id,Integer page,Integer cnt) throws TwitterException{
		return twitter.getUserTimeline(id,setPaging(page, cnt));
	}

	/**
     * ユーザ一人のTL取得（もっと見るの場合を想定）。
     * 指定したステータスID以下の値のIDのステータスのみ取得する。
     * @deprecated
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param maxId    ステータスID
     * @param screenName ユーザ名 
     * @return TL
     * @throws TwitterException 
     */  
	public List<Status> getUTimelineOld(Twitter pTwitter,long maxId,String screenName) throws TwitterException{
		twitter = pTwitter;
		//指定したステータスIDも含めてしますのでIDをマイナス１する
		return twitter.getUserTimeline(screenName,setPagingMax(checkLongId(maxId)));
	}

	/**
     * ユーザ一人のTL取得（更新の場合を想定）。
     * 指定したステータスIDより大きい値のIDのステータスのみ取得する。
     * @deprecated
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param sinceId    ステータスID 
     * @param screenName ユーザ名 
     * @return TL
     * @throws TwitterException 
     */   
	public List<Status> getUTimelineNew(Twitter pTwitter,long sinceId,String screenName) throws TwitterException{
		twitter = pTwitter;
		return twitter.getUserTimeline(screenName,setPagingNew(sinceId));
	}

	/**
     * ユーザ一人のTL取得（次ページの判断に使用）。
     * @deprecated
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param pls    取得したTL 
     * @param screenName ユーザ名 
     * @return ture:次ページあり、false:次ページなし
     * @throws TwitterException 
     */  
	public boolean getUTimelineNext(Twitter pTwitter,List<Status> pls,String screenName) throws TwitterException{
		boolean flg = false;
		
		twitter = pTwitter;
		if(pls!=null && pls.size()!=0){
			//取得したTLの最も古いID-1を利用して、それより古いTLを取得する
			List<Status> ls = twitter.getUserTimeline(screenName,setPagingMax(pls.get(pls.size()-1).getId()-1));
		
			if(ls.size()>0){
				//次ページあり
				flg = true;
			}
		}
		
		return flg;
	}
	
	/**
     * 自分に対する言及(@ユーザ名 が含まれるステータス)の一覧を取得する。
     * @deprecated
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @return TL
     * @throws TwitterException 
     */  
	public List<Status> getMentionsNow(Twitter pTwitter) throws TwitterException{
		twitter = pTwitter;
		return twitter.getMentions();
	}
	
	/**
     * 自分に対する言及(@ユーザ名 が含まれるステータス)の一覧を取得する（もっと見るの場合を想定）。
     * @deprecated
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param maxId    ステータスID
     * @return TL
     * @throws TwitterException 
     */  
	public List<Status> getMentionsOld(Twitter pTwitter,long maxId) throws TwitterException{
		twitter = pTwitter;
		return twitter.getMentions(setPagingMax(checkLongId(maxId)));
	}
	
	/**
     * 自分に対する言及(@ユーザ名 が含まれるステータス)の一覧を取得する（更新の場合を想定）。
     * @deprecated
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param sinceId    ステータスID 
     * @return TL
     * @throws TwitterException 
     */  
	public List<Status> getMentionsNew(Twitter pTwitter,long sinceId) throws TwitterException{
		twitter = pTwitter;
		return twitter.getMentions(setPagingNew(sinceId));
	}

	/**
     * 自分に対する言及(@ユーザ名 が含まれるステータス)の一覧を取得する（次ページの判断に使用）。
     * @deprecated
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param pls    取得したTL 
     * @return ture:次ページあり、false:次ページなし
     * @throws TwitterException 
     */  
	public boolean getMentionsNext(Twitter pTwitter,List<Status> pls) throws TwitterException{
		boolean flg = false;
		
		twitter = pTwitter;
		if(pls!=null && pls.size()!=0){
			//取得したTLの最も古いID-1を利用して、それより古いTLを取得する
			List<Status> ls = twitter.getMentions(setPagingMax(pls.get(pls.size()-1).getId()-1));
		
			if(ls.size()>0){
				//次ページあり
				flg = true;
			}
		}
		
		return flg;
	}
	
	/**
     * お気に入り取得（もっと見るの場合を想定）。
     * @deprecated
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param id    ユーザID
     * @param maxId    ステータスID 
     * @return TL
     * @throws TwitterException 
     */
	public List<Map<String,String>> getFavariteOld(String id,long maxId) throws TwitterException, JSONException, IOException{		
		//検索パラメータの設定
		Map<String,String> params = new HashMap<String,String>();
		params.put("id", id);
		params.put("max_id", String.valueOf(checkLongId(maxId)));
		
		//取得結果返却
		return exeFavarite(params);
	}

	/**
     * お気に入り取得（更新の場合を想定）。
     * @deprecated
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param id    ユーザID
     * @param maxId    ステータスID 
     * @return TL
     * @throws TwitterException 
     */
	public List<Map<String,String>> getFavariteNew(String id,long sinceId) throws TwitterException, JSONException, IOException{		
		//検索パラメータの設定
		Map<String,String> params = new HashMap<String,String>();
		params.put("id", id);
		params.put("since_id", String.valueOf(sinceId));
		
		//取得結果返却
		return exeFavarite(params);
	}
	
	/**
     * お気に入り取得（次ページの判断に使用）。
     * javaのライブラリが用意されていないので、APIを直接呼ぶ方法で作成した。
     * @deprecated
     * @param id    ユーザID
     * @param pls    もっとみる実行後に取得したTL 
     * @param plt    初期表示の取得したTL 
     * @return ture:次ページあり、false:次ページなし
     * @throws TwitterException 
	 * @throws IOException 
	 * @throws JSONException 
	 * @throws IOException 
     */ 
	public boolean getFavariteNext(String id,List<Map<String,String>> pls,List<Status> plt) throws TwitterException, JSONException, IOException{		
		boolean flg = false;
		//検索パラメータの設定
		Map<String,String> params = new HashMap<String,String>();

		if((pls!=null && pls.size()!=0) || (plt!=null && plt.size()!=0)){
			params.put("id", id);
			
			if(pls!=null && pls.size()!=0){
				//もっとみる実行後の場合
				Object obj = pls.get(pls.size()-1).get("id");
				String str = obj.toString();
				long l = Long.parseLong(str);
				l = l -1;
				params.put("max_id", String.valueOf(l));				
			}else{
				//初期表示の場合（もっと見るを未実行）
				params.put("max_id", String.valueOf(plt.get(plt.size()-1).getId()-1));
			}
			//取得結果返却
			List<Map<String,String>> ls = exeFavarite(params);

			if(ls.size()>0){
				//次ページあり
				flg = true;
			}
		}
		
		//取得結果返却
		return flg;
	}
	
	
	
	/**
	 * ！！！！削除予定！！！！
     * お気に入り取得。
     * 指定したユーザのお気に入りを取得する。
     * javaのライブラリが用意されていないので、APIを直接呼ぶ方法で作成した。
     * @deprecated
     * @param params  検索パラメータ
     * @return TL
	 * @throws IOException 
     */ 	
	private List<Map<String,String>> exeFavarite(Map<String,String> params) throws IOException{
		OAuthResponseMessage result = null;
		List<Map<String,String>> searchList = null;
		
		//APIで検索の実行
		//result = oauthConsumerService.executeGet(appDefDto.ST_PROVIDER, "http://twitter.com/favorites.json",params);

		InputStream in = null;
		try {
			//取得結果をJSONに変更する
			in = result.getBodyAsStream();
			searchList = (List)JSON.decode(in);
						
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			in.close();			
		}
		
		return searchList;
	}
	
	/**
     * フォローしている人取得。
     * @deprecated
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param screenName　ユーザ名
     * @param cursor データベースのカーソル位置（初回表示時は必ず-1を渡す）
     * @return フォローしている人の一覧
	 * @throws TwitterException 
     */ 	
	public PagableResponseList<User> getFollow(Twitter pTwitter,String screenName,long cursor) throws TwitterException{
		twitter = pTwitter;
		return twitter.getFriendsStatuses(screenName, cursor);
	}
	
	/**
     * フォローされている人取得。
     * @deprecated
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param screenName　ユーザ名
     * @param cursor データベースのカーソル位置（初回表示時は必ず-1を渡す）
     * @return フォローされている人の一覧
	 * @throws TwitterException 
     */ 	
	public PagableResponseList<User> getFollower(Twitter pTwitter,String screenName,long cursor) throws TwitterException{
		twitter = pTwitter;
		return twitter.getFollowersStatuses(screenName, cursor);
	}
	
	/**
     * フォローする。
     * @deprecated
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @return screenName　ユーザ名
	 * @throws TwitterException 
     */ 	
	public void makeFollow(Twitter pTwitter,String screenName) throws TwitterException{
		twitter = pTwitter;
		twitter.createFriendship(screenName);
	}
	
	/**
     * フォロー解除。
     * @deprecated
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @return screenName　ユーザ名
	 * @throws TwitterException 
     */ 
	public void removeFollow(Twitter pTwitter,String screenName) throws TwitterException{
		twitter = pTwitter;
		twitter.destroyFriendship(screenName);
	}
	
	/**
     * フォロー、フォロワーの関係を調査する。
     * @deprecated
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param sourceScreenName    ユーザ名１
     * @param targetScreenName    ユーザ名２
     * @return String 1:フォローしているだけ、2:フォローされているだけ、3:相互フォロー、4:フォロー関係なし
     */ 		
	public String checkFollow(Twitter pTwitter,String sourceScreenName,String targetScreenName) throws TwitterException{
		twitter = pTwitter;
		//フォロー関係なしで初期化
		String flg = "4";
		
		//フォロー関係を取得
		Relationship relationship = twitter.showFriendship(sourceScreenName, targetScreenName);
		
		//target userをフォローしているかどうかを取得
		boolean followingFlg = relationship.isSourceFollowingTarget();
		//target userにフォローされているかどうかを取得
		boolean followedFlg = relationship.isSourceFollowedByTarget();
		
		if(followingFlg && followedFlg){
			//相互フォローの場合
			flg = "3";
		}else if(followingFlg && !followedFlg){
			//フォローのみしている場合
			flg = "1";
		}else if(!followingFlg && followedFlg){
			//フォローされているだけ場合
			flg = "2";
		}

		return flg;
	}
	
	/**
     * ユーザが所属しているリスト。
     * @deprecated
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param screenName ユーザ名
     * @param cursor データベースのカーソル位置（初回表示時は必ず-1を渡す）
     * @return リストの一覧
	 * @throws TwitterException 
     */ 
	public PagableResponseList<UserList> belongList(Twitter pTwitter,String screenName,long cursor) throws TwitterException{
		twitter = pTwitter;
		
		return twitter.getUserListMemberships(screenName, cursor);
	}
	
	/**
     * リストに所属している人のTL取得。
     * @deprecated
     * @param listOwnerScreenName　Listを作成したユーザ名
     * @param listId リストのID
     * @return TL
	 * @throws TwitterException 
     */ 	
	public List<Status> getListTimeLine(String listOwnerScreenName,int listId) throws TwitterException{
		twitter = new TwitterFactory().getInstance();
		
		return twitter.getUserListStatuses(listOwnerScreenName, listId, new Paging());
	}
	
	/**
     * リストに所属している人のTL取得（もっと見るの場合を想定）。
     * 指定したステータスID以下の値のIDのステータスのみ取得する。
     * @deprecated
     * @param listOwnerScreenName　Listを作成したユーザ名
     * @param listId リストのID
     * @param maxId    ステータスID 
     * @return TL
     * @throws TwitterException 
     */  
	public List<Status> getListTimeLineOld(String listOwnerScreenName,int listId,long maxId) throws TwitterException{
		twitter = new TwitterFactory().getInstance();
		//指定したステータスIDも含めてしますのでIDをマイナス１する
		return twitter.getUserListStatuses(listOwnerScreenName, listId, setPagingMax(checkLongId(maxId)));

	}
	
	/**
     * リストに所属している人のTL取得（更新の場合を想定）。
     * 指定したステータスIDより大きい値のIDのステータスのみ取得する。
     * @deprecated
     * @param listOwnerScreenName　Listを作成したユーザ名
     * @param listId リストのID
     * @param sinceId    ステータスID 
     * @return TL
     * @throws TwitterException 
     */  
	public List<Status> getListTimeLineNew(String listOwnerScreenName,int listId,long sinceId) throws TwitterException{
		twitter = new TwitterFactory().getInstance();
		return twitter.getUserListStatuses(listOwnerScreenName, listId,setPagingNew(sinceId));
	}

	/**
     * リストに所属している人のTL取得（次ページの判断に使用）。
     * @deprecated
     * @param listOwnerScreenName　Listを作成したユーザ名
     * @param listId リストのID
     * @param pls    取得したTL 
     * @return ture:次ページあり、false:次ページなし
     * @throws TwitterException 
     */  
	public boolean getListTimeLineNext(String listOwnerScreenName,int listId,List<Status> pls) throws TwitterException{
		boolean flg = false;
		
		twitter = new TwitterFactory().getInstance();
		if(pls!=null && pls.size()!=0){
			//取得したTLの最も古いID-1を利用して、それより古いTLを取得する
			List<Status> ls = twitter.getUserListStatuses(listOwnerScreenName, listId,setPagingMax(pls.get(pls.size()-1).getId()-1));
		
			if(ls.size()>0){
				//次ページあり
				flg = true;
			}
		}
		
		return flg;
	}
	
	
	/**
     * idを-1する。（もっとみるの場合は指定したIDも含んでいるため）
     * @param id    ステータスID 
     * @return id
     */ 	
	private long checkLongId(long id){
		long returnId = 0;
		
		if(id>0){
			returnId = id - 1;
		}
		
		return returnId;
	}
	
	private Query setQueryNow(String word){
		//Queryクラスに検索条件を設定
		Query query = new Query(word);
		//query.setPage(page);
		//1ページの最大取得件数(デフォルトが15なので他と併せておく)
		query.setRpp(20);

		return query;
	}
	
	private Query setQueryNew(String word,long sinceId){
		//Queryクラスに検索条件を設定
		Query query = new Query(word);
		//query.setPage(page);
		//1ページの最大取得件数(デフォルトが15なので他と併せておく)
		query.setRpp(20);
		query.setSinceId(sinceId);

		return query;
	}
}
