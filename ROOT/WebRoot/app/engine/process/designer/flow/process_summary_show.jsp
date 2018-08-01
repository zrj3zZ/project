<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>  
    <title>编辑</title>
   <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
   <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>  
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script> 
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script> 
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
    <script type="text/javascript">
    var api = art.dialog.open.api, W = api.opener;
    var mainFormValidator;
	$().ready(function() {
		mainFormValidator =  $("#editForm").validate({
			debug:false
		});
	 	mainFormValidator.resetForm(); 
	}); 
      //全局变量
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
			}else{
             var options = {
				error:errorFunc,
				success:successFunc 
			   };
			 $('#editForm').ajaxSubmit(options);
        	}
      }
      errorFunc=function(){
         art.dialog.tips("保存失败！",2);
      }
      successFunc=function(responseText, statusText, xhr, $form){
           if(responseText=="success"){
            art.dialog.tips("保存成功！",2);
              setTimeout('cancel();',1000);
           }
      }
      //关闭窗口
      function cancel(){
          api.close();
      }    
      </script>
  </head>
    <body class="easyui-layout">
      <div region="center" border="false" style="padding:3px;margin-bottom:5px;overflow-y:auto;">
           <s:form name="editForm" id="editForm" action="process_step_summary_update.action" theme="simple">
               <table width="100%" border="0" cellspacing="0" cellpadding="10">
                   <tr>
                      <td class="td_title">字段名称：</td><td class="td_data"><s:textfield readonly="true" cssStyle="background-color:#cdcdcd;width:200px;" name="model.fieldName"/>&nbsp;<span style='color:red'>*</span></td>
                   </tr>
                   <tr>
                      <td class="td_title">字段标题：</td><td class="td_data"><s:textfield cssStyle="width:200px;"  cssClass="{required:true}"  name="model.fieldTitle"/>&nbsp;<span style='color:red'>*</span></td>
                   </tr> 
                   <tr>
                      <td class="td_title">是否表单链接：</td><td class="td_data"><s:radio name="model.isLink"  cssClass="{required:true}"  list="#{'1':'是','0':'否'}"></s:radio>&nbsp;<span style='color:red'>*</span></td>
                   </tr>
                   <tr>
                      <td class="td_title">显示宽度：</td><td class="td_data"><s:textfield cssStyle="width:80px;"  cssClass="{number:true,required:true}"  name="model.fieldWidth"/>&nbsp;<span style='color:red'>*</span></td>
                   </tr>
               </table> 
               <s:hidden name="model.id"/>
               <s:hidden name="model.actDefId"/> 
               <s:hidden name="model.actStepId"/> 
               <s:hidden name="model.prcDefId"/> 
               <s:hidden name="model.metadataid"/> 
               <s:hidden name="model.formid"/> 
               <s:hidden name="model.groupId"/> 
               <s:hidden name="model.orderIndex"/>
           </s:form>
      </div>
      
      <div region="south" border="false" style="text-align: right; height: 50px; line-height: 30px;padding-top:5px;padding-right:15px;">
           <a class="easyui-linkbutton" href="javascript:save();" iconCls="icon-save">保存</a>
           <a class="easyui-linkbutton" href="javascript:cancel();" iconCls="icon-cancel">取消</a>
      </div>
      
  </body>
</html>
