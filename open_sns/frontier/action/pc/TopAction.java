package frontier.action.pc;


import frontier.common.CmnUtility;
import frontier.common.TwitterUtility;
import frontier.dto.AppDefDto;
import frontier.dto.DiaryDto;
import frontier.dto.UserInfoDto;
import frontier.entity.FrontierUserManagement;
import frontier.entity.Members;
import frontier.form.pc.TopForm;
import frontier.service.CalendarService;
import frontier.service.ClistService;
import frontier.service.CommonService;
import frontier.service.FriendListService;
import frontier.service.FshoutService;
import frontier.service.MemService;
import frontier.service.MembersService;
import frontier.service.NewsService;
import frontier.service.OauthConsumerService;
import frontier.service.TopService;
import frontier.service.TwitterService;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.http.AccessToken;

public class TopAction {
	Logger logger = Logger.getLogger(this.getClass().getName());

	@Resource
	public AppDefDto appDefDto;
	@Resource
	public UserInfoDto userInfoDto;
	@Resource
	public DiaryDto diaryDto;
	@ActionForm
	@Resource
	protected TopForm topForm;
	@Resource
	protected CommonService commonService;
	@Resource
	protected ClistService clistService;
	@Resource
	protected FriendListService friendListService;
	@Resource
	protected TopService topService;
	@Resource
	protected CalendarService calendarService;
	@Resource
	protected NewsService newsService;
	@Resource
	protected MembersService membersService;
	@Resource
	protected MemService memService;
	@Resource
	protected OauthConsumerService oauthConsumerService;
	@Resource
	protected FshoutService fshoutService;
	@Resource
	protected TwitterService twitterService;

	public FrontierUserManagement frontierUserManagement;

	//一覧系変数
	public List<BeanMap> results;
	public List<BeanMap> MyUpdateInfo;
	public List<BeanMap> fdiaryNewList;
	public List<BeanMap> communityNewList;
	public List<BeanMap> fsNewList;
	public List<BeanMap> NewFollowList;
	public List<BeanMap> FriendList;
	public BeanMap ViewInfo;
	public Members MemberInfo;
	public List<BeanMap> scheduleList;
	public List<BeanMap> NewsList;
	public List<BeanMap> rssList;
	public List<String[]> RssNewsList;
	public List<BeanMap> FNetList;
	public List<BeanMap> profileList;
	public Integer GroupListCnt;
	public Integer FollowmeListCnt;
	public Integer FollowyouListCnt;
	public List<BeanMap> GroupList;
	public List<BeanMap> FollowmeList;
	public List<BeanMap> FollowyouList;
	public List<BeanMap> GList;
	public List<BeanMap> MemberItemList;
	public Integer fsNewListCntAll;
	public Integer fsNewListCntNokori;
	public List<BeanMap> fsMaxList;
	public List<BeanMap> NewDiaryCommentList;
	public List<BeanMap> NewPhotoCommentList;


	//RSS系変数
	public String RSSflg;

	//内部処理用
	public String yyyymmdd = CmnUtility.getToday("yyyyMMdd");
	public String today = CmnUtility.getToday("yyyyMMddHHmmss");
	//カレンダーのリンク表示用
	protected List<BeanMap> monthResults;
	//カレンダー生成用
	public List<Map<String,Object>> cal;
	//コミュニティ参加リクエスト件数
	public long CommunityReqCount;
	//参加コミュニティ件数
	public Integer communityCnt;
	//マイフォト画像表示用変数
	public BeanMap photoData;
	//ニックネーム表示用
	public String EscapeNM;
	//画面表示要素
	public String c1;
	public String c2;
	public String c3;
	//twitterにも投稿用
	//twitterへログインするためのID、PASS
	public String twitterIPass;
	//「twitterにも投稿」にチェック
	public String twitterFlgCheck;
	// 通常ユーザかFrontierNetユーザかで遷移先切替用
	private String rtnURL;
	//公開範囲
	public String pubFshout;
	//投稿先
	public String fsTarget;
	//テキストエリアフラグ
	public Integer txtareaFlg;

	//twitter
	public Twitter twitter;
	//twitter
	public List<Twitter> twList;
	//TLのリスト
	public List<Status> tlList;
	//TL次ページ判定フラグ
	public boolean nextPageFlg;

	//twitter名前
	public String getTwScreenName;
	//twitterフォローしている数
	public Integer getTwFriendsCnt;
	//twitterフォローされている数
	public Integer getTwFollowerCnt;
	//twitterツィート数
	public Integer getTwStatusesCnt;
	//twitterプロフィール画像ＵＲＬ
	public URL getTwProfileImgURL;
	//twitterID
	public Integer getId;

	//twitterから取得したﾘｽﾄ詰め直し用
	public List<BeanMap> getTlList;
	//twitterもっと見るリンク表示FLG
	public boolean tlMoreFlg;

	public Integer twOKNGflg;

	//Twitter利用状況判定
	public String useTwitterFlg;

	// 初期表示
	@Execute(validator=false)
	public String index(){
		topForm.calendarDay = yyyymmdd;

		//初期表示時のFShout fsoffsetセット
		topForm.fsoffset = 0;

		init();
		// メンバータイプにより遷移先切替
		return rtnURL;
	}

	// プロフィール確認
	@Execute(validator=false)
	public String check() {
		logger.debug("チェック");
		init();
		return "check.jsp";
	}

	// 同志一覧へ
	@Execute(validator=false)
	public String memberList() {
		return "/pc/mlist";
	}

	// 外部Frontierユーザからフォローされた際に外部Frontierメンバーページへ遷移する処理
	@Execute(validator=false)
	public String goOFrontier(){
		// フォロー情報の更新
		memService.setFollowConfirm(userInfoDto.memberId,topForm.pmid);
		// 外部Frontierへ遷移するためのURL組み立て
		rtnURL = "";
		rtnURL += "http://" + topForm.pfdomain + "/frontier/pc/openid/auth/";
		rtnURL += "?gm=mv&cid=" + topForm.pfmid;
		rtnURL += "&openid=" + appDefDto.FP_CMN_HOST_NAME + "/frontier/pc/openidserver";
		rtnURL += "&redirect=true";
		return rtnURL;
	}

