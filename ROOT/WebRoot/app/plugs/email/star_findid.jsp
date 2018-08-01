<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css"> 
   	<link rel="stylesheet" href="iwork_js/kindeditor/themes/simple/simple.css" />
	<link rel="stylesheet" href="iwork_js/kindeditor/plugins/code/prettify.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/autocomplete/jquery.autocomplete.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<script charset="utf-8" src="iwork_js/kindeditor/kindeditor.js"></script>
	<script charset="utf-8" src="iwork_js/kindeditor/lang/zh_CN.js"></script>
	<script charset="utf-8" src="iwork_js/kindeditor/plugins/code/prettify.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/autocomplete/jquery.autocomplete.min.js"  charset="utf-8" ></script>
	<script type="text/javascript" src="iwork_js/plugs/emaileditor.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style>
		.leftDiv{ 
			background:#f5f5f5;
			width:150px;
		} 
		.rigetDiv{
			width:230px;
			border-left:1px solid #efefef;
			vertical-align:top
		} 
		input[type~=text] {
			width:500px;
			height:20px;
			padding:2px;
			padding-left:5px;
			color:#666;
			font-family:微软雅黑;
		}
		.headTitle{
			text-align:left;
			padding:2px;
			font-size:20px;
			padding-right:5px;
			font-family:微软雅黑;
			color:#285586;
			font-werght:bold;
			line-height:28px;
		}
		.headData{
			text-align:left;
			padding:2px;
			font-size:12px;
			padding-right:5px;
			font-family:微软雅黑;
			color:red;
			font-werght:bold;
			line-height:28px;
		}
	</style>
</head>
<body>
<s:form name="eidtForm" id="editForm" theme="simple">
<table width="100%">
	<tr>
		<td colspan="2">
			<div class="tools_nav" style="padding-left:5px;">
		 	</div>
		</td>
	</tr>
	<tr>
		<td>
			<div>
		 		<table width="100%" cellspacing="0" cellpadding="0" border="0">
		 		
		 		 <s:if test="mailOwnerModel!=null">
		 		 <tr>
		 			<td class="headTitle">收件人:</td>
		 			<td class="headData"><s:property value="mailOwnerModel.owner"/></td>
		 		</tr> 
		 		<tr>
		 			<td class="headTitle">收件人:</td>
		 			<td class="headData"><s:property value="_to"/></td>
		 		</tr> 
		 		<tr>
		 			<td class="headTitle">发件人:</td>
		 			<td><s:property value="_mailFrom"/></td>
		 		</tr>
		 		<tr>
		 			<td class="headTitle">时间:</td>
		 			<td class="headData"><s:property value="_createDate"/> </td>
		 		</tr>
		 		
		 		<tr>
		 			<td class="headTitle">正文:</td>
		 			<td class="headData"> <s:property value="_content"/>
					</td>
		 		</tr>
		 		</s:if>
		 		<s:if test="mailTaskModel!=null">
		 		 <tr>
		 			<td class="headTitle">收件人:</td>
		 			<td class="headData"><s:property value="mailTaskModel.owner"/></td>
		 		</tr> 
		 		<tr>
		 			<td class="headTitle">收件人:</td>
		 			<td class="headData"><s:property value="_to"/></td>
		 		</tr> 
		 		<tr>
		 			<td class="headTitle">发件人:</td>
		 			<td><s:property value="_mailFrom"/></td>
		 		</tr>
		 		<tr>
		 			<td class="headTitle">时间:</td>
		 			<td class="headData"><s:property value="_createDate"/> </td>
		 		</tr>
		 		
		 		<tr>
		 			<td class="headTitle">正文:</td>
		 			<td class="headData"> <s:property value="_content"/>
					</td>
		 		</tr>
		 		</s:if>
		 		</table>
		 	</div>
		</td>
	</tr>
</table>
</s:form>
</body>
</html>

