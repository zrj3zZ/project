<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统计划任务</title>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/easyui-lang-zh_CN.js"></script>
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"  ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"  > </script>

<link rel="stylesheet" type="text/css" href="iwork_css/system/sys_schedule_showlog.css">

</head>
<body class="easyui-layout"> 
   <!-- TOP区 -->
	<div region="north" border="false"  style="background-color:#efefef;padding:0px;overflow:no">
	<div style="padding:2px;">
	</div>
	</div>
    <!-- 内容区 -->
	<div region="center" style="padding:3px;">
		<table id='sysscheduleloggrid'></table>
		<div id='prowed_info_grid'></div>
		<s:property value='infolist'  escapeHtml='false'/>
    </div>
</body>
</html>