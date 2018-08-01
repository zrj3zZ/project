<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>  
    <title>编辑</title>
    
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
    <link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
    <link rel="stylesheet" type="text/css" href="iwork_css/common.css"> 
    <script type="text/javascript" src="iwork_js/commons.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
    <script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script>   
    <script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"></script>
    <style type="text/css">
		.form_title{  
			font-family:黑体;
			font-size:14px;
			width:100px;
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
      //全局变量
     var api = frameElement.api, W = api.opener; 
     var mainFormValidator;
     $().ready(function() {
    		 mainFormValidator =  $("#editForm").validate({
				debug:false
			 });
			 mainFormValidator.resetForm();	
			  selectType();
	 });
	 jQuery(document).bind('keydown',function (evt){
	    if(evt.ctrlKey&&evt.shiftKey){
		return false;
	   }
	   else if(evt.ctrlKey && event.keyCode==83){ //Ctrl+s /保存操作
		         save(); return false;
	  }
      }); //快捷键
    //保存
    function save(){
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
          alert("保存失败！");
      }
      successFunc=function(responseText, statusText, xhr, $form){
           if(responseText=="success"){
               alert("保存成功!");
              window.parent.refrenshGrid();
              closeDialog();
           }
      }
	    //关闭窗口
	   function closeDialog(){
	   		api.close(); 
	   }
	   function selectType(){
	   		var select = $('input[name="model.type"]:checked').val();
	   		if(select=="1"){
	   			$("#processId").rules("add", {required: true,maxlength:64});
	   			$("#td_processId").show();
	   		}else if(select=="2"){
	   			$("#processId").rules("remove"); 
	   			$("#td_processId").hide();
	   		}
	   }
      </script>
  </head>
    <body class="easyui-layout">
      <div region="center" border="false" style="padding:3px;">
		 <s:form name="editForm" id= "editForm" theme="simple" action="resbook_space_save">
		 <table class="font" cellspacing="10">
		 	<tr>
		 		<td class="form_title"><span style="color:red;">*</span>空间名称：</td>
		 		<td class="form_data">
		 			<s:textfield name="model.spacename" cssClass="{maxlength:32,required:true}"></s:textfield>
				</td>
			</tr>
			<tr>
				<td class="form_title"><span style="color:red;">*</span>预定类型：</td>
				<td class="form_data">
					<s:radio  name="model.type" cssStyle="border:0px;" onclick="selectType()" cssClass="{maxlength:32,required:true}" listKey="key" listValue="value"  list="#{'1':'流程预定','2':'直接预定'}" theme="simple"/>
		 		</td>
		 	</tr>
			<tr id="td_processId" style="display:none">
				<td class="form_title" ><span style="color:red;">*</span>流程ID：</td>
				<td class="form_data">
					<s:textfield name="model.processId" id="processId" theme="simple"></s:textfield>
		 		</td> 
		 	</tr>
		 	<tr>
		 		<td class="form_title"><span style="color:red;">*</span>预订周期：</td>
		 		<td class="form_data">
		 		<s:radio  name="model.cycle" cssStyle="border:0px;" cssClass="{maxlength:32,required:true}" listKey="key" listValue="value"  list="#{'7':'一周','14':'两周','21':'三周','28':'四周'}"  theme="simple"/>
		 		</td>
		 	</tr>
		 	<tr>
		 		<td class="form_title"><span style="color:red;">*</span>管理员：</td>
		 		<td class="form_data">
		 			<s:textfield name="model.manager" cssClass="{maxlength:32,required:true}"></s:textfield>
		 		</td>
		 	</tr>
		 	<tr>
		 		<td class="form_title"><span style="color:red;">*</span>空间状态：</td>
		 		<td class="form_data">
		 			<s:radio  name="model.status" cssStyle="border:0px;" cssClass="{maxlength:32,required:true}" listKey="key" listValue="value"  list="#{'1':'开启','0':'关闭'}" theme="simple"/>
		 		</td>
		 	</tr>
		 	<tr>
			 	<td class="form_title"><span style="color:red;">*</span>提示信息：</td>
			 	<td class="form_data">
			 		<s:textarea name="model.memo" cols="45" rows="6"></s:textarea>
			 	</td>
		 	</tr></table>
		 	<s:hidden name="model.id"></s:hidden>
		</s:form>
	</div>
      <div region="south" border="false" style="text-align: right; height: 40px; line-height: 30px;padding-top:5px;padding-right:15px;border-top:1px solid #efefef">
           <a class="easyui-linkbutton" href="javascript:save();" iconCls="icon-save">保存</a>
           <a class="easyui-linkbutton" href="javascript:closeDialog();" iconCls="icon-cancel">取消</a>
      </div>
      
  </body>
</html>
