<%@ page language="java" contentType="text/html; charset=windows-31J" pageEncoding="UTF-8"%>
<%@ page import="frontier.dto.AppDefDto"%>
<%@ include file="/WEB-INF/view/common/contentType.jsp"%>
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
<title>[frontier]Frontier接続ｴﾗｰ画面</title>
<% 
AppDefDto add = new AppDefDto();
String userAgent = request.getHeader("user-agent");
%>

<%/* PCエラー画面はCSSを有効にする*/%>
<% if (userAgent.indexOf("DoCoMo") == -1 || userAgent.indexOf("J-PHONE") == -1 || userAgent.indexOf("Vodafone") == -1 || userAgent.indexOf("SoftBank") == -1 || userAgent.indexOf("UP.Browser") == -1 || userAgent.indexOf("KDDI") == -1){ %>
	<link rel="stylesheet" href="/static/style/<%= add.FP_CMN_COLOR_TYPE %>/css/assort.css" type="text/css" />
<% } %>
</head>

<% if (userAgent.indexOf("DoCoMo") == 0 || userAgent.indexOf("J-PHONE") == 0 || userAgent.indexOf("Vodafone") == 0 || userAgent.indexOf("SoftBank") == 0 || userAgent.indexOf("UP.Browser") == 0 || userAgent.indexOf("KDDI") == 0){ %>
	<%/* モバイルエラー画面*/%>
	<body style="width:350px;">
		<div>
			<div>
				<div style="background-color:#100800; text-align:center;">
					<img src="/images/m/sp5.gif" ><br>
					<a href="/frontier/m/top/" title="ホーム"><img src="/images/m/logom.gif" ></a>
					<img src="/images/m/sp5.gif" ><br>
				</div>
				<!--コンテンツ開始-->
				<div style="background-color:#afafaf;">
					<span style="font-size:large;">ｴﾗｰ</span>
				</div>
					<hr style="height:2px; border-color:#7d7d7d; background-color:#7d7d7d;">
	
					<!--マイコンテンツ-->
					<div>
						あなたはこのFrontierへは接続することは出来ません。
					</div>				
	
					<hr style="height:2px; border-color:#7d7d7d; background-color:#7d7d7d;">
					<!--footer-->
					<%@ include file="/WEB-INF/view/m/footer.jsp"%>
					<!--/footer-->
				<!--コンテンツ終了-->
			</div>
		</div>
	</body>
<% }else{ %>
	<%/* PCエラー画面*/%>
	<body>
	<div id="container">
	
		<!--header-->
		<div id="header">
		<div class="headerNavi">
			<div class="headerNavi_top">
				<a href="/frontier/pc/top/" title="ホーム"><img src="/static/style/<%= add.FP_CMN_COLOR_TYPE %>/images/frontier01.gif" alt="frontier"/></a>
				&nbsp;<a href="http://www.charlie-s.jp" style="font-size:11px;" title="Charlie Software" target="_blank">PRODUCED BY CHARL<font style="color:#ff0000;">i</font>E-SOFTWARE</a>
			</div>
		</div>
		</div>
		<!--/header-->
	
		<!--スペース--><div class="login_spcdiv"></div><!--スペース-->
	
		<!--コンテンツ開始-->
		<div class="login_maindiv">
			<div align="center">
				<!--ログイン-->
				<div class="login_subdiv">
					<div style="text-align:center;color:#ff0000;">
						あなたはこのFrontierへは接続することは出来ません。
					</div>
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
	</body>
<% } %>

</html>