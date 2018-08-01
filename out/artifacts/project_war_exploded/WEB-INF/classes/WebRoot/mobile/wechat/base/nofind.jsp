<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=0">

<title>页面未找到</title>
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
  <script src="http://res.wx.qq.com/open/js/jweixin-1.1.0.js"></script> 
<style type="text/css">
	body{
		background-img:url(../../../iwork_img/background/securityLayer.gif); 
	}
	.title{
		padding:5px;
		font-size:22px; 
		font-family:黑体;
		line-height:30px;
	}
	.info{
		background-color:#fff; 
		padding:10px;
	}
	.content li{
	line-height:20px;
	}
</style>
<script type="text/javascript">
	   <s:property value="initWeiXinScript" escapeHtml="false"/>
</script>
</head> 

<body>
<table width="100%" height="100%">
<tr>
<td align="center" valign="middle">
	<table cellpadding="0" cellspacing="0" >
		<tr>
		<td><img width="250" src="iwork_img/error404.jpg"/></td>
		<td class="info"> 
			<div class="title">用户会话已关闭!</div>
		</td>
		</tr>
	</table>
</td>
</tr>
</table>
</body>
</html>
<script type="text/javascript">
	wx.ready(function(){
	wx.closeWindow();
	});
</script>