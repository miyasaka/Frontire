<%@page language="java" contentType="text/html; charset=windows-31j" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS"/>
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta name="robots" content="nofollow,noindex"     />
<title>[frontier] お知らせ削除確認</title>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/colorful.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/common.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/prototype.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/video.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/youtube.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/map.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/effects.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/windowstate.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/overlay.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/popup.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/emoji_palette_base.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/emoji_palette.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier_1.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>
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
				
				<div class="listBoxNews" style="border-width:0 1px 1px 1px;">
					<div class="listBoxNewsTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name inputColStick">お知らせ削除</div></td>
							</tr>
						</table>
					</div>
					<div class="message"><span>下記のお知らせを削除します</span></div>
					<div class="btnInputArea">
						<input type="submit" name="deleteNews" value="削除する"></input><span name="sp">&nbsp;</span><input type="submit" name="cansel" value="やめる"></input>
					</div>
				</div>
			
				<div class="listBoxNews" style="border-width:0 1px 1px 1px;">
					<div class="listBoxNewsTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name myColStick">お知らせの内容</div></td>
							</tr>
						</table>
					</div>
					<table cellspacing="0" width="100%;" style="border-collapse:collapse;">
						<tr><td class="left" valign="top">タイトル</td><td class="right">${f:h(viewResults['title'])}</td></tr>
						<tr><td class="left" valign="top">詳細</td><td class="right"><div class="diaryBody">	${f:br(viewResults['comment'])}</div></td></tr>
						<tr><td class="left" valign="top">設定日時</td><td class="right"><fmt:formatDate value="${f:date(f:h(viewResults['dispdate']),'yyyyMMdd')}" pattern="yyyy年MM月dd日"/></td></tr>
						<tr><td class="left_last" valign="top">表示設定</td><td class="right_last">トップに表示${viewResults['topflg'] eq '1'?'する':'しない'}</td></tr>
					</table>

					
				</div>
				<!--/listBoxMemberUpdate-->
				
			</div>
			<!--/mainMember-main-->
		</div>
		<!--/mainMember-->
	</div>
	<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/sp10.png" alt=""/>
	<!--/contents-->
	 <%@ include file="/WEB-INF/view/pc/ffooter.jsp" %>
	 <!--/footer-->
</div>
<!--/container-->
<html:hidden property="newsId" value="${newsId}"/>
</s:form>
</body>
</html>
