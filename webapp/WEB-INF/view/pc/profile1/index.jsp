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
<title>[frontier] 基本設定</title>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/colorful.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/fshout.js"></script>
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
			

				<!--[errorArea]-->
				<html:errors/> 

				<div class="listBoxProfile01">
					<div class="listBoxProfile01Title">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp">
									<div class="ttl_name inputColStick">
										<ul class="clearfix">
											<li>基本設定<span style="margin-left:5px;">(<font color="red">*</font><font class="fxS">の項目は必須</font>)</span></li>
											<li class="alTxt"><font class="fxS">※基本設定で設定した内容は公開されません。</font></li>
										</ul>
									</div>
								</td>
							</tr>
						</table>
					</div>
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">PCアドレス<font color="red">*</font></div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<html:text property="email"  size="30" name="email" value="${email}"/>
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">携帯アドレス</div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<html:text property="mobileemail" size="30" name="mobileemail" value="${mobileemail}"/><label style="margin:0 5px;">@</label><select name="mobileDomain" style="width:150px;">
								<option<c:if test="${mobileDomain==null || mobileDomain==''}"> selected</c:if> value="0">----------</option>
								<c:forEach var="e" items="${mdomainlist}"><option value="${e.itemname}"<c:if test="${mobileDomain==e.itemname}"> selected</c:if>>${f:h(e.itemname)}</option>
								</c:forEach>
								</select>
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">パスワード<font class="fxS" style="margin-left:5px;">※変更がある場合のみ入力してください。</font></div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<ul>
									<li><html:password property="passwd" size="20" name="passwd" value="${passwd}" maxlength="50" /><label style="margin-left:5px;">(新しいパスワード)</label></li>
									<li style="margin-top:5px;"><html:password property="repasswd" size="20" name="repasswd" value="${repasswd}" maxlength="50" /><label style="margin-left:5px;">(確認用)</label></li>
								</ul>
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">日記公開範囲<font color="red">*</font></div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<select name="diaryLevel">
									<option value="9" ${diaryLevel eq '9'?'selected':''}>非公開</option>
									<option value="2" ${diaryLevel eq '2'?'selected':''}>グループに公開</option>
									<option value="1" ${diaryLevel eq '1'?'selected':''}>全体に公開</option>
								</select>
							</div>
						</div>
					</div>
					
					<%-- ■■■■■■■■■■■■■■ twitter ■■■■■■■■■■■■■ --%>
					<div class="listBoxProfile01Title" style="border-top-style:solid; border-top-width:1px;">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp">
									<div class="ttl_name inputColStick">
										<ul class="clearfix">
											<li>F Shout設定<span style="margin-left:5px;"><font class="fxS">(F ShoutをTwitterにも登録したい場合はご登録ください。)</font></span></li>
										</ul>
									</div>
								</td>
							</tr>
						</table>
					</div>
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">F Shout公開範囲</div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<select name="fshoutLevel">
									<option value="0" ${fshoutLevel eq '0'?'selected':''}>外部公開</option>
									<option value="1" ${fshoutLevel eq '1'?'selected':''}>Frontier Netまで公開</option>
									<option value="2" ${fshoutLevel eq '2'?'selected':''}>マイFrontierまで公開</option>
									<option value="3" ${fshoutLevel eq '3'?'selected':''}>グループまで公開</option>
								</select>
							</div>
						</div>
					</div>
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">F Shout更新頻度&nbsp;&nbsp;<font class="fxS">(TOPページで表示するF Shout及びTwitterの自動更新間隔を設定できます。)</font></div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<select name="updateFrequency">
									<option value="0" ${updateFrequency eq '0'?'selected':''}>設定しない</option>
									<option value="60" ${updateFrequency eq '60'?'selected':''}>1分毎</option>
									<option value="120" ${updateFrequency eq '120'?'selected':''}>2分毎</option>
									<option value="180" ${updateFrequency eq '180'?'selected':''}>3分毎</option>
									<option value="240" ${updateFrequency eq '240'?'selected':''}>4分毎</option>
									<option value="300" ${updateFrequency eq '300'?'selected':''}>5分毎</option>
									<option value="360" ${updateFrequency eq '360'?'selected':''}>6分毎</option>
									<option value="420" ${updateFrequency eq '420'?'selected':''}>7分毎</option>
									<option value="480" ${updateFrequency eq '480'?'selected':''}>8分毎</option>
									<option value="540" ${updateFrequency eq '540'?'selected':''}>9分毎</option>
									<option value="600" ${updateFrequency eq '600'?'selected':''}>10分毎</option>									
								</select>
							</div>
						</div>
					</div>
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">F Shout投稿先&nbsp;&nbsp;<font class="fxS">(デフォルトの投稿先を指定できます。)</font></div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<select name="fshoutFrom">
									<option value="0" ${fshoutFrom eq '0'?'selected':''}>F Shoutのみ投稿</option>
									<%-- Twitter使用の場合のみ表示 --%>
									<c:if test="${twitterFlg eq '2'}">
										<option value="1" ${fshoutFrom eq '1'?'selected':''}>Twitterのみ投稿</option>
										<option value="2" ${fshoutFrom eq '2'?'selected':''}>F Shout + Twitterに投稿</option>
									</c:if>
								</select>
							</div>
						</div>
					</div>
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">Twitter利用設定&nbsp;&nbsp;<font class="fxS">(FrontierからTwitterを利用する場合は下記リンクを押して下さい。)</font></div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<s:link href="/pc/profile1/oauth/">Twitterアカウントを追加する</s:link>
							</div>
						</div>
					</div>
					<%-- ■■■■■■■■■■■■■■ twitter ■■■■■■■■■■■■■ --%>
					
					<!--/listBoxHead-->
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">
							<div class="btnArea">
								<input type="submit" name="touroku" value="入力内容を登録" />
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
	<%@ include file="/WEB-INF/view/pc/ffooter.jsp"%>
	<!-- フッター -->
	 <!--/footer-->
</div>
<!--/container-->
<html:hidden property="chktwitterflg" name="chktwitterflg" value="${chktwitterflg}"/>
</s:form>
</body>
</html>
