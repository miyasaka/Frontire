<%@ page language="java" contentType="text/html; charset=windows-31J"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<title>[frontier] 
<c:choose>
	<c:when test="${!vUser}">
		${f:h(vNickname)}さん
		<c:choose>
			<c:when test="${titleFlg eq '1'}">
				|&nbsp;<fmt:formatDate value="${f:date(f:h(diaryDay),'yyyyMMdd')}" pattern="yyyy年M月" />の
			</c:when>
			<c:when test="${titleFlg eq '2'}">
				|&nbsp;<fmt:formatDate value="${f:date(f:h(diaryDay),'yyyyMMdd')}" pattern="yyyy年M月d日" />の
			</c:when>
			<c:otherwise>
				の
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<c:choose>
			<c:when test="${titleFlg eq '1'}">
				<fmt:formatDate value="${f:date(f:h(diaryDay),'yyyyMMdd')}" pattern="yyyy年M月" />の
			</c:when>
			<c:when test="${titleFlg eq '2'}">
				<fmt:formatDate value="${f:date(f:h(diaryDay),'yyyyMMdd')}" pattern="yyyy年M月d日" />の
			</c:when>
		</c:choose>
	</c:otherwise>
</c:choose>
日記一覧</title>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/common.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/assort.css" type="text/css" />
</head>

<body>
<s:form>
<div id="container">

	<!--header-->
	<!-- ヘッダー -->
	<c:choose>
		<c:when test="${userInfoDto.membertype eq '2'}">
			<%@ include file="/WEB-INF/view/pc/fnheader.jsp"%>
		</c:when>
		<c:otherwise>
			<%@ include file="/WEB-INF/view/pc/fheader.jsp"%>
		</c:otherwise>
	</c:choose>
	<!-- ヘッダー -->
	<!--/header-->

	<!--navbarメニューエリア-->
	<!-- マイページ共通 -->
	<c:choose>
		<c:when test="${userInfoDto.membertype eq '2'}">
			<br/>
		</c:when>
		<c:otherwise>
			<%@ include file="/WEB-INF/view/pc/fmenu.jsp"%>
		</c:otherwise>
	</c:choose>
	<!-- マイページ共通 -->
	<!--/navbarメニューエリア-->

	<div id="contents" class="clearfix">
		<div class="mainDiary">
			<!--メイン-->
			
			<!--mainDiary-side-->
			<!--カレンダー-->
			<%@ include file="./common/calendar.jsp"%>
			<!--/カレンダー-->
			<!--/mainDiary-side-->

			<div class="mainDiary-main">
				<!--listBox11-->
				<div class="listBox11">
					<div class="listBox11Page" style="text-align:center; border-style:solid; border-width:1px 0 0 0;">
						<ul class="PageArea">
							<li>
							<c:if test="${offset>0}">
								<s:link href="/pc/diary/prepg">&lt;&lt;前の20件</s:link>
							</c:if>&nbsp;
							</li>
							<li class="PageTitle">${f:h(vNickname)}<c:if test="${!vUser}">さん</c:if>の日記</li>
							<li>
							<c:if test="${resultscnt>(offset + fn:length(results))}">
								<s:link href="/pc/diary/nxtpg">次の20件&gt;&gt;</s:link>
							</c:if>
							</li>
						</ul>
					</div>
					
					<div class="listBox11Title">
						<table class="ttl">
							<tr>
								<td class="ttl_name_sp"><c:if test="${vUser}"><div class="ttl_name myColStick"></c:if><c:if test="${!vUser}"><div class="ttl_name memColStick"></c:if>日記一覧<c:if test="${resultscnt>0}">&nbsp;(${offset+1}件～${offset+fn:length(results)}件)</c:if></div></td>
							</tr>
						</table>
					</div>
					<%-- 0件パターン --%>
					<c:if test="${resultscnt==0}">
						<div style="border:0;text-align:center;padding:30px;">
							<font color="#000000">日記はまだありません。</font>
						</div>
					</c:if>
					<%--/0件パターン --%>
					<c:forEach var="e" items="${results}">
					<div style="border-style:solid; border-width:0 0 1px 0;">
						<div>
							<table class="diaryTitle">
								<tr>
									<td width="70%"><c:if test="${vUser}"><input type="checkbox" name="checkDiaryId" value="${f:h(e.diaryid)}" /></c:if><s:link href="/pc/diary/view/${f:h(e.diaryid)}/${fn:substring(f:h(e.entdate),0,8)}/${f:h(mid)}">${f:h(e.title) }<c:choose><c:when test="${userInfoDto.membertype eq '0' or userInfoDto.membertype eq '1'}">(${f:h(e.cnt) })</c:when><c:otherwise>(${f:h(e.cntoutside) })</c:otherwise></c:choose></s:link><c:if test="${vUser}">&nbsp;&nbsp;&nbsp;&nbsp;<s:link href="/pc/entdiary/edit?diaryid=${f:h(e.diaryid)}">編集する</s:link></c:if></td>
									<td width="30%"><fmt:formatDate value="${f:date(f:h(e.entdate),'yyyyMMddHHmm')}" pattern="yyyy年MM月dd日HH:mm" /></td>
								</tr>
							</table>
						</div>
						<div>
							<div style="border-style:dotted; border-width:1px 0 0 0; clear:both;">&nbsp;</div>
							<div class="diaryBody">
								<c:if test="${e.pic1!=null||e.pic2!=null||e.pic3!=null}">
									<div class="diaryPhoto">
									${e.pichtml}
									</div>
								</c:if>
								
								<p>${e.cmnthtml}</p>

							</div>
							<div class="diaryBottom">
								<s:link href="/pc/diary/view/${f:h(e.diaryid)}/${fn:substring(f:h(e.entdate),0,8)}/${f:h(mid)}">続きを読む</s:link>
							</div>
						</div>
					</div>
					</c:forEach>

					<c:if test="${offset>0 || resultscnt>(offset + fn:length(results))}">
					<div class="listBox11Page">
						<table class="Page">
							<tr>
								<td class="pre"><c:if test="${offset>0}"><s:link href="/pc/diary/prepg">&lt;&lt;前の20件</s:link></c:if></td>
								<td class="next"><c:if test="${resultscnt>(offset + fn:length(results))}"><s:link href="/pc/diary/nxtpg">次の20件&gt;&gt;</s:link></c:if></td>
							</tr>
						</table>
					</div>
					</c:if>
					<c:if test="${vUser && resultscnt>0}">
					<div class="listBox11Page" style="border-style:solid; border-top:1px;">
						<div class="delBtnArea">
							<input type="submit" name="delete" class="formBt01" value="チェックされた日記を削除する" />
						</div>
					</div>
					</c:if>
					
				</div>
				<!--/listBox11-->

				
			</div>
			<!--/mainDiary-main-->
		</div>
		<!--/mainDiary-->
	</div>
	<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/sp10.png" alt=""/>
	<!--/contents-->
	
	<!-- フッター -->
	<%@ include file="/WEB-INF/view/pc/ffooter.jsp"%>
	<!-- フッター -->

</div>
<!--/container-->
</s:form>
</body>
</html>
