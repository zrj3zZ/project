<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
		<title>资源预定空间管理</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/plugs/loginrmspace.css">
	    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	   <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	   <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	   <script type="text/javascript" src="iwork_js/plugs/resourcebook_space.js"></script>	
	   <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
    	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	   <script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=default"></script>    
	   	
</head>
<body  class="easyui-layout"> 
<!-- TOP区 -->
<div region="north" border="false" style="padding:0px;overflow:no">
<div class="tools_nav"> 
		<a href="javascript:addSpace();" class="easyui-linkbutton" plain="true" iconCls="icon-add">新增空间</a>
		<a href="javascript:removeSpace();" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除空间</a>		
		<a href="javascript:refrenshGrid();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
	 
	</div>
</div>
<div region="center" style="padding:3px;">
		<table id="metadata_grid" style="margin:2px;"></table>
</div>
</body>

</html>