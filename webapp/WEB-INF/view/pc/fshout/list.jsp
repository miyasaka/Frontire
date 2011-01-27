<%@ page language="java" contentType="text/html; charset=windows-31J"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<title>[frontier]</title>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/common.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/fshout.js"></script>
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/assort.css" type="text/css" />
</head>

<body>
<s:form>
<div id="container">

	<!--header-->
	<!-- ヘッダー -->
	<c:choose>
		<c:when test="${userInfoDto.membertype eq '0' || userInfoDto.membertype eq '1'}">
			<%@ include file="/WEB-INF/view/pc/fheader.jsp"%>
		</c:when>
		<c:otherwise>
			<%@ include file="/WEB-INF/view/pc/fnheader.jsp"%>
		</c:otherwise>
	</c:choose>
	<!-- ヘッダー -->
	<!--/header-->

	<!--navbarメニューエリア-->
	<!-- マイページ共通 -->
	<c:choose>
		<c:when test="${userInfoDto.membertype eq '0' || userInfoDto.membertype eq '1'}">
			<%@ include file="/WEB-INF/view/pc/fmenu.jsp"%>
		</c:when>
		<c:otherwise>
			<br/>
		</c:otherwise>
	</c:choose>
	<!-- マイページ共通 -->
	<!--/navbarメニューエリア-->

	<div id="contents" class="clearfix">
		<div class="mainFS">
			<!--メイン-->

			<div class="mainFS-main" style="margin-bottom:-15px;">

				<!--エラー-->
				<html:errors/>
				<!--/エラー-->
				<c:choose>
				<c:when test="${resultscnt>0}">
				<div class="fsNum" style="margin-bottom:3px;margin-top:-5px;">
					<table border="0">
						<tr>
							<td class="pre"><c:if test="${offset>0}"><s:link href="/pc/fshout/prepg">&lt;&lt;前を表示</s:link></c:if></td>
							<td class="next"><c:if test="${resultscnt>(offset + fn:length(fsnewList))}">&nbsp;&nbsp;&nbsp;&nbsp;<s:link href="/pc/fshout/nxtpg">次を表示&gt;&gt;</s:link></c:if></td>
						</tr>
					</table>
				</div>
				<!--/fsNum-->
				<div class="FSList clearfix" style="margin-top:-5px;">
					<div class="FSListTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp">
									<div <c:choose><c:when test="${vUser}">class="ttl_name myColStick"</c:when><c:otherwise>class="ttl_name memColStick"</c:otherwise></c:choose>>
										<c:if test="${!vUser}">${f:h(fsNickname)}さんの</c:if>F Shout一覧<span style="margin-left:5px;">（<c:choose><c:when test="${(offset+1)==(offset+fn:length(fsnewList))}">${resultscnt}</c:when><c:otherwise>${offset+1}～${offset+fn:length(fsnewList)}</c:otherwise></c:choose>／${resultscnt}件）</span></div></td>
							</tr>
						</table>
					</div>
					<!--/FSListTitle-->
					<div id ="dummy"></div>
					<c:forEach var="e" items="${fsnewList}">
					<div class="fsHead">
						<div class="listBoxHead clearfix">
							<div class="bodycontentsArea">
								<table>
									<tr>
										<td class="leftArea">
											<ul>
												<li class="leftNickname">
													<s:link href="/pc/fshout/list/${e.mid}/1">
														<c:choose>
															<c:when test="${e.photo!=null}">
																<img src="${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(e.photo,'dir','pic42')}" alt="${e.nickname}" />
															</c:when>
															<c:otherwise>
																<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg42.gif"  alt="${f:h(e.nickname)}" />
															</c:otherwise>
														</c:choose>
													</s:link>
												</li>
												<li class="leftNickname"><s:link href="/pc/fshout/list/${e.mid}/1">${f:h(e.nickname)}</s:link></li>
											</ul>
										</td>
										<td class="rightArea">
											<div class="bodyFSArea">
												<ul>
													<li>
														<span class="fsSentences"><c:if test="${e.twitter == '1'}"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/twitter.gif" alt="Twitter"/>&nbsp;</c:if>${e.viewComment}</span>
													</li>
													<li>
														<span class="date">${f:h(e.entdate)}</span>
														<span class="dateArea">
														<a href="javascript:void(0);refscmnt('http://${f:h(userInfoDto.fdomain)}/frontier/pc/top/','1','${e.jsComment}','${e.jsCommentView}','${e.mid}:${e.no}');" title="RT">
														<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/link_s.png" /></a></span>
														<span class="dateArea">
														<c:choose>
															<c:when test="${e.mid == userInfoDto.memberId}">
																<a href="javascript:void(0);delfscmnt('/frontier/pc/fshout/delfshout/',${e.no});" title="削除">
																<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/ico_ashcan1_9.gif" title="削除" alt="削除" style="cursor:pointer;"/>
																</a>
															</c:when>
															<c:otherwise>
																<a href="javascript:void(0);refscmnt('http://${f:h(userInfoDto.fdomain)}/frontier/pc/top/','0','${e.memberTag}','${e.jsCommentView}','${e.mid}:${e.no}');" title="返信">
																<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/arrow_redo_s.png" />
																</a>
															</c:otherwise>
														</c:choose>
														</span>
													</li>
													<li>
														<span class="fsSentences">
															公開範囲：
															<c:choose>
																<c:when test="${e.pubLevel=='1'}"><font style="color:#cccccc;">外部</font>&nbsp;/&nbsp;Frontier Net&nbsp;/&nbsp;ﾏｲFrontier&nbsp;/&nbsp;ｸﾞﾙｰﾌﾟ</c:when>
																<c:when test="${e.pubLevel=='2'}"><font style="color:#cccccc;">外部</font>&nbsp;/&nbsp;<font style="color:#cccccc;">Frontier Net</font>&nbsp;/&nbsp;ﾏｲFrontier&nbsp;/&nbsp;ｸﾞﾙｰﾌﾟ</c:when>
																<c:when test="${e.pubLevel=='3'}"><font style="color:#cccccc;">外部</font>&nbsp;/&nbsp;<font style="color:#cccccc;">Frontier Net</font>&nbsp;/&nbsp;<font style="color:#cccccc;">ﾏｲFrontier</font>&nbsp;/&nbsp;ｸﾞﾙｰﾌﾟ</c:when>
																<c:otherwise>外部&nbsp;/&nbsp;Frontier Net&nbsp;/&nbsp;ﾏｲFrontier&nbsp;/&nbsp;ｸﾞﾙｰﾌﾟ</c:otherwise>
															</c:choose>
														</span>
													</li>
												</ul>

											</div>
										</td>
									</tr>
								</table>
							</div>
							<!--/bodycontentsArea-->
						</div>
						<!--/listBoxHead-->
					</div>
					<!--/fsHead-->
					</c:forEach>
				</div>
				<!--/FSList-->
				<div class="fsNum clearfix" style="margin-bottom:10px;">
					<table border="0">
						<tr>
							<td class="pre"><c:if test="${offset>0}"><s:link href="/pc/fshout/prepg">&lt;&lt;前を表示</s:link></c:if></td>
							<td class="next"><c:if test="${resultscnt>(offset + fn:length(fsnewList))}">&nbsp;&nbsp;&nbsp;&nbsp;<s:link href="/pc/fshout/nxtpg">次を表示&gt;&gt;</s:link></c:if></td>
						</tr>
					</table>
				</div>
				<!--/fsNum-->
				</c:when>
				<c:when test="${resultscnt==0}">
					<!--表示件数が0件の場合-->
					<div class="FSList clearfix" style="margin-bottom:15px;">
						<div class="FSListTitle">
							<table class="list_ttl">
								<tr>
									<td class="ttl_name_sp"><div <c:choose><c:when test="${vUser}">class="ttl_name myColStick"></c:when><c:otherwise>class="ttl_name memColStick">${f:h(fsNickname)}さんの</c:otherwise></c:choose>F Shout一覧</div></td>
								</tr>
							</table>
						</div>
						<!--/FSListTitle-->

						<div class="fsHead">
							<div class="listBoxHead clearfix">
								<div class="bodycontentsArea">
									<div style="text-align:center; padding:15px 0;">
										まだ何もありません。
									</div>
								</div>
								<!--/bodycontentsArea-->
							</div>
							<!--/listBoxHead-->
						</div>
						<!--/fsHead-->
					</div>
					<!--/FSList-->
					<!--/表示件数が0件の場合-->
				</c:when>
				</c:choose>



			</div>
			<!--/mainEvent-main-->

		</div>
		<!--/mainCommunity-->
	</div>
	<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/sp10.png" alt=""/>
	<!--/contents-->
	<!-- フッター -->
	<%@ include file="/WEB-INF/view/pc/ffooter.jsp"%>
	<!-- フッター -->
</div>
<!--/container-->
<input type="hidden" name="fsno" value=""/>
<html:hidden property="selmid" name="selmid" value="${selmid}"/>
<html:hidden property="selno" name="selno" value="${selno}"/>
<html:hidden property="chktwitterflg" name="chktwitterflg" value="${chktwitterflg}"/>
<%-- TOPへ遷移用の変数 --%>
<input type="hidden" name="vtype"       value=""/>
<input type="hidden" name="vcmnt"       value=""/>
<input type="hidden" name="vcmntview"   value=""/>
<input type="hidden" name="vrepid"      value=""/>
</s:form>
</body>
</html>
