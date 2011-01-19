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
<title>[frontier]&nbsp;<c:if test="${!vUser}">${f:h(vNickname)}&nbsp;|&nbsp;</c:if>${f:h(coverResults[0]['title'])}&nbsp;|&nbsp;コメント</title>
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
				<!-- エラー -->
				<html:errors />
				<!--/エラー -->
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
								<s:link href="/pc/photo/view/${f:h(pmid)}/${f:h(coverResults[0]['ano'])}">写真一覧へ</s:link>
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
					<!-- コメント一覧 -->
					<!-- loop -->
					<c:forEach var="e" items="${commentResults}" varStatus="status">
				<%-- sequence-start --%>
					<c:if test="${status.first}">
						<div class="listBox10">
							<div class="listBox10Title">
								<table class="ttl">
									<tr>
										<td class="ttl_name_sp">
											<c:choose>
												<c:when test="${vUser}">
													<div class="ttl_name myColStick">
												</c:when>
												<c:when test="${!vUser}">
													<div class="ttl_name memColStick">
												</c:when>
											</c:choose>
													コメント
												</div>
										</td>
										<td align="right"><a href="#add_comment">コメントを書く</a>
									</tr>
								</table>
							</div>
							<!--/listBox10Title -->
						</c:if>
				<%--/sequence-start --%>
							<div>
								<table class="photoTitle">
									<tr>
										<td width="60%">
										${f:h(e.cno)}
										<c:if test="${vUser}">
											<input type="checkbox" name="cnoArray" value="${f:h(e.cno)}" />
										</c:if>
									<c:choose>
										<c:when test="${e.mid eq userInfoDto.memberId}">
											<s:link href="/pc/top/">${f:h(e.nickname)}</s:link>
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${e.status eq 1}">
													<s:link href="/pc/mem/${f:h(e.mid)}/">${f:h(e.nickname)}</s:link>
												</c:when>
												<c:otherwise>
													&nbsp;
												</c:otherwise>
											</c:choose>
										</c:otherwise>
									</c:choose>
										<c:if test="${!vUser and e.mid eq userInfoDto.memberId}">
											|
											<s:link href="/pc/photo/deleteComment/${f:h(e.cno)}">削除</s:link>
										</c:if>
										</td>
										<td width="40%" style="text-align:right;">
											<fmt:formatDate value="${f:date(f:h(e.entdate),'yyyyMMddHHmmss')}" pattern="yyyy年MM月dd日 HH:mm" />
										</td>
									</tr>
								</table>
								<!--/photoTitle -->
							</div>
							<div>
								<div style="border-style:dotted; border-width:1px 0 0 0; clear:both; padding:5px;">
									<div class="photoBody">
										${f:br(e.viewComment)}
									</div>
								</div>
							</div>
				<%-- sequence-end--%>
					<c:if test="${status.last}">
						<c:if test="${vUser}">
							<table class="photoTitle">
								<tr>
									<td align="center">
										<div style="margin:5px 0 5px 0;">
											<input type="submit" name="deleteCommentAll" value="　削　　　除　" class="ttlbtn" />
										</div>
									</td>
								</tr>
							</table>
							<!--/photoTitle -->
						</c:if>
						</div>
						<!--/listBox10-->
					</c:if>
				<%--/sequence-end--%>
					</c:forEach>
					<!--/loop -->
					<!--/コメント一覧 -->
					<!-- コメント入力 -->
					<a name="add_comment"></a>
					<div class="listBox10">
						<div class="listBox10Title">
							<table class="ttl">
								<tr>
									<td class="ttl_name_sp"><div class="ttl_name inputColStick">コメントを入力する</div></td>
								</tr>
							</table>
						</div>
						<!--/listBox10Title -->
						<div style="padding:5px 0 0 10px;">
							<span>コメントの入力</span>
						</div>
						<div>
							<div style="border-style:dotted; border-width:1px 0 0 0; clear:both; height:1px;">&nbsp;</div>
							<div class="photoCommentboxReply">
								<dl>
									<dd class="clearfix">
										<ul>
											<li>
												<a href="javascript:void(0)" onclick="openEmojiPalette(document.getElementById('albumComment'), event); return false;" title="絵文字"><img src="/images/insert_icon001.gif" width="22" height="22" alt="絵文字" /></a>
												<!-- 絵文字パレット -->
												<%@ include file="/WEB-INF/view/common/emojiPalette.jsp" %>
												<!--/絵文字パレット -->
											</li>
										</ul>
									</dd>
									<dd>
										<html:textarea rows="6" cols="50" styleId="albumComment" property="albumComment" value="${albumComment}"></html:textarea>
									</dd>
								</dl>
							</div>
							<!--/photoCommentboxReply -->
						</div>
						<div>
							<table class="photoTitle">
								<tr>
									<td align="center">
										<div style="margin:5px 0 5px 0;">
											<input type="submit" name="addComment" value="　入　　　力　" class="ttlbtn" />
										</div>
									</td>
								</tr>
							</table>
							<!--/photoTitle -->
						</div>
					</div>
					<!--/listBox10 -->
					<!--/コメント入力 -->
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

