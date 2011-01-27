<%@ page language="java" contentType="text/html; charset=windows-31J"
    pageEncoding="UTF-8"%>
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
<title>[frontier] <c:if test="${!vUser}">${f:h(vNickname)}さん&nbsp;|&nbsp;</c:if>${f:h(viewResults[0]['title']) }</title>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/colorful.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/common.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/prototype.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/video.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/youtube.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/map.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/effects.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/windowstate.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/overlay.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/popup.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/emoji_palette_base.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/emoji_palette.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier_1.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/emoji_palette.css" type="text/css" />
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/assort.css" type="text/css" />
<%-- ajax --%>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/jquery.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/diary.js"></script>
<script>
// コンフリクト対応
jQuery.noConflict();
var j$ = jQuery;
</script>
<script>
function rand(len) {
	var result = '';
	var source = 'abcdefghajklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890';
	var sourceLen = source.length;
	for (var i = 0; i < len; i++) {
		result += source.charAt(Math.floor(Math.random() * sourceLen));
	}
	return result;
}
</script>
<%--/ajax --%>
</head>

<body>
<s:form>
<div id="container">

	<!--header-->
	<!-- ヘッダー -->
	<c:choose>
		<c:when test="${userInfoDto.membertype eq '2'}">
			<%@ include file="/WEB-INF/view/pc/fnheader.jsp"%>
		</c:when>
		<c:otherwise>
			<%@ include file="/WEB-INF/view/pc/fheader.jsp"%>
		</c:otherwise>
	</c:choose>
	<!-- ヘッダー -->
	<!--/header-->

	<!--navbarメニューエリア-->
	<!-- マイページ共通 -->
	<c:choose>
		<c:when test="${userInfoDto.membertype eq '2'}">
			<br/>
		</c:when>
		<c:otherwise>
			<%@ include file="/WEB-INF/view/pc/fmenu.jsp"%>
		</c:otherwise>
	</c:choose>
	<!-- マイページ共通 -->
	<!--/navbarメニューエリア-->

	<div id="contents" class="clearfix">
		<div class="mainDiary">
			<!--メイン-->
			<!--mainDiary-side-->
			<!--カレンダー-->
			<%@ include file="./common/calendar.jsp"%>
			<!--/カレンダー-->
			<!--/mainDiary-side-->
			<div class="mainDiary-main">
				<!--エラー-->
				<html:errors/>
				<!--/エラー--> 
				<!--listBox10-->
				<div class="listBox10">
					<div class="listBox10Page">
						<ul class="PageArea">
							<li>
								<c:if test="${preDiaryId != null}">
									<s:link href="/pc/diary/preDate/">&lt;&lt;前の日記へ</s:link>
								</c:if>&nbsp;
							</li>
							<li class="PageTitle">${f:h(vNickname) }<c:if test="${!vUser}">さん</c:if>の日記</li>
							<li>
								<c:if test="${nextDiaryId != null}">
									<s:link href="/pc/diary/nextDate/">次の日記へ&gt;&gt;</s:link>
								</c:if>
							</li>
						</ul>
					</div>
					<div class="listBox10Title">
						<table class="ttl">
							<tr>
								<td class="ttl_name_sp">
									<c:if test="${vUser}"><div class="ttl_name myColStick"></c:if>
									<c:if test="${!vUser}"><div class="ttl_name memColStick"></c:if>
										日記閲覧&nbsp;&nbsp;&nbsp;&nbsp;
										<c:if test="${vUser}">
											<span><s:link href="/pc/entdiary/edit?diaryid=${f:h(diaryId)}">編集する</s:link></span>&nbsp;&nbsp;&nbsp;&nbsp;
										</c:if>
										<c:if test="${(userInfoDto.membertype) eq '0'}">
										<c:choose>
											<c:when test="${viewResults[0]['pubdiary'] eq appDefDto.FP_CMN_DIARY_AUTH4[0]}">
												<span>非公開</span>
											</c:when>
											<c:when test="${viewResults[0]['pubdiary'] eq appDefDto.FP_CMN_DIARY_AUTH3[0]}">
												<span>グループに公開</span>
											</c:when>
											<c:otherwise>
												<span>全体に公開</span>
											</c:otherwise>
										</c:choose>
										</c:if>
									</div>
								</td>
							</tr>
						</table>
					</div>
					
					<div class="diaryHeadArea">
						<div class="diaryHeadTtlArea">
							<table border="0">
								<tr>
									<td class="ttlSpace">${f:h(viewResults[0]['title']) }</td>
									<td class="timeSpace"><fmt:formatDate value="${f:date(f:h(viewResults[0]['entdate']),'yyyyMMddHHmm')}" pattern="yyyy年MM月dd日HH:mm" /></td>
								</tr>
							</table>
						</div>
					</div>
					<%-- 一般ユーザ以外は表示しない。 --%>
					<c:if test="${userInfoDto.membertype eq '0'}">
						<div class="diaryHeadArea clearfix" style="border-style:solid; border-width:1px 0 0 0;">
							<c:choose>
								<c:when test="${viewResults[0]['appStatus'] eq '1'}">
									<div class="<c:choose><c:when test="${vManagementLevel eq '1'}">diaryHead3</c:when><c:otherwise>diaryHead</c:otherwise></c:choose> fontCol-orange">Frontier Net公開申請中</div>
								</c:when>
								<c:when test="${viewResults[0]['appStatus'] eq '2'}">
									<div class="diaryHead fontCol-red">Frontier Net公開中</div>
								</c:when>
								<c:when test="${viewResults[0]['appStatus'] eq '3'}">
									<div class="<c:choose><c:when test="${vManagementLevel eq '1'}">diaryHead3</c:when><c:otherwise>diaryHead</c:otherwise></c:choose> fontCol-orange">外部公開申請中</div>
								</c:when>
								<c:when test="${viewResults[0]['appStatus'] eq '4'}">
									<div class="diaryHead fontCol-red">外部公開中</div>
								</c:when>
								<c:otherwise>
									<div class="diaryHead fontCol-blue">未設定</div>
								</c:otherwise>
							</c:choose>
							<c:if test="${vManagementLevel eq '1'}">
								<c:choose>
									<c:when test="${viewResults[0]['appStatus'] eq '1'}">
										<div class="diaryHead2"><s:link href="/pc/diary/fApproval/${f:h(mid)}/${f:h(viewResults[0]['diaryid'])}/"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/approval.png" alt="承認"/></s:link></div>
										<div class="diaryHead2"><s:link href="/pc/diary/rejection/${f:h(mid)}/${f:h(viewResults[0]['diaryid'])}/"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/rejection.png" alt="却下"/></s:link></div>
									</c:when>
									<c:when test="${viewResults[0]['appStatus'] eq '2'}">
										<div class="diaryHead2"><s:link href="/pc/diary/rejection/${f:h(mid)}/${f:h(viewResults[0]['diaryid'])}/"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/cansel.png" alt="公開取消"/></s:link></div>
									</c:when>
									<c:when test="${viewResults[0]['appStatus'] eq '3'}">
										<div class="diaryHead2"><s:link href="/pc/diary/approval/${f:h(mid)}/${f:h(viewResults[0]['diaryid'])}/"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/approval.png" alt="承認"/></s:link></div>
										<div class="diaryHead2"><s:link href="/pc/diary/rejection/${f:h(mid)}/${f:h(viewResults[0]['diaryid'])}/"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/rejection.png" alt="却下"/></s:link></div>
									</c:when>
									<c:when test="${viewResults[0]['appStatus'] eq '4'}">
										<div class="diaryHead2"><s:link href="/pc/diary/rejection/${f:h(mid)}/${f:h(viewResults[0]['diaryid'])}/"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/cansel.png" alt="公開取消"/></s:link></div>
									</c:when>
									<c:otherwise>
									</c:otherwise>
								</c:choose>
							</c:if>
						</div>
					</c:if>
					<table class="diaryTitle">
					<tr>
					<td>
					<div class="diaryBody">
						<c:if test="${viewResults[0]['pic1']!=null||viewResults[0]['pic2']!=null||viewResults[0]['pic3']!=null}">
							<div>
								<div style="text-align:center;">
									<c:if test="${viewResults[0]['pic1']!=null}">
										<a href="javascript:void(0);" onClick="ff_viewBigimg('${f:h(appDefDto.FP_CMN_CONTENTS_ROOT)}${fn:replace(viewResults[0]['pic1'],'dir','pic640')}');"<c:choose><c:when test="${f:h(viewResults[0]['picnote1'])==null || f:h(viewResults[0]['picnote1'])==''}"> title="画像１"</c:when><c:otherwise> title="${f:h(viewResults[0]['picnote1'])}"</c:otherwise></c:choose>><img src="${f:h(appDefDto.FP_CMN_CONTENTS_ROOT)}${fn:replace(viewResults[0]['pic1'],'dir','pic120')}" <c:choose><c:when test="${f:h(viewResults[0]['picnote1'])==null || f:h(viewResults[0]['picnote1'])==''}">alt="画像１"</c:when><c:otherwise>alt="${f:h(viewResults[0]['picnote1'])}"</c:otherwise></c:choose>/></a>
									</c:if>
									<c:if test="${viewResults[0]['pic2']!=null}">
										<a href="javascript:void(0);" onClick="ff_viewBigimg('${f:h(appDefDto.FP_CMN_CONTENTS_ROOT)}${fn:replace(viewResults[0]['pic2'],'dir','pic640')}');"<c:choose><c:when test="${f:h(viewResults[0]['picnote2'])==null || f:h(viewResults[0]['picnote2'])==''}"> title="画像２"</c:when><c:otherwise> title="${f:h(viewResults[0]['picnote2'])}"</c:otherwise></c:choose>><img src="${f:h(appDefDto.FP_CMN_CONTENTS_ROOT)}${fn:replace(viewResults[0]['pic2'],'dir','pic120')}" <c:choose><c:when test="${f:h(viewResults[0]['picnote2'])==null || f:h(viewResults[0]['picnote2'])==''}">alt="画像２"</c:when><c:otherwise>alt="${f:h(viewResults[0]['picnote2'])}"</c:otherwise></c:choose>/></a>
									</c:if>
									<c:if test="${viewResults[0]['pic3']!=null}">
										<a href="javascript:void(0);" onClick="ff_viewBigimg('${f:h(appDefDto.FP_CMN_CONTENTS_ROOT)}${fn:replace(viewResults[0]['pic3'],'dir','pic640')}');"<c:choose><c:when test="${f:h(viewResults[0]['picnote3'])==null || f:h(viewResults[0]['picnote3'])==''}"> title="画像３"</c:when><c:otherwise> title="${f:h(viewResults[0]['picnote3'])}"</c:otherwise></c:choose>><img src="${f:h(appDefDto.FP_CMN_CONTENTS_ROOT)}${fn:replace(viewResults[0]['pic3'],'dir','pic120')}" <c:choose><c:when test="${f:h(viewResults[0]['picnote3'])==null || f:h(viewResults[0]['picnote3'])==''}">alt="画像３"</c:when><c:otherwise>alt="${f:h(viewResults[0]['picnote3'])}"</c:otherwise></c:choose>/></a>
									</c:if>
								</div>
							</div>
						</c:if>
						${f:br(viewResults[0]['viewComment']) }
					</div>
					</td>
					</tr>
					<%-- F shoutに投稿するリンク。自分の日記のみ表示 --%>
					<c:if test="${vUser}">
						<tr>
							<td style="border-top-style:solid;border-top-width:1px;text-align:center;"><s:link href="/pc/diary/goshout/">F Shoutに投稿する</s:link></td>
						</tr>
					</c:if>
					</table>
					<div class="listBox10Page">
						<ul class="PageArea">
							<li>
								<c:if test="${preDiaryId != null}">
									<s:link href="/pc/diary/preDate/">&lt;&lt;前の日記へ</s:link>
								</c:if>&nbsp;
							</li>
							<li class="PageListTitle"><s:link href="/pc/diary/list/${f:h(mid)}">${f:h(vNickname)}<c:if test="${!vUser}">さん</c:if>の日記一覧へ</s:link></li>
							<li>
								<c:if test="${nextDiaryId != null}">
									<s:link href="/pc/diary/nextDate/">次の日記へ&gt;&gt;</s:link>
								</c:if>
							</li>
						</ul>
					</div>

				</div>
				<!--/listBox10-->
				
				<s:form>
				<div id ="dummy"></div>
					<c:forEach var="e" items="${viewResults}" begin="1" varStatus="status">
						<c:if test="${status.first}">
							<div class="listBox10">
							<div class="listBox10Title">
								<table class="ttl">
								<tr>
								<td class="ttl_name_sp"><c:if test="${vUser}"><div class="ttl_name myColStick"></c:if><c:if test="${!vUser}"><div class="ttl_name memColStick"></c:if>コメント(<c:choose><c:when test="${userInfoDto.membertype eq '0' or userInfoDto.membertype eq '1'}">${f:h(cntcomment)}</c:when><c:otherwise>${f:h(cntcommentOutside)}</c:otherwise></c:choose>)<c:if test="${vUser && viewResults[0]['appStatus'] eq '4'}"><span style="margin-left:10px;">外部公開を許可するコメントは承認して下さい。</span></c:if></div></td>
								</tr>
								</table>
							</div>
						</c:if>
						<div>
							<div style="background-color:#ccc;border-top:solid 1px #000;">No.<fmt:formatNumber value="${e.comno}" minIntegerDigits="3"/></div>
							<table class="diaryTitle">
								<tr>
									<td width="60%">
									<c:if test="${vUser}">
										<input type="checkbox" name="checkCommentNo" value="${f:h(e.comno)}" /></c:if>
										
										
										<c:choose>
											<c:when test="${(userInfoDto.membertype) eq '0'}">
											<%-- 内部 --%>
										
												<c:choose>
													<c:when test="${e.mid eq userInfoDto.memberId}">
														<s:link href="/pc/top/" title="${f:h(e.nicknametitle)}">${f:h(e.nickname)}</s:link>														
													</c:when>
													<c:otherwise>
														<c:if test="${e.status eq 1}">
															<c:choose>
																<c:when test="${(f:h(e.membertype)) eq '0'}">
