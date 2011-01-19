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
<title>[frontier]&nbsp;${f:h(entphotoFinding)}</title>
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
<s:form>
<div id="container">

	<!-- header -->
	<!-- ヘッダー -->
	<%@ include file="/WEB-INF/view/pc/fheader.jsp"%>
	<!-- ヘッダー -->
	<!--/header -->

	<!--navbarメニューエリア -->
	<!-- マイページ共通 -->
	<%@ include file="/WEB-INF/view/pc/fmenu.jsp"%>
	<!-- マイページ共通 -->
	<!--/navbarメニューエリア -->

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
								<td class="ttl_name_sp"><div class="ttl_name inputColStick">${f:h(entphotoFinding)}</div></td>
							</tr>
						</table>
					</div>
					<!--/listBoxEntPhotoTitle -->
			<c:choose>
				<c:when test="${isPhotoExists}">
					<table cellspacing="0" width="100%;" style="border-collapse:collapse;">
						<tr>
							<td class="left" valign="top">タイトル<font color="red">*</font><br />(全角100文字以内)</td>
							<td class="right title" valign="top"><html:text property="albumTitle" value="${albumTitle}" /></td>
						</tr>
						<tr>
							<td class="left" valign="top">説明<font color="red">*</font><br />(全角10000文字以内)</td>
							<td class="right detail" valign="top">
								<div class="entphotoBodyboxReply">
									<dl>
										<dd class="clearfix">
											<ul>
												<li>
													<a href="javascript:void(0)" onclick="openEmojiPalette(document.getElementById('albumBody'), event); return false;" title="絵文字"><img src="/images/insert_icon001.gif" width="22" height="22" alt="絵文字" /></a>
													<!-- 絵文字パレット -->
													<%@ include file="/WEB-INF/view/common/emojiPalette.jsp" %>
													<!--/絵文字パレット -->
												</li>
											</ul>
										</dd>
										<dd>
											<html:textarea rows="6" cols="50" styleId="albumBody" property="albumBody" value="${albumBody}"></html:textarea>
										</dd>
									</dl>
								</div>
							</td>
						</tr>
						<tr>
							<td class="left" valign="top">公開レベル</td>
							<td class="right" valign="top">
								<input type="radio" name="level" value="1" <c:if test="${level eq '1'}">checked</c:if> />全体に公開<br />
								<input type="radio" name="level" value="2" <c:if test="${level eq '2'}">checked</c:if> />グループに公開<br />
								<input type="radio" name="level" value="9" <c:if test="${level eq '9'}">checked</c:if> />非公開&nbsp;
							</td>
						</tr>
					</table>
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">
							<div class="btnArea">
								<input type="submit" name="${f:h(entphotoSubmitName)}" value="${f:h(entphotoSubmitValue)}" />
								<c:if test="${isEdit}"><input type="submit" name="cancel" value="　キャンセル　" /></c:if>
							</div>
						</div>
					</div>
					<!--/listBoxHead -->
				</div>
				<!--/listBoxEntPhoto -->
			<c:if test="${isEdit}">
				<div class="listBoxEntPhoto">
					<div class="listBoxEntPhotoTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name inputColStick">フォトアルバムを削除する</div></td>
							</tr>
						</table>
					</div>
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">
							<div class="btnArea">
								<input type="submit" name="deletePhotoAlbum" value="　削　　　除　" />
							</div>
						</div>
					</div>
					<!--/listBoxHead -->
			</c:if>
				</c:when>
				<c:otherwise>
					<div class="noResult">
						フォトアルバムが削除されているか、あるいは存在しないアドレスへのアクセスです。 
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