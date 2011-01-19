package frontier.action.m;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import frontier.common.CmnUtility;
import frontier.common.EmojiUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.entity.Schedule;
import frontier.form.m.ScheduleForm;
import frontier.service.CalendarService;
import frontier.service.CommonService;
import frontier.service.FriendListService;
import frontier.service.TopService;

public class ScheduleAction {
	Logger logger = Logger.getLogger(this.getClass().getName());

	@Resource
	public AppDefDto appDefDto;
	@Resource
	public UserInfoDto userInfoDto;

	@Resource
	protected FriendListService friendListService;
	
	@Resource
	protected CalendarService calendarService;

	@Resource
	protected TopService topService;
	
	@ActionForm
	@Resource
	protected ScheduleForm scheduleForm;
	@Resource
	protected CommonService commonService;

	//一覧系変数
	public List <BeanMap> weekResults;
	public List <BeanMap> DayResults;
	public List <BeanMap> FriendList;
	public List <BeanMap> GroupList;
    //共有ユーザー
    public List<BeanMap> ShareUser;
    //表示設定
	public BeanMap ViewInfo;
    //スケジュール閲覧検索結果
    public BeanMap viewResults;
	
    //インスタンス変数
    //今日の日付
    public String today = CmnUtility.getToday("yyyyMMddHHmmss");
    //今日の曜日
    public String youbi = CmnUtility.getToday("EEE");
    public String date = CmnUtility.getToday("yyyy'年'M'月'd'日('E')'");
    public List<Map<String,Object>> schedule;
    
    //画面表示用
    public String vMode;
	//共有ユーザー数
	public Integer ShareUserCnt =0;
	//共有状態
	public String ShareStatus = "0";//0:登録者  1:共有ユーザー 2:非共有ユーザー
    
	   //内部制御用
	public ActionMessages errors = new ActionMessages();

	
	// 初期表示
	@Execute(validator=false,urlPattern="{yyyymmdd}")
	public String index(){
		
		//初期化
		logger.debug("日付"+scheduleForm.yyyymmdd);
		
		if(scheduleForm.yyyymmdd==null){
			scheduleForm.calendarDay = today;
		}else{
			try{
				if(scheduleForm.yyyymmdd.length()!=8){
					scheduleForm.yyyymmdd=null;
					return "error.jsp";
				}
				if(appDefDto.FP_MY_CALENDAR_START_PGMAX>Integer.parseInt(scheduleForm.yyyymmdd.substring(0,4).toString())) return "error.jsp";
				if(appDefDto.FP_MY_CALENDAR_END_PGMAX<Integer.parseInt(scheduleForm.yyyymmdd.substring(0,4).toString())) return "error.jsp";
				scheduleForm.calendarDay=scheduleForm.yyyymmdd;
				//週の始めの日付を取得(本日日付基準)
				scheduleForm.calendarDay=CmnUtility.getWeekStart(scheduleForm.calendarDay);
			}catch (Exception e){
				e.printStackTrace();
				scheduleForm.yyyymmdd=null;
				return "error.jsp";
			}
		}
		scheduleForm.yyyymmdd=null;
		//historyback用変数設定
		scheduleForm.historyDay=scheduleForm.calendarDay;
		
		//scheduleForm.calendarDay=today;
		initSchedule();
		return "list.jsp";
	}
	
	@Execute(validator=false)
	public String myview(){
		//historyback用変数設定
		scheduleForm.historyDay=scheduleForm.calendarDay;
		initSchedule();
		return "list.jsp";
	}
	
	@Execute(validator=false)
	public String dayview(){
		initDaySchedule();
		return "daylist.jsp";
	}
	
	
	
	//先週
	@Execute(validator=false,redirect=true,urlPattern="before/{calendarDay}")
	public String before(){
		//1週間分減算
		scheduleForm.calendarDay=CmnUtility.getCalendarDay(scheduleForm.calendarDay,-appDefDto.FP_MY_M_SCHEDULELIST_PGMAX);
		//週の始めの日付を取得
		//scheduleForm.calendarDay=CmnUtility.getWeekStart(scheduleForm.calendarDay);

		return "myview";
	}
	//先々週
	@Execute(validator=false,redirect=true,urlPattern="before2/{calendarDay}")
	public String before2(){
		//1週間分減算
		scheduleForm.calendarDay=CmnUtility.getCalendarDay(scheduleForm.calendarDay,-appDefDto.FP_MY_M_SCHEDULELIST_PGMAX*2);
		//週の始めの日付を取得
		//scheduleForm.calendarDay=CmnUtility.getWeekStart(scheduleForm.calendarDay);

		return "myview";
	}
	
