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
<title>[frontier]ログイン画面</title>
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/style_newlogin.css" type="text/css" />
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/style01.css" type="text/css" />
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/layout.css" type="text/css" />
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/error.css" type="text/css" />
</head>
<body onload="document.getElementById('email').focus();">
<s:form>

<div id="container">
	<!--header-->
	<%@ include file="/WEB-INF/view/pc/fnheader.jsp"%>
	<!--/header-->

	<!--スペース-->
	<div class="login_spcdiv">
	コミュニケーションの輪から信頼の輪へ
	</div><!--スペース-->

	<!--コンテンツ開始-->
	<div class="login_maindiv">
		<div align="center">
			<!--ログイン-->

			<div class="login_subdiv">
				<div class="login_inputarea_main">
					<div class="login_inputarea_sub">
						<!--ｴﾗｰ文言表示ｴﾘｱ-->
						<html:errors/>
						<!--ｴﾗｰ文言表示ｴﾘｱ-->
						<table border="0" align="right">
							<tr>
								<td style="text-align:left;"><font style="font-size:12px;">メールアドレス</font></td>
							</tr>
							<tr>
								<td><input type="text" style="height:18px;width:150px;" name="email" maxlength="240"  value="${f:h(email)}" id="email"/></td>
							</tr>
							<tr>
								<td style="text-align:left;"><font style="font-size:12px;">パスワード</font></td>
							</tr>
							<tr>
								<td><input type="password" style="height:18px;width:150px;" name="passwd" maxlength="240"  value=""/></td>
							</tr>
							<tr>
								<td><div class="login_btnarea"><input type="submit" class="login_btn" name="login" value="ログイン"/></div></td>
							</tr>
						</table>
					</div>
					<div class="login_mobileurlarea">
<c:if test="${appDefDto.FP_CMN_QRIMG!=''}">
						<!-QRコード表示エリア-->
						<img src="/images/${appDefDto.FP_CMN_QRIMG}" alt="バーコードリーダーで読み込んでください"/><br/>
						<!-QRコード表示エリア-->
</c:if>
						<img src="/images/mobile.gif" alt="モバイル版FRONTIER"/>&nbsp;<a href="http://${appDefDto.FP_CMN_HOST_NAME}/frontier/m/login/" title="モバイル版FRONTIER" target="_blank"><font style="color:#000000;">モバイル版<strong>FRONT<font style="color:#ff0000;">i</font>ER</strong></font></a>
					</div>
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