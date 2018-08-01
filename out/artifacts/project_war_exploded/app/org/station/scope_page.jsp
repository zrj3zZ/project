﻿<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html> 
<head> 
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>部门地址簿</title>
	 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css"/> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/addressbook/radioaddressbookaction.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script> 
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
	var api = art.dialog.open.api, W = api.opener;
		var arr=new Array(); 
		$(function(){
			initTree();
		}); 
		function initTree(){
			var setting = {
						async: {
							enable: true, 
							url:"org_station_scope_json.action?&insId=<s:property value='insId' escapeHtml='false'/>", 
							dataType:"json",
							autoParam:["id","nodeType"] 
						},
						check: {
							enable: true,
							chkboxType:{ "Y" : "", "N" : "" }
						}, 
					    view: {
							selectedMulti: true
						},
						callback: {
							beforeAsync: beforeAsync, 
							onAsyncSuccess: onAsyncSuccess, 
							onAsyncError: onAsyncError,
							onClick:onClick
						} 
					};
				$.fn.zTree.init($("#radiotree"), setting);//加载导航树 
		}
		//点击事件
			function onClick(event, treeId, treeNode, clickFlag){
				var zTree = $.fn.zTree.getZTreeObj("radiotree");
				if(!treeNode.checked){
	 				zTree.checkNode(treeNode, true, true, clickFlag);
		 		}else{
	 				zTree.checkNode(treeNode, false, true, false);
		 		}
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
			var zTree = $.fn.zTree.getZTreeObj("radiotree");
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
			var zTree = $.fn.zTree.getZTreeObj("radiotree");
			zTree.expandAll(false);
		}
		function expandNodes(nodes) {
			if (!nodes) return;
			curStatus = "expand";
			var zTree = $.fn.zTree.getZTreeObj("radiotree");
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
			var zTree = $.fn.zTree.getZTreeObj("radiotree");
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
			var zTree = $.fn.zTree.getZTreeObj("radiotree");
			for (var i=0, l=nodes.length; i<l; i++) {
				if (nodes[i].isParent && nodes[i].zAsync) {
					asyncNodes(nodes[i].children);
				} else {
					goAsync = true;
					zTree.reAsyncChildNodes(nodes[i], "refresh", true);
				}
			}
		} 
		//查询回车事件

	    function enterKey(){
			if (window.event.keyCode==13){
				 dosearch();
				return;
			}
		}
		
		function doSubmit(){
			var zTree = $.fn.zTree.getZTreeObj("radiotree");
				var nodes = zTree.getCheckedNodes(true);
	 			var str = "";  
	 			var companylist=new Array();
	 			var deptlist=new Array();
	 			for(var i=0;i<nodes.length;i++){
	 				var tmp = nodes[i].id; 
	 				var name = nodes[i].name;
	 				var type = nodes[i].nodeType; 
	 				var chkDisabled = nodes[i].chkDisabled;  
	 				if(type=='dept'){
	 					deptlist.push(tmp);
	 				}
	 			}
					if(companylist==''&&deptlist==''){ 
						art.dialog.tips("请选择部门");
						return;
					}
					var company = document.getElementById("companylist");
					var dept = document.getElementById("deptlist");
					company.value =  companylist.join(",");; 
					dept.value = deptlist.join(","); 
					var obj = $('#editForm').serialize(); 
				     $.post("org_station_scope_save.action",obj,function(data){
				           if(data!=""){
			                	art.dialog.tips("岗位授权管理范围成功",3);
			            	}
				     });
		}
		
		function cancel(){
			api.close();
		}
	</script> 
</head>
<body class="easyui-layout">
	<div region="north"border="false" >
		       <div class="tools_nav" >
		       	 <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:doSubmit();" >
				                    确定</a> <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:cancel()">关闭</a> 
		       </div>
				
	 </div>
    <div region="center" border="false"  align="center" style="border:0px solid #C0C0C0;">
    <div style="text-align:left;padding:3px;"><a href="javascript:expandAll();">全部展开</a>/<a href="javascript:unExpandAll();">全部收起</a></div>
	    <s:form name="editForm" id="editForm" theme="simple">
	    	<div>
	         <ul id="radiotree" class="ztree"></ul>
	        </div>
	    	<s:hidden id="stationId" name="stationId"></s:hidden>
	    	<s:hidden id="insId" name="insId"></s:hidden>
	    	<s:hidden id="companylist" name="companylist"></s:hidden>
	    	<s:hidden id="deptlist" name="deptlist"></s:hidden> 
	    </s:form>
    </div>
    <div region="south"border="false" title="已授权部门" style="height:100px;color:#0000ff;padding:5px;">
		<s:property  value="stationHtml" escapeHtml="false"/>		
	</div>
</body>
</html>
