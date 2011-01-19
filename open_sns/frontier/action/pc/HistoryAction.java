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
import frontier.entity.FrontierUserManagement;
import frontier.form.pc.HistoryForm;
import frontier.service.CommonService;
import frontier.service.FriendinfoCmnService;
import frontier.service.HistoryService;

public class HistoryAction {
	Logger logger = Logger.getLogger(this.getClass().getName());

	@Resource
	public UserInfoDto userInfoDto;
	@Resource
	protected HistoryService historyService;
	@Resource
	protected FriendinfoCmnService friendinfoCmnService;
	@Resource
	protected CommonService commonService;

	@ActionForm
	@Resource
	protected HistoryForm historyForm;
	@Resource
	public AppDefDto appDefDto;
	
	public FrontierUserManagement frontierUserManagement;
	
	//画面表示用変数
	public List<BeanMap> results;
	public long totalCnt;
	
	//足跡画面初期表示
	@Execute(validator=false)
	public String index(){
		//初期化
		initHistory();
		
		//足跡検索
		selVisitors();
		
		return "/pc/history/index.jsp";
	}

	//足跡画面再表示
	@Execute(validator=false)
	public String reIndex(){
		//足跡検索
		selVisitors();
		
		return "/pc/history/index.jsp";
	}
	
	//次を表示リンク押下時
	@Execute(validator=false)
	public String nxtpg(){
		//改ページ処理実行
		setHistoryPage(1);
		
		//F5対策
		return "/pc/history/reIndex?redirect=true";
	}
	
	//前を表示リンク押下時
	@Execute(validator=false)
	public String prepg(){
		//改ページ処理実行
		setHistoryPage(-1);		
		
		//F5対策
		return "/pc/history/reIndex?redirect=true";
	}
	
	//足跡検索
	private void selVisitors(){
		results = historyService.selVisitorsList(userInfoDto.memberId, historyForm.offset);
		totalCnt = historyService.cntVisitorsList(userInfoDto.memberId);
		
		//Frontierユーザ管理情報を取得する
		frontierUserManagement = commonService.getFrontierUserManagement(userInfoDto.memberId);
		logger.debug("********** ★★★ *******************"+frontierUserManagement);

		
		setFriends();
	}
	
	//足跡でのグループ、フォロー設定
	private void setFriends(){
		
		//ここでグループかを検索
		List<BeanMap> lbm = commonService.getMidList("1",userInfoDto.memberId);
		//ここでフォローしているかを検索
		List<BeanMap> lbm2 = commonService.getMidList("2",userInfoDto.memberId);
		
		List<String> myFriendList = new ArrayList<String>();
		List<String> friendFriendList = new ArrayList<String>();
		String mid = "";
	
		for (int i=0;i<lbm.size();i++){
			//友達一覧を作成する。
			myFriendList.add(i, (String)lbm.get(i).get("mid"));
		}
		
		for (int i=0;i<lbm2.size();i++){
			//友達一覧を作成する。
			friendFriendList.add(i, (String)lbm2.get(i).get("mid"));
		}

		//足跡検索結果にグループFLGを設定
		for(int i=0;i<results.size();i++){
			mid = (String)results.get(i).get("mid");
			//友達flgを初期設定
			results.get(i).put("friendFlg", "0");
			
			for(int j=0;j<myFriendList.size();j++){
				//グループの場合はflgを立てる
				if(mid.equals((String)myFriendList.get(j))){
					results.get(i).put("friendFlg", "1");
					break;
				}
			}
		}

		//足跡検索結果にフォローFLGを設定
		for(int i=0;i<results.size();i++){
			mid = (String)results.get(i).get("mid");
			//友達の友達flgを初期設定
			results.get(i).put("friendFriendFlg", "0");
			
			for(int j=0;j<friendFriendList.size();j++){
				//フォロー中の場合はflgを立てる
				if(mid.equals((String)friendFriendList.get(j))){
					results.get(i).put("friendFriendFlg", "1");
					break;
				}
			}
		}
		
	}
	
	//足跡画面改ページ処理
	private void setHistoryPage(int num){
		// 訪問者IDにメンバーIDを設定
		userInfoDto.visitMemberId = userInfoDto.memberId;

		try {
			// ページ遷移用の計算
			historyForm.pgcnt = historyForm.pgcnt + num;
			historyForm.offset = historyForm.pgcnt * appDefDto.FP_MY_HISTORYLIST_PGMAX;

		} catch (Exception e) {
			// 計算できない場合は初期値セット
			historyForm.pgcnt = 1;
			historyForm.offset = 0;
		}		
	}
	
	//足跡画面初期化処理
	private void initHistory(){
		// 訪問者IDにメンバーIDを設定
		userInfoDto.visitMemberId = userInfoDto.memberId;

		historyForm.offset = 0;
		historyForm.pgcnt = 0;			
	}
}
