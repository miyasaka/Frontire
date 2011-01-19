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
<title>[frontier]</title>
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/assort.css" type="text/css" />
<script language="javascript" type="text/javascript" src="/static/js/prototype.js"></script>
<script language="javascript" type="text/javascript" src="/static/js/jquery.js"></script>

<script>
// コンフリクト対応
jQuery.noConflict();
var j$ = jQuery;
</script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/top.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/common.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier_1.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/rss.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontiernet.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/fshout.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/twitter.js"></script>
<script language="javascript" type="text/javascript" src="/static/js/parsexml.js"></script>


<script>
<!--
// ベーススタイル
var baseStyle = "${appDefDto.FP_CMN_COLOR_TYPE}";

/*----------------*/
/*  左コンテンツ  */
/*----------------*/
// イメージウィンドウ作成
function mkImage(){
	var w = new ImageWindow(
		baseStyle,
		"ｲﾒｰｼﾞ",
		"/frontier/pc/profile2/edit/${userInfoDto.memberId}",
		getImageBody(),
		"${defMyPhotoViewCnt}"
	);
	w.mkhtml();
}

// カレンダーウィンドウ作成
function mkCalendar(){
	var w = new CalendarWindow(
		baseStyle,
		"ｽｹｼﾞｭｰﾙ",
		"/frontier/pc/calendar",
		getCalendarBody(),
		"${defScheduleViewCnt}"
	);
	w.mkhtml();
}

//ﾏｲ更新情報ウィンドウ作成
function mkMyUpdate(){
<c:if test="${fn:length(MyUpdateInfo)>0}">
	var w = new MyUpdateWindow(
		baseStyle,
		"ﾏｲ更新情報",
		"/frontier/pc/myupdate",
		getMyUpdateBody(),
		"${defMyUpdateViewCnt}"
	);
	w.mkhtml();
</c:if>
}

//ｺﾐｭﾆﾃｨ一覧ウィンドウ作成
function mkCommunityList(){
<c:if test="${communityCnt>0}">
	var w = new CommunityWindow(
		baseStyle,
		"ｺﾐｭﾆﾃｨ(${communityCnt})",
		"/frontier/pc/clist/${userInfoDto.visitMemberId}",
		getCommunityListBody(),
		"${defCommunityViewCnt}"
	);
	w.mkhtml();
</c:if>
}

//ｸﾞﾙｰﾌﾟ一覧ウィンドウ作成
function mkGroupList(){
<c:if test="${GroupListCnt>0}">
	var w = new GroupWindow(
		baseStyle,
		"ｸﾞﾙｰﾌﾟ(${GroupListCnt})",
		"/frontier/pc/mlist/group/${userInfoDto.visitMemberId}",
		getGroupListBody(),
		"${defGroupViewCnt}"
	);
	w.mkhtml();
</c:if>
}

//私がﾌｫﾛｰ一覧ウィンドウ作成
function mkFollowYouList(){
<c:if test="${FollowyouListCnt>0}">
	var w = new FollowYouWindow(
		baseStyle,
		"ﾌｫﾛｰしている(${FollowyouListCnt})",
		"/frontier/pc/mlist/follow/${userInfoDto.visitMemberId}",
		getFollowYouListBody(),
		"${defFollowyouViewCnt}"
	);
	w.mkhtml();
</c:if>
}

//私をﾌｫﾛｰ一覧ウィンドウ作成
function mkFollowMeList(){
<c:if test="${FollowmeListCnt>0}">
	var w = new FollowMeWindow(
		baseStyle,
		"ﾌｫﾛｰされている(${FollowmeListCnt})",
		"/frontier/pc/mlist/follower/${userInfoDto.visitMemberId}",
		getFollowMeListBody(),
		"${defFollowmeViewCnt}"
	);
	w.mkhtml();
</c:if>
}

/*----------------*/
/* 中央コンテンツ */
/*----------------*/

//ﾒﾝﾊﾞｰ日記ウィンドウ作成
function mkMemberDiary(){
<c:if test="${fn:length(fdiaryNewList)>0}">
 	var w = new MemberDiaryWindow(
 		baseStyle,
 		"ﾒﾝﾊﾞｰ日記",
 		"/frontier/pc/newfrienddiary",
 		getMemberDiaryBody(),
		"${defMemDiaryViewCnt}"
 	);
 	w.mkhtml();
</c:if>
}

//ｺﾐｭﾆﾃｨウィンドウ作成
function mkBbs(){
<c:if test="${fn:length(communityNewList)>0}">
	var w = new BbsWindow(
		baseStyle,
		"ｺﾐｭﾆﾃｨ情報",
		"/frontier/pc/newbbs",
		getBbsBody(),
		"${defCommunityBbsViewCnt}"
	);
	w.mkhtml();
</c:if>
}

// ﾒﾝﾊﾞｰﾌｫﾄウィンドウ作成
function mkMemberPhoto(){
<c:if test="${fn:length(FriendUpdateInfo)>0}">
	var w = new MemberPhotoWindow(
		baseStyle,
		"ﾒﾝﾊﾞｰﾌｫﾄ",
		"/frontier/pc/friendupdate",
		getMemberPhotoBody(),
		"${defMemberUpdateViewCnt}"
	);
	w.mkhtml();
</c:if>
}
// F Shoutウィンドウ作成
function mkFShout(){
	var w = new FShoutWindow(
		baseStyle,
		"F Shout",
		"/frontier/pc/friendupdate",
		getFShoutBody(),
		"${resultscnt>0?defFShoutViewCnt:0}",
		"${twitterIPass}",
		"${twitterFlgCheck}",
		"${resultscnt}",
		"${(pubFshout)}",
		"${(fsTarget)}",
		"${useTwitterFlg}"
	);
	w.mkhtml();
}

// ----------------------------------------------------------------------- //
// ▼▼▼▼▼▼▼  内部コンテンツ取得用ファンクション  ▼▼▼▼▼▼▼
// ----------------------------------------------------------------------- //
// イメージHTMLの取得
function getImageBody(){
	var rtnval = "";

	rtnval += "<div class=\"listBoxBody\">";
	rtnval += "<div id=\"listBoxImage\">";
<c:choose>
<c:when test="${photoData.photo!=null}">
	rtnval += "<img src=\"${f:h(appDefDto.FP_CMN_CONTENTS_ROOT)}${fn:replace(photoData.photo,'dir','pic180')}\" alt=\"${f:h(EscapeNM)}\"/>";
</c:when>
<c:otherwise>
	rtnval += "<img src=\"/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg180.gif\" alt=\"${f:h(EscapeNM)}\"/>";
</c:otherwise>
</c:choose>
	rtnval += "</div>";
	rtnval += "<div align=\"center\">${f:h(EscapeNM)}さん</div>";
	rtnval += "<div style=\"border-style:dotted;border-width:1px 0 0 0;text-align:left;margin:5px 5px 5px 5px;\">";
	rtnval += "<div style=\"width:100%;\"><a href=\"/frontier/pc/profile2\">プロフィール管理</a></div>";
	rtnval += "<div style=\"width:100%;\"><a href=\"/frontier/pc/check/${userInfoDto.memberId}\">プロフィールを見る</a></div>";
	rtnval += "</div>";
	rtnval += "</div>";

	return rtnval;
}

