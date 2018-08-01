<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<link href="iwork_css/eagles_searcher.css" rel="stylesheet" type="text/css"/>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style type="text/css">
	
	</style>
<script>
	function reload(){
	    this.location.reload();
	}
</script>
<style type="text/css">
a {
 font-size: 10pt; text-decoration: none; color: #0000ff;
 }
	.result_title{
		color:#333;
		text-align:left;
		text-decoration:none; 
	}
	.op{
		color:#333;
		text-align:left;
		text-decoration:none; 
	}
	.toolbar td{
			background:url(../iwork_img/engine/tools_nav_bg.jpg) repeat-x;
			height:34px;
			line-height:30px;
			padding-left:10px;
			vertical-align:middle;
			padding-top:2px;
			padding-bottom:2px; 
			border-right:1px #efefef;
	} 
	.itemdata td{
			height:34px;
			line-height:30px;
			padding-left:5px;
			padding-left:5px;
			vertical-align:middle;
			padding-top:2px;
			padding-bottom:2px; 
			border-right:1px #efefef solid;
			border-bottom:1px #efefef solid;
	}
</style> 
</head> 
<body class="easyui-layout">
	<div region="center" style="text-align:left;border:0px">
					<s:if test="list==null||list.size<=0">
						<div class="nofind">
						<img alt="查询信息未找到"  border="0" src="iwork_img/nondynamic.gif">未找到与“<s:property value="searcherTxt"/>”相关的内容!
						</div>
					</s:if> 
					<table width="100%" style="border:1px solid #efefef" cellpadding='1' cellspacing='0'>
							<tr class="toolbar">
								<td >申请人</td>
								<td>任务标题</td>
								<td>操作</td>
							</tr>
							<s:iterator  value="list" status="status"> 
								<tr class="itemdata">
								<td >[<s:property value="owner"  escapeHtml="false"/>] </td>
								<td class="result_title">
								<s:property value="description"  escapeHtml="false"/>
								</td>
									<td>
									<a target="_blank" href="loadProcessFormPage.action?actDefId=<s:property value="processDefinitionId"  escapeHtml="false"/>&instanceId=<s:property value="processInstanceId"  escapeHtml="false"/>&excutionId=<s:property value="executionId"  escapeHtml="false"/>&taskId=<s:property value="id"  escapeHtml="false"/>">表单链接</a>
									<a target="_blank" href="process_re_activation.action?actDefId=<s:property value="processDefinitionId"  escapeHtml="false"/>&instanceId=<s:property value="processInstanceId"  escapeHtml="false"/>&excutionId=<s:property value="executionId"  escapeHtml="false"/>&taskId=<s:property value="id"  escapeHtml="false"/>">表单链接</a>
									
									</td>
									
								</tr>
						  	</s:iterator> 
					</table>
		</div>
				
</body>
</html>
