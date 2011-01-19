<%@ page language="java" contentType="text/html; charset=windows-31J"
    pageEncoding="UTF-8"%>
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
<title>${f:h(entDiaryTitle)}</title>
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
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/color_palette.css" type="text/css" />
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
	<%@ include file="/WEB-INF/view/pc/fmenu.jsp"%>
	<!-- マイページ共通 -->
	<!--/navbarメニューエリア-->

	<div id="contents" class="clearfix">
		<div class="mainDiary">
			<!--メイン-->
			<!--mainDiary-side-->
			<!--カレンダー-->
			<%@ include file="./common/calendar.jsp"%>
			<!--/カレンダー-->
			<!--/mainDiary-side-->
			
			<div class="mainDiary-main">
			
				<!--エラー-->
				<html:errors/>
				<!--/エラー--> 
			
				<div class="listBox08">
					<div class="listBox08Title">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name inputColStick">${f:h(entDiaryFinding)}</div></td>
							</tr>
						</table>
					</div>
					<div class="listBoxHead clearfix">
						<div class="ttlHeadArea">タイトル</div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<html:text property="title" value="${title}"/>
							</div>
						</div>
					</div>
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">本文</div>
						<div class="bodyArea" style="padding-left:10px;">
							<ul class="txtEditArea">
								<li><a href="javascript:void(0)" onclick="resize_font(document.getElementById('diaryBody'), 'large');" title="文字テキスト大"><img src="/images/insert_enlarge_font002.gif" width="22" height="22" alt="文字テキスト大" /></a></li>
								<li><a href="javascript:void(0)" onclick="resize_font(document.getElementById('diaryBody'), 'medium');" title="文字テキスト中"><img src="/images/insert_average_font001.gif" width="22" height="22" alt="文字テキスト中" /></a></li>
								<li><a href="javascript:void(0)" onclick="resize_font(document.getElementById('diaryBody'), 'small');" title="文字テキスト小"><img src="/images/insert_reduce_font002.gif" width="22" height="22" alt="文字テキスト小" /></a></li>
								<li><a href="javascript:void(0)" onclick="add_tag(document.getElementById('diaryBody'), 'strong');" title="太字"><img src="/images/insert_boldface001.gif" width="22" height="22" alt="太字" /></a></li>
								<li><a href="javascript:void(0)" onclick="add_tag(document.getElementById('diaryBody'), 'em');" title="斜体"><img src="/images/insert_italic001.gif" width="22" height="22" alt="斜体" /></a></li>
								<li><a href="javascript:void(0)" onclick="add_tag(document.getElementById('diaryBody'), 'u');" title="下線"><img src="/images/insert_underline001.gif" width="22" height="22" alt="下線" /></a></li>
								<li><a href="javascript:void(0)" onclick="add_tag(document.getElementById('diaryBody'), 'del');" title="打消し線"><img src="/images/insert_strike001.gif" width="22" height="22" alt="打消し線" /></a></li>
								<li><a href="javascript:void(0)" onclick="return openPalette(event, 30, 20);" title="文字カラー"><img src="/images/insert_select_color001.gif" width="22" height="22" alt="文字カラー" /></a></li>
								<li><a href="javascript:void(0)" onclick="add_link(document.getElementById('diaryBody'));" title="リンク"><img src="/images/insert_url001.gif" width="22" height="22" alt="リンク" /></a></li>
								<li><a href="javascript:add_tag(document.getElementById('diaryBody'), 'blockquote');" title="引用"><img src="/images/insert_quotation001.gif" width="22" height="22" alt="引用" /></a></li>
								<li><a href="javascript:void(0)" onClick="MM_openBrWindow('linkPhotoAlbum','','width=700,height=550,toolbar=no,scrollbars=yes,left=10,top=10')" title="フォトアルバム"><img src="/images/insert_photo001.gif" width="22" height="22" alt="フォトアルバム" /></a></li>
								<li><a href="javascript:void(0)" onclick="openEmojiPalette(document.getElementById('diaryBody'), event); return false;" title="絵文字"><img src="/images/insert_icon001.gif" width="22" height="22" alt="絵文字" /></a></li>
								<li><a href="javascript:void(0)" onclick="add_youtube_link('diaryBody')" title="YouTube"><img src="/images/insert_youtube.gif" width="22" height="22" alt="YouTube" /></a></li>
								<li><a href="javascript:add_map_link_google('diaryBody');" title="地図"><img src="/images/insert_map.gif" width="22" height="22" alt="地図" /></a></li>
								<%@include file="/WEB-INF/view/common/emojiPalette.jsp" %>
							</ul>
							<div id="color_palette" style="position: absolute;" class="palette_pop"></div>
							<html:textarea rows="8" styleId="diaryBody" style="height:400px;" property="comment" value="${comment}"></html:textarea>
						</div>
						<!--/color_palette-->
					</div>
					<!--/listBoxHead-->
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">画像のアップロード</div>
						<div class="bodyArea">
							<div class="picUploadArea">
								<c:if test="${pic1!=null}">
									<div style="margin-left:39px; margin-top:10px;"><img src="${f:h(appDefDto.FP_CMN_CONTENTS_ROOT)}${fn:replace(pic1,'dir','pic120')}" alt="" /><s:link href="delImage?photoNo=1">削除する</s:link></div>
								</c:if>
								<div>1枚目<input size="50" type="file" name="photo1" /></div>
								<div class="picUploadSpace">説明1<html:text property="picnote1" value="${picnote1}" size="50" maxlength="50"/></div>
							</div>
							
							<div class="picUploadArea">
								<c:if test="${pic2!=null}">
									<div style="margin-left:39px; margin-top:10px;"><img src="${f:h(appDefDto.FP_CMN_CONTENTS_ROOT)}${fn:replace(pic2,'dir','pic120')}" alt="" /><s:link href="delImage?photoNo=2">削除する</s:link></div>
								</c:if>
								<div class="picUploadSpace">2枚目<input size="50" type="file" name="photo2" /></div>
								<div class="picUploadSpace">説明2<html:text property="picnote2" value="${picnote2}" size="50" maxlength="50"/></div>
							</div>
							
							<div class="picUploadArea">
								<c:if test="${pic3!=null}">
									<div style="margin-left:39px; margin-top:10px;"><img src="${f:h(appDefDto.FP_CMN_CONTENTS_ROOT)}${fn:replace(pic3,'dir','pic120')}" alt="" /><s:link href="delImage?photoNo=3">削除する</s:link></div>
								</c:if>
								<div class="picUploadSpace">3枚目<input size="50" type="file" name="photo3" /></div>
								<div class="picUploadSpace">説明3<html:text property="picnote3" value="${picnote3}" size="50" maxlength="50"/></div>
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">公開範囲</div>
						<div class="bodyArea" style="padding-left:50px;">
							<select name="pubDiary">
								<optgroup label="標準の公開設定">
									<c:choose>
										<c:when test="${pubDiaryLevel eq '9'}">
											<option value="9" ${pubDiary eq '9'?'selected':''}>非公開</option>
										</c:when>
										<c:when test="${pubDiaryLevel eq '2'}">
											<option value="2" ${pubDiary eq '2'?'selected':''}>グループに公開</option>
										</c:when>
										<c:when test="${pubDiaryLevel eq '1'}">
											<option value="1" ${pubDiary eq '1'?'selected':''}>全体に公開</option>
										</c:when>
									</c:choose>
								</optgroup>
								<optgroup label="個別の公開設定">
									<option value="9" ${pubDiary eq '9'?'selected':''}>非公開</option>
									<option value="2" ${pubDiary eq '2'?'selected':''}>グループに公開</option>
									<option value="1" ${pubDiary eq '1'?'selected':''}>全体に公開</option>
								</optgroup>
							</select>
						</div>
					</div>
					<!--/listBoxHead-->
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">
							<div style="float:left;">公開状況</div>
							<c:choose>
								<c:when test="${entDiaryEditType eq '0'}">
									<div class="publicStatus">&nbsp;</div>
								</c:when>
								<c:when test="${entDiaryEditType eq '1'}">
									<%-- 申請状況によって文言を出しわける --%>
									<c:choose>
										<c:when test="${appstatus eq '1'}">
											<div class="publicStatus">Frontier Net公開申請中</div>
										</c:when>
										<c:when test="${appstatus eq '2'}">
											<div class="publicStatus">Frontier Net公開中</div>
										</c:when>
										<c:when test="${appstatus eq '3'}">
											<div class="publicStatus">外部公開申請中</div>
										</c:when>
										<c:when test="${appstatus eq '4'}">
											<div class="publicStatus">外部公開中</div>
										</c:when>
										<c:otherwise>
											<div class="publicStatus">未設定</div>
										</c:otherwise>
									</c:choose>
								</c:when>
							</c:choose>
						</div>
						<div class="bodyArea">
							<div class="publicChkArea">
								<input type="radio" name="insPubLevel" value="9" <c:if test="${insPubLevel eq '9'}">checked="checked"</c:if>>未公開
								<input type="radio" name="insPubLevel" value="1" <c:if test="${insPubLevel eq '1'}">checked="checked"</c:if>>Frontier&nbsp;Net公開
								<input type="radio" name="insPubLevel" value="0" <c:if test="${insPubLevel eq '0'}">checked="checked"</c:if>>外部公開
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">
							<div class="btnArea">
								<input type="submit" name="${f:h(entDiarySubmitName)}" value="${f:h(entDiarySubmitValue)}" />
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
					
				</div>
				<!--/listBox08-->
				
				<c:if test="${entDiaryEditType eq '1'}">
				<div class="listBox08">
					<div class="listBox08Title">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name inputColStick">日記の削除</div></td>
							</tr>
						</table>
					</div>
					<div class="listBoxHead clearfix">
						<div class="ttlHeadArea"><font class="alerttxt">上記の日記を削除します</font></div>
						<div class="bodyArea">
							<div class="btnArea">
								<input type="submit" name="delete" value="削除する" />
								<html:hidden property="linkFrom" value="edit"/>
							</div>
						</div>
					</div>
				</div>
				<!--/listBox08-->
				</c:if>
			</div>
			<!--/mainDiary-main-->
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
</s:form>
</body>
</html>
