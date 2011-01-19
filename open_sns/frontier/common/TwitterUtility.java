package frontier.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONException;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;
import net.oauth.OAuthServiceProvider;
import net.oauth.ParameterStyle;
import net.oauth.client.OAuthClient;
import net.oauth.client.OAuthResponseMessage;
import net.oauth.client.httpclient3.HttpClient3;

import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.BeanMap;

import frontier.entity.OauthTokensStore;

import twitter4j.PagableResponseList;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Relationship;
import twitter4j.Status;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.UserList;

/**
 * Twitterと接続して処理を行うクラス。
 * 
 * @author H.Saikawa
 * @version 1.0
 */
public class TwitterUtility {
	Logger logger = Logger.getLogger(this.getClass().getName());
	
	/** OAuth認証情報を含むTwitterクラス */
	private Twitter twitter;
	private OAuthClient CLIENT = new OAuthClient(new HttpClient3());

	/**
     * フォロー、フォロワーの関係を調査する。
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
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param screenName ユーザ名
     * @param cursor データベースのカーソル位置（初回表示時は必ず-1を渡す）
     * @return ユーザが所属しているリストの一覧
	 * @throws TwitterException 
     */ 
	public PagableResponseList<UserList> getBelongList(Twitter pTwitter,String screenName,long cursor) throws TwitterException{
		twitter = pTwitter;
		
		return twitter.getUserListMemberships(screenName, cursor);
	}
	
	/**
     * エラー原因の判定を行う。
     * @param te　twitter4jが返したexception
     * @return 1:Twitterの通信障害、2:API制限エラー、3:リソースが存在しない、"":その他エラー(認証無効など)
     * @throws TwitterException 
     */ 
	public String getErrorTwitter(TwitterException te){
		String errorCd = "";
		
		if(te.isCausedByNetworkIssue()){
			//Twitterの通信障害
			errorCd = "1";
		}else if (te.exceededRateLimitation()){
			//API制限
			errorCd = "2";
		}else if (te.resourceNotFound()){
			//発言が存在しない
			errorCd = "3";
		}
		
		return errorCd;
	}
	
	
	/**
     * Twitterよりお気に入り一覧を取得する。
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param screenName　お気に入りを取得したい個人のTwitterでのアカウント名
     * @param page 1ページを20件とみなした場合のページ番号（過去に遡る程番号は大きくなる）基本は１固定。
     * @return Twitterより取得したお気に入り一覧
     * @throws TwitterException 
     */ 
	public List<Status> getFavorite(Twitter pTwitter,String screenName,Integer page) throws TwitterException{
		twitter = pTwitter;
		return twitter.getFavorites(screenName, page);
	}

	/**
     * Twitterより指定したステータスIDより未来のお気に入りを取得する。
     * @param screenName お気に入りを取得したい個人のTwitterでのアカウント名
     * @param statusId 基準となるステータスID。このIDより未来の発言を取得する
     * @param provider Twitterに登録したWebサービス名
     * @param lbmConsumer oauthconsumerinfoテーブルより検索したコンシューマ情報
     * @param lbmToken oauthtokensstoreテーブルより検索したtoken情報
     * @return Twitterより取得したお気に入り一覧
     * @throws TwitterException 
     */
	public List<Map<String,String>> getFavoriteNew(String screenName,long statusId,String privider,List<BeanMap> lbmConsumer,OauthTokensStore lbmToken) throws TwitterException, JSONException, IOException{		
		//検索パラメータの設定
		Map<String,String> params = new HashMap<String,String>();
		params.put("id", screenName);
		params.put("since_id", String.valueOf(statusId));
		
		//取得結果返却
		return exeFavorite(privider,params,lbmConsumer,lbmToken);
	}
	
	/**
     * Twitterより指定したステータスIDより過去のお気に入りを取得する。
     * @param screenName お気に入りを取得したい個人のTwitterでのアカウント名
     * @param statusId 基準となるステータスID。このIDより過去の発言を取得する
     * @param provider Twitterに登録したWebサービス名
     * @param lbmConsumer oauthconsumerinfoテーブルより検索したコンシューマ情報
     * @param lbmToken oauthtokensstoreテーブルより検索したtoken情報
     * @return Twitterより取得したお気に入り一覧
     * @throws TwitterException 
     */
	public List<Map<String,String>> getFavoriteOld(String screenName,long statusId,String privider,List<BeanMap> lbmConsumer,OauthTokensStore lbmToken) throws TwitterException, JSONException, IOException{		
		//検索パラメータの設定
		Map<String,String> params = new HashMap<String,String>();
		params.put("id", screenName);
		params.put("max_id", String.valueOf(checkLongId(statusId)));
		
		//取得結果返却
		return exeFavorite(privider,params,lbmConsumer,lbmToken);
	}
	
