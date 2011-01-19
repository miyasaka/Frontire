package frontier.action.pc;

import java.sql.Timestamp;
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
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.entity.Schedule;
import frontier.form.pc.CalendarForm;
import frontier.service.CalendarService;
import frontier.service.ClistService;
import frontier.service.CommonService;
import frontier.service.FriendListService;
import frontier.service.FriendinfoCmnService;
import frontier.service.MembersService;
import frontier.service.PhotoService;
import frontier.service.TopService;


public class CalendarAction {
	 Logger logger = Logger.getLogger(this.getClass().getName());
		
	    @ActionForm
	    @Resource
	    protected CalendarForm calendarForm;
	    
	    @Resource
	    public UserInfoDto userInfoDto;
	    @Resource
	    public AppDefDto appDefDto;
	    @Resource
	    protected CalendarService calendarService;
	    @Resource
	    protected MembersService membersService;
	    @Resource
	    protected FriendinfoCmnService friendinfoCmnService;
	    @Resource
	    protected PhotoService photoService;
	    @Resource
	    protected FriendListService friendListService;
		@Resource
		protected ClistService clistService;
		@Resource
		protected TopService topService;
		@Resource
		protected CommonService commonService;
	    
	    //インスタンス変数
	    //今日の日付
	    public String today = CmnUtility.getToday("yyyyMMdd");
	    public List<Map<String,Object>> cal;
	    public Map<String,List<String>> oldCal;

	    //スケジュール閲覧検索結果
	    public BeanMap viewResults;
	    //カレンダーのリンク表示用
	    protected List<BeanMap> monthResults;
	    //ｲﾍﾞﾝﾄアイコン表示制御用
		public Integer communityCnt;
	    //同士情報
	    public List<BeanMap> FriendList;
	    //共有ユーザー
	    public List<BeanMap> ShareUser;
	    //共有リスト
	    public List<BeanMap> ShareList;
		//一覧系変数
		public List <BeanMap> FriendItems;
	    //同志Idリスト
	    List<Object> flist;
	    //表示設定
		public BeanMap ViewInfo;

	    
	    //画面表示用
	    public String vNickname;
	    public String vEntdate;
	    public boolean vUser;
	    public String vMode;
		//共有ユーザー数
		public Integer ShareUserCnt =0;
		//共有状態
		public String ShareStatus = "0";//0:登録者  1:共有ユーザー 2:非共有ユーザー
	    
	   //内部制御用
	    private Timestamp entdate;
	    private List<String> diaryAuthList;
	    
		public ActionMessages errors = new ActionMessages();


