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
		.contractTable{
			border:1px solid #f7f7f7;
		}
		.contractTable th{
			height:25px;
			background:#efefef;
			font-size:14px;
			font-family:微软雅黑;
			padding:2px;
		}
		.contractTable td{
			height:25px;
			background:#fff;
			font-family:微软雅黑;
			padding:2px;
			border-bottom:1px solid #f7f7f7;
			color:#666;
		}
		.contractTable tr:hover td{
			background:#f7f7f7;
			border-bottom:1px solid #efefef;
			cursor:pointer;
		}
		.subTable{
			border:1px solid #f7f7f7;
		}
		.subTable th{
			height:25px;
			background:#FFFFEE;
			font-size:12px;
			font-family:微软雅黑;
			padding:2px;
			color:#959591;
			border-bottom:1px solid #efefef;
		}
		.subTable td{
			height:25px;
			background:#fff;
			font-family:微软雅黑;
			padding:2px;
			border-bottom:1px solid #f7f7f7;
			color:#959591;
		}
		.subTable tr:hover td{
			background:#f7f7f7;
			border-bottom:1px solid #efefef;
			cursor:pointer;
		}
	</style>
	<script>
	$(function(){
		$('.tablist li').click(function(obj){
			 $(".tablist li").each(function(item){
				    $(this).removeClass("current");
			  });
			 $(this).addClass("current");
	    });
	});
	function expand(id){
		$("#expandBtn"+id).hide();
		$("#unExpandBtn"+id).show();
		var pageurl ="iwork_contract_showsublist.action";
		var submitdata = {itemno:id};
		  $.post(pageurl,submitdata,function(data)
		  {
			   var item = $("#subItem"+id).show();
				$("#subItem"+id).find("td").html(data);
		   });  
		
	} 
	function unExpand(id){
			    	$("#subItem"+id).hide();
			    	$("#expandBtn"+id).show();
			    	$("#unExpandBtn"+id).hide();
	}
	
	</script>
</head>
<body class="easyui-layout">
            <div region="center" border="false" style="background: #fff; border-top:1px solid #efefef;padding:5px;">
            <table width="100%" class="contractTable">
            
            	<tr>
            		<th style="width:2%"></th>
	            	<th style="width:15%">合同编号</th>
	            	<th style="width:40%">合同名称</th>
	            	<th style="width:10%">负责人</th>
	            	<th style="width:10%">合同总金额</th>
	            	<th style="width:10%">合同截止日期</th>
	            	<th style="width:10%">操作</th>
            	</tr>
            	
            	<tr>
            		对不起您访问的地址出错，请联系管理员！
            	</tr>
            	
            	
            	
            </table>
            </div>
            <s:hidden id="recNum" name="recNum"></s:hidden>
            <s:hidden id="draftNum" name="draftNum"></s:hidden>
</body>
</html>
