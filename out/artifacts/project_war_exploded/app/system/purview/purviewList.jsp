<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
	<title>权限组授权</title>
	<s:head/>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<link href="iwork_css/common.css" rel="stylesheet" type="text/css" />
		<link href="iwork_css/system/openpurview_web.css" rel="stylesheet" type="text/css" />
		<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
		<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
		<!-- 授权页面完全展开引入js -->
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
		<script language="javascript" src="iwork_js/commons.js"></script>
		<script language="javascript" src="iwork_js/system/openpurview_web.js"></script>
		<script language="javascript" src="iwork_js/sys_purviewList.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>	
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
		<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
		<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
 //==========================装载快捷键===============================//快捷键
		var api = art.dialog.open.api, W = api.opener;
		 jQuery(document).bind('keydown',function (evt){		
		    	if(evt.ctrlKey&&evt.shiftKey){
				return false;
		   }
		   else if(evt.ctrlKey && event.keyCode==83){ //Ctrl+s /保存操作
		           $('#purgroup_save').attr('action','setpurview_do.action');
			       $('#purgroup_save').submit(); return false;
		     }
}); //快捷键
		$(document).ready(function(){
			var setting = {
					check: {
						enable: true
					},
					view: {
						selectedMulti: false
					},
					async: {
						enable: true, 
						url:"showpurview_json.action?purviewid=<s:property value='purviewid'/>",
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
				
			$.fn.zTree.init($("#purviewTree"), setting);
			expandAll();
		});
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
			expandAll();
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
	 		if(!treeNode.checked){
	 			zTree.checkNode(treeNode, true, true, clickFlag);
	 		}else{
 				zTree.checkNode(treeNode, false, true, false);
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
			
			//设置权限
		function setpurview(){
		//===装载数据
			var zTree = $.fn.zTree.getZTreeObj("purviewTree");
 			var nodes = zTree.getCheckedNodes(true);
 			var str = "";  
 			for(var i=0;i<nodes.length;i++){
 				var tmp = nodes[i].nodeType+"_"+nodes[i].id;
 				if(i<nodes.length-1){
 					tmp+=","; 
 				}
 				str+=tmp;
 			}
 			$("#navigation").val(str);
 			//===请求服务器==
			$.post('setpurview_do.action',$("#editForm").serialize(),function(data){
		    	if(data=='success'){
		    		art.dialog.tips("授权成功");
		    	}else{ 
		    		art.dialog.tips("授权异常,请稍后再试");
		    	}
		  }); 
		}
</script>
</head>
<body  class="easyui-layout">
<div region="north" border="false" split="false" > 
<div class="tools_nav">
        <table width="100%" border="0" cellpadding="0" cellspacing="0" >
	      <tr>
          	<td align="left"><span id="title_span" style='font-size:14px;font-family:黑体'><img alt="权限组【导航菜单】授权" src="iwork_img/shield.png" border="0">权限组【导航菜单】授权</span></td>
          	<td style="text-align:right;padding:0px;padding-right:10px;padding-bottom:2px;">
          		<a href='javascript:setpurview();' class="easyui-linkbutton" iconCls="icon-process-purview" plain="false">授权</a>
				<a href='javascript:api.close();' class="easyui-linkbutton" iconCls="icon-cancel" plain="false">关闭</a>
          	</td>
           </tr>
	    </table>
	  </div>
</div>
<div region="west"   border="false"  region="west" icon="icon-reload"  split="true" style="width:200px;padding-left:5px;overflow:hidden;border-right:1px solid #efefef">
	<s:property value='tablist' escapeHtml="false"/>
</div>
<div region="center" border="false" >
<table width="100%" >
  <tr>
      <td  width="85%" colspan="0" align="center"   valign="top""> 
        <div align="left"> 
          <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
          <tr> 
          	<td align="left"  height='20' style="border-bottom:1px #CCCCCC solid;padding-bottom:5px;bgcolor:#9C9A69">
        <a href="" onClick="expandAll();return false;"><strong  style='color:#990033'>展开全部</strong></a>
        <a href="" onClick="unExpandAll();return false;"><strong  style='color:#990033'>全部折叠</strong></a> 
        </td>
          </tr>
            <tr> 
              <td width=60%>
              	 <ul id="purviewTree" class="ztree"></ul> 
              </td>
            </tr>
          </table>
          <p align="center">&nbsp;</p>
          <p align="center">&nbsp;</p>     
        </div></td>
    </tr>
  </table>
	
<s:form name="editForm" id="editForm" theme="simple">
		<s:if test="null == model">
		 	<s:hidden name="model.id" value="%{id}"/>
		 </s:if>	 	
	     <s:else>
		 	<s:hidden name="model.id" />
		 </s:else>
		 <s:hidden name="purviewid" />
		 <s:hidden name="navigation" id="navigation"/>
</s:form>
</div>
</body>
</html>
<script type="text/javascript">
<!--
	setTimeout("expandAll()",200);
//-->
</script>
