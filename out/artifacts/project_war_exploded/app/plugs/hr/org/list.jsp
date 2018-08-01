<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.iwork.app.navigation.directory.model.SysNavDirectory" %>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap" %>
<%@page import="com.iwork.app.navigation.sys.model.SysNavSystem" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Map" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head><title>目录管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/linkbutton.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script language="javascript" src="iwork_js/commons.js"></script>
<script language="javascript" src="iwork_js/org/department_list.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script language="JavaScript">   
		function editDept(instanceid){
			var demid = parent.document.getElementById("dept_demid").value;
			var formid = parent.document.getElementById("dept_formid").value; 
			parent.editLegalDept(demid,formid,instanceid); 
		}
		function removeItem(instanceid){
			var r=confirm("确定执行删除操作吗");
			  if (r==true){
				  var url = "iwork_hr_legal_remove.action";
					 $.post(url,{instanceid:instanceid},function(data){
				         if(data=='success'){
				        	 location.reload(); 
				         }else{
				             art.dialog.tips("保存失败",1);
				         }
				    }); 
			    }
			return;
		}
	</script>
	<style type="text/css">
	body{
		padding:5px;
	}
		.table3{
			border:1px solid #efefef;
		}
		.table3 th{
			height:25px;
			border-bottom:1px solid #efefef;
			background-color:#fcfcfc;
			padding-left:10px;
			text-align:left;
		}
		.table3 td{ 
			height:25px;
			text-align:left;
			padding-left:10px;
		}
		.table3 tr:hover{
			background-color:#fcfcfc;
		}
	</style>
</head>
<body>
	<table cellpadding="0" cellspacing="0" class="table3" >
    <tr>
        <th  width="30">序号</th>
        <th width="10"></th>
        <th width="100" >编号</th>
        <th width="200"  >名称</th> 
        <th width="163"  >操作</th>
    </tr>
    <s:iterator value="deptlist" status="status">
       <tr>
       	<td><s:property value="#status.count"/></td>
       	<td><img src="iwork_img/km/treeimg/ftv2link01.gif"></td>
       	<td><s:property value="DEPARTMENTID"/></td> 
       	<td><s:property value="DEPARTMENTNAME"/></td> 
       	<td>
       		 <a href="javascript:editDept(<s:property value="INSTANCEID"/>)">编辑</a>
       		 <a href="javascript:removeItem(<s:property value="INSTANCEID"/>)">删除</a>
       	</td>
       </tr>
    </s:iterator>
    <s:iterator value="userlist" status="status">
       <tr>
       	<td><s:property value="#status.count"/></td>
       	<td><img src="iwork_img/user.png"></td>
       	<td><s:property value="USERID"/></td> 
       	<td><s:property value="USERNAME"/></td> 
       	<td>
       		 <a href="javascript:removeItem(<s:property value="INSTANCEID"/>)">删除</a>
       	</td>
       </tr>
    </s:iterator>
</table>
</body>
</html>
