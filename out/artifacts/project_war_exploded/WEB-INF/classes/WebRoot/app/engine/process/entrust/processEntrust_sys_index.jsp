<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head><title>IWORK综合应用管理系统</title>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
<link href="iwork_css/system/sys_operation_log.css" rel="stylesheet" type="text/css"/>
<link href="iwork_css/public.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
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
<style type="text/css">
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
</style>
 <script type="text/javascript">
 //==========================装载快捷键===============================//快捷键
    jQuery(document).bind('keydown',function (evt){		
		    if(evt.ctrlKey&&evt.shiftKey){
			return false;
		   }
		   else if(evt.ctrlKey && event.keyCode==82){ //Ctrl+r /刷新操作
			         this.location.reload(); return false;
		     } 
		  else if(evt.shiftKey && event.keyCode==78){ //Shift+n 新增操作
					 add(); return false;
				}
}); //快捷键
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
					$('#terminateForm').ajaxSubmit(options);
				},function(){}
			);
		}
	}
	errorFunc = function(){
		art.dialog.tips("终止失败,返回值异常",2);
	}
	showResponse = function(){
	art.dialog.tipss("终止成功",2);
		jQuery("#processEntrust_tableGrid").jqGrid('setGridParam',{url:'processEntrust_sys_getProcessTable.action'}).trigger("reloadGrid");
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
		jQuery("#processEntrust_tableGrid").jqGrid('setGridParam',{url:'processEntrust_sys_getProcessTable.action'}).trigger("reloadGrid");
	}
	//执行查询操作
	function doQuery(){
		var entrusetUserid = $('#entrust_Userid').val();
		var entrusetedUserid = $('#entrusted_Userid').val();
		var entrustStatus = $('#entrust_status').val();
		var entrustStart_Start = $('#entrust_Start_Start').val();
		var entrustStart_End = $('#entrust_Start_End').val();
		var entrustEnd_Start = $('#entrust_End_Start').val();
		var entrustEnd_End = $('#entrust_End_End').val();
		if(entrusetUserid != ''){
			var s1 = entrusetUserid.split('[');
			var ss1 = s1[0];
			if(entrusetedUserid != ''){
				var s2 = entrusetedUserid.split('[');
				var ss2 = s2[0];
				jQuery("#processEntrust_tableGrid").jqGrid('setGridParam',{url:'processEntrust_sys_getProcessTable.action?entrusetUserid='+ss1.toUpperCase()+'&entrusetedUserid='+ss2.toUpperCase()+'&entrustStatus='+entrustStatus+'&entrustStart_Start='+entrustStart_Start+'&entrustStart_End='+entrustStart_End+'&entrustEnd_Start='+entrustEnd_Start+'&entrustEnd_End='+entrustEnd_End}).trigger("reloadGrid");
			}else{
				jQuery("#processEntrust_tableGrid").jqGrid('setGridParam',{url:'processEntrust_sys_getProcessTable.action?entrusetUserid='+ss1.toUpperCase()+'&entrusetedUserid='+entrusetedUserid+'&entrustStatus='+entrustStatus+'&entrustStart_Start='+entrustStart_Start+'&entrustStart_End='+entrustStart_End+'&entrustEnd_Start='+entrustEnd_Start+'&entrustEnd_End='+entrustEnd_End}).trigger("reloadGrid");
			}
		}else{
			if(entrusetedUserid != ''){
				var s2 = entrusetedUserid.split('[');
				var ss2 = s2[0];
				jQuery("#processEntrust_tableGrid").jqGrid('setGridParam',{url:'processEntrust_sys_getProcessTable.action?entrusetUserid='+entrusetUserid+'&entrusetedUserid='+ss2.toUpperCase()+'&entrustStatus='+entrustStatus+'&entrustStart_Start='+entrustStart_Start+'&entrustStart_End='+entrustStart_End+'&entrustEnd_Start='+entrustEnd_Start+'&entrustEnd_End='+entrustEnd_End}).trigger("reloadGrid");
			}else{
				jQuery("#processEntrust_tableGrid").jqGrid('setGridParam',{url:'processEntrust_sys_getProcessTable.action?entrusetUserid='+entrusetUserid+'&entrusetedUserid='+entrusetedUserid+'&entrustStatus='+entrustStatus+'&entrustStart_Start='+entrustStart_Start+'&entrustStart_End='+entrustStart_End+'&entrustEnd_Start='+entrustEnd_Start+'&entrustEnd_End='+entrustEnd_End}).trigger("reloadGrid");
			}
		}
	}
	//显示详细信息
	function showDetail(id){
		var pageUrl = "processEntrust_sys_edit.action?id="+id;
		 art.dialog.open(pageUrl,{
						id:'processEntrust_sys_add',
						title:'委托详细信息',
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width:'80%',
					    height:'90%'
					 });
	}
	//添加委托项
	function add(){
		var pageUrl = "processEntrust_sys_add.action";
		 art.dialog.open(pageUrl,{
						id:'processEntrust_sys_add',
						title:'新增委托',
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width:'80%', 
					    height:'90%',
						close:function(){
							location.reload();
						}
					 });
	}
	//加载JQGRID表格
	$(function(){
		jQuery("#processEntrust_tableGrid").jqGrid({
			url:'processEntrust_sys_getProcessTable.action',
			datatype: "json",
			mtype: "POST",
			autowidth:true,
			colNames:["序号","开始时间","结束时间","委托人","被委托人","状态","操作"],
			colModel:[
		   		{index:'1',name:'id',width:25,sortable:false,align:'center'},
		   		{index:'2',name:'entrusetStart',width:55,sortable:false,align:'center'},
		   		{index:'3',name:'entrusetEnd',width:55,sortable:false,align:'center'},
		   		{index:'4',name:'entrusetUserid',width:70,sortable:false,align:'center'},
		   		{index:'5',name:'entrusetedUserid',width:70,sortable:false,align:'center'},
		   		{index:'6',name:'entrusetStatus',width:55,sortable:false,align:'center'},
		   		{index:'7',name:'operation',width:55,sortable:false,align:'center'},
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
	     	height: "240"
		});
		jQuery("#processEntrust_tableGrid").jqGrid('navGrid',"#processEntrust_divGrid",{edit:false,closeOnEscape:true,add:false,del:false,search:false});
	});
	
	//查看委托历史记录
	function searchHistory(){
		window.location.href="processEntrust_sys_LogIndex.action";
	}
</script>
</head>
<body class="easyui-layout">
	<!-- TOP区 -->
	<div region="north" border="false"  style="background-color:#efefef;padding:0px;overflow:no">
		<div style="padding:2px;">
			<a href="javascript:add();" class="easyui-linkbutton" plain="true" iconCls="icon-add">新增委托</a>
			<a href="javascript:searchHistory();" class="easyui-linkbutton" id='addchannel' plain="true" iconCls="icon-search">查询委托历史</a>
			<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		</div>
	</div>
	<!-- 查询区 -->
	<div region="center" style="padding:10px;border:0px;">
	<div style="padding:0px;border:1px solid #ccc;background:#FFFFEE;">
		<table width='90%' border='0' cellpadding='0' cellspacing='0'> 
			<tr> 
				 <td style='padding-top:10px;padding-bottom:10px;'> 
					<table width='100%' border='0' cellpadding='0' cellspacing='0'> 
						<tr> 
							<td class= "searchtitle">委托人:</td>
							<td class= "searchdata">
								<input type=text size='8' name='entrust_Userid' id='entrust_Userid' style="width: 120px;"/><a href="javascript:radio_book('','','','','','','','','entrust_Userid');" class="easyui-linkbutton" plain="true" iconCls="icon-add">地址簿</a>
							</td>
							<td class= "searchtitle">被委托人:</td>
							<td class= "searchdata">
								<input type=text size='8' name='entrusted_Userid' id='entrusted_Userid' style="width: 120px;"/><a href="javascript:radio_book('','','','','','','','','entrusted_Userid');" class="easyui-linkbutton" plain="true" iconCls="icon-add">地址簿</a>
							</td>
							<td class= "searchtitle">委托状态:</td> 
							 <td class= "searchdata">
								<select name='entrust_status' id='entrust_status'>
									<option value=''>请选择</option>
									<option value='0'>草稿</option>
									<option value='1'>有效</option>
									<option value='-1'>终止</option>
								</select>
							 </td> 
						</tr>
						<tr>
							<td class= "searchtitle">开始时间:</td>
							<td class= "searchdata">
							<s:textfield name="entrust_Start_Start" cssStyle="width:120px;" theme="simple" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',maxDate:'#F{$dp.$D(\\'entrust_Start_End\\')||\\'2050-10-01\\'}'})"><s:param name="value"><s:date name="entrust_Start_Start" format="yyyy-MM-dd HH:mm"></s:date></s:param></s:textfield>
							至
							<s:textfield name="entrust_Start_End" cssStyle="width:120px;" theme="simple" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\\'entrust_Start_Start\\')}',maxDate:'2050-10-01'})"><s:param name="value"><s:date name="entrust_Start_End" format="yyyy-MM-dd HH:mm"></s:date></s:param></s:textfield>
							</td>
							<td class= "searchtitle">结束时间:</td>
							<td class= "searchdata">
							<s:textfield name="entrust_End_Start" cssStyle="width:120px;" theme="simple" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',maxDate:'#F{$dp.$D(\\'entrust_End_End\\')||\\'2050-10-01\\'}'})"><s:param name="value"><s:date name="entrust_End_Start" format="yyyy-MM-dd HH:mm"></s:date></s:param></s:textfield>
							至
							<s:textfield name="entrust_End_End" cssStyle="width:120px;" theme="simple" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\\'entrust_End_Start\\')}',maxDate:'2050-10-01'})"><s:param name="value"><s:date name="entrust_End_End" format="yyyy-MM-dd HH:mm"></s:date></s:param></s:textfield>
							</td>
							<td align='center' colspan='2'><a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:doQuery();" >查询</a></td>
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