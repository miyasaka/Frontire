<%@ page language="java" contentType="text/html; charset=windows-31J" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<title>[frontier] ${f:h(vNickname)}さん</title>
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/assort.css" type="text/css" />
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/mem.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/prototype.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/common.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/fshout.js"></script>
</head>

<body>
<s:form>
<div id="container">
<%@ include file="/WEB-INF/view/pc/fheader.jsp"%>
<!-- ヘッダー -->
	<!--/header-->
	<%@ include file="/WEB-INF/view/pc/fmenu.jsp"%>
	<!--/navbar-->

	<div id="contents" class="clearfix">
		<div class="main">
			<!--メイン-->
			<div class="main-side">

				<div class="listBoxMember">
					<div  class="listBoxMemberTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name memColStick"><s:link href="/pc/profile2/edit/${f:h(userInfoDto.visitMemberId)}" title="ｲﾒｰｼﾞ">ｲﾒｰｼﾞ</s:link></div></td>
							</tr>
						</table>
					</div>
					<div class="listBoxBody">
						<div id="listBoxImage">
							<c:choose>
							<c:when test="${FriendInfo.photo!=null}">
							<img src="${f:h(appDefDto.FP_CMN_CONTENTS_ROOT)}${fn:replace(FriendInfo.photo,'dir','pic180')}" alt="${f:h(vNickname)}" />
							</c:when>
							<c:otherwise>
							<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg180.gif" alt="${f:h(vNickname)}" />
							</c:otherwise>
							</c:choose>
							<div>${f:h(vNickname)}さん</div>
							<div class="lastlogin">（最終ログインは${f:h(FriendInfo.logindate)}）</div>
						</div>
<c:if test="${FriendStatus!=3}">
						<div class="listBoxVisit">
							<c:choose>
							<c:when test="${followFlg}">
								<input type="submit" name="release" value="フォロー解除"/>

							</c:when>
							<c:otherwise>
								<input type="submit" name="follow" value="フォローする"/>

							</c:otherwise>
							</c:choose>
							<input type="hidden" name="frommid" value="${cid}"/>
						</div>
</c:if>
					</div>
				</div>


				<div class="listBoxMember">
					<div  class="listBoxMemberTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name memColStick">活動状況</div></td>
							</tr>
						</table>

					</div>
					<div class="listBoxBody">
						<%--グループ--%>
						<div class="listBoxGroup">

							<div>
								<span style="font-weight:bold;">所属グループ</span>
							</div>
<c:choose>
<c:when test="${fn:length(GMList)>0}">
							<table border="0" style="width:100%;">
