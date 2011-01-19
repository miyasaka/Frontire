package frontier.action.pc.ajax;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.form.pc.CalendarForm;
import frontier.service.CalendarService;
import frontier.service.ClistService;
import frontier.service.CommonService;
import frontier.service.FriendListService;

public class CalendarAction {
	
	Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Resource
	protected CalendarService calendarService;
	@Resource
	protected FriendListService friendListService;
	@Resource
	protected ClistService clistService;
	@Resource
	protected CommonService commonService;
	public List<BeanMap> results;
	
	@Resource
	public UserInfoDto userInfoDto;
	
	
	@ActionForm
	@Resource
	protected CalendarForm calendarForm;
	
	@Resource
	public AppDefDto appDefDto;
	
	public String ViewData;
	public String LinkBox;
	long count;
	//内部制御用
	boolean setPosition=true;
	public List<BeanMap> FriendList;
    //カレンダーのリンク表示用
    protected List<BeanMap> monthResults;
    //カレンダー生成用
    public List<Map<String,Object>> cal;
    //ｲﾍﾞﾝﾄアイコン表示制御用
	public Integer communityCnt;
	
	@Execute(validator=false,urlPattern="loadData/{startyear}/{startmonth}/{startday}")
	public String loadData(){
		System.out.println("年"+calendarForm.startyear);
		System.out.println("月"+calendarForm.startmonth);
		// 初期値を設定
		calendarForm.offset = 0;
		calendarForm.pgcnt = 0;
		init();
		ViewData = makeViewData();
		CmnUtility.ResponseWrite(ViewData,"text/html","windows-31J","Shift_JIS");
		return null;
	}
	
	// init処理
	private void init(){
		System.out.println("ページ"+calendarForm.pgcnt);

		try {
			// offset設定
			calendarForm.offset = calendarForm.pgcnt*10;
		} catch (Exception e) {
			// エラーになった場合は初期値を設定
			calendarForm.offset = 0;
			calendarForm.pgcnt = 0;
			// TODO: handle exception
		}
		//コミュニティ件数取得
		calendarForm.resultscnt=calendarService.selectClist(userInfoDto.memberId,"0",0,0).size();
		// イベント作成可能コミュニティ一覧データ取得
		System.out.println("開始位置"+calendarForm.offset);
		results = calendarService.selectClist(userInfoDto.memberId,"0",calendarForm.offset,10);
	}
	
	//ｽｹｼﾞｭｰﾙ表示設定切り替え
	@Execute(validator=false,urlPattern="setDispSchedule/{defDisptypeCalendar}")
	public String setDispSchedule(){
		//カレンダー設定
		initCalendar();
		//設定情報更新
		calendarService.updScheduleDispSetting(userInfoDto.memberId,calendarForm.defDisptypeCalendar);
		CmnUtility.ResponseWrite(ViewData,"text/html","windows-31J","Shift_JIS");
		return null;
	}
	
	//カレンダー必須処理
	public void initCalendar(){
		monthResults = calendarService.selScheduleMonthList(userInfoDto.memberId,calendarForm.calendarDay,groupids(),calendarForm.defDisptypeCalendar);
		cal = CmnUtility.makeCustomCalendar2(calendarForm.calendarDay,monthResults);
		ViewData = makeViewDataCalendar(cal);
	}
	
	// ■ページング処理
	@Execute(validator=false)
	public String next(){
		calendarForm.pgcnt++;
		setPosition=false;
		// init処理
		init();
		ViewData = makeViewData();
		CmnUtility.ResponseWrite(ViewData,"text/html","windows-31J","Shift_JIS");
		// 一覧表示処理へ
		return null;
	}
	
	// ■ページング処理
	@Execute(validator=false)
	public String pre(){
		calendarForm.pgcnt--;
		setPosition=false;
		// init処理
		init();
		ViewData = makeViewData();
		CmnUtility.ResponseWrite(ViewData,"text/html","windows-31J","Shift_JIS");
		// 一覧表示処理へ
		return null;
	}
	
