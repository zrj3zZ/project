<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>页面未找到</title>
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="iwork_js/commons.js"></script>
<style type="text/css">
	body{
		background-img:url(../../../iwork_img/background/securityLayer.gif); 
	}
	.title{
		padding:5px;
		font-size:16px; 
		font-family:黑体;
		line-height:30px;
		color:#f77215;
	}
	.info{
		background-color:#fff; 
		padding:5px;
	}
	.content li{
	text-align:left;
	line-height:20px;
	color:#f77215;
	font-size:14px;
	padding-bottom:30px;
	}
</style>
</head> 

<body>
<table width="100%" height="100%">
<tr>
<td align="center" valign="middle">
	<table cellpadding="0" cellspacing="0" >
		<tr>
		<td align="center"><img width="200" src="iwork_img/error404.jpg"/></td>
		<td class="info"> 
			<div class="title">异常提示</div>
			<div class="content">
				<ul>
					<li><s:property value="tipsInfo"></s:property></li>
				</ul>
			</div>
		</td>
		</tr>
	</table>
</td>
</tr>
</table>

</body>
</html>