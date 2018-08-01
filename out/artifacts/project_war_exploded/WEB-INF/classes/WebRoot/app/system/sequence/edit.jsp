<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
	<title>序列维护</title>
	<s:head/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link href="iwork_css/system/sequence_edit.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script language="javascript" src="iwork_js/commons.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/system/sequence_edit.js"   ></script>
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
 //==========================装载快捷键===============================//快捷键
		 jQuery(document).bind('keydown',function (evt){		
		    if(evt.ctrlKey&&evt.shiftKey){
			return false;
		   }
		   else if(evt.ctrlKey && event.keyCode==83){ //Ctrl+s /保存操作
			         save(); return false;
		     }
}); //快捷键
</script>

</head>

<body  class="easyui-layout">
<div region="center" style="padding:3px;border:0px">
<s:form name="editForm" action="sequence_save" theme="simple">
	<table border="0" style="margin-left:10px;" cellspacing="0" cellpadding="3">
 	 	<tr>
   		  	<td class="td_title">序列键值：</td><td class="td_data"><s:textfield  cssStyle="width:220px" name="model.sequencekey"/>&nbsp;<span style='color:red'>*</span></td>
   	 	</tr>
   	 	<tr>
   		 	 <td class="td_title">序列号：</td><td class="td_data"><s:textfield  cssStyle="width:120px" name="model.sequencevalue"/>&nbsp;<span style='color:red'>*</span></td>
   	 	</tr>
   	 	<tr>
   			 <td class="td_title">序列描述：</td><td class="td_data"><s:textarea  cssStyle="width:300px;height=100" name="model.sequencedesc"></s:textarea></td>
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
  	 <a href='javascript:win_close();' class="easyui-linkbutton"  iconCls="icon-cancel" >关闭</a>
</div>
</body>
</html>
