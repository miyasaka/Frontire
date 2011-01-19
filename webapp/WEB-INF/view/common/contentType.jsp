<%@ page   pageEncoding="UTF-8"%>
<%String _ua = request.getHeader("User-Agent");
  _ua = (_ua ==null )? "":_ua;
  if(_ua.indexOf("DoCoMo")==0){
	response.setHeader("Content-Type","application/xhtml+xml; charset=Windows-31J");
  }else if(_ua.indexOf("J-PHONE") == 0 || _ua.indexOf("Vodafone") == 0 || _ua.indexOf("SoftBank") == 0 || _ua.indexOf("UP.Browser") == 0){
	response.setHeader("Content-Type","text/html; charset=UTF-8");
  }else if(_ua.indexOf("KDDI") == 0){
	response.setHeader("Content-Type","text/html; charset=UTF-8");
  }else{
	response.setHeader("Content-Type","text/html; charset=Shift_JIS");  
  }
%>