<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/process_desk_center.css">  
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.multiselect.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/processDeskManage.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"  > </script>
	<script language="javascript" src="iwork_js/commons.js"></script> 
	<script language="javascript" src="iwork_js/engine/process_desk_center.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript"> 
	$(function(){
				var lastsel;
				jQuery("#task_list_grid").jqGrid({
					url:'processManagement_LoadTodoJson.action?pagetype=0',
					colNames:["序号","操作","来自","任务标题","日期时间","流程历时","模型ID","实例ID","流转ID","任务ID","任务类别"],
					colModel:[ 
				   		{"index":"1","sortable":false,"align":"center","width":"35","name":"id"},
				   		{"index":"6","sortable":false,"align":"center","width":"50","name":"OPERATE"},
				   		{"index":"2","sortable":false,"align":"center","classes":"process_task_other","width":"80","name":"OWNER"},
				   		{"index":"3","sortable":true,"align":"left","width":"400","name":"TITLE"},
				   		{"index":"4","sortable":true,"align":"center","width":"130","name":"DATETIME"},
				   		{"index":"5","sortable":false,"align":"center","width":"80","name":"DURETIME"},
				   		{"index":"7","sortable":false,"align":"left","hidden":true,"name":"actDefId"},
				   		{"index":"8","sortable":false,"align":"left","hidden":true,"name":"instanceId"},
				   		{"index":"9","sortable":false,"align":"left","hidden":true,"name":"excutionId"},
				   		{"index":"10","sortable":false,"align":"left","hidden":true,"name":"taskId"},
				   		{"index":"11","sortable":false,"align":"left","hidden":true,"name":"TYPE"}
				   	],
					rowNum:15,
					rowList:[15,30],
					loadui:'block',
					multiselect: false,
					sortname: 'DATETIME',
					viewrecords: true,
					resizable:true,
					datatype: "json",
					mtype: "POST",
					grouping:true,
				   	groupingView : {
				   		groupField:['TYPE'],
				   		groupColumnShow:[false],
				   		groupOrder: ['desc']
				   	},
					autowidth:false,
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
		//任务休眠
		function closeTask(taskId){  
			art.dialog.confirm('你确定要关闭当前任务吗？', function(){
			  $.post('processManagement_closeTask.action',{taskId:taskId},function(msg)
		            { 
				    	if(msg=='success'){
				    		 art.dialog.tips("当前任务已关闭",5);
				    		 $('#task_list_grid').trigger('reloadGrid');
				    	}else{
				    		art.dialog.tips("当前任务关闭失败(错误编号:ERROR-00001)",5)
				    	} 
				  }); 
			});
		}
		//任务休眠
		function sleepTask(taskId){
			var pageUrl = "processManagement_showSleepPage.action?taskId="+taskId;
			art.dialog.open(pageUrl,{
					id:'sleepWinDiv', 
					title:'设置流程休眠',  
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				   width:520,
				    height:300
				 });
		} 
		//激活休眠任务
		function activeTask(taskId){
			  $.post('processManagement_activeTask.action',{taskId:taskId},function(data)
		            { 
				    	if(data=='success'){
				    		 art.dialog.tips("激活成功",5); 
				    		 $('#task_list_grid').trigger('reloadGrid');
				    	}else{
				    		art.dialog.tips("激活任务失败(错误编号:ERROR-00001)",5)
				    	}
				    	showSysTips();
				  }); 
		}
		
		//激活休眠任务
		function deleteTask(taskId){
			art.dialog.confirm('你确定要删除当前任务吗？', function(){
			  $.post('processManagement_deleteTask.action',{taskId:taskId},function(data)
		            {
				    	if(data=='success'){
				    		 art.dialog.tips("已删除",1);
				    		 $('#task_list_grid').trigger('reloadGrid');
				    	}else{
				    	}
				    	showSysTips();
				  }); 
			});
			
		}
		function openProcess(actDefId,instanceId,excutionId,taskId, title) {
			var url = 'loadProcessFormPage.action?actDefId='+actDefId+'&instanceId='+instanceId+'&excutionId='+excutionId+'&taskId='+taskId+'&title='+title;
			var target = getNewTarget();
			var page = window.open('form/loader_frame.html',target,'width='+screen.width-50+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location=url;
			return;
		}
		function reloadWorkList(){
			$("#task_list_grid").trigger('reloadGrid');
			parent.showTips('workflow');
		}
		//切换页签
		 function getTagsPage(type,url){
		 	this.location= url;
			for(var i=0;i<5;i++){
				if(i==type){
					document.getElementById('tagLeft_'+i).className='tags_left_1';
					document.getElementById('tagCenter_'+i).className='tags_center_1';
					document.getElementById('tagRight_'+i).className='tags_right_1';
					document.getElementById('mouse_p_'+i).className='select_1';
				}else{
					document.getElementById('tagLeft_'+i).className='tags_left_2';
					document.getElementById('tagCenter_'+i).className='tags_center_2';
					document.getElementById('tagRight_'+i).className='tags_right_2';
					document.getElementById('mouse_p_'+i).className='select_2';
				}
			}
		}
		//执行查询操作 
		function doQuery(){
			var taskOwner_query = $('#taskOwner_query').val();
			var taskName_query = $('#taskName_query').val();
			var creatTimeStart_query = $('#creatTimeStart_query').val();
			var creatTimeEnd_query = $('#creatTimeEnd_query').val();
			if(taskOwner_query != ''){
				var s1 = taskOwner_query.split('[');
				var ss1 = s1[0];
				jQuery("#task_list_grid").jqGrid('setGridParam',{url:'processManagement_LoadTodoJson.action?pagetype=0&taskOwner_query='+ss1.toUpperCase()+'&taskName_query='+encodeURI(encodeURI(taskName_query))+'&creatTimeStart_query='+creatTimeStart_query+'&creatTimeEnd_query='+creatTimeEnd_query}).trigger("reloadGrid");
			}else{
				jQuery("#task_list_grid").jqGrid('setGridParam',{url:'processManagement_LoadTodoJson.action?pagetype=0&taskOwner_query='+taskOwner_query+'&taskName_query='+encodeURI(encodeURI(taskName_query))+'&creatTimeStart_query='+creatTimeStart_query+'&creatTimeEnd_query='+creatTimeEnd_query}).trigger("reloadGrid");
			}
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
	  <div region="north" style="padding:0px;border:0px;;border-bottom:1px solid #efefef;">
			<div id="tabsB">
			  <ul>
			    <s:property value="tablist" escapeHtml="false"/>
			  </ul>
			</div>
	  </div>
	  <div region="center" style="padding:0px 10px 10px 10px;border:0px;padding-top:5px;">
	  <!-- 查询区 -->
		<div style="padding:0px;border:1px solid #ccc;background:#FFFFEE;margin-bottom:5px;"> 
			<table width='90%' border='0' cellpadding='0' cellspacing='0'> 
				<tr> 
					 <td style='padding-top:10px;padding-bottom:10px;'> 
						<table width='100%' border='0' cellpadding='0' cellspacing='0'> 
							<tr>
								<td class= "searchtitle">来自:</td>
								<td class= "searchdata">
								<s:textfield name="taskOwner_query" cssStyle="width:150px;" theme="simple"></s:textfield>&nbsp;<a href="javascript:radio_book('','','','','','','','','taskOwner_query');" class="easyui-linkbutton" plain="true" iconCls="icon-add">地址簿</a>
								</td>
								<td class= "searchtitle">任务标题:</td>
								<td class= "searchdata">
								<s:textfield name="taskName_query" cssStyle="width:150px;" theme="simple"></s:textfield>
								</td>
								<td align='right'><a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:doQuery();" >查询</a></td>
							</tr>
						</table> 
					</td>
				</tr>
			</table>
		</div>
		<!-- 表格内容区 -->
				<table id='task_list_grid'></table>
	       		<div id='task_list_prowed'></div>
	       		 <form name="frmMain" id="frmMain" style="display:none">
					<input type="hidden" name="taskId" id="taskId">
					<input type="hidden" name="userId" id="userId">
					<input type="hidden"  value = "test_parent" name="test_parent_userId" id="test_parent_userId">
				</form>
	</div>
</body>
</html>
<script type="text/javascript"> 
	//parent.showTips("workflow");
</script>