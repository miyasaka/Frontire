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

<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/prototype.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/effects.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/overlay.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/popup.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/windowstate.js"></script>

<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/emoji_palette_base.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/emoji_palette.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier_2.js"></script>


<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/emoji_palette.css" type="text/css" />

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
				<html:errors/>
			
				<div class="listBoxMemberList">
					<div class="listBoxMemberListTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name inputColStick">同志追加リクエスト</div></td>
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
									<div><s:link href="/pc/mem/${userInfoDto.visitMemberId}">${f:h(vNickname)}<c:if test="${FriendCnt>0}">(${FriendCnt})</c:if></s:link></div>
								</div>
							</td>
							<td valign="top">
								<div class="rightBox-title"><div><span>メッセージ<br>(全角10000文字)</span></div></div>
								<div class="rightBox-body">
									<div class="icon"><a href="javascript:void(0)" onclick="openEmojiPalette(document.getElementById('diaryBody'), event); return false;" title="絵文字"><img src="/images/insert_icon001.gif" width="22" height="22" alt="絵文字" /></a></div>
									<%@include file="/WEB-INF/view/common/emojiPalette.jsp" %>
									</div>
									<html:textarea cols="45" rows="8" styleId="diaryBody" property="comment" value="${comment}"></html:textarea>
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="2" class="request-bottom">
								<input type="submit" name="send" value="送信する"/>
							</td>
						</tr>
					</table>
					<!-- 一覧 -->
					
				</div>
				<!--/listBoxMemberList-->
				
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
<html:hidden property="reqId" value="${userInfoDto.visitMemberId}"/>
</s:form>
</body>
</html>
