<%@ page language="java" contentType="text/html; charset=windows-31J" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<meta name="Slurp" content="NOYDIR" />
<meta name="robots" content="nofollow,noindex" />
<meta name="robots" content="noodp" />
<title>[frontier]</title>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/common.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontiernet.js"></script>
<script language="javascript" type="text/javascript" src="/static/js/parsexml.js"></script>
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/assort.css" type="text/css" />
</head>

<body>
<s:form>

<div id="container">
	<!--header-->
	<%@ include file="/WEB-INF/view/pc/fheader.jsp"%>
	<!--/header-->

	<!--navbarメニューエリア-->
	<%@ include file="/WEB-INF/view/pc/fmenu.jsp"%>
	<!--/navbarメニューエリア-->

	<!--/navbar-->
	<div id="contents" class="clearfix">
		<div class="main">
			<!--メイン-->
			<div class="main-side">
				<!-- グループ -->
				<div class="listBoxMember">
					<div style="height:28px;" class="listBoxMemberTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name outColStick"><s:link href="/pc/fmem/groupAll/" title="ｸﾞﾙｰﾌﾟ">ｸﾞﾙｰﾌﾟ(${fn:length(GMList)})</s:link></div></td>
							</tr>
						</table>
					</div>
					<div class="listBoxBody">
<c:choose><c:when test="${fn:length(GMList)>0}">
<c:forEach var="e" items="${GMList}" varStatus="i">
<c:choose>
<c:when test="${i.last}">
						<!-- 一番最下部のDIVのスタイル -->
						<div style="border-style:solid; border-width:0;">
</c:when>
<c:otherwise>
						<div style="border-style:solid; border-width:0 0 1px 0;">
</c:otherwise>
</c:choose>
							<div style="margin:5px;"><s:link href="/pc/fmem/group/${f:h(e[0].gid)}" title="${f:h(e[0].gname)}">${f:h(e[0].gname)}(${e[0].joinnumber})</s:link></div>
							<div style="margin:2px;text-align:center;border-style:dotted; border-width:1px 0 0 0;">
								<table>
<c:set var="row" value="3"/>
<c:set var="ml" value="${fn:length(e[1])}"/>
<c:forEach var="e" items="${e[1]}" varStatus="j">
<c:if test="${j.count%row==1}">
									<tr>
</c:if>
										<td align="center" style="width:60px;">
											<s:link href="/pc/mem/${f:h(e.mid)}" title="${f:h(e.nickname)}">
<c:choose>
<c:when test="${f:h(e.pic) != ''}">
											<img src="${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(e.pic,'dir','pic60')}" alt="${f:h(e.nickname)}"/>
</c:when>
<c:otherwise>
											<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg60.gif" alt="${f:h(e.nickname)}"/>
</c:otherwise>
</c:choose>
											</s:link>
										</td>
<c:choose>
<c:when test="${j.count==ml}">
<c:if test="${j.count%row!=0}">
<c:forEach begin="1" end="${row-j.count%row}">
										<td style="width:60px;">&nbsp;</td>
</c:forEach>
</c:if>
									</tr>
</c:when>
<c:otherwise><c:if test="${j.count%row==0}">
									</tr>
</c:if></c:otherwise>
</c:choose>
</c:forEach>
								</table>
							</div>
						</div>
</c:forEach>
</c:when>
<c:otherwise>
						<div style="height:40px;text-align:center;">
							<div style="margin-top:20px;margin-bottom:20px;">グループはまだありません。</div>
						</div>
</c:otherwise>
</c:choose>
					</div>
				</div>
				<!-- グループ -->
			</div>

			<div class="main-main">
				<!--ﾒﾝﾊﾞｰ日記-->
				<div class="listBoxMember02 clearfix">
					<div style="height:28px;" class="listBoxMember02Title">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp">
									<div class="ttl_name outColStick">
										<table border="0" width="100%" cellspacing="0" cellpadding="0">
											<tr>
												<td><a href="/frontier/pc/fdiary/" title="ﾒﾝﾊﾞｰ日記">ﾒﾝﾊﾞｰ日記</a></td>
												<td style="height:20px" align="right" valign="top">
													<select name="groupid" style="width:180px;" onChange="javascript:ff_reload('/frontier/pc/ftop/selgroup/');">
														<option value="0">グループ選択▼</option>
