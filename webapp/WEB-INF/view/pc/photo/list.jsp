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
<title>[frontier]&nbsp;<c:if test="${!vUser}">${f:h(vNickname)}の</c:if>フォトアルバム</title>
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
				<!-- フォトアルバム一覧 -->
				<div class="listBoxPhoto">
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
												${f:h(vNickname)}のフォトアルバム一覧
												<c:if test="${resultscnt>0}">
													(${offset+1}件～${offset+fn:length(results)}件/${resultscnt}件)
												</c:if>
											</div>
										</td>
									</tr>
								</table>
							</td>
							<!--/photoCover_td1 -->
						</tr>
					<%-- 0件パターン --%>
					<c:if test="${resultscnt==0}">
						<tr>
							<td class="photoCover_td0">
								<font color="#000000">フォトアルバムはまだありません。</font>
							</td>
						</tr>
					</c:if>
					<%--/0件パターン --%>
						<c:if test="${resultscnt>appDefDto.FP_MY_PHOTOALBUMLIST_PGMAX}">
						<!-- pager -->
						<tr>
							<td colspan="2" style="border:solid 0px;" width="100%">
								<table class="photoCover_pging_table">
									<tr>
										<td class="photoCover_pging_td1">
										<c:choose>
											<c:when test="${offset>0}">&nbsp;&nbsp;<s:link href="/pc/photo/prepg">&lt;&lt;前の${appDefDto.FP_MY_PHOTOALBUMLIST_PGMAX}件</s:link></c:when>
											<c:otherwise>&nbsp;&nbsp;</c:otherwise>
										</c:choose>
										</td>
										<td class="photoCover_pging_td3">
										<c:choose>
											<c:when test="${resultscnt>(offset + fn:length(results))}"><s:link href="/pc/photo/nxtpg/">次の${appDefDto.FP_MY_PHOTOALBUMLIST_PGMAX}件&gt;&gt;</s:link>&nbsp;&nbsp;</c:when>
											<c:otherwise>&nbsp;&nbsp;</c:otherwise>
										</c:choose>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<!--/pager -->
						</c:if>
						<!-- loop -->
						<c:forEach var="e" items="${results}">
							<tr>
								<td align="center" rowspan="2" valign="middle" class="photoCover_thum">
									<s:link href="/pc/photo/view/${f:h(pmid)}/${f:h(e.ano)}">
									<c:choose>
										<c:when test="${e.pic eq null}">
											<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg180.gif" />
										</c:when>
										<c:when test="${e.publevel eq appDefDto.FP_CMN_DIARY_AUTH3[0]}">
											<c:choose>
												<c:when test="${vFriend or vUser}">
													<img src="${appDefDto.FP_CMN_CONTENTS_ROOT}/${fn:replace(e.pic,'dir','pic240')}" alt="${f:h(e.picname)}" />
												</c:when>
												<c:otherwise>
													<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg180.gif" />
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:when test="${e.publevel eq appDefDto.FP_CMN_DIARY_AUTH4[0]}">
											<c:choose>
												<c:when test="${vUser}">
													<img src="${appDefDto.FP_CMN_CONTENTS_ROOT}/${fn:replace(e.pic,'dir','pic240')}" alt="${f:h(e.picname)}" />
												</c:when>
												<c:otherwise>
													<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg180.gif" />
												</c:otherwise>
											</c:choose>
										</c:when>
									<c:otherwise>
										<img src="${appDefDto.FP_CMN_CONTENTS_ROOT}/${fn:replace(e.pic,'dir','pic240')}" alt="${f:h(e.picname)}" />
									</c:otherwise>
									</c:choose>
									</s:link>
								</td>
								<td align="center" valign="top" class="photoCover_td2">
									<div class="photoCover_div2"><font style="font-weight:bold;">タイトル</font>&nbsp;：&nbsp;<s:link href="/pc/photo/view/${f:h(pmid)}/${f:h(e.ano)}">${f:h(e.title)}</s:link></div>
									<div class="photoCover_div2"><font style="font-weight:bold;">説明</font>&nbsp;：&nbsp;<br />${f:br(e.cmnthtml)}</div>
									<div class="photoCover_div2"><font style="font-weight:bold;">掲載枚数</font>&nbsp;：&nbsp;${e.cnt2}枚</div>
									<div class="photoCover_div2"><font style="font-weight:bold;">公開レベル</font>&nbsp;：
									<c:choose>
										<c:when test="${e.publevel eq appDefDto.FP_CMN_DIARY_AUTH1[0]}">
											${appDefDto.FP_CMN_DIARY_AUTH1[1]}
										</c:when>
										<c:when test="${e.publevel eq appDefDto.FP_CMN_DIARY_AUTH3[0]}">
											${appDefDto.FP_CMN_DIARY_AUTH3[1]}
										</c:when>
										<c:when test="${e.publevel eq appDefDto.FP_CMN_DIARY_AUTH4[0]}">
											${appDefDto.FP_CMN_DIARY_AUTH4[1]}
										</c:when>
									</c:choose>
									</div>
									<div class="photoCover_div3"><font style="font-weight:bold;">作成日時</font>&nbsp;：&nbsp;<fmt:formatDate value="${f:date(f:h(e.entdate),'yyyyMMddHHmmss')}" pattern="yyyy-MM-dd HH:mm" /></div>
								</td>
							</tr>
							<tr>
								<td align="right" valign="middle" height="20" class="photoCover_td2">
									<s:link href="/pc/photo/view/${f:h(pmid)}/${f:h(e.ano)}">写真一覧へ</s:link>&nbsp;<span class="divline">|</span>&nbsp;<s:link href="/pc/photo/detail/${f:h(pmid)}/${f:h(e.ano)}">コメント(${f:h(e.cnt)})</s:link>
								<c:if test="${vUser}">
									|&nbsp;<s:link href="/pc/entphoto/edit/${f:h(e.ano)}">編集する</s:link>
								</c:if>
								</td>
							</tr>
						</c:forEach>
						<!--/loop -->
						<c:if test="${resultscnt>appDefDto.FP_MY_PHOTOALBUMLIST_PGMAX}">
						<!-- pager -->
						<tr>
							<td colspan="2" style="border:solid 0px;" width="100%">
								<table class="photoCover_pging_table">
									<tr>
										<td class="photoCover_pging_td1">
										<c:choose>
											<c:when test="${offset>0}">&nbsp;&nbsp;<s:link href="/pc/photo/prepg">&lt;&lt;前の${appDefDto.FP_MY_PHOTOALBUMLIST_PGMAX}件</s:link></c:when>
											<c:otherwise>&nbsp;&nbsp;</c:otherwise>
										</c:choose>
										</td>
										<td class="photoCover_pging_td3">
										<c:choose>
											<c:when test="${resultscnt>(offset + fn:length(results))}"><s:link href="/pc/photo/nxtpg/">次の${appDefDto.FP_MY_PHOTOALBUMLIST_PGMAX}件&gt;&gt;</s:link>&nbsp;&nbsp;</c:when>
											<c:otherwise>&nbsp;&nbsp;</c:otherwise>
										</c:choose>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<!--/pager -->
						</c:if>
					</table>
					<!--/photoCover_table -->
				</div>
				<!--/listBoxPhoto -->
				<!--/フォトアルバム一覧 -->
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