<c:forEach var="d" items="${GMList}" varStatus="i">
								<tr>
									<td style="text-align:center; width:50px;">
										<c:choose>
										<c:when test="${d[0].pic!=''}">
											<img src="${fn:replace(d[0].pic,'[dir]','pic42')}" title="${f:h(d[0].gname)}" alt="${f:h(d[0].gname)}"/>
										</c:when>
										<c:otherwise>
											<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg42.gif" title="${f:h(d[0].gname)}" alt="${f:h(d[0].gname)}"/>
										</c:otherwise>
										</c:choose>
									</td>
									<td class="grouptxt">
										<div <c:if test="${d[0].joinnumber>0}">onmouseover="setVisibleOK('0','${f:h(d[0].frontierdomain)}','${f:h(d[0].gid)}',event);"</c:if> style="z-index:3;">${f:h(d[0].gname)}(${d[0].joinnumber})</div>
										<div id="ABox_${d[0].frontierdomain}_${d[0].gid}"  style="background-color:#ffffff; z-index:5; width:180px; display:none; position:absolute; margin-bottom:-10px;" onmouseout="setVisibleNG('${d[0].frontierdomain}','${d[0].gid}')" class="listBoxMember" onmouseover="setVisibleOK('1','${d[0].frontierdomain}','${d[0].gid}',event);">
											<div  class="listBoxMemberTitle" style="cursor:default;">
												<table class="list_ttl" style="cursor:default;">
													<tr>
														<td class="ttl_name_sp"><div class="ttl_name memColStick"><a href="javascript:void(0);goGroup('/frontier/pc/mlist/group/','${d[0].frontierdomain}','${d[0].gid}','${userInfoDto.visitMemberId}');">所属メンバー(${d[0].joinnumber})</a></div></td>
													</tr>
												</table>
											</div>
											<div class="listBoxBody" style="cursor:default;" onmouseover="setVisibleOK('1','${d[0].frontierdomain}','${d[0].gid}',event);">
												<div id="listBoxCommunity">
													<table>
														<c:set var="row" value="3"/>
														<c:set var="ml" value="${fn:length(d[1])}"/>
														<c:forEach var="e" items="${d[1]}" varStatus="j">
														<c:if test="${j.count%row==1}"><tr></c:if><td align="center" style="width:42px;">
														<c:choose>
														<c:when test="${f:h(e.pic) != ''}"><s:link href="/pc/mem/${f:h(e.mid)}" title="${f:h(e.nickname)}"><img src="${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(e.pic,'dir','pic42')}" alt="${f:h(e.nickname)}"/></s:link></c:when>
														<c:otherwise><s:link href="/pc/mem/${f:h(e.mid)}" title="${f:h(e.nickname)}"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg42.gif" alt="${f:h(e.nickname)}"/></s:link></c:otherwise>
														</c:choose>
														</td>
														<c:choose>
														<c:when test="${j.count==ml}">
														<c:if test="${j.count%row!=0}">
														<c:forEach begin="1" end="${row-j.count%row}">
														<td style="width:42px;">&nbsp;</td>
														</c:forEach>
														</c:if>
														</tr>
														</c:when>
														<c:otherwise><c:if test="${j.count%row==0}"></tr></c:if></c:otherwise>
														</c:choose>
														</c:forEach>
														<tr>
															<td style="text-align:right;" colspan="3"><a href="javascript:void(0);goGroup('/frontier/pc/mlist/group/','${d[0].frontierdomain}','${d[0].gid}','${userInfoDto.visitMemberId}');">全て見る</a></td>
														</tr>
													</table>
												</div>
											</div>
										</div>
										<!--/listBoxMember-->
									</td>
								</tr>
</c:forEach>
							</table>