<c:forEach var="e" items="${GroupList}" varStatus="i">
														<option value="${e.gid}"<c:if test="${groupid==e.gid}"> selected</c:if>>${f:h(e.gname)}</option>
</c:forEach>
													</select>
												</td>
											</tr>
										</table>
									</div>
								</td>
							</tr>
						</table>
					</div>
					<div class="listBoxBody">
<c:choose>
<c:when test="${fn:length(FDiaryList)>0}">
<c:forEach var="e" items="${FDiaryList}" varStatus="i">
						<div style="border-style:solid; border-width:1px 0 0 0;" class="clearfix">
							<div style="width:440px;padding:0 1px 0 1px;">
								<table border="0" style="width:100%;border-bottom:dotted #000000 1px;">
									<tr>
										<td style="text-align:left;">
											<s:link href="/pc/diary/view/${f:h(e.diaryid)}/${fn:substring(f:h(e.entdate),0,8)}/${f:h(e.mid)}">${f:h(e.title) }<c:if test="${e.cnt != 0 && e.cnt != null}">(${f:h(e.cnt) })</c:if></s:link>
										</td>
										<td style="width:140px;text-align:right;"><fmt:formatDate value="${f:date(f:h(e.entdate),'yyyyMMddHHmm')}" pattern="yyyy年MM月dd日HH:mm" /></td>
									</tr>
								</table>
							</div>
							<div>
								<table border="0" cellspacing="0" cellpadding="0" style="width:440px;">
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
</c:when>
<c:otherwise>
						<div style="height:40px;text-align:center;">
							<div style="margin-top:20px;margin-bottom:20px;">日記はまだありません。</div>
						</div>
</c:otherwise>
</c:choose>
					</div>
				</div>
				<!--/ﾒﾝﾊﾞｰ日記-->
			</div>
			<!--/main-main-->
		</div>

		<!--/main-->
		<!-- 右側コンテンツ -->
		<div class="side">
			<div class="listBox04">
				<div  class="listBox04Title">
					<table class="list_ttl">
						<tr>
							<td class="ttl_name_sp"><div class="ttl_name">Frontier&nbsp;Net(${fn:length(FNetList)})</div></td>
						</tr>
					</table>
				</div>
				<div class="listBoxBody">
					<!-- Frontier Net 一覧 -->
<c:if test="${fn:length(FNetList)>0}"><c:forEach var="e" items="${FNetList}" varStatus="i">
					<script>
					<!--
<c:choose><c:when test="${e.network==userInfoDto.fdomain}">
						// Frontier Net(自分)
						writeFrontierNet(
							"2",
							"http://${f:h(e.network)}/frontier/pc/gettext/outside/",
							"${userInfoDto.memberId}",
							"${e.network}",
							"${userInfoDto.fdomain}",
							"${appDefDto.FP_CMN_HOST_NAME}"
						);
</c:when><c:otherwise>
						// Frontier Net(他)
						writeFrontierNet(
							"3",
							"http://${f:h(e.network)}/frontier/pc/gettext/outside/",
							"${userInfoDto.memberId}",
							"${e.network}",
							"${userInfoDto.fdomain}",
							"${appDefDto.FP_CMN_HOST_NAME}"
						);
</c:otherwise></c:choose>
					-->
					</script>
					<div style="height:10px;">&nbsp;</div>
</c:forEach></c:if>
				</div>
			</div>
		</div>
	</div>
	<!--/contents-->
	<div style="height:10px;">&nbsp;</div>
	<!--footer-->
	<%@ include file="/WEB-INF/view/pc/ffooter.jsp"%>
	<!--footer-->
</div>
<!--/container-->
</s:form>
</body>
</html>