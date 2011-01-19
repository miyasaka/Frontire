<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/contentType.jsp"%>
<?xml version="1.0" encoding="Shift_JIS"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHML 1.0 transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ja" lang="ja">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<title>[frontier]ﾏｲﾄｯﾌﾟ</title>
<style type="text/css">
<!--
	body{
		font-size:xx-small;
		background-color:#fafafa;
	}
	a:link{
		color:#2288FF;
	}
	a:focus{
		color:#ffffff;
	}
	a:visited{
		color:#2288FF;
	}
	a:hover{
		color:#ff0000;
	}

-->
</style>
</head>
<body>
<s:form>
<%-- ロケール情報を設定 --%>
<fmt:setLocale value="ja-JP" />
	<div style="width:300px;">
		<div>

			<!--header-->
			<div style="background-color:#100800; text-align:center;">
				<img src="/images/m/sp5.gif" ><br>
				<a name="TOP"><img src="/images/m/logom.gif" ></a>
				<img src="/images/m/sp5.gif" ><br>
			</div>
			<div style="background-color:#afafaf;">
				&nbsp;<span style="color:#0000ff; font-size:xx-small;">●</span>&nbsp;<span style="font-size:xx-small;">ﾏｲﾄｯﾌﾟ</span>
			</div>
			<div style="text-align:left;"><a href="#pbtm" name="ptop" id="ptop" accesskey="8"><span style="font-size:xx-small;">[8]▼</span></a></div>
			<!--/header-->
			<!--コンテンツ開始-->

				<hr style="border-color:#bdbdbd; background-color:#bdbdbd;">

				<!--マイコンテンツ-->
				<div>
					<div>
						<c:if test="${fn:length(NewDiaryCommentList)>0}">
						<c:forEach var="e" items="${NewDiaryCommentList}" varStatus="i" end="0">
						・<s:link href="/m/diary/view/${e.diaryid}/${e.yyyymmdd}/${userInfoDto.memberId}"><span style="font-size:xx-small; color:#ff0000;">新着ｺﾒﾝﾄがあります(${fn:length(NewDiaryCommentList)})</span></s:link>
						</c:forEach><br>
						</c:if>
						<img src="/images/m/sp5.gif" ><br>
					</div>

					<span style="font-size:xx-small;">【${f:h(userInfoDto.nickName)}】</span><br>
					<span style="font-size:xx-small;">■</span><s:link href="/m/fshout/list"><span style="font-size:xx-small;">F Shout一覧</span></s:link><span style="font-size:xx-small;">&nbsp;|&nbsp;</span><s:link href="/m/fshout/my"><span style="font-size:xx-small;">自分宛</span></s:link><br>
					<span style="font-size:xx-small;">■</span><s:link href="/m/schedule"><span style="font-size:xx-small;">ｽｹｼﾞｭｰﾙ</span></s:link><span style="font-size:xx-small;">${defDisptypeCalendar eq '1'?'【共有のみ】':'【全て】'}</span><br>
					<span style="font-size:xx-small;">■</span><s:link href="/m/diary/list/${f:h(userInfoDto.memberId)}"><span style="font-size:xx-small;">日記を読む</span></s:link><br>

				</div>
				<hr style="border-color:#bdbdbd; background-color:#bdbdbd;">
				<!--/マイコンテンツ-->




				<!--F Shout-->
				<div style="background-color:#4a4a4a;">
					&nbsp;<s:link href="/m/fshout/list"><span style="color:#9999ff; font-size:xx-small;">F Shout一覧</span></s:link>&nbsp;<span style="color:#ffffff; font-size:xx-small;">|</span>&nbsp;<s:link href="/m/fshout/my"><span style="color:#9999ff; font-size:xx-small;">自分宛</span></s:link>
				</div>
				<div>
					<img src="/images/m/sp5.gif" ><br>
					<div style="font-size:xx-small;color:#ff0000;"><html:errors/></div>
					<c:if test="${vfscomment!=null}">
					<!-- 返信用フィールド -->
					<span style="font-weight:bold;font-size:xx-small;color:#FF0000;">以下の内容に対して返信します。</span><br/>
					<div style="background-color:#c0c0c0;font-size:xx-small;">${vfscomment}</div><br/>
					<!-- 返信用フィールド -->
					</c:if>
					全角240文字以内<br/>
					<textarea name="fscomment" rows="2" style="width:100%;">${f:h(fscomment)}</textarea><br/>
					<input type="checkbox" name="confirmflg" value="1"<c:if test="${confirmflg==1}"> checked</c:if>>&nbsp;相手に確認を求める<br/>
					公開範囲&nbsp;
					<select name="publevel">
						<option value="0"<c:if test="${publevel==0}"> selected</c:if>>外部<c:if test="${dpublevel==0}">(ﾃﾞﾌｫﾙﾄ)</c:if></option>
						<option value="1"<c:if test="${publevel==1}"> selected</c:if>>Frontier Netまで<c:if test="${dpublevel==1}">(ﾃﾞﾌｫﾙﾄ)</c:if></option>
						<option value="2"<c:if test="${publevel==2}"> selected</c:if>>ﾏｲFrontierまで<c:if test="${dpublevel==2}">(ﾃﾞﾌｫﾙﾄ)</c:if></option>
						<option value="3"<c:if test="${publevel==3}"> selected</c:if>>ｸﾞﾙｰﾌﾟまで<c:if test="${dpublevel==3}">(ﾃﾞﾌｫﾙﾄ)</c:if></option>
					</select>
