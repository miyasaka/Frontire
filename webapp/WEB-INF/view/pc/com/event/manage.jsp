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
<title>[frontier]${f:h(communityDto.comnm)} イベント参加者管理</title>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/colorful.js"></script>
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
	<%@ include file="/WEB-INF/view/pc/com/fmenu.jsp"%>
	<!-- マイページ共通 -->
	<!--/navbarメニューエリア-->

	<div id="contents" class="clearfix">
		<div class="mainCommunity">
			<!--メイン-->

			<div class="mainEvent-main" style="margin-bottom:-10px;">
			
				<div class="eventDelCommentHead clearfix" style="border-style:solid; border-width: 1px 1px 0 1px;">
					<ul>
						<li>${f:h(result[0].title)}&nbsp;&nbsp;イベント参加者管理</li>
					</ul>
				</div>
<c:choose>
<c:when test="${listflg=='1'}">
				<div class="listBoxCommunityEventList clearfix">
					<div class="listBoxCommunityEventListTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name inputColStick">イベント参加メンバー</div></td>
							</tr>
						</table>
					</div>
					<!--/listBoxCommunityEventListTitle-->
					
					<div class="listBoxHead clearfix">
						<div class="bodycontentsArea" style="border-style:solid; border-width:1px 0 0 0;">
							<table>
								<tr>
									<td class="leftArea">
										<ul>
											<li class="leftNickname">メンバー名</li>
										</ul>
									</td>
									<td class="rightArea">
										<div class="bodyEventArea">
										<c:forEach var="e" items="${results}" varStatus="i">
											<dl class="eventManageMember clearfix">
												<dt><input type="checkbox" name="chkmem" value="${e.mid}" <c:forEach var="obj" items="${(keepchkMemList)}" varStatus="status"><c:if test="${e.mid == obj}">checked</c:if></c:forEach> /></dt>
												<dd><label><s:link href="/pc/mem/${f:h(e.mid)}">${f:h(e.nickname)}</s:link></label></dd>
											</dl>
										</c:forEach>
										</div>
									</td>
								</tr>
							</table>
						</div>
						<!--/bodycontentsArea-->
					</div>
					<!--/listBoxHead-->
					<div class="comAddEventArea">
						<ul class="clearfix" style="width:150px;">
							<li><input type="submit" value="参加者から外す" name="delmembers" class="comAddEvent" style="width:150px;" /></li>
						</ul>
					</div>
					<!--/comAddEventArea-->
				</div>
				<!--/listBoxCommunityEventList-->
			</div>
			<!--/mainEvent-main-->
</c:when>
<c:when test="${listflg=='0'}">
				<!--表示件数が0件の場合-->
				<div class="listBoxCommunityEventList clearfix">
					<div class="listBoxCommunityEventListTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name comColStick">トピック一覧</div></td>
							</tr>
						</table>
					</div>
					<div class="listBoxHead clearfix">
						<div class="bodycontentsArea noDataArea">
							<div class="noData">
								まだ参加者はいません。
							</div>
						</div>
					</div>
				</div>
				<!--/表示件数が0件の場合-->
</c:when>
</c:choose>
		</div>
		<!--/mainCommunity-->
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
