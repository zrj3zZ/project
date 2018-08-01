<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>  
    <title>字段统计</title>
    
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/public.css" /> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js" ></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=default"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/easyui-lang-zh_CN.js"></script>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
	$(document).ready(function(){
	    $('#count_grid').datagrid({
				onBeforeLoad:function(){
					$(this).datagrid('rejectChanges');
				},
				onDblClickRow:function(rowIndex, rowData){
				 var reportId=$('#reportId').val();
				var url = 'url:ireport_designer_statistics_edit.action?id='+rowData.ID+'&reportId='+reportId;
				openDialog('编辑',url);
				}
			});   
	});
	function edit(id,reportId){
	     var url = 'url:ireport_designer_statistics_edit.action?id='+id+'&reportId='+reportId;
		 openDialog('编辑',url);
	}
	//添加和编辑窗口
	function openDialog(title,url){
		art.dialog.open(url,{
					id:'openStepDefWinDiv',
					cover:true,
					title:title,
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					rang:true,
					width:500,
					cache:false,
					lock: true,
					height:350, 
					iconTitle:false, 
					extendDrag:true,
					autoSize:true,
					close:function(){
					   $('#count_grid').datagrid('reload');
					  
					}
				});	
	}
	function add(){
	    var reportId=$('#reportId').val();
	    var reportType=$('#reportType').val();
	    var chartType=$('#chartType').val();
		var url = 'url:ireport_designer_statistics_add.action?reportId='+reportId+'&reportType='+reportType+'&chartType='+chartType;
		openDialog('添加',url);
	}
	function del(){
	        var ids = new Array();
			var rows = $('#count_grid').datagrid('getSelections');
			for(var i=0;i<rows.length;i++){
				ids.push(rows[i].ID);
			}
			if(ids.length==0){
				alert("请选择要删除的记录");
			}
			else{
			      var url='ireport_designer_statistics_del.action';
			      if(confirm( '确定删除？ ') == true){
			      	$.post(url,{ids:ids.join()},function(data){
         				if(data=='ok'){
             				location.reload();
         				}
    				});
			      }
			}	    
	     
	      
	}
	function moveUp(){
	    var reportId=$('#reportId').val();
	    var rows = $('#count_grid').datagrid('getSelections');
	    if(rows.length==0){
	        alert("请选择要移动的行记录");
	         return;
	    }
	    if(rows.length>1){
	         alert("选择行过多");
	         return;
	    }
	    var url='ireport_designer_statistics_moveUp.action';
	    $.post(url,{id:rows[0].ID,reportId:reportId},function(data){
	         if(data=='ok'){
	             $('#count_grid').datagrid('reload');
	         }
	    });
	}
	function moveDown(){
	    var reportId=$('#reportId').val();
	    var rows = $('#count_grid').datagrid('getSelections');
	    if(rows.length==0){
	        alert("请选择要移动的行记录");
	         return;
	    }
	    if(rows.length>1){
	         alert("选择行过多");
	         return;
	    }
	    var url='ireport_designer_statistics_moveDown.action';
	    $.post(url,{id:rows[0].ID,reportId:reportId},function(data){
	         if(data=='ok'){
	             $('#count_grid').datagrid('reload');
	         }
	    });
	}
	function moveTop(){
		var reportId=$('#reportId').val();
	    var rows = $('#count_grid').datagrid('getSelections');
	    if(rows.length==0){
	        alert("请选择要移动的行记录");
	         return;
	    }
	    if(rows.length>1){
	         alert("选择行过多");
	         return;
	    }
	    var url='ireport_designer_statistics_moveTop.action';
	    $.post(url,{id:rows[0].ID,reportId:reportId},function(data){
	         if(data=='ok'){
	            $('#count_grid').datagrid('reload');
	         }
	    });
	}
	function moveBottom(){
		var reportId=$('#reportId').val();
	    var rows = $('#count_grid').datagrid('getSelections');
	    if(rows.length==0){
	        alert("请选择要移动的行记录");
	         return;
	    }
	    if(rows.length>1){
	         alert("选择行过多");
	         return;
	    }
	    var url='ireport_designer_statistics_moveBottom.action';
	    $.post(url,{id:rows[0].ID,reportId:reportId},function(data){
	         if(data=='ok'){
	             $('#count_grid').datagrid('reload');
	         }
	    });
	}
	</script>
  </head>
  
  <body class="easyui-layout">
     <div region="center" style="padding:3px;border-top:0px;">					
					<div style="border-bottom:1px solid #efefef;margin-bottom:3px;text-align:left;padding-right:20px;backgroud-color:#efefef">
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
					<div>
						<table id="count_grid" style="height:auto"  iconCls="icon-edit" singleSelect="false" idField="ID" url="ireport_designer_statistics_table.action?reportId=<s:property value='reportId'  escapeHtml='false'/>" >
									<thead>
										<tr>
										    <th field="CK" checkbox=true></th>
											<th field="FIELD_NAME" width="150" align="left">字段</th>
											<th field="FIELD_TITLE" width="150" >名称</th>	
											<th field="STATISTICS_TYPE" width="100" align="left" >统计类型</th>
											<th field="EDIT" width="100" align="left" >编辑</th> 
										</tr>
									</thead>
						</table>					
						<s:hidden name='reportId'></s:hidden>
						<s:hidden name='reportType'></s:hidden>
						<s:hidden name='chartType'></s:hidden>
					</div>	
	</div>
  </body>
</html>
