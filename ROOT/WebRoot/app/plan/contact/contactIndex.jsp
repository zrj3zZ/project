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
		function addNd(){
	   
	    	var pageUrl = "createFormInstance.action?formid=109&demId=29";
	    	art.dialog.open(pageUrl,{
					id:'Category_show',
					cover:true,
					title:'添加年度预算指标',
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					rang:true,
					width:900,
					cache:false,
					lock: true,
					height:480, 
					iconTitle:false,
					extendDrag:true,
					autoSize:false,
					close:function(){
							window.location.reload();
						}
				});
				dg.ShowDialog();					    	
	    }
		function showMx(id,instanceid){
			var pageUrl = "show_ndhtzb_index.action?Ndid="+id+"&instanceid="+instanceid;
			art.dialog.open(pageUrl,{ 
						id:'Category_show', 
						cover:true,
						title:'年度付款指标',
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true,
						width:890,
						cache:false,
						lock:false,
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
				<a href="###" onclick="addNd()" class="easyui-linkbutton" plain="true" iconCls="icon-add">新增预算</a>			 
			</div>
		 </div>
	</div>

	<div region="center" style="padding:10px;border:0px;">
		<table WIDTH="100%">
			<TR  class="header">
				
				<TD>年度</TD>				
				<TD>创建时间</TD>
				<TD>备注</TD>
				<TD>合同付款指标编辑</TD>
								
			</TR>
			<s:iterator value="list"  status="status">
				<TR class="cell">
				
				<TD><s:property value="NDCONTENT"/></TD>			
				<TD><s:property value="CREATTIME"/></TD>				
				<TD><s:property value="NOTES"/></TD>
				<TD><a href="javascript:showMx('<s:property value="ID"/>','<s:property value="INSTANCEID"/>')">操作</a></TD>
				
			</TR>
			</s:iterator>
		</table>
	</div>
</body>
</html>
