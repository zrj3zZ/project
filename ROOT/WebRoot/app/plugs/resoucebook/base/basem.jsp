<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>资源预定基础信息</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
		<link rel="stylesheet" type="text/css"	href="iwork_css/plugs/loadbasem.css">
	    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	   <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	   <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	   <script type="text/javascript" src="iwork_js/plugs/resourcebook_base.js"></script>	
	   <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
    	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	   <script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=default"></script>
		<script type="text/javascript">
			$(function(){	
	
		$('#metadata_grid').datagrid({
	             	url:"resbook_base_gridjson.action?spaceId=<s:property value='spaceId'  escapeHtml='false'/>",
					loadMsg: "正在加载数据...",
					fitColumns: true, 
					rownumbers:true,
					singleSelect:false,
					columns:[[ 
						{field:'ck',checkbox:true},
						{field:'id',title:'ID',width:45,align:'center'},
						{field:'spacename',title:'空间名称',width:45,align:'center'},
						{field:'resouceid',title:'资源ID',width:35,align:'center'},
						{field:'resoucename',title:'资源名称',width:45,align:'center'},
						{field:'picture',title:'图片',width:30,align:'center'},
						{field:'para1',title:'参数一',width:65,align:'center'},
						{field:'para2',title:'参数二',width:65,align:'center'}, 
						{field:'para3',title:'参数三',width:65,align:'center'},
						{field:'para4',title:'参数四',width:65,align:'center'},
						{field:'para5',title:'参数五',width:65,align:'center'},
						{field:'status',title:'状态',width:25,align:'center'},
						{field:'opera',title:'操作',width:25,align:'center'}
					]]
				});	                        
	});	
		</script>
	</head>
	
	<body  class="easyui-layout"> 
<!-- TOP区 -->
<div region="north" border="false" style="padding:0px;overflow:no">
<div class="tools_nav"> 
		<a href="javascript:addBase();" class="easyui-linkbutton"	plain="true" iconCls="icon-add">添加基础信息</a>
				<a href="javascript:remove();" class="easyui-linkbutton"	plain="true" iconCls="icon-remove">删除</a>	
		<a href="javascript:refrenshGrid();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
	 
	</div>
</div>
<div region="center" style="padding:3px;border:0px">
		<table id="metadata_grid" style="margin:2px;"></table>
</div>
<s:form name="editForm" id="editForm" theme="simple">
<s:hidden name="spaceId" id="spaceId"></s:hidden>
<s:hidden name="spacename" id="spacename"></s:hidden>
</s:form>
</body>
</html>