<%@ page language="java" contentType="text/html; charset=windows-31J" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS"/>
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta name="robots" content="nofollow,noindex"     />
<title>[frontier] メンバー日記</title>
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
								<td class="ttl_name_sp"><div class="ttl_name myColStick">メンバー日記</div></td>
							</tr>
						</table>
					</div>
				</div>
				<div class="listNewPageBox">
					<div class="listNewPage">
						<c:if test="${offset>0}">
							<s:link href="/pc/newfrienddiary/prepg/">&lt;&lt;前の${f:h(appDefDto.FP_MY_NEWDIARY_LIST_PGMAX)}件</s:link>
						</c:if>
						<c:if test="${offset>0 and resultscnt>(offset + fn:length(results))}">
							&nbsp;|&nbsp;
						</c:if>
						<c:if test="${resultscnt>(offset + fn:length(results))}">
							<s:link href="/pc/newfrienddiary/nxtpg/">次の${f:h(appDefDto.FP_MY_NEWDIARY_LIST_PGMAX)}件&gt;&gt;</s:link>
						</c:if>
					</div>
				</div>
				<div class="listBoxNewDiary">
					<div class="listBoxBody" id="memberDiaryBody">
						<c:if test="${resultscnt eq 0}">
							<center><font color="red">メンバー日記はまだ登録されていません。</font></center>
						</c:if>
						<c:forEach var="e" items="${results}" varStatus="status">
							<%-- 初回のみ実行 --%>
							<c:if test="${status.first}">
								<table border="0">
									<tr class="contCategory">
									<c:choose>
										<c:when test="${defMemDiarySort=='01'}">
											<th style="width:10%"><u>更新日</u><font color="red"><span>▲</span></font></th>
											<th style="width:10%"><u class="link"><s:link href="/pc/newfrienddiary/sort/02">登録日</s:link></u></th>
										</c:when>
										<c:when test="${defMemDiarySort=='02'}">
											<th style="width:10%"><u class="link"><s:link href="/pc/newfrienddiary/sort/01">更新日</s:link></u></th>
											<th style="width:10%"><u>登録日</u><font color="red"><span>▲</span></font></th>
										</c:when>
									</c:choose>
									<th style="width:80%">タイトル&nbsp/&nbspニックネーム</th>
									</tr>
							</c:if>
							<tr>
								<td align="center">${f:h(e.entdatesla)}</td>
								<td align="center">${f:h(e.entdate2sla)}</td>
								<td style="word-break:break-all;"><s:link href="/pc/diary/view/${f:h(e.diaryid)}/${f:h(e.yyyymmdd)}/${f:h(e.mid)}" title="${f:h(e.title)}">${f:h(e.title)}&nbsp(${f:h(e.count)})</s:link>&nbsp(<s:link href="/pc/mem/${f:h(e.mid)}" title="${f:h(e.nickname)}">${f:h(e.nickname)}</s:link>)</td>
							</tr>
							<%-- 最後のみ実行 --%>
							<c:if test="${status.last}">
								</table>
							</c:if>
						</c:forEach>
					</div>
				</div>
				<!--/listBoxMemberUpdate-->
				
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