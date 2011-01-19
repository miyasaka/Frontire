package frontier.common;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.mail.internet.MimeUtility;

import net.arnx.jsonic.JSON;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.h2.util.StringUtils;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.struts.util.RequestUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import frontier.dto.AppDefDto;



public class CmnUtility {

		private static String STR = "abcdefghajklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		/**
		 * 指定された長さのランダム生成された文字列を返す。
		 * @param size
		 * @return String
		 */
		public static String  getRandomString(int size ){
			int rlen = STR.length();
			StringBuffer rstr = new StringBuffer();
			for(int i=0;i<size;i++){
				int r = (int)(Math.random()*rlen)+1;
				rstr.append(STR.charAt(r-1));
			}
			return rstr.toString();
		}

		/**
		 * 指定された文字列をMD5でハッシュした文字列(32文字)を返す。
		 * @param String s
		 * @return String
		 */
		public static String getDigest(String s) throws Exception{
			StringBuffer ret =new StringBuffer();
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] dg = s.getBytes();
			md.update(dg);
			dg = md.digest();

			for(int i=0;i<dg.length;i++){
				int d = dg[i];
				if(d < 0){
				 d +=256;
				}
				if(d < 16){
				 ret.append("0");
				}
				ret.append(Integer.toString(d,16));

			}
			return ret.toString( );
		}

		/**
		 * バイト配列を複合化する
		 * @param String key
		 * @param byte encrypted
		 * @return String
		 */
		public static String decrypt(String key, byte[] encrypted)
	    throws Exception {

	    SecretKeySpec sksSpec =
	        new SecretKeySpec(key.getBytes(), "Blowfish");

	    Cipher cipher = Cipher.getInstance("Blowfish");
	    cipher.init(Cipher.DECRYPT_MODE, sksSpec);

	    return new String(cipher.doFinal(encrypted));
	}


		/**
		 * 暗号化された文字列を複合化する
		 * @param String key
		 * @param String param
		 * @return String
		 */
		public static String hukugou(String key,String param) throws Exception {

		    InputStream fromBase64 = MimeUtility.decode(
		        new ByteArrayInputStream(param.getBytes()), "base64");

		    byte[] buf = new byte[1024];
		    ByteArrayOutputStream toByteArray = new ByteArrayOutputStream();

		    for (int len = -1;(len = fromBase64.read(buf)) != -1;)
		        toByteArray.write(buf, 0, len);

		    return decrypt(key,toByteArray.toByteArray());
		}


		/**
		 * 文字列を暗号化する(バイト配列)
		 * @param String key
		 * @param String text
		 * @return String
		 */
		public static byte[] encrypt(String key, String text)
	    throws Exception {

	    SecretKeySpec sksSpec =
	        new SecretKeySpec(key.getBytes(), "Blowfish");

	    Cipher cipher = Cipher.getInstance("Blowfish");
	    cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, sksSpec);

	    return cipher.doFinal(text.getBytes());
	}


		/**
		 * 文字列を暗号化する
		 * @param String key
		 * @param String param
		 * @return String
		 */
		public static String angou(String key,String param) throws Exception {

		    ByteArrayOutputStream forEncode = new ByteArrayOutputStream();

		    OutputStream toBase64 = MimeUtility.encode(forEncode, "base64");
		    toBase64.write(encrypt(key,param));
		    toBase64.close();

		    return forEncode.toString("iso-8859-1");
		}


		/**
		 * XMLタグのエスケープ
		 * @param String s
		 * @return String
		 */
		public static String xmlescape(String s) throws Exception{
			String ret = "";
			if(s != null ){
				ret = s;
				ret = ret.replaceAll("&","&amp;");
				ret = ret.replaceAll(">","&gt;");
				ret = ret.replaceAll("<","&lt;");
				ret = ret.replaceAll("\"","&quot;");
				ret = ret.replaceAll("'","&#39;");
			}
			return ret;
		}

		/**
		 * XMLタグのエスケープ(obj)
		 * @param String s
		 * @return String
		 */
		public static String xmlescape(Object o) throws Exception{
			String ret = "";
			if(o != null ){
				try{
					ret = xmlescape((String)o);
				}catch(Exception e){}
			}
			return ret;
		}



		/**
		 * @param args
		 */
		public static void main(String[] args) {

			System.out.println(getRandomString(10));
			try{
				System.out.println(getDigest("hogehogehghghghhghgh"));
				System.out.println(xmlescape("aaa<>\"&'bbb"));

			}catch(Exception e){e.printStackTrace();}
		}

		/**
		 * 数値を文字へとフォーマット設定する
		 * @param format 変換するフォーマット指定
		 * @param val 変換対象の数値
		 * @return String
		 */
		public static String stringFormat(String format,int val){
			DecimalFormat df = new DecimalFormat(format);

			return df.format(val);
		}

		/**
		 * 日付フォーマット設定する
		 * @param format 変換するフォーマット指定
		 * @param val 変換対象の値
		 * @return String
		 */
		public static String dateFormat(String format,Object val){
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(val);
		}

		/**
		 * 現在日時を取得する
		 * @param ymd 変換するフォーマット指定
		 * @return String
		 */
		public static String getToday(String ymd)  {
			java.util.Date today = null;

			today = new java.util.Date();

			SimpleDateFormat sdf = new SimpleDateFormat(ymd);
			return sdf.format(today);

		}

		/**
		 * Calendarクラスの初期設定を行う。
		 * @param ymd 初期設定する日時
		 * @return Calendar
		 */
		private static Calendar setCalendar(String ymd){
			//初期化
			Calendar calendar = Calendar.getInstance();
			calendar.clear();

			//日時を初期設定。
			//1月が0なので、月は-1する。
			calendar.set(Integer.parseInt(ymd.substring(0, 4)), Integer.parseInt(ymd.substring(4, 6))-1, 1);

			return calendar;
		}

		/**
		 * 月の加算・減算をする。
		 * @param ymd 対象の日付
		 * @param num 加減算する数値（減算の場合は-1などを与える)
		 * @return String
		 */
		public static String calcCalendar(String ymd,int num){
			Calendar calendar = setCalendar(ymd);

			//加算または減算実行
			calendar.add(Calendar.MONTH, num);

			//yyyymmまで生成
			String detailDay = String.valueOf(calendar.get(Calendar.YEAR));
			//１月は０なので、+1する。
			detailDay = detailDay.concat(stringFormat("00",calendar.get(Calendar.MONTH)+1));
			detailDay = detailDay.concat(stringFormat("00",calendar.get(Calendar.DATE)));

			return detailDay;
		}

		/**
		 * 日の加算・減算をする。
		 * @param num 加減算する数値（減算の場合は-1などを与える)
		 * @return String
		 */
		public static String calcCalendarDay(int num){
			Calendar calendar = Calendar.getInstance();

			//加算または減算実行
			calendar.add(Calendar.DATE, num);

			//yyyymmまで生成
			String detailDay = String.valueOf(calendar.get(Calendar.YEAR));
			//１月は０なので、+1する。
			detailDay = detailDay.concat(stringFormat("00",calendar.get(Calendar.MONTH)+1));
			detailDay = detailDay.concat(stringFormat("00",calendar.get(Calendar.DATE)));

			return detailDay;
		}

		/**
		 * カレンダーを設定する。
		 * 単純にカレンダーを表示するのみ。
		 * @param ymd 対象の日付
		 * @return List<Map<String,Integer>>
		 */
		public static List<Map<String,Integer>> makeCalendar(String ymd){
			List<Map<String,Integer>> cal = new ArrayList<Map<String,Integer>>();
			String detailYearMonth = "";

			//カレンダー初期化
			Calendar calendar = setCalendar(ymd);

			//月の初めの曜日を求めます。
			int startWeek = calendar.get(Calendar.DAY_OF_WEEK);

			//yyyymmまで作成
			detailYearMonth = String.valueOf(calendar.get(Calendar.YEAR));
			detailYearMonth = detailYearMonth.concat(stringFormat("00",calendar.get(Calendar.MONTH)+1));

			// 月末の日付を求めます。
			calendar.add(Calendar.MONTH, 1);
			calendar.add(Calendar.DATE, -1);
			int lastDate = calendar.get(Calendar.DATE);

			for (int date = 1; date <= lastDate; date++) {
				Map<String,Integer> calendarMap = new HashMap<String,Integer>();
				calendarMap.put("day",date);

				if (date != 1) {
					//次の日の曜日にする。
					startWeek = startWeek + 1;

					if (startWeek >= 8) {
						//日曜日を過ぎたので月曜日に戻す。
						startWeek = 1;
					}
				}

				int detaildate = Integer.valueOf(detailYearMonth.concat(stringFormat("00",date)));

				calendarMap.put("week",startWeek);
				calendarMap.put("detailDay",detaildate);
				cal.add(calendarMap);
			}

			return cal;
		}

		/**
		 * カレンダーを設定する。
		 * カレンダーとDBを絡ませた場合版。
		 * @param ymd 対象の日付
		 * @return List<Map<String,Integer>>
		 */
		public static List<Map<String,Integer>> makeCustomCalendar(String ymd,List<BeanMap> lbm){
			List<Map<String,Integer>> cal = new ArrayList<Map<String,Integer>>();
			String detailYearMonth = "";

			//カレンダー初期化
			Calendar calendar = setCalendar(ymd);

			//月の初めの曜日を求めます。
			int startWeek = calendar.get(Calendar.DAY_OF_WEEK);

			//yyyymmまで作成
			detailYearMonth = String.valueOf(calendar.get(Calendar.YEAR));
			detailYearMonth = detailYearMonth.concat(stringFormat("00",calendar.get(Calendar.MONTH)+1));

			// 月末の日付を求めます。
			calendar.add(Calendar.MONTH, 1);
			calendar.add(Calendar.DATE, -1);
			int lastDate = calendar.get(Calendar.DATE);

			for (int date = 1; date <= lastDate; date++) {
				Map<String,Integer> calendarMap = new HashMap<String,Integer>();
				calendarMap.put("day",date);

				if (date != 1) {
					//次の日の曜日にする。
					startWeek = startWeek + 1;

					if (startWeek >= 8) {
						//日曜日を過ぎたので月曜日に戻す。
						startWeek = 1;
					}
				}

				int detaildate = Integer.valueOf(detailYearMonth.concat(stringFormat("00",date)));

				calendarMap.put("week",startWeek);
				calendarMap.put("detailDay",detaildate);

				//カレンダーの日付に日記が存在するかを調べる。
				for(int i=0;i<lbm.size();i++){
					//日記が存在する日付
					int entdate = Integer.valueOf((String)lbm.get(i).get("entdate"));

					//日記存在チェック
					if (detaildate == entdate){
						//日記が存在する
						calendarMap.put("existDiary", 1);
						break;
					}
				}

				cal.add(calendarMap);
			}

			return cal;
		}

		/**
		 * カレンダーを設定する。
		 * カレンダーとDBを絡ませた場合版。
		 * @param ymd 対象の日付
		 * @return List<Map<String,Integer>>
		 */
		public static List<Map<String,Object>> makeWeekCalendar(String startdate,String enddate,List<BeanMap> lbm){
			List<Map<String,Object>> schedule = new ArrayList<Map<String,Object>>();

			//初期化
			Calendar calendar = Calendar.getInstance();
			calendar.clear();
			//カレンダー開始日付を設定
			calendar.set(Integer.parseInt(startdate.substring(0, 4)), Integer.parseInt(startdate.substring(4, 6))-1, Integer.parseInt(startdate.substring(6, 8)));
			//int ScheduleCnt = 7-calendar.get(Calendar.DAY_OF_WEEK)+1;//ｽｹｼﾞｭｰﾙ表示日数
			int ScheduleCnt = 7;
			System.out.println(calendar.get(Calendar.DAY_OF_WEEK));
			//現在日付
			String cdate = startdate.substring(0,8);
			boolean existsSchedule = false;
			//開始日付以降の1週間分のｽｹｼﾞｭｰﾙを生成
			String color = null;
			for(int i=0;i<ScheduleCnt;i++){
				Map<String,Object> scheduleMap = new HashMap<String,Object>();
				List<BeanMap> b = new ArrayList<BeanMap>();
//				b.put("scheduledate",cdate);
				try{

					switch(calendar.get(Calendar.DAY_OF_WEEK)){
						case 1:color="#ffcccc";break;
						case 2:color="#cccccc";break;
						case 3:color="#909090";break;
						case 4:color="#cccccc";break;
						case 5:color="#909090";break;
						case 6:color="#cccccc";break;
						case 7:color="#ccccff";break;
					}
					//System.out.println(calendar.get(Calendar.DAY_OF_WEEK)+"color"+color);
					//ｽｹｼﾞｭｰﾙ分ループ
					for(int j=0;j<lbm.size();j++){
//						//現在日付にｽｹｼﾞｭｰﾙが存在する場合
						if(cdate.equals(lbm.get(j).get("scheduledate"))){
							scheduleMap.put("existsSchedule",1);
							b.add(lbm.get(j));
						}
//							existsSchedule = false;
//						}
					}
//					if(existsSchedule){
//						b.put("scheduledate",cdate);
//						lbm.add(j,b);
//					}
//					System.out.println(existsSchedule);

					//現在日付にｽｹｼﾞｭｰﾙが存在しない場合
//					if(!cdate.equals(lbm.get(j).get("scheduledate"))){
//						b.put("scheduledate",cdate);
//						b.put("color",color);
//						lbm.add(j,b);
//						j++;;
//						System.out.println(cdate+"ｽｹｼﾞｭｰﾙなし"+lbm.get(j).get("scheduledate"));
//					}
//					//現在日付にｽｹｼﾞｭｰﾙが存在する場合
//					while(cdate.equals(lbm.get(j).get("scheduledate"))){
//						lbm.get(j).put("color",color);
//						j++;
//					}
					scheduleMap.put("schedule",b);
					scheduleMap.put("scheduledate", cdate);
					scheduleMap.put("color",color);
					schedule.add(scheduleMap);
//					System.out.println("aaaaa"+lbm.get(j).get("color"));
					//ｽｹｼﾞｭｰﾙを1日進める
					calendar.add(Calendar.DATE,1);
					cdate = String.valueOf(calendar.get(Calendar.YEAR));
					cdate = cdate.concat(stringFormat("00",calendar.get(Calendar.MONTH)+1));
					cdate = cdate.concat(stringFormat("00",calendar.get(Calendar.DATE)));

				}catch (Exception e){
//					calendar.add(Calendar.DATE,1);
//					cdate = String.valueOf(calendar.get(Calendar.YEAR));
//					cdate = cdate.concat(stringFormat("00",calendar.get(Calendar.MONTH)+1));
//					cdate = cdate.concat(stringFormat("00",calendar.get(Calendar.DATE)));
//					b.put("scheduledate",cdate);
//					b.put("color",color);
//					lbm.add(j,b);
//					j++;;
				}

				//System.out.println(lbm.get(i));
//				if(lbm.get(i)!=null){
//				//	System.out.println(lbm.get(i).get("startday"));
//				//	System.out.println(calendar.get(Calendar.DATE));
//				}
			}

			return schedule;
		}

		/**
		 * 週始めの日付を取得する(本日日付基準)
		 * @param ymd 対象の日付
		 * @return String
		 */
		public static String getWeekStart(String ymd){
			//初期化
			Calendar calendar = Calendar.getInstance();
			calendar.clear();
			//今日の日付
			String today = getToday("yyyyMMdd");
			//今日の曜日
			calendar.set(Integer.parseInt(today.substring(0, 4)), Integer.parseInt(today.substring(4, 6))-1, Integer.parseInt(today.substring(6, 8)));
			int youbi = calendar.get(Calendar.DAY_OF_WEEK);
			//カレンダー設定(指定日)
			calendar.set(Integer.parseInt(ymd.substring(0, 4)), Integer.parseInt(ymd.substring(4, 6))-1, Integer.parseInt(ymd.substring(6, 8)));
			int youbi2 = calendar.get(Calendar.DAY_OF_WEEK);
			//指定曜日<本日曜日
			if(youbi2<youbi){
				youbi2+=7;//1週間前にずらす
			}
			return getCalendarDay(ymd,youbi-youbi2);
		}

		/**
		 * 週末の日付を取得する
		 * @param ymd 対象の日付
		 * @return String
		 */
		public static String getWeekEnd(String ymd){

			//初期化
			Calendar calendar = Calendar.getInstance();
			calendar.clear();
			calendar.set(Integer.parseInt(ymd.substring(0, 4)), Integer.parseInt(ymd.substring(4, 6))-1, Integer.parseInt(ymd.substring(6, 8)));
			return getCalendarDay(ymd,7-calendar.get(Calendar.DAY_OF_WEEK));
		}

		/**
		 * 日の加算・減算を行う
		 * @param ymd 対象の日付
		 * @return String
		 */
		public static String getCalendarDay(String ymd,int num){
			Calendar calendar = Calendar.getInstance();
			calendar.clear();
			calendar.set(Integer.parseInt(ymd.substring(0, 4)), Integer.parseInt(ymd.substring(4, 6))-1, Integer.parseInt(ymd.substring(6, 8)));
			//加算または減算実行
			calendar.add(Calendar.DATE, num);

			//yyyymmまで生成
			String detailDay = String.valueOf(calendar.get(Calendar.YEAR));
			//１月は０なので、+1する。
			detailDay = detailDay.concat(stringFormat("00",calendar.get(Calendar.MONTH)+1));
			detailDay = detailDay.concat(stringFormat("00",calendar.get(Calendar.DATE)));
			return detailDay;
		}

		/**
		 * カレンダーを設定する。
		 * カレンダーとDBを絡ませた場合版。
		 * @param ymd 対象の日付
		 * @return List<Map<String,Integer>>
		 */
		public static List<Map<String,Object>> makeCustomCalendar2(String ymd,List<BeanMap> lbm){
			List<Map<String,Object>> cal = new ArrayList<Map<String,Object>>();
			String detailYearMonth = "";

			//カレンダー初期化
			Calendar before = setCalendar(calcCalendar(ymd,-1));//前月
			Calendar calendar = setCalendar(ymd);
			//先月カレンダー
			String beforeDate=getCalendarBeforeDay(ymd);
			//来月カレンダー
			String nextDate=getCalendarNextDay(ymd);


			int startWeek = 1;
			//月の初めの曜日を求めます。
			int _startWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;
			//本日日付
			String today = CmnUtility.getToday("yyyyMMdd");

			//yyyymmまで作成
			detailYearMonth = String.valueOf(calendar.get(Calendar.YEAR));
			detailYearMonth = detailYearMonth.concat(stringFormat("00",calendar.get(Calendar.MONTH)+1));

			// 月末の日付を求めます。
			before.add(Calendar.MONTH, 1);
			before.add(Calendar.DATE, -1);
			calendar.add(Calendar.MONTH, 1);
			calendar.add(Calendar.DATE, -1);
			int lastDateBefore = before.get(Calendar.DATE);
			int lastDate = calendar.get(Calendar.DATE);
			//翌月スケジュール表示日数
			int nextViewDate = 7-(lastDate+_startWeek)%7;
			if(nextViewDate==7) nextViewDate=0;

			//先月スケジュール表示分だけ開始日付をずらす
			//翌月スケジュール表示分だけ終了日付をずらす
			for (int date = 1-_startWeek; date <= lastDate+nextViewDate; date++) {
				Map<String,Object> calendarMap = new HashMap<String,Object>();

				int detaildate =0;
				if(date<=0){//先月
					calendarMap.put("day",lastDateBefore+date);
					calendarMap.put("before",true);
					if(beforeDate!=null) detaildate = Integer.valueOf(beforeDate.concat(stringFormat("00",lastDateBefore+date)));
				}else if(date<=lastDate){//今月
					calendarMap.put("day",date);
					calendarMap.put("now",true);
					detaildate = Integer.valueOf(detailYearMonth.concat(stringFormat("00",date)));
				}else{//翌月
					calendarMap.put("day",date-lastDate);
					calendarMap.put("next",true);
					if(nextDate!=null) detaildate = Integer.valueOf(nextDate.concat(stringFormat("00",date-lastDate)));
				}
				if(date>0){//本日日付チェック
					if(detailYearMonth.substring(0,6).concat(String.valueOf(stringFormat("00",date))).equals(today)) calendarMap.put("today",1);
				}


				if (date+_startWeek != 1) {
					//次の日の曜日にする。
					startWeek = startWeek + 1;

					if (startWeek >= 8) {
						//日曜日を過ぎたので月曜日に戻す。
						startWeek = 1;
					}
				}
//				int detaildate =0;
//				if(date>=0){
//					detaildate = Integer.valueOf(detailYearMonth.concat(stringFormat("00",date)));
//				}else{
//					detaildate = Integer.valueOf(beforeDate.concat(stringFormat("00",lastDateBefore+date)));
//				}

//				calendarMap.put("beforeDate",lastDateBefore);

				calendarMap.put("week",startWeek);
				calendarMap.put("detailDay",detaildate);
				List<BeanMap> b = new ArrayList<BeanMap>();

				//カレンダーの日付にスケジュールが存在するかを調べる。
				int j = 0;
				int j_before =0;
				int j_next = 0;
				for(int i=0;i<lbm.size();i++){
					//スケジュールが存在する日付
					String entmonth = (String)lbm.get(i).get("startmonth");
					int entdate = Integer.valueOf((String)lbm.get(i).get("startday"));
					//先月のスケジュール
					if(beforeDate!=null&&date<=0){
						if(entmonth.equals(beforeDate.substring(4,6))){
							if(date+lastDateBefore == entdate){
								b.add(j_before, lbm.get(i));
								//スケジュールが存在する
								calendarMap.put("existSchedule", 1);
								j_before++;
							}
						}
					}
					//今月のスケジュール
					if(detailYearMonth.substring(4,6).equals(entmonth)){
						//スケジュール存在チェック
						if (date == entdate){
							b.add(j, lbm.get(i));
							//スケジュールが存在する
							calendarMap.put("existSchedule", 1);
							j++;
						}
					}

					//来月のスケジュール
					if(date>lastDate){
						if(nextDate!=null){
							if(nextDate.substring(4,6).equals(entmonth)){
								if(date-lastDate==entdate){
									b.add(j_next, lbm.get(i));
									//スケジュールが存在する
									calendarMap.put("existSchedule", 1);
									j_next++;
								}
							}
						}
					}

				}

				calendarMap.put("schedule",b);

				cal.add(calendarMap);
			}

			return cal;
		}
		/**
		 * 先月のカレンダー開始日付を取得(今月基準)
		 * @param ymd 対象の日付
		 * @return String
		 */
		public static String getCalendarBeforeDay(String ymd){
			//カレンダー初期化
			Calendar before = setCalendar(calcCalendar(ymd,-1));//前月
			Calendar calendar = setCalendar(ymd);//今月
			//今月の初めの曜日を求めます。
			int startWeek = calendar.get(Calendar.DAY_OF_WEEK);
			String detailYearMonth=null;
			if(startWeek!=1){
				// 先月の月末の日付を求めます。
				before.add(Calendar.MONTH, 1);
				before.add(Calendar.DATE, -1);
				//yyyymmまで作成
				detailYearMonth = String.valueOf(before.get(Calendar.YEAR));
				detailYearMonth = detailYearMonth.concat(stringFormat("00",before.get(Calendar.MONTH)+1));
				detailYearMonth = detailYearMonth.concat(stringFormat("00",before.get(Calendar.DATE)-startWeek+2));
			}

			return detailYearMonth;
		}

		/**
		 * 来月のカレンダー終了日付を取得(今月基準)
		 * @param ymd 対象の日付
		 * @return String
		 */
		public static String getCalendarNextDay(String ymd){
			//カレンダー初期化
			Calendar calendar = setCalendar(ymd);//今月
			Calendar next = setCalendar(calcCalendar(ymd,1));//来月
			//今月の初めの曜日を求めます。
			int startWeek = calendar.get(Calendar.DAY_OF_WEEK);
			// 今月の月末の日付を求めます。
			calendar.add(Calendar.MONTH, 1);
			calendar.add(Calendar.DATE, -1);
			String detailYearMonth=null;
			if((calendar.get(Calendar.DATE)-1+startWeek)%7!=0){
				//yyyymmまで作成
				detailYearMonth = String.valueOf(next.get(Calendar.YEAR));
				detailYearMonth = detailYearMonth.concat(stringFormat("00",next.get(Calendar.MONTH)+1));
				detailYearMonth = detailYearMonth.concat(stringFormat("00",7-(calendar.get(Calendar.DATE)-1+startWeek)%7));
			}

			return detailYearMonth;
		}

		/**
		 * 過去のカレンダーを生成する。
		 * @param entdate ユーザの登録日
		 * @return List
		 */
		public static Map<String,List<String>> makeOldCalendar(Timestamp entdate){
			Map<String,List<String>> calendarMap = new LinkedHashMap<String,List<String>>();

			String entYear = dateFormat("yyyy", entdate);
			String nowYear = getToday("yyyy");

			//登録年までの年を設定する
			for (int i = Integer.valueOf(nowYear);i>=Integer.valueOf(entYear);i--){
				List<String> monthList = new ArrayList<String>();

				//月の設定
				for(int j=1;j<13;j++){
					String month = Integer.toString(i) + stringFormat("00",j) + "01";
					monthList.add(month);
				}

				calendarMap.put(Integer.toString(i), monthList);
				//calendarMap.put("month", monthList);

			}

			return calendarMap;
		}


		/**
		 * ディレクトリを作成する
		 * @param Path 作成するディレクトリパス
		 * @Param Dir 作成するサブディレクトリ
		 * @return なし
		 */
		public static void makeDir(String Path, String Dir){
			// 作成するサブディレクトリリスト
			String[] dirs = {
					"pic640",
					"pic480",
					"pic240",
					"pic180",
					"pic120",
					"pic76",
					"pic60",
					"pic42"
				};

			// ディレクトリ存在チェック
			File file = new File(Path);
			if(!file.exists()){
				// ディレクトリが存在しなければ作成
				try{ file.mkdir(); }
				catch(Exception e){ e.printStackTrace(); }
			}

			// ディレクトリ存在チェック(サブ)
			file = new File(Path + "/" + Dir);
			if(!file.exists()){
				// ディレクトリが存在しなければ作成
				try{ file.mkdir(); }
				catch(Exception e){ e.printStackTrace(); }
			}

			// ディレクトリ存在チェック(イメージディレクトリ)
			for (String i : dirs) {
				file = new File(Path + "/" + Dir + "/" + i);
				if(!file.exists()){
					// ディレクトリが存在しなければ作成
					try{ file.mkdir(); }
					catch(Exception e){ e.printStackTrace(); }
				}
			}
		}


		/**
		 * 画像ファイルのアップロード
		 * @param path アップロード先の画像パス
		 * @param file アップロードする画像ファイル名
		 * @return
		 */
		public static void uploadFile(String path,FormFile file) {
			if (file.getFileSize() == 0) {
				return;
			}

			try {
				OutputStream out = new BufferedOutputStream(new FileOutputStream(
					path));
				try {
					out.write(file.getFileData(), 0, file.getFileSize());
				} finally {
					out.close();
				}
			} catch (IOException e) {
				throw new IORuntimeException(e);
			}

		}

		/**
		 * 画像ファイルのコピー
		 * @param input コピー元の画像パス
		 * @param out   コピー先の画像ファイル名
		 * @return
		 */
		public static void copy(String input,String out) {
			try {
				FileChannel srcChannel = new FileInputStream(input).getChannel();
				FileChannel destChannel = new FileOutputStream(out).getChannel();
				try {
					srcChannel.transferTo(0, srcChannel.size(), destChannel);
				} finally {
					srcChannel.close();
					destChannel.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * 画像のリサイズ（ImageMagick）
		 * @param path リサイズ対象の画像パス
		 * @param size リサイズするサイズ
		 * @param imgmagickdir イメージマジックのパス
		 * @return
		 */
		public static void resize(String path,Integer size,String basePath,String imgmagickdir) throws IOException, Exception{
			ImageIcon icon = new ImageIcon(basePath);

			if(icon.getIconHeight()>size||icon.getIconWidth()>size){
				Process process = null;
				try {
					process = Runtime.getRuntime().exec(imgmagickdir+"convert -geometry " + size + "x" + size +" "+ basePath + " " + path);
					process.waitFor();

				} catch (IOException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				} catch (Exception e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				} finally {
					if(process != null) {
						process.getErrorStream().close();
						process.getInputStream().close();
						process.getOutputStream().close();
						process.destroy();
					}
				}
			} else{
				//リサイズが必要無い
				//コピー元以外の場合
				if(!basePath.equals(path)){
					copy(basePath,path);
				}
			}

		}

		/**
		 * 一覧画面の本文編集 *アルバム一覧(画像なし)用
		 * @param results
		 * @param column
		 * @param emojiXmlPath
		 * @param cmnListCmntMax
		 * @param cmnEmojiImgPath
		 */
		public static void editDetail(List<BeanMap> results
								,String column
								,String emojiXmlPath
								,Integer cmnListCmntMax
								,String cmnEmojiImgPath){
			// 画像変換は不要なので画像項目はnullとする
			editcmnt(results
					,column
					,null, null, null
					,null, null, null
					,emojiXmlPath
					,cmnListCmntMax
					,cmnEmojiImgPath
					,null);
		}

		/**
		 * 一覧画面の本文編集
		 * @param r 一覧データ(List<BeanMap>)
		 * @param cmnt 本文のカラム名
		 * @param p1 写真1のカラム名
		 * @param p2 写真2のカラム名
		 * @param p3 写真3のカラム名
		 * @param pnote1 写真1の説明
		 * @param pnote2 写真2の説明
		 * @param pnote3 写真3の説明
		 * @param EmojiXmlPath 絵文字リストのxmlパス
		 * @param CmnListCmntMax 一覧で表示するリストの最大文字数
		 * @param CmnEmojiImgPath 絵文字イメージのパス
		 * @param CmnContentsRoot 画像イメージのパス
		 * @return なし
		 * @comment
		 *  rtnpic イメージタグに変換された写真1-3(そのまま使用する(エスケープしない))(r.rtnpicに入る)
		 *  rtncmnt 整形されたコメント(そのまま使用する(エスケープ処理済))(r.rtncmntに入る)
		 */
		public static void editcmnt(
			List<BeanMap> r,
			String cmnt,
			String p1,
			String p2,
			String p3,
			String pnote1,
			String pnote2,
			String pnote3,
			String EmojiXmlPath,
			Integer CmnListCmntMax,
			String CmnEmojiImgPath,
			String CmnContentsRoot
		){
			NodeList nlist = null;
			Matcher mt;
			Matcher mt1;
			Matcher mt2;
			String inm = "";
			String ialt = "";
			String rtnpic  = "";
			String rtncmnt = "";
			String rtncmnt1 = "";
			String rtncmnt2 = "";
			Integer cnt = 0;
			Integer cnt1 = 0;
			Integer cnt2 = 0;
			Integer cnti = 0;
			// 削除対象のタグリスト(改行含)
			String[] deltags = {
					"\\n",
					"<span class=(\"|\'|)(large|medium|small)(\"|\'|)>",
					"</span>",
					"<span style=(\"|\'|)color:#([a-zA-Z0-9]+)(\"|\'|)>",
					"<strong>",
					"</strong>",
					"<em>",
					"</em>",
					"<u>",
					"</u>",
					"<del>",
					"</del>",
					"<a ([^>]+)>",
					"</a>",
					"\\[y:([^\"^\\]]+)\\]",
					"\\[gm:([^\"^\\]]+)\\]",
					"\\[p:([0-9]+:[0-9]+:[0-9]+)\\]",
					"<blockquote>",
					"</blockquote>"
				};

			// i-mode絵文字xmlファイルの読み込み&リストの作成
			try {
				// ドキュメントビルダーファクトリを生成
				DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
				// ドキュメントビルダーを生成
				DocumentBuilder builder = dbfactory.newDocumentBuilder();
				// パースを実行してDocumentオブジェクトを取得
				Document doc = builder.parse(new File(EmojiXmlPath));
				// ルート要素を取得
				Element root = doc.getDocumentElement();
				// Listに格納
				nlist = root.getElementsByTagName("entry");
			} catch (Exception e) {
				e.printStackTrace();
			}

			// データループ
			for (BeanMap b : r) {
				// 初期化
				rtnpic  = "";
				rtncmnt = "";
				rtncmnt1 = "";
				rtncmnt2 = "";
				cnt = 0;
				cnt1 = 0;
				cnt2 = 0;
				cnti = 0;
				// フォトアルバム一覧は画像変換処理をスルー
				if(!StringUtils.isNullOrEmpty(CmnContentsRoot)){
					// 画像１～３のHTMLタグ化
					rtnpic = editimg(b,p1,p2,p3,pnote1,pnote2,pnote3,CmnContentsRoot);
					// 画像HTMLをput
					b.put("pichtml",rtnpic);
				}
				rtncmnt += b.get(cmnt);
				// 装飾タグの削除
				for (String i : deltags) {
					// 大文字小文字の区別をしない
					mt = Pattern.compile(i,Pattern.CASE_INSENSITIVE).matcher(rtncmnt);
					// ブランクにreplace
					rtncmnt = mt.replaceAll("");
				}

				// 指定文字数よりも長かった場合
				if (rtncmnt.length()>CmnListCmntMax){

					// 指定文字数でsubstring
					// 前半(0-指定文字数)
					rtncmnt1 = rtncmnt.substring(0,CmnListCmntMax);
					// 後半(指定文字数-最後)
					rtncmnt2 = rtncmnt.substring(CmnListCmntMax,rtncmnt.length());
					// i-mode絵文字タグの数を数える
					mt = Pattern.compile("\\[i:([0-9]+)\\]",Pattern.CASE_INSENSITIVE).matcher(rtncmnt);
					mt1 = Pattern.compile("\\[i:([0-9]+)\\]",Pattern.CASE_INSENSITIVE).matcher(rtncmnt1);
					mt2 = Pattern.compile("\\[i:([0-9]+)\\]",Pattern.CASE_INSENSITIVE).matcher(rtncmnt2);
					while (mt.find()){cnt++;}
					while (mt1.find()){cnt1++;}
					while (mt2.find()){cnt2++;}
					// 全体 != 前半 + 後半 の絵文字数じゃない場合、途中で切れてるので処理開始
					if (cnt != (cnt1 + cnt2)){
						cnti = 0;
						// 最後尾のi-mode絵文字タグがなくなるまでループ
						while(cnt != (cnt1 + cnt2)){
							cnti ++;
							cnt1 = 0;
							cnt2 = 0;
							rtncmnt1 = rtncmnt.substring(0,CmnListCmntMax-cnti);
							rtncmnt2 = rtncmnt.substring(CmnListCmntMax-cnti,rtncmnt.length());
							mt1 = Pattern.compile("\\[i:([0-9]+)\\]",Pattern.CASE_INSENSITIVE).matcher(rtncmnt1);
							mt2 = Pattern.compile("\\[i:([0-9]+)\\]",Pattern.CASE_INSENSITIVE).matcher(rtncmnt2);
							while (mt1.find()){cnt1++;}
							while (mt2.find()){cnt2++;}
						}
					}
					rtncmnt = rtncmnt1;
					rtncmnt += "．．．";
				}

				// サニタイジング処理
				rtncmnt = htmlSanitizing(rtncmnt);

				// iモード絵文字の変換
				// 絵文字List数分ループ
				for (int i=0; i < nlist.getLength() ; i++) {
					Element nelmnt = (Element)nlist.item(i);
					inm = nelmnt.getAttribute("id");
					ialt = nelmnt.getAttribute("name");
					mt= Pattern.compile("\\[i:"+ inm +"\\]",Pattern.CASE_INSENSITIVE).matcher(rtncmnt);
					// リストとマッチすればイメージタグに変換
					rtncmnt = mt.replaceAll("<img src=\"" + CmnEmojiImgPath + inm + ".gif\" alt=\"" + ialt + "\"/>");
				}
				// コメントHTMLをput
				b.put("cmnthtml",rtncmnt);
			}
		}

		/**
		 * 画像HTMLタグの編集
		 * @param d 一覧データ(Map<String, Object>)
		 * @param p1 写真1のカラム名
		 * @param p2 写真2のカラム名
		 * @param p3 写真3のカラム名
		 * @param pnote1 写真1の説明
		 * @param pnote2 写真2の説明
		 * @param pnote3 写真3の説明
		 * @param CmnContentsRoot 画像イメージのパス
		 * @return HTMLタグ
		 */
		public static String editimg(Map<String, Object> b,String p1,String p2,String p3,String pnote1,String pnote2,String pnote3,String CmnContentsRoot){
			// 初期化
			String rtnpic  = "";

			// サニタイジング処理
			pnote1 = htmlSanitizing(b.get(pnote1).toString());
			pnote2 = htmlSanitizing(b.get(pnote2).toString());
			pnote3 = htmlSanitizing(b.get(pnote3).toString());

			//画像説明が未入力
			if(pnote1.equals("") || pnote1.equals(null)){
				pnote1 = "画像１";
			}
			if(pnote2.equals("") || pnote2.equals(null)){
				pnote2 = "画像２";
			}
			if(pnote3.equals("") || pnote3.equals(null)){
				pnote3 = "画像３";
			}

			// 画像１～３のHTMLタグ化
			if(
				(b.get(p1)!="" && b.get(p1)!=null) ||
				(b.get(p2)!="" && b.get(p2)!=null) ||
				(b.get(p3)!="" && b.get(p3)!=null)
			){
				rtnpic += "<div style=\"text-align:center;\">";
				if(b.get(p1)!=""&&b.get(p1)!=null){
					rtnpic += "\n";
					rtnpic += "<a href=\"javascript:void(0);\" title=\""+pnote1+"\" onclick=\"ff_viewBigimg('"+CmnContentsRoot;
					rtnpic += b.get(p1).toString().replaceAll("dir","pic640");
					rtnpic += "');\">";
					rtnpic += "<img src=\""+CmnContentsRoot;
					rtnpic += b.get(p1).toString().replaceAll("dir","pic120");
					rtnpic += "\" alt=\""+pnote1+"\"/></a>";
				}
				if(b.get(p2)!=""&&b.get(p2)!=null){
					rtnpic += "\n";
					rtnpic += "<a href=\"javascript:void(0);\" title=\""+pnote2+"\" onclick=\"ff_viewBigimg('"+CmnContentsRoot;
					rtnpic += b.get(p2).toString().replaceAll("dir","pic640");
					rtnpic += "');\">";
					rtnpic += "<img src=\""+CmnContentsRoot;
					rtnpic += b.get(p2).toString().replaceAll("dir","pic120");
					rtnpic += "\" alt=\""+pnote2+"\"/></a>";
				}
				if(b.get(p3)!=""&&b.get(p3)!=null){
					rtnpic += "\n";
					rtnpic += "<a href=\"javascript:void(0);\" title=\""+pnote3+"\" onclick=\"ff_viewBigimg('"+CmnContentsRoot;
					rtnpic += b.get(p3).toString().replaceAll("dir","pic640");
					rtnpic += "');\">";
					rtnpic += "<img src=\""+CmnContentsRoot;
					rtnpic += b.get(p3).toString().replaceAll("dir","pic120");
					rtnpic += "\" alt=\""+pnote3+"\"/></a>";
				}
				rtnpic += "\n</div>";
			}
			return rtnpic;
		}

		/**
		 * 絵文字タグ→画像変換
		 * @param comment 置換対象の文字列
		 * @param CmnEmojiXmlPath 絵文字リストのxmlパス
		 * @return String 置換後の文字列
		 */
		public static String replaceEmoji(String txt,String CmnEmojiImgPath,String CmnEmojiXmlPath){
			NodeList nlist = null;
			String rtncmnt = txt;
			String inm = "";
			String ialt = "";
			Element nelmnt = null;

			// i-mode絵文字xmlファイルの読み込み&リストの作成
			try {
				// ドキュメントビルダーファクトリを生成
				DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
				// ドキュメントビルダーを生成
				DocumentBuilder builder = dbfactory.newDocumentBuilder();
				// パースを実行してDocumentオブジェクトを取得
				Document doc = builder.parse(new File(CmnEmojiXmlPath));
				// ルート要素を取得
				Element root = doc.getDocumentElement();
				// Listに格納
				nlist = root.getElementsByTagName("entry");
			} catch (Exception e) {
				e.printStackTrace();
			}

			// iモード絵文字の変換
			// 絵文字List数分ループ
			for (int i=0; i < nlist.getLength() ; i++) {
				nelmnt = (Element)nlist.item(i);
				inm = nelmnt.getAttribute("id");
				ialt = nelmnt.getAttribute("name");
				Matcher mt= Pattern.compile("\\[i:"+ inm +"\\]",Pattern.CASE_INSENSITIVE).matcher(rtncmnt);
				// リストとマッチすればイメージタグに変換
				rtncmnt = mt.replaceAll("<img src=\'" + CmnEmojiImgPath + inm + ".gif\' alt=\'" + ialt + "\'/>");
			}

			return rtncmnt;
		}

		/**
		 * HTMLサニタイジング処理
		 * @param moji サニタイジング対象の文字列
		 * @return String サニタイジング後の文字列
		 */
		public static String htmlSanitizing(String moji){
			String rmoji = moji;

			//サニタイジング
			rmoji = rmoji.replaceAll("&",  "&amp;");
			rmoji = rmoji.replaceAll("<",  "&lt;");
			rmoji = rmoji.replaceAll(">",  "&gt;");
			rmoji = rmoji.replaceAll("\"", "&quot;");

			return rmoji;
		}

		/**
		 * YouTubeタグ変換処理
		 * @param txt 変換対象の文字列
		 * @return String 変換後の文字列
		 */
		public static String replaceYoutube(String txt){
			String viewTube = "<div class=\"insertVideo\"><script type=\"text/javascript\">youtube_write(\'<object width=\"400\" height=\"373\"><param name=\"movie\" value=\"http://www.youtube.com/v/XXXX\"></param><param name=\"wmode\" value=\"transparent\"></param><embed src=\"http://www.youtube.com/v/XXXX\" type=\"application/x-shockwave-flash\" wmode=\"transparent\" width=\"400\" height=\"373\"></embed></object>\');</script></div>";

			StringBuffer sb = new StringBuffer();
			Pattern p = Pattern.compile("\\[y:([^\"^\\]]+)\\]");

			//正規表現実行
			Matcher m = p.matcher(txt);
			// 検索(find)し、マッチする部分文字列がある限り繰り返す
			while(m.find()){
				//部分文字列取得
				String partStr = m.group(1);

				//youTubeの可変変数を置換
				String youTube = viewTube.replaceAll("XXXX", partStr);

				//文字列連結
				m.appendReplacement(sb, youTube);
			}

			//残りの文字列連結
			m.appendTail(sb);

			return sb.toString();

		}

		/**
		 * 携帯版YouTubeタグ変換処理
		 * @param txt 変換対象の文字列
		 * @return String 変換後の文字列
		 */
		public static String replaceMYoutube(String txt){
			String viewTube = "[動画(YouTube)]";

			StringBuffer sb = new StringBuffer();
			Pattern p = Pattern.compile("\\[y:([^\"^\\]]+)\\]");

			//正規表現実行
			Matcher m = p.matcher(txt);
			// 検索(find)し、マッチする部分文字列がある限り繰り返す
			while(m.find()){
				//部分文字列取得
				String partStr = m.group(1);

				//youTubeの可変変数を置換
				String youTube = viewTube.replaceAll("XXXX", partStr);

				//文字列連結
				m.appendReplacement(sb, youTube);
			}

			//残りの文字列連結
			m.appendTail(sb);

			return sb.toString();

		}

		/**
		 * googleMapタグの変換処理
		 * @param txt 変換対象の文字列
		 * @return String 変換後の文字列
		 */
		public static String replaceGoogleMap(String txt){
			//初期化
			String mapTag = "";
			StringBuffer sb = new StringBuffer();
			int idNo = 1;
			Pattern p = Pattern.compile("\\[gm:([^\"^\\]]+)\\]");

			//置換用タグ
			final String mapTags[] = {"<div class=\"insertMap\" id=\"map_ZZZZ\">",
									  "<script type=\"text/javascript\">",
									  "map.push({",
									  "  map_id : \"map_ZZZZ\",",
									  "  size   : { width : 480, height : 480 },",
									  "  url    : { embed : \"http://maps.google.co.jp/mapsXXXX\", ",
									  "  link : \"http://maps.google.co.jp/mapsYYYY\" }",
									  "});",
									  "Event.observe(window, 'load', setupMap)",
									  "</script>",
									  "</div>"};

			//タグを１行にする。
			for (int i=0;i<mapTags.length;i++){
				mapTag = mapTag + mapTags[i];
			}

			//正規表現実行
			Matcher m = p.matcher(txt);
			// 検索(find)し、マッチする部分文字列がある限り繰り返す
			while(m.find()){
				//部分文字列取得
				String partStr = m.group(1);
				//部分文字列からlinkに不必要な文字列を削除
				String partStr2 = partStr.replaceAll("&source=s_q", "");

				//googleMapの可変変数を置換(URL部分)
				String googleMap = mapTag.replaceAll("XXXX", partStr);
				//googleMapの可変変数を置換(map_XのX部分)
				googleMap = googleMap.replaceAll("ZZZZ", String.valueOf(idNo));
				//googleMapの可変変数を置換(link部分)
				googleMap = googleMap.replaceAll("YYYY", partStr2);

				//文字列連結
				m.appendReplacement(sb, googleMap);

				//地図の数だけインクリメント（idを変える必要があるため）
				idNo++;
			}

			//残りの文字列連結
			m.appendTail(sb);

			return sb.toString();
		}

		/**
		 * 携帯版googleMapタグの変換処理
		 * @param txt 変換対象の文字列
		 * @return String 変換後の文字列
		 */
		public static String replaceMGoogleMap(String txt){
			//初期化
			String mapTag = "";
			StringBuffer sb = new StringBuffer();
			int idNo = 1;
			Pattern p = Pattern.compile("\\[gm:([^\"^\\]]+)\\]");

			//置換用タグ
			final String mapTags[] = {"[地図(Google マップ)]"};

			//タグを１行にする。
			for (int i=0;i<mapTags.length;i++){
				mapTag = mapTag + mapTags[i];
			}

			//正規表現実行
			Matcher m = p.matcher(txt);
			// 検索(find)し、マッチする部分文字列がある限り繰り返す
			while(m.find()){
				//部分文字列取得
				String partStr = m.group(1);
				//部分文字列からlinkに不必要な文字列を削除
				String partStr2 = partStr.replaceAll("&source=s_q", "");

				//googleMapの可変変数を置換(URL部分)
				String googleMap = mapTag.replaceAll("XXXX", partStr);
				//googleMapの可変変数を置換(map_XのX部分)
				googleMap = googleMap.replaceAll("ZZZZ", String.valueOf(idNo));
				//googleMapの可変変数を置換(link部分)
				googleMap = googleMap.replaceAll("YYYY", partStr2);

				//文字列連結
				m.appendReplacement(sb, googleMap);

				//地図の数だけインクリメント（idを変える必要があるため）
				idNo++;
			}

			//残りの文字列連結
			m.appendTail(sb);

			return sb.toString();
		}


		/**
		 * 装飾タグの復活処理
		 * @param moji 復活させたい装飾タグを含む文字列
		 * @return String 復活後の文字列
		 */
		public static String reSanitizing(String moji){
			Matcher mt;

			// 復活対象のタグリスト
			final String[] DELTAGS = {
					"&lt;/span&gt;",
					"&lt;strong&gt;",
					"&lt;/strong&gt;",
					"&lt;em&gt;",
					"&lt;/em&gt;",
					"&lt;u&gt;",
					"&lt;/u&gt;",
					"&lt;del&gt;",
					"&lt;/del&gt;",
					"&lt;/a&gt;",
					"&lt;blockquote&gt;",
					"&lt;/blockquote&gt;"
				};

			// 装飾タグの復活
			for (String i : DELTAGS) {

				// 大文字小文字の区別をしない
				mt = Pattern.compile(i,Pattern.CASE_INSENSITIVE).matcher(moji);

				if (i == DELTAGS[0]){
					moji = mt.replaceAll("</span>");
				} else if (i == DELTAGS[1]){
					moji = mt.replaceAll("<strong>");
				} else if (i == DELTAGS[2]){
					moji = mt.replaceAll("</strong>");
				} else if (i == DELTAGS[3]){
					moji = mt.replaceAll("<em>");
				} else if (i == DELTAGS[4]){
					moji = mt.replaceAll("</em>");
				} else if (i == DELTAGS[5]){
					moji = mt.replaceAll("<u>");
				} else if (i == DELTAGS[6]){
					moji = mt.replaceAll("</u>");
				} else if (i == DELTAGS[7]){
					moji = mt.replaceAll("<del>");
				} else if (i == DELTAGS[8]){
					moji = mt.replaceAll("</del>");
				} else if (i == DELTAGS[9]){
					moji = mt.replaceAll("</a>");
				} else if (i == DELTAGS[10]){
					moji = mt.replaceAll("<blockquote>");
				} else if (i == DELTAGS[11]){
					moji = mt.replaceAll("</blockquote>");
				}
			}

			final String[] DELTAGS2 = {
					"&lt;span (class=(&quot;|\'|)(large|medium|small)(&quot;|\'|))&gt;",
					"&lt;span (style=(&quot;|\'|)color:#([a-zA-Z0-9]+)(&quot;|\'|))&gt;",
					"&lt;a ((?!&gt;).+)(&gt;)"
					//"&lt;a ((?!&gt;).+)(&gt;)(.*)&lt;/a&gt;"
				};

			for (String i : DELTAGS2) {
				StringBuffer sb = new StringBuffer();

				// 大文字小文字の区別をしない
				mt = Pattern.compile(i,Pattern.CASE_INSENSITIVE).matcher(moji);

				//検索(find)し、マッチする部分文字列がある限り繰り返す
				while(mt.find()){
					//マッチした部分文字列を抽出
					String partStr = mt.group(0);

					//タグの復活
					String str = "";

					if (i == DELTAGS2[0]){
						String partStr2 = mt.group(1);
						str = partStr.replaceAll("&lt;span (class=(&quot;|\'|)(large|medium|small)(&quot;|\'|))&gt;", "<span " + partStr2 + ">");
					} else if (i == DELTAGS2[1]){
						String partStr2 = mt.group(1);
						str = partStr.replaceAll("&lt;span (style=(&quot;|\'|)color:#([a-zA-Z0-9]+)(&quot;|\'|))&gt;", "<span " + partStr2 + ">");
					} else if (i == DELTAGS2[2]){
						String partStr2 = mt.group(1);
						str = partStr.replaceAll("&lt;a ((?!&gt;).+)(&gt;)", "<a " + partStr2 + ">");
						str = str.replaceAll("(&gt;)", ">");

					}

					//文字結合
					mt.appendReplacement(sb, str);

				}

				mt.appendTail(sb);

				moji = sb.toString();
			}

			moji = moji.replaceAll("&quot;", "\"");

			return moji;
		}

		/**
		 * 画像ファイルアップロードの入力チェック処理
		 * サイズチェックとファイル形式チェックを行う
		 * @param errors エラーメッセージ
		 * @param photo チェック対象のファイル
		 * @param no ファイルの番号
		 * @return
		 */
		public static void checkPhotoFile(ActionMessages errors,FormFile photo,String no){

			if(photo.getFileName().length()>0){
				if(photo.getFileSize()==0){
					//ファイルサイズチェック
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.upload.other",new Object[] {no}));
				}else if(!photo.getContentType().equalsIgnoreCase("image/pjpeg")&&!photo.getContentType().equalsIgnoreCase("image/jpeg")){
				//ファイル形式チェック
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.upload.type",new Object[] {no}));
				}
			}

		   	//return errors;
		}

		/**
		 * URLを<a>タグにして表示する処理
		 * @param moji 置換対象文字列
		 * @return String 置換後の文字列
		 */
		public static String convURL(String moji){

			//改行直後のURLに対応
			//半角スペースの後のURLに対応
			//URLのみに対応
			final Pattern convURLLinkPtn = Pattern.compile("(((\\n(http://|https://))|( (http://|https://))|^(http://|https://)){1}[\\w\\.\\-/:\\#\\?\\=\\&\\;\\%\\~\\+]+)");
			
			StringBuffer sb = new StringBuffer();
			String link = "<a href=\"XXX\" target=\"_blank\">XXX</a>";
			Matcher matcher = convURLLinkPtn.matcher(moji);
			
			// 検索(find)し、マッチする部分文字列がある限り繰り返す
			while(matcher.find()){
				//部分文字列取得
				String partStr = matcher.group(1);
				//部分文字列からlinkに不必要な文字列を削除
				String partStr2 = partStr.replaceAll(" ", "");
				String linkStr = link.replaceAll("XXX", partStr2).replaceAll("\r\n","").replaceAll("\r","").replaceAll("\n","");
				//文字列連結
				matcher.appendReplacement(sb, " " + linkStr);

			}

			//残りの文字列連結
			matcher.appendTail(sb);
			
			return sb.toString();

		}

		/**
		 * 指定したバイト数で文字の切り出しを行う処理
		 * @param moji 切り出し対象文字列
		 * @return String 切り出し後の文字列
		 */
		public static String substrByte(String moji,int len){
			//変数初期化
			int i=0;
			StringBuffer sb = new StringBuffer();
			//文字列置換処理
			if(moji.getBytes().length>len){
				while(sb.toString().getBytes().length<len){
					sb.append(moji.charAt(i));
					i++;
				}
				sb.append("…");
				moji = sb.toString();
			}
			return moji;
		}

		/**
		 * レスポンスデータの設定を行う処理
		 * @param text レスポンスデータ
		 * @param contentType
		 * @param encoding エンコード指定
		 * @param charset 文字コード
		 * @return
		 */
	    public static void ResponseWrite(String text, String contentType, String encoding,String charset) {
	        if (contentType == null) {
	            contentType = "text/plain";
	        }
	        if (encoding == null) {
	            encoding = RequestUtil.getRequest().getCharacterEncoding();
	            if (encoding == null) {
	                encoding = "UTF-8";
	            }
	        }
			HttpServletResponse response =getResponse();

	        response.setContentType(contentType + "; charset="+charset);


	        try {
	            PrintWriter out = null;
	            try {
	                out = new PrintWriter(new OutputStreamWriter(response
	                        .getOutputStream(), encoding));
	                out.print(text);
	            } finally {
	                if (out != null) {
	                    out.close();
	                }
	            }
	        } catch (IOException e) {
	            throw new IORuntimeException(e);
	        }
	    }

	    public static HttpServletResponse getResponse() {
	        return SingletonS2Container.getComponent(HttpServletResponse.class);
	    }

		/**
		 * ディレクトリを作成する（共通で利用可能）
		 * @param Path 作成するディレクトリパス
		 * @return なし
		 */
		public static void cmnMakeDir(String Path){
			// ディレクトリ存在チェック
			File file = new File(Path);
			if(!file.exists()){
				// ディレクトリが存在しなければ作成
				try{ file.mkdir(); }
				catch(Exception e){ e.printStackTrace(); }
			}
		}

		/**
		 * ディレクトリを作成する（共通で利用可能）
		 * @param l リスト型にしたい文字列
		 * @return List
		 */
		public static List StringToArray(List l){
			String s = l.get(0).toString();
			s = s.replace("[","").replace("]","").replace(" ","");
			l.clear();
			for(int i=0;i<s.split(",").length;i++){
				l.add(s.split(",").length>1?s.split(",")[i]:s);
			}
			return l;
		}


		/**
		 * FShoutコメント返信する際のユーザーを<a>タグにして表示する処理
		 * @param moji 置換対象文字列
		 * @return String 置換後の文字列
		 */
		public static String convReCom(String moji,String mid){
			//final Pattern convURLLinkPtn = Pattern.compile("(http://|https://){1}[\\w\\.\\-/:\\#\\?\\=\\&\\;\\%\\~\\+]+");

			//final Pattern convURLLinkPtn = Pattern.compile("(@)\\S+( )");
			final Pattern convURLLinkPtn = Pattern.compile("(@)\\S+( )");


			Matcher matcher = convURLLinkPtn.matcher(moji);
			moji = matcher.replaceAll("<a href=\"/frontier/pc/mem/"+mid+"\" title=\"$0\">$0</a>");

			return moji;
		}

		/**
		 * 外部FrontierからFollowリストを取得し、追加して返す
		 * @param url 取得するurl
		 * @param flist 取得したFollowリストを追加する変数
		 * @return List<BeanMap> Followリストを追加して返す
		 * @throws Exception
		 */
		public static List<BeanMap> getOFrontierFList(String url,List<BeanMap> flist) throws Exception{
			// 変数定義
			NodeList nlist     = null;
			Element nlistelmnt = null;
			String domain      = "";

			// フォローリストxmlファイルの読み込み&リストの作成
			try {
				// ドキュメントビルダーファクトリを生成
				DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
				// ドキュメントビルダーを生成
				DocumentBuilder builder = dbfactory.newDocumentBuilder();
				// パースを実行してDocumentオブジェクトを取得
				Document doc = builder.parse(url);
				// ルート要素を取得
				Element root = doc.getDocumentElement();
				// NodeListに格納
				nlist = root.getElementsByTagName("mem");
				// domain取得
				domain = root.getElementsByTagName("domain").item(0).getChildNodes().item(0).getNodeValue();;

			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception(e);
			}

			// 取得したメンバーリスト分ループ
			if(nlist.getLength()>0){
				List<BeanMap> Mlist = new ArrayList<BeanMap>();
				for(int i=0;i<nlist.getLength();i++){
					// 変数設定
					String imid      = "";
					String inickname = "";
					String ipic      = "";
					nlistelmnt = (Element)nlist.item(i);
					//mid
					imid = nlistelmnt.getChildNodes().item(1).getChildNodes().item(0).getNodeValue();
					//nickname
					inickname = nlistelmnt.getChildNodes().item(3).getChildNodes().item(0).getNodeValue();
					//pic
					NodeList picNode = nlistelmnt.getChildNodes().item(5).getChildNodes();
					if(picNode.getLength() > 0){
						ipic = picNode.item(0).getNodeValue();
					}

					// BeanMap に変数を設定
					BeanMap b = new BeanMap();
					b.put("mid",imid); //FID
					b.put("nickname",inickname);
					b.put("pic",ipic);
					b.put("membertype",1); // 他Frontierメンバー
					b.put("fpic",ipic);
					b.put("frontierdomain",domain);
					b.put("fid",imid);
					b.put("lfnickname",inickname+" ["+domain+"]");
					b.put("sfnickname",inickname+" [F]");
					b.put("lastlogin","");
					Mlist.add(b);
				}

				// 取得したデータを詰める
				if(Mlist.size()>0){
					for(int i=0;i<Mlist.size();i++){
						flist.add(Mlist.get(i));
					}
				}
			}

			return flist;
		}

		/**
		 * SQL特殊文字の無害化
		 * @param word 検索文字列
		 * @return　無害化した後の検索文字列
		 */
		public static String replaceSql(String word){
			//SQL特殊文字の無害化
			if(word!=null){
				word = word.replaceAll("#", "##");
				word = word.replaceAll("%", "#%");
				word = word.replaceAll("_", "#_");
			}

			return word;
		}

		/**
		 * 一覧画面の本文編集
		 * @param r 一覧データ(List<BeanMap>)
		 * @param cmnt 本文のカラム名
		 * @param p1 写真1のカラム名
		 * @param p2 写真2のカラム名
		 * @param p3 写真3のカラム名
		 * @param pnote1 写真1の説明
		 * @param pnote2 写真2の説明
		 * @param pnote3 写真3の説明
		 * @param EmojiXmlPath 絵文字リストのxmlパス
		 * @param CmnListCmntMax 一覧で表示するリストの最大文字数
		 * @param CmnEmojiImgPath 絵文字イメージのパス
		 * @param CmnContentsRoot 画像イメージのパス
		 * @param deltags 削除対象のタグリスト
		 * @return なし
		 * @comment
		 *  rtnpic イメージタグに変換された写真1-3(そのまま使用する(エスケープしない))(r.rtnpicに入る)
		 *  rtncmnt 整形されたコメント(そのまま使用する(エスケープ処理済))(r.rtncmntに入る)
		 */
		public static void editcmnt(
			List<BeanMap> r,
			String cmnt,
			String p1,
			String p2,
			String p3,
			String pnote1,
			String pnote2,
			String pnote3,
			String EmojiXmlPath,
			Integer CmnListCmntMax,
			String CmnEmojiImgPath,
			String CmnContentsRoot,
			String[] deltags
		){
			NodeList nlist = null;
			Matcher mt;
			Matcher mt1;
			Matcher mt2;
			String inm = "";
			String ialt = "";
			String rtnpic  = "";
			String rtncmnt = "";
			String rtncmnt1 = "";
			String rtncmnt2 = "";
			Integer cnt = 0;
			Integer cnt1 = 0;
			Integer cnt2 = 0;
			Integer cnti = 0;

			// i-mode絵文字xmlファイルの読み込み&リストの作成
			try {
				// ドキュメントビルダーファクトリを生成
				DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
				// ドキュメントビルダーを生成
				DocumentBuilder builder = dbfactory.newDocumentBuilder();
				// パースを実行してDocumentオブジェクトを取得
				Document doc = builder.parse(new File(EmojiXmlPath));
				// ルート要素を取得
				Element root = doc.getDocumentElement();
				// Listに格納
				nlist = root.getElementsByTagName("entry");
			} catch (Exception e) {
				e.printStackTrace();
			}

			// データループ
			for (BeanMap b : r) {
				// 初期化
				rtnpic  = "";
				rtncmnt = "";
				rtncmnt1 = "";
				rtncmnt2 = "";
				cnt = 0;
				cnt1 = 0;
				cnt2 = 0;
				cnti = 0;
				// フォトアルバム一覧は画像変換処理をスルー
				if(!StringUtils.isNullOrEmpty(CmnContentsRoot)){
					// 画像１～３のHTMLタグ化
					rtnpic = editimg(b,p1,p2,p3,pnote1,pnote2,pnote3,CmnContentsRoot);
					// 画像HTMLをput
					b.put("pichtml",rtnpic);
				}
				rtncmnt += b.get(cmnt);
				// 装飾タグの削除
				for (String i : deltags) {
					// 大文字小文字の区別をしない
					mt = Pattern.compile(i,Pattern.CASE_INSENSITIVE).matcher(rtncmnt);
					// ブランクにreplace
					rtncmnt = mt.replaceAll("");
				}

				// 指定文字数よりも長かった場合
				if (rtncmnt.length()>CmnListCmntMax){

					// 指定文字数でsubstring
					// 前半(0-指定文字数)
					rtncmnt1 = rtncmnt.substring(0,CmnListCmntMax);
					// 後半(指定文字数-最後)
					rtncmnt2 = rtncmnt.substring(CmnListCmntMax,rtncmnt.length());
					// i-mode絵文字タグの数を数える
					mt = Pattern.compile("\\[i:([0-9]+)\\]",Pattern.CASE_INSENSITIVE).matcher(rtncmnt);
					mt1 = Pattern.compile("\\[i:([0-9]+)\\]",Pattern.CASE_INSENSITIVE).matcher(rtncmnt1);
					mt2 = Pattern.compile("\\[i:([0-9]+)\\]",Pattern.CASE_INSENSITIVE).matcher(rtncmnt2);
					while (mt.find()){cnt++;}
					while (mt1.find()){cnt1++;}
					while (mt2.find()){cnt2++;}
					// 全体 != 前半 + 後半 の絵文字数じゃない場合、途中で切れてるので処理開始
					if (cnt != (cnt1 + cnt2)){
						cnti = 0;
						// 最後尾のi-mode絵文字タグがなくなるまでループ
						while(cnt != (cnt1 + cnt2)){
							cnti ++;
							cnt1 = 0;
							cnt2 = 0;
							rtncmnt1 = rtncmnt.substring(0,CmnListCmntMax-cnti);
							rtncmnt2 = rtncmnt.substring(CmnListCmntMax-cnti,rtncmnt.length());
							mt1 = Pattern.compile("\\[i:([0-9]+)\\]",Pattern.CASE_INSENSITIVE).matcher(rtncmnt1);
							mt2 = Pattern.compile("\\[i:([0-9]+)\\]",Pattern.CASE_INSENSITIVE).matcher(rtncmnt2);
							while (mt1.find()){cnt1++;}
							while (mt2.find()){cnt2++;}
						}
					}
					rtncmnt = rtncmnt1;
					rtncmnt += "．．．";
				}

				// サニタイジング処理
				rtncmnt = htmlSanitizing(rtncmnt);

				// iモード絵文字の変換
				// 絵文字List数分ループ
				for (int i=0; i < nlist.getLength() ; i++) {
					Element nelmnt = (Element)nlist.item(i);
					inm = nelmnt.getAttribute("id");
					ialt = nelmnt.getAttribute("name");
					mt= Pattern.compile("\\[i:"+ inm +"\\]",Pattern.CASE_INSENSITIVE).matcher(rtncmnt);
					// リストとマッチすればイメージタグに変換
					rtncmnt = mt.replaceAll("<img src=\"" + CmnEmojiImgPath + inm + ".gif\" alt=\"" + ialt + "\"/>");
				}
				// コメントHTMLをput
				b.put("cmnthtml",rtncmnt);
			}
		}

		/**
		 * bit.lyサービスを利用したURLの短縮化
		 * @param longUrl 短縮化するURL
		 * @return rtnpic 短縮化されたURL
		 */
		public static String makeBitLy(AppDefDto appDefDto,String longUrl){
			//bit.lyのユーザ情報設定
			String bitUer = appDefDto.BIT_USER_ID;
			String bitKey = appDefDto.BIT_API_KEY;

			HttpClient httpclient = new HttpClient();
			PostMethod httppost = new PostMethod("http://api.bit.ly/shorten");

			//必要なパラメータ設定
			httppost.addParameter("version", "2.0.1");
			httppost.addParameter("format", "json");
			httppost.addParameter("longUrl", longUrl);
			httppost.addParameter("login", bitUer);
			httppost.addParameter("apiKey", bitKey);

			int statusCode;
			InputStream ins = null;
			String shortUrl;
			try {
				//POSTの実行
				statusCode = httpclient.executeMethod(httppost);

				//URLの結果を受け取る
				ins = httppost.getResponseBodyAsStream();

				//JSONからURLが入ったMAPを取得する
				Map<String,Object> lm = (Map<String,Object>) JSON.decode(ins);
				Map<String,Map<String,String>> mms = (Map<String, Map<String, String>>) lm.get("results");

				//短縮化されたURLを取得する
				shortUrl = mms.get(longUrl).get("shortUrl");

			} catch (HttpException e) {
				e.printStackTrace();
				return "NG";
			} catch (IOException e) {
				e.printStackTrace();
				return "NG";
			}finally{
				//InputStreamを閉じる
				try {
					ins.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			return shortUrl;
		}

		/**
		 * JavaScript用のエスケープ処理
		 * @param getval 対象の文字列
		 * @return rtnval エスケープ後の文字列
		 */
		public static String escJavaScript(String getval){
			String rtnval = "";
			rtnval = getval;
			rtnval = rtnval.replaceAll("\\\\","\\\\\\\\");
			rtnval = rtnval.replaceAll("\\\"","\\\\\\\"");
			rtnval = rtnval.replaceAll("'","\\\\'");
			rtnval = rtnval.replaceAll("/","\\\\/");
			rtnval = rtnval.replaceAll("<","\\\\x3c");
			rtnval = rtnval.replaceAll(">","\\\\x3e");
			rtnval = rtnval.replaceAll("0x0D","\\\\r");
			rtnval = rtnval.replaceAll("0x0A","\\\\n");
			return rtnval;
		}

		/**
		 * 指定された正規表現のパターンに基づくMatcherクラスを生成する処理
		 * @param p 正規表現のパターン
		 * @param moji 調査対象の文字列
		 * @return 指定された正規表現のパターンに基づくMatcherクラス
		 */		
		public static Matcher makeRegularExpress(String p,String moji){
			//正規表現のパターンに一致するパターンオブジェクトの作成
			Pattern convURLLinkPtn = Pattern.compile(p);
			//matcherオブジェクトの作成
			Matcher matcher = convURLLinkPtn.matcher(moji);
			return matcher;
		}
				
}