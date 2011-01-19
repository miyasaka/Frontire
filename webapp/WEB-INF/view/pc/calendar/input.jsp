<%@ page language="java" contentType="text/html; charset=windows-31J"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<title>[frontier]ｽｹｼﾞｭｰﾙの${f:h(vMode)}</title>
<link rel="stylesheet" href="/static/style/${f:h(appDefDto.FP_CMN_COLOR_TYPE)}/css/assort.css" type="text/css" />

<script language="javascript" type="text/javascript" src="/static/js/jquery.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/calendar.js"></script>

</head>

<body>

<!-- カラー設定 -->
<c:set var="colorList" value="000000,008000,FF00FF,FF0000,800080,006400,800000,00FA9A,0000CD,CCCCCC,99CCFF,FFCCFF"/>
<!-- 時間_時設定 -->
<c:set var="hourList" value="--,00,01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23"/>

<!-- 時間_分設定 -->
<c:set var="minList" value="--,00,30"/>

<div class="sp_main">
	<div align="center">
		<div class="pSchedule">
			<div class="pSchedule-body">
			<html:errors/>
			<!--メイン-->
			<div class="pSchedule-main">


				<!--listBox11-->
				<div class="listBox11Title">
						<table class="ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name inputColStick">ｽｹｼﾞｭｰﾙの${f:h(vMode)}</div></td>
							</tr>
						</table>
				</div>

<s:form>
				<table cellspacing="0" width="100%;" style="border-collapse:collapse;">

					<c:if test="${pageid eq 'edit'}">
						<tr><td class="left" valign="top">登録者</td><td class="right" valign="top">${f:h(nickname)}</td></tr>
					</c:if>

					<tr><td class="left" valign="top">タイトル<font color="red">※</font><br>(全角100文字以内)</td><td class="right title" valign="top"><input type="text" value="${f:h(title)}" name="title"/></td></tr>
					<tr><td class="left" valign="top">タイトル色</td>
						<td class="right color" valign="top">

						<c:forEach var="e" items="${colorList}" varStatus="i">
							<c:if test="${i.index==6}"><br/></c:if>
							<input type="radio" name="titlecolor" value="${e}" ${e==titlecolor?'checked':(i.index==0?'checked':'')}></input>
							<FONT COLOR=#${e}>■</FONT>&nbsp;&nbsp;
						</c:forEach>

						</td>
					</tr>
					<tr><td class="left" valign="top">詳細<br>(全角10000文字以内)</td><td class="right detail" valign="top"><textarea name="detail" rows="10">${f:h(detail)}</textarea></td></tr>
					<tr><td class="left" valign="top">日付</td><td class="right date" valign="top">
<select name="startyear" style="width:5em;">
	<c:forEach begin="${appDefDto.FP_MY_CALENDAR_START_PGMAX}" end="${appDefDto.FP_MY_CALENDAR_END_PGMAX}" var="i">
		<option value="${i}" <c:if test="${startyear==i}"> selected</c:if>>${i}</option>
	</c:forEach>
</select>
年
<select name="startmonth">
	<c:forEach begin="1" end="9" var="i">
		<c:set var="iyear" value="0${i}"/>
		<option value="${iyear}" <c:if test="${startmonth==iyear}"> selected</c:if>>${iyear}</option>
	</c:forEach>
	<c:forEach begin="10" end="12" var="i">
		<option value="${i}" <c:if test="${startmonth==i}"> selected</c:if>>${i}</option>
	</c:forEach>
</select>月
<select name="startday">
	<c:forEach begin="1" end="9" var="i">
		<c:set var="imonth" value="0${i}"/>
		<option value="${imonth}" <c:if test="${startday==imonth}"> selected</c:if>>${imonth}</option>
	</c:forEach>
	<c:forEach begin="10" end="31" var="i">
		<option value="${i}" <c:if test="${startday==i}"> selected</c:if>><c:if test="${i<10}">0</c:if>${i}</option>
	</c:forEach>
</select>日
</td></tr>
					<tr><td class="left" valign="top">時間</td><td class="right hour" valign="top">
<select name="starttime" id="end_hour">
	<c:forEach var="e" items="${hourList}" varStatus="i">
		<option value="${e}" ${starttime==e?'selected':(f:h(vMode) eq '追加'&&e==13)?'selected':''}>${e}</option>
	</c:forEach>
</select>時

<select name="startminute" id="end_minute">
	<c:forEach var="e" items="${minList}" varStatus="i">
		<option value="${e}" ${startminute==e?'selected':(f:h(vMode) eq '追加'&&e=='00')?'selected':''}>${e}</option>
	</c:forEach>
</select>分
　～　
<select name="endtime" id="end_hour">
	<c:forEach var="e" items="${hourList}" varStatus="i">
		<option value="${e}" ${endtime==e?'selected':(f:h(vMode) eq '追加'&&e==13)?'selected':''}>${e}</option>
	</c:forEach>
</select>時
<select name="endminute" id="end_minute">
	<c:forEach var="e" items="${minList}" varStatus="i">
		<option value="${e}" ${endminute==e?'selected':(f:h(vMode) eq '追加'&&e=='00')?'selected':''}>${e}</option>
	</c:forEach>
