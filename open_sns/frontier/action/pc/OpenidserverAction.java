package frontier.action.pc;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.DirectError;
import org.openid4java.message.Message;
import org.openid4java.message.MessageExtension;
import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.AxMessage;
import org.openid4java.message.ax.FetchRequest;
import org.openid4java.message.ax.FetchResponse;
import org.openid4java.message.sreg.SRegMessage;
import org.openid4java.message.sreg.SRegRequest;
import org.openid4java.message.sreg.SRegResponse;
import org.openid4java.server.InMemoryServerAssociationStore;
import org.openid4java.server.ServerManager;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.annotation.Required;

import frontier.common.CmnUtility;
import frontier.dto.AppDefDto;
import frontier.dto.UserInfoDto;
import frontier.entity.Members;
import frontier.service.MembersService;
import frontier.service.TopService;

public class OpenidserverAction {

    Logger logger = Logger.getLogger(this.getClass().getName());

	@Resource
	public AppDefDto appDefDto;
    @Resource
    HttpServletRequest  request;
    @Resource
    HttpServletResponse response;
    @Resource
    HttpSession session;
    @Resource
    ServletContext application;
    



    public String responseText;
    
    @Resource
    public UserInfoDto userinfoDto;     

    public Members members;

    @Resource
    protected MembersService membersService;
    
    @Resource
    protected TopService topService;
  
    /* 画面入力項目　*/
    @Required
	public String email;