	//翌週
	@Execute(validator=false,redirect=true,urlPattern="next/{calendarDay}")
	public String next(){
		//1週間分加算
		scheduleForm.calendarDay=CmnUtility.getCalendarDay(scheduleForm.calendarDay,appDefDto.FP_MY_M_SCHEDULELIST_PGMAX);
		//週の始めの日付を取得
		//scheduleForm.calendarDay=CmnUtility.getWeekStart(scheduleForm.calendarDay);
		return "myview";
	}
	
	//翌々週
	@Execute(validator=false,redirect=true,urlPattern="next2/{calendarDay}")
	public String next2(){
		//1週間分加算
		scheduleForm.calendarDay=CmnUtility.getCalendarDay(scheduleForm.calendarDay,appDefDto.FP_MY_M_SCHEDULELIST_PGMAX*2);
		//週の始めの日付を取得
		//scheduleForm.calendarDay=CmnUtility.getWeekStart(scheduleForm.calendarDay);
		return "myview";
	}
	
	//前日
	@Execute(validator=false,redirect=true,urlPattern="beforeday/{calendarDay}")
	public String beforeday(){
		//1日分減算
		scheduleForm.calendarDay=CmnUtility.getCalendarDay(scheduleForm.calendarDay,-1);
		//週の始めの日付を取得
		//scheduleForm.calendarDay=CmnUtility.getWeekStart(scheduleForm.calendarDay);

		return "dayview";
	}
	
	//翌日
	@Execute(validator=false,redirect=true,urlPattern="nextday/{calendarDay}")
	public String nextday(){
		//1日分加算
		scheduleForm.calendarDay=CmnUtility.getCalendarDay(scheduleForm.calendarDay,1);
		//週の始めの日付を取得
		//scheduleForm.calendarDay=CmnUtility.getWeekStart(scheduleForm.calendarDay);
		return "dayview";
	}
	
	//ｽｹｼﾞｭｰﾙ初期処理
	public void initSchedule(){
		// 表示設定情報取得
		ViewInfo = topService.selDefaultSetting(userInfoDto.memberId);
		if(ViewInfo!=null) scheduleForm.defDisptypeCalendar=(String) ViewInfo.get("disptypeCalendar");
		//ｽｹｼﾞｭｰﾙデータ取得
		logger.debug("day"+scheduleForm.calendarDay);
		weekResults = calendarService.selScheduleWeekList(userInfoDto.memberId,scheduleForm.calendarDay,groupids(),CmnUtility.calcCalendar(scheduleForm.calendarDay,appDefDto.FP_MY_M_SCHEDULELIST_PGMAX),scheduleForm.defDisptypeCalendar);
		resetResultsList(weekResults);
		//ｽｹｼﾞｭｰﾙデータ生成
		schedule = CmnUtility.makeWeekCalendar(scheduleForm.calendarDay,CmnUtility.calcCalendar(scheduleForm.calendarDay,appDefDto.FP_MY_M_SCHEDULELIST_PGMAX),weekResults);

		
	}
	
	//ｽｹｼﾞｭｰﾙ初期処理
	public void initDaySchedule(){
		// 表示設定情報取得
		ViewInfo = topService.selDefaultSetting(userInfoDto.memberId);
		if(ViewInfo!=null) scheduleForm.defDisptypeCalendar=(String) ViewInfo.get("disptypeCalendar");
		//ｽｹｼﾞｭｰﾙデータ取得
		logger.debug("day"+scheduleForm.calendarDay);
		DayResults = calendarService.selScheduleTodayList(userInfoDto.memberId,scheduleForm.calendarDay,groupids(),scheduleForm.defDisptypeCalendar);
		resetResultsList(DayResults);
		//ｽｹｼﾞｭｰﾙデータ生成
		//schedule = CmnUtility.makeWeekCalendar(scheduleForm.calendarDay,CmnUtility.calcCalendar(scheduleForm.calendarDay,1),DayResults);

		
	}
	
	
	//スケジュール入力(ﾏｲｽｹｼﾞｭｰﾙ)
	@Execute(validator=false,reset="clear")
	public String entry(){
		scheduleForm.status="1";
		scheduleForm.calendarDay = today;
		scheduleForm.title="予約";
		scheduleForm.startyear=scheduleForm.calendarDay.substring(0, 4);
		scheduleForm.startmonth=scheduleForm.calendarDay.substring(4, 6);
		scheduleForm.startday=scheduleForm.calendarDay.substring(6, 8);
		initInput();
		//return "entry.jsp";
		return "input.jsp";
	}
	
