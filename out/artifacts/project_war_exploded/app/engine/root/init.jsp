<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/public.css" />
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js" ></script>
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformsearch_index.css">
	<script type="text/javascript" src="iwork_js/engine/iformsearch_index.js" ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style>
		.tbLable td{
			font-size:16px;
			margin-left:auto; 
			margin-right:auto;
			font-family:微软雅黑;
			padding:5px;
		} 
	</style>
</head>
<body class="easyui-layout">
<!-- TOP区 --> 
	<div region="north" border="false" style="height:70px;border:1px solid #efefef;font-size:20px;font-family:微软雅黑">
	<img src="iwork_img/gear3.gif" style="width:50px"/>数据初始化 
   </div>
 
	<div region="center" style="padding-left:50px;border:0px;text-align:center;"> 
			<table class="tbLable" > 
				<tr>    
					<td style="width:100px">流程初始化</td>
					<td><a href="system_process_initPage.action">进入</a></td>
				</tr>
				<tr>   
					<td>主数据初始化</td>
					<td><a href="system_dem_initPage.action">进入</a></td>
				</tr>
				
			</table>
	</div>
</body> 
</html>
