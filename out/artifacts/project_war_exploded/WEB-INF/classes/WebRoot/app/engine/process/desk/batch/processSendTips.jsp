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
	font-size:20px;
	padding:5px;
	font-family:微软雅黑;
	text-align:left;
}
.tipinfo{
	font-size:16px;
	padding-left:30px;
	padding-right:30px;
	font-family:微软雅黑;
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

			<table border="0" width="400">
			<tr>
			<td  class="tipTitle">
				提示:
			</td></tr>
			<tr>
			<td class="tipinfo"><img src="iwork_img/notice.jpg" border="0"><s:property value="tipsInfo"/>
			</td>
			</tr>
			</table>
</td>
  <td class="onright"></td>
  </tr>
</table>
</td>
</tr>
</table>
</body>
</html>

