<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>帮助设置</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
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
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
	var editor1 ;
		KindEditor.ready(function(K) {
			editor1 = K.create('textarea[name="model.prcHelp"]', { 
				cssPath : 'iwork_css/formstyle.css',
				uploadJson : '../jsp/upload_json.jsp',
				fileManagerJson : '../jsp/file_manager_json.jsp',
				width : '780px', 
				items : [
					'source' ,'|', 'preview', '|', 'undo', 'redo' ,'template', 'code', 'cut', 'copy', 'paste',
					'plainpaste', 'wordpaste', '|', 'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
					'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|','justifyleft', 'justifycenter', 'justifyright',
					'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
					'superscript', 'clearhtml', 'quickformat', 'selectall',   
					'flash', 'media', 'insertfile', 'table', 'hr',   'pagebreak',
					'anchor', 'link', 'unlink'
				],
				themeType:'simple',
				newlineTag:'br',
				height : '330px',
				filterMode:false
			});
			prettyPrint();
		});
	function doSubmit(){
		editor1.sync();//同步html编辑器
	     var prcHelp = $.trim($('#editForm_model_prcHelp').val());
	     $('#editForm_model_prcHelp').val(prcHelp);
	    if(length2(prcHelp)>500){	         
	          art.dialog.tips("帮助过长！",2);
	          $('#editForm_model_prcHelp').focus();
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
    <!-- 导航区 -->
	<div region="center" style="padding:3px;border:0px">
		<div>
		<s:form name="editForm" id="editForm" action="sysFlowDef_PropertyIndex!save.action">
		    <s:textarea  name="model.prcHelp"></s:textarea>
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
		<s:hidden name="model.prcAbstract"/>
	
	
			        	<s:hidden name="edittype"/>
		                <s:hidden name="actdefId"/>
		                <s:hidden name="prcDefId"/>
			        	<s:hidden name="type"></s:hidden>
	</s:form>	
	</div>	
</div>	
</body>
</html>
