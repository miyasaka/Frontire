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
<title>[frontier]日記一覧</title>
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
<%-- ロケール情報を設定 --%>
<fmt:setLocale value="ja-JP" />
<s:form>
	<div>
		<div>
		
			<!--header-->
			<div style="background-color:#afafaf;">
				&nbsp;<span style="color:#0000ff; font-size:xx-small;">●</span>&nbsp;<span style="font-size:xx-small;">日記一覧</span>
			</div>
			<div style="text-align:right;"><a href="#pbtm" name="ptop" id="ptop" accesskey="8"><span style="font-size:xx-small;">[8]▼</span></a></div>
			<!--/header-->
		
		
			<!--コンテンツ開始-->
				<!--日記一覧-->
				<div style="background-color:#dddddd; text-align:center;">
					<s:link href="/m/top" accesskey="0"><span style="font-size:xx-small;">[0]ﾏｲﾄｯﾌﾟ</span></s:link>
					<c:if test="${offset>0}">
						<s:link href="/m/diary/prepg" accesskey="4"><span style="font-size:xx-small;">[4]前の${appDefDto.FP_MY_M_DIARYLIST_PGMAX}件</span></s:link>
					</c:if>
					<c:if test="${resultscnt>(offset + fn:length(results))}">
						<s:link href="/m/diary/nxtpg" accesskey="6"><span style="font-size:xx-small;">[6]次の${appDefDto.FP_MY_M_DIARYLIST_PGMAX}件</span></s:link>
					</c:if>
				</div>
				<img src="/images/m/sp5.gif" /><br />
				<div style="background-color:#4a4a4a;">
					<span style="color:#ffffff; font-size:xx-small;">&nbsp;${f:h(vNickname)}<c:if test="${!vUser}">さん</c:if>の日記</span>
				</div>
				<div>
					<c:if test="${resultscnt==0}">
					<img src="/images/m/sp5.gif" /><br />
					<span style="font-size:xx-small">日記はまだありません。</span>
					<img src="/images/m/sp5.gif" /><br />
					</c:if>
					<c:if test="${resultscnt!=0}">
					<img src="/images/m/sp5.gif" /><br />
					<c:forEach var="e" items="${results}">
					<div>
						<span style="font-size:xx-small;"><fmt:formatDate value="${f:date(f:h(e.entdate),'yyyyMMddHHmm')}" pattern="yyyy/MM/dd HH:mm" /><%--<c:if test="${vUser}"><s:link href="/m/entdiary/edit?diaryid=${f:h(e.diaryid)}">【編集】</s:link></c:if>--%></span><br />
						<s:link href="/m/diary/view/${f:h(e.diaryid)}/${fn:substring(f:h(e.entdate),0,8)}/${f:h(mid)}"><span style="font-size:xx-small;">${f:h(e.title) }<c:if test="${userInfoDto.membertype eq '0'}">(${f:h(e.cnt) })</c:if></span></s:link>
					</div>
					<hr style="border-color:#bdbdbd; background-color:#bdbdbd;"/>
					</c:forEach>
					</c:if>
				</div>
				<!--/日記一覧-->

				<div style="background-color:#dddddd; text-align:center;">
					<s:link href="/m/top" accesskey="0"><span style="font-size:xx-small;">[0]ﾏｲﾄｯﾌﾟ</span></s:link>
					<c:if test="${offset>0}">
						<s:link href="/m/diary/prepg" accesskey="4"><span style="font-size:xx-small;">[4]前の${appDefDto.FP_MY_M_DIARYLIST_PGMAX}件</span></s:link>
					</c:if>
					<c:if test="${resultscnt>(offset + fn:length(results))}">
						<s:link href="/m/diary/nxtpg" accesskey="6"><span style="font-size:xx-small;">[6]次の${appDefDto.FP_MY_M_DIARYLIST_PGMAX}件</span></s:link>
					</c:if>
				</div>
				<%@ include file="/WEB-INF/view/m/fmenu.jsp"%>
				<!--footer-->
				<div style="text-align:right;"><a href="#ptop" name="pbtm" id="pbtm" accesskey="2"><span style="font-size:xx-small;">[2]▲</span></a></div>
				<img src="/images/m/sp5.gif" /><br />
				<%@ include file="/WEB-INF/view/m/footer.jsp"%>
				<!--/footer-->
			<!--コンテンツ終了-->
		</div>
	</div>
</s:form>
</body>
</html>