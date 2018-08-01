<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
	<script type="text/javascript">
			$(document).bind("contextmenu",function(){return false;});
			$(function() { 
					$('.left-nav a').click(function(ev) {
						$('.left-nav a.selected').removeClass('selected');
						$(this).addClass('selected');
					});
			});
	</script>
	<script type="text/javascript">
		function showStepDef(actDefId,prcDefId,stepid){
			var url = "sysFlowDef_stepMap!load.action?actDefId="+actDefId+"&prcDefId="+prcDefId+"&actStepDefId="+stepid;
			window.open(url,"stepDefFrame",""); 
		}
	</script>
	<style type="text/css">
		.listTitle{
			border-bottom:1px solid #efefef;
			background-color:#efefef;
			padding-left:5px;
		}
	</style>
</head>
<body>
					<table class="layout-grid" cellspacing="0" cellpadding="0" style="width:170px;" >
						<tr>
							<td class="left-nav">
								<dl class="demos-nav">
									<dd><a id="sysEngineIForm_baseInfo" class="selected" href="sysEngineIForm_edit.action?id=<s:property value='formid'  escapeHtml='false'/>" target="iformMapIndexFrame">基本信息</a></dd>
									<dd><a id="sysEngineIForm_index" href="sysEngineIFormMap!index.action?formid=<s:property value='formid'  escapeHtml='false'/>" target="iformMapIndexFrame">域设置</a></dd>
									<dd><a id="sysEngineIForm_subform" href="sysEngineSubform_listpage.action?formid=<s:property value='formid'  escapeHtml='false'/>" target="iformMapIndexFrame">行项目子表</a></dd>
									<dd><a id="sysEngineIForm_directory" href="sys_dictionary_design_index.action?groupid=<s:property value='formid'  escapeHtml='false'/>" target="iformMapIndexFrame">选择器设置[数据字典]</a></dd>
									<dd><a id="sysEngineIForm_style" href="sysEngineIForm_formcss_load.action?id=<s:property value='formid'  escapeHtml='false'/>" target="iformMapIndexFrame">表单样式</a></dd>
									<dd><a id="sysEngineIForm_script" href="sysEngineIForm_formjs_load.action?id=<s:property value='formid'  escapeHtml='false'/>"" target="iformMapIndexFrame">页面脚本设置</a></dd>
									<dd><a id="sysEngineIForm_designer" href="sysEngineIformDesginer_show.action?formid=<s:property value='formid'  escapeHtml='false'/>" target="iformMapIndexFrame">表单设计</a></dd>
								</dl> 
							</td> 
						</tr>
					</table>
</body>
</html>