</c:when>
<c:otherwise>
まだ所属していません。
</c:otherwise>
</c:choose>
						</div>
						<!--/listBoxGroup-->
						<%--/フォローされている--%>

						<div class="listBoxFollow">
							<%--フォローしている--%>
							<div class="sublistBoxFollow">
								<span style="cursor:text;" <c:if test="${followCnt>0}">onmouseover="setFollowList('1',event,this)"</c:if>>フォローしている(${followCnt})</span>
									<div class="listBoxMember"  id="memFollowList"  style="background-color:#ffffff; position:absolute; margin-left:100px; display:none; z-index:5; width:180px;">
										<div  class="listBoxMemberTitle" style="cursor:default;">
											<table class="list_ttl" style="cursor:default;">
												<tr>
													<td class="ttl_name_sp"><div class="ttl_name memColStick"><s:link href="/pc/mlist/follow/${userInfoDto.visitMemberId}">フォローしている(${f:h(followCnt)})</s:link></div></td>
												</tr>
											</table>
										</div>
										<div class="listBoxBody" style="cursor:default;">
											<div id="listBoxCommunity">
												<table>
													<c:set var="row" value="3"/>
													<c:forEach var="e" items="${FollowyouList}" varStatus="i" end="${appDefDto.FP_MEM_TOPLIST_PGMAX-1}">
													<c:if test="${i.count%row==1}"><tr></c:if><td align="center" style="width:42px;">
														<c:choose>
															<c:when test="${(f:h(e.membertype)) eq '0'}">
																<%--自Frontier--%>
																<c:choose>
																	<c:when test="${f:h(e.pic) != ''}">
																		<%--写真がある--%>
																		<s:link href="/pc/mem/${f:h(e.mid)}" title="${f:h(e.nickname)}"><img src="${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(e.pic,'dir','pic42')}" alt="${f:h(e.nickname)}"/></s:link>
																	</c:when>
																	<c:otherwise>
																		<%--写真がない--%>
																		<s:link href="/pc/mem/${f:h(e.mid)}" title="${f:h(e.nickname)}"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg42.gif" alt="${f:h(e.nickname)}"/></s:link>
																	</c:otherwise>
																</c:choose>
															</c:when>
															<c:when test="${(f:h(e.membertype)) eq '1'}">
																<%--他Frontier--%>
																<c:choose>
																	<c:when test="${f:h(e.fpic) != ''}">
																		<%--写真がある--%>
																		<c:choose>
																			<c:when test="${frontierUserManagement.frontierdomain eq e.frontierdomain}">
																				<s:link href="http://${f:h(frontierUserManagement.frontierdomain)}/frontier/pc/mem/${f:h(e.fid)}/" title="${f:h(e.lfnickname)}"><img src="${fn:replace(e.fpic,'dir','pic42')}" alt="${f:h(e.lfnickname)}"/></s:link>
																			</c:when>
																			<c:otherwise>
																				<s:link href="http://${f:h(e.frontierdomain)}/frontier/pc/openid/auth?cid=${f:h(e.fid)}&gm=mv&openid=${f:h(frontierUserManagement.frontierdomain)}/frontier/pc/openidserver" title="${f:h(e.lfnickname)}"><img src="${fn:replace(e.fpic,'dir','pic42')}" alt="${f:h(e.lfnickname)}"/></s:link>
																			</c:otherwise>
																		</c:choose>
																	</c:when>
																	<c:otherwise>
																		<%--写真がない--%>
																		<c:choose>
																			<c:when test="${frontierUserManagement.frontierdomain eq e.frontierdomain}">
																				<s:link href="http://${f:h(frontierUserManagement.frontierdomain)}/frontier/pc/mem/${f:h(e.fid)}/" title="${f:h(e.lfnickname)}"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg42.gif" alt="${f:h(e.lfnickname)}"/></s:link>
																			</c:when>
																			<c:otherwise>
																				<s:link href="http://${f:h(e.frontierdomain)}/frontier/pc/openid/auth?cid=${f:h(e.fid)}&gm=mv&openid=${f:h(frontierUserManagement.frontierdomain)}/frontier/pc/openidserver" title="${f:h(e.lfnickname)}"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg42.gif" alt="${f:h(e.lfnickname)}"/></s:link>
																			</c:otherwise>
																		</c:choose>
																	</c:otherwise>
																</c:choose>
															</c:when>
														</c:choose>
													</td>
													<c:choose>
													<c:when test="${i.count==followCnt}">
													<c:if test="${i.count%row!=0}">
													<c:forEach begin="1" end="${row-i.count%row}">
													<td style="width:42px;">&nbsp;</td>
													</c:forEach>
													</c:if>
													</tr>
													</c:when>
													<c:otherwise><c:if test="${i.count%row==0}"></tr></c:if></c:otherwise>
													</c:choose>
													</c:forEach>
													<tr>
														<td style="text-align:right;" colspan="3"><s:link href="/pc/mlist/follow/${userInfoDto.visitMemberId}">全て見る</s:link></td>
													</tr>
												</table>
											</div>
										</div>
									</div>
									<!--/listBoxMember-->

							</div>
							<%--/フォローしている--%>
							<%--フォローされている--%>
							<div class="sublistBoxFollow">
								<span style="cursor:text;" <c:if test="${followerCnt>0}">onmouseover="setFollowerList('2',event,this)"</c:if>>フォローされている(${followerCnt})</span>
									<div class="listBoxMember" id="memFollowerList"  style="background-color:#ffffff; position:absolute; margin-left:100px; display:none; z-index:5; width:180px;">
										<div  class="listBoxMemberTitle" style="cursor:default;">
											<table class="list_ttl" style="cursor:default;">
												<tr>
													<td class="ttl_name_sp"><div class="ttl_name memColStick"><s:link href="/pc/mlist/follower/${userInfoDto.visitMemberId}">フォローされている(${f:h(followerCnt)})</s:link></div></td>
												</tr>
											</table>
										</div>
										<div class="listBoxBody" style="cursor:default;">
											<div id="listBoxCommunity">
												<table>
													<c:set var="row" value="3"/>
													<c:forEach var="e" items="${FollowmeList}" varStatus="i" end="${appDefDto.FP_MEM_TOPLIST_PGMAX-1}">
														<c:if test="${i.count%row==1}"><tr></c:if><td align="center" style="width:42px;">
														<c:choose>
															<c:when test="${(f:h(e.membertype)) eq '0'}">
																<%--自Frontier--%>
																<c:choose>
																	<c:when test="${f:h(e.pic) != ''}">
																		<%--写真がある--%>
																		<s:link href="/pc/mem/${f:h(e.mid)}" title="${f:h(e.nickname)}"><img src="${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(e.pic,'dir','pic42')}" alt="${f:h(e.nickname)}"/></s:link>
																	</c:when>
																	<c:otherwise>
																		<%--写真がない--%>
																		<s:link href="/pc/mem/${f:h(e.mid)}" title="${f:h(e.nickname)}"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg42.gif" alt="${f:h(e.nickname)}"/></s:link>
																	</c:otherwise>
																</c:choose>
															</c:when>
															<c:when test="${(f:h(e.membertype)) eq '1'}">
																<%--他Frontier--%>
																<c:choose>
																	<c:when test="${f:h(e.fpic) != ''}">
																		<%--写真がある--%>
																		<c:choose>
																			<c:when test="${frontierUserManagement.frontierdomain eq e.frontierdomain}">
																				<s:link href="http://${f:h(frontierUserManagement.frontierdomain)}/frontier/pc/mem/${f:h(e.fid)}/" title="${f:h(e.lfnickname)}"><img src="${fn:replace(e.fpic,'dir','pic42')}" alt="${f:h(e.lfnickname)}"/></s:link>
																			</c:when>
																			<c:otherwise>
																				<s:link href="http://${f:h(e.frontierdomain)}/frontier/pc/openid/auth?cid=${f:h(e.fid)}&gm=mv&openid=${f:h(frontierUserManagement.frontierdomain)}/frontier/pc/openidserver" title="${f:h(e.lfnickname)}"><img src="${fn:replace(e.fpic,'dir','pic42')}" alt="${f:h(e.lfnickname)}"/></s:link>
																			</c:otherwise>
																		</c:choose>
																	</c:when>
																	<c:otherwise>
																		<%--写真がない--%>
																		<c:choose>
																			<c:when test="${frontierUserManagement.frontierdomain eq e.frontierdomain}">
																				<s:link href="http://${f:h(frontierUserManagement.frontierdomain)}/frontier/pc/mem/${f:h(e.fid)}/" title="${f:h(e.lfnickname)}"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg42.gif" alt="${f:h(e.lfnickname)}"/></s:link>
																			</c:when>
																			<c:otherwise>
																				<s:link href="http://${f:h(e.frontierdomain)}/frontier/pc/openid/auth?cid=${f:h(e.fid)}&gm=mv&openid=${f:h(frontierUserManagement.frontierdomain)}/frontier/pc/openidserver" title="${f:h(e.lfnickname)}"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg42.gif" alt="${f:h(e.lfnickname)}"/></s:link>
																			</c:otherwise>
																		</c:choose>
																	</c:otherwise>
																</c:choose>
															</c:when>
														</c:choose>
														</td>
														<c:choose>
															<c:when test="${i.count==followerCnt}">
																<c:if test="${i.count%row!=0}">
																	<c:forEach begin="1" end="${row-i.count%row}">
																		<td style="width:42px;">&nbsp;</td>
																	</c:forEach>
																</c:if>
																</tr>
															</c:when>
															<c:otherwise><c:if test="${i.count%row==0}"></tr></c:if></c:otherwise>
														</c:choose>
													</c:forEach>
													<tr>
														<td style="text-align:right;" colspan="3"><s:link href="/pc/mlist/follower/${userInfoDto.visitMemberId}">全て見る</s:link></td>
													</tr>
												</table>
											</div>
										</div>
									</div>
									<!--/listBoxMember-->
							</div>
							<%--/フォローされている--%>
							<c:if test="${userInfoDto.membertype=='0' && userInfoDto.visitMemberType=='0'}">
							<div class="sublistBoxFollow">
								<span style="cursor:text;" <c:if test="${resultscnt>0}">onmouseover="setComList('3',event,this)"</c:if>>参加コミュニティ(${resultscnt})</span>
									<div class="listBoxMember" id="memComList"  style="background-color:#ffffff; position:absolute; margin-left:100px; display:none; z-index:5; width:180px;">
										<div  class="listBoxMemberTitle" style="cursor:default;">
											<table class="list_ttl" style="cursor:default;">
												<tr>
													<td class="ttl_name_sp"><div class="ttl_name memColStick"><s:link href="/pc/clist/${f:h(userInfoDto.visitMemberId)}">参加コミュニティ(${f:h(resultscnt)})</s:link></div></td>
												</tr>
											</table>
										</div>
										<div class="listBoxBody" style="cursor:default;">
											<div id="listBoxCommunity">
												<table>
													<c:set var="row" value="3"/>
													<c:forEach var="e" items="${CommunityList}" varStatus="i" end="${appDefDto.FP_MEM_TOPLIST_PGMAX-1}">
													<c:if test="${i.count%row==1}"><tr></c:if><td align="center" style="width:42px;">
													<c:choose>
													<c:when test="${f:h(e.pic) != ''}"><s:link href="/pc/com/top/${f:h(e.cid)}" title="${f:h(e.title)}(${f:h(e.memcnt)})"><img src="${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(e.pic,'dir','pic42')}" alt="${f:h(e.title)}(${f:h(e.memcnt)})"/></s:link></c:when>
													<c:otherwise><s:link href="/pc/com/top/${f:h(e.cid)}" title="${f:h(e.title)}(${f:h(e.memcnt)})"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg42.gif" alt="${f:h(e.title)}(${f:h(e.memcnt)})"/></s:link></c:otherwise>
													</c:choose>
													</td>
													<c:choose>
													<c:when test="${i.count==resultscnt}">
													<c:if test="${i.count%row!=0}">
													<c:forEach begin="1" end="${row-i.count%row}">
													<td style="width:42px;">&nbsp;</td>
													</c:forEach>
													</c:if>
													</tr>
													</c:when>
													<c:otherwise><c:if test="${i.count%row==0}"></tr></c:if></c:otherwise>
													</c:choose>
													</c:forEach>
													<tr>
														<td style="text-align:right;" colspan="3"><s:link href="/pc/clist/${f:h(userInfoDto.visitMemberId)}" title="ｺﾐｭﾆﾃｨ（${resultscnt}）">全て見る</s:link></td>
													</tr>
												</table>
											</div>
										</div>
									<!--/listBoxMember-->
								</div>
							</div>
							</c:if>
						</div>
						<!--listBoxFollow-->


					</div>
					<!--/listBoxBody-->
				</div>
				<!--/listBoxMember-->








			</div>
			<!--/main-side-->
			<div class="main-main">
				<!--ﾌﾟﾛﾌｨｰﾙ-->
				<div class="listBoxMember02 clearfix">
					<div class="listBoxMember02Title">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name memColStick">ﾌﾟﾛﾌｨｰﾙ</div></td>
							</tr>
						</table>
					</div>
					<div class="listBoxBody">
						<div class="profileBox">
							<ul>
								<c:if test="${FriendInfo.pubName <= FriendStatus}">
								<li class="clearfix"><dl><dt>名前</dt><dd>${f:h(FriendInfo.familyname)} ${f:h(FriendInfo.name)}</dd></dl></li>
								</c:if>
								<c:if test="${FriendInfo.pubGender <= FriendStatus}">
								<li class="clearfix"><dl><dt>性別</dt><dd>${f:h(FriendInfo.gender)}</dd></dl></li>
								</c:if>
								<c:if test="${FriendInfo.pubResidence <= FriendStatus}">
								<li class="clearfix"><dl><dt>現住所</dt><dd>${f:h(FriendInfo.residenceregion)}${f:h(FriendInfo.residencecity)}</dd></dl></li>
								</c:if>
								<c:if test="${FriendInfo.pubYearofbirth <= FriendStatus}">
								<li class="clearfix"><dl><dt>年齢</dt><dd>${f:h(FriendInfo.age)}</dd></dl></li>
								</c:if>
								<c:if test="${FriendInfo.pubDateofbirth <= FriendStatus}">
								<li class="clearfix"><dl><dt>誕生日</dt><dd>${f:h(FriendInfo.dateofbirth)}</dd></dl></li>
								</c:if>
								<c:if test="${FriendInfo.bloodtype!=null}">
								<li class="clearfix"><dl><dt>血液型</dt><dd>${f:h(FriendInfo.bloodtype)}</dd></dl></li>
								</c:if>
								<c:if test="${FriendInfo.pubHometown <= FriendStatus && FriendInfo.hometownregion !=null}">
								<li class="clearfix"><dl><dt>出身地</dt><dd>${f:h(FriendInfo.hometownregion)}${f:h(FriendInfo.hometowncity)}</dd></dl></li>
								</c:if>
								<c:if test="${FriendInfo.interest1!=null}">
								<li class="clearfix"><dl><dt>趣味</dt><dd>${f:h(FriendInfo.interest1)}</dd></dl></li>
								</c:if>
								<c:if test="${FriendInfo.pubOccupation <= FriendStatus}">
								<li class="clearfix"><dl><dt>職業</dt><dd>${f:h(FriendInfo.occupation)}</dd></dl></li>
								</c:if>
								<c:if test="${FriendInfo.pubCompany <= FriendStatus && FriendInfo.company !='' && FriendInfo.company !=null}">
								<li class="clearfix"><dl><dt>所属</dt><dd>${f:h(FriendInfo.company)}</dd></dl></li>
								</c:if>
								<li class="${endPos eq '1'?'end':''} clearfix"><dl><dt>自己紹介</dt><dd class="userInput">${f:br(FriendInfo.aboutme)}</dd></dl></li>
								<c:if test="${f:h(FriendInfo.favgenre1) != '' && f:h(FriendInfo.favcontents1) != ''}">
								<li class="${endPos eq '2'?'end':''} clearfix"><dl><dt>好きな${f:h(FriendInfo.favgenre1)}</dt><dd>${f:h(FriendInfo.favcontents1)}</dd></dl></li>
								</c:if>
								<c:if test="${f:h(FriendInfo.favgenre2) != '' && f:h(FriendInfo.favcontents2) != ''}">
								<li class="${endPos eq '3'?'end':''} clearfix"><dl><dt>好きな${f:h(FriendInfo.favgenre2)}</dt><dd>${f:h(FriendInfo.favcontents2)}</dd></dl></li>
								</c:if>
								<c:if test="${f:h(FriendInfo.favgenre3) != '' && f:h(FriendInfo.favcontents3) != ''}">
								<li class="${endPos eq '4'?'end':''} clearfix"><dl><dt>好きな${f:h(FriendInfo.favgenre3)}</dt><dd>${f:h(FriendInfo.favcontents3)}</dd></dl></li>
								</c:if>
							</ul>
						</div>
					</div>
				</div>
				<!--/ﾌﾟﾛﾌｨｰﾙ-->
				<!--/listBoxMember02-->
