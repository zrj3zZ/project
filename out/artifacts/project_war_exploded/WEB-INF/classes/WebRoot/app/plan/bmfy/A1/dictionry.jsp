<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>IWORK综合应用管理系统</title> 
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
	<script type="text/javascript"> 
		var api = frameElement.api, W = api.opener;	
		$(function(){ 
			var setting = {
				check: {
					enable: true
				},
				view: {
					selectedMulti: true
				},  
				async: {
					enable: true, 
					url:"iwork_fi_subject_loadTreejson.action?groupid=008",
					dataType:"json"   
				}, 
				callback: {
					onClick:function(event, treeId, treeNode, clickFlag){
							
					}
				} 
			};  
			var treeObj = $.fn.zTree.init($("#dirTree"), setting);
		})
		
		function doSetting(){
			
			// var categoryTree = $.fn.zTree.getZTreeObj("dirTree");
			// var nodes = categoryTree.getSelectedNodes();
			var zTree = $.fn.zTree.getZTreeObj("dirTree");
 			var nodes = zTree.getCheckedNodes(true);
			// leftNode = nodes[0];
			// var typeid = leftNode.FYLBBH;
			if (nodes.length == 0) {
				alert("请先选择一个节点");
				return;
			} 
 			var ids = [];
 			for(var i=0;i<nodes.length;i++){
 				if(nodes[i].type=='group'){
 					continue;
 				}
 				var tmp = nodes[i].FYLBBH;
 				var name = nodes[i].name;
 				var lx = nodes[i].FZMC;
 				if(tmp!='' && name!=''){
 					var subjectIds = tmp+";"+name+";"+lx;
 					ids.push(subjectIds);
 				}
 			}
 			var ids = ids.join(",");
			
			var instanceid = $("#instanceid").val();
	        var pageUrl="sanbu_plan_bmfy_add.action";
	        // $.post('iwork_fi_subject_list_set.action',{typeid:typeid,subjectIds:subjectIds,typekey:$("#typekey").val()},function(data){
	        $.post(pageUrl,{instanceid:instanceid,ids:ids},function(data){
		    	if(data=='success'){
		        	closeWin(); 
		        }else{
		         	alert("操作异常");
		        }
		    });
	       // }
		}
		
		function closeWin(){
			api.close();
		}
		
			
	</script>
</head>
<body class="easyui-layout"> 
<!-- TOP区 -->
	<div region="north" border="false" style="padding:0px;overflow:no;scrolling:no;border-bottom:1px;">
		<div class="tools_nav">
			<div style="text-align:left;padding-right:10px;">
				<a href="javascript:doSetting();"  class="easyui-linkbutton" plain="true" iconCls="icon-ok">确定</a>
				<a href="javascript:closeWin();" class="easyui-linkbutton" plain="true" iconCls="icon-cancel">取消</a>	 
			</div>
		 </div>
	</div>

	<div region="center" style="padding:10px;border:0px;">
	<!-- TOP区 -->
		<ul id="dirTree" class="ztree"></ul>
		<s:hidden name="instanceid" id="instanceid"></s:hidden>
	</div>
</body>
</html>