<c:if test="${Chktwitter==2}">
					<input type="checkbox" name="twitterflg" value="2"<c:if test="${twitterflg==2}"> checked</c:if>>&nbsp;Twitterにも投稿
</c:if>
					<br/>
					<input type="submit" name="fshout" value="shout!"/>
					
<c:if test="${fscommentConfCnt>0}">
<!-- 要確認コメントがある場合はメッセージ表示 -->
<br/><div style="font-size:xx-small; background-color:#ffffcc; text-align:center;">
<br/>
<s:link href="/m/fshout/my"><span style="color:#FF0000;">あなた宛の投稿があります(${fscommentConfCnt})</span></s:link>
<br/><br/>
</div>
</c:if>
<c:choose>
<c:when test="${fn:length(fsList)>0}">
					<!-- ここからFShout一覧 -->
<c:forEach var="e" items="${fsList}" varStatus="status">
<hr style="border-color:#bdbdbd; background-color:#bdbdbd;">
<%--
// --------------------------- //
// conftype(確認状況用変数)    //
// --------------------------- //
//    0:何もなし               //
//    1:未確認  (自分->相手)   //
//    2:確認済み(自分->相手)   //
//    3:未確認  (相手->自分)   //
//    4:確認済み(相手->自分)   //
// --------------------------- //
--%>
<%-- 背景色の設定 --%>
<c:choose>
<c:when test="${e.conftype==1}"><div style="background-color:#ccffcc;font-size:xx-small;"></c:when>
<c:when test="${e.conftype==3}"><div style="background-color:#ffcccc;font-size:xx-small;"></c:when>
<c:otherwise><div></c:otherwise>
</c:choose>
<%-- domainチェック、他Frontierならばfrontierdomainを表示 --%>
<c:choose>
<c:when test="${e.membertype==0}"><span style="font-size:xx-small;"><s:link href="/m/fshout/${f:h(e.mid)}">${f:h(e.nickname)}</s:link></span></c:when>
<c:otherwise><span style="font-size:xx-small;">${f:h(e.nickname)}&nbsp;[${e.frontierdomain}]</span></c:otherwise>
</c:choose>
<%-- 確認コメントの設定 --%>
<c:choose>
<c:when test="${e.conftype==2 || e.conftype==4}">&nbsp;[<span style="font-weight:bold;font-size:xx-small;color:#cccccc;">内容確認済み</span>]</c:when>
<c:when test="${e.conftype==3}">&nbsp;[<s:link href="confirm/${f:h(e.mid)}/${f:h(e.no)}"><span style="font-weight:bold;font-size:xx-small;color:#FF0000;">内容確認</span></s:link>]</c:when>
</c:choose>
						<br>
						<span style="font-size:xx-small;"><c:if test="${e.twitter=='1'}"><img src="/images/m/twitter.gif"/></c:if>${e.viewComment}</span><br>
						<span style="font-size:xx-small;">${f:h(e.entdate)} [<c:choose><c:when test="${userInfoDto.memberId==e.mid}"><s:link href="del/${f:h(e.no)}">削除</s:link></c:when><c:otherwise><s:link href="re/${f:h(e.mid)}/${f:h(e.no)}">返信</s:link></c:otherwise></c:choose>][<s:link href="rt/${f:h(e.mid)}/${f:h(e.no)}">RT</s:link>]
						&nbsp;&nbsp;<c:choose><c:when test="${e.pubLevel=='0'}">外部</c:when><c:when test="${e.pubLevel=='1'}">Frontier Netまで</c:when><c:when test="${e.pubLevel=='2'}">ﾏｲFrontierまで</c:when><c:otherwise>ｸﾞﾙｰﾌﾟまで</c:otherwise></c:choose>公開
						</span><br>
					</div>
					<!-- ここまでFShout一覧 -->
