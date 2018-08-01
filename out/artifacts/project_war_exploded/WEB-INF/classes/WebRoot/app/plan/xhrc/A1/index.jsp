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
		
		function add(){
			var pageUrl = "createFormInstance.action?formid=135&demId=42";
			art.dialog.open(pageUrl,{ 
						id:'Category_show', 
						cover:true,
						title:'预算编报',
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true,
						width:900,
						cache:false,
						lock:false,
						height:500, 
						iconTitle:false, 
						extendDrag:true,
						autoSize:false
					});
		}
		
		function addzj(){
			var pageUrl = "process_desk_group_center.action?groupId=400";
			art.dialog.open(pageUrl,{ 
						id:'Category_show', 
						cover:true,
						title:'月度预算追加',
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true,
						width:900,
						cache:false,
						lock:false,
						height:500, 
						iconTitle:false, 
						extendDrag:true,
						autoSize:false
					});
		}
		
		function doExcute_oc(instanceid){
			var pageUrl = "oc_showindex_action.action?instanceid="+instanceid;
			art.dialog.open(pageUrl,{
						id:'Category_show', 
						cover:true,
						title:'其他合同预算编报',
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true,
						width:930,
						cache:false,
						lock:true,
						height:500, 
						iconTitle:false, 
						extendDrag:true,
						autoSize:false
			});
		}
		function doExcute_xhht(instanceid){
			var pageUrl = "sanbu_plan_xhxm_showIndex.action?instanceid="+instanceid;
			art.dialog.open(pageUrl,{
						id:'Category_show', 
						cover:true,
						title:'项目合同预算编报',
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true,
						width:880,
						cache:false,
						lock:true,
						height:500, 
						iconTitle:false, 
						extendDrag:true,
						autoSize:false
			});
		}
		function doExcute(instanceid){
			var pageUrl = "sanbu_plan_xhrc_showAuditA1Index.action?instanceid="+instanceid;
			art.dialog.open(pageUrl,{
						id:'Category_show', 
						cover:true,
						title:'项目日常预算编报',
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true,
						width:780,
						cache:false,
						lock:true,
						height:500, 
						iconTitle:false, 
						extendDrag:true,
						autoSize:true
			});
		}
		function doExcute_dep(instanceid){
			var pageUrl = "sanbu_plan_bmfy_showAuditA1Index.action?instanceid="+instanceid;
			art.dialog.open(pageUrl,{
						id:'Category_show', 
						cover:true,
						title:'部门费用预算编报',
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true,
						width:780,
						cache:false,
						lock:true,
						height:500, 
						iconTitle:false, 
						extendDrag:true,
						autoSize:false,
						close:function(){
							window.location.reload();
						}
			});
		}
		
		// 年度
		function setnd(obj){
			var nd = $("#nd option:selected").val();
			var pageUrl="sanbu_plan_xhrc_queryYsbb.action?nd="+nd;
			window.location.href = pageUrl;
	       
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
					line-height:30x;
					border-top:1px dotted #fff;
					border-right:1px dotted #ccc;
				}
				
				.cellBtn td{
					border-bottom:1px solid #efefef;
					padding:5px;
				}
		.selectCheck{
			border:0px;
			text-align:right;
		}
		.searchAre{
			padding:10px;
			border:1px solid #efefef;
			margin-bottom:5px;
			background-color:#FFFFDD;
		}
		.searchAre table td{
			padding:5px;
		}
	</style>
</head>
<body class="easyui-layout"> 
<!-- TOP区 -->
	<div region="center" style="padding:10px;border:0px;">
		<div style="padding:5px;padding-left:20px;font-size:22px;font-family:微软雅黑">
			<img src="iwork_img/tongjireport.png" style="width:35px" alt="预算编报" /> 预算编报管理
		</div>
		<div class="searchAre">
			<table WIDTH="100%">
				<tr>
					<TD style="text-align:right">年度</TD>
					<TD><s:select list="listnd" name="nd" id="nd" theme="simple" listKey="nd" listValue="nd" value="nd" headerKey="0" headerValue="请选择"></s:select></TD>
					<TD><a href="javascript:setnd();" class="easyui-linkbutton" plain="false" iconCls="icon-search">查询</a></TD>
				</tr>
			</table>
		</div>
		
		<table WIDTH="100%" style="border:1px solid #efefef">
			<TR  class="header">
				<TD>编报年份</TD>
				<TD>编报月份</TD>
				<TD>编报部门</TD>
				<TD>编报人</TD>
				<TD>操作</TD>
				<!--<TD>型号合同编报状态</TD>
				<TD>型号日常编报状态</TD>
				<TD>其他合同编报状态</TD>
				<TD>部门费用编报状态</TD>
				<TD colspan="3">操作</TD>-->
			</TR>
			<s:iterator value="list"  status="status">
				
			<TR class="cellBtn">
			<td style="font-size:40px;font-family:黑体">
				<s:property value="YSND"/><sapn style="font-size:14px">年</sapn>
			</td>
			<td style="font-size:40px;font-family:黑体">
				<s:property value="YSYF"/><sapn style="font-size:14px">月</sapn>
			</td>
			<td>
				<s:property value="BBBM"/>/
				<s:property value="BBBMMC"/>
			</td>
			<td>
				<s:property value="BBR"/>/
				<s:property value="BBRMC"/>
			</td>
				<TD colspan="3" style="padding-left:20px"><a  class="easyui-linkbutton" plain="false"  href="javascript:doExcute_xhht(<s:property value="INSTANCEID"/>)">项目合同预算</a>
				<a  class="easyui-linkbutton" plain="false" href="javascript:doExcute(<s:property value="INSTANCEID"/>)">项目日常预算</a>
				  <a  class="easyui-linkbutton" plain="false" href="javascript:doExcute_oc(<s:property value="INSTANCEID"/>)">其他合同预算</a>
				<a  class="easyui-linkbutton" plain="false"  href="javascript:doExcute_dep(<s:property value="INSTANCEID"/>)">部门费用预算</a></TD>
			</TR>
			</s:iterator>
		</table>
			<div style="text-align: center; padding: 10px;">
				<s:if test="mainData.BBZT==mainData.QDBB">
					<a href="###" onclick="add()" class="easyui-linkbutton" plain="false" iconCls="icon-add">新增预算</a>
				</s:if>
				<s:if test="mainData.BBZT==mainData.BBJS">
					<a href="###" onclick="addzj()" class="easyui-linkbutton" plain="false" iconCls="icon-add">追加本月预算</a>
				</s:if>
			</div>
		</div>
</body>
</html>
