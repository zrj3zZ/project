<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String lookandfeel = "_def";
 %>
<html>
<head>
<title> </title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
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
	<script language="javascript" src="iwork_js/org/user_list.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			 var setting = {
				async: {
						enable: true, 
						url:"user_tree_Json.action",
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
			$.fn.zTree.init($("#orgUserTree"), setting);
		});
		function onClick(e, treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("orgUserTree");
			zTree.expandNode(treeNode, true, null, null, true);
			var url = treeNode.pageurl;
			$('#editForm').attr('action',url); 
			$('#editForm').attr('target',"orgUserFrame");
			$('#editForm').submit(); 
		}
		
		function downloadTmp(){
			$("#editForm").attr("action","user_template_download.action");
			$("#editForm").attr("target","_blank");
			$("#editForm").submit();
		}
		
		function uploadTmp(){
			var pageUrl = "user_upload_page.action"; 
			 art.dialog.open(pageUrl,{
			    	id:'orguserWinDiv',
			    	title:'批量导入用户',  
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:'90%',
				    height:'90%',
					close:function(){
				        location.reload();
					}
				 });
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
				<a href="javascript:addcompany();" class="easyui-linkbutton" plain="true" iconCls="icon-max">新增组织</a>
				<a href="javascript:addDept();" class="easyui-linkbutton" plain="true" iconCls="icon-deptbook">新增部门</a> 
					<a href="javascript:addUser();" class="easyui-linkbutton" plain="true" iconCls="icon-org-manager">新增帐号</a>
						<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
						<a href="javascript:downloadTmp();" class="easyui-linkbutton" plain="true" iconCls="icon-excel-exp">下载模板</a>
						<a href="javascript:uploadTmp();" class="easyui-linkbutton" plain="true" iconCls="icon-excel-exp">批量创建</a>
				</td>
				<td>
				<span class="search_btn" id="search_btn">
				  <input  type="text" name="searchTxt" id="searchTxt" onKeyDown="enterKey();if(window.event.keyCode==13) return false;" class="search_input"/>
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
							<ul id="orgUserTree" class="ztree"></ul>
						</td>
					</tr>
					<tr>
						<td class="tree_bottom"></td>
					</tr>
				</table>
						 <form id="editForm" name="editForm"method="post">
						 		
						 </form>
    </div>
	<div region="center" style="border:0px;">
			<iframe id="orgUserFrame" name="orgUserFrame"  src="user_list.action" frameborder="no" border="0" marginwidth="0″ marginheight="0″ scrolling="auto" allowtransparency="yes"  id="metadataFrame" name="metadataFrame" width="98%" height="100%"></iframe>
	</div>
</body>
</html> 