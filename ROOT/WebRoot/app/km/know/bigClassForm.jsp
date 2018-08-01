<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
	<title>分类维护</title>
	<s:head/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript" src="iwork_js/km/know_bigclass_form.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
 //==========================装载快捷键===============================//快捷键
 var api = art.dialog.open.api, W = api.opener;
		 jQuery(document).bind('keydown',function (evt){		
		    if(evt.ctrlKey&&evt.shiftKey){
			return false;
		   }
		   else if(evt.ctrlKey && event.keyCode==83){ //Ctrl+s /保存操作
			         save(); return false;
		     }
}); //快捷键


 /*多选地址薄*/
    function multi_book(defaultField) {
		 var obj = new Object();
		 obj.defaultField = defaultField;
         obj.dialogName = "bigclass_edit";
		//var url = "multibook_index.action?code=" + code + "&parentDept=" + parentDept + "&currentDept=" + currentDept + "&startDept=" + startDept + "&isOrg=" + isOrg + "&isRole=" + isRole + "&isGroup=" + isGroup;
		var url = "multibook_index.action?1=1";
		
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
				    width:500,
				    height:510
				 });
		/*
		var obj = new Object();
		obj.parentDept = parentDept;
		obj.currentDept = currentDept;
		obj.startDept = startDept;
		obj.defaultField = defaultField;
		obj.targetUserId = targetUserId;
		obj.targetUserName = targetUserName;
		obj.targetDeptId = targetDeptId;
		obj.targetDeptName = targetDeptName;
		obj.win = window;
		art.dialog.open(url,{
			id:"radioBookDialog",
			title: '多选地址簿',
			pading: 0,
			lock: true,
			width: 650,
			height: 550,
			top:'25%',
			left:'5%',
			data:obj
		});*/
		//$.dialog.data("paramObj",obj);
	}
</script>

</head>

<body  class="easyui-layout">
<div region="center" style="padding:3px;border:0px">
<s:form name="editForm" id="editForm" action="know_bigclass_save" theme="simple">
	<table border="0" style="margin-left:10px;" cellspacing="0" cellpadding="0">
 	 	<tr>
   		  	<td class="td_title">分类名称：</td><td class="td_data"><s:textfield  cssStyle="width:220px" name="model.cname"/>&nbsp;<span style='color:red'>*</span></td>
   	 	</tr>
   	 	<!--<tr>
   		 	 <td class="td_title">分类排序：</td><td class="td_data"><s:textfield readonly="true" cssStyle="width:120px;background-color:#d1d1d1;" name="model.corder"/>&nbsp;<span style='color:red'>*</span></td>
   	 	</tr>-->
   	 	<tr>
   			 <td class="td_title">分类状态：</td><td class="td_data"><s:radio id="status"  name="model.ctype" list="#{'可用':'可用','停用':'停用'}" />&nbsp;<span style='color:red'>*</span></td>
   	 	</tr>
   	 	<tr>
   			 <td class="td_title">分类专家：</td>
   			 <td class="td_data">
   			 	<s:textarea cssStyle="width:320px;height=50;"  id="cexpert" name="model.cexpert"></s:textarea>
   			    <a href="javascript:multi_book('cexpert');" class="easyui-linkbutton" plain="true" iconCls="icon-add">多选地址簿</a>
   			 </td>
   	 	</tr> 
     </table>
     
  <s:hidden name="model.id" />
  <s:hidden name="model.corder" />
</s:form>     
</div> 
  
<div region="south" border="false" style="text-align: right; height: 50px; line-height: 30px;padding-top:5px;padding-right:15px;margin-bottom:20px;"> 						
     <a href='javascript:saveItem();' class="easyui-linkbutton"  iconCls="icon-ok" >保存</a>
  	 <a href='javascript:win_close();' class="easyui-linkbutton"  iconCls="icon-cancel" >关闭</a>
</div>
</body>
</html>
