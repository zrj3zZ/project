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
			 border-right:1px solid #ccc;
		}
		.tablist li{
			 line-height:30px;
			 border-left:1px solid #ccc;
			 border-right:1px solid #ccc;
			 border-top:1px solid #ccc;
			 padding-left:15px;
			 font-family:微软雅黑;
			  background:#efefef;
			  color:#666;
			  font-weight:bold;
			  width:80px; float:left;
			  margin-right:2px;
		}
		.tablist li:hover{
			   background:#fff;
			   color:#333;
			 cursor:pointer;
		}
		.tablist .current{
			  background:#fff;
			   color:#0000ff;;
			 border-bottom:1px solid #fff;
		}
		
		
	</style>
	<script>
	$(function(){
		$('.tablist li').click(function(obj){        
			 $(".tablist li").each(function(item){
				    $(this).removeClass("current");
			  });
			 $(this).addClass("current");
			 var label = $(this).text();
			 var itemno = $("#itemno").val();
			 var itemname = $("#itemname").val();
			 if(label=='合同基本信息'){
			 	$("#ifm").attr("src","iwork_contract_basecontract.action?itemno="+itemno+"&itemname="+itemname);
			 }else if(label=='合同计划'){
				$("#ifm").attr("src","iwork_contract_planlist.action?itemno="+itemno+"&itemname="+itemname);
			 }else if(label=='发票计划'){
				$("#ifm").attr("src","iwork_contract_ivoicePlanlist.action?itemno="+itemno+"&itemname="+itemname);
			 }else if(label=='交货计划'){
			    $("#ifm").attr("src","iwork_contract_deliveryPlanlist.action?itemno="+itemno+"&itemname="+itemname);	 
			 }else if(label=='合同执行情况'){
			    $("#ifm").attr("src","iwork_contract_contractPerformlist.action?itemno="+itemno+"&itemname="+itemname);	 
			 }else if(label=='发票执行情况'){
			    $("#ifm").attr("src","iwork_contract_ivoicePerformlist.action?itemno="+itemno+"&itemname="+itemname);	 
			 }else if(label=='交货执行情况'){
			    $("#ifm").attr("src","iwork_contract_deliveryPerformlist.action?itemno="+itemno+"&itemname="+itemname);	 
			 }
	    });
	});
	</script>
</head>
<body >
	 <ul class="tablist">
		 			<li class="current">合同基本信息</li>
		 			<li >合同计划</li>
		 			<li>发票计划</li>
		 			<li>交货计划</li>
		 			<li>合同执行情况</li>
		 			<li>发票执行情况</li>
		 			<li>交货执行情况</li>
		 		</ul>
            	 <iframe width='100%' height='99%' src ="iwork_contract_basecontract.action?itemno=<s:property value="itemno"/>&instanceId=<s:property value="instanceId"/>" frameborder="0" marginheight="0" marginwidth="0" frameborder="0" scrolling="auto" id="ifm" name="ifm">
            	 </iframe>
            	 <s:form name="editForm" theme="simple">
           		  <s:hidden name="itemno" id="itemno"></s:hidden>
           		  <s:hidden name="itemname" id="itemname"></s:hidden>
            </s:form>
            
</body>
</html>
