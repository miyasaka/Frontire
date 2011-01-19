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
<title>[frontier]&nbsp;ファイル管理</title>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/common.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>
<%-- ajax --%>
<script language="javascript" type="text/javascript" src="/static/js/prototype.js"></script>
<script language="javascript" type="text/javascript" src="/static/js/jquery.js"></script>
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
		<div class="mainFile">
			<!-- SideBox -->
			<%@ include file="/WEB-INF/view/pc/ffilesidebox.jsp"%>
			<!--/SideBox -->

			<div class="mainFile-main">
				<!-- エラー -->
				<html:errors />
				<!--/エラー -->
				<!-- ファイル一覧 -->
				<div class="fileMainBox">
					<div class="fileMainBoxTitle">
						<table class="ttl">
							<tr>
								<td class="ttl_name_sp">
									<div class="ttl_name comColStick">ファイル一覧<c:if test="${resultsCnt > 0}">&nbsp;(${offset + 1}件～${offset + fn:length(results)}件)</c:if></div>
									<div class="ttl_name_right"><font style="color: #c0c0c0;">ファイル一覧</font>&nbsp;|&nbsp;<s:link href="/pc/entfile/" title="登録する">登録する</s:link></div>
								</td>
							</tr>
						</table>
					</div>
					<!--/fileMainBoxTitle -->
		<c:choose>
			<c:when test="${resultsCnt > 0}">
				<!-- pager -->
					<div class="fileMainBoxPage">
						<ul class="fileMainBoxPageArea">
							<li class="fileMainBoxPagePrev">
					<c:choose>
						<c:when test="${isPager && isPrev}"><s:link href="prev" title="前の${appDefDto.FP_MY_FILELIST_PGMAX}件">&lt;&lt;前の${appDefDto.FP_MY_FILELIST_PGMAX}件</s:link></c:when>
						<c:otherwise>&nbsp;</c:otherwise>
					</c:choose>
							</li>
							<li class="fileMainBoxPageTitle">${f:h(categoryName)}&nbsp;(${resultsCnt})</li>
							<li class="fileMainBoxPageNext">
					<c:choose>
						<c:when test="${isPager && isNext}"><s:link href="next" title="次の${appDefDto.FP_MY_FILELIST_PGMAX}件">次の${appDefDto.FP_MY_FILELIST_PGMAX}件&gt;&gt;</s:link></c:when>
						<c:otherwise>&nbsp;</c:otherwise>
					</c:choose>
							</li>
						</ul>
					</div>
					<!--/fileMainBoxPage -->
				<!--/pager -->
				<!-- loop -->
				<c:forEach var="e" items="${results}" varStatus="status">
				<%-- sequence-start --%>
				<c:if test="${status.first}">
					<div class="fileMainBoxBody" id="fileMainBoxBody">
						<table class="fileList">
							<tr class="finding">
								<th class="${f:h(classMap.class1)}" onclick="j$('#fileMainBoxBody').load('/frontier/pc/ajax/file/loadData/1?'+rand(10));" title="ファイル名">ファイル名${classMap.arrow1}</th>
								<th class="${f:h(classMap.class2)}" onclick="j$('#fileMainBoxBody').load('/frontier/pc/ajax/file/loadData/2?'+rand(10));" title="カテゴリ">カテゴリ${classMap.arrow2}</th>
								<th class="${f:h(classMap.class3)}" onclick="j$('#fileMainBoxBody').load('/frontier/pc/ajax/file/loadData/3?'+rand(10));" title="種類">種類${classMap.arrow3}</th>
								<th class="${f:h(classMap.class4)}" onclick="j$('#fileMainBoxBody').load('/frontier/pc/ajax/file/loadData/4?'+rand(10));" title="更新日">更新日${classMap.arrow4}</th>
								<th class="${f:h(classMap.class5)}" onclick="j$('#fileMainBoxBody').load('/frontier/pc/ajax/file/loadData/5?'+rand(10));" title="DL">DL${classMap.arrow5}</th>
								<th class="${f:h(classMap.class6)}" onclick="j$('#fileMainBoxBody').load('/frontier/pc/ajax/file/loadData/6?'+rand(10));" title="登録者">登録者${classMap.arrow6}</th>
								<th class="${f:h(classMap.class7)}" onclick="j$('#fileMainBoxBody').load('/frontier/pc/ajax/file/loadData/7?'+rand(10));" title="サイズ">サイズ${classMap.arrow7}</th>
								<th class="th8">&nbsp;</th>
							</tr>
				</c:if>
				<%--/sequence-start --%>
						<c:choose>
							<c:when test="${(status.count % 2) eq 1}"><c:set var="trClass" value="odd" /></c:when>
							<c:otherwise><c:set var="trClass" value="even" /></c:otherwise>
						</c:choose>
							<tr class="${f:h(trClass)}">
								<td class="corner">
									<div style="float: left;"><img src="${appDefDto.FP_CMN_FILE_IMAGE_PATH}${f:h(e.pic)}" title="${f:h(e.jextension)}" /></div>
									<div style="float: left;">&nbsp;<s:link href="/pc/file/view/${f:h(e.fileid)}" title="${f:h(e.titleorg)}">${f:h(e.title)}</s:link></div>
								</td>
								<td class="center">${f:h(e.categoryname)}</td>
								<td class="center">${f:h(e.jextension)}</td>
								<td class="right">
							<c:choose>
								<c:when test="${e.currentflg eq '1'}"><c:set var="formatPattern" value="HH:mm" /></c:when>
								<c:otherwise><c:set var="formatPattern" value="MM/dd" /></c:otherwise>
							</c:choose>
									<fmt:formatDate value="${f:date(f:h(e.upddate),'yyyyMMddHHmm')}" pattern="${f:h(formatPattern)}" />
								</td>
								<td class="right">${f:h(e.download)}回</td>
								<td class="center">${f:h(e.entname)}</td>
								<td class="right">${f:h(e.filesizeCalc)}</td>
								<td class="corner"><c:if test="${e.isAuth}"><s:link href="/pc/file/download/${f:h(e.fileid)}" title="DLする"><img src="${appDefDto.FP_CMN_FILE_IMAGE_PATH}icon_download.png" alt="DLする" /></s:link>&nbsp;</c:if><s:link href="/pc/entfile/edit/${f:h(e.fileid)}" title="更新する"><img src="${appDefDto.FP_CMN_FILE_IMAGE_PATH}icon_refresh.png" alt="更新する" /></s:link></td>
							</tr>
				<%-- sequence-end--%>
				<c:if test="${status.last}">
						</table>
					</div>
				</c:if>
				<%--/sequence-end--%>
				</c:forEach>
				<!--/loop -->
			<c:if test="${isPager}">
				<!-- pager -->
					<div class="fileMainBoxPage">
						<ul class="fileMainBoxPageArea">
							<li class="fileMainBoxPagePrev">
						<c:choose>
							<c:when test="${isPrev}"><s:link href="prev" title="前の${appDefDto.FP_MY_FILELIST_PGMAX}件">&lt;&lt;前の${appDefDto.FP_MY_FILELIST_PGMAX}件</s:link></c:when>
							<c:otherwise>&nbsp;</c:otherwise>
						</c:choose>
							</li>
							<li class="fileMainBoxPageTitle">&nbsp;</li>
							<li class="fileMainBoxPageNext">
						<c:choose>
							<c:when test="${isNext}"><s:link href="next" title="次の${appDefDto.FP_MY_FILELIST_PGMAX}件">次の${appDefDto.FP_MY_FILELIST_PGMAX}件&gt;&gt;</s:link></c:when>
							<c:otherwise>&nbsp;</c:otherwise>
						</c:choose>
							</li>
						</ul>
					</div>
					<!--/fileMainBoxPage -->
				<!--/pager -->
			</c:if>
			</c:when>
			<%-- 0件パターン --%>
			<c:otherwise>
					<div class="noResult">
						ファイルが存在しません。
					</div>
			</c:otherwise>
			<%--/0件パターン --%>
		</c:choose>
				</div>
				<!--/ファイル一覧 -->
				<!--/fileMainBox -->
			</div>
			<!--/mainFile-main -->
		</div>
		<!--/mainFile -->
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