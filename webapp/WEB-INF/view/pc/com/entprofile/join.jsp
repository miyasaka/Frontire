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
<title>[frontier]コミュニティ設定変更</title>
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
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/emoji_palette.css" type="text/css" />
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/assort.css" type="text/css" />
</head>

<body>
<s:form method="post" enctype="multipart/form-data">
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
		<div class="mainCommunity">
			<!--メイン-->

			<div class="mainEvent-main">
			
				<!--errArea01-->
				<!-- エラー -->
				<html:errors/>
				<!-- エラー -->
				<!--/errArea01-->
				
<c:choose>
<c:when test="${communityDto.makabletopic=='1'}">
				<div class="listBoxCommunityEventEdit">
					<div class="listBoxCommunityEventEditTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name inputColStick">コミュニティ設定変更<span style="margin-left:5px;">(<font color="red">*</font><font class="fxS">の項目は必須</font>)</span></div></td>
							</tr>
						</table>
					</div>
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">コミュニティ名<font color="red">*</font>&nbsp;(全角15文字以内)</div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<html:text property="comname" value="${comname}" size="50" maxlength="15"/>
							</div>
						</div>
					</div>
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">カテゴリ<font color="red">*</font></div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<!-- カテゴリ -->
								<select name="category">
								<option value=""<c:if test="${category==null||category==''}"> selected</c:if>>選択して下さい</option>
								<c:forEach var="e" items="${categorylist}"><option value="${e.itemcd}"<c:if test="${category==e.itemcd}"> selected</c:if>>${f:h(e.itemname)}</option>
								</c:forEach></select>
								<!-- カテゴリ -->
							</div>
						</div>
					</div>
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">参加条件<font color="red">*</font></div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<ul class="joinLevelArea">
									<li><input type="radio" name="join" value="0"<c:if test="${join==null||join!='1'}"> checked</c:if> />なし</li>
									<li><input type="radio" name="join" value="1"<c:if test="${join=='1'}"> checked</c:if> />管理人の許可が必要</li>
								</ul>
							</div>
						</div>
					</div>
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">公開範囲<font color="red">*</font></div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<ul class="joinLevelArea">
									<li><input type="radio" name="pub" value="0"<c:if test="${pub==null||pub!='9'}"> checked</c:if> />全てに公開</li>
									<li><input type="radio" name="pub" value="9"<c:if test="${pub=='9'}"> checked</c:if> />非公開</li>
								</ul>
							</div>
						</div>
					</div>
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">トピック作成権限<font color="red">*</font></div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<ul class="joinLevelArea">
									<li><input type="radio" name="auth" value="0"<c:if test="${auth==null||auth!='1'}"> checked</c:if> />参加者が作成可能</li>
									<li><input type="radio" name="auth" value="1"<c:if test="${auth=='1'}"> checked</c:if> />管理人のみ作成可能</li>
									<li style="margin-left:20px;"><font class="fxS">※管理人のみ作成可能を選択した場合、管理人だけがトピック作成、イベント作成が可能です。</font></li>
								</ul>
							</div>
						</div>
					</div>
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">コミュニティの説明<font color="red">*</font>&nbsp;(全角10000文字以内)</div>
						<div class="bodyArea" style="padding-left:10px;">
							<ul class="txtEditArea">
								<li>
									<a href="javascript:void(0)" onclick="openEmojiPalette(document.getElementById('cmnt'), event); return false;" title="絵文字"><img src="/images/insert_icon001.gif" width="22" height="22" alt="絵文字" /></a>
									<%@ include file="/WEB-INF/view/common/emojiPalette.jsp" %>
									</div>
								</li>
							</ul>
							<html:textarea cols="75" rows="15" styleId="cmnt" property="cmnt" value="${cmnt}"></html:textarea>
						</div>
						<!--/color_palette-->
					</div>
					<!--/listBoxHead-->
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">写真</div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<ul class="picUpLoadArea">
									<c:if test="${f:h(strpicpath) != '' && f:h(strpicpath) != null}">
									<li>
										<img src="${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(strpicpath,'dir','pic76')}" alt=""/>
									</li>
									<li style="padding:5px 0;"><s:link href="delpic">削除</s:link></li>
									</c:if>
									<li><input size="50" type="file" name="picpath" /></li>
								</ul>
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">
							<div class="btnArea">
								<input type="submit" name="finish" value="設定を変更" />
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
					
				</div>
				<!--/listBoxCommunityEventEdit-->
				
				<div class="listBoxCommunityEventEdit">
					<div class="listBoxCommunityEventEditTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name inputColStick">コミュニティを削除する</div></td>
							</tr>
						</table>
					</div>
					<div class="listBoxHead clearfix">
						<c:choose><c:when test="${cntbbs!=0}">
							<div class="bodyArea" style="border-style:solid;">
								<div class="bodyinputArea">
									<ul class="alertTxtArea clearfix">
										<li><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/stop_round.png" alt=""/></li>
										<li style="padding-left:10px; width:90%;">
											コミュニティを削除するには、トピックをすべて削除する必要があります。トピックがある場合は、それらを削除した後、このページに再度アクセスしてください。(削除ボタンが表示されます)<br/>
											トラブル等を避けるために、削除される際はあらかじめ参加者への告知を行っておいてください。
										</li>
									</ul>
								</div>
							</div>
						</c:when>
						<c:otherwise>
							<div class="ttlHeadArea"><font class="alerttxt">上記のコミュニティを削除します。</font></div>
							<div class="bodyArea">
								<div class="btnArea">
									<input type="submit" name="delconfirm" class="formBt01" value="削除する" />
								</div>
							</div>
						</c:otherwise>
						</c:choose>
					</div>
				</div>
				<!--/listBoxCommunityEventEdit-->
</c:when>
<c:otherwise>権限がありません</c:otherwise>
</c:choose>
			</div>
			<!--/mainEvent-main-->
		</div>
		<!--/mainCommunity-->
	</div>
	<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/sp10.png" alt=""/>
	<!--/contents-->
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
