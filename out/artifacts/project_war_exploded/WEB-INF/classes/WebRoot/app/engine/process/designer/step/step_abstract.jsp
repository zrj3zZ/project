<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title><s:property value="title"/>-节点摘要设置</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/processDesignTab.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js" ></script>
	<script type="text/javascript" src="iwork_js/metadata_index.js" charset="gb2312"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
	var api = art.dialog.open.api, W = api.opener;
	function doSubmit(){
	    var stepAbstract = $.trim($('#editForm_model_stepAbstract').val());
	     $('#editForm_model_stepAbstract').val(stepAbstract);
	    if(length2(stepAbstract)>500){	          
	          art.dialog.tips("您设置的摘要过长，最多录入500字符！");
	          $('#editForm_model_stepAbstract').focus();
	          return;
	    }
	    $('#editForm').submit();
	}
	</script>
  </head>
  
<body class="easyui-layout">
<!-- TOP区 -->
	<div region="west" border="false" style="width:180px;padding:3px;border-right:1px solid #efefef">
		<s:property value="stepToolbar" escapeHtml="false"/>
	</div>
    <!-- 导航区 -->
	<div region="center" style="padding:0px;border:0px">
		<div class="tools_nav">
			<a href="javascript:doSubmit();" class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>
			<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		</div>
		 
	
		<div>
		 <s:form name="editForm" id="editForm" action="sysFlowDef_stepMap!save.action">
		  <s:textarea  name="model.stepAbstract" cssStyle="width:650px;height:320px;"/>
				
		<s:hidden name="model.id"/>
		<s:hidden name="model.prcDefId"/>
		<s:hidden name="model.actDefId"/>
		<s:hidden name="model.actStepId"/>
		<s:hidden name="model.stepTitle"/>
		<s:hidden name="model.isAddOpinion"/>
		<s:hidden name="model.isDisplayOpinion"/>
		<s:hidden name="model.isCc"/>
		<s:hidden name="model.isForward"/>
		<s:hidden name="model.isAddsign"/>
		<s:hidden name="model.isTalk"/>
		<s:hidden name="model.isTrans"/>
		<s:hidden name="model.isWait"/>
		<s:hidden name="model.isPrint"/>
		<s:hidden name="model.stepType"/>
		<s:hidden name="model.stepMax"/>
		<s:hidden name="model.stepPurview"/>
		<s:hidden name="model.sendSysmsg"/>
		<s:hidden name="model.sendEmail"/>
		<s:hidden name="model.sendIm"/>
		<s:hidden name="model.sendSms"/>
		<s:hidden name="model.standardTime"/>
		<s:hidden name="model.warningTime"/>
		<s:hidden name="model.stepHelp"/>

	
			        	<s:hidden name="actDefId"/>
		                <s:hidden name="actStepDefId"/>
		                <s:hidden name="prcDefId"/>
			        	<s:hidden name="pageindex"></s:hidden>
	</s:form>	
  </div>			
</div>	
</body>
</html>
