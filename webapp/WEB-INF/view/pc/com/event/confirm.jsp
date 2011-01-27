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
<title>[frontier]${f:h(communityDto.comnm)} コメントの削除</title>
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
		<div class="mainDiary">
			<!--メイン-->

			<div class="mainDelDiary-main">
			
				<div class="eventDelCommentHead clearfix">
					<ul>
						<li>${f:h(communityDto.comnm)}&nbsp;&nbsp;コメントの削除</li>
					</ul>
				</div>
			
				<div class="listBox12 clearfix">
					<div class="listBox12Title">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name inputColStick">コメントの削除</div></td>
							</tr>
						</table>
					</div>
					<!--/listBox12Title-->
					<div class="listBoxHead clearfix">
						<div class="ttlHeadDelArea">下記のコメントを削除しますか？</div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<div class="bodyinputAreaSub clearfix">
									<ul>
										<li>
											<input type="submit" name="deletecmnts" value="削除する" />
										</li>
										<li style="float:right;">
											<input type="submit" name="cancel" value="やめる" />
										</li>
									</ul>
								</div>
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
				</div>
				<!--/listBox12-->
			</div>
			<!--/mainDelDiary-main-->

			<div class="mainDelDiary-main" style="margin-bottom:-10px;">
				<div class="listBox12 clearfix">
					<div class="listBox12Title">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name myColStick">コメント内容</div></td>
							</tr>
						</table>
					</div>
					<!--/listBox12Title-->
					
					<c:forEach var="e" items="${results}">
					<div class="listBoxHead clearfix">
						<div class="ttlHeadAreaList">
							<div class="ttlHeadAreaListSub">
								<dl>
									<dt class="txtArea"><s:link href="/pc/mem/${f:h(e.mid)}">${f:h(e.nickname)}</s:link></dt>
									<dd class="timeArea"><fmt:formatDate value="${f:date(f:h(e.entdate),'yyyyMMddHHmm')}" pattern="yyyy年MM月dd日HH:mm" /></dd>
								</dl>
							</div>
						</div>
						<div class="bodyArea">
							<div class="bodyDelArea delEventCommentContents">
								<div class="delEventCommentContents" style="text-align:center; padding-bottom:5px;">
									${e.pichtml}
								</div>
								${f:br(e.cmnthtml)}
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
					</c:forEach>

				</div>
				<!--/listBox12-->
			</div>
			<!--/mainDelDiary-main-->

		</div>
		<!--/mainDiary-->
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
