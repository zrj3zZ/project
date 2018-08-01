<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!--
User interface templet,HTML
 
AWS is a application middleware	for BPM	system,Powered by actionsoft,China.
Copyright(C)2001,2002,2003,2004,2005,2006 Actionsoft Co.,Ltd
-->
 
 
<html>
<head>
<title>资产领用车</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
   <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
    <script type="text/javascript" src="iwork_js/commons.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
<style>
		.contractTable{
			border:1px solid #f7f7f7;
		}
		.contractTable th{
			height:25px;
			background:#efefef;
			font-size:14px;
			font-family:微软雅黑;
			padding:2px;
		}
		.contractTable td{
			height:25px;
			background:#fff;
			font-family:微软雅黑;
			padding:2px;
			border-bottom:1px solid #f7f7f7;
			color:#666;
		}
		.contractTable tr:hover td{
			background:#f7f7f7;
			border-bottom:1px solid #efefef;
			cursor:pointer;
		}
		.subTable{
			border:1px solid #f7f7f7;
		}
		.subTable th{
			height:25px;
			background:#FFFFEE;
			font-size:12px;
			font-family:微软雅黑;
			padding:2px;
			color:#959591;
			border-bottom:1px solid #efefef;
		}
		.subTable td{
			height:25px;
			background:#fff;
			font-family:微软雅黑;
			padding:2px;
			border-bottom:1px solid #f7f7f7;
			color:#959591;
		}
		.subTable tr:hover td{
			background:#f7f7f7;
			border-bottom:1px solid #efefef;
			cursor:pointer;
		}
	</style>
<script type="text/javascript"> 
 function selectTabs(djbh){
			
			 var pageUrl = "iwork_bgyp_selectTabByDJBH.action?djbh="+djbh;
	         parent.addTab("办公用品领用记录详情",pageUrl);
	         
		}
</script>
</head>
<body class="easyui-layout">
         <div region="center" border="false" style="background: #fff; border-top:1px solid #efefef;padding:5px;">
            <table width="100%" class="contractTable">
            
            	<tr>
            		<th style="width:10%">领取人</th>
	            	<th style="width:15%">申请时间</th>
	            	<th style="width:15%">单据编号</th>
	            	<th style="width:15%">申请部门</th>
	            	<th style="width:15%">总价</th>
	            	<th style="width:15%">数量总计</th>
	            	<th style="width:15%">申请说明</th>
            	</tr>
 				<s:iterator value="list" status="status">
	            	<tr>
	            		<td style="width:15%"><s:property value="SQRXM"/></td>
	            		<td style="width:15%"><s:property value="SQRQ"/></td>
	            		<td style="width:15%"><a href="#" onclick="selectTabs('<s:property value="DJBH"/>')"><s:property value="DJBH"/></a></td>
	            		<td style="width:15%"><s:property value="SQLRBMMC"/></td>
	            		<td style="width:15%"><s:property value="ZJ"/></td>
	            		<td style="width:15%"><s:property value="SLZJ"/></td>
	            		<td style="width:15%"><s:property value="SQSM"/></td>
	            	</tr>
            	</s:iterator>
            </table>
          </div>
</body>
</html>