// カレンダーHTMLの取得
function getCalendarBody(){
	var rtnval = "";

	rtnval += "<div class=\"listBoxBody\">";
	rtnval += "<div id=\"listBoxSchedule\">";
	rtnval += "<div class=\"ScheduleHead\" id=\"scheduleBody\">";
<c:if test="${fn:length(scheduleList)>0}">
	rtnval += "<c:set var="scheduledate" value=""/>";
<c:forEach items="${scheduleList}" var="e" varStatus="status">
<c:if test="${scheduledate ne e.scheduledate}">
	rtnval += "<c:set var="scheduledate" value="${e.scheduledate}"/>";
	rtnval += "<span style=\"font-weight:bold;\"><fmt:formatDate value="${f:date(f:h(e.scheduledate),'yyyyMMdd')}" pattern="yyyy年M月d日(E)の予定" /></span>";
</c:if>
	rtnval += "<div>";
<c:choose>
<c:when test="${e.entry eq 1}">
	rtnval += "<img src=\"/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/ico05-001.gif\"/>&nbsp;<a href=\"/frontier/pc/com/event/view/${e.cid}/${e.bbsid}\" title=\"(${f:h(e.nickname)})&#13;${f:h(e.detailjs)}\" style=\"color:#${e.titlecolor};\">";
<c:choose>
<c:when test="${e.starttime == null and e.endtime == null}">
</c:when>
<c:when test="${e.starttime == null and e.endtime != null}">
	rtnval += "～${e.endtime}&nbsp;";
</c:when>
<c:otherwise>
	rtnval += "${e.starttime}${e.endtime}&nbsp;";
</c:otherwise>
</c:choose>
	rtnval += "${f:h(e.title)}</a>";
</c:when>
<c:when test="${e.entry eq 2}">
	rtnval += "<img src=\"/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/calendar2.png\"/>&nbsp;<a title=\"(${f:h(e.nickname)})&#13;${f:h(e.detailjs)}\" style=\"color:#${e.titlecolor};\" href=\"javascript:void(0)\" onClick=\"MM_openBrWindow('/frontier/pc/calendar/view/${e.sno}/${e.cid}/1','','width=760,height=640,toolbar=no,scrollbars=yes,left=10,top=10')\">";
<c:choose>
<c:when test="${e.starttime == null and e.endtime == null}">
</c:when>
<c:when test="${e.starttime == null and e.endtime != null}">
	rtnval += "～${e.endtime}&nbsp;";
</c:when>
<c:otherwise>
	rtnval += "${e.starttime}${e.endtime}&nbsp;";
</c:otherwise>
</c:choose>
	rtnval += "${f:h(e.title)}</a>";
</c:when>
</c:choose>
	rtnval += "</div>";
</c:forEach>
</c:if>
	rtnval += "</div>";
	rtnval += "<div class=\"ScheduleBody clearfix\">";
	rtnval += "<div class=\"calendar\"><div>";
	rtnval += "<table>";
	rtnval += "<caption class=\"clearfix\">";
	rtnval += "<span class=\"previousMonth\">";
	rtnval += "<u onclick=\"javascript:setMonth(document.getElementById('month'),-1,'loadBeforeMonth');\">&lt;</u>";
	rtnval += "</span>";
	rtnval += "<span class=\"calMonth\" id=\"month\">";
	rtnval += "<fmt:formatDate value="${f:date(f:h(yyyymmdd),'yyyyMMdd')}" pattern="yyyy年MM月" />";
	rtnval += "</span>";
	rtnval += "<span class=\"nextMonth\">";
	rtnval += "<u onclick=\"javascript:setMonth(document.getElementById('month'),1,'loadNextMonth');\">&gt;</u>";
	rtnval += "</span>";
	rtnval += "</caption>";
	rtnval += "<tbody id=\"calendarBody\">";
	rtnval += "<tr>";
	rtnval += "<th abbr=\"日曜日\" class=\"sun\">日</th>";
	rtnval += "<th abbr=\"月曜日\" class=\"mon\">月</th>";
	rtnval += "<th abbr=\"火曜日\" class=\"tue\">火</th>";
	rtnval += "<th abbr=\"水曜日\" class=\"wed\">水</th>";
	rtnval += "<th abbr=\"木曜日\" class=\"thu\">木</th>";
	rtnval += "<th abbr=\"金曜日\" class=\"fri\">金</th>";
	rtnval += "<th abbr=\"土曜日\" class=\"sat\">土</th>";
	rtnval += "</tr>";
	rtnval += "<tr>";
<c:forEach var="fmonth" items="${cal}" varStatus="status">
<c:choose>
<c:when test="${fmonth['before']}">
	rtnval += "<td class=\"before\"><span>";

	<c:choose>
	<c:when test="${fmonth['existSchedule'] eq 1}">
	<c:set var="titlebase" value=""/>

	<c:forEach var="s" items="${fmonth['schedule']}" varStatus="s1">
	<c:set var="Title" value="&nbsp;・${(s['starttime'] == null and s['endtime'] != null)?'～':s['starttime']}${s['endtime']}&nbsp;${f:h(s['title'])}"/>
	<c:if test="${!s1.last}">
	<c:set var="Title" value="${Title}&#13;&#10;"/>
	</c:if>
	<c:set var="titlebase" value="${titlebase}${Title}"/>
	</c:forEach>

		rtnval += "<a href=\"/frontier/pc/calendar/${yyyymmdd}\" title=\"${titlebase}\">${f:h(fmonth['day']) }</a>";
	</c:when>
	<c:otherwise>
		rtnval += "${f:h(fmonth['day']) }";
	</c:otherwise>
	</c:choose>

	rtnval += "</span>";
	rtnval += "</td>";

</c:when>
<c:when test="${fmonth['now']}">
	rtnval += "<td class=\"${fmonth['today'] eq 1?'today':fmonth['week'] eq 1?'sun':fmonth['week'] eq 7?'sat':'mini'}\">";
	rtnval += "<span>";
<c:choose>
<c:when test="${fmonth['existSchedule'] eq 1}">
<c:set var="titlebase" value=""/>

<c:forEach var="s" items="${fmonth['schedule']}" varStatus="s1">
<c:set var="Title" value="&nbsp;・${(s['starttime'] == null and s['endtime'] != null)?'～':s['starttime']}${s['endtime']}&nbsp;${f:h(s['title'])}"/>
<c:if test="${!s1.last}">
<c:set var="Title" value="${Title}&#13;&#10;"/>
</c:if>
<c:set var="titlebase" value="${titlebase}${Title}"/>
</c:forEach>

	rtnval += "<a href=\"/frontier/pc/calendar/${yyyymmdd}\" title=\"${titlebase}\">${f:h(fmonth['day']) }</a>";
</c:when>
<c:otherwise>
	rtnval += "${f:h(fmonth['day']) }";
</c:otherwise>
</c:choose>
	rtnval += "</span>";
	rtnval += "</td>";
<c:if test="${fmonth['week']=='7'}">
	rtnval += "</tr><tr>";
</c:if>
</c:when>
<c:when test="${fmonth['next']}">
	rtnval += "<td class=\"next\">";
	rtnval += "<span>";

	<c:choose>
	<c:when test="${fmonth['existSchedule'] eq 1}">
	<c:set var="titlebase" value=""/>

	<c:forEach var="s" items="${fmonth['schedule']}" varStatus="s1">
	<c:set var="Title" value="&nbsp;・${(s['starttime'] == null and s['endtime'] != null)?'～':s['starttime']}${s['endtime']}&nbsp;${f:h(s['title'])}"/>
	<c:if test="${!s1.last}">
	<c:set var="Title" value="${Title}&#13;&#10;"/>
	</c:if>
	<c:set var="titlebase" value="${titlebase}${Title}"/>
	</c:forEach>

		rtnval += "<a href=\"/frontier/pc/calendar/${yyyymmdd}\" title=\"${titlebase}\">${f:h(fmonth['day']) }</a>";
	</c:when>
	<c:otherwise>
		rtnval += "${f:h(fmonth['day']) }";
	</c:otherwise>
	</c:choose>

	rtnval +="</span>";
	rtnval +="</td>";
</c:when>
</c:choose>
</c:forEach>
	rtnval += "</tr>";
	rtnval += "</tbody>";
	rtnval += "</table>";
	rtnval += "</div></div>";
	rtnval += "</div>";
	rtnval += "<div class=\"ScheduleBottom clearfix\">";
	rtnval += "<div class=\"chk01\"><div style=\"position:absolute;white-space:nowrap;\">";
<c:choose>
<c:when test="${defDisptypeCalendar eq '0'}">
	rtnval += "<input type=\"checkbox\" onClick=\"changeSchedule(this);\"/>共有中のスケジュールのみ表示";
</c:when>
<c:otherwise>
	rtnval += "<input type=\"checkbox\" onClick=\"changeSchedule(this);\" checked>共有中のスケジュールのみ表示</input>";
</c:otherwise>
</c:choose>
	rtnval += "</div></div>";
	rtnval += "<div class=\"ScheduleBottomArea\">";
	rtnval += "<img src=\"/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/calendar2.png\" alt=\"\" />";
	rtnval += "<a href=\"javascript:void(0)\" onclick=\"MM_openBrWindow('/frontier/pc/calendar/entryTop', '', 'width=760,height=640,toolbar=no,scrollbars=yes,left=10,top=10')\">";
	rtnval += "予定を入力する</a>";
	rtnval += "</div>";
	rtnval += "</div>";
	rtnval += "</div>";
	rtnval += "</div>";

	return rtnval;
}

