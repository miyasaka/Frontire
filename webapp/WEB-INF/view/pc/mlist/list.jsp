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
<title>[frontier]${f:h(myName)}</title>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/prototype.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/mlist.js"></script>

<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/assort.css" type="text/css" />
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/style_grouplist.css" type="text/css" />
</head>


<body>
<s:form styleId="memForm" action="group">
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
<c:if test="${type eq '1'}">
					<div class="groupTitleArea">
						<span>${f:h(gName)}</span>
					</div>
</c:if>
					<div class="listBoxMemberListTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name ${myflg eq '1'?'my':'mem'}ColStick">${f:h(myName)}<c:if test="${resultscnt>0}">(<c:choose><c:when test="${(offset+1)==(offset + fn:length(resultsList))}">${resultscnt}</c:when><c:otherwise>${offset+1}～${offset + fn:length(resultsList)}</c:otherwise></c:choose>/${resultscnt}件)</c:if></div></td>
							</tr>
						</table>
					</div>
					<div class="linkBox01"><div class="note">最終ログイン　1時間以内：<span class="colorspan01">■</span>1日以内：<span class="colorspan02">■</span></div><div><u onmouseover="setGroupList(event,this)">グループ</u><span>&nbsp;</span>
					<c:choose>
					<c:when test="${type eq '2'}">フォローしている</c:when>
					<c:otherwise><s:link href="follow/${userInfoDto.visitMemberId}">フォローしている</s:link></c:otherwise>
					</c:choose>
					<span>&nbsp;</span>
					<c:choose>
					<c:when test="${type eq '3'}">フォローされている</c:when>
					<c:otherwise><s:link href="follower/${userInfoDto.visitMemberId}">フォローされている</s:link></c:otherwise>
					</c:choose>
					<span>&nbsp;</span>
					<c:if test="${resultscnt>(offset + fn:length(resultsList))||offset>0}">|<span>&nbsp;</span></c:if>
					<c:if test="${offset>0}"><s:link href="movelist/${userInfoDto.visitMemberId}/${pgcnt-1}">&lt;&lt;前を表示</s:link></c:if>
					<c:if test="${resultscnt>(offset + fn:length(resultsList))}"><span>&nbsp;</span><s:link href="movelist/${userInfoDto.visitMemberId}/${pgcnt+1}">次を表示&gt;&gt;</s:link></c:if>
					</div></div>
					<div id="listBoxGroupList">
						<div class="listBoxGroupListTitle">
							<table class="list_ttl">
								<tr>
									<td class="ttl_name_sp"><div class="ttl_name ${myflg eq '1'?'my':'mem'}ColStick">グループ</div></td>
								</tr>
							</table>
						</div>
						<div class="listBoxBody">
							<div class="listBoxGroup">
								<div class="padding01">
									<b>所属グループ</b>
	<c:choose>
	<c:when test="${fn:length(groupList)>0}">
									<div class="listBoxGroupBody">
										<table>
	<c:forEach items="${groupList}" var="e" varStatus="i">
											<tr>
												<td>
													<c:choose>
													<c:when test="${e.pic!=null&&e.pic!=''}"><a href="#" onclick="setHidden('${e.gid}','${e.frontierdomain}');"><img src="${fn:replace(e.pic,'[dir]','pic42')}" alt="${f:h(e.gname)}"/></a></c:when>
													<c:otherwise><a href="#" onclick="setHidden('${e.gid}','${e.frontierdomain}');"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg42.gif" alt="${f:h(e.gname)}"/></a></c:otherwise>
													</c:choose>
												</td>
												<td><span>${f:h(e.gname)}(${e.joinnumber})</span></td>
											</tr>
	</c:forEach>
										</table>
									</div>
	</c:when>
	<c:otherwise><br>
	なし
	</c:otherwise>
	</c:choose>
								</div>

							</div>
						</div>
					</div>
						<!-- 一覧 -->
<%-- 取得件数が0件以上の時、一覧表示 --%>
<c:choose>
<c:when test="${resultscnt>0}">
						<table border="0" cellpadding="0" cellspacing="0" class="listBoxMemberListBody">
						<c:set var="row" value="5"/>
						<c:forEach var="e" items="${resultsList}" varStatus="i">
							<c:if test="${i.count%row==1}"><tr></c:if>
								<td width="20%">
									<div class="${fn:length(resultsList)<row?'line02':fn:length(resultsList)%row>fn:length(resultsList)-i.count?'line02':i.count%row==0?(i.count==fn:length(resultsList)?'':'line03'):fn:length(resultsList)%row==0&&fn:length(resultsList)-i.count<row?'line02':'line01'}">
										<div class="${f:h(e.lastlogin) == '03'?'mount01':f:h(e.lastlogin) == '02'?'mount03':'mount04'}">
											<div class="img-back">

