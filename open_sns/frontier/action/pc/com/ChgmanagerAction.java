package frontier.action.pc.com;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import frontier.dto.AppDefDto;
import frontier.dto.CommunityDto;
import frontier.dto.UserInfoDto;
import frontier.form.pc.com.ComForm;
import frontier.service.CommunityService;

public class ChgmanagerAction {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	public CommunityDto communityDto;
	@Resource
	protected CommunityService communityService;
	@Resource
	public AppDefDto appDefDto;
	@Resource
	public UserInfoDto userInfoDto;
	//コミュニティ情報(内部制御用)
	private Map<String,Object> comresults;
	@ActionForm
	@Resource
	protected ComForm comForm;
	
	@Execute(validator=false,urlPattern="{cid}")
	// ■初期表示
	public String index(){
		communityDto.cid=comForm.cid;
		// ■コミュニティ詳細データの取得(mapオブジェクト)
		comresults = communityService.getComDtDetails(communityDto.cid);
		// コミュニティ情報設定(掲示板作成権限、ＩＤ、名前)
		communityService.getComDt(communityDto.cid);
		//非会員時、管理者有りコミュニティ時アクセス不可
		if(communityDto.makabletopic.equals("0")||!comresults.get("status").equals("2")) return "/pc/error.jsp";
		//管理人が居る場合アクセス負荷、
		return "index.jsp";
	}
	@Execute(validator=false)
	// 管理人変更
	public String updateManager(){
		communityDto.cid=comForm.cid;
		// ■コミュニティ詳細データの取得(mapオブジェクト)
		comresults = communityService.getComDtDetails(communityDto.cid);
		// コミュニティ情報設定(掲示板作成権限、ＩＤ、名前)
		communityService.getComDt(communityDto.cid);
		//非会員時,管理者有りコミュニティ時アクセス不可
		if(communityDto.makabletopic.equals("0")||!comresults.get("status").equals("2")) return "/pc/error.jsp";
		// 管理人情報更新
		communityService.updateManager(userInfoDto.memberId,communityDto.cid);
		return "/pc/com/top/"+comForm.cid;
	}
	@Execute(validator=false)
	// やめる
	public String cansel(){
		return "/pc/com/top/"+comForm.cid;
	}
}
