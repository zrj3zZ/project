<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:property value="title"/></title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script language="javascript" src="iwork_js/commons.js"></script> 
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"  > </script>
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"  charset="utf-8"   ></script>
	<script type="text/javascript" src="iwork_js/engine/ifromworkbox.js"  > </script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">

		$(function(){
			var demid = $("#demId").val();
			if(demid==9 || demid==12){
				$("#copybutton").show();
			}else{
				$("#copybutton").hide();
			}
			if('<s:property value="title"/>'!='试卷管理维护表单'){
				
				
				document.getElementById("mbxz").style.display = "none";
				 document.getElementById("aDc").style.display = "none";
			}
		});
		
		function doResize() {
			var ss = getPageSize(); 
			$("#iform_grid").jqGrid('setGridWidth', ss.WinW-16).jqGrid('setGridHeight', ss.WinH-200);  
		} 
		
		function doSearch(){ 
			<s:property value='searchscript' escapeHtml="false"/>
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
		
		function copy(){		 
		   var instanceIds = [];
	    	var ids = $("#iform_grid").jqGrid("getGridParam", "selarrrow");	    	
			for(var i=0;i<ids.length;i++){
				var ret = jQuery("#iform_grid").jqGrid('getRowData',ids[i]);
				var instanceid = ret.INSTANCEID;
				instanceIds.push(instanceid);
			}
			var formid = $("#formid").val();
			var demid = $("#demId").val();
			if(demid==12){
				var pageUrl ="sysDem_MonthSelect.action?instanceIds="+instanceIds.join(',')+"&demid="+demid+"&formid="+formid;
				 art.dialog.open(pageUrl,{
			    	id:'Category_show', 
			    	title:'选择月份',
					lock:true,
					background: '#999', 
				    opacity: 0.87,	
				    width:600,
				    height:410,
					close:function(){
						window.location.reload();
					}
				 });
			}else if(demid==9){	
			var pageUrl ="url:sysDem_WeekSelect.action?instanceIds="+instanceIds.join(',')+"&demid="+demid+"&formid="+formid;	
			 art.dialog.open(pageUrl,{
			    	id:'Category_show', 
			    	title:'选择周数',
					lock:true,
					background: '#999', 
				    opacity: 0.87,	
				    width:600,
				    height:410,
					close:function(){
						window.location.reload();
					}
				 });
			} 
		}
		function downloadTmp(){
			var pageUrl = "zxksDr.action";
			art.dialog.open(pageUrl,{
				id:'ExcelImpDialog',
				title:"数据导入",
				lock:true,
				background: '#999', // 鑳屾櫙鑹�
			    opacity: 0.87,	// 閫忔槑搴�
			    width:500,
			    height:300,
			    close:function(){
			    	if(location!=null){
			    		location.reload();
			    	}
			    }
			 });
		}
		function uploadXxzcMb(){
			 var seachUrl ="zxksMbupload.action";
			window.location.href = seachUrl;
		}
function remove(){
			
			var ids = [];
			var rows =jQuery("#iform_grid").jqGrid('getGridParam','selarrrow');
			for(var i=0;i<rows.length;i++){
				var ret = jQuery("#iform_grid").jqGrid('getRowData',rows[i]);
				ids.push(ret.INSTANCEID);
			}
			var temp = ids.join(',');
			var fid = $('#formid').val();
			var demId = $('#demId').val();
			if(temp==''){
				art.dialog.tips('请选择要删除的项目');  
				return;
			}
			art.dialog.confirm('确认删除吗?',function(result){  
						 	if(result){
						 		$.post("delJcxxwh.action", { temp : temp,formid:fid ,demId:demId }, function (data) {
						 			jQuery.ajax({
										url: 'iformDataRemove.action',
										data:{
											removeList:temp,
											formid:fid,
											demId:demId
										},
										type: 'POST',
										success: function()
										{
											removeGridItem(rows);   //鐢变簬鎵ц鍒犻櫎鏂规硶涓�鍒犱笉骞插噣锛岃皟鐢ㄥ嚱鏁拌繘琛屽惊鐜�褰掓墽琛屽垹闄ゆ搷浣�
										}
									});
									removeGridItem(rows);
						        });
									
		            }
				});
				}
	
</script>
	<style type="text/css">
		.searchtitle{
			text-align:right;
			padding:5px;
		}
		 .ui-jqgrid tr.jqgrow td {
		  white-space: normal !important;
		  height:28px;
		  font-size:12px;
		  vertical-align:text-middle;
		  padding-top:2px;
		 }
	</style>
</head> 
<body class="easyui-layout"  >
<div region="north" border="false" >
			<div  class="tools_nav">
				<s:property value="toolbar" escapeHtml="false"/>
				<a href="javascript:uploadXxzcMb();" class="easyui-linkbutton l-btn l-btn-plain" plain="true" id="mbxz" ><span class="l-btn-left">
				<span class="l-btn-text icon-excel-exp" style="padding-left: 20px;">模板下载</span></span></a>
				<a href="javascript:downloadTmp();" class="easyui-linkbutton l-btn l-btn-plain" plain="true" id="aDc" ><span class="l-btn-left">
				<span class="l-btn-text icon-excel-exp" style="padding-left: 20px;">导入试卷</span></span></a>
				<!--<span id="copybutton"><a href="javascript:copy();" class="easyui-linkbutton" plain="true" iconCls="icon-copy">澶嶅埗璁板綍</a></span>
			--></div> 
	</div> 
	<div region="center" style="padding-left:0px;padding-right:0px;border:0px;background-position:top">
	<form name='ifrmMain' id='ifrmMain'  method="post" >
	<div style="padding:5px">
		<s:property value="searchArea" escapeHtml="false"/>
	</div>
		<span style="disabled:none">
			<s:hidden name="formid" id="formid"></s:hidden> 
			<s:hidden name="demId" id="demId"></s:hidden>
			<s:hidden name="init_params" id="init_params"></s:hidden>  
			<s:hidden name="searchscript" id="searchscript"></s:hidden>
			<input type = "hidden" name="idlist" id="idlist" value='11'>
		
	</span>
	</form>
	<div style="padding:5px">
			<s:property value='content'  escapeHtml='false'/>
			<table id='iform_grid' ></table>
			<div id='prowed_ifrom_grid'></div> 
			</div>
	</div> 
</body>
</html>