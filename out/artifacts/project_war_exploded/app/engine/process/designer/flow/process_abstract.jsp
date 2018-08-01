<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>摘要设置</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/processDesignTab.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js" ></script>
	<script charset="utf-8" src="iwork_js/kindeditor/kindeditor.js"></script>
	<script charset="utf-8" src="iwork_js/kindeditor/lang/zh_CN.js"></script>
	<script charset="utf-8" src="iwork_js/kindeditor/plugins/code/prettify.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">

	function doSubmit(){
	     var prcAbstract = $.trim($('#editForm_model_prcAbstract').val());
	     $('#editForm_model_prcAbstract').val(prcAbstract);
	    if(length2(prcAbstract)>500){	         
	          art.dialog.tips("摘要过长！",2);
	          $('#editForm_model_prcAbstract').focus();
	          return;
	    }
	    $('#editForm').submit();
	}
	</script>
  </head>
  
<body class="easyui-layout">
    <!-- 导航区 -->
    <div region="north" border="false" style="height:40px;">
		<div class="tools_nav">
			<a href="javascript:doSubmit();" class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>
			<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		</div>
	</div>
	<div region="center" style="padding:3px;border:0px">
		<div>
		<s:form name="editForm" id="editForm" action="sysFlowDef_PropertyIndex!save.action">
		    <s:textarea  name="model.prcAbstract" cssStyle="width:780px;height:400px;"/>
		
		
		<s:hidden name="model.id"/>
		<s:hidden name="model.prcDefId"/>
		<s:hidden name="model.actDefId"/>
		<s:hidden name="model.defTitle"/>
		<s:hidden name="model.isTalk"/>
		<s:hidden name="model.isCcenter"/>
		<s:hidden name="model.isMonitor"/>
		<s:hidden name="model.isDelegate"/>
		<s:hidden name="model.engineType"/>
		<s:hidden name="model.formList"/>
		<s:hidden name="model.visitType"/>
		<s:hidden name="model.standardTime"/>
		<s:hidden name="model.warningTime"/>
		<s:hidden name="model.prcHelp"/>
	
	
			        	<s:hidden name="edittype"/>
		                <s:hidden name="actdefId"/>
		                <s:hidden name="prcDefId"/>
			        	<s:hidden name="type"></s:hidden>
	</s:form>	
	</div>	
</div>	
</body>
</html>