	/**
     * フォローしている人取得。
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
     * 自分のTL取得
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param page 1ページを最大取得件数とみなした場合のページ番号（過去に遡る程番号は大きくなる）基本は１固定。
     * @param cnt　Twitterから取得する発言の最大取得件数 
     * @return TL
     * @throws TwitterException 
     */  
	public List<Status> getHomeTimeLine(Twitter pTwitter,Integer page,Integer cnt) throws TwitterException{
		twitter = pTwitter;
		return twitter.getHomeTimeline(setPaging(page, cnt));
	}	
	
	/**
     * Twitterより指定したステータスIDより未来の自分のTLを取得する。
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param statusId   基準となるステータスID。このIDより未来の発言を取得する
     * @param page 1ページを最大取得件数とみなした場合のページ番号（過去に遡る程番号は大きくなる）基本は１固定。
     * @param cnt　Twitterから取得する発言の最大取得件数。
     * @return TL
     * @throws TwitterException 
     */  
	public List<Status> getHomeTimeLineNew(Twitter pTwitter,long statusId,Integer page,Integer cnt) throws TwitterException{
		twitter = pTwitter;
		return twitter.getHomeTimeline(setPagingNew(page,cnt,statusId,cnt));
	}
	
	/**
     * Twitterより指定したステータスIDより過去の自分のTLを取得する。
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param statusId   基準となるステータスID。このIDより過去の発言を取得する
     * @param page 1ページを最大取得件数とみなした場合のページ番号（過去に遡る程番号は大きくなる）基本は１固定。
     * @param cnt　Twitterから取得する発言の最大取得件数 
     * @return TL
     * @throws TwitterException 
     */  
	public List<Status> getHomeTimeLineOld(Twitter pTwitter,long statusId,Integer page,Integer cnt) throws TwitterException{
		twitter = pTwitter;
		//指定したステータスIDも含めてしますのでIDをマイナス１する
		return twitter.getHomeTimeline(setPagingMax(page,cnt,checkLongId(statusId)));
	}
	
	/**
	 * TwitterよりListに含まれるTLを取得する
	 * @param pTwitter　認証情報が設定されたTwitterクラス
	 * @param listOwnerScreenName List作成者のScreenName
	 * @param listId List個々に振られているID
     * @param page 1ページを最大取得件数とみなした場合のページ番号（過去に遡る程番号は大きくなる）基本は１固定。
     * @param cnt　Twitterから取得する発言の最大取得件数 
	 * @return Listに含まれるTL
	 * @throws TwitterException
	 */
	public List<Status> getListTimeLine(Twitter pTwitter,String listOwnerScreenName,int listId,Integer page,Integer cnt) throws TwitterException{
		twitter = pTwitter;
		//Listに含まれるTLを取得する
		return twitter.getUserListStatuses(listOwnerScreenName, listId, setPaging(page, cnt));
	}	
	
	/**
	 * TwitterよりListに含まれる指定したステータスIDより未来のTLを取得する
	 * @param pTwitter　認証情報が設定されたTwitterクラス
	 * @param listOwnerScreenName List作成者のScreenName
	 * @param listId List個々に振られているID
	 * @param statusId 基準となるステータスID。このIDより未来の発言を取得する
     * @param page 1ページを最大取得件数とみなした場合のページ番号（過去に遡る程番号は大きくなる）基本は１固定。
     * @param cnt　Twitterから取得する発言の最大取得件数 
	 * @return TL
	 * @throws TwitterException
	 */
	public List<Status> getListTimeLineNew(Twitter pTwitter,String listOwnerScreenName,int listId,long statusId,Integer page,Integer cnt) throws TwitterException{
		twitter = pTwitter;
		//Listに含まれるTLを取得する
		return twitter.getUserListStatuses(listOwnerScreenName, listId,setPagingNew(page,cnt,statusId,cnt));
	}
	
