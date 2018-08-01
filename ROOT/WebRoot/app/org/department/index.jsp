<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String lookandfeel = "_def";
 %>
<html>
<head>
<title> </title>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/design_model.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
	<script language="javascript" src="iwork_js/org/department_list.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script> 
	<script type="text/javascript">
	var mainFormValidator;
	$().ready(function() {
		mainFormValidator =  $("#editForm").validate({});
		mainFormValidator.resetForm();
	});
		$(document).ready(function(){
			 var setting = {
				async: {
						enable: true, 
						url:"department_tree_Json.action",
						dataType:"json",
						autoParam:["id","nodeType","companyId"]
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
			$.fn.zTree.init($("#deptTree"), setting);
		});
		function onClick(e, treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("deptTree"); 
			zTree.expandNode(treeNode, true, null, null, true);
			var url = treeNode.pageurl;
			$('#editForm').attr('action',url); 
			$('#editForm').attr('target',"departmenFrame");
			$('#editForm').submit(); 
		}
	</script>
</head>
 <body class="easyui-layout">
<!-- TOP区 -->
	<div region="north" border="false" style="padding:0px;overflow:no;scrolling:no;border-bottom:1px;">
		<div class="tools_nav"> 
		<table width="100%"  border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td> 
					<a href="javascript:addDept();" class="easyui-linkbutton" plain="true" iconCls="icon-add">新增部门</a>
						<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
				</td>
				<td>
				  <span class="search_btn" id="search_btn">
				  <input  type="text" name="searchTxt" id="searchTxt" onKeyDown="enterKey();if(window.event.keyCode==13) return false;" class="search_input {string:true}"/>
				  <input  onclick="doSearch();" type="button" class="search_button" value="&nbsp;"/> 
				  </span> 
		  		</td>
			</tr>
		</table> 
		 </div>
	</div>
    <!-- 导航区 -->
	<div region="west"  split="false" border="false"  style="height:100%;width:230px;padding-left:5px;overflow:auto; border-top:1px solid #F9FAFD">
				<table style="height:100%;" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="tree_top"></td>
					</tr>
					<tr>
						<td class="tree_main">
							<ul id="deptTree" class="ztree"></ul>
						</td>
					</tr>
					<tr>
						<td class="tree_bottom"></td>
					</tr>
				</table>
						 <form id="editForm" name="editForm"method="post">
						 		
						 </form> 
    </div>
	<div region="center" style="padding:0px;border:0px;">
			<iframe id="departmenFrame" name="departmenFrame"  src="department_list.action" frameborder="no" border="0" marginwidth="0" marginheight="0″ scrolling="auto" allowtransparency="yes"  id="metadataFrame" name="metadataFrame" width="100%" height="100%"></iframe>
			<!-- <table id="metadata_grid" style="margin:2px;"></table> -->
	</div>
</body>
</html> 
<script language="JavaScript">
	jQuery.validator.addMethod("string", function(value, element) {
		var sqlstr=[" and "," exec ", " count ", " chr ", " mid ", " master ", " or ", " truncate ", " char ", " declare ", " join ","insert ", "select ", "delete ", "update ","create ","drop "]
		var patrn=/[“”`~!#$%^&*+<>?"{},;'[]（）—。[\]]/im;
		if(patrn.test(value)){
    	}else{
    		var flag = false;
    		var tmp = value.toLowerCase();
    		for(var i=0;i<sqlstr.length;i++){
    			var str = sqlstr[i];
    			if(tmp.indexOf(str)>-1){
    				flag = true;
    				break;
    			}
    		}
    		if(!flag){
    			return "success";
    		}
    	}
    }, "包含非法字符!");
</script>
