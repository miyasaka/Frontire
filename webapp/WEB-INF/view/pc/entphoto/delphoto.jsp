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
<title>[frontier]&nbsp;写真を削除する</title>
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
			<!-- メイン -->
			<div class="mainEntPhoto-main">
				<div class="listBoxEntPhoto">
					<div class="listBoxEntPhotoTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name inputColStick">写真を削除する</div></td>
							</tr>
						</table>
					</div>
					<!--/listBoxEntPhotoTitle -->
			<c:choose>
				<c:when test="${isDelPhotoExists}">
					<table cellspacing="0" width="100%;" style="border-collapse:collapse;">
						<tr>
							<td colspan="2" class="center_img" align="center" valign="middle">
								<img src="${appDefDto.FP_CMN_CONTENTS_ROOT}/${fn:replace(picPath,'dir','pic640')}" alt="${picname}" />
							</td>
						</tr>
						<tr>
							<td class="left" valign="top">説明</td>
							<td class="right title" valign="top">${f:h(picname)}</td>
						</tr>
					</table>
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">
							<div class="btnArea">
								<input type="submit" name="deletePhotoExecute" value="　削　　　除　" />
								<input type="submit" name="stopPhoto" value="　キャンセル　" />
							</div>
						</div>
					</div>
				</c:when>
				<c:otherwise>
					<div class="noResult">
						写真が削除されているか、あるいは存在しないアドレスへのアクセスです。 
					</div>
				</c:otherwise>
			</c:choose>
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