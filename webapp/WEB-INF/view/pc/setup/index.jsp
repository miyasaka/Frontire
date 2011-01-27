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
<title>[frontier] 設定変更</title>
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
	<%@ include file="/WEB-INF/view/pc/fmenu.jsp"%>
	<!-- マイページ共通 -->
	<!--/navbarメニューエリア-->

	<div id="contents" class="clearfix">
		<div class="mainSetup">
			<!--メイン-->

			<div class="mainSetup-main">
				<div class="listBoxSetup clearfix">
					<div class="listBoxSetupTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name myColStick">設定変更</div></td>
							</tr>
						</table>
					</div>
					<!--/listBoxSetupTitle-->
					<div class="listBoxHead clearfix">
						<div class="ttlHeadSetupArea">基本設定<font class="setTxt">(メールアドレスやパスワードを変更できます。Twitterの利用設定もこちら。)</font></div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<div class="bodyinputAreaSub clearfix">
									<s:link title="設定を変更する" href="/pc/profile1/" >設定を変更する</s:link>
								</div>
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
					<div class="listBoxHead clearfix">
						<div class="ttlHeadSetupArea">プロフィール設定<font class="setTxt">(あなたのプロフィールを変更できます。)</font></div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<div class="bodyinputAreaSub clearfix">
									<s:link title="登録内容を変更する" href="/pc/profile2/" >登録内容を変更する</s:link>
								</div>
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
				</div>
				<!--/listBoxSetup-->
				
				<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/sp10.png" alt=""/>
				
				<div class="listBoxSetup clearfix">
					<div class="listBoxSetupTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name myColStick">Frontierを退会する</div></td>
							</tr>
						</table>
					</div>
					<!--/listBoxSetupTitle-->
					<div class="listBoxHead clearfix">
						<div class="ttlHeadSetupArea">Frontierを退会すると、このアカウントは利用出来なくなります。<br/>※現在、管理しているコミュニティがある場合、他のユーザーが1人もいないコミュニティは削除されます。<br/><c:if test="${communityCnt >0}"><br/>参加コミュニティの一覧は<s:link href="/pc/clist/${userInfoDto.memberId}">こちら</s:link></c:if></div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<div class="bodyinputAreaSub clearfix">
									<s:link href="confirm">退会確認</s:link>
								</div>
							</div>
						</div>
					</div>

				</div>
				<!--/listBoxSetup-->
				<!--/listBoxSetup-->
			</div>
			<!--/mainSetup-main-->


		</div>
		<!--/mainSetup-->
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
