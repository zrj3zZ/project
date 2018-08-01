<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script> 
    <script type="text/javascript" src="iwork_js/index.js"></script>    
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script>     
	<style>
		.topHead{
		background:url(admin/img/index/head_bg.jpg) repeat-x;
	height:53px;
			border-bottom:3px solid #677e9b;
		}
		.maintitle{
			font-size:20px;
			font-family:黑体;
			font-werght:bold;
			padding:8px;
			color:#fff;
		}
		.maintitle span{
			font-size:26px;
			color:#304d79;
			padding:2px;
		}
		.maintitle span span{
			color:red;
		}
		.tablist{
			list-style-type:none;
			 padding:0px;
			 margin-left:10px;
			 margin-top:5px;
			  border-top:1px solid #ccc;
		}
		.tablist li{
			 line-height:30px;
			 border-bottom:1px solid #ccc;
			 border-right:1px solid #ccc;
			 border-left:1px solid #ccc;
			 padding-left:15px;
			 font-family:微软雅黑;
			  background:#f7f7f7;
			  color:#666;
			  font-weight:bold;
		}
		.tablist li:hover{
			   background:#fff;
			   color:#333;
			 cursor:pointer;
		}
		.tablist .current{
			  background:#fff;
			   color:#333;
			 border-right:1px solid #fff;
		}
		.addItemBtn{ 
			line-height:30px;
			text-align:left;
			padding-left:40px;
			font-size:14px;
			 font-family:微软雅黑;
			 cursor:pointer;
		}
		.addItemBtn span{
			background-image:url(iwork_img/add_obj.gif);
			background-repeat:no-repeat;
			 background-position:20px 5px;
			padding:5px;
			padding-left:40px;
		}
		.addItemBtn:hover span{
			border:1px solid #efefef;
			color:red;
		}
		
	</style>
	
</head>
<body class="easyui-layout">
		 <div region="north" border="false"  style="overflow:hidden;height:60px" > 
		 	<div class="topHead">
		 		<table width="100%" cellspacing="0" cellpadding="0" border="0">
		 			<tr>
		 				<td style="width:50px;">
		 					<img src="admin/img/index/logo.png" alt="" />
		 				</td> 
		 				<td>
		 				<div class="maintitle">三部BPM系统<span style="font-size:30px;color:#fff">审计管理员</span>控制台</div>
		 				</td>
		 				<td></td>
		 				<td style="text-align:right;vertical-align:top;padding:10px;">
		 				<a href="#"  id="loginOut" style="color:#fff"  target="_parent">退出</a></div>
		 				</td>
		 			</tr>
		 		</table>
		 		
		 	</div>
        </div>
		 <div region="west"  class="leftDiv" border="false" style="border-right:1px solid #fff;background-color:#efefef;width:200px;padding-top:10px;">
		 		<ul class="tablist">
		 			<li class="current" id="auditBoard">审计面板</li>
		 			<li id="login_log">用户登陆日志</li>
		 			<li id="menu_log">菜单操作日志</li>
		 			<li id="oprate_log">管理员操作日志</li>
		 			<li id="org_log">组织结构日志</li>
		 		</ul>
        </div>
            <div region="center" border="false" s	tyle="background: #fff; border-top:1px solid #efefef;">
            	 <iframe width='100%' height='99%' src ="purgroup_index.action" frameborder="0" marginheight="0" marginwidth="0" frameborder="0" scrolling="auto" id="ifm" name="ifm"\>
            </div>
            <s:hidden id="recNum" name="recNum"></s:hidden>
            <s:hidden id="draftNum" name="draftNum"></s:hidden>
            <s:hidden id="issupport" name="issupport"></s:hidden>
           
</body>
</html>
