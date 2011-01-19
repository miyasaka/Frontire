<%@ page pageEncoding="UTF-8" %>
			<div class="mainDiary-side">
				<div class="listBox09">
					<div class="listBox09Title">
						<table class="ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name outColStick">カレンダー</div></td>
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
											<s:link href="/pc/fdiary/beforeMonth/">&lt;</s:link>
										</span>
										<span class="calMonth">
											<fmt:formatDate value="${f:date(f:h(diaryDay),'yyyyMMdd')}" pattern="yyyy年MM月" />
										</span>
										<c:if test="${fn:substring(f:h(diaryDay),0,6) != fn:substring(f:h(today),0,6)}">
										<span class="nextMonth">
											<s:link href="/pc/fdiary/nextMonth/">&gt;</s:link>
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
																<s:link href="/pc/fdiary/searchDay/${f:h(fmonth['detailDay']) }">${f:h(fmonth['day']) }</s:link>
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

