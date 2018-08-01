<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head><title>IWORK综合应用管理系统</title>
<style type="text/css">
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
</style>
</head>
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
<script type="text/javascript">
	//执行查询操作
	function doQuery(){
		var entrusetUserid = $('#entrust_Userid').val();
		var entrusetedUserid = $('#entrusted_Userid').val();
		var entrusetTime_Start = $('#entrusetTime_Start').val();
		var entrusetTime_End = $('#entrusetTime_End').val();
		var dealTime_Start = $('#dealTime_Start').val();
		var dealTime_End = $('#dealTime_End').val();
		if(entrusetUserid != ''){
			var s1 = entrusetUserid.split('[');
			var ss1 = s1[0];
			if(entrusetedUserid != ''){
				var s2 = entrusetedUserid.split('[');
				var ss2 = s2[0];
				jQuery("#processEntrust_tableGrid").jqGrid('setGridParam',{url:'processEntrust_sys_getLogTable.action?entrusetUserid='+ss1.toUpperCase()+'&entrusetedUserid='+ss2.toUpperCase()+'&entrusetTime_Start='+entrusetTime_Start+'&entrusetTime_End='+entrusetTime_End+'&dealTime_Start='+dealTime_Start+'&dealTime_End='+dealTime_End+'&dealStatus=1'}).trigger("reloadGrid");
			}else{
				jQuery("#processEntrust_tableGrid").jqGrid('setGridParam',{url:'processEntrust_sys_getLogTable.action?entrusetUserid='+ss1.toUpperCase()+'&entrusetedUserid='+entrusetedUserid+'&entrusetTime_Start='+entrusetTime_Start+'&entrusetTime_End='+entrusetTime_End+'&dealTime_Start='+dealTime_Start+'&dealTime_End='+dealTime_End+'&dealStatus=1'}).trigger("reloadGrid");
			}
		}else{
			if(entrusetedUserid != ''){
				var s2 = entrusetedUserid.split('[');
				var ss2 = s2[0];
				jQuery("#processEntrust_tableGrid").jqGrid('setGridParam',{url:'processEntrust_sys_getLogTable.action?entrusetUserid='+entrusetUserid+'&entrusetedUserid='+ss2.toUpperCase()+'&entrusetTime_Start='+entrusetTime_Start+'&entrusetTime_End='+entrusetTime_End+'&dealTime_Start='+dealTime_Start+'&dealTime_End='+dealTime_End+'&dealStatus=1'}).trigger("reloadGrid");
			}else{
				jQuery("#processEntrust_tableGrid").jqGrid('setGridParam',{url:'processEntrust_sys_getLogTable.action?entrusetUserid='+entrusetUserid+'&entrusetedUserid='+entrusetedUserid+'&entrusetTime_Start='+entrusetTime_Start+'&entrusetTime_End='+entrusetTime_End+'&dealTime_Start='+dealTime_Start+'&dealTime_End='+dealTime_End+'&dealStatus=1'}).trigger("reloadGrid");
			}
		}
	}
	//加载JQGRID表格
	$(function(){
		jQuery("#processEntrust_tableGrid").jqGrid({
			url:'processEntrust_sys_getLogTable.action',
			datatype: "json",
			mtype: "POST",
			autowidth:true,
			colNames:["序号","委托任务","委托人","被委托人","委托时间","办理时间","处理状态"],
			colModel:[
		   		{index:'1',name:'id',width:25,sortable:false,align:'center'},
		   		{index:'2',name:'taskTitle',width:55,sortable:false,align:'center'},
		   		{index:'3',name:'entrusetUserid',width:55,sortable:false,align:'center'},
		   		{index:'4',name:'entrusetedUserid',width:55,sortable:false,align:'center'},
		   		{index:'5',name:'entrusetTime',width:70,sortable:false,align:'center'},
		   		{index:'6',name:'dealTime',width:70,sortable:false,align:'center'},
		   		{index:'7',name:'dealStatus',width:55,sortable:false,align:'center'},
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
	function setEntrust(){
		window.location.href="processEntrust_sys_index.action";
	}
</script>
<body class="easyui-layout">
	<!-- TOP区 -->
	<div region="north" border="false"  style="background-color:#efefef;padding:0px;overflow:no">
		<div style="padding:2px;">
			<a href="javascript:setEntrust();" class="easyui-linkbutton" id='addchannel' plain="true" iconCls="icon-edit">委托设置维护</a>
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
						</tr>
						<tr>
							<td class= "searchtitle">委托时间:</td>
							<td class= "searchdata">
							<s:textfield name="entrusetTime_Start" cssStyle="width:120px;" theme="simple" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',maxDate:'#F{$dp.$D(\\'entrusetTime_End\\')||\\'2050-10-01\\'}'})"><s:param name="value"><s:date name="entrusetTime_Start" format="yyyy-MM-dd HH:mm"></s:date></s:param></s:textfield>
							至
							<s:textfield name="entrusetTime_End" cssStyle="width:120px;" theme="simple" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\\'entrusetTime_Start\\')}',maxDate:'2050-10-01'})"><s:param name="value"><s:date name="entrusetTime_End" format="yyyy-MM-dd HH:mm"></s:date></s:param></s:textfield>
							</td>
							<td class= "searchtitle">办理时间:</td>
							<td class= "searchdata">
							<s:textfield name="dealTime_Start" cssStyle="width:120px;" theme="simple" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',maxDate:'#F{$dp.$D(\\'dealTime_End\\')||\\'2050-10-01\\'}'})"><s:param name="value"><s:date name="dealTime_Start" format="yyyy-MM-dd HH:mm"></s:date></s:param></s:textfield>
							至
							<s:textfield name="dealTime_End" cssStyle="width:120px;" theme="simple" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\\'dealTime_Start\\')}',maxDate:'2050-10-01'})"><s:param name="value"><s:date name="dealTime_End" format="yyyy-MM-dd HH:mm"></s:date></s:param></s:textfield>
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