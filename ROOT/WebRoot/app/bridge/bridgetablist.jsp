<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<title>数据选择器设置</title>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
<script type="text/javascript" src="iwork_js/system/syspersion_lefttitle.js"></script>
</head> 
<body>
<table class="layout-grid" cellspacing="0" cellpadding="0" style="width:170px;" > 
	<tr>
		<td class="left-nav">
			<dl class="demos-nav">
				<dd><a id="sysConn_baseInfo" class="selected" href="conn_bridge_param_baseinfo.action?id=<s:property value='id'  escapeHtml='false'/>" target="conn_right">基本信息</a></dd>
				<dd><a id="sysConn_param_input" href="conn_bridge_param_input.action?id=<s:property value='id'  escapeHtml='false'/>" target="conn_right">输入参数设置</a></dd>
				<dd><a id="sysConn_param_output" href="conn_bridge_param_output.action?id=<s:property value='id'  escapeHtml='false'/>" target="conn_right">输出参数设置</a></dd>
			</dl> 
		</td>
	</tr> 
</table> 
</body>
</html>

