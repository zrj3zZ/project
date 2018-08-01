<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="iwork" uri="/WEB-INF/sys-commonsTags.tld" %>
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
			
		}
		function addOra(){
				if(!confirm("确认将当前组织及部门，添加至证券业务管理系统中吗？")){
						return;
				}
		}
		function addCompany(){
			var parentid = 0;
					var companyname = $("#companyName").val();
					var companyno = $("#companyCode").val();
					var departmentid = $("#departmentid").val();
					var departmentname = $("#departmentname").val(); 
					var parentid = $("#parentid").val();
					var parentname = $("#parentname").val();
					
					if(departmentid==''||departmentname==''){
						alert('部门信息不允许为空');
						return; 
					}
					if(parentid==''){
						alert('请选择添加到指定组织下');
						return;
					}
					if(companyname==''||companyno==''){
						alert('组织信息不允许为空');
						return;
					}
					if(!confirm("确认在["+parentname+"]组织下新增名称为["+companyname+"]的公司吗？")){
						return;
					}
       				$.post('company_quick_add.action',{companyName:companyname,companyCode:companyno,parentid:parentid,departmentid:departmentid,departmentname:departmentname},function(response){
   	 						if(response==''||response=='success'){ 
   	 							alert('组织添加成功');
   	 						}else{
   	 							alert(response); 
   	 						} 
   					});
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
		.border{
			padding:10px;
		}
		.border td{
			padding:5px;
			font-size:14px;
			font-family:微软雅黑;
		}
		input{
			height:25px;
			padding:2px;
			padding-left:5px; 
			width:250px;
			font-size:12px;
			color:#0000ff;
		}
	</style>
</head>
 <body class="easyui-layout">
 	<div region="south"  split="false" border="false"  style="height:35px;text-align:left;overflow:hidden;">
 		<a href="#" style="margin-left:1px;margin-right:1px"  onclick='addCompany();' class="easyui-linkbutton" plain="false" iconCls="icon-ok">确定</a>
 		<a href="#"  style="margin-left:1px;margin-right:1px" onclick='addUser();' class="easyui-linkbutton" plain="false" iconCls="icon-cancel">取消</a>
 	</div>
    <!-- 导航区 -->
	<div region="center"  split="false" border="false"  style="height:100%;width:230px;padding-left:5px;overflow:hidden; border-top:1px solid #F9FAFD;">
		 <form id="editForm" name="editForm"method="post">
		 	<div class="border">
		 		<table width="100%">
		 			
		 			<tr>
		 				<td style="text-align:right" >上级组织单位名称</td>
		 				<td><s:textfield name="parentname" id="parentname"  cssStyle="background:#efefef;color:#666" readOnly="readOnly" theme="simple"></s:textfield><s:hidden name="parentid" id="parentid" ></s:hidden></td>
		 			</tr>
		 			<tr>
		 				<td style="text-align:right">添加组织名称</td>
		 				<td><s:textfield name="companyName" id="companyName"  theme="simple"></s:textfield><s:hidden name="companyCode"  id="companyCode"  theme="simple"></s:hidden></td>
		 			</tr>
		 			<tr>
		 				<td style="text-align:right">添加部门名称</td>
		 				<td><s:textfield name="departmentname"  id="departmentname"  theme="simple"></s:textfield><s:hidden name="departmentid" id="departmentid"  theme="simple"></s:hidden></td>
		 			</tr>
		 		</table>
		 	</div>
		 		
		 </form> 
    </div>
</body>
</html> 