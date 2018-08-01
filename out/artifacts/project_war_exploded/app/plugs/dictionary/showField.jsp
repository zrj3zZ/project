<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
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
	    $('#showField_grid').datagrid({
				onBeforeLoad:function(){
					$(this).datagrid('rejectChanges');
				},
				onDblClickRow:function(rowIndex, rowData){
				 var dictionaryId=$('#dictionaryId').val();
				var url = 'sys_dictionary_showField_edit.action?id='+rowData.ID+'&dictionaryId='+dictionaryId;
				openDialog('编辑',url);
				}
			});   
	});
	function edit(id,dictionaryId){
		 var url = 'sys_dictionary_showField_edit.action?id='+id+'&dictionaryId='+dictionaryId;
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
					   $('#showField_grid').datagrid('reload');
					}
			}); 
	}
	function add(){
	    var dictionaryId=$('#dictionaryId').val();
		var url = 'sys_dictionary_showField_add.action?showtype=output&dictionaryId='+dictionaryId;
		openDialog('添加',url);
	}
	function del(){
	        var ids = new Array();
			var rows = $('#showField_grid').datagrid('getSelections');
			for(var i=0;i<rows.length;i++){
				ids.push(rows[i].ID);
			}
			if(ids.length==0){
			      alert("请选择要删除的记录"); 
			}
			else{
			      var url='sys_dictionary_showField_del.action';
	    		  if(confirm("确认删除当前选择设置吗？")){  
	          	  		$.post(url,{ids:ids.join()},function(data){
	         				if(data=='ok'){
	             				location.reload();
	         				}
	    				});
	    			}	
			}	    
	     
	      
	}
	function moveUp(){
	    var dictionaryId=$('#dictionaryId').val();
	    var rows = $('#showField_grid').datagrid('getSelections');
	    if(rows.length==0){
	         alert("请选择要移动的行记录");
	         return;
	    }
	    if(rows.length>1){
	         alert("选择行过多");
	         return;
	    }
	    var url='sys_dictionary_showField_moveUp.action';
	    $.post(url,{id:rows[0].ID,dictionaryId:dictionaryId},function(data){
	         if(data=='ok'){
	             $('#showField_grid').datagrid('reload');
	         }
	    });
	}
	function moveDown(){
	    var dictionaryId=$('#dictionaryId').val();
	     var rows = $('#showField_grid').datagrid('getSelections');
	    if(rows.length==0){
	         alert("请选择要移动的行记录");
	         return;
	    }
	    if(rows.length>1){
	         alert("选择行过多");
	         return;
	    }
	    var url='sys_dictionary_showField_moveDown.action';
	    $.post(url,{id:rows[0].ID,dictionaryId:dictionaryId},function(data){
	         if(data=='ok'){
	             $('#showField_grid').datagrid('reload');
	         }
	    });
	}
	function moveTop(){
		var dictionaryId=$('#dictionaryId').val();
	    var rows = $('#showField_grid').datagrid('getSelections');
	    if(rows.length==0){
	         alert("请选择要移动的行记录");
	         return;
	    }
	    if(rows.length>1){
	         alert("选择行过多");
	         return;
	    }
	    var url='sys_dictionary_showField_moveTop.action';
	    $.post(url,{id:rows[0].ID,dictionaryId:dictionaryId},function(data){
	         if(data=='ok'){
	            $('#showField_grid').datagrid('reload');
	         }
	    });
	}
	function moveBottom(){
		var dictionaryId=$('#dictionaryId').val();
	    var rows = $('#showField_grid').datagrid('getSelections');
	    if(rows.length==0){
	         alert("请选择要移动的行记录");
	         return;
	    }
	    if(rows.length>1){
	         alert("选择行过多");
	         return;
	    } 
	    var url='sys_dictionary_showField_moveBottom.action';
	    $.post(url,{id:rows[0].ID,dictionaryId:dictionaryId},function(data){
	         if(data=='ok'){
	             $('#showField_grid').datagrid('reload');
	         }
	    });
	}
	</script>
  </head>
  
  <body class="easyui-layout">
  <div region="north"  style="border:0px">
  		<div class="tools_nav">
  			<div style="border:0px">
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
  	</div>
     <div region="center" style="padding:3px;border:0px;">					
					
					<div>
						<table id="showField_grid" style="height:auto"  iconCls="icon-edit" singleSelect="false" idField="ID" url="sys_dictionary_showField_table.action?dictionaryId=<s:property value='dictionaryId'  escapeHtml='false'/>" >
									<thead>
										<tr>
										    <th field="CK" checkbox=true></th>
											<th field="FIELD_NAME" width="100" align="left">字段ID</th>
											<th field="FIELD_TITLE" width="100" >字段名称</th>	
											<th field="TARGET_FIELD" width="100" align="left" >目标字段</th> 
										 	<th field="DISPLAY_WIDTH" width="100" align="left" >显示宽度</th>  											
										    <th field="EDIT" width="80" align="left" >操作</th> 
										</tr>
									</thead>
						</table>					
						<s:hidden name='dictionaryId'></s:hidden>
					</div>	
	</div>
  </body>
</html>
