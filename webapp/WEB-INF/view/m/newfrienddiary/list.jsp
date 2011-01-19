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
<title>[frontier]ｸﾞﾙｰﾌﾟ最新日記</title>
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
<form action="csns02.html" method="post">
<%-- ロケール情報を設定 --%>
<fmt:setLocale value="ja-JP" />
	<div>
		<div style="font-size:xx-small;">
		
			<!--header-->
			<div style="background-color:#afafaf;">
				&nbsp;<span style="color:#0000ff; font-size:xx-small;">●</span>&nbsp;<span style="font-size:xx-small;">ｸﾞﾙｰﾌﾟ最新日記</span>
			</div>
			<div style="text-align:right;"><a href="#pbtm" name="ptop" id="ptop" accesskey="8"><span style="font-size:xx-small;">[8]▼</span></a></div>
			<!--/header-->
		
		
			<!--コンテンツ開始-->
				
				
				
				<!--最新日記一覧-->
				<img src="/images/m/sp5.gif" /><br />
				<div style="background-color:#4a4a4a;">
					<span style="color:#ffffff; font-size:xx-small;">&nbsp;ｸﾞﾙｰﾌﾟ最新日記</span>
				</div>
				<div>
					<img src="/images/m/sp5.gif" /><br />
					<c:forEach var="e" items="${results}" varStatus="status">
					<div>
						<span style="font-size:xx-small;">
							[<fmt:formatDate value="${f:date(f:h(e.LYyyymmdd),'yyyyMMddHHmmss')}" pattern="yyyy/MM/dd HH:mm" />]&nbsp;${f:h(e.nickname)}<br />
							<s:link href="/m/diary/view/${f:h(e.diaryid)}/${f:h(e.yyyymmdd)}/${f:h(e.mid)}">${f:h(e.title)}<c:if test="${e.count>0}">(${e.count})</c:if></s:link><br />
						</span>
					</div>
					<hr style="border-color:#bdbdbd; background-color:#bdbdbd;"/>
					</c:forEach>


				</div>
				<!--/最新日記一覧-->

				<div style="background-color:#dddddd; text-align:center;">
					<s:link href="/m/top" accesskey="0"><span style="font-size:xx-small;">[0]ﾏｲﾄｯﾌﾟ</span></s:link>
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
</form>
</body>
</html>