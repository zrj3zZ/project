<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>IWORK综合应用管理系统</title> 
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link href="iwork_css/common.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=default"></script>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>     		
	<script type="text/javascript"> 
	var selectedNodes;
		$(function(){ 
			
		})
		
		
		function doExcute_oc(instanceid){
			var pageUrl = "oc_showaduitIndex_action.action?instanceid="+instanceid;
			art.dialog.open(pageUrl,{
						id:'Category_show', 
						cover:true,
						title:'其它类合同部门领导预算审核',
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true,
						width:780,
						cache:false,
						lock:true,
						height:500, 
						iconTitle:false, 
						extendDrag:true,
						autoSize:false
			});
		}
		function doExcute_xhht(instanceid){
			var pageUrl = "show_auditMx_action.action?instanceid="+instanceid;
			art.dialog.open(pageUrl,{
						id:'Category_show', 
						cover:true,
						title:'型号合同预算编报',
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true,
						width:900,
						cache:false,
						lock:true,
						height:500, 
						iconTitle:false, 
						extendDrag:true,
						autoSize:false
			});
		}
		function doExcute(instanceid){
			var pageUrl = "sanbu_plan_xhrc_showAuditA2Index.action?instanceid="+instanceid;
			art.dialog.open(pageUrl,{
						id:'Category_show', 
						cover:true,
						title:'型号日常预算审核',
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true,
						width:780,
						cache:false,
						lock:true,
						height:500, 
						iconTitle:false, 
						extendDrag:true,
						autoSize:false
			});
		}
	</script>
	<style>
		* {
			margin:0px;
			padding:0px;
			font-size:12px;
		}
		img {
			border: 0 none;
		}
		.header td{
			font-weight:bold;
			font-size:12px;
			padding:3px;
			white-space:nowrap;
			padding-left:5px;
			background:#fafafa url('../../iwork_css/jquerycss/default/images/datagrid_header_bg.gif') repeat-x left bottom;
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
		.selectCheck{
			border:0px;
			text-align:right;
		}
	</style>
</head>
<body class="easyui-layout"> 
<!-- TOP区 -->
	<div region="north" border="false" style="padding:0px;overflow:no;scrolling:no;border-bottom:1px;">
		<div class="tools_nav">
			<div style="text-align:left;padding-right:10px;">
				审核预算		 
			</div>
		 </div>
	</div>

	<div region="center" style="padding:10px;border:0px;">
		<table WIDTH="100%">
			<TR  class="header">
				<TD>编报人</TD>
				<TD>编报月份</TD>
				<TD>编报类型</TD>
				<TD>编报部门</TD>
				<TD>编报状态</TD>
				<TD>型号合同编报状态</TD>
				<TD>型号日常编报状态</TD>
				<TD>其他合同编报状态</TD>
				<TD colspan="3">操作</TD>
			</TR>
			<s:iterator value="list"  status="status">
				<TR class="cell">
				<TD><s:property value="BBR"/></TD>
				<TD><s:property value="YSYF"/></TD>
				<TD><s:property value="BBLX"/></TD>
				<TD><s:property value="BBBM"/></TD>
				<TD><s:property value="BBZT"/></TD>
				<TD><s:property value="XHHTBBZT"/></TD>
				<TD><s:property value="XHRCBBZT"/></TD>				
				<TD><s:property value="QTHTBBZT"/></TD>
				<TD><a href="javascript:doExcute_xhht(<s:property value="INSTANCEID"/>)">型号合同审核</a></TD>
				<TD><a href="javascript:doExcute(<s:property value="INSTANCEID"/>)">型号日常审核</a></TD>
				<TD><a href="javascript:doExcute_oc(<s:property value="INSTANCEID"/>)">其它合同</a></TD>
			</TR>
			</s:iterator>
		</table>
	</div>
</body>
</html>
