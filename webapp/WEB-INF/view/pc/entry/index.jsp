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
<title>[frontier] メンバー登録</title>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/colorful.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/assort.css" type="text/css" />
</head>

<body>
<s:form method="post" enctype="multipart/form-data">
<div id="container">

	<!--header-->
	<!-- ヘッダー -->
	<%@ include file="/WEB-INF/view/pc/fnheader.jsp"%>
	<!-- ヘッダー -->
	<!--/header-->

	<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/sp10.png" alt=""/>
	
	<div id="contents" class="clearfix">
		<div class="mainSetup">
			<!--メイン-->
			<div class="mainSetup-main">
			
				<!--errArea01-->
				<html:errors/> 
				<!--/errArea01-->
				<div class="listBoxProfile01">
					<div class="listBoxProfile01Title">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp">
									<div class="ttl_name inputColStick">
										<ul class="clearfix">
											<li>メンバー登録<span style="margin-left:5px;">(<font color="red">*</font><font class="fxS">の項目は必須</font>)</span></li>
										</ul>
									</div>
								</td>
							</tr>
						</table>
					</div>
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">名前<font color="red">*</font><font class="fxS" style="margin-left:5px;">(全角10文字以内)</font></div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<ul class="nameArea clearfix">
									<li>姓</li>
									<li style="margin-left:5px;"><html:text property="familyname"  size="10" name="familyname" value="${familyname}"/></li>
									<li style="margin-left:15px;">名</li>
									<li style="margin-left:5px;"><html:text property="name"  size="10" name="name" value="${name}"/></li>
								</ul>
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">ニックネーム<font color="red">*</font><font class="fxS" style="margin-left:5px;">(全角10文字以内)</font></div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<html:text property="nickname" size="20" name="nickname" value="${nickname}"/>
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">メールアドレス<font color="red">*</font><font class="fxS" style="margin-left:5px;">(※メールアドレスは公開されません。また、登録後に変更することが可能です。)</font></div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<html:text property="email" size="40" name="email" value="${email}"/>
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">パスワード<font color="red">*</font><font class="fxS" style="margin-left:5px;">※6文字以上65文字以内でご記入ください。</font></div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<ul>
									<li><html:password property="passwd" size="20" name="passwd" value="${passwd}"/><label style="margin-left:5px;">(パスワード)</label></li>
									<li style="margin-top:5px;"><html:password property="repasswd" size="20" name="repasswd" value="${repasswd}"/><label style="margin-left:5px;">(確認用)</label></li>
								</ul>
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">
							<ul class="clearfix">
								<li class="lHead">グループ<font color="red">*</font><font class="fxS" style="margin-left:5px;">(全角50文字以内)</font></li>
							</ul>
						</div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<html:text property="group"  size="30" name="group" value="${group}"/>
								<font class="fxS">※メールに記載されているグループをご記入ください。</font>
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
					
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">
							<div class="btnArea">
								<input type="submit" name="touroku" value="登録">
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
					
				</div>
				<!--/listBoxProfile01-->
			</div>
			<!--/mainEvent-main-->
		</div>
		<!--/mainCommunity-->
	</div>
	<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/sp10.png" alt=""/>
	<!--/contents-->
	<!-- フッター -->
	<%@ include file="/WEB-INF/view/pc/fnfooter.jsp"%>
	<!-- フッター -->
</div>
<!--/container-->
</s:form>
</body>
</html>
