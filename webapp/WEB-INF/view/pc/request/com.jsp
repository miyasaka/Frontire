<%@ page language="java" contentType="text/html; charset=windows-31J" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<title>[frontier]</title>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/assort.css" type="text/css" />

</head>

<body>
<div id="container">
<s:form>

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
</s:form>
	<div id="contents" class="clearfix">
		<div class="mainMember">
			<!--メイン-->

			<div class="mainMember-main">
			
				<div class="listBoxMemberList">
					<div class="listBoxMemberListTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name inputColStick">コミュニティ参加申請一覧(<c:choose><c:when test="${(offset+1)==(offset+fn:length(results))}">${resultscnt}</c:when>
<c:otherwise>${offset+1}～${offset+fn:length(results)}</c:otherwise></c:choose>／${resultscnt}件)</div></td>
							</tr>
						</table>
					</div>
					<c:if test="${offset>0 || resultscnt>(offset + fn:length(results))}">
					<div class="linkBox01"><div><c:if test="${offset>0}"><s:link href="moveComlist/?pgcnt=${pgcnt-1}">&lt;&lt;前を表示</s:link></c:if><c:if test="${resultscnt>(offset + fn:length(results))}"><span name="sp">&nbsp;</span><s:link href="moveComlist?pgcnt=${pgcnt+1}">次を表示&gt;&gt;</s:link></c:if></div></div>
					</c:if>
					<!-- 一覧 -->
					<table class="requestBody" cellpadding="0" cellspacing="0">
<c:forEach var="e" items="${results}" varStatus="i">
						<tr>
							<td class="${i.count eq 1?'line01':'line02'}" valign="top">
								<div class="leftBox">
									<s:link href="/pc/mem/${e.mid}/">
										<c:choose>
										<c:when test="${e.photo!=null}">
											<img src="${f:h(appDefDto.FP_CMN_CONTENTS_ROOT)}${fn:replace(e.photo,'dir','pic76')}" alt="${f:h(e.nickname)}" />
										</c:when>
										<c:otherwise>
											<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg76.gif" alt="${f:h(e.nickname)}" />
										</c:otherwise>
										</c:choose>
									</s:link>
									${f:h(e.nickname)}<br/>
									(${e.logindate})
								</div>
							</td>
							<td valign="top" ${i.count eq 1?'':'class="line03"'}>
								<div class="rightBox-title">
									<div class="title">【${e.title}】メッセージ</div>
									<div>${e.upddate}</div>
								</div>
								<div class="rightBox-body">${f:br(e.requiredmsg)}</div>
							</td>
						</tr>
						<tr>
							<td colspan="2" class="request-bottom">
								<s:form>
								<input type="submit" name="approval" value="承認する"></input><span class="sp">&nbsp;</span>
								<input type="submit" name="refusal" value="拒否する"></input>
									<html:hidden property="reqCid" value="${e.cid}"></html:hidden>
									<html:hidden property="reqId" value="${e.mid}"></html:hidden>
								</s:form>
							</td>
						</tr>
</c:forEach>
					</table>
					<!-- 一覧 -->

					<c:if test="${offset>0 || resultscnt>(offset + fn:length(results))}">
					<div class="linkBox02"><div><c:if test="${offset>0}"><s:link href="moveComlist/?pgcnt=${pgcnt-1}">&lt;&lt;前を表示</s:link></c:if><c:if test="${resultscnt>(offset + fn:length(results))}"><span name="sp">&nbsp;</span><s:link href="moveComlist?pgcnt=${pgcnt+1}">次を表示&gt;&gt;</s:link></c:if></div></div>
					</c:if>
					<!--/listBoxMemberListTitle-->
					
				</div>
				<!--/listBoxMemberList-->
				
			</div>
			<!--/mainMember-main-->
		</div>
		<!--/mainMember-->
	</div>
	<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/sp10.png" alt=""/>
	<!--/contents-->
	<!-- フッター -->
	<%@ include file="/WEB-INF/view/pc/ffooter.jsp"%>
	<!-- フッター -->
</div>
<!--/container-->
</body>
</html>