</select>分<br/>
<input type="checkbox" onClick="setDisable(this);" id="chk01" name="chk01" ${chk01 eq 'on'?'checked':''}/>時間を指定しない
<script>if("${starttime}"=='--'&&"${startminute}"=='--'&&"${endtime}"=='--'&&"${endminute}"=='--') document.getElementById('chk01').click();</script>
</td></tr>
					<tr><td class="left" valign="top">公開範囲</td>
<td class="right" valign="top">

<c:choose>

<c:when test="${pageid eq null}">
<select id="selectBox01" name="pubLevel" style="width:140px;" onChange="chgShareInfoArea(this);">
<option value="1" ${publevel eq '1'?'selected':''}>全体に公開</option>
<option value="2" ${publevel eq '2'?'selected':''}>グループに公開</option>
<option value="9" ${publevel eq '9'?'selected':''}>非公開</option>
</select>
</c:when>

<c:when test="${friendstatus eq '0'}">
<select id="selectBox01" name="publevel" style="width:140px;" onChange="chgShareInfoArea(this);">
<option value="1" ${publevel eq '1'?'selected':''}>全体に公開</option>
<option value="2" ${publevel eq '2'?'selected':''}>グループに公開</option>
<option value="9" ${publevel eq '9'?'selected':''}>非公開</option>
</select>
</c:when>
<c:otherwise>
${publevel eq 1?'全体に公開':(publevel eq 2?'グループに公開':'非公開')}
</c:otherwise>
</c:choose>
</td>
</tr>

<c:choose>
<c:when test="${ShareStatus eq 0&&(fn:length(ShareList)>0||ShareUserCnt>0)}">

<tr id="shareCnt">
	<td class="left" valign="top">共有人数</td><td class="right" valign="top"><span id="userCnt">${ShareUserCnt}</span>人</td>
</tr>
<tr id ="shareUser">
	<td class="left" valign="top">共有ユーザー<br/><br/>※Ctrlキーを押しながら選択する事で、複数選択する事が出来ます。</td>
	<td class="right date">
		<div style="width:250px;float:left;"><input type="button" onclick="allSelect(document.getElementById('userList'));" value="全て選択"/>
		※選択リスト<select id="userList" size="10" multiple=true class="selectListBox">
			<c:forEach var="e" items="${ShareList}" varStatus="i">
				<option value="${e.mid}">${e.nickname}</option>
			</c:forEach>
		</select>
		</div>
		<div style="float:left;margin:60px 10px 0 10px;"><u onclick="moveUser(document.getElementById('userList'),document.getElementById('addList'));">&gt;&gt;</u><br/><br/><br/><u onclick="moveUser(document.getElementById('addList'),document.getElementById('userList'));">&lt;&lt;</u></div>
		<div style="width:250px;float:left;"><input type="button" onclick="allSelect(document.getElementById('addList'));" value="全て選択"/>
		※共有者<select id="addList" size="10" multiple=true class="selectListBox">
			<c:forEach var="e" items="${ShareUser}" varStatus="i">
				<option value="${e.joinmid}">${e.nickname}</option>
			</c:forEach>
		</select>
		</div>

</td>
</tr>
<script>chgShareInfoArea(document.getElementById('selectBox01'));</script>
</c:when>
<c:otherwise>
<c:if test="${ShareViewFlg&&ShareUserCnt>0}">
<tr>
	<td class="left" valign="top">共有人数</td><td class="right" valign="top"><span id="userCnt">${ShareUserCnt}</span>人</td>
</tr>
<tr>
	<td class="left" valign="top">共有ユーザー</td>
	<td class="right" valign="top">
		<span>
			<c:forEach var="e" items="${ShareUser}" varStatus="i">
				${e.nickname}<c:if test="${i.index+1!=ShareUserCnt}">、</c:if>
			</c:forEach>
		</span>
	</td>
</tr>
</c:if>
</c:otherwise>
</c:choose>

					
				</table>
				<div class="pSchedule-bottom">

<c:choose>
<c:when test="${pageid eq null}">
<input type="submit" name="add" onclick="setListMid(document.getElementById('addList'),document.getElementById('userList'));" value="スケジュールを追加する"/><html:hidden property="status" value="${status}"/>
</c:when>
<c:when test="${pageid eq 'edit'}">
					<input type="submit" onclick="setListMid(document.getElementById('addList'),document.getElementById('userList'));" name="editSchedule" value="ｽｹｼﾞｭｰﾙを編集する"></input><span class="sp">&nbsp;</span>
					<input type="submit" name="cansel" value="やめる"></input>
</c:when>
</c:choose>
<html:hidden value="${cid}" property="cid"/>
<html:hidden value="${sno}" property="sno"/>
<html:hidden value="${userInfoDto.memberId}" property="uid"/>
<html:hidden value="" property="ShareUserIds" styleId="shareUserIds"/>
<html:hidden value="" property="ShareUserNames" styleId="shareUserNames"/>
<html:hidden value="" property="ShareListIds" styleId="shareListIds"/>
<html:hidden value="" property="ShareListNames" styleId="shareListNames"/>
<html:hidden value="${ShareViewFlg}" property="ShareViewFlg"/>


</div>
				</div>
</s:form>
				<div class="close">
					<a href="javascript:window.close()">このウィンドウを閉じる</a>
				</div>

			</div>
			<!--/pSchedule-main-->

</div>
		</div>

	</div>
<!-- フッター -->
<%@ include file="/WEB-INF/view/pc/fnfooter.jsp"%>
<!-- フッター -->

</body>
</html>
    