<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<script type="text/javascript">
    var content='<s:property value="model.bcontent"/>';
    content=content.replace(/\%/g,'%25');//通配符
	window.parent.opener.location='loginmsg.action?rev=<s:property value="model.bv"/>&content='+content+'&phone=<s:property value="model.bphone"/>';
	 parent.window.close();
	 
	// window.parent.opener.location='loginmsg.action?content=<s:property value="model.bcontent"/>&phone=<s:property value="model.bphone"/>';
	
	
</script>
</head>
</html>
