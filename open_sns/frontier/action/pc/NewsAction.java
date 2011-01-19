package frontier.action.pc;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.entity.Information;
import frontier.entity.Members;
import frontier.form.pc.NewsForm;
import frontier.service.MembersService;
import frontier.service.NewsService;

public class NewsAction {
	
	Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Resource
	public UserInfoDto userInfoDto;
	
	@Resource
	public AppDefDto appDefDto;
	
	@Resource
	protected MembersService membersService;
	
	@Resource
	protected NewsService newsService;
	
	@ActionForm
	@Resource
	protected NewsForm newsForm;
	
	public Members members;
	//お知らせ一覧
	public List<Information> results;
	//お知らせデータ
	public Information viewResults;
	//お知らせ件数
    public long resultscnt;
    //今日の日付
    public String today = CmnUtility.getToday("yyyyMMdd");
    
    public ActionMessages errors = new ActionMessages();
	
	//お知らせ一覧
	@Execute(validator=false,reset="reset")
	public String list(){
    	//visitMemberIdの設定
    	setVisitMemberId();
		
		//初期化
		init();
		//DB処理
		initListNews();
		return "list.jsp";
	}
	
	//再表示
	@Execute(validator=false)
	public String myView(){
    	//visitMemberIdの設定
    	setVisitMemberId();
		
		//DB処理
		initListNews();
		return "list.jsp";
	}
	
	//お知らせ閲覧
	@Execute(validator=false,urlPattern="view/{newsId}")
	public String view(){
    	//visitMemberIdの設定
    	setVisitMemberId();
		
		//初期化
		init();
		//お知らせデータ取得
		initViewList();
		//データ存在チェック
		if(viewResults==null) return "error.jsp"; 
		
		return "view.jsp";
	}
	
	//お知らせ編集画面
	@Execute(validator=false,urlPattern="edit/{newsId}")
	public String edit(){
    	//visitMemberIdの設定
    	setVisitMemberId();
    	
		init();
		//権限チェック
		if(newsForm.informationflg==null) return "error.jsp";
		if(!newsForm.informationflg.equals("1")) return "error.jsp";
		//ページ判別フラグ設定
		newsForm.pageFlg = "1";//編集
		//お知らせデータ取得
		viewResults = newsService.selNews(newsForm.newsId);
		logger.debug("編集画面"+viewResults);
		
		//データ存在チェック
		if(viewResults==null) return "error.jsp"; 
		
		//formの変数に設定
		if(viewResults!=null){
			newsForm.year=viewResults.dispdate.substring(0,4);
			newsForm.month=viewResults.dispdate.substring(4,6);
			newsForm.day=viewResults.dispdate.substring(6,8);
			newsForm.title=viewResults.title;
			newsForm.detail=viewResults.comment;
			newsForm.topflg=viewResults.topflg;
		}
		return "input.jsp";
	}
	
	//お知らせ登録画面
	@Execute(validator=false,reset="reset")
	public String entry(){
    	//visitMemberIdの設定
    	setVisitMemberId();
		
		init();
		//権限チェック
		if(newsForm.informationflg==null) return "error.jsp";
		if(!newsForm.informationflg.equals("1")) return "error.jsp";
		//form変数設定
		newsForm.year=today.substring(0,4);
		newsForm.month=today.substring(4,6);
		newsForm.day=today.substring(6,8);
		
		return "input.jsp";
	}
	
	//次を表示リンク押下時
	@Execute(validator=false)
	public String nxtpg(){
		logger.debug("offset"+newsForm.offset);
		//改ページ処理実行
		setNewsPage(1);
		
		//F5対策
		return "/pc/news/myView?redirect=true";
	}
	
	//前を表示リンク押下時
	@Execute(validator=false)
	public String prepg(){
		//改ページ処理実行
		setNewsPage(-1);		
		
		//F5対策
		return "/pc/news/myView?redirect=true";
	}
	
	//設定する押下
	@Execute(validator=false)
	public String setting(){
		//ﾒﾝﾊﾞｰ情報の取得
		members = membersService.getResultById(userInfoDto.memberId);
		//お知らせ変更権限の設定
		if(members!=null) newsForm.informationflg=members.informationflg;
		//権限チェック
		if(newsForm.informationflg==null) return "error.jsp";
		if(!newsForm.informationflg.equals("1")) return "error.jsp";
		
		newsService.updNewsDispSetting(newsForm.chkList,userInfoDto.memberId,newsForm.offset,appDefDto.FP_CMN_LIST_NEWSMAX);
		logger.debug(newsForm.chkList);
		return "/pc/news/myView?redirect=true";
	}
	
	//登録する押下
	@Execute(validate="chkDate",input="inputError",reset="clear")
	public String entryNews(){
		//ﾒﾝﾊﾞｰ情報の取得
		members = membersService.getResultById(userInfoDto.memberId);
		//お知らせ変更権限の設定
		if(members!=null) newsForm.informationflg=members.informationflg;
		//権限チェック
		if(newsForm.informationflg==null) return "error.jsp";
		if(!newsForm.informationflg.equals("1")) return "error.jsp";
		newsService.insNews(userInfoDto.memberId,newsForm);
		
		return "list?redirect=true";
	}
	
