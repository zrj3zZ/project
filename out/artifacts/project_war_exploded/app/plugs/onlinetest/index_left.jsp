<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>试题列表</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/system/syspersion_lefttitle.js"></script>
<style type="text/css">
	.groupTitle{
		border-bottom:1px solid #efefef;
		background-color:#efefef;
		color:#0000FF;
		padding-left:15px;
		margin-top:100px;
		font-size:12px;
	}
</style>
</head>

<body class="easyui-layout" >
<div region="north" border="false" style="height:300px;padding-left:10px;padding-top:5px" split="false" >
<table class="layout-grid" cellspacing="0" cellpadding="0" style="width:170px;" >
	<tr>
		<td class="left-nav">
			<dl class="demos-nav">
				<dd  class="groupTitle">&nbsp;&nbsp;<img src="iwork_img/star_empty.png" border="0">&nbsp;开启的试卷</dd>
				<s:property value='paperListHtml' escapeHtml="false"/>
			</dl>
		</td>
	</tr>
</table>
</div>
<div region="center" border="false" style="border-left:1px solid #efefef;padding-left:10px;padding-top:5px">
<table class="layout-grid" cellspacing="0" cellpadding="0" style="width:170px;" >
	<tr>
		<td class="left-nav">
			<dl class="demos-nav">
				<dd class="groupTitle" >&nbsp;<img src="iwork_img/star_full.png" border="0">&nbsp;我参加过的考试</dd>
				<s:property value='oldPaperListHtml' escapeHtml="false"/>
			</dl>
		</td>
	</tr>
</table>
</div>
</body>
</html>