	public String makeViewData(){

		ViewData ="";
		LinkBox = "";
		int i=0;
		ViewData +="<div class=\"pEvent-frame\">";
		ViewData +="<div class=\"pEvent-main\">";
		ViewData +="<div class=\"pEvent-title\"><span>イベント作成</span><a href=\"javascript:closeEventWindow()\" title=\"閉じる\"><img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/p_close_rev.gif\" alt=\"閉じる\" /></a></div>";
		System.out.println(ViewData);
		makeLinkBox();
		ViewData +=LinkBox;
		
		ViewData +="<div class=\"pEvent-body\">";
		ViewData +="<table>";
		
		for(BeanMap b:results){
			//表示画像パス変換処理
			if(b.get("pic")==null){
				b.put("pic","/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/noimg42.gif");
			}else if(b.get("pic").equals("")){
				b.put("pic","/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/noimg42.gif");
			}else{
				b.put("pic",appDefDto.FP_CMN_CONTENTS_ROOT+b.get("pic").toString().replace("dir", "pic42"));
			}
			//タイトル名
			if(b.get("title")!=null){
				b.put("title",reStr(b.get("title").toString()));
			}
			//ツールチップ
			if(b.get("ltitle")!=null){
				b.put("ltitle",reStr(b.get("ltitle").toString()));
			}
			//コメント数
			if(b.get("memcnt")!=null){
				count = (Long)b.get("memcnt");
				if(count!=0){
					b.put("title",b.get("title")+"("+b.get("memcnt")+")");
					b.put("ltitle",b.get("ltitle")+"("+b.get("memcnt")+")");
				}
			}	
			ViewData +="<tr>";
			//イベントアイコンクリック時&&画像読み込み終了時
			if(i+1==results.size()&&setPosition){
				ViewData +="<td><img src=\""+b.get("pic")+"\" alt=\""+b.get("title")+"\" onload=\"setPosition();\"/></td>";
			}else{
				ViewData +="<td><img src=\""+b.get("pic")+"\" alt=\""+b.get("title")+"\" /></td>";
			}
			ViewData +="<td><a href=\"/frontier/pc/com/entevent/entry/"+b.get("cid")+"/"+calendarForm.startyear+"/"+calendarForm.startmonth+"/"+calendarForm.startday+""+"\" title=\""+b.get("ltitle")+"\">"+b.get("title")+"</a></td>";
			ViewData +="</tr>";
			i++;
		}		
		
		ViewData +="</table>";
		ViewData +="</div>";
		
		ViewData +=LinkBox;
		
		ViewData +="</div>";
		ViewData +="</div>";

//		ViewData +="<div style=\"width=200;border-width:1pt;border-style:solid;border-color:blue\">";
//		ViewData +="<ul>";
//		ViewData +="<li style=\"background-color:blue;\">";
//		ViewData +="<font color=\"white\">イベント作成</font>";
//		ViewData +="<a style=\"margin-left:118px;\" href=\"javascript:close()\">";
//		ViewData +="<img src=\"/images/p_close_rev.gif\" alt=\"閉じる\" />";
//		ViewData +="</a>";
//		ViewData +="</li>";
//		ViewData +="<li style=\"background-color:CC66FF;\">";
//		if(calendarForm.offset>0){
//			ViewData +="<a link href=\"javascript:pre()\">前へ</a>";
//		}
//		if(calendarForm.offset+1==maxsize){
//			ViewData +=calendarForm.resultscnt+"件";
//		}else{
//			ViewData +=calendarForm.offset+1+"～"+maxsize+"件";
//		}
//
//		if(calendarForm.resultscnt>maxsize){
//			ViewData +="<a link href=\"javascript:next()\">次へ</a>";
//		}
//		ViewData +="</li>";
//		ViewData +="</ul>";
//		for(BeanMap b:results){
//			//表示画像パス変換処理
//			if(b.get("pic")==null){
//				b.put("pic","/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/noimg42.gif");
//			}else if(b.get("pic").equals("")){
//				b.put("pic","/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/noimg42.gif");
//			}else{
//				b.put("pic",appDefDto.FP_CMN_CONTENTS_ROOT+b.get("pic").toString().replace("dir", "pic42"));
//			}
//			//タイトル名
//			if(b.get("title")!=null){
//				b.put("title",reStr(b.get("title").toString()));
//			}
//			//コメント数
//			if(b.get("memcnt")!=null){
//				count = (Long)b.get("memcnt");
//				if(count!=0){
//					b.put("title",b.get("title")+"("+b.get("memcnt")+")");
//				}
//			}
//			
//			
//			ViewData +="<li>";
//			//ViewData +="<a href=\"/frontier/pc/com/entevent/"+b.get("cid")+">";
//			ViewData +="<img src=\""+b.get("pic")+"\" alt=\""+b.get("title")+"\" />";
//			//ViewData +="</a>";
//			ViewData +="<a href=\"/frontier/pc/com/entevent/"+b.get("cid")+"\" style=\"margin-left:30px;text-align:middle;\">"+b.get("title")+"</a>";
//			ViewData +="</li>";
//		}
//		ViewData +="<li style=\"background-color:CC66FF;\">";
//		if(calendarForm.offset>0){
//			ViewData +="<a link href=\"javascript:pre()\">前へ</a>";
//		}
//		if(calendarForm.offset+1==maxsize){
//			ViewData +=calendarForm.resultscnt+"件";
//		}else{
//			ViewData +=calendarForm.offset+1+"～"+maxsize+"件";
//		}
//
//		if(calendarForm.resultscnt>maxsize){
//			ViewData +="<a link href=\"javascript:next()\">次へ</a>";
//		}
//		ViewData +="</li>";
//
//		ViewData +="</div>";


		return ViewData;
	}
	
