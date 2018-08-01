<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>    
    <title>自定义按钮编辑</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
    <link href="iwork_css/reset.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="iwork_js/commons.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
    <script type="text/javascript">
      //全局变量
     var api = art.dialog.open.api, W = api.opener;
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
          var bool= validate();
          if(bool){
             var options = {
				error:errorFunc,
				success:successFunc 
			   };
			 $('#editForm').ajaxSubmit(options);
        	}
          else{
          		return ;
        	}
      }
      errorFunc=function(){
           art.dialog.tips("保存失败！",2);
      }
      successFunc=function(responseText, statusText, xhr, $form){
           if(responseText=="ok"){
              art.dialog.tips("保存成功！",2);
              setTimeout('cancel();',1000);
           }
      }
            //表单验证
      function validate(){
            var buttonName=$.trim($('#editForm_model_buttonName').val());//不能为空，不能超过128
          var buttonScript=$.trim($('#editForm_model_buttonScript').val());//不能为空，不能超过2000 
                  var icon=$.trim($('#editForm_model_icon').val());//不能超过100                
                   var url=$.trim($('#editForm_model_url').val());//不能为空，不能超过256
            var permission=$.trim($('#editForm_model_permission').val());//不能为空，不能超过1024
               var useDesc=$.trim($('#editForm_model_useDesc').val());//不能为空，不能超过512
          $('#editForm_model_buttonName').val(buttonName);
          $('#editForm_model_buttonScript').val(buttonScript);
          $('#editForm_model_icon').val(icon);
          $('#editForm_model_url').val(url);
          $('#editForm_model_permission').val(permission);
          $('#editForm_model_useDesc').val(useDesc);
          
          if(buttonName==""){
              art.dialog.tips("按钮名称不能为空！",2);
               $('#editForm_model_buttonName').focus();
              return false;
          }
          if(buttonScript==""){
              art.dialog.tips("按钮脚本不能为空！",2);
              $('#editForm_model_buttonScript').focus();
              return false;             
          }
          if(url==""){
              art.dialog.tips("URL不能为空！",2);
              $('#editForm_model_url').focus();
              return false;              
          }
          /*
          if(permission==""){
              art.dialog.tips("请点击右边的图标进行权限设置！",2);
              $('#editForm_model_permission').focus();
              return false;              
          }*/
          if(useDesc==""){
              art.dialog.tips("用途描述不能为空！",2);
              $('#editForm_model_useDesc').focus();
              return false;              
          }
           if(length2(buttonName)>128){
                 art.dialog.tips('按钮名称过长!',2);
                 $('#editForm_model_buttonName').focus();
                 return false;
          }
          if(length2(icon)>128){
                 art.dialog.tips('按钮图标地址过长!',2);
                 $('#editForm_model_icon').focus();
                 return false;
          }
          if(length2(buttonScript)>2000){
                 art.dialog.tips('按钮脚本过长!',2);
                 $('#editForm_model_buttonScript').focus();
                 return false;
          }
          if(length2(url)>256){
                 art.dialog.tips('url过长!',2);
                 $('#editForm_model_url').focus();
                 return false;
          }
          if(length2(permission)>1024){
                 art.dialog.tips('权限设置过长!',2);
                 $('#editForm_model_permission').focus();
                 return false;
          } 
          if(length2(useDesc)>512){
                 art.dialog.tips('用途描述过长!',2);
                 $('#editForm_model_useDesc').focus();
                 return false;
          }         
          return true;
      }
      //关闭窗口
      function cancel(){
          api.close();
      }
      //权限地址簿
		function openAuthorityBook(fieldName){
			var code = $("#"+fieldName).val();
			var url = "authorityAddressBookAction!index.action?target="+fieldName+"&code="+encodeURI(encodeURI(code));
			 art.dialog.open(url,{
					id:"selectrouteParam",
					title: '权限地址簿', 
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:500,
				    height:'90%'
				 });
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
           <s:form name="editForm" id="editForm" action="sysFlowDef_stepDIYBtn_save.action" theme="simple">
               <table width="100%" border="0" cellspacing="0" cellpadding="0">
                   <tr>
                      <td class="td_title">按钮名称：</td><td class="td_data"><s:textfield cssStyle="width:200px;" name="model.buttonName"/>&nbsp;<span style='color:red'>*</span></td>
                   </tr>
                   <tr>
                      <td class="td_title">按钮图标：</td><td class="td_data">
                      	<s:select list="#{'icon-config':'设置','icon-add':'新增','icon-edit':'编辑','icon-remove':'删除','icon-save':'保存','icon-ok':'确认','icon-no':'取消','icon-cancel':'退出','icon-reload':'加载','icon-search':'查询','icon-print':'打印','icon-help':'帮助','icon-undo':'撤销'}" name="model.icon"></s:select>
                      </td>
                   </tr>
                   <tr>
                      <td class="td_title">按钮脚本：</td><td class="td_data"><s:textarea cssStyle="width:300px;height:80px;overflow:auto;" name="model.buttonScript"/>&nbsp;<span style='color:red'>*</span></td>
                   </tr>
                   <tr>
                      <td class="td_title">作用范围：</td><td class="td_data"><s:select list="#{'0':'待办事宜时显示','1':'待办/已办显示(不含归档)','2':'永久显示'}" name="model.purviewScope"></s:select>&nbsp;<span style='color:red'>*</span></td>
                   </tr>
                   <tr>
                      <td class="td_title">URL：</td><td class="td_data"><s:textfield  cssStyle="width:200px;" name="model.url"/>&nbsp;<span style='color:red'>*</span></td>
                   </tr>
                   <tr>
                      <td class="td_title">权限设置：</td><td class="td_data"><s:textarea readonly="true" cssStyle="width:300px;height:60px;overflow:auto;" name="model.permission"/>
                      <a class="easyui-linkbutton" href="javascript:openAuthorityBook('editForm_model_permission');" iconCls="icon-add" plain="true">权限地址簿</a>
                      &nbsp;</td>
                   </tr>
                   <tr>
                      <td class="td_title">用途描述：</td><td class="td_data"><s:textarea cssStyle="width:300px;height:60px;overflow:auto;" name="model.useDesc"/>&nbsp;<span style='color:red'>*</span></td>
                   </tr>
               </table>
               
               <s:if test="model==null">
               <s:hidden name="model.proDefId" value="%{prcDefId}"/>
               <s:hidden name="model.actDefId" value="%{actDefId}"/>
               <s:hidden name="model.actStepId" value="%{actStepDefId}"/>
               </s:if>
               <s:else>
               <s:hidden name="model.id"/>
               <s:hidden name="model.proDefId"/>
               <s:hidden name="model.actDefId"/>
               <s:hidden name="model.actStepId"/>
               <s:hidden name="model.orderIndex"/>
               </s:else>
           </s:form>
      </div>
      
      <div region="south" border="false" style="text-align: right; height: 50px; line-height: 30px;padding-top:5px;padding-right:15px;">
           <a class="easyui-linkbutton" href="javascript:save();" iconCls="icon-save">保存</a>
           <a class="easyui-linkbutton" href="javascript:cancel();" iconCls="icon-cancel">取消</a>
      </div>
  </body>
</html>
