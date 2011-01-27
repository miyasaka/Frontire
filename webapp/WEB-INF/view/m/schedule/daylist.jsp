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
				<div style="background-color:#dddddd;">
					<s:link href="entry2/${calendarDay}">ｽｹｼﾞｭｰﾙ入力</s:link><br>
					<s:link href="beforeday/${calendarDay}" accesskey="4">[4]前日</s:link>&nbsp;&nbsp;<s:link href="nextday/${calendarDay}" accesskey="6">[6]翌日</s:link><br>
				</div>


				<!--/マイコンテンツ-->
				<img src="/images/m/sp5.gif" ><br>
				<!--同志最新日記-->
				<div style="background-color:#dddddd;">
						<span style="font-size:xx-small;">【<fmt:formatDate value="${f:date(f:h(calendarDay),'yyyyMMdd')}" pattern="yyyy年M月d日(E)" />】</span><br>
						<c:choose>
						<c:when test="${fn:length(DayResults)>0}">
						<c:forEach items="${DayResults}" var="s" varStatus="status">
							<div style="color:#${s.titlecolor};">
								<c:choose>
									<c:when test="${s.entry eq 1}">
										<c:choose>
											<c:when test="${s.starttime == null and s.endtime == null}"></c:when>
											<c:when test="${s.starttime == null and s.endtime != null}">・【ｲﾍﾞﾝﾄ】～${s.endtime}</c:when>
											<c:otherwise>・【ｲﾍﾞﾝﾄ】${s.starttime}${s.endtime}</c:otherwise>
										</c:choose>
										<c:if test="${s.starttime == null and s.endtime == null}">・【ｲﾍﾞﾝﾄ】</c:if><span style="font-size:xx-small;">${f:h(s.title)}</span><br>
									</c:when>
									<c:when test="${s.entry eq 2}">
										<s:link href="/m/schedule/view/${s.sno}/${s.cid}" style="color:#${s.titlecolor};"><span style="font-size:xx-small;color:#${s.titlecolor};">
										<c:choose>
											<c:when test="${s.starttime == null and s.endtime == null}"></c:when>
											<c:when test="${s.starttime == null and s.endtime != null}">・【ｽｹｼﾞｭｰﾙ】～${s.endtime}</c:when>
											<c:otherwise>・【ｽｹｼﾞｭｰﾙ】${s.starttime}${s.endtime}</c:otherwise>
										</c:choose>
										<c:if test="${s.starttime == null and s.endtime == null}">・【ｽｹｼﾞｭｰﾙ】</c:if>${s.title}</span><br>
										</s:link>
									</c:when>
								</c:choose>
							</div>
						</c:forEach>
						</c:when>
						<c:otherwise>何もありません</c:otherwise>
						</c:choose>
				</div>
				<img src="/images/m/sp5.gif" ><br>
				<div style="background-color:#dddddd;">
					<s:link href="beforeday/${calendarDay}" accesskey="4">[4]前日</s:link>&nbsp;&nbsp;<s:link href="nextday/${calendarDay}" accesskey="6">[6]翌日</s:link><br>
					<s:link href="/m/schedule/${f:h(calendarDay)}">ｽｹｼﾞｭｰﾙへ戻る</s:link>
				</div>
				<img src="/images/m/sp5.gif" ><br>
				<div style="background-color:#dddddd; text-align:center;">
					<span style="font-size:xx-small;"><s:link href="/m/top" accesskey="0">[0]ﾏｲﾄｯﾌﾟ</s:link></span>
				</div>
				<img src="/images/m/sp5.gif" ><br>
				<div style="text-align:left;">
					<span style="font-size:xx-small;">◆ｻﾎﾟｰﾄﾒﾆｭｰ</span><br>
					<!--├<a href="csns01.html">ﾛｸﾞｱｳﾄ</a><br>-->
					├<s:link href="/m/schedule"><span style="font-size:xx-small;">ﾏｲｽｹｼﾞｭｰﾙへ</span></s:link><br>
					└<s:link href="/m/logout"><span style="font-size:xx-small;">ﾛｸﾞｱｳﾄ</span></s:link><br>
				</div>
				<hr style="border-color:#bdbdbd; background-color:#bdbdbd;">
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