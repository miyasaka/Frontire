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
<title>[frontier]${f:h(tittleName)}</title>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>

<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/assort.css" type="text/css" />
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/style_grouplist.css" type="text/css" />
</head>


<body>
<s:form styleId="memForm">
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
								<td class="ttl_name_sp"><div class="ttl_name outColStick">${f:h(tittleName)}<c:if test="${resultscnt>0}">(<c:choose><c:when test="${(offset+1)==(offset + fn:length(resultsList))}">${resultscnt}</c:when><c:otherwise>${offset+1}～${offset + fn:length(resultsList)}</c:otherwise></c:choose>/${resultscnt}件)</c:if></div></td>
							</tr>
						</table>
					</div>
					<div class="linkBox01">
					<div class="note"><c:if test="${type eq '2' or type eq '3'}">最終ログイン　1時間以内：<span class="colorspan01">■</span>1日以内：<span class="colorspan02">■</span></c:if></div>
					<div>
						<c:choose><c:when test="${type eq '1'}">グループ</c:when><c:otherwise><s:link href="/pc/fmem/groupAll/">グループ</s:link></c:otherwise></c:choose>
						<span>&nbsp;</span>
						<c:choose><c:when test="${type eq '2'}">メンバー</c:when><c:otherwise><s:link href="/pc/fmem/index/">メンバー</s:link></c:otherwise></c:choose>
						<span>&nbsp;</span>
						<c:if test="${resultscnt>(offset + fn:length(resultsList))||offset>0}">|<span>&nbsp;</span></c:if><c:if test="${offset>0}"><s:link href="/pc/fmem/prepg/${f:h(type)}">&lt;&lt;前を表示</s:link></c:if><c:if test="${resultscnt>(offset + fn:length(resultsList))}"><span>&nbsp;</span><s:link href="/pc/fmem/nxtpg/${f:h(type)}">次を表示&gt;&gt;</s:link></c:if>
					</div>
					</div>
					<div id="listBoxGroupList">
						<div class="listBoxGroupListTitle">
							<table class="list_ttl">
								<tr>
									<td class="ttl_name_sp"><div class="ttl_name ${myflg eq '1'?'my':'mem'}ColStick">グループ</div></td>
								</tr>
							</table>
						</div>
					</div>
						<!-- 一覧 -->
<%-- 取得件数が0件以上の時、一覧表示 --%>
<c:choose>
<c:when test="${fn:length(resultsList)>0}">
						<table border="0" cellpadding="0" cellspacing="0" class="listBoxMemberListBody">
						<c:set var="row" value="5"/>
						<c:forEach var="e" items="${resultsList}" varStatus="i">
							<c:if test="${i.count%row==1}"><tr></c:if>
								<td width="20%">
									<div class="${fn:length(resultsList)<row?'line02':fn:length(resultsList)%row>fn:length(resultsList)-i.count?'line02':i.count%row==0?(i.count==fn:length(resultsList)?'':'line03'):fn:length(resultsList)%row==0&&fn:length(resultsList)-i.count<row?'line02':'line01'}">
									<c:choose>
										<c:when test="${type eq '1'}">
											<%-- グループ全件表示の場合 --%>
											<div class="mount04">
												<div class="img-back">
												<s:link href="/pc/fmem/group/${f:h(e.gid)}">
													<c:choose>
														<c:when test="${e.pic!=null and e.pic!=''}"><IMG SRC="${f:h(fn:replace(e.pic,'[dir]','pic76'))}" alt="${f:h(e.gname)}"></c:when>
														<c:otherwise><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg76.gif"  alt="" /></c:otherwise>
													</c:choose>
												</s:link>
												</div>
												<span>${f:h(e.gname)}(${f:h(e.joinnumber)})</span><br/>
											</div>
										</c:when>
										<c:otherwise>
											<div class="${f:h(e.lastlogin) == '03'?'mount01':f:h(e.lastlogin) == '02'?'mount03':'mount04'}">
												<div class="img-back">
												<s:link href="/pc/mem/${f:h(e.mid)}">
													<c:choose>
														<c:when test="${f:h(e.pic) != ''}"><img src="${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(e.pic,'dir','pic76')}" alt="${f:h(e.nickname)}"/></c:when>
														<c:otherwise><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg76.gif" alt="${f:h(e.nickname)}"/></c:otherwise>
													</c:choose>
												</s:link>
												</div>
												<span>${f:h(e.nickname)}</span><br/>
											</div>
										</c:otherwise>
									</c:choose>
									</div>
								</td>
							<c:if test="${i.count%row==0||(offset+i.count)==resultscnt}">
								<c:if test="${fn:length(resultsList)<row}">
									<c:forEach begin="1" end="${row - fn:length(resultsList)}" varStatus="i2">
										<td width="20%">&nbsp;</td>
									</c:forEach>
								</c:if>
								</tr>
							</c:if>
						</c:forEach>
						</table>
						<!-- 一覧 -->
</c:when>
<c:otherwise>
<div id="nodata">現在該当するデータはありません。</div>
</c:otherwise>
</c:choose>

					<div class="linkBox02">
						<div class="note"><c:if test="${type eq '2' or type eq '3'}">最終ログイン　1時間以内：<span class="colorspan01">■</span>1日以内：<span class="colorspan02">■</span></c:if></div>
						<div>
							<c:choose><c:when test="${type eq '1'}">グループ</c:when><c:otherwise><s:link href="/pc/fmem/groupAll/">グループ</s:link></c:otherwise></c:choose>
							<span>&nbsp;</span>
							<c:choose><c:when test="${type eq '2'}">メンバー</c:when><c:otherwise><s:link href="/pc/fmem/index/">メンバー</s:link></c:otherwise></c:choose>
							<span>&nbsp;</span>
							<c:if test="${resultscnt>(offset + fn:length(resultsList))||offset>0}">|<span>&nbsp;</span></c:if><c:if test="${offset>0}"><s:link href="/pc/fmem/prepg/${f:h(type)}">&lt;&lt;前を表示</s:link></c:if><c:if test="${resultscnt>(offset + fn:length(resultsList))}"><span>&nbsp;</span><s:link href="/pc/fmem/nxtpg/${f:h(type)}">次を表示&gt;&gt;</s:link></c:if>
						</div>
					</div>
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