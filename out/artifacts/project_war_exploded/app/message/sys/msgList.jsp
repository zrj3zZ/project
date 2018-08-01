<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/portal.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" charset="gb2312"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/plugins/jquery.portal.js"></script>
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="iwork_js/sys_procdef.js" charset="gb2312"></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js" charset="gb2312"></script>
	
	<link rel="stylesheet" type="text/css" href="iwork_css/message/sysmsglist.css">
	<script type="text/javascript" src="iwork_js/message/sysmsglist.js"></script>
	
</head>
<body  style="text-align:center; OVERFLOW:SCROLL;OVERFLOW-X:HIDDEN">
<div id="border">
  	<s:form id="frmMain" name="frmMain" action="sysMessageAction!list.action"  theme="simple"  enctype="multipart/form-data">
		<s:property value="msgList" escapeHtml="false" />
		<s:hidden name="status"></s:hidden>
		<s:hidden name="type"></s:hidden>
		<s:hidden name="id"></s:hidden>
		<s:hidden name="currentPage"></s:hidden>
		<s:hidden name="cmd"></s:hidden>
	</s:form>	
</div> 
</body> 
</html>
