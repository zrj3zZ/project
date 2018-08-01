<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page contentType="text/html; charset=utf-8"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><s:property value='title' escapeHtml='false'/></title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
	<script type="text/javascript" src="iwork_js/commons.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"   charset="utf-8" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.src.js"  charset="utf-8"  > </script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"  charset="utf-8"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"  charset="utf-8" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"  charset="utf-8"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/engine/iformpage.js"  charset="utf-8" ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	
	 <script type="text/javascript">
	 	function openMonitorPage(){
	 			var actDefId = $("#actDefId").val();
				var actStepDefId = "";
				var instanceId = $("#instanceId").val();
				var prcDefId = $("#prcDefId").val();
				var excutionId = instanceId;; 
				var pageUrl = "processInstanceMornitor.action?actDefId="+actDefId+"&prcDefId="+prcDefId+"&instanceId="+instanceId+"&excutionId="+excutionId;
				art.dialog.open(pageUrl,{ 
					title:"流程跟踪",
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:800,
					height:600
				 });
	 	}
	 	
	</script>
	<script type="text/javascript">
		 <s:property value='script' escapeHtml='false'/>
	</script>
	
	<style> 
		<s:property value="style" escapeHtml="false"/>
	</style>
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css"/>
	
</head>
<body class="easyui-layout">
	<div id="blockPage" class="black_overlay" style="display:none"></div> 
	<div region="north" style="height:40px;" border="false" >
		<div  class="tools_nav">
		<table width="100%"><tr>
			<td align="left">
			<s:property value="toolbar" escapeHtml="false"/>
			</td>
			<td style="text-align:right;padding-right:10px">
			</td>
		</tr></table>
		
		</div>
	</div>
	<div region="center" style="text-align:center;border-left:1px #999 dotted;border-right:1px #999 dotted;border-top:1px #999 dotted;border-bottom:0px #999 dotted;padding:2px;">
	<form   id="iformMain" name="iformMain" method="post" action='saveIform.action'>
		<!--表单参数-->
		<span style="display:none">
			<s:hidden name="modelId"/>
			<s:hidden name="modelType"/> 
			<s:hidden name="isLog"/> 
			<s:hidden name="formid"/>
			<s:hidden name="instanceId"/> 
			<s:hidden name="actDefId" id="actDefId"/> 
			<s:hidden name="prcDefId" id="prcDefId"/> 
			<s:hidden name="dataid"/> 
			<s:hidden name="formContent"/>
			<input name='submitbtn' id='submitbtn' type="submit" />
			<s:property value='param' escapeHtml='false'/>
		</span>
		<s:property value='content' escapeHtml='false'/>
	</form>
	</div>
	
</body>
</html>
 