<c:choose>
<c:when test="${e.membertype == '0'}">
											<!-- 通常ユーザ -->
											<s:link href="/pc/mem/${f:h(e.mid)}">
												<c:choose>
													<c:when test="${f:h(e.pic) != ''}"><img src="${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(e.pic,'dir','pic76')}" alt="${f:h(e.nickname)}"/></c:when>
													<c:otherwise><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg76.gif" alt="${f:h(e.nickname)}"/></c:otherwise>
												</c:choose>
											</s:link>
											</div>
											<span>${f:h(e.nickname)}</span><br/>
											<c:if test="${myflg=='1'&&type=='2'}">
												<s:link href="/pc/mlist/confirm/${f:h(e.mid)}">フォロー解除</s:link>
											</c:if>
</c:when>
<c:otherwise>
	<c:choose>
		<c:when test="${e.frontierdomain == userInfoDto.fdomain}">
		<%-- 自分と同じFrontierユーザの場合 --%>
											<!-- 同じ外部Frontierユーザ -->
											<a href="http://${e.frontierdomain}/frontier/pc/mem/${e.fid}">
		</c:when>
		<c:otherwise>
		<%-- それ以外 --%>
											<!-- 外部Frontierユーザ -->
											<a href="http://${e.frontierdomain}/frontier/pc/openid/auth/?gm=mv&cid=${e.fid}&openid=${userInfoDto.fdomain}/frontier/pc/openidserver">
		</c:otherwise>
	</c:choose>
												<c:choose>
													<c:when test="${f:h(e.fpic) != ''}"><img src="${fn:replace(e.fpic,'dir','pic76')}" alt="${f:h(e.lfnickname)}"/></c:when>
													<c:otherwise><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg76.gif" alt="${f:h(e.lfnickname)}"/></c:otherwise>
												</c:choose>
											</a>
											</div>
											<span>${f:h(e.sfnickname)}</span><br/>
</c:otherwise>
</c:choose>
										</div>
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
<div id="nodata">現在${f:h(myName)}ユーザーはいません</div>
</c:otherwise>
</c:choose>
					<div class="linkBox02"><div class="note">最終ログイン　1時間以内：<span class="colorspan01">■</span>1日以内：<span class="colorspan02">■</span></div><div><u onmouseover="setGroupList(event,this)">グループ</u><span>&nbsp;</span>
					<c:choose>
					<c:when test="${type eq '2'}">フォローしている</c:when>
					<c:otherwise><s:link href="follow/${userInfoDto.visitMemberId}">フォローしている</s:link></c:otherwise>
					</c:choose>
					<span>&nbsp;</span>
					<c:choose>
					<c:when test="${type eq '3'}">フォローされている</c:when>
					<c:otherwise><s:link href="follower/${userInfoDto.visitMemberId}">フォローされている</s:link></c:otherwise>
					</c:choose>
					<span>&nbsp;</span>
					<c:if test="${resultscnt>(offset + fn:length(resultsList))||offset>0}">|<span>&nbsp;</span></c:if>
					<c:if test="${offset>0}"><s:link href="movelist/${userInfoDto.visitMemberId}/${pgcnt-1}">&lt;&lt;前を表示</s:link></c:if>
					<c:if test="${resultscnt>(offset + fn:length(resultsList))}"><span>&nbsp;</span><s:link href="movelist/${userInfoDto.visitMemberId}/${pgcnt+1}">次を表示&gt;&gt;</s:link></c:if>
					</div>
					</div>
					<!--/listBoxMemberListTitle-->

				</div>
				<!--/listBoxMemberList-->
				<c:if test="${myflg=='1'&&type=='2'}"><div style="color:#ff0000;">※外部Frontier メンバーのフォロー解除を行う場合は、直接メンバーページへ遷移してフォローを解除してください。</div></c:if>

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
<html:hidden value="${type}" property="type"/>
<html:hidden value="" property="gid" styleId="gid"/>
<html:hidden value="" property="domain" styleId="domain"/>
</s:form>
<!--/container-->
</body>
</html>