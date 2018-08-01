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
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.multiselect.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/processDeskManage.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"  > </script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>	
	<script type="text/javascript"> 
	$(function(){
				var lastsel;
				jQuery("#task_list_grid").jqGrid({
					url:'processManagement_LoadTodoJson.action',
					colNames:["序号","来自","任务标题","日期时间","操作","模型ID","实例ID","流转ID","任务ID","任务类别"],
					colModel:[
				   	{"index":"1","align":"center","width":"50","name":"id"},
				   		{"index":"2","sortable":true,"align":"center","classes":"process_task_other","width":"70","name":"OWNER"},
				   		{"index":"3","sortable":true,"align":"left","classes":"process_task_title","width":"450","name":"TITLE"},
				   		{"index":"4","sortable":true,"align":"center","classes":"process_task_other","width":"120","name":"DATETIME"},
				   		{"index":"5","sortable":true,"align":"center","classes":"process_task_other","width":"90","name":"OPERATE"},
				   		{"index":"6","sortable":false,"align":"left","hidden":true,"name":"actDefId"},
				   		{"index":"7","sortable":false,"align":"left","hidden":true,"name":"instanceId"},
				   		{"index":"8","sortable":false,"align":"left","hidden":true,"name":"excutionId"},
				   		{"index":"9","sortable":false,"align":"left","hidden":true,"name":"taskId"},
				   		{"index":"10","sortable":false,"align":"left","hidden":false,"name":"TYPE"}
				   	],
					rowNum:20,
					rowList:[10,20,30,40],
					loadui:'block',
					rownumbers:true,
					multiselect: false,
					sortname: 'DATETIME',
					viewrecords: true,
					resizable:false,
					datatype: "json",
					mtype: "POST",
					grouping:true,
				   	groupingView : {
				   		groupField:['TYPE'],
				   		groupColumnShow:[false],
				   		groupOrder: ['desc']
				   	},
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
					    if(iCol==2){
					    	if(id && id!==lastsel){
								var ret = jQuery("#task_list_grid").jqGrid('getRowData',id);
								var url = 'loadProcessFormPage.action?actDefId='+ret.actDefId+'&instanceId='+ret.instanceId+'&excutionId='+ret.excutionId+'&taskId='+ret.taskId;
								var target = "iform_"+ret.taskId;
								var page = window.open('form/loader_frame.html',target,'width='+screen.width-50+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
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
		
		function doResize() { 
			var ss = getPageSize(); 
			$("#task_list_grid").jqGrid('setGridWidth', ss.WinW-50).jqGrid('setGridHeight', ss.WinH-110); 
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
		
		
		function openProcess(actDefId,instanceId,excutionId,taskId, title) {
			var url = 'loadProcessFormPage.action?actDefId='+actDefId+'&instanceId='+instanceId+'&excutionId='+excutionId+'&taskId='+taskId+'&title='+title;
			var target = getNewTarget();
			var page = window.open('form/loader_frame.html',target,'width='+screen.width-50+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location=url;
			return;
		}
		//切换页签
		 function getTagsPage(type,url){
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
			this.location= url;
		}
	</script> 
</head>
<body >
<div class="like_body">
<div class="max_width">
<div class="div_center">
<div class="cn_left" id="cn_left">
  <div style="width:100%;float:left;">
  <div id="tabsB">
			  <ul>
			    <s:property value="tablist" escapeHtml="false"/>
			  </ul>
			</div>
  </div>
    <div style="width:100%; float:left;">
	  <div style="background-color:#FFFFFF;text-align:left;" id="showBox1">
	   			<table id='task_list_grid'></table>
          		<div id='task_list_prowed'></div>
          		 <form name="frmMain" id="frmMain">
					<input type="hidden" name="taskId" id="taskId">
					<input type="hidden" name="userId" id="userId">
					<input type="hidden"  value = "test_parent" name="test_parent_userId" id="test_parent_userId">
				</form>
	  </div>
    </div>
   </div>
</div>
</div>
</div>
</body>
</html>
