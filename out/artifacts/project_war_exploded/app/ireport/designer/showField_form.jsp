<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>  
    <title>编辑</title>
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
            var fieldName=$.trim($('#editForm_model_fieldName').val());//不能为空，不能超过32
          var fieldTitle=$.trim($('#editForm_model_fieldTitle').val());//不能为空，不能超过100         
                  var fieldWidth=$.trim($('#editForm_model_fieldWidth').val());//正整数                            
                   var fieldParams=$.trim($('#editForm_model_fieldParams').val());//不能超过200
            var fieldDisplay=$.trim($('#editForm_model_fieldDisplay').val());//不能超过500
          $('#editForm_model_fieldName').val(fieldName);
          $('#editForm_model_fieldTitle').val(fieldTitle);
          $('#editForm_model_fieldWidth').val(fieldWidth);         
          $('#editForm_model_fieldParams').val(fieldParams);         
          $('#editForm_model_fieldDisplay').val(fieldDisplay);
          
          if(fieldName==""){
              W.lhgdialog.tips("字段ID不能为空！",2);
               $('#editForm_model_fieldName').focus();
              return false;
          }
          if(fieldTitle==""){
              W.lhgdialog.tips("字段名称不能为空！",2);
              $('#editForm_model_fieldTitle').focus();
              return false;             
          }
          if(fieldWidth==""){
              W.lhgdialog.tips("显示宽度不能为空！",2);
              $('#editForm_model_fieldWidth').focus();
              return false;             
          }
          var re=/^[0-9]*[1-9][0-9]*$/;
          if(!re.test(fieldWidth)){
              W.lhgdialog.tips("显示宽度必须为正整数！",2);
              $('#editForm_model_fieldWidth').focus();
              return false;
          }
           if(length2(fieldName)>32){
                 W.lhgdialog.tips('字段ID过长!',2);
                 $('#editForm_model_fieldName').focus();
                 return false;
          }
          if(length2(fieldTitle)>100){
                 W.lhgdialog.tips('字段名称过长!',2);
                 $('#editForm_model_fieldTitle').focus();
                 return false;
          }
          if(length2(fieldParams)>200){
                 W.lhgdialog.tips('扩展参考值过长!',2);
                 $('#editForm_model_fieldParams').focus();
                 return false;
          }
          if(length2(fieldDisplay)>500){
                 W.lhgdialog.tips('显示权限过长!',2);
                 $('#editForm_model_fieldDisplay').focus();
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
           <s:form name="editForm" id="editForm" action="ireport_designer_showField_save.action" theme="simple">
               <table width="100%" border="0" cellspacing="0" cellpadding="0">
                   <tr>
                      <td class="td_title">字段ID：</td><td class="td_data"><s:textfield readonly="true" cssStyle="background-color:#cdcdcd;width:200px;" name="model.fieldName"/>&nbsp;<span style='color:red'>*</span></td>
                   </tr>
                   <tr>
                      <td class="td_title">字段名称：</td><td class="td_data"><s:textfield cssStyle="width:200px;" name="model.fieldTitle"/>&nbsp;<span style='color:red'>*</span></td>
                   </tr>
                   <tr>
                      <td class="td_title">显示宽度：</td><td class="td_data"><s:textfield cssStyle="width:200px;" name="model.fieldWidth"/>&nbsp;<span style='color:red'>*</span></td>
                   </tr>
                   <tr>
                      <td class="td_title">对齐方式：</td><td class="td_data"><s:radio name="model.align" list="#{'left':'左对齐','center':'居中','right':'右对齐'}"></s:radio>&nbsp;<span style='color:red'>*</span></td>
                   </tr>
                   <tr>
                      <td class="td_title">显示类型：</td><td class="td_data"><s:radio name="model.fieldDisplay" list="#{'text':'文本','date':'日期','datetime':'日期时间','number':'数值'}"></s:radio>&nbsp;<span style='color:red'>*</span></td>
                   </tr>
                   <tr>
                      <td class="td_title">钻取类型：</td><td class="td_data"><s:radio name="model.fieldType" list="#{'zqForm':'钻取表单','zqUrl':'外部URL','zqNone':'无'}"></s:radio>&nbsp;<span style='color:red'>*</span></td>
                   </tr>
                   <tr>
                      <td class="td_title">扩展参考值：</td><td class="td_data"><s:textfield cssStyle="width:300px;" name="model.fieldParams"/></td>
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