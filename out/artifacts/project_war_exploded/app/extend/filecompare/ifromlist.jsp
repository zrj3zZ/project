<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>财务数据对比</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
		<link rel="stylesheet" type="text/css"
			href="iwork_css/jquerycss/process-icon.css" />
		<link rel="stylesheet" type="text/css"
			href="iwork_js/jqueryjs/easyui/themes/gray/easyui.css" />
		<link rel="stylesheet" type="text/css" media="screen"
			href="iwork_css/jquerycss/validate/screen.css" />
		<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css" />
		<link rel="stylesheet" type="text/css"
			href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css" />
		<link rel="stylesheet" type="text/css"
			href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css" />
		<link rel="stylesheet" type="text/css"
			href="iwork_css/engine/iformpage.css" />
		<link rel="stylesheet" type="text/css"
			href="iwork_css/iwork/oaknow.css" />
		<script type="text/javascript" src="iwork_js/commons.js"></script>
		<script type="text/javascript"
			src="iwork_js/jqueryjs/jquery-3.1.0.min.js" charset="utf-8"></script>
		<script type="text/javascript"
			src="iwork_js/jqueryjs/jquery.easyui.min.js" charset="utf-8"></script>
		<script type="text/javascript"
			src="iwork_js/jqueryjs/languages/grid.locale-cn.js" charset="utf-8"></script>
		<script type="text/javascript"
			src="iwork_js/jqueryjs/jquery.jqGrid.src.js" charset="utf-8"> </script>
		<script type="text/javascript"
			src="iwork_js/jqueryjs/jquery.validate.js" charset="utf-8"></script>
		<script type="text/javascript"
			src="iwork_js/jqueryjs/jquery.metadata.js" charset="utf-8"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"
			charset="utf-8"></script>
		<script type="text/javascript"
			src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
		<script type="text/javascript"
			src="iwork_js/jqueryjs/languages/messages_cn.js" charset="utf-8"></script>
		<script type="text/javascript"
			src="iwork_js/lhgdialog/lhgdialog.min.js?self=true"></script>
		<script type="text/javascript" src="iwork_js/engine/iformpage.js"
			charset="utf-8"></script>
		<script type="text/javascript">
	function doSubmit(){
		var FILETEXT=document.getElementById("FILETEXT").value;
		var pageUrl="zqb_file_compare.action";
		$.post(pageUrl,{FILETEXT:FILETEXT},function(data){
			alert("a");
   		});
	}
</script>
		<style type="text/css">
.searchtitle {
	text-align: right;
	padding: 5px;
}

.ui-jqgrid tr.jqgrow td {
	white-space: normal !important;
	height: 28px;
	font-size: 12px;
	vertical-align: text-middle;
	padding-top: 2px;
}

.cell td {
	margin: 0;
	padding: 3px 4px;
	height: 25px;
	font-size: 12px;
	white-space: nowrap;
	word-wrap: normal;
	overflow: hidden;
	text-align: left;
	border-bottom: 1px dotted #eee;
	border-top: 1px dotted #fff;
	border-right: 1px dotted #eee;
}

.cell:hover {
	background-color: #F0F0F0;
}

.header td {
	height: 35px;
	font-size: 12px;
	padding: 3px;
	white-space: nowrap;
	padding-left: 5px;
	background: url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left
		bottom;
	border-top: 1px dotted #ccc;
	border-right: 1px solid #eee;
}
</style>
</head>
<body class="easyui-layout">
	<div region="center"
		style="text-align:center;border-left:1px #999 dotted;border-right:1px #999 dotted;border-top:1px #999 dotted;border-bottom:0px #999 dotted;padding:2px;">
		<s:form id="editForm" name="editForm" theme="simple">
			<!--表单参数-->
			
		</s:form>
	</div>
</body>
</html>