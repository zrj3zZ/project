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
   	<link rel="stylesheet" href="iwork_js/kindeditor/themes/simple/simple.css" />
	<link rel="stylesheet" href="iwork_js/kindeditor/plugins/code/prettify.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/autocomplete/jquery.autocomplete.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<script charset="utf-8" src="iwork_js/kindeditor/kindeditor.js"></script>
	<script charset="utf-8" src="iwork_js/kindeditor/lang/zh_CN.js"></script>
	<script charset="utf-8" src="iwork_js/kindeditor/plugins/code/prettify.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/autocomplete/jquery.autocomplete.min.js"  charset="utf-8" ></script>
	<script type="text/javascript" src="iwork_js/plugs/emaileditor.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
	$(document).ready(function(){
	
		//$('.address').bind('click', function() { 
		//art.dialog({
		//    follow:$(this),
		//    content: '让对话框跟着某个元素，一个元素同时只能出现一个对话框'
		//});
		//});
	});
	//发送动作
	function sentLetter(){
   		var form =  document.getElementById('editForm');
	    var taskid = $("#mailId").val();
	   	var flag = checkValidate();
	   	var type = -2;
	   	if(flag==true){
	   		form.action ="iwork_email_dosend.action?taskid ="+taskid;
	   		form.submit();
	   		window.opener.location.href="app/plugs/email/send_tips.jsp";
			window.close();
	   	}else{
	   		return false;
	   	}
   }
   //转发邮件
   function toWrite(){
   		var forwardid = $("#mailId").val();
  		var taskid = $("#taskid").val();
		window.location.href="iwork_email_forward.action?mailId="+forwardid+"&taskid="+taskid;
   }
	//回复邮件
	function toReply(){
	    var forwardid = $("#mailId").val();
	 	var taskid = $("#taskid").val();
	    window.location.href="iwork_email_reply.action?replyId="+forwardid+"&taskid="+taskid;//taskid
	}
	
	function toReplyAll(){
		var forwardid = $("#mailId").val();
	    var taskid = $("#taskid").val();
	  window.location.href="iwork_email_replyall.action?taskid="+taskid+"&replyId="+forwardid;
	
	}
	
	function redirectDiv(){
		$('html, body').animate({scrollTop: $(document).height()}, 300); 
		
	}
	
	function showSendStatus(){
		 	var mailId = $("#mailId").val();
		 	var html = $("#sendStatusDiv").html();
		 	if(html==''){
			 	$.post("iwork_email_showSendStatus.action",{mailId:mailId},function(data){
			          $("#sendStatusDiv").html(data);
			          $("#sendStatusDiv").show();
			     });
		 	}else{
		 		$("#sendStatusDiv").toggle();
		 	}
		     
	}
	
	function mailUndo(){
		 	var mailId = $("#mailId").val();
		 	if(confirm("确认撤销当前发送的邮件吗？撤销后收件人将接收到邮件撤销的系统消息")){
		 		$.post("iwork_email_undo.action",{mailId:mailId},function(data){
		 			if(data=='success'){
		 				alert('邮件撤销成功');
		 				parent.closeCurrTab();
		 			}else if(data=='nopurview'){
		 				alert('当前用户无权限撤销!');
		 			}else if(data=='read'){
		 				alert('邮件撤销成功，已阅用户无法撤销!');//当前邮件接收人已阅读，无法撤销!
		 			}
			     });
		 	}
	}

	function del(){
		var emailType = $("#emailType").val();
		// 发件箱
		if(emailType=='-1'){
			if(confirm("确认要删除？")){
				var id = $("#taskid").val();
				var url ='iwork_mail_send_del.action?id='+id;
				var option={
					type: "post",
					url: url,
					data: "",
					cache:false,	
					success: function(msg){
						if(msg=="succ"){
							art.dialog.tips('删除成功');
							window.parent.location.reload();
						}else{
							art.dialog.tips('删除失败');
							return false;
						}
					}
	 			}
				$.ajax(option);
			}// 收件箱
		}else if(emailType == '-2'){
			if(confirm("确认要删除？")){
				var id = $("#taskid").val();
				var url ='iwork_mail_receive_del.action?id='+id;
				var option={
					type: "post",
					url: url,
					data: "",
					cache:false,	
					success: function(msg){
						if(msg=="succ"){
							art.dialog.tips('删除成功');
							window.parent.location.reload();
						}else{
							art.dialog.tips('删除失败');
							return false;
						}
					}
	 			}
				$.ajax(option);
			}// 草稿箱
		}else{
			if(confirm("确认要删除？")){
				var letterIds = $("#editForm").find("#taskid").val();
				var zhuangtai = $("#Ownerid").val();
				var url = 'iwork_mail_star_clean.action?letterIds='+letterIds+'&zhuangtais='+encodeURI(zhuangtai);
				var option={
					type: "post",
					url: url,
					data: "",
					cache:false,	
					success: function(msg){
						if(msg=="succ"){
							art.dialog.tips('删除成功');
							window.parent.location.reload();
						}else{
							art.dialog.tips('删除失败');
							return false;
						}
					}
			 	}
				$.ajax(option);
			}
		}
	}
   </script>
   
   <style>
		.bodyDiv{
			padding-left:5px;
			padding-right:5px;
		}
		.mailHead{
			background-color:#f5f5f5;
			padding:5px;
			padding-left:15px;
			border-bottom:1px solid #999;
		}
		.mailTitle{
			font-size:16px;
			font-family:微软雅黑;
			font-weight:bold;
			line-height:30px;
		}
		.headTitle{
			text-align:left;
			padding:2px;
			font-size:14px;
			padding-right:5px;
			font-family:微软雅黑;
			color:#727d95;
			font-werght:bold;
			line-height:18px;
			width:80px;
		}
		.headData{
			text-align:left;
			padding:2px;
			font-size:12px;
			padding-right:5px;
			font-family:微软雅黑;
			color:#666;
			font-werght:bold;
			line-height:18px;
		}
		.sender{
			color:#006633;
			font-size:14px;
			font-weight:bold;
		}
		.headData .tipfileNum{
			font-size:16px;
			color:red;
		}
			
		.headData .tipfileName{
			font-size:12px;
		}
		.mailBody{
			padding:15px;
			font-size:14px;
			color:#333;
		}
		.mailAttach1{
			padding:0px;
			padding-left:0px;
			padding:5px;
			
		}
		.attchTitle{
			font-size:14px;
			font-family:微软雅黑;
			font-weight:bold;
			line-height:30px;
			padding-left:15px;
			background:#e8edf4;
			border-top:1px solid #efefef;
		}
		.attchBody{
			font-size:12px;
			font-family:微软雅黑;
			line-height:30px;
		}
		.attachItem{
			background:#fff;
			font-size:12px;
			font-family:微软雅黑;
			line-height:30px;
			padding-left:15px;
		}
		.address{
			border:none;padding:0;padding-left:16px;margin-left:10px;
			background:url(iwork_img/user.png) no-repeat;width:16px;height:16px;
			line-height:20px;
			color:#333;
			font-family:微软雅黑;
		}
		.unread{
			border:none;padding:0;padding-left:16px;margin-left:10px;
			background:url(iwork_img/unread.gif) no-repeat;width:16px;height:16px;
			line-height:20px;
			color:#333;
			font-family:微软雅黑;
		}
		.read{
			border:none;padding:0;padding-left:16px;margin-left:10px;
			background:url(iwork_img/readed.gif) no-repeat;width:16px;height:16px;
			line-height:20px;
			color:#333;
			font-family:微软雅黑;
		}
		.address:hover{
			text-decoration:underline;
			cursor:pointer;
		}
		.mailStatus{
			border:1px solid #ccc;
		}
		.mailStatus th{
			padding:2px;
			padding-left:12px;
			background:#efefef;
			font-weight:500;
			border-bottom:1px solid #ccc;
			text-align:left;
		}
		.mailStatus td{
			padding:2px;
			padding-left:12px;
			background:#fff;
			font-weight:500;
			border-bottom:1px solid #ccc;
			text-align:left;
			color:#999;
		}
	</style>
