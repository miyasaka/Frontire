package frontier.action.m;

import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.entity.Members;
import frontier.form.m.SetupForm;
import frontier.service.MembersService;
import frontier.service.SetupService;

public class SetupAction {

	Logger logger = Logger.getLogger(this.getClass().getName());

	@Resource
	public AppDefDto appDefDto;
	@Resource
	public UserInfoDto userinfoDto;

	public Members members;

	@Resource
	protected MembersService membersService;

	@ActionForm
	@Resource
	protected SetupForm setupForm;

	@Resource
	protected SetupService setupService;

	//認証キー鍵
	private String key = "setup";


	/* 認証コード設定画面*/
	@Execute(validator=false)
	public String index() throws Exception {

		//メールアドレスチェック
		if(setupForm.hemail==null||setupForm.hemail==null){
			return "/m/login?redirect=true";
		}
		//email,password暗号化
		setupForm.hiddenParam=CmnUtility.angou(key,setupForm.hemail+","+setupForm.hpasswd);


		return "index.jsp";
	}

	/* 簡易ログイン画面 */
	@Execute(validator=false)
	public String login() throws Exception {
		if(setupForm.hiddenParam==null){
			return "/m/login?redirect=true";
		}else if(setupForm.hiddenParam.length()<1){
			return "/m/login?redirect=true";
		}
		//フォーム初期設定
		initForm();
		return "login.jsp";
	}

	/* 簡易ログイン処理 */
	@Execute(validate="checkLogin",validator=false,input="error")
	public String login2() throws Exception {
		return "/m/top";
	}

	/*エラー画面*/
	@Execute(validator=false)
	public String error(){
		if(setupForm.hiddenParam==null){
			return "/m/login?redirect=true";
		}
		return "login.jsp";
	}

	/* 認証コード設定*/
	@Execute(validator=true,input ="index.jsp")
	public String setcode() throws Exception {
		//フォーム初期設定
		initForm();

		setupService.setCode(setupForm.hemail,setupForm.hpasswd,setupForm.code);

		return "login?redirect=true";
	}

	/* 戻る*/
	@Execute(validator= false)
	public String back() {
		return "/m/login";
	}

	public ActionMessages checkLogin() throws Exception {

		ActionMessages errors = new ActionMessages();

		//フォーム初期設定
		try{
			initForm();
		}catch(Exception e){

		}
		try{
			members =  membersService.getResultByEmailAndPasswd(setupForm.hemail, setupForm.hpasswd);
		}catch(Exception e){

		}
		if(members != null){

			if(setupForm.code!=null){
				if(!setupForm.code.equals(members.ninsyouid)){
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.setup.code"));
				}
			}

			// ------------------------------- //
			// ログイン直後のパラメタセット    //
			// ------------------------------- //
			userinfoDto.memberId   = members.mid;
			userinfoDto.nickName   = members.nickname;
			userinfoDto.entdate    = members.entdate;
			userinfoDto.strEntdate = CmnUtility.dateFormat("yyyyMMdd",members.entdate);
			userinfoDto.membertype = members.membertype;
			userinfoDto.fdomain    = appDefDto.FP_CMN_HOST_NAME;

			return errors;
		} else {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.login","ﾒｰﾙｱﾄﾞﾚｽ","ﾊﾟｽﾜｰﾄﾞ"));
		}
		return errors;
	}

	private void initForm() throws Exception{
		String [] re = null;
		re = CmnUtility.hukugou(key,setupForm.hiddenParam).split(",");

		//email,password複合化
		if(re!=null){
			if(re.length==2){
				setupForm.hemail=re[0];
				setupForm.hpasswd=re[1];
				try {
					//ハッシュ文字列化
					setupForm.hpasswd=CmnUtility.getDigest(setupForm.hpasswd);
				} catch (Exception e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			}
		}
	}
}