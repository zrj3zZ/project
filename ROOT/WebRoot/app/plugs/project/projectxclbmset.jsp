<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
<script type="text/javascript">
	var api = frameElement.api, W = api.opener;
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
		if(parent.document.getElementById("FZJGMC")!=null){
			parent.document.getElementById("FZJGMC").value=treeNode.name;
		}
		if(parent.document.getElementById("SGFS")!=null){
			parent.document.getElementById("SGFS").value=treeNode.name;
		}
		api.close();
	}
</script>
</head>
<body>
	<div region="west" style="width:100%;height:100%;float: left;border:0px;padding:3px; padding:0px; line-height: 20px; overflow: auto; border="false">
		<ul id="phoneTree" class="ztree"></ul>
	</div>
</body>
</html>
