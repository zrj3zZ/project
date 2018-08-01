<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head><title>IWORK综合应用管理系统</title>

<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
<link href="iwork_css/system/sys_operation_log.css" rel="stylesheet" type="text/css"/>
<link href="iwork_css/public.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/processDeskManage.css">
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<script language="javascript" src="iwork_js/commons.js"></script> 
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
	//执行终止委托操作
	function terminate(id,status){
		if(status==-1){
			art.dialog.tips("该委托已经处于终止状态",2);
		}else{
			var b = art.dialog.confirm("您确定终止这项委托吗?",
				function(){//确定修改,提交隐藏表单
					$('#terminateForm_id').val(id);
					var options = {
						error:errorFunc,
						success:showResponse 
					};
					$('#terminateForm').attr('action','processEntrust_sys_terminate.action');
					$('#terminateForm').ajaxSubmit(options);
				},function(){}
			);
		}
	}
	errorFunc = function(){
		art.dialog.tips("终止失败,返回值异常",2);
	}
	showResponse = function(){
		art.dialog.tips("终止成功",2);
		jQuery("#processEntrust_tableGrid").jqGrid('setGridParam',{url:'processEntrust_sys_getNonTerminateTable.action?entrusetedUserid=<s:property value="userid" escapeHtml="false"/>'}).trigger("reloadGrid");
	}
	//执行接收委托操作
	function receiveEntrust(id){
		$('#terminateForm_id').val(id);
		var options = {
			error:errorFun,
			success:showFun 
		};
		$('#terminateForm').attr('action','processEntrust_sys_receive.action');
		$('#terminateForm').ajaxSubmit(options);
	}
	errorFun = function(){
		art.dialog.tips("接受失败,返回值异常",2);
	}
	showFun = function(){
		art.dialog.tips("接受成功",2);
		jQuery("#processEntrust_tableGrid").jqGrid('setGridParam',{url:'processEntrust_sys_getNonTerminateTable.action?entrusetedUserid=<s:property value="userid" escapeHtml="false"/>&entrustEnd_Start=<s:property value="curTime" escapeHtml="false"/>'}).trigger("reloadGrid");
	}
	function doResize() {
			var ss = getPageSize(); 
			$("#processEntrust_tableGrid").jqGrid('setGridWidth', ss.WinW-16).jqGrid('setGridHeight', ss.WinH-200);  
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
	//显示详细信息
	function showDetail(id){
		var pageUrl =  "processEntrust_sys_edit.action?id="+id;
		art.dialog.open(pageUrl,{
					id:'processEntrust_sys_add',
					title:'委托详细信息',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:500,
				    height:280
				 });
	}
	//加载JQGRID表格
	$(function(){
		jQuery("#processEntrust_tableGrid").jqGrid({
			url:'processEntrust_sys_getNonTerminateTable.action?entrusetedUserid=<s:property value="userid" escapeHtml="false"/>&entrustEnd_Start=<s:property value="curTime" escapeHtml="false"/>',
			datatype: "json",
			mtype: "POST",
			autowidth:true,
			colNames:["序号","开始时间","结束时间","委托人","被委托人","状态","操作"],
			colModel:[
		   		{index:'1',name:'id',width:50,sortable:false,align:'center'},
		   		{index:'2',name:'entrusetStart',width:100,sortable:false,align:'center'},
		   		{index:'3',name:'entrusetEnd',width:100,sortable:false,align:'center'},
		   		{index:'4',name:'entrusetUserid',width:80,sortable:false,align:'center'},
		   		{index:'5',name:'entrusetedUserid',width:80,sortable:false,align:'center'},
		   		{index:'6',name:'entrusetStatus',width:80,sortable:false,align:'center'},
		   		{index:'7',name:'operation',width:100,sortable:false,align:'center'},
		   	],
		   	rowNum:10,
		   	rowList:[10,20,30],
		   	pager: "#processEntrust_divGrid",
		   	prmNames:{rows:"page.pageSize",page:"page.curPageNo",sort:"page.orderBy",order:"page.order"},
	        jsonReader: {
		     	root: "dataRows",
		     	page: "curPage",
		     	total: "totalPages",
		     	records: "totalRecords",
		     	repeatitems: false,
		     	userdata: "userdata"
	    	},
	    	viewrecords: true,
	    	resizable:true,
	    	shrinkToFit: false,
	     	height: "300"
		});
		jQuery("#processEntrust_tableGrid").jqGrid('navGrid',"#processEntrust_divGrid",{edit:false,closeOnEscape:true,add:false,del:false,search:false});
		doResize();
	});
	//页面跳转
	function jump(id){
		if(id == 1){
			window.location.href='syspersion_loadEntrustPage.action';
		}else if(id == 2){
			window.location.href='syspersion_loadEntrustedPage.action';
		}else if(id == 3){
			window.location.href='syspersion_loadEntrustLogPage.action';
		}
	}
	//加载全部委托项
	function getAllEntrust(){
		if($('#changeEntrust').html()=='查看全部委托'){
			$('#changeEntrust').html('查看当前委托');
			jQuery("#processEntrust_tableGrid").jqGrid('setGridParam',{url:'processEntrust_sys_getProcessTable.action?entrusetedUserid=<s:property value="userid" escapeHtml="false"/>'}).trigger("reloadGrid");
		}else if($('#changeEntrust').html()=='查看当前委托'){
			$('#changeEntrust').html('查看全部委托');
			jQuery("#processEntrust_tableGrid").jqGrid('setGridParam',{url:'processEntrust_sys_getNonTerminateTable.action?entrusetedUserid=<s:property value="userid" escapeHtml="false"/>&entrustEnd_Start=<s:property value="curTime" escapeHtml="false"/>'}).trigger("reloadGrid");
		}
	}
</script>
<style type="text/css">
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
.selected{
	display: inline-block;
	height: 16px;
	line-height: 16px;
	padding: 8px;
	font-size: 14px;
	cursor: pointer;
	font-family:黑体;
	border: 1px solid brown;
}
.noneSelect{
	display: inline-block;
	height: 16px;
	line-height: 16px;
	padding: 8px;
	font-size: 14px;
	cursor: pointer;
	font-family:黑体;
	border: 1px solid #eee;
}
 .ui-jqgrid tr.jqgrow td {
		  white-space: normal !important;
		  height:35px;
		  font-size:12px;
		  vertical-align:text-middle;
		  padding-top:2px;
		 }
</style>
</head>
<body class="easyui-layout">
	<div region="north" border="false"  style="padding:0px;overflow:no">
		<div  class="tools_nav">
			<a href="javascript:getAllEntrust();" class="easyui-linkbutton" id='addchannel' plain="true" iconCls="icon-search"><span id="changeEntrust">查看全部委托</span></a>
			<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		</div>
		<div id="tabsB">
			  <ul>
			    <li ><a href="javascript:jump(1);"><span >我发出的委托(<s:property value="nonTerminate_entrustNum" escapeHtml="false"/>)</span></a></li>
				<li><a href="javascript:jump(2);"><span style="color:#f77215;background-color:#fff;font-weight:bold;">我收到的委托(<s:property value="nonTerminate_entrustedNum" escapeHtml="false"/>)</span></a></li>
				<li><a href="javascript:jump(3);"><span>委托办理历史</span></a></li>
			  </ul>
			</div>
	</div>
	<!-- 表格区 -->
	<div region="center" style="padding:2px;border:0px;">
		<table id='processEntrust_tableGrid'></table>
		<div id='processEntrust_divGrid'></div> 
	</div>
	<!-- 表单隐藏区 -->
	<s:form name="terminateForm" id="terminateForm" action="processEntrust_sys_terminate" cssStyle="display:none">
		<s:textfield name="id"></s:textfield>
	</s:form>
</body>