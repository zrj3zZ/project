<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>短信平台-我的号码簿-信息</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	   <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	   <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	   <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">		
		
	   <link rel="stylesheet" type="text/css" href="iwork_css/plugs/queryconfig.css">
		
		</head>
	<body>
		<form onload="onload()">

<div id="win" style='margin-left:10px;width:700px'></div>
	<table  width="700px" border="1"  cellspacing="0" cellpadding="0" bordercolorlight="#CCCCCC" bordercolordark="#FFFFFF">
  	<tr> 
			<td class="font1" nowrap><div align="center" >序号</div></td>
      <td  class="font1" nowrap><div align="center" >参数ID</div></td>
      <td class="font1" ><div align="center">参数值</div></td>
			<td class="font1" nowrap><div align="center">操作</div></td>
    </tr>
      <%=request.getAttribute("type1list") %>
  </table> 
  <div id='hiddiv' style="padding:5px;width:400px;height:200px;">
<table></table></div> 
  <input type="hidden" name="returnvalue" value="<%=request.getAttribute("returnvalue") %>">
 
 
</form>
</body>

<script type="text/javascript" src="iwork_js/plugs/queryconfig.js"></script>

</html>