	// init処理
	private void init(){
		// マイページ内の遷移なので訪問メンバーIDにメンバーIDを設定
		userInfoDto.visitMemberId = userInfoDto.memberId;
		EscapeNM = SQLEscape(userInfoDto.nickName);

		// ユーザのタイプ別処理
		if(userInfoDto.membertype.equals("0")){
			// 本Frontierユーザの処理
			setUserParms();
		} else if(userInfoDto.membertype.equals("1")){
			// 他Frontierユーザの処理
			setFUserParms();
		}
	}

	// FrontierNetユーザのパラメタセット処理
	private void setFUserParms(){
		rtnURL = "/pc/ftop/";
	}

	// 通常ユーザのパラメタセット処理
	private void setUserParms(){
		rtnURL = "home.jsp";
		// メンバー情報取得
		MemberInfo = membersService.getResultById(userInfoDto.memberId);
		// メンバー情報公開設定取得
		MemberItemList = topService.selectprofile(userInfoDto.memberId);
		// 表示ﾃﾞﾌｫﾙﾄ値セット
		setDefaultValue();
		//マイフォトデータ取得
		photoData = topService.selMyPhoto(userInfoDto.memberId);
		//マイ更新情報取得
		MyUpdateInfo = topService.selMyUpdateInfo(userInfoDto.memberId,topForm.defMyUpdateViewCnt==0?1:topForm.defMyUpdateViewCnt,topForm.defMyUpdateSort);
		//タイトル名置換処理
		SQLEscapeList(MyUpdateInfo);

		//グループメンバーリスト取得
		GList = commonService.getMidList("1",userInfoDto.memberId);

		//Frontierユーザ管理情報を取得する
		frontierUserManagement = commonService.getFrontierUserManagement(userInfoDto.memberId);


		// 私をフォローリストの件数取得
		FollowmeListCnt = MemberInfo.followernumber;
		// 私がフォローリストの件数取得
		FollowyouListCnt = MemberInfo.follownumber;
		// グループリストの件数取得
		GroupListCnt = commonService.getMidList("4",userInfoDto.memberId).size();

		// 私がフォローリストの取得
		FollowyouList = commonService.getMidListTop("2",userInfoDto.memberId,topForm.defFollowyouViewCnt);

		SQLEscapeList(FollowyouList);
		// 私をフォローリストの取得
		FollowmeList = commonService.getMidListTop("3",userInfoDto.memberId,topForm.defFollowmeViewCnt);

		SQLEscapeList(FollowmeList);
		// グループリストの取得
		GroupList = commonService.getMidListTop("4",userInfoDto.memberId,topForm.defGroupViewCnt);
		SQLEscapeList(GroupList);

		// 参加コミュニティ件数取得
		communityCnt = clistService.cntClist(userInfoDto.memberId,"0");
		// 参加コミュニティ一覧データ取得
		results = clistService.selectClist(userInfoDto.memberId,"0",0,topForm.defCommunityViewCnt==0?1:topForm.defCommunityViewCnt);
		SQLEscapeList(results);
		// コミュニティ参加リクエストユーザー数取得
		CommunityReqCount = friendListService.getCommunityRequestCount(userInfoDto.memberId);
		// 新着日記データ取得
		fdiaryNewList = topService.selDiaryNewList(userInfoDto.memberId,topForm.defMemDiaryViewCnt==0?1:topForm.defMemDiaryViewCnt,topForm.defMemDiarySort,groupids());
		SQLEscapeList(fdiaryNewList);

		// F Shoutデータ取得
		fsNewList = fshoutService.selMyFShoutList("0",userInfoDto.memberId,topForm.fsoffset,topForm.defFShoutViewCnt==0?1:topForm.defFShoutViewCnt);

		//初期表示時
		topForm.fstype = "fsList";

		if(fsNewList.size()>0){
			//最新ユニークキーを取得（更新、もっと読む対応）
			topForm.ukey = fsNewList.get(0).get("ukey").toString();

			//確認すべきFShoutコメント数取得
			topForm.fsConfirmMe = fshoutService.selFShoutCnt("3",userInfoDto.memberId);
		}

		try{
			//TwitterAPIエラー判断
			twOKNGflg = 0;

			//Twitterの設定を行っているか
			useTwitterFlg = twitterService.checkUseTwitter(userInfoDto.memberId);
			List<BeanMap> lbTwitter;
			// ユーザのTwitter情報を取得
			lbTwitter = oauthConsumerService.getTokens(userInfoDto.memberId,appDefDto.TWI_PROVIDER,"1",null);
			if(useTwitterFlg.equals("2") && lbTwitter != null){
				// メインに使用しているTwitterのアカウント名、ユーザIDの取得
				topForm.myScreenName = lbTwitter.get(0).get("screenname").toString();
				topForm.userId       = lbTwitter.get(0).get("twituserid").toString();
				twitter = twitterService.setTwitter(userInfoDto.memberId,null,null);

				// Twitter情報の取得、設定
				User twInfo = new TwitterUtility().getUserInfo(twitter,topForm.myScreenName);
				getTwScreenName    = twInfo.getScreenName();
				getTwFriendsCnt    = twInfo.getFriendsCount();
				getTwFollowerCnt   = twInfo.getFollowersCount();
				getTwStatusesCnt   = twInfo.getStatusesCount();
				getTwProfileImgURL = twInfo.getProfileImageURL();
				getId              = twInfo.getId();
				//初期化
				//topForm.memInfoFlg = null;
				topForm.myTlPage = 1;
				try {
					//初期表示データ取得
					initTl();
				} catch (TwitterException e) {
					e.printStackTrace();
				}
				try {
					tlMoreFlg = twitterService.getHomeTimeLineNext(twitter, tlList);
				} catch (TwitterException e) {
					e.printStackTrace();
				}
				//Twitterもっと読むに必要
				topForm.twPgOffset = 0;
				topForm.twPgFlg = 1; //1:初期表示、2:お気に入り、3:他ユーザーのTL、4:自分宛、5:フォローしている、6:フォローされている

				topForm.twMaxOldId = (Long) getTlList.get(getTlList.size()-1).get("id");

				//Twitter更新に必要
				topForm.twMaxNewId = (Long) getTlList.get(0).get("id");
			}
		} catch (Exception e) {
			//TwitterAPIエラー判断
			twOKNGflg = 1;
		}

		// 公開範囲
		pubFshout = MemberItemList.get(0).get("pubFshout").toString();
		// 投稿先
		fsTarget = MemberInfo.target;
		// F Shout件数取得
		topForm.resultscnt = fsNewList.size();
		// F Shoutデータ件数
		SQLEscapeList(fsNewList);
		//本文の装飾
		resetResults(fsNewList);

		//相手に確認を求めるチェック（本文）
		confirmResults(fsNewList,0);
		//相手から確認を要求されるチェック（本文）
		confirmResults(fsNewList,1);

		resetResultsAlt(fsNewList);
		//返信用装飾
		resetRe(fsNewList);

		// グループメンバーmidリスト格納用変数
		List<Object> glist = new ArrayList<Object>();
		// 取得したグループメンバーmidリストが0件以上ならフォト情報の取得
		if(GList.size()>0){
			for(BeanMap f:GList){
				glist.add(f.get("mid"));
			}
		} else {
			// データが0件時の対応
			glist.add("");
		}
		// フォローメンバーmidリスト格納用変数
		List<Object> flist = new ArrayList<Object>();
		// 取得したフォローメンバーmidリストが0件以上ならフォト情報の取得
		if(FollowyouList.size()>0){
			for(BeanMap f:FollowyouList){
				flist.add(f.get("mid"));
			}
		} else {
			// データが0件時の対応
			flist.add("");
		}

		//ｽｹｼﾞｭｰﾙデータ取得
		scheduleList = calendarService.selScheduleList(userInfoDto.memberId,today,glist,topForm.defScheduleViewCnt,topForm.defDisptypeCalendar);
		SQLEscapeList(scheduleList);

		//カレンダー設定
		monthResults = calendarService.selScheduleMonthList(userInfoDto.memberId,yyyymmdd,glist,topForm.defDisptypeCalendar);
		SQLEscapeList(monthResults);
		cal = CmnUtility.makeCustomCalendar2(yyyymmdd,monthResults);

		//新着コメントあり日記件数取得
		NewDiaryCommentList = topService.selNewDiaryCommentList(userInfoDto.memberId);
		//新着コメントありフォトアルバム件数取得
		NewPhotoCommentList = topService.selNewPhotoCommentList(userInfoDto.memberId);
		//新着フォロー件数取得
		NewFollowList = topService.selNewFollowList(userInfoDto.memberId);
		//お知らせデータ取得
		NewsList = newsService.selNewsListTop();
		//RSSニュースを検索
		rssList = topService.selRss(userInfoDto.memberId);
		//Frontier Netデータ取得
		FNetList = topService.selFNet();
		//Profileデータ取得
		profileList = topService.selProfile(userInfoDto.memberId);

		//twitter情報の取得＆設定
		String twAccount = profileList.get(0).get("twitteraccount").toString();
		String twPass = profileList.get(0).get("twitterpassword").toString();
		String twFlg = profileList.get(0).get("twitterflg").toString();
		//twitterへログインするためのID、PASSがあるか
		if(!twAccount.equals("") && !twPass.equals("")){
			twitterIPass = "1";
		}
		//「twitterにも投稿」へチェックが入ってるか
		if(twFlg.equals("1")){
			twitterFlgCheck = "1";
		}

		//FShout　もっと読む数初期化
		topForm.setFsCntResult = 0;

		//表示されていないFShout件数（残りがあれば「もっと読む」表示、用）
		// F Shoutデータ取得(もっと読むを押したときにデータがあるか)
		topForm.fsCntResult = fshoutService.selMyFShoutTopList("0",userInfoDto.memberId,topForm.ukey,20,topForm.defFShoutViewCnt==0?1:topForm.defFShoutViewCnt).size();

		//fsoffset セット（「もっと読む」にパラメータとして付与）
		if(topForm.fsCntResult > 0){
			topForm.setFsCntResult = topForm.fsoffset + appDefDto.FP_MY_FSHOUTLIST_PGMAX;
		}

		//FShout一覧表示ＦＬＧ（0:一覧、1:自分宛）
		topForm.fsListFlg = 0;

		//FShoutの最新時間取得
		fsMaxList = topService.selFShoutMaxList(userInfoDto.memberId);

		//自動更新用時間取得（秒->ミリ秒変換）
		topForm.updateAutoSec = 0;
		if(MemberInfo.updatefrequency!=null){
			topForm.updateAutoSec = MemberInfo.updatefrequency * 1000;
		}

		//●他画面からトップ画面を表示したときの、テキストエリアの扱い処理
		if(diaryDto.fscomment!=null && !diaryDto.fscomment.equals("")){
			//■日記画面で日記登録後トップ画面表示
			//テキストエリアフラグ
			txtareaFlg = 1;
			//表示用のフォームにセット
			topForm.kptVal = diaryDto.fscomment;
			//初期化
			diaryDto.fscomment = null;
			topForm.kptVtype = "";
		}else{
			if(topForm.vcmntview!=null && !topForm.vcmntview.equals("")){
				//初期化
				diaryDto.fscomment = null;
				topForm.kptVtype = "";

				//■他画面からＲＴ、ＲＥ押してトップ画面表示
				if(topForm.vtype!=null){

					//他画面から来たときの内容を変換
					resetVcmnt(topForm.vcmnt);
					resetVcmntview(topForm.vcmntview);

					//テキストエリアフラグ
					txtareaFlg = 2;
					//表示用のフォームにセット
					topForm.kptVtype = topForm.vtype;
					//他画面からの値をクリア
					topForm.vtype = "";
				}else{
					//初期化
					topForm.vtype = null;
					topForm.vcmnt = "";
					topForm.vcmntview = "";
					topForm.kptVtype = "";
				}
			}else{
				//メンバートップページでFShoutが０件のとき
				txtareaFlg = 99;
				//表示用のフォームにセット
				topForm.kptVtype = topForm.vtype;
				//他画面からの値をクリア
				topForm.vtype = "";
			}
		}
	}

