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
	<title>[frontier]ｽｹｼﾞｭｰﾙ削除確認</title>
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
	<div>
		<div style="font-size:xx-small;">
			<!--header-->
			<div style="background-color:#afafaf;">
				&nbsp;<span style="color:#debe71; font-size:xx-small">●</span>&nbsp;<span style="font-size:xx-small;">ｽｹｼﾞｭｰﾙ削除確認</span>
			</div>
			<div style="text-align:right;"><a href="#pbtm" name="ptop" id="ptop" accesskey="8"><span style="font-size:xx-small;">[8]▼</span></a></div><img src="/images/m/sp5.gif" ><br>
			<!--/header-->
			<!--コンテンツ開始-->
				<div style="background-color:#afafaf;">下記の予定を削除します。よろしいですか？</div>
				<img src="/images/m/sp5.gif" ><br>
				<span style="font-size:xx-small;">■登録者</span><br>
				<span style="font-size:xx-small;">${f:h(nickname)}</span><br><br>
				<span style="font-size:xx-small;">■タイトル</span><br>
				<span style="font-size:xx-small;">${title}</span><br><br>
				<span style="font-size:xx-small;">■詳細</span><br>
				<span style="font-size:xx-small;">${f:br(detail)}</span><br><br>
				<span style="font-size:xx-small;">■日付</span><br>
				<span style="font-size:xx-small;">${startyear}年${startmonth}月${startday}日</span><br><br>
				<span style="font-size:xx-small;">■時間</span><br>
				<span style="font-size:xx-small;">${starttime}時${startminute}分 ～ ${endtime}時${endminute}分</span><br><br>
				<span style="font-size:xx-small;">■公開範囲</span><br>
				<span style="font-size:xx-small;">
					<c:choose>
						<c:when test="${publevel eq 1}">全体に公開</c:when>
						<c:when test="${publevel eq 2}">グループに公開</c:when>
						<c:when test="${publevel eq 9}">非公開</c:when>
					</c:choose>
				</span><br>
					<c:if test="${ShareUserCnt>0&&publevel ne 9}"><br>
					<span style="font-size:xx-small;">■共有人数</span><br>
					<span style="font-size:xx-small;">${ShareUserCnt}人</span><br><br>
					<span style="font-size:xx-small;">■共有ユーザー</span><br>
					<span style="font-size:xx-small;">
						<c:forEach var="e" items="${ShareUser}" varStatus="i">
							${e.nickname}<c:if test="${i.index+1!=ShareUserCnt}">、</c:if>
						</c:forEach>
					</span><br><br>
					</c:if>
				<input type="submit" name="delSchedule" value="削除"/>&nbsp;&nbsp;<input type="submit" name="editcansel" value="戻る"/><br>
				
				<hr style="border-color:#bdbdbd; background-color:#bdbdbd;">
				
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