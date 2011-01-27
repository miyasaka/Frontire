<%@ page language="java" contentType="text/html; charset=windows-31J" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<title>[frontier]</title>
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
		<div class="mainMember">
			<!--メイン-->

			<div class="mainMember-main">
			
				<div class="listBoxMemberList">
					<div class="eventDelCommentHead clearfix" style="border-style:solid; border-width: 0 0 1px 0;">
						<ul>
							<li>${f:h(communityDto.comnm)}&nbsp;&nbsp;メンバー管理</li>
						</ul>
					</div>
					<div class="listBoxMemberListTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name comColStick">メンバー一覧(<c:choose><c:when test="${(offset + 1) == (offset + fn:length(results))}">${resultscnt}</c:when><c:otherwise>${offset + 1}～${offset + fn:length(results)}</c:otherwise></c:choose>/${resultscnt}件)</div></td>
							</tr>
						</table>
					</div>
					<c:if test="${offset>0 || resultscnt>(offset + fn:length(results))}">
					<div class="linkBox01" style="border-width:0 0 0 0;">
						<div class="note"></div>
						<div>
							<c:if test="${offset>0}">
								<s:link href="movelist/${communityDto.cid}/${pgcnt - 1}">&lt;&lt;前を表示</s:link>
							</c:if>
							<span name="sp">&nbsp;</span>
							<c:if test="${resultscnt>(offset + fn:length(results))}">
								<s:link href="movelist/${communityDto.cid}/${pgcnt + 1}">次を表示&gt;&gt;</s:link>
							</c:if>
						</div>
					</div>
					</c:if>
					<!-- 一覧 -->
					<div class="comMemManage">
					
						<table>
							<tr>
								<th style="width:25%;">参加日時</th>
								<th class="thcenter">メンバー名</th>
								<th style="width:25%;">&nbsp;</th>
							</tr>
						<c:forEach var="e" items="${results}" varStatus="i">
							<tr>
								<td>${f:h(e.upddate)}</td>
								<td class="thcenter"><s:link href="/pc/mem/${f:h(e.mid)}" title="${f:h(e.nickname)}">${f:h(e.nickname)}</s:link></td>
								<td class="tdright"><c:choose><c:when test="${f:h(communityDto.makabletopic) == '1' && f:h(e.mid) == userInfoDto.memberId}"><b>管理人</b></c:when><c:otherwise><s:link href="/pc/com/mlist/confirm/${f:h(e.mid)}">メンバーから外す</s:link></c:otherwise></c:choose></td>
							</tr>
							</c:forEach>
						</table>
					</div>
					<!-- 一覧 -->
						
					<c:if test="${offset>0 || resultscnt>(offset + fn:length(results))}">
					<div class="linkBox02">
						<div class="note"></div>
						<div>
							<c:if test="${offset>0}">
								<s:link href="movelist/${communityDto.cid}/${pgcnt - 1}">&lt;&lt;前を表示</s:link>
							</c:if>
							<span name="sp">&nbsp;</span>
							<c:if test="${resultscnt>(offset + fn:length(results))}">
								<s:link href="movelist/${communityDto.cid}/${pgcnt + 1}">次を表示&gt;&gt;</s:link>
							</c:if>
						</div>
					</div>
					</c:if>
					
					<!--/listBoxMemberListTitle-->
					
				</div>
				<!--/listBoxMemberList-->
				
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