</c:forEach>
</c:when>
<c:otherwise><hr style="border-color:#bdbdbd; background-color:#bdbdbd;">まだ何もありません</c:otherwise>
</c:choose>
					<hr style="border-color:#bdbdbd; background-color:#bdbdbd;">

				</div>
				<table style="width:100%;">
					<tr>
						<td>
							<div style="text-align:left;"><a href="#ptop" accesskey="2"><span style="font-size:xx-small;">[2]▲</span></a></div>
						</td>
						<td>
							<div style="text-align:right;"><span style="font-size:xx-small;">⇒</span><s:link href="/m/fshout/list"><span style="font-size:xx-small;">一覧へ</span></s:link></div>
						</td>
					</tr>
				</table>

				<hr style="border-color:#bdbdbd; background-color:#bdbdbd;">
				<!--/F Shout-->


				<c:if test="${fn:length(fdiaryNewList)>0}">
					<!--最新日記-->
					<div style="background-color:#4a4a4a;">
						&nbsp;<s:link href="/m/newfrienddiary"><span style="color:#9999ff; font-size:xx-small;">ｸﾞﾙｰﾌﾟ最新日記</span></s:link>
					</div>
					<div>
						<img src="/images/m/sp5.gif" ><br>
						<c:forEach var="e" items="${fdiaryNewList}" varStatus="status">
						<div>
							<span style="font-size:xx-small;">[${e.entdatesla}]&nbsp;${f:h(e.nickname)}</span><br>
								<s:link href="/m/diary/view/${f:h(e.diaryid)}/${fn:substring(f:h(e.yyyymmdd),0,8)}/${f:h(e.mid)}"><span style="font-size:xx-small;">${f:h(e.title) }<c:if test="${e.count>0}">(${e.count})</c:if></span></s:link><br>
						</div>
						<hr style="border-color:#bdbdbd; background-color:#bdbdbd;">
						</c:forEach>
					</div>
					<table style="width:100%;">
						<tr>
							<td>
								<div style="text-align:left;"><a href="#ptop" accesskey="2"><span style="font-size:xx-small;">[2]▲</span></a></div>
							</td>
							<td>
								<div style="text-align:right;"><span style="font-size:xx-small;">⇒</span><s:link href="/m/newfrienddiary"><span style="font-size:xx-small;">一覧へ</span></s:link></div>
							</td>
						</tr>
					</table>
					<hr style="border-color:#bdbdbd; background-color:#bdbdbd;">
					<!--/最新日記-->
				</c:if>

				<!--ｽｹｼﾞｭｰﾙ-->
				<div style="background-color:#4a4a4a;">
					&nbsp;<s:link href="/m/schedule"><span style="color:#9999ff; font-size:xx-small;">ｽｹｼﾞｭｰﾙ</span></s:link><span style="color:#9999ff; font-size:xx-small;">${defDisptypeCalendar eq '1'?'【共有のみ】':'【全て】'}</span>
				</div>
				<div>
					<img src="/images/m/sp5.gif" ><br>
					<div style="font-size:xx-small;">
						<c:if test="${fn:length(scheduleList)>0}">
							<c:set var="scheduledate" value=""/>
						<c:forEach items="${scheduleList}" var="s" varStatus="status">
							<c:if test="${scheduledate ne s.scheduledate}">
								<c:set var="scheduledate" value="${s.scheduledate}"/>
								<c:choose>
									<c:when test="${status.first}"><span style="font-size:xx-small;">[<span style="color:#ff0000;">■</span><fmt:formatDate value="${f:date(f:h(s.scheduledate),'yyyyMMdd')}" pattern="本日の予定(M/d(E))" />]&nbsp;</span><br></c:when>
									<c:otherwise><br><span style="font-size:xx-small;">[<span style="color:#0000ff;">■</span><fmt:formatDate value="${f:date(f:h(s.scheduledate),'yyyyMMdd')}" pattern="明日の予定(M/d(E))" />]&nbsp;</span><br></c:otherwise>
								</c:choose>
							</c:if>
