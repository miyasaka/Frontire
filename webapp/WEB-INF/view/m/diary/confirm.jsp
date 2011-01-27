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
	<title>[frontier]ｺﾒﾝﾄ削除確認</title>
</head>
<body style="width:350px;">
<s:form>
	<div>
		<div style="font-size:small;">
			<!--header-->
			<div style="background-color:#afafaf;">
				&nbsp;<img src="/images/m/star_yellow.gif" ><span style="font-size:small;">&nbsp;ｺﾒﾝﾄ削除確認</span>
			</div>
			<div style="text-align:right;"><a href="#pbtm" name="ptop" id="ptop" accesskey="8"><span style="font-size:small;">[8]▼</span></a></div><img src="/images/m/sp5.gif" ><br>
			<!--/header-->
			<!--コンテンツ開始-->
				<div style="background-color:#afafaf;">下記のｺﾒﾝﾄを削除します。よろしいですか？</div>
				<img src="/images/m/sp5.gif" ><br>
				<div>
					<c:forEach var="e" items="${results}" varStatus="status">
					<c:choose>
						<c:when test="${(status.count % 2) eq 1}"><c:set var="color" value="#e5e5e5;" /></c:when>
						<c:otherwise><c:set var="color" value="#f0f0f0;" /></c:otherwise>
					</c:choose>
					<div style="background-color:${color}">
						<div>
							<span style="font-size:x-small;">
								No.<fmt:formatNumber value="${e.comno}" minIntegerDigits="3"/><br>
								<a href="#">${f:h(e.nickname)}</a>
							</span>
						</div>
						<div>
							<span style="font-size:x-small;">
								${f:br(e.viewComment)}
							</span>
						</div>
					</div>
					</c:forEach>
				</div>
				<!--/コメント-->
				<hr style="border-color:#7d7d7d; background-color:#7d7d7d;">
				<div style="text-align:center;">
				<input type="submit" name="exeDelete" value="削除する" /><html:hidden property="diaryId" value="${diaryId}"/><html:hidden property="checkCommentNo" value="${checkCommentNo}"/><html:hidden property="mid" value="${mid}"/>&nbsp;&nbsp;<input type="submit" name="stop" value="やめる" /><br>
				</div>
				
				<hr style="border-color:#bdbdbd; background-color:#bdbdbd;">
				
				<div style="background-color:#dddddd; text-align:center;">
					<s:link href="/m/top" accesskey="0"><span style="font-size:x-small;">[0]ﾏｲﾄｯﾌﾟ</span></s:link>
				</div>
				<%@ include file="/WEB-INF/view/m/fmenu.jsp"%>
				<!--footer-->
				<div style="text-align:right;"><a href="#ptop" name="pbtm" id="pbtm" accesskey="2"><span style="font-size:small;">[2]▲</span></a></div>
				<img src="/images/m/sp5.gif" ><br>
				<%@ include file="/WEB-INF/view/m/footer.jsp"%>
				<!--/footer-->
			<!--コンテンツ終了-->
		</div>
	</div>
</s:form>
</body>
</html>