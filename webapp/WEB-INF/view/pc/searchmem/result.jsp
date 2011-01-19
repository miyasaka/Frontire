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
<title>[frontier]&nbsp;メンバー検索</title>
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
		<div class="mainSearchMem">
			<!--メイン-->
			<div class="mainSearchMem-main">
<%-- ----------------------------------------------------------------------------------------------------
				<!-- エラー -->
				<html:errors />
				<!--/エラー -->
---------------------------------------------------------------------------------------------------- --%>
				<div class="listBoxSearchMem">
					<div class="listBoxSearchMemTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name myColStick">メンバー検索</div></td>
							</tr>
						</table>
					</div>
					<!--/listBoxSearchMemTitle -->
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">
							<div class="searchExplanation">
								ニックネーム、自己紹介を対象として検索します。<br>
								※検索条件未入力の場合は、全件検索になります。
							</div>
							<div class="searchCondition">
								<div class="textLeftArea">
									ニックネーム
								</div>
								<div class="textRightArea">
									<input type="text" size="80" name="nicknameCondition" value="${f:h(nicknameCondition)}">
								</div>
							</div>
							<div class="searchCondition">
								<div class="textLeftArea">
									自己紹介
								</div>
								<div class="textRightArea">
									<input type="text" size="80" name="selfIntroductionCondition" value="${f:h(selfIntroductionCondition)}">
								</div>
							</div>
							<div class="btnArea">
								<input type="submit" name="search" value="　検　　　索　" />
							</div>
						</div>
					</div>
					<!--/listBoxHead -->
				</div>
				<!--/listBoxSearchMem -->
				<!-- 検索結果一覧 -->
				<%-- 0件パターン --%>
				<c:if test="${fn:length(results)==0 && searchExeFlg}">
					<div class="listBoxSearchMem2">
						<table class="SearchMemCover_table">
							<tr>
								<td colspan="2" class="SearchMemCover_td1">
									<table>
										<tr>
											<td valign="middle">
												<div class="SearchMemCover_div1 myColStick">メンバー検索結果一覧</div>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td class="searchMemCover_td0">
									<font color="#000000">検索条件に該当するユーザーは、見つかりませんでした。</font>
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
				<div class="listBoxSearchMem2">
					<table class="SearchMemCover_table">
						<tr>
							<td colspan="2" class="SearchMemCover_td1">
								<table>
									<tr>
										<td valign="middle">
											<div class="SearchMemCover_div1 myColStick">メンバー検索結果一覧&nbsp;(${fn:length(results)}件)</div>
										</td>
									</tr>
								</table>
							</td>
						</tr>
			</c:if>
		<%--/sequence-start --%>
						<tr>
							<td align="center" valign="top" class="SearchMemCover_td4">
								<s:link href="/pc/mem/${e.mid}">
									<c:choose>
										<c:when test="${e.photo!=null}">
											<img src="${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(e.photo,'dir','pic76')}" alt="${e.nickname}" />
										</c:when>
										<c:otherwise>
											<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg76.gif"  alt="${f:h(e.nickname)}" />
										</c:otherwise>
									</c:choose>
								</s:link>
							</td>
							<td align="center" valign="top" class="SearchMemCover_td2">
								<div class="SearchMemCover_div2">
									<s:link href="/pc/mem/${e.mid}" >${f:h(e.nickname)}</s:link>&nbsp;(${e.logindate})
								</div>
								<div class="SearchMemCover_div3">
									名前：&nbsp;<c:if test="${e.pubName eq '1' || (e.pubName eq '2' && e.groupFlg eq '1')}">${f:h(e.name)}</c:if>&nbsp;&nbsp;現住所：&nbsp;<c:if test="${e.pubResidence eq '1' || (e.pubResidence eq '2' && e.groupFlg eq '1')}">${f:h(e.residenceregion)}</c:if>&nbsp;&nbsp;性別：&nbsp;<c:if test="${e.pubGender eq '1' || (e.pubGender eq '2' && e.groupFlg eq '1')}">${f:h(e.gender)}</c:if>
								</div>
								<div class="SearchMemCover_div3">${f:br(e.cmnthtml)}</div>
							</td>
						</tr>
		<%-- sequence-end --%>
				<c:if test="${i.last}">
						</table>
						<!--/SearchMemCover_table -->
					</div>
					<!--/listBoxSearchMem2 -->
				</c:if>
		<%--/sequence-end --%>
			</c:forEach>
			<!--/loop -->
				<!--/検索結果一覧 -->
			</div>
			<!--/mainSearchMem-main -->
		</div>
		<!--/mainSearchMem -->
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