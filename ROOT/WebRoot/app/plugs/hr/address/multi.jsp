<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html> 
<head> 
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>地址簿</title>
	 
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"/>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>	 
	<script type="text/javascript">
	var api = frameElement.api, W = api.opener;	
		//加载导航树 
		var arr=new Array();
		var userIdObj = new Array();
		var userNameObj = new Array();
		var deptIdObj  = new Array();
		var deptNameObj  = new Array();
		var def_fieldObj = new Array();
		
		$(function(){ 
			initTree();
			//initSelectNode();
		});
		/*
		function initSelectNode(){
			var nodes = <s:property value="selectJSON" escapeHtml="false"/>;
			var str = "";
			for(var i=0;i<nodes.length;i++){
			 	arr.push(nodes[i]);
			}
			setSelectNode();
		}*/
		function initTree(){
			var setting = {
						check: {
							enable: false
					    },
					    view: {
							selectedMulti: false
						},
						async: {
							enable: true, 
							url:"hrDataAddressBook_showjson.action", 
							dataType:"json",
							autoParam:["id","nodeType"]
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
			 				if(arr[i]==treeNode){
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
 				str+="<span class='selItem' id='"+arr[i].id+"'>"+arr[i].name+"<img src='iwork_img/close.gif' border='0'/></span>";
 			}
			$('#selectinfo').html(str); 
 			$("#selectinfo span").bind("click",function(){
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
			function insertData(){
				var targetUserId = $("#editForm_targetUserId").val();
				var targetUserName = $("#editForm_targetUserName").val();
				var defaultField = $("#editForm_defaultField").val(); 
				//============获取列表============================
				//var zTree = $.fn.zTree.getZTreeObj("multitree");
	 			var nodes = arr;
	 			for(var i=0;i<nodes.length;i++){
	 				if(nodes[i].nodeType=='org'){
	 					continue;
	 				}
	 				pushData(userIdObj,targetUserId,nodes[i].id);
	 				pushData(userNameObj,targetUserName,nodes[i].name);
	 				pushData(def_fieldObj,defaultField,nodes[i].name);
	 			}
	 			rebackData(targetUserId,userIdObj);
	 			rebackData(targetUserName,userNameObj);
	 			rebackData(defaultField,userNameObj);
				api.close();
			}
			//装载数据对象
			function pushData(listData,inputName,value){
				if(inputName!=""){
						
						var v =W.document.getElementById(inputName);
						if(v!=null){
							listData.push(value); 
						}else if(api.get(api.data.dialogName)!=null){
							v = api.get(api.data.dialogName).document.getElementById(inputName);
							listData.push(value);
						} 
				} 
			}
			
			//装载数据对象
			function rebackData(inputName,listData){
				if(inputName!=""){
					var v  = W.document.getElementById(inputName);
					if(v!=null){
						v.value = userNameObj.join(",");
					}else if(api.get(api.data.dialogName)!=null){
						v = api.get(api.data.dialogName).document.getElementById(inputName);
					}
					if(v!=null){
							v.value = listData.join(",");
					}
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
			$("#tips").text("数据量较大，请耐心等待...");
			var url = "";
			var url = "hrDataAddressBook_searchjson.action?searchOrg="+searchOrg
			var setting = { 
					check: {
							enable: false
					},
					async: {
						enable: true, 
						url:url, 
						dataType:"json",
						autoParam:["id","nodeType"] 
					}, 
					callback: { 
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
		}
	</script>
	<style>
		.selItem{
			font-size:10px;
			padding:2px;
			background-color:#efefef;
			border:1px solid #efefef;
			margin-top:3px;
			margin-right:4px;
			cursor:pointer; 
		}
	</style> 
</head>
<body class="easyui-layout">
	<div region="north" style="border-bottom:1px solid #efefef" border="false" >
		<div class="tools_nav">
			<a href="#" onclick="insertData()" class="easyui-linkbutton" plain="true" iconCls="icon-ok">确认</a>	
			<a href="javascript:api.close();" class="easyui-linkbutton" plain="true" iconCls="icon-cancel">关闭</a>
			<input type="text"  onclick="this.value=''"  onKeyDown="enterKey();if(window.event.keyCode==13) return false;"  style="width:60px;" name="searchOrg" id="searchOrg"/><input type="button" onclick="dosearch()" value="查询"/><input type="button" onclick="empty()" value="清空选择"/>
			<span   id="tips"  style="color:red"></span>
		</div>	
		<div nowrap style="padding: 5px;text-align:center;color:red">
			
		</div>
	</div>
    <div region="center" border="false"  align="center" style="border:0px solid #C0C0C0;">
    <div style="text-align:left;padding:3px;"><a href="javascript:expandAll();">全部展开</a>/<a href="javascript:unExpandAll();">全部收起</a></div>
	    <s:form name="editForm" id="editForm" theme="simple">
	    	<div>
	         <ul id="multitree" class="ztree"></ul>
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
	    </s:form> 
    </div>
    <div region="south" style="vertical-align:bottom;height:60px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;" border="false" >
		已选择：<span id="selectinfo"></span>
	</div>
</body>
</html>
<s:if test="input!=null&&''!=input">
	<script type="text/javascript">
		//setTimeout("expandAll()",500);
	</script>
</s:if>
