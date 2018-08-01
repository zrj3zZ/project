<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>IWORK综合应用管理系统</title>
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" href="iwork_js/kindeditor/themes/simple/simple.css" />
	<link rel="stylesheet" href="iwork_js/kindeditor/plugins/code/prettify.css" />
	<link href="iwork_css/plugs/cmsrelease.css" rel="stylesheet" type="text/css" />
	<script charset="utf-8" src="iwork_js/kindeditor/kindeditor.js"></script>
	<script charset="utf-8" src="iwork_js/kindeditor/lang/zh_CN.js"></script>
	<script charset="utf-8" src="iwork_js/kindeditor/plugins/code/prettify.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" charset="gb2312"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
	  <script type="text/javascript" src="iwork_js/upload/swfobject.js"></script>
	<script type="text/javascript" src="iwork_js/upload/jquery.uploadify.v2.1.4.js"></script>
	 <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
	 <style type="text/css">
	 	.item_ul{width:100%;margin-left:5px;} 
		.sep_ul{width:96%;}
		.top_mar{width:10px;height:200px;float:left;} 
		.item_ul li{position:relative;display:inline;} 
		.item_ul li a{display:block;width:80%;white-space:nowrap;overflow:hidden;float:left;line-height:22px;height:22px;
		    -o-text-overflow: ellipsis;    /**//* for Opera */
		    text-overflow:ellipsis;        /**//* for IE */
		} 
		.time{float:right;line-height:22px;height:22px;margin-right:15px;}
		
	 </style>
	 <script type="text/javascript">
	 	 function openCms(infoid){
		    var url = "cmsOpen.action?infoid="+infoid;
		    var newWin="newVikm"+infoid;
		    var page = window.open('form/waiting.html',newWin,'width=700,height=500,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		    page.location=url;
		}
	 	
	 </script>
</head>
<body class="easyui-layout">

<div region="center"  border="false" split="false" style="text-align:center" >
<s:property value="calendarList" escapeHtml="false"/> 
</div>
</body>
</html>
