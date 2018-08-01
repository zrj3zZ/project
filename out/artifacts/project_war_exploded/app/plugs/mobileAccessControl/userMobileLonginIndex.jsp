<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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

		function doResize() {
			var ss = getPageSize(); 
			$("#access_grid").jqGrid('setGridWidth', 540).jqGrid('setGridHeight', ss.WinH-165); 
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
	<div region="center" style="padding:10px;padding-top:3px;border:0px;">
	<div style="padding:3px;background-color:#efefef">
			<table id='access_grid'></table>
			<div id='prowed_access_grid'></div>
			<script type="text/javascript">
			var lastsel;
			jQuery("#access_grid").jqGrid({
		   		url:"userMobileLonginData.action?userId=<s:property  value='userId' escapeHtml='false' />",
				datatype: "json",
				mtype: "POST",
				autowidth:true,
				rownumbers:true,
				viewrecords: true,
			   	colNames:<s:property  value="colNames" escapeHtml="false" />,
			   	colModel:<s:property  value="colModel" escapeHtml="false" />,
			   	rowNum:<s:property  value="rowNum" escapeHtml="false" />,
			   	rowList:[10,20,30,40],
			   	pager: '#prowed_access_grid',
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
			    height: "400"			
			});
			jQuery("#access_grid").jqGrid('navGrid',"#prowed_access_grid",{edit:false,closeOnEscape:true,add:false,del:false});
			doResize();
			</script>
	</div>
	</div>
	
</body>
</html>
