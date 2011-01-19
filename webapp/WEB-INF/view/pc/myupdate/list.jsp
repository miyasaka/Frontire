<%@ page language="java" contentType="text/html; charset=windows-31J"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS"/>
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta name="robots" content="nofollow,noindex"     />



<title>[frontier] ${f:h(userInfoDto.nickname)}の更新履歴</title>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<meta name="Slurp" content="NOYDIR" />
<meta name="robots" content="noodp" />
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/assort.css" type="text/css" />
</head>

<body>
<s:form>
<div id="container">
	<!--header-->
	<%@ include file="/WEB-INF/view/pc/fheader.jsp"%>
	<!--/header-->

	<!--navbarメニューエリア-->
	<%@ include file="/WEB-INF/view/pc/fmenu.jsp"%>
	<!--/navbarメニューエリア-->
	
	<div id="contents" class="clearfix">
		<div class="mainMyUpdate">
			<!--メイン-->

			<div class="mainMyUpdate-main">
			
				<div class="listBoxMyUpdate">
					<div class="listBoxMyUpdateHead">${f:h(userInfoDto.nickname)}の更新履歴</div>
<c:if test="${fn:length(DiaryList)>0}">
					<div class="listBoxMyUpdateTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name myColStick"><s:link href="/pc/diary/list/${userInfoDto.memberId}" title="最新日記">最新日記</s:link></div></td>
							</tr>
						</table>
					</div>
					<div class="listBoxMyUpdateBody">
						<div class="diary">
							<c:forEach var="e" items="${DiaryList}" varStatus="i">
								<div><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/book_blue.gif"/><span><fmt:formatDate value="${f:date(f:h(e.entdate),'yyyyMMddHHmm')}" pattern="yyyy年MM月dd日 HH:mm" /></span><s:link href="/pc/diary/view/${f:h(e.diaryid)}/${fn:substring(f:h(e.entdate),0,8)}/${f:h(userInfoDto.memberId)}" title="${f:h(e.title) }">${f:h(e.stitle) }<c:if test="${e.cnt>0}">(${e.cnt})</c:if></s:link></div>
							</c:forEach>
						</div>
					</div>
					<div class="moreLink"><s:link href="/pc/diary/list/${userInfoDto.memberId}">もっと見る</s:link></div>
</c:if>
<c:if test="${fn:length(PhotoList)>0}">
					<div class="listBoxMyUpdateTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name myColStick"><s:link href="/pc/photo/list/${userInfoDto.memberId}" title="最新フォト">最新フォト</s:link></div></td>
							</tr>
						</table>
					</div>
					<div class="listBoxMyUpdateBody">
						<div class="photo">
							<c:forEach var="e" items="${PhotoList}" varStatus="i">
								<div><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/camera-deji1-silver.gif"/><span>${e.entdate}</span><s:link href="/pc/photo/view/${e.mid}/${e.ano}" title="${f:h(e.title)}">${f:h(e.stitle)}<c:if test="${e.comments!=null}">(${e.comments})</c:if></s:link></div>
							</c:forEach>
						</div>
					</div>
					<div class="moreLink"><s:link href="/pc/photo/list/${userInfoDto.memberId}">もっと見る</s:link></div>
</c:if>
				</div>
				<!--/listBoxMyUpdate-->
				
			</div>
			<!--/mainMyUpdate-main-->
		</div>
		<!--/mainMyUpdate-->
	</div>
	<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/sp10.png" alt=""/>
	<!--/contents-->
	 <div id="footer">
	 	<div style="color:#fff; text-align:center;">
	 		<address>Copyright c 2009 Charlie Software Co.,Ltd. All rights reserved.</address>
	 	</div>
	 

	 </div>
	 <!--/footer-->
</div>
<!--/container-->
</s:form>
</body>
</html>