//ﾏｲ更新情報HTMLの取得
function getMyUpdateBody(){
	var rtnval ="";

	rtnval += "<div class=\"listBoxBody\" id=\"MyUpdateBody\">";
<c:if test="${defMyUpdateViewCnt>0}">
	rtnval += "<div id=\"listBoxMyUpdate\">";
<c:forEach var="e" items="${MyUpdateInfo}" varStatus="i">
	rtnval += "<div>";
<c:choose>
<c:when test="${e.type eq '1'}">
	rtnval += "<img src=\"/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/camera-deji1-silver.gif\"/><a href=\"/frontier/pc/photo/view/${e.mid}/${e.ano}\" title=\"${f:h(e.LTitle)}\">${f:h(e.title)}";
	<c:if test="${e.comments!=null}">
	rtnval += "(${e.comments})";
	</c:if>
	rtnval += "</a>";
</c:when>
<c:otherwise>
	rtnval += "<img src=\"/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/book_blue.gif\"/><a href=\"/frontier/pc/diary/view/${f:h(e.diaryid)}/${fn:substring(f:h(e.ent),0,8)}/${f:h(userInfoDto.memberId)}\" title=\"${f:h(e.LTitle)}\">${f:h(e.title) }";
	<c:if test="${e.comments>0}">
	rtnval +="(${e.comments})";
	</c:if>
	rtnval +="</a>";
</c:otherwise>
</c:choose>
	rtnval += "</div>";
</c:forEach>
	rtnval += "</div>";
</c:if>
	rtnval += "</div>";

	return rtnval;
}

//ｺﾐｭﾆﾃｨ一覧HTMLの取得
function getCommunityListBody(){
	rtnval ="";

	rtnval +="<div class=\"listBoxBody\" id=\"communityListBody\">";
<c:if test="${defCommunityViewCnt>0}">
	rtnval +="<div id=\"listBoxCommunity\">";
	rtnval +="<table>";
<c:set var="row" value="3"/>
<c:forEach var="e" items="${results}" varStatus="i">
<c:if test="${i.count%row==1}">
	rtnval +="<tr>";
</c:if>
	rtnval +="<td align=\"center\">";

<c:choose>
<c:when test="${f:h(e.pic) != ''}">
	rtnval +="<a href=\"/frontier/pc/com/top/${f:h(e.cid)}\" title=\"${f:h(e.title)}(${f:h(e.memcnt)})\"><img src=\"${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(e.pic,'dir','pic60')}\" alt=\"${f:h(e.title)}(${f:h(e.memcnt)})\"/></a>";
</c:when>
<c:otherwise>
	rtnval +="<a href=\"/frontier/pc/com/top/${f:h(e.cid)}\" title=\"${f:h(e.title)}(${f:h(e.memcnt)})\"><img src=\"/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg60.gif\" alt=\"${f:h(e.title)}(${f:h(e.memcnt)})\"/></a>";
</c:otherwise>
</c:choose>
	rtnval +="</td>";
<c:choose>
<c:when test="${i.count==communityCnt}">
<c:if test="${i.count%row!=0}">
<c:forEach begin="1" end="${row-i.count%row}">
	rtnval +="<td align=\"center\">";
	rtnval +="&nbsp;";
	rtnval +="</td>";
</c:forEach>
</c:if>
	rtnval +="</tr>";
</c:when>
<c:otherwise>
<c:if test="${i.count%row==0}">
	rtnval +="</tr>";
</c:if>
</c:otherwise>
</c:choose>
</c:forEach>
	rtnval +="</table>";
	rtnval +="</div>";
</c:if>
	rtnval +="</div>";
	return rtnval;
}

//ｸﾞﾙｰﾌﾟ一覧HTMLの取得
function getGroupListBody(){
	rtnval ="";
	rtnval +="<div class=\"listBoxBody\" id=\"groupListBody\">";
<c:if test="${defGroupViewCnt>0}">
	rtnval +="<div id=\"listBoxGroup\">";
	rtnval += "<table style=\"width:100%;\" border=\"0\">";
<c:forEach var="e" items="${GroupList}" varStatus="i">
	rtnval += "<tr><td style=\"width:42px;\" align=\"middle\">";
	rtnval += "<a href=\"javascript:void(0);goGroup('/frontier/pc/mlist/group/','${e.frontierdomain}','${e.gid}','${userInfoDto.visitMemberId}');\" title=\"${f:h(e.gname)}\">";
<c:choose>
<c:when test="${e.pic!=null && e.pic!=''}">
	rtnval += "<img src=\"${fn:replace(e.pic,'[dir]','pic42')}\" alt=\"${f:h(e.gname)}\"/>"
</c:when>
<c:otherwise>
	rtnval += "<img src=\"/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg42.gif\" alt=\"${f:h(e.gname)}(${e.joinnumber})\"/>"
</c:otherwise>
</c:choose>
	rtnval += "</a></td>";
	rtnval += "<td><a href=\"javascript:void(0);goGroup('/frontier/pc/mlist/group/','${e.frontierdomain}','${e.gid}','${userInfoDto.visitMemberId}');\" title=\"${f:h(e.gname)}(${e.joinnumber})\">${f:h(e.gname)}(${e.joinnumber})</a></td>";
	rtnval += "</tr>";
</c:forEach>
	rtnval +="</table>";
	rtnval +="</div>";
</c:if>
	rtnval += "</div>";
	return rtnval;
}

//私がﾌｫﾛｰ一覧HTMLの取得
function getFollowYouListBody(){
	rtnval ="";

	rtnval +="<div class=\"listBoxBody\" id=\"followyouListBody\">";
<c:if test="${defFollowyouViewCnt>0}">
	rtnval +="<div id=\"listBoxFollowyou\"><table>";
<c:set var="row" value="3"/>
<c:forEach var="e" items="${FollowyouList}" varStatus="i" begin="0" end="${defFollowyouViewCnt-1}">
<c:if test="${i.count%row==1}">
	rtnval +="<tr>";
</c:if>
	rtnval +="<td align=\"center\">";

<c:choose>
	<c:when test="${(f:h(e.membertype)) eq '0'}">
		<%--自Frontier--%>
		<c:choose>
			<c:when test="${f:h(e.pic) != ''}">
				<%--写真がある--%>
				rtnval +="<a href=\"/frontier/pc/mem/${f:h(e.mid)}\" title=\"${f:h(e.nickname)}\"><img src=\"${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(e.pic,'dir','pic60')}\" alt=\"${f:h(e.nickname)}\"/></a>";
			</c:when>
			<c:otherwise>
				<%--写真がない--%>
				rtnval +="<a href=\"/frontier/pc/mem/${f:h(e.mid)}\" title=\"${f:h(e.nickname)}\"><img src=\"/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg60.gif\" alt=\"${f:h(e.nickname)}\"/></a>";
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:when test="${(f:h(e.membertype)) eq '1'}">
		<%--他Frontier--%>
		<c:choose>
			<c:when test="${f:h(e.fpic) != ''}">
				<%--写真がある--%>
				<c:choose>
					<c:when test="${frontierUserManagement.frontierdomain eq e.frontierdomain}">
						rtnval +="<a href=\"http://${f:h(frontierUserManagement.frontierdomain)}/frontier/pc/mem/${f:h(e.fid)}/\" title=\"${f:h(e.lfnickname)}\"><img src=\"${fn:replace(e.fpic,'dir','pic60')}\" alt=\"${f:h(e.lfnickname)}\"/></a>";
					</c:when>
					<c:otherwise>
						rtnval +="<a href=\"http://${f:h(e.frontierdomain)}/frontier/pc/openid/auth?cid=${f:h(e.fid)}&gm=mv&openid=${f:h(frontierUserManagement.frontierdomain)}/frontier/pc/openidserver\" title=\"${f:h(e.lfnickname)}\"><img src=\"${fn:replace(e.fpic,'dir','pic60')}\" alt=\"${f:h(e.lfnickname)}\"/></a>";
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise>
				<%--写真がない--%>
				<c:choose>
					<c:when test="${frontierUserManagement.frontierdomain eq e.frontierdomain}">
						rtnval +="<a href=\"http://${f:h(frontierUserManagement.frontierdomain)}/frontier/pc/mem/${f:h(e.fid)}/\" title=\"${f:h(e.lfnickname)}\"><img src=\"/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg60.gif\" alt=\"${f:h(e.lfnickname)}\"/></a>";
					</c:when>
					<c:otherwise>
						rtnval +="<a href=\"http://${f:h(e.frontierdomain)}/frontier/pc/openid/auth?cid=${f:h(e.fid)}&gm=mv&openid=${f:h(frontierUserManagement.frontierdomain)}/frontier/pc/openidserver\" title=\"${f:h(e.lfnickname)}\"><img src=\"/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg60.gif\" alt=\"${f:h(e.lfnickname)}\"/></a>";
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
	</c:when>
</c:choose>

	rtnval +="</td>";
<c:choose>
<c:when test="${i.count==followyouCnt}">
<c:if test="${i.count%row!=0}">
<c:forEach begin="1" end="${row-i.count%row}">
	rtnval +="<td align=\"center\">&nbsp;</td>";
</c:forEach>
</c:if>
	rtnval +="</tr>";
</c:when>
<c:otherwise>
<c:if test="${i.count%row==0}">
	rtnval +="</tr>";
</c:if>
</c:otherwise>
</c:choose>
</c:forEach>
	rtnval +="</table></div>";
</c:if>
	rtnval +="</div>";

	return rtnval;
}

