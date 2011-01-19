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
<title>[frontier] 同志に追加</title>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/assort.css" type="text/css" />

</head>

<body>
<s:form>
<div id="container">
<%@ include file="/WEB-INF/view/pc/fheader.jsp"%>
<!-- ヘッダー -->
	<!--/header-->
	<%@ include file="/WEB-INF/view/pc/fmenu.jsp"%>
	<!--/navbar-->
	
	<div id="contents" class="clearfix">
		<div class="mainMember">
			<!--メイン-->

			<div class="mainMember-main">
			
				<div class="listBoxMemberList">
					<div class="listBoxMemberListTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name memColStick">同志追加リクエスト</div></td>
							</tr>
						</table>
					</div>

					<table class="entrequestBody" cellpadding="0" cellspacing="0">
						<tr>
							<td class="line01" valign="top">
								<div class="leftBox">
									<s:link href="/pc/mem/${userInfoDto.visitMemberId}">
										<c:choose>
										<c:when test="${memberphoto.photo!=null}">
										<img src="${f:h(appDefDto.FP_CMN_CONTENTS_ROOT)}${fn:replace(memberphoto.photo,'dir','pic76')}" alt="${f:h(vNickname)}" />
										</c:when>
										<c:otherwise>
										<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg76.gif" alt="${f:h(currnetDto.cname)}" />
										</c:otherwise>
										</c:choose>
									</s:link>
									<div><s:link href="/pc/mem/${userInfoDto.visitMemberId}">${f:h(vNickname)}<c:if test="${FriendCnt>0}">(${FriendCnt})</c:if></s:link></a>
								</div>
							</td>
							<td valign="top">
								<div class="message"><b>${f:h(vNickname)}</b>さんは既にあなたの同志です。</div>
							</td>
						</tr>
					</table>
					
				</div>
				<!--/listBoxMemberList-->
					<!-- 一覧 -->
				<div class="LinkTop"><s:link href="/pc/top">トップへ</s:link></div>

				
			</div>
			<!--/mainMember-main-->
		</div>
		<!--/mainMember-->
	</div>
	<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/sp10.png" alt=""/>
	<!--/contents-->
	<!-- フッター -->
	<%@ include file="/WEB-INF/view/pc/fnfooter.jsp"%>
	<!-- フッター -->
	 <!--/footer-->
</div>
<!--/container-->
</s:form>
</body>
</html>