	@Execute(validator = false)
	public String exeInsRss(){
		if(topForm.rssurl!=""){
			//「追加」ボタン押下後、ニュースを登録
			topService.insRss(userInfoDto.memberId,topForm.rssurl);
		}
		//init();
		return "/pc/top/redirect=true";
	}

	//RSSニュース削除
	@Execute(validator=false,urlPattern="exeDelete/{rssno}")
	public String exeDelete(){
		topService.delRss(userInfoDto.memberId,topForm.rssno);
		return "/pc/top/redirect=true";
	}

	// ￥記号変換処理
	private String SQLEscape(String s) {
		try{
			if(s!=null){
				return s.toString().replace("\\","\\\\");
			}
		}catch(Exception e){

		}
		return null;
	}

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

	// ￥記号変換処理(list)
	private void SQLEscapeList(List<BeanMap> list) {
		for(BeanMap b:list){
			//タイトル
			try{
				if(b.get("title")!=null){
					b.put("title",b.get("title").toString().replace("\\","\\\\"));
				}
			}catch(Exception e){}

			//ニックネーム
			try{
				if(b.get("nickname")!=null){
					b.put("nickname",b.get("nickname").toString().replace("\\","\\\\"));
				}
			}catch(Exception e){}

			//ｺﾐｭﾆﾃｨ名
			try{
				if(b.get("community")!=null){
					b.put("community",b.get("community").toString().replace("\\","\\\\"));
				}
			}catch(Exception e){}

			//ｸﾞﾙｰﾌﾟ名
			try{
				if(b.get("gname")!=null){
					b.put("gname",b.get("gname").toString().replace("\\","\\\\"));
				}
			}catch(Exception e){}

			//ツールチップ表示
			try{
				if(b.get("LTitle")!=null){
					b.put("LTitle",b.get("LTitle").toString().replace("\\","\\\\"));
				}
			}catch(Exception e){}

			//ｽｹｼﾞｭｰﾙ詳細表示
			try{
				if(b.get("detailjs")!=null){
					b.put("detailjs",b.get("detailjs").toString().replace("\\","\\\\"));
					//改行コード置換処理
					b.put("detailjs",b.get("detailjs").toString().replace("\\\\n","\\n"));
				}
			}catch(Exception e){}

			//ツールチップ表示2
			try{
				if(b.get("comment")!=null){
					b.put("commentall",b.get("comment").toString().replace("\\","\\\\\\\\").replace("'", "\\\\'"));
				}
			}catch(Exception e){}

		}
	}