//私をﾌｫﾛｰ一覧HTMLの取得
function getFollowMeListBody(){
	rtnval ="";

	rtnval +="<div class=\"listBoxBody\" id=\"followmeListBody\">";
<c:if test="${defFollowmeViewCnt>0}">
	rtnval +="<div id=\"listBoxFollowme\"><table>";
<c:set var="row" value="3"/>
<c:forEach var="e" items="${FollowmeList}" varStatus="i">
<c:if test="${i.count%row==1}">
	rtnval +="<tr>";
</c:if>
	rtnval +="<td align=\"center\">";

<c:choose>
	<c:when test="${(f:h(e.membertype)) eq '0'}">
		<%--自Frontier--%>
		<c:choose>
			<c:when test="${f:h(e.pic) != ''}">
				<%--写真がある--%>
				rtnval +="<a href=\"/frontier/pc/mem/${f:h(e.mid)}\" title=\"${f:h(e.nickname)}\"><img src=\"${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(e.pic,'dir','pic60')}\" alt=\"${f:h(e.nickname)}\"/></a>";
			</c:when>
			<c:otherwise>
				<%--写真がない--%>
				rtnval +="<a href=\"/frontier/pc/mem/${f:h(e.mid)}\" title=\"${f:h(e.nickname)}\"><img src=\"/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg60.gif\" alt=\"${f:h(e.nickname)}\"/></a>";
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:when test="${(f:h(e.membertype)) eq '1'}">
		<%--他Frontier--%>
		<c:choose>
			<c:when test="${f:h(e.fpic) != ''}">
				<%--写真がある--%>
				<c:choose>
					<c:when test="${frontierUserManagement.frontierdomain eq e.frontierdomain}">
						rtnval +="<a href=\"http://${f:h(frontierUserManagement.frontierdomain)}/frontier/pc/mem/${f:h(e.fid)}/\" title=\"${f:h(e.lfnickname)}\"><img src=\"${fn:replace(e.fpic,'dir','pic60')}\" alt=\"${f:h(e.lfnickname)}\"/></a>";
					</c:when>
					<c:otherwise>
						rtnval +="<a href=\"http://${f:h(e.frontierdomain)}/frontier/pc/openid/auth?cid=${f:h(e.fid)}&gm=mv&openid=${f:h(frontierUserManagement.frontierdomain)}/frontier/pc/openidserver\" title=\"${f:h(e.lfnickname)}\"><img src=\"${fn:replace(e.fpic,'dir','pic60')}\" alt=\"${f:h(e.lfnickname)}\"/></a>";
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise>
				<%--写真がない--%>
				<c:choose>
					<c:when test="${frontierUserManagement.frontierdomain eq e.frontierdomain}">
						rtnval +="<a href=\"http://${f:h(frontierUserManagement.frontierdomain)}/frontier/pc/mem/${f:h(e.fid)}/\" title=\"${f:h(e.lfnickname)}\"><img src=\"/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg60.gif\" alt=\"${f:h(e.lfnickname)}\"/></a>";
					</c:when>
					<c:otherwise>
						rtnval +="<a href=\"http://${f:h(e.frontierdomain)}/frontier/pc/openid/auth?cid=${f:h(e.fid)}&gm=mv&openid=${f:h(frontierUserManagement.frontierdomain)}/frontier/pc/openidserver\" title=\"${f:h(e.lfnickname)}\"><img src=\"/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg60.gif\" alt=\"${f:h(e.lfnickname)}\"/></a>";
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
	</c:when>
</c:choose>

	rtnval +="</td>";
<c:choose>
<c:when test="${i.count==followmeCnt}">
<c:if test="${i.count%row!=0}">
<c:forEach begin="1" end="${row-i.count%row}">
	rtnval +="<td align=\"center\">&nbsp;</td>";
</c:forEach>
</c:if>
	rtnval +="</tr>";
</c:when>
<c:otherwise>
<c:if test="${i.count%row==0}">
	rtnval +="</tr>";
</c:if>
</c:otherwise>
</c:choose>
</c:forEach>
	rtnval +="</table></div>";
</c:if>
	rtnval +="</div>";

	return rtnval;
}

//ﾒﾝﾊﾞｰﾌｫﾄHTMLの取得
function getMemberPhotoBody(){
	rtnval ="";


	rtnval += "<div class=\"listBoxBody\" id=\"memberPhotoBody\">";
<c:if test="${fn:length(FriendUpdateInfo)>0&&defMemberUpdateViewCnt>0}">
	rtnval += "<table border=\"0\">";
	rtnval += "<tr class=\"contCategory\">";
	rtnval += "<th style=\"width:10%;\">フォト</th>";
	rtnval += "<th style=\"width:10%;\">";
<c:choose><c:when test="${defMemberUpdateSort=='01'}">
	rtnval += "<u>更新日</u><font color=\"red\"><span>▲</span></font>";
</c:when><c:otherwise>
	rtnval += "<u class=\"link\" onclick=\"j$('#memberPhotoBody').load('/frontier/pc/ajax/top/loadData/${defMemberUpdateViewCnt}/memberUpdateSort/01?'+rand(10));\">更新日</u>";
</c:otherwise></c:choose>
	rtnval += "</th>";
	rtnval += "<th style=\"width:10%;\">";
<c:choose><c:when test="${defMemberUpdateSort=='02'}">
	rtnval += "<u>登録日</u><font color=\"red\"><span>▲</span></font>";
</c:when><c:otherwise>
	rtnval += "<u class=\"link\" onclick=\"j$('#memberPhotoBody').load('/frontier/pc/ajax/top/loadData/${defMemberUpdateViewCnt}/memberUpdateSort/02?'+rand(10));\">登録日</u>";
</c:otherwise></c:choose>
	rtnval += "</th>";
	rtnval += "<th style=\"width:70%;\">ﾀｲﾄﾙ&nbsp;/&nbsp;ﾆｯｸﾈｰﾑ</th>";
	rtnval += "</tr>";
<c:forEach var="e" items="${FriendUpdateInfo}" varStatus="i">
	rtnval += "<tr>";
	rtnval += "<td align=\"right\">";
<c:choose>
<c:when test="${e.pic!=null}">
	rtnval += "<img src=\"${f:h(appDefDto.FP_CMN_CONTENTS_ROOT)}${fn:replace(e.pic,'dir','pic42')}\" alt=\"${f:h(e.title)}\" />";
</c:when>
<c:otherwise>
	rtnval += "<img src=\"/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg42.gif\" style=\"width:42px; height:42px; margin-left:auto; margin-right:auto;\" alt=\"${f:h(e.title)}\"/>";
</c:otherwise>
</c:choose>
	rtnval += "</td>";
	rtnval += "<td align=\"right\">${e.upddate2}</td>";
	rtnval += "<td align=\"right\">${e.entdate2}</td>";
	rtnval += "<td><a href=\"/frontier/pc/photo/view/${e.mid}/${e.ano}\" title=\"${f:h(e.LTitle)}\">${f:h(e.title)}&nbsp;(${e.comments})</a>&nbsp;(<a href=\"/frontier/pc/mem/${f:h(e.mid)}\" title=\"${f:h(e.nickname)}\">${f:h(e.nickname)}</a>)</td>";
	rtnval += "</tr>";
</c:forEach>
	rtnval += "</table>";

</c:if>
	rtnval += "</div>";

	return rtnval;
}

