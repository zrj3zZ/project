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
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style type="text/css">
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
		}.maintitle span span{
			color:red;
		}
		.leftDiv{
			background:#f5f5f5;
			width:150px;
		} 
		.leftTopDiv{
			border-bottom:1px solid #e2e2e2;
			padding:5px;
		}
		.leftTopItem{
			line-height:30px;
			font-family:微软雅黑;
			font-size:14px;
			color:#3b5999;
			font-weight:bold;
			padding-left:45px;
			list-style-type:none;
		}
		.leftTopItem:hover{
			background-color:#dce0e1;
			cursor:pointer
		}
		.leftTopWriteIcon{
			background-image:url(iwork_img/email/work.gif);
			background-repeat:no-repeat;
			 background-position:20px 5px;
			 
		}
		.leftBottomWriteIcon{
			background-image:url(iwork_img/man3.gif);
			background-repeat:no-repeat;
			 background-position:20px 5px;
			 
		}
		.leftTopReceverIcon{
			background-image:url(iwork_img/email/mail.gif);
			background-repeat:no-repeat;
			 background-position:20px 5px;
		}
		.leftTopAddressIcon{
			background-image:url(iwork_img/email/book.gif);
			background-repeat:no-repeat;
			background-position:20px 5px;
		}
		.leftMiddleDiv{
			border-bottom:1px solid #e2e2e2;
			padding:5px;
			color:#586C85; 
		}
		.leftMiddleDiv li{
			line-height:24px;
			font-family:微软雅黑;
			font-size:12px;
			font-weight:bold;
			padding-left:20px;
		}
		.leftMiddleItemCurr{
			background-color:#687f9b;
			cursor:pointer;
			color:#fff; 
		}
		.leftMiddleItemCurr a{
			background-color:#687f9b;
			cursor:pointer;
			color:#fff; 
			
		}
		.leftMiddleDiv li:hover{
			background-color:#dce0e1;
			cursor:pointer;
			color:#586C85; 
		}
		.leftBottomDiv{
			border-bottom:1px solid #e2e2e2;
			padding:5px;
			color:#586C85; 
		}
		.leftBottomDiv li{
			line-height:24px;
			font-family:微软雅黑;
			font-size:12px;
			font-weight:bold;
			padding-left:20px;
		}
		.leftBottomDiv li:hover{
			background-color:#dce0e1;
			cursor:pointer;
			color:#586C85; 
		}
		.search_btn {
			background:url(iwork_img/engine/search_btn.png) no-repeat;
			height:26px;
			width:189px;
			cursor:pointer; 
			white-space:nowrap;
			margin-right:10px;
			float:right;
			margin-top:3px;
		}
		.search_btn_onclick {
			background:url(iwork_img/engine/search_btn_onclick.png) no-repeat;
			height:26px;
			width:189px;
			cursor:pointer;
			margin-right:10px;
			float:right;
			margin-top:3px; 
		}
		.search_input {
			background:none;
			border:0 none;
			cursor:pointer;
			width:140px;
			height:20px;
			line-height:20px;
			margin-left:5px;
			margin-top:2px;
		}
		.search_button {
			background:none;
			border:0 none;
			width:40px;
			height:20px;
			cursor:pointer
		}
		.tipsTitle{
			padding:10px;
			color:#666;
			font-family:微软雅黑;
			font-weight:bold;
		}
		.tipsItem{
		  padding:5px;	
		  color:#999;
		  font-weight:normal;
		}
	</style>
	
	<script>
	$(function(){
		$(".leftTopDiv li").click(function(obj){
			 var label = $(this).attr('id');
			  $(".leftMiddleDiv li").each(function(item){
					    $(this).removeClass("leftMiddleItemCurr");
				  });
			 if(label=='newMail'){
				addTab("写邮件","iwork_email_newadd.action","");
			 }else if(label=='doRecive'){
				 $(reciveBox).addClass("leftMiddleItemCurr");
				 $("#ifm").attr("src","iwork_email_reclist.action");
			 }else if(label=='addressbook'){
				 addTab("通讯录","iwork_mail_booklist.action","");
			 }
			
		});
		$('.leftMiddleDiv li').click(function(obj){
				 $(".leftMiddleDiv li").each(function(item){
					    $(this).removeClass("leftMiddleItemCurr");
				  });
				 $(this).addClass("leftMiddleItemCurr");
				  var label = $(this).attr('id');
				  $('#mainFrameTab').tabs('select',"我的邮件列表");
				 if(label=='reciveBox'){
					$("#ifm").attr("src","iwork_email_reclist.action");
				 }else if(label=='starBox'){
					 $("#ifm").attr("src","iwork_mail_list_star.action");
				 }else if(label=='sendBox'){
					 $("#ifm").attr("src","iwork_mail_send_index.action");
				 }else if(label=='draftBox'){
					 $("#ifm").attr("src","iwork_mail_draft_index.action");
				 }else if(label=='delBox'){
					 $("#ifm").attr("src","iwork_mail_del_list.action");
				 }
	    });
		$('.leftBottomDiv li').click(function(obj){
				 $(".leftMiddleDiv li").each(function(item){
					    $(this).removeClass("leftMiddleItemCurr");
				  });
				  var label = $(this).attr('id');
				 if(label=='searchBox'){
				 	addTab("查询邮件","iwork_email_adv_search.action","");
				 }
	    });
	});
	
	
	//添加页签
	function addTab(subtitle,url,icon){ 
		if(!$('#mainFrameTab').tabs('exists',subtitle)){
			$('#mainFrameTab').tabs('add',{
				title:subtitle,
				content:createFrame(url),
				closable:true,
				icon:icon
			}); 
		}else{
		    //删除已存在的页签
			$('#mainFrameTab').tabs('close',subtitle);
			//重新创建一个新的标签
			$('#mainFrameTab').tabs('add',{
				title:subtitle,
				content:createFrame(url),
				closable:true,
				icon:icon
			}); 
		}
		return false;
	}
	
	function createFrame(url)
		{
			var s = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
			return s; 
		}
	function closeTab(subtitle){
		$('#mainFrameTab').tabs('close',subtitle);
	}
	function closeCurrTab(){
		var pp = $('#mainFrameTab').tabs('getSelected');
		var tab = pp.panel('options').tab;
		closeTab(tab.text());
		document.getElementById('ifm').contentWindow.location.reload(true);
	}
	//查询回车事件
	 function enterKey(){
	 	if (window.event.keyCode==13){
	 		 doSearch();
	 		return;
	 	}
	 }
	 function doSearch(){
	 	var searchTxt = $("#searchTxt").val();
	 	if(searchTxt==""){
	 		alert("请输入查询关键字");
	 		return;
	 	}else{ 
	 		searchTxt = searchTxt.toUpperCase();
	 		var url = "iwork_email_search.action?searchkey="+encodeURI(searchTxt);
	 		$('#editForm').attr('action',url); 
	 		$('#editForm').attr('target',"_self");
	 		$('#editForm').submit(); 
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
		 					<div class="maintitle"><span>H<span>i</span>mail</span>邮箱</div>
		 				</td>
		 				
		 				<td style="text-align:right;padding-right:100px;">
		 					<div style="float:right">
		 					
		 					<span class="search_btn" id="search_btn">
							  <input  type="text" name="searchTxt" id="searchTxt" onKeyDown="enterKey();if(window.event.keyCode==13) return false;" class="search_input"/>
							  <input  onclick="doSearch();" type="button" class="search_button" value="&nbsp;"/> 
							  </span>
							</div>
		 				</td>
		 			</tr>
		 		</table>
		 		
		 	</div>
        </div>   
		 <div region="west"  class="leftDiv" border="false" >
		 	<ul class="leftTopDiv"> 
			 	<li id="newMail" class="leftTopItem leftTopWriteIcon">写信</li>
			 	<!--<li id="doRecive"  class="leftTopItem leftTopReceverIcon" >收信</li>-->
			 	<li id="addressbook" class="leftTopItem leftTopAddressIcon">通讯录</li>
		 	</ul>
		 	<ul  class="leftMiddleDiv">
		 		<li id="reciveBox" class="leftMiddleItemCurr">收件箱<label id="recTotal"></label></li> 		
		 		<li id="starBox">标星邮件</li>
		 		<li id="sendBox">已发送</li>
		 		<li id="draftBox">草稿箱<label id="total"></label></li>
		 		<li id="delBox">已删除</li>
		 	</ul>
		 	<ul  class="leftBottomDiv">
		 		<li id="searchBox"><img src="iwork_img/find.png" border="0"/>&nbsp;邮件查询</li> 		
		 	</ul>
		 	<!-- 
		 	<div class="tipsTitle">
		 		邮箱信息
		 		<div class="tipsItem">邮箱容量：2G</div>
		 		<div class="tipsItem">已使用：</div>
		 	</div>
		 	 -->
        </div>  
            <div region="center" border="false" style="background: #fff; border:1px solid #fff">
	            <div id="mainFrameTab"  class="easyui-tabs" fit="true" border="false">
				<div id="mainFrameTabs"  title="我的邮件列表"  cache="false" > 
						 <iframe width='100%' height='99%' src ="iwork_email_reclist.action" frameborder="0" marginheight="0" marginwidth="0" frameborder="0" scrolling="auto" id="ifm" name="ifm"></iframe>
					</div>
	            </div> 
            </div>
            <s:hidden id="recNum" name="recNum"></s:hidden>
            <s:hidden id="draftNum" name="draftNum"></s:hidden>
</body>
</html>