	/**
	 * TwitterよりListに含まれる指定したステータスIDより過去のTLを取得する
	 * @param pTwitter　認証情報が設定されたTwitterクラス
	 * @param listOwnerScreenName List作成者のScreenName
	 * @param listId List個々に振られているID
	 * @param statusId 基準となるステータスID。このIDより過去の発言を取得する。
     * @param page 1ページを最大取得件数とみなした場合のページ番号（過去に遡る程番号は大きくなる）基本は１固定。
     * @param cnt　Twitterから取得する発言の最大取得件数 
	 * @return TL
	 * @throws TwitterException
	 */
	public List<Status> getListTimeLineOld(Twitter pTwitter,String listOwnerScreenName,int listId,long statusId,Integer page,Integer cnt) throws TwitterException{
		twitter = pTwitter;
		//指定したステータスIDより過去のListに含まれるTLを取得する
		return twitter.getUserListStatuses(listOwnerScreenName, listId, setPagingMax(page,cnt,checkLongId(statusId)));
	}
	
	/**
     * ログインユーザが作成したリスト。
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param screenName ユーザ名
     * @param cursor データベースのカーソル位置（初回表示時は必ず-1を渡す）
     * @return ユーザが所属しているリストの一覧
	 * @throws TwitterException 
     */ 
	public PagableResponseList<UserList> getMadeList(Twitter pTwitter,String screenName,long cursor) throws TwitterException{
		twitter = pTwitter;
		
		return twitter.getUserLists(screenName, cursor);
	}
	
	/**
     * Mention(@ユーザ名 が含まれるステータス)の一覧を取得する。
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param page 1ページを最大取得件数とみなした場合のページ番号（過去に遡る程番号は大きくなる）基本は１固定。
     * @param cnt　Twitterから取得する発言の最大取得件数 
     * @return Mentionの一覧
     * @throws TwitterException 
     */  
	public List<Status> getMentions(Twitter pTwitter,Integer page,Integer cnt) throws TwitterException{
		twitter = pTwitter;
		return twitter.getMentions(setPaging(page, cnt));
	}

	/**
     * Twitterより指定したステータスIDより未来のMention(@ユーザ名 が含まれるステータス)の一覧を取得する。
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param statusId   基準となるステータスID。このIDより過去の発言を取得する
     * @param page 1ページを最大取得件数とみなした場合のページ番号（過去に遡る程番号は大きくなる）基本は１固定。
     * @param cnt　Twitterから取得する発言の最大取得件数 
     * @return Mentionの一覧
     * @throws TwitterException 
     */  
	public List<Status> getMentionsNew(Twitter pTwitter,long statusId,Integer page,Integer cnt) throws TwitterException{
		twitter = pTwitter;
		return twitter.getMentions(setPagingNew(page,cnt,statusId,cnt));
	}
	
	/**
     * Twitterより指定したステータスIDより過去のMention(@ユーザ名 が含まれるステータス)の一覧を取得する。
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param statusId   基準となるステータスID。このIDより過去の発言を取得する
     * @param page 1ページを最大取得件数とみなした場合のページ番号（過去に遡る程番号は大きくなる）基本は１固定。
     * @param cnt　Twitterから取得する発言の最大取得件数 
     * @return Mentionの一覧
     * @throws TwitterException 
     */  
	public List<Status> getMentionsOld(Twitter pTwitter,long statusId,Integer page,Integer cnt) throws TwitterException{
		twitter = pTwitter;
		return twitter.getMentions(setPagingMax(page,cnt,checkLongId(statusId)));
	}

	/**
     * Twitterより指定したステータスIDの情報を取得する。
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param statusId   取得したい発言のステータスID。
     * @return 発言１件の情報
     * @throws TwitterException 
     */  
	public Status getOneStatus(Twitter pTwitter,long statusId) throws TwitterException{
		twitter = pTwitter;
		return twitter.showStatus(statusId);
	}

