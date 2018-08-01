<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
	<title>角色管理</title>
	<s:head/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link href="iwork_css/org/role_edit.css" rel="stylesheet" type="text/css" />
	<link href="iwork_skins/_def/css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script language="javascript" src="iwork_js/commons.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
		var api = art.dialog.open.api;
		var mainFormValidator;
		$(document).ready(function(){
			mainFormValidator =  $("#editForm").validate({
				//出错时增加的标签
		        errorPlacement: function (error, element) {
		                error.appendTo(element.parent());    //将错误信息添加当前元素的父结点后面               
		        }
		   }); 
		    mainFormValidator.resetForm();
		 });
				
		function save(){
			var valid = mainFormValidator.form(); //执行校验操作 
			if(!valid){
				  art.dialog.tips("表单验证失败，请检查信息项是否合法",2);
					return;
			}
			 var obj = $('#editForm').serialize(); 
		     $.post("role_save.action",obj,function(data){
		           if(data=="success"){ 
		        	    art.dialog.tips("保存成功",2);
		            	api.close();
		        	}
		     });
		}
		  
		//关闭窗口
		function cancel(){
			api.close();
		}
	</script>
	<style type="text/css">
		.form_title{  
			font-family:黑体;
			font-size:14px;
			text-align:right;
		}
		.form_data{
			font-family:宋体;
			font-size:12px;
			text-align:left;
			color:0000FF; 
		}
	</style>
</head>
<body>
<s:form name="editForm" id="editForm"  action="role_save" validate="true" theme="simple">
<table>
   			<tr>
   				<td style="padding-top:10px;">
   		<table wdith="100%"  cellspacing="10" cellpadding="5">
   			<tr>
   				<td class="form_title" align="right">角色名称：</td>
   				<td class="form_data" ><s:textfield theme="simple"  cssClass="{maxlength:64,required:true}"   cssStyle="width:220px" name="model.rolename"/>&nbsp;<span style='color:red'>*</span></td>
   			</tr>
   			<tr>
   				<td class="form_title"  align="right">角色组：</td>
   				<td class="form_data" ><s:select name="model.groupid"  cssClass="{required:true}"   list="list" listKey="id" listValue="groupName" value="model.groupid"  headerKey="" headerValue="--选择角色分组--"></s:select>&nbsp;<span style='color:red'>*</span></td>
   			</tr>
   			
   			<tr>
   				<td class="form_title"  align="right">角色类型：</td><td class="form_data" >
   						<s:radio  name="model.roletype"  cssClass="{required:true}"  cssStyle="border:0px;" list="#{'行政角色':'行政角色','授权角色':'授权角色'}"></s:radio>&nbsp;<span style='color:red'>*</span>
   				</td>
   			</tr>
   			<tr>
   				<td class="form_title"  align="right">外观样式：</td><td class="form_data" ><s:select cssClass="{maxlength:64,required:true}" value="model.lookandfeel"  name="model.lookandfeel" list="#{'def':'标准版样式'}" headerKey="def" headerValue="--请选择--" theme="simple"/>&nbsp;<span style='color:red'>*</span></td>
   			</tr>
   			<tr>
   				<td class="form_title"  align="right">角色描述:</td><td class="form_data" ><s:textarea theme="simple" cssClass="{maxlength:256}"  cssStyle="width:300px" name="model.roledesc"></s:textarea></td>
   			</tr>
   			<tr>
   				<td class="form_title"  align="right">备注:</td><td class="form_data" ><s:textarea theme="simple"  cssClass="{maxlength:256}" cssStyle="width:300px" name="model.memo"></s:textarea></td>
   			</tr> 
   		</table>
	</td>
  </tr> 
  <tr>
  		<td align="right">
  			<a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:save();" >确定</a> 
                <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:cancel()">取消</a>
  		</td>
  </tr>
</table> 
		<s:if test="null == model">
		 	<s:hidden name="model.id" value="%{id}"/>
		 	<s:hidden name="model.orderindex" value="%{id}"/>
		 </s:if>	 	
	     <s:else>
		 	<s:hidden name="model.id" />
		 	<s:hidden name="model.orderindex"/>
		 </s:else>
		 <s:hidden name="model.logincounter" />
		 <s:hidden name="model.userstate" />
		 <s:hidden name="queryName" />
		 <s:hidden name="queryValue" />	     
</s:form>
</body>
</html>
