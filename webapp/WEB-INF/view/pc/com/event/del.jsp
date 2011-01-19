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
<title>[frontier]${f:h(communityDto.comnm)} 参加のキャンセル</title>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/colorful.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/common.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/video.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/youtube.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/color_palette.css" type="text/css" />
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/emoji_palette.css" type="text/css" />
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
		<div class="mainCommunity">
			<!--メイン-->


			<div class="mainEvent-main" style="margin-bottom:-10px;">
			
				<!--errArea01-->
				<!-- エラー -->
				<html:errors/>
				<!-- エラー -->
				<!--/errArea01-->
			
				<div class="eventHead clearfix">
					<ul>
						<li>${f:h(communityDto.comnm)}&nbsp;&nbsp;イベントキャンセル</li>
					</ul>
				</div>
				
				
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
					<div class="listBoxCommunityEventViewTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp">
									<div class="ttl_name inputColStick">イベントキャンセル</div>
								</td>
							</tr>
						</table>
					</div>
					<!--/listBoxCommunityEventViewTitle-->
					
					<div class="listBoxHead clearfix">
						<div class="ttlHeadAreaEventView">
							
							<div class="ttlHeadAreaEventViewSub clearfix">
								<ul>
									<li class="txtArea">
										<font class="inputArea">コメント</font>
									</li>
								</ul>
							</div>
						</div>
						<div class="bodycontentsArea">
							<table>
								<tr>
									<td class="rightArea">
										<div class="bodyEventCancelArea">
											${f:br(comment)}
										</div>
									</td>
								</tr>
							</table>
						</div>
						<!--/bodycontentsArea-->
					</div>
					<!--/listBoxHead-->
					
					<div class="listBoxHead clearfix">
						<div class="ttlHeadAreaEventView">
							
							<div class="ttlHeadAreaEventViewSub clearfix">
								<ul>
									<li class="txtArea">
										<font class="inputArea">画像</font>
									</li>
								</ul>
							</div>
						</div>
						<div class="bodycontentsArea">
							<table>
								<tr>
									<td class="rightArea">
										<div class="bodyEventCancelArea">
											<ul class="clearfix">
												<li>1枚目</li>
												<li class="picArea">${picpath1}</li>
											</ul>
											<ul class="clearfix">
												<li>説明1</li>
												<li class="picArea">${picnote1}</li>
											</ul>
										</div>
										<div class="bodyEventCancelArea">
											<ul class="clearfix">
												<li>2枚目</li>
												<li class="picArea">${picpath2}</li>
											</ul>
											<ul class="clearfix">
												<li>説明2</li>
												<li class="picArea">${picnote2}</li>
											</ul>
										</div>
										<div class="bodyEventCancelArea">
											<ul class="clearfix">
												<li>3枚目</li>
												<li class="picArea">${picpath3}</li>
											</ul>
											<ul class="clearfix">
												<li>説明3</li>
												<li class="picArea">${picnote3}</li>
											</ul>
										</div>
									</td>
								</tr>
							</table>
						</div>
						<!--/bodycontentsArea-->
					</div>
					<!--/listBoxHead-->
					
					<div class="DelComArea">
							<ul>
								<li class="alerttxt">上記理由でイベントをキャンセルします。</li>
							</ul>
						<div style="padding:10px;">
							<input type="submit" name="cancelevent" value="OK" />
							<input type="submit" name="cancel" class="formBt02" value="キャンセル" style="margin-left:30px;" />
						</div>
					</div>
					
				</div>
				<!--/listBoxCommunityEventList-->


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
