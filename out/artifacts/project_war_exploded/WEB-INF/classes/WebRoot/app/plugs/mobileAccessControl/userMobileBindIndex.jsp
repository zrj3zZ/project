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
		var api = frameElement.api, W = api.opener;
		function cancel(){
			api.close();
		} 
		 
		function doResize() {
			var ss = getPageSize(); 
			$("#access_grid").jqGrid('setGridWidth', 492).jqGrid('setGridHeight', 220); 
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
		
		function getSelectRowNo(){
		
			var s = jQuery("#access_grid").jqGrid('getGridParam','selarrrow') + "";
			var ids = s.split(",");
			var result = "";
			var oldDatas = W.document.getElementById('deviceIds').value;
			var resultNum = 0;
			var flag = true;
			if(ids.length > 0){
				for(var i=0; i<ids.length; i++){
					var ret = jQuery("#access_grid").jqGrid('getRowData',ids[i]);
					if(oldDatas.indexOf(ret.BIDNID) == -1){
						if(resultNum == 0){
							result = ret.BIDNID;
						}else{
							result = result + "," + ret.BIDNID;
						}
						resultNum++;
					}
				}
			}
			
			//写入父页面
			if($.trim(oldDatas).length == 0){
				W.document.getElementById('deviceIds').value = result;
			}else{
				if(result.length > 0){
					W.document.getElementById('deviceIds').value = oldDatas + "," + result;
				}
				
			}
			cancel();
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
		   		url:"userMobileBindData.action?userId=<s:property  value='userId' escapeHtml='false' />",
				datatype: "json",
				mtype: "POST",
				autowidth:true,
				rownumbers:true,
				viewrecords: true,
				multiselect: true,
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
			    height: "240"
			});
			jQuery("#access_grid").jqGrid('navGrid',"#prowed_access_grid",{edit:false,closeOnEscape:true,add:false,del:false});
			doResize();
			</script>
	</div>
	<div style="padding:5px;text-align:right; ">
		<a href="javascript:getSelectRowNo();" class="easyui-linkbutton"  iconCls="icon-ok">确定</a>
		<a href="javascript:cancel();" class="easyui-linkbutton" iconCls="icon-cancel">取消</a>
	</div>
	</div>
</body>
</html>
