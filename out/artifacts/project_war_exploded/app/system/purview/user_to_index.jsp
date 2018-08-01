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
			var setting = {
					check: {
						enable: false
					},
					view: {
						selectedMulti: false
					},
					async: {
						enable: true, 
						url:encodeURI("openPurviewUser_treejson.action?companyid=<s:property value="companyid"/>&companyname=<s:property value="companyname"/>"),
						dataType:"json", 
						dataFilter: filter,
						autoParam:["id","nodeType"]
					},callback: {
						beforeAsync: beforeAsync,
						onAsyncSuccess: onAsyncSuccess,
						onAsyncError: onAsyncError,
						onClick:onClick
					}
				};
			var setting2 = {
					check: {
						enable: true
					},
					view: { 
						selectedMulti: false
					},
					async: {
						enable: true, 
						url:"openUnUserPurviewList.action",
						dataType:"json", 
						autoParam:["id","type"]
					},callback: { 
						onClick:onPurviewClick,
						onCheck:onCheck
					}
				};
			//加载组织树 
			var setting3 = {
				async: {
					enable: true, 
					url:encodeURI("company_tree_json.action?companyid=<s:property value="companyid"/>&companyname=<s:property value="companyname"/>"),
					dataType:"json",
					autoParam:["id","nodeType"] 
				},
				data: {
					simpleData: {
						enable: true
					}
				},
				callback: {
					onClick:setOrgComapny
				} 
			};
			$.fn.zTree.init($("#grouptree"), setting3);	
			$.fn.zTree.init($("#purviewTree"), setting);
			$.fn.zTree.init($("#purviewListTree"), setting2);
		});
		
		function setOrgComapny(event, treeId, treeNode, clickFlag){
			var url = "openUnPurviewUserIndex.action?companyid="+treeNode.id+"&companyname="+encodeURI(treeNode.companyname)+"&companytype="+encodeURI(treeNode.companytype);
			window.location.href = url; 
		}
		var demoMsg = {
			async:"正在进行异步加载，请等一会儿再点击...",
			expandAllOver: "全部展开完毕",
			asyncAllOver: "后台异步加载完毕",
			asyncAll: "已经异步加载完毕，不再重新加载",
			expandAll: "已经异步加载完毕，使用 expandAll 方法"
		}
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
			alert(2);
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
	function showMenu() {
				var cityObj = $("#citySel"); 
				var cityOffset = $("#citySel").offset();
				$("#menuContent").css({left:+ "10px", top:cityOffset.top-20  + "px"}).slideDown("fast");
				$("body").bind("mousedown", onBodyDown);
				return false; 
		} 
		function hideMenu() {
			$("#menuContent").fadeOut("fast");
			$("body").unbind("mousedown", onBodyDown);
		} 
		function onBodyDown(event) {
			if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
				hideMenu();
			}
		}
	</script>
</head>
<body class="easyui-layout">
<!-- TOP区 -->
	<div region="north" border="false" style="height:40px;border-bottom:1px;">
		<div class="tools_nav">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" >
	      <tr>
          	<td align="left"><span id="title_span" style='font-size:14px;font-family:黑体'><img alt="用户-->权限组授权" src="iwork_img/shield.png" border="0"/>用户-->权限组授权</span></td>
          	<td style="text-align:right;padding:0px;padding-right:10px;padding-bottom:2px;">
          		<a href='javascript:setpurview();' class="easyui-linkbutton" iconCls="icon-process-purview" plain="false">授权</a>
				<a href='javascript:this.location.reload();' class="easyui-linkbutton" iconCls="icon-reload" plain="false">刷新</a>
          	</td>
           </tr>
	    </table>

	  </div>
		 
		 </div>
    <!-- 导航区 -->
	<div region="west"  split="false" border="false"  style="height:100%;width:180px;padding-left:5px;overflow:auto; border-top:1px solid #F9FAFD">
				<s:property value="tablist" escapeHtml="false"/> 
    </div>
	<div region="center" style="padding:3px;border:0px;">
	<table style="height:100%;" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td  style="vertical-align:top">
				 <div style="padding:2px; text-align:center;background:#E7E7E7;border:1px solid #fff;">
							 	<input id="citySel" type="text" readonly value='<s:property value="companyname"/>' style="width:200px;background:#F5F5F5;padding-left:5px;color:#666;border:1px solid #fff"/>
							&nbsp;<a id="menuBtn" href="#" onclick="showMenu(); return false;">选择</a></li>
							 </div>
							 <div id="menuContent" class="menuContent" style="display:none;background-color:#fff;border:1px solid #efefef; position: absolute;z-index:1;"> 
													<ul id="grouptree" class="ztree" style="margin-top:0;background:#fafafa;border:1px solid #FFCC00"></ul> 
									</div> 
							 <div>
							 	<a href="" onClick="expandAll();return false;"><strong  style='color:#990033'>展开全部</strong></a>
        <a href="" onClick="unExpandAll();return false;"><strong  style='color:#990033'>全部折叠</strong></a>
							<ul id="purviewTree" class="ztree"></ul>
							 </div>
			</td>
			<td  style="vertical-align:top">
			<table style="height:100%;" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="tree_top"></td>
					</tr>
					<tr>
						<td class="tree_main">
							<ul id="purviewListTree" class="ztree"></ul>
						</td>
					</tr>
					<tr>
						<td class="tree_bottom"></td>
					</tr>
					
					
				</table>
			</td>
			<td style="vertical-align:top">
			<fieldset style="width:150px;margin-left:5px;padding:5px;">

								<legend >功能提示</legend>
								 【说明】当前显示的用户对应的权限组，仅为用户自身对应的权限组信息，不包含用户对应部门、角色所绑定的权限组。<br><br>
								 【建议】基于通过管理的需求,建议个性化授权时使用此授权。<br><br>
							</fieldset>
			
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
