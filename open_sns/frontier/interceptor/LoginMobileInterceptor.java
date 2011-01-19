package frontier.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;

import frontier.dto.UserInfoDto;
import frontier.service.MembersService;

public class LoginMobileInterceptor extends AbstractInterceptor{
    private static final long serialVersionUID = 1L;

    @Resource
    UserInfoDto userInfoDto;
    @Resource
    private MembersService membersService;
    @Resource
    private HttpServletRequest request;
    
	Logger logger = Logger.getLogger(this.getClass().getName());

	public Object invoke(MethodInvocation arg0) throws Throwable {
	    logger.debug("*★MoblieLogin★****");
	    logger.debug(userInfoDto==null?"NULL":userInfoDto.toString());
	    logger.debug(userInfoDto==null?"NULL":userInfoDto.memberId+"");
	    logger.debug("*★MoblieLogin★****");
	    Object rtnval;
	    
	    if((userInfoDto!=null)&&(userInfoDto.memberId!=null)&&(userInfoDto.memberId.length()!=0)) {
	    	// ログインしていればOK
	       	//ユーザエージェント設定
	    	userInfoDto.userAgent = request.getHeader("user-agent"); 
	    	
	    	//最終アクセス日時を更新する。
	    	membersService.updLastAcessDate(userInfoDto.memberId);
	    	// 実行結果を取得
	    	rtnval = arg0.proceed();
	    	
	    	// 足跡(訪問者メンバーIDを取得後に処理を行う)
	    	if((userInfoDto.visitMemberId!=null)&&(!userInfoDto.memberId.equals(userInfoDto.visitMemberId))){
	    		// 訪問メンバーIDがnullじゃなければ&メンバーID!=訪問メンバーIDならば足跡登録or更新
	    		membersService.updVisitors(userInfoDto.memberId, userInfoDto.visitMemberId);
	    	}
	    	
	    	return rtnval;
	    }
	    	    
	    return "/m/login/?redirect=true";
	}
	    
}
