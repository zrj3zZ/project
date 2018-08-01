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
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/engine/process_launcher_center.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>	
	<script type="text/javascript"> 
	var selectedNodes;
	var api = art.dialog.open.api, W = api.opener;
		$(function(){
			var instanceIds = $("#instanceIds").val();
			if(instanceIds!=''){
				$("#tr_month").hide();
			}
		});
		
		// 复制
		function copy(){
		  alert("1111");
			var comeWeek = $("#comeWeek option:selected").text()=="请选择"?"":$("#comeWeek option:selected").text();
			var toWeek = $("#toWeek option:selected").text()=="请选择"?"":$("#toWeek option:selected").text();
			var comeYear = $("#comeYear option:selected").text()=="请选择"?"":$("#comeYear option:selected").text();
			var toYear = $("#toYear option:selected").text()=="请选择"?"":$("#toYear option:selected").text();
			var instanceIds = $("#instanceIds").val();
			var formid = $("#formid").val();
			var demid = $("#demid").val();
			
			if(instanceIds==''){
				if(comeYear==''){
					alert("请选择来源年度!");
					return;
				}
				if(comeWeek==''){
					art.dialog.tips("请选择来源周数!");
					return;
				}
			}
			if(toYear==''){
				art.dialog.tips("请选择目标年度!");
				return;
			}
			if(toWeek==''){
				art.dialog.tips("请选择目标周数!");
				return;
			}			
			var pageUrl="sysDem_DataCopy.action";		
			$.post(pageUrl,{comeWeek:comeWeek,toWeek:toWeek,instanceIds:instanceIds,formid:formid,demid:demid,comeYear:comeYear,toYear:toYear},function(data){
				if(data=='success'){
					api.close();
				}else{
					art.dialog.tips("复制异常!");
				}
			});
		}
	</script>
	<style>
		.cell:hover{
			background-color:#F0F0F0;
		}
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
		.cellTop td{
			margin:0;
			padding:3px 4px;
			white-space:nowrap;
			word-wrap:normal;
			overflow:hidden;
			text-align:left;
			border-bottom:1px dotted #eee;
			border-top:1px dotted #fff;
			border-right:1px dotted #ccc;
			line-hieght:30px;
		}
		.searchAre{
			padding:10px;
			border:1px solid #efefef;
			margin-bottom:5px;
			background-color:#FFFFDD;
		}
		.searchAre table td{
			padding:5px;
			text-align:right;
		}
	</style>
</head>
<body class="easyui-layout">
<!-- TOP区 -->
	<div region="center" style="padding:10px;border:0px;">
		<div class="searchAre">
			<table WIDTH="100%" >
				<tr id="tr_month">
					<td>来源年度</td>
					<TD><s:select list="listYear" name="comeYear" id="comeYear" theme="simple" cssStyle="width:100px"  listKey="year" listValue="year" value="year" headerKey="0" headerValue="请选择"></s:select></TD>
					<td>来源周数</td>
					<TD><s:select list="listWeek" name="comeWeek" id="comeWeek" theme="simple" listKey="week" listValue="week" value="week" headerKey="0" headerValue="请选择"></s:select></TD>
				</tr>
				<tr>
					<td>目标年度</td>
					<TD><s:select list="listYear" name="toYear" id="toYear" theme="simple" cssStyle="width:100px"  listKey="year" listValue="year" value="year" headerKey="0" headerValue="请选择"></s:select></TD>
					<td>目标周数</td>
					<TD><s:select list="listWeek" name="toWeek" id="toWeek" theme="simple" listKey="week" listValue="week" value="week+1" headerKey="0" headerValue="请选择"></s:select></TD>
				</tr>
				<tr>
					<TD  colspan="4" align=right style="padding-right:5px">
						<a href="javascript:copy();" class="easyui-linkbutton" plain="false" iconCls="icon-search">确定</a>
					</TD>
				</tr>
			</table>
		</div>
		<s:hidden name="instanceid" id="instanceid"></s:hidden>
		<s:hidden name="instanceIds" id="instanceIds"></s:hidden>
		<s:hidden name="formid" id="formid"></s:hidden>
		<s:hidden name="demid" id="demid"></s:hidden>
	</div>
</body>
</html>
