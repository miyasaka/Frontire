<%@ page language="java" contentType="text/html; charset=windows-31J"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS"/>
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta name="robots" content="nofollow,noindex"     />



<title>[frontier] スケジュールの閲覧</title>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<meta name="Slurp" content="NOYDIR" />
<meta name="robots" content="noodp" />
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/assort.css" type="text/css" />

</head>

<body>
	<!--/navbar-->
	<div class="sp_main">
	<div align="center">
		<div class="pSchedule">
			<!--メイン-->
			<div class="pSchedule-main">
				<!--listBox11-->
				<div class="listBox11Title">
						<table class="ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name myColStick">ｽｹｼﾞｭｰﾙの閲覧</div></td>
							</tr>
						</table>
				</div>

				
				<table cellspacing="0" width="100%;" style="border-collapse:collapse;">
					<tr><td class="left" valign="top">登録者</td><td class="right">${f:h(nickname)}</td></tr>
					<tr><td class="left" valign="top">タイトル</td><td class="right">${f:h(title)}</td></tr>
					<tr><td class="left" valign="top">詳細</td><td class="right">${f:br(f:h(detail))}</td></tr>
					<tr><td class="left" valign="top">日付</td><td class="right">${startyear}年${startmonth}月${startday}日 </td></tr>
					<tr><td class="left" valign="top">時間</td><td class="right">${starttime}時${startminute}分 ～ ${endtime}時${endminute}分</td></tr>
					<tr><td class="left" valign="top">公開範囲</td><td class="right">
						<c:choose>
							<c:when test="${publevel eq 1}">全体に公開</c:when>
							<c:when test="${publevel eq 2}">グループに公開</c:when>
							<c:when test="${publevel eq 9}">非公開</c:when>
						</c:choose></td></tr>
					<c:if test="${ShareUserCnt>0&&publevel ne 9}">
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
				</table>
<c:if test="${friendstatus eq 0 or publevel eq 1}">
				<div class="pSchedule-bottom">
					<s:form><html:hidden property="sno" value="${sno}"/><html:hidden property="cid" value="${cid}"/>
						<c:if test="${ShareStatus eq 2}"><input type="submit" name="shareSchedule" value="ｽｹｼﾞｭｰﾙを共有する" class="formBt01" /></c:if>
						<c:if test="${ShareStatus ne 2}"><input type="submit" name="edit" value="編集する" class="formBt01" /></c:if>
						<c:if test="${ShareStatus eq 1}"><span class="sp">&nbsp;</span><input type="submit" name="shareCansel" value="共有をやめる" class="formBt01" /></c:if>
					<c:if test="${friendstatus eq 0}">
					<span class="sp">&nbsp;</span><input type="submit" name="delete" value="削除する" class="formBt02" />
					</c:if></s:form>
				</div>
</c:if>
<c:if test="${friendstatus eq 1 and publevel eq 2}">
				<div class="pSchedule-bottom">
					<s:form><html:hidden property="sno" value="${sno}"/><html:hidden property="cid" value="${cid}"/>
						<c:if test="${ShareStatus eq 2}"><input type="submit" name="shareSchedule" value="ｽｹｼﾞｭｰﾙを共有する" class="formBt01" /></c:if>
						<c:if test="${ShareStatus eq 1}"><span class="sp">&nbsp;</span><input type="submit" name="shareCansel" value="共有をやめる" class="formBt01" /></c:if>
					</s:form>
				</div>
</c:if>
			</div>

			<!--/pSchedule-main-->
			<div class="close">
				<a href="javascript:window.close()">このウインドウを閉じる</a>
			</div>

		</div>
	</div>
	<!--footer-->
	<%@ include file="/WEB-INF/view/pc/fnfooter.jsp"%>
	<!--footer-->
	</div>
</body>
</html>
