<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/design_model.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/design_model.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/portal.css">	
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<link href="iwork_css/engine/sysenginegroup.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
	<script type="text/javascript" src="../iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="../iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
    <script type="text/javascript">
  
 //==========================装载快捷键===============================//快捷键
    jQuery(document).bind('keydown',function (evt){		
		    if(evt.ctrlKey&&evt.shiftKey){
			return false; 
		   }
		   else if(evt.ctrlKey && event.keyCode==82){ //Ctrl+r /刷新操作
			         this.location.reload(); return false;
		     } 
		  else if(evt.shiftKey && event.keyCode==78){ //Shift+n 添加首级分类
					 addItem(); return false;
				}
		 else if(evt.shiftKey && event.keyCode==77){ //Shift+m 添加子分类
					addSubItem(); return false;
				}
		 else if(evt.ctrlKey && event.keyCode==83){ //ctrl+s保存
					doSubmit(); return false;
				}				
}); //快捷键
		$(function(){
  				var setting = {
					async: {
						enable: true, 
						url:"sysEngineGroup!openjson.action",
						dataType:"json"
					}, 
					callback: { 
						onClick:onClick
					} 
				};  
				var treeObj = $.fn.zTree.init($("#grouptree"), setting);
				function onClick(node){
                  
               }
		});
		
		function addItem(){
			var pageUrl = "sysEngineGroup_add.action";
			art.dialog.open(pageUrl,{
			    	id:'groupWinDiv',
			    	title:'新建首级目录',  
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:600,
				    height:410
				 });
		}
		function addSubItem(){
			var zTree = $.fn.zTree.getZTreeObj("grouptree");
			var nodes = zTree.getSelectedNodes();
			var parentid = 0;
			if(nodes.length>0){
				parentid = nodes[0].id; 
				if(parentid>0){  
					var pageUrl = "sysEngineGroup_add.action?parentid="+parentid;
					art.dialog.open(pageUrl,{
				    	id:'groupWinDiv',
				    	title:'新建分组模型',  
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width:600,
					    height:410
					 });
				}else{
					alert('请在树视图中选择您要添加的目录');
					return ;
				}
			}else{
				alert('请在树视图中选择您要添加的目录');
				return ;
			}
		}
		function edit(){
			var zTree = $.fn.zTree.getZTreeObj("grouptree");
			var nodes = zTree.getSelectedNodes();
			var id = 0;
			if(nodes.length>0){ 
				id = nodes[0].id; 
				if(id>0){  
					var pageUrl = "sysEngineGroup_edit.action?id="+id;
					art.dialog.open(pageUrl,{
				    	id:'groupWinDiv',
				    	title:'编辑模型',  
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width:520,
					    height:350
					 });
				}else{ 
					alert('请在树视图中选择您要编辑的目录');
					return ; 
				}
			}else{
				alert('请在树视图中选择您要编辑的目录');
				return ;
			}
		}
		
		function remove(){
			if(confirm("确认删除当前分组模型吗？")){
				var zTree = $.fn.zTree.getZTreeObj("grouptree");
				var nodes = zTree.getSelectedNodes();
				if(nodes.length>0){
					var id = nodes[0].id; 
					//var index = zTree.getNodeIndex(nodes[0]);
					 var pageurl = 'sysEngineGroup_remove.action?id='+id;
							$.ajax({ 
						            type:'POST',
						            url:pageurl,
						            success:function(msg){ 
						               if(msg=='success'){
											zTree.reAsyncChildNodes(null,"refresh",true);
											alert('删除成功');
						               }else if(msg=='havingsub'){
						               		alert('您删除的目录下包含有定义的业务对象，无法删除');
						               }else if(msg=='nopurview'){
						               		alert('权限不足，无法删除');
						               }else{
						              		 alert('删除错误');
						               }
						            }  
						        });
				}else{
					alert('请选择您要删除的目录');
					return ;
				}
			}
		}
		function moveUp(){
			var zTree = $.fn.zTree.getZTreeObj("grouptree");
			var nodes = zTree.getSelectedNodes();
			var parentid = 0;
			if(nodes.length>0){
				if(nodes[0].parentid!=0){
					alert('只允许对首级目录进行排序');
					return;
				} 
				var id = nodes[0].id; 
				//var index = zTree.getNodeIndex(nodes[0]);
				 var pageurl = 'sysEngineGroup_moveUp.action?id='+id;
						$.ajax({ 
					            type:'POST',
					            url:pageurl,
					            success:function(msg){ 
					               if(msg!=''){
										zTree.reAsyncChildNodes(null,"refresh",true);
										var tmp = zTree.getNodesByParam("id",id, null);
										zTree.selectNode(tmp,true); 
					               } 
					            } 
					        });
			}else{
				alert('请选择您要移动的目录');
				return ;
			}
		}
		
		function moveDown(){
			var zTree = $.fn.zTree.getZTreeObj("grouptree");
			var nodes = zTree.getSelectedNodes();
			var parentid = 0;
			if(nodes.length>0){
				if(nodes[0].isParent){
					alert('只允许对首级目录进行排序');
					return;
				} 
				var id = nodes[0].id;
				 var pageurl = 'sysEngineGroup_moveDown.action?id='+id;
						$.ajax({ 
					            type:'POST',
					            url:pageurl,
					            success:function(msg){ 
					               if(msg!=''){
										zTree.reAsyncChildNodes(null,"refresh",true);
										zTree.selectNode(nodes[0]); 
					               } 
					            } 
					        });
			}else{
				alert('请选择您要移动的目录');
				return ;
			}
		}
	</script>
</head>
<body class="easyui-layout">

<!-- TOP区 -->
	<div region="north" border="false" >
	<div class="tools_nav">
		<table widht="100%">
			<tr>
				<td align="left">
					<a href="javascript:addItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加首级分类</a>
					<a href="javascript:addSubItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加子分类</a>
					<a href="javascript:edit();" class="easyui-linkbutton" plain="true" iconCls="icon-edit">编辑</a>
					<a href="javascript:remove();" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除</a>
					<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
				</td>
				<td  style="text-align:right">
					【排序】
					<a href="javascript:moveUp();" class="easyui-linkbutton" plain="true" >上移</a>
					<a href="javascript:moveDown();" class="easyui-linkbutton" plain="true" >下移</a>
				</td> 
			</tr>
		</table> 
		
		
	</div>
	</div>
    <!-- 导航区 -->
	<div region="west"  split="false" border="false"  style="height:100%;width:230px;padding-left:5px;overflow:hidden; border-top:1px solid #F9FAFD">
			<table style="height:100%;" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="tree_top"></td>
					</tr>
					<tr>
						<td class="tree_main">
						 <ul id="grouptree" class="ztree"></ul> 
						 </td>
					</tr> 
					<tr>
						<td class="tree_bottom"></td>
					</tr>
				</table>
	</div>
	<div region="center" style="padding:3px;border:0px;">
			<iframe id="groupFrame" name="groupFrame"  src="sysEngineGroup_loadinfo.action" frameborder="no" border="0" marginwidth="0″ marginheight="0″ scrolling="auto" allowtransparency="yes"  id="metadataFrame" name="metadataFrame" width="100%" height="100%"></iframe>
			<!-- <table id="metadata_grid" style="margin:2px;"></table> --> 

	</div>
	
</body>
</html>