<c:if test="${fn:length(RecentDiary)>0}">
				<!--ﾒﾝﾊﾞｰ日記-->
				<div class="listBoxMember02 clearfix">
					<div class="listBoxMember02Title">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name memColStick"><s:link href="/pc/diary/list/${f:h(userInfoDto.visitMemberId)}" title="ﾒﾝﾊﾞｰ日記">ﾒﾝﾊﾞｰ日記</s:link></div></td>
							</tr>
						</table>
					</div>
					<div class="listBoxBody">
						<div class="memDiaryBox">
							<ul>
								<c:forEach var="e" items="${RecentDiary}">
								<c:choose>
								<c:when test="${fMemType=='0'}">
								<li class="clearfix"><dl><dt>${e.entdate}</dt>
								<dd><s:link href="/pc/diary/view/${e.diaryid}/${e.yyyymmdd}/${userInfoDto.visitMemberId}" title="${f:h(e.ltitle)}">${f:h(e.title)}</s:link></dd>
								</c:when>
								<c:otherwise>
								<li class="clearfix"><dl><dt>${e.entdatel}</dt>
								<dd><s:link href="/pc/diary/view/${e.diaryid}/${fn:substring(f:h(e.entdate),0,8)}/${userInfoDto.visitMemberId}" title="${f:h(e.title)}">${f:h(e.title)}</s:link></dd>
								</c:otherwise>
								</c:choose>
								</dl></li>
								</c:forEach>
							</ul>
							<div class="listBoxBodyBottom"><s:link href="/pc/diary/list/${f:h(userInfoDto.visitMemberId)}">一覧へ</s:link></div>
						</div>
					</div>
				</div>
				<!--/ﾒﾝﾊﾞｰ日記-->
