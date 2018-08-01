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
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
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
	<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js" ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style>
		.topHead{
			background:#fff;
			border-bottom:3px solid #677e9b;
		}
		.title{
			background:#f2f4f6;
			border-bottom:1px solid #c6c9ca;
			border-right:1px solid #c6c9ca;
			padding:2px;
			padding-left:5px;
			font-family:微软雅黑;
		}
		.title_icon{
			background:url(iwork_img/email/mail227195.png) 112px -18px no-repeat;width:26px;height:16px;
		}
		.unread_icon{
			border:none;padding:0;margin:0;background:url(iwork_img/MailUnread.gif) no-repeat;width:16px;height:16px;
		}
		.read_icon{
			border:none;padding:0;margin:0;background:url(iwork_img/readed.gif) no-repeat;width:16px;height:16px;
		}
		.attach{
			border:none;padding:0;margin:0;background:url(iwork_img/email/mail227195.png) -16px -82px no-repeat;width:10px;height:16px;
			margin-left:8px;
		}
		.maintitle{
			font-size:14px;
			font-family:微软雅黑;
			font-werght:bold;
			padding:3px;
			color:#304d79;
			border-bottom:1px solid #efefef;
		}
		.maintitle span{
			color:#666;
			font-size:12px;
			padding:2px;
		}
		.mailItem{
			height:30px;
			border-bottom:1px solid #efefef;
		}
		.mailItem td{
			font-family:微软雅黑;
		}
		.mailItem .mailTitle span{
			color:#a0a0a0;
			font-weight:500;
		}
		.mailItem:hover{
			height:30px;
			background:#f3f3f3;
			cursor:pointer;
		}
		.unread{
			font-weight:bold;
		}
	</style>
	<script type="text/javascript">
	$(function(){
		$("#pp").pagination({
			total:<s:property value="total"/>, 
			pageNumber:<s:property value="pageNumber"/>,
			pageSize:<s:property value="pageSize"/>,
			onSelectPage:function(pageNumber, pageSize){
				var sender = $("#sender").val();
				var recever = $("#recever").val();
				var position = $("#position").val();
				var folderid = $("#folderid").val();
				var begindate = $("#begindate").val();
				var enddate = $("#enddate").val();
				var inputs = new Array();
				inputs[0] = sender;
				inputs[1] = recever;
				inputs[2] = position;
				inputs[3] = folderid;
				inputs[4] = begindate;
				inputs[5] = enddate;
				var pageUrl="iwork_email_search.action?pageSize="+pageSize+"&pageNumber="+pageNumber+"&input="+inputs;
					window.location.href = pageUrl;
				}
			});
		});
		function showDetailInfo(id,taskid,title,emailType){
      		if(title.length>5){
      			title = title.substring(0,5)+"...";
      		}
      		//$("#item_"+taskid).removeClass("unread");
      		var pageurl="iwork_email_read.action?mailId="+id+"&taskid="+taskid+"&emailType="+emailType;
      		
      		parent.parent.addTab(title,pageurl,"");
      	}
		//彻底删除收件箱中的邮件
	   	function totalDel(){		
			var counts = document.getElementsByName('ckb_selectAll');
			var ids = "";
			if(counts.length>0){
				for(var i=0;i<counts.length;i++){
					if(counts[i].checked){
						ids = ids + counts[i].value + ",";
					}
				}
				if(ids!=""){
					if(confirm("确认要删除？")){
						ids = ids.substring(0,ids.lastIndexOf(','));
						var url ='iwork_mail_receive_clean.action?id='+ids;
		 				var option={
							type: "post",
							url: url,
							data: "",
							cache:false,	
							success: function(msg){
								if(msg=="succ"){
									art.dialog.tips('删除成功');
									window.location.reload();
								}else{
									art.dialog.tips('删除失败');
									return false;
								}
							}
			 			}
						$.ajax(option);
					}
				}else{
					art.dialog.tips('未选中任何邮件');
					return false;
				}
			}
		}
   //删除收件箱中的数据
   function del(){		
			var counts = document.getElementsByName('ckb_selectAll');
			var ids = "";
			if(counts.length>0){
				for(var i=0;i<counts.length;i++){
					if(counts[i].checked){
						ids = ids + counts[i].value + ",";
					}
				}
				if(ids!=""){
					if(confirm("确认要删除？")){
						ids = ids.substring(0,ids.lastIndexOf(','));
						var url ='iwork_mail_receive_del.action?id='+ids;
		 				var option={
							type: "post",
							url: url,
							data: "",
							cache:false,	
							success: function(msg){
								if(msg=="succ"){
									art.dialog.tips('删除成功');
									window.location.reload();
								}else{
									art.dialog.tips('删除失败');
									return false;
								}
							}
				 		}
						$.ajax(option);
					}
				}else{
					art.dialog.tips('未选中任何邮件');
					return false;
				}
			}
		}
