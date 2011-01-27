<%@ page language="java" contentType="text/html; charset=windows-31J"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS"/>
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta name="robots" content="nofollow,noindex"     />



<title>[frontier] メンバーを外す</title>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<meta name="Slurp" content="NOYDIR" />
<meta name="robots" content="noodp" />
<script language="javascript" type="text/javascript" src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/js/frontier.js"></script>
<link rel="stylesheet" href="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/css/assort.css" type="text/css" />


<body>
<s:form>
<div id="container">
	<!--header-->
	<%@ include file="/WEB-INF/view/pc/fheader.jsp"%>
	<!--/header-->

	<!--navbarメニューエリア-->
	<%@ include file="/WEB-INF/view/pc/com/fmenu.jsp"%>
	<!--/navbarメニューエリア-->
	
	<div id="contents" class="clearfix">
		<div class="mainMember">
			<!--メイン-->

			<div class="mainMember-main">
			
				<div class="listBoxMemberDelete">
					<div class="listBoxMemberDeleteTitle">
						<table class="list_ttl">
							<tr>
								<td class="ttl_name_sp"><div class="ttl_name inputColStick">メンバーから外す</div></td>
							</tr>
						</table>
					</div>
					<div class="listBoxMemberDeleteBody">
						<span>「<b>${f:h(nickname)}</b>さん」をメンバーから外しますか？</span>
						<div class="listBoxHead listBoxHeadline clearfix">
							<div class="ttlHeadArea">
								<div class="btnArea">
									<s:form>
									<input type="submit" name="exeDelete" value="はい" />
									<input type="submit" name="stop" value="いいえ" />
									</s:form>
								</div>
							</div>
						</div>
					</div>

					
				</div>
				<!--/listBoxMemberDelete-->
				
			</div>
			<!--/mainMember-main-->
		</div>
		<!--/mainMember-->
	</div>
	<img src="/static/style/${appDefDto.FP_CMN_COLOR_TYPE}/images/sp10.png" alt=""/>
	<!--/contents-->
	<!--footer-->
	<%@ include file="/WEB-INF/view/pc/fnfooter.jsp"%>
	<!--footer-->
	 <!--/footer-->
</div>
</s:form>
<!--/container-->
</body>
</html>