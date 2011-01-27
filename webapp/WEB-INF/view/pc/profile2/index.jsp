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
<title>[frontier] プロフィール変更</title>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/colorful.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/prototype.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/effects.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/overlay.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/popup.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/windowstate.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/color_palette.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/youtube.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/emoji_palette_base.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/emoji_palette.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier_2.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/map.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier_1.js"></script>
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/common.js"></script>
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/color_palette.css" type="text/css" />
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/emoji_palette.css" type="text/css" />
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
		<div class="mainSetup">
			<!--メイン-->
			<div class="mainSetup-main">
			
				<!--errArea01-->
				<html:errors/> 
				<!--/errArea01-->
				
				<div class="listBoxProfile01">
					<div class="listBoxProfile01Title">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp">
									<div class="ttl_name inputColStick">
										<ul class="clearfix">
											<li>プロフィール設定<span style="margin-left:5px;">(<font color="red">*</font><font class="fxS">の項目は必須</font>)</span></li>
										</ul>
									</div>
								</td>
							</tr>
						</table>
					</div>
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">
							<ul class="clearfix">
								<li class="lHead">名前<font color="red">*</font>&nbsp;(各項目全角10文字以内)</li>
								<li class="rHead">
									<select name="nameLevel">
										<option value="2" ${nameLevel eq '2'?'selected':''}>グループに公開</option>
										<option value="1" ${nameLevel eq '1'?'selected':''}>全体に公開</option>
									</select>
								</li>
							</ul>
						</div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<ul class="nameArea clearfix">
									<li>姓</li>
									<li style="margin-left:5px;"><html:text property="familyname"  size="10" name="familyname" value="${familyname}"/></li>
									<li style="margin-left:15px;">名</li>
									<li style="margin-left:5px;"><html:text property="name"  size="10" name="name" value="${name}"/></li>
								</ul>
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">
							<ul class="clearfix">
								<li class="lHead">ニックネーム<font color="red">*</font>&nbsp;(全角10文字以内)</li>
							</ul>
						</div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<html:text property="nickname"  size="20" name="nickname" value="${nickname}"/>
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">
							<ul class="clearfix">
								<li class="lHead">現住所<font color="red">*</font></li>
								<li class="rHead">
									<select name="locationLevel">
									<option value="2" ${locationLevel eq '2'?'selected':''}>グループに公開</option>
									<option value="1" ${locationLevel eq '1'?'selected':''}>全体に公開</option>
									</select>
								</li>
							</ul>
						</div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<ul class="addArea clearfix">
									<li>
										<input type=hidden name=location_change value="">
										<!-- 現住所・都道府県 -->
										<select name="residenceregion" onChange="JavaScript:ff_reload('reload');">
										<option<c:if test="${residenceregion==null || residenceregion==''}"> selected</c:if> value="">▼都道府県</option>
										<c:forEach var="e" items="${ResidenceRegionlist}"><option value="${e.regioncd}"<c:if test="${residenceregion==e.regioncd}"> selected</c:if>>${f:h(e.regionnm)}</option>
										</c:forEach>
										</select>
										<!-- 現住所・都道府県 -->
									</li>
									<li style="margin-left:10px;">
										<!-- 現住所・市区町村 -->
										<select name="residencecity" >
										<option<c:if test="${residencecity==null || residencecity==''}"> selected</c:if> value="">▼市郡</option>
										<c:forEach var="e" items="${ResidenceCitieslist}"><option value="${e.citycd}"<c:if test="${residencecity==e.citycd}"> selected</c:if>>${f:h(e.cityname)}</option>
										</c:forEach>
										</select>
										<!-- 現住所・市区町村 -->
									</li>
									<li style="margin-left:10px;"><font class="fxS">※市区町村の項目は任意です。</font></li>
								</ul>
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">
							<ul class="clearfix">
								<li class="lHead">性別<font color="red">*</font></li>
								<li class="rHead">
									<select name="genderLevel">
									<option value="2" ${genderLevel eq '2'?'selected':''}>グループに公開</option>
									<option value="1" ${genderLevel eq '1'?'selected':''}>全体に公開</option>
									</select>
								</li>
							</ul>
						</div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<ul class="genderArea clearfix">
									<li>
										<input name="gender" type="radio" value="1" id="male" <c:if test="${gender==null || gender=='' || gender =='1'}"> checked</c:if> />
										<label>男</label>
									</li>
									<li>
										<input name="gender" type="radio" value="2" id="female" <c:if test="${gender =='2'}"> checked</c:if> />
										<label>女</label>
									</li>
								</ul>
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">
							<ul class="clearfix">
								<li class="lHead">誕生日<font color="red">*</font></li>
								<li class="rHead">
									<select name="birthdayLevel">
									<option value="9" ${birthdayLevel eq '9'?'selected':''}>非公開</option>
									<option value="2" ${birthdayLevel eq '2'?'selected':''}>グループに公開</option>
									<option value="1" ${birthdayLevel eq '1'?'selected':''}>全体に公開</option>
									</select>
								</li>
							</ul>
						</div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<ul class="birthArea clearfix">
									<li>
										<select name="monthofbirth">
											<option value=""<c:if test="${dayofbirth==null || dayofbirth==''}"> selected</c:if>>--</option>
											<c:forEach begin="01" end="12" var="i">
												<option value="${i}" <c:if test="${monthofbirth==i}"> selected</c:if>><c:if test="${i<10}">0</c:if>${i}</option>
											</c:forEach>
										</select>月
									</li>
									<li style="margin-left:10px;">
										<select name="dayofbirth">
											<option value=""<c:if test="${dayofbirth==null || dayofbirth==''}"> selected</c:if>>--</option>
											<c:forEach begin="1" end="31" var="i">
												<option value="${i}" <c:if test="${dayofbirth==i}"> selected</c:if>><c:if test="${i<10}">0</c:if>${i}</option>
											</c:forEach>
										</select>日
									</li>
								</ul>
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">
							<ul class="clearfix">
								<li class="lHead">生まれた年<font color="red">*</font></li>
								<li class="rHead">
									<select name="ageLevel">
									<option value="9" ${ageLevel eq '9'?'selected':''}>非公開</option>
									<option value="2" ${ageLevel eq '2'?'selected':''}>グループに公開</option>
									<option value="1" ${ageLevel eq '1'?'selected':''}>全体に公開</option>
									</select>
								</li>
							</ul>
						</div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<ul class="birthArea clearfix">
									<li>
										<select name="yearofbirth">
											<option value="" ${yearofbirth eq ''?'selected':''}>----</option>
											<c:forEach begin="${appDefDto.FP_MY_CALENDAR_START_PGMAX}" end="${f:h(endYear)}" var="i">
											<xsl:sort select="price div unit" data-type="number" order="descending"/> 
												<option value="${i}" <c:if test="${yearofbirth==i}"> selected</c:if>>${i}</option>
											</c:forEach>
										</select>年
									</li>
								</ul>
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">
							<ul class="clearfix">
								<li class="lHead">血液型</li>
							</ul>
						</div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<ul class="bloodArea clearfix">
									<li>
										<select name="bloodtype">
										<option value="0" ${bloodtype eq ''?'selected':''}>▼選択</option>
										<option value="1" ${bloodtype eq '1'?'selected':''}>A型</option>
										<option value="2" ${bloodtype eq '2'?'selected':''}>B型</option>
										<option value="3" ${bloodtype eq '3'?'selected':''}>O型</option>
										<option value="4" ${bloodtype eq '4'?'selected':''}>AB型</option>
										</select>
									</li>
								</ul>
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">
							<ul class="clearfix">
								<li class="lHead">出身地</li>
								<li class="rHead">
									<select name="hometownLevel">
									<option value="2" ${hometownLevel eq '2'?'selected':''}>グループに公開</option>
									<option value="1" ${hometownLevel eq '1'?'selected':''}>全体に公開</option>
									</select>
								</li>
							</ul>
						</div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<ul class="addArea clearfix">
									<li>
										<input type="hidden" name="hometown_change" value="">
										<!-- 出身地・都道府県 -->
										<select name="hometownregion" onChange="JavaScript:ff_reload('reload');">
										<option<c:if test="${hometownregion==null || hometownregion==''}"> selected</c:if> value="">▼都道府県</option>
										<c:forEach var="e" items="${HometownRegionlist}"><option value="${e.regioncd}"<c:if test="${hometownregion==e.regioncd}"> selected</c:if>>${f:h(e.regionnm)}</option>
										</c:forEach>
										</select>
										<!-- 出身地・都道府県 -->
									</li>
									<li style="margin-left:10px;">
										<!-- 出身地・市区町村 -->
										<select name="hometowncity" >
										<option<c:if test="${hometowncity==null || hometowncity==''}"> selected</c:if> value="">▼市郡</option>
										<c:forEach var="e" items="${HometownCitieslist}"><option value="${e.citycd}"<c:if test="${hometowncity==e.citycd}"> selected</c:if>>${f:h(e.cityname)}</option>
										</c:forEach>
										</select>
										<!-- 出身地・市区町村 -->
									</li>
								</ul>
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">
							<ul class="clearfix">
								<li class="lHead">趣味<font color="red">*</font></li>
							</ul>
						</div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<ul class="hobbyArea clearfix">
									<!-- 趣味 -->
									<c:forEach var="e" items="${interestlist}">
									<li><input type="checkbox" name="interest1" id="hobby1" value="${e.itemcd}" <c:forEach var="v" items="${interest1}"><c:if test="${v==e.itemcd}">checked="checked"</c:if></c:forEach> /><label>${f:h(e.itemname)}</label></li>
									</c:forEach>
									<!-- 趣味 -->
								</ul>
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">
							<ul class="clearfix">
								<li class="lHead">職業</li>
								<li class="rHead">
									<select name="jobLevel">
									<option value="2" ${jobLevel eq '2'?'selected':''}>グループに公開</option>
									<option value="1" ${jobLevel eq '1'?'selected':''}>全体に公開</option>
									</select>
								</li>
							</ul>
						</div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<ul class="jobArea clearfix">
									<li>
										<!-- 職業 -->
										<select name="occupation" style="width:150px;">
										<option<c:if test="${occupation==null || occupation==''}"> selected</c:if> value="0">▼選択</option>
										<c:forEach var="e" items="${occupationlist}"><option value="${e.itemcd}"<c:if test="${occupation==e.itemcd}"> selected</c:if>>${f:h(e.itemname)}</option>
										</c:forEach>
										</select>
										<!-- 職業 -->
									</li>
								</ul>
							</div>
						</div>
					</div>
					<!--/listBoxHead-->

					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">
							<ul class="clearfix">
								<li class="lHead">自己紹介<font color="red">*</font>&nbsp;(全角10000文字以内)</li>
							</ul>
						</div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<ul class="clearfix">
									<li>
										<a href="javascript:void(0)" onclick="openEmojiPalette(document.getElementById('aboutme'), event); return false;" title="絵文字"><img src="/images/insert_icon001.gif" width="22" height="22" alt="絵文字" style="display:inline;" /></a>
										<%@include file="/WEB-INF/view/common/emojiPalette.jsp" %>
									</li>
									<li style="margin-left:5px;">
										<html:textarea rows="15" styleId="aboutme" property="aboutme" value="${aboutme}"></html:textarea>
									</li>
								</ul>
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">
							<ul class="clearfix">
								<li class="lHead">好きな<!-- 好きなジャンル１ -->
									<select name="favgenre1">
									<option<c:if test="${favgenre1==null || favgenre1==''}"> selected</c:if> value="0">▼選択</option>
									<c:forEach var="e" items="${FavGenrelist}"><option value="${e.itemcd}"<c:if test="${favgenre1==e.itemcd}"> selected</c:if>>${f:h(e.itemname)}</option>
									</c:forEach>
									</select>
									<!-- 好きなジャンル１-->
									&nbsp;(全角100文字以内)
								</li>
							</ul>
						</div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<html:text property="favcontents1"  size="50" name="favcontents1" value="${favcontents1}"/>
								<label style="margin-left:10px;"><font class="fxS">※上の項目を選択して、下の項目にご記入ください。</font></label>
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">
							<ul class="clearfix">
								<li class="lHead">好きな<!-- 好きなジャンル２ -->
									<select name="favgenre2">
									<option<c:if test="${favgenre2==null || favgenre2==''}"> selected</c:if> value="0">▼選択</option>
									<c:forEach var="e" items="${FavGenrelist}"><option value="${e.itemcd}"<c:if test="${favgenre2==e.itemcd}"> selected</c:if>>${f:h(e.itemname)}</option>
									</c:forEach>
									</select>
									<!-- 好きなジャンル２-->
									&nbsp;(全角100文字以内)
								</li>
							</ul>
						</div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<html:text property="favcontents2"  size="50" name="favcontents2" value="${favcontents2}"/>
								<label style="margin-left:10px;"><font class="fxS">※上の項目を選択して、下の項目にご記入ください。</font></label>
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">
							<ul class="clearfix">
								<li class="lHead">好きな<!-- 好きなジャンル３ -->
									<select name="favgenre3">
									<option<c:if test="${favgenre3==null || favgenre3==''}"> selected</c:if> value="0">▼選択</option>
									<c:forEach var="e" items="${FavGenrelist}"><option value="${e.itemcd}"<c:if test="${favgenre3==e.itemcd}"> selected</c:if>>${f:h(e.itemname)}</option>
									</c:forEach>
									</select>
									<!-- 好きなジャンル３-->
									&nbsp;(全角100文字以内)
								</li>
							</ul>
						</div>
						<div class="bodyArea">
							<div class="bodyinputArea">
								<html:text property="favcontents3"  size="50" name="favcontents3" value="${favcontents3}"/>
								<label style="margin-left:10px;"><font class="fxS">※上の項目を選択して、下の項目にご記入ください。</font></label>
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
					
					<div class="listBoxHead listBoxHeadline clearfix">
						<div class="ttlHeadArea">
							<div class="btnArea">
								<input type="hidden" name="submit_hidden" value="main" />
								<input type="submit" name="touroku" value="この内容で設定" />
							</div>
						</div>
					</div>
					<!--/listBoxHead-->
					
				</div>
				<!--/listBoxProfile01-->
			</div>
			<!--/mainEvent-main-->
		</div>
		<!--/mainCommunity-->
	</div>
	<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/sp10.png" alt=""/>
	<!--/contents-->
	<!-- フッター -->
	<%@ include file="/WEB-INF/view/pc/ffooter.jsp"%>
	<!-- フッター -->
	 <!--/footer-->
</div>
<!--/container-->
</s:form>
</body>
</html>