	private void setDefaultValue(){
		// 表示設定情報取得
		ViewInfo = topService.selDefaultSetting(userInfoDto.memberId);
		if(ViewInfo!=null){
			//表示件数
			topForm.defScheduleViewCnt         = (Integer) ViewInfo.get("dispnumCalendar");;
			topForm.defMemberViewCnt           = (Integer) ViewInfo.get("dispnumMem");
			topForm.defCommunityViewCnt        = (Integer) ViewInfo.get("dispnumCom");
			topForm.defMemDiaryViewCnt         = (Integer) ViewInfo.get("dispnumMemdiary");
			topForm.defMemDiaryCmntViewCnt     = (Integer) ViewInfo.get("dispnumMemdiarycmnt");
			topForm.defCommunityBbsViewCnt     = (Integer) ViewInfo.get("dispnumCombbs");
			topForm.defCommunityBbsCmntViewCnt = (Integer) ViewInfo.get("dispnumCombbscmnt");
			topForm.defMemberUpdateViewCnt     = (Integer) ViewInfo.get("dispnumMemupdate");
			topForm.defMyUpdateViewCnt         = (Integer) ViewInfo.get("dispnumMy");
			topForm.defMyPhotoViewCnt          = (Integer) ViewInfo.get("dispnumMyphoto");
			topForm.defFShoutViewCnt           = appDefDto.FP_MY_FSHOUTLIST_PGMAX;
			topForm.defGroupViewCnt            = (Integer) ViewInfo.get("dispnumGroup");
			topForm.defFollowmeViewCnt         = (Integer) ViewInfo.get("dispnumFollowme");
			topForm.defFollowyouViewCnt        = (Integer) ViewInfo.get("dispnumFollowyou");

			//ソート順
			topForm.defCommunitySort           = (String) ViewInfo.get("sortitemCombbs");
			topForm.defCommunityCmntSort       = (String) ViewInfo.get("sortitemCombbscmnt");
			topForm.defMemDiarySort            = (String) ViewInfo.get("sortitemMemdiary");
			topForm.defMemDiaryCmntSort        = (String) ViewInfo.get("sortitemMemdiarycmnt");
			topForm.defMemberUpdateSort        = (String) ViewInfo.get("sortitemMemupdate");
			topForm.defMyUpdateSort            = (String) ViewInfo.get("sortitemMy");

			//表示位置(左)
			topForm.defScheduleViewPos         = (String) ViewInfo.get("dispposCalendar");
			topForm.defMyUpdateViewPos         = (String) ViewInfo.get("dispposMy");
			topForm.defMemberViewPos           = (String) ViewInfo.get("dispposMem");
			topForm.defCommunityViewPos        = (String) ViewInfo.get("dispposCom");
			topForm.defGroupViewPos            = (String) ViewInfo.get("dispposGroup");
			topForm.defFollowmeViewPos         = (String) ViewInfo.get("dispposFollowme");
			topForm.defFollowyouViewPos        = (String) ViewInfo.get("dispposFollowyou");
			topForm.defMyPhotoViewPos          = (String) ViewInfo.get("dispposMyphoto");

			//表示位置(中央)
			topForm.defCommunityUpdateViewPos  = (String) ViewInfo.get("dispposComupdate");
			topForm.defDiaryUpdateViewPos      = (String) ViewInfo.get("dispposDiaryupdate");
			topForm.defDiaryCmntViewPos        = (String) ViewInfo.get("dispposMemdiarycmnt");
			topForm.defCommunityCmntViewPos    = (String) ViewInfo.get("dispposCombbscmnt");
			topForm.defMemberUpdateViewPos     = (String) ViewInfo.get("dispposMemupdate");
			topForm.defFShoutViewPos           = (String) ViewInfo.get("dispposFshout");

			//表示タイプ
			topForm.defDisptypeCalendar        = (String) ViewInfo.get("disptypeCalendar");

		}

	}

