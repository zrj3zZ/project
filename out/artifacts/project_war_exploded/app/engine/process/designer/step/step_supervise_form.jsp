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
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js" ></script>
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
		body {
			margin-left: 0px;
			margin-top: 0px;
			margin-right: 0px;
			margin-bottom: 0px;
		}
	</style>
	<script type="text/javascript">
	//全局变量
var api = art.dialog.open.api, W = api.opener;
		// 增加必填提示		
		function changeDis(){
		var srAction=$('#editForm_model_srAction').val();
		if(srAction==0){
		    $('#re_type').css('display','none');
		    $('#tri_event').css('display','');
		}
		else{
		    $('#re_type').css('display','');
		    $('#tri_event').css('display','none');
		}
	   }	
		//表单验证
		function validate(){ 
		     var srType=$('#editForm_model_srType').val();
		     if(srType==""){
		           art.dialog.tips("请选择计时方式！",2);
		           $('#editForm_model_srType').focus();
		           return false;
		     }
		     var srCondition=$('#editForm_model_srCondition').val();
		     if(srCondition==""){
		           art.dialog.tips("请选择触发条件！",2);
		           $('#editForm_model_srCondition').focus();
		           return false;
		     }
		     var daleyTime=$.trim($('#editForm_model_daleyTime').val());  
		     $('#editForm_model_daleyTime').val(daleyTime); 
		     if(daleyTime==""){
		          art.dialog.tips("延时时间不能为空！",2);
		          $('#editForm_model_daleyTime').focus();
		          return false;
		     }
		     if(!/^\d+$/.test(daleyTime)){
		          art.dialog.tips("延时时间必须为非负整数！",2);
		          $('#editForm_model_daleyTime').focus();
		          return false;
		     }//非负整数的判断
		     var srAction=$('#editForm_model_srAction').val();
		     if(srAction==""){
		           art.dialog.tips("请选择督办动作！",2);
		           $('#editForm_model_srAction').focus();
		           return false;
		     }
		     if(srAction!=4){ 
		            var bool_1=document.getElementById('model.remindType-1').checked;
		     		var bool_2=document.getElementById('model.remindType-2').checked;
		     		var bool_3=document.getElementById('model.remindType-3').checked;
		     		var bool_4=document.getElementById('model.remindType-4').checked;
		     		if(!bool_1&!bool_2&!bool_3&!bool_4){
		          		art.dialog.tips("通知类型不能为空！",2);
		          		return false;
		     		  }		     
		     }else{
		            var triggerEvent=$.trim($('#editForm_model_triggerEvent').val());  
		     		$('#editForm_model_triggerEvent').val(triggerEvent); 
		     		if(triggerEvent==""){
		          		art.dialog.tips("触发事件不能为空！",2);
		         		$('#editForm_model_triggerEvent').focus();
		          		return false;
		     		}
		     		else if(length2(triggerEvent)>256){
		          		art.dialog.tips("触发事件过长！",2);
		         	    $('#editForm_model_triggerEvent').focus();
		         	    return false;
		     		}
		    }		     
		     var srPurview=$.trim($('#editForm_model_srPurview').val());  
		     $('#editForm_model_srPurview').val(srPurview); 
		     if(length2(srPurview)>256){
		          art.dialog.tips("权限设置过长！",2);
		          $('#editForm_model_srPurview').focus();
		          return false;
		     }
		     return true;		     
		}
		//表单提交
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
           else if(responseText=="error"){
              art.dialog.tips("保存失败！",2);
           } 
      }
		//退出
		function cancel(){
			api.close();
		}
		//权限地址簿
		function openAuthorityBook(obj){
			var code = obj.value;	
			var code = $("#"+fieldName).val();
			var url = "authorityAddressBookAction!index.action?target="+fieldName+"&code="+encodeURI(encodeURI(code));
			 art.dialog.open(url,{
					id:"selectrouteParam",
					title: '权限地址簿', 
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:'90%',
				    height:'90%'
				 });
		}
		//判断通知类型和触发事件是否显示
	$(document).ready(function(){    
	     <s:if test="model.remindType==null">
	         document.getElementById('model.remindType-1').checked=false;
	         document.getElementById('model.remindType-2').checked=false;
	         document.getElementById('model.remindType-3').checked=false;
	         document.getElementById('model.remindType-4').checked=false;
	     </s:if> 
	     <s:if test="model.remindType!=null&&model.remindType.indexOf('msgSysmsg')!=-1">
	         document.getElementById('model.remindType-1').checked=true;
	     </s:if>
	     <s:else>
	         document.getElementById('model.remindType-1').checked=false;
	     </s:else>
	     <s:if test="model.remindType!=null&&model.remindType.indexOf('msgEmail')!=-1">
	         document.getElementById('model.remindType-2').checked=true;
	     </s:if>
	     <s:else>
	         document.getElementById('model.remindType-2').checked=false;
	     </s:else>
	     <s:if test="model.remindType!=null&&model.remindType.indexOf('msgIm')!=-1">
	         document.getElementById('model.remindType-3').checked=true;
	     </s:if>
	     <s:else>
	         document.getElementById('model.remindType-3').checked=false;
	     </s:else>
	     <s:if test="model.remindType!=null&&model.remindType.indexOf('msgSms')!=-1">
	         document.getElementById('model.remindType-4').checked=true;
	     </s:if>
	     <s:else>
	         document.getElementById('model.remindType-4').checked=false;
	     </s:else>
	      changeDis();
	 });
      </script> 
