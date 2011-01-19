<%@ page language="java" contentType="text/html; charset=windows-31J"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<title>[frontier]検索結果一覧</title>
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
		<div class="mainSearchAll">
			<!--メイン-->

			<div class="mainSearchAll-main">
				<div class="listBoxSearchAll">

					<table class="SearchAllCover_table2" style="width:100%">
						<tr>
							<td  colspan="2" class="SearchAllCover_td5">
								<table style="width:100%">
									<tr>
										<td valign="middle" style="width:100%;">
											<div class="SearchAllCover_div1 myColStick" style="width:100%;">検索</div>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>

					<div class="vmenu_button_area">
	
						<!-- マイメニュー -->

						<div class="vmenu_button_out">
							<input type="text" name="search" value="${f:h(searchtext)}" style="width:450px"/>
							<br/><input type="radio" name="radioOption" value="1" <c:if test="${radioOption eq '1'}">checked</c:if>>AND検索&nbsp;&nbsp;<input type="radio" name="radioOption" value="2" <c:if test="${radioOption eq '2'}">checked</c:if>>OR検索&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="searchchk" value="1" <c:if test="${mydtonly eq '1'}">checked</c:if>/>自分の登録した情報のみ
						</div>
						<div class="vmenu_button_space"></div>
						<div class="vmenu_button_out">
							<div class="vmenu_button">
								<div class="vmenu_button_in"><input type="submit" name="searchbtn" value="検索" style="border:solid 1px #000000; background-color:#808080; color:#fff; font-weight:bold; width:50px;"/></div>
							</div>
						</div>
						<div class="vmenu_button_space"></div>

					</div>

					<!--/listBoxHead-->
				</div>
				<!--/listBoxSearchAll-->
				<table class="searchPagingTable">
					<tr>
						<td class="searchPagingTable_td1">
						<c:if test="${offset>0}">
							<s:link href="/pc/search/prepg">&lt;&lt;前の20件</s:link>
						</c:if>
						</td>
						<td class="searchPagingTable_td2">
						<c:if test="${resultscnt>(offset + fn:length(results))}">
							<s:link href="/pc/search/nxtpg">次の20件&gt;&gt;</s:link>
						</c:if>
						</td>
					</tr>
				</table>
				<!-- 検索結果一覧 -->
