package frontier.action.pc;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.form.pc.NewfrienddiaryForm;
import frontier.service.CommonService;
import frontier.service.NewfrienddiaryService;

public class NewfrienddiaryAction {
	Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Resource
	public UserInfoDto userInfoDto;
	@Resource
	public AppDefDto appDefDto;
	@Resource
	protected NewfrienddiaryService newfrienddiaryService;
	@Resource
	protected CommonService commonService;
	@ActionForm
	@Resource
	public NewfrienddiaryForm newfrienddiaryForm;
	
	public List<BeanMap> results;
	public long resultscnt;
	
	//同志最新日記画面初期表示
	@Execute(validator=false)
	public String index(){
		newfrienddiaryForm.defMemDiarySort = "01";
		//ページカウンター初期化
		initPageCnt();

		//一覧の取得
		exeSearch();
		
		return "list.jsp";
	}
	
	//更新日、登録日のソート変更
	@Execute(validator=false,urlPattern="sort/{defMemDiarySort}")
	public String sort(){
		//ページカウンター初期化
		initPageCnt();
		
		//一覧の取得
		exeSearch();
		
		return "list.jsp";		
	}
	
	//改ページ後の再表示用
	@Execute(validator=false)
	public String view(){
		//一覧の取得
		exeSearch();

		return "list.jsp";
	}
	
	//次を表示リンク押下時
	@Execute(validator=false)
	public String nxtpg(){
		//改ページ処理実行
		setNextPage(1);
		
		//F5対策
		return "/pc/newfrienddiary/view/?redirect=true";
	}
	
	//前を表示リンク押下時
	@Execute(validator=false)
	public String prepg(){
		//改ページ処理実行
		setNextPage(-1);		
		
		//F5対策
		return "/pc/newfrienddiary/view/?redirect=true";
	}
	
	//日記検索の実行
	private void exeSearch(){
		// 訪問者IDにメンバーIDを設定
		userInfoDto.visitMemberId = userInfoDto.memberId;

		// 新着日記データ取得
		List<String> gList = groupids();
		
		results = newfrienddiaryService.selDiaryNewList(userInfoDto.memberId,newfrienddiaryForm.defMemDiarySort,gList,newfrienddiaryForm.offset);
		resultscnt = newfrienddiaryService.cntDiaryNewList(userInfoDto.memberId, gList);
	}
	
	//グループに所属しているメンバーのIDをリスト化
	private List<String> groupids(){
		List<BeanMap> GroupList = new ArrayList<BeanMap>();
		//グループ一覧データ取得
		GroupList = commonService.getMidList("1",userInfoDto.memberId);
		//同志リスト格納用変数
		List<String> glist = new ArrayList<String>();
		//同志0対策
		glist.add(userInfoDto.memberId);
		for(BeanMap f:GroupList){
			glist.add((String)f.get("mid"));
		}
		return glist;
	}
	
    //ページカウンター初期化
    private void initPageCnt(){
    	newfrienddiaryForm.pgcnt = 0;
    	newfrienddiaryForm.offset = 0;
    }

	//一覧改ページ処理
	private void setNextPage(int num){
		try {
			// ページ遷移用の計算
			newfrienddiaryForm.pgcnt = newfrienddiaryForm.pgcnt + num;
			newfrienddiaryForm.offset = newfrienddiaryForm.pgcnt * appDefDto.FP_MY_NEWDIARY_LIST_PGMAX;

		} catch (Exception e) {
			// 計算できない場合は初期値セット
			newfrienddiaryForm.pgcnt = 1;
			newfrienddiaryForm.offset = 0;
		}		
	}
}
