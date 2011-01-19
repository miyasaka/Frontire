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
<title>[frontier]&nbsp;ファイル管理</title>
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
<script language="JavaScript" type="text/JavaScript">
<!--
function disp(){
	var obj1 = document.all && document.all("categoryid") || document.getElementById && document.getElementById("categoryid");
	var obj2 = document.all && document.all("inputcategory") || document.getElementById && document.getElementById("inputcategory");
	if(obj1 && obj1.style) obj1.style.display = "none" == obj1.style.display ?"" : "none"
	if(obj2 && obj2.style) obj2.style.display = "none" == obj2.style.display ?"" : "none"
	obj1.value = "";
	obj2.value = "";
}
//-->
</script>
</head>

<body>
<s:form method="post" enctype="multipart/form-data">
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
		<div class="mainFile">
			<!-- SideBox -->
			<%@ include file="/WEB-INF/view/pc/ffilesidebox.jsp"%>
			<!--/SideBox -->

			<div class="mainEntFile-main">
				<!-- エラー -->
				<html:errors />
				<!--/エラー -->
				<!-- ファイル登録 -->
				<div class="entFileMainBox">
					<div class="entFileMainBoxTitle">
						<table class="ttl">
							<tr>
								<td class="ttl_name_sp">
									<div class="ttl_name inputColStick">${f:h(entfileFinding)}</div>
									<div class="ttl_name_right"><s:link href="/pc/file/list/" title="ファイル一覧">ファイル一覧</s:link>&nbsp;|&nbsp;<font style="color: #c0c0c0;">登録する</font></div>
								</td>
							</tr>
						</table>
					</div>
					<!--/entFileMainBoxTitle -->
			<c:choose>
				<c:when test="${isFileExists}">
					<table class="entFileMainBoxBody">
						<tr>
							<td class="left">カテゴリ<font color="red">*</font></td>
							<td class="right select">
								<select name="categoryid" id="categoryid" style="width: 200px;${f:h(categoryidStyle)}">
									<option value="">選択してください</option>
								<c:forEach var="e" items="${fileSideBoxCategoryList}">
									<option value="${e.categoryid}"<c:if test="${categoryid == e.categoryid}"> selected</c:if>>${f:h(e.categorynameorg)}</option>
								</c:forEach>
								</select>
								<html:text property="inputcategory" value="${inputcategory}" maxlength="100" style="ime-mode: active;${f:h(inputcategoryStyle)}" styleId="inputcategory" />
								<a href="javascript:void(0);" onclick="disp(); return false;" title="入力切替"><img src="${appDefDto.FP_CMN_FILE_IMAGE_PATH}icon_notebook_pencil.png" alt="入力切替" /></a>
							</td>
						</tr>
						<tr>
							<td class="left">ファイル名<font color="red">*</font><br />(全角100文字以内)</td>
							<td class="right text"><html:text property="filetitle" value="${filetitle}" maxlength="100" style="ime-mode: active;" /></td>
						</tr>
						<tr>
							<td class="left">説明<font color="red">*</font><br />(全角10000文字以内)</td>
							<td class="right textarea">
								<div class="entFileBodyBoxReply">
									<dl>
										<dd class="clearfix">
											<ul>
												<li><a href="javascript:void(0)" onclick="resize_font(document.getElementById('diaryBody'), 'large');" title="文字テキスト大"><img src="/images/insert_enlarge_font002.gif" width="22" height="22" alt="文字テキスト大" /></a></li>
												<li><a href="javascript:void(0)" onclick="resize_font(document.getElementById('diaryBody'), 'medium');" title="文字テキスト中"><img src="/images/insert_average_font001.gif" width="22" height="22" alt="文字テキスト中" /></a></li>
												<li><a href="javascript:void(0)" onclick="resize_font(document.getElementById('diaryBody'), 'small');" title="文字テキスト小"><img src="/images/insert_reduce_font002.gif" width="22" height="22" alt="文字テキスト小" /></a></li>
												<li><a href="javascript:void(0)" onclick="add_tag(document.getElementById('diaryBody'), 'strong');" title="太字"><img src="/images/insert_boldface001.gif" width="22" height="22" alt="太字" /></a></li>
												<li><a href="javascript:void(0)" onclick="add_tag(document.getElementById('diaryBody'), 'em');" title="斜体"><img src="/images/insert_italic001.gif" width="22" height="22" alt="斜体" /></a></li>
												<li><a href="javascript:void(0)" onclick="add_tag(document.getElementById('diaryBody'), 'u');" title="下線"><img src="/images/insert_underline001.gif" width="22" height="22" alt="下線" /></a></li>
												<li><a href="javascript:void(0)" onclick="add_tag(document.getElementById('diaryBody'), 'del');" title="打消し線"><img src="/images/insert_strike001.gif" width="22" height="22" alt="打消し線" /></a></li>
												<li><a href="javascript:void(0)" onclick="return openPalette(event, 30, 20);" title="文字カラー"><img src="/images/insert_select_color001.gif" width="22" height="22" alt="文字カラー" /></a></li>
												<li><a href="javascript:void(0)" onclick="add_link(document.getElementById('diaryBody'));" title="リンク"><img src="/images/insert_url001.gif" width="22" height="22" alt="リンク" /></a></li>
												<li><a href="javascript:add_tag(document.getElementById('diaryBody'), 'blockquote');" title="引用"><img src="/images/insert_quotation001.gif" width="22" height="22" alt="引用" /></a></li>
												<li><a href="javascript:void(0)" onclick="openEmojiPalette(document.getElementById('diaryBody'), event); return false;" title="絵文字"><img src="/images/insert_icon001.gif" width="22" height="22" alt="絵文字" /></a></li>
												<%@ include file="/WEB-INF/view/common/emojiPalette.jsp" %>
											</ul>
										</dd>
										<dd>
											<div id="color_palette" style="position: absolute;" class="palette_pop"></div>
											<html:textarea rows="8" styleId="diaryBody" property="explanation" value="${explanation}" style="ime-mode: active;"></html:textarea>
										</dd>
									</dl>
								</div>
							</td>
						</tr>
						<tr>
							<td class="left">ファイル<font color="red">*</font></td>
							<td class="right text"><html:file property="filename" /></td>
						</tr>
						<tr>
							<td class="left">バージョン</td>
							<td class="right text"><html:text property="version" value="${version}" maxlength="10" style="ime-mode: inactive;" /></td>
						</tr>
						<tr>
							<td class="left">公開範囲<font color="red">*</font></td>
							<td class="right">
						<c:choose>
							<c:when test="${isFileEdit}">
								<input type="radio" name="pubfile" value="${appDefDto.FP_CMN_FILE_AUTH_OPEN[0]}"<c:if test="${pubfile eq appDefDto.FP_CMN_FILE_AUTH_OPEN[0]}"> checked="checked"</c:if> />${appDefDto.FP_CMN_FILE_AUTH_OPEN[1]}<br />
								<input type="radio" name="pubfile" value="${appDefDto.FP_CMN_FILE_AUTH_CLOSE[0]}"<c:if test="${pubfile eq appDefDto.FP_CMN_FILE_AUTH_CLOSE[0]}"> checked="checked"</c:if> />${appDefDto.FP_CMN_FILE_AUTH_CLOSE[1]}
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${pubfile eq appDefDto.FP_CMN_FILE_AUTH_OPEN[0]}">${appDefDto.FP_CMN_FILE_AUTH_OPEN[1]}</c:when>
									<c:when test="${pubfile eq appDefDto.FP_CMN_FILE_AUTH_CLOSE[0]}">${appDefDto.FP_CMN_FILE_AUTH_CLOSE[1]}</c:when>
									<c:otherwise>非公開</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
							</td>
						</tr>
					</table>
					
					<!--/entFileMainBoxBody -->
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">
							<div class="btnArea">
								<input type="submit" name="${f:h(entfileSubmitName)}" value="${f:h(entfileSubmitValue)}" />
							</div>
						</div>
					</div>
					<!--/listBoxHead -->
				</c:when>
				<c:otherwise>
					<div class="noResult">
						ファイルが削除されているか、あるいは存在しないアドレスへのアクセスです。 
					</div>
				</c:otherwise>
			</c:choose>
				</div>
				<!--/ファイル登録 -->
				<!--/entFileMainBox -->
			</div>
			<!--/mainEntFile-main -->
		</div>
		<!--/mainFile -->
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