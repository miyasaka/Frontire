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
				<!-- ファイル詳細 -->
				<div class="fileMainBox">
					<div class="fileMainBoxTitle">
						<table class="ttl">
							<tr>
								<td class="ttl_name_sp">
									<div class="ttl_name comColStick">ファイル詳細</div>
									<div class="ttl_name_right"><s:link href="/pc/file/list/" title="ファイル一覧">ファイル一覧</s:link>&nbsp;|&nbsp;<s:link href="/pc/entfile/" title="登録する">登録する</s:link></div>
								</td>
							</tr>
						</table>
					</div>
					<!--/fileMainBoxTitle -->
	<c:forEach var="e" items="${results}" varStatus="status">
		<%-- sequence-start --%>
		<c:if test="${status.first}">
					<div class="fileMainBoxBody">
						<div class="listBoxHead clearfix">
							<div class="fileViewHead">
								<div class="fileViewHeadSub clearfix">
									<ul>
										<li class="finding">
											<div style="float: left;vertical-align: bottom;"><img src="${appDefDto.FP_CMN_FILE_IMAGE_PATH}${f:h(e.pic)}" title="${f:h(e.jextension)}" /></div>
											<div style="float: left;">&nbsp;${f:h(e.title)}</div>
										</li>
										<li class="right">
									<c:if test="${e.isAuth}">
											<div style="float: left;"><s:link href="/pc/file/download/${f:h(e.fileid)}/${f:h(e.historyno)}" title="DLする"><img src="${appDefDto.FP_CMN_FILE_IMAGE_PATH}icon_download.png" alt="DLする" /></s:link></div>
											<div style="float: left;">&nbsp;<s:link href="/pc/file/download/${f:h(e.fileid)}/${f:h(e.historyno)}" title="DLする">DLする</s:link></div>
											<div style="float: left;">&nbsp;|&nbsp;</div>
									</c:if>
											<div style="float: left;"><s:link href="/pc/entfile/edit/${f:h(e.fileid)}" title="更新する"><img src="${appDefDto.FP_CMN_FILE_IMAGE_PATH}icon_refresh.png" alt="更新する" /></s:link></div>
											<div style="float: left;">&nbsp;<s:link href="/pc/entfile/edit/${f:h(e.fileid)}" title="更新する">更新する</s:link></div>
										</li>
									</ul>
								</div>
							</div>
							<!--/fileViewHead -->
							<div class="fileViewBody">
								<table>
									<tr>
										<td class="left">
											<div class="html_div"><%-- <font style="font-weight: bold;">説明</font>&nbsp;：<br /> --%>
												${f:br(e.explanationhtml)}
											</div>
										</td>
										<td class="right">
											<div class="right_div"><font style="font-weight: bold;">カテゴリ</font>&nbsp;：&nbsp;${f:h(e.categoryname)}</div>
											<div class="right_div"><font style="font-weight: bold;">種類</font>&nbsp;：&nbsp;${f:h(e.jextension)}</div>
											<div class="right_div"><font style="font-weight: bold;">サイズ</font>&nbsp;：&nbsp;${f:h(e.filesizeCalc)}</div>
											<div class="right_div"><font style="font-weight: bold;">更新者</font>&nbsp;：&nbsp;${f:h(e.updname)}</div>
											<div class="right_div"><font style="font-weight: bold;">更新日時</font>&nbsp;：&nbsp;<fmt:formatDate value="${f:date(f:h(e.upddate),'yyyyMMddHHmm')}" pattern="yyyy年MM月dd日 HH:mm" /></div>
											<div class="right_div"><font style="font-weight: bold;">登録者</font>&nbsp;：&nbsp;${f:h(e.entname)}</div>
											<div class="right_divBottom"><font style="font-weight: bold;">登録日時</font>&nbsp;：&nbsp;<fmt:formatDate value="${f:date(f:h(e.entdate),'yyyyMMddHHmm')}" pattern="yyyy年MM月dd日 HH:mm" /></div>
										</td>
									</tr>
								</table>
							</div>
							<!--/fileViewBody -->
							<div class="fileViewFoot">
								<div class="fileViewFootSub clearfix">
									<ul>
										<li class="left">
											DL数&nbsp;：&nbsp;${f:h(e.download)}回&nbsp;&nbsp;/&nbsp;
											更新回数&nbsp;：&nbsp;${fn:length(results)}回&nbsp;&nbsp;/&nbsp;
											バージョン&nbsp;：&nbsp;${f:h(e.version)}&nbsp;&nbsp;/&nbsp;
										<c:choose>
											<c:when test="${e.pubfile eq appDefDto.FP_CMN_FILE_AUTH_OPEN[0]}">${appDefDto.FP_CMN_FILE_AUTH_OPEN[1]}</c:when>
											<c:when test="${e.pubfile eq appDefDto.FP_CMN_FILE_AUTH_CLOSE[0]}">${appDefDto.FP_CMN_FILE_AUTH_CLOSE[1]}</c:when>
											<c:otherwise>非公開</c:otherwise>
										</c:choose>
										</li>
									</ul>
								</div>
							</div>
							<!--/fileViewFoot -->
						</div>
						<!--/listBoxHead -->
					</div>
					<!--/fileMainBoxBody -->
				</div>
				<!--/ファイル詳細 -->
				<!--/fileMainBox -->
		</c:if>
		<%--/sequence-start --%>

		<c:if test="${!status.first}">
			<c:if test="${status.index == 1}">
				<!-- 更新履歴 -->
				<div class="fileMainBox">
					<div class="fileMainBoxTitle">
						<table class="ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name comColStick">更新履歴</div></td>
							</tr>
						</table>
					</div>
					<!--/fileMainBoxTitle -->
			</c:if>
					<div class="fileMainBoxBody">
						<div class="listBoxHead clearfix">
							<div class="fileViewHead">
								<div class="fileViewHeadSub clearfix">
									<ul>
										<li class="left">
											<s:link href="/pc/mem/${f:h(e.updid)}" title="${f:h(e.updname)}">${f:h(e.updname)}</s:link>
										</li>
										<li class="right">
											<fmt:formatDate value="${f:date(f:h(e.upddate),'yyyyMMddHHmm')}" pattern="yyyy年MM月dd日 HH:mm" />
										</li>
									</ul>
								</div>
							</div>
							<div class="fileViewBody">
								<table>
									<tr>
										<td class="center">
											<div class="html_div">
												${f:br(e.explanationhtml)}
											</div>
										</td>
									</tr>
								</table>
							</div>
							<!--/fileViewBody -->
							<div class="fileViewFoot">
								<div class="fileViewFootSub clearfix">
									<ul>
										<li class="left">
											DL数&nbsp;：&nbsp;${f:h(e.download)}回&nbsp;&nbsp;/&nbsp;
											バージョン&nbsp;：&nbsp;&nbsp;${f:h(e.version)}&nbsp;&nbsp;/&nbsp;
											サイズ&nbsp;：&nbsp;${f:h(e.filesizeCalc)}&nbsp;&nbsp;/&nbsp;
										<c:choose>
											<c:when test="${e.pubfile eq appDefDto.FP_CMN_FILE_AUTH_OPEN[0]}">${appDefDto.FP_CMN_FILE_AUTH_OPEN[1]}</c:when>
											<c:when test="${e.pubfile eq appDefDto.FP_CMN_FILE_AUTH_CLOSE[0]}">${appDefDto.FP_CMN_FILE_AUTH_CLOSE[1]}</c:when>
											<c:otherwise>非公開</c:otherwise>
										</c:choose>
										</li>
									<c:if test="${e.isAuth}">
										<li class="right">
											<div style="float: left;"><s:link href="/pc/file/download/${f:h(e.fileid)}/${f:h(e.historyno)}" title="DLする"><img src="${appDefDto.FP_CMN_FILE_IMAGE_PATH}icon_download.png" alt="DLする"/></s:link></div>
											<div style="float: left;">&nbsp;<s:link href="/pc/file/download/${f:h(e.fileid)}/${f:h(e.historyno)}" title="DLする">DLする</s:link></div>
										</li>
									</c:if>
									</ul>
								</div>
							</div>
							<!--/fileViewFoot -->
						</div>
						<!--/listBoxHead -->
					</div>
					<!--/fileMainBoxBody -->
			<c:if test="${status.last}">
				</div>
				<!--/更新履歴 -->
				<!--/fileMainBox -->
			</c:if>
		</c:if>
	</c:forEach>
			<%-- 0件パターン --%>
			<c:if test="${resultsCnt == 0}">
					<div class="noResult">
						ファイルが存在しません。
					</div>
				</div>
				<!--/fileMainBox -->
			</c:if>
			<%--/0件パターン --%>
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