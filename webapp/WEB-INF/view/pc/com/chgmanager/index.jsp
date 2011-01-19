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
<title>[frontier] 管理人変更確認</title>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/colorful.js"></script>
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
		<div class="mainSetup">
			<!--メイン-->
			<div class="mainSetup-main">
			

				<!--[errorArea]-->
				 

				<div class="listBoxProfile01">

					<div class="listBoxProfile01Title">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp">
									<div class="ttl_name inputColStick">
										<ul class="clearfix">
											<li>管理人変更確認</li>
										</ul>
									</div>
								</td>
							</tr>
						</table>
					</div>


					<!--/listBoxHead-->
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">
							<div class="btnArea">
								<div class="message01">
									<span>「${f:h(communityDto.comnm)}」の管理人を引き受けますか？</span><br>
								</div>
								<input type="submit" name="updateManager" value="はい" />&nbsp;<input type="submit" name="cansel" value="いいえ" />
							</div>
						</div>
					</div>
					<!--/listBoxHead-->

					
				</div>

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
</div>
<!--/container-->
<html:hidden property="cid" value="${cid}"/>
</s:form>
</body>
</html>