	//スケジュール入力(ｽｹｼﾞｭｰﾙ閲覧)
	@Execute(validator=false,reset="clear",urlPattern="entry2/{calendarDay}")
	public String entry2(){
		scheduleForm.status="2";
		scheduleForm.title="予約";
		scheduleForm.startyear=scheduleForm.calendarDay.substring(0, 4);
		scheduleForm.startmonth=scheduleForm.calendarDay.substring(4, 6);
		scheduleForm.startday=scheduleForm.calendarDay.substring(6, 8);
		initInput();
		//return "entry.jsp";
		return "input.jsp";
	}
	
	//スケジュール編集画面
	@Execute(validator=false,urlPattern="edit")
	public String edit(){
		logger.debug("編集"+scheduleForm.sno);
		//画面ID設定
		scheduleForm.pageid="edit";
		
		//表示中スケジュールデータ取得
	    CmnSchedule();
	    
	    //表示権限チェック
	    if(check().equals("NG")){
	    	return "error.jsp";
	    }
	    
	    Beans.copy(viewResults,scheduleForm ).execute();
		initInput();

		//return "edit.jsp";
		return "input.jsp";
	}
	
	//ｽｹｼﾞｭｰﾙ削除画面
	@Execute(validator=false)
	public String delete(){
		//画面ID設定
		scheduleForm.pageid="delete";
		
		//表示中スケジュールデータ取得
	    CmnSchedule();
	    //タグ変換処理
	    resetResults();
	    
	    //表示権限チェック
	    if(check().equals("NG")){
	    	return "error.jsp";
	    }
	    
	    Beans.copy(viewResults,scheduleForm ).execute();

		return "delete.jsp";
	}
	
	//ｽｹｼﾞｭｰﾙ閲覧画面(日付指定)
	@Execute(validator=false,urlPattern="daylist/{calendarDay}")
	public String daylist(){
		//画面ID設定
		scheduleForm.pageid="view";
		initDaySchedule();
	    
		return "daylist.jsp";
	}
	
	
	//スケジュール閲覧画面
	@Execute(validator=false,urlPattern="view/{sno}/{cid}")
	public String view(){
		//ﾄｯﾌﾟページから遷移時
		if(scheduleForm.yyyymmdd==null){
			//カレンダー日付に今日をセット
			scheduleForm.calendarDay = today;
		}
		//画面ID設定
		scheduleForm.pageid="view";

		//表示中スケジュールデータ取得
		logger.debug("view");
	    CmnSchedule();
	    //タグ変換処理
	    resetResults();
	    
	    //表示権限チェック
	    if(check().equals("NG")){
	    	return "error.jsp";
	    }
	    
	    logger.debug("コピー");

	    Beans.copy(viewResults,scheduleForm ).execute();

	    
		return "view.jsp";
	}
	
	//スケジュール追加
	@Execute(validate="chkDate",input="error",redirect=true,reset="reset")
	public String add(){
		calendarService.insScheduleM(userInfoDto.memberId,scheduleForm);
		//週の始めの日付を取得(入力日付基準)
		scheduleForm.calendarDay=CmnUtility.getWeekStart(scheduleForm.startyear+scheduleForm.startmonth+scheduleForm.startday);
		return "myview";
	}
	
	//スケジュール編集
	@Execute(validate="chkDate",input="error",redirect=true,reset="reset")
	public String editSchedule(){
		//画面ID設定
		scheduleForm.pageid="edit";
		
		//表示中スケジュールデータ取得
	    //initSchedule();
	    
	    //表示権限チェック
	    if(check().equals("NG")){
	    	return "error.jsp";
	    }
	
		Schedule entity = Beans.createAndCopy(Schedule.class,scheduleForm).execute();

		calendarService.updateSchedule(entity,userInfoDto.memberId,scheduleForm.sno);
		
		//週の始めの日付を取得(入力日付基準)
		scheduleForm.calendarDay=CmnUtility.getWeekStart(scheduleForm.startyear+scheduleForm.startmonth+scheduleForm.startday);
		
		
		return "myview";
	}
	
