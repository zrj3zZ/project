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
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js"></script>
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
		
	</style>
	<script type="text/javascript">
	var api = art.dialog.open.api, W = api.opener;
	   //保存
		function doSubmit(){ 
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
		    var eventType=$('#editForm_model_eventType').val(); 
		    var eventParam=$.trim($('#editForm_model_eventParam').val());
		    var eventMemo=$.trim($('#editForm_model_eventMemo').val());
		    $('#editForm_model_eventParam').val(eventParam);
		    $('#editForm_model_eventMemo').val(eventMemo);
		    if(eventType==""){
		      art.dialog.tips("请选择事件类型！",2);
		       $('#editForm_model_eventType').focus();
		       return false;
		    }
		    if(eventParam==""){
		      art.dialog.tips("事件参考值不能为空！",2);
		       $('#editForm_model_eventParam').focus();
		       return false;
		    }
		    if(length2(eventParam)>500){
		      art.dialog.tips("事件参考值过长！",2);
		       $('#editForm_model_eventParam').focus();
		       return false;
		    }
		    if(length2(eventMemo)>500){
		      art.dialog.tips("业务描述过长！",2);
		       $('#editForm_model_eventMemo').focus();
		       return false;
		    }
		    return true;
		}
		//取消
		function cancel(){
			api.close();
		}
	</script>
</head>
<body  class="easyui-layout">
		<div region="center" style="border:0px;padding:3px;margin-bottom:5px;overflow-y:auto;">
            	<s:form id ="editForm" name="editForm" action="sysDem_trigger_save"  theme="simple">
			                <table width="100%" border="0" cellspacing="0" cellpadding="0">
							 <tr>
							   <td  class="td_title">触发器类型:</td>
							    <td class="td_data">
							    	<span id="div_trigerType">
							    		<s:radio value="model.triggerType" list="#{'0':'JAVA触发器'}" name="model.triggerType" theme="simple"/>
							    		<input type="radio" name="model.triggerType" id="editForm_model_trigerType1" disabled value="1"/><label for="editForm_model_trigerType1">WebService触发器</label>
									</span>
									<span style='color:red'>*</span>
								</td>
							  </tr>
							  <tr>
							   <td  class="td_title">事件类型:</td>
							    <td  class="td_data">
									<s:select headerKey="" headerValue="-----请选择触发器事件----" name="model.eventType" list="%{trigerTypeList}" cssStyle="font-size:12px;color:#0000FF;" theme="simple"></s:select>
									<span style='color:red'>*</span>
								</td>
							  </tr>
							  <tr>
							    <td class="td_title">事件注册类路径:</td>
							    <td class="td_data">
							    	<s:textfield name="model.eventParam" cssStyle="width:280px;"/>
							   		<span style='color:red'>*</span>
							   	</td>
							   </tr>
							     
							  <tr>
							    <td class="td_title">业务描述:</td>
							    <td class="td_data"><s:textarea name = "model.eventMemo" cssStyle="width:300px;height:60px;overflow:auto;"></s:textarea></td>
							    </tr>
							</table>
	                <s:hidden name="model.demId"/>
	                <s:hidden name="model.id"/>
	                 </s:form> 
	            </div>
	                <div region="south" border="false" style="text-align: right; height: 50px; line-height: 30px;padding-top:5px;padding-right:15px;">
		                <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:doSubmit();" >保存</a> 
		                <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:cancel()">取消</a>
		            </div>
</body>
</html>
