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
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/process_desk_center.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/processDeskManage.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"  > </script>
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/process_box.css">
	<script type="text/javascript" src="iwork_js/sys_procdef.js"></script>
	<script language="javascript" src="iwork_js/commons.js"></script> 
	<script language="javascript" src="iwork_js/process/process_box.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"></script>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
	$(function(){		
				var lastsel;
				jQuery("#task_list_grid").jqGrid({
				   		url:'processManagement_LoadHistoryData.action?actDefkey=<s:property value="actDefkey" escapeHtml="false"/>&pagetype=3',
						datatype: "json",
						mtype: "POST",
					autowidth:true,
					shrinkToFit:false,
				   	colNames:["序号","来自","任务标题","开始时间","结束时间","耗时","模型ID","实例ID","流转ID","任务ID"],
				   	colModel:[
				   		{"index":"1","sortable":false,"align":"center","width":"30","classes":"process_task_other","name":"id"},
				   		{"index":"2","sortable":true,"align":"center","width":"70","classes":"process_task_other","name":"OWNER"},
				   		{"index":"3","sortable":true,"align":"left","width":"450","classes":"process_task_title","name":"TITLE"},
				   		{"index":"4","sortable":true,"align":"center","width":"120","classes":"process_task_other","name":"STARTTIME"},
				   		{"index":"5","sortable":true,"align":"center","width":"120","classes":"process_task_other","name":"ENDTIME"},
				   		{"index":"6","sortable":true,"align":"left","width":"80","classes":"process_task_other","name":"DURTIME"},
				   		{"index":"7","sortable":false,"align":"left","hidden":true,"name":"actDefId"},
				   		{"index":"8","sortable":false,"align":"left","hidden":true,"name":"instanceId"},
				   		{"index":"9","sortable":false,"align":"left","hidden":true,"name":"excutionId"},
				   		{"index":"10","sortable":false,"align":"left","hidden":true,"name":"taskId"}
				   	],
				   	rowNum:15,
				   	rowList:[15,30],
				   	loadui:'block',
				   	multiselect:false,
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
					    		art.dialog.tips("打开历史任务", 3);
								var ret = jQuery("#task_list_grid").jqGrid('getRowData',id);
								var url = 'loadProcessFormPage.action?actDefId='+ret.actDefId+'&instanceId='+ret.instanceId+'&excutionId='+ret.excutionId+'&taskId='+ret.taskId;
								var target = getNewTarget();
								var win_width = window.screen.width-50;
								var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
								page.location=url;
							}
					    }
					},
				   	sortname: 'DATETIME',
				    viewrecords: true,
				    resizable:true
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
		function openProcess(actDefId,instanceId,excutionId,taskId, title) {
			var url = 'loadProcessFormPage.action?actDefId='+actDefId+'&instanceId='+instanceId+'&excutionId='+excutionId+'&taskId='+taskId+'&title='+title;
			var target = getNewTarget();
			var page = window.open('form/loader_frame.html',target,'width='+screen.width-50+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location=url;
			return;
		}
		
		//切换页签
		 function getTagsPage(type,url){
		 	this.location= url;  //跳转页面
		}
		
		//执行查询操作 
		function doQuery(){
			var taskOwner_query = $('#taskOwner_query').val();
			var taskName_query = $('#taskName_query').val();
			if(taskOwner_query != ''){
				var s1 = taskOwner_query.split('[');
				var ss1 = s1[0];
				jQuery("#task_list_grid").jqGrid('setGridParam',{url:'processManagement_LoadHistoryData.action?pagetype=3&taskOwner_query='+ss1.toUpperCase()+'&taskName_query='+encodeURI(encodeURI(taskName_query))}).trigger("reloadGrid");
			}else{
				jQuery("#task_list_grid").jqGrid('setGridParam',{url:'processManagement_LoadHistoryData.action?pagetype=3&taskOwner_query='+taskOwner_query+'&taskName_query='+encodeURI(encodeURI(taskName_query))}).trigger("reloadGrid");
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
<div region="north" border="false" split="false" class="processBoxTitle" id="layoutNorth">
	<table width="100%">
		<tr>
			<td style="vertical-align:middle;"><img src="iwork_img/menulogo_huiyishenqin.gif" alt="" /><s:property value="processTitle" escapeHtml="false"/></td>
			<td style="vertical-align:top;">
				   <div style="padding-top:3px;font-size:12px;float:right;padding-right:20px;"> 
									<s:checkbox id="isCreate" name="isCreate" label="我发起的流程" value="false" fieldValue="1" theme="simple"/><label for="isCreate">我发起的流程</label>&nbsp;&nbsp;|&nbsp;&nbsp;
									<s:radio list="#{'0':'全部','1':'未归档','2':'已归档'}" id="processStatus" name="processStatus"  value="0"  theme="simple"/>
						      </div> 
			</td>
		</tr>
	</table>
							
</div>
<div region="west" border="false"  id="layoutLeft" class="leftDiv">
	<div class="leftTools">
		<a href="javascript:createProcess('<s:property value="actDefId" escapeHtml="false"/>');" class="easyui-linkbutton" plain="false" iconCls="icon-add">&nbsp;&nbsp;新　建&nbsp;&nbsp;</a>
	</div>
    <hr style="width:180px; height:2px;border:none;border-top:2px solid #99ccff;"/>
    <div>
    	<s:property value="tabHtml" escapeHtml="false"/>
    </div>
   <div class="memoTip">
   	  流程说明:
     </div>
    <div class="memo">
    	<s:property value="processMemo" escapeHtml="false"/>
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
							<s:textfield name="taskOwner_query" cssStyle="width:150px;" theme="simple"></s:textfield><a href="javascript:radio_book('','','','','','','','','taskOwner_query');" class="easyui-linkbutton" plain="true" iconCls="icon-add">地址簿</a>
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
      <div style="width:100%;">
	  <div style="padding:2px;margin-top:10px;border:1px solid #efefef">
  			<table id='task_list_grid'></table>
        	<div id='task_list_prowed'></div>
	  </div>
	  </div>
	</div>
</body>
</html>
