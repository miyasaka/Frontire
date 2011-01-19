<%@ page pageEncoding="UTF-8" %>
<a name="pagetop"></a>
<div id="header">
	<div class="headerNavi">
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
				<div class="logoutArea">
					<s:link href="/pc/logout/" title="ログアウト">ログアウト</s:link>
				</div>
				<div class="linkArea clearfix">
					<ul class="headerNaviList">
						<li><s:link href="/pc/top/" title="マイトップ">マイトップ</s:link></li>
						<li>|</li>
						<li><s:link href="/pc/searchmem/" title="メンバー検索">メンバー検索</s:link></li>
						<li>|</li>
						<li><s:link href="/pc/searchcom/" title="コミュニティ検索">コミュニティ検索</s:link></li>
						<li>|</li>
						<li><s:link href="/pc/file/" title="ファイル管理">ファイル管理</s:link></li>
					</ul>
				</div>
				<div class="searchArea">
					<div><input type="checkbox" name="mydtonly" id="mydtonly" value="1"/>&nbsp;自分の登録した情報のみ</div>
					<input type="text" name="searchtext" id="searchtext" style="width:180px; padding-left:5px;border:solid 1px #000000; color:#808080;" value="Frontier内から情報を検索する" onclick="javascript:ff_searchonclick(this);" onblur="javascript:ff_searchonblur(this);"/>
					<input type="button" name="searchbtn" value="検索" style="border:solid 1px #000000; background-color:#808080; color:#fff; font-weight:bold; width:45px;" onclick="javascript:ff_search('searchtext');"/>
				</div>
		</c:when>
		<c:otherwise>
			<%/*
				// ========================================
				// 外部Frontierメンバー用メニュー 
				// ========================================
			*/%>
				<div class="logoutArea">
					<!-- 自分のFrontierトップページへ遷移 -->
					<s:link href="http://${userInfoDto.fdomain}/frontier/" title="My Frontier へ戻る">My Frontier へ戻る</s:link>
				</div>
		</c:otherwise>
		</c:choose>
		<div class="headerNavi_top">
			<s:link href="/pc/top/" title="ホーム"><img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/frontier01.gif" alt="frontier"/></s:link>
			&nbsp;<a href="http://www.charlie-s.jp" style="font-size:11px;" title="Charlie Software" target="_blank">PRODUCED BY CHARL<font style="color:#ff0000;">i</font>E-SOFTWARE</a>
		</div>
	</div>
</div>