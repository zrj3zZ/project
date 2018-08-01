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
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script> 
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script>     
	<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
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
		$("#pp").pagination({
			    total:<s:property value="total"/>, 
			    pageNumber:<s:property value="pageNumber"/>,
			    pageSize:<s:property value="pageSize"/>,
			    onSelectPage:function(pageNumber, pageSize){
			    	var pageUrl="iwork_contract_ContractList.action?pageSize="+pageSize+"&pageNumber="+pageNumber;
					window.location.href = pageUrl;
				}
			});
	  });
	$(function(){
		$('.tablist li').click(function(obj){
			 $(".tablist li").each(function(item){
				    $(this).removeClass("current");
			  });
			 $(this).addClass("current");
	    });
	    trace();
	});
	function trace(){
	      //var pageUrl="iwork_contract_ContractList.action";
					//window.location.href = pageUrl;
	}
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
	            	
            	</tr>
            	<s:iterator value="list" status="status">
	            	<tr>
	            	
	            		<td>
		            		<s:if test="IS_SUB==1">
		            		<a id="expandBtn<s:property value="PACTNO"/>" href="#" onclick="expand('<s:property value="PACTNO"/>')">
		            		<img src="iwork_img/expand.gif" border="0"></a>
		            		</s:if>
		            		<a  id="unExpandBtn<s:property value="PACTNO"/>" style="display:none" href="#" onclick="unExpand('<s:property value="PACTNO"/>')">
		            		<img src="iwork_img/unExpand.gif" border="0"></a>
	            		</td>
	            		<td><a href="iwork_contract_ContractInstanceid.action?instanceId=<s:property value="INSTANCEID"/>&itemno=<s:property value="PACTNO"/>&itemname=<s:property value="PACTTITLE"/>" target="_self"><s:property value="PACTNO"/></a></td>
	            		<td><s:property value="PACTTITLE"/></td>
	            		<td><s:property value="ADMIN"/></td>
	            		<td>
	            			<s:property value="PACTSUN"/>
	            			<s:if test="CURRENCY==1">人民币</s:if>
	            			<s:if test="CURRENCY==2">美元</s:if>
	            			<s:if test="CURRENCY==3">英镑</s:if>
	            			<s:if test="CURRENCY==4">港币</s:if>
	            			<s:if test="CURRENCY==5">欧元</s:if>
	            		</td>
	            		<td><s:property value="FROMDATE"/></td>
	            		
	            	</tr>
	            	<tr id="subItem<s:property value="PACTNO"/>" style="display:none">
	            	<td colspan="7" style="padding-left:20px;"></td>
	            	</tr>
            	</s:iterator>
            	
            </table>
            <s:if test='list.size==0'><p></p>
            	<span style="color:#666666;font-family:微软雅黑;display:block;font-size:14px;text-align:center;line-height:21px;white-space:normal;background-color:#FFFFFF;">没有对应数据</span>
            </s:if>
            </div>
            
            <s:if test="list.size!=0">
            <div region="south" border="false" style="height: 32px;">
			<div id="pp"
				style="background: url('../iwork_img/engine/tools_nav_bg.jpg') repeat-x; border: 1px solid #ccc;"></div>
		</div> 
		</s:if>
		
            <s:hidden id="recNum" name="recNum"></s:hidden>
            <s:hidden id="draftNum" name="draftNum"></s:hidden>
</body>
</html>
