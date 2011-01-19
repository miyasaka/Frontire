<%@ page language="java" contentType="text/html; charset=windows-31J" pageEncoding="UTF-8"%>
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
<title>[frontier]OpenID認証画面</title>
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/assort.css" type="text/css" />
</head>
<body onload="document.getElementById('openid').focus();">
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
					<div class="login_inputarea_sub">
						<table border="0">
							<tr>
								<td style="text-align:left;"><font style="font-size:12px;">Openid</font></td>
								<td><input type="text" style="height:18px;width:150px;" name="openid" maxlength="240"  value="${f:h(openid)}" id="openid"/></td>
							</tr>
						</table>
					</div>
				</div>

				<div class="login_btnarea">
					<!--<input type="checkbox" disabled/><font style="font-size:9px;">次回から自動的にログイン</font>-->
					<input type="submit" class="login_btn" name="auth" value="ＯＫ"/>
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

</s:form>
</body>
</html>