<c:forEach var="e" items="${results}" varStatus="status">
				<c:if test="${status.first}">
					<table class="SearchAllCover_table" style="width:100%">
						<tr>
							<td colspan="2" class="SearchAllCover_td1">
								<table>
									<tr>
										<td valign="middle">
											<div class="SearchAllCover_div1 myColStick">検索結果一覧&nbsp;&nbsp;(${offset+1}～${offset+fn:length(results)}件/${resultscnt}件）</div>
										</td>
									</tr>
								</table>
							</td>
						</tr>
				</c:if>
					<!-- ループ -->


					<tr>
						<td align="center" valign="top" class="SearchAllCover_td4">
							<div style="top:center">

								<c:choose>
									<c:when test="${e.entrytype eq 'D' and e.comno eq 0}">[日記]</c:when>
									<c:when test="${e.entrytype eq 'D'}">[日記･ｺﾒﾝﾄ]</c:when>
									<c:when test="${e.entrytype eq 'C'}">[ｺﾐｭﾆﾃｨ]</c:when>
									<c:when test="${e.entrytype eq '1' and e.comno eq 0}">[ﾄﾋﾟｯｸ]</c:when>
									<c:when test="${e.entrytype eq '1'}">[ﾄﾋﾟｯｸ･ｺﾒﾝﾄ]</c:when>
									<c:when test="${e.entrytype eq '2' and e.comno eq 0}">[ｲﾍﾞﾝﾄ]</c:when>
									<c:when test="${e.entrytype eq '2'}">[ｲﾍﾞﾝﾄ･ｺﾒﾝﾄ]</c:when>
									<c:when test="${e.entrytype eq 'S'}">[F Shout]</c:when>
								</c:choose>

							</div>

								<c:choose>
									<c:when test="${e.entrytype eq 'D'}">
										<c:choose>
											<c:when test="${(f:h(e.membertype)) eq '1'}">
												<s:link href="http://${f:h(e.frontierdomain)}/frontier/pc/openid/auth?cid=${f:h(e.fid)}&gm=mv&openid=${f:h(appDefDto.FP_CMN_HOST_NAME)}/frontier/pc/openidserver">
												<!-- 画像の表示 -->
												<c:choose>
													<c:when test="${e.pic!=null and e.pic!=''}">
														<img src="${fn:replace(e.pic,'dir','pic76')}" alt="${f:h(e.nickname)}"/>
													</c:when>
													<c:otherwise>
														<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg76.gif"  alt="${f:h(e.nickname)}" />
													</c:otherwise>
												</c:choose>
												</s:link>
											</c:when>
											<c:otherwise>
												<s:link href="/pc/mem/${e.writeid}" title="${f:h(e.nickname)}">
												<!-- 画像の表示 -->
												<c:choose>
													<c:when test="${e.pic!=null}">
														<img src="${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(e.pic,'dir','pic76')}" alt="${f:h(e.nickname)}"/>
													</c:when>
													<c:otherwise>
														<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg76.gif"  alt="${f:h(e.nickname)}" />
													</c:otherwise>
												</c:choose>
												</s:link>
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:when test="${e.entrytype eq 'S'}">
										<c:choose>
											<c:when test="${(f:h(e.membertype)) eq '1'}">
												<s:link href="http://${f:h(e.frontierdomain)}/frontier/pc/openid/auth?cid=${f:h(e.fid)}&gm=mv&openid=${f:h(appDefDto.FP_CMN_HOST_NAME)}/frontier/pc/openidserver">
												<!-- 画像の表示 -->
												<c:choose>
													<c:when test="${e.pic!=null and e.pic!=''}">
														<img src="${fn:replace(e.pic,'dir','pic76')}" alt="${f:h(e.nickname)}"/>
													</c:when>
													<c:otherwise>
														<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg76.gif"  alt="${f:h(e.nickname)}" />
													</c:otherwise>
												</c:choose>
												</s:link>
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${userInfoDto.memberId eq e.writeid}">
														<%-- 自分が登録したF Shoutの場合 --%>
														<s:link href="/pc/fshout/list/${e.writeid}/0" title="${f:h(e.nickname)}">
														<!-- 画像の表示 -->
														<c:choose>
															<c:when test="${e.pic!=null}">
																<img src="${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(e.pic,'dir','pic76')}" alt="${f:h(e.nickname)}"/>
															</c:when>
															<c:otherwise>
																<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg76.gif"  alt="${f:h(e.nickname)}" />
															</c:otherwise>
														</c:choose>
														</s:link>
													</c:when>
													<c:otherwise>
														<%-- 自分以外の同じFrontierのメンバーが登録したF Shoutの場合 --%>
														<s:link href="/pc/fshout/list/${e.writeid}/${e.comno}" title="${f:h(e.nickname)}">
														<!-- 画像の表示 -->
														<c:choose>
															<c:when test="${e.pic!=null}">
																<img src="${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(e.pic,'dir','pic76')}" alt="${f:h(e.nickname)}"/>
															</c:when>
															<c:otherwise>
																<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg76.gif"  alt="${f:h(e.nickname)}" />
															</c:otherwise>
														</c:choose>
														</s:link>
													</c:otherwise>
												</c:choose>
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:otherwise>
										<!-- コミュニティの画像の表示 -->
										<s:link href="/pc/com/top/${e.firstid}" title="${f:h(e.nickname)}">
											<!-- 画像の表示 -->
											<c:choose>
												<c:when test="${e.pic!=null and e.pic!=''}">
													<img src="${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(e.pic,'dir','pic76')}" alt="${f:h(e.nickname)}"/>
												</c:when>
												<c:otherwise>
													<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg76.gif"  alt="${f:h(e.nickname)}" />
												</c:otherwise>
											</c:choose>									
										</s:link>
									</c:otherwise>
							</c:choose>

						</td>
						<td align="center" valign="top" class="SearchAllCover_td2">
							<!-- CSSを日記、コミュニティなどで切り替えます。"の関係で1行で書きます。 -->
							<div class="<c:choose><c:when test="${e.entrytype eq 'D' and e.comno eq 0}">SearchAllCover_diary</c:when><c:when test="${e.entrytype eq 'D'}">SearchAllCover_comment</c:when><c:when test="${e.entrytype eq 'C'}">SearchAllCover_community</c:when><c:when test="${e.entrytype eq '1' and e.comno eq 0}">SearchAllCover_topic</c:when><c:when test="${e.entrytype eq '1'}">SearchAllCover_comment</c:when><c:when test="${e.entrytype eq '2' and e.comno eq 0}">SearchAllCover_event</c:when><c:when test="${e.entrytype eq '2'}">SearchAllCover_comment</c:when><c:when test="${e.entrytype eq 'S'}">SearchAllCover_fshout</c:when></c:choose>">
							<div style="float:left;">

								<!-- タイトルリンクの出しわけ。 -->
								<c:choose>
									<c:when test="${e.entrytype eq 'D'}"><s:link href="/pc/diary/view/${e.secondid}/${fn:substring(f:h(e.entdate),0,8)}/${e.firstid}">${f:h(e.title)}</s:link></c:when>
									<c:when test="${e.entrytype eq 'C'}"><s:link href="/pc/com/top/${e.firstid}">${f:h(e.title)}</s:link></c:when>
									<c:when test="${e.entrytype eq '1'}"><s:link href="/pc/com/topic/view/${e.firstid}/${e.secondid}">${f:h(e.title)}</s:link></c:when>
									<c:when test="${e.entrytype eq '2'}"><s:link href="/pc/com/event/view/${e.firstid}/${e.secondid}">${f:h(e.title)}</s:link></c:when>
									<c:when test="${e.entrytype eq 'S'}"><fmt:formatDate value="${f:date(f:h(e.entdate),'yyyyMMddHHmmss')}" pattern="yyyy年MM月dd日 HH:mm:ss" />に投稿したF Shout</c:when>
								</c:choose>

								<!--　コメント数とコメントナンバーの出しわけ。 -->
								<c:choose>
									<c:when test="${e.comno != 0 and e.comno != null and e.entrytype != 'S'}">&nbsp;(No.${f:h(e.comno)})</c:when>
									<c:when test="${e.cnt != 0}">&nbsp;(${f:h(e.cnt)})</c:when>
								</c:choose>

								</div>
								<div style="text-align:right;">
									<fmt:formatDate value="${f:date(f:h(e.entdate),'yyyyMMddHHmmss')}" pattern="yyyy年MM月dd日 HH:mm" />
								</div>
							</div>
							<div class="SearchAllCover_div3">
								${e.cmnthtml}
							</div>
						</td>
					</tr>
</c:forEach>

					<!-- ループ -->

				</table>

				<!-- フォトアルバム一覧 -->
				<table class="searchPagingTable">
					<tr>
						<td class="searchPagingTable_td1">
						<c:if test="${offset>0}">
							<s:link href="/pc/search/prepg">&lt;&lt;前の20件</s:link>
						</c:if>
						</td>
						<td class="searchPagingTable_td2">
						<c:if test="${resultscnt>(offset + fn:length(results))}">
							<s:link href="/pc/search/nxtpg">次の20件&gt;&gt;</s:link>
						</c:if>
						</td>
					</tr>
				</table>

				<!-- 0件対応 -->
				<c:if test="${fn:length(results)==0}">
					<table class="SearchAllCover_table" style="width:100%">
						<tr>
							<td  colspan="2" class="SearchAllCover_td1">
								<table style="width:100%">
									<tr>
										<td valign="middle" style="width:100%;">
											<div class="SearchAllCover_div1 myColStick" style="width:100%;">検索結果一覧</div>
										</td>
									</tr>
									<tr>
										<td class="no_result_font">
										<c:choose>
											<c:when test="${f:h(searchtext) eq ''}">検索文字列は必須項目です。</c:when>
											<c:otherwise>条件に一致するものは見つかりませんでした。</c:otherwise>
										</c:choose>
										</td>										
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</c:if>
			</div>
			<!--/mainSearchAll-main-->
		</div>
		<!--/mainSearchAll-->
	</div>
	<br/>
	<!--/contents-->
	
	<!--footer-->
	<%@ include file="/WEB-INF/view/pc/ffooter.jsp"%>
	<!--footer-->
</div>
<!--/container-->
</s:form>
</body>
</html>