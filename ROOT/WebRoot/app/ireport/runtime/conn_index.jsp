<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>IWORK综合应用管理系统</title>
		<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="iwork_css/common.css"/>
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
		<link rel="stylesheet" type="text/css"	href="iwork_css/jquerycss/default/easyui.css"/>
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/report.jqgrid.css"/>
		<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css" />
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css"/>
		<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
		<script language="javascript" src="iwork_js/commons.js"></script>
		<script type="text/javascript"	src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
		<script type="text/javascript"	src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
		<script type="text/javascript"	src="iwork_js/jqueryjs/languages/grid.locale-cn.js"></script>
		<script type="text/javascript"	src="iwork_js/jqueryjs/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script> 

	<script type="text/javascript">
		function doSubmit(){
			var msg = $("#editForm").serialize(); 
			var pageUrl = "ireport_rt_doConnSearch.action?" + msg;
			$("#ireport_grid").setGridParam({page:1,url:pageUrl}).trigger("reloadGrid");
		}
		function doResize() {
			var ss = getPageSize(); 
			$("#ireport_grid").jqGrid('setGridWidth', ss.WinW-23).jqGrid('setGridHeight', ss.WinH-180); 
		} 
		function getPageSize() {  
			var winW, winH; 
			if(window.innerHeight) {// all except IE 
			winW = window.innerWidth; 
			winH = window.innerHeight; 
			} else if (document.documentElement && document.documentElement.clientHeight) {// IE 6 Strict Mode 
			winW = document.documentElement.clientWidth; 
			winH = document.documentElement.clientHeight; 
			} else if (document.body) { // other 
				winW = document.body.clientWidth; 
				winH = document.body.clientHeight; 
			}  // for small pages with total size less then the viewport  
			return {WinW:winW, WinH:winH}; 
		}
		function exportExcel() {
			$("#editForm").attr("action","ireport_rt_connReportExportExcel.action");
			$("#editForm").submit();
		}
		function newWin(){
			var reportId = $("#reportId").val();
			var pageUrl = this.location.href;;
			var win_width = window.screen.width-50;
			var page = window.open(pageUrl,reportId,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		
		}
	</script>
	<style type="text/css">
		.searchtitle {
			text-align: right;
		}
		
		.ui-jqgrid tr.jqgrow td {
			white-space: normal !important;
			height: auto;
			vertical-align: text-top;
			padding-top: 2px;
		}
	</style>
	</head>
	<body class="easyui-layout">
		<!-- TOP区 -->
		<div region="center" style="padding: 10px; padding-top: 3px; border: 0px;">
			<div style="text-align:left;height:30px;font-size:12px;background-color:#efefef">
		<table width="100%">
			<tr>
				<td><img src="iwork_img/table.png" border="0"/>报表中心>><s:property value="title" escapeHtml="false" /></td>
				<td style="text-align:right;padding-right:10px"><a href="###" onclick="exportExcel()"><img src="../iwork_img/file/excel.gif" border="0"/>导出Excel</a> <a href="###" onclick="newWin()"><img src="iwork_img/blank2.gif"  target="_blank" border="0"/>在新窗口显示</a> </td>
			</tr>
		</table>
	</div>
			<s:form name="editForm" id="editForm"
				action="ireport_rt_doSqlSearch.action" theme="simple">
				<s:property value="searchArea" escapeHtml="false" />
				<s:hidden name="conditionStr" id="conditionStr"></s:hidden>
				<s:hidden name="reportId" id="reportId"></s:hidden>
			</s:form>
			<table id='ireport_grid'></table>
			<div id='prowed_ireport_grid'></div>
			<script type="text/javascript">
			var lastsel;
			jQuery("#ireport_grid").jqGrid({
				datatype: "json",
				//mtype: "POST",
				rownumbers:true,
				viewrecords: true,
			   	colNames:<s:property  value="colNames" escapeHtml="false" />,
			   	colModel:<s:property  value="colModel" escapeHtml="false" />,
			   	rowNum:<s:property  value="rowNum" escapeHtml="false" />,
			   	rowList:[10,20,30,40],
			   	pager: '#prowed_ireport_grid',
				height: "400",
				prmNames:{rows:"page.pageSize",page:"page.curPageNo",sort:"page.orderBy",order:"page.order"},
				jsonReader: {
					root: "dataRows",
					page: "curPage", 
					total: "totalPages",
					records: "totalRecords",
					repeatitems: false,
					id: "id",
					userdata: "userdata"
				},gridComplete:function(){
					showSysTips();
				}
			});
			jQuery("#ireport_grid").jqGrid('navGrid',"#prowed_ireport_grid",{edit:false,closeOnEscape:true,add:false,del:false});
			window.onresize = function(){
				doResize(); 
			}
			setTimeout("doResize()",1000);
		</script>
		</div>
	</body>
</html>
