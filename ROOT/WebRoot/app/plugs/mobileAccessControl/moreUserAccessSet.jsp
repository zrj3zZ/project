<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>移动端权限管理</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script>     		
	<script type="text/javascript"> 
		
		//加载导航树 
		var api = frameElement.api, W = api.opener;
		function initTree(){
			var setting = {
						async: {
							enable: true, 
							url:"radiobook_orgjson.action?parentDept=&currentDept=&startDept=", 
							dataType:"json",
							autoParam:["id","nodeType"] 
						},
						check: {
							enable: true
						},
						callback: {
							beforeAsync: beforeAsync,
							onAsyncSuccess: onAsyncSuccess,
							onAsyncError: onAsyncError,
							onClick:onClick
						} 
					};
			$.fn.zTree.init($("#tree"), setting);//加载导航树 
		}
		//点击事件
		function onClick(event, treeId, treeNode, clickFlag){
				if(treeNode.isParent){
				var zTree = $.fn.zTree.getZTreeObj("tree"); 
					if(treeNode.open){
						zTree.expandNode(treeNode, false, null, null, true);
					}else{
						zTree.expandNode(treeNode, true, null, null, true);
					}
				}else{
					var treeObj = $.fn.zTree.getZTreeObj("tree");
					var nodes = treeObj.getNodesByParamFuzzy("userId", treeNode.userId, null);
					if (nodes.length > 0) {
						var userChecked = nodes[0].checked;
						if(userChecked == "true"){
							treeObj.checkNode(nodes[0], false, true);
						}else{
							treeObj.checkNode(nodes[0], true, true);
						}
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
			var zTree = $.fn.zTree.getZTreeObj("tree");
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
			var zTree = $.fn.zTree.getZTreeObj("tree");
			zTree.expandAll(false);
		}
		
		function expandNodes(nodes) {
			if (!nodes) return;
			curStatus = "expand";
			var zTree = $.fn.zTree.getZTreeObj("tree");
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
			var zTree = $.fn.zTree.getZTreeObj("tree");
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
			var zTree = $.fn.zTree.getZTreeObj("tree");
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
			var url = "radiobook_search.action?parentDept=&currentDept=&startDept=&searchOrg="+searchOrg
			var setting = {
					async: {
						enable: true, 
						url:url, 
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
			$.fn.zTree.init($("#tree"), setting);//加载导航树 
		}
		//查询回车事件

	    function enterKey(){
			if (window.event.keyCode==13){
				 dosearch();
				return;
			}
		}

		function clearCheckedOldNodes() {
			var zTree = $.fn.zTree.getZTreeObj("tree"),
			nodes = zTree.getChangeCheckedNodes();
			for (var i=0, l=nodes.length; i<l; i++) {
				nodes[i].checkedOld = nodes[i].checked;
			}
		}
		
		function submitMoreUserAccessSet(){
		
			var zTree = $.fn.zTree.getZTreeObj("tree");
			var checkItem = zTree.getCheckedNodes(true);
			var userIds = "";
			var flag = 0;
			for (var i = 0; i< checkItem.length; i++) {
				var itemUserId = checkItem[i].userId;
				if(typeof(itemUserId) == "string" && itemUserId.length > 3){
					if(flag == 0){
						userIds = itemUserId;
					}else{
						userIds = userIds + "," + itemUserId;
					}
					flag ++;
				}
			
			}
			
			var isVisit = $('input:radio[name=isVisit]:checked').val();
			var visitType = $('input:radio[name=visitType]:checked').val();
			var isBind = $('input:radio[name=isBind]:checked').val();
			if($.trim(userIds).length > 0){
				$.post("moreUserAccessSubmit.action",
					{userIds:userIds, isVisit:isVisit, visitType:visitType, isBind:isBind},
					function(data){
						alert(data);
						cancel();
				});
			}else{
				alert("请选择要更新的人员!!");
			}
			
		}
	
		function openOneItem(userId){

			var treeObj = $.fn.zTree.getZTreeObj("tree");
			var nodes = treeObj.getNodesByParamFuzzy("userId", userId, null);
			if (nodes.length > 0) {
				var userChecked = nodes[0].checked;
				if(userChecked == "true"){
					treeObj.checkNode(nodes[0], false, true);
				}else{
					treeObj.checkNode(nodes[0], true, true);
				}
			}
		}
		
		function cancel(){
			api.close();
		} 
		
	</script>
	<style type="text/css">
		* {
			margin:0px;
			padding:0px;
			font-size:12px;
		}
		img {
			border: 0 none;
		}
		.tools_nav {
			background:url(iwork_img/engine/tools_nav_bg.jpg) repeat-x;
			height:34px;
			line-height:34px;
			padding-left:20px;
		}
		.search_btn {
			background:url(iwork_img/engine/search_btn.png) no-repeat;
			height:26px;
			width:189px;
			cursor:pointer; 
			white-space:nowrap;
			margin-right:10px;
			float:left;
			margin-top:3px;
		}
		.search_btn_onclick {
			background:url(iwork_img/engine/search_btn_onclick.png) no-repeat;
			height:26px;
			width:189px;
			cursor:pointer;
			margin-right:10px;
			float:right;
			margin-top:3px; 
		}
		.search_input {
			background:none;
			border:0 none;
			cursor:pointer;
			width:140px;
			height:20px;
			line-height:20px;
			margin-left:5px;
			margin-top:2px;
		}
		
		.search_button {
			background:none;
			border:0 none;
			width:40px;
			height:20px;
			cursor:pointer
		}
		.view {
			background:url(iwork_img/engine/view1.png);
			display:block;
			height:26px;
			width:61px;
			margin-right:10px;
			margin-top:3px;
			float:right;
			cursor:pointer
		}
		.view2 {
			background:url(iwork_img/engine/view2.png);
			display:block;
			height:26px;
			width:61px;
			margin-right:10px;
			margin-top:3px;
			float:right;
			cursor:pointer
		}
		.button1 {
			background:url(iwork_img/engine/button1_1.png) no-repeat;
			width:70px;
			height:27px;
			cursor:pointer;
			display:block;
			float:right;
			line-height:27px;
			margin-right:5px;
			padding-left:30px;
		}
		.button1_hover {
			background:url(iwork_img/engine/button1_2.png) no-repeat;
			width:70px;
			height:27px;
			cursor:pointer;
			display:block;
			float:right;
			line-height:27px;
			margin-right:5px;
			padding-left:30px;
		}
		.button1_click {
			background:url(iwork_img/engine/button1_3.png) no-repeat;
			width:70px;
			height:27px;
			cursor:pointer;
			display:block;
			float:right;
			line-height:27px;
			margin-right:5px;
			padding-left:30px;
		}
		.button2 {
			background:url(iwork_img/engine/button2_1.png) no-repeat;
			width:70px;
			height:27px;
			cursor:pointer;
			display:block;
			float:right;
			line-height:27px;
			margin-right:5px;
			padding-left:30px;
		}
		.button2_hover {
			background:url(iwork_img/engine/button2_2.png) no-repeat;
			width:70px;
			height:27px;
			cursor:pointer;
			display:block;
			float:right;
			line-height:27px;
			margin-right:5px;
			padding-left:30px;
		}
		.button2_click {
			background:url(iwork_img/engine/button2_3.png) no-repeat;
			width:70px;
			height:27px;
			cursor:pointer;
			display:block;
			float:right;
			line-height:27px;
			margin-right:5px;
			padding-left:30px;
		}
		.del {
			background:url(iwork_img/engine/bin_del.png) no-repeat;
			width:16px;
			height:16px;
			cursor:pointer;
			display:block;
			float:right;
			margin-right:5px;
			margin-top:15px;
			margin-bottom:5px;
		}
		.data {
			width:80px;
			height:16px;
			cursor:pointer;
			display:block;
			float:right;
			margin-right:5px;
			margin-top:15px;
			margin-bottom:5px;
		}
		/*树形结构样式*/
		.tree_left {
			height:500px;
			float:left;
			position:absolute;
			left:5px;
			top:36px;
			width:227px;
			color:#5c5c5c;
		}
		.tree_top {
			background:url(iwork_img/engine/tree_top_bg.jpg) no-repeat;
			height:15px;
			line-height:26px;
			padding-left:10px;
			padding-top:0px;
			font-weight:bold;
			width:217px;
		}
		.tree_top_icon {
			background:url(iwork_img/engine/tree_title.png) no-repeat;
			display:block;
			height:16px;
			padding-left:20px;
		}
		.tree_main {
			
			padding:0px 0px 0px 0px;
			text-align:left;
			vertical-align:top;
			background:url(iwork_img/engine/tree_bg.jpg);
		}
		.tree_bottom {
			background:url(iwork_img/engine/tree_bottom_bg.jpg);
			height:14px;
		}
		/*存储管理样式*/
		.right {
			margin-top:3px;
			margin-left:235px;
			color:#5c5c5c;
		}
		.right_frame {
		}
		.right_left {
			background:url(iwork_img/engine/right_left.jpg) no-repeat;
			width:7px;
			height:136px;
			float:left;
		}
		.right_center {
			background:url(iwork_img/engine/right_center.jpg);
			height:136px;
			width:100%;
		}
		.right_right {
			background:url(iwork_img/engine/right_right.jpg) no-repeat;
			width:8px;
			height:136px;
		}
		.right_left_click {
			background:url(iwork_img/engine/right_left_click.jpg) no-repeat;
			width:7px;
			height:136px;
			float:left;
			color:#0381ce;
		}
		.right_center_click {
			background:url(iwork_img/engine/right_center_click.jpg) repeat-x;
			height:136px;
			color:#0381ce;
		}
		.right_right_click {
			background:url(iwork_img/engine/right_right_click.jpg) no-repeat;
			width:8px;
			height:136px;
		}
		
	</style>
</head>
<body class="easyui-layout">
    <!-- 导航区 -->
	<div region="west"  split="false" border="false"  style="height:100%;width:230px;padding-left:5px;overflow:auto; border-top:1px solid #F9FAFD">
				<table style="height:100%;" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="tree_top"></td>
					</tr>
					<tr>
						<td class="tree_main">
							<ul id="tree" class="ztree"></ul>
						</td>
					</tr>
					<tr>
						<td class="tree_bottom"></td>
					</tr>
					
					
				</table>
    </div>
	<div region="center" style="padding:3px;border:0px;">
			<table style="height:40%;" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						是否允许登陆:
					</td>
					<td>
						<label><input type="radio" name="isVisit" value="1"/> 是</label>
						<label><input type="radio" name="isVisit" value="0" checked="checked"/> 否</label>
					</td>
				</tr>
				<tr>
					<td>
						登录方式:
					</td>
					<td>
						<label><input type="radio" name="visitType" value="common" checked="checked"/> 普通登陆</label>
						<label><input type="radio" name="visitType" value="anmeng"/> 令牌登陆</label>
					</td>
				</tr>
				<tr>
					<td>
						是否绑定设备:
					</td>
					<td>
						<label><input type="radio" name="isBind" value="1"/> 是</label>
						<label><input type="radio" name="isBind" value="0" checked="checked"/> 否</label>
					</td>
				</tr>
			</table>
			<div style="padding:5px;text-align:right;">
			<a href="javascript:submitMoreUserAccessSet();" class="easyui-linkbutton"  iconCls="icon-ok">确定</a>
			<a href="javascript:cancel();" class="easyui-linkbutton" iconCls="icon-cancel">取消</a>
			</div>
	</div>
	<script type="text/javascript">
		initTree();
	</script>
</body>
</html>