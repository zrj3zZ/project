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
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
	<link rel="stylesheet" type="text/css" href="../iwork_css/engine/demsearch_index.css">
	<script type="text/javascript" src="iwork_js/engine/demsearch_index.js" ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript"> 
	$(function(){
				var viewdate =new Date();
				var time = viewdate.getTime();
	             $('#iformSearch_grid').datagrid({
	             	url:"sysDem_search_load.action?id=<s:property value='id'  escapeHtml='false'/>&formid=<s:property value='formid'  escapeHtml='false'/>&datetime=time",
					loadMsg: "正在加载数据...", 
					fitColumns: true,
					singleSelect: true,
					height:450,
					columns:[[
						{field:'ID',title:'序号',width:30,hidden:true},
						{field:'SRC_TYPE',title:'类型',width:60},
						{field:'JOINTYPE',title:'关系',width:30},
						{field:'CONDITION',title:'查询条件',width:250},
						{field:'DISPLAY_TYPE',title:'外观样式',width:80,align:'left'},
						{field:'DISPLAY_ENUM',title:'参考值',width:50}, 
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
			var pageUrl =  'sysDem_searchMap_load.action?id='+id; 
			 art.dialog.open(pageUrl,{
			    	id:'formMapIndexWinDiv',
			    	title:'表单域设置',  
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:600,
				    height:450
				 });
		}
		function moveUp(){
	    var demId=$('#demId').val();
	    var formid=$('#formid').val();
	    var rows = $('#iformSearch_grid').datagrid('getSelections');
	    if(rows.length==0){
	         art.dialog.tips("请选择要移动的行记录",1);
	         return;
	    }
	    if(rows.length>1){
	         art.dialog.tips("选择行过多",1);
	         return;
	    }
	    var url='sysDem_search_moveUp.action';
	    $.post(url,{searchMapid:rows[0].ID,demId:demId,formid:formid},function(data){
	         if(data=='ok'){
	             $('#iformSearch_grid').datagrid('reload');
	         }
	    });
	}
	function moveDown(){
	    var demId=$('#demId').val();
	    var formid=$('#formid').val();
	     var rows = $('#iformSearch_grid').datagrid('getSelections');
	    if(rows.length==0){
	         art.dialog.tips("请选择要移动的行记录",1);
	         return;
	    }
	    if(rows.length>1){
	         art.dialog.tips("选择行过多",1);
	         return;
	    }
	    var url='sysDem_search_moveDown.action';
	    $.post(url,{searchMapid:rows[0].ID,demId:demId,formid:formid},function(data){
	         if(data=='ok'){
	             $('#iformSearch_grid').datagrid('reload');
	         }
	    });
	}
	function moveTop(){
		var demId=$('#demId').val();
		var formid=$('#formid').val();
	    var rows = $('#iformSearch_grid').datagrid('getSelections');
	    if(rows.length==0){
	         art.dialog.tips("请选择要移动的行记录",1);
	         return;
	    }
	    if(rows.length>1){
	         art.dialog.tips("选择行过多",1);
	         return;
	    }
	    var url='sysDem_search_moveTop.action';
	    $.post(url,{searchMapid:rows[0].ID,demId:demId,formid:formid},function(data){
	         if(data=='ok'){
	            $('#iformSearch_grid').datagrid('reload');
	         }
	    });
	}
	function moveBottom(){
		var demId=$('#demId').val();
		var formid=$('#formid').val();
	    var rows = $('#iformSearch_grid').datagrid('getSelections');
	    if(rows.length==0){
	         art.dialog.tips("请选择要移动的行记录",1);
	         return;
	    }
	    if(rows.length>1){
	         art.dialog.tips("选择行过多",1);
	         return; 
	    }
	    var url='sysDem_search_moveBottom.action';
	    $.post(url,{searchMapid:rows[0].ID,demId:demId,formid:formid},function(data){
	         if(data=='ok'){
	             $('#iformSearch_grid').datagrid('reload'); 
	         }
	    });
	}
		
	</script>
</head>
<body class="easyui-layout">
<!-- TOP区 -->
	<div region="north" border="false" style="padding:5px;margin:5px;overflow:no;border:1px solid #efefef;">
	<s:form  id="searchForm" name="searchForm" action="sysDem_search_add.action"  theme="simple">
	<table width="100%">
		<tr>
			<td>
			<s:select name="model.joinType" list="#{'AND':'并且','OR':'或者'}"></s:select>
		<s:property value="condition"  escapeHtml="false"/>
		<s:select name="model.compareType" list="#{'等于':'等于','大于':'大于','小于':'小于','大于等于':'大于等于','小于等于':'小于等于','like':'包含于'}"></s:select>
		<input type="submit" value="添  加"  /> 
        <input type="button" value="删除选择"  onclick="removeItem();"/>
        <s:hidden name="formid" id="formid" value="%{formid}" theme="simple"></s:hidden>
        <s:hidden name="ids" theme="simple"></s:hidden> 
        <s:hidden name="demId" id="demId" value="%{id}"  theme="simple"></s:hidden>  
			</td>
			<td>
			<div style="float:right;width:200px;text-align:right;">
						<a href="javascript:moveTop();" class="easyui-linkbutton" plain="true" >置顶</a>
						<a href="javascript:moveUp();" class="easyui-linkbutton" plain="true" >上移</a>
						<a href="javascript:moveDown();" class="easyui-linkbutton" plain="true" >下移</a>
						<a href="javascript:moveBottom();" class="easyui-linkbutton" plain="true" >置底</a>
					    </div>
			</td>
		</tr>
	</table>
   </s:form>
   
   </div>
	<div region="center" style="padding:5px;border:0px;height:300px">
			<table id="iformSearch_grid"  style="border:0px;"></table>
	</div>
</body>
</html>
