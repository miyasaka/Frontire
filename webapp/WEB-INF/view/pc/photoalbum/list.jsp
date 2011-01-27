<%@ page language="java" contentType="text/html; charset=windows-31J"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta name="robots" content="nofollow,noindex"     />
<title>[frontier] <c:if test="${!vUser}">${f:h(vNickname)}の</c:if>フォトアルバム</title>
<script language="javascript" type="text/javascript" src="/static/js/prototype.js"></script>
<script language="javascript" type="text/javascript" src="/static/js/frontier_1.js"></script>

<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/assort.css" type="text/css" />

</head>

<body>
<form action="">
<div class="sp_main">
	<div align="center">
		<div class="sp_sub">
			<div align="center">
				<!-- フォトアルバム一覧 -->
				<table class="sp_table">
					<tr>
						<td colspan="2" style="border:solid 0px;">
							<table class="sp_pging_table">
								<tr>
									<td class="sp_pging_td1">&nbsp;&nbsp;<c:if test="${offset>0}"><s:link href="/pc/photoalbum/prepg" title="前の10件">&lt;&lt;前の10件</s:link></c:if></td>
									<td class="sp_pging_td2">${f:h(vNickname)}のフォトアルバム</td>
									<td class="sp_pging_td3"><c:if test="${resultscnt>(offset + fn:length(results))}"><s:link href="/pc/photoalbum/nxtpg" title="次の10件">次の10件&gt;&gt;</s:link></c:if>&nbsp;&nbsp;</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td colspan="2" class="sp_td1">
							<table>
								<tr>
									<td valign="middle">
										<div class="sp_div1 myColStick">フォトアルバム一覧<c:if test="${resultscnt>0}">&nbsp;(${offset+1}件～${offset+fn:length(results)}件/${resultscnt}件)</c:if></div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<%-- 0件パターン --%>
					<c:if test="${resultscnt==0}">
						<tr>
							<td>
								<div style="border:0;text-align:center;padding:30px;">
									<font color="#000000">フォトアルバムはまだありません。</font>
								</div>
							</td>
						</tr>
					</c:if>
					<%--/0件パターン --%>
					<!-- ループ -->
					<c:forEach var="e" items="${results}">

					<tr>
						<td align="center" rowspan="2" valign="middle" width="250" class="sp_td2">
							<c:choose>
								<c:when test="${e.pic eq null}">
									<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/noimg180.gif" />
								</c:when>
								<c:otherwise>
										<img src="${appDefDto.FP_CMN_CONTENTS_ROOT}/${fn:replace(e.pic,'dir','pic240')}" alt="${f:h(e.picname)}"/>
								</c:otherwise>
							</c:choose>
						</td>
						<td align="center" valign="top" width="350" class="sp_td2">
							<div class="sp_div2"><font style="font-weight:bold;">タイトル</font>&nbsp;：&nbsp;${f:h(e.title)}</div>
							<div class="sp_div2"><font style="font-weight:bold;">説明</font>&nbsp;：<br/>
							${f:br(e.cmnthtml)}
							</div>
							<div class="sp_div3"><font style="font-weight:bold;">掲載枚数</font>&nbsp;：&nbsp;${e.cnt2}枚</div>
						</td>
					</tr>
					<tr>
						<td align="center" valign="middle" height="20" class="sp_td2">
							<s:link href="/pc/photoalbum/view/${f:h(e.ano)}" title="写真を選択する">写真を選択する</s:link>
						</td>
					</tr>
					</c:forEach>
					<c:if test="${resultscnt>appDefDto.FP_MY_PHOTOALBUMLIST_PGMAX}">
					<tr>
						<td colspan="2" style="border:solid 0px;">
							<table class="sp_pging_table">
								<tr>
									<td class="sp_pging_td1">&nbsp;&nbsp;<c:if test="${offset>0}"><s:link href="/pc/photoalbum/prepg" title="前の10件">&lt;&lt;前の10件</s:link></c:if></td>
									<td class="sp_pging_td2">&nbsp;</td>
									<td class="sp_pging_td3"><c:if test="${resultscnt>(offset + fn:length(results))}"><s:link href="/pc/photoalbum/nxtpg" title="次の10件">次の10件&gt;&gt;</s:link></c:if>&nbsp;&nbsp;</td>
								</tr>
							</table>
						</td>
					</tr>
					</c:if>
					<!-- ループ -->
				</table>

				<div style="padding:5px;text-align:center;">
					<a href='javascript:window.close()' title="CLOSE">CLOSE</a><br>
				</div>
				<!-- フォトアルバム一覧 -->
			</div>
		</div>
	</div>
</div>
</form>
</body>
</html>
