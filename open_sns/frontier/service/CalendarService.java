package frontier.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.beans.util.BeanMap;

import frontier.common.CmnUtility;
import frontier.common.EmojiUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.entity.MembersetupInfo;
import frontier.entity.Schedule;
import frontier.entity.ScheduleJoinInfo;
import frontier.form.m.ScheduleForm;
import frontier.form.pc.CalendarForm;

public class CalendarService {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	public AppDefDto appDefDto;
	@Resource
	protected JdbcManager jdbcManager;
	
	@Resource
	public UserInfoDto userInfoDto;
	
	//インスタンス変数
	protected List<BeanMap> results;
	
	
	//表示月のスケジュール情報を取得(全てのｽｹｼﾞｭｰﾙ)
	public List<BeanMap> selScheduleMonthList(String mid,String calendarday,List<Object> idlist,String Viewtype){
		Map<String,Object> params = new HashMap<String,Object>();
		
		//パラメータ設定
		params.put("mid", mid);
		params.put("year", calendarday.substring(0, 4));
		params.put("month", calendarday.substring(4, 6));
		String before = CmnUtility.getCalendarBeforeDay(calendarday);
		if(before!=null){
			params.put("before_year", before.substring(0, 4));
			params.put("before_month", before.substring(4, 6));
			params.put("before_day", before.substring(6, 8));
		}
		String next = CmnUtility.getCalendarNextDay(calendarday);
		if(next!=null){

			params.put("next_year", next.substring(0, 4));
			params.put("next_month", next.substring(4, 6));
			params.put("next_day", next.substring(6, 8));
		}
		
		
		params.put("idlist", idlist);
		
		//共有+自分のｽｹｼﾞｭｰﾙ
		if(Viewtype.equals("1")){
			//SQL実行
			return jdbcManager
						.selectBySqlFile(BeanMap.class, "/data/selShareScheduleMonthList",params)
						.getResultList();
		//全てのｽｹｼﾞｭｰﾙ
		}else{
		//SQL実行
			return jdbcManager
						.selectBySqlFile(BeanMap.class, "/data/selScheduleMonthList",params)
						.getResultList();
		}
	}
	
	//1週間のスケジュール情報を取得
	public List<BeanMap> selScheduleWeekList(String mid,String calendarday,List<Object> idlist,String enddate,String Viewtype){
		Map<String,Object> params = new HashMap<String,Object>();
		
		//パラメータ設定
		params.put("mid", mid);
		params.put("startdate",calendarday.substring(0,8));
		params.put("enddate",enddate);
		params.put("idlist", idlist);
		
		//SQL実行
		//共有+自分のｽｹｼﾞｭｰﾙ
		if(Viewtype.equals("1")){
			return jdbcManager
			.selectBySqlFile(BeanMap.class, "/data/selShareScheduleWeekList",params)
			.getResultList();	
		}else{
			return jdbcManager
						.selectBySqlFile(BeanMap.class, "/data/selScheduleWeekList",params)
						.getResultList();
		}
	}
	
	//今日以降のｽｹｼﾞｭｰﾙ情報を取得
	public List<BeanMap> selScheduleList(String mid,String calendarday,List<Object> idlist,int limit,String Viewtype){
		Map<String,Object> params = new HashMap<String,Object>();
		logger.debug("parameter");
		
		//パラメータ設定
		params.put("mid", mid);
		params.put("year", calendarday.substring(0, 4));
		params.put("month", calendarday.substring(4, 6));
		params.put("day", calendarday.substring(6, 8));
		params.put("hour", calendarday.substring(8, 10));
		params.put("minute", calendarday.substring(10, 12));
		params.put("idlist", idlist);
		
		logger.debug("end"+Viewtype);
		
		//共有+自分のｽｹｼﾞｭｰﾙ
		if(Viewtype.equals("1")){
			//SQL実行
			return jdbcManager
						.selectBySqlFile(BeanMap.class, "/data/selScheduleShareList",params)
						.limit(limit)
						.getResultList();
		}else{
			//SQL実行
			return jdbcManager
						.selectBySqlFile(BeanMap.class, "/data/selScheduleList",params)
						.limit(limit)
						.getResultList();
		}
	}
	