	//本文装飾
	private void resetResults(List<BeanMap> lbm){
		for (int i=0;i<lbm.size();i++){
			//本文を取得
			String reComment = (String)lbm.get(i).get("comment");
			reComment = reComment.replace("\\","\\\\");

			//サニタイジング
			reComment = CmnUtility.htmlSanitizing(reComment);

			//URL化
			reComment = CmnUtility.convURL(reComment);

			//FShoutコメント文字列置換
			reComment = replaceFSComment(reComment);

			//FShoutコメント絵文字置換
			reComment = CmnUtility.replaceEmoji(reComment,appDefDto.FP_CMN_EMOJI_IMG_PATH,appDefDto.FP_CMN_EMOJI_XML_PATH);

			//ダブルクォーテーションの前の\が消えてしまっているので、ここで置換（ " -> \" ）
			reComment = reComment.replaceAll("\"", "\\\\\"");

			//BeanMapへ格納
			lbm.get(i).put("viewComment", reComment);

		}
	}


	//■■■　type 0:相手に確認を要求する、1:相手から確認を要求される　■■■
	private void confirmResults(List<BeanMap> lbm,Integer type){
		for (int i=0;i<lbm.size();i++){

			//データから取得
			String getcomment = (String)lbm.get(i).get("comment");
			String getdemandflg = (String)lbm.get(i).get("demandflg");
			String getconfirmflg = (String)lbm.get(i).get("confirmflg");
			String getmid = (String)lbm.get(i).get("mid");
			String chkfstagflg = "0";
			String getprofilemid = "";

			Pattern p = Pattern.compile("\\[@:(\\S+)\\]");
			//前半
			Pattern p2 = Pattern.compile("(\\S+)\\,");
			//後半
			Pattern p3 = Pattern.compile("\\,(\\S+)");

			//正規表現実行
			Matcher m = p.matcher(getcomment);


			// 検索(find)し、マッチする部分文字列がある限り繰り返す
			while(m.find()){
				//部分文字列取得
				String partStr = m.group(1);

				//["frontierdomain","短縮されたmid"]より、それぞれを取り出す
				Matcher m2 = p2.matcher(partStr);
				Matcher m3 = p3.matcher(partStr);

				//Frontierdomain取得
				String partStr2 = "";
				String tagFdomain = "";
				while(m2.find()){
					//test
					partStr2 = m2.group(1);
					tagFdomain = partStr2;
				}

				//メンバーID取得
				String partStr3 = "";
				String tagMid = "";
				while(m3.find()){
					//test
					partStr3 = m3.group(1);
					tagMid = partStr3;
				}

				//Profileデータ取得
				profileList = topService.selProfileFShout(tagFdomain,tagMid);

				// 取得したmidの存在チェック
				if(profileList.size() == 1){

					//プロフィールよりmidを取得
					getprofilemid = profileList.get(0).get("mid").toString();

					//タグチェック
					String chkfstag = "[@:"+partStr2+","+partStr3+"]";
					if(getcomment.length()>chkfstag.length()){
						if(getcomment.substring(0,chkfstag.length()).equals(chkfstag)){
							//コメントの一文字目から指定の長さまでの文字列がメンバーのタグであればフラグに"1"を立てる
							chkfstagflg = "1";
						}
					}
				}
				break;
			}

			//type 0:相手に確認を要求する、1:相手から確認を要求される
			if(type==0){
				//■　相手に確認を求める
				//相手に確認を求めるフラグ
				String confirmyou = "0";

				//確認要求フラグ="1"
				//確認フラグ="0"
				//自分のメンバーＩＤ
				//コメントの頭がメンバーのＩＤ
				//タグのメンバーが自分以外
				if(
					getdemandflg.equals("1") &&
					getconfirmflg.equals("0") &&
					getmid.equals(userInfoDto.memberId) &&
					chkfstagflg.equals("1") &&
					!getprofilemid.equals(userInfoDto.memberId)
				){
					confirmyou = "1";
				}else if(
					getdemandflg.equals("1") &&
					getconfirmflg.equals("1") &&
					getmid.equals(userInfoDto.memberId) &&
					chkfstagflg.equals("1") &&
					!getprofilemid.equals(userInfoDto.memberId)
				){
					//確認要求フラグ="1"
					//確認フラグ="1"
					//自分のメンバーＩＤ
					//コメントの頭がメンバーのＩＤ
					//タグのメンバーが自分以外
					confirmyou = "2";
				}

				//BeanMapへ格納
				lbm.get(i).put("confirmyou", confirmyou);

			}else if(type==1){
				//■　相手から確認を要求される

				//相手から確認を要求されるフラグ
				String confirmme = "0";

				//確認要求フラグ="1"
				//確認フラグ="0"
				//自分のメンバーＩＤ
				//コメントの頭がメンバーのＩＤ
				//タグのメンバーが自分
				if(
					getdemandflg.equals("1") &&
					getconfirmflg.equals("0") &&
					!getmid.equals(userInfoDto.memberId) &&
					chkfstagflg.equals("1") &&
					getprofilemid.equals(userInfoDto.memberId)
				){
					confirmme = "1";
				}else if(
					getdemandflg.equals("1") &&
					getconfirmflg.equals("1") &&
					!getmid.equals(userInfoDto.memberId) &&
					chkfstagflg.equals("1") &&
					getprofilemid.equals(userInfoDto.memberId)
				){
					//確認要求フラグ="1"
					//確認フラグ="1"
					//自分のメンバーＩＤ
					//コメントの頭がメンバーのＩＤ
					//タグのメンバーが自分
					confirmme = "2";
				}

				//BeanMapへ格納
				lbm.get(i).put("confirmme", confirmme);

			}

		}
	}

