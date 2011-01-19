<%@ page pageEncoding="UTF-8" %>
<div class="menu_button_area">
	<%/*
		// ========================================
		// Frontierのメンバータイプで条件分岐
		// 【userInfoDto.membertype】
		//     0 : 本Frontierメンバー
		//     1 : 外部Frontierメンバー
		// ========================================
	*/%>
	<c:choose><c:when test="${userInfoDto.membertype == '0'}">
		<%/*
			// ========================================
			// 本Frontierメンバー用メニュー
			// ========================================
		*/%>
		<c:choose><c:when test="${
			userInfoDto.visitMemberId == '' ||
			userInfoDto.visitMemberId == null ||
			userInfoDto.memberId == userInfoDto.visitMemberId
		}">
			<!-- マイメニュー -->
			<div class="menu_button_out">
				<div class="menu_button" onMouseOver="ff_mouse_on01(this);" onMouseOut="ff_mouse_off01(this);" onClick="ff_mouse_click('/frontier/pc/top/');" title="トップ">
					<div class="menu_button_in">トップ</div>
				</div>
			</div>
			<div class="menu_button_space"></div>
			<div class="menu_button_out">
				<div class="menu_button" onMouseOver="ff_mouse_on02(this,'list00');" onMouseOut="ff_mouse_off02(this,'list00');" onClick="ff_mouse_click('/frontier/pc/calendar/');" title="ｽｹｼﾞｭｰﾙ">
					<div class="menu_button_in">ｽｹｼﾞｭｰﾙ&#9660;</div>
				</div>
				<div id="list00" class="menu_button_list" onMouseOver="ff_list_mouse_on(this);" onMouseOut="ff_list_mouse_off(this);" style="width:100px;">
					<a href="javascript:void(0)" class="menu_link" title="予定を入力する" onclick="MM_openBrWindow('/frontier/pc/calendar/entryTop', '', 'width=760,height=640,toolbar=no,scrollbars=yes,left=10,top=10')">予定を入力する</a>
				</div>
			</div>


			<div class="menu_button_space"></div>
			<div class="menu_button_out">
				<div class="menu_button" onMouseOver="ff_mouse_on02(this,'list04');" onMouseOut="ff_mouse_off02(this,'list04');" title="マイ情報">
					<div class="menu_button_in">マイ情報&#9660;</div>
				</div>
				<div id="list04" class="menu_button_list" onMouseOver="ff_list_mouse_on(this);" onMouseOut="ff_list_mouse_off(this);" style="width:105px;">
					<a href="/frontier/pc/diary/list/${f:h(userInfoDto.memberId)}" class="menu_link" title="日記一覧">日記一覧</a><br/>
					<a href="/frontier/pc/entdiary/" class="menu_link" title="日記登録">日記登録</a><br/>
					<a href="/frontier/pc/photo/list/${f:h(userInfoDto.memberId)}" class="menu_link" title="フォト一覧">フォト一覧</a><br/>
					<a href="/frontier/pc/entphoto/index/" class="menu_link" title="フォト登録">フォト登録</a><br/>
					<a href="/frontier/pc/history/" class="menu_link" title="足あと">足あと</a>
				</div>
			</div>
			<div class="menu_button_space"></div>
			<div class="menu_button_out">
				<div class="menu_button" onMouseOver="ff_mouse_on02(this,'list05');" onMouseOut="ff_mouse_off02(this,'list05');" title="ﾒﾝﾊﾞｰ情報">
					<div class="menu_button_in">ﾒﾝﾊﾞｰ情報&#9660;</div>
				</div>
				<div id="list05" class="menu_button_list" onMouseOver="ff_list_mouse_on(this);" onMouseOut="ff_list_mouse_off(this);" style="width:105px;">
					<a href="/frontier/pc/newfrienddiary/index/" class="menu_link" title="日記一覧">日記一覧</a><br/>
					<a href="/frontier/pc/friendupdate/index/" class="menu_link" title="フォト一覧">フォト一覧</a><br/>
				</div>
			</div>
			<div class="menu_button_space"></div>
			<div class="menu_button_out">
				<div class="menu_button" onMouseOver="ff_mouse_on01(this);" onMouseOut="ff_mouse_off01(this);" onClick="ff_mouse_click('/frontier/pc/recentbbscomment/index/');" title="ｺﾐｭﾆﾃｨ情報">
					<div class="menu_button_in">ｺﾐｭﾆﾃｨ情報</div>
				</div>
			</div>



			<div class="menu_button_space"></div>
			<div class="menu_button_out">
				<div class="menu_button" onMouseOver="ff_mouse_on01(this);" onMouseOut="ff_mouse_off01(this);" onClick="ff_mouse_click('/frontier/pc/fshout/list/${f:h(userInfoDto.memberId)}/0');" title="F Shout">
					<div class="menu_button_in">F Shout</div>
				</div>
			</div>
			<div class="menu_button_space"></div>
			<div class="menu_button_out">
				<div class="menu_button" onMouseOver="ff_mouse_on02(this,'list03');" onMouseOut="ff_mouse_off02(this,'list03');" onClick="ff_mouse_click('/frontier/pc/setup/');" title="設定変更">
					<div class="menu_button_in">設定変更&#9660;</div>
				</div>
				<div id="list03" class="menu_button_list" onMouseOver="ff_list_mouse_on(this);" onMouseOut="ff_list_mouse_off(this);" style="width:105px;">
					<a href="/frontier/pc/profile1/" class="menu_link" title="基本設定">基本設定</a><br/>
					<a href="/frontier/pc/profile2/edit/${f:h(userInfoDto.memberId)}" class="menu_link" title="イメージ設定">イメージ設定</a><br/>
					<a href="/frontier/pc/profile2/" class="menu_link" title="プロフィール設定">プロフィール設定</a>
				</div>
			</div>

			<c:if test="${userInfoDto.mlevel=='1'}">
			<div class="menu_button_out2">
				<div class="menu_button2" onMouseOver="ff_mouse_on02(this,'list99');" onMouseOut="ff_mouse_off02(this,'list99');" title="管理ﾒﾆｭｰ▼">
					<div class="menu_button_in2">管理ﾒﾆｭｰ▼</div>
				</div>
				<div id="list99" class="menu_button_list" onMouseOver="ff_list_mouse_on(this);" onMouseOut="ff_list_mouse_off(this);" style="width:90px;">
					<a href="/frontier/pc/group/index" class="menu_link" title="グループ管理">グループ管理</a><br/>
					<a href="/frontier/pc/rssmanage/index" class="menu_link" title="RSS管理">RSS管理</a>
				</div>
			</div>
			</c:if>
			<!--
			<div class="menu_button_out2">
				<div class="menu_button2" title="Frontier Net">
					<div class="menu_button_in2">Frontier Net</div>
				</div>
			</div>
			-->
			<!-- マイメニュー -->
		</c:when>
		<c:otherwise>
			<!-- メンバーメニュー -->
			<div class="menu_button_out">
				<div class="menu_button" onMouseOver="ff_mouse_on01(this);" onMouseOut="ff_mouse_off01(this);" onClick="ff_mouse_click('/frontier/pc/mem/${f:h(userInfoDto.visitMemberId)}');" title="トップ">
					<div class="menu_button_in">トップ</div>
				</div>
			</div>
			<div class="menu_button_space"></div>
			<div class="menu_button_out">
				<div class="menu_button" onMouseOver="ff_mouse_on01(this);" onMouseOut="ff_mouse_off01(this);" onClick="ff_mouse_click('/frontier/pc/diary/list/${f:h(userInfoDto.visitMemberId)}');" title="日記">
					<div class="menu_button_in">日記</div>
				</div>
			</div>
			<div class="menu_button_space"></div>
			<div class="menu_button_out">
				<div class="menu_button" onMouseOver="ff_mouse_on01(this);" onMouseOut="ff_mouse_off01(this);" onClick="ff_mouse_click('/frontier/pc/photo/list/${f:h(userInfoDto.visitMemberId)}');" title="フォト">
					<div class="menu_button_in">フォト</div>
				</div>
			</div>
			<div class="menu_button_space"></div>
			<div class="menu_button_out">
				<div class="menu_button" onMouseOver="ff_mouse_on01(this);" onMouseOut="ff_mouse_off01(this);" onClick="ff_mouse_click('/frontier/pc/fshout/list/${f:h(userInfoDto.visitMemberId)}/1');" title="F Shout">
					<div class="menu_button_in">F Shout</div>
				</div>
			</div>
			<!-- メンバーメニュー -->
		</c:otherwise></c:choose>

	</c:when>
	<c:otherwise>
		<%/*
			// ========================================
			// 外部Frontierメンバー用メニュー
			// ========================================
		*/%>
		<c:choose><c:when test="${
			userInfoDto.visitMemberId == '' ||
			userInfoDto.visitMemberId == null ||
			userInfoDto.memberId == userInfoDto.visitMemberId
		}">
			<!-- マイメニュー -->
			<div class="menu_button_out">
				<div class="menu_button" onMouseOver="ff_mouse_on01(this);" onMouseOut="ff_mouse_off01(this);" onClick="ff_mouse_click('/frontier/pc/top/');" title="トップ">
					<div class="menu_button_in">トップ</div>
				</div>
			</div>
			<div class="menu_button_space"></div>
			<div class="menu_button_out">
				<div class="menu_button" onMouseOver="ff_mouse_on01(this);" onMouseOut="ff_mouse_off01(this);" onClick="ff_mouse_click('/frontier/pc/fmem/');" title="メンバー">
					<div class="menu_button_in">メンバー</div>
				</div>
			</div>
			<div class="menu_button_space"></div>
			<div class="menu_button_out">
				<div class="menu_button" onMouseOver="ff_mouse_on01(this);" onMouseOut="ff_mouse_off01(this);" onClick="ff_mouse_click('/frontier/pc/fdiary/');" title="ﾒﾝﾊﾞｰ日記">
					<div class="menu_button_in">ﾒﾝﾊﾞｰ日記</div>
				</div>
			</div>
			<!-- マイメニュー -->
		</c:when>
		<c:otherwise>
			<!-- メンバーメニュー -->
			<div class="menu_button_out">
				<div class="menu_button" onMouseOver="ff_mouse_on01(this);" onMouseOut="ff_mouse_off01(this);" onClick="ff_mouse_click('/frontier/pc/mem/${f:h(userInfoDto.visitMemberId)}');" title="トップ">
					<div class="menu_button_in">トップ</div>
				</div>
			</div>
			<div class="menu_button_space"></div>
			<div class="menu_button_out">
				<div class="menu_button" onMouseOver="ff_mouse_on01(this);" onMouseOut="ff_mouse_off01(this);" onClick="ff_mouse_click('/frontier/pc/diary/list/${f:h(userInfoDto.visitMemberId)}');" title="日記">
					<div class="menu_button_in">日記</div>
				</div>
			</div>
			<div class="menu_button_space"></div>
			<div class="menu_button_out">
				<div class="menu_button" onMouseOver="ff_mouse_on01(this);" onMouseOut="ff_mouse_off01(this);" onClick="ff_mouse_click('/frontier/pc/fshout/list/${f:h(userInfoDto.visitMemberId)}/1');" title="F Shout">
					<div class="menu_button_in">F Shout</div>
				</div>
			</div>
			<!-- メンバーメニュー -->
		</c:otherwise></c:choose>
	</c:otherwise></c:choose>
</div>