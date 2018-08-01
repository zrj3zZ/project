<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>  
	<link rel="stylesheet" type="text/css" href="iwork_css/public.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/report.jqgrid.css">
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/commons.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"  > </script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script> 
	<script type="text/javascript">
		
		function doSubmit(){
			var msg = $("#editForm").serialize(); 
			var pageUrl = "ireport_rt_doFormSearch.action?"+msg;
			$("#ireport_grid").setGridParam({page:1,url:pageUrl}).trigger("reloadGrid");
		}
		function doResize() {
			var ss = getPageSize(); 
			$("#ireport_grid").jqGrid('setGridWidth', ss.WinW-23).jqGrid('setGridHeight', ss.WinH-230); 
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
			$("#editForm").attr("action","ireport_rt_formReportExportExcel.action");
			$("#editForm").submit();
			
		}
		function newWin(){
			var reportId = $("#reportId").val();
			var pageUrl = this.location.href;;
			var win_width = window.screen.width-50;
			var page = window.open(pageUrl,reportId,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		}
		
		function openForm(reportId,instanceId){ 
				var pageUrl = "ireport_rt_linkform.action?reportId="+reportId+"&instanceId="+instanceId;
			var win_width = window.screen.width-50;
			var page = window.open(pageUrl,instanceId,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		}
</script>  
	<style type="text/css">
		 .searchtitle{
		 	text-align:right;
		 }
		 .ui-jqgrid tr.jqgrow td {
		  white-space: normal !important;
		  height:auto;
		  vertical-align:text-top;
		  padding-top:2px;
		 }
		 /*选项卡内容中的二级tab选项卡通用样式*/
		.top_tab {
			background-color:#FFFFFF;
			width:100%;
			position:fixed !important; 
			top:0px;
			position:absolute; 
			z-index:100; 
			top:expression(offsetParent.scrollTop);
		}
		
		.sub_tab li {
			display:block;
			float:left;
		}
		.sub_tab li a {
			display:block;
			height:29px;
			line-height:29px;
			text-align:center;
			font-size:12px;
			padding:0px 10px 0px 10px;
		}
		.linkFrom{
			color:#0000FF;
			font-size:10px;
		}
	</style>
</head>
<body class="easyui-layout"> 
	<div region="north" style="height:22px;border:0px;">
	<div style="text-align:left;font-size:12px;background-color:#efefef">
		<table width="100%">
			<tr> 
				<td><img src="iwork_img/table.png" border="0"/>报表中心>><s:property value="title" escapeHtml="false" /></td>
				<td style="text-align:right;padding-right:10px"><a href="###" onclick="exportExcel()"><img src="iwork_img/file/excel.gif" border="0"/>导出Excel</a> 
				<a id="newWinBtn" href="###" onclick="newWin()"><img src="iwork_img/blank2.gif"  target="_blank" border="0"/>在新窗口显示</a> </td>
			</tr>
		</table>
	</div>
	</div>
	
		<div region="center" style="padding:3px;padding-top:3px;border:0px;">
	<s:form name="editForm" id="editForm" action="ireport_rt_doFormSearch.action" theme="simple">
				<s:property value="searchArea" escapeHtml="false" />
				<s:hidden name="conditionStr" id="conditionStr" ></s:hidden>
				<s:hidden name="reportId" id="reportId" ></s:hidden>
			</s:form>
			<table id='ireport_grid'></table>
			<div id='prowed_ireport_grid'></div>
			<script type="text/javascript">
			var lastsel;
			jQuery("#ireport_grid").jqGrid({
		   		url:'ireport_rt_doFormSearch.action?reportId=<s:property  value="reportId" escapeHtml="false" />',
				datatype: "json",
				mtype: "POST",
				rownumbers:true,
				viewrecords: true,
				footerrow:true,
			   	colNames:<s:property  value="colNames" escapeHtml="false" />,
			   	colModel:<s:property  value="colModel" escapeHtml="false" />,
			   	rowNum:<s:property  value="rowNum" escapeHtml="false" />,
			   	rowList:[10,20,30,40],
			   	pager: '#prowed_ireport_grid', 
			   	shrinkToFit: false,
			  	prmNames:{rows:"page.pageSize",page:"page.curPageNo",sort:"page.orderBy",order:"page.order"},
				jsonReader: {
					root: "dataRows",
					page: "curPageNo", 
					total: "totalPages", 
					records: "totalRecords",
					repeatitems: false,
					id: "id",
					userdata: "userdata"
				},
				height: "90%",
				gridComplete:function(){
			            var rowNum=parseInt($(this).getGridParam("records"),10);
			            if(rowNum>0){
			                $(".ui-jqgrid-sdiv").show();
			                	<s:property  value="sumStr" escapeHtml="false" />
			            }else{
			                $(".ui-jqgrid-sdiv").hide();
			            }
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
