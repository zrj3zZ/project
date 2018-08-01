<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="iwork" uri="/WEB-INF/tld/linkbtn.tld"%>
<%@ taglib prefix="cache"  uri="/oscache"%>

<html>
<head><title>角色树</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link href="iwork_css/common.css" rel="stylesheet" type="text/css" />
<link href="iwork_css/org/company_list.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="iwork_js/commons.js"></script>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			 var setting = {
	             	check:{
						enable:true
					},
					view: {
						showLine: true, 
						selectedMulti: false
					},  
					async: {
						enable:true,
						url:"role_tree_json.action",
						dataType:"json"
					},
					callback: {
						onClick: onClick,
						onAsyncSuccess: onAsyncSuccess
					}
				};
	             function onClick(event, treeId, treeNode){
	             	var treeObj = $.fn.zTree.getZTreeObj(treeId);
	             	if(treeNode.checked){
	             		treeObj.checkNode(treeNode, false, true);
	             	}else{
	             		treeObj.checkNode(treeNode, true, true);
	             	}
	             } 
	             function onAsyncSuccess(event, treeId, treeNode, msg){
	                var feildsChoosen = new Array();
	                var param = $("#editForm_model_routeParam").val();
	                 feildsChoosen = param.split(","); //已经字段变灰色
	                 for(var i=0;i<feildsChoosen.length;i++){
	                     var treeObj = $.fn.zTree.getZTreeObj(treeId);
	                     var nodes=treeObj.getNodesByParam('id',feildsChoosen[i],null);
	                     for(var j=0;j<nodes.length;j++){
	                     	  if(nodes[j].type=='role'){
	                     	  		treeObj.checkNode(nodes[j], true, true);
	                     	  		treeObj.setChkDisabled(nodes[j], true);
	                     	  }
	                     }
	                 }  
	                
	             }
	             $.fn.zTree.init($("#fieldTree"), setting);
		
		});
		//装载数据对象
		  var api = art.dialog.open.api, W = api.opener;
		  
		  function add(){
		  	var treeObj = $.fn.zTree.getZTreeObj("fieldTree");
					var nodes = treeObj.getCheckedNodes(true); 
					var idArray = new Array();  
					var titleArray = new Array();
					var num = 0;
					for(var i=0;i<nodes.length;i++){	
						if(nodes[i].nodeType=='role'){  
							num++;
							idArray.push(nodes[i].id);
							titleArray.push(nodes[i].name);
						}
					}
					if(num==0){
						alert('请选择路由角色');
					}else{
						rebackData(idArray.join(","),titleArray.join(","));
					}
		  }
		  
			function rebackData(value,title){
				var inputName = $("#inputName").val();
				var inputTitle = $("#inputTitle").val();
				if(inputName!=""&&inputTitle!=""){
					var origin = artDialog.open.origin; 
					var v = origin.document.getElementById(inputName);
					var titleInput = origin.document.getElementById(inputTitle);
					if(v!=null){
						v.value = value;
					}else if(api.get(api.data.dialogName)!=null){
						v = api.get(api.data.dialogName).document.getElementById(inputName);
					}
					if(v!=null){
							v.value = value;
					}
					
					if(titleInput!=null){
						titleInput.value = title;
					}else if(api.get(api.data.dialogName)!=null){
						titleInput = api.get(api.data.dialogName).document.getElementById(inputTitle);
					}
					if(titleInput!=null){
							titleInput.value = title;
					}
				}
				api.close();
			}
		
	</script>
</head>	
<body  class="easyui-layout">
<!-- <div class="menubackground"style="padding-left:5px;padding-top:2px;padding-bottom:2px;"> -->
<div region="north" border="false" split="false"   class="nav" id="layoutNorth">
<div class="tools_nav"> 
		<a href="javascript:add();" class="easyui-linkbutton"  plain="true" iconCls="icon-ok" >确定</a>
		<a href="javascript:api.close();" class="easyui-linkbutton"  plain="true" iconCls="icon-cancel" >取消</a>
	</div>
</div>
<div region="center" border="false" split="false"   class="nav" id="layoutNorth">
	<ul id="fieldTree" class="ztree" style="margin-top:0; width:160px;"></ul> 
<form id="editForm" method="post">
<s:hidden  id='inputName' name='inputName'></s:hidden>
<s:hidden  id='inputTitle' name='inputTitle'></s:hidden>
<s:hidden  id='stationId' name='stationId'></s:hidden>
</form>
</div>
</body>
</html>