</c:if>
				<!--/listBoxMember02-->
<c:if test="${fn:length(RecentAlbum)>0}">

				<!--ﾒﾝﾊﾞｰﾌｫﾄ-->
				<c:if test="${userInfoDto.membertype=='0' && userInfoDto.visitMemberType=='0'}">
				<div class="listBoxMember02 clearfix">
					<div class="listBoxMember02Title">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name memColStick"><s:link href="/pc/photo/list/${userInfoDto.visitMemberId}" title="ﾒﾝﾊﾞｰﾌｫﾄ">ﾒﾝﾊﾞｰﾌｫﾄ</s:link></div></td>
							</tr>
						</table>
					</div>
					<div class="listBoxBody">
						<div class="newAlbumBox">
							<table border="0">
								<c:forEach var="e" items="${RecentAlbum}">
								<tr>
									<td class="albumimg">

										<c:choose>
										<c:when test="${e.pic!=null}">
										<img src="${f:h(appDefDto.FP_CMN_CONTENTS_ROOT)}${fn:replace(e.pic,'dir','pic42')}" alt="${f:h(e.ltitle)}" />
										</c:when>
										<c:otherwise>
										<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg42.gif" alt="${f:h(e.ltitle)}" />
										</c:otherwise>
										</c:choose>
									</td>
									<td class="albumdate">${e.entdate}</td>
									<td class="albumtxt"><s:link href="/pc/photo/view/${userInfoDto.visitMemberId}/${e.ano}" title="${f:h(e.ltitle)}">${f:h(e.title)}</s:link></td>
								</tr>
								</c:forEach>
							</table>
							<div class="listBoxBodyBottom"><s:link href="/pc/photo/list/${userInfoDto.visitMemberId}">一覧へ</s:link></div>
						</div>



					</div>
				</div>
				</c:if>
				<!--/ﾒﾝﾊﾞｰﾌｫﾄ-->
				<!--/listBoxMember02-->
