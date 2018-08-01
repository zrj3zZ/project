
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
		<title>短信平台-短信日志查询</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	   <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	   <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	   <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">		
		</head>
<body>
<form method=post name=frmMain>
<div id="nav"><div class='tit2'>
<span id="url_route" name="url_route"> </span></div></div>

<div style="margin-left:10px;margin-top:10px;margin-bottom:5px;width:97%;">
<table><tr><td>
发送人：</td><td><input type=text class='actionsoftInput' length='30' id='sender' name='sender' value=''></td><td>
短信提交日期：<input type=text class='actionsoftInput'  name='startdate' class='easyui-datebox' editable='false'>
</td><td>至 <input type=text class='actionsoftInput'  name='enddate' class='easyui-datebox' editable='false'>
</td></tr></table>
</div>
<div style='margin-left:10px;width:650px;'>
<center>
	<input type=button value='查 询'  onClick="userManagermentQuery(frmMain,'userManagement_Query_web');return false;" name='buttonquery'  border='0'>
</center>
</div>

<iframe name=MSG_PHONEBOOK_FRAME id=MSG_PHONEBOOK_FRAME width=98% height="85%" frameborder=0 scrolling=no marginheight=0 marginwidth=0
	 src='<%=request.getAttribute("selecturl")%>'></iframe>
</form>
</body>
<script>

function userManagermentQuery(){
var startdate=document.getElementById("startdate");
var enddate=document.getElementById("enddate");
	if(startdate.value!=""&&enddate.value!=""&&(startdate.value>enddate.value)){
		alert("请输入正确的时间段");
		return false;
	}
	var url='quser.action'
    document.forms[0].action=url; 
	document.forms[0].target="MSG_PHONEBOOK_FRAME";
 	document.forms[0].submit();
 	return false;

}

</script>
</html>