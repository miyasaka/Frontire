<%@ page language="java" contentType="text/html; charset=windows-31J" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, org.openid4java.message.AuthSuccess, org.openid4java.server.InMemoryServerAssociationStore, org.openid4java.message.DirectError,org.openid4java.message.Message,org.openid4java.message.ParameterList, org.openid4java.discovery.Identifier, org.openid4java.discovery.DiscoveryInformation, org.openid4java.message.ax.FetchRequest, org.openid4java.message.ax.FetchResponse, org.openid4java.message.ax.AxMessage,  org.openid4java.message.*, org.openid4java.OpenIDException, java.util.List, java.io.IOException, javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.openid4java.server.ServerManager, org.openid4java.consumer.InMemoryConsumerAssociationStore, org.openid4java.consumer.VerificationResult" %>
<%
    // HOWTO:
    // the session var parameterlist contains openid authreq message parameters
    // this JSP should set the session attribute var "authenticatedAndApproved" and
    // redirect to provider.jsp?_action=complete

    ParameterList requestp=(ParameterList) session.getAttribute("parameterlist");
    String openidtrust_root=requestp.hasParameter("openid.trust_root") ? requestp.getParameterValue("openid.trust_root") : null;
    String openidreturnto=requestp.hasParameter("openid.return_to") ? requestp.getParameterValue("openid.return_to") : null;
    String openidclaimedid=requestp.hasParameter("openid.claimed_id") ? requestp.getParameterValue("openid.claimed_id") : null;
    String openididentity=requestp.hasParameter("openid.identity") ? requestp.getParameterValue("openid.identity") : null;

    String openid=(String) session.getAttribute("openid.identity");

    String gofrontiername = "";
    if(openidreturnto != null && !openidreturnto.equals("")){
    	//画面上に相手Frontierの名前を出すために余計な部分を削除
    	gofrontiername = openidreturnto.replaceAll("http://","");
    	gofrontiername = gofrontiername.replaceAll("https://","");
    	gofrontiername = gofrontiername.replaceAll(":80/frontier/pc/openid/returnurl","");
    	gofrontiername = gofrontiername.replaceAll("/frontier/pc/openid/returnurl","");
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<meta name="Slurp" content="NOYDIR" />
<meta name="robots" content="nofollow,noindex" />
<meta name="robots" content="noodp" />
<title>[frontier]OpenIDログイン画面</title>
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/assort.css" type="text/css" />
</head>
<body>
<s:form>

<div id="container">
	<!--header-->
	<%@ include file="/WEB-INF/view/pc/fnheader.jsp"%>
	<!--/header-->

	<!--スペース--><div class="login_spcdiv"></div><!--スペース-->

	<!--コンテンツ開始-->
	<div class="login_maindiv">
		<div align="center">
			<!--ログイン-->
			<div class="login_subdiv">
				<div class="login_errordiv">
					<!--ｴﾗｰ文言表示ｴﾘｱ-->
					<html:errors/>
					<!--ｴﾗｰ文言表示ｴﾘｱ-->
				</div>
				<div class="login_inputarea_main">
					<div style="width:100%;text-align:left;color:#ff0000;">
						<b>「<%=gofrontiername%>」</b>
						へログインしようとしています。<br/>
						このサイトへログインする場合は、「続ける」を押してください。<br/>
						あなたのユーザー情報は、<br/>
						<b>「<%=gofrontiername%>」</b><br/>
						へ送信されます。
					</div>
				</div>
				<center>
				<div class="login_btnarea">
					<input type="submit" class="login_btn" name="auth" value="続ける"/>
				</div>
				</center>
			</div>
			<!--ログイン-->
		</div>
	</div>
	<!--コンテンツ終了-->

	<!--スペース--><div class="login_spcdiv"></div><!--スペース-->

	<!--footer-->
	<%@ include file="/WEB-INF/view/pc/fnfooter.jsp"%>
	<!--footer-->

</div>
<input type="hidden"  name="_action" value="complete"/>
</s:form>
</body>
</html>
