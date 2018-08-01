<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>  
    <title>参数列表</title>
    
    <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/public.css" /> 
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"   ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
		<script type="text/javascript">
	$(function(){
			$('#dg').datagrid({
				url: 'conn_runctime_showjson.action?pid=<s:property value="pid"/>',
				iconCls: 'icon-save',
				width: 735,
				height: 350,
				fitColumns: true,
				singleSelect: true,
				columns:[[
					{field:'ID',title:'序号',width:30},
					{field:'ICON',title:'',width:30},
					{field:'CREATEDATE',title:'调用时间',width:150,align:'left'},
					{field:'SHOWTIME',title:'调用时长(秒)',width:80,align:'left'},
					{field:'LOGINFO',title:'日志信息',width:200},
					{field:'STATUS',title:'调用状态',width:100},
					{field:'EDIT',title:'操作',width:100,align:'left'}
				]],
				onDblClickRow: function(rowIndex, rowData){
					showinfo(rowData.ID);
				}
			});
		});
	
		function dosearch(){
			var startdate = $("#startdate").val();
			var enddate = $("#enddate").val();
			var isconn = 0;
			if($("#isConn").attr("checked")==true){
				isconn = 1;
			} 
			if(startdate==""||enddate==""){
				alert('请输入查询条件');
				return;
			}
			var pid = $("#pid").val();
			$('#dg').datagrid({url:'conn_runctime_showjson.action',    
				queryParams:{pid:pid, startdate:startdate,enddate:enddate,isconn:isconn}
			});
		}
		function showinfo(id){
			var pageUrl = 'conn_runctime_showitem.action?id='+id;
			 art.dialog.open(pageUrl,{
						id:'connBaseWinDiv',
						title:'输入输出参数', 
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					   width:650,
					    height:350
					 });
		}
	</script>
	<style type="text/css">
		.executeInfo{
			color:#0000FF;
			padding-top:5px;
			padding-left:10px;
		}
		.showNum{
		color:red;
		}
	</style>
  </head>
  
  <body class="easyui-layout">
  <s:form name="editForm" theme="simple">		
  	<div region="north"  style="border:0px;height:90px;padding:12px;">
  		<div style="height:50px;border:1px solid #efefef;padding:5px;vertical-align:middle;background-color:#FCFCF4">
     				<table width="100%">
     				<tr>
     					<td><input type="checkbox" name="isConn" id="isConn" checked="checked"/>调用未成功</td>
     					<td >开始时间</td>
     					<td> <input style="width:145px" name="startdate" value="${startdate}" id="startdate" class="{dateISO:true,required:true}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})">
     					</td>
     					<td>结束时间</td>
     					<td><input style="width:145px" name="enddate" value="${enddate}"  class="{dateISO:true,required:true}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" id="enddate" ></td>
     					<td><a href="javascript:dosearch();" class="easyui-linkbutton" plain="false" iconCls="icon-search">查询</a></td>
     				</tr>
     				<tr>
     					<td colspan="6" class="executeInfo">
     					总调用次数 <span class="showNum"><s:property value="sumNum"/></span> 次  ，成功次数  <span class="showNum"><s:property value="successNum"/></span> 次，失败次数  <span class="showNum"><s:property value="failureNum"/></span>次
     					</td>
     				</tr>
     				</table>
					</div> 
  	</div>
     <div region="center" style="padding:3px;border:0px;">	
		<table id="dg" ></table>
						<script type="text/javascript">
							$(function(){
								var pager = $('#dg').datagrid('getPager');	// get the pager of datagrid
								pager.pagination({
								});			
							})
						</script>
												
							
						<s:hidden name='pid' id="pid"></s:hidden>
					
					</div>	
	</div>
	</s:form>
  </body>
</html>
