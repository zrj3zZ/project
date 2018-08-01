<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>  
    <title>查询条件</title>
    
    <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/public.css" /> 
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
	$(document).ready(function(){
	    $('#condition_grid').datagrid({
				onBeforeLoad:function(){
					$(this).datagrid('rejectChanges');
				},
				onDblClickRow:function(rowIndex, rowData){
				 	var dictionaryId=$('#dictionaryId').val();
					var url = 'sys_dictionary_condition_edit.action?id='+rowData.ID+'&dictionaryId='+dictionaryId;
					openDialog('编辑',url);
				}
			});   
	});
	function edit(id,dictionaryId){
	     var url = 'sys_dictionary_condition_edit.action?id='+id+'&dictionaryId='+dictionaryId;
		 openDialog('编辑',url);
	}
	//添加和编辑窗口
	function openDialog(title,pageUrl){
			var width = 600;
			var height= 450;
			art.dialog.open(pageUrl,{
			    	id:'openConditionWinDiv',
			    	title:title,
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:width,
				    height:height, 
					close:function(){
					   $('#condition_grid').datagrid('reload');
					}
			});
	}
	function add(){
	    var dictionaryId=$('#dictionaryId').val();
		var url = 'sys_dictionary_condition_add.action?showtype=input&dictionaryId='+dictionaryId;
		openDialog('添加',url);
	}
	function del(){
	        var ids = new Array();
			var rows = $('#condition_grid').datagrid('getSelections');
			for(var i=0;i<rows.length;i++){
				ids.push(rows[i].ID);
			}
			if(ids.length==0){
			      alert("请选择要删除的记录");
			}
			else{
			      var url='sys_dictionary_condition_del.action';
	    		  if(confirm("确认删除当前条件模型吗？")){
	          	  		$.post(url,{ids:ids.join()},function(data){
	         				if(data=='ok'){
	             				location.reload();
	         				}
	    				});
	    			};	
			}	    
	}
	function moveUp(){
	    var dictionaryId=$('#dictionaryId').val();
	    var rows = $('#condition_grid').datagrid('getSelections');
	    if(rows.length==0){
	         alert("请选择要移动的行记录");
	         return;
	    }
	    if(rows.length>1){
	         alert("选择行过多");
	         return;
	    }
	    var url='sys_dictionary_condition_moveUp.action';
	    $.post(url,{id:rows[0].ID,dictionaryId:dictionaryId},function(data){
	         if(data=='ok'){
	             $('#condition_grid').datagrid('reload');
	         }
	    });
	}
	function moveDown(){
	    var dictionaryId=$('#dictionaryId').val();
	    var rows = $('#condition_grid').datagrid('getSelections');
	    if(rows.length==0){
	         alert("请选择要移动的行记录");
	         return;
	    }
	    if(rows.length>1){
	         alert("选择行过多");
	         return;
	    }
	    var url='sys_dictionary_condition_moveDown.action';
	    $.post(url,{id:rows[0].ID,dictionaryId:dictionaryId},function(data){
	         if(data=='ok'){
	             $('#condition_grid').datagrid('reload');
	         }
	    });
	}
	function moveTop(){
		var dictionaryId=$('#dictionaryId').val();
	    var rows = $('#condition_grid').datagrid('getSelections');
	    if(rows.length==0){
	         alert("请选择要移动的行记录");
	         return;
	    }
	    if(rows.length>1){
	         alert("选择行过多");
	         return;
	    }
	    var url='sys_dictionary_condition_moveTop.action';
	    $.post(url,{id:rows[0].ID,dictionaryId:dictionaryId},function(data){
	         if(data=='ok'){
	            $('#condition_grid').datagrid('reload');
	         }
	    });
	}
	function moveBottom(){
		var dictionaryId=$('#dictionaryId').val();
	    var rows = $('#condition_grid').datagrid('getSelections');
	    if(rows.length==0){
	         alert("请选择要移动的行记录");
	         return;
	    }
	    if(rows.length>1){
	         alert("选择行过多");
	         return;
	    }
	    var url='sys_dictionary_condition_moveBottom.action';
	    $.post(url,{id:rows[0].ID,dictionaryId:dictionaryId},function(data){
	         if(data=='ok'){
	             $('#condition_grid').datagrid('reload');
	         }
	    });
	}
	</script>
  </head>
  
  <body class="easyui-layout">
  	<div region="north"  style="border:0px">
  		<div class="tools_nav">
						<div style="float:left;width:200px;">
						<a href="javascript:add();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加</a>
						<a href="javascript:del();" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除</a>
						<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
						</div>
						<div style="float:right;width:200px;text-align:right;">
						<a href="javascript:moveTop();" class="easyui-linkbutton" plain="true" >置顶</a>
						<a href="javascript:moveUp();" class="easyui-linkbutton" plain="true" >上移</a>
						<a href="javascript:moveDown();" class="easyui-linkbutton" plain="true" >下移</a>
						<a href="javascript:moveBottom();" class="easyui-linkbutton" plain="true" >置底</a>
					    </div>
					    <div style="clear: both;"></div>
					</div>
  	</div>
     <div region="center" style="padding:3px;border:0px;">					
					<div>
						<table id="condition_grid" style="height:auto"  iconCls="icon-edit" singleSelect="false" idField="ID" url="sys_dictionary_condition_table.action?dictionaryId=<s:property value='dictionaryId'  escapeHtml='false'/>" >
									<thead>
										<tr>
										    <th field="CK" checkbox=true></th>
											<th field="FIELD_NAME" width="100" align="left">字段ID</th>
											<th field="FIELD_TITLE" width="80" >字段名称</th>	
											<th field="COMPARE_TYPE" width="50" align="left" >比较</th>
											<th field="DISPLAY_TYPE" width="100" align="left" >外观</th>																						
											<th field="DISPLAY_ENUM" width="100" align="left" >参考值</th>
											<th field="EDIT" width="100" align="left" >操作</th> 
										</tr>
									</thead>
						</table>					
						<s:hidden name='dictionaryId'></s:hidden>
					</div>	
	</div>
  </body>
</html>
