<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
	<script language="javascript" src="iwork_js/commons.js"></script> 
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"  > </script>
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	
	<script type="text/javascript" src="iwork_js/engine/ifromworkbox.js"  > </script>
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/ifromworkbox.css">
</head>
<body class="easyui-layout">

<!-- TOP区 -->
	<div region="center" style="padding:10px;border:0px;">
		<s:property value="searchArea" escapeHtml="false"/>
		<div style="padding:2px;margin-top:10px;margin-top:10px;border:1px solid #efefef">
			<a href="javascript:addItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">新增行</a>
			<a href="javascript:remove();" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除</a>
			<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
			<table id='iform_grid'></table>
			<div id='prowed_ifrom_grid'></div>
			<s:property value='content'  escapeHtml='false'/>
		</div>
	</div>
	<span style="disabled:none">
		<form name='ifrmMain'  method="post" >
			<s:hidden name="formid"></s:hidden>
			<input type = "hidden" name="idlist" id="idlist" value='11'>
		</form>
	</span>
	
</body>
</html>
