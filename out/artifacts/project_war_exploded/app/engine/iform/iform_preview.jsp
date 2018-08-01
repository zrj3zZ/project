
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title><s:property value='title' escapeHtml='false'/></title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/openformpage.css"/>
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
	<script type="text/javascript" src="iwork_js/engine/openformpage.js"  ></script>
	<script type="text/javascript">
		 <s:property value='script' escapeHtml='false'/>
	</script>
	<style>
		<s:property value='style' escapeHtml='false'/>
		body { font-family:Verdana; font-size:14px; margin:0;}
		#container {margin:0 auto; width:100%;}
		#header { height:25px;  margin-bottom:1px;padding-left:5px; border-bottom:1px solid #CCCCCC; background-color:#F4FCFF}
		#mainContent { height:100%; margin-bottom:1px;}
		#sidebar { float:left; width:200px;  background:#FEFEFE;padding:5px;}
		#content { margin-left:201px !important; margin-left:198px;  background:#F4FEE9; padding:5px;}
		#iforminfo { width:200px; height:120px; background:#EDF7FE;}
		#infotitle{ background:#FEFDEF; padding:3px;border-bottom:1px solid #666;}
		.iformContent {background:#EDF7FE;padding:5px;overflow:auto;display:table;border-collapse:separate;}
		.info_row{display:table-row;}
		.info_row div {display:table-cell;border-bottom:1px dotted #666}
		.info_row .info_name {width:60px;font-size:12px;}
		.info_row .info_value {width:120px;font-size:12px;color:#E8A300;}
		.iformContent ul {margin:0 0 0 2px;padding:0;list-style:inside left;color:#E8A300;}
		.iformContent li {font-size:12px;display:block; margin:0 0 0 3px; LIST-STYLE-TYPE:disc}
	</style>
</head>
<body class="easyui-layout">
	<form method="post" name="frmMain">
			<div id="container">
			  <div id="header"><a href="javascript:print();"><img src="iwork_img/printer.png" border="0" /> 打印</a>|<img src='iwork_img/application_view_list.gif' alt='隐藏预览日志' border='0'/>|<img src='iwork_img/overlays.png' alt='显示预览日志' border='0'/>|<a href="javascript:close();">关闭</a></div>
			  <div id="mainContent">
			    <div id="sidebar">
					<div id="iforminfo">
						<div id="infotitle">基本信息</div>
						<div class="iformContent">
							<s:property value="baseinfo" escapeHtml="false"/>
						</div>
					</div>
					<div id="infotitle">域绑定异常列表</div>
					<div id="iforminfo" style="height:200px;overflow:auto;">
						<div class="iformContent"><s:property value="verifyErrorInfo" escapeHtml="false"/></div>
					</div> 
					<div id="infotitle">域绑定信息列表</div>
					<div id="iforminfo"  style="height:200px;overflow:auto;">
						<div class="iformContent"><s:property value="verifySuccessInfo" escapeHtml="false"/></div>
					</div>
				</div>
			    <div id="content">
			    	<s:property value="content" escapeHtml="false"/>
			    </div>
			</div>
	</form>
	
</body>
</html>
<script language="JavaScript">  
     window.opener.location.href=window.opener.location.href;  
</script> 
 
