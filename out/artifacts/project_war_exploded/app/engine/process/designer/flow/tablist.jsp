<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<title>流程模型设置</title>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
<script type="text/javascript" src="iwork_js/system/syspersion_lefttitle.js"></script>
</head>
<body>

<table class="layout-grid" cellspacing="0" cellpadding="0" style="width:170px;" > 
	<tr>
		<td class="left-nav">
			<dl class="demos-nav">
				<dd><a id="sysFlowDef_baseInfo" href="sysFlowDef_baseInfo!load.action?edittype=tab&prcDefId=<s:property value='prcDefId' escapeHtml='false'/>&actdefId=<s:property value='actdefId'  escapeHtml='false'/>" target="sysDef_Designer_right">基本信息</a></dd>
				<dd><a id="sysFlowDef_PropertyIndex"  class="selected"  href="sysFlowDef_PropertyIndex!load.action?edittype=tab&prcDefId=<s:property value='prcDefId' escapeHtml='false'/>&actdefId=<s:property value='actdefId'  escapeHtml='false'/>&type=base" target="sysDef_Designer_right">流程设置</a></dd>
				<dd><a id="sysFlowDef_actionLoad" href="sysFlowDef_actionLoad.action?edittype=tab&prcDefId=<s:property value='prcDefId' escapeHtml='false'/>&actdefId=<s:property value='actdefId'  escapeHtml='false'/>" target="sysDef_Designer_right">流程动作设置</a></dd>
				<dd><a id="sysFlowDef_param" href="sysFlowDef_param!load.action?edittype=tab&prcDefId=<s:property value='prcDefId' escapeHtml='false'/>&actdefId=<s:property value='actdefId'  escapeHtml='false'/>" target="sysDef_Designer_right">流程变量</a></dd>
				<dd><a id="sysFlowDef_triger" href="sysFlowDef_trigger!load.action?edittype=tab&prcDefId=<s:property value='prcDefId' escapeHtml='false'/>&actdefId=<s:property value='actdefId'  escapeHtml='false'/>" target="sysDef_Designer_right">触发器设置</a></dd>
				<dd><a id="sysFlowDef_openProcessImage" href="processDeploy_openProcessImage.action?sysProcDefId=<s:property value='prcDefId' escapeHtml='false'/>&actProcId=<s:property value='actdefId'  escapeHtml='false'/>" target="sysDef_Designer_right">流程图</a></dd>
				<dd><a id="sysFlowDef_summary" href="process_summary.action?prcDefId=<s:property value='prcDefId' escapeHtml='false'/>&actDefId=<s:property value='actdefId'  escapeHtml='false'/>&type=abstract" target="sysDef_Designer_right">流程摘要设置</a></dd>
				<dd><a id="syspersion_loadOtherParamPage" href="sysFlowDef_PropertyIndex!load.action?edittype=tab&prcDefId=<s:property value='prcDefId' escapeHtml='false'/>&actdefId=<s:property value='actdefId'  escapeHtml='false'/>&type=help" target="sysDef_Designer_right">帮助设置</a></dd>
			</dl>
		</td>
	</tr>
</table> 
</body>
</html>