</c:if>

			</div>
			<!--/main-main-->
		</div>
		<!--/main-->

		<!--/side-->
		<!-- F Shout一覧の表示 -->
		<div class="side">

			<!--/listBox04-->
			<div class="listBox04">
				<div  class="listBox04Title">
					<table class="list_ttl">
						<tr>
							<td class="ttl_name_sp"><div class="ttl_name memColStick"><s:link href="/pc/fshout/list/${userInfoDto.visitMemberId}/1">F&nbsp;Shout</s:link></div></td>
						</tr>
					</table>
				</div>
				<div class="listBoxBody">
					<div class="listBoxBodyArea">
<c:choose>
<c:when test="${fn:length(FShoutList)==0}">
						<!-- まだ1件も投稿していない時 -->
						<div class="listBoxBodyAreaHead">
							<table border="0" style="margin:5px;"><tr><td align="center">
									&nbsp;<br/>
									まだ何もありません。<br/>
									&nbsp;
							</td></tr></table>
						</div>
						<div class="listBoxBodyAreaBody">&nbsp;&nbsp;-&gt;&nbsp;<a href="javascript:void(0);refscmnt('http://${userInfoDto.fdomain}/frontier/pc/top/','0','[@:${FriendInfo.frontierdomain},${FriendInfo.fidnum}]','','');">${f:h(vNickname)}さん宛に投稿する</a></div>
