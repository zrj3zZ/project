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
			width:150px;
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
      </script>
  </head>
    <body class="easyui-layout">
      <div region="center" border="false" style="padding:3px;">
		 <s:form name="editForm" id= "editForm" theme="simple" action="resbook_base_save">
		 <table class="font">
		 	<tr>
		 		<td class="form_title"><span style="color:red;">*</span>空间ID/名称：</td>
		 		<td class="form_data">
		 			<s:textfield name="model.spaceid" readonly="true" cssStyle="width:50px;background-color:#efefef" cssClass="{maxlength:32,required:true}"></s:textfield>	/
		 			<s:textfield name="model.spacename" readonly="true"  cssStyle="width:100px;background-color:#efefef"  cssClass="{maxlength:32,required:true}"></s:textfield>
				</td> 
			</tr>
			<tr>
				<td class="form_title"><span style="color:red;">*</span>资源编号/资源名称：</td>
				<td class="form_data">
					<s:textfield name="model.resouceid"  cssStyle="width:50px"  cssClass="{maxlength:32,required:true}"></s:textfield>	/
		 			<s:textfield name="model.resoucename" cssClass="{maxlength:32,required:true}"></s:textfield>
		 		</td>
		 	</tr>
		 	<tr>
		 		<td class="form_title"><span style="color:red;">*</span>显示图片路径：</td>
		 		<td class="form_data">
		 			<s:textfield name="model.picture" cssStyle="width:300px"  cssClass="{maxlength:200,required:true}"></s:textfield>
		 		</td>
		 	</tr>
		 	<tr>
		 		<td class="form_title"><span style="color:red;">*</span>状态：</td>
		 		<td class="form_data">
		 			<s:radio  name="model.status" cssStyle="border:0px;" cssClass="{maxlength:32,required:true}" listKey="key" listValue="value"  list="#{'1':'开启','0':'关闭'}" theme="simple"/>
		 		</td>
		 	</tr>
		 	<tr>
		 		<td class="form_title">扩展参数一：</td>
		 		<td class="form_data">
		 			<s:textfield name="model.parameter1" theme="simple"></s:textfield>
		 		</td>
		 	</tr>
		 	<tr>
		 		<td class="form_title">扩展参数二：</td>
		 		<td class="form_data">
		 			<s:textfield name="model.parameter2" theme="simple"></s:textfield>
		 		</td>
		 	</tr>
		 	<tr>
		 		<td class="form_title">扩展参数三：</td>
		 		<td class="form_data">
		 			<s:textfield name="model.parameter3" theme="simple"></s:textfield>
		 		</td>
		 	</tr>
		 	<tr>
		 		<td class="form_title">扩展参数四：</td>
		 		<td class="form_data">
		 			<s:textfield name="model.parameter4" theme="simple"></s:textfield>
		 		</td>
		 	</tr>
		 	<tr>
		 		<td class="form_title">扩展参数五：</td>
		 		<td class="form_data">
		 			<s:textfield name="model.parameter5" theme="simple"></s:textfield>
		 		</td>
		 	</tr>
		 	
		 	<tr>
		 		<td class="form_title">备注：</td>
		 		<td class="form_data">
		 			<s:textarea name="model.memo" cols="45" rows="6"></s:textarea>
		 		</td>
		 	</tr>
		 	</table>
		 	<s:hidden name="model.id"></s:hidden>
		</s:form>
	</div>
      <div region="south" border="false" style="text-align: right; height: 40px; line-height: 30px;padding-top:5px;padding-right:15px;border-top:1px solid #efefef">
           <a class="easyui-linkbutton" href="javascript:save();" iconCls="icon-save">保存</a>
           <a class="easyui-linkbutton" href="javascript:closeDialog();" iconCls="icon-cancel">取消</a>
      </div>
      
  </body>
</html>
		