<s:link href="/pc/mem/${f:h(e.mid)}/" title="${f:h(e.nicknametitle)}">${f:h(e.nickname)}</s:link>

																</c:when>
<c:when test="${(f:h(e.membertype)) eq '1'}">
<s:link href="http://${f:h(e.frontierdomain)}/frontier/pc/openid/auth?cid=${f:h(e.fid)}&gm=mv&openid=${f:h(frontierUserManagement.frontierdomain)}/frontier/pc/openidserver" title="${f:h(e.nicknametitle)}">${f:h(e.nickname)}</s:link>
</c:when>
																<c:otherwise>
																	<c:choose><c:when test="${(f:h(e.guestName))==''}">名無しさん</c:when><c:otherwise>${f:h(e.guestName)}</c:otherwise></c:choose>
																	<c:if test="${f:h(e.guestUrl)!=''}"><span style="margin-left:10px;"><a href="javascript:void(0)" onClick="MM_openBrWindow('${f:h(e.guestUrl)}','','width=760,height=640,toolbar=no,scrollbars=yes,left=10,top=10')" title="${f:h(e.guestUrl)}"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/32_home_01_red.gif" width="22" alt="${f:h(e.guestUrl)}" title="${f:h(e.guestUrl)}" /></a></span></c:if>
																	<c:if test="${f:h(e.guestEmail)!=''}"><span style="margin-left:10px;"><s:link href="mailto:${f:h(e.guestEmail)}"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/mailto.png" width="22" alt="${f:h(e.guestEmail)}" title="${f:h(e.guestEmail)}" /></s:link></span></c:if>
																</c:otherwise>
															</c:choose>
														</c:if>
													</c:otherwise>
												</c:choose>
												<c:if test="${vUser && viewResults[0]['appStatus'] eq '4'}">
												<div style="display:inline;">
												<c:choose>
												<c:when test="${e.publevel eq '0' && e.appStatus eq '4'}">
												<%-- <input type="button" name="ng" value="取消" class="okngbtn ng" /> --%>
												<span class="okng"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/ng.png" name="imgNG" onclick="jsOKNG(this,${f:h(e.comno)});" alt="取消" title="取消" /></span>
												</c:when>
												<c:otherwise>
												<%-- <input type="submit" name="ok" value="承認" class="okngbtn ok" /> --%>
												<span class="okng"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/ok.png" name="imgOK" onclick="jsOKNG(this,${f:h(e.comno)});" alt="承認" title="承認" /></span>
												</c:otherwise>
												</c:choose>
												</div>
												</c:if>
												<c:if test="${!vUser and e.mid eq userInfoDto.memberId && (userInfoDto.membertype eq '0' or userInfoDto.membertype eq '1')}">
													&nbsp;|&nbsp;<s:link href="/pc/diary/memDelete/${f:h(e.comno)}">自分のコメントを削除する</s:link>
												</c:if>
												
											</c:when>
											<c:when test="${(userInfoDto.membertype) eq '1'}">
											<%-- FrontierNetユーザ --%>
										
												<c:choose>
													<c:when test="${e.mid eq userInfoDto.memberId}">
														<%-- FrontierNetユーザ自身のコメントなので自分のFrontierへ戻る --%>
														<s:link href="http://${f:h(frontierUserManagement.frontierdomain)}/frontier/pc/top/" title="${f:h(e.nicknametitle)}">${f:h(e.nickname)}</s:link>
													</c:when>
													<c:otherwise>
														<c:if test="${e.status eq 1}">
															<c:choose>
																<c:when test="${(f:h(e.membertype)) eq '0'}">
