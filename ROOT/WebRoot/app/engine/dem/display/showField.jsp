<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>  
    <title>查询条件</title>
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
					openMapModify(rowData.ID);
				}
			});   
	});
	function openMapModify(id){
		 var demId=$('#demId').val();
		 var url = 'sysDem_display_edit.action?id='+id+'&demId='+demId;
		openDialog('编辑',url);  
	}
	function edit(id,reportId){
		 var url = 'ireport_designer_showField_edit.action?id='+id+'&reportId='+reportId;
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
	    var demId=$('#demId').val();
		var url = 'sysDem_display_add.action?demId='+demId;
		openDialog('添加',url); 
	}
	function del(){
	        var ids = new Array();
			var rows = $('#showField_grid').datagrid('getSelections');
			for(var i=0;i<rows.length;i++){
				ids.push(rows[i].ID);
			}
			if(ids.length==0){
			      art.dialog.tips("请选择要删除的记录",1);
			}
			else{
			      var url='sysDem_display_del.action';
	    		  art.dialog.confirm('确定删除？',function(){   
	          	  		$.post(url,{ids:ids.join()},function(data){
	         				if(data=='success'){
	             				location.reload();
	         				} 
	    				});
	    			});	
			}	    
	     
	      
	}
	function moveUp(){
	    var demId=$('#demId').val();
	    var rows = $('#showField_grid').datagrid('getSelections');
	    if(rows.length==0){
	         art.dialog.tips("请选择要移动的行记录",1);
	         return;
	    }
	    if(rows.length>1){
	         art.dialog.tips("选择行过多",1);
	         return;
	    }
	    var url='sysDem_display_moveUp.action';
	    $.post(url,{id:rows[0].ID,demId:demId},function(data){
	         if(data=='ok'){
	             $('#showField_grid').datagrid('reload');
	         }
	    });
	}
	function moveDown(){
	    var demId=$('#demId').val();
	     var rows = $('#showField_grid').datagrid('getSelections');
	    if(rows.length==0){
	         art.dialog.tips("请选择要移动的行记录",1);
	         return;
	    }
	    if(rows.length>1){
	         art.dialog.tips("选择行过多",1);
	         return;
	    }
	    var url='sysDem_display_moveDown.action';
	    $.post(url,{id:rows[0].ID,demId:demId},function(data){
	         if(data=='ok'){
	             $('#showField_grid').datagrid('reload');
	         }
	    });
	}
	function moveTop(){
		var demId=$('#demId').val();
	    var rows = $('#showField_grid').datagrid('getSelections');
	    if(rows.length==0){
	         art.dialog.tips("请选择要移动的行记录",1);
	         return;
	    }
	    if(rows.length>1){
	         art.dialog.tips("选择行过多",1);
	         return;
	    }
	    var url='sysDem_display_moveTop.action';
	    $.post(url,{id:rows[0].ID,demId:demId},function(data){
	         if(data=='ok'){
	            $('#showField_grid').datagrid('reload');
	         }
	    });
	}
	function moveBottom(){
		var demId=$('#demId').val();
	    var rows = $('#showField_grid').datagrid('getSelections');
	    if(rows.length==0){
	         art.dialog.tips("请选择要移动的行记录",1);
	         return;
	    }
	    if(rows.length>1){
	         art.dialog.tips("选择行过多",1);
	         return; 
	    }
	    var url='sysDem_display_moveBottom.action';
	    $.post(url,{id:rows[0].ID,demId:demId},function(data){
	         if(data=='ok'){
	             $('#showField_grid').datagrid('reload');
	         }
	    });
	}
	</script>
  </head>
  <body class="easyui-layout">
     <div region="center" style="padding:3px;border:0px">				
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
						<table id="showField_grid" style="height:auto" style="height:500px" iconCls="icon-edit" singleSelect="false" idField="ID"  url="sysDem_display_tableJson.action?demId=<s:property value='demId'  escapeHtml='false'/>" >
									<thead>
										<tr>
										    <th field="CK" checkbox=true></th>
										    <th field="TITLE" width="80" >名称</th>	
											<th field="FIELD_NAME" width="200" align="left">字段</th>
											
										 	<th field="WIDTH" width="60" align="left" >显示宽度</th>  
										<!--	<th field="FIELD_DISPLAY" width="150" align="left" >显示权限</th>  -->
											<th field="IS_DISPLAY" width="80" align="left" >是否显示</th> 
										<!-- 	<th field="FIELD_PARAMS" width="150" align="left" >钻取参考值</th>  -->
										    <th field="DO" width="150" align="left" >编辑</th> 
										</tr> 
									</thead>
						</table>					
						<s:hidden name='demId'></s:hidden> 
					</div>	
	</div>
  </body>
</html>
