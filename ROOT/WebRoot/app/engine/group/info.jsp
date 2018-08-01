<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/public.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysengineiformmap.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
	 <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
	 <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script> 
	<style type="text/css"> 
		.form_title{  
			font-family:黑体;
			font-size:14px;
			text-align:right;
		}
		.form_data{
			font-family:宋体;
			font-size:12px;
			text-align:left;
			color:0000FF; 
		}
		.td_memo{
			text-align:left; 
			padding:12px;
			font-family:"宋体";
			border:1px solid #666;
			color:#666;
			font-size:12px;
		} 
		.td_memo_title{
			color:red;
		}
	</style>  
</head>
<body class="easyui-layout">
            <div region="center" border="false" style="padding: 10px; background: #fff;border: 1px solid #ccc;">
	            <div style="font-size:18px;padding:50px;"> 
	           		 分组是后台管理维护的重要环节，请根据实际业务区分，定义分组结构，首级分组将作为【发起中心】及【报表中心】的栏目分类，分组管理支持无限递归，请详细进行功能分类
	            </div>
            </div>
</body>
</html>
