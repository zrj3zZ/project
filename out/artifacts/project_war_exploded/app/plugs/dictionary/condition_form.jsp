<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>  
    <title>编辑</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
    <link href="iwork_css/reset.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
    <script type="text/javascript" src="iwork_js/commons.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
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
            var fieldName=$.trim($('#editForm_model_fieldName').val());//不能为空，不能超过64
          var fieldTitle=$.trim($('#editForm_model_fieldTitle').val());//不能为空，不能超过200         
                   var displayEnum=$.trim($('#editForm_model_displayEnum').val());//不能超过200

          $('#editForm_model_fieldName').val(fieldName);
          $('#editForm_model_fieldTitle').val(fieldTitle);
          $('#editForm_model_displayEnum').val(displayEnum);
          
          if(fieldName==""){
              art.dialog.tips("字段ID不能为空！",2);
               $('#editForm_model_fieldName').focus();
              return false;
          }
          if(fieldTitle==""){
              art.dialog.tips("字段名称不能为空！",2);
              $('#editForm_model_fieldTitle').focus();
              return false;             
          }
           if(length2(fieldName)>64){
                 art.dialog.tips('字段ID过长!',2);
                 $('#editForm_model_fieldName').focus();
                 return false;
          }
          if(length2(fieldTitle)>200){
                 art.dialog.tips('字段名称过长!',2);
                 $('#editForm_model_fieldTitle').focus();
                 return false;
          }
          if(length2(displayEnum)>200){
                 art.dialog.tips('参考值过长!',2);
                 $('#editForm_model_displayEnum').focus();
                 return false;
          }        
          return true;
      }
      //关闭窗口
      function cancel(){
          api.close();
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
           <s:form name="editForm" id="editForm" action="sys_dictionary_condition_save.action" theme="simple">
               <table width="100%" border="0" cellspacing="0" cellpadding="0">
                   <tr>
                      <td class="td_title">字段ID：</td><td class="td_data"><s:textfield readonly="true" cssStyle="background-color:#cdcdcd;width:200px;" name="model.fieldName"/>&nbsp;<span style='color:red'>*</span></td>
                   </tr>
                   <tr>
                      <td class="td_title">字段名称：</td><td class="td_data"><s:textfield cssStyle="width:200px;" name="model.fieldTitle"/>&nbsp;<span style='color:red'>*</span></td>
                   </tr> 
                   <tr>
                      <td class="td_title">条件获取方式：</td><td class="td_data">
                      <s:radio  name="model.dsType"  listKey="key" listValue="value"  list="#{'1':'用户录入','2':'从表单中获取','3':'表单获取后可编辑'}" value="model.dsType" theme="simple"/>
					  </td> 
                   </tr> 
                   <tr>
                      <td class="td_title">字段类型：</td><td class="td_data">
                      <s:radio  name="model.fieldType"  listKey="key" listValue="value"  list="#{'1':'文本','2':'数值','3':'日期'}" value="model.fieldType" theme="simple"/>
					  </td>   
                   </tr> 
                   <tr>
                      <td class="td_title">外观：</td><td class="td_data">
                      <s:select label="外观：" name="model.displayType"  list="%{displayTypeList}" listKey="key" listValue="title"></s:select>
					  </td>
                   </tr>
                    <tr>
                      <td class="td_title">比较：</td><td class="td_data">
                      <s:select label="比较：" name="model.compareType"  list="#{'equal':'等于','bigThan':'大于','smallThan':'小于','bigEqual':'大于等于','smallEqual':'小于等于','included(fuzzyMatch)':'包含于（模糊匹配）','included(firstFuzzyMatch)':'包含于（从第一个位置开始模糊匹配）'}" listKey="key" listValue="value"></s:select>
					  </td>
                   </tr>
                   <tr>
                      <td class="td_title">参考值：</td><td class="td_data"><s:textfield cssStyle="width:300px;" name="model.displayEnum"/>
                      <br/><br/>
                      <span style="color:#999">注：从表单中获取数值作为查询条件可在参考值中填写&#36; &#123;表单字段  &#125;例如:&#36; &#123;SQR&#125;</span>
                      </td>
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