//邮件标星
   function setStar(){		
			var counts = document.getElementsByName('ckb_selectAll');
			var ids = "";
			if(counts.length>0){
				for(var i=0;i<counts.length;i++){
					if(counts[i].checked){
						ids = ids + counts[i].value + ",";
					}
				}
				if(ids!=""){
					ids = ids.substring(0,ids.lastIndexOf(','));
					var url ='iwork_mail_receive_star.action?id='+ids;
		 			var option={
						type: "post",
						url: url,
						data: "",
						cache:false,	
						success: function(msg){
							if(msg=="succ"){
								window.location.reload();
							}else{
								art.dialog.tips('标星失败');
								return false;
							}
						}
			 		}
					$.ajax(option);
				}else{
					art.dialog.tips('未选中任何邮件');
					return false;
				}
			}
		}
		//取消标星
		 function cancelSetStar(){		
			var counts = document.getElementsByName('ckb_selectAll');
			var ids = "";
			if(counts.length>0){
				for(var i=0;i<counts.length;i++){
					if(counts[i].checked){
						ids = ids + counts[i].value + ",";
					}
				}
				if(ids!=""){
					ids = ids.substring(0,ids.lastIndexOf(','));
					var url ='iwork_mail_restar.action?id='+ids;
		 			var option={
						type: "post",
						url: url,
						data: "",
						cache:false,	
						success: function(msg){
							if(msg=="succ"){
								window.location.reload();
							}else{
								art.dialog.tips('取消标星失败');
								return false;
							}
						}
			 		}
					$.ajax(option);
				}else{
					art.dialog.tips('未选中任何邮件');
					return false;
				}
			}
		}
		
 // 全选、全清功能
	function selectAll() {
		if ($("input[name='ckb_selectAll']").attr("checked")) {
			$("input[name='ckb_selectAll']").attr("checked", true);
		} else {
			$("input[name='ckb_selectAll']").attr("checked", false);
		}
	}
//转发邮件
function toWrite(){
	  var counts = document.getElementsByName('ckb_selectAll');			
			var replyId = "";
			if(counts.length>0){
				for(var i=0;i<counts.length;i++){
					if(counts[i].checked){
					replyId = replyId + counts[i].value;
			        }
				}
			}
			if(replyId!=""){
				$.ajax({
            	 type: "post",
            	 url: "iwork_email_showMailId.action",
            	 data: {id:replyId, emailType:-2,},
           		 cache:false,
            	 success: function(msg){
                     if(msg){
                     	window.location.href="iwork_email_forward.action?mailId="+msg;
                     }
                  }
       		  });
			}else{
					art.dialog.tips('未选中任何邮件');
					return false;
				 }
       }

//回复邮件
function toReply(){
 var counts = document.getElementsByName('ckb_selectAll');			
			var replyId = "";
			if(counts.length>0){
				for(var i=0;i<counts.length;i++){
					if(counts[i].checked){
					replyId = replyId + counts[i].value;
			        }
				}
			}
            window.location.href="iwork_email_reply.action?replyId="+replyId;//taskid
}
function toReplyAll(){
 var counts = document.getElementsByName('ckb_selectAll');			
			var replyId = "";
			if(counts.length>0){
				for(var i=0;i<counts.length;i++){
					if(counts[i].checked){
					replyId = replyId + counts[i].value;
			        }
				}
			}
            window.location.href="iwork_email_replyall.action?replyId="+replyId;//taskid

}
//点击星星邮件标星
   function clickSetStar(ids){		
			if(ids>0){
				var url ='iwork_mail_receive_star.action?id='+ids;
		 			var option={
						type: "post",
						url: url,
						data: "",
						cache:false,	
						success: function(msg){
							if(msg=="succ"){
								window.location.reload();
							}else{
								art.dialog.tips('标星失败');
								return false;
							}
						}
			 		}
					$.ajax(option);
			}
		}
		
		
		//点击星星取消标星
		 function clickCancerStar(ids){		
			if(ids>0){
				var url ='iwork_mail_restar.action?id='+ids;
		 			var option={
						type: "post",
						url: url,
						data: "",
						cache:false,	
						success: function(msg){
							if(msg=="succ"){
								window.location.reload();
							}else{
								art.dialog.tips('取消标星失败');
								return false;
							}
						}
			 	}
				$.ajax(option);
			}
		}
	</script>
</head>
<body class="easyui-layout">
		 <div region="north" border="false" > 
		 <!-- 
		 	<div class="maintitle">收件箱<span>(共214封)</span></div> -->
		 	<div class="tools_nav" style="padding-left:2px;">
		 		<input type="button" onclick="javascript:del();"  name="removeBtn" value="删除"/>
		 		<input type="button" onclick="javascript:totalDel();" name="removeBtn" value="彻底删除"/>
		 		<input type="button" onclick="toWrite()" name="removeBtn" value="转发"/>
		 		<input type="button" onclick="javascript:setStar();" name="removeBtn" value="标星"/>
		 		<input type="button" onclick="javascript:cancelSetStar();" name="removeBtn" value="取消标星"/>
		 	</div>
        </div>  
            <div region="center" border="false" style="background: #fff; border:1px solid #fff">
            	<table width="100%" cellspacing="0" cellpadding="0" border="0">
            		<tr>
		 				<td class="title" style="width:15px;"><input type="checkbox"  name="ckb_selectAll" value="0" onclick="selectAll()";/></td>
		 				<td class="title" style="width:30px;">&nbsp;</td>
		 				<td class="title" style="width:120px;" >发件人</td>
		 				<td class="title">主题</td>
		 				<td class="title"  style="width:120px;" >时间</td>
		 				<td class="title" style="width:20px;">&nbsp;</td>
		 			</tr>
		 			 <s:property value="html" escapeHtml="false"/>
		 		</table>
            </div> 
            <div region="south" border="false" style="height: 32px;">
				<div id="pp" style="background: url('../iwork_img/engine/tools_nav_bg.jpg') repeat-x;border:1px solid #ccc;"></div>
			</div>
			<div style="display:none" > 
		 	  <input type="hidden" id="sender" value="<s:property value="sm.sender"/>"/>
	          <input type="hidden" id="recever" value="<s:property value="sm.recever"/>"/>
	          <input type="hidden" id="position" value="<s:property value="sm.position"/>"/>
	          <input type="hidden" id="folderid" value="<s:property value="sm.folderid"/>"/>
	          <input type="hidden" id="begindate" value="<s:property value="sm.begindate"/>"/>
	          <input type="hidden" id="enddate" value="<s:property value="sm.enddate"/>"/>
        	</div>
</body>
</html>
