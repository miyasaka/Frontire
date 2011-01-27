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
<title>[frontier]${f:h(communityDto.comnm)}</title>
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
		<div class="main">
			<!--メイン-->
			<div class="main-side">
				
				<div class="communityBox">
					<div  class="communityBoxTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name comColStick">コミュニティ</div></td>
							</tr>
						</table>
					</div>
					<div class="listBoxBody">
						<!-- コミュニティ画像エリア -->
						<div class="communityBoxImage">
							<c:choose>
							<c:when test="${f:h(communityDto.pic) != '' && f:h(communityDto.pic) != null}"><img src="${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(communityDto.pic,'dir','pic180')}" alt="${f:h(communityDto.comnm)}"/></c:when>
							<c:otherwise><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg180.gif" alt="${f:h(communityDto.comnm)}"/></c:otherwise>
							</c:choose>
							<div class="comName">${f:h(communityDto.comnm)}</div>
						</div>
						<!-- /コミュニティ画像エリア -->
						
						<c:if test="${communityDto.makabletopic=='1'}">
						<!-- 管理人用項目 -->
						<div class="comListBox">
							<ul>
								<li class="cngPic"><s:link href="/pc/com/entprofile/${f:h(communityDto.cid)}">写真の変更</s:link></li>
								<li class="cngSet"><s:link href="/pc/com/entprofile/${f:h(communityDto.cid)}">コミュニティ設定変更</s:link></li>
							</ul>
						</div>
						<!-- /管理人用項目 -->
						</c:if>
						<div class="comListBox">
							<ul>
								<li>開設日：<fmt:formatDate value="${f:date(resultscdd.entdate, 'yyyyMMdd')}" pattern="yyyy年MM月dd日"/></li>
								<li class="manageTime">(運営期間<fmt:formatNumber value="${f:number(resultscdd.periodday, '0')}" pattern="0"/>日<c:if test="${resultscdd.periodhour!=0}"><fmt:formatNumber value="${f:number(resultscdd.periodhour, '0')}" pattern="0"/>時間</c:if>)</li>
							</ul>
						</div>
						<div class="comListBox">
							<ul>
								<li>管理人：<c:choose><c:when test="${resultscdd.status eq '2'}">現在、このコミュニティの管理人はいません<br/><c:if test="${communityDto.makabletopic ne '0'}">(<s:link href="/pc/com/chgmanager/${f:h(communityDto.cid)}">管理人を引き受ける</s:link>)</c:if></c:when><c:otherwise><s:link href="/pc/mem/${f:h(resultscdd.admmid)}">${f:h(resultscdd.nickname)}</s:link></c:otherwise></c:choose></li>
							</ul>
						</div>
						<div class="comListBox">
							<ul>
								<li>ｶﾃｺﾞﾘ：${f:h(resultscdd.itemname)}</li>
							</ul>
						</div>
						<div class="comListBox">
							<ul>
								<li>ﾒﾝﾊﾞｰ数：${f:h(resultscntm)}人</li>
							</ul>
						</div>
						<div class="comListBox">
							<ul>
								<li>参加条件：${f:h(resultscdd.jointxt)}</li>
							</ul>
						</div>
						<div class="comListBox">
							<ul>
								<li>公開範囲：${f:h(resultscdd.pubtxt)}</li>
							</ul>
						</div>
						<div class="comListBox">
							<ul>
								<li>作成権限：${f:h(resultscdd.authtxt)}</li>
							</ul>
						</div>
					</div>
				</div>

				<c:if test="${resultscntm > 0}">
				<c:set var="row" value="3"/>
				<!-- メンバー一覧 -->
				<div class="communityBox">
					<div  class="communityBoxTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name comColStick"><s:link href="/pc/com/mlist/${f:h(communityDto.cid)}">メンバー一覧</s:link></div></td>
							</tr>
						</table>
					</div>
					<div class="listBoxBody">
						<div class="listBoxCommunity">
							<table>
							<c:forEach var="e" items="${resultsm}" varStatus="i">
								<c:if test="${i.count%row==1}">
								<tr>
								</c:if>
									<td align="center">
										<c:choose>
											<c:when test="${f:h(e.pic) != ''}">
												<s:link href="/pc/mem/${f:h(e.mid)}" title="${f:h(e.nickname)}"><img src="${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(e.pic,'dir','pic60')}" alt="${f:h(e.nickname)}"/></s:link>
											</c:when>
											<c:otherwise>
												<s:link href="/pc/mem/${f:h(e.mid)}" title="${f:h(e.nickname)}"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg60.gif" alt="${f:h(e.nickname)}"/></s:link>
											</c:otherwise>
										</c:choose>
									</td>
									<c:choose>
										<c:when test="${i.count==resultscntm}">
											<c:if test="${i.count%row!=0}">
											<c:forEach begin="1" end="${row-i.count%row}">
												<td>&nbsp;</td>
											</c:forEach>
											</c:if>
											</tr>
										</c:when>
										<c:otherwise><c:if test="${i.count%row==0}"></tr></c:if></c:otherwise>
									</c:choose>
							</c:forEach>
							</table>
						</div>
						<c:choose>
						<c:when test="${communityDto.makabletopic=='1'}">
						<div style="text-align:right; padding:3px 5px; border-style:dotted; border-width:1px 0 0 0;">
							<span><s:link href="/pc/com/mlist/manage/${f:h(communityDto.cid)}">メンバー管理</s:link></span>
						</div>
						</c:when>
						</c:choose>
					</div>
				</div>
				<!--/communityBox-->
				<!-- /メンバー一覧 -->
				</c:if>

				
			</div>
			<!--/main-side-->
			<div class="main-main">
				<!--コミュニティ説明-->
				<div class="listBoxCommunity02 clearfix">
					<div class="listBoxCommunity02Title">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name comColStick">コミュニティ説明</div></td>
							</tr>
						</table>
					</div>
					<div class="listBoxBody">
						<div class="profileBox">
							<ul>
								<li class="clearfix">
									${f:br(resultscdd.detailhtml)}
								</li>
							</ul>
						</div>
					</div>
				</div>
				<!--/コミュニティ説明-->
				<!--/listBoxCommunity02-->
				
				<c:if test="${resultscntt > 0 || resultscnte > 0}">
				<c:if test="${resultscntt > 0}">
				<!--トピック-->
				<div class="listBoxCommunity02 clearfix">
					<div class="listBoxCommunity02Title">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name comColStick"><s:link href="/pc/com/topic/${f:h(communityDto.cid)}">トピック</s:link></div></td>
							</tr>
						</table>
					</div>
					<div class="listBoxBody">
						<div class="topicBox">
							<ul class="topicList clearfix">
								<c:forEach var="e" items="${resultst}">
								<li class="clearfix">
									<dl>
										<dt class="topicDate">${f:h(e.maxupddatetop2)}</dt>
										<dd><s:link href="/pc/com/topic/view/${f:h(communityDto.cid)}/${f:h(e.bbsid)}">${f:h(e.title)}(${f:h(e.comcnt)})</s:link></dd>
									</dl>
								</li>
								</c:forEach>
							</ul>
							<div class="listBoxBodyBottom"><s:link href="/pc/com/topic/${f:h(communityDto.cid)}">一覧へ</s:link></div>
						</div>
					</div>
				</div>
				<!--/トピック-->
				<!--/listBoxCommunity02-->
				</c:if>
			
				<c:if test="${resultscnte > 0}">
				<!--イベント-->
				<div class="listBoxCommunity02 clearfix">
					<div class="listBoxCommunity02Title">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name comColStick"><s:link href="/pc/com/event/${f:h(communityDto.cid)}">イベント</s:link></div></td>
							</tr>
						</table>
					</div>
					<div class="listBoxBody">
						<div class="topicBox">
							<ul class="topicList clearfix">
								<c:forEach var="e" items="${resultse}">
								<li class="clearfix">
									<dl>
										<dt class="eventDate">${f:h(e.maxupddatetop2)}</dt>
										<dd><s:link href="/pc/com/event/view/${f:h(communityDto.cid)}/${f:h(e.bbsid)}">${f:h(e.title)}(${f:h(e.comcnt)})</s:link></dd>
									</dl>
								</li>
								</c:forEach>
							</ul>
							<div class="listBoxBodyBottom"><s:link href="/pc/com/event/${f:h(communityDto.cid)}">一覧へ</s:link></div>
						</div>
					</div>
				</div>
				<!--/イベント-->
				<!--/listBoxCommunity02-->
				</c:if>
				</c:if>
			</div>
			<!--/main-main-->
		</div>
		<!--/main-->


	</div>
	<!--/contents-->
	<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/sp10.png" alt=""/>
	<!-- フッター -->
	<%@ include file="/WEB-INF/view/pc/ffooter.jsp"%>
	<!-- フッター -->
	 <!--/footer-->
</div>
<!--/container-->
</s:form>
</body>
</html>
