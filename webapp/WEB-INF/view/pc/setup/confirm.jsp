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
<title>[frontier] 退会確認</title>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/colorful.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>

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
		<div class="mainSetup">
			<!--メイン-->
			<div class="mainSetup-main">
			

				<!--[errorArea]-->
				<html:errors/>
				<!--[errorArea]-->
				 

				<div class="listBoxProfile01">

					<div class="listBoxProfile01Title">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp">
									<div class="ttl_name inputColStick">
										<ul class="clearfix">
											<li>退会確認</li>
										</ul>
									</div>
								</td>
							</tr>
						</table>
					</div>


					<!--/listBoxHead-->
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">
							<font size="2">※よろしければ退会理由もお書きください。(全角10000文字以内)</font><br/><br/>
							<font size="2">退会理由：</font><br/>
							<html:textarea property="reason"></html:textarea><br/><br/>
						</div>
							

						<div class="bodyArea2">
							<div class="btnArea">
								<font size="2">Frontierを退会します。よろしいですか？</font><br/>
								<input type="submit" name="leave" value="退会する" class="btn01"/>&nbsp;<input type="submit" name="cansel" value="やめる" class="btn01"/>
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
	 <!--/footer-->
</div>
<!--/container-->

</s:form>
</body>
</html>
