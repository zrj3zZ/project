<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
	var api,W;
	try{
		api=  art.dialog.open.api;
		W = api.opener;	
	}catch(e){}
	$(function() {
		initTreeTo();
	});
	function initTreeTo() {
		var setting = {
			async : {
				enable : true,
				url : "zqb_addxmcy_department_ztree.action",
				dataType : "json",
				autoParam : [ "id", "nodeType", "companyId"]
			},
			data : {
				simpleData : {
					enable : true
				}
			},
			callback : {
				onClick : onClick
			}
		};
		$.fn.zTree.init($("#phoneTree"), setting);
	}
	function onClick(event, treeId, treeNode, clickFlag) {
		if(treeNode.pId==null){
			return;
		}
		var targetname = $("#targetname").val();
		var targetid = $("#targetid").val();
		if(parent.document.getElementById(targetname)!=null){
			parent.document.getElementById(targetname).value=treeNode.name;
		}
		if(parent.document.getElementById(targetid)!=null){
			parent.document.getElementById(targetid).value=treeNode.id;
		}
		api.close();
	}
</script>
</head>
<body>
	<div region="west" style="width:100%;height:100%;float: left;border:0px;padding:3px; padding:0px; line-height: 20px; overflow: auto; border="false">
		<ul id="phoneTree" class="ztree"></ul>
	</div>
	<div style="display:none;">
		<form action="zqb_gpfx_addxmcy_department_index.action" method=post name=frmMain>
			<s:hidden name="targetid" id="targetid"></s:hidden>
			<s:hidden name="targetname" id="targetname"></s:hidden>
		</form>
	</div>
</body>
</html>
