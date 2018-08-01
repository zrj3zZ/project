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
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script>
<script language="javascript" src="iwork_js/org/department_list.js"></script>
	<script language="JavaScript">   
		function edit(type,instanceid){
			if(type=="group"){
				parent.editCategory(instanceid);
			}else if(type=="category"){
				parent.editDem(instanceid);
			}
		}
		
		function removeItem(instanceid){
			var r=confirm("确定执行删除操作吗");
			  if(r){
			  	$.post('iwork_fi_subject_remove.action',{instanceid:instanceid},function(data){
 			    	if(data=='success'){ 
 			    		alert("删除成功");
 			    	}else{ 
 			    		alert("删除操作异常,请稍后再试");
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
    <s:iterator value="groupList" status="status">
       <tr>
       	<td><s:property value="#status.count"/></td>
       	<td><img src="iwork_img/dtree/folder.gif"></td>
       	<td><s:property value="FZBH"/></td> 
       	<td><s:property value="FZMC"/></td>  
       	<td>
       		 <a href="javascript:edit('group',<s:property value="INSTANCEID"/>)">编辑</a>
       		 <a href="javascript:removeItem(<s:property value="INSTANCEID"/>)">删除</a>
       	</td>
       </tr>
    </s:iterator>
    <s:iterator value="categoryList" status="status">
       <tr>
       	<td><s:property value="#status.count"/></td>
       	<td><img src="iwork_img/fi_type.gif"></td>
       	<td><s:property value="FYLBBH"/></td> 
       	<td><s:property value="FYLBMC"/></td> 
       	<td>
       	 <a href="javascript:edit('category',<s:property value="INSTANCEID"/>)">编辑</a>
       		 <a href="javascript:removeItem(<s:property value="INSTANCEID"/>)">删除</a>
       	</td>
       </tr>
    </s:iterator>
</table>
</body>
</html>
