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
	<style>
		.topHead{
			height:40px;
			background:#fff;
			border-bottom:3px solid #677e9b;
		}
		.maintitle{
			font-size:25px;
			font-family:黑体;
			font-werght:bold;
			padding:8px;
			color:#304d79;
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
	<script>
		function initOrg(){
			 var r=confirm("是否同步当前系统用户及组织到微信平台？")
			  if (r==true){
			    $.post('weixin_org_init.action',{},function(response){
    	 						if(response!='success'){ 
    	 							alert('移除错误，请稍候重试');
    	 						}
    					});
			  }
		}
	</script>
</head>
<body class="easyui-layout">
		 <div region="north" border="false"  style="overflow:hidden;" > 
		 	<div class="topHead">
		 		<table width="100%" cellspacing="0" cellpadding="0" border="0">
		 			<tr>
		 				<td>
		 					<div class="maintitle">微信设置</div>
		 				</td>
		 			</tr>
		 		</table>
		 	</div>
        </div>
		 <div region="west"  class="leftDiv" border="false" style="border-right:1px solid #fff;background-color:#efefef;width:200px;padding-top:10px;">
		 		<ul class="tablist">
		 			<li class="current" id="contract_list">组织初始化</li>
		 			<li id="menu_list">菜单设置</li>
		 			<li id="options">设置</li>
		 		</ul> 
        </div>  
            <div region="center" border="false" style="background: #fff; border-top:1px solid #efefef;">
            	 <div class="tools_nav">
				<a  href="#" style="margin-left:1px;margin-right:1px"  onclick='initOrg()' text='Ctrl+s' class="easyui-linkbutton" plain="true" iconCls="icon-save">组织机构同步</a>
				<a href="#" style="margin-left:1px;margin-right:1px"  onclick='location.reload();' class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		</div>
            	 
            </div>
            <s:hidden id="recNum" name="recNum"></s:hidden>
            <s:hidden id="draftNum" name="draftNum"></s:hidden>
            <s:hidden id="issupport" name="issupport"></s:hidden>
</body>
</html>
