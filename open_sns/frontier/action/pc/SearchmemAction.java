package frontier.action.pc;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.Execute;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.service.CommonService;
import frontier.service.FriendListService;
import frontier.service.MembersService;

public class SearchmemAction {
	Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Resource
	protected MembersService membersService;
	
	@Resource
	protected FriendListService friendListService;
	
	@Resource
	protected CommonService commonService;
	
	public UserInfoDto userInfoDto;
	public AppDefDto appDefDto;
	
	public List<BeanMap> results;
	public boolean searchExeFlg;
	public String nicknameCondition;
	public String selfIntroductionCondition;
	
	List<BeanMap> listmid;
	
	@Execute(validator=false)
	public String index(){
		// 画面表示に必要な処理
		searchNecessaryProces(false);
		
		return "result.jsp";

	}
	
	@Execute(validator=false)
	public String search(){
		// 画面表示に必要な処理
		searchNecessaryProces(true);
		
		//メンバー一覧データ取得
		results = membersService.getMemberList(userInfoDto.memberId,nicknameCondition,selfIntroductionCondition);
		resetResults(results,"aboutme");
		
		setGroup();
		
		return "result.jsp";
	}
	
	//本文装飾
	private void resetResults(List<BeanMap> lbm,String property){
		// 削除対象のタグリスト(改行含)
		String[] deltags = {
				"\\n"
			};		
		
		//装飾タグの削除
		CmnUtility.editcmnt(lbm, property, null, null,null,null,null,null,appDefDto.FP_CMN_EMOJI_XML_PATH,appDefDto.FP_CMN_LIST_CMNTMAX,appDefDto.FP_CMN_EMOJI_IMG_PATH,null,deltags);
		
	}
	
	//メンバー検索でのグループ設定
	private void setGroup(){
			
		//ここでグループかを検索
		List<BeanMap> lbm = commonService.getMidList("1",userInfoDto.memberId);
		
		List<String> myGroupList = new ArrayList<String>();
		String mid = "";
		
		for (int i=0;i<lbm.size();i++){
			//グループ一覧を作成する。
			myGroupList.add(i, (String)lbm.get(i).get("mid"));
		}
		
		//メンバー検索結果にグループFLGを設定
		for(int i=0;i<results.size();i++){
			mid = (String)results.get(i).get("mid");
			//グループflgを初期設定
			results.get(i).put("groupFlg", "0");
			
			for(int j=0;j<myGroupList.size();j++){
				//グループの場合はflgを立てる
				if(mid.equals((String)myGroupList.get(j))){
					results.get(i).put("groupFlg", "1");
					break;
				}
			}
		}
		
	}
	
	//画面表示に必要な処理
	private void searchNecessaryProces(boolean flg){
		// マイページ内の遷移なので訪問メンバーIDにメンバーIDを設定
		userInfoDto.visitMemberId = userInfoDto.memberId;

		//検索実行の判断
		searchExeFlg = flg;
	}
	
}