//ﾒﾝﾊﾞｰ日記HTMLの取得
function getMemberDiaryBody(){
	rtnval ="";

	rtnval +="<div class=\"listBoxBody\" id=\"diaryBody\">";
	<c:if test="${fn:length(fdiaryNewList)>0&&defMemDiaryViewCnt>0}">
	rtnval +="<table border=\"0\">";
	rtnval +="<tr class=\"contCategory\">";
	rtnval +="<th style=\"width:10%;\">";
	<c:choose>
		<c:when test="${defMemDiarySort=='01'}">
			rtnval +="<u>更新日</u><font color=\"red\"><span>▲</span></font>";
		</c:when>
		<c:otherwise>
			rtnval +="<u class=\"link\" onclick=\"j$('#diaryBody').load('/frontier/pc/ajax/top/loadData/${defMemDiaryViewCnt}/diaryUpdateSort/01?'+rand(10));\">更新日</u>";
		</c:otherwise>
	</c:choose>
	rtnval +="</th>";
	rtnval +="<th style=\"width:10%;\">";
	<c:choose>
		<c:when test="${defMemDiarySort=='02'}">
			rtnval +="<u>登録日</u><font color=\"red\"><span>▲</span></font>";
		</c:when>
		<c:otherwise>
			rtnval +="<u class=\"link\" onclick=\"j$('#diaryBody').load('/frontier/pc/ajax/top/loadData/${defMemDiaryViewCnt}/diaryUpdateSort/02?'+rand(10));\">登録日</u>";
		</c:otherwise>
		</c:choose>
	rtnval +="</th>";
	rtnval +="<th style=\"width:80%;\">ﾀｲﾄﾙ&nbsp;/&nbsp;ﾆｯｸﾈｰﾑ</th>";
	rtnval +="</tr>";
	<c:forEach var="e" items="${fdiaryNewList}" varStatus="status">
	rtnval +="<tr>";
	rtnval +="<td align=\"right\">${e.entdatesla}</td>";
	rtnval +="<td align=\"right\">${e.entdate2sla}</td>";
	rtnval +="<td><a href=\"/frontier/pc/diary/view/${e.diaryid}/${e.yyyymmdd}/${e.mid}\" title=\"${f:h(e.LTitle)}\">${f:h(e.title)}&nbsp;(${e.count})</a>&nbsp;(<a href=\"/frontier/pc/mem/${f:h(e.mid)}\" title=\"${f:h(e.nickname)}\">${f:h(e.nickname)}</a>)</td>";
	rtnval +="</tr>";
	</c:forEach>
	rtnval +="</table>";
	</c:if>
	rtnval +="</div>";

	return rtnval;
}

// ｺﾐｭﾆﾃｨHTMLの取得
function getBbsBody(){
	rtnval ="";

	rtnval +="<div class=\"listBoxBody\" id=\"newBbsBody\">";
<c:if test="${fn:length(communityNewList)>0&&defCommunityBbsViewCnt>0}">
	rtnval +="<table border=\"0\">";
	rtnval +="<tr class=\"contCategory\">";
	rtnval +="<th style=\"width:5%;\"></th>";
	rtnval +="<th style=\"width:10%;\">";
<c:choose>
	<c:when test="${defCommunitySort=='01'}">
	rtnval +="<u>更新日</u><font color=\"red\"><span>▲</span></font>";
	</c:when>
	<c:otherwise>
		rtnval +="<u class=\"link\" onclick=\"j$('#newBbsBody').load('/frontier/pc/ajax/top/loadData/${defCommunityBbsViewCnt}/communityUpdateSort/01?'+rand(10));\">更新日</u>";
	</c:otherwise>
</c:choose>
	rtnval +="</th>";
	rtnval +="<th style=\"width:10%;\">";
	<c:choose>
	<c:when test="${defCommunitySort=='02'}">
		rtnval +="<u>登録日</u><font color=\"red\"><span>▲</span></font>";
	</c:when>
	<c:otherwise>
		rtnval +="<u class=\"link\" onclick=\"j$('#newBbsBody').load('/frontier/pc/ajax/top/loadData/${defCommunityBbsViewCnt}/communityUpdateSort/02?'+rand(10));\">登録日</u>";
	</c:otherwise>
	</c:choose>
		rtnval +="</th>";
	rtnval +="<th style=\"width:75%;\">ﾀｲﾄﾙ&nbsp;/&nbsp;ｺﾐｭﾆﾃｨ名</th>";
	rtnval +="</tr>";
<c:forEach var="e" items="${communityNewList}" varStatus="status">
	rtnval +="<tr>";
	rtnval +="<td class=\"${e.entrytype eq 1?'topic':'event'}\"></td>";
	rtnval +="<td align=\"right\">${e.updatedate2}</td>";
	rtnval +="<td align=\"right\">${e.entdate2}</td>";
	rtnval +="<td><a href=\"/frontier/pc/com/${e.entrytype eq 1?'topic':'event'}/view/${f:h(e.cmid)}/${e.bbsid}\">${f:h(e.title)}&nbsp;(${e.comments})</a>&nbsp;(<a href=\"/frontier/pc/com/top/${f:h(e.cmid)}\" title=\"${f:h(e.community)}\">${f:h(e.community)}</a>)</td>";
	rtnval +="</tr>";
</c:forEach>
	rtnval +="</table>";

</c:if>
	rtnval +="</div>";
	return rtnval;
}


