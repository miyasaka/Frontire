<%@ page pageEncoding="UTF-8" %>
			<div class="mainDiary-side">
				<div class="listBox09">
					<div class="listBox09Title">
						<table class="ttl">
							<tr>
								<td class="ttl_name_sp"><c:if test="${vUser}"><div class="ttl_name myColStick"></c:if><c:if test="${!vUser}"><div class="ttl_name memColStick"></c:if>カレンダー</div></td>
							</tr>
						</table>
					</div>
					<div class="listBoxBody">
						<div id="listBoxDiary">
							<div class="DiaryBody clearfix">
								<div class="calendar"><div>
								<table>
									<caption class="clearfix">
										<span class="previousMonth">
											<c:choose>
												<c:when test="${existDiary eq false}">
													<s:link href="/pc/diary/list/${f:h(mid)}">&lt;</s:link>
												</c:when>
												<c:otherwise>
													<s:link href="/pc/diary/beforeMonth/${f:h(mid)}">&lt;</s:link>
												</c:otherwise>
											</c:choose>
										</span>
										<span class="calMonth">
											<fmt:formatDate value="${f:date(f:h(diaryDay),'yyyyMMdd')}" pattern="yyyy年MM月" />
										</span>
										<c:if test="${fn:substring(f:h(diaryDay),0,6) != fn:substring(f:h(today),0,6)}">
										<span class="nextMonth">
											<s:link href="/pc/diary/nextMonth/${f:h(mid)}">&gt;</s:link>
										</span>
										</c:if>
									</caption>
									<tbody>
										<tr>
											<th abbr="日曜日" class="sun">日</th>
											<th abbr="月曜日" class="mon">月</th>
											<th abbr="火曜日" class="tue">火</th>
											<th abbr="水曜日" class="wed">水</th>
											<th abbr="木曜日" class="thu">木</th>
											<th abbr="金曜日" class="fri">金</th>
											<th abbr="土曜日" class="sat">土</th>
										</tr>
										<tr>
											<c:forEach var="fmonth" items="${cal}" varStatus="status">
												<c:if test="${status.first}">
													<c:forEach begin="1" end="${f:h(fmonth['week']) - 1}" var="i">
														<td>&nbsp;
														</td>
													</c:forEach>
												</c:if>

												<td <c:if test="${fmonth['existDiary'] eq 1}">class="on"</c:if>>
													<span>
														<c:choose>
															<c:when test="${fmonth['existDiary'] eq 1}">
																<s:link href="/pc/diary/searchDay/${f:h(fmonth['detailDay']) }/${f:h(mid)}">${f:h(fmonth['day']) }</s:link>
															</c:when>
															<c:otherwise>
																${f:h(fmonth['day']) }
															</c:otherwise>
														</c:choose>
													</span>
												</td>

												<c:choose>
													<c:when test="${fmonth['week']=='7'}">
														</tr>
														<tr>
													</c:when>
												</c:choose>

												<c:if test="${status.last}">
													<c:forEach begin="1" end="${7 - f:h(fmonth['week'])}" var="i">
														<td>&nbsp;
														</td>
													</c:forEach>
												</c:if>
											</c:forEach>
										</tr>
									</tbody>
								</table>
								</div></div>
							</div>
							<!--/DiaryBody-->

						</div>
					</div>
				</div>
			</div>
			<!--/mainDiary-side-->

<%-- 
<div id="bodyMainAreaSub" class="clearfix">
<div class="diaryCalendar"><div>
<table>
	<caption class="clearfix">
		<span class="previousMonth">

			<c:choose>
				<c:when test="${existDiary eq false}">
					<s:link href="/pc/diary/list/${f:h(mid)}">&lt;</s:link>
				</c:when>
				<c:otherwise>
					<s:link href="/pc/diary/beforeMonth/${f:h(mid)}">&lt;</s:link>
				</c:otherwise>
			</c:choose>

		</span>
		<span class="calMonth">
			<fmt:formatDate value="${f:date(f:h(diaryDay),'yyyyMMdd')}" pattern="yyyy年MM月" />
		</span>

		<c:if test="${fn:substring(f:h(diaryDay),0,6) != fn:substring(f:h(today),0,6)}">
			<span class="nextMonth">
				<s:link href="/pc/diary/nextMonth/${f:h(mid)}">&gt;</s:link>
			</span>
		</c:if>

	</caption>
	<tbody>
		<tr>
			<th abbr="日曜日" class="sun">日</th>
			<th abbr="月曜日" class="mon">月</th>
			<th abbr="火曜日" class="tue">火</th>
			<th abbr="水曜日" class="wed">水</th>
			<th abbr="木曜日" class="thu">木</th>
			<th abbr="金曜日" class="fri">金</th>
			<th abbr="土曜日" class="sat">土</th>
		</tr>
		<tr>

<c:forEach var="fmonth" items="${cal}" varStatus="status">
	<c:if test="${status.first}">
		<c:forEach begin="1" end="${f:h(fmonth['week']) - 1}" var="i">
			<td>&nbsp;
			</td>
		</c:forEach>
	</c:if>

	<td <c:if test="${fmonth['existDiary'] eq 1}">class="on"</c:if>>
		<span>
			<c:choose>
				<c:when test="${fmonth['existDiary'] eq 1}">
					<s:link href="/pc/diary/searchDay/${f:h(fmonth['detailDay']) }/${f:h(mid)}">${f:h(fmonth['day']) }</s:link>
				</c:when>
				<c:otherwise>
					${f:h(fmonth['day']) }
				</c:otherwise>
			</c:choose>
		</span>
	</td>

	<c:choose>
		<c:when test="${fmonth['week']=='7'}">
			</tr>
			<tr>
		</c:when>
	</c:choose>

	<c:if test="${status.last}">
		<c:forEach begin="1" end="${7 - f:h(fmonth['week'])}" var="i">
			<td>&nbsp;
			</td>
		</c:forEach>
	</c:if>
</c:forEach>

	</tr>
	</tbody>
</table>
</div></div>

<c:if test="${existDiary}">
	<div class="diaryHistory">
	<div class="heading">
	<h3>過去の日記</h3>
	</div>
	<div class="contents">
		<dl>
		<c:forEach var="fmonth" items="${oldCal}" varStatus="status">
			<dt>${f:h(fmonth.key)}年</dt>
			<dd <c:if test="${f:h(fmonth.key) eq fn:substring(f:h(vEntdate),0,4)}"> class="last"</c:if>>
			<ul class="clearfix">
	
			<c:forEach var="i" begin="0" end="11">
				<li>
	
				<c:choose>
					<c:when test="${fn:substring(f:h(today),0,6) < fn:substring(f:h(fmonth.value[i]),0,6) || fn:substring(f:h(vEntdate),0,6) > fn:substring(f:h(fmonth.value[i]),0,6)}">
						<fmt:formatDate value="${f:date(fmonth.value[i],'yyyyMMdd')}" pattern="MM月" />
					</c:when>
					<c:otherwise>
						<s:link href="/pc/diary/searchMonth/${f:h(fmonth.value[i])}/${f:h(mid)}">
							<fmt:formatDate value="${f:date(fmonth.value[i],'yyyyMMdd')}" pattern="MM月" />
						</s:link>
					</c:otherwise>
				</c:choose>
	
				</li>
			</c:forEach>
	
			</ul>
			</dd>
		</c:forEach>
	
		</dl>
	</div>
	</div>
</c:if>

<!--/bodyMainAreaSub--></div>
 --%>