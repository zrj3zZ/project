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
				art.dialog.tips("请填写收回原因",3);偶
				$("#resion").focus();
				return false;
			}else{
				 $.post('processManagement_doStepReback.action',$("#editForm").serialize(),function(data) {
				    	if(data=='success'){
				    		art.dialog.tips("任务收回成功",3);
				    		api.close();
				    	}else{ 
				    		try{
						    	showSysTips();
							 }catch(e){}
				    		art.dialog.tips("任务撤销失败，请联系管理员",5)
				    	}
				 });  
				
			}
			
		}
	</script> 
	<style>
		#info td{
			color:#0000ff;
			line-height:16px;
		}
	</style>
</head>
<body  class="easyui-layout" >

 	<div region="center" style="border:1px;padding:10px 10px 10px 10px;">
 	<s:form name="editForm" id="editForm" theme="simple">
	 <fieldset>
			         <legend>流程信息:</legend>
			         <table id="info">
			         	<tr>
			         		<td>流  程 标 题 :</td>
			         		<td><s:property value="title"/></td>
			         	</tr>
			         	<tr>
			         		<td>当前办理人:</td>
			         		<td><s:property value="currentUser"/></td>
			         	</tr>
			         	<tr>
			         		<td>读 取 状 态 :</td>
			         		<td><s:property value="readStatus"/></td>
			         	</tr>
			         	<s:if test="RemindTypeList!=null">
						<tr>
						<td class="ItemTitle">提  醒 方 式:	</td><td  class="pageInfo">
							 <s:checkboxlist name="remindType" id="remindType" list="remindTypeList" 
					         labelposition="top"
					         listKey="key"
					         listValue="value" 
					         value="remindTypeList"
					         >
					        </s:checkboxlist>
						</td>
					</tr>
					</s:if>
			         </table>
			</fieldset>
			<div style="margin-top:10px;"> 
			 <fieldset>
			         <legend>收回原因:</legend>
			         <textarea name="resion" id="resion" style="background-color:#FFFAF1;width:470px;height:90px;"></textarea>
			</fieldset>
				
			</div>
			<div style="padding:5px;color:red;line-height:20px;">
				提示：执行任务收回后，任务将收回至前账户，系统将保留收回原因记录。继续流转时，将按照原流程重新流转
				，任务当前办理人已读任务时，无法进行单步撤销操作。
					<s:hidden name="taskId"></s:hidden>
			</div>
			</s:form>
		</div>
		<div region="south" border="false" style="border-top:1px solid #ccc;padding:10px;text-align:right;padding-left:10px;">
		<a href="#" onclick="doReback()"  class="easyui-linkbutton" plain="false" iconCls="icon-undo">执行撤销</a>
		<a id="btnEp" class="easyui-linkbutton" icon="icon-cancel" href="javascript:api.close();" >关闭</a>
		</div>
</body>
</html>