<c:choose>
<c:when test="${s.cid!=null}">
								<div style="color:#${s.titlecolor};">
								<c:choose>
									<c:when test="${s.entry eq 1}">
										<c:choose>
											<c:when test="${s.starttime == null and s.endtime == null}"></c:when>
											<c:when test="${s.starttime == null and s.endtime != null}">・【ｲﾍﾞﾝﾄ】～${s.endtime}</c:when>
											<c:otherwise>・【ｲﾍﾞﾝﾄ】${s.starttime}${s.endtime}</c:otherwise>
										</c:choose>
										<c:if test="${s.starttime == null and s.endtime == null}">・【ｲﾍﾞﾝﾄ】</c:if><span style="font-size:xx-small;">${s.title}</span><br>
									</c:when>
									<c:when test="${s.entry eq 2}">
										<s:link href="/m/schedule/view/${s.sno}/${s.cid}"><span style="font-size:xx-small;color:#${s.titlecolor};">
										<c:choose>
											<c:when test="${s.starttime == null and s.endtime == null}"></c:when>
											<c:when test="${s.starttime == null and s.endtime != null}">・【ｽｹｼﾞｭｰﾙ】～${s.endtime}</c:when>
											<c:otherwise>・【ｽｹｼﾞｭｰﾙ】${s.starttime}${s.endtime}</c:otherwise>
										</c:choose>
										<c:if test="${s.starttime == null and s.endtime == null}">・【ｽｹｼﾞｭｰﾙ】</c:if>${s.title}</span></s:link><br>
									</c:when>
								</c:choose></div>
</c:when>
<c:otherwise>何もありません
</c:otherwise>
</c:choose>
						</c:forEach>
						</c:if><br/><br/>
					<input type="submit" name="setting1" value="全てのｽｹｼﾞｭｰﾙを表示"/><br/>
					<input type="submit" name="setting2" value="共有中のｽｹｼﾞｭｰﾙのみ表示"/>
					</div>
				</div>
				<hr style="border-color:#bdbdbd; background-color:#bdbdbd;">
				<table style="width:100%;">
					<tr>
						<td>
							<div style="text-align:left;"><a href="#ptop" accesskey="2"><span style="font-size:xx-small;">[2]▲</span></a></div>
						</td>
						<td>
							<div style="text-align:right;"><span style="font-size:xx-small;">⇒</span><s:link href="/m/schedule"><span style="font-size:xx-small;">一覧へ</span></s:link></div>
						</td>
					</tr>
				</table>
				<hr style="border-color:#bdbdbd; background-color:#bdbdbd;">
				<!--/ｽｹｼﾞｭｰﾙ-->

				<div style="background-color:#dddddd; text-align:center;">
					<s:link href="/m/top" accesskey="0"><span style="font-size:xx-small;">[0]ﾏｲﾄｯﾌﾟ</span></s:link>
				</div>
				<%@ include file="/WEB-INF/view/m/fmenu.jsp"%>
				<!--footer-->
				<div style="text-align:left;"><a href="#ptop" name="pbtm" id="pbtm" accesskey="2"><span style="font-size:xx-small;">[2]▲</span></a></div>
				<img src="/images/m/sp5.gif" ><br>
				<%@ include file="/WEB-INF/view/m/footer.jsp"%>
				<!--/footer-->
			<!--コンテンツ終了-->
		</div>
	</div>
</s:form>
</body>
</html>