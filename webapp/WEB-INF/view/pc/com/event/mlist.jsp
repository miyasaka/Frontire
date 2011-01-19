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
<title>[frontier]イベント参加メンバー一覧</title>
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
	<%@ include file="/WEB-INF/view/pc/com/fmenu.jsp"%>
	<!-- マイページ共通 -->
	<!--/navbarメニューエリア-->

	<div id="contents" class="clearfix">
		<div class="mainMember">
			<!--メイン-->

			<div class="mainMember-main">
			
				<div class="listBoxMemberList">
					<div class="eventDelCommentHead clearfix" style="border-style:solid; border-width: 0 0 1px 0;">
						<ul>
							<li>${f:h(result[0].title)}&nbsp;&nbsp;イベント参加メンバー一覧</li>
						</ul>
					</div>
					<div class="listBoxMemberListTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name comColStick">メンバー一覧(<c:choose><c:when test="${(offset+1)==(offset + fn:length(results))}">${resultscnt}</c:when><c:otherwise>${offset+1}～${offset + fn:length(results)}</c:otherwise></c:choose>/${resultscnt}件)</div></td>
							</tr>
						</table>
					</div>
					
					<c:if test="${offset>0 || resultscnt>(offset + fn:length(results))}">
					<div class="linkBox01">
						<div class="note"></div>
						<div>
							<c:if test="${offset>0}">
								<s:link href="movelist/${communityDto.cid}/${pgcnt-1}">&lt;&lt;前を表示</s:link>
							</c:if>
							<span name="sp">&nbsp;</span>
							<c:if test="${resultscnt>(offset + fn:length(results))}">
								<s:link href="movelist/${communityDto.cid}/${pgcnt+1}">次を表示&gt;&gt;</s:link>
							</c:if>
						</div>
					</div>
					</c:if>
					
					<!-- 一覧 -->
<%-- 取得件数が0件以上の時、一覧表示 --%>
<c:if test="${resultscnt>0}">
					<table border="0" cellpadding="0" cellspacing="0" class="listBoxMemberListBody">
						<c:set var="row" value="5"/>
						<c:forEach var="e" items="${results}" varStatus="i">
							<c:if test="${i.count%row==1}"><tr></c:if>
								<td width="20%">
									<div class="${fn:length(results)<row?'line02':fn:length(results)%row>fn:length(results)-i.count?'line02':i.count%row==0?(i.count==fn:length(results)?'':'line03'):fn:length(results)%row==0&&fn:length(results)-i.count<row?'line02':'line01'}">
										<div class="mount04">
											<div class="img-back">
												<s:link href="/pc/mem/${f:h(e.mid)}" title="${f:h(e.nickname)}">
												<c:choose>
													<c:when test="${f:h(e.pic) != ''}">
														<img src="${appDefDto.FP_CMN_CONTENTS_ROOT}/${fn:replace(e.pic,'dir','pic76')}" alt="${f:h(e.nickname)}"/>
													</c:when>
													<c:otherwise><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg76.gif" alt="${f:h(e.nickname)}"/></c:otherwise>
												</c:choose>
												</s:link>
											</div>
											<span>${f:h(e.nickname)}さん</span>
										</div>
									</div>
								</td>
							<c:if test="${i.count%row==0||(offset+i.count)==resultscnt}">
								<c:if test="${fn:length(results)<row}">
									<c:forEach begin="1" end="${row - fn:length(results)}" varStatus="i2">
										<td width="20%">&nbsp;</td>
									</c:forEach>
								</c:if>
								</tr>
							</c:if>
						</c:forEach>
					</table>
</c:if>
					<!-- 一覧 -->

					<c:if test="${offset>0 || resultscnt>(offset + fn:length(results))}">
					<div class="linkBox02">
						<div class="note"></div>
						<div>
							<c:if test="${offset>0}">
								<s:link href="movelist/${communityDto.cid}/${pgcnt-1}">&lt;&lt;前を表示</s:link>
							</c:if>
							<span name="sp">&nbsp;</span>
							<c:if test="${resultscnt>(offset + fn:length(results))}">
								<s:link href="movelist/${communityDto.cid}/${pgcnt+1}">次を表示&gt;&gt;</s:link>
							</c:if>
						</div>
					</div>
					</c:if>
					
					<!--/listBoxMemberListTitle-->
					<div style="border-style:solid; border-width:1px 0 0 0; text-align:center; padding:10px 0;">
						<s:link href="/pc/com/event/view/${f:h(communityDto.cid)}/${f:h(bbsid)}">[${f:h(result[0].title)}]&nbsp;&nbsp;イベントへ</s:link>
					</div>

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
	 <!--/footer-->
</div>
<!--/container-->
</s:form>
</body>
</html>
