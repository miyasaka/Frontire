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
<title>[frontier]${f:h(communityDto.comnm)} トピック</title>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/colorful.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/common.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/prototype.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/video.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/youtube.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/map.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/effects.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/windowstate.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/overlay.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/popup.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/emoji_palette_base.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/emoji_palette.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier_1.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/emoji_palette.css" type="text/css" />
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
	<%@ include file="/WEB-INF/view/pc/com/fmenu.jsp"%>
	<!-- マイページ共通 -->
	<!--/navbarメニューエリア-->


	<div id="contents" class="clearfix">
		<div class="mainCommunity">
			<!--メイン-->


			<div class="mainEvent-main" style="margin-bottom:-10px;">
			
				<c:choose>
				<c:when test="${fn:length(result)==0}">
				<div class="listBoxCommunityEventList clearfix">
					<div class="listBoxCommunityEventListTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name comColStick">トピック</div></td>
							</tr>
						</table>
					</div>
					<div class="listBoxHead clearfix">
						<div class="bodycontentsArea noDataArea">
							<div class="noData">
								データがありません
							</div>
						</div>
					</div>
				</div>
				</c:when>
				<c:otherwise>
				
				<!--errArea01-->
				<!-- エラー -->
				<html:errors/>
				<!-- エラー -->
				<!--/errArea01-->

				<!-- コミュニティ名表示エリア -->
				<div class="eventHead clearfix">
					<ul>
						<li>${f:h(communityDto.comnm)}&nbsp;&nbsp;トピック</li>
						<c:if test="${communityDto.makabletopic=='1' || communityDto.makabletopic=='2'}">
						<li class="pgArea">
							<div>
								<s:link href="/pc/com/enttopic/${f:h(communityDto.cid)}">トピックを作成する</s:link>
							</div>
						</li>
						</c:if>
					</ul>
				</div>
				<!-- /コミュニティ名表示エリア -->
				
				
				<!--表示件数が0件の場合-->
				<!--
				<div class="listBoxCommunityEventList clearfix">
					<div class="listBoxCommunityEventListTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name comColStick">トピック</div></td>
							</tr>
						</table>
					</div>
					<div class="listBoxHead clearfix">
						<div class="bodycontentsArea noDataArea">
							<div class="noData">
								まだ何もありません。
							</div>
						</div>
					</div>
				</div>
				-->
				<!--/表示件数が0件の場合-->
				
				
				
				<div class="listBoxCommunityEventList clearfix">
					<div class="listBoxCommunityEventListTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name comColStick">トピック</div></td>
							</tr>
						</table>
					</div>
					<!--/listBoxCommunityEventListTitle-->
					
					<div class="listBoxHead clearfix">
						<div class="ttlHeadAreaEventList">
							
							<div class="ttlHeadAreaEventListSub clearfix">
								<ul>
									<li class="txtArea">${f:h(result[0].title)}<c:if test="${result[0].editflg=='1'}">&nbsp;&nbsp;&nbsp;&nbsp;<s:link href="/pc/com/enttopic/edit/${f:h(communityDto.cid)}/${f:h(bbsid)}">編集する</s:link></c:if></li>
									<li class="timeArea"><fmt:formatDate value="${f:date(f:h(result[0].entdate),'yyyyMMddHHmm')}" pattern="yyyy年MM月dd日HH:mm" /></li>
								</ul>
							</div>
						</div>
						
						<div class="bodycontentsArea">
							<table>
								<tr>
									<td class="leftArea">
										<ul>
											<li class="leftNickname"><s:link href="/pc/mem/${f:h(result[0].mid)}">${f:h(result[0].nickname)}</s:link></li>
										</ul>
									</td>
									<td class="rightArea">
										<div class="bodyEventArea">
										
											<div class="eventPicArea">
												${result[0].pichtml}
											</div>
											${f:br(result[0].cmnthtml)}<br/>
										</div>
									</td>
								</tr>
							</table>
						</div>
						<!--/bodycontentsArea-->
					</div>
					<!--/listBoxHead-->
				</div>
				<!--/listBoxCommunityEventList-->
				
				<c:if test="${resultscnt>0}">
				<div class="listBoxCommunityEventList clearfix">
					<div class="listBoxCommunityEventViewTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp">
									<div class="ttl_name comColStick">コメント(${resultscnt}件)</div>
								</td>
								<c:if test="${resultscnt>appDefDto.FP_COM_BBSLIST_CMNTMAX}">
								<td class="ttl_new_sp" style="text-align:right;">
									<!-- ページング -->
									<c:choose>
									<c:when test="${allflg=='1'}">
									全てを表示
									<span><s:link href="view/${communityDto.cid}/${bbsid}">最新の${appDefDto.FP_COM_BBSLIST_CMNTMAX}件を表示</s:link></span>
									</c:when>
									<c:otherwise>
									<s:link href="viewall/${communityDto.cid}/${bbsid}">全てを表示</s:link>
									<span>最新の${appDefDto.FP_COM_BBSLIST_CMNTMAX}件を表示</span>
									</c:otherwise>
									</c:choose>
									<!-- ページング -->
								</td>
								</c:if>
							</tr>
						</table>
					</div>
					<!--/listBoxCommunityEventViewTitle-->
					
					<!-- コメントエリア -->
					<c:forEach var="e" items="${results}">
					<div class="listBoxHead clearfix">
						<div class="ttlHeadAreaEventView">
							
							<div class="ttlHeadAreaEventViewSub clearfix">
								<ul>
									<li class="txtArea">
										<c:if test="${result[0].editflg=='1'}"><input id="commentCheck01" name="chkcmnt" type="checkbox" value="${e.comno}" /></c:if>
										<span class="inputArea">${e.comno}<s:link href="/pc/mem/${f:h(e.mid)}">${f:h(e.nickname)}</s:link></span>
										<c:if test="${result[0].editflg!='1' && e.editflg=='1'}">&nbsp;&nbsp;&nbsp;&nbsp;<s:link href="delcmnt/${e.comno}">自分のコメントを削除する</s:link></c:if>
									</li>
									<li class="timeArea"><fmt:formatDate value="${f:date(f:h(e.entdate),'yyyyMMddHHmm')}" pattern="yyyy年MM月dd日HH:mm" /></li>
								</ul>
							</div>
						</div>
						<div class="bodycontentsArea">
							<table>
								<tr>
									<td class="leftAreaView">

									</td>
									<td class="rightArea">
										<div class="bodyEventArea">
											${e.pichtml}
											${f:br(e.cmnthtml)}<br/>
										</div>
									</td>
								</tr>
							</table>
						</div>
						<!--/bodycontentsArea-->
					</div>
					<!--/listBoxHead-->
					</c:forEach>
					<!-- コメントエリア -->

					<c:if test="${result[0].editflg=='1'}">
					<div class="DelComArea" style="padding:20px;">
						<input type="submit" name="delcmnts" value="チェックされたコメントを削除する" class="formBt01" />
					</div>
					</c:if>
					
				</div>
				<!--/listBoxCommunityEventList-->
				</c:if>
				
				<c:if test="${communityDto.makabletopic!='0'}">
				<div class="listBoxCommunityEventList clearfix">
					<div class="listBoxCommunityEventViewTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp">
									<div class="ttl_name inputColStick">コメントを入力する</div>
								</td>
							</tr>
						</table>
					</div>
					<!--/listBoxCommunityEventViewTitle-->
					
					<div class="listBoxHead clearfix">
						<div class="ttlHeadAreaEventView">
							<div class="ttlHeadAreaEventViewSub clearfix">
								<ul>
									<li class="txtArea">コメントの入力</li>
								</ul>
							</div>
						</div>
						<div class="bodycontentsArea">
							<div class="eventCommentboxReply">
								<dl>
									<dd class="clearfix">
										<ul>
											<li>
												<a href="javascript:void(0)" onclick="openEmojiPalette(document.getElementById('comment'), event); return false;" title="絵文字"><img src="/images/insert_icon001.gif" alt="絵文字" /></a>
												<!-- 絵文字パレット -->
												<%@ include file="/WEB-INF/view/common/emojiPalette.jsp" %>
												</div>
												<!-- 絵文字パレット -->
											</li>
											<li>
												<a href="javascript:void(0)" onclick="add_youtube_link('comment')" title="YouTube"><img src="/images/insert_youtube.gif" width="22" height="22" alt="YouTube" /></a>
											</li>
										</ul>
									</dd>
									<dd>
										<div id="color_palette" style="position: absolute;" class="palette_pop"></div>
										<html:textarea rows="15" styleId="comment" property="comment" value="${comment}"></html:textarea>
									</dd>
								</dl>
							</div>
						</div>
						<!--/bodycontentsArea-->
						<div class="ttlHeadAreaEventView">
							<div class="ttlHeadAreaEventViewSub clearfix">
								<ul>
									<li class="txtArea">画像のアップロード</li>
								</ul>
							</div>
						</div>
						<div class="bodycontentsArea">
							<div class="picUpload">
								<div>
									<div style="padding:0 0 5px 0;"><span class="upload">1枚目</span><input type="file" name="picpath1" id="picpath1" size="50" /></div>
									<div style="padding:0 0 5px 0;"><span class="upload">説明1</span><html:text property="picnote1" value="${picnote1}" size="50" maxlength="50"/></div>
								</div>
								
								<div>
									<div style="padding:5px 0 5px 0;"><span class="upload">2枚目</span><input type="file" name="picpath2" id="picpath2" size="50" /></div>
									<div style="padding:0 0 5px 0;"><span class="upload">説明2</span><html:text property="picnote2" value="${picnote2}" size="50" maxlength="50"/></div>
								</div>
								
								<div>
									<div  style="padding:5px 0 5px 0;"><span class="upload">3枚目</span><input type="file" name="picpath3" id="picpath3" size="50" /></div>
									<div  style="padding:0 0 0 0;"><span class="upload">説明3</span><html:text property="picnote3" value="${picnote3}" size="50" maxlength="50"/></div>
								</div>
							</div>
						</div>
						<!--/bodycontentsArea-->
					</div>
					<!--/listBoxHead-->
					<div class="DelComArea" style="padding:20px;">
						<input type="submit" name="finish" value="コメントを入力" />
						<html:hidden property="cid" value="${cid}"/>
						<html:hidden property="bbsid" value="${bbsid}"/>
					</div>
					
				</div>
				<!--/listBoxCommunityEventList-->
				</c:if>
				
				</c:otherwise>
				</c:choose>
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