//FShoutHTMLの取得
function getFShoutBody(){
	rtnval ="";

	rtnval += "<div <c:if test="${fn:length(fsNewList)>0}">class=\"listBoxBody\"</c:if> id=\"FShoutBody\" style=\"border-width:0;\">";

	<c:choose>
	<c:when test="${fsConfirmMe>0}">
	rtnval += "<div id=\"confirmMeArea\">";
	rtnval += "<div style=\"width:80%; background-color:#ffffcc; text-align:center; margin-left:auto; margin-right:auto; border-style:solid; border-width:1px; border-color:#ff0000; padding-top:5px; padding-bottom:5px;\">";
	rtnval += "<span style=\"color:#ff0000; font-weight:bold; text-decoration:underline; cursor:pointer;\" onclick=\"cngFsList(${appDefDto.FP_MY_FSHOUTLIST_PGMAX},'fsListToMe');\">あなた宛の投稿があります</span>";
	rtnval += "</div>";
	rtnval += "</div>";
	</c:when>
	<c:otherwise>
	rtnval += "<div id=\"confirmMeArea\" style=\"margin:0; padding:0;\">";
	rtnval += "</div>";
	</c:otherwise>
	</c:choose>

	//最新コメント表示エリア
	rtnval += "<div id=\"fsdivNew\"></div>";

	rtnval += "<div id=\"fsdivTop\">";
	<c:if test="${fn:length(fsNewList)>0}">
	<c:forEach var="e" items="${fsNewList}" varStatus="i">
	rtnval += "<div class=\"fshoutAreaHead\" id=\"delview\">";
	rtnval += "<div class=\"fshoutAreaTop fshoutAreaTopLine\">";
rtnval += "<div class=\"bodycontentsArea bodycontentsAreaTop\">";
rtnval += "<table style=\"width:100%; <c:if test="${(e.confirmyou == '1')}">background-color:#ccffcc;</c:if> <c:if test="${(e.confirmme == '1')}">background-color:#ffcccc;</c:if>\">";
rtnval += "<tr>";
rtnval += "<td class=\"leftArea leftAreaTop\">";
rtnval += "<ul>";
rtnval += "<li class=\"leftNickname\">";

<c:choose>
	<c:when test="${(f:h(e.membertype)) eq '0'}">
		<%--自Frontier--%>
		<c:choose>
			<c:when test="${f:h(e.photo) != ''}">
				<%--写真がある--%>
				rtnval +="<a href=\"/frontier/pc/fshout/list/${f:h(e.mid)}/1\" title=\"${f:h(e.nickname)}\"><img src=\"${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(e.photo,'dir','pic42')}\" alt=\"${f:h(e.nickname)}\" /></a>";
			</c:when>
			<c:otherwise>
				<%--写真がない--%>
				rtnval +="<a href=\"/frontier/pc/fshout/list/${f:h(e.mid)}/1\" title=\"${f:h(e.nickname)}\"><img src=\"/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg42.gif\"  alt=\"${f:h(e.nickname)}\" /></a>";
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:when test="${(f:h(e.membertype)) eq '1'}">
		<%--他Frontier--%>
		<c:choose>
			<c:when test="${f:h(e.fpic) != ''}">
				<%--写真がある--%>
				<c:choose>
					<c:when test="${frontierUserManagement.frontierdomain eq e.frontierdomain}">
						rtnval +="<a href=\"http://${f:h(frontierUserManagement.frontierdomain)}/frontier/pc/fshout/list/${f:h(e.fid)}/1\" title=\"${f:h(e.fnickname)}\"><img src=\"${fn:replace(e.fpic,'dir','pic42')}\" alt=\"${f:h(e.fnickname)}\" /></a>";
					</c:when>
					<c:otherwise>
						rtnval +="<a href=\"http://${f:h(e.frontierdomain)}/frontier/pc/openid/auth?cid=${f:h(e.fid)}&gm=mv&openid=${f:h(frontierUserManagement.frontierdomain)}/frontier/pc/openidserver\" title=\"${f:h(e.fnickname)}\"><img src=\"${fn:replace(e.fpic,'dir','pic42')}\" alt=\"${f:h(e.fnickname)}\" /></a>";
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise>
				<%--写真がない--%>
				<c:choose>
					<c:when test="${frontierUserManagement.frontierdomain eq e.frontierdomain}">
						rtnval +="<a href=\"http://${f:h(frontierUserManagement.frontierdomain)}/frontier/pc/fshout/list/${f:h(e.fid)}/1\" title=\"${f:h(e.fnickname)}\"><img src=\"/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg42.gif\"  alt=\"${f:h(e.fnickname)}\" /></a>";
					</c:when>
					<c:otherwise>
						rtnval +="<a href=\"http://${f:h(e.frontierdomain)}/frontier/pc/openid/auth?cid=${f:h(e.fid)}&gm=mv&openid=${f:h(frontierUserManagement.frontierdomain)}/frontier/pc/openidserver\" title=\"${f:h(e.fnickname)}\"><img src=\"/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg42.gif\"  alt=\"${f:h(e.fnickname)}\" /></a>";
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
	</c:when>
</c:choose>

rtnval += "<c:if test="${e.twitter == '1'}"><div style=\"margin-top:5px; text-align:left;\"><img src=\"/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/twitter.gif\" title=\"Twitter\" alt=\"Twitter\"/ style=\"display:inline;\"></div></c:if>";
rtnval += "</li>";
rtnval += "</ul>";
rtnval += "</td>";
rtnval += "<td class=\"rightArea rightAreaTop\">";
rtnval += "<div class=\"bodyFSArea\">";
rtnval += "<ul>";
rtnval += "<li>";
rtnval += "<table class=\"nmArea\"><tr>";

<c:choose>
	<c:when test="${(f:h(e.membertype)) eq '0'}">
		<%--自Frontier--%>
		rtnval += "<td class=\"nmspace\"><span class=\"fsSentences\"><a href=\"/frontier/pc/fshout/list/${f:h(e.mid)}/1\">${f:h(e.nickname)}</a></span></td>";
	</c:when>
	<c:when test="${(f:h(e.membertype)) eq '1'}">
		<%--他Frontier--%>
		<c:choose>
			<c:when test="${frontierUserManagement.frontierdomain eq e.frontierdomain}">
				rtnval += "<td class=\"nmspace\"><span class=\"fsSentences\"><a href=\"http://${f:h(frontierUserManagement.frontierdomain)}/frontier/pc/fshout/list/${f:h(e.fid)}/1\" title=\"${f:h(e.fnickname)}\">${f:h(e.fnickname)}</a></span></td>";
			</c:when>
			<c:otherwise>
				rtnval += "<td class=\"nmspace\"><span class=\"fsSentences\"><a href=\"http://${f:h(e.frontierdomain)}/frontier/pc/openid/auth?cid=${f:h(e.fid)}&gm=mv&openid=${f:h(frontierUserManagement.frontierdomain)}/frontier/pc/openidserver\" title=\"${f:h(e.fnickname)}\">${f:h(e.fnickname)}</a></span></td>";
			</c:otherwise>
		</c:choose>
	</c:when>
</c:choose>

rtnval += "<td class=\"markspace\"><c:if test="${(e.confirmme == '1')}"><img src=\"/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/icon_warning.png\" title=\"内容確認\" alt=\"内容確認\" style=\"cursor:pointer;\" onclick=\"chkConfirm('${e.mid}',${e.no},${appDefDto.FP_MY_FSHOUTLIST_PGMAX},this,'${appDefDto.FP_CMN_HOST_NAME}','${appDefDto.FP_CMN_COLOR_TYPE}')\" /></c:if><c:if test="${(e.confirmyou == '2' || e.confirmme == '2')}"><img src=\"/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/arrow_tick.png\" title=\"確認済み\" alt=\"確認済み\"/></c:if><td>";
rtnval += "</tr></table>";
rtnval += "</li>";
rtnval += "<li>";
rtnval += "<span class=\"fsSentences\">${e.viewComment}</span>";
rtnval += "</li>";
rtnval += "<li>";
rtnval += "<span class=\"date\">${f:h(e.entdate)}</span>";

<c:choose>
	<c:when test="${(f:h(e.membertype)) eq '0'}">
		<%--自Frontier--%>
		rtnval += "<span class=\"dateArea\"><img src=\"/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/link_s.png\" alt=\"RT\" title=\"RT\" onclick=\"onSetFocus('rt','${f:h(e.nickname)}','${e.viewCommentAlt}',${e.substrmid},'${e.frontierdomain}','${e.mid}:${e.no}');\" style=\"cursor:pointer;\" /></span>";
		rtnval += "<span class=\"dateArea\"><c:choose><c:when test="${e.mid == userInfoDto.memberId}"><img src=\"/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/ico_ashcan1_9.gif\" onclick=\"fncConfirm('${e.mid}',${e.no},fsCnt,this);\" title=\"削除\" alt=\"削除\" style=\"cursor:pointer;\"/></c:when><c:otherwise><img src=\"/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/arrow_redo_s.png\" onclick=\"onSetFocus('redo','${e.nickname}','${e.viewCommentRe}',${e.substrmid},'${e.frontierdomain}','${e.mid}:${e.no}');\" title=\"返信\" alt=\"返信\" style=\"cursor:pointer;\"/></c:otherwise></c:choose></span>";
	</c:when>
	<c:when test="${(f:h(e.membertype)) eq '1'}">
		<%--他Frontier--%>
		rtnval += "<span class=\"dateArea\"><img src=\"/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/link_s.png\" alt=\"RT\" title=\"RT\" onclick=\"onSetFocus('rt','${f:h(e.fnickname)}','${e.viewCommentAlt}',${e.substrfid},'${e.frontierdomain}','${e.mid}:${e.no}');\" style=\"cursor:pointer;\" /></span>";
		rtnval += "<span class=\"dateArea\"><c:choose><c:when test="${e.fid == userInfoDto.memberId}"><img src=\"/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/ico_ashcan1_9.gif\" onclick=\"fncConfirm('${e.fid}',${e.no},fsCnt,this);\" title=\"削除\" alt=\"削除\" style=\"cursor:pointer;\"/></c:when><c:otherwise><img src=\"/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/arrow_redo_s.png\" onclick=\"onSetFocus('redo','${e.nickname}','${e.viewCommentRe}',${e.substrfid},'${e.frontierdomain}','${e.mid}:${e.no}');\" title=\"返信\" alt=\"返信\" style=\"cursor:pointer;\"/></c:otherwise></c:choose></span>";
	</c:when>
</c:choose>


rtnval += "</li>";
rtnval += "<li style=\"margin-top:5px;\">";
rtnval += "<span class=\"fsSentences\">公開範囲：<span <c:if test="${(e.pubLevel)>0}">class=\"pubLeveltxt\"</c:if>>外部</span>&nbsp;/&nbsp;<span <c:if test="${(e.pubLevel)>1}">class=\"pubLeveltxt\"</c:if>>Frontier Net</span>&nbsp;/&nbsp;<span <c:if test="${(e.pubLevel)>2}">class=\"pubLeveltxt\"</c:if>>ﾏｲFrontier</span>&nbsp;/&nbsp;<span>ｸﾞﾙｰﾌﾟ</span></span>";
rtnval += "</li>";
rtnval += "</ul>";
rtnval += "</div>";
rtnval += "</td>";
rtnval += "</tr>";
rtnval += "</table>";
rtnval += "</div>";
	rtnval += "</div>";
	rtnval += "</div>";
	</c:forEach>
	</c:if>
	<c:if test="${fn:length(fsNewList)<0}">
		rtnval += "<div>まだありません。</div>";
	</c:if>
rtnval += "</div>";
//fsdivTop

	rtnval += "<div id=\"fsdivBottom\">";
	//もっと読むコメント表示エリア
	rtnval += "</div>";



rtnval += "<div id=\"morelink\" style=\"width:100%; text-align:center; margin-left:auto; margin-right:auto; border-style:none none none none; border-width:1px; padding-top:5px; padding-bottom:5px; margin-top:5px;\">";
<c:choose>
<c:when test="${fsCntResult>0}">
rtnval += "<span style=\"text-decoration:underline; color:#0000ff; cursor:pointer;\" onclick=\"moreFshout(${setFsCntResult})\">もっと読む</span>";
</c:when>
<c:otherwise>
rtnval += "<span style=\"text-decoration:underline; color:#cccccc;\">もっと読む</span>";
</c:otherwise>
</c:choose>
rtnval += "</div>";


	rtnval += "</div>";

	return rtnval;
}

