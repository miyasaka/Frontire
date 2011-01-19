<%@ page language="java" contentType="text/html; charset=windows-31J" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<title>[frontier]イベント${f:h(vMode)}</title>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/colorful.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/prototype.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/effects.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/overlay.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/popup.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/emoji_palette_base.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/emoji_palette.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier_1.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/youtube.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/common.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/community.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/color_palette.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/windowstate.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/map.js"></script>
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/color_palette.css" type="text/css" />
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/emoji_palette.css" type="text/css" />
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/basic/common.css" type="text/css" />
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/basic/home.css" type="text/css" />
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/basic/community.css" type="text/css" />
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/basic/pull.css" type="text/css" />
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/assort.css" type="text/css" />
</head>

<body>
<s:form method="post" enctype="multipart/form-data">
<div id="container">
	<!-- ヘッダー -->
	<%@ include file="/WEB-INF/view/pc/fheader.jsp"%>
	<!-- ヘッダー -->
	<!--メニューエリア-->
	<%@ include file="/WEB-INF/view/pc/com/fmenu.jsp"%>
	<!--メニューエリア-->
	<div id="contents" class="clearfix">
		<div class="mainCommunity">
			<!--メイン-->
			<div class="mainEvent-main">
				<!-- エラー -->
				<html:errors/>
				<!-- エラー -->
				<c:choose>
				<c:when test="${(communityDto.makabletopic=='1' || communityDto.makabletopic=='2') && f:h(mLevel)!='1'}">
				<div class="listBoxCommunityEventEdit">
					<div class="listBoxCommunityEventEditTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name inputColStick">イベント${f:h(vMode)}</div></td>
							</tr>
						</table>
					</div>
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">タイトル<font color="red">*</font></div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<html:text property="title" value="${title}" size="50" maxlength="100"/>
							</div>
						</div>
					</div>
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">開催日時<font color="red">*</font></div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<select name="yearofevent">
									<option value="" ${yearofevent eq ''?'selected':''}>----</option>
									<c:forEach begin="${appDefDto.FP_MY_CALENDAR_START_PGMAX}" end="${appDefDto.FP_MY_CALENDAR_END_PGMAX}" var="i">
										<option value="${i}" <c:if test="${yearofevent==i}"> selected</c:if>>${i}</option>
									</c:forEach>
								</select>年
								<select name="monthofevent">
									<option value=""<c:if test="${monthofevent==null || monthofevent==''}"> selected</c:if>>--</option>
									<c:forEach begin="1" end="12" var="i">
										<option value="${i}" <c:if test="${monthofevent==i}"> selected</c:if>><c:if test="${i<10}">0</c:if>${i}</option>
									</c:forEach>
								</select>月
								<select name="dayofevent">
									<option value=""<c:if test="${dayofevent==null || dayofevent==''}"> selected</c:if>>--</option>
									<c:forEach begin="1" end="31" var="i">
										<option value="${i}" <c:if test="${dayofevent==i}"> selected</c:if>><c:if test="${i<10}">0</c:if>${i}</option>
									</c:forEach>
								</select>日
								（補足：<html:text property="eventnote" value="${eventnote}" size="30" maxlength="100" style="width:200px;"/>）
							</div>
						</div>
					</div>
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">開催場所</div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<html:text property="locationnote" value="${locationnote}" size="50" maxlength="100"/>
							</div>
						</div>
					</div>
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">本文<font color="red">*</font></div>
						<div class="bodyArea" style="padding-left:10px;">
							<ul class="txtEditArea">
								<li><a href="javascript:void(0)" onclick="openEmojiPalette(document.getElementById('comment'), event); return false;" title="絵文字"><img src="/images/insert_icon001.gif" alt="絵文字" /></a></li>
								<li><a href="javascript:void(0)" onclick="add_youtube_link('comment')" title="YouTube"><img src="/images/insert_youtube.gif" width="22" height="22" alt="YouTube" /></a></li>
								<li><a href="javascript:add_map_link_google('comment');" title="地図"><img src="/images/insert_map.gif" width="22" height="22" alt="地図" /></a></li>
							</ul>
							<!-- 絵文字パレット -->
							<%@ include file="/WEB-INF/view/common/emojiPalette.jsp" %>
							<!-- 絵文字パレット -->
							</div>
							<html:textarea cols="75" rows="8" styleId="comment" property="comment" value="${comment}"></html:textarea>
						</div>
						<!--/color_palette-->
					</div>
					<!--/listBoxHead-->
<c:if test="${f:h(resultscntm)>1}">
					<%--イベントを立てた人または新規作成のみ--%>
					<c:choose>
						<c:when test="${f:h(edittype) eq '0'}">
							<div class="listBoxHead listBoxHeadline clearfix">
							<div class="ttlHeadArea">参加者追加</div>
							<div class="bodyArea">
							<div class="bodyinputArea">
								<span style="margin-left:20px;">
									<a href="javascript:void(0)" onclick="childwinOpen('/frontier/pc/com/entevent/add/${f:h(communityDto.cid)}');">参加者を追加する</a>
								</span>
							</div>
							</div>
							</div>
						</c:when>
						<c:when test="${f:h(eventmaker)=='1'}">
							<div class="listBoxHead listBoxHeadline clearfix">
							<div class="ttlHeadArea">参加者追加</div>
							<div class="bodyArea">
							<div class="bodyinputArea">
								<span style="margin-left:20px;">
									<a href="javascript:void(0)" onclick="childwinOpen('/frontier/pc/com/entevent/add/${f:h(communityDto.cid)}');">参加者を追加する</a>
								</span>
							</div>
							</div>
							</div>
						</c:when>
					</c:choose>
					<!--/listBoxHead-->
