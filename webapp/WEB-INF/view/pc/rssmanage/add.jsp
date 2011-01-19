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
<title>[frontier]${f:h(vRssName)}のメンバ追加</title>
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/assort.css" type="text/css" />

<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>
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
	
	<!--navbarメニューエリア-->
	<!-- マイページ共通 -->
	<%@ include file="/WEB-INF/view/pc/fmenu.jsp"%>
	<!-- マイページ共通 -->
	<!--/navbarメニューエリア-->
	
	<div id="contents" class="clearfix">
		<div class="mainMember">
			<!--メイン-->

			<div class="mainMember-main">
				
				<div class="listBoxRSS" style="border-width:0 1px 1px 1px;">
					<div class="listBoxRSSTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name inputColStick">${f:h(vRssName)}のメンバ追加</div></td>
							</tr>
						</table>
					</div>
					<table cellspacing="0" width="100%;" style="border-collapse:collapse;">
						<tr><td colspan="3" class="left dotted" valign="top" width="100%"><b>検索を実行して、RSSファイル出力対象となるメンバーを追加してください。</b>
							</td></tr>
						<tr><td class="left dotted" valign="top" width="120px">名前</td><td class="center dotted"><input type="text" name="searchword" value="${f:h(searchword)}"></td><td class="right02 dotted"><input type="radio" name="rd01" value="1" <c:if test="${rd01 == 1}">checked</c:if>>ニックネーム<input type="radio" name="rd01" value="2" <c:if test="${rd01 == 2}">checked</c:if>>名前</td></tr>
						<tr><td class="left dotted" valign="top" width="120px">グループ</td>
						<td class="center dotted">
							<select name="searchgroup" style="width:350px;">
							<option<c:if test="${searchgroup==null || searchgroup==''}"> selected</c:if> value="">選択して下さい</option>
							<c:forEach var="e" items="${groupList}">
								<option value="${e.gid}"<c:if test="${searchgroup==e.gid}"> selected</c:if>>${f:h(e.gname)}</option>
							</c:forEach>
							</select>
						</td>
						<td class="dotted">&nbsp;</td></tr>
					</table>
					<div class="ttlHeadArea" style="text-align: center;width:100%;">
						<div class="btnArea pad10">
							<input type="submit" name="search" value="検索" class="ttlbtn" />
							<input type="submit" name="index" value="一覧へ戻る" class="ttlbtn" />
						</div>
					</div>

					<%-- RSSファイルメンバー一覧表示 --%>
					<s:form>
					<c:forEach var="e" items="${rssMember}" varStatus="status">
						<%-- 初回のみ実行 --%>	
						<c:if test="${status.first}">
							<div class="listBoxRSSBottom" id="listBoxRSSBottom">
							<!-- <div class="align03"><a href="#">&lt;&lt;前のXX件</a><span>|</span><a href="#">次のXX件&gt;&gt;</a></div> -->
							<div class="align03"><c:if test="${offset>0}"><s:link href="/pc/rssmanage/prepg/">&lt;&lt;前の${appDefDto.FP_RSS_MEMBER_LIST_MAX}件</s:link></c:if><c:if test="${offset>0 and resultscnt>(offset + fn:length(rssMember))}"><span>|</span></c:if><c:if test="${resultscnt>(offset + fn:length(rssMember))}"><s:link href="/pc/rssmanage/nxtpg/">次の${appDefDto.FP_RSS_MEMBER_LIST_MAX}件&gt;&gt;</s:link></c:if></div>
							<table cellspacing="0" border="1" bordercolor="#000" width="100%;" style="border-collapse:collapse;">
							<tr><th width="5%">&nbsp;</th><th width="40%">名前<label onclick="j$('#listBoxRSSBottom').load('/frontier/pc/ajax/rssmanage/loadData/1?'+rand(10));"><c:choose><c:when test="${sortname eq '1'}"><font color="#ff0000">▲</font></c:when><c:otherwise>▲</c:otherwise></c:choose></label><label onclick="j$('#listBoxRSSBottom').load('/frontier/pc/ajax/rssmanage/loadData/2?'+rand(10));"><c:choose><c:when test="${sortname eq '2'}"><font color="#ff0000">▼</font></c:when><c:otherwise>▼</c:otherwise></c:choose></label></th>
								<th width="40%">グループ</th>
								<th width="15%">登録日<label onclick="j$('#listBoxRSSBottom').load('/frontier/pc/ajax/rssmanage/loadData/3?'+rand(10));"><c:choose><c:when test="${sortname eq '3'}"><font color="#ff0000">▲</font></c:when><c:otherwise>▲</c:otherwise></c:choose></label><label onclick="j$('#listBoxRSSBottom').load('/frontier/pc/ajax/rssmanage/loadData/4?'+rand(10));"><c:choose><c:when test="${sortname eq '4'}"><font color="#ff0000">▼</font></c:when><c:otherwise>▼</c:otherwise></c:choose></label></th>
							</tr>
						</c:if>
						<tr>
							<td><input type="checkbox" value="${f:h(e.mid)}" name="checkJoin" <c:if test="${e.mid eq checkJoin[status.index]}">checked</c:if>/></td>
							<td class="align04">${f:h(e.nickname)}（${f:h(e.name)}）${f:h(e.statusname)}</td> 
							<td class="align04">${f:h(e.gname)}</td>
							<td class="align02">${f:h(e.entdate)}</td>
						</tr>
						<%-- 最後のみ実行 --%>
						<c:if test="${status.last}">
							</table>
							</div>
							<div class="ttlHeadArea" style="text-align: center;width:100%;">
								<div class="btnArea padBtm10">
									<input type="submit" name="memberAdd" value="登録" class="ttlbtn" />
								</div>
							</div>
						</c:if>
					</c:forEach>
					</s:form>

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
