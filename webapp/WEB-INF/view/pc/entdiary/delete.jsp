<%@ page language="java" contentType="text/html; charset=windows-31J"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<title>[frontier] 日記を削除する</title>

<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/common.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/video.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier_1.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/colorful.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/prototype.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/effects.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/overlay.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/popup.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/windowstate.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/color_palette.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/youtube.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/emoji_palette_base.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/emoji_palette.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier_2.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/map.js"></script>
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/color_palette.css" type="text/css" />
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/emoji_palette.css" type="text/css" />
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/assort.css" type="text/css" />
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>
</head>

<body>

<div id="container">
<s:form>
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
</s:form>
	<div id="contents" class="clearfix">
		<div class="mainDiary">
			<!--メイン-->

			<div class="mainDelDiary-main">
				<div class="listBox12 clearfix">
					<div class="listBox12Title">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name inputColStick">日記の削除</div></td>
							</tr>
						</table>
					</div>
					<!--/listBox12Title-->
					<div class="listBoxHead clearfix">
						<div class="ttlHeadDelArea">下記の日記を削除しますか？</div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<div class="bodyinputAreaSub clearfix">
									<ul>
										<li>
											<s:form method="post">
											<input type="submit" name="delDiary" value="削除する" />
											</s:form>
										</li>
										<li style="float:right;">
											<s:form method="post">
											<input type="submit" name="cansel" value="やめる" />
											<html:hidden property="linkFrom" value="edit"/>
											</s:form>
										</li>
									</ul>
								</div>
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
				</div>
				<!--/listBox12-->
			</div>
			<!--/mainDelDiary-main-->

			<div class="mainDelDiary-main" style="margin-bottom:-10px;">
				<div class="listBox12 clearfix">
					<div class="listBox12Title">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name myColStick">日記内容</div></td>
							</tr>
						</table>
					</div>
					<!--/listBox12Title-->
					<c:forEach var="e" items="${results}">
					<div class="listBoxHead clearfix">
						<div class="ttlHeadArea">
							<div class="ttlHeadAreaSub">
								<dl>
									<dt class="txtArea">${f:h(e.title)}</dt>
									<dd class="timeArea clearfix"><fmt:formatDate value="${f:date(f:h(e.entdate),'yyyyMMddHHmm')}" pattern="yyyy年MM月dd日HH:mm" /></dd>
								</dl>
							</div>
						</div>
						<div class="bodycontentsArea">
							<div class="bodyDelArea">
								<c:if test="${e.pic1!=null||e.pic2!=null||e.pic3!=null}">
									<div class="delPicArea" align="center">
									<c:if test="${e.pic1!=null}">
										<a href="javascript:void(0);" onClick="ff_viewBigimg('${f:h(appDefDto.FP_CMN_CONTENTS_ROOT)}${fn:replace(pic1,'dir','pic640')}');" title="${f:h(oyaPicnote1)}"><img src="${f:h(appDefDto.FP_CMN_CONTENTS_ROOT)}${fn:replace(pic1,'dir','pic120')}" alt="${f:h(oyaPicnote1)}"/></a>
									</c:if>
									<c:if test="${e.pic2!=null}">
										<a href="javascript:void(0);" onClick="ff_viewBigimg('${f:h(appDefDto.FP_CMN_CONTENTS_ROOT)}${fn:replace(pic2,'dir','pic640')}');" title="${f:h(oyaPicnote2)}"><img src="${f:h(appDefDto.FP_CMN_CONTENTS_ROOT)}${fn:replace(pic2,'dir','pic120')}" alt="${f:h(oyaPicnote2)}"/></a>
									</c:if>
									<c:if test="${e.pic3!=null}">
										<a href="javascript:void(0);" onClick="ff_viewBigimg('${f:h(appDefDto.FP_CMN_CONTENTS_ROOT)}${fn:replace(pic3,'dir','pic640')}');" title="${f:h(oyaPicnote3)}"><img src="${f:h(appDefDto.FP_CMN_CONTENTS_ROOT)}${fn:replace(pic3,'dir','pic120')}" alt="${f:h(oyaPicnote3)}"/></a>
									</c:if>
									</div>
								</c:if>
								
								<div class="diaryBody" id="diary_body">
									${f:br(e.viewComment) }
									<br />
								</div>
								
							</div>
						</div>
					</div>
					</c:forEach>
					<!--/listBoxHead-->
				</div>
				<!--/listBox12-->
			</div>
			<!--/mainDelDiary-main-->

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

</body>
</html>
