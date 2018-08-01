<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html> 
<head> 
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>会签地址簿</title>
	 
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css" /> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/addressbook/radioaddressbookaction.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>	 
		<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
		//加载导航树 
		var api = art.dialog.open.api, W = api.opener;
		var arr=new Array();
		$(function(){ 
			initTree();
		});
		function initTree(){
			var isFixed =  <s:property value="fixed"/>; 
			var jsonurl = "multibook_orgjson.action";
			if(isFixed){
				var actDefId = $("#actDefId").val();
				var prcDefId = $("#prcDefId").val();
				var actStepDefId = $("#actStepDefId").val();
				var taskId = $("#taskId").val();
				jsonurl = "processRuntimeShowUserJson.action?actDefId="+actDefId+"&prcDefId="+prcDefId+"&actStepDefId="+actStepDefId+"&taskId="+taskId;
			}
			var setting = {
						check: {
							enable: false
					    },
					    view: {
							selectedMulti: false
						},
						async: {
							enable: true, 
							url:jsonurl, 
							dataType:"json",
							autoParam:["id","nodeType","companyid"] 
						},
						callback: {
						beforeAsync: beforeAsync,
							onAsyncSuccess: onAsyncSuccess,
							onAsyncError: onAsyncError,
							onClick:onClick
						} 
					};
				$.fn.zTree.init($("#multitree"), setting);//加载导航树 
		}
		//点击事件
			function onClick(event, treeId, treeNode, clickFlag){
			var zTree = $.fn.zTree.getZTreeObj("multitree"); 
					if(treeNode.isParent){
						if(treeNode.open){
							zTree.expandNode(treeNode, false, null, null, true);
						}else{ 
							zTree.expandNode(treeNode, true, null, null, true);
						}
					}else{
						var ishaving=false;
			 			for(var i=0;i<arr.length;i++){
			 				if(arr[i].id==treeNode.id){
			 					ishaving = true;
			 					break;
			 				}
			 			}
			 			if(!ishaving){
			 				arr.push(treeNode); 
			 			}
			 			setSelectNode();
					}
			}
			//添加选择节点对象
		function setSelectNode(){
			var str = ""; 
			for(var i=0;i<arr.length;i++){
 				str+="<div class='selItem' id='"+arr[i].id+"'>"+arr[i].name+"<img src='iwork_img/close.gif' style='float:right' border='0'/></div>";
 			}
			$('#selectinfo').html(str); 
 			$("#selectinfo div").bind("click",function(){
 				//移除数组对象
 				for(var i=0;i<arr.length;i++){
 					if($(this).attr("id")==arr[i].id){
 						arr.splice($.inArray(arr[i],arr),1);
 						break; 
 					}
 				}
 				//移除选中显示
 				$(this).remove();
 			});
		}
			//全部展开
		var demoMsg = {
				async:"正在进行异步加载，请等一会儿再点击...",
				expandAllOver: "全部展开完毕",
				asyncAllOver: "后台异步加载完毕",
				asyncAll: "已经异步加载完毕，不再重新加载",
				expandAll: "已经异步加载完毕，使用 expandAll 方法"
		}
	function beforeAsync() {
			curAsyncCount++;
		}
		function onAsyncSuccess(event, treeId, treeNode, msg) {
			curAsyncCount--;
			if (curStatus == "expand") {
				expandNodes(treeNode.children);
			} else if (curStatus == "async") {
				asyncNodes(treeNode.children);
			}
			if (curAsyncCount <= 0) {
				if (curStatus != "init" && curStatus != "") {
					$("#demoMsg").text((curStatus == "expand") ? demoMsg.expandAllOver : demoMsg.asyncAllOver);
					asyncForAll = true;
				}
				curStatus = "";
			}
		}
		function onAsyncError(event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {
			curAsyncCount--;
			if (curAsyncCount <= 0) {
				curStatus = "";
				if (treeNode!=null) asyncForAll = true;
			}
		}
		
		var curStatus = "init", curAsyncCount = 0, asyncForAll = false,
		goAsync = false;
		function expandAll() {
			var zTree = $.fn.zTree.getZTreeObj("multitree");
			if (asyncForAll) {
				$("#demoMsg").text(demoMsg.expandAll);
				zTree.expandAll(true);
			} else {
				expandNodes(zTree.getNodes());
				if (!goAsync) {
					$("#demoMsg").text(demoMsg.expandAll);
					curStatus = "";
				}
			}
		}
		//全部折叠
		function unExpandAll() {
			var zTree = $.fn.zTree.getZTreeObj("multitree");
			zTree.expandAll(false);
		}
		function expandNodes(nodes) {
			if (!nodes) return;
			curStatus = "expand";
			var zTree = $.fn.zTree.getZTreeObj("multitree");
			for (var i=0, l=nodes.length; i<l; i++) {
				zTree.expandNode(nodes[i], true, false, false);
				if (nodes[i].isParent && nodes[i].zAsync) {
					expandNodes(nodes[i].children);
				} else {
					goAsync = true;
				}
			}
		}

		function asyncAll() {
			var zTree = $.fn.zTree.getZTreeObj("multitree");
			if (asyncForAll) {
				$("#demoMsg").text(demoMsg.asyncAll);
			} else {
				asyncNodes(zTree.getNodes());
				if (!goAsync) {
					$("#demoMsg").text(demoMsg.asyncAll);
					curStatus = "";
				}
			}
		}
		function asyncNodes(nodes) {
			if (!nodes) return;
			curStatus = "async";
			var zTree = $.fn.zTree.getZTreeObj("multitree");
			for (var i=0, l=nodes.length; i<l; i++) {
				if (nodes[i].isParent && nodes[i].zAsync) {
					asyncNodes(nodes[i].children);
				} else {
					goAsync = true;
					zTree.reAsyncChildNodes(nodes[i], "refresh", true);
				}
			}
		}  
		function dosearch(){
			var searchOrg = $("#searchOrg").val();
			if(searchOrg==""){
				alert("请输入查询条件");
				return; 
			}
			var url = "multibook_search.action?parentDept=&currentDept=&startDept=&searchOrg="+encodeURI(searchOrg);
			var setting = { 
					check: {
							enable: false
					},
					async: {
						enable: true, 
						url:url, 
						dataType:"json",
						autoParam:["id","nodeType","companyid"] 
					}, 
					callback: { 
					beforeAsync: beforeAsync,
						onAsyncSuccess: onAsyncSuccess,
						onAsyncError: onAsyncError,
						onClick:onClick 
					} 
				};
			$.fn.zTree.init($("#multitree"), setting);//加载导航树 
		}
		//查询回车事件

	    function enterKey(){
			if (window.event.keyCode==13){
				 dosearch();
				return; 
			}
		} 
		function empty(){
			arr = [];
			setSelectNode();
			$("#searchOrg").val("");
			initTree();
		}
		
		function sendSigns(){
			var userlist=new Array();
			var actDefId = $("#actDefId").val();
			var prcDefId = $("#prcDefId").val();
			var actStepDefId = $("#actStepDefId").val();
			var taskId = $("#taskId").val();
			var instanceId = $("#instanceId").val();
			var excutionId = $("#excutionId").val();
			for(var i=0;i<arr.length;i++){
 					userlist.push(arr[i].id);
 			}
 			var b = userlist.join(",");
 			$("#userlist").val(b);
 			var obj = $('#editForm').serialize();
			$.post('processRuntimeSigns_send.action',obj,function(data){
				if(data=='success'){ 
					$.post("sx_zqb_hqdx.action?userid="+b+"&actDefId="+actDefId+"&instanceid="+instanceId,function(data){});
					_close();  
				}else{
					alert("发送会签异常，不允许重复给同一用户发送会签！");
				}
			});
		}
		function _close(){
			api.close();
		}
	</script>
	<style>
		.selItem{
			font-size:14px;
			padding:5px;
			padding-top:10px;
			padding-bottom:10px;
			background-color:#fff;
			border:1px solid #F5DA81;
			margin-top:5px;
			margin-right:4px;
			padding-left:22px;
			color:#424242;
			cursor:pointer; 
			background-image: url(../../../iwork_img/user_edit.png);
			background-repeat:no-repeat;
			background-position:3px ;
		}
		.selItem:hover{
			color:#0101DF;
			cursor:pointer; 
			background-color:#efefef;
			background-image: url(../../../iwork_img/user_edit.png);
			background-repeat:no-repeat;
			background-position:3px ;
		}
		.selList{
			vertical-align:bottom;
			width:200px;
			border-top:1px solid #efefef;
			color:#0000FF;
			font-size:12px;
			padding-top:10px;
			padding-left:10px;
			background:#FBFBEF;
			border-left:1px solid #efefef;
		}
	</style> 
</head>
<body class="easyui-layout">
	<div region="north" style="border-bottom:1px solid #efefef;padding-top:10px;" border="false" >
		<div class="tools_nav">
			<input type="text"  onclick="this.value=''"  onKeyDown="enterKey();if(window.event.keyCode==13) return false;"  style="width:160px;" name="searchOrg" id="searchOrg"/><input type="button" onclick="dosearch()" value="查询"/><input type="button" onclick="empty()" value="清空选择"/>
		</div>	
		<div nowrap style="padding: 5px;text-align:center">
		</div>
	</div>
    <div region="center" border="false"  align="center" style="border:0px solid #C0C0C0;">
    <div style="text-align:left;padding:3px;"><a href="javascript:expandAll();">全部展开</a>/<a href="javascript:unExpandAll();">全部收起</a></div>
	    	<div>
	         <ul id="multitree" class="ztree"></ul>
	        </div>
    </div>
      
    <div region="south" border="false" style="border-top:1px solid #efefef;" >
 <form id="editForm" name="editForm" method="post">
	         <s:hidden id ="actDefId" name="actDefId"/>
			<s:hidden  id ="prcDefId" name="prcDefId"/>
			<s:hidden id ="actStepDefId" name="actStepDefId"/>
			<s:hidden id ="taskId" name="taskId"/>
			<s:hidden id ="instanceId" name="instanceId"/>
			<s:hidden id ="excutionId" name="excutionId"/>
			<s:hidden id ="userlist" name="userlist"/>
			<s:hidden id ="fixed" name="fixed"/>
    <s:if test="remindTypeList!=null">
      	<table width="100%">
					<tr>
						<td class="ItemTitle">提醒方式:	</td><td  class="pageInfo">
							 <s:checkboxlist name="remindType" id="remindType" list="remindTypeList" 
					         labelposition="top"
					         listKey="key"
					         listValue="value" 
					         value="remindTypeList"
					         >
					        </s:checkboxlist>
						</td>
					</tr>
					</table>
	</s:if>
	<input type="hidden" id="userlist" name="userlist"/>
	 </form>
	<div style="text-align:right;vertical-align:bottom;height:60px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;" >
    	<a href="###" onclick="sendSigns()" class="easyui-linkbutton" plain="false" iconCls="icon-ok">发送会签</a>	
			<a href="javascript:api.close();" class="easyui-linkbutton" plain="false" iconCls="icon-cancel">关闭</a>
	</div>
	</div>
	<div region="east" class="selList" border="false" >
		已选择会签人：<span id="selectinfo"></span> 
	</div>
</body>
</html>
