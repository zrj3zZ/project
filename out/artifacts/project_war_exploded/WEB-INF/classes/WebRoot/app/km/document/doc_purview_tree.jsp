<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>文档基本信息</title>
		<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/km/km_baseinfo.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
		<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
		<script type="text/javascript"	src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
		<script type="text/javascript"	src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
		<script type="text/javascript"  src="iwork_js/jqueryjs/jquery.form.js"></script>
		<script type="text/javascript"	src="iwork_js/jqueryjs/jquery.metadata.js"></script>
		<script type="text/javascript"	src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
		<script type="text/javascript"	src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
		<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
		<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
		<script type="text/javascript">
			//加载导航树 
		var api = art.dialog.open.api;
		$(function(){ 
			initTree();
		
		});
		function initTree(){
			var setting = {
						check: {						
							enable: true
					    },
					    view: {
							selectedMulti: false
						},
						async: {
							enable: true, 
							//url:"km_purview_orgjson.action?purviewGroup=<s:property value="purviewGroup"/>&type=<s:property value="type"/>&purviewType=0&pid=<s:property value="id"/>", 
							url:"zq_purview_orgjson.action?purviewGroup=<s:property value="purviewGroup"/>&type=<s:property value="type"/>&purviewType=0&pid=<s:property value="id"/>", 
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
				$.fn.zTree.init($("#orgtree"), setting);//加载导航树 
				expandAll();
		}
		//点击事件
			function onClick(event, treeId, treeNode, clickFlag){
			var zTree = $.fn.zTree.getZTreeObj("orgtree"); 
					if(treeNode.isParent){
						if(treeNode.open){
							zTree.expandNode(treeNode, false, null, null, true);
						}else{ 
							zTree.expandNode(treeNode, true, null, null, true);
						}
					}else{
						if(!treeNode.checked){ 
							zTree.checkNode(treeNode, true, true,true);
				 		}else{  
			 				zTree.checkNode(treeNode,false, true,true);
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
			var zTree = $.fn.zTree.getZTreeObj("orgtree");
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
			var zTree = $.fn.zTree.getZTreeObj("orgtree");
			zTree.expandAll(false);
		}
		function expandNodes(nodes) {
			if (!nodes) return;
			curStatus = "expand";
			var zTree = $.fn.zTree.getZTreeObj("orgtree");
			for (var i=0, l=nodes.length; i<l; i++) {
				zTree.expandNode(nodes[i], true, false, true); 
				if (nodes[i].isParent && nodes[i].zAsync) {
					expandNodes(nodes[i].children);
				} else {
					goAsync = true;
				}
			}
		}
 
		function asyncAll() {
			var zTree = $.fn.zTree.getZTreeObj("orgtree");
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
			var zTree = $.fn.zTree.getZTreeObj("orgtree");
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
		//设置授权类型 
		function selType(obj){
			var type=obj.value;
		
			var searchOrg=$("#searchOrg").val();
			if(obj==''){
				type=$("input[type=radio]:checked").val();
			}
			var pageurl =  encodeURI("zq_purview_orgjson.action?purviewGroup=<s:property value="purviewGroup"/>&type=<s:property value="type"/>&purviewType="+type+"&pid=<s:property value="id"/>&searchOrg="+searchOrg);			
			var setting = {  
						check: {
							enable: true
					    },
					    view: {
							selectedMulti: false
						},
						async: {
							enable: true, 
							url:pageurl, 
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
				$("#purviewType").val(type);
				$.fn.zTree.init($("#orgtree"), setting);//加载导航树 
				if(	type==0){
					location.reload();
				}
		}
		
		//设置权限
		function setPurview(){
			var zTree = $.fn.zTree.getZTreeObj("orgtree");						 
 			var nodes = zTree.getCheckedNodes(true);						
 			//if(nodes==""||nodes==null){
 			//  alert("请选择要授权的对象");
 			// }else{
 			var str = ""; 
 			var nodelist = new Array();
 			for(var i=0;i<nodes.length;i++){
	 			if(nodes[i].nodeType=="dept"||nodes[i].nodeType=="user"){
	 				nodelist.push(nodes[i].id); 	 				
	 			}
 			}
 			var ids = nodelist.join(","); 			
 			$("#purviewIdList").val(ids);
 			//$("#purviewType").val(0);   //0:按部门授权  1:按人员授权
 			//$("#purviewGroup").val("folder");    //授权文件夹 
 			 
 			var options = {
 					error:errorFunc,
 					success:successFunc 
 				   };
 				$('#editForm').ajaxSubmit(options);
		    }
          errorFunc=function(){
                      art.dialog.tips("保存失败！");
          }
          successFunc=function(responseText, statusText, xhr, $form){		          
             if(responseText=="success"){
               art.dialog.tips("保存成功!",5);
               api.close();
             }else if(responseText=="error"){
               art.dialog.tips("保存失败!");
             } 
		   }
	function empty(){
		$("#searchOrg").val("");  		    		 	    
	}
	</script>
		<style type="text/css">
.td_title {
	color: #000;
	letter-spacing: 0.1em;
	padding-right: 10px;
	white-space: nowrap;
	vertical-align: middle;
	font-family: 黑体;
	font-size: 14px;
	text-align: right;
	width: 100px;
	line-height: 20px;
}

.td_data {
	color: #0000FF;
	text-align: left;
	padding-left: 3px;
	font-size: 12px;
	vertical-align: middle;
	word-wrap: break-word;
	word-break: break-all;
	font-weight: 500;
	padding-top: 5px;
	font-family: "宋体";
	border-bottom: 1px solid #efefef;
	line-height: 20px;
}
</style>
	</head>

	<body class="easyui-layout">
		<div region="north" border="false">
			<div class="tools_nav">
				<table width="100%">
					<tr>
						<td>
							<a href="#" plain="true" class="easyui-linkbutton"
								iconCls="icon-add" onclick="javascript:setPurview();"
								title="添加授权">添加授权</a>
							<a href="#" plain="true" class="easyui-linkbutton"
								iconCls="icon-reload" onclick="javascript:location.reload();"
								title="刷新组织树">刷新</a>
						
						</td>
						<td>
						<input type="text"  onclick="this.value=''"  onKeyDown="enterKey();if(window.event.keyCode==13) return false;"  
						style="width:60px;" name="searchOrg" id="searchOrg"/>
						<input type="button" onclick="selType('');" value="查询"/>
						<input type="button" onclick="empty()" value="清空选择"/>
						</td>
						<td style="text-align: right; padding: 3px; font-size: 12px">
							<input type="radio" name="model.docType" checked="checked"
								id="editForm_model_docType1" value="0" onclick="selType(this);" />
							<label for="editForm_model_docType1">
								部门授权
							</label>
							<input type="radio" name="model.docType"
								id="editForm_model_docType2" value="1" onclick="selType(this);" />
							<label for="editForm_model_docType2">
								用户授权
							</label>

						</td>
						<td style="text-align: right; padding: 3px; font-size: 12px">
							<a href="javascript:expandAll();">全部展开</a>/
							<a href="javascript:unExpandAll();">全部收起</a>
						</td>
					</tr>
				</table>


			</div>
		</div>
		<div region="center" border="false">
			<ul id="orgtree" class="ztree"></ul>
				<s:form name="editForm" id="editForm" action="km_purview_set.action" theme="simple">
					<s:hidden name="id"></s:hidden>
					<s:hidden name="pid" value="%{id}"></s:hidden>
					<s:hidden name="purviewIdList" id="purviewIdList"></s:hidden>
					<s:hidden name="purviewType" id="purviewType" value="0"></s:hidden>
					<s:hidden name="purviewGroup" id="purviewGroup"></s:hidden>
					<s:hidden name="type" id="type"></s:hidden>
				</s:form>
			
		</div>
	</body>
</html>
<script type="text/javascript">
<!--
	setTimeout("expandAll()",200);
//-->
</script>