	//今日のスケジュール情報を取得
	public List<BeanMap> selScheduleTodayList(String mid,String calendarday,List<Object> idlist,String Viewtype){
		Map<String,Object> params = new HashMap<String,Object>();
		
		//パラメータ設定
		params.put("mid", mid);
		params.put("year", calendarday.substring(0, 4));
		params.put("month", calendarday.substring(4, 6));
		params.put("day", calendarday.substring(6, 8));
		params.put("idlist", idlist);
		
		//SQL実行
		//共有+自分のｽｹｼﾞｭｰﾙ
		if(Viewtype.equals("1")){
			return jdbcManager
						.selectBySqlFile(BeanMap.class, "/data/selShareScheduleTodayList",params)
						.getResultList();
		}else{
			return jdbcManager
			.selectBySqlFile(BeanMap.class, "/data/selScheduleTodayList",params)
			.getResultList();
		}
	}
	
	public BeanMap selSchedule(Integer sno,String cid){
		//SQL実行
		Map<String,Object> params = new HashMap<String,Object>();
		
		//パラメータ設定
		params.put("sno", sno);
		params.put("mid", cid);
		return jdbcManager.selectBySqlFile(BeanMap.class, "/data/selSchedule",params)
		.getSingleResult();
	}
	
	//ｽｹｼﾞｭｰﾙ表示設定更新処理
	public void updScheduleDispSetting(String mid,String viewtype){
		//現在の設定情報を取得
		MembersetupInfo mi = jdbcManager.from(MembersetupInfo.class).id(mid).getSingleResult();
		mi.disptypeCalendar = viewtype;
		jdbcManager.update(mi).includes("disptypeCalendar").execute();
	}
	
	//スケジュール登録処理
	public void insSchedule(String mid,CalendarForm calendarForm) {
		Object sno = jdbcManager.selectBySqlFile(BeanMap.class, "/data/selMaxSno",mid).getSingleResult().get("sno");
		Schedule s = new Schedule();
		s.mid=mid;
		s.sno= Integer.parseInt(sno.toString());
		calendarForm.sno=s.sno;
		s.title=calendarForm.title;
		s.titlecolor=calendarForm.titlecolor;
		s.startyear=calendarForm.startyear;
		s.startmonth=calendarForm.startmonth;
		s.startday=calendarForm.startday;
		s.starttime=calendarForm.starttime;
		s.startminute=calendarForm.startminute;
		s.endyear=calendarForm.startyear;
		s.endmonth=calendarForm.startmonth;
		s.endday=calendarForm.startday;
		s.endtime=calendarForm.endtime;
		s.endminute=calendarForm.endminute;
		s.detail=calendarForm.detail;
		s.newsflg="0";
		s.publevel=calendarForm.publevel;
		s.entid=mid;
		s.entdate=new Timestamp ((new java.util.Date()).getTime());
		s.updid=mid;
		s.upddate=new Timestamp ((new java.util.Date()).getTime());
		
		jdbcManager.insert(s).execute();

	}
	
	//スケジュール登録処理(携帯用)
	public void insScheduleM(String mid,ScheduleForm scheduleForm) {
		Object sno = jdbcManager.selectBySqlFile(BeanMap.class, "/data/selMaxSno",mid).getSingleResult().get("sno");
		Schedule s = new Schedule();
		s.mid=mid;
		s.sno= Integer.parseInt(sno.toString());
		s.title=EmojiUtility.replaceMoblileToPc(scheduleForm.title,
				appDefDto.FP_CMN_M_EMOJI_XML, userInfoDto.userAgent,appDefDto.FP_CMN_INNER_ROOT_PATH);
		//改行コード削除
		String crlf = "\r\n";
		s.title = s.title.replaceAll(crlf,"");
		s.titlecolor="000000";
		s.startyear=scheduleForm.startyear;
		s.startmonth=scheduleForm.startmonth;
		s.startday=scheduleForm.startday;
		s.starttime=scheduleForm.starttime;
		s.startminute=scheduleForm.startminute;
		s.endyear=scheduleForm.startyear;
		s.endmonth=scheduleForm.startmonth;
		s.endday=scheduleForm.startday;
		s.endtime=scheduleForm.endtime;
		s.endminute=scheduleForm.endminute;
		s.detail=EmojiUtility.replaceMoblileToPc(scheduleForm.detail,
				appDefDto.FP_CMN_M_EMOJI_XML, userInfoDto.userAgent,appDefDto.FP_CMN_INNER_ROOT_PATH);
		s.newsflg="0";
		s.publevel=scheduleForm.publevel;
		s.entid=mid;
		s.entdate=new Timestamp ((new java.util.Date()).getTime());
		s.updid=mid;
		s.upddate=new Timestamp ((new java.util.Date()).getTime());
		
		jdbcManager.insert(s).execute();

	}
	
