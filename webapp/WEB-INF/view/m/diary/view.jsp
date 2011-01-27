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
<title>[frontier]日記閲覧</title>
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
<title>[frontier]日記閲覧</title>
</head>
<body style="width:350px;">
<s:form>
<%-- ロケール情報を設定 --%>
<fmt:setLocale value="ja-JP" />
	<div>
		<div>
		
			<!--header-->
			<div style="background-color:#afafaf;">
				&nbsp;<span style="color:#debe71; font-size:xx-small">●</span>&nbsp;<span style="font-size:xx-small;">日記閲覧</span>
			</div>
			<div style="text-align:right;"><a href="#pbtm" name="ptop" id="ptop" accesskey="8"><span style="font-size:xx-small;">[8]▼</span></a></div>
			<!--/header-->
		
		
			<!--コンテンツ開始-->
				
				
				
				<!--日記内容-->
				<div style="background-color:#dddddd; text-align:center;">
					<s:link href="/m/top" accesskey="0"><span style="font-size:xx-small;">[0]ﾏｲﾄｯﾌﾟ</span></s:link><c:if test="${preDiaryId != null}"><s:link href="/m/diary/preDate/" accesskey="4"><span style="font-size:xx-small;">[4]前の日記</span></s:link></c:if>&nbsp;<c:if test="${nextDiaryId != null}"><s:link href="/m/diary/nextDate/" accesskey="6"><span style="font-size:xx-small;">[6]次の日記</span></s:link></c:if>&nbsp;
				</div>
				<div style="color:#ff0000;font-size:xx-small;"><html:errors/></div>
				<img src="/images/m/sp5.gif" ><br>
				<div style="background-color:#4a4a4a;">
					<span style="color:#ffffff; font-size:xx-small;">&nbsp;${f:h(viewResults[0]['title']) }</span>
				</div>
				<div>
					<img src="/images/m/sp5.gif" ><br>
					<span style="font-size:xx-small;">[<fmt:formatDate value="${f:date(f:h(viewResults[0]['entdate']),'yyyyMMddHHmm')}" pattern="yy/MM/dd HH:mm" />]&nbsp;${f:h(viewResults[0]['nickname'])}</span><br>
					<img src="/images/m/sp5.gif" ><br>
						<c:if test="${viewResults[0]['pic1']!=null||viewResults[0]['pic2']!=null||viewResults[0]['pic3']!=null}">
							<div>
									<c:if test="${viewResults[0]['pic1']!=null}">
										<a href="${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(viewResults[0]['pic1'],'dir','pic180')}"><span style="font-size:xx-small;">[画像1]</span></a>
									</c:if>
									<c:if test="${viewResults[0]['pic2']!=null}">
										<a href="${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(viewResults[0]['pic2'],'dir','pic180')}"><span style="font-size:xx-small;">[画像2]</span></a>
									</c:if>
									<c:if test="${viewResults[0]['pic3']!=null}">
										<a href="${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(viewResults[0]['pic3'],'dir','pic180')}"><span style="font-size:xx-small;">[画像3]</span></a>
									</c:if>
							</div>
						</c:if>
					<img src="/images/m/sp5.gif" ><br>
					<span style="font-size:xx-small;">${f:br(viewResults[0]['viewComment']) }</span>
				</div>
				<!--/日記内容-->
				
				<hr style="border-color:#bdbdbd; background-color:#bdbdbd;">
				
				<!--コメント-->
				<div>
					<div>
						<span style="font-size:xx-small;">ｺﾒﾝﾄ&nbsp;${f:h(cntcomment)}件</span>
					</div>
				<c:forEach var="e" items="${viewResults}" begin="1" varStatus="status">
					<c:if test="${status.first}"><c:set var="delAuth" value=""/></c:if>
					<c:choose>
						<c:when test="${(status.count % 2) eq 1}"><c:set var="color" value="#e5e5e5;" /></c:when>
						<c:otherwise><c:set var="color" value="#f0f0f0;" /></c:otherwise>
					</c:choose>
						<div style="background-color:${color}">
							<div>
								<span style="font-size:xx-small;">
									<!--<c:if test="${vUser}"><c:set var="delAuth" value="1"/><input type="checkbox" name="checkCommentNo" value="${f:h(e.comno)}" /></c:if>
									<c:if test="${!vUser and e.mid eq userInfoDto.memberId}"><c:set var="delAuth" value="1"/><input type="checkbox" name="checkCommentNo" value="${f:h(e.comno)}" /></c:if>-->
										No.<fmt:formatNumber value="${e.comno}" minIntegerDigits="3"/>&nbsp;&nbsp;<fmt:formatDate value="${f:date(f:h(e.entdate),'yyyyMMddHHmm')}" pattern="yy/MM/dd HH:mm" /><br>
									<c:choose>
										<c:when test="${e.mid eq userInfoDto.memberId}">
											${f:h(e.nickname)}
										</c:when>
										<c:otherwise>
											<c:if test="${e.status eq 1}">
												<c:choose>
													<c:when test="${(f:h(e.membertype)) eq '0' or (f:h(e.membertype)) eq '1'}">
														${f:h(e.nickname)}
													</c:when>
													<c:otherwise>
														<c:choose><c:when test="${(f:h(e.guestName))==''}">名無しさん</c:when><c:otherwise>${f:h(e.guestName)}</c:otherwise></c:choose>
													</c:otherwise>
												</c:choose>
											</c:if>
										</c:otherwise>
									</c:choose>
								</span>
							</div>
							<div>
									<c:if test="${e.pic1!=null||e.pic2!=null||e.pic3!=null}">
										<c:if test="${e.pic1!=null && e.pic1!=''}">
											<a href="${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(e.pic1,'dir','pic180')}"><span style="font-size:xx-small;">[画像1]</span></a>
										</c:if>
										<c:if test="${e.pic2!=null && e.pic2!=''}">
											<a href="${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(e.pic2,'dir','pic180')}"><span style="font-size:xx-small;">[画像2]</span></a>
										</c:if>
										<c:if test="${e.pic3!=null && e.pic3!=''}">
											<a href="${appDefDto.FP_CMN_CONTENTS_ROOT}${fn:replace(e.pic3,'dir','pic180')}"><span style="font-size:xx-small;">[画像3]</span></a>
										</c:if><br/>
									</c:if><br/>
								<span style="font-size:xx-small;">
									${f:br(e.viewComment)}
								</span>
							</div>
						</div>
						<c:if test="${status.last}">
							<c:if test="${vUser}">
							</c:if>
						</c:if>

				</c:forEach>
				</div>
				<!--/コメント-->
