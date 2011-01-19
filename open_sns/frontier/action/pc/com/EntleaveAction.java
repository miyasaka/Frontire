package frontier.action.pc.com;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import frontier.dto.AppDefDto;
import frontier.dto.CommunityDto;
import frontier.dto.UserInfoDto;
import frontier.form.pc.com.ComForm;
import frontier.service.CommunityService;
import frontier.service.EntleaveService;
import frontier.service.EntprofileService;

public class EntleaveAction {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	public AppDefDto appDefDto;
	@Resource
	public UserInfoDto userInfoDto;
	@Resource
	public CommunityDto communityDto;
	@Resource
	protected CommunityService communityService;
	@Resource
	protected EntleaveService entleaveService;
	@Resource
	protected EntprofileService entprofileService;
	@ActionForm
	@Resource
	protected ComForm comForm;
	public List<BeanMap> results;
	public List<BeanMap> resultsMakeEvent;
	public List<BeanMap> resultsEvent;
	
	public String leaveLevel = "";

	// ■初期表示
	@Execute(validator=false,urlPattern="{cid}")
	public String index(){
		String makabletopic = "";
		Integer geteventSize;
		Integer geteventmakeSize;
		// init処理
		init();
		// 1:管理人
		makabletopic = results.get(0).get("makabletopic").toString();
		
		logger.debug("******** resultsEvent.size() ************"+resultsEvent.size());
		
		// イベント作成者状況
		geteventmakeSize = resultsMakeEvent.size();
		
		// イベント参加状況
		geteventSize = resultsEvent.size();
		// leaveLevel（1:管理人、2:イベント参加済み、3:イベント未参加、4：イベント作成者）
		if(makabletopic.equals("1")){
			leaveLevel = "1";
		}else{
			if(geteventmakeSize > 0){
				leaveLevel = "4";
			} else {
				if(geteventSize > 0){
					leaveLevel = "2";
				} else {
					leaveLevel = "3";
				}
			}
		}
		
		
		return "leave.jsp";
	}

	// ■退会する押下処理
	@Execute(validator=false)
	public String leave(){
		// init処理
		//init();
		//コミュニティ情報取得
		//results = entleaveService.selCom(userInfoDto.memberId,comForm.cid);
		//コミュニティに非参加の場合は登録処理実行
		
		//makabletopic = results.get(0).get("makabletopic").toString();
		//if(makabletopic.equals("0")){
		//	// コミュニティメンバー登録処理
		//	entleaveService.insertCommunities(comForm.cid,userInfoDto.memberId,"×");
		//}
		results = communityService.selectFriend(userInfoDto.memberId,communityDto.cid);
		entleaveService.updateCommunityEnterant(comForm.cid,userInfoDto.memberId,results.get(0).get("requiredmsg").toString());
		//参加人数が1人の場合、コミュニティ削除
		if(communityService.cntMemList(comForm.cid)==0){
			entprofileService.delComAll(communityDto.cid);
			//トップページに遷移
			return "/pc/top?redirect=true";
		}
		
		
		return "../top/"+comForm.cid+"/redirect=true";
	}
	
	// ■キャンセル押下処理
	@Execute(validator=false)
	public String cancel(){
		// init処理
		init();
		
		return "../top/"+comForm.cid+"/redirect=true";
	}

	// init処理
	private void init(){
		// セッションとしてcommunityDto.cidに設定
		communityDto.cid = comForm.cid;
		// コミュニティ情報設定(掲示板作成権限、ＩＤ、名前)
		communityService.getComDt(communityDto.cid);
		//コミュニティ情報取得
		results = entleaveService.selCom(userInfoDto.memberId,comForm.cid);
		//イベント作成者情報取得
		resultsMakeEvent = entleaveService.selMakeEvent(userInfoDto.memberId,comForm.cid);
		//イベント参加情報取得
		resultsEvent = entleaveService.selEvent(userInfoDto.memberId,comForm.cid);
		
	}
}