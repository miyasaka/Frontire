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
			<div style="background-color:#afafaf;">
				&nbsp;<span style="color:#0000ff; font-size:xx-small;">●</span>&nbsp;<span style="font-size:xx-small;">F Shout</span>
			</div>
			<div style="text-align:right;"><a href="#pbtm" name="ptop" id="ptop" accesskey="8"><span style="font-size:xx-small;">[8]▼</span></a></div>
			<!--/header-->
			<!--コンテンツ開始-->
				<!--F Shout一覧-->
				<div style="background-color:#dddddd; text-align:center;">
					<s:link href="/m/top" accesskey="0"><span style="font-size:xx-small;">[0]ﾏｲﾄｯﾌﾟ</span></s:link>
<c:if test="${pgcnt>0}">
						<s:link href="/m/fshout/pre" accesskey="4"><span style="font-size:xx-small;">[4]前の${appDefDto.FP_MY_M_FSHOUTLIST_PGMAX}件</span></s:link>
</c:if>
<c:if test="${(offset + appDefDto.FP_MY_M_FSHOUTLIST_PGMAX) < fsCnt}">
						<s:link href="/m/fshout/nxt" accesskey="6"><span style="font-size:xx-small;">[6]次の${appDefDto.FP_MY_M_FSHOUTLIST_PGMAX}件</span></s:link>
</c:if>
				</div>
				<img src="/images/m/sp5.gif" /><br />

<c:if test="${fslistpgType==1 || fslistpgType==2}">
				<!-- F Shout一覧&自分宛 -->
				<div style="background-color:#dddddd;">
					<s:link href="/m/fshout/list"><span style="font-size:xx-small;">F Shout一覧</span></s:link>&nbsp;|&nbsp;<s:link href="/m/fshout/my"><span style="font-size:xx-small;">自分宛</span></s:link>
				</div>
</c:if>
				<div style="background-color:#4a4a4a;">
					<span style="color:#ffffff; font-size:xx-small;">&nbsp;${f:h(vNickname)}<c:if test="${userInfoDto.memberId!=mid}">さん</c:if>の
<c:choose>
<c:when test="${fslistpgType==0 || fslistpgType==1}">F Shout一覧</c:when>
<c:otherwise>自分宛一覧</c:otherwise>
</c:choose>
<c:if test="${fsCnt>0}">
&nbsp;(${offset + 1}～
<c:choose>
<c:when test="${(offset + appDefDto.FP_MY_M_FSHOUTLIST_PGMAX) < fsCnt}">${offset + appDefDto.FP_MY_M_FSHOUTLIST_PGMAX}</c:when>
<c:otherwise>${fsCnt}</c:otherwise>
</c:choose>
/${fsCnt}件)
</c:if>
</span>
				</div>
<%-- F Shout一覧、もしくは自分宛一覧だったら入力フィールドを表示 --%>
<c:if test="${fslistpgType==1 || fslistpgType==2}">
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
					<textarea name="fscomment" rows="5" style="width:100%;">${f:h(fscomment)}</textarea><br/>
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
<%-- メンバー画像の表示 --%>
<c:choose>
<c:when test="${f:h(e.photo) != '' && f:h(e.photo) != null}"><img src="<c:if test="${e.membertype==0}">${appDefDto.FP_CMN_CONTENTS_ROOT}</c:if>${fn:replace(e.photo,'dir','pic42')}"/></c:when>
<c:otherwise><img src="/images/m/noimg42.gif"/></c:otherwise>
</c:choose>
<%-- メンバータイプチェック、他Frontierならばfrontierdomainを表示 --%>
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
</c:forEach>
					<!-- ここまでFShout一覧 -->
</c:when>
<c:otherwise><hr style="border-color:#bdbdbd; background-color:#bdbdbd;">まだ何もありません。</c:otherwise>
</c:choose>

					<hr style="border-color:#bdbdbd; background-color:#bdbdbd;">
				</div>
				<!--/F Shout一覧-->

				<div style="background-color:#dddddd; text-align:center;">
					<s:link href="/m/top"><span style="font-size:xx-small;">[0]ﾏｲﾄｯﾌﾟ</span></s:link>
<c:if test="${pgcnt>0}">
						<s:link href="/m/fshout/pre"><span style="font-size:xx-small;">[4]前の${appDefDto.FP_MY_M_FSHOUTLIST_PGMAX}件</span></s:link>
</c:if>
<c:if test="${(offset + appDefDto.FP_MY_M_FSHOUTLIST_PGMAX) < fsCnt}">
						<s:link href="/m/fshout/nxt"><span style="font-size:xx-small;">[6]次の${appDefDto.FP_MY_M_FSHOUTLIST_PGMAX}件</span></s:link>
</c:if>				</div>

<img src="/images/m/sp5.gif" ><br>
<div style="text-align:left;">
<span style="font-size:xx-small;">◆ｻﾎﾟｰﾄﾒﾆｭｰ</span><br>
├<s:link href="/m/fshout/list"><span style="font-size:xx-small;">F Shout一覧へ</span></s:link><br>
├<s:link href="/m/fshout/my"><span style="font-size:xx-small;">F Shout自分宛へ</span></s:link><br>
└<s:link href="/m/logout"><span style="font-size:xx-small;">ﾛｸﾞｱｳﾄ</span></s:link><br>
</div>
<hr style="border-color:#bdbdbd; background-color:#bdbdbd;">
<img src="/images/m/sp5.gif" ><br>
				<!--footer-->
				<div style="text-align:right;"><a href="#ptop" name="pbtm" id="pbtm" accesskey="2"><span style="font-size:xx-small;">[2]▲</span></a></div>
				<img src="/images/m/sp5.gif" /><br />

<div style="background-color:#100800; text-align:center;">

				<%@ include file="/WEB-INF/view/m/footer.jsp"%>
				<!--/footer-->
			<!--コンテンツ終了-->
		</div>
	</div>
</s:form>
</body>
</html>