	public void makeLinkBox(){
		int maxsize = calendarForm.offset+results.size();
		//前へ、次へリンク生成
		LinkBox +="<table class=\"LinkBox\"><tr><td width=\"30%\" style=\"text-align:right;\">";
		
		if(calendarForm.offset>0){
			LinkBox +="<a link href=\"javascript:pre()\">前へ</a>";
		}else{
			LinkBox +="&nbsp;";
		}
		LinkBox +="</td><td width=\"40%\" style=\"text-align:center\">";
		if(calendarForm.offset+1==maxsize){
			LinkBox +=calendarForm.resultscnt+"件";
		}else{
			LinkBox +=calendarForm.offset+1+"～"+maxsize+"件";
		}
		LinkBox +="</td><td>";
	
		if(calendarForm.resultscnt>maxsize){
			LinkBox +="<a link href=\"javascript:next()\">次へ</a>";
		}else{
			LinkBox +="&nbsp;";
		}
		LinkBox +="</td></tr></table>";
	}
	
	private String makeViewDataCalendar(List<Map<String, Object>> data){
		ViewData ="";
		ViewData +="<tr>";
		ViewData +="<th style=\"color:#ff0000;\">日</th>";
		ViewData +="<th>月</th>";
		ViewData +="<th>火</th>";
		ViewData +="<th>水</th>";
		ViewData +="<th>木</th>";
		ViewData +="<th>金</th>";
		ViewData +="<th style=\"color:#0085cc;\">土</th>";
		ViewData +="</tr>";
		
		
		
		ViewData +="<tr>";
		int cnt = 0;
		String youbi;
		for(Map<String, Object> b:data){
			
			//前の月のｽｹｼﾞｭｰﾙ
			if(b.get("before")!=null){
				ViewData +="<td class=\"before\">";
				ViewData +="<div class=\"caldiv1\">";
				ViewData +="<div class=\"divDay\">"+b.get("day")+"</div>";
				ViewData +="<div class=\"divContents\">";
				if(b.get("existSchedule")!=null){
					if(b.get("existSchedule").equals(1)){
						ViewData +=makeScheduleData(b.get("schedule"));
					}
				}
				ViewData +="</div></div></td>";
				logger.debug(b.get("day")+ViewData);
				
				
			//今月のｽｹｼﾞｭｰﾙ
			}else if(b.get("now")!=null){
				
				//曜日設定
				if(b.get("today")!=null){
					youbi ="today";
				}else if(b.get("week").equals(1)){
					youbi ="san";
				}else if(b.get("week").equals(7)){
					youbi ="sat";
				}else{
					youbi ="";
				}
				
				ViewData +="<td class=\""+youbi+"\">";
				logger.debug(b.get("day")+":"+b.get("today")+youbi);
				ViewData +="<div class=\"caldiv1\">";
				ViewData +="<div class=\"divDay\">"+b.get("day")+"</div>";
				
				ViewData +="<div class=\"divStatus\">";
				ViewData +="<a href=\"javascript:void(0)\" onClick=\"MM_openBrWindow('/frontier/pc/calendar/entry/"+b.get("detailDay").toString().substring(0,4)+"/"+b.get("detailDay").toString().substring(4,6)+"/"+b.get("detailDay").toString().substring(6,8)+"/2','','width=760,height=640,toolbar=no,scrollbars=yes,left=10,top=10')\" title=\"ｽｹｼﾞｭｰﾙ\"><img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/calendar2.png\" alt=\"ｽｹｼﾞｭｰﾙ\" /></a>";
				// 参加コミュニティ件数取得
				communityCnt = clistService.cntClist(userInfoDto.memberId,"0");
				if(communityCnt>0){
					ViewData +="<a href=\"javascript:void(0)\" onload=\"addEvent(this)\" onclick=\"handler(event,"+b.get("detailDay").toString().substring(0,4)+","+b.get("detailDay").toString().substring(4,6)+","+b.get("detailDay").toString().substring(6,8)+")\" title=\"イベント\"><img src=\"/static/style/"+appDefDto.FP_CMN_COLOR_TYPE+"/images/ico05-001.gif\" alt=\"イベント\" /></a>";
				}
				ViewData +="</div>";
				
				
				ViewData +="<div class=\"divContents\">";
				if(b.get("existSchedule")!=null){
					if(b.get("existSchedule").equals(1)){
						ViewData +=makeScheduleData(b.get("schedule"));
					}
				}
				ViewData +="</div></div></td>";
				if(b.get("week").equals(7)){
					ViewData +="</tr><tr>";
				}
			//来月のｽｹｼﾞｭｰﾙ
			}else if(b.get("next")!=null){
				ViewData +="<td class=\"next\">";
				ViewData +="<div class=\"caldiv1\">";
				ViewData +="<div class=\"divDay\">"+b.get("day")+"</div>";
				ViewData +="<div class=\"divContents\">";
				if(b.get("existSchedule")!=null){
					if(b.get("existSchedule").equals(1)){
						ViewData +=makeScheduleData(b.get("schedule"));
					}
				}
				ViewData +="</div></div></td>";
			}

//			logger.debug("exist"+b.get("existSchedule"));
//			if(b.get("existSchedule")!=null){
//				if(b.get("existSchedule").equals(1)){
//					//スケジュールリスト型変換
//					List<BeanMap> ScheduleListMap = new ArrayList<BeanMap>();
//					ScheduleListMap = (List<BeanMap>) b.get("schedule");
//					StringBuffer sb = new StringBuffer();
//					int scheduleCnt = 0;
//					
//					for(BeanMap scheduleMap:ScheduleListMap){
//						//ｽｹｼﾞｭｰﾙ名
//						if(scheduleMap.get("title")!=null){
//							scheduleMap.put("title",reStr(scheduleMap.get("title").toString()));
//							if(scheduleMap.get("starttime")==null && scheduleMap.get("endtime") ==null){
//								scheduleMap.put("title","&nbsp;"+scheduleMap.get("title"));
//							}else if(scheduleMap.get("starttime")!=null && scheduleMap.get("endtime")==null){
//									scheduleMap.put("title",scheduleMap.get("starttime")+"&nbsp;"+scheduleMap.get("title").toString());
//							}else if(scheduleMap.get("starttime")==null && scheduleMap.get("endtime")!=null){
//								scheduleMap.put("title","～"+scheduleMap.get("endtime")+"&nbsp;"+scheduleMap.get("title").toString());
//							}else{
//								scheduleMap.put("title",scheduleMap.get("starttime").toString()+scheduleMap.get("endtime")+"&nbsp;"+scheduleMap.get("title").toString());
//							}
//						}
//						sb.append("&nbsp;・"+scheduleMap.get("title"));
//						scheduleCnt++;
//						if(scheduleCnt!=ScheduleListMap.size()) sb.append("&#13;&#10;");
//					}
//					
//					if(b.get("today")!=null){
//						ViewData +="<td class=\"today\">";
//					}else if(b.get("before")!=null){
//						ViewData +="<td class=\"before\">";
//					}else if(b.get("next")!=null){
//						ViewData +="<td class=\"next\">";
//					}else if(b.get("week").equals(1)){
//						ViewData +="<td class=\"sun\">";
//					}else if(b.get("week").equals(7)){
//						ViewData +="<td class=\"sat\">";
//					}else{
//						ViewData +="<td class=\"mini\">";
//					}
//					ViewData +="<span><a href=\"/frontier/pc/calendar/20091020\" title=\""+sb+"\">";
//					ViewData +=b.get("day");
//					ViewData +="</s:link>";
//					ViewData +="</span>";
//				}
//			}else{
//				if(b.get("today")!=null){
//					ViewData +="<td class=\"today\">";
//				}else if(b.get("before")!=null){
//					ViewData +="<td class=\"before\">";
//				}else if(b.get("next")!=null){
//					ViewData +="<td class=\"next\">";
//				}else if(b.get("week").equals(1)){
//					ViewData +="<td class=\"sun\">";
//				}else if(b.get("week").equals(7)){
//					ViewData +="<td class=\"sat\">";
//				}else{
//					ViewData +="<td class=\"mini\">";
//				}
//				ViewData +="<span>";
//				ViewData +=b.get("day");
//				ViewData +="</span>";
//			}
//			ViewData +="</td>";
//			
//			logger.debug("week"+b.get("week"));
//			if(b.get("week").equals(7)){
//				logger.debug("true");
//				ViewData +="</tr><tr>";
//			}
//			cnt++;
//			logger.debug(cnt+"    "+data.size());
//			if(cnt==data.size()){
//				for(int i=0;i<7-Integer.parseInt(b.get("week").toString());i++){
//					ViewData +="<td class=\"next\"><span>"+(i+1)+"</span></td>";
//				}
//			}
			

		}
		logger.debug(ViewData);
		ViewData +="</tr>";
		
		
		return ViewData;
	}
	
