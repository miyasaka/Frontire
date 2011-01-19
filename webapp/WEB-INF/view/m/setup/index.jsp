<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/contentType.jsp"%>
<?xml version="1.0" encoding="Shift_JIS"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHML 1.0 transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ja" lang="ja">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
	<title>簡易ﾛｸﾞｲﾝ設定</title>
<style type="text/css">
<!--
	body{
		font-size:xx-small;
		background-color:#fafafa;
	}
	a:link{
		color:#2288FF;
	}
	a:focus{
		color:#ffffff;
	}
	a:visited{
		color:#2288FF;
	}
	a:hover{
		color:#ff0000;
	}

-->
</style>
</head>
<body style="width:350px;">
<s:form>
	<div>
		<div style="font-size:xx-small;">
			<div style="background-color:#100800; text-align:center;">
				<img src="/images/m/sp5.gif" ><br>
				<a name="TOP"><img src="/images/m/logom.gif" ></a>
				<img src="/images/m/sp5.gif" ><br>
			</div>
			<!--コンテンツ開始-->
			<div style="background-color:#afafaf;">
				<span style="font-size:xx-small;">簡易ﾛｸﾞｲﾝ設定</span>
			</div>
				<hr style="border-color:#bdbdbd; background-color:#bdbdbd;">

				<!--ｴﾗｰ文言表示ｴﾘｱ-->
				<div style="font-size:xx-small;color:#ff0000;"><html:errors/></div>
				<!--マイコンテンツ-->
				<span style="font-size:xx-small;">※簡易ﾛｸﾞｲﾝを行うために、認証コードの設定を行います。<br>※認証コードは半角数字1-10桁で設定を行ってください。<br><br>
				<div>
					■認証コード：<br>
					<input type="password" style="width:80px;" istyle="4" format="*N" mode="numeric" name="code" maxlength="10"  value="">
				</div>
				<div>
					■認証コード(再)：<br>
					<input type="password" style="width:80px;" istyle="4" format="*N" mode="numeric" name="confirm" maxlength="10"  value="">
				</div>
				
				<input type="submit" name="setcode" value="設定">&nbsp;<input type="submit" name="back" value="戻る">
				

				<hr style="border-color:#bdbdbd; background-color:#bdbdbd;">
				<!--footer-->
				<%@ include file="/WEB-INF/view/m/footer.jsp"%>
				<!--/footer-->
			<!--コンテンツ終了-->
		</div>
	</div>
	<html:hidden property="hiddenParam" value="${hiddenParam}"/>
</s:form>
</body>
</html>