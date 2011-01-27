<%@ page pageEncoding="UTF-8" %>
<div class="mainFile-side">
	<div class="fileSideBox">
		<div class="fileSideBoxTitle">
			<table class="list_ttl">
				<tr>
					<td class="ttl_name_sp"><div class="ttl_name comColStick">管理メニュー</div></td>
				</tr>
			</table>
		</div>
		<!--/fileSideBoxTitle -->
		<div class="fileSideBoxBody">
			<div class="fileSideBoxFinding">
				<ul>
					<li class="pic"><s:link href="/pc/entfile/" title="ファイルを登録する">ファイルを登録する</s:link></li>
				</ul>
			</div>
			<!--/fileSideBoxFinding -->
		<c:choose>
			<c:when test="${!fileDto.isCategory}"><c:set var="parentLiClass" value="pic_selected" /></c:when>
			<c:otherwise><c:set var="parentLiClass" value="pic" /></c:otherwise>
		</c:choose>
			<div class="fileSideBoxParent">
				<ul>
					<li class="${f:h(parentLiClass)}"><s:link href="/pc/file/" title="一覧">一覧&nbsp;(新着順)</s:link></li>
				</ul>
			</div>
			<!--/fileSideBoxParent -->
		<c:forEach var="e" items="${fileSideBoxCategoryList}">
		<c:choose>
			<c:when test="${e.isSelected}"><c:set var="childLiClass" value="pic_selected" /></c:when>
			<c:otherwise><c:set var="childLiClass" value="pic" /></c:otherwise>
		</c:choose>
			<div class="fileSideBoxChild">
				<ul>
					<li class="${f:h(childLiClass)}"><s:link href="/pc/file/list/${f:h(e.categoryid)}" title="${f:h(e.categorynameorg)}">${f:h(e.categoryname)}&nbsp;(${f:h(e.categorycnt)})</s:link></li>
				</ul>
			</div>
		</c:forEach>
		<!--/fileSideBoxChild -->
		</div>
		<!--/fileSideBoxBody -->
	</div>
	<!--/fileSideBox -->
</div>
<!--/mainFile-side -->