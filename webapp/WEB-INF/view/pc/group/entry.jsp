<%@ page language="java" contentType="text/html; charset=windows-31J" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<title>[frontier]グループの${f:h(mode)}</title>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/group.js"></script>
<%-- ここからajax関係 --%>
<script language="javascript" type="text/javascript" src="/static/js/prototype.js"></script>
<script language="javascript" type="text/javascript" src="/static/js/jquery.js"></script>
<script>
// コンフリクト対応
jQuery.noConflict();
var j$ = jQuery;
</script>
<script>
function rand(len) {
	var result = '';
	var source = 'abcdefghajklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890';
	var sourceLen = source.length;
	for (var i = 0; i < len; i++) {
		result += source.charAt(Math.floor(Math.random() * sourceLen));
	}
	return result;
}
</script>
<%-- ここまでajax関係 --%>
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/assort.css" type="text/css" />

</head>

<body>
<s:form enctype="multipart/form-data">
<div id="container">
	<!-- ヘッダー -->
	<%@ include file="/WEB-INF/view/pc/fheader.jsp"%>
	<!-- ヘッダー -->

	<!--navbarメニューエリア-->
	<!-- マイページ共通 -->
	<%@ include file="/WEB-INF/view/pc/fmenu.jsp"%>
	<!-- マイページ共通 -->
	<!--/navbarメニューエリア-->
	
	<div id="contents" class="clearfix">
		<div class="mainMember-g">
			<!--メイン-->

			<div class="mainMember-main-g">
			
				<!--エラー-->
				<html:errors/>
				<!--/エラー--> 

				<div class="listBoxMemberList-g">
					<div class="listBoxMemberListTitle-g">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name inputColStick">グループの${f:h(mode)}</div></td>
							</tr>
						</table>
					</div>
					<div class="linkBox01">
						<div CLASS="input-left">
							<div CLASS="input-left-letter">グループ名<span CLASS="input-left-asterisk">*</span></div>
						</div>
						<div CLASS="input-right">&nbsp;<input TYPE="text" size="70" name="gname" value="${f:h(gname)}"></div>
						<div CLASS="oya-pic">
							<div CLASS="${f:h(picLeft)}">
								<div CLASS="input-left-letter">グループ画像</div>
							</div>
							<%-- 画像登録がある場合のみ表示 --%>
							<c:if test="${picFlg}">
								<div CLASS="${f:h(picRight)}" style="height:76px;"><IMG SRC="${f:h(fn:replace(pic,'[dir]','pic76'))}"></div>
								<div CLASS="${f:h(picRight)}"><s:link href="/pc/group/delPic/">削除</s:link></div>
							</c:if>
							<div CLASS="${f:h(picRight)}"><input size="50" type="file" name="picpath" /></div>
						</div>
						<!-- 検索 -->
						<div CLASS="searchTitle">参加者<c:if test="${mode eq '編集'}">(${f:h(joinnumber)})</c:if><span CLASS="redTitle">※グループに参加させる人を選択して下さい。表示しているユーザのみ反映されます。</span></div>
						<div CLASS="searchTitle"><input TYPE="text" SIZE="100" VALUE="${f:h(searchmem)}" name="searchmem">&nbsp;&nbsp;<input TYPE="submit" VALUE="絞り込む" CLASS="ttlbtn" name="search"></div>
						<div CLASS="searchTitle"><input TYPE="radio" NAME="r1" value="1" <c:if test="${r1 eq '1'}">checked</c:if>>名前で絞り込む&nbsp;&nbsp;<input TYPE="radio" NAME="r1" value="2" <c:if test="${r1 eq '2'}">checked</c:if>>PCアドレスで絞り込む&nbsp;&nbsp;<input TYPE="checkbox" name="joincheck" value="1" <c:if test="${joincheck eq '1'}">checked</c:if>>グループ未参加者のみ表示</div>
						<!-- 一覧 -->
							<%-- ajax --%>
							<c:forEach var="e" items="${groupMemberList}" varStatus="status">
								<%-- 初回のみ実行 --%>
								<c:if test="${status.first}">
									<div class="groupMainBoxBody" id="groupMainBoxBody">
									<div CLASS="searchTitle">
										<div CLASS="search-left">
											<label STYLE="font-size:10px;padding:0 2px 0 0;">全て</label><a HREF="#" onclick="JavaScript:checkAll('0', 'checkJoin', true);">参加</a>&nbsp;|&nbsp;<a HREF="#"  onclick="JavaScript:checkAll('0', 'checkJoin', false);">不参加</a>
										</div>
										<div CLASS="search-right"><c:if test="${offset>0}"><s:link href="/pc/group/prepg/">&lt;&lt;前の${appDefDto.FP_GRP_LIST_MAX}件</s:link></c:if><c:if test="${offset>0 and resultscnt>(offset + fn:length(groupMemberList))}">&nbsp;|&nbsp;</c:if><c:if test="${resultscnt>(offset + fn:length(groupMemberList))}"><s:link href="/pc/group/nxtpg/">次の${appDefDto.FP_GRP_LIST_MAX}件&gt;&gt;</s:link></c:if></div>
									</div>
									<table>
										<tr>
											<th CLASS="th1">参加</th>
											<th CLASS="th2">管理</th>
											<%-- ソースは見にくいが改行はNG。半角スペースが入るため  --%>
											<th CLASS="th3">名前<label onclick="j$('#groupMainBoxBody').load('/frontier/pc/ajax/group/loadData/1?'+rand(10));"><c:choose><c:when test="${sortname eq '1'}"><font color="#ff0000">▲</font></c:when><c:otherwise>▲</c:otherwise></c:choose></label><label onclick="j$('#groupMainBoxBody').load('/frontier/pc/ajax/group/loadData/2?'+rand(10));"><c:choose><c:when test="${sortname eq '2'}"><font color="#ff0000">▼</font></c:when><c:otherwise>▼</c:otherwise></c:choose></label></th>
											<th CLASS="th4">PCアドレス<label onclick="j$('#groupMainBoxBody').load('/frontier/pc/ajax/group/loadData/3?'+rand(10));"><c:choose><c:when test="${sortname eq '3'}"><font color="#ff0000">▲</font></c:when><c:otherwise>▲</c:otherwise></c:choose></label><label onclick="j$('#groupMainBoxBody').load('/frontier/pc/ajax/group/loadData/4?'+rand(10));"><c:choose><c:when test="${sortname eq '4'}"><font color="#ff0000">▼</font></c:when><c:otherwise>▼</c:otherwise></c:choose></label></th>
											<th CLASS="th5">登録日<label onclick="j$('#groupMainBoxBody').load('/frontier/pc/ajax/group/loadData/5?'+rand(10));"><c:choose><c:when test="${sortname eq '5'}"><font color="#ff0000">▲</font></c:when><c:otherwise>▲</c:otherwise></c:choose></label><label onclick="j$('#groupMainBoxBody').load('/frontier/pc/ajax/group/loadData/6?'+rand(10));"><c:choose><c:when test="${sortname eq '6'}"><font color="#ff0000">▼</font></c:when><c:otherwise>▼</c:otherwise></c:choose></label></th>
										</tr>
								</c:if>
								<tr>
									<td CLASS="td1"><input TYPE="checkbox" value="${f:h(e.mid)}" name="checkJoin" <c:if test="${e.mid eq checkJoin[status.index]}">checked</c:if>></td>
									<td CLASS="td2"><input TYPE="checkbox" value="${f:h(e.mid)}" name="checkAuth" <c:if test="${e.mid eq checkAuth[status.index]}">checked</c:if>></td>
									<td CLASS="td3">${f:h(e.name)}</td>
									<td CLASS="td4">${f:h(e.email)}</td>
									<td CLASS="td5">${f:h(e.entdate)}</td>
								</tr>
								<%-- 最後のみ実行 --%>
								<c:if test="${status.last}">
									</table>
									</div>
									<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/sp10.png" alt=""/>
								</c:if>
							</c:forEach>

							<%-- 検索結果が一件もない場合 --%>
							<c:if test="${fn:length(groupMemberList)==0}">
								<div class="noMember">条件に一致するユーザは見つかりませんでした。 </div>
							</c:if>
					</div>

					<div class="ttlHeadArea" style="text-align: center;width:100%;">
						<div class="btnArea" STYLE="border-width:1px 0 0 0;border-style:solid;">
							<%-- 遷移状態によってボタンを切り替える --%>
							<c:choose>
								<c:when test="${mode eq '登録'}"><input type="submit" name="insert" value="登録" class="ttlbtn" /></c:when>
								<c:otherwise><input type="submit" name="updateGroup" value="更新" class="ttlbtn" /></c:otherwise>
							</c:choose>
							<input type="submit" name="index" value="一覧に戻る" class="ttlbtn" />
						</div>
					</div>

				</div>
				<!--/listBoxMemberList-->
				<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/sp10.png" alt=""/>

				<%-- グループ削除エリアここから --%>
				<c:if test="${mode eq '編集'}">
					<div class="mainMember-main-g">
						<div class="listBoxMemberList-g">
							<div class="listBoxMemberListTitle-g">
								<table class="list_ttl">
									<tr>
										<td class="ttl_name_sp"><div class="ttl_name inputColStick">グループの削除</div></td>
									</tr>
								</table>
							</div>
							<div class="listBoxHead listBoxHeadline clearfix">
								
										<div CLASS="del">グループを削除します。</div>
										<div CLASS="del">削除したグループは戻すことはできません。</div>
								<div class="ttlHeadArea" style="text-align: center;">
									<div class="btnArea" STYLE="border-width:1px 0 0 0;border-style:dotted;">
										<input type="submit" name="confirm" value="削除" class="ttlbtn" />
									</div>
								</div>
							</div>
							<!--/listBoxHead -->
						</div>
						<!--/listBoxEntPhoto -->
					</div>
				</c:if>
				<%-- グループ削除エリアここまで --%>

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
