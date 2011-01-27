<%@ page language="java" contentType="text/html; charset=windows-31J"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>[frontier] <fmt:formatDate value="${f:date(f:h(calendarDay),'yyyyMMdd')}" pattern="yyyy年MM月" />のｽｹｼﾞｭｰﾙ</title>
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS"/>
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />

<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/prototype.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier_1.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/jquery.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/calendar.js"></script>

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
		<div class="mainSchedule">
			<!--メイン-->

			<div class="mainSchedule-main">
				<div class="listBoxSchedule clearfix">
					<div class="listBoxSchedulePage">

						<table border="0">
							<tr>
								<td><span class="whoSchedule">${f:h(userInfoDto.nickName)}のｽｹｼﾞｭｰﾙ</span></td>
								<td style="text-align:right;"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/calendar2.png" alt="" /><a href="javascript:void(0)" onclick="MM_openBrWindow('/frontier/pc/calendar/entryCalendar', '', 'width=760,height=640,toolbar=no,scrollbars=yes,left=10,top=10')">予定を入力する</a></td>
							</tr>
						</table>
					</div>
					<div class="listBoxScheduleTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name myColStick"><fmt:formatDate value="${f:date(f:h(calendarDay),'yyyyMMdd')}" pattern="yyyy年 MM月" /></div></td>
							</tr>
						</table>
					</div>
					<!--/listBoxScheduleTitle-->
					<div class="listBoxHead clearfix">
						<div class="ttlHeadArea" style="text-align:right;">
							<s:link href="beforeMonth">&lt;&lt;前の月</s:link> | <s:link href=".">当月</s:link> | <s:link href="nextMonth">次の月&gt;&gt;</s:link>
						</div>
						<div class="bodyArea">
							<div class="introArea clearfix">
								<ul>
									<li class="scheduleEnd">…予定&nbsp;&nbsp;アイコンをクリックすると、ｽｹｼﾞｭｰﾙに予定を入力することができます。&nbsp;&nbsp;</li>
									<li class="eventEnd">…イベント&nbsp;&nbsp;アイコンをクリックすると、ｽｹｼﾞｭｰﾙにイベントを入力することができます。</li>
								</ul>
							</div>
							<input type="checkbox" onclick="changeSchedule(this);" <c:if test="${defDisptypeCalendar eq '1'}">checked</c:if>/>共有中のスケジュールのみ表示

							<!--カレンダー-->
							<table class="calendarSchedule clearfix" id="calendarBody">
								<tr>
									<th style="color:#ff0000;">日</th>
									<th>月</th>
									<th>火</th>
									<th>水</th>
									<th>木</th>
									<th>金</th>
									<th style="color:#0085cc;">土</th>
								</tr>
								<tr>

								<c:forEach var="fmonth" items="${cal}" varStatus="status">
									<c:choose>
									<c:when test="${fmonth['before']}">
										<td class="before">
											<div class="caldiv1">
												<div class="divDay">
													${fmonth['day']}
												</div>
												<div class="divContents">
												<c:if test="${fmonth['existSchedule'] eq 1}">
													<c:forEach var="s" items="${fmonth['schedule']}">
														<c:choose>
														<c:when test="${s['entry'] eq 1}">
														<ul class="contEvent">
															<li><a href="/frontier/pc/com/event/view/${s['cid']}/${s['bbsid']}" title="(${f:h(s['ctitle'])})&#13;${f:h(s['detail'])}" style="color:#${s['titlecolor']};"/>
															${(s['starttime'] == null and s['endtime'] != null)?'～':s['starttime']}${s['endtime']}&nbsp;${f:h(s['title'])}</li>
														</ul>
														</c:when>
														<c:when test="${s['entry'] eq 2}">
														<ul class="contSchedule">
															<li><a title="(${f:h(s['nickname'])})&#13;${f:h(s['detail'])}" style="color:#${s['titlecolor']};" href="javascript:void(0)" onClick="MM_openBrWindow('/frontier/pc/calendar/view/${s['sno']}/${s['cid']}/2','','width=760,height=640,toolbar=no,scrollbars=yes,left=10,top=10')">
															${(s['starttime'] == null and s['endtime'] != null)?'～':s['starttime']}${s['endtime']}&nbsp;${f:h(s['title'])}
															</a></li>
														</ul>
														</c:when>
														</c:choose>
													</c:forEach>
												</c:if>
												</div>
											</div>
										</td>
									</c:when>
									<c:when test="${fmonth['now']}">

									<td class="${fmonth['today'] eq 1?'today':fmonth['week'] eq 1?'san':fmonth['week'] eq 7?'sat':''}">
										<div class="caldiv1">
											<div class="divDay">
												${f:h(fmonth['day']) }
											</div>
											<div class="divStatus">
												<a href="javascript:void(0)" onClick="MM_openBrWindow('/frontier/pc/calendar/entry/${fn:substring(f:h(fmonth['detailDay']),0,4)}/${fn:substring(f:h(fmonth['detailDay']),4,6)}/${fn:substring(f:h(fmonth['detailDay']),6,8)}/2','','width=760,height=640,toolbar=no,scrollbars=yes,left=10,top=10')" title="ｽｹｼﾞｭｰﾙ"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/calendar2.png" alt="ｽｹｼﾞｭｰﾙ" /></a>
												<c:if test="${communityCnt>0}"><a href="javascript:void(0)" onload="addEvent(this)" onclick="handler(event,${fn:substring(f:h(fmonth['detailDay']),0,4)},${fn:substring(f:h(fmonth['detailDay']),4,6)},${fn:substring(f:h(fmonth['detailDay']),6,8)})" title="イベント"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/ico05-001.gif" alt="イベント" /></a></c:if>
											</div>



											<div class="divContents">
											<c:if test="${fmonth['existSchedule'] eq 1}">
												<c:forEach var="s" items="${fmonth['schedule']}">
													<c:choose>
													<c:when test="${s['entry'] eq 1}">
													<ul class="contEvent">
														<li><a href="/frontier/pc/com/event/view/${s['cid']}/${s['bbsid']}" title="(${f:h(s['ctitle'])})&#13;${f:h(s['detail'])}" style="color:#${s['titlecolor']};">
														${(s['starttime'] == null and s['endtime'] != null)?'～':s['starttime']}${s['endtime']}&nbsp;${f:h(s['title'])}</a></li>
													</ul>
													</c:when>
													<c:when test="${s['entry'] eq 2}">
													<ul class="contSchedule">
														<li><a title="(${f:h(s['nickname'])})&#13;${f:h(s['detail'])}" style="color:#${s['titlecolor']};" href="javascript:void(0)" onClick="MM_openBrWindow('/frontier/pc/calendar/view/${s['sno']}/${s['cid']}/2','','width=760,height=640,toolbar=no,scrollbars=yes,left=10,top=10')">
														${(s['starttime'] == null and s['endtime'] != null)?'～':s['starttime']}${s['endtime']}&nbsp;${f:h(s['title'])}
														</a></li>
													</ul>
													</c:when>
													</c:choose>
												</c:forEach>
											</c:if>
											</div>
										</div>
									</td>
									<c:if test="${fmonth['week']=='7'}">
										</tr><tr>
									</c:if>
									</c:when>
									<c:when test="${fmonth['next']}">
										<td class="next">
											<div class="caldiv1">
												<div class="divDay">
													${fmonth['day']}
												</div>
												<div class="divContents">
												<c:if test="${fmonth['existSchedule'] eq 1}">
													<c:forEach var="s" items="${fmonth['schedule']}">
														<c:choose>
														<c:when test="${s['entry'] eq 1}">
														<ul class="contEvent">
															<li><a href="/frontier/pc/com/event/view/${s['cid']}/${s['bbsid']}" title="(${f:h(s['ctitle'])})&#13;${f:h(s['detail'])}" style="color:#${s['titlecolor']};"/>
															${(s['starttime'] == null and s['endtime'] != null)?'～':s['starttime']}${s['endtime']}&nbsp;${f:h(s['title'])}</li>
														</ul>
														</c:when>
														<c:when test="${s['entry'] eq 2}">
														<ul class="contSchedule">
															<li><a title="(${f:h(s['nickname'])})&#13;${f:h(s['detail'])}" style="color:#${s['titlecolor']};" href="javascript:void(0)" onClick="MM_openBrWindow('/frontier/pc/calendar/view/${s['sno']}/${s['cid']}/2','','width=760,height=640,toolbar=no,scrollbars=yes,left=10,top=10')">
															${(s['starttime'] == null and s['endtime'] != null)?'～':s['starttime']}${s['endtime']}&nbsp;${f:h(s['title'])}
															</a></li>
														</ul>
														</c:when>
														</c:choose>
													</c:forEach>
												</c:if>
												</div>
											</div>
										</td>
									</c:when>
									</c:choose>
								</c:forEach>
								</tr>	

		
								
								
							</table>
							<!--/calendarSchedule-->
							
							<div class="introArea clearfix">
								<ul>
									<li class="scheduleEnd">…予定&nbsp;&nbsp;アイコンをクリックすると、ｽｹｼﾞｭｰﾙに予定を入力することができます。&nbsp;&nbsp;</li>
									<li class="eventEnd">…イベント&nbsp;&nbsp;アイコンをクリックすると、ｽｹｼﾞｭｰﾙにイベントを入力することができます。</li>
								</ul>
							</div>




						</div>
						<!--bodyArea-->
						<div class="ttlHeadArea" style="text-align:right;">
							<s:link href="beforeMonth">&lt;&lt;前の月</s:link> | <s:link href="${today}">当月</s:link> | <s:link href="nextMonth">次の月&gt;&gt;</s:link>
						</div>
					</div>
					<!--/listBoxHead-->
				</div>
				<!--/listBoxSchedule-->
			</div>
			<!--/mainSchedule-main-->

			<div class="calendarTable" id="cal"></div>

		</div>
		<!--/mainSchedule-->
	</div>
	<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/sp10.png" alt=""/>
	<!--/contents-->
	<!--footer-->
	<%@ include file="/WEB-INF/view/pc/ffooter.jsp"%>
	<!--footer-->
</div>
<!--/container-->






</s:form>
</body>
</html>