<s:link href="/pc/mem/${f:h(e.mid)}/" title="${f:h(e.nicknametitle)}">${f:h(e.nickname)}</s:link>

																</c:when>
<c:when test="${(f:h(e.membertype)) eq '1'}">
<c:choose>
<c:when test="${frontierUserManagement.frontierdomain eq e.frontierdomain}">
<s:link href="http://${f:h(frontierUserManagement.frontierdomain)}/frontier/pc/mem/${f:h(e.fid)}/" title="${f:h(e.nicknametitle)}">${f:h(e.nickname)}</s:link>
</c:when>
<c:otherwise>
<s:link href="http://${f:h(e.frontierdomain)}/frontier/pc/openid/auth?cid=${f:h(e.fid)}&gm=mv&openid=${f:h(frontierUserManagement.frontierdomain)}/frontier/pc/openidserver" title="${f:h(e.nicknametitle)}">${f:h(e.nickname)}</s:link>
</c:otherwise>
</c:choose>
</c:when>
																<c:otherwise>
																	<c:choose><c:when test="${(f:h(e.guestName))==''}">名無しさん</c:when><c:otherwise>${f:h(e.guestName)}</c:otherwise></c:choose>
																	<c:if test="${f:h(e.guestUrl)!=''}"><span style="margin-left:10px;"><a href="javascript:void(0)" onClick="MM_openBrWindow('${f:h(e.guestUrl)}','','width=760,height=640,toolbar=no,scrollbars=yes,left=10,top=10')" title="${f:h(e.guestUrl)}"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/32_home_01_red.gif" width="22" alt="${f:h(e.guestUrl)}" title="${f:h(e.guestUrl)}" /></a></span></c:if>
																	<c:if test="${f:h(e.guestEmail)!=''}"><span style="margin-left:10px;"><s:link href="mailto:${f:h(e.guestEmail)}"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/mailto.png" width="22" alt="${f:h(e.guestEmail)}" title="${f:h(e.guestEmail)}" /></s:link></span></c:if>
																</c:otherwise>
															</c:choose>
														</c:if>
													</c:otherwise>
												</c:choose>
												<c:if test="${vUser && viewResults[0]['appStatus'] eq '4'}">
												<div style="display:inline;">						
												</div>
												</c:if>
												<c:if test="${!vUser and e.mid eq userInfoDto.memberId && (userInfoDto.membertype eq '1')}">
													&nbsp;|&nbsp;<s:link href="/pc/diary/memDelete/${f:h(e.comno)}">自分のコメントを削除する</s:link>
												</c:if>
											</c:when>
											<c:otherwise>
												<%-- 外部 --%>
												<c:choose>
													<c:when test="${(f:h(e.membertype)) eq '0'}">
														${f:h(e.nickname)}
													</c:when>
													<c:otherwise>
														<c:choose><c:when test="${(f:h(e.guestName))==''}">名無しさん</c:when><c:otherwise>${f:h(e.guestName)}</c:otherwise></c:choose>
													</c:otherwise>
												</c:choose>
												<c:if test="${f:h(e.guestUrl)!=''}"><span style="margin-left:10px;"><a href="javascript:void(0)" onClick="MM_openBrWindow('${f:h(e.guestUrl)}','','width=760,height=640,toolbar=no,scrollbars=yes,left=10,top=10')" title="${f:h(e.guestUrl)}"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/32_home_01_red.gif" width="22" alt="${f:h(e.guestUrl)}" title="${f:h(e.guestUrl)}" /></a></span></c:if>
											</c:otherwise>
										</c:choose>
										
									</td>
									<td width="40%" style="text-align:right;"><fmt:formatDate value="${f:date(f:h(e.entdate),'yyyyMMddHHmm')}" pattern="yyyy年MM月dd日 HH:mm" /></td>
								</tr>
							</table>
						</div>
						<div>
							<div style="border-style:dotted; border-width:1px 0 0 0; clear:both; padding:5px;">
								<div class="diaryBody">
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
						<c:if test="${status.last}">
							<c:if test="${vUser}">
								<div class="listBox10DelComArea">
									<input type="submit" name="delComment" class="formBt01" value="チェックされたコメントを削除する" /><html:hidden property="diaryId" value="${diaryId}"/><html:hidden property="mid" value="${mid}"/>
								</div>
							</c:if>
							</div>
						</c:if>
					</c:forEach>
				
				<!--/listBox10-->
				</s:form>

				<s:form action="/pc/diary/comConfirm/" enctype="multipart/form-data">
				<div class="listBox10">
					<div class="listBox10Title">
						<table class="ttl">
							<tr>
								<td class="ttl_name_sp"><c:if test="${vUser}"><div class="ttl_name inputColStick"></c:if><c:if test="${!vUser}"><div class="ttl_name inputColStick"></c:if>コメントを入力する<c:if test="${userInfoDto.membertype eq '2'}"><span style="margin-left:10px; font-size:11px;">（記入したコメントは承認された後に表示されます。）</span></c:if></div></td>
							</tr>
						</table>
					</div>
					
							<div class="listBoxHead listBoxHeadline clearfix">
						
						<c:choose>
						<c:when test="${userInfoDto.membertype eq '2'}">
						<div class="ttlHeadArea">名前(全角10文字以内)</div>
						<div class="bodyArea" style="padding-left:10px; border-style:dotted none solid none; border-width:1px 0 1px 0;">
							<html:text property="guestname" value="${guestname}" size="50" maxlength="10" />
						</div>
							
						<div class="ttlHeadArea"><span style="cursor:auto;">E-mail</span><span style="margin-left:10px;cursor:auto;">（当方からの連絡に利用させていただきます。）</span></div>
						<div class="bodyArea" style="padding-left:10px; border-style:dotted none solid none; border-width:1px 0 1px 0;">
							<html:text property="guestemail" value="${guestemail}" size="50" maxlength="100" />
						</div>
							
						<div class="ttlHeadArea"><span style="cursor:auto;">ホームページ</span><span style="margin-left:10px;cursor:auto;">（ホームページへのリンクが表示されます。）</span></div>
						<div class="bodyArea" style="padding-left:10px; border-style:dotted none solid none; border-width:1px 0 1px 0;">
							<html:text property="guesturl" value="${guesturl}" size="50" maxlength="200" />
						</div>
						</c:when>
						</c:choose>
							
						<div class="ttlHeadArea">コメントの入力<c:if test="${userInfoDto.membertype eq '2'}"><font color="red">※必須</font></c:if></div>
						<div class="bodyArea" style="padding-left:10px;">
							<ul class="txtEditArea">
								<li><a href="javascript:void(0)" onclick="openEmojiPalette(document.getElementById('diaryBody'), event); return false;" title="絵文字"><img src="/images/insert_icon001.gif" width="22" height="22" alt="絵文字" /></a></li>
								<%@include file="/WEB-INF/view/common/emojiPalette.jsp" %>
							</ul>
							<div id="color_palette" style="position: absolute;" class="palette_pop"></div>
							<html:textarea rows="10" styleId="diaryBody" property="comment" value="${comment}"></html:textarea>
						</div>
						<!--/color_palette-->
					</div>
					<!--/listBoxHead-->

					<div>
						<table class="diaryTitle">
							<tr>
								<td style="padding:5px 0 0 10px;"><span>画像のアップロード</span></td>
							</tr>
						</table>
					</div>
					<div class="picUpload">
						<div style="border-style:dotted; border-width:1px 0 0 0; clear:both;">&nbsp;</div>
						<div>
							<div style="padding:0 0 5px 0;"><span class="upload">1枚目</span><input size="50" type="file" name="photo1" /></div>
							<div style="padding:0 0 5px 0;"><span class="upload">説明1</span><html:text property="picnote1" value="${picnote1}" size="50" maxlength="50"/></div>
						</div>
						
						<div>
							<div style="padding:5px 0 5px 0;"><span class="upload">2枚目</span><input size="50" type="file" name="photo2" /></div>
							<div style="padding:0 0 5px 0;"><span class="upload">説明2</span><html:text property="picnote2" value="${picnote2}" size="50" maxlength="50"/></div>
						</div>
						
						<div>
							<div  style="padding:5px 0 5px 0;"><span class="upload">3枚目</span><input size="50" type="file" name="photo3" /></div>
							<div  style="padding:0 0 10px 0;"><span class="upload">説明3</span><html:text property="picnote3" value="${picnote3}" size="50" maxlength="50"/></div>
						</div>
					</div>
					<div>
						<table class="diaryTitle">
							<tr>
								<td align="center"><div style="margin:5px 0 5px 0;"><input type="submit" value="コメントを入力" name="comConfirm" class="ttlbtn" /></div></td>
								<html:hidden property="diaryId" value="${diaryId}"/>
								<html:hidden property="mid" value="${mid}"/>
							</tr>
						</table>
					</div>
				</div>
				<!--/listBox10-->
				</s:form>


				
			</div>
			<!--/mainDiary-main-->
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
