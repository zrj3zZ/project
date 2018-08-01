<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html> 
<head> 
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>版本历史</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/bootstrap/easyui.css">
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css"/>
	<link href="iwork_css/reset.css" rel="stylesheet" type="text/css"/> 
	<link href="iwork_css/pformpage.css" rel="stylesheet" type="text/css"/>
	<script type="text/javascript" src="iwork_js/commons.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.src.js"  > </script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=default"></script>
	<script type="text/javascript" src="iwork_js/pformpage.js"></script>
	<script type="text/javascript" src="iwork_js/json.js"></script>
	<script type="text/javascript" >
	
	function openDoc(recordid){
		/* 金格插件*/
		
		var url = encodeURI("ggls_docShow.action?recordid="+recordid); 
		/*卓正插件
		var pageUrl =encodeURI( "getlsbb.action?NOTICEFILE="+recordid);
		var target = "_blank";
		  var iTop = (window.screen.availHeight - 30 - 100) / 2; 
        //获得窗口的水平位置 
        var iLeft = (window.screen.availWidth - 10 - 100) / 2; 
		var page = window.open('form/loader_frame.html',target,'width='+100+',left='+iLeft+',top='+iTop+',height=100,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		//var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;*/
		window.location.href = url;
	}
	function downloadDoc(recordid){
		var fj=document.getElementById("fj").value;
		var url =encodeURI("ggls_docDownload.action?recordid="+recordid+"&fj="+fj);
		window.location.href = url;
	}
	</script>
<style type="text/css">
  .header td{
			height:30px;
			font-size:12px;
			padding:3px;
			white-space:nowrap;
			padding-left:5px;
			background:url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;
			border-top:1px dotted #ccc;
			border-right:1px solid #eee;
		}  
		.cell td{
			margin:0;
			padding:3px 4px;
			height:25px;
			font-size:12px;
			white-space:nowrap;
			word-wrap:normal;
			overflow:hidden;
			text-align:left;
			border-bottom:1px dotted #eee;
			border-top:1px dotted #fff;
			border-right:1px dotted #eee;
		}
		.cell:hover{
			background-color:#F0F0F0;
		}
</style>
</head>
<body  >
  <div region="center" border="false"  align="center" style="border:0px solid #C0C0C0;text-align:left;padding:5px;">
  	<table WIDTH="100%" style="border:1px solid #efefef">
			<TR  class="header">
			
				<TD style="width:150px">保存时间</TD>
				<TD style="width:100px">用户名</TD>
				<TD style="width:60px">版本说明</TD>
				<TD style="width:30px">版本</TD>
				<TD style="width:30px">操作</TD>
			</TR>
			<s:iterator value="list"  status="status">
			<TR class="cell">
				<TD><s:property value="dotime"/></TD>
				<TD><s:property value="username"/></TD>
				<TD>&nbsp;<s:property value="descript"/></TD>
				<TD><a href="#" onclick="openDoc('<s:property value="recordid"/>');" class="easyui-linkbutton" plain="false">查看</a></TD>
				<TD><a href="#" onclick="downloadDoc('<s:property value="recordid"/>');" class="easyui-linkbutton" plain="false">下载</a></TD>
			</TR>
			</s:iterator>
			<input type="hidden" id="fj" name="fj" value="<s:property value="fj"/>"/>
		</table>
  </div>
  <form id="editForm" name="editForm"  method="post">
			<div style="display:none">
			<object id="WebOffice" width="98%"  classid="clsid:8B23EA28-2009-402F-92C4-59BE0E063499" codebase="iwork_plugs/iWebOffice2009.cab#version=10,7,2,8">
			</object>
			</div>
				
			</form>
  
</body>
</html>