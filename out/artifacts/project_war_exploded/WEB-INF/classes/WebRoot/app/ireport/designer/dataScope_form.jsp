<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
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
    <script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?skin=default"></script>    
    <script type="text/javascript">
      //全局变量
     var api = frameElement.api, W = api.opener; 
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
           W.lhgdialog.tips("保存失败！",2);
      }
      successFunc=function(responseText, statusText, xhr, $form){
           if(responseText=="ok"){
              W.lhgdialog.tips("保存成功！",2);
              setTimeout('cancel();',1000);
           }
      }
            //表单验证
      function validate(){
            var scopeSql=$.trim($('#editForm_model_scopeSql').val());//不能为空，不能超过200
          var purview=$.trim($('#editForm_model_purview').val());//不能超过200 
                  var memo=$.trim($('#editForm_model_memo').val());//不能超过200
          $('#editForm_model_scopeSql').val(scopeSql);
          $('#editForm_model_purview').val(purview);
          $('#editForm_model_memo').val(memo);
          
          if(scopeSql==""){
              W.lhgdialog.tips("数据范围SQL不能为空！",2);
               $('#editForm_model_scopeSql').focus();
              return false;
          }
           if(length2(scopeSql)>200){
                 W.lhgdialog.tips('数据范围SQL过长!',2);
                 $('#editForm_model_scopeSql').focus();
                 return false;
          }         
          if(length2(purview)>200){
                 W.lhgdialog.tips('权限设置过长!',2);
                 $('#editForm_model_purview').focus();
                 return false;
          }
          if(length2(memo)>200){
                 W.lhgdialog.tips('备注过长!',2);
                 $('#editForm_model_memo').focus();
                 return false;
          }        
          return true;
      }
      //关闭窗口
      function cancel(){
          api.close();
      }
      //权限地址簿
		function openAuthorityBook(obj){
			var code = obj.value;	
			var url = "authorityAddressBookAction!index.action?code=" + code;
			window.showModalDialog(url, obj, 'dialogWidth:650px;dialogHeight:535px;help:no;resizable:no;status:no;location:no');
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
           <s:form name="editForm" id="editForm" action="ireport_designer_dataScope_save.action" theme="simple">
               <table width="100%" border="0" cellspacing="0" cellpadding="0">
                   <tr>
                      <td class="td_title">数据范围SQL：</td><td class="td_data"><s:textarea cssStyle="width:300px;height:80px;overflow:auto;" name="model.scopeSql"/>&nbsp;<span style='color:red'>*</span></td>
                   </tr>
                   <tr>
                      <td class="td_title">权限设置：</td><td class="td_data"><s:textarea readonly="true" cssStyle="width:300px;height:60px;overflow:auto;" name="model.purview"/>
                      <a class="easyui-linkbutton" href="javascript:openAuthorityBook(document.getElementById('editForm_model_purview'));" iconCls="icon-add" plain="true">权限地址簿</a>
                      </td>
                   </tr>
                   <tr>
                      <td class="td_title">备注：</td><td class="td_data"><s:textarea cssStyle="width:300px;height:60px;overflow:auto;" name="model.memo"/></td>
                   </tr>
               </table>
               
              
               <s:hidden name="model.id"/>
               <s:hidden name="model.ireportId"/>
               <s:hidden name="model.orderIndex"/>
           </s:form>
      </div>
      
      <div region="south" border="false" style="text-align: right; height: 50px; line-height: 30px;padding-top:5px;padding-right:15px;">
           <a class="easyui-linkbutton" href="javascript:save();" iconCls="icon-save">保存</a>
           <a class="easyui-linkbutton" href="javascript:cancel();" iconCls="icon-cancel">取消</a>
      </div>
  </body>
</html>