	/**
     * 検索結果取得。
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param word    検索語
     * @param page 1ページを最大取得件数とみなした場合のページ番号（過去に遡る程番号は大きくなる）基本は１固定。
     * @param cnt　Twitterから取得する発言の最大取得件数 
     * @return 検索語を含む発言の一覧
     * @throws TwitterException 
     */ 	
	public List<Tweet> getSearchList(Twitter pTwitter,String word,Integer page,Integer cnt) throws TwitterException{
		twitter = pTwitter;
		//検索実行
		QueryResult result = twitter.search(setQuery(page,cnt,word, -1,-1));

		//取得結果返却
		return result.getTweets();
	}
	
	/**
     * 検索結果取得。
     * Twitterより指定したステータスIDより未来の指定したワードでの検索結果を取得する。
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param word    検索語
     * @param statusid    ステータスID（ここで指定したIDより大きなIDの発言が検索される。） 
     * @param page 1ページを最大取得件数とみなした場合のページ番号（過去に遡る程番号は大きくなる）基本は１固定。
     * @param cnt　Twitterから取得する発言の最大取得件数 
     * @return 検索語を含む発言の一覧
     * @throws TwitterException 
     */ 
	public List<Tweet> getSearchListNew(Twitter pTwitter,String word,long statusid,Integer page,Integer cnt) throws TwitterException{
		twitter = pTwitter;
		//検索実行
		QueryResult result = twitter.search(setQuery(page,cnt,word, statusid,-1));

		//取得結果返却
		return result.getTweets();
	}
	
	/**
     * 検索結果取得。
     * Twitterより指定したステータスID未満の指定したワードでの検索結果を取得する。
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param word    検索語
     * @param statusid    ステータスID（ここで指定したID未満の発言が検索される。） 
     * @param page 1ページを最大取得件数とみなした場合のページ番号（過去に遡る程番号は大きくなる）基本は１固定。
     * @param cnt　Twitterから取得する発言の最大取得件数 
     * @return 検索語を含む発言の一覧
     * @throws TwitterException 
     */ 
	public List<Tweet> getSearchListOld(Twitter pTwitter,String word,long statusid,Integer page,Integer cnt) throws TwitterException{
		twitter = pTwitter;
		//検索実行
		QueryResult result = twitter.search(setQuery(page,cnt,word, -1,statusid));

		//取得結果返却
		return result.getTweets();
	}
	
	/**
     * Twitterより特定の個人の発言一覧を取得する。
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param screenName　発言を取得したい個人のTwitterでのアカウント名
     * @param page 1ページを最大取得件数とみなした場合のページ番号（過去に遡る程番号は大きくなる）基本は１固定。
     * @param cnt　Twitterから取得する発言の最大取得件数 
     * @return Twitterより取得した特定の個人の発言一覧
     * @throws TwitterException 
     */ 
	public List<Status> getUTimeline(Twitter pTwitter,String screenName,Integer page,Integer cnt) throws TwitterException{
		twitter = pTwitter;
		return twitter.getUserTimeline(screenName,setPaging(page, cnt));
	}
	
	/**
     * Twitterより指定したステータスIDより過去の特定の個人の発言一覧を取得する。
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param screenName　発言を取得したい個人のTwitterでのアカウント名
     * @param statusId   基準となるステータスID。このIDより過去の発言を取得する
     * @param page 1ページを最大取得件数とみなした場合のページ番号（過去に遡る程番号は大きくなる）基本は１固定。
     * @param cnt　Twitterから取得する発言の最大取得件数。
     * @return Twitterより取得した特定の個人の発言一覧
     * @throws TwitterException 
     */  
	public List<Status> getUTimelineOld(Twitter pTwitter,String screenName,long statusId,Integer page,Integer cnt) throws TwitterException{
		twitter = pTwitter;
		//指定したステータスIDも含めてしまうのでIDをマイナス１する
		return twitter.getUserTimeline(screenName,setPagingMax(page,cnt,checkLongId(statusId)));
	}	

	/**
     * Twitterより指定したステータスIDより未来の特定の個人の発言一覧を取得する。
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param screenName　発言を取得したい個人のTwitterでのアカウント名
     * @param statusId   基準となるステータスID。このIDより未来の発言を取得する
     * @param page 1ページを最大取得件数とみなした場合のページ番号（過去に遡る程番号は大きくなる）基本は１固定。
     * @param cnt　Twitterから取得する発言の最大取得件数。
     * @return Twitterより取得した特定の個人の発言一覧
     * @throws TwitterException 
     */  
	public List<Status> getUTimelineNew(Twitter pTwitter,String screenName,long statusId,Integer page,Integer cnt) throws TwitterException{
		twitter = pTwitter;
		return twitter.getUserTimeline(screenName,setPagingNew(page,cnt,statusId,cnt));
	}		
	
