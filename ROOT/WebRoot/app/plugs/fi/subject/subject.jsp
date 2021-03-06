﻿<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html> 
<head> 
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>单选地址簿</title>
	 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"/>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	 
	<style type="text/css">
		.memoTitle{
			font-size:14px;
			padding:5px;
			color:#666;
		}
		.memoTitle a{
			font-size:12px;
			padding:5px;
		}
	</style>
	<script type="text/javascript">
		//加载导航树  
		var api = frameElement.api, W = api.opener;	
		$(function(){
			initTree();
		});
		function initTree(){
			var setting = {
						async: {
							enable: true, 
							url:"iwork_fi_subject_listJson.action?ispurview=true", 
							//dataFilter: filter,
							dataType:"json"
						}, 
						callback: {
								onClick:onClick
							} 
					};
				$.fn.zTree.init($("#radiotree"), setting);//加载导航树 
		}
		
		function filter(treeId, parentNode, childNodes) {
			if (!childNodes) return null;
			for (var i=0, l=childNodes.length; i<l; i++) {
				if(childNodes[i].type=='dept'){
					
				}
				for(var j = 0;j<selectedNodes.length;j++){
					if(selectedNodes[j]==childNodes[i].userId){
						childNodes[i].checked = true;
						break;
					}
				}
				childNodes[i].name = childNodes[i].name;
			}
			return childNodes;
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
						//回调父页面方法
						var defaultField = $("#defaultField").val();
						try{
							parent.selectTreeNode(defaultField,treeNode);
							api.close();
						}catch(e){} 
					}
			}
		//查询回车事件
	    function enterKey(){
			if (window.event.keyCode==13){
				 dosearch();
				return;
			}
		} 
		//全部展开
	    function expandAll() {
			var zTree = $.fn.zTree.getZTreeObj("radiotree");
				zTree.expandAll(true);
		}

	  //全部折叠
		function unExpandAll() {
			var zTree = $.fn.zTree.getZTreeObj("radiotree");
			zTree.expandAll(false);
		}
	</script> 
</head>
<body class="easyui-layout">
	<div region="north" class="memoTitle" border="false" >
		请选择权限范围内的费用类别<br><a href="javascript:expandAll();">全部展开</a>/<a href="javascript:unExpandAll();">全部收起</a>
	</div>
    <div region="center" border="false"  align="center" style="border:0px solid #C0C0C0;">
    <div style="text-align:left;padding:3px;"></div>
	    <s:form name="editForm" id="editForm" theme="simple">
	    	<div>
	         <ul id="radiotree" class="ztree"></ul>
	        </div>
	    	<s:hidden name="defaultField" id="defaultField"></s:hidden>
	    </s:form> 
    </div>
    <div region="south" style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;" border="false" >
		已选择：<s:property value="input"/> 
	</div>
</body>
</html>
