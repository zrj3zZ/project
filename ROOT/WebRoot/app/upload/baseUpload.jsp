<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>文件上传</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/commons.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
		<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
	var api = art.dialog.open.api, W = api.opener;
		function doUpload(){
			$("#frmMain").submit();
		}
		function closeWin(){
			api.close(); 
		}
		function setMutiUpload(){
			history.go(-1);
		}
	</script>
		<style type="text/css">
		.tab{
			border-top:1px solid #999;
			border-left:1px solid #999;
			border-right:1px solid #999;
			border-bottom:1px solid #999;
			background:#ccc;
			padding:5px;
			width:100px;
			margin-top:100px;
			cursor:pointer;
		}
		.sel{
			background:#fff;  
			cursor:auto;
			border-bottom:1px solid #fff;
		}
	</style>
  </head>
  <body class="easyui-layout">
 <div region="north" border="false" style="border-top:1px solid #efefef;padding-top:20px;overflow:hidden;text-align:left;">
 		<div>
  			<span class="tab "  onclick="setMutiUpload();">多文件上传</span>  <span class="tab sel">单文件上传</span> 
  		</div>
    </div>
 <div region="center" style="text-align:center;border-left:1px #999 solid;border-right:1px #999solid;border-top:1px #fff solid;border-bottom:0px #999 solid;padding:2px;">
   <center>
  	<h2>上传</h2>
	  <form name="frmMain" id="frmMain" action="uploadifyBaseUpload.action" method="post" enctype="multipart/form-data">
	  	<table>
	  		<tr>
	  			<td></td>
	  			<td> 
					<s:hidden name="parentColId"  id="parentColId"></s:hidden>
					<s:hidden name="parentDivId"  id="parentDivId"></s:hidden>
				</td>
	  		</tr>
	  		<tr>
	  			<td>上传文件:</td>
	  			<td><input type="file" name="uploadify"></td>
	  		</tr>
	  	</table>
	  </form>
  </center>
   </div>
    <div region="south" border="false" style="border-top:1px solid #efefef;height:35px;text-align:right;padding:5px;">
    	<a href="#" onclick="doUpload();return false;" class="easyui-linkbutton" plain="false" iconCls="icon-add">确定上传</a>
  		<a href="#" onclick="closeWin();" class="easyui-linkbutton" plain="false" iconCls="icon-cancel">关闭</a>
  		
    </div>
  </body>
</html>
