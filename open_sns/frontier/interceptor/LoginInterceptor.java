package frontier.interceptor;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;


import frontier.common.CmnUtility;
import frontier.dto.UrlDto;
import frontier.dto.UserInfoDto;
import frontier.entity.Members;
import frontier.service.DiaryService;
import frontier.service.MembersService;

public class LoginInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = 1L;

    @Resource
    UserInfoDto userInfoDto;
    @Resource
    private MembersService membersService;
    @Resource
    private HttpServletRequest request;
    @Resource
    private DiaryService diaryService;
    @Resource
    UrlDto urlDto;
    private Members members;
    Logger logger = Logger.getLogger(this.getClass().getName());
    
	public Object invoke(MethodInvocation arg0) throws Throwable {
	    logger.debug("*★1★****");
	    logger.debug(userInfoDto==null?"NULL":userInfoDto.toString());
	    logger.debug(userInfoDto==null?"NULL":userInfoDto.memberId+"");
	    logger.debug("*★1★****");
	    
	    //Frontier Netユーザで画面遷移を許可する配列
	    final String[] uriList = {"/frontier/pc/diary.do",
	    						  "/frontier/pc/ftop.do",
	    						  "/frontier/pc/fmem.do",
	    						  "/frontier/pc/fdiary.do",
	    						  "/frontier/pc/mem.do",
	    						  "/frontier/pc/mlist.do",
	    						  "/frontier/pc/profile2.do",
	    						  "/frontier/pc/fshout.do"};
	    
	    Object rtnval;
	    if((userInfoDto!=null)&&(userInfoDto.memberId!=null)&&(userInfoDto.memberId.length()!=0)) {
	    	// ログインしていればOK
	    	
	    	//ゲストでログインして日記以外の画面に行こうとした場合はNG
	    	if (userInfoDto.membertype.equals("2") && !request.getRequestURI().equals("/frontier/pc/diary.do")){
	    		return "/pc/login/?redirect=true";
	    	}
	    	
	    	//Frontier Netユーザでログインした場合
	    	if(userInfoDto.membertype.equals("1")){
	    		String nowUri = request.getRequestURI();
	    		boolean moveFlg = false;
	    		
	    		//遷移可能な画面かを調べる
	    		for(int i=0;i<uriList.length;i++){
	    			if(nowUri.equals(uriList[i])){
	    				//遷移可能な場合はフラグを立てる
	    				moveFlg = true;
	    				break;
	    			}
	    		}
	    		
	    		//遷移付加の場合はTOPページへ遷移する
	    		if(!moveFlg){
	    			return "/pc/ftop/?redirect=true";
	    		}
	    	}
	    	
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
	    }else{
		    logger.debug("*★2★****");	
	    	//リクエストパラメータ取得
	    	//日記の場合はmidを必ず持っている。
		    String mid = request.getParameter("mid");
		    //URIを取得。画面判断に利用する。
		    String uri = request.getRequestURI();

		    //日記の場合はOKとする。
		    if((mid != null) && (mid.length() != 0) && (uri.equals("/frontier/pc/diary.do"))){
		    	//ゲストユーザ検索
		    	members = membersService.getResultByMembertype("2");
		    	
		    	if(members != null){
		    		
		    		long diaryNum = 0;

		    		//外部公開日記を検索する
		    		if(request.getParameter("diaryId") == null){
		    			//日記一覧に遷移する場合
		    			diaryNum = diaryService.selOutPubDiary(mid);
		    		}else{
		    			//日記閲覧に遷移する場合
		    			diaryNum = diaryService.selOutPubDiary(mid, request.getParameter("diaryId"));
		    		}
		    		
		    		if(diaryNum > 0){
		    			//ゲストユーザとしてログイン
			    		//ゲストユーザのセッションの設定
			    		userInfoDto.memberId = members.mid;
			    		userInfoDto.nickName = members.nickname;
			        	userInfoDto.entdate=members.entdate;
			        	userInfoDto.strEntdate=CmnUtility.dateFormat("yyyyMMdd",members.entdate);
			        	userInfoDto.membertype=members.membertype;
		    		}else{
		    			//ゲストではない。遷移先のURLを引き継いでログイン画面へ
			    	    setMoveUrl();

		    			return "/pc/login/?redirect=true";
		    		}
		    		
		    	} else {
		    	    //遷移先のURLを引き継ぐ
		    	    setMoveUrl();
		    		
		    		//ゲストユーザがいない場合は、ログイン画面へ。
		    		return "/pc/login/?redirect=true";
		    	}
		    	// 実行結果を取得
			   	rtnval = arg0.proceed();
			   	return rtnval;
		    }

	    }
	    
	    //遷移先のURLを引き継ぐ
	    setMoveUrl();
	    
	    return "/pc/login/?redirect=true";
	}
	
	//ログイン後の遷移先の設定
	private void setMoveUrl(){
	    //遷移先のURLを取得
	    StringBuffer url = request.getRequestURL();
	    //遷移先のURLに付加されるパラメータを取得
	    url.append("?").append(request.getQueryString());
	    //遷移先をセッションに格納（初期化して格納）
	    urlDto.moveUrl = null;
	    urlDto.moveUrl = url.toString();		
	}
}
