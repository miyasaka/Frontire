package frontier.action.pc.ajax;

import frontier.common.CmnUtility;
import frontier.common.DecorationUtility;
import frontier.common.TwitterUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.entity.FrontierUserManagement;
import frontier.entity.MembersetupInfo;
import frontier.entity.OauthTokensStore;
import frontier.form.pc.TopForm;
import frontier.service.CalendarService;
import frontier.service.ClistService;
import frontier.service.CommonService;
import frontier.service.FshoutService;
import frontier.service.OauthConsumerService;
import frontier.service.TopService;
import frontier.service.TwitterService;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import twitter4j.PagableResponseList;
import twitter4j.Status;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.UserList;

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
	protected ClistService clistService;
	@Resource
	protected TopService topService;
	@Resource
	protected CalendarService calendarService;
	@Resource
	protected CommonService commonService;
	@Resource
	protected OauthConsumerService oauthConsumerService;
	@Resource
	protected TwitterService twitterService;
	@Resource
	protected FshoutService fshoutService;
	public FrontierUserManagement frontierUserManagement;
	//twitter
	public Twitter twitter;
	//一覧系変数
	public List<BeanMap> results;
	public List<BeanMap> MyUpdateInfo;
	public List<BeanMap> fdiaryNewList;
	public List<BeanMap> diaryCommentList;
	public List<BeanMap> communityCommentList;
	public List<BeanMap> fsNewList;
	public List<BeanMap> FriendList;
	public List<BeanMap> scheduleList;
	public List<BeanMap> profileList;
	public List<BeanMap> GFList;
	public List<BeanMap> GroupList;
	public List<BeanMap> FollowyouList;
	public List<BeanMap> FollowmeList;
	public List<BeanMap> GList;
	public List<BeanMap> FList;
	public List<BeanMap> TopFNetList;
	public List<BeanMap> kptFollowyouList;
	public List<BeanMap> moreFsList;
	public Integer fsNewListCntAll;
	public Integer fsNewListCntNokori;
	public List<BeanMap> fsMaxList;

	//追加リクエスト件数
	public long FriendReqCount;
	//同志件数
	public Integer memberCnt;
	//マイフォト画像表示用変数
	public BeanMap photoData;
	//一覧系表示用変数
	public String ViewData = "";
	//リクエスト変数
	public HttpServletRequest request;
	//内部処理用
	public String today = CmnUtility.getToday("yyyyMMddHHmmss");
	public String youbi;
	public Object yyyymmdd;
	//selectBox生成用
	public int maxCnt;
	public int addCnt;
	public String dispViewCnt;
	public String dispViewBody;
	//カレンダーのリンク表示用
	protected List<BeanMap> monthResults;
	//カレンダー生成用
	public List<Map<String,Object>> cal;
	int cnt;
	long count;
	public int Cnthtml = 0;
	public User user;
	//twitterから取得したﾘｽﾄ詰め直し用
	public List<BeanMap> getTlList;
	//TLのリスト
	public List<Status> tlList;
	//TLの検索リスト
	public List<Tweet> tlSearcList;
	//TLの検索もっと読むリスト
	public List<Map<String, String>> tlSearcListMore;
	//TLのお気に入りリスト
	public List<Map<String, String>> tlFavList;
	//TLのお気に入りもっと読むリスト
	public List<Map<String, String>> tlFavListMore;
	//他ユーザのTLのリスト
	public List<Status> utlList;
	//twitterお気に入り一覧
	public List<Status> favoriteList;
	//フォローしている人一覧
	public List<User> followList;
	//フォローされている人一覧
	public List<User> followerList;
	//1人TL結果次ページ判定フラグ
	public boolean nextOnePageFlg;
	//ユーザ同志の関係
	public boolean userRelation;
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
	@Execute(validator=false)
	public String getSetupInfo(){
		CmnUtility.ResponseWrite("a","text/html","windows-31J","Shift_JIS");
		return null;
	}

	@Execute(validator=false,urlPattern="update/{type}/{action}/{pos}")
	public String update(){
		// メンバー設定情報の更新(※ここで位置の更新)
		topService.updatePosition(userInfoDto.memberId, topForm.type,topForm.pos);
		return null;
	}

	@Execute(validator=false,urlPattern="updRssPosition/{rssno}/{_rssno}")
	public String updRssPosition(){
		topService.updateRssPosition(userInfoDto.memberId, topForm.rssno,topForm._rssno);
		return null;
	}

	//前の月カレンダー
	@Execute(validator=false)
	public String loadBeforeMonth(){
		//月の減算
		topForm.calendarDay = CmnUtility.calcCalendar(topForm.calendarDay,-1);
		//カレンダー設定
		initCalendar();
		CmnUtility.ResponseWrite(ViewData,"text/html","windows-31J","Shift_JIS");
		return null;
	}
	//次の月カレンダー
	@Execute(validator=false)
	public String loadNextMonth(){
		//月の加算
		topForm.calendarDay = CmnUtility.calcCalendar(topForm.calendarDay,1);
		//カレンダー設定
		initCalendar();
		CmnUtility.ResponseWrite(ViewData,"text/html","windows-31J","Shift_JIS");
		return null;
	}

	//ｽｹｼﾞｭｰﾙ表示設定切り替え(カレンダー)
	@Execute(validator=false,urlPattern="setDispSchedule/{defDisptypeCalendar}")
	public String setDispSchedule(){
		//カレンダー設定
		initCalendar();
		//設定情報更新
		calendarService.updScheduleDispSetting(userInfoDto.memberId,topForm.defDisptypeCalendar);
		CmnUtility.ResponseWrite(ViewData,"text/html","windows-31J","Shift_JIS");
		return null;
	}

	//ｽｹｼﾞｭｰﾙ表示設定切り替え(ｽｹｼﾞｭｰﾙ)
	@Execute(validator=false,urlPattern="setDispSchedule2/{defDisptypeCalendar}")
	public String setDispSchedule2(){
		if(topForm.defScheduleViewCnt>0){
			scheduleList = calendarService.selScheduleList(userInfoDto.memberId,today,groupids(),topForm.defScheduleViewCnt,topForm.defDisptypeCalendar);
			setSchedule(scheduleList);
			ViewData = makeViewDataSchedule(scheduleList);
			CmnUtility.ResponseWrite(ViewData,"text/html","windows-31J","Shift_JIS");
		}
		return null;
	}

	//selectBox
	@Execute(validator=false,urlPattern="loadSelectBox/{type}/{viewcnt}")
	public String loadSelectBox(){
		//selectBox設定
		setSelectBox();
		CmnUtility.ResponseWrite(ViewData,"text/html","windows-31J","Shift_JIS");
		return null;
	}

	// 同志一覧へ
	@Execute(validator=false,urlPattern="loadData/{viewcnt}/{type}/{sortcd}")
	public String loadData() {
		MembersetupInfo mi = topService.selMemberSetting(userInfoDto.memberId);
		//Frontierユーザ管理情報を取得する
		frontierUserManagement = commonService.getFrontierUserManagement(userInfoDto.memberId);

		if(topForm.type.equals("myPhoto")){
			ViewData = makeViewDataMyPhoto(topForm.viewcnt);
		}else if(topForm.type.equals("schedule")){
			//セッションへ変数を格納
			topForm.defScheduleViewCnt=topForm.viewcnt;
			//表示設定：非表示以外
			if(topForm.defScheduleViewCnt>0){
				scheduleList = calendarService.selScheduleList(userInfoDto.memberId,today,groupids(),topForm.defScheduleViewCnt,topForm.defDisptypeCalendar);
				setSchedule(scheduleList);
				ViewData = makeViewDataSchedule(scheduleList);
			}
		}else if(topForm.type.equals("myUpdateSort")){
			//セッションへ変数を格納
			topForm.defMyUpdateSort=topForm.sortcd;
			//表示設定：非表示以外
			if(topForm.defMyUpdateViewCnt>0){
				MyUpdateInfo = topService.selMyUpdateInfo(userInfoDto.memberId,topForm.defMyUpdateViewCnt,topForm.defMyUpdateSort);
				//タイトル名置換処理
				for(BeanMap b:MyUpdateInfo){
					//b.put("title",CmnUtility.substrByte(b.get("title").toString(),28));
					try{
						if(b.get("title").toString().length()>10){
							b.put("title",b.get("title").toString().substring(0,10)+"・・・");
						}
					}catch(Exception e){

					}
				}
				ViewData = makeViewDataMyUpdate(MyUpdateInfo);
			}
		}else if(topForm.type.equals("myUpdate")){
			//セッションへ変数を格納
			topForm.defMyUpdateViewCnt=topForm.viewcnt;
			//表示設定：非表示以外
			if(topForm.defMyUpdateViewCnt>0){
				MyUpdateInfo = topService.selMyUpdateInfo(userInfoDto.memberId,topForm.defMyUpdateViewCnt,topForm.defMyUpdateSort);
				//タイトル名置換処理
				for(BeanMap b:MyUpdateInfo){
					try{
						if(b.get("title").toString().length()>10){
							b.put("title",b.get("title").toString().substring(0,10)+"・・・");
						}
					}catch(Exception e){

					}
				}
				ViewData = makeViewDataMyUpdate(MyUpdateInfo);
			}
		}else if(topForm.type.equals("community")){
			if(topForm.viewcnt==0){
				results = null;
			}else{
				results = clistService.selectClist(userInfoDto.memberId,"0",0,topForm.viewcnt);
			}
			ViewData = makeViewDataCommunity(results);
		}else if(topForm.type.equals("communityUpdateSort")){
			//セッションへ変数を格納
			topForm.defCommunitySort=topForm.sortcd;
			//表示設定：非表示以外
			if(topForm.defCommunityBbsViewCnt>0){
			}
		}else if(topForm.type.equals("communityUpdate")){
			//セッションへ変数を格納
			topForm.defCommunitySort=mi.sortitemCombbs;
			topForm.sortcd=mi.sortitemCombbs;
			topForm.defCommunityBbsViewCnt=topForm.viewcnt;
			//表示設定：非表示以外
			if(topForm.defCommunityBbsViewCnt>0){
			}
		}else if(topForm.type.equals("diaryUpdateSort")){
			//セッションへ変数を格納
			topForm.defMemDiarySort=topForm.sortcd;
			//表示設定：非表示以外
			if(topForm.defMemDiaryViewCnt>0){
				fdiaryNewList = topService.selDiaryNewList(userInfoDto.memberId,topForm.defMemDiaryViewCnt,topForm.defMemDiarySort,groupids());
				ViewData = makeViewDataDiary(fdiaryNewList);
			}
		}else if(topForm.type.equals("diaryUpdate")){
			//セッションへ変数を格納
			topForm.defMemDiarySort=mi.sortitemMemdiary;
			topForm.sortcd=mi.sortitemMemdiary;
			topForm.defMemDiaryViewCnt=topForm.viewcnt;

			//表示設定：非表示以外
			if(topForm.defMemDiaryViewCnt>0){
				fdiaryNewList = topService.selDiaryNewList(userInfoDto.memberId,topForm.defMemDiaryViewCnt,topForm.defMemDiarySort,groupids());
				ViewData = makeViewDataDiary(fdiaryNewList);
			}
		}else if(topForm.type.equals("memberUpdateSort")){
			//セッションへ変数を格納
			topForm.defMemberUpdateSort=topForm.sortcd;

			//グループメンバーのmid取得
			GList = commonService.getMidList("1",userInfoDto.memberId);
			//同志リスト格納用変数
			List<Object> glist = new ArrayList<Object>();
			if(GList.size()>0){
				for(BeanMap f:GList){
					glist.add(f.get("mid"));
				}
			} else {
				// データが0件時の対応
				glist.add("");
			}
			//フォローメンバーのmid取得
			FList = commonService.getMidListTop("2",userInfoDto.memberId,topForm.defFollowyouViewCnt);
			//同志リスト格納用変数
			List<Object> flist = new ArrayList<Object>();
			if(FList.size()>0){
				for(BeanMap f:FList){
					flist.add(f.get("mid"));
				}
			} else {
				// データが0件時の対応
				flist.add("");
			}
		}else if(topForm.type.equals("memberUpdate")){
			//セッションへ変数を格納
			topForm.defMemberUpdateSort=mi.sortitemMemupdate;
			topForm.sortcd=mi.sortitemMemupdate;
			topForm.defMemberUpdateViewCnt=topForm.viewcnt;

			//グループ＋フォローメンバーのmid取得
			GList = commonService.getMidList("1",userInfoDto.memberId);
			//同志リスト格納用変数
			List<Object> glist = new ArrayList<Object>();
			if(GList.size()>0){
				for(BeanMap f:GList){
					glist.add(f.get("mid"));
				}
			} else {
				// データが0件時の対応
				glist.add("");
			}
			//フォローメンバーのmid取得
			FList = commonService.getMidListTop("2",userInfoDto.memberId,topForm.defFollowyouViewCnt);
			//同志リスト格納用変数
			List<Object> flist = new ArrayList<Object>();
			if(FList.size()>0){
				for(BeanMap f:FList){
					flist.add(f.get("mid"));
				}
			} else {
				// データが0件時の対応
				flist.add("");
			}
		}else if(topForm.type.equals("FShout")){

			//fsoffset初期化
			topForm.fsoffset = 0;
			//FShout一覧表示ＦＬＧ（0:一覧、1:自分宛）
			topForm.fsListFlg = 0;
			topForm.fstype = "fsList";
			//セッションへ変数を格納
			topForm.defFShoutViewCnt=topForm.viewcnt;
			//表示設定：非表示以外
			if(topForm.defFShoutViewCnt>0){
				// F Shoutデータ取得
				fsNewList = fshoutService.selMyFShoutList("0",userInfoDto.memberId,topForm.fsoffset,topForm.defFShoutViewCnt==0?1:topForm.defFShoutViewCnt);
				if(fsNewList.size()>0){
					//最新ユニークキーを取得（更新、もっと読む対応）
					topForm.ukey = fsNewList.get(0).get("ukey").toString();
					//確認すべきFShoutコメント数取得
					topForm.fsConfirmMe = fshoutService.selFShoutCnt("3",userInfoDto.memberId);
				}
				//HTML作成
				ViewData = makeViewDataFShout(fsNewList);
			}
			// F Shoutデータ件数
			topForm.fsNewListcnt = topService.selFShoutListCnt(userInfoDto.memberId,topForm.defFShoutViewCnt==0?1:topForm.defFShoutViewCnt);

		}else if(topForm.type.equals("FShoutToMe")){
			//fsoffset初期化
			topForm.fsoffset = 0;
			//FShout一覧表示ＦＬＧ（0:一覧、1:自分宛）
			topForm.fsListFlg = 1;
			topForm.fstype = "fsListMe";
			//セッションへ変数を格納
			topForm.defFShoutViewCnt=topForm.viewcnt;
			//表示設定：非表示以外
			if(topForm.defFShoutViewCnt>0){
				// F Shoutデータ取得
				fsNewList = fshoutService.selMyFShoutList("2",userInfoDto.memberId,topForm.fsoffset,topForm.defFShoutViewCnt==0?1:topForm.defFShoutViewCnt);
				if(fsNewList.size()>0){
					//最新ユニークキーを取得（更新、もっと読む対応）
					topForm.ukey = fsNewList.get(0).get("ukey").toString();
				}
				if(fsNewList.size()==0){
				}
				//確認すべきFShoutコメント数取得
				topForm.fsConfirmMe = 0;
				//HTML作成（自分宛のFShoutで構成）
				ViewData = makeViewDataFShout(fsNewList);
			}
			// F Shoutデータ件数
			topForm.fsNewListcnt = topService.selFShoutListCnt(userInfoDto.memberId,topForm.defFShoutViewCnt==0?1:topForm.defFShoutViewCnt);

		}else if(topForm.type.equals("group")){
			//セッションへ変数を格納
			topForm.defGroupViewCnt=topForm.viewcnt;
			if(topForm.defGroupViewCnt==0){
				GroupList = null;
			}else{
				GroupList = commonService.getMidListTop("4",userInfoDto.memberId,topForm.defGroupViewCnt);
			}
			ViewData = makeViewDataGroup(GroupList);
		}else if(topForm.type.equals("followyou")){
			//セッションへ変数を格納
			topForm.defFollowyouViewCnt=topForm.viewcnt;
			if(topForm.defFollowyouViewCnt==0){
				FollowyouList = null;
			}else{
				FollowyouList = commonService.getMidListTop("2",userInfoDto.memberId,topForm.defFollowyouViewCnt);
			}
			ViewData = makeViewDataFollowyou(FollowyouList,topForm.defFollowyouViewCnt);
		}else if(topForm.type.equals("followme")){
			//セッションへ変数を格納
			topForm.defFollowmeViewCnt=topForm.viewcnt;
			if(topForm.defFollowmeViewCnt==0){
				FollowmeList = null;
			}else{
				FollowmeList = commonService.getMidListTop("3",userInfoDto.memberId,topForm.defFollowmeViewCnt);
			}
			frontierUserManagement = commonService.getFrontierUserManagement(userInfoDto.memberId);
			ViewData = makeViewDataFollowme(FollowmeList);
		}
		// メンバー設定情報の更新(※ここで表示件数の更新)
		topService.updateSetting(userInfoDto.memberId, topForm.type,topForm.viewcnt,topForm.sortcd);

		CmnUtility.ResponseWrite(ViewData,"text/html","windows-31J","Shift_JIS");
		//1:レスポンスデータ  2:content 3:encoding 4:charset
		return null;
	}

	private void setSelectBox(){
		//selectBox共通処理
		initSelectBox();
		ViewData +="";
		for(int i=1;i<maxCnt;i++){
			if(i<11||addCnt==3||i%5==0){
				if(topForm.viewcnt==i*addCnt){
					ViewData +="<div class=\"select\">";
					ViewData +="<span>"+i*addCnt+"件</span>";
					ViewData +="</div>";
				}else{
					ViewData +="<div>";
					ViewData +="<u onmouseover=\"showSelectBox(this,'"+topForm.type+"',document.getElementById('"+dispViewCnt+"').innerHTML);\" onclick=\"setVal("+i*addCnt+");j$('"+dispViewBody+"').load('/frontier/pc/ajax/top/loadData/'+getViewCnt(document.getElementById('"+dispViewCnt+"'),"+i*addCnt+"-parseInt(document.getElementById('"+dispViewCnt+"').innerHTML))+'/"+topForm.type+"/01?'+rand(10));setSelected('"+topForm.type+"',"+i*addCnt+");\">"+i*addCnt+"件</u>";
					ViewData +="</div>";
				}
			}
		}
	}

	private String makeViewDataMyPhoto(Integer cnt){
		ViewData ="";
		if(cnt!=0){
			//マイフォトデータ取得
			photoData = topService.selMyPhoto(userInfoDto.memberId);

			ViewData += "<div class=\"listBoxBody\">";
			ViewData += "<div id=\"listBoxImage\">";

			//表示画像パス変換処理
			if(photoData.get("photo")!=null){
				photoData.put("photo",appDefDto.FP_CMN_CONTENTS_ROOT+photoData.get("photo").toString().replace("dir", "pic180"));
			}else{
				photoData.put("photo","/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/noimg180.gif");
			}

			ViewData += "<img src=\""+photoData.get("photo")+"\" alt=\""+userInfoDto.nickName+"\"/>";
			ViewData += "</div>";
			ViewData += "<div align=\"center\">"+userInfoDto.nickName+"さん</div>";
			ViewData += "<div style=\"border-style:dotted;border-width:1px 0 0 0;text-align:left;margin:5px 5px 5px 5px;\">";
			ViewData += "<div style=\"width:100%;\"><a href=\"/frontier/pc/profile2\">プロフィール管理</a></div>";
			ViewData += "<div style=\"width:100%;\"><a href=\"/frontier/pc/check/"+userInfoDto.memberId+"\">プロフィールを見る</a></div>";
			ViewData += "</div>";
			ViewData += "</div>";
		}
		return ViewData;
	}

	private String makeViewDataCalendar(List<Map<String, Object>> data){
		ViewData ="";
		ViewData +="<tr>";
		ViewData +="<th abbr=\"日曜日\" class=\"sun\">日</th>";
		ViewData +="<th abbr=\"月曜日\" class=\"mon\">月</th>";
		ViewData +="<th abbr=\"火曜日\" class=\"tue\">火</th>";
		ViewData +="<th abbr=\"水曜日\" class=\"wed\">水</th>";
		ViewData +="<th abbr=\"木曜日\" class=\"thu\">木</th>";
		ViewData +="<th abbr=\"金曜日\" class=\"fri\">金</th>";
		ViewData +="<th abbr=\"土曜日\" class=\"sat\">土</th>";
		ViewData +="</tr>";
		ViewData +="<tr>";
		cnt = 0;
		for(Map<String, Object> b:data){
			if(cnt==0){
				for(int i=0;i<Integer.parseInt(b.get("week").toString())-1;i++){
					ViewData +="<td class=\"before\"><span>"+(Integer.parseInt(b.get("beforeDate").toString())-(Integer.parseInt(b.get("week").toString())-1)+i+1)+"</span></td>";
				}
			}
			if(b.get("existSchedule")!=null){
				if(b.get("existSchedule").equals(1)){
					//スケジュールリスト型変換
					List<BeanMap> ScheduleListMap = new ArrayList<BeanMap>();
					ScheduleListMap = (List<BeanMap>) b.get("schedule");
					StringBuffer sb = new StringBuffer();
					int scheduleCnt = 0;

					for(BeanMap scheduleMap:ScheduleListMap){
						//ｽｹｼﾞｭｰﾙ名
						if(scheduleMap.get("title")!=null){
							scheduleMap.put("title",reStr(scheduleMap.get("title").toString()));
							if(scheduleMap.get("starttime")==null && scheduleMap.get("endtime") ==null){
								scheduleMap.put("title","&nbsp;"+scheduleMap.get("title"));
							}else if(scheduleMap.get("starttime")!=null && scheduleMap.get("endtime")==null){
									scheduleMap.put("title",scheduleMap.get("starttime")+"&nbsp;"+scheduleMap.get("title").toString());
							}else if(scheduleMap.get("starttime")==null && scheduleMap.get("endtime")!=null){
								scheduleMap.put("title","～"+scheduleMap.get("endtime")+"&nbsp;"+scheduleMap.get("title").toString());
							}else{
								scheduleMap.put("title",scheduleMap.get("starttime").toString()+scheduleMap.get("endtime")+"&nbsp;"+scheduleMap.get("title").toString());
							}
						}
						sb.append("&nbsp;・"+scheduleMap.get("title"));
						scheduleCnt++;
						if(scheduleCnt!=ScheduleListMap.size()) sb.append("&#13;&#10;");
					}
					if(b.get("today")!=null){
						ViewData +="<td class=\"today\">";
					}else if(b.get("before")!=null){
						ViewData +="<td class=\"before\">";
					}else if(b.get("next")!=null){
						ViewData +="<td class=\"next\">";
					}else if(b.get("week").equals(1)){
						ViewData +="<td class=\"sun\">";
					}else if(b.get("week").equals(7)){
						ViewData +="<td class=\"sat\">";
					}else{
						ViewData +="<td class=\"mini\">";
					}
					ViewData +="<span><a href=\"/frontier/pc/calendar/"+topForm.calendarDay+"\" title=\""+sb+"\">";
					ViewData +=b.get("day");
					ViewData +="</s:link>";
					ViewData +="</span>";
				}
			}else{
				if(b.get("today")!=null){
					ViewData +="<td class=\"today\">";
				}else if(b.get("before")!=null){
					ViewData +="<td class=\"before\">";
				}else if(b.get("next")!=null){
					ViewData +="<td class=\"next\">";
				}else if(b.get("week").equals(1)){
					ViewData +="<td class=\"sun\">";
				}else if(b.get("week").equals(7)){
					ViewData +="<td class=\"sat\">";
				}else{
					ViewData +="<td class=\"mini\">";
				}
				ViewData +="<span>";
				ViewData +=b.get("day");
				ViewData +="</span>";
			}
			ViewData +="</td>";
			if(b.get("week").equals(7)){
				ViewData +="</tr><tr>";
			}
			cnt++;
			if(cnt==data.size()){
				for(int i=0;i<7-Integer.parseInt(b.get("week").toString());i++){
					ViewData +="<td class=\"next\"><span>"+(i+1)+"</span></td>";
				}
			}
		}
		ViewData +="</tr>";
		return ViewData;
	}

	//ｽｹｼﾞｭｰﾙ
	private String makeViewDataSchedule(List<BeanMap> data){
		if(data!=null){
			for(BeanMap b:data){
				if(yyyymmdd==null) {
					yyyymmdd =b.get("scheduledate");
					ViewData +="<span style=\"font-weight:bold;\">"+b.get("scheduledate")+"の予定</span>";
				}else if(!yyyymmdd.equals(b.get("scheduledate"))){
					yyyymmdd =b.get("scheduledate");
					ViewData +="<span style=\"font-weight:bold;\">"+b.get("scheduledate")+"の予定</span>";
				}
				//ｽｹｼﾞｭｰﾙ名
				if(b.get("title")!=null){
					b.put("title",reStr(b.get("title").toString()));
					if(b.get("starttime")==null && b.get("endtime") ==null){
					}else if(b.get("starttime")!=null && b.get("endtime")==null){
							b.put("title",b.get("starttime")+"&nbsp;"+b.get("title").toString());
					}else if(b.get("starttime")==null && b.get("endtime")!=null){
						b.put("title","～"+b.get("endtime")+"&nbsp;"+b.get("title").toString());
					}else{
						b.put("title",b.get("starttime").toString()+b.get("endtime")+"&nbsp;"+b.get("title").toString());
					}
				}
				//ニックネーム
				if(b.get("nickname")!=null){
					b.put("nickname",reStr(b.get("nickname").toString()));
				}
				//詳細
				if(b.get("detail")!=null){
					b.put("detail",reStr(b.get("detail").toString()));
				}
				ViewData +="<div>";
				if(b.get("entry").equals("1")){
					ViewData +="<img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/ico05-001.gif\"/>&nbsp;<a href=\"/frontier/pc/com/event/view/"+b.get("cid")+"/"+b.get("bbsid")+"\" title=\"("+b.get("nickname")+")&#13;"+b.get("detail")+"\" style=\"color:#"+b.get("titlecolor")+";\">"+b.get("title")+"</a>";
				}else if(b.get("entry").equals("2")){
					ViewData +="<img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/calendar2.png\"/>&nbsp;<a title=\"("+b.get("nickname")+")&#13;"+b.get("detail")+"\" style=\"color:#"+b.get("titlecolor")+";\" href=\"javascript:void(0)\" onClick=\"MM_openBrWindow('/frontier/pc/calendar/view/"+b.get("sno")+"/"+b.get("cid")+"/1','','width=760,height=640,toolbar=no,scrollbars=yes,left=10,top=10')\">"+b.get("title")+"</a>";
				}
				ViewData +="</div>";
			}
		}

		return ViewData;
	}
	//あなたの更新履歴
	private String makeViewDataMyUpdate(List<BeanMap> data){
		if(data!=null){
			ViewData += "<div id=\"listBoxMyUpdate\">";
			for(BeanMap b:data){
				//ﾄﾋﾟｯｸ名
				if(b.get("title")!=null){
					b.put("title",reStr(b.get("title").toString()));
				}
				//タイトル
				if(b.get("LTitle")!=null){
					b.put("LTitle",reStr(b.get("LTitle").toString()));
				}
				//コメント数
				if(b.get("comments")!=null){
					count = (Long)b.get("comments");
					if(count>0){
						b.put("title",b.get("title")+"("+b.get("comments")+")");
					}
				}
				ViewData += "<div>";
				if(b.get("type").equals("1")){
					ViewData += "<img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/camera-deji1-silver.gif\"/><a href=\"/frontier/pc/photo/view/"+b.get("mid")+"/"+b.get("ano")+"\" title=\""+b.get("LTitle")+"\">"+b.get("title")+"</a>";
				}else if(b.get("type").equals("2")){
					ViewData += "<img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/book_blue.gif\"/><a href=\"/frontier/pc/diary/view/"+b.get("diaryid")+"/"+b.get("ent").toString().substring(0,7)+"/"+userInfoDto.memberId+"\" class=\"diary\" title=\""+b.get("LTitle")+"\">"+b.get("title")+"</a>";
				}
				ViewData += "</div>";
			}
			ViewData +="</div>";
		}
		return ViewData;
	}

	//コミュニティ一覧
	private String makeViewDataCommunity(List<BeanMap> data){
		cnt = 0;
		if(data!=null){

			ViewData = "<div id=\"listBoxCommunity\">";
			ViewData +=	"<table>";
			for(BeanMap b:data){
				//表示画像パス変換処理
				if(b.get("pic")==null||b.get("pic")==""){
					b.put("pic","/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/noimg60.gif");
				}else if(b.get("pic").equals("")){
					b.put("pic","/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/noimg60.gif");
				}else{
					b.put("pic",appDefDto.FP_CMN_CONTENTS_ROOT+b.get("pic").toString().replace("dir", "pic60"));
				}
				//コミュニティ名
				if(b.get("title")!=null){
					b.put("title",reStr(b.get("title").toString()));
				}
				if(cnt%3==0){
					ViewData += "<tr>";
				}
				ViewData += "<td align=\"center\">";
				ViewData += "<a href=\"/frontier/pc/com/top/"+b.get("cid")+"\" title=\""+b.get("title")+"("+b.get("memcnt")+")"+"\"><img src=\""+b.get("pic")+"\" alt=\""+b.get("title")+"("+b.get("memcnt")+")"+"\"/></a>";
				ViewData += "</td>";
				if(cnt%3==2){
					ViewData += "</tr>";
				}else if(data.size()-cnt==1){
					if(data.size()%3==1){
						ViewData += "<td>&nbsp;</td><td>&nbsp;</td></tr>";
					}else if(data.size()%3==2){
						ViewData += "<td>&nbsp;</td></tr>";
					}
				}
				cnt++;
			}
			ViewData += "</table>";
			ViewData += "</div>";
		}
		return ViewData;
	}

	//同志最新日記
	private String makeViewDataDiary(List<BeanMap> data){
		cnt = 0;
		if(data!=null){
			ViewData +="<table border=\"0\">";
			for(BeanMap b:data){
				//ﾄﾋﾟｯｸ名
				if(b.get("title")!=null){
					b.put("title",reStr(b.get("title").toString()));
				}
				//タイトル
				if(b.get("LTitle")!=null){
					b.put("LTitle",reStr(b.get("LTitle").toString()));
				}
				//ニックネーム
				if(b.get("nickname")!=null){
					b.put("nickname",reStr(b.get("nickname").toString()));
				}
				if(cnt==0){
					ViewData +="<tr class=\"contCategory\">";
					if(topForm.sortcd.equals("01")){
						ViewData +="<th style=\"width:10%;\"><u>更新日</u><font color=\"red\"><span>▲</span></font></th>";
						ViewData +="<th style=\"width:10%;\"><u class=\"link\" onclick=\"j$('#diaryBody').load('/frontier/pc/ajax/top/loadData/"+topForm.viewcnt+"/diaryUpdateSort/02?"+CmnUtility.getRandomString(10)+"');\">登録日</u></th>";
					}else{
						ViewData +="<th style=\"width:10%;\"><u class=\"link\" onclick=\"j$('#diaryBody').load('/frontier/pc/ajax/top/loadData/"+topForm.viewcnt+"/diaryUpdateSort/01?"+CmnUtility.getRandomString(10)+"');\">更新日</u></th>";
						ViewData +="<th style=\"width:10%;\"><u>登録日</u><font color=\"red\"><span>▲</span></font></th>";
					}
					ViewData +="<th style=\"width:80%;\">ﾀｲﾄﾙ&nbsp;/&nbsp;ﾆｯｸﾈｰﾑ</th>";
					ViewData +="</tr>";
				}
				ViewData +="<tr>";
				ViewData +="<td align=\"right\">"+b.get("entdatesla")+"</td>";
				ViewData +="<td align=\"right\">"+b.get("entdate2sla")+"</td>";
				ViewData +="<td><a href=\"/frontier/pc/diary/view/"+b.get("diaryid")+"/"+b.get("yyyymmdd")+"/"+b.get("mid")+"\" title=\""+b.get("LTitle")+"\">"+b.get("title")+"&nbsp;("+b.get("count")+")"+"</a>&nbsp;(<a href=\"/frontier/pc/mem/"+b.get("mid")+"\" title=\""+b.get("nickname")+"\">"+b.get("nickname")+"</a>)</td>";
				ViewData +="</tr>";
				cnt++;
			}
			ViewData +="</table>";
		}
		return ViewData;
	}

	//F Shout情報
	private String makeViewDataFShout(List<BeanMap> data){
		//Frontierユーザ管理情報を取得する
		frontierUserManagement = commonService.getFrontierUserManagement(userInfoDto.memberId);
		//FShout　もっと読む数初期化
		topForm.setFsCntResult = 0;
		//pgtype "0":一覧、"2":自分宛
		String pgtype = "";
		if(topForm.type.equals("FShout")){
			pgtype = "0";
		}else if(topForm.type.equals("FShoutToMe")){
			pgtype = "2";
		}
		//表示されていないFShout件数（残りがあれば「もっと読む」表示、用）
		// F Shoutデータ取得(もっと読むを押したときにデータがあるか)
		topForm.fsCntResult = fshoutService.selMyFShoutTopList(pgtype,userInfoDto.memberId,topForm.ukey,appDefDto.FP_MY_FSHOUTLIST_PGMAX,topForm.defFShoutViewCnt==0?1:topForm.defFShoutViewCnt).size();
		//fsoffset セット（「もっと読む」にパラメータとして付与）
		if(topForm.fsCntResult > 0){
			topForm.setFsCntResult = topForm.fsoffset + appDefDto.FP_MY_FSHOUTLIST_PGMAX;
		}
		if(data!=null && data.size()>0){
			if(data!=null && data.size()>0){
				resetResults(data);
				resetResultsAlt(data);
				resetRe(data);
				//相手に確認を求めるチェック（本文）
				confirmResults(fsNewList,0);
				//相手から確認を要求されるチェック（本文）
				confirmResults(fsNewList,1);
				if(topForm.fsListFlg==0 && topForm.fstype==null){
					//初期表示
					topForm.fstype = "fsList";
				}
			if(topForm.fsConfirmMe>0 && topForm.fstype.equals("fsList")){
				ViewData += "<div id=\"confirmMeArea\">";
				ViewData += "<div style=\"width:80%; background-color:#ffffcc; text-align:center; margin-left:auto; margin-right:auto; border-style:solid; border-width:1px; border-color:#ff0000; padding-top:5px; padding-bottom:5px;\">";
				ViewData += "<span style=\"color:#ff0000; font-weight:bold; text-decoration:underline; cursor:pointer;\" onclick=\"cngFsList("+appDefDto.FP_MY_FSHOUTLIST_PGMAX+",'fsListToMe');\">あなた宛の投稿があります</span>";
				ViewData += "</div>";
				ViewData += "</div>";
			}
			//最新コメント表示エリア
			ViewData += "<div id=\"fsdivNew\"></div>";
			ViewData += "<div id=\"fsdivTop\">";
			for(BeanMap b:data){
				//ニックネーム
				if(b.get("nickname")!=null){
					b.put("nickname",reStr(b.get("nickname").toString()));
				}
				//コメント
				if(b.get("comment")!=null){
					b.put("comment",reStr(b.get("comment").toString()));
				}
				//コメントセット
				String commentallAlt = b.get("comment").toString();
				//ツールチップ
				if(b.get("comment")!=null){
					b.put("commentall",reStr(b.get("comment").toString().replace("\\","\\\\").replace("'", "\\'")));
				}
				//ツールチップ（Alt表示用）
				if(b.get("comment")!=null){
					commentallAlt = replaceFSCommentAlt(commentallAlt);
					// 20091215\を削る処理を取り除いた
					b.put("commentallAlt",reStr(commentallAlt));
				}
				//表示画像パス変換処理
				if(b.get("membertype").equals("0")){
					//自Frontier
					if(b.get("photo")==null||b.get("photo")==""){
						//写真がない
					}else if(b.get("photo").equals("")){
						//写真がない
					}else{
						//写真がある
						b.put("photo",appDefDto.FP_CMN_CONTENTS_ROOT+b.get("photo").toString().replace("dir", "pic42"));
					}
				}else{
					//他Frontier
					if(b.get("fpic")==null||b.get("fpic")==""){
						//写真がない
					}else if(b.get("fpic").equals("")){
						//写真がない
					}else{
						//写真がある
						b.put("fpic",b.get("fpic").toString().replace("dir", "pic42"));
					}
				}
				ViewData += "<div class=\"fshoutAreaHead\">";
				ViewData += "<div class=\"fshoutAreaTop fshoutAreaTopLine\">";
					ViewData += "<div class=\"bodycontentsArea bodycontentsAreaTop\">";
					if(b.get("confirmyou").equals("1")){
						ViewData += "<table style=\"width:100%; background-color:#ccffcc;\">";
					} else if(b.get("confirmme").equals("1")){
						ViewData += "<table style=\"width:100%; background-color:#ffcccc;\">";
					} else {
						ViewData += "<table style=\"width:100%;\">";
					}
					ViewData += "<tr>";
					ViewData += "<td class=\"leftArea leftAreaTop\">";
					ViewData += "<ul>";
					ViewData += "<li class=\"leftNickname\">";
					if(b.get("membertype").equals("0")){
						//自Frontier
						if(b.get("photo")==null||b.get("photo")==""||b.get("photo").equals("")){
							//写真がない
							ViewData += "<a href=\"/frontier/pc/fshout/list/"+b.get("mid")+"/1\"><img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/noimg42.gif\"  alt=\""+b.get("nickname")+"\" /></a>";
						}else{
							//写真がある
							ViewData += "<a href=\"/frontier/pc/fshout/list/"+b.get("mid")+"/1\"><img src=\""+b.get("photo")+"\"  alt=\""+b.get("nickname")+"\" /></a>";
						}
					}else{
						//他Frontier
						if(b.get("fpic")==null||b.get("fpic")==""||b.get("fpic").equals("")){
							//写真がない
							if(frontierUserManagement.frontierdomain.equals(b.get("frontierdomain")))
								ViewData += "<a href=\"http://"+b.get("frontierdomain")+"/frontier/pc/fshout/list/"+b.get("fid")+"/1\"><img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/noimg42.gif\"  alt=\""+b.get("fnickname")+"\" /></a>";
							else{
								ViewData += "<a href=\"http://"+b.get("frontierdomain")+"/frontier/pc/openid/auth?cid="+b.get("fid")+"&gm=mv&openid="+frontierUserManagement.frontierdomain+"/frontier/pc/openidserver\"><img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/noimg42.gif\"  alt=\""+b.get("fnickname")+"\" /></a>";
							}
						}else{
							//写真がある
							if(frontierUserManagement.frontierdomain.equals(b.get("frontierdomain")))
								ViewData += "<a href=\"http://"+b.get("frontierdomain")+"/frontier/pc/fshout/list/"+b.get("fid")+"/1\"><img src=\""+b.get("fpic")+"\"  alt=\""+b.get("fnickname")+"\" /></a>";
							else{
								ViewData += "<a href=\"http://"+b.get("frontierdomain")+"/frontier/pc/openid/auth?cid="+b.get("fid")+"&gm=mv&openid="+frontierUserManagement.frontierdomain+"/frontier/pc/openidserver\"><img src=\""+b.get("fpic")+"\"  alt=\""+b.get("fnickname")+"\" /></a>";
							}
						}
					}

					if(b.get("twitter").equals("1")){
						ViewData += "<div style=\"margin-top:5px; text-align:left;\"><img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/twitter.gif\" title=\"Twitter\" alt=\"Twitter\"/ style=\"display:inline;\"></div>";
					}
					ViewData += "</li>";
					ViewData += "</ul>";
					ViewData += "</td>";
					ViewData += "<td class=\"rightArea rightAreaTop\">";
					ViewData += "<div class=\"bodyFSArea\">";
					ViewData += "<ul>";
					ViewData += "<li>";
					ViewData += "<table class=\"nmArea\"><tr>";
					if(b.get("membertype").equals("0")){
						//自Frontier
						ViewData += "<td class=\"nmspace\"><span class=\"fsSentences\"><a href=\"/frontier/pc/fshout/list/"+b.get("mid")+"/1\">"+b.get("nickname")+"</a></span></td>";
					}else{
						//他Frontier
						if(frontierUserManagement.frontierdomain.equals(b.get("frontierdomain"))){
							ViewData += "<td class=\"nmspace\"><span class=\"fsSentences\"><a href=\"http://"+frontierUserManagement.frontierdomain+"/frontier/pc/fshout/list/"+b.get("fid")+"/1\">"+b.get("fnickname")+"</a></span></td>";
						}else{
							ViewData += "<td class=\"nmspace\"><span class=\"fsSentences\"><a href=\"http://"+b.get("frontierdomain")+"/frontier/pc/openid/auth?cid="+b.get("fid")+"&gm=mv&openid="+frontierUserManagement.frontierdomain+"/frontier/pc/openidserver\">"+b.get("fnickname")+"</a></span></td>";
						}
					}
					if(b.get("confirmme").equals("1")){
						ViewData += "<td class=\"markspace\"><img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/icon_warning.png\" title=\"内容確認\" alt=\"内容確認\" style=\"cursor:pointer;\" onclick=\"chkConfirm('"+b.get("mid")+"',"+b.get("no")+","+appDefDto.FP_MY_FSHOUTLIST_PGMAX+",this,'"+appDefDto.FP_CMN_HOST_NAME+"','"+appDefDto.FP_CMN_COLOR_TYPE+"')\" /><td>";
					}else if(b.get("confirmyou").equals("2") || b.get("confirmme").equals("2")){
						ViewData += "<td class=\"markspace\"><img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/arrow_tick.png\" title=\"確認済み\" alt=\"確認済み\"/><td>";
					}else{
						ViewData += "<td class=\"markspace\"><td>";
					}
					ViewData += "</tr></table>";
					ViewData += "</li>";
					ViewData += "<li>";
					ViewData += "<span class=\"fsSentences\">"+b.get("viewComment")+"</span>";
					ViewData += "</li>";
					ViewData += "<li>";
					ViewData += "<span class=\"date\">"+b.get("entdate")+"</span>";
					if(b.get("membertype").equals("0")){
						//自Frontier
						ViewData += "<span class=\"dateArea\"><img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/link_s.png\" alt=\"RT\" title=\"RT\" onclick=\"onSetFocus('rt','"+b.get("nickname")+"','"+b.get("viewCommentAlt")+"',"+b.get("substrmid")+",'"+b.get("frontierdomain")+"','"+b.get("mid")+":"+b.get("no")+"');\" style=\"cursor:pointer;\" /></span>";
						if(b.get("mid").equals(userInfoDto.memberId)){
							ViewData += "<span class=\"dateArea\"><img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/ico_ashcan1_9.gif\" onclick=\"fncConfirm('"+b.get("mid")+"',"+b.get("no")+",fsCnt,this);\" title=\"削除\" alt=\"削除\" style=\"cursor:pointer;\"/></span>";
						}else{
							ViewData += "<span class=\"dateArea\"><img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/arrow_redo_s.png\" onclick=\"onSetFocus('redo','"+b.get("nickname")+"','"+b.get("viewCommentRe")+"',"+b.get("substrmid")+",'"+b.get("frontierdomain")+"','"+b.get("mid")+":"+b.get("no")+"');\" title=\"返信\" alt=\"返信\" style=\"cursor:pointer;\"/></span>";
						}
					}else{
						//他Frontier
						ViewData += "<span class=\"dateArea\"><img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/link_s.png\" alt=\"RT\" title=\"RT\" onclick=\"onSetFocus('rt','"+b.get("fnickname")+"','"+b.get("viewCommentAlt")+"',"+b.get("substrfid")+",'"+b.get("frontierdomain")+"','"+b.get("mid")+":"+b.get("no")+"');\" style=\"cursor:pointer;\" /></span>";
						if(b.get("fid").equals(userInfoDto.memberId)){
							ViewData += "<span class=\"dateArea\"><img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/ico_ashcan1_9.gif\" onclick=\"fncConfirm('"+b.get("fid")+"',"+b.get("no")+",fsCnt,this);\" title=\"削除\" alt=\"削除\" style=\"cursor:pointer;\"/></span>";
						}else{
							ViewData += "<span class=\"dateArea\"><img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/arrow_redo_s.png\" onclick=\"onSetFocus('redo','"+b.get("fnickname")+"','"+b.get("viewCommentRe")+"',"+b.get("substrfid")+",'"+b.get("frontierdomain")+"','"+b.get("mid")+":"+b.get("no")+"');\" title=\"返信\" alt=\"返信\" style=\"cursor:pointer;\"/></span>";
						}
					}

					ViewData += "</li>";
					ViewData += "<li style=\"margin-top:5px;\">";
					ViewData += "<span class=\"fsSentences\">公開範囲：";
					if(b.get("pubLevel").equals("0")){
						ViewData += "<span>外部</span>&nbsp;/&nbsp;<span>Frontier Net</span>&nbsp;/&nbsp;<span>ﾏｲFrontier</span>&nbsp;/&nbsp;<span>ｸﾞﾙｰﾌﾟ</span></span>";
					}else if(b.get("pubLevel").equals("1")){
						ViewData += "<span class=\"pubLeveltxt\">外部</span>&nbsp;/&nbsp;<span>Frontier Net</span>&nbsp;/&nbsp;<span>ﾏｲFrontier</span>&nbsp;/&nbsp;<span>ｸﾞﾙｰﾌﾟ</span></span>";
					}else if(b.get("pubLevel").equals("2")){
						ViewData += "<span class=\"pubLeveltxt\">外部</span>&nbsp;/&nbsp;<span class=\"pubLeveltxt\">Frontier Net</span>&nbsp;/&nbsp;<span>ﾏｲFrontier</span>&nbsp;/&nbsp;<span>ｸﾞﾙｰﾌﾟ</span></span>";
					}else if(b.get("pubLevel").equals("3")){
						ViewData += "<span class=\"pubLeveltxt\">外部</span>&nbsp;/&nbsp;<span class=\"pubLeveltxt\">Frontier Net</span>&nbsp;/&nbsp;<span class=\"pubLeveltxt\">ﾏｲFrontier</span>&nbsp;/&nbsp;<span>ｸﾞﾙｰﾌﾟ</span></span>";
					}
					ViewData += "</li></ul></div></td></tr></table></div></div></div>";
				}
				ViewData +="</table>";
			}
			ViewData += "</div>";
			ViewData += "<div id=\"fsdivBottom\">";
			ViewData += "</div>";
			ViewData += "<div id=\"morelink\" style=\"width:100%; text-align:center; margin-left:auto; margin-right:auto; border-style:none none none none; border-width:1px; padding-top:5px; padding-bottom:5px; margin-top:5px;\">";
			if(topForm.fsCntResult>0){
				ViewData += "<span style=\"text-decoration:underline; color:#0000ff; cursor:pointer;\" onclick=\"moreFshout("+topForm.setFsCntResult+")\">もっと読む</span>";
			}else{
				ViewData += "<span style=\"color:#cccccc;\"\">もっと読む</span>";
			}
			ViewData += "</div>";
			ViewData += "<span></span>";
		}else{
			//最新コメント表示エリア
			ViewData += "<div id=\"fsdivNew\"></div>";
			ViewData += "<div id=\"fsdivTop\">";
			ViewData += "</div>";
			ViewData += "<div id=\"fsdivBottom\">";
			ViewData += "</div>";
			ViewData += "<div id=\"morelink\" style=\"width:100%; text-align:center; margin-left:auto; margin-right:auto; border-style:none none none none; border-width:1px; padding-top:5px; padding-bottom:5px; margin-top:5px;\">";
			ViewData += "<span style=\"color:#cccccc;\"\">もっと読む</span>";
			ViewData += "</div>";
		}
		return ViewData;
	}

	//F Shout情報（もっと読むで表示されるコメント部分のＨＴＭＬ）
	private String makeViewDataFShoutMore(List<BeanMap> data){
		//Frontierユーザ管理情報を取得する
		frontierUserManagement = commonService.getFrontierUserManagement(userInfoDto.memberId);
		//返信用（返信用画像にセット）
		resetRe(data);
		if(data!=null && data.size()>0){
			resetResults(data);
			//相手に確認を求めるチェック（本文）
			confirmResults(fsNewList,0);
			//相手から確認を要求されるチェック（本文）
			confirmResults(fsNewList,1);
			if(topForm.fsListFlg==0 && topForm.fstype==null){
				//初期表示
				topForm.fstype = "fsList";
			}
			for(BeanMap b:data){
				//ニックネーム
				if(b.get("nickname")!=null){
					b.put("nickname",reStr(b.get("nickname").toString()));
				}
				//コメント
				if(b.get("comment")!=null){
					b.put("comment",reStr(b.get("comment").toString()));
				}
				//コメントセット
				String commentallAlt = b.get("comment").toString();
				//ツールチップ
				if(b.get("comment")!=null){
					b.put("commentall",reStr(b.get("comment").toString().replace("\\","\\\\").replace("'", "\\'")));
				}
				//ツールチップ（Alt表示用）
				if(b.get("comment")!=null){
					commentallAlt = replaceFSCommentAlt(commentallAlt);
					// 20091215\を削る処理を取り除いた
					b.put("commentallAlt",reStr(commentallAlt));
				}
				//表示画像パス変換処理
				if(b.get("membertype").equals("0")){
					//自Frontier
					if(b.get("photo")==null||b.get("photo")==""){
						//写真がない
					}else if(b.get("photo").equals("")){
						//写真がない
					}else{
						//写真がある
						b.put("photo",appDefDto.FP_CMN_CONTENTS_ROOT+b.get("photo").toString().replace("dir", "pic42"));
					}
				}else{
					//他Frontier
					if(b.get("fpic")==null||b.get("fpic")==""){
						//写真がない
					}else if(b.get("fpic").equals("")){
						//写真がない
					}else{
						//写真がある
						b.put("fpic",b.get("fpic").toString().replace("dir", "pic42"));
					}
				}
				ViewData += "<div class=\"fshoutAreaHead\">";
				ViewData += "<div class=\"fshoutAreaTop fshoutAreaTopLine\">";
					ViewData += "<div class=\"bodycontentsArea bodycontentsAreaTop\">";
					if(b.get("confirmyou").equals("1")){
						ViewData += "<table style=\"width:100%; background-color:#ccffcc;\">";
					} else if(b.get("confirmme").equals("1")){
						ViewData += "<table style=\"width:100%; background-color:#ffcccc;\">";
					} else {
						ViewData += "<table style=\"width:100%;\">";
					}
					ViewData += "<tr>";
					ViewData += "<td class=\"leftArea leftAreaTop\">";
					ViewData += "<ul>";
					ViewData += "<li class=\"leftNickname\">";
					if(b.get("membertype").equals("0")){
						//自Frontier
						if(b.get("photo")==null||b.get("photo")==""||b.get("photo").equals("")){
							//写真がない
							ViewData += "<a href=\"/frontier/pc/fshout/list/"+b.get("mid")+"/1\"><img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/noimg42.gif\"  alt=\""+b.get("nickname")+"\" /></a>";
						}else{
							//写真がある
							ViewData += "<a href=\"/frontier/pc/fshout/list/"+b.get("mid")+"/1\"><img src=\""+b.get("photo")+"\"  alt=\""+b.get("nickname")+"\" /></a>";
						}
					}else{
						//他Frontier
						if(b.get("fpic")==null||b.get("fpic")==""||b.get("fpic").equals("")){
							//写真がない
							if(frontierUserManagement.frontierdomain.equals(b.get("frontierdomain")))
								ViewData += "<a href=\"http://"+b.get("frontierdomain")+"/frontier/pc/fshout/list/"+b.get("fid")+"/1\"><img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/noimg42.gif\"  alt=\""+b.get("fnickname")+"\" /></a>";
							else{
								ViewData += "<a href=\"http://"+b.get("frontierdomain")+"/frontier/pc/openid/auth?cid="+b.get("fid")+"&gm=mv&openid="+frontierUserManagement.frontierdomain+"/frontier/pc/openidserver\"><img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/noimg42.gif\"  alt=\""+b.get("fnickname")+"\" /></a>";
							}
						}else{
							//写真がある
							if(frontierUserManagement.frontierdomain.equals(b.get("frontierdomain")))
								ViewData += "<a href=\"http://"+b.get("frontierdomain")+"/frontier/pc/fshout/list/"+b.get("fid")+"/1\"><img src=\""+b.get("fpic")+"\"  alt=\""+b.get("fnickname")+"\" /></a>";
							else{
								ViewData += "<a href=\"http://"+b.get("frontierdomain")+"/frontier/pc/openid/auth?cid="+b.get("fid")+"&gm=mv&openid="+frontierUserManagement.frontierdomain+"/frontier/pc/openidserver\"><img src=\""+b.get("fpic")+"\"  alt=\""+b.get("fnickname")+"\" /></a>";
							}
						}
					}
					if(b.get("twitter").equals("1")){
					ViewData += "<div style=\"margin-top:5px; text-align:left;\"><img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/twitter.gif\" title=\"Twitter\" alt=\"Twitter\"/ style=\"display:inline;\"></div>";
					}
					ViewData += "</li>";
					ViewData += "</ul>";
					ViewData += "</td>";
					ViewData += "<td class=\"rightArea rightAreaTop\">";
					ViewData += "<div class=\"bodyFSArea\">";
					ViewData += "<ul>";
					ViewData += "<li>";
					ViewData += "<table class=\"nmArea\"><tr>";
					if(b.get("membertype").equals("0")){
						//自Frontier
						ViewData += "<td class=\"nmspace\"><span class=\"fsSentences\"><a href=\"/frontier/pc/fshout/list/"+b.get("mid")+"/1\">"+b.get("nickname")+"</a></span></td>";
					}else{
						//他Frontier
						if(frontierUserManagement.frontierdomain.equals(b.get("frontierdomain"))){
							ViewData += "<td class=\"nmspace\"><span class=\"fsSentences\"><a href=\"http://"+frontierUserManagement.frontierdomain+"/frontier/pc/fshout/list/"+b.get("fid")+"/1\">"+b.get("fnickname")+"</a></span></td>";
						}else{
							ViewData += "<td class=\"nmspace\"><span class=\"fsSentences\"><a href=\"http://"+b.get("frontierdomain")+"/frontier/pc/openid/auth?cid="+b.get("fid")+"&gm=mv&openid="+frontierUserManagement.frontierdomain+"/frontier/pc/openidserver\">"+b.get("fnickname")+"</a></span></td>";
						}
					}
					if(b.get("confirmme").equals("1")){
						ViewData += "<td class=\"markspace\"><img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/icon_warning.png\" title=\"内容確認\" alt=\"内容確認"+b.get("mid")+"\" style=\"cursor:pointer;\" onclick=\"chkConfirm('"+b.get("mid")+"',"+b.get("no")+","+appDefDto.FP_MY_FSHOUTLIST_PGMAX+",this,'"+appDefDto.FP_CMN_HOST_NAME+"','"+appDefDto.FP_CMN_COLOR_TYPE+"')\" /><td>";
					}else if(b.get("confirmyou").equals("2") || b.get("confirmme").equals("2")){
						ViewData += "<td class=\"markspace\"><img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/arrow_tick.png\" title=\"確認済み\" alt=\"確認済み\"/><td>";
					}else{
						ViewData += "<td class=\"markspace\"><td>";
					}
					ViewData += "</tr></table>";
					ViewData += "</li>";
					ViewData += "<li>";
					ViewData += "<span class=\"fsSentences\">"+b.get("viewComment")+"</span>";
					ViewData += "</li>";
					ViewData += "<li>";
					ViewData += "<span class=\"date\">"+b.get("entdate")+"</span>";
					if(b.get("membertype").equals("0")){
						//自Frontier
						ViewData += "<span class=\"dateArea\"><img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/link_s.png\" alt=\"RT\" title=\"RT\" onclick=\"onSetFocus('rt','"+b.get("nickname")+"','"+b.get("comment")+"',"+b.get("substrmid")+",'"+b.get("frontierdomain")+"','"+b.get("mid")+":"+b.get("no")+"');\" style=\"cursor:pointer;\" /></span>";
						if(b.get("mid").equals(userInfoDto.memberId)){
						ViewData += "<span class=\"dateArea\"><img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/ico_ashcan1_9.gif\" onclick=\"fncConfirm('"+b.get("mid")+"',"+b.get("no")+",fsCnt,this);\" title=\"削除\" alt=\"削除\" style=\"cursor:pointer;\"/></span>";
						}else{
						ViewData += "<span class=\"dateArea\"><img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/arrow_redo_s.png\" onclick=\"onSetFocus('redo','"+b.get("nickname")+"','"+b.get("viewCommentRe")+"',"+b.get("substrmid")+",'"+b.get("frontierdomain")+"','"+b.get("mid")+":"+b.get("no")+"');\" title=\"返信\" alt=\"返信\" style=\"cursor:pointer;\"/></span>";
						}
					}else{
						//他Frontier
						ViewData += "<span class=\"dateArea\"><img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/link_s.png\" alt=\"RT\" title=\"RT\" onclick=\"onSetFocus('rt','"+b.get("fnickname")+"','"+b.get("comment")+"',"+b.get("substrfid")+",'"+b.get("frontierdomain")+"','"+b.get("mid")+":"+b.get("no")+"');\" style=\"cursor:pointer;\" /></span>";
						if(b.get("fid").equals(userInfoDto.memberId)){
						ViewData += "<span class=\"dateArea\"><img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/ico_ashcan1_9.gif\" onclick=\"fncConfirm('"+b.get("fid")+"',"+b.get("no")+",fsCnt,this);\" title=\"削除\" alt=\"削除\" style=\"cursor:pointer;\"/></span>";
						}else{
						ViewData += "<span class=\"dateArea\"><img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/arrow_redo_s.png\" onclick=\"onSetFocus('redo','"+b.get("fnickname")+"','"+b.get("viewCommentRe")+"',"+b.get("substrfid")+",'"+b.get("frontierdomain")+"','"+b.get("mid")+":"+b.get("no")+"');\" title=\"返信\" alt=\"返信\" style=\"cursor:pointer;\"/></span>";
						}
					}

					ViewData += "</li>";
					ViewData += "<li style=\"margin-top:5px;\">";
					ViewData += "<span class=\"fsSentences\">公開範囲：";
					if(b.get("pubLevel").equals("0")){
						ViewData += "<span>外部</span>&nbsp;/&nbsp;<span>Frontier Net</span>&nbsp;/&nbsp;<span>ﾏｲFrontier</span>&nbsp;/&nbsp;<span>ｸﾞﾙｰﾌﾟ</span></span>";
					}else if(b.get("pubLevel").equals("1")){
						ViewData += "<span class=\"pubLeveltxt\">外部</span>&nbsp;/&nbsp;<span>Frontier Net</span>&nbsp;/&nbsp;<span>ﾏｲFrontier</span>&nbsp;/&nbsp;<span>ｸﾞﾙｰﾌﾟ</span></span>";
					}else if(b.get("pubLevel").equals("2")){
						ViewData += "<span class=\"pubLeveltxt\">外部</span>&nbsp;/&nbsp;<span class=\"pubLeveltxt\">Frontier Net</span>&nbsp;/&nbsp;<span>ﾏｲFrontier</span>&nbsp;/&nbsp;<span>ｸﾞﾙｰﾌﾟ</span></span>";
					}else if(b.get("pubLevel").equals("3")){
						ViewData += "<span class=\"pubLeveltxt\">外部</span>&nbsp;/&nbsp;<span class=\"pubLeveltxt\">Frontier Net</span>&nbsp;/&nbsp;<span class=\"pubLeveltxt\">ﾏｲFrontier</span>&nbsp;/&nbsp;<span>ｸﾞﾙｰﾌﾟ</span></span>";
					}
					ViewData += "</li></ul></div></td></tr></table></div></div></div>";
			}
			ViewData +="</table>";
		}
		return ViewData;
	}

	//グループ一覧
	private String makeViewDataGroup(List<BeanMap> data){
		if(data!=null){
			ViewData += "<div id=\"listBoxGroup\">";
			ViewData += "<table style=\"width:100%;\" border=\"0\">";
			for(BeanMap b:data){
				//グループ名
				if(b.get("gname")!=null){
					b.put("gname",reStr(b.get("gname").toString()));
				}
				// 表示画像パス変換処理([dir]を置換)
				if(b.get("pic")==null || b.get("pic").equals("")){
					b.put("pic","/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/noimg42.gif");
				}else{
					b.put("pic",b.get("pic").toString().replace("[dir]", "pic42"));
				}
				ViewData += "<tr><td style=\"width:42px;\" align=\"middle\">";
				ViewData += "<a href=\"javascript:void(0);goGroup('/frontier/pc/mlist/group/','"+b.get("frontierdomain")+"','"+b.get("gid")+"','"+userInfoDto.memberId+"');\" title=\"$"+b.get("gname")+"("+b.get("joinnumber")+")\">";
				ViewData += "<img src=\""+b.get("pic")+"\" alt=\""+b.get("gname")+"("+b.get("joinnumber")+")\"/>";
				ViewData += "</a></td>";
				ViewData += "<td><a href=\"javascript:void(0);goGroup('/frontier/pc/mlist/group/','"+b.get("frontierdomain")+"','"+b.get("gid")+"','"+userInfoDto.memberId+"');\" title=\""+b.get("gname")+"("+b.get("joinnumber")+")\">"+b.get("gname")+"("+b.get("joinnumber")+")</a></td>";
				ViewData += "</tr>";
			}
			ViewData +="</table>";
			ViewData +="</div>";
		}
		return ViewData;
	}

	//私がフォロー一覧
	private String makeViewDataFollowyou(List<BeanMap> data,Integer getDefCnt){
		cnt = 0;
		if(data!=null){

		ViewData = "<div id=\"listBoxFollowyou\">";
		ViewData +=	"<table>";
		for(BeanMap b:data){

			//表示画像パス変換処理
			if(b.get("membertype").equals("0")){
				//自Frontier
				if(b.get("pic")==null||b.get("pic")==""){
					//写真がない
					b.put("pic","/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/noimg60.gif");
				}else if(b.get("pic").equals("")){
					//写真がない
					b.put("pic","/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/noimg60.gif");
				}else{
					//写真がある
					b.put("pic",appDefDto.FP_CMN_CONTENTS_ROOT+b.get("pic").toString().replace("dir", "pic60"));
				}
			}else{
				//他Frontier
				if(b.get("fpic")==null||b.get("fpic")==""){
					//写真がない
					b.put("pic","/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/noimg60.gif");
				}else{
					//写真がある
					b.put("pic",b.get("fpic").toString().replace("dir", "pic60"));
				}
			}
			//ニックネーム
			if(b.get("membertype").equals("0")){
				//自Frontier
				if(b.get("nickname")!=null){
					b.put("nickname",reStr(b.get("nickname").toString()));
				}
			}else{
				//他Frontier
				if(b.get("lfnickname")!=null){
					b.put("lfnickname",reStr(b.get("lfnickname").toString()));
				}
			}
			if(cnt%3==0){
				ViewData += "<tr>";
			}
			ViewData += "<td align=\"center\">";
			if(b.get("membertype").equals("0")){
				ViewData += "<a href=\"/frontier/pc/mem/"+b.get("mid")+"\" title=\""+b.get("nickname")+"\"><img src=\""+b.get("pic")+"\" alt=\""+b.get("nickname")+"\"/></a>";
			}else{
				ViewData += "<a href=\"http://"+b.get("frontierdomain")+"/frontier/pc/openid/auth?cid="+b.get("fid")+"&gm=mv&openid="+frontierUserManagement.frontierdomain+"/frontier/pc/openidserver\" title=\""+b.get("lfnickname")+"\"><img src=\""+b.get("pic")+"\" src=\""+b.get("fpic")+"\" alt=\""+b.get("lfnickname")+"\"/></a>";
			}
			ViewData += "</td>";
			if(cnt%3==2){
				ViewData += "</tr>";
			}else if(data.size()-cnt==1){
				if(data.size()%3==1){
					ViewData += "<td>&nbsp;</td><td>&nbsp;</td></tr>";
				}else if(data.size()%3==2){
					ViewData += "<td>&nbsp;</td></tr>";
				}
			}
			cnt++;

			//表示件数の数で終了
			Cnthtml++;
			if(Cnthtml==getDefCnt){
				Cnthtml = 0;
				break;
			}

		}
		ViewData += "</table>";
		ViewData += "</div>";
		}
		return ViewData;
	}

	//私をフォロー一覧
	private String makeViewDataFollowme(List<BeanMap> data){
		cnt = 0;
		if(data!=null){
		ViewData = "<div id=\"listBoxFollowme\">";
		ViewData +=	"<table>";
		for(BeanMap b:data){
			//表示画像パス変換処理
			if(b.get("membertype").equals("0")){
				//自Frontier
				if(b.get("pic")==null||b.get("pic")==""){
					//写真がない
					b.put("pic","/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/noimg60.gif");
				}else if(b.get("pic").equals("")){
					//写真がない
					b.put("pic","/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/noimg60.gif");
				}else{
					//写真がある
					b.put("pic",appDefDto.FP_CMN_CONTENTS_ROOT+b.get("pic").toString().replace("dir", "pic60"));
				}
			}else{
				//他Frontier
				if(b.get("fpic")==null||b.get("fpic")==""){
					//写真がない
					b.put("pic","/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/noimg60.gif");
				}else if(b.get("fpic").equals("")){
					//写真がない
					b.put("pic","/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/noimg60.gif");
				}else{
					//写真がある
					b.put("pic",b.get("fpic").toString().replace("dir", "pic60"));
				}
			}
			//ニックネーム
			if(b.get("nickname")!=null){
				b.put("nickname",reStr(b.get("nickname").toString()));
			}
			if(cnt%3==0){
				ViewData += "<tr>";
			}
			ViewData += "<td align=\"center\">";
			if(b.get("membertype").equals("0")){
				ViewData += "<a href=\"/frontier/pc/mem/"+b.get("mid")+"\" title=\""+b.get("nickname")+"\"><img src=\""+b.get("pic")+"\" alt=\""+b.get("nickname")+"\"/></a>";
			}else{
				ViewData += "<a href=\"http://"+b.get("frontierdomain")+"/frontier/pc/openid/auth?cid="+b.get("fid")+"&gm=mv&openid="+frontierUserManagement.frontierdomain+"/frontier/pc/openidserver\" title=\""+b.get("lfnickname")+"\"><img src=\""+b.get("pic")+"\" src=\""+b.get("fpic")+"\" alt=\""+b.get("lfnickname")+"\"/></a>";
			}
			ViewData += "</td>";
			if(cnt%3==2){
				ViewData += "</tr>";
			}else if(data.size()-cnt==1){
				if(data.size()%3==1){
					ViewData += "<td>&nbsp;</td><td>&nbsp;</td></tr>";
				}else if(data.size()%3==2){
					ViewData += "<td>&nbsp;</td></tr>";
				}
			}
			cnt++;
		}
		ViewData += "</table>";
		ViewData += "</div>";
		}
		return ViewData;
	}

	//カレンダー必須処理
	private void initCalendar(){
		monthResults = calendarService.selScheduleMonthList(userInfoDto.memberId,topForm.calendarDay,groupids(),topForm.defDisptypeCalendar);
		cal = CmnUtility.makeCustomCalendar2(topForm.calendarDay,monthResults);
		ViewData = makeViewDataCalendar(cal);
	}
	//htmlSanitizing+文字コード変換
	private String reStr(String str){
		str = CmnUtility.htmlSanitizing(str);
		return str;
	}

	//ｽｹｼﾞｭｰﾙ日付生成処理
	private void setSchedule(List<BeanMap> lst){
		Calendar cal1 = Calendar.getInstance();
		for(BeanMap b:lst){
			cal1.set(Integer.parseInt(b.get("startyear").toString()), Integer.parseInt(b.get("startmonth").toString())-1,Integer.parseInt(b.get("startday").toString()));
			b.put("scheduledate",b.get("startyear").toString()+"年"+Integer.parseInt(b.get("startmonth").toString())+"月"+Integer.parseInt(b.get("startday").toString())+"日"+"("+youbi(cal1)+")");
		}
	}

	//ｽｹｼﾞｭｰﾙ曜日判定処理
	private String youbi(Calendar cal1){
		youbi = "";
		switch (cal1.get(Calendar.DAY_OF_WEEK)) {  //(8)現在の曜日を取得
			case Calendar.SUNDAY:    youbi = "日"; break;
			case Calendar.MONDAY:    youbi = "月"; break;
			case Calendar.TUESDAY:   youbi = "火"; break;
			case Calendar.WEDNESDAY: youbi = "水"; break;
			case Calendar.THURSDAY:  youbi = "木"; break;
			case Calendar.FRIDAY:    youbi = "金"; break;
			case Calendar.SATURDAY:  youbi = "土"; break;
		}
		return youbi;
	}

	//selectBox共通処理
	private void initSelectBox(){
		maxCnt =31;
		addCnt =1;
		if(
				topForm.type.equals("community")||
				topForm.type.equals("followyou")||
				topForm.type.equals("followme")
		){
			maxCnt =11;
			addCnt = 3;
		}
		if(topForm.type.equals("schedule")){
			dispViewCnt="scheduleCnt";
			dispViewBody="#scheduleBody";
		}else if(topForm.type.equals("myUpdate")){
			dispViewCnt="MyUpdateCnt";
			dispViewBody="#MyUpdateBody";
		}else if(topForm.type.equals("community")){
			dispViewCnt="comCnt";
			dispViewBody="#communityListBody";
		}else if(topForm.type.equals("diaryUpdate")){
			dispViewCnt="diaryCnt";
			dispViewBody="#diaryBody";
		}else if(topForm.type.equals("communityUpdate")){
			dispViewCnt="BbsCnt";
			dispViewBody="#newBbsBody";
		}else if(topForm.type.equals("memberUpdate")){
			dispViewCnt="MemUpdateCnt";
			dispViewBody="#memberPhotoBody";
		}else if(topForm.type.equals("FShout")){
			dispViewCnt="FShoutCnt";
			dispViewBody="#FShoutBody";
		}else if(topForm.type.equals("group")){
			dispViewCnt="groupCnt";
			dispViewBody="#groupListBody";
		}else if(topForm.type.equals("followyou")){
			dispViewCnt="followyouCnt";
			dispViewBody="#followyouListBody";
		}else if(topForm.type.equals("followme")){
			dispViewCnt="followmeCnt";
			dispViewBody="#followmeListBody";
		}
	}

	//本文装飾
	private void resetResults(List<BeanMap> lbm){
		for (int i=0;i<lbm.size();i++){
			//本文を取得
			String reComment = (String)lbm.get(i).get("comment");
			//サニタイジング
			reComment = CmnUtility.htmlSanitizing(reComment);
			//URL化
			reComment = CmnUtility.convURL(reComment);
			//FShoutコメント文字列置換
			reComment = replaceFSComment(reComment);
			//FShoutコメント絵文字置換
			reComment = CmnUtility.replaceEmoji(reComment,appDefDto.FP_CMN_EMOJI_IMG_PATH,appDefDto.FP_CMN_EMOJI_XML_PATH);
			//BeanMapへ格納
			lbm.get(i).put("viewComment", reComment);
		}
	}

	//本文装飾(ALT表示)
	private void resetResultsAlt(List<BeanMap> lbm){
		for (int i=0;i<lbm.size();i++){
			//本文を取得
			String reComment = (String)lbm.get(i).get("comment");
			//サニタイジング
			reComment = CmnUtility.htmlSanitizing(reComment);
			reComment = reComment.replace("\\", "\\\\");
			//BeanMapへ格納
			lbm.get(i).put("viewCommentAlt", reComment);
		}
	}

	//コメント文装飾
	private void resetRe(List<BeanMap> lbm){
		for (int i=0;i<lbm.size();i++){
			//本文を取得
			String reComment = (String)lbm.get(i).get("comment");
			//サニタイジング
			reComment = CmnUtility.htmlSanitizing(reComment);
			//サニタイジング
			reComment = CmnUtility.htmlSanitizing(reComment);
			//絵文字変換
			reComment = CmnUtility.replaceEmoji(reComment,appDefDto.FP_CMN_EMOJI_IMG_PATH,appDefDto.FP_CMN_EMOJI_XML_PATH);
			//変換※その１：一度「'」を「\'」とする
			reComment = reComment.replace("'", "\\'");
			//FShoutコメント文字列置換
			reComment = replaceFSCommentAlt(reComment);
			//「\」->「\\」変換
			reComment = reComment.replace("\\", "\\\\");
			//変換※その２：直前で「\\'」となるので「\'」に変換
			reComment = reComment.replace("\\\\'", "\\'");
			//BeanMapへ格納
			lbm.get(i).put("viewCommentRe", reComment);
		}
	}

	//FShoutコメント文字列置換（twitter）
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
			String getMid = "";
			String FShout = "";
			String getFrontierDomain = "";
			String myFrontierDomain = appDefDto.FP_CMN_HOST_NAME;
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
				//「\」対応※注意：Top.Action側とは異なる
				FShout = FShout.replace("\\","\\\\");
				Matcher matcher = convURLLinkPtn.matcher(FShout);
				if(profileList.get(0).get("membertype").toString().equals("0")){
					//自Frontier
					FShout = matcher.replaceAll("<a href=\\\\\"/frontier/pc/fshout/list/"+getMid+"/1\\\\\" title=\\\\\"$0\\\\\">$0</a>");
				}else if(profileList.get(0).get("membertype").toString().equals("1")){
					//他Frontier
					if(myFrontierDomain.equals(getFrontierDomain)){
						FShout = matcher.replaceAll("<a href=\\\\\"http://"+myFrontierDomain+"/frontier/pc/fshout/list/"+getMid+"/1\\\\\" title=\\\\\"$0\\\\\">$0</a>");
					} else {
						FShout = matcher.replaceAll("<a href=\\\\\"http://"+getFrontierDomain+"/frontier/pc/openid/auth?cid="+getMid+"&gm=mv&openid="+myFrontierDomain+"/frontier/pc/openidserver\\\\\" title=\\\\\"$0\\\\\">$0</a>");
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

	/**
	 * .load
	 * @return　String
	 */
	//F Shoutコメント登録（※Twitter登録）（twitter）
	@Execute(validator=false)
	public String loadDataIns(){
		String decComment = "";
		// Twitterへ登録時の結果取得用変数
		Status rtnStts = null;
		// コメント取得
		String getCmnt = topForm.fscomment;
		// Twitterへ登録した時に採番されたステータスIDセット用変数
		Object rtnSid = null;
		// リプライ元のID(Twitterの場合は[statusid]、Frontierの場合は[mid:no]で設定)
		String replyId = topForm.VVRepId;
		// -------------------- //
		// リプライ元IDチェック //
		// -------------------- //
		// セット用変数
		String getMid = null;
		String getNo  = null;
		String getSid = null;
		// 値が設定されているか
		if(replyId != null && !replyId.equals("")){
			// 「:」が含まれているか
			if(replyId.indexOf(":") != -1){
				// 含まれていればFShout
				String[] replyIdList = replyId.split(":");
				// 配列化してセット
				getMid = replyIdList[0];
				getNo  = replyIdList[1];
			} else {
				// 含まれていなければTwitter
				getSid = replyId;
			}
		}
		// -------------------- //
		// リプライ元IDチェック //
		// -------------------- //

		//文字化け対応。デコードする。
		try {
			getCmnt = URLDecoder.decode(getCmnt, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//「Twitterのみ」「F Shout + Twitter」にチェックが入っているか
		if(topForm.fscheck!=0){
			// Twitterへの登録処理
			twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
			String twitCmnt = getCmnt;
			// ========================== //
			//   ◆Twitterへの投稿処理◆  //
			// ========================== //
			// Twitterアカウントを持つメンバーのIDをTwitterアカウント名に変換
			twitCmnt = chgFidtoTid(twitCmnt);
			// 140文字で切り取り
			twitCmnt = new DecorationUtility().cutString(twitCmnt,140);
			// Twitterへ登録
			try {
				rtnStts = new TwitterUtility().entryStatus(twitter, twitCmnt,(Object)getSid);
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
		//改行を取り除く
		decComment = getCmnt.replaceAll("\r","").replaceAll("\n","");
		//FrontierShoutに登録
		if(topForm.fscheck!=1){
			fshoutService.insFSComment(userInfoDto.memberId,decComment,topForm.fscheck,topForm.fskoukaihani,topForm.fsConfirm,(Object)getNo,getMid,rtnSid);
		}
		//登録後、HTMLを作成
		fsNewList = topService.selFShoutList(userInfoDto.memberId,topForm.defFShoutViewCnt==0?1:topForm.defFShoutViewCnt,1,topForm.fsoffset);
		if(fsNewList.size()==0){
			fsNewList = topService.selFShoutListMem(userInfoDto.memberId,topForm.defFShoutViewCnt==0?1:topForm.defFShoutViewCnt,1);
		}
		return null;
	}

	//TOPページでの削除（twitter）
	@Execute(validator=false)
	public String loadDataDel() throws IOException, Exception{
		List<BeanMap> lb;    // データ格納用変数
		String twitFlg = null;      // Twitterへも投稿したか
		String twitStatusid = null; // Twitterへ投稿した際のステータスID
		// メンバーID、NOよりF Shoutデータ取得
		lb = fshoutService.selFShoutOne(userInfoDto.memberId,topForm.no);
		// データが1件だけなら値をセット、それ以外ならスルー
		if(lb.size() == 1){
			if(lb.get(0).get("twitter") != null){
				twitFlg = lb.get(0).get("twitter").toString();
			}
			if(lb.get(0).get("statusid") != null){
				twitStatusid = lb.get(0).get("statusid").toString();
			}
		}
		// FrontierShout削除
		fshoutService.delFSComment(userInfoDto.memberId,topForm.no);
		// ツイート削除
		if(twitFlg.equals("1") && twitStatusid != null && !twitStatusid.equals("")){
			// Twitterへ投稿したフラグが立っていて、ステータスIDも取得出来たらツイート削除処理
			// Twitter、Frontierからツイート削除
			delTweetTandF(twitStatusid);
		}
		return null;
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

	//グループのメンバーIDリスト取得
	private List<Object> groupids(){
		List<BeanMap> GroupList = new ArrayList<BeanMap>();
		//グループ一覧データ取得
		GroupList = commonService.getMidList("1",userInfoDto.memberId);
		//同志リスト格納用変数
		List<Object> glist = new ArrayList<Object>();
		if(GroupList.size() > 0){
			for(BeanMap f:GroupList){
				glist.add(f.get("mid"));
			}
		} else {
			glist.add("");
		}
		return glist;
	}

	//相手から確認を要求されたコメントの確認
	@Execute(validator=false)
	public String loadDataChkFsComment() throws IOException, Exception{
		//更新
		topService.updConfirm(topForm.mid,topForm.no,userInfoDto.memberId);
		//HTMLタグをつっこんでHTML表示
		CmnUtility.ResponseWrite(makeViewDataFSAlertArea(),"text/html","windows-31J","Shift_JIS");
		return null;
	}

	//相手から確認を要求されたコメントの確認
	@Execute(validator=false)
	public String loadDataFsCofirmMe(){
		//HTMLタグをつっこんでHTML表示
		CmnUtility.ResponseWrite(makeViewDataFSAlertArea(),"text/html","windows-31J","Shift_JIS");
		return null;
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
			if(type==0){
				//■　相手に確認を求める
				//相手に確認を求めるフラグ
				String confirmyou = "0";
				if(
					getdemandflg.equals("1") &&
					getconfirmflg.equals("0") &&
					getmid.equals(userInfoDto.memberId) &&
					chkfstagflg.equals("1") &&
					!getprofilemid.equals(userInfoDto.memberId)
				){
					//確認要求フラグ="1"
					//確認フラグ="0"
					//自分のメンバーＩＤ
					//コメントの頭がメンバーのＩＤ
					//タグのメンバーが自分以外
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
				if(
					getdemandflg.equals("1") &&
					getconfirmflg.equals("0") &&
					!getmid.equals(userInfoDto.memberId) &&
					chkfstagflg.equals("1") &&
					getprofilemid.equals(userInfoDto.memberId)
				){
					//確認要求フラグ="1"
					//確認フラグ="0"
					//自分のメンバーＩＤ
					//コメントの頭がメンバーのＩＤ
					//タグのメンバーが自分
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


	//FShoutもっと読む
	@Execute(validator=false)
	public String loadDataMore(){
		// F Shoutデータ取得(未表示分のFShoutを取得)
		//pgtype "0":一覧、"2":自分宛
		String pgtype = "";
		if(topForm.fsListFlg==0){
			pgtype = "0";
		}else if(topForm.fsListFlg==1){
			pgtype = "2";
		}
		fsNewList = fshoutService.selMyFShoutTopList(pgtype,userInfoDto.memberId,topForm.ukey,topForm.setFsCntResult,topForm.defFShoutViewCnt==0?1:topForm.defFShoutViewCnt);
		//offsetの追加
		topForm.fsoffset = topForm.fsoffset + appDefDto.FP_MY_FSHOUTLIST_PGMAX;
		//表示件数
		topForm.fsHyoujiCnt = topForm.fsoffset + appDefDto.FP_MY_FSHOUTLIST_PGMAX;
		//HTMLタグをつっこんでHTML表示
		CmnUtility.ResponseWrite(makeViewDataFShoutMore(fsNewList),"text/html","windows-31J","Shift_JIS");
		return null;
	}
	//FShoutもっと読む
	@Execute(validator=false)
	public String loadDataMoreLink(){
		//HTMLタグをつっこんでHTML表示
		CmnUtility.ResponseWrite(makeViewDataFShoutMorelink(),"text/html","windows-31J","Shift_JIS");
		return null;
	}

	//FShout更新（一覧）
	@Execute(validator=false)
	public String loadDataUpdate(){
		//取得した最新時間よりも新しいFShoutコメントの取得
		// F Shoutデータ取得(未表示分のFShoutを取得)
		//pgtype "0":一覧、"2":自分宛
		String pgtype = "";
		if(topForm.fsListFlg==0){
			pgtype = "0";
		}else if(topForm.fsListFlg==1){
			pgtype = "2";
		}
		//最新のFShout取得
		fsNewList = topService.selMyFShoutTopList(pgtype,userInfoDto.memberId,topForm.ukey,topForm.setFsCntResult,topForm.defFShoutViewCnt==0?1:topForm.defFShoutViewCnt);
		if(fsNewList.size()>0){
			//HTMLタグをつっこんでHTML表示
			CmnUtility.ResponseWrite(makeViewDataFShoutMore(fsNewList),"text/html","windows-31J","Shift_JIS");
			//FShoutの最新時間取得
			fsMaxList = topService.selMyFShoutTopList(pgtype,userInfoDto.memberId,topForm.ukey,topForm.setFsCntResult,topForm.defFShoutViewCnt==0?1:topForm.defFShoutViewCnt);
			if(fsMaxList.size()>0){
				topForm.ukey = fsMaxList.get(0).get("ukey").toString();
			}
		}
		return null;
	}

	//F Shout情報（もっと読む）
	private String makeViewDataFShoutMorelink(){
		//pgtype "0":一覧、"2":自分宛
		String pgtype = "";
		if(topForm.fsListFlg==0){
			pgtype = "0";
		}else if(topForm.fsListFlg==1){
			pgtype = "2";
		}
		//offsetとデフォルト表示件数の合計（…によって、先にFShoutコメントがあるかどうか）
		Integer sumfsoffset = topForm.fsoffset+appDefDto.FP_MY_FSHOUTLIST_PGMAX;
		//表示されていないFShout件数（残りがあれば「もっと読む」表示、用）
		// F Shoutデータ取得(もっと読むを押したときにデータがあるか)
		topForm.fsCntResult = fshoutService.selMyFShoutTopList(pgtype,userInfoDto.memberId,topForm.ukey,sumfsoffset,topForm.defFShoutViewCnt==0?1:topForm.defFShoutViewCnt).size();
		//もっと読むリンク表示ＦＬＧ
		Integer fsLinkMoreFlg = 0;
		if(topForm.fsCntResult>0){
			//もっと読むリンク表示
			fsLinkMoreFlg = 1;
			//fsoffset セット（「もっと読む」にパラメータとして付与）
			topForm.setFsCntResult = topForm.fsoffset + appDefDto.FP_MY_FSHOUTLIST_PGMAX;
		}else{
			//もっと読むリンク非表示
			fsLinkMoreFlg = 0;
			//fsoffset セット（「もっと読む」がないのでリセット）
			topForm.setFsCntResult = 0;
		}
		ViewData += "<div id=\"morelink\" style=\"width:100%; text-align:center; margin-left:auto; margin-right:auto; border-style:none none none none; border-width:1px;\">";
		if(fsLinkMoreFlg==1){
			ViewData += "<span style=\"text-decoration:underline; color:#0000ff; cursor:pointer;\" onclick=\"moreFshout("+topForm.setFsCntResult+")\">もっと読む</span>";
		}else{
			ViewData += "<span style=\"color:#cccccc;\">もっと読む</span>";
		}
		ViewData += "</div>";
		return ViewData;
	}

	//F Shout情報（「あなた宛のコメントがあります」エリアの制御）
	private String makeViewDataFSAlertArea(){
		//確認すべきFShoutコメント数取得
		topForm.fsConfirmMe = fshoutService.selFShoutCnt("3",userInfoDto.memberId);
		if(topForm.fsConfirmMe>0 && topForm.fstype.equals("fsList")){
			ViewData += "<div id=\"confirmMeArea\" style=\"margin-top:0px; padding:0;\">";
			ViewData += "<div style=\"width:80%; background-color:#ffffcc; text-align:center; margin-left:auto; margin-right:auto; border-style:solid; border-width:1px; border-color:#ff0000; padding-top:5px; padding-bottom:5px;\">";
			ViewData += "<span style=\"color:#ff0000; font-weight:bold; text-decoration:underline; cursor:pointer;\" onclick=\"cngFsList("+appDefDto.FP_MY_FSHOUTLIST_PGMAX+",'fsListToMe');\">あなた宛の投稿があります</span>";
			ViewData += "</div>";
			ViewData += "</div>";
		}else{
			ViewData += "<div id=\"confirmMeArea\" style=\"margin-top:0px; padding:0;\">";
			ViewData += "</div>";
		}
		return ViewData;
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
			//final Pattern convURLLinkPtn = Pattern.compile("(#)\\S+");
			final Pattern convURLLinkPtn = Pattern.compile("(#)[a-zA-Z_]+");
			//「\」対応
			Matcher matcher = convURLLinkPtn.matcher(FShout);
			//自Frontier
			//FShout = matcher.replaceAll("<a href=\"/frontier/pc/fshout/list/"+txt+"/1\" title=\"$0\">$0</a>");
			FShout = matcher.replaceAll("<span onclick=\"SearchTwitter('#"+partStr+"');\" class=\"twLink\">$0</span>");
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
			FShout = matcher.replaceAll("<span onclick=\"chgListTop('"+partStr+"');\" class=\"twLink\">@"+partStr+"</span>");
			//文字列連結
			m.appendReplacement(sb, FShout);
		}
		//残りの文字列連結
		m.appendTail(sb);
		return sb.toString();
	}

// ================================================================================================== //
// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ここから斉川追加分↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓     //
// ================================================================================================== //
	@Execute(validator=false)
	public String AAInitAccount(){
		//現在使用中のTwitterアカウントを取得する
		List<BeanMap> lbm = oauthConsumerService.getTokens(userInfoDto.memberId, appDefDto.TWI_PROVIDER, "1", null);

		// HTML生成
		CmnUtility.ResponseWrite(mkHtmlAccount(lbm),"text/html","windows-31J","Shift_JIS");
		return null;
	}

	@Execute(validator=false)
	public String AAmakeAccountList(){
		TwitterUtility tu = new TwitterUtility();

		//Twitterアカウントリスト(画面表示用)
		List<BeanMap> viewLbm = new ArrayList<BeanMap>();

		//登録してあるTwitterアカウントを取得する
		List<BeanMap> lbm = oauthConsumerService.getTokens(userInfoDto.memberId, appDefDto.TWI_PROVIDER, null, null);

		//コンシューマ情報の取得
		List<BeanMap> conLbm = oauthConsumerService.getConsumerInfo(appDefDto.TWI_PROVIDER);

		//アカウントの有効チェックを行なう
		for(int i=0;i<lbm.size();i++){
			//Oauth認証情報の設定
			Twitter privateTwitter = twitterService.setTwitter(userInfoDto.memberId, lbm.get(i).get("accesstoken").toString(), lbm.get(i).get("tokensecret").toString(), conLbm);

			try {
				//Twitterからユーザ情報を取得
				tu.getHomeTimeLine(privateTwitter, 1,1);

				//有効なアカウントを画面表示用のリストに追加
				viewLbm.add(lbm.get(i));
			} catch (TwitterException e) {
				//エラー原因を取得
				String errCode = tu.getErrorTwitter(e);
				//認証が無効の場合
				if(errCode.equals("")){
					//アカウント無効化
					oauthConsumerService.delTokensReal((Integer)lbm.get(i).get("twituserid"));
				}
				continue;
			}
		}

		// HTML生成
		CmnUtility.ResponseWrite(mkHtmlAccountList(viewLbm),"text/html","windows-31J","Shift_JIS");

		return null;
	}

	/**
	 * Twitterのアカウントを切り替る
	 * @return
	 */
	@Execute(validator=false)
	public String AAChangeAccount(){
		//アカウントを切り替える
		oauthConsumerService.changeTwitterAccount(userInfoDto.memberId, Integer.parseInt(topForm.oldUserId), Integer.parseInt(topForm.userId));

		// HTML生成
		CmnUtility.ResponseWrite(mkHtmlChkCangeAccount(),"text/html","windows-31J","Shift_JIS");

		return null;
	}

	/**
	 * 初期表示時のアカウント表示欄のHTML作成処理
	 * @param lbm 現在使用中のアカウント情報を含むList
	 * @return 生成されたHTML
	 */
	private String mkHtmlAccount(List<BeanMap> lbm){
		String ViewData = "<span style=\"vertical-align:bottom;width:20%;\">"+"アカウント&nbsp;&nbsp;"+"</span>";
		ViewData += "<img src=\""+lbm.get(0).get("pic")+"\" width=\"18\" height=\"18\" style=\"display:inline;\"/>&nbsp;&nbsp;";
		ViewData += "<span style=\"vertical-align:bottom;width:70%;text-decoration:underline;cursor:pointer!important;\" onClick=\"chgAccountTop();\">"+lbm.get(0).get("screenname")+"</span>";
		ViewData += "<div class=\"twitAccount\" id=\"twitterAccountArea\"></div>";
		return ViewData;

	}

	/**
	 * Twitterアカウント切り替え用の一覧を表示するHTMLを作成
	 * @param lbm
	 * @return
	 */
	private String mkHtmlAccountList(List<BeanMap> lbm){
		String ViewData = "";

		ViewData += "<div id=\"childAccountDiv\" style=\"position:absolute;text-align:left;background-color:#ffffff;width:182px;\">";
		ViewData += "<table cellspacing=\"0\" cellpadding=\"0\">";

		for (int i=0;i<lbm.size();i++){
			//取得したアカウントの一覧を作成する
			ViewData += "<tr";

			if(lbm.get(i).get("useflg").equals("1")){
				//現在使用中のアカウントは背景色を変える
				ViewData += " id=\"nowUseAccountColor\"";
			}

			ViewData += ">";
			ViewData += "<td><img src=\""+lbm.get(i).get("pic")+"\" width=\"18\" height=\"18\" style=\"display:inline;\"/></td>";
			ViewData += "<td><span class=\"accountName\" onClick=\"chgAccount('"+topForm.userId+"','"+lbm.get(i).get("twituserid")+"','"+lbm.get(i).get("screenname").toString()+"');\">"+lbm.get(i).get("screenname")+"</span></td>";
			ViewData += "</tr>";
		}

		ViewData += "</table>";
		ViewData += "<div id=\"lineAccountDiv\"></div>";
		ViewData += "<div id=\"linkAccountDiv\"><a href=\"/frontier/pc/profile1/\">アカウントを追加する</a></div>";

		ViewData += "</div>";

		return ViewData;
	}

	/**
	 * アカウント切替時にプロフィール及びTL部分との同期を取るために空の画像を設定。
	 * そこでonloadし、同期処理を実施する。
	 */
	private String mkHtmlChkCangeAccount(){
		String rtnVal = "";
		rtnVal += "<img src=\"/images/dummy.png\" onload=\"closeAccountTop();readTwAccount();readTwTop('"+topForm.myScreenName+"');\"/>";
		return rtnVal;
	}

// ================================================================================================== //
// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ここから高際が新規作成↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ //
// ================================================================================================== //
	// ************************************************ //
	//            ！！！！注意！！！！                  //
	// ※※※要修正※※※と言うコメント付きのものは     //
	// 高際が手を入れてない、もしくは修正し切れていない //
	// メソッドです。                                   //
	// ************************************************ //
	// ==================================== //
	//       ▼プロフィール表示処理▼       //
	// ==================================== //
	// クリックユーザのプロフィールHTML出力
	@Execute(validator=false)
	public String AAMkProfile(){
		twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
		User twInfo = null;
		// 関係チェック変数
		String chkUandI = "";
		// screenName取得(値が設定されて居なければ自分のscreenNameを設定する)
		if(topForm.screenName==null || topForm.screenName.equals("")){
			topForm.screenName = topForm.myScreenName;
		}
		// ユーザの情報を取得
		try {
			twInfo = new TwitterUtility().getUserInfo(twitter,topForm.screenName);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ユーザと自分との関係をチェック
		try {
			// --------------------- //
			// 1:フォローしている    //
			// 2:フォローされている  //
			// 3:相互フォロー        //
			// --------------------- //
			chkUandI = new TwitterUtility().checkFollow(twitter, topForm.myScreenName, twInfo.getScreenName());
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// HTML生成
		CmnUtility.ResponseWrite(mkHtmlPrf(chkUandI,twInfo,topForm.VVSelMenuBtn),"text/html","windows-31J","Shift_JIS");
		return null;
	}

	// ==================================== //
	//      ▼検索フィールド表示処理▼      //
	// ==================================== //
	// クリックユーザのプロフィールHTML出力
	@Execute(validator=false)
	public String AAMkTwitSearchArea(){
		//登録してあるTwitterアカウントを取得する
		List<BeanMap> lbm = oauthConsumerService.getTokens(userInfoDto.memberId, appDefDto.TWI_PROVIDER, null, null);
		// HTML生成
		CmnUtility.ResponseWrite(mkHtmlTwitSearchArea(lbm),"text/html","windows-31J","Shift_JIS");
		return null;
	}

	// ==================================== //
	//  ▼日付クリック(ツイート表示)処理▼  //
	// ==================================== //
	// クリックツイートHTML出力
	@Execute(validator=false)
	public String AAMkTweetPrf(){
		twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
		// Twitterデータ取得用変数
		Status stts = null;
		// 取得したsidをlong型へ変換
		long lsid = Long.parseLong(topForm.VVTargetSid);
		// ツイート情報を取得
		try {
			stts = new TwitterUtility().getOneStatus(twitter,lsid);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// HTML生成
		CmnUtility.ResponseWrite(mkHtmlPrfTweet(chgSttsToBmp(stts)),"text/html","windows-31J","Shift_JIS");
		return null;
	}
	
	// ==================================== //
	//  ▼初期、TLボタン押下時の表示処理▼  //
	// ==================================== //
	// TLデータ取得、ツイート一覧のHTML出力
	@Execute(validator=false)
	public String AAMkTl(){
		topForm.gapFlg    = false; // ギャップ判定用変数
		topForm.tlMoreFlg = false; // もっと読む判定変数
		topForm.cntGapCnt = 0;     // twitterのギャップ用変数・初期値設定
		twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
		// Twitterデータ取得用変数
		List<Status> twitterdb = null;
		// Twitterデータ取得用変数(もっと読む用)
		List<Status> twitterdb2 = null;
		// Twitterデータ取得
		try {
			twitterdb = new TwitterUtility().getHomeTimeLine(twitter, 1, appDefDto.ST_MY_TL_LIST_MAX);
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// 上記で取得したデータが1件以上あれば判定
		if(twitterdb!=null){
			// 取得データの一番最後のステータスIDをセット(もっと読む用)
			topForm.twMaxOldId = (Long) twitterdb.get(twitterdb.size()-1).getId();
			// 取得データの一番最初のステータスIDをセット(更新、自動更新用)
			topForm.twMaxNewId = (Long) twitterdb.get(0).getId();
			// Twitterデータ取得
			try {
				twitterdb2 = new TwitterUtility().getHomeTimeLineOld(twitter, topForm.twMaxOldId,1,1);
			} catch (TwitterException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// -------------------- //
			// ◆もっと読む判定処理 //
			// -------------------- //
			// 上記で取得したデータが1件以上あれば判定
			if(twitterdb2!=null){
				// もっと読むリンクを表示するか判定する変数に結果をセット
				topForm.tlMoreFlg = chgObjToBoolean((Object)twitterdb2.size());
			}
		}

		// TL情報の詰め直し(画面の変数にセット)
		getTlList = chgTwiDtLBM(twitterdb);
		// HTML出力
		CmnUtility.ResponseWrite(mkHtmlTl(getTlList),"text/html","windows-31J","Shift_JIS");
		return null;
	}

	// ==================================== //
	//     ▼(ボタン)@クリック時の処理▼    //
	// ==================================== //
	// TLデータ取得、メンションのHTML出力
	@Execute(validator=false)
	public String AAMkMention(){
		twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
		// Twitterデータ取得用変数
		List<Status> twitterlb = null;
		// Twitterデータ取得
		try {
			twitterlb = new TwitterUtility().getMentions(twitter, 1, appDefDto.ST_MY_TL_LIST_MAX);
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// データ登録
		insTweetToFrontier("1",twitterlb);
		// メンション情報の取得
		getTlList = twitterService.selMentionOrFavoriteList(null,null,Integer.valueOf(topForm.userId).intValue(),"1",null,appDefDto.ST_MY_TL_LIST_MAX,0);
		// 1件以上あれば処理を実行
		if(getTlList.size() > 0){
			// 取得データを既読に更新
			twitterService.updPackageReadflg(getTlList,userInfoDto.memberId);
			// 画面の変数にセット
			getTlList = chgFTwiDtLBM(getTlList);
			// 取得データの一番最後のステータスIDをセット(もっと読む用)
			topForm.twMaxOldId = (Long) getTlList.get(getTlList.size()-1).get("id");
			// 取得データの一番最初のステータスIDをセット(更新、自動更新用)
			topForm.twMaxNewId = (Long) getTlList.get(0).get("id");
			// もっと読む、ギャップ判定
			chkMoreGapMention(topForm.twMaxOldId);
		} else {
			// データが取得できなければフラグをクリア
			topForm.gapFlg = false;
			topForm.tlMoreFlg = false;
		}
		// HTML作成(メンション作成)
		CmnUtility.ResponseWrite(mkHtmlMention(getTlList),"text/html","windows-31J","Shift_JIS");
		return null;
	}

	// ==================================== //
	//     ▼(ボタン)★クリック時の処理▼   //
	// ==================================== //
	// TLデータ取得、お気に入りのHTML出力
	@Execute(validator=false)
	public String AAMkFavorite(){
		twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
		// Twitterデータ取得用変数
		List<Status> twitterlb = null;
		// Twitterデータ取得
		try {
			// お気に入り検索(※現在は取得件数が20件固定)
			twitterlb = new TwitterUtility().getFavorite(twitter,topForm.myScreenName,1);
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// データ登録
		insTweetToFrontier("2",twitterlb);
		// お気に入り情報の取得
		getTlList = twitterService.selMentionOrFavoriteList(null,null,Integer.valueOf(topForm.userId).intValue(),null,"1",appDefDto.ST_MY_TL_LIST_MAX,0);
		// 1件以上あれば処理を実行
		if(getTlList.size() > 0){
			// 取得データを既読に更新
			twitterService.updPackageReadflg(getTlList,userInfoDto.memberId);
			// 画面の変数にセット
			getTlList = chgFTwiDtLBM(getTlList);
			// 取得データの一番最後のステータスIDをセット(もっと読む用)
			topForm.twMaxOldId = (Long) getTlList.get(getTlList.size()-1).get("id");
			// 取得データの一番最初のステータスIDをセット(更新、自動更新用)
			topForm.twMaxNewId = (Long) getTlList.get(0).get("id");
			// もっと読む、ギャップ判定
			chkMoreGapFavorite(topForm.twMaxOldId);
		} else {
			// データが取得できなければフラグをクリア
			topForm.gapFlg = false;
			topForm.tlMoreFlg = false;
		}
		// HTML作成(お気に入り作成)
		CmnUtility.ResponseWrite(mkHtmlFavorite(getTlList),"text/html","windows-31J","Shift_JIS");
		return null;
	}

	// ==================================== //
	//  ▼(ボタン)リストクリック時の処理▼  //
	// ==================================== //
	// TLデータ取得、ツイート一覧のHTML出力
	@Execute(validator=false)
	public String AAMkMyList(){
		topForm.gapFlg    = false; // ギャップ判定用変数
		topForm.tlMoreFlg = false; // もっと読む判定変数
		// Listのカーソルをlong型へ変換
		long lcs = Long.parseLong(topForm.VVLCursor);
		twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
		// Twitterデータ取得用変数
		PagableResponseList<UserList> twitterdb = null;
		// Twitterデータ取得
		try {
			twitterdb = new TwitterUtility().getMadeList(twitter,topForm.screenName,lcs);
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// HTML出力
		CmnUtility.ResponseWrite(mkHtmlList("0",twitterdb),"text/html","windows-31J","Shift_JIS");
		return null;
	}

	// ==================================== //
	//  ▼(リンク)リストクリック時の処理▼  //
	// ==================================== //
	// TLデータ取得、ツイート一覧のHTML出力
	@Execute(validator=false)
	public String AAMkList(){
		topForm.gapFlg    = false; // ギャップ判定用変数
		topForm.tlMoreFlg = false; // もっと読む判定変数
		// Listのカーソルをlong型へ変換
		long lcs = Long.parseLong(topForm.VVLCursor);
		twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
		// Twitterデータ取得用変数
		PagableResponseList<UserList> twitterdb = null;
		// Twitterデータ取得
		try {
			twitterdb = new TwitterUtility().getBelongList(twitter,topForm.screenName,lcs);
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// HTML出力
		CmnUtility.ResponseWrite(mkHtmlList("1",twitterdb),"text/html","windows-31J","Shift_JIS");
		return null;
	}

	// ==================================== //
	// ▼ユーザアカウントクリック時の処理▼ //
	// ==================================== //
	// TLデータ取得、ツイート一覧のHTML出力
	@Execute(validator=false)
	public String AAMkUTl(){
		// 自分、他人のどちらが押されたか判定
		if(topForm.screenName.equals(topForm.myScreenName)){
			// ************* //
			//   ★自分★    //
			// ************* //
			twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
			// Twitterデータ取得用変数
			List<Status> twitterlb = null;
			// Twitterデータ取得
			try {
				twitterlb = new TwitterUtility().getUTimeline(twitter, topForm.myScreenName, 1, appDefDto.ST_MY_TL_LIST_MAX);
			} catch (TwitterException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// データ登録
			insTweetToFrontier("0",twitterlb);
			// 自分のTL情報の取得＆セット
			getTlList = chgFTwiDtLBM(twitterService.selTwitterList(null,Integer.valueOf(topForm.userId).intValue(),appDefDto.ST_MY_TL_LIST_MAX));
			// 取得データの一番最後のステータスIDをセット(もっと読む用)
			topForm.twMaxOldId = (Long) getTlList.get(getTlList.size()-1).get("id");
			// 取得データの一番最初のステータスIDをセット(更新、自動更新用)
			topForm.twMaxNewId = (Long) getTlList.get(0).get("id");
			// もっと読む、ギャップ判定
			chkMoreGapMyTl(topForm.twMaxOldId);
			// HTML作成(自分のTL作成)
			CmnUtility.ResponseWrite(mkHtmlMyTl(getTlList),"text/html","windows-31J","Shift_JIS");
		} else {
			// ************* //
			// ★自分以外★  //
			// ************* //
			twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
			// Twitterデータ取得用変数
			List<Status> twitterdb = null;
			// Twitterデータ取得用変数(もっと読む用)
			List<Status> twitterdb2 = null;
			// Twitterデータ取得
			try {
				twitterdb = new TwitterUtility().getUTimeline(twitter, topForm.screenName, 1, appDefDto.ST_MY_TL_LIST_MAX);
			} catch (TwitterException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// 上記で取得したデータが1件以上あれば判定
			if(twitterdb!=null){
				// 取得データの一番最後のステータスIDをセット(もっと読む用)
				topForm.twMaxOldId = (Long) twitterdb.get(twitterdb.size()-1).getId();
				// 取得データの一番最初のステータスIDをセット(更新、自動更新用)
				topForm.twMaxNewId = (Long) twitterdb.get(0).getId();
				// Twitterデータ取得
				try {
					twitterdb2 = new TwitterUtility().getUTimelineOld(twitter, topForm.screenName,topForm.twMaxOldId,1,1);
				} catch (TwitterException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// -------------------- //
				// ◆もっと読む判定処理 //
				// -------------------- //
				// 上記で取得したデータが1件以上あれば判定
				if(twitterdb2!=null){
					// もっと読むリンクを表示するか判定する変数に結果をセット
					topForm.tlMoreFlg = chgObjToBoolean((Object)twitterdb2.size());
				}
				// TL情報の詰め直し(画面の変数にセット)
				getTlList = chgTwiDtLBM(twitterdb);
				// HTML出力
				CmnUtility.ResponseWrite(mkHtmlTl(getTlList),"text/html","windows-31J","Shift_JIS");
			}
		}
		return null;
	}

	// ==================================== //
	//     ▼リスト名クリック時の処理▼     //
	// ==================================== //
	// TLデータ取得、ツイート一覧のHTML出力
	@Execute(validator=false)
	public String AAMkListTL(){
		topForm.gapFlg    = false; // ギャップ判定用変数
		topForm.tlMoreFlg = false; // もっと読む判定変数
		topForm.cntGapCnt = 0;     // twitterのギャップ用変数・初期値設定
		twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
		// Twitterデータ取得用変数
		List<Status> twitterdb = null;
		// Twitterデータ取得用変数(もっと読む用)
		List<Status> twitterdb2 = null;
		// リストのIDの型変換
		int lid = Integer.valueOf(topForm.VVListId).intValue();
		// Twitterデータ取得
		try {
			twitterdb = new TwitterUtility().getListTimeLine(twitter,topForm.screenName,lid,1, appDefDto.ST_MY_TL_LIST_MAX);
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// 上記で取得したデータが1件以上あれば判定
		if(twitterdb!=null){
			// 取得データの一番最後のステータスIDをセット(もっと読む用)
			topForm.twMaxOldId = (Long) twitterdb.get(twitterdb.size()-1).getId();
			// 取得データの一番最初のステータスIDをセット(更新、自動更新用)
			topForm.twMaxNewId = (Long) twitterdb.get(0).getId();
			// Twitterデータ取得
			try {
				twitterdb2 = new TwitterUtility().getListTimeLineOld(twitter,topForm.screenName,lid,topForm.twMaxOldId,1,1);
			} catch (TwitterException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// -------------------- //
			// ◆もっと読む判定処理 //
			// -------------------- //
			// 上記で取得したデータが1件以上あれば判定
			if(twitterdb2!=null){
				// もっと読むリンクを表示するか判定する変数に結果をセット
				topForm.tlMoreFlg = chgObjToBoolean((Object)twitterdb2.size());
			}
		}

		// TL情報の詰め直し(画面の変数にセット)
		getTlList = chgTwiDtLBM(twitterdb);
		// HTML出力
		CmnUtility.ResponseWrite(mkHtmlListTl(getTlList),"text/html","windows-31J","Shift_JIS");
		return null;
	}

	// ==================================== //
	//         ▼検索結果表示処理▼         //
	// ==================================== //
	// TLデータ取得、ツイート一覧のHTML出力
	@Execute(validator=false)
	public String AAMkSearch(){
		topForm.gapFlg    = false; // ギャップ判定用変数
		topForm.tlMoreFlg = false; // もっと読む判定変数
		String kwdTxt = "";
		twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
		// Twitterデータ取得用変数
		List<Tweet> twitterdb = null;
		// Twitterデータ取得用変数(もっと読む用)
		List<Tweet> twitterdb2 = null;
		// 送信時にエンコードした文字列をデコードする
		try {
			kwdTxt = URLDecoder.decode(topForm.VVKeywd, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// Twitterデータ取得
		try {
			twitterdb = new TwitterUtility().getSearchList(twitter,kwdTxt,1,appDefDto.ST_MY_TL_LIST_MAX);
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// 上記で取得したデータが1件以上あれば判定
		if(twitterdb.size()>0){
			// 取得データの一番最後のステータスIDをセット(もっと読む用)
			topForm.twMaxOldId = (Long) twitterdb.get(twitterdb.size()-1).getId();
			// 取得データの一番最初のステータスIDをセット(更新、自動更新用)
			topForm.twMaxNewId = (Long) twitterdb.get(0).getId();
			// Twitterデータ取得
			try {
				twitterdb2 = new TwitterUtility().getSearchListOld(twitter,kwdTxt,topForm.twMaxOldId,1,1);
			} catch (TwitterException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// -------------------- //
			// ◆もっと読む判定処理 //
			// -------------------- //
			// 上記で取得したデータが1件以上あれば判定
			if(twitterdb2.size()>0){
				// もっと読むリンクを表示するか判定する変数に結果をセット
				topForm.tlMoreFlg = chgObjToBoolean((Object)twitterdb2.size());
			}
		}

		// TL情報の詰め直し(画面の変数にセット)
		getTlList = chgTwiDtLtLBM(twitterdb);
		// HTML出力
		CmnUtility.ResponseWrite(mkHtmlSearch(getTlList,kwdTxt),"text/html","windows-31J","Shift_JIS");
		return null;
	}

	// ==================================== //
	//  ▼検索結果表示処理(FrontierDB版)▼  //
	// ==================================== //
	// TLデータ取得、ツイート一覧のHTML出力
	@Execute(validator=false)
	public String AAMkTwitSearch(){
		String kwdTxt = "";
		// 送信時にエンコードした文字列をデコードする
		try {
			kwdTxt = URLDecoder.decode(topForm.VVKeywd, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 検索文字列の整形
		kwdTxt = twitterService.fixLudiaWord(kwdTxt,topForm.VVAndOr);
		// アカウントリスト判定
		if((topForm.VVAId != null) && (topForm.VVAId.size()==1) && (topForm.VVAId.get(0).toString().equals("null"))){
			topForm.VVAId = null;
		}
		// 検索総数の取得
		long resultCnt = twitterService.cntSearchTwitter(
			userInfoDto.memberId,
			kwdTxt,
			topForm.VVYYYYFrom,
			topForm.VVMMFrom,
			topForm.VVDDFrom,
			topForm.VVYYYYTo,
			topForm.VVMMTo,
			topForm.VVDDTo,
			topForm.VVAId
		);
		// ページ数計算(ページ数より、開始位置を計算)
		topForm.VVOffset = topForm.VVPgcnt * appDefDto.ST_MY_TL_LIST_MAX;
		// 検索
		List<BeanMap> lbm = twitterService.selSearchTwitter(
			userInfoDto.memberId,
			kwdTxt,
			topForm.VVYYYYFrom,
			topForm.VVMMFrom,
			topForm.VVDDFrom,
			topForm.VVYYYYTo,
			topForm.VVMMTo,
			topForm.VVDDTo,
			topForm.VVAId,
			appDefDto.ST_MY_TL_LIST_MAX,
			topForm.VVOffset
		);
		getTlList = chgFTwiDtLBM(lbm);
		// HTML出力
		CmnUtility.ResponseWrite(mkHtmlTwitSearch(resultCnt,getTlList,kwdTxt),"text/html","windows-31J","Shift_JIS");
		return null;
	}

	// ==================================== //
	//      ▼返信元ツイート表示処理▼      //
	// ==================================== //
	// クリックツイートHTML出力
	@Execute(validator=false)
	public String AAMkTweetTL(){
		twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
		// Twitterデータ取得用変数
		Status stts = null;
		// 返信元のステータスIDがあれば取得(-1は未設定)
		if(!topForm.VVTargetSid.equals("-1")){
			// 取得したsidをlong型へ変換
			long lsid = Long.parseLong(topForm.VVTargetSid);
			// ツイート情報を取得
			try {
				stts = new TwitterUtility().getOneStatus(twitter,lsid);
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// HTML生成
		CmnUtility.ResponseWrite(mkHtmlTlTweet(chgSttsToBmp(stts)),"text/html","windows-31J","Shift_JIS");
		return null;
	}


	// ==================================== //
	//    ▼お気に入りの登録・解除処理▼    //
	// ==================================== //
	// 登録
	@Execute(validator=false)
	public String AAInsFavorite(){
		twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
		// ステータスIDをlong型へ変換
		long sid = Long.parseLong(topForm.VVTargetSid);
		// 結果取得用変数
		Status stts = null;
		// Twitterへお気に入りの登録
		try {
			stts = new TwitterUtility().entryFavorite(twitter, sid);
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// ------------ //
		//   登録処理   //
		// ------------ //
		// Twitterで登録出来れば結果が取得出来るので、処理開始
		if(stts != null){
			// Twitterの発言のステータスID
			Object objId = stts.getId();
			long tsid = Long.parseLong(objId.toString());
			// ステータスIDよりTwitterDBに登録されているかチェック
			if(twitterService.cntTwitter(tsid)==0){
				// 登録されていない場合、登録を行う
				twitterService.insTwitter(stts,userInfoDto.memberId);
			}
			// ステータスID、TwitterのユーザIDよりTwitter投稿管理テーブルに登録されているかチェック
			if(twitterService.cntTwitterPostManagement(tsid,Integer.valueOf(topForm.userId).intValue())==0){
				// 登録されていない場合、登録を行う
				twitterService.insTwitterPostManagement(tsid,Integer.valueOf(topForm.userId).intValue(),"1","1","0","0","0",userInfoDto.memberId,"0");
			} else {
				// 登録されている場合、データの更新を行う(お気に入りフラグを立てる)
				twitterService.updTwitterPostManagement(tsid,Integer.valueOf(topForm.userId).intValue(),"1","1","0","0","0",userInfoDto.memberId,null,0,0);
			}
		}

		// 全ての処理が終わったら星マークのイメージを差し替える
		// HTML作成(お気に入り、解除HTMLの作成)
		CmnUtility.ResponseWrite(mkHtmlDelFavorite(topForm.VVTargetSid),"text/html","windows-31J","Shift_JIS");
		return null;
	}

	// 解除
	@Execute(validator=false)
	public String AADelFavorite(){
		twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
		// ステータスIDをlong型へ変換
		long sid = Long.parseLong(topForm.VVTargetSid);
		// 結果取得用変数
		Status stts = null;
		// Twitterへお気に入りの解除
		try {
			stts = new TwitterUtility().deleteFavorite(twitter, sid);
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// ------------ //
		//   解除処理   //
		// ------------ //
		// Twitterで解除出来れば結果が取得出来るので、処理開始
		if(stts != null){
			// Twitterの発言のステータスID
			Object objId = stts.getId();
			long tsid = Long.parseLong(objId.toString());
			// ステータスID、TwitterのユーザIDよりTwitter投稿管理テーブルに登録されているかチェック
			if(twitterService.cntTwitterPostManagement(tsid,Integer.valueOf(topForm.userId).intValue())==0){
				// 登録されていない場合、登録を行う
				twitterService.insTwitterPostManagement(tsid,Integer.valueOf(topForm.userId).intValue(),"1","0","0","0","0",userInfoDto.memberId,"0");
			} else {
				// 登録されている場合、データの更新を行う(お気に入りフラグを落とす)
				twitterService.updTwitterPostManagement(tsid,Integer.valueOf(topForm.userId).intValue(),"1","0","0","0","0",userInfoDto.memberId,null,0,0);
			}
		}

		// 全ての処理が終わったら星マークのイメージを差し替える
		// HTML作成(お気に入り、登録HTMLの作成)
		CmnUtility.ResponseWrite(mkHtmlInsFavorite(topForm.VVTargetSid),"text/html","windows-31J","Shift_JIS");
		return null;
	}

	// ==================================== //
	//      ▼自分のツイートの削除処理▼    //
	// ==================================== //
	@Execute(validator=false)
	public String AADelTweet(){
		// Twitter、Frontierからツイート削除
		delTweetTandF(topForm.VVTargetSid);
		// HTML作成(対象のDIVの表示をNoneにするHTML)
		CmnUtility.ResponseWrite(mkHtmldelTweet(topForm.VVTargetSid),"text/html","windows-31J","Shift_JIS");
		return null;
	}

	// ==================================== //
	//         ▼文字列カウント処理▼       //
	// ==================================== //
	// クリックユーザのプロフィールHTML出力
	@Execute(validator=false)
	public String AATxtCnt(){
		String getTxt = "";
		String chgFtxt = "";
		String chgTtxt = "";
		// 送信時にエンコードした文字列をデコードする
		try {
			getTxt = URLDecoder.decode(topForm.VVFShoutTxt, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// FShout投稿用の文字列に変換(@ -> アカウント名)
		chgFtxt = fshoutService.changeIdToName(getTxt);
		// Twitterアカウントが登録されている場合
		String twiFlg = twitterService.checkUseTwitter(userInfoDto.memberId);
		if(twiFlg.equals("2")){
			// Twitter投稿用の文字列に変換(@ -> アカウント名)
			chgTtxt = twitterService.chgFidtoTid(getTxt);
		}
		// HTML生成
		CmnUtility.ResponseWrite(mkHtmlTxtCnt(chgFtxt,chgTtxt),"text/html","windows-31J","Shift_JIS");
		return null;
	}

	// ==================================== //
	//   ▼新着Mention取得&HTML作成処理▼   //
	// ==================================== //
	// プロフィールとTLのHTML出力後にメニューのメンション(@)の新着数の取得＆HTML出力
	@Execute(validator=false)
	public String AAGetMentionNew(){
		// 自分のプロフィールならば処理実行
		if(topForm.screenName.equals(topForm.myScreenName)){
			// 未読のメンション数の取得
			long cntMentionNew = 0;
			cntMentionNew = twitterService.cntTwitterPostManagement(null,Integer.valueOf(topForm.userId).intValue(),"0",null,null,"1",null);
			// HTML生成
			CmnUtility.ResponseWrite(mkHtmlPrfMenu(cntMentionNew,topForm.VVSelMenuBtn),"text/html","windows-31J","Shift_JIS");
		}
		return null;
	}

	// ==================================== //
	//    ▼更新・定期更新&HTML作成処理▼   //
	// ==================================== //
	// 通常のTL/ユーザのTL
	@Execute(validator=false)
	public String AAGetNewTl(){
		twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
		//ステータスIDをlong型へ変換
		long st = Long.parseLong(topForm.VVMostNewSid);
		// Twitterデータ取得用変数
		List<Status> twitterDt = null;
		// Twitterデータ取得
		try {
			// 通常のTLか、ユーザのTLか判定
			if(topForm.screenName.equals(topForm.myScreenName)){
				// 通常のTL(=選択されているアカウント名が自分のアカウント名と同一)の場合
				twitterDt = new TwitterUtility().getHomeTimeLineNew(twitter, st, 1, appDefDto.ST_LIST_MAX);
			} else {
				// ユーザのTL(=選択されているアカウント名が自分のアカウント名と異なる)の場合
				twitterDt = new TwitterUtility().getUTimelineNew(twitter, topForm.screenName, st, 1, appDefDto.ST_LIST_MAX);
			}
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// 上記で取得したデータが1件以上あれば判定
		if(twitterDt.size() > 0){
			// TL情報の詰め直し(画面の変数にセット)
			getTlList = chgTwiDtLBM(twitterDt);
			// 取得データの一番最初のステータスIDをセット(更新、自動更新用)
			topForm.twMaxNewId = (Long) getTlList.get(0).get("id");
			// HTML出力
			CmnUtility.ResponseWrite(mkHtmlNew(getTlList),"text/html","windows-31J","Shift_JIS");
		}
		return null;
	}

	// 自分のTL
	@Execute(validator=false)
	public String AAGetNewMyTl(){
		topForm.gapFlg = false; // ギャップ判定用変数
		twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
		//ステータスIDをlong型へ変換
		long st = Long.parseLong(topForm.VVMostNewSid);
		long stEnd = 0;
		// Twitterデータ取得用変数
		List<Status> twitterDt = null;
		// Twitterデータ取得
		try {
			twitterDt = new TwitterUtility().getUTimelineNew(twitter, topForm.screenName, st, 1, appDefDto.ST_LIST_MAX);
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// データ登録
		Integer cntTList = insTweetToFrontier("0",twitterDt);
		// -------------------- //
		// ◆ギャップ判定処理   //
		// -------------------- //
		// 取得したデータ数と登録したデータ数が同じだった場合、trueをセット
		if((cntTList != 0) && (twitterDt.size() != 0) && (cntTList == twitterDt.size())){
			topForm.gapFlg = true;
		}
		// 自分のTL情報の取得＆セット(FrontierDBより)
		getTlList = chgFTwiDtLBM(twitterService.selTwitterList(null,st,Integer.valueOf(topForm.userId).intValue(),appDefDto.ST_LIST_MAX));
		// 上記で取得したデータが1件以上あれば判定
		if(getTlList.size() > 0){
			// 取得データの一番最後のステータスIDをセット(ギャップ用)
			stEnd = (Long) getTlList.get(getTlList.size()-1).get("id");
			// 取得データの一番最初のステータスIDをセット(更新、自動更新用)
			topForm.twMaxNewId = (Long) getTlList.get(0).get("id");
		}
		// HTML出力
		CmnUtility.ResponseWrite(mkHtmlNewMy(getTlList,st,stEnd),"text/html","windows-31J","Shift_JIS");
		return null;
	}

	// メンション
	@Execute(validator=false)
	public String AAGetNewMention(){
		topForm.gapFlg = false; // ギャップ判定用変数
		twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
		//ステータスIDをlong型へ変換
		long st = Long.parseLong(topForm.VVMostNewSid);
		long stEnd = 0;
		// Twitterデータ取得用変数
		List<Status> twitterDt = null;
		// Twitterデータ取得
		try {
			twitterDt = new TwitterUtility().getMentionsNew(twitter, st, 1, appDefDto.ST_LIST_MAX);
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// データ登録
		Integer cntTList = insTweetToFrontier("1",twitterDt);
		// -------------------- //
		// ◆ギャップ判定処理   //
		// -------------------- //
		// 取得したデータ数と登録したデータ数が同じだった場合、trueをセット
		if((cntTList != 0) && (twitterDt.size() != 0) && (cntTList == twitterDt.size())){
			topForm.gapFlg = true;
		}
		// メンションの取得＆セット(FrontierDBより)
		getTlList = twitterService.selMentionOrFavoriteList(null,st,Integer.valueOf(topForm.userId).intValue(),"1",null,appDefDto.ST_LIST_MAX,0);
		// 上記で取得したデータが1件以上あれば判定
		if(getTlList.size() > 0){
			// 取得データを既読に更新
			twitterService.updPackageReadflg(getTlList,userInfoDto.memberId);
			// 更新後に詰め直し
			getTlList = chgFTwiDtLBM(getTlList);
			// 取得データの一番最後のステータスIDをセット(ギャップ用)
			stEnd = (Long) getTlList.get(getTlList.size()-1).get("id");
			// 取得データの一番最初のステータスIDをセット(更新、自動更新用)
			topForm.twMaxNewId = (Long) getTlList.get(0).get("id");
		}
		// HTML出力
		CmnUtility.ResponseWrite(mkHtmlNewMention(getTlList,st,stEnd),"text/html","windows-31J","Shift_JIS");
		return null;
	}

	// お気に入り
	@Execute(validator=false)
	public String AAGetNewFavorite() throws IOException, Exception{
		topForm.gapFlg = false; // ギャップ判定用変数
		twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
		//ステータスIDをlong型へ変換
		long st = Long.parseLong(topForm.VVMostNewSid);
		long stEnd = 0;
		// Twitterデータ取得用変数
		List<Map<String,String>> twitterDt = null;
		//Frontierに登録されているtwitterのユーザ情報を取得する
		List<OauthTokensStore> lots = twitterService.selAllTwitterUser(userInfoDto.memberId,appDefDto.TWI_PROVIDER,"0",null,topForm.myScreenName,"1");
		//コンシューマ情報を取得
		List<BeanMap> lbmConsumer = oauthConsumerService.getConsumerInfo(appDefDto.ST_PROVIDER);
		// Twitterデータ取得
		try {
			twitterDt = new TwitterUtility().getFavoriteNew(topForm.screenName, st,appDefDto.TWI_PROVIDER,lbmConsumer,lots.get(0));
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// データ登録
		Integer cntTList = insTweetToFrontierLmss("2",twitterDt);
		// -------------------- //
		// ◆ギャップ判定処理   //
		// -------------------- //
		// 取得したデータ数と登録したデータ数が同じだった場合、trueをセット
		if((cntTList != 0) && (twitterDt.size() != 0) && (cntTList == twitterDt.size())){
			topForm.gapFlg = true;
		}
		// お気に入りの取得＆セット(FrontierDBより)
		getTlList = chgFTwiDtLBM(twitterService.selMentionOrFavoriteList(null,st,Integer.valueOf(topForm.userId).intValue(),null,"1",appDefDto.ST_LIST_MAX,0));
		// 上記で取得したデータが1件以上あれば判定
		if(getTlList.size() > 0){
			// 取得データの一番最後のステータスIDをセット(ギャップ用)
			stEnd = (Long) getTlList.get(getTlList.size()-1).get("id");
			// 取得データの一番最初のステータスIDをセット(更新、自動更新用)
			topForm.twMaxNewId = (Long) getTlList.get(0).get("id");
		}
		// HTML出力
		CmnUtility.ResponseWrite(mkHtmlNewFavorite(getTlList,st,stEnd),"text/html","windows-31J","Shift_JIS");
		return null;
	}

	// 検索結果
	@Execute(validator=false)
	public String AAGetNewSearch(){
		twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
		String kwdTxt = "";
		//ステータスIDをlong型へ変換
		long st = Long.parseLong(topForm.VVMostNewSid);
		// Twitterデータ取得用変数
		List<Tweet> twitterDt = null;
		// 送信時にエンコードした文字列をデコードする
		try {
			kwdTxt = URLDecoder.decode(topForm.VVKeywd, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// Twitterデータ取得
		try {
			twitterDt = new TwitterUtility().getSearchListNew(twitter,kwdTxt,st,1,appDefDto.ST_LIST_MAX);
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// 上記で取得したデータが1件以上あれば判定
		if(twitterDt.size() > 0){
			// TL情報の詰め直し(画面の変数にセット)
			getTlList = chgTwiDtLtLBM(twitterDt);
			// 取得データの一番最初のステータスIDをセット(更新、自動更新用)
			topForm.twMaxNewId = (Long) getTlList.get(0).get("id");
			// HTML出力
			CmnUtility.ResponseWrite(mkHtmlNewSearch(getTlList),"text/html","windows-31J","Shift_JIS");
		}
		return null;
	}

	// リストTL
	@Execute(validator=false)
	public String AAGetNewListTl(){
		twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
		// リストのIDの型変換
		int lid = Integer.valueOf(topForm.VVListId).intValue();
		//ステータスIDをlong型へ変換
		long st = Long.parseLong(topForm.VVMostNewSid);
		// Twitterデータ取得用変数
		List<Status> twitterDt = null;
		// Twitterデータ取得
		try {
			twitterDt = new TwitterUtility().getListTimeLineNew(twitter, topForm.screenName,lid,st, 1, appDefDto.ST_LIST_MAX);
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// 上記で取得したデータが1件以上あれば判定
		if(twitterDt.size() > 0){
			// TL情報の詰め直し(画面の変数にセット)
			getTlList = chgTwiDtLBM(twitterDt);
			// 取得データの一番最初のステータスIDをセット(更新、自動更新用)
			topForm.twMaxNewId = (Long) getTlList.get(0).get("id");
			// HTML出力
			CmnUtility.ResponseWrite(mkHtmlNew(getTlList),"text/html","windows-31J","Shift_JIS");
		}
		return null;
	}

	// ==================================== //
	// ▼もっと読むリンククリック時の処理▼ //
	// ==================================== //
	// 通常のTL/ユーザのTL
	@Execute(validator=false)
	public String AAAddList(){
		topForm.gapFlg    = false; // ギャップ判定用変数
		topForm.tlMoreFlg = false; // もっと読む判定変数
		twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
		//ステータスIDをlong型へ変換
		long st = Long.parseLong(topForm.VVMostOldSid);
		// Twitterデータ取得用変数
		List<Status> twitterdb = null;
		// Twitterデータ取得用変数(もっと読む用)
		List<Status> twitterdb2 = null;

		// Twitterデータ取得
		try {
			// 通常のTLか、ユーザのTLか判定
			if(topForm.screenName.equals(topForm.myScreenName)){
				// 通常のTL(=選択されているアカウント名が自分のアカウント名と同一)の場合
				twitterdb = new TwitterUtility().getHomeTimeLineOld(twitter, st, 1, appDefDto.ST_MY_TL_LIST_MAX);
			} else {
				// ユーザのTL(=選択されているアカウント名が自分のアカウント名と異なる)の場合
				twitterdb = new TwitterUtility().getUTimelineOld(twitter, topForm.screenName, st, 1, appDefDto.ST_MY_TL_LIST_MAX);
			}
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// 上記で取得したデータが1件以上あれば判定
		if(twitterdb!=null){
			// 取得データの一番最後のステータスIDをセット(もっと読む用)
			topForm.twMaxOldId = (Long) twitterdb.get(twitterdb.size()-1).getId();
			// Twitterデータ取得
			try {
				// 通常のTLか、ユーザのTLか判定
				if(topForm.screenName.equals(topForm.myScreenName)){
					// 通常のTL(=選択されているアカウント名が自分のアカウント名と同一)の場合
					twitterdb2 = new TwitterUtility().getHomeTimeLineOld(twitter, topForm.twMaxOldId,1,1);
				} else {
					// ユーザのTL(=選択されているアカウント名が自分のアカウント名と異なる)の場合
					twitterdb2 = new TwitterUtility().getUTimelineOld(twitter, topForm.screenName, topForm.twMaxOldId,1,1);
				}
			} catch (TwitterException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// -------------------- //
			// ◆もっと読む判定処理 //
			// -------------------- //
			// 上記で取得したデータが1件以上あれば判定
			if(twitterdb2!=null){
				// もっと読むリンクを表示するか判定する変数に結果をセット
				topForm.tlMoreFlg = chgObjToBoolean((Object)twitterdb2.size());
			}
		}
		// TL情報の詰め直し(画面の変数にセット)
		getTlList = chgTwiDtLBM(twitterdb);
		// HTML出力
		CmnUtility.ResponseWrite(mkHtmlMore(getTlList),"text/html","windows-31J","Shift_JIS");
		return null;
	}

	// 自分のTL
	@Execute(validator=false)
	public String AAAddMyList(){
		twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
		//ステータスIDをlong型へ変換
		long st = Long.parseLong(topForm.VVMostOldSid);
		// 自分のTL情報の取得＆セット
		getTlList = chgFTwiDtLBM(twitterService.selTwitterList(st,Integer.valueOf(topForm.userId).intValue(),appDefDto.ST_MY_TL_LIST_MAX));
		// 取得データの一番最後のステータスIDをセット(もっと読む用)
		topForm.twMaxOldId = (Long) getTlList.get(getTlList.size()-1).get("id");
		// もっと読む、ギャップ判定
		chkMoreGapMyTl(topForm.twMaxOldId);
		// HTML出力
		CmnUtility.ResponseWrite(mkHtmlMoreMy(getTlList),"text/html","windows-31J","Shift_JIS");
		return null;
	}

	// メンション
	@Execute(validator=false)
	public String AAAddMention(){
		twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
		//ステータスIDをlong型へ変換
		long st = Long.parseLong(topForm.VVMostOldSid);
		// メンション情報の取得＆セット(FrontierDBより)
		getTlList = twitterService.selMentionOrFavoriteList(st,null,Integer.valueOf(topForm.userId).intValue(),"1",null,appDefDto.ST_MY_TL_LIST_MAX,0);
		// 取得データを既読に更新
		twitterService.updPackageReadflg(getTlList,userInfoDto.memberId);
		// 更新後に詰め直し
		getTlList = chgFTwiDtLBM(getTlList);
		// 取得データの一番最後のステータスIDをセット(もっと読む用)
		topForm.twMaxOldId = (Long) getTlList.get(getTlList.size()-1).get("id");
		// もっと読む、ギャップ判定
		chkMoreGapMention(topForm.twMaxOldId);
		// HTML出力
		CmnUtility.ResponseWrite(mkHtmlMoreMention(getTlList),"text/html","windows-31J","Shift_JIS");
		return null;
	}

	// お気に入り
	@Execute(validator=false)
	public String AAAddFavorite() throws IOException, Exception{
		//ステータスIDをlong型へ変換
		long st = Long.parseLong(topForm.VVMostOldSid);
		// お気に入り情報の取得＆セット(FrontierDBより)
		getTlList = chgFTwiDtLBM(twitterService.selMentionOrFavoriteList(st,null,Integer.valueOf(topForm.userId).intValue(),null,"1",appDefDto.ST_MY_TL_LIST_MAX,0));
		// 取得データの一番最後のステータスIDをセット(もっと読む用)
		topForm.twMaxOldId = (Long) getTlList.get(getTlList.size()-1).get("id");
		// もっと読む、ギャップ判定
		chkMoreGapFavorite(topForm.twMaxOldId);
		// HTML出力
		CmnUtility.ResponseWrite(mkHtmlMoreFavorite(getTlList),"text/html","windows-31J","Shift_JIS");
		return null;
	}

	// 検索結果
	@Execute(validator=false)
	public String AAAddSearch(){
		topForm.gapFlg    = false; // ギャップ判定用変数
		topForm.tlMoreFlg = false; // もっと読む判定変数
		String kwdTxt = "";
		twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
		//ステータスIDをlong型へ変換
		long st = Long.parseLong(topForm.VVMostOldSid);
		// Twitterデータ取得用変数
		List<Tweet> twitterdb = null;
		// Twitterデータ取得用変数(もっと読む用)
		List<Tweet> twitterdb2 = null;
		// 送信時にエンコードした文字列をデコードする
		try {
			kwdTxt = URLDecoder.decode(topForm.VVKeywd, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// Twitterデータ取得
		try {
			twitterdb = new TwitterUtility().getSearchListOld(twitter,kwdTxt,st,1,appDefDto.ST_MY_TL_LIST_MAX);
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// 上記で取得したデータが1件以上あれば判定
		if(twitterdb.size()>0){
			// 取得データの一番最後のステータスIDをセット(もっと読む用)
			topForm.twMaxOldId = (Long) twitterdb.get(twitterdb.size()-1).getId();
			// Twitterデータ取得
			try {
				twitterdb2 = new TwitterUtility().getSearchListOld(twitter,kwdTxt,topForm.twMaxOldId,1,1);
			} catch (TwitterException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// -------------------- //
			// ◆もっと読む判定処理 //
			// -------------------- //
			// 上記で取得したデータが1件以上あれば判定
			if(twitterdb2.size()>0){
				// もっと読むリンクを表示するか判定する変数に結果をセット
				topForm.tlMoreFlg = chgObjToBoolean((Object)twitterdb2.size());
			}
		}
		// TL情報の詰め直し(画面の変数にセット)
		getTlList = chgTwiDtLtLBM(twitterdb);
		// HTML出力
		CmnUtility.ResponseWrite(mkHtmlMoreSearch(getTlList),"text/html","windows-31J","Shift_JIS");
		return null;
	}

	// リストTL
	@Execute(validator=false)
	public String AAAddListTl(){
		topForm.gapFlg    = false; // ギャップ判定用変数
		topForm.tlMoreFlg = false; // もっと読む判定変数
		twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
		// リストのIDの型変換
		int lid = Integer.valueOf(topForm.VVListId).intValue();
		//ステータスIDをlong型へ変換
		long st = Long.parseLong(topForm.VVMostOldSid);
		// Twitterデータ取得用変数
		List<Status> twitterdb = null;
		// Twitterデータ取得用変数(もっと読む用)
		List<Status> twitterdb2 = null;

		// Twitterデータ取得
		try {
			twitterdb = new TwitterUtility().getListTimeLineOld(twitter, topForm.screenName,lid, st, 1, appDefDto.ST_MY_TL_LIST_MAX);
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// 上記で取得したデータが1件以上あれば判定
		if(twitterdb!=null){
			// 取得データの一番最後のステータスIDをセット(もっと読む用)
			topForm.twMaxOldId = (Long) twitterdb.get(twitterdb.size()-1).getId();
			// Twitterデータ取得
			try {
				twitterdb2 = new TwitterUtility().getListTimeLineOld(twitter, topForm.screenName,lid, topForm.twMaxOldId,1,1);
			} catch (TwitterException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// -------------------- //
			// ◆もっと読む判定処理 //
			// -------------------- //
			// 上記で取得したデータが1件以上あれば判定
			if(twitterdb2!=null){
				// もっと読むリンクを表示するか判定する変数に結果をセット
				topForm.tlMoreFlg = chgObjToBoolean((Object)twitterdb2.size());
			}
		}
		// TL情報の詰め直し(画面の変数にセット)
		getTlList = chgTwiDtLBM(twitterdb);
		// HTML出力
		CmnUtility.ResponseWrite(mkHtmlMoreListTl(getTlList),"text/html","windows-31J","Shift_JIS");
		return null;
	}

	// ==================================== //
	//     ▼ギャップ取得処理(新着版)▼     //
	// ==================================== //
	// 自分のTL
	@Execute(validator=false)
	public String AAGetGapNewMyTl() throws IOException, Exception{
		topForm.gapFlg = false; // ギャップ判定用変数
		twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
		//ステータスIDをlong型へ変換
		long sidFrom = Long.parseLong(topForm.VVGapSidFrom);
		long sidTo   = Long.parseLong(topForm.VVGapSidTo);
		long sidEnd  = 0;
		// Twitterデータ取得用変数
		List<Status> twitterDt = null;
		// Twitterデータ取得
		try {
			twitterDt = new TwitterUtility().getUTimelineOld(twitter, topForm.screenName, sidTo, 1, appDefDto.ST_LIST_MAX);
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// データ登録
		Integer cntTList = insTweetToFrontier("0",twitterDt);
		// -------------------- //
		// ◆ギャップ判定処理   //
		// -------------------- //
		// 取得したデータ数と登録したデータ数が同じだった場合、trueをセット
		if((cntTList != 0) && (twitterDt.size() != 0) && (cntTList == twitterDt.size())){
			topForm.gapFlg = true;
		}
		// 自分のTL情報の取得＆セット(FrontierDBより)
		getTlList = chgFTwiDtLBM(twitterService.selTwitterList(sidTo,sidFrom,Integer.valueOf(topForm.userId).intValue(),appDefDto.ST_LIST_MAX));
		// 上記で取得したデータが1件以上あれば判定
		if(getTlList.size() > 0){
			// 取得データの一番最後のステータスIDをセット(ギャップ用)
			sidEnd = (Long) getTlList.get(getTlList.size()-1).get("id");
		}
		// HTML出力
		CmnUtility.ResponseWrite(mkHtmlGapNewMyTl(getTlList,sidFrom,sidEnd),"text/html","windows-31J","Shift_JIS");
		return null;
	}

	// メンション
	@Execute(validator=false)
	public String AAGetGapNewMention() throws IOException, Exception{
		topForm.gapFlg = false; // ギャップ判定用変数
		twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
		//ステータスIDをlong型へ変換
		long sidFrom = Long.parseLong(topForm.VVGapSidFrom);
		long sidTo   = Long.parseLong(topForm.VVGapSidTo);
		long sidEnd  = 0;
		// Twitterデータ取得用変数
		List<Status> twitterDt = null;
		// Twitterデータ取得
		try {
			twitterDt = new TwitterUtility().getMentionsOld(twitter, sidTo, 1, appDefDto.ST_LIST_MAX);
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// データ登録
		Integer cntTList = insTweetToFrontier("1",twitterDt);
		// -------------------- //
		// ◆ギャップ判定処理   //
		// -------------------- //
		// 取得したデータ数と登録したデータ数が同じだった場合、trueをセット
		if((cntTList != 0) && (twitterDt.size() != 0) && (cntTList == twitterDt.size())){
			topForm.gapFlg = true;
		}
		// メンション情報の取得＆セット(FrontierDBより)
		getTlList = chgFTwiDtLBM(twitterService.selMentionOrFavoriteList(sidTo,sidFrom,Integer.valueOf(topForm.userId).intValue(),"1",null,appDefDto.ST_LIST_MAX,0));
		// 上記で取得したデータが1件以上あれば判定
		if(getTlList.size() > 0){
			// 取得データの一番最後のステータスIDをセット(ギャップ用)
			sidEnd = (Long) getTlList.get(getTlList.size()-1).get("id");
		}
		// HTML出力
		CmnUtility.ResponseWrite(mkHtmlGapNewMention(getTlList,sidFrom,sidEnd),"text/html","windows-31J","Shift_JIS");
		return null;
	}

	// お気に入り
	@Execute(validator=false)
	public String AAGetGapNewFavorite() throws IOException, Exception{
		topForm.gapFlg = false; // ギャップ判定用変数
		twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
		//ステータスIDをlong型へ変換
		long sidFrom = Long.parseLong(topForm.VVGapSidFrom);
		long sidTo   = Long.parseLong(topForm.VVGapSidTo);
		long sidEnd  = 0;
		// Twitterデータ取得用変数
		List<Map<String,String>> twitterDt = null;
		//Frontierに登録されているtwitterのユーザ情報を取得する
		List<OauthTokensStore> lots = twitterService.selAllTwitterUser(userInfoDto.memberId,appDefDto.TWI_PROVIDER,"0",null,topForm.myScreenName,"1");
		//コンシューマ情報を取得
		List<BeanMap> lbmConsumer = oauthConsumerService.getConsumerInfo(appDefDto.ST_PROVIDER);
		// Twitterデータ取得
		try {
			twitterDt = new TwitterUtility().getFavoriteOld(topForm.screenName,sidTo,appDefDto.TWI_PROVIDER,lbmConsumer,lots.get(0));
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// データ登録
		Integer cntTList = insTweetToFrontierLmss("2",twitterDt);
		// -------------------- //
		// ◆ギャップ判定処理   //
		// -------------------- //
		// 取得したデータ数と登録したデータ数が同じだった場合、trueをセット
		if((cntTList != 0) && (twitterDt.size() != 0) && (cntTList == twitterDt.size())){
			topForm.gapFlg = true;
		}
		// お気に入り情報の取得＆セット(FrontierDBより)
		getTlList = chgFTwiDtLBM(twitterService.selMentionOrFavoriteList(sidTo,sidFrom,Integer.valueOf(topForm.userId).intValue(),null,"1",appDefDto.ST_LIST_MAX,0));
		// 上記で取得したデータが1件以上あれば判定
		if(getTlList.size() > 0){
			// 取得データの一番最後のステータスIDをセット(ギャップ用)
			sidEnd = (Long) getTlList.get(getTlList.size()-1).get("id");
		}
		// HTML出力
		CmnUtility.ResponseWrite(mkHtmlGapNewFavorite(getTlList,sidFrom,sidEnd),"text/html","windows-31J","Shift_JIS");
		return null;
	}

	// ==================================== //
	//  ▼ギャップ取得処理(もっと読む版)▼  //
	// ==================================== //
	// 自分のTL
	@Execute(validator=false)
	public String AAGetGapMyTl() throws IOException, Exception{
		twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
		//ステータスIDをlong型へ変換
		long st = Long.parseLong(topForm.VVMostOldSid);
		// Twitterデータ取得用変数
		List<Status> twitterDt = null;
		// Twitterデータ取得
		try {
			twitterDt = new TwitterUtility().getUTimelineOld(twitter, topForm.myScreenName,st,1,appDefDto.ST_MY_TL_LIST_MAX);
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// データ登録
		insTweetToFrontier("0",twitterDt);
		// 自分のTL情報の取得＆セット
		getTlList = chgFTwiDtLBM(twitterService.selTwitterList(st,Integer.valueOf(topForm.userId).intValue(),appDefDto.ST_MY_TL_LIST_MAX));
		// データが取得出来れば処理を行う
		if(getTlList.size() > 0){
			// 取得データの一番最後のステータスIDをセット(もっと読む用)
			topForm.twMaxOldId = (Long) getTlList.get(getTlList.size()-1).get("id");
			// もっと読む、ギャップ判定
			chkMoreGapMyTl(topForm.twMaxOldId);
		} else {
			// データが取得できなければフラグをクリア
			topForm.tlMoreFlg = false;
			topForm.gapFlg = false;
		}
		// HTML出力
		CmnUtility.ResponseWrite(mkHtmlMoreMy(getTlList),"text/html","windows-31J","Shift_JIS");
		return null;
	}

	// メンション
	@Execute(validator=false)
	public String AAGetGapMention() throws IOException, Exception{
		twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
		//ステータスIDをlong型へ変換
		long st = Long.parseLong(topForm.VVMostOldSid);
		// Twitterデータ取得用変数
		List<Status> twitterDt = null;
		// Twitterデータ取得
		try {
			twitterDt = new TwitterUtility().getMentionsOld(twitter, topForm.twMaxOldId,1,appDefDto.ST_MY_TL_LIST_MAX);
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// データ登録
		insTweetToFrontier("1",twitterDt);
		// メンション情報の取得＆セット(FrontierDBより)
		getTlList = chgFTwiDtLBM(twitterService.selMentionOrFavoriteList(st,null,Integer.valueOf(topForm.userId).intValue(),"1",null,appDefDto.ST_MY_TL_LIST_MAX,0));
		// データが取得出来れば処理を行う
		if(getTlList.size() > 0){
			// 取得データの一番最後のステータスIDをセット(もっと読む用)
			topForm.twMaxOldId = (Long) getTlList.get(getTlList.size()-1).get("id");
			// もっと読む、ギャップ判定
			chkMoreGapMention(topForm.twMaxOldId);
		} else {
			// データが取得できなければフラグをクリア
			topForm.tlMoreFlg = false;
			topForm.gapFlg = false;
		}
		// HTML出力
		CmnUtility.ResponseWrite(mkHtmlMoreMention(getTlList),"text/html","windows-31J","Shift_JIS");
		return null;
	}

	// お気に入り
	@Execute(validator=false)
	public String AAGetGapFavorite() throws IOException, Exception{
		twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
		//ステータスIDをlong型へ変換
		long st = Long.parseLong(topForm.VVMostOldSid);
		// Twitterデータ取得用変数
		List<Map<String,String>> twitterDt = null;
		//Frontierに登録されているtwitterのユーザ情報を取得する
		List<OauthTokensStore> lots = twitterService.selAllTwitterUser(userInfoDto.memberId,appDefDto.TWI_PROVIDER,"0",null,topForm.myScreenName,"1");
		//コンシューマ情報を取得
		List<BeanMap> lbmConsumer = oauthConsumerService.getConsumerInfo(appDefDto.ST_PROVIDER);
		// Twitterデータ取得
		try {
			twitterDt = new TwitterUtility().getFavoriteOld(topForm.screenName, topForm.twMaxOldId,appDefDto.TWI_PROVIDER,lbmConsumer,lots.get(0));
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// データ登録
		insTweetToFrontierLmss("2",twitterDt);
		// お気に入り情報の取得＆セット(FrontierDBより)
		getTlList = chgFTwiDtLBM(twitterService.selMentionOrFavoriteList(st,null,Integer.valueOf(topForm.userId).intValue(),null,"1",appDefDto.ST_MY_TL_LIST_MAX,0));
		// データが取得出来れば処理を行う
		if(getTlList.size() > 0){
			// 取得データの一番最後のステータスIDをセット(もっと読む用)
			topForm.twMaxOldId = (Long) getTlList.get(getTlList.size()-1).get("id");
			// もっと読む、ギャップ判定
			chkMoreGapFavorite(topForm.twMaxOldId);
		} else {
			// データが取得できなければフラグをクリア
			topForm.tlMoreFlg = false;
			topForm.gapFlg = false;
		}
		// HTML出力
		CmnUtility.ResponseWrite(mkHtmlMoreFavorite(getTlList),"text/html","windows-31J","Shift_JIS");
		return null;
	}

// ================================= //
// ◆共通ファンクション              //
// ================================= //
	// FrontierDBより取得したデータを変数にセットし、List<BeanMap>の型にして詰め直し
	private List<BeanMap> chgFTwiDtLBM(List<BeanMap> lb){
		List<BeanMap> returnBM = new ArrayList<BeanMap>();
		// 詰め直し
		if(lb!=null){
			for(BeanMap b:lb){
				// 詰め直し用の変数にput(DBデータをTwitterHTML生成用の変数へ格納)
				BeanMap bm = new BeanMap();
				bm.put("id"               ,b.get("statusid"));
				bm.put("createdAt"        ,b.get("contributetime"));
				bm.put("source"           ,"");
				bm.put("inReplyToUserId"  ,b.get("replytwituserid"));
				bm.put("replyToStatusId"  ,b.get("replytwitstatusid"));
				bm.put("favorited"        ,chgObjToBoolean(b.get("favoriteflg")));
				bm.put("user_id"          ,b.get("twituserid"));
				bm.put("name"             ,b.get("twitname"));
				bm.put("screenName"       ,b.get("screenname"));
				bm.put("location"         ,"");
				bm.put("description"      ,"");
				bm.put("profileImageUrl"  ,b.get("pic"));
				bm.put("followersCount"   ,"");
				bm.put("friendsCount"     ,"");
				bm.put("favouritesCount"  ,"");
				bm.put("viewCommentTl"    ,cnvCmntToView(b.get("comment").toString()));
				bm.put("viewCommentTlAlt" ,cnvCmntToAlt(b.get("comment").toString()));
				bm.put("viewCommentTlRe"  ,cnvCmntToRe(b.get("comment").toString()));
				bm.put("myMentionFlg"     ,chkMyMention(b.get("comment").toString()));
				returnBM.add(bm);
			}
		}
		return returnBM;
	}

	// Twitterから取得したデータを変数にセットし、List<BeanMap>の型にして詰め直し
	private List<BeanMap> chgTwiDtLBM(List<Status> ls){
		List<BeanMap> returnBM = new ArrayList<BeanMap>();
		// 取得したtwitterリスト分ループ
		if(ls.size()>0){
			for(int i=0;i<ls.size();i++){
				// 詰め直して追加
				returnBM.add(chgSttsToBmp(ls.get(i)));
			}
		}
		return returnBM;
	}

	// Twitterから取得したデータを変数にセットし、List<BeanMap>の型にして詰め直し(List<Tweet>版)
	private List<BeanMap> chgTwiDtLtLBM(List<Tweet> lt){
		List<BeanMap> returnBM = new ArrayList<BeanMap>();
		// 取得したtwitterリスト分ループ
		if(lt.size()>0){
			for(int i=0;i<lt.size();i++){
				// 時間フォーマット
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
				// BeanMap に変数を設定
				BeanMap bm = new BeanMap();
				bm.put("id"               ,lt.get(i).getId());
				bm.put("createdAt"        ,formatter.format(lt.get(i).getCreatedAt()));
				bm.put("source"           ,lt.get(i).getSource().toString());
				bm.put("inReplyToUserId"  ,"");
				bm.put("replyToStatusId"  ,"");
				bm.put("favorited"        ,"");
				bm.put("user_id"          ,lt.get(i).getFromUserId());
				bm.put("name"             ,"");
				bm.put("screenName"       ,lt.get(i).getFromUser());
				bm.put("location"         ,"");
				bm.put("description"      ,"");
				bm.put("profileImageUrl"  ,lt.get(i).getProfileImageUrl());
				bm.put("followersCount"   ,"");
				bm.put("friendsCount"     ,"");
				bm.put("favouritesCount"  ,"");
				bm.put("viewCommentTl"    ,cnvCmntToView(lt.get(i).getText()));
				bm.put("viewCommentTlAlt" ,cnvCmntToAlt(lt.get(i).getText()));
				bm.put("viewCommentTlRe"  ,cnvCmntToRe(lt.get(i).getText()));
				bm.put("myMentionFlg"     ,chkMyMention(lt.get(i).getText()));
				returnBM.add(bm);
			}
		}
		return returnBM;
	}

	// 詰め直し処理外出し
	private BeanMap chgSttsToBmp(Status stts){
		// BeanMap に変数を設定
		BeanMap bm = new BeanMap();
		if(stts != null){
			// 時間フォーマット
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			bm.put("id"               ,stts.getId());
			bm.put("createdAt"        ,formatter.format(stts.getCreatedAt()));
			bm.put("source"           ,stts.getSource().toString());
			bm.put("inReplyToUserId"  ,stts.getInReplyToUserId());
			bm.put("replyToStatusId"  ,stts.getInReplyToStatusId());
			bm.put("favorited"        ,stts.isFavorited());
			bm.put("user_id"          ,stts.getUser().getId());
			bm.put("name"             ,stts.getUser().getName());
			bm.put("screenName"       ,stts.getUser().getScreenName());
			bm.put("location"         ,"");
			bm.put("description"      ,"");
			bm.put("profileImageUrl"  ,stts.getUser().getProfileImageURL());
			bm.put("followersCount"   ,stts.getUser().getFollowersCount());
			bm.put("friendsCount"     ,stts.getUser().getFriendsCount());
			bm.put("favouritesCount"  ,stts.getUser().getFavouritesCount());
			bm.put("viewCommentTl"    ,cnvCmntToView(stts.getText()));
			bm.put("viewCommentTlAlt" ,cnvCmntToAlt(stts.getText()));
			bm.put("viewCommentTlRe"  ,cnvCmntToRe(stts.getText()));
			bm.put("myMentionFlg"     ,chkMyMention(stts.getText()));
		} else {
			bm = null;
		}
		return bm;
	}

	// TwitterデータをFrontierDBへ登録する(returnは登録した件数を返す)
	private Integer insTweetToFrontier(String insType,List<Status> ls){
		// --------------------
		// insType:
		//  0:自分のTL
		//  1:メンション
		//  2:お気に入り
		// --------------------
		String insMtFlg = "0"; // メンションフラグ
		String insFvFlg = "0"; // お気に入りフラグ
		// タイプ別にフラグの設定
		if(insType.equals("1")){
			// メンションフラグに1を設定
			insMtFlg = "1";
		}else if(insType.equals("2")){
			// お気に入りフラグに1を設定
			insFvFlg = "1";
		}

		Integer cntTList = 0; // リストカウント用変数
		// Twitterより取得したリストデータのループ
		for (int i=0;i<ls.size();i++){
			// Twitterの発言のステータスID
			long tsid = ls.get(i).getId();
			// ステータスIDよりTwitterDBに登録されているかチェック
			if(twitterService.cntTwitter(tsid)==0){
				// 登録する場合はカウント+1
				cntTList += 1;
				// 登録されていない場合、登録を行う
				twitterService.insTwitter(ls.get(i),userInfoDto.memberId);
			}
			// ステータスID、TwitterのユーザIDよりTwitter投稿管理テーブルに登録されているかチェック
			if(twitterService.cntTwitterPostManagement(tsid,Integer.valueOf(topForm.userId).intValue())==0){
				// 登録されていない場合、登録を行う
				twitterService.insTwitterPostManagement(tsid,Integer.valueOf(topForm.userId).intValue(),"1",insFvFlg,"0","0","0",userInfoDto.memberId,insMtFlg);
			}
		}
		return cntTList;
	}

	// TwitterデータをFrontierDBへ登録する(returnは登録した件数を返す)(List<Map<String String>>版)
	private Integer insTweetToFrontierLmss(String insType,List<Map<String,String>> lmss){
		// --------------------
		// insType:
		//  0:自分のTL
		//  1:メンション
		//  2:お気に入り
		// --------------------
		String insMtFlg = "0"; // メンションフラグ
		String insFvFlg = "0"; // お気に入りフラグ
		// タイプ別にフラグの設定
		if(insType.equals("1")){
			// メンションフラグに1を設定
			insMtFlg = "1";
		}else if(insType.equals("2")){
			// お気に入りフラグに1を設定
			insFvFlg = "1";
		}

		Integer cntTList = 0; // リストカウント用変数
		// Twitterより取得したリストデータのループ
		for (int i=0;i<lmss.size();i++){
			// Twitterの発言のステータスID
			Object objId = lmss.get(i).get("id");
			long tsid = Long.parseLong(objId.toString());
			// ステータスIDよりTwitterDBに登録されているかチェック
			if(twitterService.cntTwitter(tsid)==0){
				// 登録する場合はカウント+1
				cntTList += 1;
				// 登録されていない場合、登録を行う
				twitterService.insTwitter(lmss.get(i),userInfoDto.memberId);
			}
			// ステータスID、TwitterのユーザIDよりTwitter投稿管理テーブルに登録されているかチェック
			if(twitterService.cntTwitterPostManagement(tsid,Integer.valueOf(topForm.userId).intValue())==0){
				// 登録されていない場合、登録を行う
				twitterService.insTwitterPostManagement(tsid,Integer.valueOf(topForm.userId).intValue(),"1",insFvFlg,"0","0","0",userInfoDto.memberId,insMtFlg);
			}
		}
		return cntTList;
	}

	// ツイートの削除(Twitter、Frontier両方)
	private void delTweetTandF(String targetSid){
		twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
		// ステータスIDをlong型へ変換
		long sid = Long.parseLong(targetSid);
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

	// メンション判定(文章中に自分のアカウントが含まれるか)
	private String chkMyMention(String getVal){
		String myMentionFlg = "0";
		// 自分のアカウントが含まれるか検索(含まれれば-1以外を返す)
		if (getVal.indexOf(topForm.myScreenName) != -1){
			myMentionFlg = "1";
		}
		return myMentionFlg;
	}

	// Object -> (String -> Integer ->) Boolean型に変換
	// 特殊な変換だが結構使ったので共通ファンクション化
	private boolean chgObjToBoolean(Object objval){
		Integer intval = Integer.valueOf(objval.toString()).intValue();
		if(intval == 0){
			return false;
		} else {
			return true;
		}
	}

	// コメントを本文表示用に装飾する処理
	// ※※※要修正※※※
	private String cnvCmntToView(String getVal){
		String rtnVal;
		// 装飾処理
		rtnVal = CmnUtility.htmlSanitizing(getVal);          // サニタイジング
		rtnVal = CmnUtility.convURL(rtnVal);                 // URL化
		rtnVal = repCmnt(rtnVal);                            // FShoutコメント文字列置換
		rtnVal = replaceTwComment(rtnVal);                   // FShoutコメント文字列置換（ハッシュタグ対応）
		rtnVal = replaceTwCommentMem(rtnVal);                // FShoutコメント文字列置換（メンバー名リンク化対応）
		rtnVal = rtnVal.replace("\n", "").replace("\r", ""); // 改行コード削除
		return rtnVal;
	}

	// コメントをAlt属性で使用できるように装飾する処理
	// ※※※要修正※※※
		private String cnvCmntToAlt(String getVal){
		String rtnVal;
		// 装飾処理
		rtnVal = getVal.replace("\n", "").replace("\r", ""); // 改行コード削除
		rtnVal = CmnUtility.htmlSanitizing(rtnVal);          // サニタイジング
		rtnVal = repCmnt(rtnVal);                            // FShoutコメント文字列置換
		rtnVal = rtnVal.replace("'", "\\'");                 // 「'」->「\\'」変換
		return rtnVal;
	}

	// コメントをReのコメントとして使用できるように装飾する処理
	// ※※※要修正※※※
	private String cnvCmntToRe(String getVal){
		String rtnVal;
		// 装飾処理
		rtnVal = getVal.replace("\n", "").replace("\r", ""); // 改行コード削除
		rtnVal = CmnUtility.htmlSanitizing(rtnVal);          // サニタイジング
		rtnVal = rtnVal.replace("'", "\\'");                 // 変換※その１：一度「'」を「\'」とする
		rtnVal = repCmntAlt(rtnVal);                         // FShoutコメント文字列置換
		rtnVal = rtnVal.replace("\\", "\\\\");               // 「\」->「\\」変換
		rtnVal = rtnVal.replace("\\\\'", "\\'");             // 変換※その２：直前で「\\'」となるので「\'」に変換
		rtnVal = repCmnt(rtnVal);                            // FShoutコメント文字列置換
		return rtnVal;
	}

	// もっと読む、ギャップのチェック(自分のアカウントのTL)
	private void chkMoreGapMyTl(long lsid){
		// Formのフラグ初期化
		topForm.tlMoreFlg = true;
		topForm.gapFlg = true;
		// Twitterデータ格納用変数
		List<Status> ls  = null; // Twitterより
		List<BeanMap> lb = null; // Frontierより
		twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
		// 過去にデータがあるか検索(FrontierDB)
		lb = chgFTwiDtLBM(twitterService.selTwitterList(lsid,Integer.valueOf(topForm.userId).intValue(),1));
		// 過去にデータがあるか検索(Twitter)
		try {
			ls = new TwitterUtility().getUTimelineOld(twitter, topForm.screenName,lsid,1,1);
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// もっと読む判定
		if(lb.size()==0){
			topForm.tlMoreFlg = false;
		}
		// ギャップ判定
		topForm.gapFlg = chkGap(ls,lb);
	}

	// もっと読む、ギャップのチェック(メンション)
	private void chkMoreGapMention(long lsid){
		// Formのフラグ初期化
		topForm.tlMoreFlg = true;
		topForm.gapFlg = true;
		// Twitterデータ格納用変数
		List<Status> ls  = null; // Twitterより
		List<BeanMap> lb = null; // Frontierより
		twitter = twitterService.setTwitter(userInfoDto.memberId,null,null,null);
		// 過去にデータがあるか検索(FrontierDB)
		lb = chgFTwiDtLBM(twitterService.selMentionOrFavoriteList((Object)lsid,null,Integer.valueOf(topForm.userId).intValue(),"1",null,1,0));
		// 過去にデータがあるか検索(Twitter)
		try {
			ls = new TwitterUtility().getMentionsOld(twitter, lsid,1,1);
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// もっと読む判定
		if(lb.size()==0){
			topForm.tlMoreFlg = false;
		}
		// ギャップ判定
		topForm.gapFlg = chkGap(ls,lb);
	}

	// もっと読む、ギャップのチェック(お気に入り)
	private void chkMoreGapFavorite(long lsid){
		// Formのフラグ初期化
		topForm.tlMoreFlg = true;
		topForm.gapFlg = true;
		// Twitterデータ格納用変数
		List<Map<String,String>> lmss = null; // Twitterより
		List<BeanMap> lb = null;              // Frontierより
		//Frontierに登録されているtwitterのユーザ情報を取得する
		List<OauthTokensStore> lots = twitterService.selAllTwitterUser(userInfoDto.memberId,appDefDto.TWI_PROVIDER,"0",null,topForm.myScreenName,"1");
		//コンシューマ情報を取得
		List<BeanMap> lbmConsumer = oauthConsumerService.getConsumerInfo(appDefDto.ST_PROVIDER);
		// 過去にデータがあるか検索(FrontierDB)
		lb = chgFTwiDtLBM(twitterService.selMentionOrFavoriteList((Object)lsid,null,Integer.valueOf(topForm.userId).intValue(),null,"1",1,0));
		// 過去にデータがあるか検索(Twitter)
		try {
			lmss = new TwitterUtility().getFavoriteOld(topForm.screenName, lsid,appDefDto.TWI_PROVIDER,lbmConsumer,lots.get(0));
		} catch (net.arnx.jsonic.JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// もっと読む判定
		if(lb.size()==0){
			topForm.tlMoreFlg = false;
		}
		// ギャップ判定
		topForm.gapFlg = chkGapLmss(lmss,lb);
	}


	// ギャップのチェック(TwitterとFrontierDBよりデータを取得し比較する)
	private boolean chkGap(List<Status> ls,List<BeanMap>lb){
		boolean rtnbl = true; // 初期値true (ギャップがあればtrue、なければfalse(=同期が取れている))
		long tstatusId;
		long fstatusId;
		if(ls.size()==0 && lb.size()==0){
			// データがあるかチェック
			// 両方ともなければ(0件なら)false
			rtnbl = false;
		} else {
			// データが0件以上
			if(ls.size()!=0 && lb.size()!=0){
				// ２、取得したデータのステータスIDを取得する
				tstatusId = ls.get(0).getId();
				fstatusId = (Long) lb.get(0).get("id");
				if(tstatusId == fstatusId){
					// 同じステータスIDだったらfalse
					rtnbl = false;
				}
			}
		}
		return rtnbl;
	}

	// ギャップのチェック(TwitterとFrontierDBよりデータを取得し比較する)(List<Map<String,String>>版)
	private boolean chkGapLmss(List<Map<String,String>> lmss,List<BeanMap>lb){
		boolean rtnbl = true; // 初期値true (ギャップがあればtrue、なければfalse(=同期が取れている))
		long tstatusId;
		long fstatusId;
		if(lmss.size()==0 && lb.size()==0){
			// データがあるかチェック
			// 両方ともなければ(0件なら)false
			rtnbl = false;
		} else {
			// データが0件以上
			if(lmss.size()!=0 && lb.size()!=0){
				// ２、取得したデータのステータスIDを取得する
				Object objid = lmss.get(0).get("id");
				tstatusId = Long.parseLong(objid.toString());
				fstatusId = (Long) lb.get(0).get("id");
				if(tstatusId == fstatusId){
					// 同じステータスIDだったらfalse
					rtnbl = false;
				}
			}
		}
		return rtnbl;
	}
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

	// コメント文字列置換（twitter）
	// ※※※要修正※※※
	private String repCmnt(String txt){
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
			String getMid = "";
			String FShout = "";
			String getFrontierDomain = "";
			String myFrontierDomain = appDefDto.FP_CMN_HOST_NAME;
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
				//「\」対応※注意：Top.Action側とは異なる
				FShout = FShout.replace("\\","\\\\");
				Matcher matcher = convURLLinkPtn.matcher(FShout);
				if(profileList.get(0).get("membertype").toString().equals("0")){
					//自Frontier
					FShout = matcher.replaceAll("<a href=\\\\\"/frontier/pc/fshout/list/"+getMid+"/1\\\\\" title=\\\\\"$0\\\\\">$0</a>");
				}else if(profileList.get(0).get("membertype").toString().equals("1")){
					//他Frontier
					if(myFrontierDomain.equals(getFrontierDomain)){
						FShout = matcher.replaceAll("<a href=\\\\\"http://"+myFrontierDomain+"/frontier/pc/fshout/list/"+getMid+"/1\\\\\" title=\\\\\"$0\\\\\">$0</a>");
					} else {
						FShout = matcher.replaceAll("<a href=\\\\\"http://"+getFrontierDomain+"/frontier/pc/openid/auth?cid="+getMid+"&gm=mv&openid="+myFrontierDomain+"/frontier/pc/openidserver\\\\\" title=\\\\\"$0\\\\\">$0</a>");
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

	// FShoutコメント文字列置換
	// ※※※要修正※※※
	private String repCmntAlt(String txt){
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

// ================================= //
// ◆HTML作成メソッド                //
// ================================= //
	// ========================================================== //
	// [Twitter]プロフィール作成                                  //
	// ========================================================== //
	private String mkHtmlPrf(String chkUandI,User ui,String selBtnName){
		ViewData = "";
		// パラメタセット
		String getTsnm  = ui.getScreenName();                     // アカウント名
		String getTnm   = CmnUtility.escJavaScript(ui.getName()); // 名前
		Integer getTfc  = ui.getFriendsCount();                   // フォローしている数
		Integer getTfwc = ui.getFollowersCount();                 // フォローされている数
		Integer getTsc  = ui.getStatusesCount();                  // ツイート数
		URL     getTimg = ui.getProfileImageURL();                // 画像のURL

		// 各ボタンのクラス設定
		String btnTlclass = "twbtn01off";
		String btnMtclass = "twbtn01off";
		String btnFvclass = "twbtn01off";
		String btnLiclass = "twbtn01off";
		// 押されてるボタン判定
		if(selBtnName.equals("btnTl")){
			btnTlclass = "twbtn01on";
		}else if(selBtnName.equals("btnMt")){
			btnMtclass = "twbtn01on";
		}else if(selBtnName.equals("btnFv")){
			btnFvclass = "twbtn01on";
		}else if(selBtnName.equals("btnLi")){
			btnLiclass = "twbtn01on";
		}

		// 画像、名前、ツイート数エリア
		ViewData += "<table width=\"100%\">";
		ViewData += "<tr>";
		ViewData += "<td>";
		ViewData += "<img src=\""+getTimg+"\" alt=\""+getTnm+"\" style=\"margin:5px 5px 0 10px;\" title=\""+getTnm+"\" class=\"profileImg linkImg\" onClick=\"chgListTop('"+getTsnm+"');\" />";
		ViewData += "<img src=\"/images/twitter-b.png\" style=\"margin:2px;\" class=\"linkImg\" alt=\"公式Twitterへ\"  title=\"公式Twitterへ\" onClick=\"moveTwit('/"+getTsnm+"');\"/>";
		ViewData += "</td>";
		ViewData += "<td colspan=\"2\">";
		ViewData += "<span class=\"fBold twLink\" onClick=\"chgListTop('"+getTsnm+"');\">"+getTsnm+"</span>";
		ViewData += "&nbsp;&nbsp;/&nbsp;&nbsp;";
		ViewData += "<span class=\"fBold twLink\" onClick=\"chgListTop('"+getTsnm+"');\">"+getTnm+"</span><br/>";
		// 表示アカウントとユーザとの関係表示エリア
		if(!topForm.myScreenName.equals(getTsnm)){
			ViewData += "<span class=\"fBold\">";
			if(chkUandI.equals("1")){
				ViewData += "フォロー中";
			} else if(chkUandI.equals("2")){
				ViewData += "フォローされています。";
			} else if(chkUandI.equals("3")){
				ViewData += "お互いフォロー中";
			} else {
				ViewData += "フォロー関係にありません。";
			}
			//ViewData += "あなたです！";
			ViewData += "</span><br/>";
		}
		ViewData += "<span class=\"twLink\" onClick=\"chgListTop('"+getTsnm+"');\">"+getTsc+"ツィート</span>";
		ViewData += "</td>";
		ViewData += "</tr>";
		// 画像、名前、ツイート数エリア

		// 情報エリア(フォローしている/フォローされている/リスト)
		ViewData += "<tr class=\"twitfollowList\">";
		ViewData += "<td style=\"text-align:center;\">";
		ViewData += "<font style=\"font-size:14px;font-weight:bold;\">"+getTfc+"</font><br/>";
		ViewData += "<span class=\"twLink\" onClick=\"moveTwit('/"+getTsnm+"/following');\" class=\"twfollow\">フォロー<br/>している</span>";
		ViewData += "</td>";
		ViewData += "<td style=\"text-align:center;\">";
		ViewData += "<font style=\"font-size:14px;font-weight:bold;\">"+getTfwc+"</font><br/>";
		ViewData += "<span class=\"twLink\" onClick=\"moveTwit('/"+getTsnm+"/followers');\" class=\"twfollow\">フォロー<br/>されている</span>";
		ViewData += "</td>";
		ViewData += "<td style=\"text-align:center;\">";
		ViewData += "&nbsp;<br/>";
		ViewData += "<span class=\"twLink\" onClick=\"resetBtnStyle();readTwList('"+getTsnm+"');\" class=\"twfollow\">リスト</span>";
		ViewData += "</td>";
		ViewData += "</tr>";
		// 情報エリア(フォローしている/フォローされている/リスト)

		// 自分のプロフィールならば表示
		if(topForm.screenName.equals(topForm.myScreenName)){
			// ボタンエリア(ＴＬ/＠/★/リスト)
			// 押せない状態(disabled)で初期表示を行う
			ViewData += "<tr>";
			ViewData += "<td colspan=\"3\" style=\"text-align:center;\">";
			ViewData += "<div id=\"prfMenuDiv\">";
			ViewData += "<input type=\"button\" id=\"btnTl\" class=\""+btnTlclass+"\" value=\"ＴＬ\" disabled/>";
			ViewData += "&nbsp;&nbsp;&nbsp;";
			ViewData += "<input type=\"button\" id=\"btnMt\" class=\""+btnMtclass+"\" value=\"＠\" disabled/>";
			ViewData += "&nbsp;&nbsp;&nbsp;";
			ViewData += "<input type=\"button\" id=\"btnFv\" class=\""+btnFvclass+"\" value=\"★\" disabled/>";
			ViewData += "&nbsp;&nbsp;&nbsp;";
			ViewData += "<input type=\"button\" id=\"btnLi\" class=\""+btnLiclass+"\" value=\"リスト\" disabled/>";
			ViewData += "</div>";
			ViewData += "</td>";
			ViewData += "</tr>";
			// ボタンエリア(ＴＬ/＠/★/リスト)
		}

		ViewData += "</table>";
		// 読み込み完了ファンクション実行用画像(onloadアクションを利用する)
		ViewData += mkHtmlPrfLoadComplete();
		return ViewData;
	}

	// ========================================================== //
	// [Twitter]プロフィールのメニューボタン部分の作成            //
	// ========================================================== //
	private String mkHtmlPrfMenu(long cntMentionNew,String selBtnName){
		ViewData = "";
		// 各ボタンのクラス設定
		String btnTlclass = "twbtn01off";
		String btnMtclass = "twbtn01off";
		String btnFvclass = "twbtn01off";
		String btnLiclass = "twbtn01off";
		// 押されてるボタン判定
		if(selBtnName.equals("btnTl")){
			btnTlclass = "twbtn01on";
		}else if(selBtnName.equals("btnMt")){
			btnMtclass = "twbtn01on";
		}else if(selBtnName.equals("btnFv")){
			btnFvclass = "twbtn01on";
		}else if(selBtnName.equals("btnLi")){
			btnLiclass = "twbtn01on";
		}

		// ボタンエリア(ＴＬ/＠/★/リスト)
		ViewData += "<input type=\"button\" id=\"btnTl\" class=\""+btnTlclass+"\" value=\"ＴＬ\" onClick=\"chgBtnStyle('btnTl');readTwTop('"+topForm.myScreenName+"');\"/>";
		ViewData += "&nbsp;&nbsp;&nbsp;";
		ViewData += "<input type=\"button\" id=\"btnMt\" class=\""+btnMtclass+"\" value=\"＠";
		// 未読メンションが1件以上あれば表示
		if(cntMentionNew > 0){
			ViewData += "("+cntMentionNew+")";
		}
		ViewData += "\" onClick=\"chgBtnStyle('btnMt');readTwMention('"+topForm.myScreenName+"');\"/>";
		ViewData += "&nbsp;&nbsp;&nbsp;";
		ViewData += "<input type=\"button\" id=\"btnFv\" class=\""+btnFvclass+"\" value=\"★\" onClick=\"chgBtnStyle('btnFv');readTwFavorite('"+topForm.myScreenName+"');\"/>";
		ViewData += "&nbsp;&nbsp;&nbsp;";
		ViewData += "<input type=\"button\" id=\"btnLi\" class=\""+btnLiclass+"\" value=\"リスト\" onClick=\"chgBtnStyle('btnLi');readTwMyList('"+topForm.myScreenName+"');\"/>";
		// ボタンエリア(ＴＬ/＠/★/リスト)
		return ViewData;
	}
	
	// ========================================================== //
	// [Twitter]検索フィールドの作成(プロフィールエリア)          //
	// ========================================================== //
	private String mkHtmlTwitSearchArea(List<BeanMap> lbm){
		ViewData = "";
		// 今年を取得(コンボボックス用)
		String thisYear = CmnUtility.getToday("yyyy");
		ViewData += "<div style=\"margin:5px 5px 10px 5px;\">";
		// AND検索、OR検索
		ViewData += "<div>";
		ViewData += "<input type=\"radio\" name=\"twitAndOr\" value=\"0\" checked>&nbsp;AND検索&nbsp;&nbsp;";
		ViewData += "<input type=\"radio\" name=\"twitAndOr\" value=\"1\">&nbsp;OR検索";
		ViewData += "</div>";
		
		// 年月日(From～)
		ViewData += "<div style=\"margin:3px;\">";
		ViewData += "&nbsp;&nbsp;";
		// 年
		ViewData += "<select name=\"twitYYYYFrom\">";
		ViewData += "<option value=\"0\">----</option>";
		for(int i=Integer.parseInt(thisYear);i>=2006;i--){
			ViewData += "<option value=\"" + i + "\">" + i + "</option>";
		}
		ViewData += "</select>";
		ViewData += "年";
		// 月
		ViewData += "<select name=\"twitMMFrom\">";
		ViewData += "<option value=\"0\">--</option>";
		for(int i=1;i<=12;i++){
			ViewData += "<option value=\"" + String.format("%1$02d",i) + "\">" + String.format("%1$02d",i) + "</option>";
		}
		ViewData += "</select>";
		ViewData += "月";
		// 日
		ViewData += "<select name=\"twitDDFrom\">";
		ViewData += "<option value=\"0\">--</option>";
		for(int i=1;i<=31;i++){
			ViewData += "<option value=\"" + String.format("%1$02d",i) + "\">" + String.format("%1$02d",i) + "</option>";
		}
		ViewData += "</select>";
		ViewData += "日";
		ViewData += "</div>";
		
		// 年月日(～To)
		ViewData += "<div style=\"margin:3px;\">";
		ViewData += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;～&nbsp;&nbsp;&nbsp;&nbsp;";
		// 年
		ViewData += "<select name=\"twitYYYYTo\">";
		ViewData += "<option value=\"0\">----</option>";
		for(int i=Integer.parseInt(thisYear);i>=2006;i--){
			ViewData += "<option value=\"" + i + "\">" + i + "</option>";
		}
		ViewData += "</select>";
		ViewData += "年";
		// 月
		ViewData += "<select name=\"twitMMTo\">";
		ViewData += "<option value=\"0\">--</option>";
		for(int i=1;i<=12;i++){
			ViewData += "<option value=\"" + String.format("%1$02d",i) + "\">" + String.format("%1$02d",i) + "</option>";
		}
		ViewData += "</select>";
		ViewData += "月";
		// 日
		ViewData += "<select name=\"twitDDTo\">";
		ViewData += "<option value=\"0\">--</option>";
		for(int i=1;i<=31;i++){
			ViewData += "<option value=\"" + String.format("%1$02d",i) + "\">" + String.format("%1$02d",i) + "</option>";
		}
		ViewData += "</select>";
		ViewData += "日";
		ViewData += "</div>";
		
		// アカウント一覧(チェックボックス、写真、アカウント名)
		ViewData += "<div style=\"margin:5px;\">";
		for(BeanMap b:lbm){
			ViewData += "<div style=\"margin:5px;font-size:12px;\">";
			ViewData += "<input type=\"checkbox\" style=\"vertical-align:middle;\" name=\"twitAccountId\" value=\"" + b.get("twituserid") + "\">" ;
			ViewData += "<img src=\"" + b.get("pic") + "\" width=\"18\" height=\"18\" style=\"display:inline;vertical-align:middle;\" title=\"" + b.get("screenname") + "\">&nbsp;";
			ViewData += b.get("screenname") + "&nbsp;/&nbsp;" + b.get("twitname");
			ViewData += "<br/>";
			ViewData += "</div>";
		}
		ViewData += "</div>";
		
		// 検索モード判別用変数
		ViewData += "<input type=\"hidden\" id=\"twiSearchMd\" value=\"1\"/>";
		
		ViewData += "</div>";
		return ViewData;
	}
	
	// ========================================================== //
	// [Twitter]プロフィールエリアに指定の1ツイートHTML作成       //
	// ========================================================== //
	private String mkHtmlPrfTweet(BeanMap b){
		ViewData = "";
		// 1コメントのHTML作成
		ViewData += mkHtmlTlList("1","0",b);
		// 返信元IDを設定
		ViewData += "<img src=\"/images/dummy.png\" style=\"height:0px;width:0px\" onload=\"viewTweetRep('"+b.get("replyToStatusId")+"');\"/>";
		return ViewData;
	}

	// ========================================================== //
	// [Twitter]通常TL、他人のツイート一覧作成                    //
	// ========================================================== //
	private String mkHtmlTl(List<BeanMap> data){
		ViewData = "";
		// 更新ボタンエリア
		ViewData += mkHtmlChkNew("0");
		ViewData += "<div id=\"twitterBody\">";
		// 最新のステータスIDのセット
		ViewData += mkHtmlsetNewSid();
		// 最新コメント表示エリア
		ViewData += "<div id=\"twdivNewBody\"><div id=\"twdivNew\"></div></div>";
		ViewData += "<div id=\"twdivTop\">";
		if(data.size() > 0){
			for(BeanMap b:data){
				// 1コメントのHTML作成
				ViewData += mkHtmlTlList("1","1",b);
			}
		}else{
			ViewData += "<div class=\"twListBox\"><div style=\"padding-top:20px;padding-bottom:20px;text-align:center;\">まだありません。</div></div>";
		}
		ViewData += "</div>";
		ViewData += "</div>";
		ViewData += "<div id=\"twdivBottomBody\">";
		ViewData += "<div id=\"twdivBottom\"></div>";
		ViewData += "<div class=\"morelinkArea\" id=\"twmorelink\">";
		// もっと読むリンク判定
		if(topForm.tlMoreFlg==true){
			ViewData += mkHtmlMoreLink("0",topForm.twMaxOldId.toString());

		}else{
			ViewData += "<span style=\"color:#cccccc;\">もっと読む</span>";
		}
		ViewData += "</div>";
		// 読み込み完了ファンクション実行用画像(onloadアクションを利用する)
		ViewData += mkHtmlTlLoadComplete();
		ViewData += "</div>";
		return ViewData;
	}

	// ========================================================== //
	// [Twitter]自分のツイート一覧作成                            //
	// ========================================================== //
	private String mkHtmlMyTl(List<BeanMap> data){
		ViewData = "";
		// 更新ボタンエリア
		ViewData += mkHtmlChkNew("1");
		ViewData += "<div id=\"twitterBody\">";
		// 最新のステータスIDのセット
		ViewData += mkHtmlsetNewSid();
		// 最新コメント表示エリア
		ViewData += "<div id=\"twdivNewBody\"><div id=\"twdivNew\"></div></div>";
		ViewData += "<div id=\"twdivTop\">";
		if(data.size() > 0){
			for(BeanMap b:data){
				// 1コメントのHTML作成
				ViewData += mkHtmlTlList("1","1",b);
			}
		}else{
			ViewData += "<div class=\"twListBox\"><div style=\"padding-top:20px;padding-bottom:20px;text-align:center;\">まだありません。</div></div>";
		}
		ViewData += "</div>";
		ViewData += "</div>";
		ViewData += "<div id=\"twdivBottomBody\">";
		ViewData += "<div id=\"twdivBottom\"></div>";
		ViewData += "<div class=\"morelinkArea\" id=\"twmorelink\">";
		// ギャップ判定(ギャップリンク追加)
		if(topForm.gapFlg){
			ViewData += mkHtmlGap("0",topForm.twMaxOldId.toString());
		}
		// もっと読むリンク判定
		if(topForm.tlMoreFlg==true){
			ViewData += mkHtmlMoreLink("1",topForm.twMaxOldId.toString());
		}else{
			ViewData += "<span style=\"color:#cccccc;\">もっと読む</span>";
		}
		ViewData += "</div>";
		// 読み込み完了ファンクション実行用画像(onloadアクションを利用する)
		ViewData += mkHtmlTlLoadComplete();
		ViewData += "</div>";
		return ViewData;
	}

	// ========================================================== //
	// [Twitter]メンション作成                                    //
	// ========================================================== //
	private String mkHtmlMention(List<BeanMap> data){
		ViewData = "";
		// 更新ボタンエリア
		ViewData += mkHtmlChkNew("2");
		ViewData += "<div id=\"twitterBody\">";
		// 最新のステータスIDのセット
		ViewData += mkHtmlsetNewSid();
		// 最新コメント表示エリア
		ViewData += "<div id=\"twdivNewBody\"><div id=\"twdivNew\"></div></div>";
		ViewData += "<div id=\"twdivTop\">";
		if(data.size() > 0){
			for(BeanMap b:data){
				// 1コメントのHTML作成
				ViewData += mkHtmlTlList("1","1",b);
			}
		}else{
			ViewData += "<div class=\"twListBox\"><div style=\"padding-top:20px;padding-bottom:20px;text-align:center;\">まだありません。</div></div>";
		}
		ViewData += "</div>";
		ViewData += "</div>";
		ViewData += "<div id=\"twdivBottomBody\">";
		ViewData += "<div id=\"twdivBottom\"></div>";
		ViewData += "<div class=\"morelinkArea\" id=\"twmorelink\">";
		// ギャップ判定(ギャップリンク追加)
		if(topForm.gapFlg){
			ViewData += mkHtmlGap("1",topForm.twMaxOldId.toString());
		}
		// もっと読むリンク判定
		if(topForm.tlMoreFlg==true){
			ViewData += mkHtmlMoreLink("2",topForm.twMaxOldId.toString());
		}else{
			ViewData += "<span style=\"color:#cccccc;\">もっと読む</span>";
		}
		ViewData += "</div>";
		// 読み込み完了ファンクション実行用画像(onloadアクションを利用する)
		ViewData += mkHtmlTlLoadComplete();
		ViewData += "</div>";
		return ViewData;
	}

	// ========================================================== //
	// [Twitter]お気に入り作成                                    //
	// ========================================================== //
	private String mkHtmlFavorite(List<BeanMap> data){
		ViewData = "";
		// 更新ボタンエリア
		ViewData += mkHtmlChkNew("3");
		ViewData += "<div id=\"twitterBody\">";
		// 最新のステータスIDのセット
		ViewData += mkHtmlsetNewSid();
		// 最新コメント表示エリア
		ViewData += "<div id=\"twdivNewBody\"><div id=\"twdivNew\"></div></div>";
		ViewData += "<div id=\"twdivTop\">";
		if(data.size() > 0){
			for(BeanMap b:data){
				// 1コメントのHTML作成
				ViewData += mkHtmlTlList("1","1",b);
			}
		}else{
			ViewData += "<div class=\"twListBox\"><div style=\"padding-top:20px;padding-bottom:20px;text-align:center;\">まだありません。</div></div>";
		}
		ViewData += "</div>";
		ViewData += "</div>";
		ViewData += "<div id=\"twdivBottomBody\">";
		ViewData += "<div id=\"twdivBottom\"></div>";
		ViewData += "<div class=\"morelinkArea\" id=\"twmorelink\">";
		// ギャップ判定(ギャップリンク追加)
		if(topForm.gapFlg){
			ViewData += mkHtmlGap("2",topForm.twMaxOldId.toString());
		}
		// もっと読むリンク判定
		if(topForm.tlMoreFlg==true){
			ViewData += mkHtmlMoreLink("3",topForm.twMaxOldId.toString());
		}else{
			ViewData += "<span style=\"color:#cccccc;\">もっと読む</span>";
		}
		ViewData += "</div>";
		// 読み込み完了ファンクション実行用画像(onloadアクションを利用する)
		ViewData += mkHtmlTlLoadComplete();
		ViewData += "</div>";
		return ViewData;
	}

	// ========================================================== //
	// [Twitter]検索結果一覧作成                                  //
	// ========================================================== //
	private String mkHtmlSearch(List<BeanMap> data,String kwdTxt){
		ViewData = "";
		ViewData += "<div style=\"padding-top:5px;padding-bottom:5px; border-style:none none dotted none; border-width:1px; border-color:#000000;\">";
		ViewData += "「<b>" + CmnUtility.htmlSanitizing(kwdTxt) + "</b>」<br/>に一致するツイート一覧";
		ViewData += "</div>";
		// 更新ボタンエリア
		ViewData += mkHtmlChkNew("4");
		ViewData += "<div id=\"twitterBody\">";
		// 最新のステータスIDのセット
		ViewData += mkHtmlsetNewSid();
		// 最新コメント表示エリア
		ViewData += "<div id=\"twdivNewBody\"><div id=\"twdivNew\"></div></div>";
		ViewData += "<div id=\"twdivTop\">";
		if(data.size() > 0){
			for(BeanMap b:data){
				// 1コメントのHTML作成
				ViewData += mkHtmlTlList("3","1",b);
			}
		}else{
			// 検索結果が得られなかった時
			ViewData += "<div class=\"twListBox\"><div style=\"padding-top:20px;padding-bottom:20px;text-align:center;\">";
			ViewData += "「<b>" + CmnUtility.htmlSanitizing(kwdTxt) + "</b>」<br/>に一致するツイートは見つかりませんでした。";
			ViewData += "</div></div>";
		}
		ViewData += "</div>";
		ViewData += "</div>";
		ViewData += "<div id=\"twdivBottomBody\">";
		ViewData += "<div id=\"twdivBottom\"></div>";
		ViewData += "<div class=\"morelinkArea\" id=\"twmorelink\">";
		// もっと読むリンク判定
		if(topForm.tlMoreFlg==true){
			ViewData += mkHtmlMoreLink("4",topForm.twMaxOldId.toString());

		}else{
			ViewData += "<span style=\"color:#cccccc;\">もっと読む</span>";
		}
		ViewData += "</div>";
		// 読み込み完了ファンクション実行用画像(onloadアクションを利用する)
		ViewData += mkHtmlTlLoadComplete();
		ViewData += "</div>";
		return ViewData;
	}
	
	// ========================================================== //
	// [Twitter]検索結果一覧作成(FrontierDB版)                    //
	// ========================================================== //
	private String mkHtmlTwitSearch(long cnt,List<BeanMap> data,String kwdTxt){
		ViewData = "";
		ViewData += "<div style=\"padding-top:5px; padding-bottom:5px; border-style:none none dotted none; border-width:1px; border-color:#000000;\">";
		ViewData += "検索結果(";
		// 検索結果が0件の場合
		if(cnt==0){
			ViewData += "0件";
		} else {
			// 現在表示している件数の表示
			ViewData += topForm.VVOffset + 1 + "～";
			// 総件数の方が大きかったら、総件数を表示
			if((topForm.VVPgcnt + 1) * appDefDto.ST_MY_TL_LIST_MAX < cnt){
				ViewData += (topForm.VVPgcnt + 1) * appDefDto.ST_MY_TL_LIST_MAX;
			} else {
				ViewData += cnt;
			}
			ViewData += "/";
			ViewData += cnt + "件";
		}
		ViewData += ")";
		ViewData += "</br>";
		// 前へ・次へリンク生成
		ViewData += mkHtmlSearchPreNext(cnt);
		ViewData += "</div>";
		ViewData += "<div id=\"twitterBody\">";
		if(data.size() > 0){
			for(int i=0;i<data.size();i++){
				// 1コメントのHTML作成
				if(data.size()>i+1){
					// 下線あり
					ViewData += mkHtmlTlList("3","1",data.get(i));
				} else {
					// 下線なし
					ViewData += mkHtmlTlList("3","0",data.get(i));
				}
			}
		}else{
			// 検索結果が得られなかった時
			ViewData += "<div class=\"twListBox\"><div style=\"padding-top:20px;padding-bottom:20px;text-align:center;\">検索条件に一致するツイートは<br/>見つかりませんでした。</div></div>";
		}
		ViewData += "</div>";
		ViewData += "<div style=\"padding-top:5px; padding-bottom:5px; border-style:dotted none none none; border-width:1px; border-color:#000000;\">";
		// 前へ・次へリンク生成
		ViewData += mkHtmlSearchPreNext(cnt);
		ViewData += "</div>";
		return ViewData;
	}

	// ========================================================== //
	// [Twitter]TLフィールドに指定の1ツイートHTMLを作成           //
	// ========================================================== //
	private String mkHtmlTlTweet(BeanMap b){
		ViewData = "";
		if(b != null){
			// 返信元があるか判定
			if(!b.get("replyToStatusId").toString().equals("-1")){
				// ◆返信元がある場合
				// ツイートが取得出来ればHTML生成
				ViewData += "<div id=\"tweetNewView\">";
				// 返信元が存在すれば下線を表示
				// 1コメントのHTML作成
				ViewData += mkHtmlTlList("1","1",b);
				ViewData += "</div>";
				// 返信元が存在すればIDを設定
				ViewData += "<img src=\"/images/dummy.png\" style=\"height:0px;width:0px\" onload=\"viewTweetRep('"+b.get("replyToStatusId")+"');\"/>";
			} else {
				// ◆返信元がない場合
				// ツイートが取得出来ればHTML生成
				ViewData += "<div id=\"tweetNewView\">";
				// 1コメントのHTML作成
				ViewData += mkHtmlTlList("1","0",b);
				ViewData += "</div>";
			}
		} else {
			// 結果が得られなかった時
			ViewData += "<div class=\"twListBox\"><div style=\"padding-top:20px;padding-bottom:20px;text-align:center;\">返信元はありません。</div></div>";
		}
		return ViewData;
	}

	// ========================================================== //
	// [Twitter]リストTL作成                                      //
	// ========================================================== //
	private String mkHtmlListTl(List<BeanMap> data){
		ViewData = "";
		String ldc = "";
		// 送信時にエンコードした文字列をデコードする
		try {
			ldc = URLDecoder.decode(topForm.VVListDc, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		ViewData += "<div style=\"padding-top:5px;padding-bottom:5px; border-style:none none dotted none; border-width:1px; border-color:#000000;\">";
		ViewData += "<span class=\"twLink\" onclick=\"readTwListTL('" + topForm.screenName + "','" + topForm.VVListId + "','" + topForm.VVListNm + "','" + cnvCmntToView(ldc) + "');\" style=\"font-size:15px;font-weight:bold;\">";
		ViewData += topForm.VVListNm;
		ViewData += "</span>";
		// 説明文があれば表示
		if(!topForm.VVListDc.equals("")){
			ViewData += "<div style=\"width:100%;text-align:left;padding:5px;\">" + ldc + "</div>";
		}
		ViewData += "</div>";
		// 更新ボタンエリア
		ViewData += mkHtmlChkNew("5");
		ViewData += "<div id=\"twitterBody\">";
		// 最新のステータスIDのセット
		ViewData += mkHtmlsetNewSid();
		// 最新コメント表示エリア
		ViewData += "<div id=\"twdivNewBody\"><div id=\"twdivNew\"></div></div>";
		ViewData += "<div id=\"twdivTop\">";
		if(data.size() > 0){
			for(BeanMap b:data){
				// 1コメントのHTML作成
				ViewData += mkHtmlTlList("1","1",b);
			}
		}else{
			ViewData += "<div class=\"twListBox\"><div style=\"padding-top:20px;padding-bottom:20px;text-align:center;\">まだありません。</div></div>";
		}
		ViewData += "</div>";
		ViewData += "</div>";
		ViewData += "<div id=\"twdivBottomBody\">";
		ViewData += "<div id=\"twdivBottom\"></div>";
		ViewData += "<div class=\"morelinkArea\" id=\"twmorelink\">";
		// もっと読むリンク判定
		if(topForm.tlMoreFlg==true){
			ViewData += mkHtmlMoreLink("5",topForm.twMaxOldId.toString());

		}else{
			ViewData += "<span style=\"color:#cccccc;\">もっと読む</span>";
		}
		ViewData += "</div>";
		// 読み込み完了ファンクション実行用画像(onloadアクションを利用する)
		ViewData += mkHtmlTlLoadComplete();
		ViewData += "</div>";
		return ViewData;
	}

	// ========================================================== //
	// [Twitter]リスト作成                                        //
	// ========================================================== //
	private String mkHtmlList(String listType,PagableResponseList<UserList> data){
		// ------------------------
		// listType :
		//   0 : 自分が作ったリスト
		//   1 : ユーザのリスト
		// ------------------------
		ViewData = "";
		ViewData += "<div style=\"padding-top:5px; padding-bottom:5px; border-style:none none dotted none; border-width:1px; border-color:#000000;\">";
		ViewData += topForm.screenName;
		if(listType.equals("0")){
			ViewData += "の作成したリスト一覧";
		} else {
			ViewData += "の所属しているリスト一覧";
		}
		ViewData += "</br>";
		// 前へ・次へリンク生成
		ViewData += mkHtmlListPreNext(listType,data);
		ViewData += "</div>";
		ViewData += "<div id=\"twitterBody\">";
		if(data.size() > 0){
			// リストHTMLの出力
			for(int i=0;i<data.size();i++){
				UserList ul = data.get(i);
				User us = ul.getUser();
				ViewData += "<div class=\"twListBox\">";
				// 一番最後の行はラインをドット表示しない
				if(i+1 == data.size()){
					// 最終行(ラインをドットにしない)
					ViewData += "<div class=\"fshoutAreaTop\">";
				} else {
					// 下線ラインをドットにする
					ViewData += "<div class=\"fshoutAreaTop fshoutAreaTopLine\">";
				}
				ViewData += "<div class=\"bodycontentsArea bodycontentsAreaTop\">";
				ViewData += "<table style=\"width:100%;\">";
				ViewData += "<tr>";
				ViewData += "<td class=\"leftArea leftAreaTop\">";
				ViewData += "<ul><li class=\"leftNickname\">";
				ViewData += "<img src=\"" + us.getProfileImageURL() + "\" alt=\"" + us.getName() + "\" class=\"profileImg linkImg\" onclick=\"chgListTop('" + us.getScreenName() + "');\" />";
				ViewData += "</li></ul>";
				ViewData += "</td>";
				ViewData += "<td class=\"rightArea rightAreaTop\">";
				ViewData += "<div class=\"bodyFSArea\">";
				ViewData += "<ul><li>";
				// リスト名リンク
				ViewData += "<span class=\"fsSentences twLink\" onclick=\"readTwListTL('" + us.getScreenName() + "','" + ul.getId() + "','" + ul.getFullName() + "','" + cnvCmntToView(ul.getDescription()) + "');\">" + ul.getName() + "</span>";
				ViewData += "</li><li>";
				// 説明文があれば表示
				if(!ul.getDescription().equals("")){
					ViewData += "<span class=\"date\">[説明]&nbsp;</span>";
					ViewData += "<span class=\"fsSentences\" style=\"color:#000000;\">" + cnvCmntToView(ul.getDescription()) + "</span>";
					ViewData += "</li><li>";
				}
				ViewData += "<span class=\"date\" style=\"color:#000000;\">フォロー:" + ul.getMemberCount() + "</span>";
				ViewData += "&nbsp;/&nbsp;";
				ViewData += "<span class=\"date\" style=\"color:#000000;\">リストをフォロー:" + ul.getSubscriberCount() + "</span>";
				ViewData += "</li></ul>";
				ViewData += "</div>";
				ViewData += "</td></tr></table>";
				ViewData += "</div></div></div>";
			}
		}else{
			ViewData += "<div class=\"twListBox\"><div style=\"padding-top:20px;padding-bottom:20px;text-align:center;\">まだありません。</div></div>";
		}
		ViewData += "</div>";
		ViewData += "<div style=\"padding-top:5px; padding-bottom:5px; border-style:dotted none none none; border-width:1px; border-color:#000000;\">";
		// 前へ・次へリンク生成
		ViewData += mkHtmlListPreNext(listType,data);
		ViewData += "</div>";
		// 読み込み完了ファンクション実行用画像(onloadアクションを利用する)
		ViewData += mkHtmlTlLoadComplete();
		return ViewData;
	}

	// ========================================================== //
	// [Twitter]更新ボタン押下時のコメント一覧作成                //
	// ========================================================== //
	// 通常のTL、ユーザのTL、リストのTL
	private String mkHtmlNew(List<BeanMap> data){
		ViewData = "";
		// 最新のステータスIDのセット
		ViewData += mkHtmlsetNewSid();
		ViewData += "<div id=\"twdivNew\">";
		for(BeanMap b:data){
			// 1コメントのHTML作成
			ViewData += mkHtmlTlList("0","1",b);
		}
		ViewData += "</div>";
		return ViewData;
	}

	// 自分のTL
	private String mkHtmlNewMy(List<BeanMap> data,long stFrom,long stTo){
		ViewData = "";
		// 最新のステータスIDのセット
		ViewData += mkHtmlsetNewSid();
		ViewData += "<div id=\"twdivNew\">";
		for(BeanMap b:data){
			// 1コメントのHTML作成
			ViewData += mkHtmlTlList("0","1",b);
		}
		// ギャップ判定(ギャップリンク追加)
		if(topForm.gapFlg){
			ViewData += mkHtmlGapNew("0",stFrom,stTo);
		}
		ViewData += "</div>";
		return ViewData;
	}

	// メンション
	private String mkHtmlNewMention(List<BeanMap> data,long stFrom,long stTo){
		ViewData = "";
		// 最新のステータスIDのセット
		ViewData += mkHtmlsetNewSid();
		ViewData += "<div id=\"twdivNew\">";
		for(BeanMap b:data){
			// 1コメントのHTML作成
			ViewData += mkHtmlTlList("0","1",b);
		}
		// ギャップ判定(ギャップリンク追加)
		if(topForm.gapFlg){
			ViewData += mkHtmlGapNew("1",stFrom,stTo);
		}
		ViewData += "</div>";
		return ViewData;
	}

	// お気に入り
	private String mkHtmlNewFavorite(List<BeanMap> data,long stFrom,long stTo){
		ViewData = "";
		// 最新のステータスIDのセット
		ViewData += mkHtmlsetNewSid();
		ViewData += "<div id=\"twdivNew\">";
		for(BeanMap b:data){
			// 1コメントのHTML作成
			ViewData += mkHtmlTlList("0","1",b);
		}
		// ギャップ判定(ギャップリンク追加)
		if(topForm.gapFlg){
			ViewData += mkHtmlGapNew("2",stFrom,stTo);
		}
		ViewData += "</div>";
		return ViewData;
	}

	// 検索結果
	private String mkHtmlNewSearch(List<BeanMap> data){
		ViewData = "";
		// 最新のステータスIDのセット
		ViewData += mkHtmlsetNewSid();
		ViewData += "<div id=\"twdivNew\">";
		for(BeanMap b:data){
			// 1コメントのHTML作成
			ViewData += mkHtmlTlList("2","1",b);
		}
		ViewData += "</div>";
		return ViewData;
	}
	// ========================================================== //
	// [Twitter]ギャップ解消                                      //
	// ========================================================== //
	// 自分のTL
	private String mkHtmlGapNewMyTl(List<BeanMap> data,long stFrom,long stTo){
		ViewData = "";
		for(BeanMap b:data){
			// 1コメントのHTML作成
			ViewData += mkHtmlTlList("0","1",b);
		}
		// ギャップ判定(ギャップリンク追加)
		if(topForm.gapFlg){
			ViewData += mkHtmlGapNew("0",stFrom,stTo);
		}
		return ViewData;
	}

	// メンション
	private String mkHtmlGapNewMention(List<BeanMap> data,long stFrom,long stTo){
		ViewData = "";
		for(BeanMap b:data){
			// 1コメントのHTML作成
			ViewData += mkHtmlTlList("0","1",b);
		}
		// ギャップ判定(ギャップリンク追加)
		if(topForm.gapFlg){
			ViewData += mkHtmlGapNew("1",stFrom,stTo);
		}
		return ViewData;
	}

	// お気に入り
	private String mkHtmlGapNewFavorite(List<BeanMap> data,long stFrom,long stTo){
		ViewData = "";
		for(BeanMap b:data){
			// 1コメントのHTML作成
			ViewData += mkHtmlTlList("0","1",b);
		}
		// ギャップ判定(ギャップリンク追加)
		if(topForm.gapFlg){
			ViewData += mkHtmlGapNew("2",stFrom,stTo);
		}
		return ViewData;
	}
	// ========================================================== //
	// [Twitter]もっと読む押下時コメント一覧作成                  //
	// ========================================================== //
	// 通常TL、ユーザのTL
	private String mkHtmlMore(List<BeanMap> data){
		ViewData = "";
		ViewData += "<div id=\"twdivBottomBody\">";
		ViewData += "<div id=\"twdivBottom\">";
		for(BeanMap b:data){
			// 1コメントのHTML作成
			ViewData += mkHtmlTlList("1","1",b);
		}
		ViewData += "</div>";
		ViewData += "<div class=\"morelinkArea\" id=\"twmorelink\">";
		// もっと読むリンク判定
		if(topForm.tlMoreFlg==true){
			ViewData += mkHtmlMoreLink("0",topForm.twMaxOldId.toString());
		}else{
			ViewData += "<span style=\"color:#cccccc;\">もっと読む</span>";
		}
		ViewData += "</div>";
		ViewData += "</div>";
		return ViewData;
	}

	// 自分のTL
	private String mkHtmlMoreMy(List<BeanMap> data){
		ViewData = "";
		ViewData += "<div id=\"twdivBottomBody\">";
		ViewData += "<div id=\"twdivBottom\">";
		for(BeanMap b:data){
			// 1コメントのHTML作成
			ViewData += mkHtmlTlList("1","1",b);
		}
		ViewData += "</div>";
		// ギャップ判定(ギャップリンク追加)
		if(topForm.gapFlg){
			ViewData += mkHtmlGap("0",topForm.twMaxOldId.toString());
		}
		ViewData += "<div class=\"morelinkArea\" id=\"twmorelink\">";
		// もっと読むリンク判定
		if(topForm.tlMoreFlg==true){
			ViewData += mkHtmlMoreLink("1",topForm.twMaxOldId.toString());
		}else{
			ViewData += "<span style=\"color:#cccccc;\">もっと読む</span>";
		}
		ViewData += "</div>";
		ViewData += "</div>";
		return ViewData;
	}

	// メンション
	private String mkHtmlMoreMention(List<BeanMap> data){
		ViewData = "";
		ViewData += "<div id=\"twdivBottomBody\">";
		ViewData += "<div id=\"twdivBottom\">";
		for(BeanMap b:data){
			// 1コメントのHTML作成
			ViewData += mkHtmlTlList("1","1",b);
		}
		ViewData += "</div>";
		// ギャップ判定(ギャップリンク追加)
		if(topForm.gapFlg){
			ViewData += mkHtmlGap("1",topForm.twMaxOldId.toString());
		}
		ViewData += "<div class=\"morelinkArea\" id=\"twmorelink\">";
		// もっと読むリンク判定
		if(topForm.tlMoreFlg==true){
			ViewData += mkHtmlMoreLink("2",topForm.twMaxOldId.toString());
		}else{
			ViewData += "<span style=\"color:#cccccc;\">もっと読む</span>";
		}
		// 読み込み完了ファンクション実行用画像(onloadアクションを利用する)
		ViewData += mkHtmlPrfLoadComplete();
		ViewData += mkHtmlTlLoadComplete();
		ViewData += "</div>";
		ViewData += "</div>";
		return ViewData;
	}

	// お気に入り
	private String mkHtmlMoreFavorite(List<BeanMap> data){
		ViewData = "";
		ViewData += "<div id=\"twdivBottomBody\">";
		ViewData += "<div id=\"twdivBottom\">";
		for(BeanMap b:data){
			// 1コメントのHTML作成
			ViewData += mkHtmlTlList("1","1",b);
		}
		ViewData += "</div>";
		// ギャップ判定(ギャップリンク追加)
		if(topForm.gapFlg){
			ViewData += mkHtmlGap("2",topForm.twMaxOldId.toString());
		}
		ViewData += "<div class=\"morelinkArea\" id=\"twmorelink\">";
		// もっと読むリンク判定
		if(topForm.tlMoreFlg==true){
			ViewData += mkHtmlMoreLink("3",topForm.twMaxOldId.toString());
		}else{
			ViewData += "<span style=\"color:#cccccc;\">もっと読む</span>";
		}
		ViewData += "</div>";
		ViewData += "</div>";
		return ViewData;
	}

	// 検索結果
	private String mkHtmlMoreSearch(List<BeanMap> data){
		ViewData = "";
		ViewData += "<div id=\"twdivBottomBody\">";
		ViewData += "<div id=\"twdivBottom\">";
		for(BeanMap b:data){
			// 1コメントのHTML作成
			ViewData += mkHtmlTlList("3","1",b);
		}
		ViewData += "</div>";
		ViewData += "<div class=\"morelinkArea\" id=\"twmorelink\">";
		// もっと読むリンク判定
		if(topForm.tlMoreFlg==true){
			ViewData += mkHtmlMoreLink("4",topForm.twMaxOldId.toString());
		}else{
			ViewData += "<span style=\"color:#cccccc;\">もっと読む</span>";
		}
		ViewData += "</div>";
		ViewData += "</div>";
		return ViewData;
	}

	// リストTL
	private String mkHtmlMoreListTl(List<BeanMap> data){
		ViewData = "";
		ViewData += "<div id=\"twdivBottomBody\">";
		ViewData += "<div id=\"twdivBottom\">";
		for(BeanMap b:data){
			// 1コメントのHTML作成
			ViewData += mkHtmlTlList("1","1",b);
		}
		ViewData += "</div>";
		ViewData += "<div class=\"morelinkArea\" id=\"twmorelink\">";
		// もっと読むリンク判定
		if(topForm.tlMoreFlg==true){
			ViewData += mkHtmlMoreLink("5",topForm.twMaxOldId.toString());
		}else{
			ViewData += "<span style=\"color:#cccccc;\">もっと読む</span>";
		}
		ViewData += "</div>";
		ViewData += "</div>";
		return ViewData;
	}
	// ========================================================== //
	// [Twitter]TLの1行作成                                       //
	// ========================================================== //
	private String mkHtmlTlList(String tweetType,String LineDottFlg,BeanMap bm){
		// ------------------------
		// tweetType :
		//   0 : 新着
		//   1 : 通常
		//   2 : 新着(検索結果)
		//   3 : 通常(検索結果)
		// ------------------------
		String rtnVal = "";
		// 大外のスタイル設定
		if (bm.get("myMentionFlg").toString().equals("1")){
			// メンションならばメンションのスタイルシート
			rtnVal += "<div class=\"twListBoxMention\"";
		} else {
			// 自分の発言かどうか判断
			if (bm.get("user_id").toString().equals(topForm.userId.toString())){
				rtnVal += "<div class=\"twListBoxMy\"";
			} else {
				// スタイルシート切り分け(0、2の時新着)
				if(tweetType.equals("0") || tweetType.equals("2")){
					rtnVal += "<div class=\"twListBoxNew\"";
				} else {
					rtnVal += "<div class=\"twListBox\"";
				}
			}
		}
		// 上記DIVの閉じタグ＋ID
		rtnVal += "id=\"tweetDiv"+bm.get("id")+"\">";
		rtnVal += "<div class=\"fshoutAreaTop";
		// 下線部分のドット線が必要かどうか
		if(LineDottFlg.equals("1")){
			rtnVal += " fshoutAreaTopLine";
		}
		rtnVal += "\">";
		rtnVal += "<div class=\"fshoutAreaTop\">";
		rtnVal += "<div class=\"bodycontentsArea bodycontentsAreaTop\">";
		rtnVal += "<table style=\"width:100%;\">";
		rtnVal += "<tr>";
		rtnVal += "<td class=\"leftArea leftAreaTop\">";
		rtnVal += "<ul>";
		rtnVal += "<li class=\"leftNickname\">";
		rtnVal += "<img src=\""+bm.get("profileImageUrl")+"\" alt=\""+bm.get("name")+"\" class=\"profileImg linkImg\" onclick=\"chgListTop('"+bm.get("screenName")+"');\" />";
		rtnVal += "</li></ul>";
		rtnVal += "</td>";
		rtnVal += "<td class=\"rightArea rightAreaTop\">";
		rtnVal += "<div class=\"bodyFSArea\">";
		rtnVal += "<ul><li>";
		rtnVal += "<span class=\"fsSentences twLink\" onclick=\"chgListTop('"+bm.get("screenName")+"');\">"+bm.get("screenName")+"</span>";
		rtnVal += "</li><li>";
		rtnVal += "<span class=\"fsSentences\" style=\"color:#000000;\">"+bm.get("viewCommentTl")+"</span>";
		rtnVal += "</li><li>";
		rtnVal += "<span class=\"date twLink\" onclick=\"viewTweet('"+bm.get("id")+"');\">"+bm.get("createdAt")+"</span>";
		rtnVal += "<span class=\"dateArea\" style=\"margin-left:9px;\">";
		rtnVal += "<img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/link_s.png\" alt=\"RT\" title=\"RT\" onclick=\"onSetFocus('rt','"+bm.get("screenName")+"','"+bm.get("viewCommentTlAlt")+"','','','"+bm.get("id")+"');\" style=\"cursor:pointer;\" />";
		rtnVal += "</span>";
		// 自分のツイートか、そうじゃないかの判断
		if(bm.get("user_id").toString().equals(topForm.userId.toString())){
			rtnVal += "<span class=\"dateArea\" id=\"delspan"+bm.get("id")+"\" style=\"margin-left:9px;\">";
			rtnVal += "<img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/ico_ashcan1_9.gif\" onclick=\"delTweet('"+bm.get("id")+"');\" title=\"削除\" alt=\"削除\" style=\"cursor:pointer;\"/>";
			rtnVal += "</span>";
		}else{
			rtnVal += "<span class=\"dateArea\" style=\"margin-left:9px;\">";
			rtnVal += "<img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/arrow_redo_s.png\" onclick=\"onSetFocus('redo','"+bm.get("screenName")+"','"+bm.get("viewCommentTlRe")+"','','','"+bm.get("id")+"');\" title=\"返信\" alt=\"返信\" style=\"cursor:pointer;\"/>";
			rtnVal += "</span>";
		}
		// 検索結果の時はお気に入り情報が取得できないので非表示
		if(!tweetType.equals("2") && !tweetType.equals("3")){
			// お気に入りエリア
			rtnVal += "<span class=\"dateArea\" id=\"favspan"+bm.get("id")+"\" style=\"margin-left:9px;\">";
			if(bm.get("favorited").equals(false)){
				// 登録
				rtnVal += mkHtmlInsFavorite(bm.get("id").toString());
			}else{
				// 解除
				rtnVal += mkHtmlDelFavorite(bm.get("id").toString());
			}
			rtnVal += "</span>";
		}
		rtnVal += "</li></ul></div></td></tr></table></div></div></div></div>";
		return rtnVal;
	}

	// ========================================================== //
	// [Twitter]もっと読むリンクHTMLの生成                        //
	// ========================================================== //
	private String mkHtmlMoreLink(String getType,String getStatus){
		// ------------------------
		// gapType :
		//   0 : 通常、ユーザのTL
		//   1 : 自分のTL
		//   2 : メンション
		//   3 : お気に入り
		//   4 : 検索結果
		//   5 : リストTL
		// ------------------------
		String rtnVal = "";
		rtnVal += "<span style=\"text-decoration:underline; color:#0000ff; cursor:pointer;\" onClick=\"getAddition('"+getType+"','"+getStatus+"')\">もっと読む</span>";
		return rtnVal;
	}

	// ========================================================== //
	// [Twitter]ギャップリンクHTMLの生成(新着版)                  //
	// ========================================================== //
	private String mkHtmlGapNew(String gapType,long stFrom,long stTo){
		// ------------------------
		// gapType :
		//   0 : 自分のTL
		//   1 : メンション
		//   2 : お気に入り
		// ------------------------
		// stFrom:ステータスID From
		// stTo:ステータスID To
		// 上記のFrom ～ Toの間のギャップを取得するFunctionを呼ぶ
		String rtnVal = "";
		rtnVal += "<div id=\"twiDivGap"+topForm.cntGapCnt.toString()+"\" style=\"margin:10px;padding:10px;height:20px;width:220px;background-color:#C0C0C0;\">";
		rtnVal += "<span style=\"color:#0000ff; font-weight:bold; text-decoration:underline; cursor:pointer;\" onClick=\"JavaScript:getGapNew('" + gapType + "','twiDivGap"+topForm.cntGapCnt.toString()+"','"+stFrom+"','"+stTo+"');;\">";
		rtnVal += "ギャップを取得する";
		rtnVal += "</span>";
		rtnVal += "</div>";
		// ギャップカウント+1
		topForm.cntGapCnt += 1;
		return rtnVal;
	}

	// ========================================================== //
	// [Twitter]ギャップリンクHTMLの生成(もっと読む版)            //
	// ========================================================== //
	private String mkHtmlGap(String gapType,String getStatus){
		// ------------------------
		// gapType :
		//   0 : 自分のTL
		//   1 : メンション
		//   2 : お気に入り
		// ------------------------
		String rtnVal = "";
		rtnVal += "<div style=\"margin:10px;padding:10px;height:20px;width:220px;background-color:#C0C0C0;\">";
		rtnVal += "<span style=\"color:#0000ff; font-weight:bold; text-decoration:underline; cursor:pointer;\" onClick=\"JavaScript:getGap('"+gapType+"','"+getStatus+"');\">";
		rtnVal += "ギャップを取得する";
		rtnVal += "</span>";
		rtnVal += "</div>";
		return rtnVal;
	}

	// ========================================================== //
	// [Twitter & FShout]文字数カウントHTMLの生成                 //
	// ========================================================== //
	private String mkHtmlTxtCnt(String getFtxt,String getTtxt){
		String rtnVal = "";
		rtnVal += "<div style=\"text-align:left;\">";
		rtnVal += "&nbsp;&nbsp;";
		// Twitter設定していなければレイアウトを変える
		// 設定あり -> [FShout]XX文字 [Twitter]YY文字
		// 設定なし -> XX文字
		if(!getTtxt.equals("")){
			rtnVal += "[FShout]&nbsp;&nbsp;";
			rtnVal += getFtxt.length() + "文字";
			rtnVal += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
			rtnVal += "[Twitter]&nbsp;&nbsp;";
			rtnVal += getTtxt.length() + "文字";
		} else {
			rtnVal += getFtxt.length() + "文字";
		}
		rtnVal += "</div>";
		return rtnVal;
	}

	// ====================== //
	//  ▼ HTMLのパーツ集 ▼  //
	// ====================== //
	// 自動更新ボタン用HTML
	private String mkHtmlChkNew(String updType){
		// ------------------------
		// updType :
		//   0 : 通常、他人のTL
		//   1 : 自分のTL
		//   2 : メンション
		//   3 : お気に入り
		//   4 : 検索結果
		//   5 : リストTL
		// ------------------------
		String rtnVal = "";
		rtnVal += "<div style=\"padding-top:5px; padding-bottom:5px; border-style:none none dotted none; border-width:1px; border-color:#000000;\">";
		rtnVal += "<input type=\"button\" name=\"exeInsRss\" value=\"更新\" class=\"twbtn\" onClick=\"getNew(" + updType + ");\" />";
		// 自動更新ファンクション仕込み
		if(topForm.updateAutoSec != 0){
			rtnVal += "<img src=\"/images/dummy.png\" style=\"height:0px;width:1px\" onload=\"chkFnk = setInterval('getNew(" + updType + ")'," + topForm.updateAutoSec + ");\"/>";
		}
		rtnVal += "</div>";
		return rtnVal;
	}

	// TL読み込み終了時の新着メンションチェックファンクション仕込み(プロフィール)
	private String mkHtmlPrfLoadComplete(){
		String rtnVal = "";
		rtnVal += "<img src=\"/images/dummy.png\" onload=\"PrfLoadComplete();\"/>";
		return rtnVal;
	}

	// TL読み込み終了時の新着メンションチェックファンクション仕込み
	private String mkHtmlTlLoadComplete(){
		String rtnVal = "";
		rtnVal += "<img src=\"/images/dummy.png\" onload=\"TlLoadComplete();\"/>";
		return rtnVal;
	}

	// リストの前へ、次へリンクエリア作成
	private String mkHtmlListPreNext(String listType,PagableResponseList<UserList> data){
		// ------------------------
		// listType :
		//   0 : 自分が作ったリスト
		//   1 : ユーザのリスト
		// ※JavaScriptのfunctionに渡すパラメタ
		// ------------------------
		String rtnVal = "";
		rtnVal += "<div>";
		// 前へ・次へ判定
		if(data.hasPrevious() && data.hasNext()){
			// 前ページも次ページも存在する場合
			rtnVal += "<span style=\"color:#0000ff; text-decoration:underline; cursor:pointer;\" onClick=\"readTwListPreNext('" + listType + "','" + topForm.screenName + "','" + data.getPreviousCursor() + "')\">&lt;&lt;前へ</span>";
			rtnVal += "&nbsp;|&nbsp;";
			rtnVal += "<span style=\"color:#0000ff; text-decoration:underline; cursor:pointer;\" onClick=\"readTwListPreNext('" + listType + "','" + topForm.screenName + "','" + data.getNextCursor() + "')\">次へ&gt;&gt;</span>";
		} else if(!data.hasPrevious() && !data.hasNext()){
			// 前ページも次ページも存在しない場合
			// 前へ・次へ
			rtnVal += "<span style=\"color:#c0c0c0;\">&lt;&lt;前へ&nbsp;|&nbsp;次へ&gt;&gt;</span>";
		} else {
			// 前ページ、次ページのどちらかが存在する場合
			// 前へ
			if(data.hasPrevious()){
				rtnVal += "<span style=\"color:#0000ff; text-decoration:underline; cursor:pointer;\" onClick=\"readTwListPreNext('" + listType + "','" + topForm.screenName + "','" + data.getPreviousCursor() + "')\">&lt;&lt;前へ</span>";
			} else {
				rtnVal += "<span style=\"color:#c0c0c0;\">&lt;&lt;前へ</span>";
			}
			rtnVal += "&nbsp;|&nbsp;";
			// 次へ
			if(data.hasNext()){
				rtnVal += "<span style=\"color:#0000ff; text-decoration:underline; cursor:pointer;\" onClick=\"readTwListPreNext('" + listType + "','" + topForm.screenName + "','" + data.getNextCursor() + "')\">次へ&gt;&gt;</span>";
			} else {
				rtnVal += "<span style=\"color:#c0c0c0;\">次へ&gt;&gt;</span>";
			}
		}
		rtnVal += "</div>";
		return rtnVal;
	}

	// 検索結果の前へ、次へリンクエリア作成
	private String mkHtmlSearchPreNext(long cnt){
		// 計算
		// 総数を1ページで割る
		long pgMaxCnt = cnt / appDefDto.ST_MY_TL_LIST_MAX;
		// 余りがあれば+1
		if(cnt % appDefDto.ST_MY_TL_LIST_MAX > 0){
			pgMaxCnt += 1;
		}
		String rtnVal = "";
		rtnVal += "<div>";
		// 前へ判定
		if(topForm.VVPgcnt <= 0){
			rtnVal += "<span style=\"color:#c0c0c0;\">&lt;&lt;前へ</span>";
		} else {
			rtnVal += "<span style=\"color:#0000ff; text-decoration:underline; cursor:pointer;\" onClick=\"mvTwitSearchPreNext('" + (topForm.VVPgcnt - 1) + "');\">&lt;&lt;前へ</span>";
		}
		rtnVal += "&nbsp;|&nbsp;";
		// 次へ判定
		if(topForm.VVPgcnt + 1 >= pgMaxCnt){
			rtnVal += "<span style=\"color:#c0c0c0;\">次へ&gt;&gt;</span>";
		} else {
			rtnVal += "<span style=\"color:#0000ff; text-decoration:underline; cursor:pointer;\" onClick=\"mvTwitSearchPreNext('" + (topForm.VVPgcnt + 1) + "');\">次へ&gt;&gt;</span>";
		}
		rtnVal += "</div>";
		return rtnVal;
	}

	// 最新のステータスIDセット用HTML
	private String mkHtmlsetNewSid(){
		String rtnVal = "";
		rtnVal += "<img src=\"/images/dummy.png\" style=\"height:0px;width:1px\" onload=\"document.forms[0].MstNewSid.value='" + topForm.twMaxNewId + "';\"/>";
		return rtnVal;
	}

	// お気に入り、登録用HTML
	private String mkHtmlInsFavorite(String targetid){
		String rtnVal = "";
		rtnVal += "<img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/favorite_black.png\" onclick=\"updFavorite('0','" + targetid + "');\" title=\"お気に入り登録\" alt=\"お気に入り登録\" style=\"cursor:pointer;\"/>";
		return rtnVal;
	}

	// お気に入り、解除用HTML
	private String mkHtmlDelFavorite(String targetid){
		String rtnVal = "";
		rtnVal += "<img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/favorite.png\" onclick=\"updFavorite('1','" + targetid + "');\" title=\"お気に入り解除\" alt=\"お気に入り解除\" style=\"cursor:pointer;\"/>";
		return rtnVal;
	}

	// 自分のツイート削除用HTML
	private String mkHtmldelTweet(String targetid){
		String rtnVal = "";
		// onloadのタイミングで自分のDIVを消す処理
		rtnVal += "<img src=\"/images/dummy.png\" style=\"height:0px;width:1px\" onload=\"document.getElementById('tweetDiv"+targetid+"').style.display='none';\"/>";
		return rtnVal;
	}
}