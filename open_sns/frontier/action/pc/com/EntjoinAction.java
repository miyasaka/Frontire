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
import frontier.service.EntjoinService;
import frontier.service.EntrequestService;

public class EntjoinAction {
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
	protected EntjoinService entjoinService;
	@ActionForm
	@Resource
	protected ComForm comForm;
	public List<BeanMap> results;
	public List<BeanMap> resultsEnt;

	// ■初期表示
	@Execute(validator=false,urlPattern="{cid}")
	public String index(){
		// init処理
		init();
		// 承認の有無チェック
		// 参加条件が0:なしの場合、確認画面へ
		if(communityDto.joincond.equals("0")){return "join.jsp";}
		// 参加条件が1:許可が必要の場合、参加リクエスト画面へ
		else{return "request.jsp";}
		// 本当はリクエスト画面へ行くが、今はコメントアウト
		//else{return "join.jsp";}
	}

	// ■参加する押下処理
	@Execute(validator=false)
	public String join(){
		// init処理
		init();
		//コミュニティ情報取得
		results = entjoinService.selCom(userInfoDto.memberId,comForm.cid);
		//コミュニティ参加者情報取得
		resultsEnt = entjoinService.selComEnt(userInfoDto.memberId,comForm.cid);		
		//コミュニティに非参加の場合は登録処理実行
		String makabletopic = "";
		Integer getComEntSize;
		makabletopic = results.get(0).get("makabletopic").toString();
		getComEntSize = resultsEnt.size();
		if(results.get(0).get("joincond").toString().equals("0")){
			if(makabletopic.equals("0")){
				//logger.debug("******** コミュニティ・非会員ですよーー *************");
				if(getComEntSize == 0){
					//logger.debug("******** コミュニティ・新規で登録ですよーー *************");
					// コミュニティメンバー登録処理
					entjoinService.insertCommunities(comForm.cid,userInfoDto.memberId,"×","1");
				} else {
					//logger.debug("******** コミュニティ・再度登録ですよーー *************");
					// コミュニティメンバー更新処理
					entjoinService.updateCommunityEnterant(comForm.cid,userInfoDto.memberId,"1","×");
				}
			}
		}else{
			return "request.jsp";
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
	
	
	@Execute(validator=true,input="error")
	public String send(){
		//ブラウザバック対応
		//logger.debug("■■■■■■■■■■■■■■■■■■■■■"+comForm.reqComId);
		//logger.debug("■■■■■■■■■■■■■■■■■■■■■"+comForm.comment);
		//logger.debug("■■■■■■■■■■■■■■■■■■■■■"+communityDto.cid);
		//logger.debug("■■■■■■■■■■ communityDto.joincond ■■■■■■■■■■■"+communityDto.joincond);
		if(communityDto.cid.equals(comForm.reqComId)){
			//コミュニティ情報取得
			results = entjoinService.selCom(userInfoDto.memberId,comForm.cid);
			//コミュニティ参加者情報取得
			resultsEnt = entjoinService.selComEnt(userInfoDto.memberId,comForm.cid);		
			//コミュニティに非参加の場合は登録処理実行
			String makabletopic = "";
			Integer getComEntSize;
			makabletopic = results.get(0).get("makabletopic").toString();
			getComEntSize = resultsEnt.size();
			if(results.get(0).get("joincond").toString().equals("1")){
				if(makabletopic.equals("0")){
					//logger.debug("******** コミュニティ・非会員ですよーー *************");
					if(getComEntSize == 0){
						//logger.debug("******** コミュニティ・新規で登録ですよーー *************");
						// コミュニティメンバー登録処理
						entjoinService.insertCommunities(comForm.cid,userInfoDto.memberId,comForm.comment,"0");
					} else {
						//logger.debug("******** コミュニティ・再度登録ですよーー *************");
						//logger.debug("********* joinstatus ***********"+resultsEnt.get(0).get("joinstatus").toString());
						if(resultsEnt.get(0).get("joinstatus").toString().equals("2")||resultsEnt.get(0).get("joinstatus").toString().equals("3")){
							//logger.debug("******** このステータスならもっかい登録できます。 *************");
							//コミュニティ参加者情報更新処理
							entjoinService.updateCommunityEnterant(comForm.cid,userInfoDto.memberId,"0",comForm.comment);
						}
					}
				}
			}else{
				return "join.jsp";
			}
		}

		reset();
		//logger.debug("******* 参加希望を押したよー ***********");
		return "../top/"+comForm.cid+"/redirect=true";
	}
	
	
	
	

	// init処理
	private void init(){
		
		logger.debug("■■■■■■■■■ comForm.reqId ■■■■■■■■■■■■"+comForm.reqComId);
		
		// セッションとしてcommunityDto.cidに設定
		communityDto.cid = comForm.cid;
		// コミュニティ情報設定(掲示板作成権限、ＩＤ、名前)
		communityService.getComDt(communityDto.cid);
		// リセット
		reset();
	}
	
	// リセット処理
	private void reset(){
		comForm.comment = null;
	}
	
	//登録時エラー
	@Execute(validator=false)
	public String error(){

		try {
			//初期化
			init();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return "request.jsp";
	}
}