	/**
     * Twitterへお気に入りを削除する。
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param statusid お気に入り削除をする発言のステータスID。
     * @return Twitterにお気に入り削除した発言に関する情報
	 * @throws TwitterException 
     */  	
	public Status deleteFavorite(Twitter pTwitter,long statusid) throws TwitterException{
		twitter = pTwitter;
		
		//指定したステータスIDの情報を取得
		Status s = getOneStatus(twitter,statusid);
		
		if(s.isFavorited()){
			//お気に入り設定済みの場合
			return twitter.destroyFavorite(statusid);			
		}else{
			//お気に入り未設定の場合
			return null;			
		}
	}
	
	/**
     * 指定したステータスIDをTwitterから削除する。
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param statusId   削除対象となるステータスID。
     * @return Twitterより取得した削除対象の発言に関する情報
     * @throws TwitterException 
     */  
	public Status deleteStatus(Twitter pTwitter,long statusId) throws TwitterException{
		twitter = pTwitter;
		return twitter.destroyStatus(statusId);
	}	

	/**
     * Twitterへお気に入りを設定する。
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param statusid お気に入り登録をする発言のステータスID。
     * @return Twitterにお気に入り登録した発言に関する情報
	 * @throws TwitterException 
     */  	
	public Status entryFavorite(Twitter pTwitter,long statusid) throws TwitterException{
		twitter = pTwitter;
		
		//指定したステータスIDの情報を取得
		Status s = getOneStatus(twitter,statusid);
		
		if(s.isFavorited()){
			//お気に入り設定済みの場合
			return null;
		}else{
			//お気に入り未設定の場合
			return twitter.createFavorite(statusid);			
		}
	}
	
	/**
     * Twitterへ投稿する。
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param txt   投稿したい内容。
     * @param  replyStatusId reply元のステータスID。<br>設定しなければreply元はなしとして投稿する。
     * @return Twitterに投稿した発言に関する情報
     * @throws TwitterException 
     */  	
	public Status entryStatus(Twitter pTwitter,String txt,Object replyStatusId) throws TwitterException{
		twitter = pTwitter;
		
		if(replyStatusId==null || replyStatusId.equals("")){
			//replayなしの場合
			return twitter.updateStatus(txt);			
		}else{
			//replayありの場合
			return twitter.updateStatus(txt, Long.parseLong(replyStatusId.toString()));
		}
		
	}
	
	/**
     * Twitterからユーザ情報を取得する。
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param userid   TwitterでのユーザID。指定したIDの情報を取得できる。
     * @return 指定したユーザIDに関するユーザ情報
     * @throws TwitterException 
     */  		
	public User getUserInfo(Twitter pTwitter,Integer userid) throws TwitterException{
		twitter = pTwitter;
		return twitter.showUser(userid);
	}

	/**
     * Twitterからユーザ情報を取得する。
     * @param pTwitter　認証情報が設定されたTwitterクラス
     * @param screenName　Twitterでのアカウント名。指定したアカウントの情報を取得できる。
     * @return 指定したアカウント名に関するユーザ情報
     * @throws TwitterException 
     */  		
	public User getUserInfo(Twitter pTwitter,String screenName) throws TwitterException{
		twitter = pTwitter;
		return twitter.showUser(screenName);
	}
	