	//ｽｹｼﾞｭｰﾙを共有するボタン押下
	@Execute(validator=false)
	public String shareSchedule(){
		//ｽｹｼﾞｭｰﾙ共有
		//過去に共有を行っていない場合
		if(calendarService.selNoShareUserList(scheduleForm.cid,scheduleForm.sno,userInfoDto.memberId).size()>0){
			calendarService.insScheduleShareUserList(scheduleForm.cid,scheduleForm.sno,userInfoDto.memberId);
		}else{
			calendarService.updScheduleShareUserList(scheduleForm.cid,scheduleForm.sno,userInfoDto.memberId);
		}
		
		return "view/"+scheduleForm.sno+"/"+scheduleForm.cid;
	}
	
	//共有をやめるボタン押下
	@Execute(validator=false)
	public String shareCansel(){
		//ｽｹｼﾞｭｰﾙ共有削除
		calendarService.delScheduleShareUserList(scheduleForm.cid,scheduleForm.sno,userInfoDto.memberId);
		
		return "view/"+scheduleForm.sno+"/"+scheduleForm.cid;
	}
	
	//戻る(ﾏｲｽｹｼﾞｭｰﾙ)
	@Execute(validator=false)
	public String inputcansel1(){
		logger.debug("キャンセル"+scheduleForm.historyDay);
		//日付にhistoryback用変数の値をセット
		scheduleForm.calendarDay=scheduleForm.historyDay;
		return "myview";
	}
	
	//戻る(ｽｹｼﾞｭｰﾙ閲覧)
	@Execute(validator=false)
	public String inputcansel2(){
		//日付にhistoryback用変数の値をセット
//		scheduleForm.calendarDay=scheduleForm.historyDay;
		return "daylist/"+scheduleForm.calendarDay;
	}
	
	//やめる
	@Execute(validator=false)
	public String editcansel(){
		logger.debug("キャンセル"+scheduleForm.historyDay);
		//日付にhistoryback用変数の値をセット
//		scheduleForm.calendarDay=scheduleForm.historyDay;
		return "view/"+scheduleForm.sno+"/"+scheduleForm.cid;
	}
	
	//スケジュール削除
	@Execute(validator=false)
	public String delSchedule(){
		//画面ID設定
		scheduleForm.pageid="delete";
		
		//表示中スケジュールデータ取得
	    initSchedule();
	    
	    //表示権限チェック
	    if(check().equals("NG")){
	    	return "error.jsp";
	    }

		//ｽｹｼﾞｭｰﾙ共有情報削除
		calendarService.delScheduleShareUserListAll(userInfoDto.memberId,scheduleForm.sno);
		//スケジュール削除
		calendarService.delSchedule(scheduleForm.sno,scheduleForm.cid);
		

		return "/m/schedule/"+scheduleForm.startyear+scheduleForm.startmonth+scheduleForm.startday;
	}
	
	//全てのスケジュールを表示
	@Execute(validator=false)
	public String setting1(){
		//更新
		scheduleForm.defDisptypeCalendar="0";
		calendarService.updScheduleDispSetting(userInfoDto.memberId,scheduleForm.defDisptypeCalendar);
		return "/m/schedule/"+scheduleForm.calendarDay.substring(0,8);
	}
	//共有中のスケジュールのみ表示
	@Execute(validator=false)
	public String setting2(){
		//更新
		scheduleForm.defDisptypeCalendar="1";
		calendarService.updScheduleDispSetting(userInfoDto.memberId,scheduleForm.defDisptypeCalendar);
		return "/m/schedule/"+scheduleForm.calendarDay.substring(0,8);
	}
	
	//入力エラー
	@Execute(validator=false)
	public String error(){
		
		//タイトル文言設定
		//initInput();
		if (scheduleForm.pageid == null){
			vMode = "追加";
		}else if(scheduleForm.pageid.equals("edit")){
			vMode = "編集";
		}
		
		return "input.jsp";
	}
	
	//スケジュール閲覧・編集・削除画面共通処理
	public void CmnSchedule(){
		//現在表示中スケジュールのデータ取得
		viewResults = calendarService.selSchedule(scheduleForm.sno,scheduleForm.cid);
		logger.debug("ニックネーム"+viewResults.get("nickname"));
		//ステータスセット
	    scheduleForm.friendstatus=friendStatus(viewResults.get("mid"));
	    //公開権限セット
	    if(!viewResults.get("mid").equals(userInfoDto.memberId)){
	    	scheduleForm.publevel=viewResults.get("publevel").toString();
	    }
	    //共有情報設定
	    setShareInfo();
	    
	}
	
