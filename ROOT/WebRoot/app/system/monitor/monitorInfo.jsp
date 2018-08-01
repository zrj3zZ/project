<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head><title>监控信息</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
		<script language="javascript" src="iwork_js/commons.js"></script>
	    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/linkbutton.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
        <link href="iwork_css/system/purgroup_list.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="iwork_js/system/purgroup_list.js"></script>
        <script type="text/javascript">
 //==========================装载快捷键===============================//快捷键
    jQuery(document).bind('keydown',function (evt){		
		    if(evt.ctrlKey&&evt.shiftKey){
			return false;
		   }
		   else if(evt.ctrlKey && event.keyCode==82){ //Ctrl+r /刷新操作
			         this.location.reload(); return false;
		     }
}); //快捷键
</script>
<style type="text/css">
	.infoTb td{
			line-height:30px;
	}
	.infoTitle{
		font-size:16px;
		text-align:right;
		padding-right:10px;
	}
	.infoData{
	font-size:30px;
	color:red; 
	text-align:center;
	width:30px;
	}
</style>
</head>	
<body class="easyui-layout">
<div region="center" border="false" style="border-left:1px solid #efefef">
 <s:property value="list" escapeHtml="false"/>
</div>
</body>
</html>
