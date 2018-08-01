<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			 var setting = {
				async: {
						enable: true, 
						url:"iwork_hr_legal_json.action",
						dataType:"json",
						autoParam:["id","nodeType"]
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
			var url = "";
			if(treeNode.type=="company"){
				url = "iwork_hr_legal_list.action?companyno="+treeNode.COMPANY_NO+"&deptno=0";
			}else if(treeNode.type=="dept"){
				url = "iwork_hr_legal_list.action?companyno="+treeNode.COMPANY_NO+"&deptno="+treeNode.DEPT_NO;
			}
			
			$('#editForm').attr('action',url); 
			$('#editForm').attr('target',"orgUserFrame");
			$('#editForm').submit(); 
		}
		function addLegalCompany(demid,fromId){
			 var pageUrl = "createFormInstance.action?formid="+fromId+"&demId="+demid;
			 	 art.dialog.open(pageUrl,{
						id:'legal_show',
						title:'新建法人公司',
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					   width:680,
					    height:510
					 });
		}
		function addLegalDept(demid,fromId){
			 var zTree = $.fn.zTree.getZTreeObj("orgUserTree");
				var nodes = zTree.getSelectedNodes();
				var parentid = 0;
				if(nodes.length>0){ 
					parentid = nodes[0].id;
					var type = nodes[0].type;
					var companyno,companyName,deptno,deptname,parentDeptNo;
					if(type=='company'){
						companyno = nodes[0].COMPANY_NO; 
						companyName = nodes[0].COMPANY_NAME;
						deptno = "0";
					}else if(type=='dept'){
						companyno = nodes[0].COMPANY_NO; 
						companyName = nodes[0].COMPANY_NAME;
						deptno = nodes[0].DEPT_NO;
						deptname = nodes[0].DEPT_NAME;
						
					}else{
						alert('请选择法人组织目录');  
						return ;
					}
					var pageUrl = "createFormInstance.action?formid="+fromId+"&demId="+demid+"&LEGAL_COMY_NO="+companyno+"&LEGAL_COMY_NAME="+companyName+"&PARENTID="+deptno;
			 	 	art.dialog.open(pageUrl,{
						id:'legal_show',
						title:'添加部门', 
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width:680,
					    height:510
					 });
				}else{
					alert('请选择添加目录'); 
				}
		}
		function editLegalDept(demid,fromId,instanceid){
						 var pageUrl = "openFormPage.action?formid="+fromId+"&demId="+demid+"&instanceId="+instanceid;
						 art.dialog.open(pageUrl,{
							id:'dept_show',
							title:'编辑部门', 
							lock:true,
							background: '#999', // 背景色
						    opacity: 0.87,	// 透明度
						    width:680,
						    height:510
						 });
		} 
		function addUser(){
			 var zTree = $.fn.zTree.getZTreeObj("orgUserTree");
				var nodes = zTree.getSelectedNodes();
				var parentid = 0;
				if(nodes.length>0){
					parentid = nodes[0].id;
					var type = nodes[0].type;
					if(type=="dept"){ 
						var deptno = nodes[0].DEPT_NO;
						var instanceid = nodes[0].INSTANCEID; 
						 var pageUrl = "iwork_hr_legal_showuser.action?instanceid="+instanceid+"&&deptno="+deptno;
						 art.dialog.open(pageUrl,{
							id:'dept_show',
							title:'关联人员', 
							lock:true,
							background: '#999', // 背景色
						    opacity: 0.87,	// 透明度
						    width:680,
						    height:510
						 });
					}else{
						alert('请选择部门目录');  
						return ;
					}
					
				}else{
					alert('请选择部门目录');  
					return ;
				}
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
					<a href="javascript:addLegalCompany(<s:property value="company_demid"/>,<s:property value="company_formid"/>);" class="easyui-linkbutton" plain="true" iconCls="icon-add">新建法人公司</a>
					<a href="javascript:addLegalDept(<s:property value="dept_demid"/>,<s:property value="dept_formid"/>);"  class="easyui-linkbutton" plain="true" iconCls="icon-add">新建部门</a>
					<a href="javascript:addUser();"  class="easyui-linkbutton" plain="true" iconCls="icon-add">关联人员</a>
					<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		
				</td>
				<td>
				<span class="search_btn" id="search_btn">
				  <input  type="text" name="searchTxt" id="searchTxt" onKeyDown="enterKey();if(window.event.keyCode==13)return false;" class="search_input"/>
				  <input  onclick="doSearch();" type="button" class="search_button" value="&nbsp;"/> 
				  </span> 
		  		</td>
			</tr>
		</table> 
		 </div>
	</div>
    <!-- 导航区 -->
	<div region="west"  split="false" border="false"  style="height:100%;width:230px;padding-left:5px;overflow:hidden; border-top:1px solid #F9FAFD">
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
						 		<s:hidden name="dept_demid" id="dept_demid"></s:hidden>
						 		<s:hidden name="dept_formid" id="dept_formid"></s:hidden> 
						 </form>
    </div>
	<div region="center" style="border:0px;">
			<iframe id="orgUserFrame" name="orgUserFrame"  src="user_list.action" frameborder="no" border="0" marginwidth="0″ marginheight="0″ scrolling="auto" allowtransparency="yes"  id="metadataFrame" name="metadataFrame" width="98%" height="100%"></iframe>
	</div>
</body>
</html> 