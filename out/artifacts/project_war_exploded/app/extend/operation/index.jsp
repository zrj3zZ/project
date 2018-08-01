<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>  
    <title>日常业务呈报</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"/>
    <script type="text/javascript" src="iwork_js/commons.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script>
    <script type="text/javascript">
    $(function(){
   		//加载导航树  
			initTree();
		});
		function initTree(){
		// 加载导航树
			var setting = {
				async: {
					enable: true, 
					url:"zqb_event_eventJSON.action",  
					dataType:"json"
				},
				callback: {
					onClick:onClick,
					onAsyncSuccess:zTreeOnAsyncSuccess
				},
				view:{
					fontCss:setFontCss
				} 
			};
			$.fn.zTree.init($("#radiotree"), setting);
			function setFontCss(treeId, treeNode) {
				if(treeNode.classification=='正常'){
					return {}
				}
				if(treeNode.classification=='高风险'){
					return {color:"#FF0000"}
				}
				if(treeNode.classification=='次高风险'){
					return {color:"#EE9572"}
				}
				if(treeNode.classification=='关注'){
					return {color:"#6495ED"}
				}
			}
		}
		//点击事件
		function onClick(event, treeId, treeNode, clickFlag){
	        var khbh = treeNode.khbh;
	        var customername = treeNode.khmc==undefined?"":treeNode.khmc;
	        var type = treeNode.type;
	        var zqjc = treeNode.zqjc==undefined?"":treeNode.zqjc;
	        var zqdm = treeNode.zqdm==undefined?"":treeNode.zqdm;
	        if(khbh==""||khbh==undefined||customername==""||customername==undefined){
	        	var orgRoleId = $("#orgRoleId").val();
	        	if(orgRoleId==3){
	        		return;
	        	}else{
	        		$("#mainFrame").attr("src",encodeURI("zqb_operation_index_content.action?customerno="+khbh+"&type="+type+"&customername="+customername+"&zqjc="+zqjc+"&zqdm="+zqdm));
	        	}
	        }
			$("#mainFrame").attr("src",encodeURI("zqb_operation_index_content.action?customerno="+khbh+"&type="+type+"&customername="+customername+"&zqjc="+zqjc+"&zqdm="+zqdm)); 
		}
		function zTreeOnAsyncSuccess() {
			var zTree=$.fn.zTree.getZTreeObj("radiotree");
			var node = zTree.getNodeByTId("announcementTree_2");
			if(node!=null){
				zTree.selectNode(node);
				var khbh = node.khbh;
		        var customername = node.khmc==undefined?"":node.khmc;
		        var type = node.type;
		        var zqjc = node.zqjc;
		        var zqdm = node.zqdm;
				$("#mainFrame").attr("src",encodeURI("zqb_operation_index_content.action?customerno="+khbh+"&type="+type+"&customername="+customername+"&zqjc="+zqjc+"&zqdm="+zqdm));
			}
		}
    </script>
    <style type="text/css">
		 body {
		margin-left: 0px;
		margin-top: 0px;
		margin-right: 0px;
		margin-bottom: 0px;
	}
	</style>	
</head> 
<body class="easyui-layout">
	<div region="west" style="width:250px;background-color:#efefef" border="false"  >
		<div>
			<ul id="radiotree" class="ztree"></ul>
		</div>
	</div>
	<div region="center" border="false"  align="center" style="border:0px solid #C0C0C0;">
		<iframe id="mainFrame" name="mainFrame" scrolling="no" frameborder="0" style="width:100%;height:100%;"></iframe> 
	</div>
	<div style="display:none;">
		<s:hidden name="orgRoleId" id="orgRoleId"></s:hidden>
	</div>
</body>
</html>