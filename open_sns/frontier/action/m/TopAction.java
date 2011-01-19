package frontier.action.m;

import frontier.common.CmnUtility;
import frontier.common.DecorationUtility;
import frontier.common.EmojiUtility;
import frontier.common.TwitterUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.entity.FrontierUserManagement;
import frontier.entity.Members;
import frontier.form.m.TopForm;
import frontier.service.CalendarService;
import frontier.service.CommonService;
import frontier.service.FshoutService;
import frontier.service.MembersService;
import frontier.service.OauthConsumerService;
import frontier.service.TopService;
import frontier.service.TwitterService;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TopAction {
	Logger logger = Logger.getLogger(this.getClass().getName());

	@Resource
	public AppDefDto appDefDto;
	@Resource
	public UserInfoDto userInfoDto;
	@ActionForm
	@Resource
	protected TopForm topForm;
	@Resource
	protected MembersService membersService;
	@Resource
	protected TopService topService;
	@Resource
	protected TwitterService twitterService;
	@Resource
	protected FshoutService fshoutService;
	@Resource
	protected CalendarService calendarService;
	@Resource
	protected CommonService commonService;
	@Resource
	protected OauthConsumerService oauthConsumerService;
	//twitter
	public Twitter twitter;

	//一覧系変数
	protected Members MemberInfo;
	public BeanMap ViewInfo;
	public List<BeanMap> FriendItems;
	public List<BeanMap> FriendUpdateInfo;
	public List<BeanMap> MemberItemInfo;
	public List<BeanMap> MyUpdateInfo;
	public List<BeanMap> NewDiaryCommentList;
	public List<BeanMap> diaryCommentList;
	public List<BeanMap> fdiaryNewList;
	public List<BeanMap> fsDt;
	public List<BeanMap> fsList;
	public List<BeanMap> profileList;
	public List<BeanMap> results;
	public List<BeanMap> scheduleList;

	//内部処理用
	public String yyyymmdd = CmnUtility.getToday("yyyyMMdd");
	public String today = CmnUtility.getToday("yyyyMMddHHmmss");
	public ActionMessages errors = new ActionMessages();
	//カレンダーのリンク表示用
	protected List<BeanMap> monthResults;
	//カレンダー生成用
	public List<Map<String,Object>> cal;
	//追加リクエスト件数
	public long FriendReqCount;
	//同志件数
	public Integer memberCnt;
	//参加コミュニティ件数
	public Integer communityCnt;
	//マイフォト画像表示用変数
	public BeanMap photoData;
	//画面表示要素
	public String c1;
	public String c2;
	public String c3;
	// F Shoutデフォルト投稿先
	private String dtarget;
	// F Shoutデフォルト公開範囲
	public String dpublevel;

	// 初期表示
	@Execute(validator=false,reset="resetall")
	public String index(){
		topForm.calendarDay = yyyymmdd;//本日日付
		init();
		// Formパラメタセット
		setform();
		return "home.jsp";
	}

	// エラー時の処理
	@Execute(validator=false,reset="reset")
	public String rtnErr(){
		topForm.calendarDay = yyyymmdd;//本日日付
		init();
		return "home.jsp";
	}

	// =================================== //
	//  ▼▼▼   F Shout関連処理    ▼▼▼ //
	// =================================== //
	// Shout!(投稿)
	@Execute(validator=true,input="rtnErr")
	public String fshout(){
		// Twitterへ登録時の結果取得用変数
		Status rtnStts = null;
		// Twitterへ登録した時に採番されたステータスIDセット用変数
		Object rtnSid = null;
		// 登録前のパラメタチェック(null or not程度)

		// 相手に確認を求める
		if(topForm.confirmflg==null){
			topForm.confirmflg=0;
		}
		// Twitterにも投稿
		if(topForm.twitterflg==null){
			topForm.twitterflg=0;
		}

		// Twitterへの登録処理(Twitterにも登録にチェックが入っていたら登録)
		if(topForm.twitterflg==2){
			twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
			// Twitterへの登録処理
			String twitCmnt = topForm.fscomment;     // 取得したコメント
			String replySid = topForm.VVReplySid; // リプライ元のStatusID
			// デコード
			try {
				twitCmnt = URLDecoder.decode(twitCmnt, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			// ========================== //
			//   ◆Twitterへの投稿処理◆  //
			// ========================== //
			// Twitterアカウントを持つメンバーのIDをTwitterアカウント名に変換
			twitCmnt = chgFidtoTid(twitCmnt);
			// 140文字で切り取り
			twitCmnt = new DecorationUtility().cutString(twitCmnt,140);
			// Twitterへ登録
			try {
				rtnStts = new TwitterUtility().entryStatus(twitter, twitCmnt,(Object)replySid);
			} catch (TwitterException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// 登録結果が取得出来ればFrontierDBへ登録を行う
			if(rtnStts != null){
				// ========================== //
				// ◆FrontierDBへの投稿処理◆ //
				// ========================== //
				// Twitterの発言のステータスID
				long tsid = rtnStts.getId();
				// FrontierShout用変数にセット
				rtnSid = (Object)tsid;
				// TwitterDBに登録
				twitterService.insTwitter(rtnStts,userInfoDto.memberId);
				// Twitter投稿管理テーブルに登録
				twitterService.insTwitterPostManagement(tsid,Integer.valueOf(topForm.userId).intValue(),"1","0","0","0","0",userInfoDto.memberId,"0");
			}
		}

		// ----------------- //
		// コメントの装飾    //
		// ----------------- //
		// 改行コード削除
		topForm.fscomment = topForm.fscomment.replaceAll("\r\n","");
		// 絵文字装飾
		topForm.fscomment = EmojiUtility.replaceMoblileToPc(
			topForm.fscomment,
			appDefDto.FP_CMN_M_EMOJI_XML,
			userInfoDto.userAgent,
			appDefDto.FP_CMN_INNER_ROOT_PATH
		);

		// Shoutを登録
		fshoutService.insFSComment(
			userInfoDto.memberId,
			topForm.fscomment,
			topForm.twitterflg,
			topForm.publevel,
			topForm.confirmflg,
			(Object)topForm.fsno,
			topForm.mid,
			rtnSid
			);
		// Formパラメタリセット
		resetform();
		return "index/?redirect=true";
	}

	// F Shout 内容確認リンク押下時の処理
	@Execute(validator=false,urlPattern="confirm/{mid}/{fsno}")
	public String confirm(){
		// コメント更新
		try {
			topService.updConfirm(topForm.mid,topForm.fsno,userInfoDto.memberId);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "index/?redirect=true";
	}

	// F Shout 削除リンク押下時の処理
	@Execute(validator=false,urlPattern="del/{fsno}")
	public String del(){
		List<BeanMap> lb;    // データ格納用変数
		String twitFlg = null;      // Twitterへも投稿したか
		String twitStatusid = null; // Twitterへ投稿した際のステータスID
		// メンバーID、NOよりF Shoutデータ取得
		lb = fshoutService.selFShoutOne(userInfoDto.memberId,topForm.fsno);
		// データが1件だけなら値をセット、それ以外ならスルー
		if(lb.size() == 1){
			if(lb.get(0).get("twitter") != null){
				twitFlg = lb.get(0).get("twitter").toString();
			}
			if(lb.get(0).get("statusid") != null){
				twitStatusid = lb.get(0).get("statusid").toString();
			}
		}
		// コメント削除
		try {
			fshoutService.delFSComment(userInfoDto.memberId,topForm.fsno);
		} catch (Exception e) {
			// TODO: handle exception
		}
		// ツイート削除
		if(twitFlg.equals("1") && twitStatusid != null && !twitStatusid.equals("")){
			// Twitterへ投稿したフラグが立っていて、ステータスIDも取得出来たらツイート削除処理
			// Twitter、Frontierからツイート削除
			twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
			// ステータスIDをlong型へ変換
			long sid = Long.parseLong(twitStatusid);
			// Twitterサイトよりデータの削除
			try {
				new TwitterUtility().deleteStatus(twitter,sid);
			} catch (TwitterException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// DB更新(削除フラグを立てる)
			twitterService.updTwitter(sid,topForm.userId);
		}
		return "index/?redirect=true";
	}

	// F Shout 返信リンク押下時の処理
	@Execute(validator=false,urlPattern="re/{mid}/{fsno}",reset="resetall")
	public String re(){
		String rtnTxtView = "";
		// RE用 値取得処理
		topForm.fscomment = getREtag(topForm.mid);
		// --------------------------- //
		// 閲覧用テキスト置換処理      //
		// --------------------------- //
		// F Shoutコメント取得(mid、noより)
		rtnTxtView = getShout(topForm.mid,topForm.fsno);
		// リンクなしニックネーム置換
		rtnTxtView = fshoutService.replaceFSCommentNickName(rtnTxtView);
		// HTMLサニタイジング
		rtnTxtView = CmnUtility.htmlSanitizing(rtnTxtView);
		// 携帯用絵文字変換
		rtnTxtView = EmojiUtility.replacePcToMoblile(
			rtnTxtView,
			appDefDto.FP_CMN_M_EMOJI_XML,
			userInfoDto.userAgent,
			appDefDto.FP_CMN_INNER_ROOT_PATH
		);
		topForm.vfscomment = rtnTxtView;
		init();
		// Formパラメタセット
		setform();
		return "home.jsp";
	}

	// F Shout RTリンク押下時の処理
	@Execute(validator=false,urlPattern="rt/{mid}/{fsno}",reset="resetall")
	public String rt(){
		// RT用 値取得処理
		topForm.fscomment = getRTtag(topForm.mid,topForm.fsno);
		init();
		// Formパラメタセット
		setform();
		return "home.jsp";
	}

	// -------------------------------- //
	// **********  function  ********** //
	// -------------------------------- //
	// メンバータグ生成処理
	private String getREtag(String getmid){
		// リターン用変数
		String rtnval = "";
		Integer mid_int = null;
		// メンバーIDよりFrontierユーザ管理テーブル情報を取得
		FrontierUserManagement fum = new FrontierUserManagement();
		fum = membersService.getFrontierUserManagement(getmid);
		// データが取得できれば処理開始
		if(fum != null){
			// メンバーIDの型変換(String -> Integer)
			try {
				mid_int = Integer.valueOf(fum.mid.substring(1)).intValue();
			} catch (Exception e) {
				// TODO: handle exception
			}
			// RT形式に組み立て
			rtnval += "[@:" + fum.frontierdomain + "," + mid_int + "]";
		}
		return rtnval;
	}
	// RTタグ生成処理
	private String getRTtag(String getmid,Integer getfsno){
		// リターン用変数
		String rtnval = "";
		// メンバータグ取得
		rtnval = getREtag(getmid);
		// 取得したメンバータグが空じゃなければRTなど装飾
		if(!rtnval.equals("")){
			rtnval = " RT " + rtnval + " ";
		}
		// 特定Shoutコメント取得＆セット
		rtnval += getShout(getmid,getfsno);;
		return rtnval;
	}

	// 特定Shoutコメント取得処理
	private String getShout(String getmid,Integer getfsno){
		// リターン用変数
		String rtnval = "";
		// メンバーID、NOよりF Shoutデータ取得
		fsDt = fshoutService.selFShoutOne(getmid,getfsno);
		// データが1件だけなら値をセット、それ以外ならスルー
		if(fsDt.size() == 1){
			rtnval = fsDt.get(0).get("comment").toString();
		}
		return rtnval;
	}
	// -------------------------------- //
	// **********  function  ********** //
	// -------------------------------- //
	// =================================== //
	//  ▲▲▲   F Shout関連処理    ▲▲▲ //
	// =================================== //

	// =================================== //
	//  ▼▼▼   Twitter関連処理    ▼▼▼ //
	// =================================== //
	// 文中のFrontierアカウント -> Twitterアカウントへ変換
	private String chgFidtoTid(String getText){
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
	// =================================== //
	//  ▲▲▲   Twitter関連処理    ▲▲▲ //
	// =================================== //

	//全てのスケジュールを表示
	@Execute(validator=false)
	public String setting1(){
		//更新
		topForm.defDisptypeCalendar="0";
		calendarService.updScheduleDispSetting(userInfoDto.memberId,topForm.defDisptypeCalendar);
		return "index";
	}

	//共有中のスケジュールのみ表示
	@Execute(validator=false)
	public String setting2(){
		//更新
		topForm.defDisptypeCalendar="1";
		calendarService.updScheduleDispSetting(userInfoDto.memberId,topForm.defDisptypeCalendar);
		return "index";
	}

	// Formパラメタセット処理
	private void setform(){
		// メンバーがデフォルトで設定している値をセットする
		// Twitterにも投稿する(0:F Shoutのみ、1:Twitterのみ、2:F Shout + Twitter)
		if(dtarget.equals("1") || dtarget.equals("2")){
			topForm.twitterflg = 2;
		}

		// 公開範囲の設定(0:外部、1:Frontier Netまで、2:ﾏｲFrontierまで、3:ｸﾞﾙｰﾌﾟまで)
		// 型が違うのでやむを得ず下記方法で処理。
		if(dpublevel.equals("1")){
			topForm.publevel=1;
		}else if(dpublevel.equals("2")){
			topForm.publevel=2;
		}else if(dpublevel.equals("3")){
			topForm.publevel=3;
		}else{
			topForm.publevel=0;
		}

		List<BeanMap> lbTwitter;
		// ユーザのTwitter情報を取得
		lbTwitter = oauthConsumerService.getTokens(userInfoDto.memberId,appDefDto.TWI_PROVIDER,"1",null);
		// Twitter設定していて、情報が取得出来たらパラメタの設定を行う
		if(topForm.Chktwitter.equals("2") && lbTwitter != null){
			// メインに使用しているTwitterのアカウント名、ユーザIDの取得
			topForm.myScreenName = lbTwitter.get(0).get("screenname").toString();
			topForm.userId       = lbTwitter.get(0).get("twituserid").toString();
		}
	}

	// init処理
	private void init(){
		// マイページ内の遷移なので訪問メンバーIDにメンバーIDを設定
		userInfoDto.visitMemberId = userInfoDto.memberId;
		// メンバー情報取得
		MemberInfo = membersService.getResultById(userInfoDto.memberId);
		// Twitterにも投稿する用
		dtarget = MemberInfo.target;
		// メンバー公開情報取得
		MemberItemInfo = topService.selectprofile(userInfoDto.memberId);
		// Twitter利用可能かどうかのチェックをする。
		//    0:未使用、1:Token削除、2:設定済み
		topForm.Chktwitter = twitterService.checkUseTwitter(userInfoDto.memberId);
		// F Shout公開範囲
		dpublevel = MemberItemInfo.get(0).get("pubFshout").toString();
		// F Shoutデータ取得
		fsList = fshoutService.selMyFShoutList("1",userInfoDto.memberId,0,appDefDto.FP_MY_M_FSHOUT_PGMAX);
		// F Shoutデータ、確認状況チェック(conftype(確認状況用変数)追加)
		fshoutService.chkconfirmFSComment(fsList);
		// 自分が確認すべきF Shout数の取得
		topForm.fscommentConfCnt = fshoutService.selFShoutCnt("3",userInfoDto.memberId);
		// 新着日記データ取得
		fdiaryNewList = topService.selDiaryNewList(userInfoDto.memberId,appDefDto.FP_MY_M_MEMDIARY_PGMAX,"01",groupids());
		//デフォルト表示設定
		setDefaultValue();
		//ｽｹｼﾞｭｰﾙデータ取得
		scheduleList = calendarService.selScheduleWeekList(
			userInfoDto.memberId,
			yyyymmdd,
			groupids(),
			CmnUtility.getCalendarDay(yyyymmdd,appDefDto.FP_MY_M_SCHEDULE_PGMAX-1),
			topForm.defDisptypeCalendar
		);
		resetResults(scheduleList);
		//今日の予定、明日の予定設定
		setSchedule();
		//新着コメントあり日記件数取得
		NewDiaryCommentList = topService.selNewDiaryCommentList(userInfoDto.memberId);
	}

	//スケジュール必須処理
	public void setSchedule(){
		int count = 0;
		//空のスケジュール
		BeanMap b = new BeanMap();
		b.put("scheduledate",yyyymmdd);
		b.put("cid",null);
		//予定なし
		if(scheduleList.size()==0){
			for(int i=0;i<appDefDto.FP_MY_M_SCHEDULE_PGMAX;i++){
				scheduleList.add(i,(BeanMap) b.clone());
				yyyymmdd = CmnUtility.getCalendarDay(yyyymmdd,1);
				b.put("scheduledate",yyyymmdd);
			}

		}else{
			for(BeanMap s:scheduleList){
				count++;
				//先頭データ
				if(count==1){
					//今日のスケジュールが存在しない
					if(!s.get("scheduledate").equals(yyyymmdd)){
						scheduleList.add(0,(BeanMap) b.clone());
						break;
					}
				}
				//末尾データ
				if(scheduleList.size()==count){
					//次の日
					yyyymmdd = CmnUtility.getCalendarDay(yyyymmdd,1);
					b.put("scheduledate",yyyymmdd);
					//明日のスケジュールが存在しない
					if(!s.get("scheduledate").equals(yyyymmdd)){
						scheduleList.add((BeanMap) b.clone());
						break;
					}
				}
			}
		}
	}

	// グループリスト取得
	private List<Object> groupids(){
		List<BeanMap> GroupList = new ArrayList<BeanMap>();
		//グループ一覧データ取得
		GroupList = commonService.getMidList("1",userInfoDto.memberId);
		//同志リスト格納用変数
		List<Object> glist = new ArrayList<Object>();
		//同志0対策
		glist.add(userInfoDto.memberId);
		for(BeanMap f:GroupList){
			glist.add(f.get("mid"));
		}
		return glist;
	}

	//本文装飾
	private void resetResults(List<BeanMap> lbm){
		for (int i=0;i<lbm.size();i++){
			//タイトルを取得
			String title = (String)lbm.get(i).get("title");

			//日記/ｽｹｼﾞｭｰﾙタイトル
			if(title!=null){
				//サニタイジング
				title = CmnUtility.htmlSanitizing(title);
				//絵文字装飾
				title = EmojiUtility.replacePcToMoblile(
					title,
					appDefDto.FP_CMN_M_EMOJI_XML,
					userInfoDto.userAgent,
					appDefDto.FP_CMN_INNER_ROOT_PATH
				);
				//BeanMapへ格納
				lbm.get(i).put("title", title);
			}
		}
	}

	//表示設定デフォルト値設定
	public void setDefaultValue(){
		ViewInfo = topService.selDefaultSetting(userInfoDto.memberId);
		if(ViewInfo!=null){
			topForm.defDisptypeCalendar = (String) ViewInfo.get("disptypeCalendar");
		}else{
			topForm.defDisptypeCalendar = "0";//デフォルト値設定
		}
	}

	// Formパラメタリセット処理
	private void resetform(){
		topForm.mid        = null;
		topForm.fsno       = null;
		topForm.fscomment  = null;
		topForm.vfscomment = null;
		topForm.twitterflg = null;
		topForm.publevel   = null;
		topForm.confirmflg = null;
	}
}