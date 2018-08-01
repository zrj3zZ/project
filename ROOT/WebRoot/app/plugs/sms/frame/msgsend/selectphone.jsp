<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>短信平台-我的号码簿</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/plugins/jquery.portal.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>	
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script> 
	<script type="text/javascript">
	var api = frameElement.api, W = api.opener;	
	$(function(){
			//加载导航树
			var setting = {
					check: {
						enable: true
					},
					async: {
						enable: true, 
						url:"showPhoneBookJson.action",
						dataType:"json"
					},
					data: {
						simpleData: {
							enable: true
						}
					},
					callback: {
						onClick: onClick
					}
				};
				$.fn.zTree.init($("#phonebook"), setting);
	});
	 
	function onClick(event, treeId, treeNode){
	             	var treeObj = $.fn.zTree.getZTreeObj(treeId);
	             	if(treeNode.checked){
	             		treeObj.checkNode(treeNode, false, true);
	             	}else{
	             		treeObj.checkNode(treeNode, true, true);
	             	}
	} 
	function setValue(){
		var treeObj = $.fn.zTree.getZTreeObj("phonebook");
		var nodes = treeObj.getCheckedNodes(true);
		var arr=new Array();
		for (var i=0, l=nodes.length; i < l; i++) {
			if(nodes[i].type=="item"){
				var data = nodes[i].phone+"["+nodes[i].name+"]"
				arr.push(data);
			}
		}
		var content = arr.join(",");
		 W.document.getElementById("phone").value=content;
		 api.close();
		//$("#phone",window.parent.document).val(content);
	}
</script>
</head>
<body class="easyui-layout">

<!-- TOP区 -->
	<div region="north" border="false" style="padding:0px;overflow:no">
	<div class="tools_nav">
		<table width="100%">
			<tr>
				<td><input type="text" name="searchkey"> <input type="button" value="查询"></td>
				<td><input type="button" onclick="setValue()" value="确定"></td>
			</tr>
		</table>
		
		
	</div>
	</div>
    <div region="center" style="padding:3px;border:1px solid #efefef">
    	<ul id="phonebook" class="ztree"></ul> 
  </div>
</body>
</html>