// ----------------------------------------------------------------------- //
// ▲▲▲▲▲▲▲  内部コンテンツ取得用ファンクション  ▲▲▲▲▲▲▲
// ----------------------------------------------------------------------- //


// onloadアクション
function home_onload(){
	init(
		[
			[${defScheduleViewPos},        document.getElementById('schedule')],
			[${defMyUpdateViewPos},        document.getElementById('myUpdateInfo')],
			[${defMyPhotoViewPos},         document.getElementById('myPhoto')],
			[${defCommunityViewPos},       document.getElementById('communityList')],
			[${defDiaryUpdateViewPos},     document.getElementById('newFriendDiary')],
			[${defCommunityUpdateViewPos}, document.getElementById('newBbs')],
			[${defMemberUpdateViewPos},    document.getElementById('memberPhoto')],
			[${defFShoutViewPos},          document.getElementById('FShout')],
			[${defGroupViewPos},           document.getElementById('groupList')],
			[${defFollowyouViewPos},       document.getElementById('followyouList')],
			[${defFollowmeViewPos},        document.getElementById('followmeList')]
		]
	);
<c:choose>
	<c:when test="${txtareaFlg==1}">
		onSetFocus('diary','','${kptVal}','','','');
	</c:when>
	<c:when test="${txtareaFlg==2}">
		<c:choose>
			<c:when test="${kptVtype=='1'}">
				onSetFocus('ort','','${vcmnt}','','','${vrepid}');
			</c:when>
			<c:when test="${kptVtype=='0'}">
				onSetFocus('ore','','${vcmntview}','${vcmnt}','','${vrepid}');
			</c:when>
		</c:choose>
	</c:when>
	<c:when test="${txtareaFlg==99}">
		<c:if test="${kptVtype=='0'}">
			onSetFocus('orenull','','${vcmntview}','${vcmnt}','','');
		</c:if>
	</c:when>
</c:choose>
}
<c:if test="${fn:length(fsNewList)>0}">
// FShoutの自動更新function
setTimeout('updWindowfree(${updateAutoSec})',${updateAutoSec});
</c:if>
-->
</script>

</head>

<body onload="home_onload();">

<s:form>
<div id ="dummy"></div>
<div id="container">
	<!--header-->
	<%@ include file="/WEB-INF/view/pc/fheader.jsp"%>
	<!--/header-->

	<!--navbarメニューエリア-->
	<%@ include file="/WEB-INF/view/pc/fmenu.jsp"%>
	<!--/navbarメニューエリア-->

	<div id="contents" class="clearfix">
		<div class="main">
			<!--メイン-->
			<div class="main-side" style="height:100%;" id="bodySide"></div>
			<!--/main-side-->
			<c:if test="${
				CommunityReqCount > 0 ||
				fn:length(NewPhotoCommentList) > 0 ||
				fn:length(NewDiaryCommentList) > 0 ||
				fn:length(NewFollowList) > 0
			}">
			<!-- 新着 -->
			<div id="frontierInfo">
				<div class="news">
					<div class="newsTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name"><b>新着</b></div></td>

							</tr>
						</table>
					</div>

					<div class="newsBody">
<c:if test="${fn:length(NewDiaryCommentList)>0}">
						<!-- 新着日記コメント -->
						<font style="color:#ff0000;font-weight:bold;">■日記に新着コメントがあります&nbsp;(${fn:length(NewDiaryCommentList)})</font><br/>
						<c:forEach var="e" items="${NewDiaryCommentList}" varStatus="i">
						&nbsp;&nbsp;・<s:link href="/pc/diary/view/${e.diaryid}/${e.yyyymmdd}/${userInfoDto.memberId}" title="${f:h(e.title)}">${f:h(e.stitle)}&nbsp;(${e.comcnt})</s:link><br/>
						</c:forEach>
</c:if>
<c:if test="${fn:length(NewPhotoCommentList)>0}">
						<!-- 新着フォトアルバムコメント -->
						<font style="color:#ff0000;font-weight:bold;">■フォトアルバムに新着コメントがあります&nbsp;(${fn:length(NewPhotoCommentList)})</font><br/>
						<c:forEach var="e" items="${NewPhotoCommentList}" varStatus="i">
						&nbsp;&nbsp;・<s:link href="/pc/photo/detail/${userInfoDto.memberId}/${e.ano}" title="${f:h(e.title)}">${f:h(e.stitle)}&nbsp;(${e.comcnt})</s:link><br/>
						</c:forEach>
</c:if>
<c:if test="${fn:length(NewFollowList)>0}">
						<!-- 新着フォロー -->
						<font style="color:#ff0000;font-weight:bold;">■メンバーからの新着フォローがあります&nbsp;(${fn:length(NewFollowList)})</font><br/>
						<c:forEach var="e" items="${NewFollowList}" varStatus="i">
						<c:choose><c:when test="${appDefDto.FP_CMN_HOST_NAME==e.frontierdomain}">
						&nbsp;&nbsp;・<s:link href="/pc/mem/${e.followmid}" title="${f:h(e.nickname)}さん">${f:h(e.nickname)}さん</s:link><br/>
						</c:when><c:otherwise>
						&nbsp;&nbsp;・<a href="javascript:void(0);goOFrontier('goOFrontier','${e.frontierdomain}','${e.fid}','${e.followmid}');" title="${f:h(e.fnickname)}さん&nbsp;[${f:h(e.frontierdomain)}]">${f:h(e.fnickname)}さん&nbsp;[${f:h(e.frontierdomain)}]</a><br/>
						</c:otherwise></c:choose>
						</c:forEach>
</c:if>
<c:if test="${CommunityReqCount>0}">
						<!-- 新着コミュニティ参加リクエスト -->
						<font style="color:#ff0000;font-weight:bold;">■<s:link href="/pc/request/com">コミュニティへの参加リクエストがあります&nbsp;(${CommunityReqCount})</s:link></font>
