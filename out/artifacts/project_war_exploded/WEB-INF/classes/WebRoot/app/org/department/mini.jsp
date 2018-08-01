<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String lookandfeel = "_def";
 %>
<html>
<head>
<title> </title>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/design_model.css"> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
	<script language="javascript" src="iwork_js/org/department_list.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script> 
	<script type="text/javascript">
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
			if(!treeNode.open){
					var zTree = $.fn.zTree.getZTreeObj("deptTree");
				    zTree.expandNode(treeNode, true, null, null, true);
		        }
			
		}
		function addOra(){
				if(!confirm("确认将当前组织及部门，添加至证券业务管理系统中吗？")){
						return;
				}
		}
		function addCompany(){
			var zTree = $.fn.zTree.getZTreeObj("deptTree");
			var nodes = zTree.getSelectedNodes();
			var parentid = 0;
			if(nodes.length>0){
				var type = nodes[0].nodeType;
				if(type=='company'){
					var companyname = $("#COMPANYNAME",parent.document).val();
					var companyno = $("#COMPANYNO",parent.document).val();
					var departmentid = $("#SQBMID",parent.document).val();
					var departmentname = $("#SQBMMC",parent.document).val();
					var parentid = nodes[0].id;
					var parentname = nodes[0].name;
					//alert(departmentid+"<>"+departmentname);
					if(departmentid==''||departmentname==''){
						alert('部门信息不允许为空');
						return; 
					}
					if(parentid==''){
						alert('请选择添加到指定组织下');
						return;
					}
					/*
					if(!confirm("确认在["+parentname+"]组织下新增名称为["+companyname+"]的公司吗？")){
						return;
					}*/
					var pageurl = "company_quick_addconfirm.action?departmentid="+departmentid+"&departmentname="+encodeURI(departmentname)+"&parentid="+parentid+"&parentname="+encodeURI(parentname)+"&companyCode="+companyno+"&companyName="+encodeURI(companyname);
					art.dialog.open(pageUrl,{ 
					    id: 'checkCompanyInfo', 
					    title: "确认组织信息",
					  	cover:true, 
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						width:550,
						cache:false, 
						lock: true,
						esc: true,
						height:350,   
						iconTitle:false,  
						extendDrag:true,
						autoSize:true,
						close:function(){
						   location.reload();
						}
					});
				}else{
					alert('请选择添加到指定组织下');
				}
			}else{
				alert('请选择要添加的组织');
				return;
			}	
		}
		
		function addDept(){
			var zTree = $.fn.zTree.getZTreeObj("deptTree");
			var nodes = zTree.getSelectedNodes();
			var parentid = 0;
			if(nodes.length>0){
				var type = nodes[0].nodeType;
				var parentdeptid="0";
				var companyid = "0";
				var name = "";
				if(type=='dept'){
					parentdeptid = nodes[0].id;
					companyid = nodes[0].companyId;
					name =  nodes[0].name;
				}else{
					var companyid =  nodes[0].id;
					name =  nodes[0].name;
				}
				var departmentid = $("#SQBMID",parent.document).val();
				var departmentname = $("#SQBMMC",parent.document).val();
					if(!confirm("确认在["+name+"]下，增加["+departmentname+"]的部门吗？")){
						return;
					} 
       				$.post('quickAddDepartment.action',{companyId:companyid,parentdeptid:parentdeptid,departmentid:departmentid,departmentname:departmentname},function(response){
   	 						if(response=='success'){ 
   	 							alert('账户添加成功');
   	 							this.location.reload();
   	 						}else{
   	 							alert(response); 
   	 						}
   					});
				
			}else{
				alert('请选择要添加的组织部门');
				return;
			}
		}
		
		function addUser(){
			var zTree = $.fn.zTree.getZTreeObj("deptTree");
			var nodes = zTree.getSelectedNodes();
			var parentid = 0;
			if(nodes.length>0){
				var type = nodes[0].nodeType;
				if(type=='dept'){
					var userid = $("#SQRZH",parent.document).val();
					var username = $("#SQRXM",parent.document).val();
					var roleid = $("#ROLEID",parent.document).val();
					var deptid = nodes[0].id;
					var deptname =nodes[0].name; 
					var postsid = $("#POSTSID",parent.document).val();
					var postsname = $("#POSTSNAME",parent.document).val();
					var email = $("#EMAIL",parent.document).val();
					if(deptid==''||deptname==''){
						alert('部门信息不允许为空');
						return;
					}
					if(roleid==''){
						alert('请选择要添加的角色');
						return;
					}
					if(userid==''||username==''){
						alert('用户信息不允许为空');
						return;
					}
					if(!confirm("确认在部门名称为["+deptname+"]的部门下，增加["+username+"]的账号吗？")){
						return;
					}
					
       				$.post('quickAddUserAccount.action',{userid:userid,username:username,roleid:roleid,departmentid:deptid,departmentname:deptname,postsid:postsid,postsname:postsname,email:email},function(response){
   	 						if(response=='success'){ 
   	 							alert('账户添加成功');
   	 						}else{
   	 							alert(response); 
   	 						}
   					});
				}else{
					alert('请选择添加的部门');
				}
			}else{
				alert('请选择要添加的组织部门');
				return;
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
 	<div region="north"  split="false" border="false"  style="height:35px;overflow:hidden;">
 		<div class="tools_nav">
 		<a href="#" style="margin-left:1px;margin-right:1px"  onclick='addCompany();' class="easyui-linkbutton" plain="true" iconCls="icon-deptbook">添加组织</a>
 		<a href="#" style="margin-left:1px;margin-right:1px"  onclick='addDept();' class="easyui-linkbutton" plain="true" iconCls="icon-deptbook">添加部门</a>
 		<a href="#"  style="margin-left:1px;margin-right:1px" onclick='addUser();' class="easyui-linkbutton" plain="true" iconCls="icon-org-user">添加用户</a>
</div>
 	</div>
    <!-- 导航区 -->
	<div region="center"  split="false" border="false"  style="height:100%;width:230px;padding-left:5px;overflow:hidden; border-top:1px solid #F9FAFD;background:#efefef">
				
							<ul id="deptTree" class="ztree"></ul>
						 <form id="editForm" name="editForm"method="post">
						 		
						 </form> 
    </div>
</body>
</html> 