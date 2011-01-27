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
<title>[frontier]&nbsp;${f:h(coverResults[0]['title'])}&nbsp;|&nbsp;表紙変更</title>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/colorful.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/common.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/prototype.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/effects.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/overlay.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/popup.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/windowstate.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/color_palette.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/youtube.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/emoji_palette_base.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/emoji_palette.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier_2.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/map.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/color_palette.css" type="text/css" />
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/emoji_palette.css" type="text/css" />
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/assort.css" type="text/css" />
</head>

<body>
<s:form>
<div id="container">

	<!-- header -->
	<!-- ヘッダー -->
	<%@ include file="/WEB-INF/view/pc/fheader.jsp"%>
	<!-- ヘッダー -->
	<!--/header -->

	<!--navbarメニューエリア -->
	<!-- マイページ共通 -->
	<%@ include file="/WEB-INF/view/pc/fmenu.jsp"%>
	<!-- マイページ共通 -->
	<!--/navbarメニューエリア -->

	<div id="contents" class="clearfix">
		<div class="mainEntPhotoCover">
			<!-- メイン -->
			<div class="mainEntPhotoCover-main">
				<div class="listBoxEntPhotoCover">
					<!-- フォト一覧 -->
					<table class="entPhotoCover_table">
						<tr>
							<td colspan="3" class="entPhotoCover_td1">
								<table>
									<tr>
										<td valign="middle">
											<div class="entPhotoCover_div1 inputColStick">表紙変更<c:if test="${photoResultscnt>0}">
													(${offset+1}件～${offset+fn:length(photoResults)}件/${f:h(coverResults[0]['photocnt'])}件)
												</c:if></div>
										</td>
									</tr>
								</table>
							</td>
							<!--/entPhotoCover_td1 -->
						</tr>
					<!-- pager -->
					<c:if test="${photoResultscnt>appDefDto.FP_MY_PHOTO_PGMAX}">
						<tr>
							<td colspan="3" style="border:solid 0px;" width="100%">
								<table class="entPhotoCover_pging_table">
									<tr>
										<td class="entPhotoCover_pging_td1">
										<c:choose>
											<c:when test="${offset>0}">&nbsp;&nbsp;<s:link href="/pc/entphoto/prepg/">&lt;&lt;前の15件</s:link></c:when>
											<c:otherwise>&nbsp;&nbsp;</c:otherwise>
										</c:choose>
										</td>
										<td class="entPhotoCover_pging_td3">
										<c:choose>
											<c:when test="${photoResultscnt>(offset + fn:length(photoResults))}"><s:link href="/pc/entphoto/nxtpg/">次の15件&gt;&gt;</s:link>&nbsp;&nbsp;</c:when>
											<c:otherwise>&nbsp;&nbsp;</c:otherwise>
										</c:choose>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</c:if>
					<!--/pager -->
					<c:set var="row" value="3" />
					<!-- loop -->
				<c:forEach var="e" items="${photoResults}" varStatus="status">
					<c:if test="${(status.count % row) eq 1}">
						<tr>
					</c:if>
							<td align="center" valign="middle" class="entPhotoCover_td4">
								<a href="javascript:void(0);" onClick="ff_viewBigimg('${f:h(appDefDto.FP_CMN_CONTENTS_ROOT)}${fn:replace(e.pic,'dir','pic640')}');">								
									<img src="${appDefDto.FP_CMN_CONTENTS_ROOT}/${fn:replace(e.pic,'dir','pic180')}" alt="${f:h(e.picnameorg)}" />
								</a>
								<c:choose>
									<c:when test="${e.coverflg eq 1}">表紙</c:when>
									<c:otherwise><s:link href="/pc/entphoto/updCover/${f:h(e.fno)}">表紙にする</s:link></c:otherwise>
								</c:choose>
							</td>
							<c:if test="${status.last || (offset + status.count) == photoResultscnt}">
								<c:if test="${(fn:length(photoResults) % row) != 0}">
									<c:forEach begin="1" end="${row - fn:length(photoResults) % row}" varStatus="i2">
										<td class="entPhotoCover_td4">&nbsp;</td>
									</c:forEach>
								</c:if>
						</tr>
							</c:if>
				</c:forEach>
					<!--/loop -->
					<!-- pager -->
					<c:if test="${photoResultscnt>appDefDto.FP_MY_PHOTO_PGMAX}">
						<tr>
							<td colspan="3" style="border:solid 0px;" width="100%">
								<table class="entPhotoCover_pging_table">
									<tr>
										<td class="entPhotoCover_pging_td1">
										<c:choose>
											<c:when test="${offset>0}">&nbsp;&nbsp;<s:link href="/pc/entphoto/prepg/">&lt;&lt;前の15件</s:link></c:when>
											<c:otherwise>&nbsp;&nbsp;</c:otherwise>
										</c:choose>
										</td>
										<td class="entPhotoCover_pging_td3">
										<c:choose>
											<c:when test="${photoResultscnt>(offset + fn:length(photoResults))}"><s:link href="/pc/entphoto/nxtpg/">次の15件&gt;&gt;</s:link>&nbsp;&nbsp;</c:when>
											<c:otherwise>&nbsp;&nbsp;</c:otherwise>
										</c:choose>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</c:if>
					<!--/pager -->
					</table>
					<!--/フォト一覧 -->
				</div>
				<!--/listBoxEntPhotoCover -->
				<div style="margin-bottom:10px;text-align:center;">
					<s:link href="/pc/photo/view/${f:h(userInfoDto.memberId)}/${f:h(cano)}/">アルバムへ戻る</s:link>
				</div>
			</div>
			<!--/mainEntPhotoCover-main -->
		</div>
		<!--/mainEntPhotoCover -->
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