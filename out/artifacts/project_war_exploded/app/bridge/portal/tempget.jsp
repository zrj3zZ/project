<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>SAP连接登陆</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
  </head>
  <script type="text/javascript">
    function timedMsg(time){
		$("#second").html(time/1000);
		time=time-1000; 
		if(time==0){
			var t=setTimeout(sub,0);
		}
		var t=setTimeout('timedMsg('+time+')',1000);
	}
	function sub(){
		var toAddress = $("#toAddress").val();
        this.location.href = toAddress;
        //$("#postiframe").attr("href",toAddress);
         
        //$("#responseiframe").attr("src","about:blank");
	}
	
  </script>
	
  <body onload="timedMsg(<s:property value="delay" escapeHtml="false"/>);">
  	<input id="toAddress" type="hidden" value="<s:property value="toAddress" escapeHtml="false"/>"/>
  	<input id="delay" type="hidden" value="<s:property value="delay" escapeHtml="false"/>"/>
  	<div style="display:none">
  	<%--<iframe src='<s:property value="validateAddress" escapeHtml="false" />'></iframe>
  		--%><iframe src="about:self" name="postiframe"></iframe>
		<a href='<s:property value="validateAddress" escapeHtml="false" />' target="postiframe"></a> 
  	</div>
  	
  		
  	
  	<input id="validateAddress" type="hidden" value="<s:property value="validateAddress" escapeHtml="false"/>"/>
  	<div style="margin-top: 50px;margin-left: 15px;color: #585858;">&nbsp;&nbsp;<span id='second'></span>秒后跳转，请稍等...</div>
  </body>
</html>
