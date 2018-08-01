<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="iwork" uri="/WEB-INF/sys-commonsTags.tld" %>

<html> 
<head>
	<title>兼职信息维护</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script language="javascript" src="iwork_js/commons.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
		var mainFormValidator;
		var api = art.dialog.open.api,W = api.opener;
		var dialog;
		$(document).ready(function(){
			mainFormValidator =  $("#editForm").validate({
				 errorPlacement: function (error, element) {
		             error.appendTo(element.parent());    //将错误信息添加当前元素的父结点后面               
		        }
		   }); 
		    mainFormValidator.resetForm();
		});	
		function dept_book(targetDeptId, targetDeptName, defaultField) {
			var url = "deptbook_index.action?1=1";
			if(targetDeptId!=''){
				url+="&targetDeptId="+targetDeptId;
			}
			if(targetDeptName!=''){
				url+="&targetDeptName="+targetDeptName;
			}
			if(defaultField!=''){
				url+="&defaultField="+defaultField;
			}
			//获得input内容
			var v = document.getElementById(defaultField);
			if(v.value!=""){
				url+="&input="+v.value;
			}
			
			art.dialog.open(url,{
						id:'addressDialog', 
						title:"地址簿",
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width: 500,
						height: 510
					 });
		}
		function save(){
			var valid = mainFormValidator.form(); //执行校验操作 
			if(!valid){
					return;
			}
			var obj = $('#editForm').serialize(); 
		     $.post("usermap_save.action",obj,function(data){
		           if(data=="success"){ 
		        	   art.dialog.tips("保存成功",2);
		        	   setTimeout("api.close()",2000);
		        	}
		        	
		     }); 
		}
</script>
</head>
<body class="easyui-layout" >
<div region="north" border="false" split="false" >
	<div class="tools_nav"> 
			<a href="javascript:save()" class="easyui-linkbutton"  plain="true" iconCls="icon-save" >保存</a>
			<a href="javascript:window.location.reload();" class="easyui-linkbutton"  plain="true" iconCls="icon-reload" >刷新</a>
	</div>
</div>
<div region="center" border="false" split="false" > 
<s:form name="editForm" id="editForm"> 
                    <table width="100%" border="0" cellspacing="0" cellpadding="5">
                                      <td align="right" class="form_title">兼任部门ID/名称:</td>
                                      <td class="form_data"><s:textfield theme="simple" readonly="true"  cssStyle="width:60px;color:#999999"   id="userMapDeptId" name="model.departmentid" />/<s:textfield theme="simple" readonly="true" cssClass="{required:true}"  cssStyle="width:80px;color:#999999" id="userMapDeptName" name="model.departmentname" /><a href="###" onclick="dept_book('userMapDeptId','userMapDeptName','userMapDeptName');" title="部门地址薄" class="easyui-linkbutton" plain="true" iconCls="icon-deptbook"></a></td>
                                    </tr>
                                    <tr>
                                      <td align="right" class="form_title">兼任角色:</td>
                                      <td  class="form_data">
<s:select name="model.orgroleid" cssClass="{required:true}" list="%{availableItems}" listValue="rolename" value="model.orgroleid" listKey="id"  headerKey="" headerValue="--请选择--" theme="simple"  ></s:select>                                      </td>
                                    </tr>
                                    <tr>
                                      <td class="form_title">是否为部门负责人:</td>
                                      <td class="form_data">
                                        <s:radio value="model.ismanager" cssClass="{required:true}"  list="#{'1':'是','0':'否'}"  name="model.ismanager" theme="simple" ></s:radio>
                                      </td> 
                                    </tr>
                                    <tr>
                                      <td class="form_title">兼任类型:</td>
                                      <td  class="form_data">
                                        <s:radio  value="model.usermaptype" cssClass="{required:true}"  list="#{'0':'行政兼任','1':'系统兼任'}"  name="model.usermaptype" theme="simple"></s:radio>
                                      </td>
                                    </tr>
                                </table>
		 <s:hidden name="model.id" value="%{id}"/>
		 <s:hidden name="model.userid" id="userid" value="%{userid}"/>
</s:form>
</div>
</body>
</html>