	//編集する押下
	@Execute(validate="chkDate",input="inputError",reset="clear")
	public String editNews(){
		//ﾒﾝﾊﾞｰ情報の取得
		members = membersService.getResultById(userInfoDto.memberId);
		//お知らせ変更権限の設定
		if(members!=null) newsForm.informationflg=members.informationflg;
		//権限チェック
		if(newsForm.informationflg==null) return "error.jsp";
		if(!newsForm.informationflg.equals("1")) return "error.jsp";
		newsService.updNews(userInfoDto.memberId,newsForm);
		
		return "list?redirect=true";
	}
	
	//お知らせ削除
	@Execute(validator=false)
	public String delete(){
    	//visitMemberIdの設定
    	setVisitMemberId();

		logger.debug("お知らせ削除"+newsForm.newsId);
		//権限チェック
		if(newsForm.informationflg==null) return "error.jsp";
		if(!newsForm.informationflg.equals("1")) return "error.jsp";
		if(!newsForm.pageFlg.equals("1")) return "error.jsp";
		//お知らせデータ取得
		initViewList();
		return "delete.jsp";
	}
	
	//やめる
	@Execute(validator=false)
	public String cansel(){
		return "/pc/news/edit/"+newsForm.newsId+"?redirect=true";
	}
	
	//削除する
	@Execute(validator=false)
	public String deleteNews(){
		//権限チェック
		if(newsForm.informationflg==null) return "error.jsp";
		if(!newsForm.informationflg.equals("1")) return "error.jsp";
		try{
			newsService.delNews(newsForm.newsId);
		}catch(Exception e){
			
		}
		return "/pc/news/list?redirect=true";
		
	}
	
	//登録・編集時エラー
	@Execute(validator=false)
	public String inputError(){
    	//visitMemberIdの設定
    	setVisitMemberId();

		//権限チェック
		if(newsForm.informationflg==null) return "error.jsp";
		if(!newsForm.informationflg.equals("1")) return "error.jsp";
		return "input.jsp";
	}
	
	//お知らせ一覧改ページ処理
	private void setNewsPage(int num){
		try {
			//総件数取得
			resultscnt = newsService.cntNewsList(); 
			// ページ遷移用の計算
			newsForm.pgcnt+=num;
			//ページング範囲チェック
			if(newsForm.pgcnt<0||newsForm.pgcnt * appDefDto.FP_CMN_LIST_NEWSMAX>resultscnt) newsForm.pgcnt-=num;
			// オフセット設定
			newsForm.offset = newsForm.pgcnt * appDefDto.FP_CMN_LIST_NEWSMAX;

		} catch (Exception e) {
			// 計算できない場合は初期値セット
			newsForm.pgcnt = 1;
			newsForm.offset = 0;
		}		
	}
	
	//共通処理
	private void init(){
		//デフォルト設定
		newsForm.informationflg="";
		newsForm.offset=0;
		newsForm.pgcnt=0;
		//ﾒﾝﾊﾞｰ情報の取得
		members = membersService.getResultById(userInfoDto.memberId);
		//お知らせ変更権限の設定
		if(members!=null) newsForm.informationflg=members.informationflg;
	}
	
	//閲覧系共通処理
	private void initViewList(){
		viewResults = newsService.selNews(newsForm.newsId);
		//タグ変換
		if(viewResults!=null) viewResults.comment = resetResults(viewResults.comment);
	}
	
	//本文装飾
	private String resetResults(String comment){
		
		if(comment!=null){
			//サニタイジング
			comment = CmnUtility.htmlSanitizing(comment);
					
			//一部のタグを有効にする span u li ・・・
			comment = CmnUtility.reSanitizing(comment);
			
			//URLを<a>タグに変換
			//comment = CmnUtility.convURL(comment);
			
			//YouTubeタグ変換
			comment = CmnUtility.replaceYoutube(comment);
			
			//googleMapタグ変換
			comment = CmnUtility.replaceGoogleMap(comment);
			
		    //絵文字装飾
		  	String emojiComment = CmnUtility.replaceEmoji(comment,appDefDto.FP_CMN_EMOJI_IMG_PATH,appDefDto.FP_CMN_EMOJI_XML_PATH);
			  	
		  	return emojiComment;
		}
		return null;

	}
	
	//日付チェック処理
	public ActionMessages chkDate(){
		logger.debug("check"+newsForm.topflg);
		//初期値設定
		Integer year = Integer.parseInt(newsForm.year);
		Integer month = Integer.parseInt(newsForm.month);
		Integer day = Integer.parseInt(newsForm.day);
		int[] monthList = {31,28,31,30,31,30,31,31,30,31,30,31};
		
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


		return errors;
	}
	
	//一覧DB処理
	private void initListNews(){
		//お知らせ一覧データ取得
		results = newsService.selNewsList(newsForm.offset,appDefDto.FP_CMN_LIST_NEWSMAX);
		//お知らせ一覧件数取得
	    resultscnt = newsService.cntNewsList(); 
	}
	
	//ボタン制御用visitMemberId設定処理
	private void setVisitMemberId(){
    	//ボタン出し分け設定
    	userInfoDto.visitMemberId = userInfoDto.memberId;
	}
}
