<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
	<title>权限组管理</title>
	<s:head/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link href="iwork_css/system/purgroup_edit.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
<script type="text/javascript" src="iwork_js/system/purgroup_edit.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
 //==========================装载快捷键===============================//快捷键
		 jQuery(document).bind('keydown',function (evt){		
		    if(evt.ctrlKey&&evt.shiftKey){
			return false;
		   }
		   else if(evt.ctrlKey && event.keyCode==83){ //Ctrl+s /保存操作
			         $("#purgroup_save").submit(); return false;
		     }
}); //快捷键

	</script>
</head>
<body class="easyui-layout">
<div region="center" style="padding:3px;border:0px">
<s:form name="editForm" id="editForm"   action="purgroup_save" theme="simple">
      <table border="0" align="center" cellspacing="10" cellpadding="0">
   			<tr>
   				<td class="td_title">权限组名称：</td><td class="td_data"><s:textfield  cssClass="{maxlength:32,required:true}" cssStyle="width:220px" name="model.groupname"/>&nbsp;<span style='color:red'>*</span></td>
   			</tr>
   			<tr>
   				<td class="td_title">权限组分类：</td><td class="td_data"><s:textfield  cssClass="{maxlength:32,required:true}" cssStyle="width:120px" name="model.categoryname"/>&nbsp;<span style='color:red'>*</span></td>
   			</tr>
   			<tr>
   				<td class="td_title">隶属服务：</td><td class="td_data"><s:select name="model.serviceid" list="servicelist" listKey="id" listValue="servicename"  value="model.serviceid"  cssClass="{maxlength:32,required:true}" theme="simple" headerKey="" headerValue="--请选择--"></s:select>&nbsp;<span style='color:red'>*</span></td>
   			</tr> 
   			<tr>
   				<td class="td_title">权限组描述：</td><td class="td_data"><s:textarea   cssClass="{maxlength:300}" cssStyle="width:300px;height=100" name="model.groupdesc"></s:textarea></td>
   			</tr>
   			  
   </table>
		<s:if test="null == model">
		 	<s:hidden name="model.id" value="%{id}"/>
		 </s:if>	 	
	     <s:else>
		 	<s:hidden name="model.id" />
		 </s:else>
</s:form>
</div>

 <div region="south" border="false" style="text-align: right; height: 50px; line-height: 30px;padding-top:5px;padding-right:15px;margin-bottom:20px;">
 			<a href='javascript:save();' class="easyui-linkbutton"  iconCls="icon-ok" >保存</a>
  			<a href='javascript:window_close();' class="easyui-linkbutton"  iconCls="icon-cancel" >关闭</a>
 </div>
</body>
</html>
