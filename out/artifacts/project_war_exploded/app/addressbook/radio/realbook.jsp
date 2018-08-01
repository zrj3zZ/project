<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html> 
<head> 
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>授权发起地址簿</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"/>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/addressbook/radioaddressbookaction.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	 
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script> 
	<script type="text/javascript">
		//加载导航树 
		var api = art.dialog.open.api, W = api.opener;
		$(function(){
			initTree();
		});
		function initTree(){
			var setting = {
						async: {
							enable: true, 
							url:"real_book_json.action?actDefId="+parent.$("#actDefId").val(), 
							dataType:"json"
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
					if(treeNode.isParent){
					var zTree = $.fn.zTree.getZTreeObj("radiotree"); 
						if(treeNode.open){
							zTree.expandNode(treeNode, false, null, null, true);
						}else{
							zTree.expandNode(treeNode, true, null, null, true);
						}
					}else{
						insertData(treeNode);
					}
			}
			function insertData(treeNode){
				var targetUserId = $("#editForm_targetUserId").val();
				var targetUserNo = $("#editForm_targetUserNo").val();
				var targetUserName = $("#editForm_targetUserName").val();
				var targetDeptId = $("#editForm_targetDeptId").val();
				var targetDeptName = $("#editForm_targetDeptName").val();
				var defaultField = $("#editForm_defaultField").val();
				 
				rebackData(targetUserId,treeNode.userId);
				rebackData(targetUserNo,treeNode.userno);
				rebackData(targetUserName,treeNode.userName);
				rebackData(targetDeptId,treeNode.deptId);
				rebackData(targetDeptName,treeNode.deptname);
				if(targetUserId!=defaultField&&targetUserNo!=defaultField&&targetUserName!=defaultField&&targetDeptId!=defaultField&&targetDeptName!=defaultField){
					rebackData(defaultField,treeNode.useraddress);
				}
				//回调父页面方法
				try{
					parent.selectRadioAddress(defaultField,treeNode);
				}catch(e){} 
				//关闭窗口
				api.close();
			}
			//装载数据对象
			function rebackData(inputName,value){
				if(inputName!=""){
					try{
					var origin = artDialog.open.origin; 
					var input = origin.document.getElementById(inputName);
					input.value = value;
					}catch(e){}
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
		
	</script> 
</head>
<body class="easyui-layout">
    <div region="center" border="false"  align="center" style="border:0px solid #C0C0C0;padding-top:30px">
	    <s:form name="editForm" id="editForm" theme="simple">
	    	<div>
	         <ul id="radiotree" class="ztree"></ul>
	        </div>
	    	<s:hidden name="currentDept"></s:hidden>
	    	<s:hidden name="parentDept"></s:hidden>
	    	<s:hidden name="parentDept"></s:hidden>
	    	<s:hidden name="targetUserId"></s:hidden>
	    	<s:hidden name="targetUserNo"></s:hidden>
	    	<s:hidden name="targetUserName"></s:hidden>
	    	<s:hidden name="targetDeptId"></s:hidden>
	    	<s:hidden name="targetDeptName"></s:hidden>
	    	<s:hidden name="defaultField"></s:hidden> 
	    	<s:hidden name="actDefId" id="actDefId"></s:hidden> 
	    </s:form>
    </div>
    <div region="south" style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;" border="false" >
		已选择：<s:property value="input"/> 
	</div>
</body>
</html>
