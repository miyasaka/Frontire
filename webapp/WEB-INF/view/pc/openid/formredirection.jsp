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
<title>[frontier]OpenID認証処理</title>
</head>
<body onload="document.forms[0].submit()">
<form name="openid-form-redirection" action="${OPEndPoint}" method="post" accept-charset="utf-8">
<c:forEach var="parameter" items="${message.parameterMap}"><input type="hidden" name="${parameter.key}" value="${parameter.value}"/></c:forEach>


</form>
</body>
</html>