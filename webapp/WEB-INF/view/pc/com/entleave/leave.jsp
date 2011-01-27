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
<title>[frontier]コミュニティを退会する</title>
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
	<%@ include file="/WEB-INF/view/pc/com/fmenu.jsp"%>
	<!-- マイページ共通 -->
	<!--/navbarメニューエリア-->

	<div id="contents" class="clearfix">
		<div class="mainMember">
			<!--メイン-->

			<div class="mainMember-main">
			
				<div class="listBoxMemberDelete">
					<div class="eventDelCommentHead clearfix" style="border-style:solid; border-width: 0 0 1px 0;">
						<ul>
							<li>${f:h(communityDto.comnm)}&nbsp;&nbsp;コミュニティを退会する</li>
						</ul>
					</div>
					<div class="listBoxMemberDeleteTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name inputColStick">コミュニティ退会確認</div></td>
							</tr>
						</table>
					</div>
<c:choose>
<c:when test="${f:h(leaveLevel)=='1'}">
	<div class="comEntLeaveArea">
		<span>管理人は退会できません。<br />コミュニティを削除する場合は「<s:link href="/pc/com/entprofile/${f:h(communityDto.cid)}">コミュニティ設定変更</s:link>」よりおこなってください。</span>
	</div>
</c:when>
<c:otherwise>
	<c:choose>
		<c:when test="${f:h(leaveLevel)=='2'}">
					<div class="comEntLeaveArea">
						<span>あなたが参加し、まだキャンセルしていないイベントがあるため退会できません。<br />退会されたい場合は参加イベントのキャンセルを行ってください。</span>
					</div>
		</c:when>
		<c:when test="${f:h(leaveLevel)=='4'}">
					<div class="comEntLeaveArea">
						<span>あなたが企画し、まだ終了していないイベントがあるため退会できません。</span>
					</div>
		</c:when>
		<c:otherwise>
			<c:if test="${f:h(leaveLevel)=='3'}">
					<div class="listBoxMemberDeleteBody">
						<span>「<b>${f:h(communityDto.comnm)}</b>」から本当に退会しますか？</span>
						<div class="listBoxHead listBoxHeadline clearfix">
							<div class="ttlHeadArea">
								<div class="btnArea">
									<input type="submit" name="leave" value="はい" />
									<input type="submit" name="cancel" value="いいえ" />
								</div>
							</div>
						</div>
					</div>
			</c:if>
		</c:otherwise>
	</c:choose>
</c:otherwise>
</c:choose>
				</div>
				<!--/listBoxMemberDelete-->
				
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