	//ｽｹｼﾞｭｰﾙデータ生成
	public String makeScheduleData(Object scheduleData){
		
		//スケジュールリスト型変換
		List<BeanMap> ScheduleListMap = new ArrayList<BeanMap>();
		ScheduleListMap = (List<BeanMap>) scheduleData;

		String _ViewData="";
		StringBuffer sb = new StringBuffer();
		int scheduleCnt = 0;
		
		for(BeanMap scheduleMap:ScheduleListMap){
			logger.debug("タイトル"+scheduleMap.get("title"));
			
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
			
			//詳細
			if(scheduleMap.get("detail")!=null){
				scheduleMap.put("detail",reStr(scheduleMap.get("detail").toString()));
			}
			
			if(scheduleMap.get("entry").equals(1)||scheduleMap.get("entry").equals("1")){
				_ViewData +="<ul class=\"contEvent\">";
				_ViewData +="<li><a href=\"/frontier/pc/com/event/view/"+scheduleMap.get("cid")+"/"+scheduleMap.get("bbsid")+"\" title=\"("+scheduleMap.get("title")+")&#13;"+scheduleMap.get("detail")+"\" style=\"color:#"+scheduleMap.get("titlecolor")+";\">";
				_ViewData +=scheduleMap.get("title")+"</a></li></ul>";
			}else if(scheduleMap.get("entry").equals(2)||scheduleMap.get("entry").equals("2")){
				_ViewData +="<ul class=\"contSchedule\">";
				_ViewData +="<li><a title=\"("+scheduleMap.get("nickname")+")&#13;"+scheduleMap.get("detail")+"\" style=\"color:#"+scheduleMap.get("titlecolor")+";\" href=\"javascript:void(0)\" onClick=\"MM_openBrWindow('/frontier/pc/calendar/view/"+scheduleMap.get("sno")+"/"+scheduleMap.get("cid")+"/2','','width=760,height=640,toolbar=no,scrollbars=yes,left=10,top=10')\">";
				_ViewData +=scheduleMap.get("title")+"</a></li></ul>";
			}

			

//			sb.append("&nbsp;・"+scheduleMap.get("title"));
//			scheduleCnt++;
//			if(scheduleCnt!=ScheduleListMap.size()) sb.append("&#13;&#10;");
		}
		logger.debug("ｽｹｼﾞｭｰﾙデータ"+_ViewData);
		return _ViewData;
		
	}
	
	//グループIDリスト取得処理
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
	
	//htmlSanitizing+文字コード変換
	private String reStr(String str){
		str = CmnUtility.htmlSanitizing(str);

		return str;
	}

}
