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
	<title>[frontier]ｽｹｼﾞｭｰﾙ${f:h(vMode)}</title>
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
<!-- 時間_時設定 -->
<c:set var="hourList" value="--,00,01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23"/>

<!-- 時間_分設定 -->
<c:set var="minList" value="--,00,30"/>
<s:form>
	<div>
		<div style="font-size:xx-small;">
			<!--header-->
			<div style="background-color:#afafaf;">
				&nbsp;<span style="color:#debe71; font-size:xx-small">●</span>&nbsp;<span style="font-size:xx-small;">ｽｹｼﾞｭｰﾙ${f:h(vMode)}</span>
			</div>
			<div style="text-align:right;"><a href="#pbtm" name="ptop" id="ptop" accesskey="8"><span style="font-size:xx-small;">[8]▼</span></a></div><img src="/images/m/sp5.gif" ><br>
			<div style="font-size:xx-small;color:#ff0000;"><html:errors/></div>
			<!--/header-->
			<!--コンテンツ開始-->
				<span style="font-size:xx-small;">■タイトル</span><span style="color:#ff0000;font-size:xx-small;">※</span><br>
				<input type="text" style="width:236px;" value="${f:h(title)}" name="title"/><br><br>
				<span style="font-size:xx-small;">■詳細</span><br>
				<textarea name="detail" rows="8" style="width:100%;">${f:h(detail)}</textarea><br><br>
				<span style="font-size:xx-small;">■日付</span><br>
				<select name="startyear">
					<c:forEach begin="${appDefDto.FP_MY_CALENDAR_START_PGMAX}" end="${appDefDto.FP_MY_CALENDAR_END_PGMAX}" var="i">
						<option value="${i}" <c:if test="${startyear==i}"> selected</c:if>>${i}</option>
					</c:forEach>
				</select>年
				<select name="startmonth">
					<c:forEach begin="1" end="9" var="i">
						<option value="0${i}" <c:if test="${startmonth==i}"> selected</c:if>>0${i}</option>
					</c:forEach>
					<c:forEach begin="10" end="12" var="i">
						<option value="${i}" <c:if test="${startmonth==i}"> selected</c:if>>${i}</option>
					</c:forEach>
				</select>月
				<select name="startday">
					<c:forEach begin="1" end="9" var="i">
						<option value="0${i}" <c:if test="${startday==i}"> selected</c:if>>0${i}</option>
					</c:forEach>
					<c:forEach begin="10" end="31" var="i">
						<option value="${i}" <c:if test="${startday==i}"> selected</c:if>><c:if test="${i<10}">0</c:if>${i}</option>
					</c:forEach>
				</select>日<br><br>

				<span style="font-size:xx-small;">■時間の指定</span><br>
				<input type="checkbox" name="chk01" ${(chk01 eq 'on'||(starttime=='--'&&startminute=='--'&&endtime=='--'&&endminute=='--'))?'checked':''}/>指定しない<br><br>
				
				<span style="font-size:xx-small;">■開始時間：</span><br>
				<select name="starttime"><span style="font-size:xx-small;">	<c:forEach var="e" items="${hourList}" varStatus="i">
						<option value="${e}" ${starttime==e?'selected':(f:h(vMode) eq '追加'&&e==13)?'selected':''}>${e}</option>
					</c:forEach>
				</select>時
				<select name="startminute">
					<c:forEach var="e" items="${minList}" varStatus="i">
						<option value="${e}" ${startminute==e?'selected':(f:h(vMode) eq '追加'&&e=='00')?'selected':''}>${e}</option>
					</c:forEach>
				</select>分<br><br>
				
				<span style="font-size:xx-small;">■終了時間：</span><br>
				<select name="endtime">
					<c:forEach var="e" items="${hourList}" varStatus="i">
						<option value="${e}" ${endtime==e?'selected':(f:h(vMode) eq '追加'&&e==13)?'selected':''}>${e}</option>
					</c:forEach>
				</select>時
				<select name="endminute">
					<c:forEach var="e" items="${minList}" varStatus="i">
						<option value="${e}" ${endminute==e?'selected':(f:h(vMode) eq '追加'&&e=='00')?'selected':''}>${e}</option>
					</c:forEach>
				</select>分<br><br>
				
				<span style="font-size:xx-small;">■公開範囲</span><br>
				<c:choose>
				<c:when test="${pageid eq null}">
				<select name="pubLevel" style="width:140px;">
				<option value="1" ${publevel eq '1'?'selected':''}>全体に公開</option>
				<option value="2" ${publevel eq '2'?'selected':''}>グループに公開</option>
				<option value="9" ${publevel eq '9'?'selected':''}>非公開</option>
				</select>
				</c:when>
				
				<c:when test="${friendstatus eq '0'}">
				<select name="publevel" style="width:140px;">
				<option value="1" ${publevel eq '1'?'selected':''}>全体に公開</option>
				<option value="2" ${publevel eq '2'?'selected':''}>グループに公開</option>
				<option value="9" ${publevel eq '9'?'selected':''}>非公開</option>
				</select>
				</c:when>
				<c:otherwise>
				${publevel eq 1?'全体に公開':(publevel eq 2?'グループに公開':'非公開')}
				</c:otherwise>
				</c:choose><br><br>
<c:choose>
<c:when test="${pageid eq null}">
	<input type="submit" name="add" value="登録"/>&nbsp;&nbsp;<input type="submit" name="inputcansel${status}" value="戻る"/><br>
</c:when>
<c:when test="${pageid eq 'edit'}">
	<input type="submit" name="editSchedule" value="編集"/>&nbsp;&nbsp;<input type="submit" name="editcansel" value="戻る"/><br>
</c:when>
</c:choose>

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