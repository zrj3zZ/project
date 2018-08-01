<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>  
		<title></title>
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
		<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
		<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	     <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
		<style type="text/css">
		.form_title{  
			font-family:黑体;
			font-size:14px;
			text-align:right;
		}
		.form_data{
			font-family:宋体;
			font-size:12px;
			text-align:left;
			color:0000FF; 
		}
		</style>	
<script type="text/javascript">
		 var api = art.dialog.open.api, W = api.opener;
		 var mainFormValidator;
		 //添加验证脚本
		 mainFormValidator =  $("#editForm").validate({
				debug:true
		 });
		 //提交
		function doSubmit(){
			if($('#title').val()==''){
				art.dialog.tips("流程标题不能为空");
				$('#title').foucs();
				return false;
			}else if($('#prcKey').val()==''){
				art.dialog.tips("流程键值不能为空");
				$('#prcKey').foucs();
				return false;
			}
            var options = {
				error:errorFunc,
				success:successFunc 
			   };
			$('#editForm').ajaxSubmit(options);
      		}
		 errorFunc=function(){
          		art.dialog.tips("保存失败！");
    		 }
      	successFunc=function(responseText, statusText, xhr, $form){
           if(responseText!="error"&&responseText!=""){
              art.dialog.tips("保存成功！");
              //setTimeout('cancel();',1000);
              var title = $("#title").val();
              var prcKey = $("#prcKey").val(); 
              showDesiner(title,prcKey,responseText);
           }
           else if(responseText=="error"){
              art.dialog.tips("保存失败！");
           } 
      	}
      	//加载
		function loadProcessKey(){
			var title = $("#title").val();
			var pageurl = 'processDeploy_processkey_load.action';
			if(title!=''){
				$.post(pageurl,{processtitle:title},function(msg){ 
			               if(msg!=''){
			               		$("#prcKey").val(msg); 
			               }else{
			               		art.dialog.tips("提取失败");
			               }
			        });
			}else{
				art.dialog.tips("请填写流程标题"); 
			}
		}
		
		function showDesiner(title,prcKey,proId){
			var pageUrl = 'processDeploy_show_designer.action?processKey='+prcKey+'&id='+proId;
			art.dialog.open(pageUrl,{
			 	id:'openProcessDefWinDiv',
				title:'流程设计器['+title+']',  
				lock:true,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:'95%',
			    height:'95%'
			 });
		}
	     //执行退出
		function cancel(){
			api.close();
		}
		
		</script>
	</head>
	<body class="easyui-layout">
	<div region="south" split="true" style="border:0px;height:50px;text-align:right;color:#666;padding-right:10px">
		<a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="###" onclick="javascript:doSubmit();">设置流程</a> 
			<a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:cancel();">取消</a><br>
		
	</div>
	<div region="center"  style="border:0px;height:50px;padding:10px;background:#gegege" > 
	<s:form action="processDeploy_process_save" id="editForm" name="editForm" theme="simple">
			<table width="100%" border = "0">
				<tr> 
					<td class="form_title"><span style="color:red;">*</span>流程标题:</td>
					<td  class="form_data"><s:textfield id="title" name="model.title" theme="simple"/><a href="javascript:loadProcessKey();" style="font-family:宋体;font-size:12px;padding-left:5px;">生成流程键值</a></td>
				</tr>
				<tr> 
					<td class="form_title"><span style="color:red;">*</span>流程键值:</td>
					<td class="form_data"><s:textfield id = "prcKey" name="model.prcKey" theme="simple"/></td>
				</tr>
				<tr>
					<td class="form_title">流程发布人:</td>
					<td class="form_data"><s:property value="model.uploader" /></td>
				</tr>
				<tr>
					<td class="form_title"><span style="color:red;">*</span>发布时间:</td>
					<td class="form_data"><s:property value="%{getText('{0,date,yyyy-MM-dd }',{model.uploadDate})}"/></td>
				</tr>
				<tr>
					<td class="form_title"><span style="color:red;">*</span>流程描述:</td>
					<td class="form_data"><s:textarea name="model.memo" cssStyle="width:200px;height:120px;" theme="simple"></s:textarea></td>
				</tr>
			</table>
			<s:hidden name="model.groupid"></s:hidden> 
			<s:hidden name="model.uploader" theme="simple"/>
			<s:hidden name="model.uploadDate" theme="simple"/>
		</s:form>	
	</div>
</body> 
</html>