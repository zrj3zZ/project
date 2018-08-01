<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title><s:property value="title"/>-节点规则设置</title>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/public.css" />
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/easyui-lang-zh_CN.js"></script>
	<script src="iwork_js/process/process_desk.js"></script>
	<script type="text/javascript" src="iwork_js/processDesignTab.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style type="text/css">
	.td_title {
				padding-left:5px;
				padding-right:10px;
				width:15%;
				text-align:right;
			}
		.td_data{
			    width:85%;
		}
		/*节点设置*/
		.node_body{ background-color:#f6f9fb;width:100%;}
		.node_nav{  height:32px; padding-left:20px; line-height:32px;}
		.node_box{  background:url(iwork_img/node_frame_page.jpg) repeat-x ; }
		.node_frame{ padding:15px; clear:both; line-height:30px; color:#6c6c6c; background-color:#f1f8fc;width:100%\9; border-bottom:1px solid #838383; }
		.node_line{  height:2px;  background:url(iwork_img/horizontal.jpg) repeat-x; width:100%; }
		.node_buttonframe{width:360px; float:right;align:right}
		.node_buttonframe a{ text-decoration:none;color:#6c6c6c}
		.setup_pic{ height:89px; width:86px; background:url(iwork_img/node_icon1.jpg) no-repeat;}
        .setup_pic_font{ color:#000; font-family:Arial; font-size:10px; padding-left:35px; padding-top:50px;-webkit-text-size-adjust:none;}				 
	    .none_item{height:450px;line-height:450px;text-align:center;font-style:italic;color:#888888; font-family:宋体; font-size:18px;}
	</style>
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
	function showItem(id){
		var pageUrl = "process_summary_showItem.action?id="+id;
		this.openDialog("属性设置",pageUrl);
	}
	//添加和编辑窗口
	function openDialog(title,pageUrl){
		   art.dialog.open(pageUrl,{
			    		id:'openStepDefWinDiv',
			    	title:title, 
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:600,
				    height:410,
					close:function(){
					   $('#showField_grid').datagrid('reload');
					  
					}
				 });
	}
	function add(){
	   var  prcDefId =$("#editForm_prcDefId").val();
				var actDefId = $("#editForm_actDefId").val();
				var actStepDefId  = $("#editForm_actStepDefId").val();
				var pageUrl  = "process_summary_add.action?prcDefId="+prcDefId+"&actDefId="+actDefId+"&actStepDefId="+actStepDefId;
				openDialog('添加',pageUrl);
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
			      var url='process_summary_del.action';
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
	    var  prcDefId =$("#editForm_prcDefId").val();
		var actDefId = $("#editForm_actDefId").val();
		var actStepDefId  = $("#editForm_actStepDefId").val();
	    var rows = $('#showField_grid').datagrid('getSelections');
	    if(rows.length==0){
	         art.dialog.tips("请选择要移动的行记录",1);
	         return;
	    }
	    if(rows.length>1){
	         art.dialog.tips("选择行过多",1);
	         return;
	    }
	    var url='process_summary_moveUp.action';
	    $.post(url,{id:rows[0].ID,prcDefId:prcDefId,actDefId:actDefId,actStepDefId:actStepDefId},function(data){
	         if(data=='ok'){
	             $('#showField_grid').datagrid('reload');
	         }
	    });
	}
	function moveDown(){
	    var  prcDefId =$("#editForm_prcDefId").val();
		var actDefId = $("#editForm_actDefId").val();
		var actStepDefId  = $("#editForm_actStepDefId").val();
	     var rows = $('#showField_grid').datagrid('getSelections');
	    if(rows.length==0){
	         art.dialog.tips("请选择要移动的行记录",1);
	         return;
	    }
	    if(rows.length>1){
	         art.dialog.tips("选择行过多",1);
	         return;
	    }
	    var url='process_summary_moveDown.action';
	    $.post(url,{id:rows[0].ID,prcDefId:prcDefId,actDefId:actDefId,actStepDefId:actStepDefId},function(data){
	         if(data=='ok'){
	             $('#showField_grid').datagrid('reload');
	         }
	    });
	}
	function moveTop(){
		var  prcDefId =$("#editForm_prcDefId").val();
		var actDefId = $("#editForm_actDefId").val();
		var actStepDefId  = $("#editForm_actStepDefId").val();
	    var rows = $('#showField_grid').datagrid('getSelections');
	    if(rows.length==0){
	         art.dialog.tips("请选择要移动的行记录",1);
	         return;
	    }
	    if(rows.length>1){
	         art.dialog.tips("选择行过多",1);
	         return;
	    }
	    var url='process_summary_moveTop.action';
	    $.post(url,{id:rows[0].ID,prcDefId:prcDefId,actDefId:actDefId,actStepDefId:actStepDefId},function(data){
	         if(data=='ok'){
	            $('#showField_grid').datagrid('reload');
	         }
	    });
	}
	function moveBottom(){
		var  prcDefId =$("#editForm_prcDefId").val();
		var actDefId = $("#editForm_actDefId").val();
		var actStepDefId  = $("#editForm_actStepDefId").val();
	    var rows = $('#showField_grid').datagrid('getSelections');
	    if(rows.length==0){
	         art.dialog.tips("请选择要移动的行记录",1);
	         return;
	    }
	    if(rows.length>1){
	         art.dialog.tips("选择行过多",1);
	         return; 
	    }
	    var url='process_summary_moveBottom.action';
	    $.post(url,{id:rows[0].ID,prcDefId:prcDefId,actDefId:actDefId,actStepDefId:actStepDefId},function(data){
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
						<table id="showField_grid" style="height:auto" style="height:500px" iconCls="icon-edit" singleSelect="false" idField="ID"  url="process_summary_json.action?actDefId=<s:property value="actDefId"/>&prcDefId=<s:property value="prcDefId"/>&actStepDefId=<s:property value="actStepDefId"/>" >
									<thead>
										<tr>
										    <th field="CK" checkbox=true></th>
										     <th field="ID" hidden="true" width="50" align="left" >序号</th> 
										     <th field="FORM_TITLE" width="180" >表单标题</th>	 
										    <th field="FIELD_TITLE" width="100" >名称</th>	
											<th field="FIELD_NAME" width="100" align="left">字段</th>
											<th field="GROUPID" width="80" align="left" >分组</th> 
											<th field="DO" width="80" align="left" >操作</th> 
										</tr> 
									</thead>
						</table>					
					</div>
	</div>
		<s:form name="editForm" id="editForm">
			        	<s:hidden name="actDefId"/>
		                <s:hidden name="actStepDefId"/>
		                <s:hidden name="prcDefId"/>
			        	<s:hidden name="id"></s:hidden>
			        </s:form>
	</div>
</div>	
</body>
</html>