<!--  
<c:if test="${delAuth eq 1}">
				<hr style="border-color:#7d7d7d; background-color:#7d7d7d;">
				<div style="text-align:center;">
					<input type="submit" name="delComment" class="formBt01" value="ﾁｪｯｸしたｺﾒﾝﾄを削除" /><html:hidden property="diaryId" value="${diaryId}"/><html:hidden property="mid" value="${mid}"/>
					<hr style="border-color:#7d7d7d; background-color:#7d7d7d;">
				</div>
</c:if>
-->
				<div style="text-align:center;font-size:xx-small;">
					<html:textarea rows="10" cols="30" styleId="diaryBody" property="comment" value="${comment}"></html:textarea>
					<br>
					<input type="submit" value="ｺﾒﾝﾄを入力" name="comConfirm"/>
				</div>
				<hr style="border-color:#bdbdbd; background-color:#bdbdbd;">

				<div style="background-color:#dddddd; text-align:center;">
					<s:link href="/m/top" accesskey="0"><span style="font-size:xx-small;">[0]ﾏｲﾄｯﾌﾟ</span></s:link><c:if test="${preDiaryId != null}"><s:link href="/m/diary/preDate/" accesskey="4"><span style="font-size:xx-small;">[4]前の日記</span></s:link></c:if>&nbsp;<c:if test="${nextDiaryId != null}"><s:link href="/m/diary/nextDate/" accesskey="6"><span style="font-size:xx-small;">[6]次の日記</span></s:link></c:if>&nbsp;
				</div>
				<img src="/images/m/sp5.gif" ><br>
				<div style="text-align:left;">
					<span style="font-size:xx-small;">◆ｻﾎﾟｰﾄﾒﾆｭｰ</span><br>
					<span style="font-size:xx-small;">├</span><c:choose><c:when test="${vUser}"><s:link href="/m/diary/list/${userInfoDto.memberId}"><span style="font-size:xx-small;">日記一覧へ</span></s:link></c:when><c:otherwise><s:link href="/m/newfrienddiary"><span style="font-size:xx-small;">日記一覧へ</span></s:link></c:otherwise></c:choose><br>
					<!--
					├<a href="csns01.html">日記を編集する</a><br>
					├<a href="csns01.html">日記を削除する</a><br>
					├<a href="csns01.html">ｺﾒﾝﾄを書き込む</a><br>
					-->
					<span style="font-size:xx-small;">└</span><s:link href="/m/logout"><span style="font-size:xx-small;">ﾛｸﾞｱｳﾄ</span></s:link><br>
				</div>
				<hr style="border-color:#bdbdbd; background-color:#bdbdbd;">
				<!--footer-->
				<div style="text-align:right;"><a href="#ptop" name="pbtm" id="pbtm" accesskey="2"><span style="font-size:xx-small;">[2]▲</span></a></div>
				<img src="/images/m/sp5.gif" ><br>
				<%@ include file="/WEB-INF/view/m/footer.jsp"%>
				<!--/footer-->
			<!--コンテンツ終了-->
		</div>
	</div>
</s:form>
</body>
</html>