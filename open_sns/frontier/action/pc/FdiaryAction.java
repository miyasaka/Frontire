package frontier.action.pc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.form.pc.FdiaryForm;
import frontier.service.DiaryService;
import frontier.service.GroupService;

public class FdiaryAction {
	Logger logger = Logger.getLogger(this.getClass().getName());

	@Resource
	public UserInfoDto userInfoDto;
	@Resource
	protected DiaryService diaryService;
	@Resource
	protected GroupService groupService;
	@Resource
	public AppDefDto appDefDto;
	@ActionForm
	@Resource
	public FdiaryForm fdiaryForm;
	
	//画面表示用
	public List<BeanMap> results;
    public List<Map<String,Integer>> cal;
    public List<BeanMap> GroupList;
    public String today = CmnUtility.getToday("yyyyMMdd");
    public long resultscnt;

    //カレンダーのリンク表示用
    protected List<BeanMap> monthResults;
	
	//外部用日記一覧画面：メンバー日記ボタンより遷移
	@Execute(validate="checkAuth",input="/pc/top/")
	public String index(){
		fdiaryForm.diaryDay = today;
		fdiaryForm.caltype = "0";
		fdiaryForm.groupid = "0";
		
		//ボタン出し分け設定
		setVisitMemberId();
		
		//初期化
		initPageCnt();
		
		//日記検索
		searchDiary("0",null);
		
		//グループ検索
		searchGroup();
		
		return "index.jsp";
	}

	//外部用日記一覧画面：再表示
	@Execute(validate="checkAuth",input="/pc/top/")
	public String view(){
		//ボタン出し分け設定
		setVisitMemberId();
		
		//日記検索
		if(fdiaryForm.caltype.equals("2")){
			//日付リンクを押した後の改ページ
			searchDiary("0",fdiaryForm.diaryDay);			
		}else{
			//上記以外の場合の改ページ
			searchDiary("0",fdiaryForm.diaryDay.substring(0, 6));			
		}
		
		//グループ検索
		searchGroup();
		
		return "index.jsp";
	}	
	
	//外部用日記一覧画面：「<」を押した場合の処理
	@Execute(validator=false)
	public String beforeMonth(){
		//初期化
		initPageCnt();	
		
		fdiaryForm.caltype = "1";
		
		//月の減算
		fdiaryForm.diaryDay = CmnUtility.calcCalendar(fdiaryForm.diaryDay,-1);
		
		//F5対策
		return "/pc/fdiary/view?redirect=true";
	}
	
	//外部用日記一覧画面：「>」を押した場合の処理
	@Execute(validator=false)
	public String nextMonth(){
		//初期化
		initPageCnt();	

		fdiaryForm.caltype = "1";
		
		//月の加算
		fdiaryForm.diaryDay = CmnUtility.calcCalendar(fdiaryForm.diaryDay,1);

		//F5対策		
		return "/pc/fdiary/view?redirect=true";
	}
	
	//外部用日記一覧画面：グループ変更した場合の処理
	@Execute(validate="checkAuth",input="/pc/top/")
	public String selgroup(){
//		fdiaryForm.diaryDay = today;
//		fdiaryForm.caltype = "0";
		
		//ボタン出し分け設定
		setVisitMemberId();
		
		//初期化
		initPageCnt();
		
		//日記検索
		if(fdiaryForm.caltype.equals("2")){
			//日付リンクを押した後の改ページ
			searchDiary("1",fdiaryForm.diaryDay);			
		}else{
			//上記以外の場合の改ページ
			searchDiary("1",fdiaryForm.diaryDay.substring(0, 6));			
		}
		//searchDiary("1",fdiaryForm.diaryDay);
		
		//グループ検索
		searchGroup();

		return "index.jsp";
	}
	
