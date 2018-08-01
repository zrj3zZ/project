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
	<script>
	$(function(){
		$('.tablist li').click(function(obj){
			 $(".tablist li").each(function(item){
				    $(this).removeClass("current");
			  });
			 $(this).addClass("current");
			  var label = $(this).attr('id');
			 if(label=='menu_log'){ 
				$("#ifm").attr("src","ireport_rt_index.action?reportUUID=d4966e83-dfa9-47e3-9e22-13376b323578");
			 }else if(label=='sys_org_log'){ 
			    $("#ifm").attr("src","sys_operation_log.action?isDoSearch=false&logType=0");
			 }else if(label=='security_login_log'){
			    	$("#ifm").attr("src","ireport_rt_index.action?reportId=42&reportType=2");
			 }else if(label=='sys_login_log'){
			    	$("#ifm").attr("src","ireport_rt_index.action?reportId=46&reportType=2");
			 }else if(label=='security_operator_log'){
			    	$("#ifm").attr("src","ireport_rt_index.action?reportId=41&reportType=2");
			 }else if(label=='sys_oparate_log'){
			    	$("#ifm").attr("src","ireport_rt_index.action?reportId=45&reportType=2");
			 }
	    });
	    
	    $(".tablist li:eq(0)").trigger("click");
	   });
	    function loginOut(){
	     if(confirm('您确定要退出本次登录吗?')){
		                        $.ajax({
		        	       			url:'logout.action', //后台处理程序
		        	       			type:'post',         //数据发送方式
		        	       			dataType:'json'
		             			});
		         location.href = 'login.action';
		  }
	    }
	</script>
</head>
<body class="easyui-layout">
		 <div region="north" border="false"  style="overflow:hidden;height:60px" > 
		 	<div class="topHead">
		 		<table width="100%" cellspacing="0" cellpadding="0" border="0">
		 			<tr>
		 				<td style="width:50px;">
		 					<img src="iwork_img/mornitor.gif" alt="" />
		 				</td> 
		 				<td>
		 				<div class="maintitle">科研生产管理平台<span style="font-size:30px;color:#fff">系统管理员</span>控制台</div>
		 				</td>
		 				<td></td>
		 				<td style="text-align:right;vertical-align:top;padding:10px;">
		 				<!--  <a href="javascript:loginOut()" id="loginOut" style="color:#fff" >退出</a>--></div>
		 				</td>
		 			</tr>
		 		</table>
		 		
		 	</div> 
        </div>
		 <div region="west"  class="leftDiv" border="false" style="border-right:1px solid #fff;background-color:#efefef;width:200px;padding-top:10px;">
		 		<ul class="tablist">
		 		<!--<li class="current" id="auditBoard">审计面板</li> -->
		 			<li id="sys_login_log">用户设置</li>
		 			<li id="security_login_log">安全管理员登录日志</li>
		 			<li id="security_login_log">审计管理员登录日志</li>
		 		 	<li id="sys_org_log">组织机构管理日志</li>
		 		</ul>
        </div>
            <div region="center" border="false" s	tyle="background: #fff; border-top:1px solid #efefef;">
            	 <iframe width='100%' height='99%' src ="sys_operation_log.action?isDoSearch=false&logType=0" frameborder="0" marginheight="0" marginwidth="0" frameborder="0" scrolling="auto" id="ifm" name="ifm"\>
            </div>
           
</body>
</html>
