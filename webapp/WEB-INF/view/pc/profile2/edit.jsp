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
<title>
[frontier]
<c:choose>
<c:when test="${f:h(vflg) == '1'}">
 写真を編集する
</c:when>
<c:otherwise>
${f:h(vNickname)}さんの写真一覧
</c:otherwise>
</c:choose>
</title>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/colorful.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/assort.css" type="text/css" />
</head>

<body>
<s:form enctype="multipart/form-data">
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
		<div class="mainSetup">
			<!--メイン-->
			<div class="mainSetup-main">
			
				<!--errArea01-->
				<!--
				<div class="errArea01">
					<div class="errArea02">
						<div class="cautionSpace01 clearfix">
							<div class="cautionFont">入力エラー</div>
						</div>
						<div class="cautionSpace02">
							<ul>
								<li>タイトルは必須です</li>
								<li>本文は必須です</li>
							</ul>
						</div>
					</div>
				</div>
				-->
				<!--/errArea01-->
				<div class="listBoxProfile01">
					<div class="listBoxProfile01Title">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp">
									<c:if test="${vUser}"><div class="ttl_name inputColStick"></c:if>
									<c:if test="${!vUser}"><div class="ttl_name memColStick"></c:if>
										<ul class="clearfix">
											<li>イメージ</li>
										</ul>
									</div>
								</td>
							</tr>
						</table>
					</div>
					<div class="listBoxUploadHead clearfix">
					<c:forEach var="e" items="${results}" varStatus="i">
						<table border="0" cellspacing="10" class="uploadPicList">
							<tr>
								<c:choose>
									<c:when test="${f:h(e.pic1) != ''}">
										<td class="picList"><img src="${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(e.pic1,'dir','pic180')}" alt=""/></td>
									</c:when>
									<c:otherwise>
										<td class="picList"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg180.gif" alt=""/></td>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${f:h(e.pic2) != ''}">
										<td class="picList"><img src="${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(e.pic2,'dir','pic180')}" alt=""/></td>
									</c:when>
									<c:otherwise>
										<td class="picList"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg180.gif" alt=""/></td>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${f:h(e.pic3) != ''}">
										<td class="picList"><img src="${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(e.pic3,'dir','pic180')}" alt=""/></td>
									</c:when>
									<c:otherwise>
										<td class="picList"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg180.gif" alt=""/></td>
									</c:otherwise>
								</c:choose>
							</tr>
					<c:choose>
						<c:when test="${f:h(e.pic1) != '' || f:h(e.pic2) != '' || f:h(e.pic3) != ''}">
							<tr>
								<c:choose>
									<c:when test="${f:h(e.pic1) != ''}">
										<c:choose>
											<c:when test="${f:h(vflg) == '1'}">
												<td>【&nbsp;<s:link href="/pc/profile2/delpic/1">削除する</s:link>&nbsp;|&nbsp;<c:choose><c:when test="${f:h(e.mainpicno) == '1'}">メイン写真</c:when><c:otherwise><s:link href="/pc/profile2/mainpic/1">メイン写真にする</s:link></c:otherwise></c:choose>&nbsp;】</td>
											</c:when>
										</c:choose>
									</c:when>
									<c:otherwise>
										<td>&nbsp;</td>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${f:h(e.pic2) != ''}">
										<c:choose>
											<c:when test="${f:h(vflg) == '1'}">
												<td>【&nbsp;<s:link href="/pc/profile2/delpic/2">削除する</s:link>&nbsp;|&nbsp;<c:choose><c:when test="${f:h(e.mainpicno) == '2'}">メイン写真</c:when><c:otherwise><s:link href="/pc/profile2/mainpic/2">メイン写真にする</s:link></c:otherwise></c:choose>&nbsp;】</td>
											</c:when>
										</c:choose>
									</c:when>
									<c:otherwise>
										<td>&nbsp;</td>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${f:h(e.pic3) != ''}">
										<c:choose>
											<c:when test="${f:h(vflg) == '1'}">
												<td>【&nbsp;<s:link href="/pc/profile2/delpic/3">削除する</s:link>&nbsp;|&nbsp;<c:choose><c:when test="${f:h(e.mainpicno) == '3'}">メイン写真</c:when><c:otherwise><s:link href="/pc/profile2/mainpic/3">メイン写真にする</s:link></c:otherwise></c:choose>&nbsp;】</td>
											</c:when>
										</c:choose>
									</c:when>
									<c:otherwise>
										<td>&nbsp;</td>
									</c:otherwise>
								</c:choose>
							</tr>
						</c:when>
					</c:choose>

						</table>
					</c:forEach>
					</div>
					<!--/listBoxUploadHead-->
					<c:choose>
						<c:when test="${f:h(vflg) == '1'}">
						<div class="listBoxUploadHead clearfix">
							<div class="bodyUploadArea">
								<ul class="clearfix">
									<li><input name="picpath" type="file" value=" 参 照 " size="50" /></li>
									<li>
										<input name="upload" type="submit" value="写真をアップロード" id="写真をアップロード" alt="写真をアップロード" class="picUpLoadBtn" />
									</li>
								</ul>
							</div>
						</div>
						<!--/listBoxHead-->
						<div class="listBoxHead listBoxHeadline clearfix">
							<div class="bodyArea" style="border-width:0;">
								<div class="bodyinputArea">
									<ul class="explanTxtArea clearfix">
										<li>・写真は最大3枚まで掲載できます。</li>
										<li>・5MB以下の静止のJPEG(ジェイペグ)画像のみアップロードできます。</li>
										<li>・著作権・肖像権の侵害に当たる写真、暴力的、卑猥な写真、その他一般の方が不快に感じる写真の掲載は禁止しています。掲載はユーザー様ご自身の責任でお願いいたします。</li>
									</ul>
								</div>
							</div>
						</div>
						<!--/listBoxHead-->
						</c:when>
					</c:choose>
					
				</div>
				<!--/listBoxProfile01-->
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
	 <!--/footer-->
</div>
<!--/container-->
</s:form>
</body>
</html>
