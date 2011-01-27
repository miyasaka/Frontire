<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/contentType.jsp"%>
<?xml version="1.0" encoding="Shift_JIS"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHML 1.0 transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ja" lang="ja">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
	<title>[frontier]ｽｹｼﾞｭｰﾙ</title>
<style type="text/css">
<!--
	body{
		font-size:xx-small;
		background-color:#fafafa;
	}
	a:link{
		color:#2288FF;
	}
	a:focus{
		color:#ffffff;
	}
	a:visited{
		color:#2288FF;
	}
	a:hover{
		color:#ff0000;
	}

-->
</style>
</head>
<body style="width:350px;">
<s:form>
<%-- ロケール情報を設定 --%>
<fmt:setLocale value="ja-JP" />
	<div>
		<div style="font-size:xx-small;">
			<!--header-->
			<div style="background-color:#afafaf;">
				&nbsp;<span style="color:#0000ff; font-size:xx-small;">●</span>&nbsp;<span style="font-size:xx-small;">ｽｹｼﾞｭｰﾙ${defDisptypeCalendar eq '1'?'【共有のみ】':'【全て】'}</span>
			</div>
			<div style="text-align:right;"><a href="#pbtm" name="ptop" id="ptop" accesskey="8"><span style="font-size:xx-small;">[8]▼</span></a></div>
			<!--/header-->
			<!--コンテンツ開始-->
				
				<!--マイコンテンツ-->
				<img src="/images/m/sp5.gif" ><br>
				<div style="background-color:#dddddd;font-size:xx-small;">
					<s:link href="entry">ｽｹｼﾞｭｰﾙ入力</s:link><br>
					<s:link href="before/${calendarDay}" accesskey="1">[1]先週</s:link>&nbsp;&nbsp;<s:link href="next/${calendarDay}" accesskey="3">[3]翌週</s:link><br>
					<s:link href="before2/${calendarDay}" accesskey="4">[4]先々週</s:link>&nbsp;&nbsp;<s:link href="next2/${calendarDay}" accesskey="6">[6]翌々週</s:link><br>
					<s:link href="/m/schedule" accesskey="5">[5]今週</s:link>
				</div>
				<!--/マイコンテンツ-->
				<img src="/images/m/sp5.gif" ><br>
				<!--同志最新日記-->
				<c:forEach var="e" items="${schedule}" varStatus="i">
					<div style="background-color:${e['color']};font-size:xx-small;">
						<s:link href="daylist/${e['scheduledate']}">【<fmt:formatDate value="${f:date(e['scheduledate'],'yyyyMMdd')}" type="DATE" dateStyle="FULL"/><fmt:formatDate value="${f:date(e['scheduledate'],'yyyyMMdd')}" pattern="(E)"/>】</s:link><br/>
						<c:if test="${e['existsSchedule'] eq 1}">
							<c:forEach var="s" items="${e['schedule']}" varStatus="status">
								<c:set var="scheduledate" value=""/>
								<div style="color:#${s.titlecolor};">
								<c:choose>
									<c:when test="${s.entry eq 1}">
										<c:choose>
										<c:when test="${s.starttime == null and s.endtime == null}">・【ｲﾍﾞﾝﾄ】
										</c:when>
										<c:when test="${s.starttime == null and s.endtime != null}">
										・【ｲﾍﾞﾝﾄ】～${s.endtime}
										</c:when>
										<c:otherwise>
										・【ｲﾍﾞﾝﾄ】${s.starttime}${s.endtime}
										</c:otherwise>
										</c:choose>
										<span style="font-size:xx-small;">${f:h(s.title)}</span><br>
									</c:when>
									<c:when test="${s.entry eq 2}">
										<c:choose>
										<c:when test="${s.starttime == null and s.endtime == null}">・【ｽｹｼﾞｭｰﾙ】
										</c:when>
										<c:when test="${s.starttime == null and s.endtime != null}">
										・【ｽｹｼﾞｭｰﾙ】～${s.endtime}
										</c:when>
										<c:otherwise>
										・【ｽｹｼﾞｭｰﾙ】${s.starttime}${s.endtime}
										</c:otherwise>
										</c:choose>
										<span style="font-size:xx-small;color:#${s.titlecolor};">${s.title}</span><br>
									</c:when>
								</c:choose>
								</div>
							</c:forEach>
						</c:if>
						
					</div>
				</c:forEach>
				<img src="/images/m/sp5.gif" ><br>
				<div style="background-color:#dddddd;font-size:xx-small;">
					<s:link href="before/${calendarDay}" accesskey="1">[1]先週</s:link>&nbsp;&nbsp;<s:link href="next/${calendarDay}" accesskey="3">[3]翌週</s:link><br>
					<s:link href="before2/${calendarDay}" accesskey="4">[4]先々週</s:link>&nbsp;&nbsp;<s:link href="next2/${calendarDay}" accesskey="6">[6]翌々週</s:link><br>
					<s:link href="/m/schedule" accesskey="5">[5]今週</s:link>
				</div><br/>
					<input type="submit" name="setting1" value="全てのｽｹｼﾞｭｰﾙを表示"/><br/>
					<input type="submit" name="setting2" value="共有中のｽｹｼﾞｭｰﾙのみ表示"/>
				<img src="/images/m/sp5.gif" ><br>
				<div style="background-color:#dddddd; text-align:center;">
					<span style="font-size:xx-small;"><s:link href="/m/top" accesskey="0">[0]ﾏｲﾄｯﾌﾟ</s:link></span>
				</div>
				<%@ include file="/WEB-INF/view/m/fmenu.jsp"%>
				<!--footer-->
				<div style="text-align:right;"><a href="#ptop" name="pbtm" id="pbtm" accesskey="2"><span style="font-size:xx-small;">[2]▲</span></a></div>
				<img src="/images/m/sp5.gif" ><br>
				<%@ include file="/WEB-INF/view/m/footer.jsp"%>
				<!--/footer-->
			<!--コンテンツ終了-->
		</div>
	</div>
</s:form>
</body>
</html>