	//FShoutコメント文字列置換
	private String replaceFSComment(String txt){

		String viewFShout = "@xxxx ";
		StringBuffer sb = new StringBuffer();
		Pattern p = Pattern.compile("\\[@:(\\S+)\\]");
		//前半
		Pattern p2 = Pattern.compile("(\\S+)\\,");
		//後半
		Pattern p3 = Pattern.compile("\\,(\\S+)");
		//正規表現実行
		Matcher m = p.matcher(txt);
		// 検索(find)し、マッチする部分文字列がある限り繰り返す
		while(m.find()){
			//部分文字列取得
			String partStr = m.group(1);
			//["frontierdomain","短縮されたmid"]より、それぞれを取り出す
			Matcher m2 = p2.matcher(partStr);
			Matcher m3 = p3.matcher(partStr);
			//Frontierdomain取得
			String partStr2 = "";
			String tagFdomain = "";
			while(m2.find()){
				//test
				partStr2 = m2.group(1);
				tagFdomain = partStr2;
			}
			//メンバーID取得
			String partStr3 = "";
			String tagMid = "";
			while(m3.find()){
				//test
				partStr3 = m3.group(1);
				tagMid = partStr3;
			}
			//Profileデータ取得
			profileList = topService.selProfileFShout(tagFdomain,tagMid);
			String getNickName = partStr;
			String getMid = "";
			String getFrontierDomain = "";
			String myFrontierDomain = appDefDto.FP_CMN_HOST_NAME;
			String FShout = "";
			// 取得したmidが存在すればニックネームセット
			if(profileList.size() == 1){
				if(profileList.get(0).get("membertype").toString().equals("0")){
					//自Frontier
					getNickName = profileList.get(0).get("nickname").toString();
					getMid = profileList.get(0).get("mid").toString();
				}else if(profileList.get(0).get("membertype").toString().equals("1")){
					//他Frontier
					getNickName = profileList.get(0).get("fnickname").toString();
					getMid = profileList.get(0).get("fid").toString();
					getFrontierDomain = profileList.get(0).get("frontierdomain").toString();
				}
				getNickName = getNickName.replace("\\","\\\\");
				//FShoutの可変変数を置換
				FShout = viewFShout.replaceAll("xxxx", getNickName);
				FShout = FShout.replaceAll(" ", "");
				final Pattern convURLLinkPtn = Pattern.compile("(@)\\S+");
				//「\」対応
				FShout = FShout.replace("\\","\\\\\\\\");
				Matcher matcher = convURLLinkPtn.matcher(FShout);
				if(profileList.get(0).get("membertype").toString().equals("0")){
					//自Frontier
					FShout = matcher.replaceAll("<a href=\"/frontier/pc/fshout/list/"+getMid+"/1\" title=\"$0\">$0</a>");
				}else if(profileList.get(0).get("membertype").toString().equals("1")){
					//他Frontier
					if(myFrontierDomain.equals(getFrontierDomain)){
						FShout = matcher.replaceAll("<a href=\"http://"+myFrontierDomain+"/frontier/pc/fshout/list/"+getMid+"/1\" title=\"$0\">$0</a>");
					} else {
						FShout = matcher.replaceAll("<a href=\"http://"+getFrontierDomain+"/frontier/pc/openid/auth?cid="+getMid+"&gm=mv&openid="+myFrontierDomain+"/frontier/pc/openidserver\" title=\"$0\">$0</a>");
					}
				}
				//文字列連結
				m.appendReplacement(sb, FShout);
			}
		}
		//残りの文字列連結
		m.appendTail(sb);
		return sb.toString();
	}

	//コメント文装飾
	private void resetResultsAlt(List<BeanMap> lbm){
		for (int i=0;i<lbm.size();i++){
			//本文を取得
			String reComment = (String)lbm.get(i).get("commentall");
			//サニタイジング
			reComment = CmnUtility.htmlSanitizing(reComment);
			//BeanMapへ格納
			lbm.get(i).put("viewCommentAlt", reComment);
		}
	}

	//コメント文装飾
	private void resetRe(List<BeanMap> lbm){
		for (int i=0;i<lbm.size();i++){
			//本文を取得
			String reComment = (String)lbm.get(i).get("commentall");
			//サニタイジング
			reComment = CmnUtility.htmlSanitizing(reComment);
			//サニタイジング
			reComment = CmnUtility.htmlSanitizing(reComment);
			//絵文字変換
			reComment = CmnUtility.replaceEmoji(reComment,appDefDto.FP_CMN_EMOJI_IMG_PATH,appDefDto.FP_CMN_EMOJI_XML_PATH);
			//変換
			reComment = reComment.replace("'", "\\\\\\'");
			//変換
			reComment = reComment.replace("/", "\\\\\\/");
			//FShoutコメント文字列置換
			reComment = replaceFSCommentAlt(reComment);
			//「\\」->「\」変換
			reComment = reComment.replace("\\\\", "\\");
			//BeanMapへ格納
			lbm.get(i).put("viewCommentRe", reComment);
		}
	}

