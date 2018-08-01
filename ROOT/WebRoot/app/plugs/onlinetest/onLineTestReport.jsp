<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>  
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" /> 
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
	<script language="javascript" src="iwork_js/commons.js"></script> 
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"  > </script>
	
	<script type="text/javascript">
		function doSubmit(){
			var msg = $("#editForm").serialize();
			var pageUrl = "onLineTestReportDoSearch.action?"+msg;
		//	alert(pageUrl);
			$("#ireport_grid").setGridParam({page:1,url:pageUrl}).trigger("reloadGrid");
		}
		function doResize() {
			var ss = getPageSize(); 
			$("#ireport_grid").jqGrid('setGridWidth', ss.WinW-30).jqGrid('setGridHeight', ss.WinH-178); 
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
		    //$("#editForm").attr("action","ireport_rt_formReportExportExcel.action");
			//$("#editForm").submit();
			var msg = $("#editForm").serialize();
			exportUrl = "ireport_rt_formReportExportExcel.action?" + msg;
			window.open(exportUrl);
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
	</style>
</head>
<body class="easyui-layout"> 
<!-- TOP区 -->
	
	<div region="north" border="false" style="padding:10px;">
		<form method="post" action="/onLineTestReportDoSearch.action"
			name="editForm" id="editForm">
			<div
				style="padding: 0px; border: 1px solid #ccc; background: #FFFFEE;">
				<table width="100%" cellspacing="0" cellpadding="0" border="0">
					<tbody>
						<tr>
							<td style="padding-top: 10px; padding-bottom: 10px;">
								<table width="100%" cellspacing="0" cellpadding="0" border="0">
									<tbody>
										<tr>
											<td class="searchtitle">
												报表名称:
											</td>
											<td class="searchdata"  style="width: 200px;">
												<select id="paperno" name="paperno"
													style="width: 200px">
												<s:property  value="searchArea" escapeHtml="false" />
												</select>
											</td>
											<td class="searchtitle">
												用户账号:
											</td>
											<td class="searchdata">
												<input type="text" value="" id="userInfo" name="userInfo"
													style="width: 100px" class="{required:false}" onkeypress="if(event.keyCode==13||event.which==13){return false;}">
											</td>
										</tr>
									</tbody>
								</table>
							</td>
							<td>
							</td>
							<td valign="bottom" style="padding-bottom: 9px;">
								<a href="javascript:doSubmit();" icon="icon-search"
									class="easyui-linkbutton l-btn" id="btnEp">	查询报表
								</a>
							</td>
						</tr>
						<tr>
						</tr>
					</tbody>
				</table>
			</div>
		</form>
	</div> 
	<div region="center" style="padding:10px;padding-top:3px;border:0px;">
	<div style="padding:0px;background-color:#efefef">
	<!-- <a href="#"><img src="../iwork_img/table.png" border="0">列表输出</a> -->  
	<!-- <a href="###" onclick="exportExcel()"><img src="../iwork_img/file/excel.gif" border="0">导出Excel</a> -->
	 <!-- <a href="#"><img src="../iwork_img/file/pdf.jpg" border="0">导出PDF</a>  <a href="#"><img src="../iwork_img/file/word.gif" border="0">导出Word</a> --></div>
			<table id='ireport_grid'></table>
			<div id='prowed_ireport_grid'></div>
			<script type="text/javascript">
			var lastsel;
			jQuery("#ireport_grid").jqGrid({
		   		url:'onLineTestReportDoSearch.action',
				datatype: "json",
				mtype: "POST",
				autowidth:true,
				rownumbers:true,
				viewrecords: true,
			   	colNames:<s:property  value="colNames" escapeHtml="false" />,
			   	colModel:<s:property  value="colModel" escapeHtml="false" />,
			   	rowNum:<s:property  value="rowNum" escapeHtml="false" />,
			   	rowList:[10,20,30,40],
			   	pager: '#prowed_ireport_grid',
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
			    height: "400",
				ondblClickRow: function(id){				
					var rowData = jQuery("#ireport_grid").jqGrid('getRowData', id);
					var paperno = rowData.PAPERNO;
					var userId = rowData.USERID;
					window.open("/onLineTestUserReport.action?userId=" + userId + "&paperno=" + paperno);
				}				
			});
			jQuery("#ireport_grid").jqGrid('navGrid',"#prowed_ireport_grid",{edit:false,closeOnEscape:true,add:false,del:false});
			doResize();
			</script>
	</div>
	
</body>
</html>
