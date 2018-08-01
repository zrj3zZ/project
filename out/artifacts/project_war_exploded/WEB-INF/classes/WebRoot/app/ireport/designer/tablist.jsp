<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<title>流程模型设置</title>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
<script type="text/javascript" src="iwork_js/system/syspersion_lefttitle.js"></script>
</head>
<body>
<table class="layout-grid" cellspacing="0" cellpadding="0" style="width:170px;" > 
	<tr>
		<td class="left-nav">
			<s:property value='tabList'  escapeHtml='false'/>
		</td>
	</tr>
</table> 
</body>
</html>

