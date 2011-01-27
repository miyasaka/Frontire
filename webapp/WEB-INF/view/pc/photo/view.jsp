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
<title>[frontier]&nbsp;<c:if test="${!vUser}">${f:h(vNickname)}&nbsp;|&nbsp;</c:if>${f:h(coverResults[0]['title'])}</title>
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
		<div class="mainPhoto">
			<!-- メイン -->
			<div class="mainPhoto-main">
				<div class="listBoxPhoto">
			<c:choose>
				<c:when test="${!vUser and (coverResults[0]['publevel'] eq appDefDto.FP_CMN_DIARY_AUTH4[0] or (!vFriend) and coverResults[0]['publevel'] eq appDefDto.FP_CMN_DIARY_AUTH3[0])}">
					<table class="photoCover_table2">
						<tr>
							<td colspan="2" class="photoCover_td1">
								<div class="photoCover_div1 memColStick">アルバムを見る</div>
							</td>
						</tr>
						<tr>
							<td colspan="2" class="photoCover_td0">
						<c:choose>
							<c:when test="${coverResults[0]['publevel'] eq appDefDto.FP_CMN_DIARY_AUTH3[0]}">
								<font color="#000000">${appDefDto.FP_CMN_DIARY_AUTH3[1]}のため閲覧することが出来ません。</font>
							</c:when>
							<c:when test="${coverResults[0]['publevel'] eq appDefDto.FP_CMN_DIARY_AUTH4[0]}">
								<font color="#000000">${appDefDto.FP_CMN_DIARY_AUTH4[1]}のため閲覧することが出来ません。</font>
							</c:when>
							<c:otherwise>&nbsp;</c:otherwise>
						</c:choose>
							</td>
						</tr>
					</table>
					<!--/photoCover_table2 -->
				</c:when>
				<c:otherwise>
					<!-- フォトアルバム -->
					<table class="photoCover_table">
						<tr>
							<td colspan="2" class="photoCover_td1">
								<table>
									<tr>
										<td valign="middle">
									<c:choose>
										<c:when test="${vUser}">
											<div class="photoCover_div1 myColStick">
										</c:when>
										<c:otherwise>
											<div class="photoCover_div1 memColStick">
										</c:otherwise>
									</c:choose>
												${f:h(vNickname)}のフォトアルバム
											</div>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td align="center" rowspan="2" valign="middle" class="photoCover_thum">
								<c:choose>
									<c:when test="${coverResults[0]['pic'] eq null}">
										<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg240.gif" />
									</c:when>
									<c:otherwise>
										<img src="${appDefDto.FP_CMN_CONTENTS_ROOT}/${fn:replace(coverResults[0]['pic'],'dir','pic240')}" alt="${f:h(coverResults[0]['picname'])}" />
									</c:otherwise>
								</c:choose>
								<c:if test="${vUser}">
									<c:if test="${fn:length(photoResults)!=0}">
										<s:link href="/pc/entphoto/change/${f:h(coverResults[0]['ano'])}">表紙を変更</s:link>
									</c:if>
								</c:if>
							</td>
							<td align="center" valign="top" class="photoCover_td2">
								<div class="photoCover_div2"><font style="font-weight:bold;">タイトル</font>&nbsp;：&nbsp;${f:h(coverResults[0]['title'])}</div>
								<div class="photoCover_div2"><font style="font-weight:bold;">説明</font>&nbsp;：<br />${f:br(coverResults[0]['viewDetail'])}</div>
								<div class="photoCover_div2"><font style="font-weight:bold;">掲載枚数</font>&nbsp;：&nbsp;${f:h(coverResults[0]['photocnt'])}枚</div>
								<div class="photoCover_div2"><font style="font-weight:bold;">公開レベル</font>&nbsp;：
									<c:choose>
										<c:when test="${coverResults[0]['publevel'] eq appDefDto.FP_CMN_DIARY_AUTH1[0]}">
											${appDefDto.FP_CMN_DIARY_AUTH1[1]}
										</c:when>
										<c:when test="${coverResults[0]['publevel'] eq appDefDto.FP_CMN_DIARY_AUTH3[0]}">
											${appDefDto.FP_CMN_DIARY_AUTH3[1]}
										</c:when>
										<c:when test="${coverResults[0]['publevel'] eq appDefDto.FP_CMN_DIARY_AUTH4[0]}">
											${appDefDto.FP_CMN_DIARY_AUTH4[1]}
										</c:when>
									</c:choose>
								</div>
								<div class="photoCover_div3"><font style="font-weight:bold;">作成日時</font>&nbsp;：&nbsp;<fmt:formatDate value="${f:date(f:h(coverResults[0]['entdate']),'yyyyMMddHHmmss')}" pattern="yyyy-MM-dd HH:mm" /></div>
							</td>
						</tr>
						<tr>
							<td align="center" valign="middle" height="20" class="photoCover_td2">
								<c:if test="${vUser}">
									<s:link href="/pc/entphoto/add/${f:h(coverResults[0]['ano'])}/">写真を追加</s:link>
									|
								</c:if>
								<s:link href="/pc/photo/detail/${f:h(pmid)}/${f:h(coverResults[0]['ano'])}">コメント(${f:h(coverResults[0]['cnt'])})</s:link>
								<c:if test="${vUser}">
									|
									<s:link href="/pc/entphoto/edit/${f:h(coverResults[0]['ano'])}">編集する</s:link>
								</c:if>
							</td>
						</tr>
					</table>
					<!--/photoCover_table -->
					<!--/フォトアルバム -->
				</div>
				<!--/listBoxPhoto -->
				<div class="listBoxPhoto">
					<!-- フォト一覧 -->
					<table class="photoCover_table2">
						<tr>
							<td colspan="3" class="photoCover_td1">
								<table>
									<tr>
										<td valign="middle">
									<c:choose>
										<c:when test="${vUser}">
											<div class="photoCover_div1 myColStick">
										</c:when>
										<c:otherwise>
											<div class="photoCover_div1 memColStick">
										</c:otherwise>
									</c:choose>
												写真一覧
												<c:if test="${photoResultscnt>0}">
													(${viewOffset+1}件～${viewOffset+fn:length(photoResults)}件/${f:h(coverResults[0]['photocnt'])}件)
												</c:if>
											</div>
										</td>
									</tr>
								</table>
							</td>
						</tr>
				<c:choose>
					<c:when test="${photoResultscnt==0}">
						<tr>
							<td class="photoCover_td0">
								<font color="#000000">写真はまだありません。</font>
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:set var="row" value="3" />
						<!-- pager -->
						<c:if test="${photoResultscnt>appDefDto.FP_MY_PHOTO_PGMAX}">
						<tr>
							<td colspan="${f:h(row)}" style="border:solid 0px;" width="100%">
								<table class="photoCover_pging_table">
									<tr>
										<td class="photoCover_pging_td1">
										<c:choose>
											<c:when test="${viewOffset>0}">&nbsp;&nbsp;<s:link href="/pc/photo/viewPrepg/">&lt;&lt;前の${appDefDto.FP_MY_PHOTO_PGMAX}件</s:link></c:when>
											<c:otherwise>&nbsp;&nbsp;</c:otherwise>
										</c:choose>
										</td>
										<td class="photoCover_pging_td3">
										<c:choose>
											<c:when test="${photoResultscnt>(viewOffset + fn:length(photoResults))}"><s:link href="/pc/photo/viewNxtpg/">次の${appDefDto.FP_MY_PHOTO_PGMAX}件&gt;&gt;</s:link>&nbsp;&nbsp;</c:when>
											<c:otherwise>&nbsp;&nbsp;</c:otherwise>
										</c:choose>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						</c:if>
						<!--/pager -->
						<!-- loop -->
					<c:forEach var="e" items="${photoResults}" varStatus="status">
						<c:if test="${(status.count % row) eq 1}">
							<tr>
						</c:if>
								<td align="center" valign="middle" class="photoCover_td4">
									<s:link href="/pc/photo/viewphoto/${f:h(pmid)}/${f:h(pano)}/${f:h(e.fno)}" title="${f:h(e.picnameorg)}">
										<img src="${appDefDto.FP_CMN_CONTENTS_ROOT}/${fn:replace(e.pic,'dir','pic180')}" alt="${f:h(e.picnameorg)}" />
									</s:link>
									<s:link href="/pc/photo/viewphoto/${f:h(pmid)}/${f:h(pano)}/${f:h(e.fno)}" title="${f:h(e.picnameorg)}">${f:h(e.picname)}</s:link>
								</td>
								<c:if test="${status.last || (viewOffset + status.count) == photoResultscnt}">
									<c:if test="${(fn:length(photoResults) % row) != 0}">
										<c:forEach begin="1" end="${row - fn:length(photoResults) % row}" varStatus="i2">
											<td class="photoCover_td4">&nbsp;</td>
										</c:forEach>
									</c:if>
							</tr>
								</c:if>
					</c:forEach>
						<!--/loop -->
						<!-- pager -->
						<c:if test="${photoResultscnt>appDefDto.FP_MY_PHOTO_PGMAX}">
						<tr>
							<td colspan="${f:h(row)}" style="border:solid 0px;" width="100%">
								<table class="photoCover_pging_table">
									<tr>
										<td class="photoCover_pging_td1">
										<c:choose>
											<c:when test="${viewOffset>0}">&nbsp;&nbsp;<s:link href="/pc/photo/viewPrepg/">&lt;&lt;前の${appDefDto.FP_MY_PHOTO_PGMAX}件</s:link></c:when>
											<c:otherwise>&nbsp;&nbsp;</c:otherwise>
										</c:choose>
										</td>
										<td class="photoCover_pging_td3">
										<c:choose>
											<c:when test="${photoResultscnt>(viewOffset + fn:length(photoResults))}"><s:link href="/pc/photo/viewNxtpg/">次の${appDefDto.FP_MY_PHOTO_PGMAX}件&gt;&gt;</s:link>&nbsp;&nbsp;</c:when>
											<c:otherwise>&nbsp;&nbsp;</c:otherwise>
										</c:choose>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						</c:if>
						<!--/pager -->
					</c:otherwise>
				</c:choose>
					</table>
					<!--/フォト一覧 -->
				</c:otherwise>
			</c:choose>
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

