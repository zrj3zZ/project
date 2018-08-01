<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<html>
<head>
<title>OFFICE在线编辑：<s:property value="fileName"/></title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="iwork_css/common.css" type="text/css">
		<link rel="stylesheet" href="iwork_css/plugs/exam.css" type="text/css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
<script src="iwork_js/plugs/iweboffice_load.js"></script>
<script type="text/javascript">
$(function(){
	var t=document.documentElement.clientHeight;  
		window.onresize = function(){  
			if(t != document.documentElement.clientHeight){ 
				t = document.documentElement.clientHeight; 
				t = t-50;
				$("#WebOffice").attr("height",t); 
			} 
		} 
  	try{
  		Load();
 	}catch(e){}
 	
 	
  function Load(){
	try{
		iformMain.WebOffice.WebUrl="<s:property value="url"/>/weboffice/"
		iformMain.WebOffice.RecordID="<s:property value="fileUUID"/>"; 
		iformMain.WebOffice.FileName="<s:property value="fileName"/>"; 
		iformMain.WebOffice.Compatible=false;;
		iformMain.WebOffice.FileType="<s:property value="fileType"/>";
		iformMain.WebOffice.UserName="<s:property value="userId"/>"; 
		iformMain.WebOffice.EditType="<s:property value="editTypeStr"/>"; 
		iformMain.WebOffice.MaxFileSize = 10 * 1024; 
		iformMain.WebOffice.Language="CH";
		iformMain.WebOffice.PenColor="#FF0000";
		iformMain.WebOffice.PenWidth="1";
		iformMain.WebOffice.Print="1";
		iformMain.WebOffice.ShowToolBar="0";
		iformMain.WebOffice.ShowMenu="0";
		iformMain.WebOffice.WebRepairMode = true;
		iformMain.WebOffice.WebOpen();
		t = document.documentElement.clientHeight; 
		t = t-50;
		$("#WebOffice").attr("height",t); 
	}catch(e){
		alert(e);
	}
}
	
}); 

function closePage(){
	window.close();
}
</script> 
</head>
<body class="easyui-layout" >
<div region="north" style="padding:0px;border:0px;">
<div class="tools_nav">
<s:if test="edittype==\"w\"">
 		<a  href="#" style="margin-left:1px;margin-right:1px"  onclick='SaveDocument()' text='Ctrl+s' class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>
</s:if>
				<a href="#" style="margin-left:1px;margin-right:1px"  onclick='location.reload();' class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
				<a href="#"  style="margin-left:1px;margin-right:1px" onclick='closePage();' class="easyui-linkbutton" plain="true" iconCls="icon-cancel">关闭</a>
      </div>  
      </div>
      <div region="center">
      		 <form name="iformMain" method="post">  <!--保存iWebOffice后提交表单信息--> 
			<table width="100%"  cellspacing="0" cellpadding="0"><tr><td >
			<object id="WebOffice" width="98%" height="550" classid="clsid:8B23EA28-2009-402F-92C4-59BE0E063499" codebase="iwork_plugs/iWebOffice2009.cab#version=10,7,2,8">
			</object>
			</td> 
			</tr>
			</table>
  		</form>
      </div>
 
</body>
</html> 
<<script type="text/javascript">
<!--
//-->
</script>