	//共有情報設定
	private void setShareInfo(){
		//ｽｹｼﾞｭｰﾙ共有ユーザー取得
		ShareUser = calendarService.selScheduleShareUserList(scheduleForm.cid,scheduleForm.sno);

		if(ShareUser!=null){
			//共有ユーザー件数取得
			ShareUserCnt = ShareUser.size();
			//共有状態確認
			//登録者
			if(userInfoDto.memberId.equals(scheduleForm.cid)){
				ShareStatus="0";
			}else{
				ShareStatus="2";//非共有者
				for(BeanMap b:ShareUser)
					//共有状態にある場合
					if(b.get("joinmid").equals(userInfoDto.memberId)) ShareStatus = "1";//共有者
			}
		}
	}
	
	//ｽｹｼﾞｭｰﾙ表示ページ設定
	public void setpage(){
		
	}
	
	public String friendStatus(Object fid){
		String status = groupids().contains(fid)?(userInfoDto.memberId.equals(fid)?"0":"1"):"2";

	    return status;
		
	}
	
	public String check(){
	    //マイ以外のスケジュールの表示権限チェック
		logger.debug("権限"+scheduleForm.friendstatus);
		
	    if(!scheduleForm.friendstatus.equals("0")){
			//閲覧画面
			if(scheduleForm.pageid.equals("view")){
				//全体に公開以外
				if(!scheduleForm.publevel.equals("1")){
					//グループに公開以外
					if(!(scheduleForm.friendstatus.equals("1")&&scheduleForm.publevel.equals("2"))){
						return "NG";
					}
				}
			//編集画面
			}else if(scheduleForm.pageid.equals("edit")){
				//非共有者
				if(!ShareStatus.equals("1")){
					return "NG";
				//全体に公開以外
				}else if(!scheduleForm.publevel.equals("1")){
					return "NG";
				}
				
			//削除画面
			}else if(scheduleForm.pageid.equals("delete")){
				return "NG";
			}
	    }
		return "OK";
	}
	
