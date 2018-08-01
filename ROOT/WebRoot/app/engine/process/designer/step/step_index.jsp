<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/ui/base/jquery.ui.all.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<script type="text/javascript" src="../iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="../iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link href="iwork_css/reset.css" rel="stylesheet" type="text/css"/>
		<style> 
		<!--
			#header { background:#6cf;}
			#title { height:20px; background:#EFEFEF; border-bottom:1px solid #990000; font:12px; font-family:宋体; padding-left:5px; padding-top:5px;}
			#baseframe { margin:0px;background:#FFFFFF; border:1px solid #CCCCCC;}
			#baseinfo { height:30px; background:#FFFFFF; padding:5px;font:12px; font-family:宋体;}
			.toobar{
				 border-bottom:1px solid #990000; 
				 padding-bottom:5px; 
			}
			/*只读数据样式*/
			.readonly_data {
				vertical-align:bottom;
				font-size: 12px;
				line-height: 20px;
				color: #888888;
				padding-right:10px;
				border-bottom:1px #999999 dotted;
				font-family:"宋体";
				line-height:15px;
			}
			/*数据字段标题样式*/
			.td_title {
			color:#004080;
				line-height: 23px;
				font-size: 12px;
				text-align: right;
				letter-spacing: 0.1em;
				padding-right:10px;
				white-space:nowrap;
				border-bottom:1px #999999 thick;
				vertical-align:middle;
				font-family:"宋体";
			}
			/*数据字段内容样式*/
			.td_data {
				color:#0000FF;
				line-height: 23px;
				text-align: left;
				padding-left: 3px;
				font-size: 12px;
				font-family:"宋体";
				border-bottom:1px #999999 dotted;
				vertical-align:middle;
				word-wrap:break-word;
				word-break:break-all;
				font-weight:500;
				line-height:15px;
				padding-top:5px;
			}
			#item li{
				 float:left;   width:70px;   list-style:none;
			}
		-->
		</style>
		<script type="text/javascript">
			function iFrameHeight(obj) { 
				var ifm= document.getElementById(obj); 
				if(ifm != null) { 
					ifm.height = document.body.scrollHeight-50; 
				} 
			} 
			
		</script>
</head> 
<body class="easyui-layout">
<div  border="false" style="padding:0px;overflow:no;height:40px;border-bottom:1px solid #efefef">
	<!--  
	<div style="padding:2px;text-align:left;">
	
		<ul id="item">
			<li><a target="stepIndexFrame" href="sysFlowDef_stepMap!load.action?pageindex=0&prcDefId=<s:property value='prcDefId'/>&actDefId=<s:property value='actDefId'/>&actStepDefId=<s:property value='actStepDefId'/>">任务设置</a></li>
			<li><a target="stepIndexFrame" href="sysFlowDef_stepRoute!load.action?pageindex=1&prcDefId=<s:property value='prcDefId'/>&actDefId=<s:property value='actDefId'/>&actStepDefId=<s:property value='actStepDefId'/>">路由策略</a></li>
			<li><a target="stepIndexFrame" href="sysFlowDef_stepFormSet!load.action?pageindex=2&prcDefId=<s:property value='prcDefId'/>&actDefId=<s:property value='actDefId'/>&actStepDefId=<s:property value='actStepDefId'/>">表单设置</a></li>
			<li><a target="stepIndexFrame" href="sysFlowDef_stepManualRoute!load.action?pageindex=3&prcDefId=<s:property value='prcDefId'/>&actDefId=<s:property value='actDefId'/>&actStepDefId=<s:property value='actStepDefId'/>">人工跳转</a></li>
			<li><a target="stepIndexFrame" href="sysFlowDef_stepSysRoute!load.action?pageindex=4&prcDefId=<s:property value='prcDefId'/>&actDefId=<s:property value='actDefId'/>&actStepDefId=<s:property value='actStepDefId'/>">规则跳转</a></li>
			<li><a target="stepIndexFrame" href="sysFlowDef_stepSupervise!load.action?pageindex=5&prcDefId=<s:property value='prcDefId'/>&actDefId=<s:property value='actDefId'/>&actStepDefId=<s:property value='actStepDefId'/>">任务督办</a></li>
			<li><a target="stepIndexFrame" href="sysFlowDef_stepTrigger!load.action?pageindex=6&prcDefId=<s:property value='prcDefId'/>&actDefId=<s:property value='actDefId'/>&actStepDefId=<s:property value='actStepDefId'/>">触发器设置</a></li>
		</ul> 
	</div>
</div>
<div region="center"  style="padding:3px;border:0px;">
<iframe width="100%"  id="stepIndexFrame" name="stepIndexFrame" scrolling='auto' onLoad="iFrameHeight('stepIndexFrame')"  frameborder="0"  src="sysFlowDef_stepMap!load.action?pageindex=0&prcDefId=<s:property value='prcDefId'/>&actDefId=<s:property value='actDefId'/>&actStepDefId=<s:property value='actStepDefId'/>"></iframe>
</div>-->
	<s:property value="stepToolbar" escapeHtml="false"/>
</body> 
</html>