</head>
<body>
<table width="100%">
	<tr>
		<td colspan="2">
		<div class="tools_nav" style="padding-left:5px;">
		 		<input type="button" onclick="toWrite()" name="removeBtn" value="转发"/>
		 		<input type="button" onclick="javascript:toReply();" name="removeBtn" value="邮件回复"/>
		 		<input type="button" onclick="javascript:toReplyAll();" name="removeBtn" value="全部回复"/>
		 		<input type="button" onclick="javascript:del();" name="removeBtn" value="删除"/>
		 	</div>
		</td>
	</tr>
</table>
<div class="bodyDiv">
<div class="mailHead">
	<div class="mailTitle"><s:property value="importantHtml"/><s:property value="model._title"/> </div>
	<div>
		<table width="100%" cellspacing="0" cellpadding="0" border="0">
				<tr> 
		 			<td class="headTitle">发件人 :</td>
		 			<td class="headData sender"><s:property value="senderTitle" escapeHtml="false"/></td>
		 		</tr>
		 		<tr>
		 			<td class="headTitle">时　间 :</td>
		 			<td class="headData"><s:date name="model._createDate" format="yyyy-MM-dd HH:mm:ss"/> </td>
		 		</tr>
		 		<tr>
		 			<td class="headTitle">收件人 :</td>
		 			<td class="headData"><s:property value="recevieTitle"  escapeHtml="false"/></td>
		 		</tr> 
		 		<tr>
		 			<td class="headTitle">抄　送 :</td>
		 			<td class="headData"><s:property value="ccTitle"  escapeHtml="false"/></td>
		 		</tr> 
		 		<s:if test="attachHtml==''">
		 		
		 		</s:if>
		 		<s:else>
			 		<tr>
			 			<td class="headTitle">附　件 :</td>
			 			<td class="headData"><s:property value="attachTipsHtml" escapeHtml="false"/></td>
			 		</tr>
		 		</s:else>
		 		<s:if test="sendStatus!=null">
		 			 <tr>
			 			<td class="headTitle">发送状态：</td>
			 			<td class="headData"><span><s:property value="sendStatus" escapeHtml="false"/></span>&nbsp;<a href="javascript:showSendStatus()">[查看详情]</a>&nbsp;&nbsp;
			 			<span style="float:right"><a href="javascript:mailUndo();"><img src="iwork_img/arrow_undo.png">[邮件撤销]</a></span>
			 		</td>
			 		</tr>
		 			
			 		<tr>
			 			<td colspan="2">
			 				<div id="sendStatusDiv"></div>
			 			</td>
			 		</tr>
		 		</s:if>
		 		<s:else>
			 		<tr>
			 			<td class="headData"><span><s:property value="sendStatus" escapeHtml="false"/></span>&nbsp;<a href="javascript:showSendStatus()">[查看详情]</a>
			 		</td>
			 		</tr>
			 		<tr>
			 			<td colspan="2">
			 				<div id="sendStatusDiv"></div>
			 			</td>
			 		</tr>
		 		</s:else>
		</table>
	</div>
</div>
</div>
<div class="mailBody">
	<s:property value="model._content" escapeHtml="false"/>
	<s:form name="eidtForm" id="editForm" theme="simple">
	  <s:hidden id="mailId" name ="mailId"></s:hidden>
	  <s:hidden id="taskid" name ="taskid"></s:hidden>
	</s:form>
	<s:hidden id="emailType" name ="emailType"></s:hidden>
	<s:hidden id="Ownerid" name ="Ownerid"></s:hidden>
</div>
<div class="mailAttach1">
	<div class="attchTitle"></div>
	<div class="attchBody"><s:property value="attachHtml" escapeHtml="false"/></div>
</div>
<table width="100%" style="border-top:1px solid #efefef">
	<tr>
		<td colspan="2">
		<div class="tools_nav" style="padding-left:5px;">
		 		<input type="button" onclick="toWrite()" name="removeBtn" value="转发"/>
		 		<input type="button" onclick="javascript:toReply();" name="removeBtn" value="邮件回复"/>
		 		<input type="button" onclick="javascript:toReplyAll();" name="removeBtn" value="全部回复"/>
		 		<input type="button" onclick="javascript:del();" name="removeBtn" value="删除"/>
		 	</div>
		</td>
	</tr>
</table> 
</body>
</html>

