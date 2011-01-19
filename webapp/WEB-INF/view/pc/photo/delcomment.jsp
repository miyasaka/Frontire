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
<title>[frontier]&nbsp;コメントを削除する</title>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/common.js"></script>
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
		<div class="mainPhoto">
			<!--メイン-->
			<div class="mainPhoto-main">
				<div class="listBoxPhoto">
					<div class="listBox10">
						<div class="listBox10Title">
							<table class="ttl">
								<tr>
									<td class="ttl_name_sp"><div class="ttl_name inputColStick">コメントの削除</div></td>
								</tr>
							</table>
						</div>
						<!--/listBox10Title -->
		<c:choose>
			<c:when test="${fn:length(commentResults) > 0}">
				<!-- loop -->
				<c:forEach var="e" items="${commentResults}" varStatus="status">
						<div>
							<table class="photoTitle">
								<tr>
									<td width="60%">
									${f:h(e.cno)}
								<c:choose>
									<c:when test="${e.mid eq userInfoDto.memberId}">
										<s:link href="/pc/top/">${f:h(e.nickname)}</s:link>
									</c:when>
									<c:otherwise>
										<s:link href="/pc/mem/${f:h(e.mid)}/">${f:h(e.nickname)}</s:link>
									</c:otherwise>
								</c:choose>
									</td>
									<td width="40%" style="text-align:right;">
										<fmt:formatDate value="${f:date(f:h(e.entdate),'yyyyMMddHHmmss')}" pattern="yyyy年MM月dd日 HH:mm" />
									</td>
								</tr>
							</table>
							<!--/photoTitle -->
						</div>
						<div>
							<div style="border-style:dotted; border-width:1px 0 0 0; clear:both; padding:5px;">
								<div class="photoBody">
									${f:br(e.viewComment)}
								</div>
							</div>
						</div>
				</c:forEach>
				<!--/loop -->
						<table class="photoTitle">
							<tr>
								<td align="center">
									<div style="margin:5px 0 5px 0;">
										<input type="submit" name="deleteCommentExecute" value="　削　　　除　" class="ttlbtn" />
										<input type="submit" name="stopComment" value="　や　め　る　" class="ttlbtn" />
									</div>
								</td>
							</tr>
						</table>
						<!--/photoTitle -->
			</c:when>
			<c:otherwise>
					<div class="noResult">
						コメントが削除されているか、あるいは存在しないアドレスへのアクセスです。 
					</div>
			</c:otherwise>
		</c:choose>
					</div>
					<!--/listBox10 -->
				</div>
				<!--/listBoxPhoto -->
			</div>
			<!--/mainPhoto-main -->
		</div>
		<!--/mainPhoto -->
	</div>
	<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/sp10.png" alt="" />
	<!--/contents -->

	<!-- footer -->
	<%@ include file="/WEB-INF/view/pc/ffooter.jsp"%>
	<!--/footer -->
</div>
<!--/container -->
</s:form>
</body>
</html>