	/**
     * お気に入り取得。
     * 指定したユーザのお気に入りを取得する。
     * javaのライブラリが用意されていないので、APIを直接呼ぶ方法で作成した。
     * @param provider Twitterに登録したWebサービス名
     * @param params APIに渡すパラメータ
     * @param lbmConsumer oauthconsumerinfoテーブルより検索したコンシューマ情報
     * @param lbmToken oauthtokensstoreテーブルより検索したtoken情報
     * @return Twitterから取得したお気に入り一覧
     */	
	private List<Map<String,String>> exeFavorite(String provider,Map<String,String> params,List<BeanMap> lbmConsumer,OauthTokensStore lbmToken) throws IOException{
		OAuthResponseMessage result = null;
		List<Map<String,String>> searchList = null;
		
		//APIで検索の実行
		result = executeGet(provider, "http://twitter.com/favorites.json",params,lbmConsumer,lbmToken);

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
     * 改ページに利用するPagingクラスを初期化する。
     * @param page 1ページを最大取得件数とみなした場合のページ番号（過去に遡る程番号は大きくなる）基本は１固定。
     * @param cnt　Twitterから取得する発言の最大取得件数 
     * @return 表示ページと最大取得数を設定したPagingクラス
     */ 
	private Paging setPaging(Integer page,Integer cnt){
		Paging paging = new Paging(page, cnt);		
		return paging;
	}
	
	/**
     * 改ページに利用するPagingクラスを初期化する。
     * このメソッドは指定したステータスID以下の一覧を取得したい場合に使用する。
     * @param page 1ページを最大取得件数とみなした場合のページ番号（過去に遡る程番号は大きくなる）基本は１固定。
     * @param cnt　Twitterから取得する発言の最大取得件数 
     * @param statusId    基準となるステータスID 
     * @return 表示ページ、最大取得数、基準となるステータスIDを設定したPagingクラス
     */  
	private Paging setPagingMax(Integer page,Integer cnt,long statusId){
		Paging paging = new Paging(page, cnt);
		paging.setMaxId(statusId);
		return paging;
	}
	
	/**
     * 改ページに利用するPagingクラスを初期化する。
     * このメソッドは指定したステータスID以上の一覧を取得したい場合に使用する。
     * @param page 1ページを最大取得件数とみなした場合のページ番号（過去に遡る程番号は大きくなる）基本は１固定。
     * @param cnt　Twitterから取得する発言の最大取得件数 
     * @param sinceId    基準となるステータスID 
     * @param maxNum　Twitterから取得する発言の最大取得件数(cntと同じ) 
     * @return 表示ページ、最大取得数、基準となるステータスIDを設定したPagingクラス
    */  
	private Paging setPagingNew(Integer page,Integer cnt,long sinceId,int maxNum){
		Paging paging = new Paging(page,cnt,sinceId);
		paging.setCount(maxNum);
		return paging;
	}
	
	/**
     * 検索に利用するQueryクラスの設定をする。
     * @param page 1ページを最大取得件数とみなした場合のページ番号（過去に遡る程番号は大きくなる）基本は１固定。
     * @param cnt　Twitterから取得する発言の最大取得件数 
     * @param word 検索語
     * @param sinceid ここで指定したステータスID以上の検索結果を取得したい場合に設定する
     * @param maxid ここで指定したステータスID未満の検索結果を取得したい場合に設定する
     * @return 検索語、表示ページ、最大取得数、基準となるステータスIDを設定したQueryクラス
    */  	
	private Query setQuery(Integer page,Integer cnt,String word,long sinceid,long maxid){
		//Queryクラスに検索条件を設定
		Query query = new Query(word);
		//表示するページ番号の設定
		query.setPage(page);
		//1ページの最大取得件数
		query.setRpp(cnt);
		//あるステータスID以上の検索結果を取得したい場合
		if(sinceid != -1){
			query.setSinceId(sinceid);
		}
		//あるステータスID未満の検索結果を取得したい場合に設定する
		if(maxid != -1){
			query.setMaxId(maxid-1);
		}
		
		return query;
	}
	
	/**
     * ステータスIdを-1する。
     * @param id    ステータスID 
     * @return -1を実施したステータスid
     */ 	
	private long checkLongId(long id){
		long returnId = 0;
		
		if(id>0){
			returnId = id - 1;
		}
		
		return returnId;
	}
	
	/**
     * OAuth認証を利用してTwitterへアクセス。
     * @param provider Twitterに登録したWebサービス名
     * @param url 使用するAPIのURL
     * @param params APIに渡すパラメータ
     * @param lbmConsumer oauthconsumerinfoテーブルより検索したコンシューマ情報
     * @param lbmToken oauthtokensstoreテーブルより検索したtoken情報
     * @param oauthTokensDto セッション
     * @return Twitterから取得した結果
     */
	private OAuthResponseMessage executeGet(String provider,String url,Map<String,String> params,List<BeanMap> lbmConsumer,OauthTokensStore lbmToken){

		OAuthConsumer consumer = null;
		OAuthAccessor accessor = null;
		//コンシューマ定義をするクラスにコンシューマ情報を設定
		consumer = getConsumer(provider,lbmConsumer);

        try{
        	//アクセス定義をするクラスにtoken情報を設定
            accessor = getAccessor(consumer,lbmToken);
            
            //OAuth認証＋APIを利用してtwitterへ接続
            OAuthResponseMessage result = CLIENT.access(accessor.newRequestMessage(OAuthMessage.GET,
                url, params.entrySet()), ParameterStyle.BODY);
            return result;
        }catch(Exception e){
        		e.printStackTrace();
        		throw new RuntimeException();        	
        }
        
	}
	
    /**
     * コンシューマ情報を設定。
     * @param provider Twitterに登録したWebサービス名
     * @param lb oauthconsumerinfoテーブルより検索したコンシューマ情報
     * @return 必要な情報を設定したOAuthConsumerクラス
     */
	private OAuthConsumer getConsumer(String provider,List<BeanMap> lb){
    
		OAuthConsumer consumer   = null;
		
		// コンシューマ情報を設定
		if(lb.size() > 0 ){			
			consumer = new OAuthConsumer(
					(String)lb.get(0).get("callbackurl"),
					(String)lb.get(0).get("consumerkey"),
					(String)lb.get(0).get("consumersecret"),
					new OAuthServiceProvider(
							(String)lb.get(0).get("requesttokenurl"),
							(String)lb.get(0).get("userauthrizationurl"),
							(String)lb.get(0).get("accesstokenurl")				
							)
								
					);
		}
		consumer.setProperty("name", provider);
		return consumer;
	}
	
    /**
     * 個々の認証に必要なtoken情報をセッションより設定する。
     * @param consumer　コンシューマを定義しているクラス
     * @param lbm oauthtokensstoreテーブルより検索したtoken情報
     * @return 個々の認証に必要なtoken情報を設定したクラス
     * @throws OAuthException
     */
	private OAuthAccessor getAccessor(OAuthConsumer consumer,OauthTokensStore lbm)
	throws OAuthException, IOException, URISyntaxException{
	    
		
		OAuthAccessor accessor = null;
		
		HashMap<String,Map<String,String>> oauthTokens = new HashMap<String,Map<String,String>>();
	
		// tokenを取得。
		Map<String,String>tokens = oauthTokens.get((String)consumer.getProperty("name"));
		
		// tokenが保持されていなければ、トークン情報を作成。
		if(tokens==null){
			tokens = new HashMap<String,String>();
		   	tokens.put("requestToken", null);
		   	tokens.put("accessToken" , null);
		   	tokens.put("tokenSecret" , null);

		   	// DBに保持しているトークンを設定。
		   	if(lbm != null ){
			   	tokens.put("requestToken", (String)lbm.requestToken);
			   	tokens.put("accessToken" , (String)lbm.accessToken);
			   	tokens.put("tokenSecret" , (String)lbm.tokenSecret);
		   	}
		   	
		   	// token情報を保存
		   	oauthTokens.put((String)consumer.getProperty("name"), tokens);

		}
		
		// Access Tokenが設定すみであれば、tokenの情報をそのまま返す。
        accessor = newAccessor(consumer,oauthTokens);

		return accessor;
	}	
	
    /**
     * 個々の認証に必要なtoken情報をセッションより設定する。
     * @param consumer　コンシューマを定義しているクラス
     * @param oauthTokens token情報
     * @return 個々の認証に必要なtoken情報を設定したクラス
     * @throws OAuthException
     */
	private OAuthAccessor newAccessor(OAuthConsumer consumer,HashMap<String,Map<String,String>> oauthTokens)
    throws OAuthException {
    	OAuthAccessor accessor = new OAuthAccessor(consumer);
    	
    	//token情報を取得
    	Map<String,String> tokens = oauthTokens.get(consumer.getProperty("name"));

    	//OAuthAccessorクラスにtoken情報を設定
    	accessor.requestToken = tokens.get("requestToken");
    	accessor.accessToken  = tokens.get("accessToken");
    	accessor.tokenSecret  = tokens.get("tokenSecret");
    	return accessor;
    }
}