	//外部用日記一覧画面：日付リンクを押した場合の処理
	@Execute(validate="checkAuth",input="/pc/top/",urlPattern="searchDay/{diaryDay}")
	public String searchDay(){
		//ボタン出し分け設定
		setVisitMemberId();
		
		fdiaryForm.caltype = "2";
		
		//初期化
		initPageCnt();
		
		//日記検索
		searchDiary("0",fdiaryForm.diaryDay);
		
		//グループ検索
		searchGroup();
		
		return "index.jsp";
	}
	
	//グループ・メンバー一覧画面：次を表示リンク押下時
	@Execute(validator=false)
	public String nxtpg(){
		//改ページ処理実行
		setFDiaryPage(1);

		//F5対策
		return "/pc/fdiary/view?redirect=true";
	}
	
	//グループ・メンバー一覧画面：前を表示リンク押下時
	@Execute(validator=false)
	public String prepg(){
		//改ページ処理実行
		setFDiaryPage(-1);		

		//F5対策
		return "/pc/fdiary/view?redirect=true";
	}
	
    //権限チェック
    public ActionMessages checkAuth(){
    	ActionMessages errors = new ActionMessages();
    	
		// ユーザのタイプ別処理
		if(userInfoDto.membertype.equals("0")){
			// 本Frontierユーザの場合は遷移不可
			//ダミーのエラーメッセージを設定
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.required","権限"));
		}
    	
		return errors;
    }
	
	//ボタン出し分け設定
    private void setVisitMemberId(){
    	userInfoDto.visitMemberId = userInfoDto.memberId;    	
    }

    //日記検索
    private void searchDiary(String type,String entdate){
		results = diaryService.getDiaryList(type,fdiaryForm.caltype,entdate,fdiaryForm.offset,fdiaryForm.groupid);
		resultscnt = diaryService.cntDiaryList(type,fdiaryForm.caltype,entdate, fdiaryForm.groupid);
		
		//装飾タグの削除
		CmnUtility.editcmnt(
				results,
				"comment",
				"pic1",
				"pic2",
				"pic3",
				"picnote1",
				"picnote2",
				"picnote3",
				appDefDto.FP_CMN_EMOJI_XML_PATH,
				appDefDto.FP_CMN_LIST_CMNTMAX,
				appDefDto.FP_CMN_EMOJI_IMG_PATH,
				appDefDto.FP_CMN_CONTENTS_ROOT
			);

		//カレンダー生成
		cmnMakeCalendar();

    }
    
	//共通カレンダー作成
	private void cmnMakeCalendar(){
		monthResults = diaryService.getDiaryList("0","1",fdiaryForm.diaryDay.substring(0, 6),null,fdiaryForm.groupid);
		//カレンダー生成
		cal = CmnUtility.makeCustomCalendar(fdiaryForm.diaryDay,monthResults);
	}

	//ページカウンター初期化
	private void initPageCnt(){
		fdiaryForm.pgcnt = 0;
		fdiaryForm.offset = 0;
	}
	
	//改ページ処理
	private void setFDiaryPage(int num){
		try {
			// ページ遷移用の計算
			fdiaryForm.pgcnt = fdiaryForm.pgcnt + num;
			fdiaryForm.offset = fdiaryForm.pgcnt * appDefDto.FP_MY_DIARYLIST_PGMAX;

		} catch (Exception e) {
			// 計算できない場合は初期値セット
			fdiaryForm.pgcnt = 1;
			fdiaryForm.offset = 0;
		}		
	}
	
	private void searchGroup(){
		// グループ一覧の取得(一時変数に代入)
		List<BeanMap> GroupListTmp = groupService.selGroup(null);
		// 初期化
		GroupList = new ArrayList<BeanMap>();
		// グループ一覧の詰め直し(メンバーが0のグループは除外)
		if(GroupListTmp.size() > 0){
			// グループ数分ループ
			for(BeanMap f:GroupListTmp){
				// メンバーが居ればリストに詰めなおし
				if((Integer) f.get("joinnumber") > 0){
					GroupList.add(f);
				}
			}
		} else {
			// グループが無ければそのまま代入
			GroupList = GroupListTmp;
		}
	}
}
