<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link href="iwork_css/process_center.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.multiselect.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/process_desk_center.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/processDeskManage.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"  > </script>
	<script type="text/javascript" src="iwork_js/engine/process_desk_center.js"></script> 
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript"> 
	$(function(){
				var lastsel;
				jQuery("#task_list_grid").jqGrid({
					url:'processManagement_LoadTodoJson.action?pagetype=4',
					colNames:["序号","任务标题","开始时间","结束时间","状态","操作","模型ID","实例ID","流转ID","任务ID"],
					colModel:[ 
				   		{"index":"1","sortable":false,"align":"center","width":"30","classes":"process_task_other","name":"id"},
				   		{"index":"2","sortable":true,"align":"left","width":"450","classes":"process_task_title","name":"TITLE"},
				   		{"index":"3","sortable":true,"align":"center","width":"120","classes":"process_task_other","name":"STARTTIME"},
				   		{"index":"4","sortable":true,"align":"center","width":"120","classes":"process_task_other","name":"ENDTIME"},
				   		{"index":"5","sortable":true,"align":"center","width":"70","classes":"process_task_other","name":"STATUS"},
				   		{"index":"6","sortable":true,"align":"center","width":"50","classes":"process_task_other","name":"OPERA"},
				   		{"index":"7","sortable":false,"align":"left","hidden":true,"name":"actDefId"}, 
				   		{"index":"8","sortable":false,"align":"left","hidden":true,"name":"instanceId"},
				   		{"index":"9","sortable":false,"align":"left","hidden":true,"name":"excutionId"},
				   		{"index":"10","sortable":false,"align":"left","hidden":true,"name":"taskId"}
				   	], 
					rowNum:15,
					rowList:[15,30],
					loadui:'block',
					multiselect: false,
					sortname: 'DATETIME',
					viewrecords: true,
					resizable:false,
					datatype: "json",
					mtype: "POST",
					autowidth:true,
					shrinkToFit:false,
					sortorder: "desc",
					pager: '#task_list_prowed',
					prmNames:{rows:"page.pageSize",page:"page.curPageNo",sort:"page.orderBy",order:"page.order"},
					jsonReader: {
						root: "dataRows",
						page: "curPage",
						total: "totalPages",
						records: "totalRecords",
						repeatitems: false,
						id: "id",
						userdata: "userdata"
					},
				    onCellSelect: function(id,iCol,cellcontent,e){
				    	if(id && id!==lastsel){
							var ret = jQuery("#task_list_grid").jqGrid('getRowData',id);
							var url = 'loadProcessFormPage.action?actDefId='+ret.actDefId+'&instanceId='+ret.instanceId+'&excutionId='+ret.excutionId+'&taskId='+ret.taskId;
							var target = "iform_"+ret.taskId;
							var win_width = window.screen.width;
							var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
							page.location=url;
							page.document.title = cellcontent;
						}
					}
				});
				jQuery("#task_list_grid").jqGrid('navGrid',"#task_list_prowed",{edit:false,closeOnEscape:false,add:false,del:false,search:false});
				doResize();
				showTaskCount();
		});
		
		var t=document.documentElement.clientWidth;  
		window.onresize = function(){  
			if(t != document.documentElement.clientWidth){ 
				t = document.documentElement.clientWidth; 
				doResize(); 
			} 
		} 
		function doResize() { 
			var ss = getPageSize(); 
			$("#task_list_grid").jqGrid('setGridWidth', ss.WinW-30).jqGrid('setGridHeight', ss.WinH-175); 
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
		
		function claim(taskId,userId){
			document.frmMain.taskId.value = taskId;
			document.frmMain.userId.value = userId;
			  $.post('processManagement_claimTask.action',$("#frmMain").serialize(),function(data)
		            {
				    	if(data=='success'){
				    		 art.dialog.tips("任务领取成功",5);
				    		 $('#task_list_grid').trigger('reloadGrid');
				    	}else{
				    		art.dialog.tips("任务领取失败(错误编号:ERROR-00001)",5)
				    	}
				  }); 
		}
		
		function openProcess(actDefId,instanceId,excutionId,taskId, title) {
			var url = 'loadProcessFormPage.action?actDefId='+actDefId+'&instanceId='+instanceId+'&excutionId='+excutionId+'&taskId='+taskId+'&title='+title;
			var target = getNewTarget();
			var page = window.open('form/loader_frame.html',target,'width='+screen.width-50+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location=url;
			return;
		}
		//切换页签
		 function getTagsPage(type,url){
			this.location= url;
		}
		//执行查询操作 
		function doQuery(){
			var taskName_query = $('#taskName_query').val();
			jQuery("#task_list_grid").jqGrid('setGridParam',{url:'processManagement_LoadTodoJson.action?pagetype=4&taskName_query='+encodeURI(encodeURI(taskName_query))}).trigger("reloadGrid");
		}
		function reback(id){
			var pageUrl = "url:processManagement_showReback.action?instanceId="+id;
			 art.dialog.open(pageUrl,{
						id:'rebackWinDiv', 
						title:'流程撤销',   
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width:520,
					    height:350
					 });
		}
		
		//==========================装载快捷键===============================//
			 	jQuery(document).bind('keydown',function (evt){		
				    	if(evt.ctrlKey&&evt.shiftKey){
							return false;
			   			}
			   	else if(event.keyCode==13){ //回车 /查询操作
				         doQuery(); return false;
			     	}
				}); //快捷键
	</script> 
	<style type="text/css">
		.ui-jqgrid tr.jqgrow td {
		  white-space: normal !important;
		  vertical-align:middle;
		  padding-top:2px;
		  color:#666;
		  height:35px;
		  text-
		 }
		.ui-jqgrid tr.jqgrow td:hover {
		  color:#fa7a20;
		  text-decoration:underline;
		 }
	</style>
</head>
<body class="easyui-layout" >
<!-- TOP标签区 -->
  <div region="north" style="padding:0px;border:0px;;border-bottom:0px;">
	<div class="process_header">
  <div class="process_head_tab_box"> 
 	 <a class="process_head_tab" href="process_desk_index.action">待办流程</a>
     <a class="process_head_tab" href="process_desk_processing.action"> 在办流程</a>  
     <a class="process_head_tab" href="process_desk_history.action"> 办理历史</a> 
     <a class="process_head_tab_active" href="process_desk_createlog.action">我发起的流程</a> 
     <a class="process_head_tab" href="process_desk_notice.action"> 通知/抄送</a>
      </div>
      <div style="padding-top:3px;">
      </div>
      <!-- 
  <div class="process_head_tab_right">
    <form id="search" class="search">
      <input class="txt" type="text">
      </input>
      <button class="search-submit" data-ca="search-btn"></button>
    </form>
  </div>
  <div class="switch-view">
	<a href="#" class="list-selected"  title="切换到缩略图模式"></a>
		<a href="#" class="icon" title="切换到列表模式" style="display:none"></a>
 </div> -->
</div>
	  </div>
	  <div region="center" style="padding:0px 10px 10px 10px;border:0px;padding-top:5px;border:0px;">
    <!-- 查询区 -->
    <div style="padding:0px;border:1px solid #ccc;background:#FFFFEE;padding-top:5px;">
		<table width='90%' border='0' cellpadding='0' cellspacing='0'> 
			<tr> 
				 <td style='padding-top:10px;padding-bottom:10px;'> 
					<table width='100%' border='0' cellpadding='0' cellspacing='0'> 
						<tr>
							<td class= "searchtitle" style="padding-left: 100px;">任务标题:</td>
							<td class= "searchdata">
							<s:textfield name="taskName_query" cssStyle="width:150px;" theme="simple"></s:textfield>
							</td>
							<td align='right' style="padding-right:100px;"><a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:doQuery();" >查询</a></td>
						</tr>
					</table> 
				</td>
			</tr>
		</table>
	</div>
	<!-- 表格内容区 -->
	 <div style="width:100%;">
	  <div style="padding:2px;margin-top:10px;margin-top:10px;border:1px solid #efefef">
  			<table id='task_list_grid'></table>
        	<div id='task_list_prowed'></div>
        	<form name="frmMain" id="frmMain" style="display:none">
			<input type="hidden" name="taskId" id="taskId">
			<input type="hidden" name="userId" id="userId">
			<input type="hidden"  value = "test_parent" name="test_parent_userId" id="test_parent_userId">
			</form>
	  </div>
    </div>
</body>
</html>
