<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script> 
	 <script type="text/javascript" src="iwork_js/commons.js" ></script>
	 <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<link href="iwork_css/reset.css" rel="stylesheet" type="text/css" />
	<style type="text/css">
		.td_title {
				color:#004080;
				font-size: 12px;
				text-align: right;
				letter-spacing: 0.1em;
				padding-right:10px;
				white-space:nowrap;
				vertical-align:middle;
				font-family:"微软雅黑";
				height:15px;
				
			}
		.td_data{
			color:#0000FF;
			text-align:left;
			padding-left:3px;
			font-size: 12px;
			vertical-align:middle;
			word-wrap:break-word;
			word-break:break-all;
			font-weight:500;
			line-height:12px;
			padding-top:5px;
			font-family:"微软雅黑";
			height:15px;
		}
		input {
		 color:#0000FF;
		}
		textarea{
		color:#0000FF;
		}
		body {
			margin-left: 0px;
			margin-top: 0px;
			margin-right: 0px;
			margin-bottom: 0px;
		}
		.state{
		color:red;
		font-size:12px;
		}
	</style>
	<script type="text/javascript">
	//全局变量
    var api = art.dialog.open.api, W = api.opener;
     var mainFormValidator;
		$().ready(function() {
			mainFormValidator =  $("#editForm").validate({
				debug:false
			 });
			 mainFormValidator.resetForm();
		});
		
		//表单提交
		function doSubmit(){ 
          var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){
					return;
				}
            var options = {
				error:errorFunc,
				success:successFunc 
			   };
			$('#editForm').ajaxSubmit(options);
       }
      errorFunc=function(){
           art.dialog.tips("保存失败！",2);
      } 
      successFunc=function(responseText, statusText, xhr, $form){
           if(responseText=="ok"){
              art.dialog.tips("保存成功！");
              setTimeout('cancel();',1000);
           }
           else if(responseText=="error"){
              art.dialog.tips("保存失败！");
           } 
      }
		//执行退出
		function cancel(){
			api.close();
		}
	</script>
</head>
<body  class="easyui-layout">
		<div region="center" style="padding:13px;border:0px">
            	<s:form id ="editForm" name="editForm" action="sysFlowDef_stepOffLine_save.action"  theme="simple">
	            	<table border="0"  cellspacing="0" cellpadding="0">
	            		<tr>
	            			<td class="td_title">线下任务名称:</td>
	            			<td class="td_data">
	            				<s:textfield name = "model.title"  cssClass="{maxlength:32,required:true}"  theme="simple" cssStyle="width:100px;"></s:textfield>
	            				<span style='color:red'>*</span>	            				
	            			</td>
	            		</tr>
	            		<tr>
	            			<td class="td_title">是否必填:</td>
	            			<td class="td_data">
								<s:radio name="model.isCheck"  cssClass="{required:true}"  list="#{'1':'是','0':'否'}" theme="simple"> </s:radio>
							</td>
	            		</tr>
	            		
	            		<tr>
	            			<td class="td_title">线下任务描述：</td>
	            			<td class="td_data" >
	            				<s:textarea name = "model.memo"  cssClass="{maxlength:200,required:true}"  cssStyle="width:250px;height:100px" theme="simple"></s:textarea>
								<span id="msg_user" style="color:red;display:none;">*</span>
							</td>
	            		</tr>
	            	</table>
	                <s:hidden name="model.actDefId"/>
	                <s:hidden name="model.actStepId"/>
	                <s:hidden name="model.prcDefId"/>
	                <s:hidden name="model.orderIndex"/>
	                <s:hidden name="model.id"/>
	                <s:hidden name="actDefId"/>
	                <s:hidden name="actStepDefId"/>
	                <s:hidden name="prcDefId"/>
	                <s:hidden name="id"/>
	                 </s:form> 
	     </div>
	                <div region="south" border="false" style="text-align: right; height: 50px; line-height: 30px;padding-top:5px;padding-right:15px;">
		                <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:doSubmit();" >
		                    保存</a> <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:cancel()">关闭</a>
		            </div>
              
           
</body>
</html>
