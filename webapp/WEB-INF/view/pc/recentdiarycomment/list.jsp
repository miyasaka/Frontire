<%@ page language="java" contentType="text/html; charset=windows-31J" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS"/>
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta name="robots" content="nofollow,noindex"     />
<title>[frontier] 日記ｺﾒﾝﾄ記入履歴</title>
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
		<div class="mainMember">
			<!--メイン-->

			<div class="mainMember-main">
			
				<div class="listBoxMemberUpdate" style="border-width:0 1px 1px 1px;">
					<div class="listBoxMemberUpdateTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name myColStick">日記ｺﾒﾝﾄ記入履歴(1～${fn:length(results)}件)</div></td>
							</tr>
						</table>
					</div>
					<div class="listBoxMemberUpdateBody">
<c:forEach var="e" items="${results}" varStatus="status">

						<div class="newdiary">
							<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/book_blue.gif"/><span class="date">${f:h(e.entdate)}</span><span class="title"><s:link href="/pc/diary/view/${f:h(e.diaryid)}/${f:h(e.yyyymmdd)}/${f:h(e.mid)}">${f:h(e.title)}<c:if test="${e.count>0}">(${e.count})</c:if></s:link> (${f:h(e.nickname)})</span>
						</div><br/>
</c:forEach>

					</div>
					
				</div>
				<!--/listBoxMemberUpdate-->
				
			</div>
			<!--/mainMember-main-->
		</div>
		<!--/mainMember-->
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