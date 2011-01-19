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
<title>[frontier]メンバー一覧</title>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/community.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/assort.css" type="text/css" />

</head>

<body onload="getVal();chkval();">
<s:form>
	<div class="mainComAddEvent">
		<!--メイン-->
		<div class="mainComAddEvent-main">
		
			<div class="listBoxAddEventList">
				<div class="listBoxAddEventListTitle">
					<table class="list_ttl">
						<tr>
							<td class="ttl_name_sp"><div class="ttl_name comColStick">メンバー一覧(<c:choose><c:when test="${(offset+1)==(offset + fn:length(results))}">${resultscnt}</c:when><c:otherwise>${offset+1}～${offset + fn:length(results)}</c:otherwise></c:choose>/${resultscnt}件)</div></td>
						</tr>
					</table>
				</div>
					<c:if test="${offset>0 || resultscnt>(offset + fn:length(results))}">
					<div class="linkBox01">
						<div class="note"></div>
						<div>
							<c:if test="${offset>0}">
								<a name="tprpg" href="/frontier/pc/com/entevent/movelist/${communityDto.cid}/${pgcnt-1}" onclick="nextprepg('/frontier/pc/com/entevent/movelist/${communityDto.cid}/${pgcnt-1}',mlist,'tprpg')">前を表示</a>
							</c:if>
							<span name="sp">&nbsp;</span>
							<c:if test="${resultscnt>(offset + fn:length(results))}">
								<a name="tnxtpg" href="/frontier/pc/com/entevent/movelist/${communityDto.cid}/${pgcnt+1}" onclick="nextprepg('/frontier/pc/com/entevent/movelist/${communityDto.cid}/${pgcnt+1}',mlist,'tnxtpg')">次を表示&gt;&gt;</a>
							</c:if>
						</div>
					</div>
					</c:if>
				<!-- 一覧 -->
				<table border="0" cellpadding="0" cellspacing="0" class="listBoxAddEventListBody">
					<c:set var="row" value="5"/>
					<c:forEach var="e" items="${results}" varStatus="i">
						<c:if test="${i.count%row==1}"><tr></c:if>
							<td width="20%">
								<div class="${fn:length(results)<row?'line02':i.count<row?'line01':i.count==row?'line03':i.count%row==0?'':'line02'}">
									<div class="mount04" id="div${e.mid}">
										<div class="img-back">
											<s:link href="/pc/mem/${f:h(e.mid)}" title="${f:h(e.nickname)}">
												<c:choose>
												<c:when test="${f:h(e.pic) != ''}"><img src="${appDefDto.FP_CMN_CONTENTS_ROOT}/${fn:replace(e.pic,'dir','pic76')}" alt="${f:h(e.nickname)}"/></c:when>
												<c:otherwise><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg76.gif" alt="${f:h(e.nickname)}"/></c:otherwise>
												</c:choose>
											</s:link>
										</div>
										<span>${f:h(e.nickname)}さん</span><br/>
										<div id="add${e.mid}">
											<input type="button" onclick="mlistadd('${e.mid}','add${e.mid}','del${e.mid}','div${e.mid}');" value="選択する" class="comAddEvent" />
										</div>
										<div id="del${e.mid}" style="display:none;">
											<input type="button" onclick="mlistdel('${e.mid}','add${e.mid}','del${e.mid}','div${e.mid}');" value="外す" class="comAddEvent" />
										</div>
										
									</div>
								</div>
							</td>
						<c:if test="${i.count%row==0||(offset+i.count)==resultscnt}">
							<c:if test="${fn:length(results)<row}">
								<c:forEach begin="1" end="${row - fn:length(results)}" varStatus="i2">
									<td width="20%">&nbsp;</td>
								</c:forEach>
							</c:if>
							</tr>
						</c:if>
					</c:forEach>
				</table>
				<!-- 一覧 -->

				<div class="comAddEventArea">
					<ul class="clearfix">
						<li>
							<input type="button" onclick="exefinish(mlist,'${oyacid}','${oyabbsid}');" value="完了" class="comAddEvent" />
						</li>
						<li style="margin-left:10px;"><input type="button" onclick="window.close();" value="やめる" class="comAddEvent" /></li>
					</ul>
				</div>
					<c:if test="${offset>0 || resultscnt>(offset + fn:length(results))}">
					<div class="linkBox02">
						<div class="note"></div>
						<div>
							<c:if test="${offset>0}">
								<a name="tprpg" href="/frontier/pc/com/entevent/movelist/${communityDto.cid}/${pgcnt-1}" onclick="nextprepg('/frontier/pc/com/entevent/movelist/${communityDto.cid}/${pgcnt-1}',mlist,'tprpg')">前を表示</a>
							</c:if>
							<span name="sp">&nbsp;</span>
							<c:if test="${resultscnt>(offset + fn:length(results))}">
								<a name="tnxtpg" href="/frontier/pc/com/entevent/movelist/${communityDto.cid}/${pgcnt+1}" onclick="nextprepg('/frontier/pc/com/entevent/movelist/${communityDto.cid}/${pgcnt+1}',mlist,'tnxtpg')">次を表示&gt;&gt;</a>
							</c:if>
						</div>
					</div>
					</c:if>
				<!--/listBoxAddEventListTitle-->
				
			</div>
			<!--/listBoxAddEventList-->
			
		</div>
		<!--/mainComAddEvent-main-->
	</div>
	<!--/mainComAddEvent-->
<html:hidden property="selmid" name="selmid" value="${selmid}"/>
</s:form>
</body>
</html>
