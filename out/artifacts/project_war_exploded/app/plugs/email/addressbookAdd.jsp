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
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>	
	<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js" ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style>
		.topHead{
			background:#fff;
			border-bottom:3px solid #677e9b;
		}
		.title{
			background:#f2f4f6;
			border-bottom:1px solid #c6c9ca;
			border-right:1px solid #c6c9ca;
			padding:2px;
			padding-left:5px;
			font-family:微软雅黑;
		}
		.title_icon{
			background:url(iwork_img/email/mail227195.png) 112px -18px no-repeat;width:26px;height:16px;
		}
		.unread_icon{
			border:none;padding:0;margin:0;background:url(iwork_img/MailUnread.gif) no-repeat;width:16px;height:16px;
		}
		.read_icon{
			border:none;padding:0;margin:0;background:url(iwork_img/readed.gif) no-repeat;width:16px;height:16px;
		}
		.attach{
			border:none;padding:0;margin:0;background:url(iwork_img/email/mail227195.png) -16px -82px no-repeat;width:10px;height:16px;
			margin-left:8px;
		}
		.maintitle{
			font-size:14px;
			font-family:微软雅黑;
			font-werght:bold;
			padding:3px;
			color:#304d79;
			border-bottom:1px solid #efefef;
		}
		.maintitle span{
			color:#666;
			font-size:12px;
			padding:2px;
		}
		.mailItem{
			height:30px;
			border-bottom:1px solid #efefef;
		}
		.mailItem td{
			font-family:微软雅黑;
		}
		.mailItem .mailTitle span{
			color:#a0a0a0;
			font-weight:500;
		}
		.mailItem:hover{
			height:30px;
			background:#f3f3f3;
			cursor:pointer;
		}
		.unread{
			font-weight:bold;
		}
	</style>
	<script type="text/javascript">
	
		function addGroup(){
			 var pageUrl = "iwork_mail_booklist_add.action";
			 art.dialog.open(pageUrl,{
			    	id:"planlistDlg",
			    	title:"新建分组 ", 
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:'300',
				    height:'90%',
					close:function(){
						location.reload();
					}
				 });
			
		}
		function removeGroup(){
		
		}
		//加载导航树 
		var api = art.dialog.open.api, W = api.opener;
		var arr=new Array();
		var listData=new Array();
		
		$(function(){ 
			initTree();
			initSelectNode();
		});
		function initSelectNode(){
			var nodes = '';
			var str = "";
			for(var i=0;i<nodes.length;i++){
			 	arr.push(nodes[i]);
			}
			setSelectNode();
		}
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
							url:"multibook_orgjson.action", 
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
				//============获取列表============================
	 			var nodes = arr;
	 			for(var i=0;i<nodes.length;i++){
	 				if(nodes[i].nodeType=='dept'||nodes[i].nodeType=='com'){
	 					continue;
	 				}
	 				listData.push(nodes[i].useraddress); 
	 			} 
	 			 $("#userlist").val(listData.join(","));
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
			var url = "multibook_search.action?parentDept=<s:property value='parentDept' escapeHtml='false'/>&currentDept=<s:property value='currentDept' escapeHtml='false'/>&startDept=<s:property value='startDept' escapeHtml='false'/>&searchOrg="+searchOrg+"&input=<s:property value="input"/>"
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
		}
		
		function doSubmit(){
			var title = $("#title").val();
			if(title==''){
				alert('标题不允许为空!');
				return;
			}
			insertData();
			$.post('iwork_mail_booklist_save.action',$("#editForm").serialize(),function(data){
		    	if(data=='success'){
		    		alert("添加成功");
		    		api.close();
		    	}else{ 
		    		alert("设置异常,请稍后再试");
		    	}

		  }); 
		}
		
	</script>
</head>
<body class="easyui-layout">
		 <div region="north" border="false" style=" border:1px solid #efefef"> 
		 	<div class="tools_nav" style="padding-left:2px;">
		 		<input type="button" onclick="javascript:doSubmit();"  name="removeBtn" value="保存 "/>
		 	</div>
		 	<s:form name="editForm" id="editForm" theme="simple">
		 	<div>
		 		<table>
		 			<tr>
		 				<td>分组名称</td>
		 				<td>
		 					<s:textfield name="title" id="title" theme="simple"></s:textfield>
		 				</td>
		 			</tr>
		 			<tr>
		 				<td>分组描述</td>
		 				<td>
		 					<s:textarea name="desc" id="desc" theme="simple" cssStyle="width:300px;height:50px;">
		 					</s:textarea>
		 					<s:hidden id="userlist" name="userlist"></s:hidden> 
		 					<s:hidden id="id" name="id"></s:hidden> 
		 				</td>
		 			</tr>
		 		</table>
		 	</div>
		 	 </s:form> 
        </div> 
            <div region="center" border="false" style="background:#fff; border:1px solid #efefef">
            	<fieldset style="height:95%">
            		<legend>选择分组成员</legend>
            		 <div style="text-align:left;padding:3px;"><a href="javascript:expandAll();">全部展开</a>/<a href="javascript:unExpandAll();">全部收起</a></div>
					    
					    	<div>
					         <ul id="multitree" class="ztree"></ul>
					        </div>
					    	
					    	
					   
            	</fieldset>
            </div> 
            <div region="south" border="false" style="height:100px;">
            	已选择：<span id="selectinfo"></span>
			</div>
			
</body>
</html>
