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
<title>[frontier] RSSファイル出力形式一覧</title>
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/assort.css" type="text/css" />

<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>

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
				
				<div class="listBoxRSS" style="border-width:0 1px 1px 1px;">
					<div class="listBoxRSSTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name manageColStick">RSSファイル出力形式一覧(全${f:h(fn:length(rssList))}件)</div></td>
							</tr>
						</table>
					</div>
					<div class="align03" style="padding:10px 0;"><s:link href="/pc/rssmanage/newentry/" style="margin-right:10px;">新規追加</s:link></div>

					<%-- RSSファイル出力形式一覧表示 --%>
					<c:forEach var="e" items="${rssList}" varStatus="status">
						<%-- 初回のみ実行 --%>	
						<c:if test="${status.first}">
							<table cellspacing="0" width="100%;" style="border-collapse:collapse;">
						</c:if>

						<tr>
							<td width="450px" class="left solid01 dotted" valign="top">${f:h(e.patternname)}</td>
							<td class="solid01 dotted" style="text-align:right;"><span style="margin-right:10px;"><s:link href="/pc/rssmanage/updentry/${f:h(e.id)}">内容更新</s:link>&nbsp;&nbsp;<s:link href="/pc/rssmanage/memberadd/${f:h(e.id)}">メンバ変更</s:link>&nbsp;&nbsp;<s:link href="/pc/rssmanage/confirmlist/${f:h(e.id)}">削除</s:link></span></td>
						</tr>
						<tr>
							<td colspan="2" class="left" valign="top"><b>内容：</b><br/>
							${f:br(f:h(e.detail))}</td>
						</tr>
						<tr>
							<td colspan="2" class="left" valign="top"><b>対象メンバ：</b><br/>
							${f:h(e.nickname)}</td>
						</tr>

						<%-- 最後のみ実行 --%>
						<c:if test="${status.last}">
							</table>
						</c:if>
					</c:forEach>

				</div>

				<!--/listBoxMemberUpdate-->
				
			</div>
			<!--/mainMember-main-->
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
</s:form>
</body>
</html>
