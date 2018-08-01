<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<script type="text/javascript" src="iwork_js/commons.js" charset="gb2312"></script>
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<style type="text/css">
		.header td{
			font-weight:bold;
			font-size:12px;
			padding:3px;
			white-space:nowrap;
			padding-left:5px;
			background:#fafafa url('iwork_css/jquerycss/default/images/datagrid_header_bg.gif') repeat-x left bottom;
			border-bottom:1px dotted #ccc;
			border-top:1px dotted #fff;
			border-right:1px dotted #ccc;
		}
		.cell td{
			margin:0;
			padding:3px 4px;
			white-space:nowrap;
			word-wrap:normal;
			overflow:hidden;
			text-align:left;
			border-bottom:1px dotted #eee;
			border-top:1px dotted #fff;
			border-right:1px dotted #ccc;
		}
	</style>
</head>
<body class="easyui-layout">
	<div region="center" style="padding:3px;border-top:0px;" >
	<!-- 
		<embed pluginspage="http://www.macromedia.com/go/getflashplayer" 
	 menu="true" loop="true" width="100%" height="100%"  play="true" type="application/x-shockwave-flash" 
	 style="z-index:-1;"
	 src="designer/kbpmDesigner.swf">
	</embed> -->
	<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0" width="100%" height="100%" id="FlashVars" align="middle">
		<param name="allowscrīptAccess" value="sameDomain" />
		<param name="movie" value="designer/kbpmDesigner.swf" />
		<param name="FlashVars" value="status=create&processKey=caigoushenqing&processName=采购申请流程&actProcId=<s:property value="actProcId"/>&ip=127.0.0.1&port=8080" />
		<param name="quality" value="high" />
		<param name="bgcolor" value="#ffffff" /> 
<embed src="designer/kbpmDesigner.swf" FlashVars="status=create&processKey=caigoushenqing&processName=采购申请流程&actProcId=<s:property value="actProcId"/>&ip=127.0.0.1&port=8080" width="100%" height="99%"   quality="high" bgcolor="#ffffff" width="550" height="400" name="FlashVars" align="middle" allowscrīptAccess="sameDomain" FlashVars="foo=happy2005&program=flash&language=简体中文-中国" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />
    </div>    
</body>
</html>
