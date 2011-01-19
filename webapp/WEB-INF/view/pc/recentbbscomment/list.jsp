<%@ page language="java" contentType="text/html; charset=windows-31J"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS"/>
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta name="robots" content="nofollow,noindex"     />



<title>[frontier] ｺﾐｭﾆﾃｨｺﾒﾝﾄ記入履歴</title>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<meta name="Slurp" content="NOYDIR" />
<meta name="robots" content="noodp" />
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
	<%@ include file="/WEB-INF/view/pc/fmenu.jsp"%>
	<!-- マイページ共通 -->
	<!--/navbarメニューエリア-->
	
	<div id="contents" class="clearfix">
		<div class="mainNewList">
			<!--メイン-->

			<div class="mainNewList-main">
			
				<div class="listBoxNewTittle" style="border-width:0 1px 1px 1px;">
					<div class="listBoxMemberUpdateTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name myColStick">コミュニティ情報</div></td>
							</tr>
						</table>
					</div>
				</div>
				<div class="listNewPageBox">
					<div class="listNewPage">
						<c:if test="${offset>0}">
							<s:link href="/pc/recentbbscomment/prepg/">&lt;&lt;前の${f:h(appDefDto.FP_MY_NEWCOMMUNITY_COMMENT_LIST_PGMAX)}件</s:link>
						</c:if>
						<c:if test="${offset>0 and resultscnt>(offset + fn:length(results))}">
							&nbsp;|&nbsp;
						</c:if>
						<c:if test="${resultscnt>(offset + fn:length(results))}">
							<s:link href="/pc/recentbbscomment/nxtpg/">次の${f:h(appDefDto.FP_MY_NEWCOMMUNITY_COMMENT_LIST_PGMAX)}件&gt;&gt;</s:link>
						</c:if>
					</div>
				</div>

				<div class="listBoxNewCommunity">
					<div class="listBoxBody" id="memberCommunityBody">
						<c:if test="${resultscnt eq 0}">
							<center><font color="red">コミュニティ情報はまだ登録されていません。</font></center>
						</c:if>
						<c:forEach var="e" items="${results}" varStatus="status">
							<%-- 初回のみ実行 --%>
							<c:if test="${status.first}">
								<table border="0">
									<tr class="contCategory">
										<th style="width:3%"></th>
										<c:choose>
											<c:when test="${defCommunitySort=='01'}">
												<th style="width:10%"><u>更新日</u><font color="red"><span>▲</span></font></th>
												<th style="width:10%"><u class="link"><s:link href="/pc/recentbbscomment/sort/02">登録日</s:link></u></th>
											</c:when>
											<c:when test="${defCommunitySort=='02'}">
												<th style="width:10%"><u class="link"><s:link href="/pc/recentbbscomment/sort/01">更新日</s:link></u></th>
												<th style="width:10%"><u>登録日</u><font color="red"><span>▲</span></font></th>
											</c:when>
										</c:choose>
										<th style="width:77%">タイトル&nbsp/&nbspコミュニティ名</th>
									</tr>
							</c:if>
							<tr>
								<td class="${e.entrytype eq 1?'topic':'event'}"></td>
								<td align="center">${f:h(e.updatedate2)}</td>
								<td align="center">${f:h(e.entdate2)}</td>
								<td style="word-break:break-all;">
									<s:link href="/pc/com/${e.entrytype eq 1?'topic':'event'}/view/${f:h(e.cmid)}/${e.bbsid}" title="${f:h(e.title)}">${f:h(e.title)}&nbsp;(${e.comments})</s:link>
									&nbsp;(<s:link href="/pc/com/top/${f:h(e.cmid)}" title="${f:h(e.community)}">${f:h(e.community)}</s:link>)
								</td>
							</tr>
							<%-- 最後のみ実行 --%>
							<c:if test="${status.last}">
								</table>
							</c:if>
						</c:forEach>
					</div>
				</div>
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
