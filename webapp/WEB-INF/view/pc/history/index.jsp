<%@ page language="java" contentType="text/html; charset=windows-31J" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta name="robots" content="nofollow,noindex"     />



<title>[frontier] 足あと</title>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<meta name="Slurp" content="NOYDIR" />
<meta name="robots" content="noodp" />
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/assort.css" type="text/css" />
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>


</head>

<body>
<s:form method="post">
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
		<div class="mainHistory">
			<!--メイン-->
			<div class="mainHistory-main">
				<!--listBox11-->
				<div class="History">
					<div class="HistoryTitle">
						<table class="ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name myColStick">足あと</div></td>
							</tr>
						</table>
					</div>
				</div>
				<div class="viewCnt">
					<div>
						総アクセス数：${f:h(totalCnt)}件
					</div>
				</div>
				<div class="listBoxPage">
					<div class="box01"><img src="/images/mark-g.gif" style="width:16px; height:16px;" /><span>…グループ　</span><img src="/images/mark-f.gif" style="width:16px; height:16px;"/><span>…フォロー中</span></div>
					<div class="box02_"><span>あなたのページにアクセスしたユーザーを最大で${f:h(appDefDto.FP_MY_HISTORYLIST_ALLMAX)}件まで表示しています。</span></div>
					<div class="box02">
						<c:if test="${offset>0}">
							<s:link href="/pc/history/prepg/">前を表示</s:link>
						</c:if>
						
						<c:if test="${totalCnt > (offset + fn:length(results)) and appDefDto.FP_MY_HISTORYLIST_ALLMAX>(offset + fn:length(results))}">
							<s:link href="/pc/history/nxtpg/">次を表示</s:link>
						</c:if>
						&nbsp;
					</div>
				</div>
				<div class="listBoxBody">
					<div class="box03">
						<c:forEach var="e" items="${results}">
							<div>
								<span><fmt:formatDate value="${f:date(f:h(e.lastvisittime),'yyyyMMddHHmmss')}" pattern="MM月dd日　HH:mm" /></span>
								<span class="name">
									<c:choose>
										<c:when test="${(f:h(e.membertype)) eq '0' || (f:h(e.membertype)) eq '2'}">
											<s:link href="/pc/mem/${f:h(e.mid)}">${f:h(e.nickname)}</s:link>
										</c:when>
										<c:when test="${(f:h(e.membertype)) eq '1'}">
											<c:choose>
											<c:when test="${frontierUserManagement.frontierdomain eq e.frontierdomain}">
												<s:link href="http://${f:h(frontierUserManagement.frontierdomain)}/frontier/pc/mem/${f:h(e.fid)}/">${f:h(e.sfnickname)}</s:link>
											</c:when>
											<c:otherwise>
												<s:link href="http://${f:h(e.frontierdomain)}/frontier/pc/openid/auth?cid=${f:h(e.fid)}&gm=mv&openid=${f:h(frontierUserManagement.frontierdomain)}/frontier/pc/openidserver">${f:h(e.sfnickname)}</s:link>
											</c:otherwise>
											</c:choose>
										</c:when>
									</c:choose>
									<c:choose>
										<c:when test="${e.friendFlg eq 1}">
											<img height="13" width="12" alt="同志" src="/images/mark-g.gif" style="width:16px; height:16px;"/>
										</c:when>
										<c:when test="${e.friendFriendFlg eq 1}">
											<img height="14" width="16" alt="同志の同志" src="/images/mark-f.gif" style="width:16px; height:16px;"/>
										</c:when>
									</c:choose>
								</span>
								<span>${f:h(e.visitcount)}回訪問</span>
							</div><br/>
						</c:forEach>
                     </div>
					<c:if test="${fn:length(results) eq 0}"><div style="padding:0 0 30px 0;">まだ足あとがありません</div></c:if>
				</div>
				<div class="listBoxPage">
					<div class="box02">
						<c:if test="${offset>0}">
							<s:link href="/pc/history/prepg/">前を表示</s:link>
						</c:if>
						
						<c:if test="${totalCnt > (offset + fn:length(results)) and appDefDto.FP_MY_HISTORYLIST_ALLMAX>(offset + fn:length(results))}">
							<s:link href="/pc/history/nxtpg/">次を表示</s:link>
						</c:if>
						&nbsp;
					</div>
				</div>

				
			</div>
			<!--/mainHistory-main-->
		</div>
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
