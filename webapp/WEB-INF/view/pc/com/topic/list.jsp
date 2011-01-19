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
<title>[frontier]${f:h(communityDto.comnm)} トピック一覧</title>
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
	<%@ include file="/WEB-INF/view/pc/com/fmenu.jsp"%>
	<!-- マイページ共通 -->
	<!--/navbarメニューエリア-->

	<div id="contents" class="clearfix">
		<div class="mainCommunity">
			<!--メイン-->


			<div class="mainEvent-main" style="margin-bottom:-10px;">
			
				<div class="eventHead clearfix">
					<ul>
						<li>${f:h(communityDto.comnm)}&nbsp;&nbsp;トピック一覧</li>
						<li class="pgArea">
							<div>
								<c:if test="${offset>0}"><s:link href="prepg/${f:h(communityDto.cid)}">&lt;&lt;前を表示</s:link></c:if>
								<c:if test="${resultscnt>(offset + fn:length(results))}">&nbsp;&nbsp;&nbsp;&nbsp;<s:link href="nxtpg/${f:h(communityDto.cid)}">次を表示&gt;&gt;</s:link></c:if>
							</div>
						</li>
					</ul>
				</div>
				
				<c:choose>
				<c:when test="${resultscnt == 0}">
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
								まだ何もありません。
							</div>
						</div>
					</div>
				</div>
				<!--/表示件数が0件の場合-->
				</c:when>
				<c:otherwise>
				
				<div class="listBoxCommunityEventList clearfix">
					<div class="listBoxCommunityEventListTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name comColStick">トピック一覧(<c:choose><c:when test="${(offset+1)==(offset+fn:length(results))}">${resultscnt}</c:when><c:otherwise>${offset+1}～${offset+fn:length(results)}</c:otherwise></c:choose>／${resultscnt}件)</div></td>
							</tr>
						</table>
					</div>
					<!--/listBoxCommunityEventListTitle-->
					
					<c:forEach var="e" items="${results}">
					<div class="listBoxHead clearfix">
						<div class="ttlHeadAreaEventList">
							
							<div class="ttlHeadAreaEventListSub clearfix">
								<ul>
									<li class="txtArea"><s:link href="view/${f:h(communityDto.cid)}/${f:h(e.bbsid)}">${f:h(e.title)}</s:link><c:if test="${e.editflg=='1'}">&nbsp;&nbsp;&nbsp;&nbsp;<s:link href="/pc/com/enttopic/edit/${f:h(communityDto.cid)}/${f:h(e.bbsid)}">編集する</s:link></c:if></li>
									<li class="timeArea"><fmt:formatDate value="${f:date(f:h(e.maxupddate),'yyyyMMddHHmmss')}" pattern="yyyy年MM月dd日HH:mm" /></li>
								</ul>
							</div>
						</div>
						<div class="bodycontentsArea">
							<table>
								<tr>
									<td class="leftArea">
										<ul>
											<li class="leftNumComment"><s:link href="view/${f:h(communityDto.cid)}/${f:h(e.bbsid)}">${f:h(e.comcnt)}</s:link></li>
											<li class="leftComment"><s:link href="view/${f:h(communityDto.cid)}/${f:h(e.bbsid)}">ｺﾒﾝﾄ</s:link></li>
											<li class="leftNickname"><s:link href="/pc/mem/${f:h(e.mid)}">${f:h(e.nickname)}</s:link></li>
										</ul>
									</td>
									<td class="rightArea">
										<div class="bodyEventArea">
										
											<div class="eventPicArea">
												${e.pichtml}
											</div>
											${e.cmnthtml}
										</div>
									</td>
								</tr>
							</table>
						</div>
						<!--/bodycontentsArea-->
					</div>
					<!--/listBoxHead-->
					</c:forEach>

					<c:if test="${offset>0 || resultscnt>(offset + fn:length(results))}">
					<div class="eventHead clearfix" style="border-width:1px 0 0 0;">
						<ul>
							<li class="pgArea">
								<div>
									<c:if test="${offset>0}"><s:link href="prepg/${f:h(communityDto.cid)}">&lt;&lt;前を表示</s:link></c:if>
									<c:if test="${resultscnt>(offset + fn:length(results))}">&nbsp;&nbsp;&nbsp;&nbsp;<s:link href="nxtpg/${f:h(communityDto.cid)}">次を表示&gt;&gt;</s:link></c:if>
								</div>
							</li>
						</ul>
					</div>
					<!--/eventHead-->
				</div>
				<!--/listBoxCommunityEventList-->
				</c:if>
				</c:otherwise>
				</c:choose>
			</div>
			<!--/mainEvent-main-->

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
