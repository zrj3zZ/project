<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>选择添加字段设置</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
    <style type="text/css">
    #rightbox {
		font-weight:bold; font-family:微软雅黑; font-size:12px; text-align:left;
		width:250px;
		height:auto;    	
		margin-right:20px;
		overflow:hidden;
    }
    </style>
   <script type="text/javascript">
		$(function(){
	             var setting = {
	             	check:{
						enable:true
					},
					view: {
						showLine: true, 
						selectedMulti: false
					},
					data: { 
						simpleData: {
							enable: true,
							idKey: "id",
							pIdKey: "pid",
							rootPId: 0
						}
					},  
					async: {
						enable:true,
						url:"radiobook_orgjson.action",
						dataType:"json",
						autoParam:["id","nodeType"]
					}, 
					callback: {
						onClick: onClick,
						onAsyncSuccess: onAsyncSuccess
					}
				};
				
	             function onClick(event, treeId, treeNode){
	            	var treeObj = $.fn.zTree.getZTreeObj(treeId);
	            	if(treeNode.isParent){
	            		if(treeNode.open){
		            		 treeObj.expandNode(treeNode, false, null, null, true);
						  }else{
								treeObj.expandNode(treeNode, true, null, null, true);
						  }
	            	}else{
	            		if(treeNode.checked){
		             		treeObj.checkNode(treeNode, false, true);
		             	}else{
		             		treeObj.checkNode(treeNode, true, true);
		             	}
	            	}
	             } 
	             function onAsyncSuccess(event, treeId, treeNode, msg){
	                 
	             }
	             $.fn.zTree.init($("#fieldTree"), setting);
		});
		//添加字段
		function add(){
					var treeObj = $.fn.zTree.getZTreeObj("fieldTree");
					var nodes = treeObj.getCheckedNodes(true); 
					var arr = new Array();  
					for(var i=0,j=0;i<nodes.length;i++){					    				   
						if(nodes[i].nodeType=="dept"){
							continue;
						}
						arr.push(nodes[i].userId);
					}
					if(arr.length==0){
					    art.dialog.tips("请选择要添加的字段",1);
					}
					else{
					     doAdd(arr.join());
					}
					
		}
		function doAdd(userids){
			var deptno = $("#deptno").val(); 
			var instanceid = $("#instanceid").val(); 
			 var url = 'iwork_hr_legal_adduser.action';
		    $.post(url,{instanceid:instanceid,deptno:deptno,userids:userids},function(data){
		         if(data=='success'){ 
		        	 
		         }else{
		            art.dialog.tips("保存失败",1);
		         }
		    }); 
		}   
   </script>
  </head>
  
  <body>
  <div style="border-bottom:1px solid #efefef;margin-bottom:3px;padding-bottom:8px;text-align:left;padding-right:20px;backgroud-color:#efefef">
					<div style="float:left;width:200px;">
						<a href="javascript:add();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加</a>
						<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
						</div>
						<br>
					</div>
					<div style="text-align:left;padding:5px;color='#808080';font-size:10px;">
	        				请选择您要添加的条件字段
	        		</div>
	            	<div style="padding-left:5px;margin-left:30px; width:400px;  overflow-y:auto; border:1px #C0C0C0 solid;">
	            			<ul id="fieldTree"  class="ztree"> 
						   </ul> 
	                </div>
	 	<s:hidden name="deptno" id="deptno"></s:hidden>
	 	<s:hidden name="instanceid" id="instanceid"></s:hidden>
  </body>
</html>
