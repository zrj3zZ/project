<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/design_showlist.css"> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
	<script type="text/javascript" src ="iwork_js/process/design_showlist.js"></script> 
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script>     
	<script type="text/javascript">
	$(function(){
		 $('#report_grid').datagrid({
	             	url:"ireport_designer_table.action?groupid=<s:property value='groupid'  escapeHtml='false'/>",
					loadMsg: "正在加载数据...",
					fitColumns: true,
					singleSelect:true,
					height:460,
					columns:[[
						{field:'ID',title:'序号',width:30},
						{field:'REPORTNAME',title:'报表名称',width:100},
						{field:'REPORTTYPESTR',title:'类型',width:80,align:'left'},
						{field:'STATUS',title:'状态',width:50,align:'left'},
						{field:'MASTER',title:'管理员',width:50},
						{field:'OPA',title:'操作',width:100}
					]],
					idField:'ID',
					onDblClickRow:function(rowIndex){
						var row = $('#report_grid').datagrid('getSelected');
						parent.openReport(row.ITEMID, row.REPORTTYPE, row.CHARTTYPE);
					},
					onClickRow:function(rowIndex){
						var row = $('#report_grid').datagrid('getSelected');
						parent.document.editForm.ireportId.value = row.ID;
					}
				});
				
				$('#editForm').form({  
				    onSubmit:function(){  
				        return $(this).form('validate');  
				    },  
				    success:function(data){  
				        alert(data);  
				    }  
				});
	})
	function showReport(reportId,type){  
		var pageUrl ="ireport_rt_index.action?reportId="+reportId+"&reportType="+type;
		var name = "模拟演示"; //网页名称，可为空;
		var iWidth = 1024; //弹出窗口的宽度;
		var iHeight = 768; //弹出窗口的高度;    
		var iTop = (window.screen.availHeight-30-iHeight)/2; //获得窗口的垂直位置;
		var iLeft = (window.screen.availWidth-10-iWidth)/2; //获得窗口的水平位置; 
		window.open(pageUrl,name,'height='+iHeight+',,innerHeight='+iHeight+',width='+iWidth+',innerWidth='+iWidth+',top='+iTop+',left='+iLeft+',toolbar=no,menubar=no,scrollbars=auto,resizeable=no,location=no,status=no');
	}
	
	</script>
</head>
<body> 
	<table id="report_grid" style="margin:2px;border:0px"></table>
</body>
</html>
