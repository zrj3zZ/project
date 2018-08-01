<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"  ></script>
	<script language="javascript" src="iwork_js/commons.js"></script> 
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript"> 
	var api = art.dialog.open.api, W = api.opener;
		function doReback(){
			var resion = $("#resion").val();
			if(resion==""){
				art.dialog.tips("请填写休眠原因",3);
				$("#resion").focus();
				return false;
			}else{ 
				var taskId = $("#editForm_taskId").val();
				var pageURL = "processManagement_sleepTask.action?taskId="+taskId;
				 $.post(pageURL,{resion:resion},function(data) {
				    	if(data=='success'){ 
				    		art.dialog.tips("已将当前任务设置成休眠状态",2);
				    		parent.$('#task_list_grid').trigger('reloadGrid');
				    		api.close();
				    	}else{
				    		try{ 
						    	showSysTips();
							 }catch(e){}
				    		art.dialog.tips("任务设置休眠失败，请联系管理员",5)
				    	}
				 });  
				
			}
			
		}
	</script> 
</head>
<body  class="easyui-layout" >

 	<div region="center" style="border:1px;padding:10px 10px 10px 10px;">
 	<s:form name="editForm" id="editForm" theme="simple">
			<div style="margin-top:10px;"> 
			 <fieldset>
			         <legend>休眠原因:</legend>
			         <textarea name="resion" id="resion" style="background-color:#FFFAF1;width:470px;height:100px;"></textarea>
			</fieldset>
				
			</div>
			<div style="padding:5px;color:red;line-height:20px;">
				警告：1、执行休眠操作后，将不再对该任务进行提醒或催办等操作。<br/>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2、休眠后将给申请人发送休眠提醒。
					<s:hidden name="taskId"></s:hidden>
				
			</div>
			</s:form>
		</div>
		<div region="south" border="false" style="border-top:1px solid #ccc;padding:10px;text-align:right;padding-left:10px;">
		<a href="#" onclick="doReback()"  class="easyui-linkbutton" plain="false" iconCls="icon-ok">执行休眠</a>
		<a id="btnEp" class="easyui-linkbutton" icon="icon-cancel" href="javascript:api.close();" >关闭</a>
		</div>
</body>
</html>