	//スケジュール更新処理
	public void updateSchedule(Schedule s,String mid,Integer sno){
		//キーカラム指定
		s.sno=sno;
		//更新項目設定
		s.updid=mid;
		s.title=EmojiUtility.replaceMoblileToPc(s.title,
				appDefDto.FP_CMN_M_EMOJI_XML, userInfoDto.userAgent,appDefDto.FP_CMN_INNER_ROOT_PATH);
		//改行コード削除
		String crlf = "\r\n";
		s.title = s.title.replaceAll(crlf,"");
		s.detail=EmojiUtility.replaceMoblileToPc(s.detail,
				appDefDto.FP_CMN_M_EMOJI_XML, userInfoDto.userAgent,appDefDto.FP_CMN_INNER_ROOT_PATH);
		s.upddate=new Timestamp ((new java.util.Date()).getTime());
		jdbcManager.update(s).excludes("newsflg","entid","entdate","endyear","endmonth","endday").execute();
	}
	//ｽｹｼﾞｭｰﾙ非共有ユーザーリスト取得
	public List<BeanMap> selNoShareUserList(String mid,Integer sno,String listmid){
		String[] li;
		li = listmid.split(",");
		List<String> shareUserList = new ArrayList<String>();
		//型変換
		for(int i=0;i<li.length;i++){
			shareUserList.add(i,li[i]);
		}
		//非共有ユーザーID取得
		Map<String,Object> params = new HashMap<String,Object>();
		//パラメータ設定
		params.put("mid", mid);
		params.put("sno", sno);
		params.put("listmid",shareUserList);
		return jdbcManager.selectBySqlFile(BeanMap.class, "/data/selScheduleNoShareUserList.sql",params)
		.getResultList();
	}
	
	//ｽｹｼﾞｭｰﾙ共有ユーザー登録
	public void insScheduleShareUserList(String mid,Integer sno,String listmid){
		ScheduleJoinInfo s = new ScheduleJoinInfo();
		
		s.mid=mid;
		s.sno=sno;
		s.joinmid=listmid;
		s.delflg="0";
		s.entdate=new Timestamp ((new java.util.Date()).getTime());
		s.entid=listmid;
		s.upddate=new Timestamp ((new java.util.Date()).getTime());
		s.updid=listmid;
		jdbcManager.insert(s).execute();

	}
	
	//ｽｹｼﾞｭｰﾙ共有ユーザー一括登録
	public void insScheduleShareUserListAll(String mid,Integer sno,String listmid){
		ScheduleJoinInfo s = new ScheduleJoinInfo();
		//非共有ユーザーリスト取得
		List<BeanMap> noShareIds = selNoShareUserList(mid,sno,listmid);
	
		//共有リスト登録
		for(BeanMap b:noShareIds){
			s.mid=mid;
			s.sno=sno;
			s.joinmid=b.get("mid").toString();
			s.delflg="0";
			s.entdate=new Timestamp ((new java.util.Date()).getTime());
			s.entid=mid;
			s.upddate=new Timestamp ((new java.util.Date()).getTime());
			s.updid=mid;
			jdbcManager.insert(s).execute();
			logger.debug("登録なし"+b.get("mid"));
		}
	}
	
