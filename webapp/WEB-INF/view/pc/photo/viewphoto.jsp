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
<title>[frontier]&nbsp;<c:if test="${!vUser}">${f:h(vNickname)}&nbsp;|&nbsp;</c:if>&nbsp;|&nbsp;写真詳細</title>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/common.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>
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
		<div class="mainPhoto">
			<!-- メイン -->
			<div class="mainPhoto-main">
				<div class="listBoxPhoto">
					<table class="photoCover_table">
						<tr>
							<td colspan="2" class="photoCover_td1">
							<c:choose>
								<c:when test="${vUser}"><c:set var="divClass" value="photoCover_div1 myColStick" /></c:when>
								<c:otherwise><c:set var="divClass" value="photoCover_div1 memColStick" /></c:otherwise>
							</c:choose>
								<table><tr><td valign="middle"><div class="${divClass}">${f:h(vNickname)}の写真詳細</div></td></tr></table>
							</td>
						</tr>
			<c:choose>
				<c:when test="${!isAuth}">
						<tr><td colspan="2" class="photoCover_td0">${unauthMsg}</td></tr>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${fn:length(viewphotoResults) > 0}">
							<tr>
								<td style="border:solid 0px;" width="100%">
									<table class="photoCover_pging_table">
										<tr>
											<td class="photoCover_pging_td5">
												<c:choose>
												<c:when test="${isPrevViewphoto}">&nbsp;&nbsp;<s:link href="/pc/photo/viewphoto/${f:h(pmid)}/${f:h(pano)}/${f:h(viewphotoResults[0]['prevfno'])}">&lt;&lt;前の写真</s:link></c:when>
												<c:otherwise>&nbsp;</c:otherwise>
												</c:choose>
											</td>
											<td class="photoCover_pging_td6">
												<s:link href="/pc/photo/view/${f:h(pmid)}/${f:h(pano)}">写真一覧へ</s:link>&nbsp;|
												<s:link href="/pc/photo/detail/${f:h(pmid)}/${f:h(pano)}">コメントを書く</s:link>
												<c:if test="${vUser}">
												|&nbsp;<s:link href="/pc/entphoto/editphoto/${f:h(pano)}/${f:h(strFno)}">編集する</s:link>
												</c:if>
											</td>
											<td class="photoCover_pging_td7">
												<c:choose>
												<c:when test="${isNextViewphoto}"><s:link href="/pc/photo/viewphoto/${f:h(pmid)}/${f:h(pano)}/${f:h(viewphotoResults[0]['nextfno'])}">次の写真&gt;&gt;</s:link>&nbsp;&nbsp;</c:when>
												<c:otherwise>&nbsp;</c:otherwise>
												</c:choose>
											</td>
										</tr>
									</table>
									<!--/photoCover_pging_table -->
								</td>
							</tr>
							<tr>
								<td align="center" valign="middle" class="photoCover_td5">
									<img src="${appDefDto.FP_CMN_CONTENTS_ROOT}/${fn:replace(viewphotoResults[0]['pic'],'dir','pic640')}" alt="${f:h(viewphotoResults[0]['picname'])}" />
								</td>
							</tr>
							<tr>
								<td class="photoCover_td6">
									<font style="font-weight:bold;">説明</font>&nbsp;：${f:h(viewphotoResults[0]['picname'])}
								</td>
							</tr>
							<tr>
								<td style="border:solid 0px;" width="100%">
									<table class="photoCover_pging_table">
										<tr>
											<td class="photoCover_pging_td5">
												<c:choose>
												<c:when test="${isPrevViewphoto}">&nbsp;&nbsp;<s:link href="/pc/photo/viewphoto/${f:h(pmid)}/${f:h(pano)}/${f:h(viewphotoResults[0]['prevfno'])}">&lt;&lt;前の写真</s:link></c:when>
												<c:otherwise>&nbsp;</c:otherwise>
												</c:choose>
											</td>
											<td class="photoCover_pging_td6">
												<s:link href="/pc/photo/view/${f:h(pmid)}/${f:h(pano)}">写真一覧へ</s:link>&nbsp;|
												<s:link href="/pc/photo/detail/${f:h(pmid)}/${f:h(pano)}">コメントを書く</s:link>
												<c:if test="${vUser}">
												|&nbsp;<s:link href="/pc/entphoto/editphoto/${f:h(pano)}/${f:h(strFno)}">編集する</s:link>
												</c:if>
											</td>
											<td class="photoCover_pging_td7">
												<c:choose>
												<c:when test="${isNextViewphoto}"><s:link href="/pc/photo/viewphoto/${f:h(pmid)}/${f:h(pano)}/${f:h(viewphotoResults[0]['nextfno'])}">次の写真&gt;&gt;</s:link>&nbsp;&nbsp;</c:when>
												<c:otherwise>&nbsp;</c:otherwise>
												</c:choose>
											</td>
										</tr>
									</table>
									<!--/photoCover_pging_table -->
								</td>
							</tr>
						</c:when>
						<c:otherwise>
							<tr><td colspan="2" class="photoCover_td0">写真が削除されているか、あるいは存在しないアドレスへのアクセスです。 </td></tr>
						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
					</table>
					<!--/photoCover_table -->
				</div>
				<!--/listBoxPhoto -->
			</div>
			<!--/mainPhoto-main -->
		</div>
		<!--/mainPhoto -->
	</div>
	<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/sp10.png" alt="" />
	<!--/contents -->

	<!-- footer -->
	<%@ include file="/WEB-INF/view/pc/ffooter.jsp"%>
	<!--/footer -->
</div>
<!--/container -->
</s:form>
</body>
</html>