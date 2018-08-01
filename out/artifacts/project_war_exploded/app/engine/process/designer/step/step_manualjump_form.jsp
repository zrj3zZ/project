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
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<link href="iwork_css/reset.css" rel="stylesheet" type="text/css" />
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
		input {
		 color:#0000FF;
		}
		textarea{
		color:#0000FF;
		}
		body {
			margin-left: 0px;
			margin-top: 0px;
			margin-right: 0px;
			margin-bottom: 0px;
		}
		.state{
		color:red;
		font-size:12px;
		}
	</style>
	<script type="text/javascript">
	var api = art.dialog.open.api, W = api.opener;
		//修改路由策略
		function showForm(obj){
		    $('form').attr('action','sysFlowDef_stepRoute!loadform.action');
		    $("#editForm").submit();
		}
		//选择策略
		function selectRoute(obj){
		    $('form').attr('action','sysFlowDef_stepRoute!selectRoute.action');
		    $("#editForm").submit();
		}
		
		// 增加必填提示
		function setActionType(obj){
			if(obj.value=="isTriger"){
				if($("#actionType-1").attr('checked')){
					$('#triger_type').css('display','');
					$('#triger_map').css('display','');
				}else{
					$('#triger_type').css('display','none');
					$('#triger_map').css('display','none');
					$('#mdoel_trigerTypeJAVA').attr('checked',false);
					$('#mdoel_trigerTypeSQL').attr('checked',false);
					$('#mdoel_trigerTypeJavaScript').attr('checked',false);
				    $('#editForm_model_trigerMap').val('');
				    
				}
			}else if(obj.value=="isRemind"){
				if($("#actionType-2").attr('checked')){
					$('#msg_type').css('display','');
					$('#msg_user').css('display','');
				}else{
					$('#msg_type').css('display','none');
					$('#msg_user').css('display','none');
				    $('#msgType-1').attr('checked',false);
				    $('#msgType-2').attr('checked',false);
				    $('#msgType-3').attr('checked',false);
				    $('#msgType-4').attr('checked',false);
				    $('#editForm_model_msgUsers').val('');
				}
			}
		}
		//表单验证
		function validate(){
		     var mjName=$.trim($('#editForm_model_mjName').val());
		     $('#editForm_model_mjName').val(mjName);
		     var mjType=$('#editForm_model_mjType').val();   
		     var bool1=$('#actionType-1').attr('checked'); 
		     var bool2=$('#actionType-2').attr('checked');   
		     if(mjName==""){
		           art.dialog.tips("菜单名称不能为空！",2);
		           $('#editForm_model_mjName').focus();
		           return false;
		     } 
		     if(mjType==""){
		           art.dialog.tips("跳转动作不能为空！",2);
		           $('#editForm_model_mjType').focus();
		           return false;
		     }
		     if(bool1){
		          var bool_1=$('#mdoel_trigerTypeJAVA').attr('checked');
		          var bool_2=$('#mdoel_trigerTypeSQL').attr('checked');
		          var bool_3=$('#mdoel_trigerTypeJavaScript').attr('checked');
		          if(!bool_1&!bool_2&!bool_3){
		               art.dialog.tips("触发器类型不能为空！",2);
		               return false;
		          }
		          var trigerMap=$.trim($('#editForm_model_trigerMap').val());
		          $('#editForm_model_trigerMap').val(trigerMap);
		          if(trigerMap==""){
		               art.dialog.tips("脚本类路径不能为空！",2);
		               $('#editForm_model_trigerMap').focus();
		               return false;
		          }
		          if(length2(trigerMap)>256){
		               art.dialog.tips("脚本类路径过长！",2);
		               $('#editForm_model_trigerMap').focus();
		               return false;
		          }
		     }
		     if(bool2){
		           var msgUsers=$.trim($('#editForm_model_msgUsers').val());
		           $('#editForm_model_msgUsers').val(msgUsers);
		           if(msgUsers==""){
		               art.dialog.tips("设置通知人不能为空！",2);
		               $('#editForm_model_msgUsers').focus();
		               return false;
		          }	
		          if(length2(msgUsers)>256){
		               art.dialog.tips("设置通知人过长！",2);
		               $('#editForm_model_msgUsers').focus();
		               return false;
		          }	         
		     }		      
		     if(length2(mjName)>256){
		           art.dialog.tips("菜单名称过长！",2);
		           $('#editForm_model_mjName').focus();
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
		//执行退出
		function cancel(){
			api.close();
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
					    width:500,
					    height:510
					 });
		}
	</script>
