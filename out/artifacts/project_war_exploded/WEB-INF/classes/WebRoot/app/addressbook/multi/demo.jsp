<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<base target="_self">
<title>无标题文档</title>
	
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/portal.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js"   ></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"  ></script>
</head>

<body class="easyui-layout">
<div region="center">
<input type="text" value="" id="address">
<input type="button" value="多选地址簿" onclick="multi_book('false','false','false','false','false','','','','','','address')" />

<div id="iwindow" class="easyui-window" title="整理收藏夹" iconCls="icon-ok" modal="true" closed="true"  style="width:650px;height:535px;padding:5px;background: #fafafa;">			
					<div id="addressbookTarget"></div>
			</div>
</div>
</body>
</html>
