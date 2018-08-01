<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>流程任务批量处理</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/process_center.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/process_batch.css">
    <link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
    <link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/pformpage.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/easyui/jquery.easyui.min.js"></script>
	 <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>   
    <script type="text/javascript">
    
    	function openProcess(actDefId,instanceId,excutionId,taskId) {
			var url = 'loadProcessFormPage.action?actDefId='+actDefId+'&instanceId='+instanceId+'&excutionId='+excutionId+'&taskId='+taskId;
			var target = instanceId;
			var page = window.open('form/loader_frame.html',target,'width='+screen.width-50+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location=url;
			return;
		}
		 
		function executeHandle(){
    			$("#editForm").attr("action","processBatchHandleOperate.action");
    			$("#editForm").submit();
		}
    </script>
    	<style>
		a.btn{ height:40px; line-height:40px; width:233px; font-family:微软雅黑; font-size:16px; text-align:center; color:#fff; display:block; cursor:pointer; float:left; margin:0px 5px;}
		.sumit1{ background:url(iwork_img/desk/trans_btn1.png) no-repeat;}
		.sumit2{ background:url(iwork_img/desk/trans_btn2.png) no-repeat;}
		.sumit3{ background:url(iwork_img/desk/trans_btn3.png) no-repeat;}
		.reject1{ background:url(iwork_img/desk/back_btn1.png) no-repeat;}
		.reject2{ background:url(iwork_img/desk/back_btn1.png) no-repeat;}
		.reject3{ background:url(iwork_img/desk/back_btn1.png) no-repeat;}
    </style>
</head>
<body class="easyui-layout" >
<s:form name="editForm" id="editForm">
<div region="north" border="false" style="height:35px;padding:5px;font-size:20px;border-bottom:1px solid #fa7a20;background:#fafafa;" split="false" id="layoutNorth">
	批量办理
</div> 
	<div region="center" border="false" style="border-left:1px solid #efefef;padding-top:10px" id="layoutCenter" >
	
	<fieldset  class="td_memo" style="padding:5px;color:#666">
								<legend class="td_memo_title">办理意见</legend>
								<textarea name="opinion" cols="" rows="" id="opinion" style="width:300px;height:80px;border:1px solid #ccc;background-color:#FEFFEC;font-size:16px;color:red"></textarea>
								</fieldset>
	<fieldset  class="td_memo" style="padding:5px;color:#666">
			<legend class="td_memo_title">批量办理任务</legend>
			<s:property value="content" escapeHtml="false"/>
	</fieldset>
	<s:hidden name="prcDefId"></s:hidden>
	<s:hidden name="actDefId"></s:hidden>
	<s:hidden name="actStepId"></s:hidden>
	<s:hidden name="taskId"></s:hidden>
	<s:hidden name="formId"></s:hidden>
    </div>
   <div region="south" border="false" style="height:100px;text-align:right;border-top:1px solid #fa7a20;padding:5px;"split="false" id="layoutNorth">
   <table width="100%">
   		<tr>
   		
   		</tr>
   </table>
   <s:property value="actionBtn" escapeHtml="false"/>
	</div> 
	</s:form>
</body>
</html>