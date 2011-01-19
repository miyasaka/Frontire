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
import frontier.form.pc.FriendupdateForm;
import frontier.service.CommonService;
import frontier.service.FriendupdateService;

public class FriendupdateAction {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	public AppDefDto appDefDto;
	@Resource
	public UserInfoDto userInfoDto;
	@Resource
	protected FriendupdateService friendupdateService;
	@Resource
	protected CommonService commonService;
	@ActionForm
	@Resource
	public FriendupdateForm friendupdateForm;
	
	public List<BeanMap> friendPhotoList;
	public long resultscnt;
	
	@Execute(validator=false)
	public String index(){
		
		friendupdateForm.defMemberUpdateSort = "01";

		//ページカウンター初期化
		initPageCnt();
		
		//一覧の取得
		exeSearch();

		return "list.jsp";
	}
	
	//更新日、登録日のソート変更
	@Execute(validator=false,urlPattern="sort/{defMemberUpdateSort}")
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
		return "/pc/friendupdate/view/?redirect=true";
	}
	
	//前を表示リンク押下時
	@Execute(validator=false)
	public String prepg(){
		//改ページ処理実行
		setNextPage(-1);		
		
		//F5対策
		return "/pc/friendupdate/view/?redirect=true";
	}
	
	//フォト検索の実行
	private void exeSearch(){
		// 訪問者IDにメンバーIDを設定
		userInfoDto.visitMemberId = userInfoDto.memberId;

		//グループメンバーリスト取得
		List<BeanMap> gListResults = commonService.getMidList("1",userInfoDto.memberId);
		List<String> gList = makeIdList(gListResults);
		
		// 私がフォローリストの取得
		List<BeanMap> followYouList = commonService.getMidList("2",userInfoDto.memberId);
		List<String> fList = makeIdList(followYouList);

		friendPhotoList = friendupdateService.selFriendUpdateInfo(gList,fList,friendupdateForm.defMemberUpdateSort,friendupdateForm.offset);
		//総件数取得
		resultscnt = friendupdateService.cntFriendUpdateInfo(gList, fList);
		
	}
	
	//同じグループのメンバーID、フォローしている人のメンバーID格納用
	private List<String> makeIdList(List<BeanMap> lb){
		// midリスト格納用変数
		List<String> idList = new ArrayList<String>();
		
		// 取得したグループメンバーmidリストが0件以上ならフォト情報の取得
		if(lb.size()>0){
			for(BeanMap f:lb){
				idList.add((String)f.get("mid"));
			}
		} else {
			// データが0件時の対応
			idList.add("");
		}
		
		return idList;
	}
	
    //ページカウンター初期化
    private void initPageCnt(){
    	friendupdateForm.pgcnt = 0;
    	friendupdateForm.offset = 0;
    }

	//一覧改ページ処理
	private void setNextPage(int num){
		try {
			// ページ遷移用の計算
			friendupdateForm.pgcnt = friendupdateForm.pgcnt + num;
			friendupdateForm.offset = friendupdateForm.pgcnt * appDefDto.FP_MY_NEWPHOTO_LIST_PGMAX;

		} catch (Exception e) {
			// 計算できない場合は初期値セット
			friendupdateForm.pgcnt = 1;
			friendupdateForm.offset = 0;
		}		
	}
}