</c:if>
					</div>

				</div>
			</div>
			</c:if>

			<!-- お知らせ -->
			<div id="frontierNews">
				<div class="news">
					<div class="newsTitle">

						<table class="news_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name"><s:link href="/pc/news/list"><b>お知らせ</b></s:link></div></td>

							</tr>
						</table>
					</div>
			<c:if test="${fn:length(NewsList)>0}">
					<div class="newsBody">
						<c:forEach var="e" items="${NewsList}" varStatus="i">
							・ <s:link href="/pc/news/view/${e.id}"><b>${f:h(e.title)}</b></s:link><br/>
						</c:forEach>
					</div>
			</c:if>
				</div>
			</div>

			<div class="main-main" id="bodyMain">

			</div>
			<!--/main-main-->
		</div>
		<!--/main-->
		<div class="side">
			<div class="listBox04">
				<div  class="listBox04Title">
					<table class="list_ttl">
						<tr>
							<td class="ttl_name_sp"><div class="ttl_name outColStick">Frontier&nbsp;Net<c:if test="${fn:length(FNetList)>0}">(${fn:length(FNetList)})</c:if></div></td>
						</tr>
					</table>
				</div>
				<div class="listBoxBody">
					<c:forEach var="e" items="${FNetList}" varStatus="i">
					<script>
					<!--
						// Frontier Net
						writeFrontierNet(
							"1",
							"http://${f:h(e.network)}/frontier/pc/gettext/outside/",
							"${userInfoDto.memberId}",
							"${e.network}",
							"${appDefDto.FP_CMN_HOST_NAME}",
							"${appDefDto.FP_CMN_HOST_NAME}"
							);
					-->
					</script>
					<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/sp10.png" alt=""/>
					</c:forEach>
					<c:if test="${fn:length(FNetList)==0}">
						<div style="text-align:center;color:white;">
							&nbsp;<br/>
							何も設定されていません<br/>
							&nbsp;<br/>
							&nbsp;
						</div>
					</c:if>
				</div>
			</div>
			<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/sp10.png" alt=""/>

			<!--/listBox04-->
			<div class="listBox04">
				<div  class="listBox04Title">
					<table class="list_ttl">
						<tr>
							<td class="ttl_name_sp"><div class="ttl_name outColStick">RSS&nbsp;News<c:if test="${fn:length(rssList)>0}">(${fn:length(rssList)})</c:if></div></td>
						</tr>
					</table>
				</div>
				<div class="listBoxBody">
					<div style="padding:0 0 10px 10px;">
						<input type="text" name="rssurl" style="width:75%; border:solid 1px #000" />
						<input type="submit" name="exeInsRss" value="登録" style="border:solid 1px #000; background-color:#808080; color:#fff; font-weight:bold; width:50px;" />
						<div style="text-align:right; color:#fff; padding:3px 10px 1px 0px;">RSS2.0のURLを入力</div>
					</div>
					<div id="rssBody">
					<c:forEach var="e" items="${rssList}" varStatus="i">
					<div id="rss${e.no}">
					<script>
					<!--
						// RSSニュース
						writeRssNews(["${f:h(e.rssurl)}","${e.no}"],5,15);
						RssObjects.push(document.getElementById("rss"+"${no}"));
					-->
					</script>
					<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/sp10.png" alt=""/>
					</div>
					</c:forEach>
					</div>
					<c:if test="${fn:length(rssList)==0}">
						<div style="border-top:1px dotted white;border-right:0px;border-bottom:0px;border-left:0px;text-align:center;color:white;">
							&nbsp;<br/>
							何も登録されていません<br/>
							&nbsp;
						</div>
					</c:if>
				</div>
				<!--/listBoxBody-->
			</div>
			<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/sp10.png" alt=""/>
			<!--/listBox04-->

			<!--/listBox04-->
			<div class="listBox04">
				<c:choose>
				<c:when test="${twOKNGflg==0}">


					<c:choose>
					<c:when test="${useTwitterFlg != '2'}">
					<div  class="listBox04Title">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp" style="width:100px;"><div class="ttl_name outColStick">Twitter</div></td>
							</tr>
						</table>
					</div>
					<div class="listBoxBody">
						<div style="padding:0 0 10px 10px;">
							<s:link href="http://twitter.com/" title="新規取得" target="_blank"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/twitter_logo_header.png" alt="twitter" /></s:link>
						</div>

						<div style="text-align:center;">
							<div class="twitBox" style="text-align:left; margin-top:5px;">
								<ul class="listStyle">
									<li style="color:#ff0000;">TwitterのIDが未設定です。</li>
									<li style="margin-top:5px;padding-left:15px;">既にIDを持っている　->　<a href="/frontier/pc/profile1/" title="設定">設定</a></li>
									<li style="padding-left:15px;">新規にIDを取得する　->　<s:link href="http://twitter.com/" title="新規取得" target="_blank">新規取得</s:link></li>
								</ul>
							</div>
						</div>
						<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/sp10.png" alt=""/>
					</div>
					</c:when>
					<c:otherwise>
					<div  class="listBox04Title">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp" style="width:100px;"><div class="ttl_name outColStick">Twitter</div></td>
								<td style="width:200px; color:#ffffff; position:relative;" id="twiAccountInfo"></td>
							</tr>
						</table>
					</div>
					<div class="listBoxBody">
						<!-- Twitterロゴ -->
						<div style="margin-left:10px;">
							<img src="/images/twitter_logo.png" class="linkImg" alt="twitter" title="twitter" onclick="readTwTop();" />
						</div>
						<!-- Twitterロゴ -->

						<!-- Twitter検索エリア -->
						<div style="margin-bottom:5px;text-align:center;">
							<input type="text" name="SearchTwitterText" style="width:75%; border:solid 1px #000; margin-top:10px;" value=""/>
							<input type="button" onclick="SearchTwitter(document.forms[0].SearchTwitterText.value);" value="検索" class="twbtn" />
							<div style="text-align:left;margin-left:10px;color:#ffffff;font-size:12px;">
								<input type="checkbox" name="chkMyTwit"/>&nbsp;Frontierで登録した情報<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(自分の発言/＠/★)から検索
							</div>
						</div>
						<!-- Twitter検索エリア -->

						<!-- Twitterメインエリア -->
						<div style="text-align:center;">
							<div class="twitBox" style="text-align:left;" id="memInfo"><%--プロフィール表示エリア--%></div>
							<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/sp10.png" alt=""/>
							<div class="twitBox" id="twitterTL"><%-- TL表示エリア(新着/通常/もっと読む) --%></div>
							<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/sp10.png" alt=""/>
						</div>
						<!-- Twitterメインエリア -->
					</div>
<script>
<!--
// Twitter アカウント選択表示ファンクション
readTwAccount();
// Twitter TOP初期表示ファンクション
readTwTop();
-->
</script>

					</c:otherwise>
					</c:choose>
				</c:when>
				<c:otherwise>
					<div  class="listBox04Title">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp" style="width:100px;"><div class="ttl_name outColStick">Twitter</div></td>
								<td style="width:200px; color:#ffffff; position:relative;" id="twiAccountInfo"></td>
							</tr>
						</table>
					</div>
					<div class="listBoxBody">
						<div style="padding:0 0 10px 10px;">
							<s:link href="http://twitter.com/" title="新規取得" target="_blank"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/twitter_logo_header.png" alt="twitter" /></s:link>
						</div>

						<div style="text-align:center;">
							<div class="twitBox" style="text-align:left; margin-top:5px;">
								<ul class="listStyle">
									<li style="color:#ff0000;">APIの制限によりデータを取得できない、もしくはサーバーが混み合っています。<br/>しばらくお待ちください。</li>
								</ul>
							</div>
						</div>
						<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/sp10.png" alt=""/>
					</div>
<script>
<!--
// Twitter アカウント選択表示ファンクション
// エラー時でもアカウントの切替はできてもよいはず
//readTwAccount();
-->
</script>
				</c:otherwise>
				</c:choose>
				<!--/listBoxBody-->
			</div>
			<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/sp10.png" alt=""/>
			<!--/listBox04-->


		</div>
		<!--/side-->
	</div>
	<!--/contents-->
	<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/sp10.png" alt=""/>

	<!--footer-->
	<%@ include file="/WEB-INF/view/pc/ffooter.jsp"%>
	<!--footer-->
</div>
<!--/container-->
<input type="hidden" name="domain"      value=""/>
<input type="hidden" name="gid"         value=""/>
<input type="hidden" name="vmid"        value=""/>
<input type="hidden" name="pfdomain"    value=""/>
<input type="hidden" name="pfmid"       value=""/>
<input type="hidden" name="pmid"        value=""/>
<input type="hidden" name="fsListFlg"   value="${fsListFlg}"/>
<input type="hidden" name="MstNewSid"   value=""/>
<!-- プロフィール、ＴＬの両方が読まれた際に実行する新着チェック用変数 -->
<input type="hidden" name="loadPrf"     value="0"/>
<input type="hidden" name="loadTL"      value="0"/>
<!-- アカウント切替を押したかのチェック用変数 -->
<input type="hidden" name="pushAccount" value="0"/>
<!-- 投稿元情報用の変数 -->
<input type="hidden" name="repId"       value=""/>
</s:form>
</body>
</html>