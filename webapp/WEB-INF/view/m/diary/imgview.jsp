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
	<title>[frontier]日記画像閲覧</title>
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
			<!--header-->
			<div style="background-color:#afafaf;">
				&nbsp;<span style="color:#0000ff; font-size:xx-small;">●</span>&nbsp;<span style="font-size:xx-small;">日記画像閲覧</span>
			</div>

			<!--/header-->
			<!--コンテンツ開始-->
				
				<img src="${f:h(appDefDto.FP_CMN_CONTENTS_ROOT)}${f:h(picURL)}"><br>
				<s:link href="/m/diary/view/${diaryId}/${diaryDay}/${mid}"><span style="font-size:xx-small;">戻る</span></s:link><br>


				
				<hr style="height:2px; border-color:#7d7d7d; background-color:#7d7d7d;">
				
				<div style="background-color:#dddddd; text-align:center;">
					<span style="font-size:xx-small;"><s:link href="/m/top/" accesskey="0">[0]ﾏｲﾄｯﾌﾟ</s:link></span>
				</div>

				<img src="/images/m/sp5.gif" ><br>
				<div style="text-align:left;">
					<span style="font-size:xx-small;">◆ｻﾎﾟｰﾄﾒﾆｭｰ</span><br>
					<!--├<a href="csns01.html">ﾛｸﾞｱｳﾄ</a><br>-->
					└<s:link href="/m/logout/"><span style="font-size:xx-small;">ﾛｸﾞｱｳﾄ</span></s:link><br>

				</div>
				<hr style="height:2px; border-color:#7d7d7d; background-color:#7d7d7d;">
				<!--footer-->
				<img src="/images/m/sp5.gif" ><br>
				
	<!-- フッター -->
	<%@ include file="/WEB-INF/view/m/footer.jsp"%>
	<!-- フッター -->
	</div>
</s:form>
</body>
</html>