</c:when>
<c:otherwise>
						<!-- 1件以上投稿している時 -->
<c:forEach var="e" items="${FShoutList}" varStatus="i">
						<div class="listBoxBodyAreaHead">
							<table border="0" style="margin:5px;">
								<tr>
									<td>
										<c:if test="${e.twitter=='1'}"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/twitter.gif" alt="Twitter"/></c:if>${e.viewComment}
									</td>
								</tr>
								<tr>
									<td style="font-size:12px;color:#808080;">
										<span style="vertical-align:middle;">${f:h(e.entdate)}&nbsp;&nbsp;</span><a href="javascript:void(0);refscmnt('http://${userInfoDto.fdomain}/frontier/pc/top/','1','${e.jsComment}','0','${e.mid}:${e.no}');" title="RT"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/link_s.png"/></a>&nbsp;<a href="javascript:void(0);refscmnt('http://${userInfoDto.fdomain}/frontier/pc/top/','0','${e.memberTag}','${e.jsCommentView}','${e.mid}:${e.no}');" title="返信"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/arrow_redo_s.png"/></a>
									</td>
								</tr>
								<tr>
									<td style="font-size:12px;">
										<b>公開範囲：</b><br/>
										&nbsp;&nbsp;
<c:choose>
<c:when test="${e.pubLevel=='1'}"><font style="color:#cccccc;">外部</font>&nbsp;/&nbsp;Frontier Net&nbsp;/&nbsp;ﾏｲFrontier&nbsp;/&nbsp;ｸﾞﾙｰﾌﾟ</c:when>
<c:when test="${e.pubLevel=='2'}"><font style="color:#cccccc;">外部</font>&nbsp;/&nbsp;<font style="color:#cccccc;">Frontier Net</font>&nbsp;/&nbsp;ﾏｲFrontier&nbsp;/&nbsp;ｸﾞﾙｰﾌﾟ</c:when>
<c:when test="${e.pubLevel=='3'}"><font style="color:#cccccc;">外部</font>&nbsp;/&nbsp;<font style="color:#cccccc;">Frontier Net</font>&nbsp;/&nbsp;<font style="color:#cccccc;">ﾏｲFrontier</font>&nbsp;/&nbsp;ｸﾞﾙｰﾌﾟ</c:when>
<c:otherwise>外部&nbsp;/&nbsp;Frontier Net&nbsp;/&nbsp;ﾏｲFrontier&nbsp;/&nbsp;ｸﾞﾙｰﾌﾟ</c:otherwise>
</c:choose>
									</td>
								</tr>
							</table>
						</div>
