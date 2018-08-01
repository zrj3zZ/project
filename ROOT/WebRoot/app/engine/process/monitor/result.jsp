<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<link href="iwork_css/eagles_searcher.css" rel="stylesheet" type="text/css"/>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style type="text/css">
	
	</style>
<script>
function changeTab(obj,tabkey){
		var className = obj.className;
		$('ul li').each(function(){
						if(tabkey != this.id){
							$(this).attr("class",""); 	
						}
		});	
		
		if(className==''){
			obj.className = "mini_selected";
			$("#searchType").val(obj.id);
			//如果检索内容不为空，则直接查询
			var searchTxt = $('#searcherTxt').val();
			if(searchTxt!=null){
				$('form').attr('action','eaglesSearch_Do.action');
				$("#editForm").submit();
			}
		}
	}

	function doForword(taskId){
				 var pageUrl = "processInstanceManageForwardPage.action?taskId="+taskId;
				 art.dialog.open(pageUrl,{
					id:'sendDialog',
					title:"管理员任务转发",
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:500,
				    height:510
				 });
	}
	function doJump(taskId){
		 pageUrl = "processInstanceManageJumpPage.action?taskId="+taskId;
		  art.dialog.open(pageUrl,{
					id:'sendDialog',
					title:"管理员任务跳转",
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:500,
				    height:510
				 });
	}
	function doDel(id){
		var info = "确认删除当前流程任务吗?"
		if(confirm(info)){
				var pageurl = "processInstanceManageRemove.action?taskId="+id;
				$.ajax({ 
		            type:'POST',
		            url:pageurl,
		            success:function(msg){ 
		            	  if(msg=="success"){  
		                  	art.dialog.tips("移除成功！");
		                  	reload();
		                  }else if(msg=="isrun"){
		                     art.dialog.tips("‘运行中’的流程模型无法移除，请先停用后移除流程！");
		                  }else if(msg=="nopurview"){
		                     art.dialog.tips("权限不足");
		                  }
		                  else if(msg=="error"){
		                     art.dialog.tips("移除失败！");
		                  } 
		            }
		        });
		}
	}
	function reload(){
	    this.location.reload();
	}
</script>
<style type="text/css">
a {
 font-size: 10pt; text-decoration: none; color: #0000ff;
 }
	.result_title{
		color:#333;
		text-align:left;
		text-decoration:none; 
	}
	.op{
		color:#333;
		text-align:left;
		text-decoration:none; 
	}
	.toolbar td{
			background:url(../iwork_img/engine/tools_nav_bg.jpg) repeat-x;
			height:34px;
			line-height:30px;
			padding-left:10px;
			vertical-align:middle;
			padding-top:2px;
			padding-bottom:2px; 
			border-right:1px #efefef;
	} 
	.itemdata td{
			height:34px;
			line-height:30px;
			padding-left:5px;
			padding-left:5px;
			vertical-align:middle;
			padding-top:2px;
			padding-bottom:2px; 
			border-right:1px #efefef solid;
			border-bottom:1px #efefef solid;
	}
</style> 
</head> 
<body class="easyui-layout">
	<div region="center" style="text-align:left;border:0px">
					<s:if test="searchlist==null||searchlist.size<=0">
						<div class="nofind">
						<img alt="查询信息未找到"  border="0" src="iwork_img/nondynamic.gif">未找到与“<s:property value="searcherTxt"/>”相关的内容!
						</div>
					</s:if> 
					<table width="100%" style="border:1px solid #efefef" cellpadding='1' cellspacing='0'>
							<tr class="toolbar">
								<td >发送人</td>
								<td >当前办理人</td>
								<td>任务标题</td>
								<td>当前节点</td>
								<td>操作</td>
							</tr>
							<s:iterator  value="searchlist" status="status"> 
								<tr class="itemdata">
								<td ><s:property value="owner"  escapeHtml="false"/></td>
								<td ><s:property value="assignee"  escapeHtml="false"/> </td>
								<td class="result_title"> 
								<s:property value="title"  escapeHtml="false"/>
								</td>
								<td ><s:property value="stepName"  escapeHtml="false"/> </td>
								
									<td class="op">
									<a href="javascript:doForword(<s:property value="taskId"  escapeHtml="false"/>);">移交他人办理</a> 
									<a href="javascript:doJump(<s:property value="taskId"  escapeHtml="false"/>);">节点跳转 </a> 
									<a href="javascript:doDel(<s:property value="taskId"  escapeHtml="false"/>);">删除任务</a> 
									 </td>
								</tr>
						  	</s:iterator> 
					</table>
		</div>
				
</body>
</html>
