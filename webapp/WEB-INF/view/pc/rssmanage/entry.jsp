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
<title>[frontier] RSSファイル出力形式の<c:choose><c:when test="${rssid eq ''}">登録</c:when><c:otherwise>編集</c:otherwise></c:choose></title>
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/assort.css" type="text/css" />

<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>

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
		<div class="mainMember">
			<!--メイン-->

			<div class="mainMember-main">
				<!--エラー-->
				<html:errors/>
				<!--/エラー-->				
				<div class="listBoxRSS" style="border-width:0 1px 1px 1px;">
					<div class="listBoxRSSTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name inputColStick">RSSファイル出力形式の<c:choose><c:when test="${rssid eq ''}">登録</c:when><c:otherwise>編集</c:otherwise></c:choose></div></td>
							</tr>
						</table>
					</div>
					<table cellspacing="0" width="100%;" style="border-collapse:collapse;">
						<tr><td width="120px" class="left dotted" valign="top">パターン名<font color="#ff0000">*</font></td><td class="right dotted"><input type="text" name="pname" value="${f:h(pname)}"></td></tr>
						<tr><td width="120px" class="left dotted" valign="top">ファイル名<font color="#ff0000">*</font></td><td class="right dotted"><input type="text" name="fname" value="${f:h(fname)}"></td></tr>
						<tr><td width="120px" class="left dotted" valign="top">内容<font color="#ff0000">*</font></td><td class="right dotted"><textarea name="contents" style="overflow:hidden;">${f:h(contents)}</textarea></td></tr>
						<tr><td width="120px" class="left" valign="top">対象ユーザ(${fn:length(rssEntryList)})</td><td class="right">${f:h(vRssMember)} </td></tr>
					</table>
					<div class="ttlHeadArea" style="text-align: center;width:100%;">
						<div class="btnArea pad10" STYLE="border-width:1px 0 0 0;border-style:solid;">
							<c:choose>
								<c:when test="${rssid eq ''}"><input type="submit" name="finsert" value="登録" class="ttlbtn" /></c:when>
								<c:otherwise><input type="submit" name="fupdate" value="更新" class="ttlbtn" /></c:otherwise>
							</c:choose>
							<input type="submit" name="index" value="戻る" class="ttlbtn" />
						</div>
					</div>
				</div>

			<c:if test="${rssid != ''}">
				<div class="listBoxRSS" style="border-width:0 1px 1px 1px;">
					<div class="listBoxRSSTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name inputColStick">RSSファイル出力形式の削除</div></td>
							</tr>
						</table>
					</div>
					<div class="listBoxRSSMsg">
						設定した出力形式を削除します。<br/>
						削除した出力形式は戻すことはできません。
					</div>
					<div class="ttlHeadArea" style="text-align: center;width:100%;">
						<div class="btnArea pad10">
							<input type="submit" name="confirm" value="削除" class="ttlbtn" />
						</div>
					</div>
				</div>
			</c:if>
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