</c:forEach>
						<div class="listBoxBodyAreaBody" style="text-align:right;margin-right:5px;"><s:link href="/pc/fshout/list/${userInfoDto.visitMemberId}/1">一覧へ</s:link></div>
</c:otherwise>
</c:choose>

					</div>
					<div style="height:10px;">&nbsp;</div>
				</div>
			</div>
			<!--/listBox04-->
			<div style="height:10px;">&nbsp;</div>
		</div>
		<!-- F Shout一覧の表示 -->
		<!--/side-->




	</div>
	<!--/contents-->
	<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/sp10.png" alt=""/>
	<!-- フッター -->
	<%@ include file="/WEB-INF/view/pc/ffooter.jsp"%>
	<!-- フッター -->
</div>
<!--/container-->

<input type="hidden" name="setflg"     value=""/>
<input type="hidden" name="setfdomain" value=""/>
<input type="hidden" name="setgid"     value=""/>
<input type="hidden" name="domain"     value=""/>
<input type="hidden" name="gid"        value=""/>
<input type="hidden" name="vmid"       value=""/>
<!-- TOPへ遷移用の変数 -->
<input type="hidden" name="vtype"       value=""/>
<input type="hidden" name="vcmnt"       value=""/>
<input type="hidden" name="vcmntview"   value=""/>
<input type="hidden" name="vrepid"      value=""/>
</s:form>
</body>
</html>
