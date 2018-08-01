<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>introducemysef</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<style type="text/css">
		.td_title1 {
					line-height: 30px;
					font-size: 12px;
					text-align: center;
					letter-spacing: 0.1em;
					padding-right:10px;
					white-space:nowrap;
					border-bottom:1px #999999 thick;
					vertical-align:middle;
					font-family:"宋体";
			}
			/*数据字段内容样式*/
			.td_data1 {
				color:#0000FF;
				line-height: 23px;
				text-align: center;
				padding-left: 3px;
				font-size: 12px;
				font-family:"宋体";
				border-bottom:1px #999999 dotted;
				vertical-align:middle;
				word-wrap:break-word;
				word-break:break-all;
				font-weight:500;
				line-height:15px;
				padding-top:5px;
				color:0000FF; 
			}
	</style>
  </head>
  
  <body>
  	<form action="./message.wf" method=post name=frmMain>	 
<table cellspacing=0 cellpadding=0 width=100% border=0>
	<tr align="center">
		<td width="14%" align="left" valign="top" >
	<fieldset  style="table-layout:fixed;padding:8px;border:1px solid #ACBCC9;line-height:2.0; margin-top:2px;word-break:break-all;word-wrap:break-word;">
      <legend><B>我酷故我秀</B></legend>
      <table>
      	<tr>
      		<td class="td_data1" style="width:160px;word-break:break-all;word-wrap:break-word;">
					我的兴趣爱好就是：${hobbies }
			</td>
	   </tr>
	</table>
   </fieldset>
   </td>
	<!--<td width="86%" style="padding-left:5px; padding-top:10px;text-align:center; vertical-align:text-top">
		<table cellspacing=0 cellpadding=0 width=100% border=0>	
			<tr >
				<td align="center" valign="top"  >
					<img  src=../aws_img/nopic.gif border=0>		</td>
			</tr>
		</table>	
	</td>
--></tr>
</table>
  	
  </body>
</html>
