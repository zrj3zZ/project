<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link href="iwork_css/reset.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js" ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
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
		function setActionType(obj){
			if(obj.value=="isJump"){
				if($("#actionType-1").attr('checked')){
						$('#target_person').css('display','');				
				}else{
						$('#target_person').css('display','none');
						$('#editForm_model_targetStep').val('');
				}
			}else if(obj.value=="isNextuser"){
				if($("#actionType-2").attr('checked')){
				        $('#next_user').css('display','');					   
				}else{
					    $('#next_user').css('display','none');
					    $('#editForm_model_nextUser').val('');
				}
			}else if(obj.value=="isRemind"){
				if($("#actionType-3").attr('checked')){
				        $('#msg_user').css('display','');	
				        $('#msg_type').css('display','');						    
				}else{
				        $('#msg_user').css('display','none');	
				        $('#msg_type').css('display','none');
				        $('#editForm_model_msgUsers').val('');
				        $('#msgType-1').attr('checked',false);
				        $('#msgType-2').attr('checked',false);
				        $('#msgType-3').attr('checked',false);
				        $('#msgType-4').attr('checked',false);					
			}
		}
	}	
		//表单验证
		function validate(){
		     var sjExpression=$.trim($('#editForm_model_sjExpression').val());
		     $('#editForm_model_sjExpression').val(sjExpression);
		     var sjPurview=$.trim($('#editForm_model_sjPurview').val());
		     
		     var bool1=$('#editForm_actionType-1').attr('checked'); 
		     var bool2=$('#editForm_actionType-2').attr('checked');
		     var bool3=$('#editForm_actionType-3').attr('checked');
		     if(sjExpression==""){
		           art.dialog.tips("系统规则表达式不能为空！",2);
		           $('#editForm_model_sjExpression').focus();
		           return false;
		     } 
		     if(!bool1&!bool2&!bool3){
		           art.dialog.tips("动作设置不能为空！",2);
		           return false;
		     }
		     if(bool1){
		          var targetStep=$('#editForm_model_targetStep').val();
		          if(targetStep==""){
		               art.dialog.tips("执行动作跳转至不能为空！",2);
		               $('#editForm_model_targetStep').focus();
		               return false;
		          }
		     }
		     if(bool2){
		          var nextUser=$('#editForm_model_nextUser').val();
		           if(nextUser==""){
		               art.dialog.tips("设置下一办理人不能为空！",2);
		               $('#editForm_model_nextUser').focus();
		               return false;
		          }
		     }
		      if(bool3){
		          var msgUsers=$('#editForm_model_msgUsers').val();
		           if(msgUsers==""){
		               art.dialog.tips("设置通知人不能为空！",2);
		               $('#editForm_model_msgUsers').focus();
		               return false;
		          }
		          /*
		          var bool_1=$('#msgType-1').attr('checked');
		          var bool_2=$('#msgType-2').attr('checked');
		          var bool_3=$('#msgType-3').attr('checked');
		          var bool_4=$('#msgType-4').attr('checked');
		           if(!bool_1&!bool_2&!bool_3&!bool_4){
		               art.dialog.tips("通知类型不能为空！",2);
		               return false;
		          }*/
		     }
		     if(length2(sjExpression)>500){
		           art.dialog.tips("系统规则表达式过长！",2);
		           $('#editForm_model_sjExpression').focus();
		           return false;
		     }
		      if(length2(sjPurview)>256){
		           art.dialog.tips("权限设置过长！",2);
		           $('#editForm_model_sjPurview').focus();
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
	     function openSysJump_rule(defaultField){
	    	 var code = document.getElementById(defaultField).value;
	    	 var sjExpression = $("#editForm_model_sjExpression").val();
         	var actDefId = $("#editForm_model_actDefId").val();
         	var actStepId = $("#editForm_model_actStepId").val();
         	var prcDefId = $("#editForm_model_prcDefId").val();
         	var str = sjExpression.replace(/&/g,"_");	
			var pageUrl = "stepSysJump_loadRuleEditer.action?defaultField="+defaultField+"&&actDefId="+actDefId+"&actStepDefId="+actStepId+"&prcDefId="+prcDefId+"&sjExpression="+encodeURI(encodeURI(str));;
			 art.dialog.open(pageUrl,{
			    	id:"systemRule",
					title: '规则表达式编辑器',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:650,
				    height:'80%'
				 });
	     }
	    //多选地址簿
		function multi_book(defaultField) {
		var code = document.getElementById(defaultField).value;	
		var pageUrl = "multibook_index.action?input="+encodeURI(code)+"&defaultField="+defaultField;
		 art.dialog.open(pageUrl,{
			    	id:"selectrouteParam",
				title: '多选地址簿',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:650,
				    height:'80%'
				 });
				 
		}
			//权限地址簿
			function openAuthorityBook(fieldName){
				var code = $("#"+fieldName).val();
				var pageUrl = "authorityAddressBookAction!index.action?target="+fieldName+"&code="+encodeURI(encodeURI(code));
				 art.dialog.open(pageUrl,{
			    	id:"selectrouteParam",
					title: '权限地址簿', 
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:650,
				    height:'80%'
				 });
			}
      </script> 
</head>
<body >
            	<s:form id ="editForm" name="editForm" action="sysFlowDef_stepSysJump!save.action"  theme="simple">
	            	<table border="0"  cellspacing="0" cellpadding="0" style="margin:15px;">
	            		<tr>
	            			<td class="td_title">系统规则表达式：</td>
	            			<td class="td_data">
	            				<s:textarea  name="model.sjExpression" readonly="readonly" cssStyle="width:240px;height:60px;overflow:auto;"/>
	            				<a href="javascript:openSysJump_rule('editForm_model_sjExpression');" class="easyui-linkbutton" plain="true" iconCls="icon-add"> 编辑规则</a>
<!--            				<a href="javascript:add_sysjump_rule('editForm_model_sjExpression');" class="easyui-linkbutton" plain="true" iconCls="icon-add"> 编辑规则</a> -->	  
	            			    <span style="color:red;">*</span>
	            			    <br><span style="color:#666">例如：&#36;{印章使用申请单.SQR} == "张三"</span>
	            			</td>
	            		</tr>
	            		
	            		<tr>
	            			<td class="td_title">动作设置：</td>
	            			<td class="td_data">
	            				<s:checkboxlist onclick="setActionType(this);" theme="simple" list="#{'isJump':'是否跳转', 'isNextuser':'是否指定下一办理人', 'isRemind':'是否发送通知'}"  name="actionType"></s:checkboxlist>
	            			   <span style="color:red;">*</span>
	            			</td>
	            		</tr>
	            		<!-- 
	            		<tr>
	            			<td class="td_title">动作设置：</td><td class="td_data"><s:checkbox  name="isJump" value="flase" fieldValue="%{isJump}"/>是否跳转<s:checkbox  name="isNextuser"  fieldValue="%{isNextuser}"/>是否指定下一个用户<s:checkbox  name="isRemind"  fieldValue="%{isRemind}"/>是否发送通知</td>
	            		</tr>-->
	            		<tr>
	            			<td class="td_title">执行跳转动作至：</td>
	            			<td class="td_data" id="targetStep_td">
	            				<s:select name="model.targetStep"  list="%{stepList}" listValue="stepTitle"  listKey="actStepId" headerKey="" headerValue="--------请选择--------"  cssStyle="font-size:12px;color:#0000FF;"/>
	            			    <span id="target_person" style="color:red;display:none;">*</span>
	            			</td>
	            		</tr>
	            		<tr>
	            			<td class="td_title">设置下一办理人：</td>
	            			<td class="td_data" id="nextUser_td">
	            				<s:textfield name="model.nextUser" theme="simple"/>
	            				<a href="###" onclick="multi_book('editForm_model_nextUser');" title="多选地址薄" class="easyui-linkbutton" plain="true" iconCls="icon-multibook"></a>
	            				<span id="next_user" style="color:red;display:none;">*</span>
	            			</td>
	            		</tr>
	            		<tr>
	            			<td class="td_title">设置通知人：</td>
	            			<td class="td_data" id="msgUsers_td">
	            				<s:textfield name="model.msgUsers" theme="simple"/>
	            				<a href="###" onclick="multi_book('editForm_model_msgUsers');" title="多选地址薄" class="easyui-linkbutton" plain="true" iconCls="icon-multibook"></a>
	            				<span id="msg_user" style="color:red;display:none;">*</span>
	            			</td>
	            		</tr>
	            		
	            		<tr style="display:none">
	            			<td class="td_title">通知类型：</td>
	            			<td class="td_data" id="msgType_td">
	            				<s:checkboxlist theme="simple" list="#{'msgSysmsg':'系统消息', 'msgEmail':'邮件', 'msgIm':'即时通讯','msgSms':'短信'}"  name="msgType"></s:checkboxlist>
	            				<span id="msg_type" style="color:red;display:none;">*</span>
	            			</td>
	            		</tr>
	            		<tr>
	            			<td class="td_title">权限设置：</td>
	            			<td class="td_data" id="">
	            				<s:textarea name="model.sjPurview" cssStyle="width:240px;height:60px;"/>
	            				<a href="javascript:openAuthorityBook('editForm_model_sjPurview');" class="easyui-linkbutton" plain="true" iconCls="icon-add">权限地址簿</a>
	            			</td>
	            		</tr>
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
