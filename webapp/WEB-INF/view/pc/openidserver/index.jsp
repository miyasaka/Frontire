<%@ page contentType="application/xrds+xml"%><?xml version="1.0" encoding="UTF-8"?>
<xrds:XRDS
  xmlns:xrds="xri://$xrds"
  xmlns:openid="http://openid.net/xmlns/1.0"
  xmlns="xri://$xrd*($v*2.0)">
  <XRD>
    <Service priority="0">
      <Type>http://specs.openid.net/auth/2.0/server</Type>
      <Type>http://openid.net/sreg/1.0</Type>
      <Type>http://openid.net/extensions/sreg/1.1</Type>
      <Type>http://openid.net/srv/ax/1.0</Type>
<!-- 
      <Type>http://openid.net/signon/1.0</Type>
-->
      <URI><%= request.getScheme() + "://"%>${f:h(appDefDto.FP_CMN_HOST_NAME)}<%= ":" + request.getServerPort()%>/frontier/pc/openidserver/auth</URI>
    </Service>
  </XRD>
</xrds:XRDS>