    @Required
    public String passwd;
   
    
	///////////////////////
	// ENDPointのURL
	///////////////////////
    private String OPEndpointUrl;
    private String OPLocalIDURL;

    
    /* ServerManagerの取得 */
    private ServerManager getServerManager(){
    	
    	//OPEndpointUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/frontier/pc/Openidserver/auth"; 
    	//OPLocalIDURL  = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/frontier/pc/Openidserver/check";
    	OPEndpointUrl = request.getScheme() + "://" + appDefDto.FP_CMN_HOST_NAME + ":" + request.getServerPort() + "/frontier/pc/Openidserver/auth"; 
    	OPLocalIDURL  = request.getScheme() + "://" + appDefDto.FP_CMN_HOST_NAME + ":" + request.getServerPort() + "/frontier/pc/Openidserver/check";
    	
    	ServerManager manager = (ServerManager)application.getAttribute("servermanager");
    	//
    	if(manager==null){
    		try{
		        manager = new ServerManager();
		        manager.setSharedAssociations (new InMemoryServerAssociationStore());
		        manager.setPrivateAssociations(new InMemoryServerAssociationStore());
		        manager.setOPEndpointUrl(OPEndpointUrl);
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    		application.setAttribute("servermanager",manager);
    	}
    	return manager; 
    }
    
    
    /* EndPoint XRD */
    @Execute(validator = false)
	public String index() {
    	return "index.jsp";
    }
    
    /* EndPoint */
    @Execute(validator = false ,urlPattern="check/{mid}")
	public String check() {
        return "index.jsp"; 
    }

    
    
    /* provider main */
    @Execute(validator = false)
	public String auth() {

    	ServerManager manager = getServerManager();

        ParameterList requestp;    	


        if ("complete".equals(request.getParameter("_action"))){ // Completing the authz and authn process by redirecting here
           	requestp=(ParameterList) session.getAttribute("parameterlist"); 
        }else{
            requestp = new ParameterList(request.getParameterMap());
        }
        
        String mode = requestp.hasParameter("openid.mode") ? requestp.getParameterValue("openid.mode") : null;

        Message responsem;
        
        if ("associate".equals(mode)){
            // --- process an association request ---
            responsem = manager.associationResponse(requestp);
            responseText = responsem.keyValueFormEncoding();
        }else if ("checkid_setup".equals(mode) || "checkid_immediate".equals(mode)){
            // interact with the user and obtain data needed to continue
            //List userData = userInteraction(requestp);
            String userSelectedId = null;
            String userSelectedClaimedId = null;
            Boolean authenticatedAndApproved = Boolean.FALSE;

            if ((session.getAttribute("authenticatedAndApproved") == null)||(((Boolean)session.getAttribute("authenticatedAndApproved")) == Boolean.FALSE) ){
                session.setAttribute("parameterlist", requestp);
                /*
                response.sendRedirect("provider_authorization.jsp");
                */

                /* ログインチェック処理 */
                return prepareLogin();
                
            }else{
          	
            	userSelectedId = (String) session.getAttribute("openid.claimed_id");
                userSelectedClaimedId = (String) session.getAttribute("openid.identity");
                
                authenticatedAndApproved = (Boolean) session.getAttribute("authenticatedAndApproved");
                // Remove the parameterlist so this provider can accept requests from elsewhere
                session.removeAttribute("parameterlist");
                session.setAttribute("authenticatedAndApproved", Boolean.FALSE); // Makes you authorize each and every time
            }
            AuthRequest authReq=null;
            try{
            	authReq = AuthRequest.createAuthRequest(requestp, manager.getRealmVerifier());
            }catch(Exception e){
            	e.printStackTrace();
            }

            //
            /*
            String ci = authReq.getClaimed();
            authReq.setClaimed(userSelectedClaimedId);
            if((userSelectedClaimedId!=null)&&userSelectedClaimedId.equals(authReq.getClaimed())){
            	//userSelectedId=userSelectedClaimedId;
            }
            */
            
            // --- process an authentication request ---
            responsem = manager.authResponse(requestp,userSelectedId,userSelectedClaimedId,authenticatedAndApproved.booleanValue(),false);

            //
            
            try{
            
	            if(authReq.hasExtension(AxMessage.OPENID_NS_AX)){
	            	MessageExtension ext = authReq.getExtension(AxMessage.OPENID_NS_AX);
	            	if(ext instanceof FetchRequest){
	            		FetchRequest fetchReq = (FetchRequest)ext;
	            		Map required = fetchReq.getAttributes(true);
	            		Map userDataExt = new HashMap();
	            		//ニックネーム設定
	            		if(required.containsKey("nickname")){
	            			
	            			userDataExt.put("nickname", URLEncoder.encode(userinfoDto.nickName, "UTF-8"));
	            			
	            		}
	            		//画像パス設定
	            		if(required.containsKey("image")){
	            			
            				String picPath ="";
            				try{
            					picPath = topService.selMyPhoto(userinfoDto.memberId).get("photo").toString();
            					
            					//画像登録ありの場合は、絶対パスに変更する
            					if(picPath!=null && !picPath.equals("")){
                					picPath = "http://"+appDefDto.FP_CMN_HOST_NAME+appDefDto.FP_CMN_CONTENTS_ROOT + picPath;            						
            					}
            				}catch(Exception e){
            					
            				}
	            			userDataExt.put("image", picPath);
	            			
	            		}
	            		
            			FetchResponse fetchResp = FetchResponse.createFetchResponse(fetchReq, userDataExt);
            			responsem.addExtension(fetchResp);


	            	}
	            }
	           
	            /* SREGは使用しない。(2010/1/28 saikawa)
	            if(authReq.hasExtension(SRegMessage.OPENID_NS_SREG)){
	            	MessageExtension ext = authReq.getExtension(SRegMessage.OPENID_NS_SREG);
	            	if(ext instanceof SRegRequest){
	            		SRegRequest sregReq = (SRegRequest)ext;
	            		List required = sregReq.getAttributes(false);
            			Map userDataExt = new HashMap();
            			SRegResponse sregResp = SRegResponse.createSRegResponse(sregReq, userDataExt);

            			//このif文がうまく動作していないので一旦、コメントアウト(2009/6/24)
	            		//if(required.contains("nickname")){
            				userDataExt.put("nickname", userinfoDto.nickName);
            				//文字化けするのでURLエンコードをする。
            				sregResp.addAttribute("nickname", URLEncoder.encode(userinfoDto.nickName, "UTF-8"));
            				//メンバーIDを設定
            				userDataExt.put("email", userinfoDto.memberId);
            				sregResp.addAttribute("email", URLEncoder.encode(userinfoDto.memberId,"UTF-8"));
            				//画像パス設定
            				String picPath ="";
            				try{
            					picPath = topService.selMyPhoto(userinfoDto.memberId).get("photo").toString();
            					
            					//画像登録ありの場合は、絶対パスに変更する
            					if(picPath!=null && !picPath.equals("")){
                					picPath = "http://"+appDefDto.FP_CMN_HOST_NAME+appDefDto.FP_CMN_CONTENTS_ROOT + picPath;            						
            					}
            				}catch(Exception e){
            					
            				}
            				userDataExt.put("fullname", picPath);
            				sregResp.addAttribute("fullname", URLEncoder.encode(picPath,"UTF-8"));
            				
	            			//userDataExt.put("nickname", "NICKNAME");
	            			//sregResp.addAttribute("nickname", "NUCKNAME");
	            		//}
	            		//if(required.contains("openid")){
	            		//	userDataExt.put("openid"  , userSelectedClaimedId);
	            		//	sregResp.addAttribute("openid", userSelectedClaimedId);
	            		//}
	            		responsem.addExtension(sregResp);

	            	}
	            }
	            */

	            
	            manager.sign((AuthSuccess)responsem);

	            
            }catch(Exception e){
            	e.printStackTrace();           	
            }
            //
            
            
            
            // caller will need to decide which of the following to use:
            // - GET HTTP-redirect to the return_to URL
            // - HTML FORM Redirection
            //responseText = response.wwwFormEncoding();
            if (responsem instanceof AuthSuccess){
            	
            	try{
            		response.sendRedirect(((AuthSuccess) responsem).getDestinationUrl(true));
            	}catch(Exception e){
            		e.printStackTrace();
            	}
                return null;
                
            	//return ((AuthSuccess) responsem).getDestinationUrl(true)+"?redirect=true";
            }else{
                responseText="<pre>"+responsem.keyValueFormEncoding()+"</pre>";
            }
        }else if ("check_authentication".equals(mode)){
            // --- processing a verification request ---
            responsem = manager.verify(requestp);
            responseText = responsem.keyValueFormEncoding();
        }else{
            // --- error response ---
            responsem = DirectError.createDirectError("Unknown request");
            responseText = responsem.keyValueFormEncoding();
        }
        //　レスポンス文字列を返す。
    	return "text.jsp";
	}


    /* ログインチェック処理 */
    private String prepareLogin() {
    	// (1) セッションに保持していたパラメータ値の取得
        ParameterList requestp=(ParameterList) session.getAttribute("parameterlist");
       
        Members opMember = null;
        
        // (2) ログイン中かどうかセッションチェック
        //  　　　　※ダミーユーザーは除いて。
        if(userinfoDto.memberId!=null&&userinfoDto.memberId.length()>1){
        	
        	// (3) select Member by ID    	
            opMember =  membersService.getResultById(userinfoDto.memberId);

            if(opMember.mid!=null&&opMember.membertype.equals("0")){
            	/////////////////////////////////
            	// OpenIDを編集して、ログイン確認画面へ
            	/////////////////////////////////
            	//String openid = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/frontier/pc/openidserver/check/"+userinfoDto.memberId;
            	String openid = request.getScheme() + "://" + appDefDto.FP_CMN_HOST_NAME + ":" + request.getServerPort() + "/frontier/pc/openidserver/check/"+userinfoDto.memberId;
            	session.setAttribute("openid.identity",openid);   
                //requestp.set(new Parameter("openid.claimed_id",openid));
                
            	session.setAttribute("openid.claimed_id",openid);

                
            	session.setAttribute("authenticatedAndApproved",Boolean.TRUE);
            	return  "authorization.jsp";            		
            }       	
        }
        // ログインしていなければ、ログイン画面へ遷移。
    	return "login.jsp";
    }
    
    
    /* ログイン処理 */
    @Execute(validate="checkLogin",input = "login.jsp")
    public String login() {
    	return prepareLogin();
    }

    public ActionMessages checkLogin() {
    	ActionMessages errors = new ActionMessages();
    		
        String p = null;
        try{
        	p = CmnUtility.getDigest(passwd);
        }catch(Exception e){}

        members =  membersService.getResultByEmailAndPasswd(email, p);
        if(members != null){ 
        	userinfoDto.memberId=members.mid;
           	userinfoDto.nickName=members.nickname;
           	userinfoDto.entdate=members.entdate;
           	userinfoDto.strEntdate=CmnUtility.dateFormat("yyyyMMdd",members.entdate);         	
           	return errors;
         } else {
    		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
    				"errors.login","ﾒｰﾙｱﾄﾞﾚｽ","ﾊﾟｽﾜｰﾄﾞ"));
         }
         return errors;		
    }
    
    
}
