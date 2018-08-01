<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>测试员工自助首页</title>
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
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
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
			 margin-left:5px;
			 margin-top:5px;
			  border-top:1px solid #ccc;
		}
		.tablist li{
			 line-height:30px;
			 border-bottom:1px solid #ccc;
			 border-right:1px solid #ccc;
			 border-left:1px solid #ccc;
			 padding-left:25px;
			 font-family:微软雅黑;
			  background:#f7f7f7;
			  color:#666;
			  font-weight:bold;
			  text-align:left;
		}
		.tablist li:hover{
			   background-color:#fff;
			   background-image:url(iwork_img/arrow.png);
			   background-repeat:no-repeat;
			   background-position:5px 10px;
			   color:#333;
			 cursor:pointer;
		}
		.tablist .current{
			  background:#fff;
			   color:#333;
			   background-image:url(iwork_img/zoom.png);
			   background-repeat:no-repeat;
			   background-position:5px 7px;
			   border-right:1px solid #fff;
		}
		.tablist .current:hover{
		 background-image:url(iwork_img/zoom.png);
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
		.mainTable{
			width:100%;
		}
		.infoTable{
		  width:100%;
		}
		.infoTable td{
			vertical-align:top;
			padding:3px;
			padding-left:20px;
			width:30xp;
			text-align:left;
			font-size:14px;
			font-family:微软雅黑;
			
		}
		.infoTable td span{
			color:#0000ff;
		}
		
		.groupTitle{
			background:#efefef;
			padding:5px;
			padding-left:25px;
			font-family:微软雅黑;
			font-size:14px;
			 background-image:url(iwork_img/orange_grid.png);
			   background-repeat:no-repeat;
			   background-position:5px 5px;
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
			 //员工履历
			 if(label=='baseinfo'){
				$("#ifm").attr("src","iwork_staff_info.action");
			 //请假记录
			 }else if(label=='qingjia'){
			    $("#ifm").attr("src","iwork_staff_info.action");
			 //销假记录
			 }else if(label=='xj'){
			    $("#ifm").attr("src","iwork_staff_info.action");
			 //组织信息
			 }else if(label=='org'){
			    $("#ifm").attr("src","iwork_staff_info.action");
			 //我的资产列表
			 }else if(label=='eam'){
			    $("#ifm").attr("src","iwork_staff_assets.action");
			 //考勤记录
			 }else if(label=='kaoqin'){
			    $("#ifm").attr("src","iwork_staff_info.action");
			 //自我介绍
			 } else if(label=='myshow'){
			 	$("#ifm").attr("src","iwork_staff_info.action");
			 }
	    });
	});
	
	function doSearch(){
		var itemno = $("#itemno").val();
		var itemname = $("#itemname").val();
		 var year = $('select').val();
		$("#ifm").attr("src","iwork_contract_ContractList.action?year="+year+"&itemno="+itemno+"&itemname="+itemname);
	}
	</script>
</head>
<body class="easyui-layout">
		 <div region="north" border="false"  style="overflow:hidden;" > 
		 	<div class="topHead">
		 		<table width="100%" cellspacing="0" cellpadding="0" border="0">
		 			<tr>
		 				<td>
		 					<div class="maintitle">员工自助</div>
		 				</td>
		 				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		 				<td style="text-align:right;padding-right:10px;">
		 					<div style="float:right">
		 					<a href="javascript:addItem();" class="easyui-linkbutton" plain="false" iconCls="icon-edit">预留按钮</a>&nbsp;&nbsp;
							<a href="javascript:addItem();" class="easyui-linkbutton" plain="false" iconCls="icon-edit">预留按钮</a>&nbsp;&nbsp;
							  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							</div>
		 				</td>
		 			</tr>
		 		</table>
		 	</div>
        </div>
         <div region="west"  class="leftDiv" border="false" style="border-right:1px solid #fff;background-color:#efefef;width:200px;padding-top:5px;text-align:center;">
			 		<div id="user_image_div">
							<img width="150" id="user_image" src='iwork_img/default_userImg.jpg' alt='用户照片' title='用户照片' onerror="this.src='iwork_img/nopic.gif'" name='photoUpload' id='photoUpload' style="border:1px solid #e5e5e5;margin:3px;">
					</div>
		 		<ul class="tablist">
		 		<!--
		 			<li class="current" id="contract_list">合同首页</li>
		 			<li id="query_list">合同查询</li>
		 			<li id="query_customer">按客户统计查询</li>
		 			<li id="query_supplier">按供应商统计查询</li>
		 			<li id="query_receivable">应收账款统计查询</li>
		 			<li id="query_pay">应付账款统计查询</li>
		 			<s:if test="hashMap.IS_SUB==1">
		 			<li id="options">设置</li>
		 			</s:if>
		 			-->
		 			
		 			<li class="current" id=baseinfo>员工履历</li>
					<li id="qingjia">请假记录</li>
					<li id="xj">销假记录</li>
					<li id="org">组织信息</li>
					<li id="eam">我的资产</li>
					<li id="kaoqin">考勤说明</li>
					<li id="myshow">自我介绍</li>
		 			
		 		</ul>
        </div>  
            <div region="center" border="false" s	tyle="background: #fff; border-top:1px solid #efefef;">
            	 <iframe width='100%' height='99%' src ="iwork_staff_info.action" frameborder="0" marginheight="0" marginwidth="0" frameborder="0" scrolling="auto" id="ifm" name="ifm"\>
            </div>
            <s:hidden id="recNum" name="recNum"></s:hidden>
            <s:hidden id="draftNum" name="draftNum"></s:hidden>
            <s:hidden id="issupport" name="issupport"></s:hidden>
</body>
</html>
