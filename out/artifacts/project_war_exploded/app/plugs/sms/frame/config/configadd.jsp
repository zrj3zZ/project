<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
<title>短信平台-系统设置</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">

<link rel="stylesheet" type="text/css" href="iwork_css/plugs/loginconfig.css">
		
</head>
<body onload="onload()">
<form>

<div id="nav"><div class='tit2'>
<span id="url_route" name="url_route"> </span></div></div>
<div style="margin-left:10px;margin-top:10px;margin-bottom:5px;width:97%;">
 <table class="font"><tr><td>类别：<%=request.getAttribute("type") %>
　参数ID：<input type=text name='parameterid' id='parameterid'>　参数值：<input type=text name='parameter' id='parameter'>
　<input type=button value='新 增'  class ='font' onClick="add()" name='buttonquery'  border='0'>
</td></tr></table>
</div>
<div style='color:red;margin-left:10px;'>
<table class="font1"><tr><td>
注意：下列系统参数与程序密切相关，请勿随意改动。调整前请与相关开发人员联系，以免影响短信平台运行！谢谢。
</td></tr></table></div>

<iframe name='MSG_PHONEBOOK_FRAME' id='MSG_PHONEBOOK_FRAME' width='800px' height='600px' frameborder=0 scrolling=no marginheight=0 marginwidth=0
 src='<%=request.getAttribute("srcd") %>'></iframe>

 <input type=hidden name=selectvalue>
   <input type="hidden" name="returnvalue2" value="<%=request.getAttribute("returnvalue2") %>">
 
</form>
</body>

<script type="text/javascript" src="iwork_js/plugs/loginconfig.js"></script>

</html>