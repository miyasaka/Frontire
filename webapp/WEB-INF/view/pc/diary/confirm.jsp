<%@ page language="java" contentType="text/html; charset=windows-31J"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<title>[frontier] コメントを削除する</title>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/colorful.js"></script>
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

			<div class="mainDelDiary-main">
				<div class="listBox12 clearfix">
					<div class="listBox12Title">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name inputColStick">コメントの削除</div></td>
							</tr>
						</table>
					</div>
					<!--/listBox12Title-->
					<div class="listBoxHead clearfix">
						<div class="ttlHeadDelArea">下記のコメントを削除しますか？</div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<div class="bodyinputAreaSub clearfix">
									<ul>
										
										<li><input type="submit" name="exeDelete" value="削除する" /></li><html:hidden property="diaryId" value="${diaryId}"/><html:hidden property="checkCommentNo" value="${checkCommentNo}"/><html:hidden property="mid" value="${mid}"/>
										<li style="float:right;"><input type="submit" name="stop" value="やめる" /></li>
										
									</ul>
								</div>
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
				</div>
				<!--/listBox12-->
			</div>
			<!--/mainDelDiary-main-->

			<div class="mainDelDiary-main" style="margin-bottom:-10px;">
				<div class="listBox12 clearfix">
					<div class="listBox12Title">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name myColStick">コメント内容</div></td>
							</tr>
						</table>
					</div>
					<!--/listBox12Title-->
					
					<c:forEach var="e" items="${results}">
					<div class="listBoxHead clearfix">
						<div class="ttlHeadAreaList">
							<div class="ttlHeadAreaListSub">
								<dl>
									<dt class="txtArea">
										<c:choose>
										<c:when test="${(f:h(e.membertype)) eq '0'}">
											<c:choose>
												<c:when test="${e.mid eq userInfoDto.memberId}">
													${f:h(e.nickname)}
												</c:when>
												<c:otherwise>
													<c:if test="${e.status eq 1}">
														${f:h(e.nickname)}
													</c:if>
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:otherwise>
											<c:if test="${e.status eq 1}">
											<c:choose>
											<c:when test="${(f:h(e.membertype)) eq '1'}">
												<c:choose>
													<c:when test="${e.mid eq userInfoDto.memberId}">
														<%-- FrontierNetユーザ自身のコメントなので自分のFrontierへ戻る --%>
														${f:h(e.sfnickname)}
													</c:when>
													<c:otherwise>
														<c:if test="${e.status eq 1}">
															<c:choose>
																<c:when test="${(f:h(e.membertype)) eq '0'}">
																	${f:h(e.sfnickname)}
																</c:when>
																<c:when test="${(f:h(e.membertype)) eq '1'}">
																	<c:choose>
																	<c:when test="${frontierUserManagement.frontierdomain eq e.frontierdomain}">
																	${f:h(e.sfnickname)}
																	</c:when>
																	<c:otherwise>
																	${f:h(e.sfnickname)}
																	</c:otherwise>
																	</c:choose>
																</c:when>
															</c:choose>
														</c:if>
													</c:otherwise>
												</c:choose>
											</c:when>
											<c:when test="${(f:h(e.membertype)) eq '2'}">
												<c:choose><c:when test="${(f:h(e.guestName))==''}">名無しさん</c:when><c:otherwise>${f:h(e.guestName)}</c:otherwise></c:choose>
											</c:when>
											</c:choose>
											</c:if>
										</c:otherwise>
										</c:choose>
									</dt>
									<dd class="timeArea"><fmt:formatDate value="${f:date(f:h(e.entdate),'yyyyMMddHHmm')}" pattern="yyyy年MM月dd日 HH:mm" /></dd>
								</dl>
							</div>
						</div>
						<div class="bodyArea">
							<div class="bodyDelArea delEventCommentContents">
									<c:if test="${e.pic1!=null||e.pic2!=null||e.pic3!=null}">
										<div style="text-align:center;">
										<c:if test="${e.pic1!=null && e.pic1!=''}">
											<a href="javascript:void(0);" onClick="ff_viewBigimg('${f:h(appDefDto.FP_CMN_CONTENTS_ROOT)}${fn:replace(e.pic1,'dir','pic640')}');"<c:choose><c:when test="${f:h(e.picnote1)==null || f:h(e.picnote1)==''}"> title="画像１"</c:when><c:otherwise> title="${f:h(e.picnote1)}"</c:otherwise></c:choose>><img src="${f:h(appDefDto.FP_CMN_CONTENTS_ROOT)}${fn:replace(e.pic1,'dir','pic120')}" <c:choose><c:when test="${f:h(e.picnote1)==null || f:h(e.picnote1)==''}">alt="画像１"</c:when><c:otherwise>alt="${f:h(e.picnote1)}"</c:otherwise></c:choose>/></a>
										</c:if>
										<c:if test="${e.pic2!=null && e.pic2!=''}">
											<a href="javascript:void(0);" onClick="ff_viewBigimg('${f:h(appDefDto.FP_CMN_CONTENTS_ROOT)}${fn:replace(e.pic2,'dir','pic640')}');"<c:choose><c:when test="${f:h(e.picnote2)==null || f:h(e.picnote2)==''}"> title="画像２"</c:when><c:otherwise> title="${f:h(e.picnote2)}"</c:otherwise></c:choose>><img src="${f:h(appDefDto.FP_CMN_CONTENTS_ROOT)}${fn:replace(e.pic2,'dir','pic120')}" <c:choose><c:when test="${f:h(e.picnote2)==null || f:h(e.picnote2)==''}">alt="画像２"</c:when><c:otherwise>alt="${f:h(e.picnote2)}"</c:otherwise></c:choose>/></a>
										</c:if>
										<c:if test="${e.pic3!=null && e.pic3!=''}">
											<a href="javascript:void(0);" onClick="ff_viewBigimg('${f:h(appDefDto.FP_CMN_CONTENTS_ROOT)}${fn:replace(e.pic3,'dir','pic640')}');"<c:choose><c:when test="${f:h(e.picnote3)==null || f:h(e.picnote3)==''}"> title="画像３"</c:when><c:otherwise> title="${f:h(e.picnote3)}"</c:otherwise></c:choose>><img src="${f:h(appDefDto.FP_CMN_CONTENTS_ROOT)}${fn:replace(e.pic3,'dir','pic120')}" <c:choose><c:when test="${f:h(e.picnote3)==null || f:h(e.picnote3)==''}">alt="画像３"</c:when><c:otherwise>alt="${f:h(e.picnote3)}"</c:otherwise></c:choose>/></a>
										</c:if>
										</div>
									</c:if>
								${f:br(e.viewComment)}
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
					</c:forEach>
					
				</div>
				<!--/listBox12-->
			</div>
			<!--/mainDelDiary-main-->

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
