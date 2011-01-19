package frontier.action.pc;

import java.sql.Timestamp;
import java.util.List;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import frontier.dto.AppDefDto;
import frontier.common.CmnUtility;
import frontier.dto.UserInfoDto;
import frontier.entity.Entry;
import frontier.form.pc.EntryForm;
import frontier.service.EntryService;
public class EntryAction {
	Logger logger = Logger.getLogger(this.getClass().getName());

	@Resource
	public AppDefDto appDefDto;

	@ActionForm
	@Resource
	protected EntryForm entryForm;
	public Entry entry;
	@Resource
	protected EntryService entryService;
	public ActionMessages errors = new ActionMessages();
	public String encpass;
	public List<BeanMap> results;
	public List<BeanMap> gresults;
	@Resource
	public UserInfoDto userInfoDto;
	public String email;
	public String getFrontierDomain;
	public String getGid;

	// ■初期表示
	@Execute(validator=false)
	public String index(){
		//パラメータ初期化
		reset();

		// init処理
		init();
		return "index.jsp";
	}

	// init処理
	private void init(){

	}

	// 登録するボタン押下
	//@Execute(validator=true,input="index.jsp")
	@Execute(validate="checkemail",input="index.jsp")
	public String touroku() {
		return entry();
	}

	public ActionMessages checkemail(){
		exeEmail();

		if(entryForm.resultscnt > 0){
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.entry.email"));
		}

		//グループ名存在チェック
		exeGroup();

		return errors;
	}


	//新規登録処理
	public String entry(){
		exeMid();

		//DB登録
		entryService.insMember(
			 entryForm.newmid
			,entryForm.nickname
			,entryForm.email
			,entryForm.passwd
			,getFrontierDomain
			,getGid
			,entryForm.familyname
			,entryForm.name
		);

		return goTop();
	}

	//登録処理後、マイトップへ遷移
	public String goTop(){
		String Newmid = "m"+entryForm.newmid;
		results = entryService.selectFriend(Newmid);
		
		// ------------------------------- //
		// 新規登録直後のパラメタセット    //
		// ------------------------------- //
		userInfoDto.memberId   = results.get(0).get("mid").toString();
		userInfoDto.nickName   = results.get(0).get("nickname").toString();
		userInfoDto.entdate    = (Timestamp) results.get(0).get("entdate");
		userInfoDto.strEntdate = CmnUtility.dateFormat("yyyyMMdd",(Timestamp) results.get(0).get("entdate"));
		userInfoDto.membertype = results.get(0).get("membertype").toString();
		userInfoDto.fdomain    = appDefDto.FP_CMN_HOST_NAME;

		return "/pc/top?redirect=true";
	}

	//メールアドレス使用済みチェック
	public void exeEmail(){
		entryForm.resultscnt = entryService.cntEmail(entryForm.email);
	}

	//メンバーＩＤ発行
	public void exeMid(){
		results = entryService.newMid();
		entryForm.newmid = results.get(0).get("mid").toString();
	}

	//パラメータ初期化
	private void reset(){
		entryForm.nickname   = "";
		entryForm.email      = "";
		entryForm.passwd     = "";
		entryForm.repasswd   = "";
		entryForm.familyname = "";
		entryForm.name       = "";
		entryForm.group      = "";
	}

	//グループ名存在チェック
	public void exeGroup(){
		getFrontierDomain = "";
		getGid = "";

		gresults = entryService.selGroupList(entryForm.group);
		if(gresults.size()==0){
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
		"errors.entry.group"));
		}else{
			getFrontierDomain = gresults.get(0).get("frontierdomain").toString();
			getGid = gresults.get(0).get("gid").toString();
		}
	}

}