<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*,java.text.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
	<style type="text/css">
		.gridTable td{
			line-height:25px;
		}
	</style>
	<script type="text/javascript">
	var api,W;
		try{
			api= frameElement.api;
			W = api.opener;	
		}catch(e){}

		function pageClose(){ 
			if(typeof(api) =="undefined"){
				window.close();
			}else{ 
				api.close(); 
			}
		}
		
		$(function(){ 
			//加载导航树
			var setting = {
					async: {
						enable: true, 
						url:"schShowOtherUserJson.action",
						dataType:"json" 
					},
					callback: {
						onClick:onClick 
					}
				};  
		var treeObj = $.fn.zTree.init($("#mytree"), setting);
		})
		//点击树视图事件

		function onClick(event, treeId, treeNode, clickFlag){
			var zTree = $.fn.zTree.getZTreeObj("mytree");
			var nodes = zTree.getSelectedNodes();
			var parentid = 0; 
			if(nodes.length>0){ 
				parentid = nodes[0].id;
				var type = nodes[0].type;
					doSetting(nodes[0].userid);
			}
		}
		
function doSetting(visitor){
	var pageUrl = "schVisitorOtherCalendar.action?visitor="+visitor; 
		parent.document.location.href=pageUrl;
}
	</script>
</head>
<body  class="easyui-layout">
		
		<div region="center" style="width:300px;height:100px;padding:3px;border:0px;padding-left:10px;padding-top:10px;padding-right:10px;">
		<fieldset style="border:1px solid #999;">
			<legend>请选择您有权限查看的日程</legend>
			
			<ul id="mytree" class="ztree"></ul>
		</fieldset>
            	
	     </div>
</body>
</html>