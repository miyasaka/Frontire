<%@ page language="java" contentType="text/html; charset=windows-31J"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta name="robots" content="nofollow,noindex"     />
<meta name="Slurp" content="NOYDIR" />
<title>[frontier] <c:if test="${!vUser}">${f:h(vNickname)} | </c:if>${f:h(coverResults[0]['title'])}</title>

<script language="javascript" type="text/javascript" src="/static/js/prototype.js"></script>
<script language="javascript" type="text/javascript" src="/static/js/frontier_1.js"></script>

<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/assort.css" type="text/css" />
<script language="javascript">
<!--
function insert_photo(form) { 
    var src = window.opener.document.pc_entdiaryActionForm.diaryBody;
    var res = "";
    var e = form.elements;
    for(var i=0;i<e.length;++i) {
        if (e[i].type == 'select-one' && e[i].selectedIndex != 0)
            res += '[p:' +e[i].value+ ']' + "\n";
    }
    src.focus();
    switch (get_mode()) {
        case 1: 
            var str1 = src.value.substring(0, src.selectionStart);
            var str2 = src.value.substring(src.selectionEnd, src.value.length);
            src.value = str1 + res + str2;
            break;
        case 2:
            window.opener.document.selection.createRange().text = res;
            break;
        case 4: 
            src.value += res;
    }
    window.close();
}
//-->
</script>
</head>
<body>
<form action="">
<div class="sp_main">
	<div align="center">
		<div class="sp_sub">
			<div align="center">
				<!-- フォト一覧 -->
			<c:choose>
			<c:when test="${photoResultscnt eq 0}">
			データがありません
			</c:when>
			<c:otherwise>

				<c:set var="row" value="3" />
				<table class="sp_table">
					<tr>
						<td colspan="${f:h(row)}" style="border:solid 0px;">
							<table class="sp_pging_table">
								<tr>
									<td class="sp_pging_td1">&nbsp;&nbsp;<c:if test="${viewOffset>0}"><s:link href="/pc/photoalbum/viewPrepg/" title="前の${appDefDto.FP_MY_PHOTO_PGMAX}件">&lt;&lt;前の${appDefDto.FP_MY_PHOTO_PGMAX}件</s:link></c:if></td>
									<td class="sp_pging_td2">${f:h(vNickname)}のフォトアルバム</td>
									<td class="sp_pging_td3"><c:if test="${photoResultscnt>(viewOffset + fn:length(photoResults))}"><s:link href="/pc/photoalbum/viewNxtpg/" title="次の${appDefDto.FP_MY_PHOTO_PGMAX}件">次の${appDefDto.FP_MY_PHOTO_PGMAX}件&gt;&gt;</s:link></c:if>&nbsp;&nbsp;</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td colspan="${f:h(row)}" class="sp_td1">
							<table>
								<tr>
									<td valign="middle">
										<div class="sp_div1 myColStick">フォト一覧(${viewOffset+1}件～${viewOffset+fn:length(photoResults)}件/${photoResultscnt}件)</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				<!-- ループ -->
			<c:forEach var="e" items="${photoResults}" varStatus="status">
				<c:set var="picURL" value="${appDefDto.FP_CMN_CONTENTS_ROOT}/${fn:replace(e.pic,'dir','pic180')}"/>
				<c:set var="picName" value="${f:h(e.picname)}"/>
				<c:set var="count" value="${status.count}"/>
				<c:set var="albumNo" value="${f:h(e.ano)}"/>
				<c:set var="photoNo" value="${f:h(e.fno)}"/>
				<c:if test="${(status.count % row) eq 1}">
					<tr>
				</c:if>
						<td align="center" valign="middle" class="sp_td2">
							${f:h(e.picname)}
							<img src="${picURL}" alt="${f:h(e.picnameorg)}"/>
							<div>掲載サイズを選択<br/>
							<select id="num_${count}_m">
								<option value="">▼選択</option>
								<option value="${albumNo}:${photoNo}:480">480×480</option>
								<option value="${albumNo}:${photoNo}:240">240×240</option>
								<option value="${albumNo}:${photoNo}:180">180×180</option>
								<option value="${albumNo}:${photoNo}:120">120×120</option>
								<option value="${albumNo}:${photoNo}:76">76×76</option>
								<option value="${albumNo}:${photoNo}:60">60×60</option>
								<option value="${albumNo}:${photoNo}:42">42×42</option>
							</select></div>
						</td>
						<c:if test="${status.last || (viewOffset + status.count) == photoResultscnt}">
							<c:if test="${(fn:length(photoResults) % row) != 0}">
								<c:forEach begin="1" end="${row - fn:length(photoResults) % row}" varStatus="i2">
									<td class="sp_td2">&nbsp;</td>
								</c:forEach>
							</c:if>
					</tr>
						</c:if>
			</c:forEach>
				<!-- ループ -->
					<tr>
						<td colspan="${f:h(row)}" class="sp_td3">
							<input type="button" class="sp_button" value="掲載" onClick="insert_photo(this.form)"/>
						</td>
					</tr>
					<c:if test="${photoResultscnt>appDefDto.FP_MY_PHOTO_PGMAX}">
					<tr>
						<td colspan="${f:h(row)}" style="border:solid 0px;">
							<table class="sp_pging_table">
								<tr>
									<td class="sp_pging_td4">&nbsp;&nbsp;<c:if test="${viewOffset>0}"><s:link href="/pc/photoalbum/viewPrepg/" title="前の${appDefDto.FP_MY_PHOTO_PGMAX}件">&lt;&lt;前の${appDefDto.FP_MY_PHOTO_PGMAX}件</s:link></c:if></td>
									<%-- <td class="sp_pging_td2">&nbsp;</td> --%>
									<td class="sp_pging_td5"><c:if test="${photoResultscnt>(viewOffset + fn:length(photoResults))}"><s:link href="/pc/photoalbum/viewNxtpg/" title="次の${appDefDto.FP_MY_PHOTO_PGMAX}件">次の${appDefDto.FP_MY_PHOTO_PGMAX}件&gt;&gt;</s:link></c:if>&nbsp;&nbsp;</td>
								</tr>
							</table>
						</td>
					</tr>
					</c:if>
				</table>
				<div style="padding:5px;text-align:center;">
					<s:link href="/pc/photoalbum/list/" title="一覧に戻る">一覧に戻る</s:link>&nbsp;&nbsp;&nbsp;&nbsp;
					<a href='javascript:window.close()' title="CLOSE">CLOSE</a><br>
				</div>
				</c:otherwise>
				</c:choose>
				<!-- フォト一覧 -->
			</div>
		</div>
	</div>
</div>
</form>
</body>
</html>
