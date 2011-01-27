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
<title>[frontier]&nbsp;フォトアルバムを削除する</title>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/common.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/assort.css" type="text/css" />
</head>

<body>
<s:form>
<div id="container">

	<!--header-->
	<!-- ヘッダー -->
	<%@ include file="/WEB-INF/view/pc/fheader.jsp"%>
	<!-- ヘッダー -->
	<!--/header-->

	<!--navbarメニューエリア-->
	<!-- マイページ共通 -->
	<%@ include file="/WEB-INF/view/pc/fmenu.jsp"%>
	<!-- マイページ共通 -->
	<!--/navbarメニューエリア-->

	<div id="contents" class="clearfix">
		<div class="mainEntPhoto">
			<!--メイン-->
			<div class="mainEntPhoto-main">
				<div class="listBoxEntPhoto">
					<div class="listBoxEntPhotoTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name inputColStick">フォトアルバムの削除</div></td>
							</tr>
						</table>
					</div>
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea" style="text-align: center;">
								アルバムに含まれる全ての写真が削除されますので、ご注意ください。<br />
							<div class="btnArea">
								<input type="submit" name="execute" value="　削　　　除　" class="ttlbtn" />
								<input type="submit" name="stop" value="　や　め　る　" class="ttlbtn" />
							</div>
						</div>
					</div>
					<!--/listBoxHead -->
				</div>
				<!--/listBoxEntPhoto -->
			</div>
			<!--/mainEntPhoto-main -->
		</div>
		<!--/mainEntPhoto -->
	</div>
	<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/sp10.png" alt="" />
	<!--/contents -->

	<!-- footer -->
	<%@ include file="/WEB-INF/view/pc/ffooter.jsp"%>
	<!--/footer -->
</div>
<!--/container -->
</s:form>
</body>
</html>