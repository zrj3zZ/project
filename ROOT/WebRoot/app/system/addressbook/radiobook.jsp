<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
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
	
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	
	<link href="iwork_css/system/radiobook.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="iwork_js/system/radiobook.js"></script>
	
</head>
<body>
<div style="padding:2px;background:#efefef;width:100%;text-align:left;">
<a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-ok">执行授权</a>
		<a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-cancel">取消</a>
	<a href="javascript:void(0)" id="mb1" class="easyui-menubutton" menu="#mm1" iconCls="icon-edit">Edit</a>
		<a href="javascript:void(0)" id="mb2" class="easyui-menubutton" menu="#mm2" iconCls="icon-help">Help</a>
		
		
	</div>
	<div id="mm1" style="width:150px;">
		<div iconCls="icon-undo">Undo</div>
		<div iconCls="icon-redo">Redo</div>
		<div class="menu-sep"></div>
		<div>Cut</div>
		<div>Copy</div>
		<div>Paste</div>
		<div class="menu-sep"></div>
		<div>
			<span>Toolbar</span>
			<div style="width:150px;">
				<div>Address</div>
				<div>Link</div>
				<div>Navigation Toolbar</div>
				<div>Bookmark Toolbar</div>
				<div class="menu-sep"></div>
				<div>New Toolbar...</div>
			</div>
		</div>
		<div iconCls="icon-remove">Delete</div>
		<div>Select All</div>
	</div>
	<div id="mm2" style="width:100px;">
		<div>Help</div>
		<div>Update</div>
		<div>About</div>
	</div>
	<ul id="addresstree"></ul> 
</body>
</html>