</head>
<body >
            	<s:form id ="editForm" name="editForm" action="sysFlowDef_stepSupervise!save.action"  theme="simple">
	            	<table border="0"  cellspacing="0" cellpadding="0" style="margin:15px;">
	            		<tr>
	            			<td class="td_title">计时方式：</td>
	            			<td class="td_data">
	            				<s:select name="model.srType"  list="#{'0':'按任务接受时间计算','1':'按任务打开时间计算'}"  headerKey="" headerValue="----请选择计时方式----"  cssStyle="font-size:12px;color:#0000FF;"/>
	            			    <span style="color:red;">*</span>
	            			</td>
	            		</tr>
	            		
	            		<tr>
	            			<td class="td_title">触发条件：</td>
	            			<td class="td_data">
	            				<s:select name="model.srCondition"  list="#{'0':'超过合理办理时间','1':'超过预警办理时间'}"  headerKey="" headerValue="----请选择触发条件----"  cssStyle="font-size:12px;color:#0000FF;"/>
	            			   <span style="color:red;">*</span>
	            			</td>
	            		</tr>
	            		<tr>
	            			<td class="td_title">延迟时间：</td>
	            			<td class="td_data">	            				
	            			    <s:textfield name="model.daleyTime" cssStyle="width:80px;"/>小时
	            			    <span style="color:red;">*</span>
	            			</td>
	            		</tr>
	            		<tr>
	            			<td class="td_title">督办动作：</td>
	            			<td class="td_data">
	            				<s:select onchange="changeDis();" name="model.srAction"  list="#{'1':'通知发起人','2':'通知当前办理人','3':'自动向下办理','4':'转发至当前用户直属上级','5':'驳回至发送人','6':'终止当前流程','0':'触发一个事件'}"  headerKey="" headerValue="----请选择督办动作----"  cssStyle="font-size:12px;color:#0000FF;"/>
	            				<span style="color:red;">*</span>
	            			</td>
	            		</tr>
	            		<tr id="re_type" >
	            			<td class="td_title">提醒类型：</td>
	            			<td class="td_data">
	            				<s:checkboxlist  theme="simple" list="#{'_sysmsg':'系统消息', '_email':'邮件', '_sms':'即时通讯','_sms':'短信','_weixin':'微信'}"  name="model.remindType"></s:checkboxlist>
	            				<span id="msg_type" style="color:red;">*</span>
	            			</td>
	            		</tr>
	            		<tr id="tri_event" style="display:">
	            			<td class="td_title">触发事件：</td>
	            			<td class="td_data">
	            				<s:textfield name="model.triggerEvent" cssStyle="width:300px;"/>
	            				<span style="color:red;">*</span>
	            			</td>
	            		</tr>
	            		<!--  
	            		<tr>
	            			<td class="td_title">权限设置：</td>
	            			<td class="td_data">
	            				<s:textarea readonly="true" name="model.srPurview" cssStyle="width:240px;height:60px;"/>
	            				<a href="javascript:openAuthorityBook(document.getElementById('editForm_model_srPurview'));" class="easyui-linkbutton" plain="true" iconCls="icon-add">权限地址簿</a>
	            			</td>
	            		</tr>-->
	            	</table>
	                <s:hidden name="model.actDefId"/>
	                <s:hidden name="model.actStepId"/>
	                <s:hidden name="model.prcDefId"/>
	                <s:hidden name="model.orderIndex"/>
	                <s:hidden name="model.id"/>
	                <div region="south" border="false" style="text-align: right; height: 30px; line-height: 30px;padding-top:5px;padding-right:15px;">
		                <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:doSubmit();" >
		                    保存</a> <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:cancel()">关闭</a>
		            </div>
               </s:form> 
</body>
</html>
