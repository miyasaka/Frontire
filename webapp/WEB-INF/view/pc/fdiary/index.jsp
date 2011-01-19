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
	<c:when test="${caltype eq '1'}">
		<fmt:formatDate value="${f:date(f:h(diaryDay),'yyyyMMdd')}" pattern="yyyy年M月" />の
	</c:when>
	<c:when test="${caltype eq '2'}">
		<fmt:formatDate value="${f:date(f:h(diaryDay),'yyyyMMdd')}" pattern="yyyy年M月d日" />の
	</c:when>
</c:choose>
メンバー日記一覧</title>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/common.js"></script>
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/assort.css" type="text/css" />
</head>

<body>
<s:form>
<div id="container">

	<!--header-->
	<!-- ヘッダー -->
	<%@ include file="/WEB-INF/view/pc/fheader.jsp"%>
	<!-- ヘッダー -->
	<!--/header-->

	<!--navbarメニューエリア-->
	<!-- マイページ共通 -->
	<%@ include file="/WEB-INF/view/pc/fmenu.jsp"%>
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
								<s:link href="/pc/fdiary/prepg">&lt;&lt;前の20件</s:link>
							</c:if>&nbsp;
							</li>
							<li class="PageTitle" style="valign:middle;"><c:choose><c:when test="${caltype eq '1'}"><fmt:formatDate value="${f:date(f:h(diaryDay),'yyyyMMdd')}" pattern="yyyy年M月" />の</c:when><c:when test="${caltype eq '2'}"><fmt:formatDate value="${f:date(f:h(diaryDay),'yyyyMMdd')}" pattern="yyyy年M月d日" />の</c:when></c:choose>メンバー日記</li>
							<li>
							<c:if test="${resultscnt>(offset + fn:length(results))}">
								<s:link href="/pc/fdiary/nxtpg">次の20件&gt;&gt;</s:link>
							</c:if>
							</li>
						</ul>
					</div>					

					<div class="listBox11Title" style="height:28px;">
						<table class="ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name outColStick">日記一覧<c:if test="${resultscnt>0}">&nbsp;(${offset+1}件～${offset+fn:length(results)}件)</c:if></div></td>
								<td style="height:20px" align="right" valign="top">
									<select name="groupid" style="width:180px;" onChange="javascript:ff_reload('/frontier/pc/fdiary/selgroup/');">
									<option value="0">グループ選択▼</option>
									<c:forEach var="e" items="${GroupList}" varStatus="i">
											<option value="${e.gid}"<c:if test="${groupid==e.gid}"> selected</c:if>>${f:h(e.gname)}</option>
									</c:forEach>
									</select>
								</td>
							</tr>
						</table>
					</div>
					<%-- 0件パターン --%>
					<c:if test="${resultscnt==0}">
						<div style="border:0;text-align:center;padding:30px;">
							<font color="#000000">日記はありません。</font>
						</div>
					</c:if>
					<%--/0件パターン --%>
					<c:forEach var="e" items="${results}">
						<div style="border-style:solid; border-width:1px 0 0 0;" class="clearfix">
							<div style="width:100%;padding:0 1px 0 1px;">
								<table border="0" style="width:100%;border-bottom:dotted #000000 1px;">
									<tr>
										<td style="text-align:left;">
											<s:link href="/pc/diary/view/${f:h(e.diaryid)}/${fn:substring(f:h(e.entdate),0,8)}/${f:h(e.mid)}">${f:h(e.title) }<c:if test="${e.cnt != 0 && e.cnt != null}">(${f:h(e.cnt) })</c:if></s:link>
										</td>
										<td style="width:140px;text-align:right;"><fmt:formatDate value="${f:date(f:h(e.entdatev),'yyyyMMddHHmm')}" pattern="yyyy年MM月dd日HH:mm" /></td>
									</tr>
								</table>
							</div>
							<div>
								<table border="0" cellspacing="0" cellpadding="0" style="width:100%;">
									<tr>
										<td style="width:80px;border-right:dotted #000000 1px;" align="center">
											<s:link href="/pc/mem/${f:h(e.mid)}" title="${f:h(e.nickname)}">
											<c:choose>
												<c:when test="${f:h(e.pic) != ''}">
													<img src="${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(e.pic,'dir','pic60')}" alt="${f:h(e.nickname)}"/>
												</c:when>
												<c:otherwise>
													<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg60.gif" alt="${f:h(e.nickname)}"/>
												</c:otherwise>
											</c:choose>
											${f:h(e.nickname)}
											</s:link>
										</td>
										<td style="width:360px;">
											<!-- タイトルと本文 -->
											<div>
												<div>
													<p>${e.cmnthtml}</p>
												</div>
												<div style="width:100%;text-align:right;padding-bottom:0px;">
													<s:link href="/pc/diary/view/${f:h(e.diaryid)}/${fn:substring(f:h(e.entdate),0,8)}/${f:h(e.mid)}">続きを読む</s:link></div>
											</div>
										</td>
									</tr>
								</table>
							</div>
						</div>
					</c:forEach>

					<c:if test="${offset>0 || resultscnt>(offset + fn:length(results))}">
					<div class="listBox11Page">
						<table class="Page" style="border-top:solid #000000 1px;">
							<tr>
								<td class="pre"><c:if test="${offset>0}"><s:link href="/pc/fdiary/prepg">&lt;&lt;前の20件</s:link></c:if>&nbsp;</td>
								<td class="next">&nbsp;<c:if test="${resultscnt>(offset + fn:length(results))}"><s:link href="/pc/fdiary/nxtpg">次の20件&gt;&gt;</s:link></c:if></td>
							</tr>
						</table>
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
