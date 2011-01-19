<%@ page language="java" contentType="text/html; charset=windows-31J" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS"/>
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta name="robots" content="nofollow,noindex"     />
<title>[frontier] お知らせ${pageFlg eq '1'?'編集':'登録'}</title>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/colorful.js"></script>
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
		<div class="mainMember">
			<!--メイン-->

			<div class="mainMember-main">

				<!--エラー-->
				<html:errors/>
				<!--/エラー-->
			
				<div class="listBoxNews" style="border-width:0 1px 1px 1px;">
					<div class="listBoxNewsTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name inputColStick">お知らせ${pageFlg eq '1'?'編集':'登録'}</div></td>
							</tr>
						</table>
					</div>
					<table cellspacing="0" width="100%;" style="border-collapse:collapse;">
						<tr class="title"><td class="left" valign="top">タイトル<font color="red">※</font><br>(全角100文字以内)</td><td class="right" valign="top"><html:text property="title" value="${title}"/></td></tr>
						<tr><td class="left" valign="top">詳細<font color="red">※</font><br>(全角10000文字以内)</td>
							<td class="right detail" valign="top">
							<ul id="txtEditArea">
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
								<li><a href="javascript:void(0)" onclick="add_youtube_link('diaryBody')" title="YouTube"><img src="/images/insert_youtube.gif" width="22" height="22" alt="YouTube" /></a></li>
								<li><a href="javascript:add_map_link_google('diaryBody');" title="地図"><img src="/images/insert_map.gif" width="22" height="22" alt="地図" /></a></li>
								<%@include file="/WEB-INF/view/common/emojiPalette.jsp" %>
							</ul>
							<div id="color_palette" style="position: absolute;" class="palette_pop"></div>
								<html:textarea property="detail" styleId="diaryBody" rows="10" value="${detail}"></html:textarea>
							</td>
						</tr>
						<tr>
							<td class="left" valign="top">設定日時</td>
<td class="right date" valign="top">
<select name="year" style="width:5em;">
	<c:forEach begin="${appDefDto.FP_MY_CALENDAR_START_PGMAX}" end="${appDefDto.FP_MY_CALENDAR_END_PGMAX}" var="i">
		<option value="${i}" <c:if test="${year==i}"> selected</c:if>>${i}</option>
	</c:forEach>
</select>
年
<select name="month">
	<c:forEach begin="1" end="9" var="i">
		<c:set var="iyear" value="0${i}"/>
		<option value="${iyear}" <c:if test="${month==iyear}"> selected</c:if>>${iyear}</option>
	</c:forEach>
	<c:forEach begin="10" end="12" var="i">
		<option value="${i}" <c:if test="${month==i}"> selected</c:if>>${i}</option>
	</c:forEach>
</select>月
<select name="day">
	<c:forEach begin="1" end="9" var="i">
		<c:set var="imonth" value="0${i}"/>
		<option value="${imonth}" <c:if test="${day==imonth}"> selected</c:if>>${imonth}</option>
	</c:forEach>
	<c:forEach begin="10" end="31" var="i">
		<option value="${i}" <c:if test="${day==i}"> selected</c:if>><c:if test="${i<10}">0</c:if>${i}</option>
	</c:forEach>
</select>日

</td></tr>
						<tr><td class="left" valign="top">表示設定</td><td class="right" valign="top"><input type="checkbox" value="1" name="topflg" ${topflg eq '1'?'checked':''}/>トップに表示する</td></tr>
					</table>
					<div class="btnInputArea">
					<c:choose>
						<c:when test="${pageFlg eq '1'}"><input type="submit" name="editNews" value="編集する"></input></c:when>
						<c:otherwise><input type="submit" name="entryNews" value="登録する"></input></c:otherwise>
					</c:choose>
					</div>
				</div>
				<!--/listBoxMemberUpdate-->
			<c:if test="${pageFlg eq '1'}">
				<div class="listBoxNews" style="border-width:0 1px 1px 1px;">
					<div class="listBoxNewsTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name inputColStick">お知らせ削除</div></td>
							</tr>
						</table>
					</div>
					<div class="message"><span>上記のお知らせを削除します</span></div>
					<div class="btnInputArea">
						<input type="submit" name="delete" value="削除する"></input>
					</div>
				</div>
			</c:if>
			<!--/mainMember-main-->
			<div class="linkArea"><a href="/frontier/pc/news/list">お知らせ一覧へ</a></div>
		</div>
		<!--/mainMember-->
	</div>
	<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/sp10.png" alt=""/>
	<!--/contents-->
	<!-- フッター -->
	<%@ include file="/WEB-INF/view/pc/ffooter.jsp"%>
	<!-- フッター -->
	 <!--/footer-->
</div>
<!--/container-->
<html:hidden value="${newsId}" property="newsId"/>
<html:hidden value="${pageFlg}" property="pageFlg"/>
</s:form>
</body>
</html>
