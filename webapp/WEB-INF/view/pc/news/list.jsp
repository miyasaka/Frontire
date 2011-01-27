<%@ page language="java" contentType="text/html; charset=windows-31J" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS"/>
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta name="robots" content="nofollow,noindex"     />
<title>[frontier] お知らせ一覧</title>
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
			
				<div class="listBoxNews" style="border-width:0 1px 1px 1px;">
					<div class="listBoxNewsTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name ${informationflg eq '1'?'input':'my'}ColStick">お知らせ一覧<c:if test="${resultscnt>0}">&nbsp;(${offset+1}件～${offset+fn:length(results)}件)</c:if>&nbsp;&nbsp;&nbsp;&nbsp;<c:if test="${informationflg eq '1'}"><span><s:link href="entry">新規作成</s:link></span></c:if>&nbsp;&nbsp;&nbsp;&nbsp;</div></td>
							</tr>
						</table>
					</div>
					<div class="listBoxNewsPageLink01">&nbsp;
						<c:if test="${offset>0}">
								<s:link href="/pc/news/prepg">&lt;&lt;前の30件</s:link>
						</c:if>
						<c:if test="${resultscnt>(offset + fn:length(results))}">
							<s:link href="/pc/news/nxtpg">次の30件&gt;&gt;</s:link>
						</c:if>
					</div>
					<div class="listBoxNewsBody">
						<div>
						<c:choose>
							<c:when test="${resultscnt>0}">
								<c:choose>
								<c:when test="${informationflg eq '1'}">
									<div class="newsHead clearfix"><span class="date">設定日時</span><span class="htitle">タイトル</span><span class="display">TOPへの表示</span></div>
									<c:forEach var="e" items="${results}" varStatus="i">
										<div class="newsBody clearfix"><span class="date"><fmt:formatDate value="${f:date(e.dispdate,'yyyyMMdd')}" pattern="yyyy年MM月dd日"/></span><span class="title"><s:link href="view/${e.id}">${f:h(e.title)}</s:link></span><span class="edit"><s:link href="edit/${e.id}">[編集する]</s:link></span><div class="chk01"><input type="checkbox" name="chkList" value="${e.id}" ${e.topflg eq '1'?'checked':''}></input></div></div>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<div class="newsHead clearfix"><span class="date">お知らせ日時</span><span class="ltitle">タイトル</span></div>
									<c:forEach var="e" items="${results}" varStatus="i">
										<div class="newsBody clearfix"><span class="date"><fmt:formatDate value="${f:date(e.dispdate,'yyyyMMdd')}" pattern="yyyy年MM月dd日"/></span><span class="ltitle"><s:link href="view/${e.id}">${f:h(e.title)}</s:link></span></div>
									</c:forEach>
								</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise><div class="noData">最新のお知らせはありません</div></c:otherwise>
						</c:choose>
							
						</div>
					</div>
					<div class="listBoxNewsPageLink02">&nbsp;
						<c:if test="${offset>0}">
								<s:link href="/pc/news/prepg">&lt;&lt;前の30件</s:link>
						</c:if>
						<c:if test="${resultscnt>(offset + fn:length(results))}">
							<s:link href="/pc/news/nxtpg">次の30件&gt;&gt;</s:link>
						</c:if>
					</div>


					<c:if test="${informationflg eq '1'&&resultscnt>0}">
						<div class="btnArea"><input type="submit" name="setting" value="設定する"></input></div>
					</c:if>

					
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
<html:hidden value="${offset}" property="offset"/>
<html:hidden value="${pgcnt}" property="pgcnt"/>
</s:form>
</body>
</html>
