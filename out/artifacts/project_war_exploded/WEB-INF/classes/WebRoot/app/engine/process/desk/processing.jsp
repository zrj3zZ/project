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
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/process_desk_center.css">
	<link href="iwork_css/system/sys_operation_log.css" rel="stylesheet" type="text/css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.multiselect.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/processDeskManage.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"  > </script>
	<script language="javascript" src="iwork_js/commons.js"></script> 
	<script language="javascript" src="iwork_js/engine/process_desk_center.js"></script> 
	<script language="javascript" src="iwork_js/process/process_desk.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style type="text/css">
		.like_body{width:expression(document.body.clientWidth); min-width:830px; margin-top:10px; text-align:center; margin-bottom:5px;}
		.max_width{min-width:829px;width:expression((documentElement.clientWidth<830 && document.body.clientWidth<830)?"830px":"100%");}
		.div_center{width:99%;  margin:0 auto;}
		.cn_left{width:100%;border:1px solid #efefef; float:left;}
		.tags_all_2{
			float:left;
		}
		.tags_content_1{
			font-size:12px; 
			font-weight:bold;
			color:#333333;
			margin-left:3px;
			margin-right:3px;
		}
		.tags_left_1{
			height:1px; font-size:1px; overflow:hidden; display:block; margin:0 1px; background-color:#C0C0C0;
		} 
		.tags_center_1{
			background-color:#fff;
			height:24px; 
			line-height:24px; 
			overflow:hidden; 
			text-align:center;	
			border-right:1px solid;
			border-left:1px solid;
			border-color:#C0C0C0;
			display:block;
			margin-bottom:1px;	
		}
		.tags_right_1{
			height:1px; font-size:1px; overflow:hidden; display:block; margin:0 1px; background-color:#fff;
		}
		.tags_left_2{
			height:1px; font-size:1px; overflow:hidden; display:block;
		}
		.tags_center_2{
			height:24px; 
			line-height:24px; 
			overflow:hidden; 
			text-align:center;
		}
		.tags_right_2{
			height:1px; font-size:1px; overflow:hidden; display:block; background-color:#fff;
		}
		.select_1{cursor:auto;font-size:12px; font-weight:bold;	color:#C00102;}
		.select_2{cursor:pointer;font-size:12px; font-weight:bold;color:#505050;}
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
	</style>
	</style>
	<script type="text/javascript"> 
	$(function(){
				var lastsel;
				jQuery("#task_list_grid").jqGrid({
					url:'processManagement_LoadTodoJson.action?pagetype=2',
					datatype: "json",
					mtype: "POST",
					autowidth:true,
					shrinkToFit:true,
					colNames:["序号","来自","任务标题","日期时间","当前节点","当前办理人","操作","模型ID","实例ID","流转ID","任务ID"],
					colModel:[
				   		{"index":"1","sortable":false,"align":"center","hidden":true,"width":"50","name":"id"},
				   		{"index":"2","sortable":true,"align":"center","classes":"process_task_other","width":"70","name":"OWNER"},
				   		{"index":"3","sortable":true,"align":"left","classes":"process_task_title","width":"450","name":"TITLE"},
				   		{"index":"4","sortable":true,"align":"center","classes":"process_task_other","width":"120","name":"STARTTIME"},
				   		{"index":"5","sortable":true,"align":"center","classes":"process_task_other","width":"90","name":"CURR_STEP"},
				   		{"index":"5","sortable":true,"align":"center","classes":"process_task_other","width":"90","name":"CURR_USER"},
				   		{"index":"6","sortable":false,"align":"center","classes":"process_task_other","width":"60","name":"OPERATE"},
				   		{"index":"7","sortable":false,"align":"left","hidden":true,"name":"actDefId"},
				   		{"index":"8","sortable":false,"align":"left","hidden":true,"name":"instanceId"},
				   		{"index":"9","sortable":false,"align":"left","hidden":true,"name":"excutionId"},
				   		{"index":"10","sortable":false,"align":"left","hidden":true,"name":"taskId"}
				   	],
					rowNum:15,
					rowList:[15,30],
					loadui:'block',
					rownumbers:true,
					multiselect: false,
					pager: '#task_list_prowed',
					sortname: 'DATETIME',
					viewrecords: true,
					shrinkToFit:false,
					resizable:true,
					sortorder: "desc",
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
					    if(iCol==3){
					    	if(id && id!==lastsel){
								var ret = jQuery("#task_list_grid").jqGrid('getRowData',id);
								var url = 'loadProcessFormPage.action?actDefId='+ret.actDefId+'&instanceId='+ret.instanceId+'&excutionId='+ret.excutionId+'&taskId='+ret.taskId;
								var target = "iform_"+ret.taskId;
								var win_width = window.screen.width-50;
								var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
								page.location=url;
								page.document.title = cellcontent;
							}
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
		
		//切换页签
		 function getTagsPage(type,url){
			this.location= url;
		}
		//执行查询操作 
		function doQuery(){
			var taskOwner_query = $('#taskOwner_query').val();
			var taskName_query = $('#taskName_query').val();
			if(taskOwner_query != ''){
				var s1 = taskOwner_query.split('[');
				var ss1 = s1[0];
				jQuery("#task_list_grid").jqGrid('setGridParam',{url:'processManagement_LoadTodoJson.action?pagetype=2&taskOwner_query='+ss1.toUpperCase()+'&taskName_query='+encodeURI(encodeURI(taskName_query))}).trigger("reloadGrid");
			}else{
				jQuery("#task_list_grid").jqGrid('setGridParam',{url:'processManagement_LoadTodoJson.action?pagetype=2&taskOwner_query='+taskOwner_query+'&taskName_query='+encodeURI(encodeURI(taskName_query))}).trigger("reloadGrid");
			}
		}
		
		//打开流程任务监控窗口
		function openMonitorPage(actDefId,actStepDefId,prcDefId,taskId,instanceId){
				var pageUrl = "processInstanceMornitor.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&taskId="+taskId+"&instanceId="+instanceId;
				art.dialog.open(pageUrl,{
						id:'mornitorDialog',
						cover:true,
						title:"流程跟踪",
						width:800,
						height:600,
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true, 
						cache:true,
						lock: true,
						iconTitle:false,
						extendDrag:true,
						autoSize:true,
						resize:true
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
</head>
<body class="easyui-layout" >
<!-- TOP标签区 -->
  <div region="north" style="padding:0px;border:0px;">
	<div class="process_header">
  		<div class="process_head_tab_box"> 
		   <a class="process_head_tab" title="显示所有待审批流程未读的抄送任务或通知" href="process_desk_index.action"><s:text name="process.desk.todo"/><span id="taskNum"></span></a>
		     <a class=process_head_tab_active  title="显示当前用户已办理，但未最终归档的流程任务"  href="process_desk_finish.action"><s:text name="process.desk.history"/></a>  
		     <a class="process_head_tab"  title="显示所有接收的抄送及流程通知任务"  href="process_desk_notice.action"> <s:text name="process.desk.notice"/><span id="NoticeNum"></span></a>
		      <a class="process_head_tab"  title="显示当前用户已办理，所有办理过的任务历史及审批意见"  href="process_desk_history.action"><s:text name="process.desk.dolog"/></a>
		 </div>
      <div style="padding-top:3px;float:right;padding-right:20px;"> 
									<s:checkbox id="isCreate" name="isCreate" label="我发起的流程" value="false" fieldValue="isCreate" theme="simple"/><label for="isCreate">我发起的流程</label>&nbsp;&nbsp;|&nbsp;&nbsp;
			<s:radio list="#{'0':'全部','1':'未归档','2':'已归档'}" id="processStatus" name="processStatus"  value="0"  theme="simple"/>
      </div> 
</div>
	  </div>
	  <div region="center" style="padding:5px 5px 5px 5px;border:0px;">
  <!-- 查询区 --> 
  <div style="padding:0px;border:1px solid #ccc;background:#FFFFEE;">
		<table width='90%' border='0' cellpadding='0' cellspacing='0'> 
			<tr> 
				 <td style='padding-top:10px;padding-bottom:10px;'> 
					<table width='100%' border='0' cellpadding='0' cellspacing='0'> 
						<tr>
							<td class= "searchtitle">来自:</td>
							<td class= "searchdata">
							<s:textfield name="from" id="processFrom"  cssStyle="width:100px;" theme="simple"></s:textfield><a href="javascript:radio_book('','','','','','','','','processFrom');"  class="easyui-linkbutton" plain="true" iconCls="icon-add">地址簿</a>
							</td>
							<td class= "searchtitle">任务标题:</td>
							<td class= "searchdata">
							<s:textfield name="title" cssClass="" cssStyle="width:150px;" theme="simple"></s:textfield>
							</td>
							<td align='right'>
									<a id="btnEp" class="easyui-linkbutton" icon="icon-search" href="javascript:doQuery();" >查询</a>
							</td>
						</tr>
					</table> 
				</td>
			</tr>
		</table>
	</div>
	<!-- 表格内容区 -->
    <div style="width:100%;">
	  <div style="padding:2px;margin-top:5px;">
  			<table id='task_list_grid'></table>
        	<div id='task_list_prowed'></div>
        	<form name="frmMain" id="frmMain" style="display:none">
			<input type="hidden" name="taskId" id="taskId">
			<input type="hidden" name="userId" id="userId">
			<input type="hidden"  value = "test_parent" name="test_parent_userId" id="test_parent_userId">
			</form>
	  </div>
    </div>
   </div>
</body>
</html>
