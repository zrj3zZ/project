<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>操作提示</title>
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="iwork_js/commons.js"></script>
<style>
<!--
.tipTitle{
	font-size:14px;
	font-family:微软雅黑;
	text-align:left;
}
.tipinfo{
	font-size:16px;
	padding-left:30px;
	padding-right:30px;
	font-family:微软雅黑;
}
.explog{
	width:430px;
	height:200px;
	overflow:auto;
}
-->
</style>
</head>

<body>
<table width="100%" height="100%">
<tr>
<td align="center" valign="middle">
	<table border="0" align="center" cellpadding="0" cellspacing="0" width="460" valign="middle">
	  <tr>
	    <td class="onleft"></td>
		 <td class="onbg" style="vertical-align:top;padding-top:30px;">
		 <div class="explog">
		 		<s:property value="log" escapeHtml="false"/>
		 </div>
		 </td>
	  <td class="onright"></td>
	  </tr>
	</table>
</td>
</tr> 
<tr>
	<td style="text-align:center;padding-bottom:20px;vertical-align:top;">
			<s:url value="sysExp_download.action" id="detail">
				<s:param name="downloadFileName" value="downloadFileName"></s:param>
			</s:url> 
				<s:a href="%{detail}"><img src="iwork_img/download.jpg"/></s:a>
	</td>
</tr>
</table>
</body>
</html>

