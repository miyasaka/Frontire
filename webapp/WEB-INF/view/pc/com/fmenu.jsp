<%@ page pageEncoding="UTF-8" %>
<!-- コミュニティメニューバー -->
<div class="menu_button_area">
<c:choose>
<c:when test="${communityDto.makabletopic=='0'}">
<%/*
	//===========================
	// 【makabletopic】
	//    0 : 非会員
	//    1 : 管理人
	//    2 : 会員(作成権限あり)
	//    3 : 会員(作成権限なし)
	//===========================
*/%>
<%/* 非会員用メニューバー */%>
	<div class="menu_button_out">
		<div class="menu_button" onMouseOver="ff_mouse_on01(this);" onMouseOut="ff_mouse_off01(this);" onClick="ff_mouse_click('/frontier/pc/com/top/${f:h(cid)}');" title="トップ">
			<div class="menu_button_in">トップ</div>
		</div>
	</div>
	<div class="menu_button_space"></div>
	<div class="menu_button_out">
		<div class="menu_button" onMouseOver="ff_mouse_on01(this);" onMouseOut="ff_mouse_off01(this);" onClick="ff_mouse_click('/frontier/pc/com/topic/${f:h(cid)}');" title="トピック">
			<div class="menu_button_in">トピック</div>
		</div>
	</div>
	<div class="menu_button_space"></div>
	<div class="menu_button_out">
		<div class="menu_button" onMouseOver="ff_mouse_on01(this);" onMouseOut="ff_mouse_off01(this);" onClick="ff_mouse_click('/frontier/pc/com/event/${f:h(cid)}');" title="イベント">
			<div class="menu_button_in">イベント</div>
		</div>
	</div>
	<div class="menu_button_space"></div>
	<div class="menu_button_out">
		<div class="menu_button" onMouseOver="ff_mouse_on01(this);" onMouseOut="ff_mouse_off01(this);" onClick="ff_mouse_click('/frontier/pc/com/entjoin/${f:h(cid)}');" title="このコミュニティに参加">
			<div class="menu_button_in">参加</div>
		</div>
	</div>
</c:when>
<c:otherwise>
<%/* 会員用メニューバー */%>
	<div class="menu_button_out">
		<div class="menu_button" onMouseOver="ff_mouse_on01(this);" onMouseOut="ff_mouse_off01(this);" onClick="ff_mouse_click('/frontier/pc/com/top/${f:h(cid)}');" title="トップ">
			<div class="menu_button_in">トップ</div>
		</div>
	</div>
	<div class="menu_button_space"></div>
<c:choose>
<c:when test="${communityDto.makabletopic=='1' || communityDto.makabletopic=='2'}">
<%/* トピック作成権限あり */%>
	<div class="menu_button_out">
		<div class="menu_button" onMouseOver="ff_mouse_on02(this,'list01');" onMouseOut="ff_mouse_off02(this,'list01');" onClick="ff_mouse_click('/frontier/pc/com/topic/${f:h(cid)}');" title="トピック">
			<div class="menu_button_in">トピック&#9660;</div>
		</div>
		<div id="list01" class="menu_button_list" onMouseOver="ff_list_mouse_on(this);" onMouseOut="ff_list_mouse_off(this);" style="width:120px;">
			<a href="/frontier/pc/com/enttopic/${f:h(cid)}" class="menu_link" title="トピックを作成する">トピックを作成する</a>
		</div>
	</div>
	<div class="menu_button_space"></div>
	<div class="menu_button_out">
		<div class="menu_button" onMouseOver="ff_mouse_on02(this,'list02');" onMouseOut="ff_mouse_off02(this,'list02');" onClick="ff_mouse_click('/frontier/pc/com/event/${f:h(cid)}');" title="イベント">
			<div class="menu_button_in">イベント&#9660;</div>
		</div>
		<div id="list02" class="menu_button_list" onMouseOver="ff_list_mouse_on(this);" onMouseOut="ff_list_mouse_off(this);" style="width:120px;">
			<a href="/frontier/pc/com/entevent/${f:h(cid)}" class="menu_link" title="イベントを作成する">イベントを作成する</a>
		</div>
	</div>
	<div class="menu_button_space"></div>
</c:when>
<c:otherwise>
<%/* トピック作成権限なし */%>
	<div class="menu_button_out">
		<div class="menu_button" onMouseOver="ff_mouse_on01(this);" onMouseOut="ff_mouse_off01(this);" onClick="ff_mouse_click('/frontier/pc/com/topic/${f:h(cid)}');" title="トピック">
			<div class="menu_button_in">トピック</div>
		</div>
	</div>
	<div class="menu_button_space"></div>
	<div class="menu_button_out">
		<div class="menu_button" onMouseOver="ff_mouse_on01(this);" onMouseOut="ff_mouse_off01(this);" onClick="ff_mouse_click('/frontier/pc/com/event/${f:h(cid)}');" title="イベント">
			<div class="menu_button_in">イベント</div>
		</div>
	</div>
	<div class="menu_button_space"></div>
</c:otherwise>
</c:choose>
	<div class="menu_button_out">
		<div class="menu_button" onMouseOver="ff_mouse_on01(this);" onMouseOut="ff_mouse_off01(this);" onClick="ff_mouse_click('/frontier/pc/com/entleave/${f:h(cid)}');" title="退会">
			<div class="menu_button_in">退会</div>
		</div>
	</div>
</c:otherwise>
</c:choose>
<c:if test="${communityDto.makabletopic=='1'}">
	<div class="menu_button_space"></div>
	<div class="menu_button_out">
		<div class="menu_button" onMouseOver="ff_mouse_on01(this);" onMouseOut="ff_mouse_off01(this);" onClick="ff_mouse_click('/frontier/pc/com/entprofile/${f:h(cid)}');" title="設定変更">
			<div class="menu_button_in">設定変更</div>
		</div>
	</div>
</c:if>
</div>
<!-- コミュニティメニューバー -->