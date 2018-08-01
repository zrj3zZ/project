<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
	<title>服务管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link href="iwork_css/system/sysservice_edit.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
<script type="text/javascript" src="iwork_js/system/sysservice_edit.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"   ></script>
>
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

//带年月日,格式为yyyy-MM-dd  
function showtimedemo(){  
    WdatePicker({  
        dateFmt:'yyyy-MM-dd'  
        });  
}
</script>

</head>
<body class="easyui-layout">
<div region="center" style="padding:3px;border:0px">
<s:form name="editForm" action="sysService_save" theme="simple">
     <table border="0" style="margin-left:10px;" cellspacing="0" cellpadding="3">
   			<tr>
   				<td class="td_title">服务名称:</td><td class="td_data"><s:textfield  cssStyle="width:220px" id="servicename" name="model.servicename" theme="simple"/>&nbsp;<span style='color:red'>*</span></td>
   			</tr>
   			<tr>
   				<td class="td_title">服务键值:</td><td class="td_data"><s:textfield  cssStyle="width:120px" id="servicekey"  name="model.servicekey" theme="simple"/>&nbsp;<span style='color:red'>*</span></td>
   			</tr>
   			<tr>
   				<td class="td_title">服务描述:</td><td class="td_data"><s:textarea  cssStyle="width:300px;height:80px" id="servicedesc"  name="model.servicedesc" theme="simple"></s:textarea>&nbsp;<span style='color:red'>*</span></td>
   			</tr>
   			<tr>
   				<td class="td_title">服务状态:</td><td class="td_data"><s:radio id="status"  name="model.status" list="#{'1':'启动','0':'停止'}" theme="simple"/>&nbsp;<span style='color:red'>*</span></td>
   			</tr>
   			<tr> 
   				<td class="td_title">有效日期:</td><td class="td_data">
   				<s:textfield format="yyyy-MM-dd" id="startdate" name="model.startdate" cssClass="Wdate"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'{%y-20}-%M-%d'})" theme="simple" value="" />
   				
   				
   				&nbsp;到&nbsp;<s:textfield  id = "enddate" format="yyyy-MM-dd" cssClass="Wdate"  name="model.enddate" onfocus="WdatePicker({minDate:'#F{$dp.$D(\"startdate\")}'})"  theme="simple" />&nbsp;<span style='color:red'>*</span></td>
   			</tr>  
   	  </table> 
   	  
		 	<s:hidden name="model.id" />
		 	<s:hidden name="model.orderindex" />
		 	<div style="display:none" id="tempSd" ><s:date name='model.startdate'format='yyyy-MM-dd' /></div>
		 	<div style="display:none" id="tempEd" ><s:date name='model.enddate'format='yyyy-MM-dd' /></div>
		 	
</s:form>
</div>
     <div region="south" border="false" style="text-align: right; height: 50px; line-height: 30px;padding-top:5px;padding-right:15px;margin-bottom:20px;">
 			<a href='javascript:save();' class="easyui-linkbutton"  iconCls="icon-ok" >保存</a>
  			<a href='javascript:win_close();' class="easyui-linkbutton"  iconCls="icon-cancel" >关闭</a>
     </div>  	
     
     <script>
     	$("#startdate").val($("#tempSd").text());
     	$("#enddate").val($("#tempEd").text());
     </script>		
</body>
</html>