		@Execute(validator=false,urlPattern="{yyyymmdd}")
		public String index(){
			//初期化
			logger.debug("日付"+calendarForm.yyyymmdd);
			
			if(calendarForm.yyyymmdd==null){
				calendarForm.calendarDay = today;
			}else{
				try{
					if(calendarForm.yyyymmdd.length()!=8){
						calendarForm.yyyymmdd=null;
						return "error.jsp";
					}
					if(appDefDto.FP_MY_CALENDAR_START_PGMAX>Integer.parseInt(calendarForm.yyyymmdd.substring(0,4).toString())) return "error.jsp";
					if(appDefDto.FP_MY_CALENDAR_END_PGMAX<Integer.parseInt(calendarForm.yyyymmdd.substring(0,4).toString())) return "error.jsp";
					calendarForm.calendarDay=calendarForm.yyyymmdd;
				}catch (Exception e){
					e.printStackTrace();
					calendarForm.yyyymmdd=null;
					return "error.jsp";
				}
			}
			calendarForm.yyyymmdd=null;
			setcalendarForm(false,"0");		

			try {
				//初期化
				initCalendar();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//共通処理
			initListCalendar(null,null,0);
	
			return "index.jsp";
		}
		
		//自画面からの遷移用
		@Execute(validator=false)
		public String myView(){

			//初期化処理
			try {
				initCalendar();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//共通処理
			if(calendarForm.searchFlg){
				//条件あり検索
				initListCalendar(calendarForm.calendarDay.substring(0, 6),null,calendarForm.offset);
			}else{
				//全件検索
				initListCalendar(null,null,calendarForm.offset);
			}
			
			return "index.jsp";
			
		}
		
		//「<」を押した場合の処理
		@Execute(validator=false)
		public String beforeMonth(){
			//初期化
			setcalendarForm(true,"1");		
			
			//月の減算
			calendarForm.calendarDay = CmnUtility.calcCalendar(calendarForm.calendarDay,-1);
			
			//F5対策
			return "myView?redirect=true";
		}
		
		//「>」を押した場合の処理
		@Execute(validator=false)
		public String nextMonth(){
			//初期化
			setcalendarForm(true,"1");		

			//月の加算
			calendarForm.calendarDay = CmnUtility.calcCalendar(calendarForm.calendarDay,1);
			
			//F5対策
			return "myView?redirect=true";
		}
		
		//スケジュール登録画面(calendar【アイコン】)
		@Execute(validator=false,urlPattern="entry/{startyear}/{startmonth}/{startday}/{status}")
		public String entry(){
			clear();
			calendarForm.status="2";
			initInput();
			//return "entry.jsp";
			return "input.jsp";
		}
		
		//スケジュール登録画面(calendar【予定を入力する】)
		@Execute(validator=false)
		public String entryCalendar(){
			clear();
			calendarForm.status="2";
			calendarForm.calendarDay = today;
			calendarForm.startyear=calendarForm.calendarDay.substring(0, 4);
			calendarForm.startmonth=calendarForm.calendarDay.substring(4, 6);
			calendarForm.startday=calendarForm.calendarDay.substring(6, 8);
			initInput();
			//return "entry.jsp";
			return "input.jsp";
		}
		
		//スケジュール登録画面(top)
		@Execute(validator=false)
		public String entryTop(){
			clear();
			calendarForm.status="1";
			calendarForm.calendarDay = today;
			calendarForm.startyear=calendarForm.calendarDay.substring(0, 4);
			calendarForm.startmonth=calendarForm.calendarDay.substring(4, 6);
			calendarForm.startday=calendarForm.calendarDay.substring(6, 8);
			initInput();
			//return "entry.jsp";
			return "input.jsp";
		}
		
		
		//スケジュール閲覧画面
		@Execute(validator=false,urlPattern="view/{sno}/{cid}/{status}")
		public String view(){
			//画面ID設定
			calendarForm.pageid="view";

			//表示中スケジュールデータ取得
		    initSchedule();
		    
		    //表示権限チェック
		    if(check().equals("NG")){
		    	return "error.jsp";
		    }

		    Beans.copy(viewResults,calendarForm ).execute();

		    
			return "view.jsp";
		}
		
		//スケジュール編集画面
		@Execute(validator=false,urlPattern="edit")
		public String edit(){
			logger.debug("編集"+calendarForm.sno);
			//画面ID設定
			calendarForm.pageid="edit";
			
			initInput();
			
			//表示中スケジュールデータ取得
		    initSchedule();
		    
		    //表示権限チェック
		    if(check().equals("NG")){
		    	return "error.jsp";
		    }
		    
		    Beans.copy(viewResults,calendarForm ).execute();
	
			//return "edit.jsp";
			return "input.jsp";
		}
		
		//スケジュール削除確認画面
		@Execute(validator=false)
		public String delete(){
			//画面ID設定
			calendarForm.pageid="delete";
			
			//表示中スケジュールデータ取得
		    initSchedule();
		    
		    //表示権限チェック
		    if(check().equals("NG")){
		    	return "error.jsp";
		    }
		    
		    Beans.copy(viewResults,calendarForm ).execute();
	
			return "delete.jsp";
		}
		
		//スケジュール追加
		@Execute(validate="chkDate",input="error",reset="reset")
		public String add(){
			//ID摩り替わり防止
			if(!userInfoDto.memberId.equals(calendarForm.uid)){
				return "error.jsp";
			}
			calendarService.insSchedule(userInfoDto.memberId,calendarForm);
			//共有ユーザー登録
			if(!calendarForm.publevel.equals("9")){
				calendarService.insScheduleShareUserListAll(userInfoDto.memberId,calendarForm.sno,calendarForm.ShareUserIds);
			}
			
			if(calendarForm.status.equals("1")) return "dummy_top.jsp";//topページ
	
			return "dummy_calendar.jsp";
		}
		
		//ｽｹｼﾞｭｰﾙを共有するボタン押下
		@Execute(validator=false)
		public String shareSchedule(){
			//ｽｹｼﾞｭｰﾙ共有
			//過去に共有を行っていない場合
			if(calendarService.selNoShareUserList(calendarForm.cid,calendarForm.sno,userInfoDto.memberId).size()>0){
				calendarService.insScheduleShareUserList(calendarForm.cid,calendarForm.sno,userInfoDto.memberId);
			}else{
				calendarService.updScheduleShareUserList(calendarForm.cid,calendarForm.sno,userInfoDto.memberId);
			}
			
			return "view/"+calendarForm.sno+"/"+calendarForm.cid+"/"+calendarForm.status;
		}
		
		//共有をやめるボタン押下
		@Execute(validator=false)
		public String shareCansel(){
			//ｽｹｼﾞｭｰﾙ共有削除
			calendarService.delScheduleShareUserList(calendarForm.cid,calendarForm.sno,userInfoDto.memberId);
			
			return "view/"+calendarForm.sno+"/"+calendarForm.cid+"/"+calendarForm.status;
		}
		
		//スケジュール編集
		@Execute(validate="chkDate",input="error",reset="reset")
		public String editSchedule(){
			//ID摩り替わり防止
			if(!userInfoDto.memberId.equals(calendarForm.uid)){
				return "error.jsp";
			}
			//画面ID設定
			calendarForm.pageid="edit";
			
			//表示中スケジュールデータ取得
		    initSchedule();
		    
		    //表示権限チェック
		    if(check().equals("NG")){
		    	return "error.jsp";
		    }
		
			Schedule entity = Beans.createAndCopy(Schedule.class,calendarForm).execute();
			//ｽｹｼﾞｭｰﾙ更新
			calendarService.updateSchedule(entity,userInfoDto.memberId,calendarForm.sno);
			//登録者のみ変更可

			if(calendarForm.publevel.equals("9")){
				calendarForm.ShareViewFlg = false;
			}else{
				calendarForm.ShareViewFlg = true;
			}
			if(userInfoDto.memberId.equals(calendarForm.cid)&&calendarForm.ShareViewFlg){
				//共有ユーザー更新
				calendarService.updScheduleShareUserListAll(userInfoDto.memberId,calendarForm.sno,calendarForm.ShareUserIds);
				//共有ユーザー登録
				calendarService.insScheduleShareUserListAll(userInfoDto.memberId,calendarForm.sno,calendarForm.ShareUserIds);
			}
			
			
			if(calendarForm.status.equals("1")) return "dummy_top.jsp";//topページ
			
			
			return "dummy_calendar.jsp";
		}
		
		
		//スケジュール削除
		@Execute(validator=false)
		public String delSchedule(){
			//画面ID設定
			calendarForm.pageid="delete";
			
			//表示中スケジュールデータ取得
		    initSchedule();
		    
		    //表示権限チェック
		    if(check().equals("NG")){
		    	return "error.jsp";
		    }
		  //ｽｹｼﾞｭｰﾙ共有情報削除
			calendarService.delScheduleShareUserListAll(userInfoDto.memberId,calendarForm.sno);
		    //ｽｹｼﾞｭｰﾙ削除
			calendarService.delSchedule(calendarForm.sno,calendarForm.cid);
			
			if(calendarForm.status.equals("1")) return "dummy_top.jsp";//topページ
			
	
			return "dummy_calendar.jsp";
		}

		//やめる
		@Execute(validator=false)
		public String cansel(){
	
			return "view/"+calendarForm.sno+"/"+calendarForm.cid+"/"+calendarForm.status;
		}
		
		//入力エラー
		@Execute(validator=false,reset="reset")
		public String error(){
			
			//登録者
			if(userInfoDto.memberId.equals(calendarForm.cid)){
				
				if (calendarForm.pageid == null){
					vMode = "追加";
				}else if(calendarForm.pageid.equals("edit")){
					vMode = "編集";
				}
		    
			    List<BeanMap> _ShareList = new ArrayList<BeanMap>();
			    List<BeanMap> _ShareUser = new ArrayList<BeanMap>();
			    logger.debug(calendarForm.ShareUserIds);
			    logger.debug(calendarForm.ShareListIds);
			    
			    String[] userIds = calendarForm.ShareUserIds.split(",");
			    String[] userNames = calendarForm.ShareUserNames.split(",");
			    String[] ListIds = calendarForm.ShareListIds.split(",");
			    String[] ListNames = calendarForm.ShareListNames.split(",");
			    
			    logger.debug(calendarForm.ShareListIds!=null);
			    logger.debug(calendarForm.ShareListIds.length());
			    
			    if(calendarForm.ShareListIds.length()>0)
				    for(int i=0;i<ListIds.length;i++){
				    	logger.debug(ListNames[i]);
				    	BeanMap b = new BeanMap();
				    	b.put("mid",ListIds[i]);
				    	b.put("nickname",ListNames[i]);
				    	_ShareList.add(b);
				    }
			    ShareList = _ShareList;
			    
			    logger.debug(calendarForm.ShareUserIds!=null);
			    logger.debug(calendarForm.ShareUserIds.length());
	
			    if(calendarForm.ShareUserIds.length()>0)
				    for(int i=0;i<userIds.length;i++){
				    	BeanMap b = new BeanMap();
				    	b.put("joinmid",userIds[i]);
				    	b.put("nickname",userNames[i]);
				    	_ShareUser.add(b);
				    }
			    ShareUser = _ShareUser;
			    
			    ShareUserCnt = ShareUser.size();
			//共有ユーザー
			}else{
				//タイトル文言設定
				initInput();
			    //共有情報設定
			    setShareInfo();
			}
			
			return "input.jsp";
		}
		
		//カレンダー必須処理
		private void initListCalendar(String month,String day,int offset){

			monthResults = calendarService.selScheduleMonthList(userInfoDto.memberId,calendarForm.calendarDay,groupids(),calendarForm.defDisptypeCalendar);	

			cmnMakeCalendar();
			
		}
		
		//カレンダー変数初期化処理
		private void setcalendarForm(boolean bl,String flg){
			calendarForm.pgcnt = 0;
			calendarForm.offset = 0;
			calendarForm.searchFlg = bl;
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
		
		public String friendStatus(Object fid){
			String status = groupids().contains(fid)?(userInfoDto.memberId.equals(fid)?"0":"1"):"2";

		    return status;
			
		}
		
		//スケジュール閲覧・編集・削除画面共通処理
		public String initSchedule(){
			//現在表示中スケジュールのデータ取得
			viewResults = calendarService.selSchedule(calendarForm.sno,calendarForm.cid);

			//同志ステータスセット
		    calendarForm.friendstatus=friendStatus(viewResults.get("mid"));
		    //公開権限セット
		    if(!viewResults.get("mid").equals(userInfoDto.memberId)){
		    	calendarForm.publevel=viewResults.get("publevel").toString();
		    }
		    
		    //共有情報設定
		    setShareInfo();
		    
		    return null;
		    
		}
		
		//共有情報設定
		public void setShareInfo(){
			//ｽｹｼﾞｭｰﾙ共有ユーザー取得
			ShareUser = calendarService.selScheduleShareUserList(calendarForm.cid,calendarForm.sno);

			if(ShareUser!=null){
				//共有ユーザー件数取得
				ShareUserCnt = ShareUser.size();
				//共有状態確認
				//登録者
				if(userInfoDto.memberId.equals(calendarForm.cid)){
					ShareStatus="0";
				}else{
					ShareStatus="2";//非共有者
					for(BeanMap b:ShareUser)
						//共有状態にある場合
						if(b.get("joinmid").equals(userInfoDto.memberId)) ShareStatus = "1";//共有者
				}
			}else{
				//登録者
				if(userInfoDto.memberId.equals(calendarForm.cid)){
					ShareStatus="0";
				}else{
					ShareStatus="2";//非共有者
				}
			}
		}
		
		
		public String check(){
		    //マイ以外のスケジュールの表示権限チェック
		    if(!calendarForm.friendstatus.equals("0")){
				//閲覧画面
				if(calendarForm.pageid.equals("view")){
					//全体に公開以外
					if(!calendarForm.publevel.equals("1")){
						//グループ&グループに公開以外
						if(!(calendarForm.friendstatus.equals("1")&&calendarForm.publevel.equals("2"))){
							return "NG";
						}
					}
				//編集画面
				}else if(calendarForm.pageid.equals("edit")){
					//非共有者
					if(!ShareStatus.equals("1")){
						return "NG";
					//全体に公開以外
					}else if(!calendarForm.publevel.equals("1")){
						return "NG";
					}
					
				//削除画面
				}else if(calendarForm.pageid.equals("delete")){
					return "NG";
				}
		    }
			return "OK";
		}

		
		//共通カレンダー作成
		private void cmnMakeCalendar(){
			//カレンダー生成
			cal = CmnUtility.makeCustomCalendar2(calendarForm.calendarDay,monthResults);
			//過去カレンダー生成
			oldCal = CmnUtility.makeOldCalendar(entdate);	
		}

		//カレンダーに必要な初期設定
		private void initCalendar() throws Exception{
			// 参加コミュニティ件数取得
			communityCnt = clistService.cntClist(userInfoDto.memberId,"0");
			// 表示設定情報取得
			ViewInfo = topService.selDefaultSetting(userInfoDto.memberId);
			if(ViewInfo!=null) calendarForm.defDisptypeCalendar=(String) ViewInfo.get("disptypeCalendar");
			
			diaryAuthList = new ArrayList<String>();
			//yyyymmdd = calendarForm.calendarDay.substring(0,3)+"年";

			//メニュー出しわけ用変数設定
			setVisitMemberId();
			
			vNickname = userInfoDto.nickName;
			vEntdate = CmnUtility.dateFormat("yyyyMMdd", userInfoDto.entdate);
			entdate = userInfoDto.entdate;

			diaryAuthList.add(0, appDefDto.FP_CMN_DIARY_AUTH1[0]);
//			diaryAuthList.add(1, appDefDto.FP_CMN_DIARY_AUTH2[0]);
			diaryAuthList.add(2, appDefDto.FP_CMN_DIARY_AUTH3[0]);
			diaryAuthList.add(3, appDefDto.FP_CMN_DIARY_AUTH4[0]);
				
			//画面表示用
			vUser = true;
			
		}
		
		//フォーム初期化
		private void clear(){
			calendarForm.title=null;
			calendarForm.sno=null;
			calendarForm.detail=null;
			calendarForm.starttime=null;
			calendarForm.startminute=null;
			calendarForm.endtime=null;
			calendarForm.endminute=null;
			calendarForm.titlecolor=null;
			calendarForm.publevel=null;
			calendarForm.pageid=null;
		}

		//メニュー出しわけ用変数設定
		private void setVisitMemberId(){
			userInfoDto.visitMemberId = userInfoDto.memberId;
		}
		
		//日付チェック処理
		public ActionMessages chkDate(){
			//初期値設定
			Integer year = Integer.parseInt(calendarForm.startyear);
			Integer month = Integer.parseInt(calendarForm.startmonth);
			Integer day = Integer.parseInt(calendarForm.startday);
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
			if(calendarForm.chk01.equals("on")){
				calendarForm.starttime="--";
				calendarForm.endtime="--";
				calendarForm.startminute="--";
				calendarForm.endminute="--";
			}
			logger.debug(calendarForm.chk01);
			if(calendarForm.starttime==null) calendarForm.starttime="--";
			if(calendarForm.endtime==null) calendarForm.endtime="--";
			if(calendarForm.startminute==null) calendarForm.startminute="--";
			if(calendarForm.endminute==null) calendarForm.endminute="--";
			
			//開始時間チェック			
			if(!calendarForm.starttime.equals("--")){
				hour = Integer.parseInt(calendarForm.starttime);
				if(hour<0||hour>23){
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
	        				"errors.schedule.time",new Object[] {}));
				}
			}
			
			//終了時間チェック			
			if(!calendarForm.endtime.equals("--")){
				hour = Integer.parseInt(calendarForm.endtime);
				if(hour<0||hour>23){
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
	        				"errors.schedule.time",new Object[] {}));
				}
			}
			//開始分チェック	
			for(int i=0;i<minList.length;i++){
				//開始
				if(minList[i].equals(calendarForm.startminute)){
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
				if(minList[i].equals(calendarForm.endminute)){
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
			if (calendarForm.pageid == null){
				vMode = "追加";
				calendarForm.cid=userInfoDto.memberId;
				ShareStatus="0";
				calendarForm.ShareViewFlg=false;
			}else if(calendarForm.pageid.equals("edit")){
				vMode = "編集";
				ShareStatus="1";
				if(calendarForm.publevel.equals("9")){
					calendarForm.ShareViewFlg=false;
				}else{
					calendarForm.ShareViewFlg=true;
				}
			}
			//ｽｹｼﾞｭｰﾙ共有ユーザー選択リスト取得(登録者)
			if(userInfoDto.memberId.equals(calendarForm.cid)){
				ShareList = calendarService.selScheduleShareUserSelectList(calendarForm.cid,calendarForm.sno,groupids());
			}
						
		}
		
}
