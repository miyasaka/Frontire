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
<title>[frontier]&nbsp;コミュニティ検索</title>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>
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
		<div class="mainSearchCom">
			<!-- メイン -->
			<div class="mainSearchCom-main">
<%-- ----------------------------------------------------------------------------------------------------
				<!-- エラー -->
				<html:errors />
				<!--/エラー -->
---------------------------------------------------------------------------------------------------- --%>
				<div class="listBoxSearchCom">
					<div class="listBoxSearchComTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name myColStick">コミュニティ検索</div></td>
							</tr>
						</table>
					</div>
					<!--/listBoxSearchComTitle -->
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">
							<div class="searchExplanation">
								コミュニティ名、コミュニティの説明を対象として検索します。<br>
								※検索条件未入力の場合は、全件検索になります。
							</div>
							<div class="searchCondition">
								<div class="textLeftArea">
									コミュニティ名
								</div>
								<div class="textRightArea">
									<input type="text" size="80" name="communityNameCondition" value="${f:h(communityNameCondition)}">
								</div>
							</div>
							<div class="searchCondition">
								<div class="textLeftArea">
									コミュニティの説明
								</div>
								<div class="textRightArea">
									<input type="text" size="80" name="communityDescriptionCondition" value="${f:h(communityDescriptionCondition)}">
								</div>
							</div>
							<div class="btnArea">
								<input type="submit" name="search" value="　検　　　索　" />
							</div>
						</div>
					</div>
					<!--/listBoxHead -->
				</div>
				<!--/listBoxSearchCom -->
				<div style="margin-bottom:10px;text-align:right;">
					<s:link href="make">コミュニティを作成する</s:link>				
				</div>
				<!-- 検索結果一覧 -->
				<%-- 0件パターン --%>
				<c:if test="${fn:length(results)==0 && searchExeFlg}">
					<div class="listBoxSearchCom2">
						<table class="SearchComCover_table">
							<tr>
								<td colspan="2" class="SearchComCover_td1">
									<table>
										<tr>
											<td valign="middle">
												<div class="SearchComCover_div1 myColStick">コミュニティ検索結果一覧</div>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td class="searchComCover_td0">
									<font color="#000000">検索条件に該当するコミュニティは、見つかりませんでした。</font>
								</td>
							</tr>
						</table>
					</div>
				</c:if>
				<%--/0件パターン --%>
			<!-- loop -->
			<c:forEach var="e" items="${results}" varStatus="i">
		<%-- sequence-start --%>
			<c:if test="${i.first}">
				<div class="listBoxSearchCom2">
					<table class="SearchComCover_table">
						<tr>
							<td colspan="2" class="SearchComCover_td1">
								<table>
									<tr>
										<td valign="middle">
											<div class="SearchComCover_div1 myColStick">コミュニティ検索結果一覧&nbsp;（${fn:length(results)}件）</div>
										</td>
									</tr>
								</table>
							</td>
						</tr>
			</c:if>
		<%--/sequence-start --%>
						<tr>
							<td align="center" valign="top" class="SearchComCover_td4">
								<s:link href="/pc/com/top/${f:h(e.cid)}">
									<c:choose>
										<c:when test="${f:h(e.pic) != '' && f:h(e.pic) != null}">
											<img src="${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(e.pic,'dir','pic76')}" alt="${f:h(e.title)}" />
										</c:when>
										<c:otherwise>
											<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg76.gif"  alt="${f:h(e.title)}" />
										</c:otherwise>
									</c:choose>
								</s:link>
							</td>
							<td align="center" valign="top" class="SearchComCover_td2">
								<div class="SearchComCover_div2">
									<div style="float:left;">
										<s:link href="/pc/com/top/${f:h(e.cid)}">${f:h(e.title)}(${f:h(e.memcnt)})</s:link>&nbsp;(${f:h(e.itemname)})
									</div>
									<div style="text-align:right;">
										開設日:<fmt:formatDate value="${f:date(e.entdate, 'yyyyMMdd')}" pattern="yyyy年MM月dd日" />
									</div>
								</div>
								<div class="SearchComCover_div3">${e.cmnthtml}</div>
							</td>
						</tr>
		<%-- sequence-end --%>
			<c:if test="${i.last}">
					</table>
					<!--/SearchComCover_table -->
				</div>
				<!--/listBoxSearchCom2 -->
			</c:if>
		<%--/sequence-end --%>
			</c:forEach>
			<!--/loop -->
				<!--/検索結果一覧 -->
			</div>
			<!--/mainSearchCom-main -->
		</div>
		<!--/mainSearchCom -->
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