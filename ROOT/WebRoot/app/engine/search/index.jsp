<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="../iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="../iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="../iwork_css/public.css" />
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="../iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="../iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../iwork_js/commons.js" ></script>
	<link rel="stylesheet" type="text/css" href="../iwork_css/engine/iformsearch_index.css">
	<script type="text/javascript" src="../iwork_js/engine/iformsearch_index.js" ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
	$(function(){
				var viewdate =new Date();
				var time = viewdate.getTime();
	             $('#iformSearch_grid').datagrid({
	             	url:"iformSearch_load.action?formid=<s:property value='formid'  escapeHtml='false'/>&datetime=time",
					loadMsg: "正在加载数据...",
					fitColumns: true,
					singleSelect: true, 
					columns:[[
						{field:'ID',title:'序号',width:30},
						{field:'JOINTYPE',title:'关系',width:20},
						{field:'CONDITION',title:'查询条件',width:150},
						{field:'DISPLAY_TYPE',title:'外观样式',width:80,align:'left'},
						{field:'DISPLAY_ENUM',title:'参考值',width:150},
						{field:'ISNULL',title:'是否允许为空',width:50},
						{field:'DO',title:'操作',width:50}
					]], 
					idField:'ID',
					onBeforeLoad:function(){
						$(this).datagrid('rejectChanges');
					},
					onDblClickRow:function(rowIndex){
						//var row = $('#iformSearch_grid').datagrid('getSelected');
						var r = $(this).datagrid('getSelected');
						openMapModify(r.ID);
					}
				});
		})
		//修改属性信息
		function openMapModify(id){  
			var pageUrl =  'iformSearchMap_load.action?id='+id; 
			  art.dialog.open(pageUrl,{
			    	id:'dg_addAuditOpinion',
			    	title:'表单域设置',   
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:600,
				    height:450
				 });
		}
	</script>
</head>
<body class="easyui-layout">
<!-- TOP区 -->
	<div region="north" border="false" style="padding:5px;margin:5px;overflow:no;border:1px solid #efefef;">
	<s:form  id="searchForm" name="searchForm" action="iformSearch_add.action"  theme="simple">
	<s:select name="model.joinType" list="#{'AND':'并且','OR':'或者'}"></s:select>
		<s:property value="condition"  escapeHtml="false"/>
		<s:select name="model.compareType" list="#{'等于':'等于','大于':'大于','小于':'小于','大于等于':'大于等于','小于等于':'小于等于','like':'包含于'}"></s:select>
		<input type="submit" value="添  加"  border="0"> 
        <input type=button value="删除选择" border="0" onclick="remove()">
        <s:hidden name="formid" value="%{formid}" theme="simple"></s:hidden>
        <s:hidden name="ids" theme="simple"></s:hidden>
   </s:form>
   </div>
	<div region="center" style="padding:5px;border:0px;height:300px">
			<table id="iformSearch_grid"></table>
	</div>
</body>
</html>
