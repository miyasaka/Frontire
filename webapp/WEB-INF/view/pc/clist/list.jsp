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
<title>[frontier]コミュニティ一覧</title>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>
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
	
	<div id="contents" class="clearfix">
		<div class="mainMember">
			<!--メイン-->

			<div class="mainMember-main">
			
				<div class="listBoxMemberList">
					<div class="listBoxMemberListTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name ${myflg eq '1'?'my':'mem'}ColStick">コミュニティ一覧(<c:choose><c:when test="${(offset+1)==(offset + fn:length(results))}">${resultscnt}</c:when><c:otherwise>${offset+1}～${offset + fn:length(results)}</c:otherwise></c:choose>/${resultscnt}件)</div></td>
							</tr>
						</table>
					</div>
					<div class="linkBox01"><div class="note">管理人：<span class="colorspan01">■</span></div><div>&nbsp;<c:if test="${offset>0}"><s:link href="movelist/${userInfoDto.visitMemberId}/${pgcnt-1}">&lt;&lt;前を表示</s:link></c:if><c:if test="${resultscnt>(offset + fn:length(results))}"><span name="sp">&nbsp;</span><s:link href="movelist/${userInfoDto.visitMemberId}/${pgcnt+1}">次を表示&gt;&gt;</s:link></c:if></div></div>
						<!-- 一覧 -->
<%-- 取得件数が0件以上の時、一覧表示 --%>
<c:if test="${resultscnt>0}">
						<table border="0" cellpadding="0" cellspacing="0" class="listBoxMemberListBody">
						<c:set var="row" value="5"/>
						<c:forEach var="e" items="${results}" varStatus="i">
							<c:if test="${i.count%row==1}"><tr></c:if>
								<td width="20%">
									<div class="${fn:length(results)<row?'line02':fn:length(results)%row>fn:length(results)-i.count?'line02':i.count%row==0?(i.count==fn:length(results)?'':'line03'):fn:length(results)%row==0&&fn:length(results)-i.count<row?'line02':'line01'}">
										<div class="${f:h(e.admflg) == '1'?'mount01':'mount04'}">
											<div class="img-back">
											<s:link href="/pc/com/top/${f:h(e.cid)}">
												<c:choose>
													<c:when test="${f:h(e.pic) != ''}"><img src="${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(e.pic,'dir','pic76')}" alt="${f:h(e.title)}"/></c:when>
													<c:otherwise><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg76.gif" alt="${f:h(e.title)}"/></c:otherwise>
												</c:choose>
											</s:link>
											</div>
											<span>${f:h(e.title)}(${f:h(e.memcnt)})</span><br/>

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
						<!-- 一覧 -->
</c:if>

					<div class="linkBox02"><div class="note">管理人：<span class="colorspan01">■</span></div><div>&nbsp;<c:if test="${offset>0}"><s:link href="movelist/${userInfoDto.visitMemberId}/${pgcnt-1}">&lt;&lt;前を表示</s:link></c:if><c:if test="${resultscnt>(offset + fn:length(results))}"><span name="sp">&nbsp;</span><s:link href="movelist/${userInfoDto.visitMemberId}/${pgcnt+1}">次を表示&gt;&gt;</s:link></c:if></div></div>
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
	<!--footer-->
	<%@ include file="/WEB-INF/view/pc/fnfooter.jsp"%>
	<!--footer-->
	 <!--/footer-->
</div>
</s:form>
<!--/container-->
</body>
</html>
