<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>  
    <title>编辑</title>
   <link rel="stylesheet" type="text/css" href="iwork_css/common.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
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
      //==========================装载快捷键===============================//快捷键
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
           if(responseText=="ok"){
              cancel();
           }
      }
      //关闭窗口
      function cancel(){
          api.close();
      }   
      //是否显示字段宽度 
      function isShowWidth(obj){
          if(obj.checked){
             $('#editForm_model_isShow').val(0);
             $('#editForm_model_displayWidth').attr('readonly',false);
             $('#flag').css('display',''); 
          }
          else{
             $('#editForm_model_isShow').val(1);
             $('#editForm_model_displayWidth').val('');
             $('#editForm_model_displayWidth').attr('readonly',true);
             $('#flag').css('display','none');           
          }
      }   
      </script>
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
   </style>   
  </head>
  
    <body class="easyui-layout">
      <div region="center" border="false" style="padding:3px;margin-bottom:5px;overflow-y:auto;">
           <s:form name="editForm" id="editForm" action="sys_dictionary_showField_save.action" theme="simple">
               <table width="100%" border="0" cellspacing="0" cellpadding="0">
                   <tr>
                      <td class="td_title"><span style='color:red'>*</span>字段ID：</td><td class="td_data"><s:textfield cssClass="{maxlength:32,required:true}"  readonly="true" cssStyle="background-color:#cdcdcd;width:200px;" name="model.fieldName"/>&nbsp;</td>
                   </tr>
                   <tr>
                      <td class="td_title"><span style='color:red'>*</span>字段名称：</td><td class="td_data"><s:textfield cssClass="{maxlength:32,required:true}"  cssStyle="width:200px;" name="model.fieldTitle"/>&nbsp;</td>
                   </tr>
                   <tr>
                      <td class="td_title"><span style='color:red' id="flag">*</span>显示宽度：</td><td class="td_data"><s:textfield  cssClass="{maxlength:8,required:true,number:true}"  cssStyle="width:200px;" name="model.displayWidth"/> 
                          
                      </td>
                   </tr>
                   <tr>
                      <td class="td_title"><span style='color:red' id="flag">*</span>是否显示列：</td><td class="td_data">
                          <s:radio  name="model.isShow" cssStyle="border:0px"  listKey="key" listValue="value"  list="#{'0':'是','1':'否'}"  theme="simple"/>
                      </td> 
                   </tr>
                   <tr>
                      <td class="td_title">插入目标字段：</td><td class="td_data"><s:textfield cssClass="{maxlength:32}" cssStyle="width:200px;" name="model.targetField"/><br>&nbsp;&nbsp;&nbsp;&nbsp;说明:如果无目标字段,可置为空
                   </tr>
               </table>
               
               <s:hidden name="model.id"/>
               <s:hidden name="model.dictionaryId"/>
               <s:hidden name="model.orderIndex"/>
           </s:form>
      </div>
      
      <div region="south" border="false" style="text-align: right; height: 50px; line-height: 30px;padding-top:5px;padding-right:15px;">
           <a class="easyui-linkbutton" href="javascript:save();" iconCls="icon-save">保存</a>
           <a class="easyui-linkbutton" href="javascript:cancel();" iconCls="icon-cancel">取消</a>
      </div>
      
  </body>
</html>
