package frontier.action.pc;

import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.annotation.Required;
import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UrlDto;
import frontier.dto.UserInfoDto;
import frontier.entity.Members;
import frontier.service.MembersService;

public class LoginAction {

	Logger logger = Logger.getLogger(this.getClass().getName());

	@Resource
	public AppDefDto appDefDto;
	@Resource
	public UserInfoDto userinfoDto;
	@Resource
	protected UrlDto urlDto;

	public Members members;

	@Resource
	protected MembersService membersService;

	/* 画面入力項目　*/
	@Required
	public String email;

	@Required
	public String passwd;

	/* 初期表示 */
	@Execute(validator = false)
	public String index() {
		return "index.jsp";
	}

	/* ログイン処理 */
	@Execute(validate="checkLogin",input = "index.jsp")
	public String login() {
		if(urlDto.moveUrl==null){
			return "/pc/top";
		}else{
			return urlDto.moveUrl+"?redirect=true";
		}

	}

	public ActionMessages checkLogin() {
		ActionMessages errors = new ActionMessages();

		String p = null;
		try{
			p = CmnUtility.getDigest(passwd);
		}catch(Exception e){}

		members =  membersService.getResultByEmailAndPasswd(email, p);
		if(members != null){

			// ------------------------------- //
			// ログイン直後のパラメタセット    //
			// ------------------------------- //
			userinfoDto.memberId   = members.mid;
			userinfoDto.nickName   = members.nickname;
			userinfoDto.entdate    = members.entdate;
			userinfoDto.strEntdate = CmnUtility.dateFormat("yyyyMMdd",members.entdate);
			userinfoDto.membertype = members.membertype;
			userinfoDto.mlevel     = members.managementLevel;
			userinfoDto.fdomain    = appDefDto.FP_CMN_HOST_NAME;

			logger.debug("＃＃＃＃＃＃＃＃＃＃");
			logger.debug(userinfoDto.nickName);
			logger.debug("＃＃＃＃＃＃＃＃＃＃");

			return errors;
		} else {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.login","ﾒｰﾙｱﾄﾞﾚｽ","ﾊﾟｽﾜｰﾄﾞ"));
		}
		return errors;
	}

	/* 新規登録画面に遷移*/
	@Execute(validator = false)
	public String make() {
		return "/pc/entry/";
	}

}