</c:if>
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">募集期限</div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<select name="deadlineyear">
									<option value="" ${deadlineyear eq ''?'selected':''}>----</option>
									<c:forEach begin="${appDefDto.FP_MY_CALENDAR_START_PGMAX}" end="${appDefDto.FP_MY_CALENDAR_END_PGMAX}" var="i">
										<option value="${i}" <c:if test="${deadlineyear==i}"> selected</c:if>>${i}</option>
									</c:forEach>
								</select>年
								<select name="deadlinemonth">
									<option value=""<c:if test="${deadlinemonth==null || deadlinemonth==''}"> selected</c:if>>--</option>
									<c:forEach begin="01" end="12" var="i">
										<option value="${i}" <c:if test="${deadlinemonth==i}"> selected</c:if>><c:if test="${i<10}">0</c:if>${i}</option>
									</c:forEach>
								</select>月
								<select name="deadlineday">
									<option value=""<c:if test="${deadlineday==null || deadlineday==''}"> selected</c:if>>--</option>
									<c:forEach begin="1" end="31" var="i">
										<option value="${i}" <c:if test="${deadlineday==i}"> selected</c:if>><c:if test="${i<10}">0</c:if>${i}</option>
									</c:forEach>
								</select>日
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">画像のアップロード</div>
						<div class="bodyArea">
							<div class="picUploadArea">
								<c:if test="${f:h(strpicpath1) != '' && f:h(strpicpath1) != null}">
								<div><img src="${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(strpicpath1,'dir','pic76')}" alt=""/>
								<s:link href="delpic1">削除</s:link></div>
								</c:if>
								<div>1枚目<input type="file" name="picpath1" id="picpath1" size="50" /></div>
								<div class="picUploadSpace">説明1<html:text property="picnote1" value="${picnote1}" size="50" maxlength="50"/></div>
							</div>
							<div class="picUploadArea">
								<c:if test="${f:h(strpicpath2) != '' && f:h(strpicpath2) != null}">
								<br/>
								<div><img src="${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(strpicpath2,'dir','pic76')}" alt=""/>
								<s:link href="delpic2">削除</s:link></div>
								</c:if>
								<div>2枚目<input type="file" name="picpath2" id="picpath1" size="50" /></div>
								<div class="picUploadSpace">説明2<html:text property="picnote2" value="${picnote2}" size="50" maxlength="50"/></div>
							</div>
							<div class="picUploadArea">
								<c:if test="${f:h(strpicpath3) != '' && f:h(strpicpath3) != null}">
								<br/>
								<div><img src="${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(strpicpath3,'dir','pic76')}" alt=""/>
								<s:link href="delpic3">削除</s:link></div>
								</c:if>
								<div>3枚目<input type="file" name="picpath3" id="picpath3" size="50" /></div>
								<div class="picUploadSpace">説明3<html:text property="picnote3" value="${picnote3}" size="50" maxlength="50"/></div>
							</div>
						</div>
					</div>
					<!-- hidden -->
					<html:hidden property="selmid" name="selmid" value="${selmid}"/>
					<html:hidden property="kocid" name="kocid" value="${kocid}"/>
					<html:hidden property="kobbsid" name="kobbsid" value="${kobbsid}"/>
					<!-- hidden -->
					<!--/listBoxHead-->
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">
							<div class="btnArea">
								<c:choose>
									<c:when test="${edittype eq '0'}">
										<input type="submit" name="insevent" value="作成する"/>
									</c:when>
									<c:when test="${edittype eq '1'}">
										<input type="submit" name="editevent" value="編集する"/>
									</c:when>
								</c:choose>
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
				</div>

				<c:if test="${edittype eq '1'}">
					<!--/listBoxCommunityEventEdit-->
					<div class="listBoxCommunityEventEdit">
						<div class="listBoxCommunityEventEditTitle">
							<table class="list_ttl">
								<tr>
									<td class="ttl_name_sp"><div class="ttl_name inputColStick">イベントの削除</div></td>
								</tr>
							</table>
						</div>
						<div class="listBoxHead clearfix">
							<div class="ttlHeadArea"><font class="alerttxt">上記のイベントを削除します</font></div>
							<div class="bodyArea">
								<div class="btnArea">
									<input type="submit" name="delevent" value="削除する"/>
								</div>
							</div>
						</div>
					</div>
					<!--/listBoxCommunityEventEdit-->
				</c:if>

				</c:when>
				<c:otherwise>イベント${f:h(vMode)}権限がありません</c:otherwise>
				</c:choose>
			</div>
			<!--/mainEvent-main-->
		</div>
		<!--/mainCommunity-->
	</div>
	<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/sp10.png" alt=""/>
	
	<!-- フッター -->
	<%@ include file="/WEB-INF/view/pc/ffooter.jsp"%>
	<!-- フッター -->
	
</div>
<!--/container-->
</s:form>
</body>
</html>