	//コメント文装飾
	private void resetVcmnt(String vcmnt){
			//本文を取得
			String reComment = vcmnt;
			//サニタイジング
			reComment = CmnUtility.htmlSanitizing(reComment);
			//サニタイジング
			reComment = CmnUtility.htmlSanitizing(reComment);
			//変換
			reComment = reComment.replace("'", "\\\\\\'");
			//変換
			reComment = reComment.replace("/", "\\\\\\/");
			//「\\」->「\」変換
			reComment = reComment.replace("\\\\", "\\");
			reComment = reComment.replace("\\\\/", "/");
			reComment = reComment.replace("&amp;", "&");
			//変換
			topForm.vcmnt = reComment;
	}

	//コメント文装飾
	private void resetVcmntview(String vcmntview){
			//本文を取得
			String reComment = vcmntview;
			//サニタイジング
			reComment = CmnUtility.htmlSanitizing(reComment);
			//サニタイジング
			reComment = CmnUtility.htmlSanitizing(reComment);
			reComment = CmnUtility.escJavaScript(reComment);
			//絵文字変換
			reComment = CmnUtility.replaceEmoji(reComment,appDefDto.FP_CMN_EMOJI_IMG_PATH,appDefDto.FP_CMN_EMOJI_XML_PATH);
			reComment = CmnUtility.escJavaScript(reComment);
			//「\\」->「\」変換
			reComment = reComment.replace("\\\\", "\\");
			//変換
			topForm.vcmntview = reComment;
	}

	//FShoutコメント文字列置換
	private String replaceFSCommentAlt(String txt){
		String viewFShout = "@xxxx";
		StringBuffer sb = new StringBuffer();
		Pattern p = Pattern.compile("\\[@:(\\S+)\\]");
		//前半
		Pattern p2 = Pattern.compile("(\\S+)\\,");
		//後半
		Pattern p3 = Pattern.compile("\\,(\\S+)");

		//正規表現実行
		Matcher m = p.matcher(txt);

		// 検索(find)し、マッチする部分文字列がある限り繰り返す
		while(m.find()){
			//部分文字列取得
			String partStr = m.group(1);
			//["frontierdomain","短縮されたmid"]より、それぞれを取り出す
			Matcher m2 = p2.matcher(partStr);
			Matcher m3 = p3.matcher(partStr);

			//Frontierdomain取得
			String partStr2 = "";
			String tagFdomain = "";
			while(m2.find()){
				partStr2 = m2.group(1);
				tagFdomain = partStr2;
			}

			//メンバーID取得
			String partStr3 = "";
			String tagMid = "";
			while(m3.find()){
				partStr3 = m3.group(1);
				tagMid = partStr3;
			}

			//Profileデータ取得
			profileList = topService.selProfileFShout(tagFdomain,tagMid);
			String getNickName = partStr;
			String FShout = "";
			// 取得したmidが存在すればニックネームセット
			if(profileList.size() == 1){
				if(profileList.get(0).get("membertype").toString().equals("0")){
					//自Frontier
					getNickName = profileList.get(0).get("nickname").toString();
				}else if(profileList.get(0).get("membertype").toString().equals("1")){
					//他Frontier
					getNickName = profileList.get(0).get("fnickname").toString();
				}
				getNickName = getNickName.replace("\\","\\\\");

				//FShoutの可変変数を置換
				FShout = viewFShout.replaceAll("xxxx", getNickName);
				FShout = FShout.replaceAll(" ", "");
				final Pattern convURLLinkPtn = Pattern.compile("(@)\\S+");
				//「\」対応
				FShout = FShout.replace("\\","\\\\\\\\");
				Matcher matcher = convURLLinkPtn.matcher(FShout);
				FShout = matcher.replaceAll("$0");
				//文字列連結
				m.appendReplacement(sb, FShout);
			}
		}
		//残りの文字列連結
		m.appendTail(sb);
		return sb.toString();
	}

	//AccessToken
	 private AccessToken loadAccessToken(String accesstoken,String tokensecret){
		String token = accesstoken;
		String tokenSecret = tokensecret;
		return new AccessToken(token, tokenSecret);
	}

