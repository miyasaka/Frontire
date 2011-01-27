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
<title>[frontier]&nbsp;写真を編集する</title>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/colorful.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/common.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/prototype.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/effects.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/overlay.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/popup.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/windowstate.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/color_palette.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/youtube.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/emoji_palette_base.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/emoji_palette.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier_2.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/map.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/color_palette.css" type="text/css" />
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/emoji_palette.css" type="text/css" />
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/assort.css" type="text/css" />
</head>

<body>
<s:form method="post" enctype="multipart/form-data">
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
				<!-- エラー -->
				<html:errors />
				<!--/エラー -->
				<div class="listBoxEntPhoto">
					<div class="listBoxEntPhotoTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name inputColStick">写真を編集する</div></td>
							</tr>
						</table>
					</div>
					<!--/listBoxEntPhotoTitle -->
			<c:choose>
				<c:when test="${isEditPhotoExists}">
					<table cellspacing="0" width="100%;" style="border-collapse:collapse;">
						<tr>
							<td colspan="2" class="center_img" align="center" valign="middle">
								<img src="${appDefDto.FP_CMN_CONTENTS_ROOT}/${fn:replace(picPath,'dir','pic640')}" alt="${picname}" />
								<html:hidden property="picPath" value="${picPath}"/>
							</td>
						</tr>
						<tr>
							<td class="left" valign="top">写真</td>
							<td class="right title" valign="top"><input type="file" name="pic" size="50" /></td>
						</tr>
						<tr>
							<td class="left" valign="top">説明<font color="red">*</font><br />(全角100文字以内)</td>
							<td class="right title" valign="top"><html:text property="picname" value="${picname}" /></td>
						</tr>
					</table>
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">
							<div class="btnArea">
								<input type="submit" name="updatePhoto" value="　編　　　集　" />
							</div>
						</div>
					</div>
				</div>
				<!--/listBoxEntPhoto -->
				<div class="listBoxEntPhoto">
					<div class="listBoxEntPhotoTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name inputColStick">写真を削除する</div></td>
							</tr>
						</table>
					</div>
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">
							<div class="btnArea">
								<input type="submit" name="deletePhoto" value="　削　　　除　" />
							</div>
						</div>
					</div>
					<!--/listBoxHead -->
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