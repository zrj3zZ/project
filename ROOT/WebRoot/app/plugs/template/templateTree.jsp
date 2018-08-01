<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html> 
<head> 
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>模板列表</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link href="iwork_css/common.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script>
	
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
		$(function(){
			initTree();
		});
		
		function initTree(){
			var setting = {
						async: {
							enable: true, 
							url:"hna_sanhui_templateTreeJSON.action",  
							dataType:"json",
							autoParam:["id","nodeType"]
						},
						callback: {
							onClick:onClick
						} 
					};
				$.fn.zTree.init($("#templateTree"), setting);//加载导航树 
		}
		
		//点击事件
		function onClick(event, treeId, treeNode, clickFlag){
			var zTree = $.fn.zTree.getZTreeObj("templateTree");
			var nodes = zTree.getSelectedNodes();
			var parentid = 0; 
			if(nodes.length>0){ 
				parentid = nodes[0].id;
				var type = nodes[0].type;
				
				if(type=='folder'){
					zTree.expandNode(treeNode, true, null, null, true);
				}else if(type=='file'){
					 var uuid = nodes[0].uuid;
					 if(uuid!=""){
					 	window.opener.document.iformMain.WebOffice.Template=uuid;
					 	window.opener.document.iformMain.WebOffice.EditType="1";                             //控制为不保留痕迹的状态
					 	window.opener.document.iformMain.WebOffice.WebLoadTemplate();
					 	window.close();
					 }
					// mResult=webform.WebOffice.WebDownLoadFile("http://www.goldgrid.com/Images/abc.doc","c:\\abc.doc");
					
				}
			}
		}
		//全部展开
	    function expandAll() {
			var zTree = $.fn.zTree.getZTreeObj("templateTree");
				zTree.expandAll(true);
		}

	   	//全部折叠
		function unExpandAll() {
			var zTree = $.fn.zTree.getZTreeObj("templateTree");
			zTree.expandAll(false);
		}
		
	</script>
</head> 
<body class="easyui-layout">
	<div region="center" style="width:250px;background-color:#efefef" border="false" >
		<ul id="templateTree" class="ztree"></ul>
	</div>
    
</body>
</html>
