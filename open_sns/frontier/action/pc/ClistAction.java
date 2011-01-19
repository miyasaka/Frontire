package frontier.action.pc;

import java.util.List;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.form.pc.ClistForm;
import frontier.service.ClistService;

public class ClistAction {
	Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	public AppDefDto appDefDto;
	public List<BeanMap> results;
	@Resource
	public UserInfoDto userInfoDto;
	@ActionForm
	@Resource
	protected ClistForm clistForm;
	@Resource
	protected ClistService clistService;
	public String myflg;

	// ■初期表示
	@Execute(validator=false,urlPattern="{vmid}")
	public String index(){
		// 初期値を設定
		clistForm.offset = 0;
		clistForm.pgcnt = 0;
		// init処理
		init();
		// 一覧表示処理へ
		return "list.jsp";
	}
	
	// ■ページング処理
	@Execute(validator=false,urlPattern="movelist/{vmid}/{pgcnt}")
	public String movelist(){
		// init処理
		init();
		// 一覧表示処理へ
		return "list.jsp";
	}
	
	// init処理
	private void init(){
		userInfoDto.visitMemberId = clistForm.vmid;
		try {
			// offset設定
			clistForm.offset = clistForm.pgcnt*appDefDto.FP_CMN_LIST_COMMAX;
		} catch (Exception e) {
			// エラーになった場合は初期値を設定
			clistForm.offset = 0;
			clistForm.pgcnt = 0;
			// TODO: handle exception
		}
		// 参加コミュニティ件数取得
		clistForm.resultscnt = clistService.cntClist(userInfoDto.visitMemberId,"0");
		// 参加コミュニティ一覧データ取得
		results = clistService.selectClist(userInfoDto.visitMemberId,"0",clistForm.offset,appDefDto.FP_CMN_LIST_COMMAX);
		
		//自分かそれ以外か判断
		if(userInfoDto.memberId.equals(clistForm.vmid)){//マイ
			myflg = "1";

		}else{//グループ
			myflg = "";
		}
		
		
	}
}