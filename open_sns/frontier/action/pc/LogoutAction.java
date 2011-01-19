package frontier.action.pc;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.seasar.struts.annotation.Execute;

public class LogoutAction {

	/* セッションオブジェクト破棄 */
	@Resource
	protected HttpSession session;
	
	@Execute(validator = false)
	public String index() {
		/* セッションを破棄し、トップページへ遷移 */
		session.invalidate();
		return "/pc/login/?redirect=true";
	}
}
