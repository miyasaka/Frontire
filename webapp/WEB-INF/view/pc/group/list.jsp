<%@ page language="java" contentType="text/html; charset=windows-31J" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<title>[frontier]グループ一覧</title>
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
		<div class="mainMember-g">
			<!--メイン-->

			<div class="mainMember-main-g">
			
				<div class="listBoxMemberList-g">
					<div class="listBoxMemberListTitle-g">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name manageColStick">グループ一覧<c:if test="${fn:length(groupList)!=0}">(全${fn:length(groupList)}件)</c:if></div></td>
							</tr>
						</table>
					</div>
					<div class="linkBox01"><div class="note"></div><div><s:link href="/pc/group/entry/">新規追加</s:link></div></div>
						<!-- 一覧 -->

					<c:forEach var="e" items="${groupList}" varStatus="status">
						<%-- 初回のみ実行 --%>	
						<c:if test="${status.first}">
							<div class="comMemManage-g">
								<table>
									<tr>
										<th class="thleft">画像</th>
										<th>グループ名</th>
										<th class="thcenter">人数</th>
										<th style="width:10%;">&nbsp;</th>
									</tr>
						</c:if>

						<tr>
							<td class="tdleft">
								<div ALIGN="center">
									<c:choose>
										<c:when test="${e.pic!=null and e.pic!=''}"><IMG SRC="${f:h(fn:replace(e.pic,'[dir]','pic42'))}" alt=""></c:when>
										<c:otherwise><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg42.gif"  alt="" /></c:otherwise>
									</c:choose>
								</div></td>
							<td>${f:h(e.gname)}</td>
							<td class="tdcenter">${f:h(e.joinnumber)}人</td>
							<td class="tdright">[<s:link href="/pc/group/update/${f:h(e.gid)}">編集</s:link>]</td>
						</tr>

						<%-- 最後のみ実行 --%>
						<c:if test="${status.last}">
								</table>
							</div>
						</c:if>

					</c:forEach>

					<%-- 0件の場合 --%>
					<c:if test="${fn:length(groupList)==0}">
						<div class="noGroup">グループが登録されていません。「新規追加」よりグループを作成して下さい。</div>
					</c:if>
						<!-- 一覧 -->

					<!--/listBoxMemberListTitle-->
					
				</div>
				<!--/listBoxMemberList-->
				
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
