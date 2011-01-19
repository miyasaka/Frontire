package frontier.action.pc.com;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.h2.util.StringUtils;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import frontier.dto.AppDefDto;
import frontier.dto.CommunityDto;
import frontier.dto.UserInfoDto;
import frontier.form.pc.com.ComForm;
import frontier.service.CommunityService;

public class TopAction {
    @Resource
    public UserInfoDto userInfoDto;
	@Resource
	public AppDefDto appDefDto;
	@Resource
	public CommunityDto communityDto;
	@Resource
	protected CommunityService communityService;
	@ActionForm
	@Resource
	protected ComForm comForm;
	public List<BeanMap> resultst;
	public List<BeanMap> resultse;
	public List<BeanMap> resultsm;
	public Map<String, Object> resultscdd;
	@Execute(validator=false,urlPattern="{cid}")
	
	// ■初期表示
	public String index(){
		init();
		return "home.jsp";
	}
	
	// init処理
	private void init(){
		// セッションとしてcommunityDto.cidに設定
		communityDto.cid = comForm.cid;
		// TOPに表示用情報の取得
		// ■コミュニティ詳細データの取得(mapオブジェクト)
		resultscdd = communityService.getComDtDetails(communityDto.cid);
		// ■件数取得
		comForm.resultscntm = communityService.cntMemList(communityDto.cid);     // 参加メンバー
		comForm.resultscntt = communityService.cntBbsList(communityDto.cid,"1"); // トピック
		comForm.resultscnte = communityService.cntBbsList(communityDto.cid,"2"); // イベント
		// ■一覧データ取得
		resultsm = communityService.selectMemList(communityDto.cid, 0, appDefDto.FP_COM_TOPLIST_PGMAX);   // 参加メンバー
		resultst = communityService.selectBbsList(communityDto.cid,"1",0,appDefDto.FP_COM_BBSLIST_PGMAX); // トピック
		resultse = communityService.selectBbsList(communityDto.cid,"2",0,appDefDto.FP_COM_BBSLIST_PGMAX); // イベント
		// コミュニティ情報設定(掲示板作成権限、ＩＤ、名前)
		communityService.getComDt(communityDto.cid);
		// visitMemberIdをクリア
		userInfoDto.visitMemberId = userInfoDto.memberId;
	}
}
