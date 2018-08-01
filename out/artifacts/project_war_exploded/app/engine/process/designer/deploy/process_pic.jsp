<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" charset="gb2312"></script>
	<script type="text/javascript" src="iwork_js/processListIndex.js" charset="gb2312"></script>
	<script type="text/javascript" src="iwork_js/commons.js" charset="gb2312"></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js" charset="gb2312"></script>
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
	$(function(){
		$(document).bind("contextmenu",function(e){
              return false;   
        });
	});
	var api = art.dialog.open.api, W = api.opener;
	function openStepDef(title,prcDefId,actdefId,actStepDefId){
		var pageUrl = 'sysFlowDef_stepMap!load.action?actDefId='+actdefId+'&prcDefId='+prcDefId+'&actStepDefId='+actStepDefId;
		 art.dialog.open(pageUrl,{
			    	id:'wfStepDesignWinDiv',
					title:'节点模型设计['+title+']',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:'90%',
				    height:410
				 });
	}
	</script>
	<style type="text/css">
		.header td{
			font-weight:bold;
			font-size:12px;
			padding:3px;
			white-space:nowrap;
			padding-left:5px;
			background:#fafafa url('iwork_css/jquerycss/default/images/datagrid_header_bg.gif') repeat-x left bottom;
			border-bottom:1px dotted #ccc;
			border-top:1px dotted #fff;
			border-right:1px dotted #ccc;
		}
		.cell td{
			margin:0;
			padding:3px 4px;
			white-space:nowrap;
			word-wrap:normal;
			overflow:hidden;
			text-align:left;
			border-bottom:1px dotted #eee;
			border-top:1px dotted #fff;
			border-right:1px dotted #ccc;
		}
	</style>
</head>
<body class="easyui-layout">
	<div region="center" style="padding:3px;border:0px;text-align:center;" >
		<img src="iwork_img/on_no.gif" border="0" onerror="javascript:this.src='iwork_img/on_no.gif';" name="processPicImg" usemap="#Map">
		<map name="Map" id="Map">
			<s:property value="hotArea"  escapeHtml='false'/>
		</map> 
	</div> 
	 <div region="south" border="false" style="text-align:center; height:40px; line-height:30px;padding-top:5px;font-size:16px;">
	 	<s:property value="title"/>
     </div>
    
</body>
</html>