	//TL表示初期処理
	private void initTl() throws TwitterException{
		//TLを取得する
		tlList =  twitter.getHomeTimeline(topService.setPaging(topForm.myTlPage,appDefDto.ST_MY_TL_LIST_MAX));

		// 取得したtwitterリスト分ループ
		if(tlList.size()>0){
			List<BeanMap> stuffTlList = new ArrayList<BeanMap>();
			for(int i=0;i<tlList.size();i++){

				//時間フォーマット
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");

				// 変数設定
				Long    tlId                = null;
				String  tlCreatedAt         = "";
				String  tlText              = "";
				String  tlSource            = "";
				Integer tlInReplyToUserId   = null;
				Long    tlInReplyToStatusId = null;
				boolean tlFavorited;
				//user
				Integer tlUser_Id          = null;
				String  tlName             = "";
				String  tlScreenname       = "";
				String  tlLocation         = "";
				String  tlDescription      = "";
				URL     tlProfileImangeUrl = null;
				Integer tlFollowersCount   = null;
				Integer tlFriendsCount     = null;
				Integer tlFavouritesCount  = null;

				tlCreatedAt         = formatter.format(tlList.get(i).getCreatedAt());
				tlId                = tlList.get(i).getId();
				tlText              = tlList.get(i).getText();
				tlSource            = tlList.get(i).getSource().toString();
				tlInReplyToUserId   = tlList.get(i).getInReplyToUserId();
				tlInReplyToStatusId = tlList.get(i).getInReplyToStatusId();
				tlFavorited         = tlList.get(i).isFavorited();
				//user
				tlUser_Id           = tlList.get(i).getUser().getId();
				tlName              = tlList.get(i).getUser().getName();
				tlScreenname        = tlList.get(i).getUser().getScreenName();
				tlLocation          = tlList.get(i).getUser().getLocation().toString();
				tlDescription       = tlList.get(i).getUser().getDescription().toString();
				tlProfileImangeUrl  = tlList.get(i).getUser().getProfileImageURL();
				tlFollowersCount    = tlList.get(i).getUser().getFollowersCount();
				tlFriendsCount      = tlList.get(i).getUser().getFriendsCount();
				tlFavouritesCount   = tlList.get(i).getUser().getFavouritesCount();

				//String変換
				String strTlUser_Id = tlUser_Id.toString();

				// BeanMap に変数を設定
				BeanMap b = new BeanMap();
				b.put("id"              ,tlId);
				b.put("createdAt"       ,tlCreatedAt);
				b.put("text"            ,tlText);
				b.put("source"          ,tlSource);
				b.put("inReplyToUserId" ,tlInReplyToUserId);
				b.put("replyToStatusId" ,tlInReplyToStatusId);
				b.put("favorited"       ,tlFavorited);
				//user
				b.put("user_id"         ,strTlUser_Id);
				b.put("name"            ,tlName);
				b.put("screenName"      ,tlScreenname);
				b.put("location"        ,tlLocation);
				b.put("description"     ,tlDescription);
				b.put("profileImageUrl" ,tlProfileImangeUrl);
				b.put("followersCount"  ,tlFollowersCount);
				b.put("friendsCount"    ,tlFriendsCount);
				b.put("favouritesCount" ,tlFavouritesCount);

				//サニタイジング
				tlText = CmnUtility.htmlSanitizing(tlText);
				//URL化(本文用)
				String realText = CmnUtility.convURL(tlText);
				//FShoutコメント文字列置換
				realText = replaceFSComment(realText);
				tlText = replaceFSComment(tlText);
				//FShoutコメント文字列置換（ハッシュタグ対応）
				realText = replaceTwComment(realText);
				//FShoutコメント文字列置換（メンバー名リンク化対応）
				realText = replaceTwCommentMem(realText);
				//FShoutコメント絵文字置換
				realText = CmnUtility.replaceEmoji(realText,appDefDto.FP_CMN_EMOJI_IMG_PATH,appDefDto.FP_CMN_EMOJI_XML_PATH);
				//改行コード削除
				realText = realText.replace("\n", "").replace("\r", "");
				tlText = tlText.replace("\n", "").replace("\r", "");
				//「'」->「\\'」変換
				tlText = tlText.replace("'", "\\'");
				//BeanMapへ格納(本文)
				b.put("viewCommentTl", realText);
				//ALT
				b.put("viewCommentTlAlt", tlText);
				//返信
				b.put("viewCommentTlRe", tlText);
				stuffTlList.add(b);
			}
			//データをﾘｽﾄに格納(表示用の変数に詰める）
			getTlList = stuffTlList;
		}
		//次ページ判定
		checkNextPage(tlList);
	}

	//次ページTL存在チェック
	private void checkNextPage(List<Status> ls) throws TwitterException{
		if(ls.size() >= appDefDto.ST_MY_TL_LIST_MAX){
			//取得レコード数が1ページの最大表示数と同じ場合、次ページの存在をチェックする。
			int nextPage = topForm.myTlPage + 1;
			List<Status> nextList = twitter.getFriendsTimeline(topService.setPaging(nextPage,appDefDto.ST_MY_TL_LIST_MAX));
			if(nextList.size()==0){
				//次ページがない場合
				nextPageFlg = false;
			}else{
				nextPageFlg = true;
			}
		}else{
			nextPageFlg = false;
		}
	}

	//Twitterコメント文字列置換（ハッシュタグ対応）
	private String replaceTwComment(String txt){

		String viewFShout = "#xxxx";
		StringBuffer sb = new StringBuffer();
		Pattern p = Pattern.compile("#(\\S+)");
		//正規表現実行
		Matcher m = p.matcher(txt);
		// 検索(find)し、マッチする部分文字列がある限り繰り返す
		while(m.find()){
			//部分文字列取得
			String partStr = m.group(1);
			String FShout = "";
			// 取得したmidが存在すればニックネームセット
			FShout = viewFShout.replaceAll("xxxx", partStr);
			final Pattern convURLLinkPtn = Pattern.compile("(#)[a-zA-Z]+");
			//「\」対応
			Matcher matcher = convURLLinkPtn.matcher(FShout);
			//自Frontier
			FShout = matcher.replaceAll("<span onclick=\"twhash('"+partStr+"');\" class=\"twLink\">$0</span>");
			//文字列連結
			m.appendReplacement(sb, FShout);
		}
		//残りの文字列連結
		m.appendTail(sb);
		return sb.toString();
	}

	//Twitterコメント文字列置換（メンバー名リンク化対応）
	private String replaceTwCommentMem(String txt){

		String viewFShout = "@xxxx";
		StringBuffer sb = new StringBuffer();
		Pattern p = Pattern.compile("@(\\w+)");
		//正規表現実行
		Matcher m = p.matcher(txt);
		// 検索(find)し、マッチする部分文字列がある限り繰り返す
		while(m.find()){
			//部分文字列取得
			String partStr = m.group(1);
			String FShout = "";
			// 取得したmidが存在すればニックネームセット
			FShout = viewFShout.replaceAll("xxxx", partStr);
			final Pattern convURLLinkPtn = Pattern.compile("(@)\\S+");
			//「\」対応
			Matcher matcher = convURLLinkPtn.matcher(FShout);
			//「:」排除　※ユーザー名は英数字と'_'が使えます。
			partStr = partStr.replace(":","");
			//自Frontier
			FShout = matcher.replaceAll("<span onclick=\"cngTwList('users',this,'"+partStr+"');\" class=\"twLink\">@"+partStr+"</span>");
			//文字列連結
			m.appendReplacement(sb, FShout);
		}
		//残りの文字列連結
		m.appendTail(sb);
		return sb.toString();
	}

}