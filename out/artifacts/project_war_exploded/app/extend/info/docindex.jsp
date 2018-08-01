<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.iwork.app.conf.SystemConfig"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<html>
<head>
<title>公告历史版本</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="iwork_css/common.css" type="text/css">
		<link rel="stylesheet" href="iwork_css/plugs/exam.css" type="text/css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
		<link rel="stylesheet" href="iwork_css/jquerycss/zTreeStyle.css" type="text/css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"  ></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"  > </script>
		<script charset="utf-8" src="iwork_js/kindeditor/lang/zh_CN.js"></script>
		<script charset="utf-8" src="iwork_js/kindeditor/plugins/code/prettify.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
<script src="iwork_js/plugs/iweboffice_load.js"></script>
<script type="text/javascript">
 $(function(){
  	try{
  	Load();
  		}catch(e){
  		}
  	}); 
  function Load(){
 
	try{
		var host = window.location.host;
		
		var protocolStr = document.location.protocol;  
		
		if(protocolStr == "https:")  {
    		iformMain.WebOffice.WebUrl="https://"+host+"/weboffice/";
    		
    	}else{
    		iformMain.WebOffice.WebUrl="http://"+host+"/weboffice/";
    	}
		iformMain.WebOffice.RecordID="<s:property value="recordid"/>"; 
		iformMain.WebOffice.Compatible=false;
		iformMain.WebOffice.FileType=".docx";
		iformMain.WebOffice.Template="2344444";  
		iformMain.WebOffice.EditType="2,1"; 
		iformMain.WebOffice.MaxFileSize = 8 * 1024; 
		iformMain.WebOffice.Language="CH";//
		iformMain.WebOffice.PenColor="#FF0000";
		iformMain.WebOffice.PenWidth="1";
		iformMain.WebOffice.Print="1";
		iformMain.WebOffice.ShowToolBar="0";
		iformMain.WebOffice.ShowMenu="0";
		iformMain.WebOffice.WebRepairMode = true;
		iformMain.WebOffice.WebOpen();
		iformMain.WebOffice.height=window.screen.height-80;
		
	}catch(e){
	   alert(e.message);
		$("#plugsTd").html("<div style='font-size:18px;padding:10px;test-align:center'>在线OFFICE编辑插件未安装，请点击下载&nbsp;<a target='_blank' href='iwork_plugs/iWebPlugin_V1.0.0.18.rar'>下载插件</a><div>");
	}
}
function closePage(){
	window.close();
}
function saveMeetDoc(){
	SaveDocument();
	alert('保存成功');
}
// 打开本地文件
function openLocalDoc(){
	WebOpenLocal();
}
// 另存为
function saveLocalDoc(){
	WebSaveLocal();
}
</script> 
<style type="text/css">
   .title{
   		height:30px;
   		padding:5px;
   		border-bottom:1px solid #ccc;
   }
   .title h1{
   		font-size:20px;
   }
</style>
</head>
<body class="easyui-layout" > 
<div region="north" style="padding:0px;border:0px;">
<div class="title">
<h1><IMG alt="" src="iwork_img/menulogo_zidian.gif" border="0"/><s:property value="title"/></h1>
</div>
<!--  <div class="tools_nav">
 		<a  href="#" style="margin-left:1px;margin-right:1px"  onclick='saveMeetDoc()' text='Ctrl+s' class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>
		<a href="#" style="margin-left:1px;margin-right:1px"  onclick='location.reload();' class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
	    <a href="#"  style="margin-left:1px;margin-right:1px" onclick='closePage();' class="easyui-linkbutton" plain="true" iconCls="icon-cancel">关闭</a>
	    <a href="#"  style="margin-left:1px;margin-right:1px" onclick='openLocalDoc();' class="easyui-linkbutton" plain="true" iconCls="icon-ftv2folderopen">打开本地文件</a>
	    <a href="#"  style="margin-left:1px;margin-right:1px" onclick='saveLocalDoc();' class="easyui-linkbutton" plain="true" iconCls="icon-save">另存为</a>
      </div>  -->
      </div>
      <div region="center">
      		 <form name="iformMain" method="post">  <!--保存iWebOffice后提交表单信息--> 
			<table width="100%"  cellspacing="0" cellpadding="0"><tr><td id="plugsTd">
			<s:property value="html" escapeHtml="false"/>
			
			</td> 
			</tr>
			</table>
  		</form>
      </div>
 
</body>
</html> 
<<s:if test="isLoadBookMarks==1">
<script>
setTimeout("iformMain.WebOffice.WebLoadBookMarks()",1000);
</script>
</s:if>