	public List<Object> groupids(){
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
	
	//日付チェック処理
	public ActionMessages chkDate(){
		//初期値設定
		Integer year = Integer.parseInt(scheduleForm.startyear);
		Integer month = Integer.parseInt(scheduleForm.startmonth);
		Integer day = Integer.parseInt(scheduleForm.startday);
		Integer hour;
		boolean minChk = false;
		int[] monthList = {31,28,31,30,31,30,31,31,30,31,30,31};
		String[] minList = {"--","00","15","30","45"};
		
		//閏年判定
		GregorianCalendar cal = new GregorianCalendar();
		if(cal.isLeapYear(year)){
			monthList[1]=29;
		}
	   	
		//年チェック
		if(year<appDefDto.FP_MY_CALENDAR_START_PGMAX||year>appDefDto.FP_MY_CALENDAR_END_PGMAX){
    		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
    				"errors.schedule.year",new Object[] {appDefDto.FP_MY_CALENDAR_START_PGMAX.toString(),appDefDto.FP_MY_CALENDAR_END_PGMAX.toString()}));				
		}
		//日付チェック
		if(month<1||month>12){
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
    				"errors.schedule.date",new Object[] {}));
		}else if(day<1||day>31){
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
    				"errors.schedule.date",new Object[] {}));
		}else{
			if(monthList[month-1]<day){
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
        				"errors.schedule.date",new Object[] {}));
			}
		}
		if(scheduleForm.chk01!=null){
			if(scheduleForm.chk01.equals("on")){
				scheduleForm.starttime="--";
				scheduleForm.endtime="--";
				scheduleForm.startminute="--";
				scheduleForm.endminute="--";
			}
		}
		logger.debug(scheduleForm.chk01);
		logger.debug(scheduleForm.starttime);
		logger.debug(scheduleForm.endtime);
		logger.debug(scheduleForm.startminute);
		logger.debug(scheduleForm.endminute);
		

		if(scheduleForm.starttime==null) scheduleForm.starttime="--";
		if(scheduleForm.endtime==null) scheduleForm.endtime="--";
		if(scheduleForm.startminute==null) scheduleForm.startminute="--";
		if(scheduleForm.endminute==null) scheduleForm.endminute="--";
		
		//開始時間チェック			
		if(!scheduleForm.starttime.equals("--")){
			hour = Integer.parseInt(scheduleForm.starttime);
			if(hour<0||hour>23){
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
        				"errors.schedule.time",new Object[] {}));
			}
		}
		
		//終了時間チェック			
		if(!scheduleForm.endtime.equals("--")){
			hour = Integer.parseInt(scheduleForm.endtime);
			if(hour<0||hour>23){
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
        				"errors.schedule.time",new Object[] {}));
			}
		}
		//開始分チェック	
		for(int i=0;i<minList.length;i++){
			//開始
			if(minList[i].equals(scheduleForm.startminute)){
				minChk = true;
			}
		}
		if(!minChk){
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
    				"errors.schedule.time",new Object[] {}));
		}
		minChk = false;
		//終了分チェック	
		for(int i=0;i<minList.length;i++){
			//開始
			if(minList[i].equals(scheduleForm.endminute)){
				minChk = true;
			}
		}
		if(!minChk){
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
    				"errors.schedule.time",new Object[] {}));
		}

		return errors;
	}
	
	//入力画面の共通処理
	public void initInput(){
		if (scheduleForm.pageid == null){
			vMode = "追加";
		}else if(scheduleForm.pageid.equals("edit")){
			vMode = "編集";
		}
		//表示月置換処理
		logger.debug(scheduleForm.startmonth);
		if(scheduleForm.startmonth.substring(0,1).equals("0")){
			logger.debug("aaaaa");
			scheduleForm.startmonth=scheduleForm.startmonth.substring(1,2);
			logger.debug(scheduleForm.startmonth);
		}
		//表示日置換処理
		if(scheduleForm.startday.substring(0,1).equals("0")){
			scheduleForm.startday=scheduleForm.startday.substring(1,2);
		}
					
	}
	
	//本文装飾
	private void resetResults(){
	//サニタイジング
		
	Object emojiComment ="";
	
	if(viewResults.get("detail")!=null){
		
		emojiComment = EmojiUtility.replacePcToMoblile(viewResults.get("detail").toString(),
				appDefDto.FP_CMN_M_EMOJI_XML, userInfoDto.userAgent,appDefDto.FP_CMN_INNER_ROOT_PATH);
		//サニタイジング
		emojiComment = CmnUtility.htmlSanitizing(emojiComment.toString());
	    viewResults.put("detail",emojiComment);
	}
	
	if(viewResults.get("title")!=null){
		emojiComment = EmojiUtility.replacePcToMoblile(viewResults.get("title").toString(),
				appDefDto.FP_CMN_M_EMOJI_XML, userInfoDto.userAgent,appDefDto.FP_CMN_INNER_ROOT_PATH);
		//サニタイジング
		emojiComment = CmnUtility.htmlSanitizing(emojiComment.toString());
	    viewResults.put("title",emojiComment);
	}
	}
	
	
	
	//本文装飾
	private void resetResultsList(List<BeanMap> lbm){
		
		for (int i=0;i<lbm.size();i++){
			//本文を取得
			String title = (String)lbm.get(i).get("title");
			
			String detail = (String)lbm.get(i).get("detail");
			
			//日記/ｽｹｼﾞｭｰﾙタイトル
			if(title!=null){
				//サニタイジング
				title = CmnUtility.htmlSanitizing(title);
				logger.debug(appDefDto.FP_CMN_M_EMOJI_XML);
			    //絵文字装飾
				title = EmojiUtility.replacePcToMoblile(title,
						appDefDto.FP_CMN_M_EMOJI_XML, userInfoDto.userAgent,appDefDto.FP_CMN_INNER_ROOT_PATH);
			  	
			  	//BeanMapへ格納
			  	lbm.get(i).put("title", title);
		  	
			}	
			
			//日記/ｽｹｼﾞｭｰﾙ詳細
			if(detail!=null){
				//サニタイジング
				detail = CmnUtility.htmlSanitizing(detail);
				
			    //絵文字装飾
				detail = EmojiUtility.replacePcToMoblile(detail,
						appDefDto.FP_CMN_M_EMOJI_XML, userInfoDto.userAgent,appDefDto.FP_CMN_INNER_ROOT_PATH);
			  	
			  	//BeanMapへ格納
			  	lbm.get(i).put("detail", detail);
		  	
			}	
			
			
			
		}
	}


}
