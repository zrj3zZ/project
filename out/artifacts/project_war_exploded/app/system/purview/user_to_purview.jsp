<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>IWORK综合应用管理系统</title> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link href="iwork_css/global_frame.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
 //==========================装载快捷键===============================//快捷键
				$(document).ready(function(){
			var setting2 = {
					check: {
						enable: true
					},
					view: { 
						selectedMulti: false
					},
					async: {
						enable: true, 
						url:'openUnUserPurviewList.action?userid=<s:property value="userid"/>',
						dataType:"json", 
						autoParam:["id","type"]
					},callback: { 
						onClick:onPurviewClick,
						onCheck:onCheck
					}
				};
			$.fn.zTree.init($("#purviewListTree"), setting2);
		});
		
		function filter(treeId, parentNode, childNodes) {
				if (!childNodes) return null;
				for (var i=0, l=childNodes.length; i<l; i++) {
					childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
				}
				return childNodes;
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
 		function onClick(event, treeId, treeNode, clickFlag){
 			var zTree = $.fn.zTree.getZTreeObj("purviewTree");
 			var type = treeNode.nodeType;
	 		if(type=='dept'){
	 				$("#userid").val("");
	 			zTree.expandNode(treeNode, true, null, null, true);
	 		}else{
	 			var url = "openUnUserPurviewList.action?userid="+treeNode.id;
				var setting2 = {
					check: {
						enable: true
					},
					view: { 
						selectedMulti: false
					},
					async: {
						enable: true, 
						url:url,
						dataType:"json"
					},callback: { 
						onClick:onPurviewClick,
						onCheck:onCheck
					}
				};
				$("#userid").val(treeNode.id);
				$.fn.zTree.init($("#purviewListTree"), setting2); 
	 		}
 		}
		var curStatus = "init", curAsyncCount = 0, asyncForAll = false,
		goAsync = false;
		//全部展开
		function expandAll() {
			if (!check()) { 
				return;
			}
			var zTree = $.fn.zTree.getZTreeObj("purviewTree");
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
			var zTree = $.fn.zTree.getZTreeObj("purviewTree");
			zTree.expandAll(false);
		}
		function expandNodes(nodes) {
			if (!nodes) return;
			curStatus = "expand";
			var zTree = $.fn.zTree.getZTreeObj("purviewTree");
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
			if (!check()) {
				return;
			}
			var zTree = $.fn.zTree.getZTreeObj("purviewTree");
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
			var zTree = $.fn.zTree.getZTreeObj("purviewTree");
			for (var i=0, l=nodes.length; i<l; i++) {
				if (nodes[i].isParent && nodes[i].zAsync) {
					asyncNodes(nodes[i].children);
				} else {
					goAsync = true;
					zTree.reAsyncChildNodes(nodes[i], "refresh", true);
				}
			}
		}
		function check() {
			if (curAsyncCount > 0) {
				$("#demoMsg").text(demoMsg.async);
				return false;
			}
			return true; 
		}
		
		function onCheck(event, treeId, treeNode) {
			
		
		}
		//点击权限组树
		function onPurviewClick(event, treeId, treeNode,clickFlag) {
		var zTree = $.fn.zTree.getZTreeObj("purviewListTree");
			var type = treeNode.type;
			if(type=='category'){
				zTree.expandNode(treeNode, true, null, null, true);
			}else{
				if(!treeNode.checked){
	 				zTree.checkNode(treeNode, true, true, clickFlag);
		 		}else{
	 				zTree.checkNode(treeNode, false, true, false);
		 		}
			}
			
	 		
		}
	
				//设置权限
				function setpurview(){
					if($("#userid").val()==""){
		 				art.dialog.tips('请选择您要授权的对象');
		 				return;
		 			}
					var zTree = $.fn.zTree.getZTreeObj("purviewListTree");
		 			var nodes = zTree.getCheckedNodes(true);
		 			var str = ""; 
		 			for(var i=0;i<nodes.length;i++){
		 				var type = nodes[i].type;
		 				if(type=='category')continue;
		 				var tmp = nodes[i].id;
		 				if(i<nodes.length-1){ 
		 					tmp+=","; 
		 				}
		 				str+=tmp;
		 			}
		 			
		 			$("#purviewIds").val(str);
		 			
					$.post('openPurviewUser_setUserlist.action',$("#editForm").serialize(),function(data){
				    	if(data=='success'){
				    		art.dialog.tips("授权成功");
				    	}else{
				    		art.dialog.tips("授权异常,请稍后再试");
				    	}
				  }); 
				}
	</script>
</head>
<body class="easyui-layout">
<!-- TOP区 -->
	<div region="north" border="false" style="height:40px;border-bottom:1px solid #ccc;">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" >
	      <tr>
          	<td style="text-align:right;padding:0px;padding-right:10px;padding-bottom:2px;">
          		<a href='javascript:setpurview();' class="easyui-linkbutton" iconCls="icon-process-purview" plain="false">授权</a>
				<a href='javascript:this.location.reload();' class="easyui-linkbutton" iconCls="icon-reload" plain="false">刷新</a>
          	</td>
           </tr>
	    </table>
		 </div>
    <!-- 导航区 -->
	<div region="center" style="padding:3px;border:0px;">
	<table style="height:100%;" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td  style="vertical-align:top">
							<ul id="purviewListTree" class="ztree"></ul>
			</td>
		</tr>
	</table>
	<s:form name="editForm" id="editForm" theme="simple">
		 <s:hidden name="purviewIds" id="purviewIds" />
		 <s:hidden name="userid" id="userid"/>
</s:form>
	</div>
	
</body>
</html>
