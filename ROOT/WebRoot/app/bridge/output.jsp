<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>  
    <title>参数列表</title>
    
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
					edit(rowData.ID);
				}
			});   
	});
	function edit(id){
	     var url = 'conn_bridge_param_editOutParam.action?id='+id;
		 openDialog('编辑',url);
	}
	function linkSub(id,pid){
	     var url = 'conn_design_param_output.action?id='+id+'&pid='+pid;
		 this.location = url;
	}
	
	function add(){
		var uuid = $("#paramUUID").val();
		var url = 'conn_bridge_param_addOutParam.action?uuid='+uuid;
		openDialog('添加',url);
	}
	//添加和编辑窗口
	function openDialog(title,pageUrl){
		 art.dialog.open(pageUrl,{
			    	id:'formMapIndexWinDiv',
			    	title:title,  
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:600,
				    height:450,
					close:function(){
					   $('#condition_grid').datagrid('reload');
					  
					}
				 });
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
			      var url='conn_bridge_outparam_del.action';
	    		  if(confirm("确认删除当前参数模型吗？")){
	          	  		$.post(url,{ids:ids.join()},function(data){
	         				if(data=='ok'){
	             				location.reload();
	         				}
	    				});
	    			};	
			}	    
	     
	      
	}
	
	function moveUp(){
	    var rows = $('#condition_grid').datagrid('getSelections');
	    if(rows.length==0){
	         alert("请选择要移动的行记录");
	         return;
	    }
	    if(rows.length>1){
	         alert("选择行过多");
	         return;
	    }
	    var url='conn_bridge_param_moveUpout.action';
	    $.post(url,{id:rows[0].ID},function(data){
	         if(data=='ok'){
	             $('#condition_grid').datagrid('reload');
	         }
	    });
	}
	function moveDown(){
	    var rows = $('#condition_grid').datagrid('getSelections');
	    if(rows.length==0){
	         alert("请选择要移动的行记录");
	         return;
	    }
	    if(rows.length>1){
	         alert("选择行过多");
	         return;
	    }
	    var url='conn_bridge_param_moveDownout.action';
	    $.post(url,{id:rows[0].ID},function(data){
	         if(data=='ok'){
	             $('#condition_grid').datagrid('reload');
	         }
	    });
	}
	function setOutputParams(){
		var pid=$('#pid').val();
	    var rows = $('#condition_grid').datagrid('getSelections');
	    if(rows.length==0){
	         alert("请选择要调整为输出参数的行");
	         return;
	    }
	    if(rows.length>1){
	         alert("选择行过多");
	         return;
	    }
	     if(rows[0].TYPE!='TABLE'){
	    	 alert("只允许设置类型为'TABLE'的参数");
	         return;
	    }
	    var url='conn_design_param_setType.action';
	    $.post(url,{id:rows[0].ID,inorout:'input'},function(data){
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
						<div style="float:right;width:200px;text-align:right;padding-right:10px;">
						<a href="javascript:moveUp();" class="easyui-linkbutton" plain="true" >上移</a> 
						<a href="javascript:moveDown();" class="easyui-linkbutton" plain="true" >下移</a>
					    </div>
					    <div style="clear: both;"></div>
					</div>
  	</div>
     <div region="center" style="padding:3px;border:0px;">					
					<div>
						<table id="condition_grid" style="height:400px"  iconCls="icon-edit" singleSelect="false" idField="ID" url="conn_bridge_outparam_json.action?uuid=<s:property value="model.uuid"/>" >
									<thead>
										<tr>
										    <th field="CK" checkbox=true></th>
											<th field="ID" width="50" align="left">序号</th>
											<th field="PARAM_NAME" width="130" >参数名称</th>	
											<th field="PARAM_KEY" width="180">关键字</th>
											<th field="URL" width="380">url</th>
											<th field="EDIT" width="180" align="left" >操作</th>
										</tr>
										</thead>
										
						</table>	
					<input type="hidden" id="paramUUID" value="<s:property value="model.uuid"/>"/>
					</div>	
	</div>
  </body>
</html>
