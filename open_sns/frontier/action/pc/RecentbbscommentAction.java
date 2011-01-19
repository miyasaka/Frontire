package frontier.action.pc;

import java.util.List;

import javax.annotation.Resource;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.form.pc.RecentbbscommentForm;
import frontier.service.RecentbbscommentService;
import frontier.service.TopService;

public class RecentbbscommentAction {
	@Resource
	public UserInfoDto userInfoDto;
	@Resource
	public AppDefDto appDefDto;
	@Resource
	protected RecentbbscommentService recentbbscommentService;
	@ActionForm
	@Resource
	public RecentbbscommentForm recentbbscommentForm;
	
	public List<BeanMap> results;
	public long resultscnt;
	
	
	@Execute(validator=false)
	public String index(){
		recentbbscommentForm.defCommunitySort = "01";
		//ページカウンター初期化
		initPageCnt();

		//一覧の取得
		exeSearch();		
		
		return "list.jsp";
	}
	
	//更新日、登録日のソート変更
	@Execute(validator=false,urlPattern="sort/{defCommunitySort}")
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
		return "/pc/recentbbscomment/view/?redirect=true";
	}
	
	//前を表示リンク押下時
	@Execute(validator=false)
	public String prepg(){
		//改ページ処理実行
		setNextPage(-1);		
		
		//F5対策
		return "/pc/recentbbscomment/view/?redirect=true";
	}
	
	//コミュニティ検索の実行
	private void exeSearch(){
		// 訪問者IDにメンバーIDを設定
		userInfoDto.visitMemberId = userInfoDto.memberId;
		
		results = recentbbscommentService.selTopicNewList(userInfoDto.memberId,recentbbscommentForm.defCommunitySort,recentbbscommentForm.offset);
		resultscnt = recentbbscommentService.cntTopicNewList(userInfoDto.memberId);
	}
	
    //ページカウンター初期化
    private void initPageCnt(){
    	recentbbscommentForm.pgcnt = 0;
    	recentbbscommentForm.offset = 0;
    }

	//一覧改ページ処理
	private void setNextPage(int num){
		try {
			// ページ遷移用の計算
			recentbbscommentForm.pgcnt = recentbbscommentForm.pgcnt + num;
			recentbbscommentForm.offset = recentbbscommentForm.pgcnt * appDefDto.FP_MY_NEWCOMMUNITY_COMMENT_LIST_PGMAX;

		} catch (Exception e) {
			// 計算できない場合は初期値セット
			recentbbscommentForm.pgcnt = 1;
			recentbbscommentForm.offset = 0;
		}		
	}
}
