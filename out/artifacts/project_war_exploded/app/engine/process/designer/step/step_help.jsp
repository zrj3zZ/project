<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title><s:property value="title"/>-帮助设置</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/process/process_step.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/processDesignTab.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js" ></script>
	<link rel="stylesheet" href="iwork_js/kindeditor/themes/simple/simple.css" />
	<link rel="stylesheet" href="iwork_js/kindeditor/plugins/code/prettify.css" />
	<script charset="utf-8" src="iwork_js/kindeditor/kindeditor.js"></script>
	<script charset="utf-8" src="iwork_js/kindeditor/lang/zh_CN.js"></script>
	<script charset="utf-8" src="iwork_js/kindeditor/plugins/code/prettify.js"></script>
	<script type="text/javascript" src="iwork_js/metadata_index.js" charset="gb2312"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
	var editor1 ;
	KindEditor.ready(function(K) {
			 editor1 = K.create('textarea[name="model.stepHelp"]', { 
				cssPath : 'iwork_css/formstyle.css',
				uploadJson : '../jsp/upload_json.jsp',
				fileManagerJson : '../jsp/file_manager_json.jsp',
				width : '650px', 
				themeType:'simple',
				items : [
					'source' ,'|', 'preview', '|', 'undo', 'redo' ,'template', 'code', 'cut', 'copy', 'paste',,   
					'flash', 'media', 'insertfile', 'table', 'hr',   'pagebreak',
					'anchor', 'link', 'unlink',
					'plainpaste', 'wordpaste', '|', '/', 'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
					'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|','justifyleft', 'justifycenter', 'justifyright',
					'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
					'superscript', 'clearhtml', 'quickformat', 'selectall'
				], 
				newlineTag:'br',
				height : '330px',
				filterMode:false
			});
			prettyPrint();
		});
	function doSubmit(){
		editor1.sync();//同步html编辑器 
	     var stepHelp = $.trim($('#editForm_model_stepHelp').val());
		
	    if(length2(stepHelp)>500){	         
	          art.dialog.tips("帮助过长！");
	          $('#editForm_model_stepHelp').focus();
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
	<div region="north" border="false" style="height:40px">
		<div class="stepTitle">
			<s:property value="title"/><img src="iwork_img/gear.gif" style="float:right;height:25px" alt="节点设置"/>
		</div>
	</div>
    <!-- 导航区 -->
	<div region="center" style="padding:0px;border:0px">
		<div class="tools_nav">
			<a href="javascript:doSubmit();" class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>
			<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		</div>
		 	 
		<div>
		<s:form name="editForm" id="editForm" action="sysFlowDef_stepMap!save.action">
		    <s:textarea  name="model.stepHelp" cssStyle="width:650px;height:320px;"/>
		
		
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
		<s:hidden name="model.stepAbstract"/>
	
	
			        	<s:hidden name="actDefId"/>
		                <s:hidden name="actStepDefId"/>
		                <s:hidden name="prcDefId"/>
			        	<s:hidden name="pageindex"></s:hidden>
	</s:form>	
	</div>	
</div>	
</body>
</html>