	//ｽｹｼﾞｭｰﾙ共有ユーザー更新
	public void updScheduleShareUserList(String mid,Integer sno,String joinmid){
		ScheduleJoinInfo s = new ScheduleJoinInfo();
		s.mid=mid;//キー設定
		s.sno=sno;
		s.joinmid=joinmid;
		s.delflg="0";
		s.upddate=new Timestamp ((new java.util.Date()).getTime());
		s.updid=joinmid;
		jdbcManager.update(s).includes("delflg","updid","upddate").execute();
		
	}
	

	//ｽｹｼﾞｭｰﾙ共有ユーザー一括更新
	public void updScheduleShareUserListAll(String mid,Integer sno,String listmid){
		ScheduleJoinInfo s = new ScheduleJoinInfo();
		String[] li;
		li = listmid.split(",");
		List<String> shareUserList = new ArrayList<String>();
		//型変換
		for(int i=0;i<li.length;i++){
			shareUserList.add(i,li[i]);
		}

		//共有ユーザー情報取得
		Map<String,Object> params = new HashMap<String,Object>();
		//パラメータ設定
		params.put("mid", mid);
		params.put("sno", sno);
		params.put("listmid",shareUserList);
		List<BeanMap> userUpdateInfo = jdbcManager.selectBySqlFile(BeanMap.class, "/data/selScheduleUpdateInfo.sql",params)
		.getResultList();
		
		//共有リスト更新
		for(BeanMap b:userUpdateInfo){
			s.mid=mid;//キー設定
			s.sno=sno;
			s.joinmid=b.get("joinmid").toString();
			
			s.delflg=b.get("delflg").toString();
			s.upddate=new Timestamp ((new java.util.Date()).getTime());
			s.updid=mid;
			jdbcManager.update(s).includes("delflg","updid","upddate").execute();
		}
		
		
		
	}
	
	//ｽｹｼﾞｭｰﾙ共有ユーザー削除
	public void delScheduleShareUserList(String mid,Integer sno,String listmid){
		ScheduleJoinInfo s = new ScheduleJoinInfo();		
		s.mid=mid;//キー設定
		s.sno=sno;
		s.joinmid=listmid;
		jdbcManager.delete(s).execute();

	}
	
	//ｽｹｼﾞｭｰﾙ共有ユーザー一括削除
	public void delScheduleShareUserListAll(String mid,Integer sno){
		List<ScheduleJoinInfo> ShareList = jdbcManager.from(ScheduleJoinInfo.class).where("mid=? and sno = ?",mid,sno).getResultList();
		if(ShareList.size()>0) jdbcManager.deleteBatch(ShareList).execute();

	}
	
	
	//スケジュール削除処理
	public void delSchedule(Integer sno,String mid){
		Schedule s = new Schedule();
		s.mid=mid;
		s.sno=sno;
		jdbcManager.updateBySqlFile("/data/delSchedule",s).execute();
	}
	
	// イベント作成可能コミュニティ一覧取得
	public List<BeanMap> selectClist(String listcid,String seltype,Integer offset,Integer limit){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("listcid",listcid);
		params.put("seltype",seltype);
		params.put("limit",limit);
		params.put("offset",offset);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selClist2.sql",params)
		.getResultList();
		return results;
	}
	
	// ｽｹｼﾞｭｰﾙ共有ユーザーリスト取得
	public List<BeanMap> selScheduleShareUserList(String mid,Integer sno){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		params.put("sno",sno);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selScheduleShareUserList.sql",params)
		.getResultList();
		return results;
	}
	
	// ｽｹｼﾞｭｰﾙ共有ユーザーリスト取得
	public List<BeanMap> selScheduleShareUserSelectList(String mid,Integer sno,List<Object> idlist){
		Map<String, Object> params = new HashMap<String, Object>();
		// Mapオブジェクトに条件用パラメタを定義
		params.put("mid",mid);
		params.put("sno",sno);
		params.put("idlist",idlist);
		results = jdbcManager
		.selectBySqlFile(BeanMap.class,"/data/selScheduleShareUserSelectList.sql",params)
		.getResultList();
		return results;
	}
	

}