</head>
<body  class="easyui-layout">
		<div region="center" style="padding:13px;border:0px">
            	<s:form id ="editForm" name="editForm" action="sysFlowDef_stepManualJump!save.action"  theme="simple">
	            	<table border="0"  cellspacing="0" cellpadding="0">
	            		<tr>
	            			<td class="td_title">菜单名称:</td>
	            			<td class="td_data">
	            				<s:textfield name="model.mjName" cssStyle="width:180px;"></s:textfield>
	            				<span style='color:red'>*</span>	            				
	            			</td>
	            		</tr>
	            		<tr>
	            			<td class="td_title">跳转动作:</td>
	            			<td class="td_data">
								<s:property value="mjTypeListHtm"   escapeHtml="false"/> 								
								<span style='color:red'>*</span>
							</td>
	            		</tr>
	            		<tr>
	            			<td class="td_title"></td><td class="td_data">
									<s:checkboxlist  onclick="setActionType(this);" theme="simple" list="#{'isRemind':'发送通知'}"  name="actionType"></s:checkboxlist>
<label for="actionType-1" class="checkboxLabel">执行触发器</label>
									
							</td>
	            		</tr>	            		
	            		<tr style="display:none">
	            			<td class="td_title">触发器类型:</td>
	            			<td class="td_data"  id="trigerType_td">
								<s:radio  value="model.trigerType" list="#{'JAVA':'JAVA','SQL':'SQL'}" id="mdoel_trigerType" name="model.trigerType" theme="simple"/>
								<input type="radio" name="model.trigerType" id="mdoel_trigerTypeJavaScript" disabled="disabled" value="JavaScript"/><label for="mdoel_trigerTypeJavaScript">JavaScript</label>
								<span id="triger_type" style="color:red;display:none;">*</span>
							</td>
	            		</tr> 
	            		<tr  style="display:none">
	            			<td class="td_title">脚本类路径:</td>
	            			<td class="td_data" id="trigerMap_td">
	            				<s:textfield name = "model.trigerMap" cssStyle="width:280px;"></s:textfield>
								<span id="triger_map" style="color:red;display:none;">*</span>
							</td>
	            		</tr>
	            		<tr style="display:none">
	            			<td class="td_title">通知类型：</td>
	            			<td class="td_data" id="msgType_td">
	            				<s:checkboxlist theme="simple" disabled="true" list="#{'msgSysmsg':'系统消息', 'msgEmail':'邮件', 'msgIm':'即时通讯','msgSms':'短信'}"  name="msgType"></s:checkboxlist>
	            				<span id="msg_type" style="color:red;display:none;">*</span>
	            			</td>
	            		</tr>
	            		<tr>
	            			<td class="td_title">通知人：</td>
	            			<td class="td_data" id="msgUsers_td">
	            				<s:textfield name = "model.msgUsers"  cssStyle="width:280px;"></s:textfield>
	            				<a href="###" onclick="multi_book('editForm_model_msgUsers');" title="多选地址薄" class="easyui-linkbutton" plain="true" iconCls="icon-multibook"></a>
	            				<span id="msg_user" style="color:red;display:none;">*</span>
							</td>
	            		</tr>
	            	</table>
	                <s:hidden name="model.actDefId"/>
	                <s:hidden name="model.actStepId"/>
	                <s:hidden name="model.prcDefId"/>
	                <s:hidden name="model.orderIndex"/>
	                <s:hidden name="model.id"/>
	                <s:hidden name="actDefId"/>
	                <s:hidden name="actStepDefId"/>
	                <s:hidden name="prcDefId"/>
	                <s:hidden name="id"/>
	                 </s:form> 
	     </div>
	                <div region="south" border="false" style="text-align: right; height: 50px; line-height: 30px;padding-top:5px;padding-right:15px;">
		                <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:doSubmit();" >
		                    保存</a> <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:cancel()">关闭</a>
		            </div>
              
           
</body>
</html>
