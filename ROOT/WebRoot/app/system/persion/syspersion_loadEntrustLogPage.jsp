<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=8"/> 
<title>IWORK综合应用管理系统</title>
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
	//执行查询操作
	function doQuery(){
		var entrusetTime_Start = $('#entrusetTime_Start').val();
		var entrusetTime_End = $('#entrusetTime_End').val();
		var dealTime_Start = $('#dealTime_Start').val();
		var dealTime_End = $('#dealTime_End').val();
		jQuery("#processEntrust_tableGrid").jqGrid('setGridParam',{url:'processEntrust_sys_getPersonLogTable.action?entrusetTime_Start='+entrusetTime_Start+'&entrusetTime_End='+entrusetTime_End+'&dealTime_Start='+dealTime_Start+'&dealTime_End='+dealTime_End+'&dealStatus=1'}).trigger("reloadGrid");
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
	
	//加载JQGRID表格
	$(function(){
		jQuery("#processEntrust_tableGrid").jqGrid({
			url:'processEntrust_sys_getPersonLogTable.action',
			datatype: "json",
			mtype: "POST",
			autowidth:true,
			colNames:["序号","委托任务","委托人","被委托人","委托时间","办理时间","处理状态",'操作'],
			colModel:[
		   		{index:'1',name:'id',width:50,sortable:false,align:'center'},
		   		{index:'2',name:'taskTitle',width:350,sortable:false,align:'left'},
		   		{index:'3',name:'entrusetUserid',width:80,sortable:false,align:'center'},
		   		{index:'4',name:'entrusetedUserid',width:80,sortable:false,align:'center'},
		   		{index:'5',name:'entrusetTime',width:100,sortable:false,align:'center'},
		   		{index:'6',name:'dealTime',width:100,sortable:false,align:'center'},
		   		{index:'7',name:'dealStatus',width:80,sortable:false,align:'center'},
		   		{index:'8',name:'instanceid',width:80,sortable:false,align:'center',formatter:customFmatter,unformat:customunFmatter}
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
	     	height: "290", 
     		/* ondblClickRow: function(id,obj){ 
        		  var rowData = $("#processEntrust_tableGrid").jqGrid("getRowData", id);   
        		  var taskTitle=rowData.taskTitle;
        		  var entrusetUserid=rowData.entrusetUserid;
        		  var entrusetTime=rowData.entrusetTime
        		 
					
        		  $.ajax({
            	    type : "post",
            	     url :"getentrust.action?taskTitle="+taskTitle+"&entrusetUserid="+entrusetUserid+"&entrusetTime="+entrusetTime,
            	 dataType:"json",//
           		  success:function(data){
           		 
           		  
        		  var pageUrl = encodeURI("loadProcessFormPage.action?actDefId="+data[0].LCBH+"&actStepDefId="+data[0].STEPID+"&instanceId="+data[0].LCBS+"&excutionId="+data[0].LCBS+"&taskId="+data[0].TASKID);	
				  art.dialog.open(pageUrl,{
						id:'projectItem',
						cover:true, 
						title:'',
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true,
						width:1000,
						cache:false,
						lock: true,
						height:600, 
						iconTitle:false,
						extendDrag:true,
						autoSize:false,
						 close:function(){
                           window.location.reload();
                           }
					});
           		  }
           		 
          		})  
        		 
      		}*/
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
	};
	function     customFmatter(cellvalue, options, rowObj){//\''+cellvalue+'\',\''+rowObj.entrusetUserid+'\',\''+rowObj.entrusetTime+'\'  \''+cellvalue+'\',\''+rowObj.entrusetUserid+'\',\''+rowObj.entrusetTime+'\'
		   		 var ss="查看详情";
                  return '<a  href="#" onclick="javascript:getentrust(\''+rowObj.id+'\')">'+ss+'</a>';   
        };
	function     customunFmatter(cellvalue, options, cell){
		   			
                  return $('a', cell).attr('value');   
        };
        
        
        
    function   getentrust(id,Obj){
    				
    			  var rowData = $("#processEntrust_tableGrid").jqGrid("getRowData", id);   
    			
        		  var taskTitle=rowData.taskTitle;
        		  
        		  var entrusetUserid=rowData.entrusetUserid;
        		  var entrusetTime=rowData.entrusetTime;
        		
    			  $.ajax({
            	    type : "post",
            	     url :encodeURI("getentrust.action?taskTitle="+taskTitle+"&entrusetUserid="+entrusetUserid+"&entrusetTime="+entrusetTime),
            	 dataType:"json",//
           		  success:function(data){
           		 
           		  
        		  var pageUrl = encodeURI("loadProcessFormPage.action?actDefId="+data[0].LCBH+"&actStepDefId="+data[0].STEPID+"&instanceId="+data[0].LCBS+"&excutionId="+data[0].LCBS+"&taskId="+data[0].TASKID);	
				  art.dialog.open(pageUrl,{
						id:'projectItem',
						cover:true, 
						title:'',
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true,
						width:1000,
						cache:false,
						lock: true,
						height:600, 
						iconTitle:false,
						extendDrag:true,
						autoSize:false,
						 close:function(){
                           window.location.reload();
                           }
					});
           		  }
           		 
          		})  
        		  
    
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
	<!-- TOP区 -->
	<div region="north" border="false"  style="padding:0px;overflow:no">
		<div  class="tools_nav">
			<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		</div>
		<div id="tabsB">
			  <ul>
			    <li ><a href="javascript:jump(1);"><span >我发出的委托(<s:property value="nonTerminate_entrustNum" escapeHtml="false"/>)</span></a></li>
				<li><a href="javascript:jump(2);"><span >我收到的委托(<s:property value="nonTerminate_entrustedNum" escapeHtml="false"/>)</span></a></li>
				<li><a href="javascript:jump(3);"><span style="color:#f77215;background-color:#fff;font-weight:bold;">委托办理历史</span></a></li>
			  </ul>
			</div>
	</div>
	<!-- 查询区 -->
	<div region="center" style="padding:2px;border:0px;">
	<div style="padding:0px;border:1px solid #ccc;background:#FFFFEE;">
		<table width='90%' border='0' cellpadding='0' cellspacing='0'> 
			<tr> 
				 <td style='padding-top:10px;padding-bottom:10px;'> 
					<table width='100%' border='0' cellpadding='0' cellspacing='0'> 
						<tr>
							<td class= "searchtitle">委托时间:</td>
							<td class= "searchdata">
							<s:textfield name="entrusetTime_Start" cssStyle="width:110px;" theme="simple" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\\'entrusetTime_End\\')||\\'2050-10-01\\'}'})"><s:param name="value"><s:date name="entrusetTime_Start" format="yyyy-MM-dd"></s:date></s:param></s:textfield>
							至
							<s:textfield name="entrusetTime_End" cssStyle="width:110px;" theme="simple" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\\'entrusetTime_Start\\')}',maxDate:'2050-10-01'})"><s:param name="value"><s:date name="entrusetTime_End" format="yyyy-MM-dd"></s:date></s:param></s:textfield>
							</td>
							<td class= "searchtitle">办理时间:</td>
							<td class= "searchdata">
							<s:textfield name="dealTime_Start" cssStyle="width:110px;" theme="simple" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\\'dealTime_End\\')||\\'2050-10-01\\'}'})"><s:param name="value"><s:date name="dealTime_Start" format="yyyy-MM-dd"></s:date></s:param></s:textfield>
							至
							<s:textfield name="dealTime_End" cssStyle="width:110px;" theme="simple" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\\'dealTime_Start\\')}',maxDate:'2050-10-01'})"><s:param name="value"><s:date name="dealTime_End" format="yyyy-MM-dd"></s:date></s:param></s:textfield>
							</td>
							<td align='right'><a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:doQuery();" >查询</a></td>
						</tr>
						
					</table> 
				</td>
			</tr>
		</table>
	</div>
	<div style="padding:2px;margin-top:10px;margin-top:10px;border:1px solid #efefef">
		<table id='processEntrust_tableGrid'></table>
		<div id='processEntrust_divGrid'></div> 
		<s:property value='infolist'  escapeHtml='false'/>
	</div>
	</div>
	<!-- 表单隐藏区 -->
	<s:form name="terminateForm" id="terminateForm" action="processEntrust_sys_terminate" cssStyle="display:none">
		<s:textfield name="id"></s:textfield>
	</s:form>
</body>