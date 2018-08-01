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
	<style type="text/css">
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
			border:none;padding:0;margin:0;background:url(iwork_img/email/mail227195.png) 0px -82px no-repeat;width:26px;height:16px;
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
	//分页
	$(function(){
		$("#pp").pagination({
			    total:<s:property value="total"/>, 
			    pageNumber:<s:property value="pageNumber"/>,
			    pageSize:<s:property value="pageSize"/>,
			    onSelectPage:function(pageNumber, pageSize){
			    	var pageUrl="iwork_mail_list_star.action?pageSize="+pageSize+"&pageNumber="+pageNumber;
					window.location.href = pageUrl;
				}
			});
	  });
	
	//取消标星
	   function cancelStar(){
	   	   var checks = document.getElementsByName("chk_list");
		   var zhuangtai = document.getElementsByName("zhuangtai");
		   var letterIds = "";
		   var zhuangtais = "";
			if(checks.length>0){
				for(var i=0;i<checks.length;i++){
					if(checks[i].checked ){
						letterIds = letterIds + checks[i].value + ",";
						zhuangtais = zhuangtais + zhuangtai[i].value + ",";
					}
				}
				if(letterIds==""){
					art.dialog.tips("未选中任何邮件");
				}else{
					letterIds = letterIds.substring(0,letterIds.lastIndexOf(','));
					zhuangtais = zhuangtais.substring(0,zhuangtais.lastIndexOf(','));
					var url = 'iwork_mail_list_restar.action?letterIds='+letterIds+'&zhuangtais='+encodeURI(zhuangtais);
					var option={
						type: "post",
						url: url,
						data: "",
						cache:false,	
						success: function(msg){
							if(msg=="succ"){
								window.location.reload();
							}else{
								return false;
							}
						}
			 		}
					$.ajax(option);
				}
			}
	   }
	
	//彻底删除
	function reallyDelete(){
	    var checks = document.getElementsByName("chk_list");
	    var zhuangtai = document.getElementsByName("zhuangtai");
	    var letterIds = "";
	    var zhuangtais = "";
			if(checks.length>0){
				for(var i=0;i<checks.length;i++){
					if(checks[i].checked ){
						letterIds = letterIds + checks[i].value + ",";
						zhuangtais = zhuangtais + zhuangtai[i].value + ",";
					}
				}
			}
			if(letterIds==""){
				art.dialog.tips("未选中任何邮件");
			}else if(confirm("确认要删除？")){
				letterIds = letterIds.substring(0,letterIds.lastIndexOf(','));
				zhuangtais = zhuangtais.substring(0,zhuangtais.lastIndexOf(','));
				var url = 'iwork_mail_star_clean.action?letterIds='+letterIds+'&zhuangtais='+encodeURI(zhuangtais);
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
	}
	
    // 全选、全清功能
	function selectAll() {
		if ($("input[name='chk_list']").attr("checked")) {
			$("input[name='chk_list']").attr("checked", true);
		} else {
			$("input[name='chk_list']").attr("checked", false);
		}
	}
     //单击星星取消标星
	function clickStar(id,emailType,isstar){
		//判断是否标星
		if(isstar==1){
			var url="iwork_mail_is_read.action?id="+id+"&emailType="+emailType+"&isstar="+isstar;
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
	
	function showDetailInfo(id,emailType,taskId,title){
      		if(title.length>5){
      			title = title.substring(0,5)+"...";
      		}
      		$("#item_"+taskid).removeClass("unread");
      		var pageurl="iwork_email_read.action?mailId="+id+"&taskid="+taskId+"&emailType="+emailType;
      		parent.addTab(title,pageurl,"");
      	}
	</script>
	
</head>
<body class="easyui-layout">
		 <div region="north" border="false" > 
		 	<div class="tools_nav" style="padding-left:2px;">
		 		<input type="button" name="removeBtn" onclick="javascript:reallyDelete();"value="彻底删除"/>
		 		<input type="button" name="removeBtn" onclick="javascript:cancelStar();" value="取消标星"/>
		 	</div>
        </div>  
            <div region="center" border="false" style="background: #fff; border:1px solid #fff">
            	<table width="100%" cellspacing="0" cellpadding="0" border="0">
            		<tr>
		 				<td class="title" style="width:15px;"><input type="checkbox" name="chk_list" value="0" onclick="selectAll()"></td>
		 				<input type="hidden" name="zhuangtai" value="0"/>
		 				<td class="title" style="width:30px;">&nbsp;</td>
		 				<td class="title" style="width:120px;" >发件人</td>
		 				<td class="title">主题</td>
		 				<td class="title"  style="width:120px;" >时间</td>
		 				<td class="title"  style="width:20px;"></td>
		 			</tr>
		 			  <s:iterator value="listMap" status="status">
		 			  <s:if test=""></s:if>
		 			   <s:if test="isread==1"> 
		 			  		<tr  class="mailItem" name="receiveMail"  id="item_<s:property value="id"/>"  >
		 			   </s:if>
		 			   <s:if test="isread==0">
		 			   <tr  class="mailItem unread" name="receiveMail"  id="item_<s:property value="id"/>"   >
		 			   </s:if> 
				 				<td ><input name="chk_list" type="checkbox" value="<s:property value="id"/>"/></td>
				 				<td  class="mailicon" onClick="javascript:showDetailInfo('<s:property value="bindid"/>','<s:property value="type"/>','<s:property value="id"/>','<s:property value="title"/>');">
				 				<s:if test="isread==1"><img id="bflag" src="iwork_img/sysletter/mail_have_readed.png"/></s:if>
				 				<s:if test="isread==0"><img id="bflag" src="iwork_img/sysletter/mail_no_readed.png"/></s:if></td>
				 				<td  class="mailUser" onClick="javascript:showDetailInfo('<s:property value="bindid"/>','<s:property value="type"/>','<s:property value="id"/>','<s:property value="title"/>');"><s:property value="owner"/></td>
				 				<td  class="mailTitle" onClick="javascript:showDetailInfo('<s:property value="bindid"/>','<s:property value="type"/>','<s:property value="id"/>','<s:property value="title"/>');" title="<s:property value="titles"/>"><s:property value="title"/><span> -</span></td>
				 				<td  class="mailtime" onClick="javascript:showDetailInfo('<s:property value="bindid"/>','<s:property value="type"/>','<s:property value="id"/>','<s:property value="title"/>');"><s:property value="%{getText('{0,date,yyyy-MM-dd HH:mm}',{createTime})}"/></td>
				 				<td><s:if test="isstar==1"><img id="bflag" onclick="javascript:clickStar('<s:property value="id"/>','<s:property value="type"/>','<s:property value="isstar"/>');" src="iwork_img/star_full.png"/></s:if>
				 				<s:if test="isstar==0"><img id="bflag" onclick="javascript:clickStar('<s:property value="id"/>','<s:property value="type"/>','<s:property value="isstar"/>');" src="iwork_img/star_empty.png"/></s:if>
				 				</td>
				 				<input type="hidden" id="taskid" value="<s:property value="id"/>"/>
				 				<input type="hidden" id="bindid" value="<s:property value="bindid"/>"/>
				 				<input name="zhuangtai" type="hidden" value="<s:property value="owner"/>"/>
				 				<input name="isread" type="hidden" value="<s:property value="isread"/>"/>
				 			</tr>
		 			</s:iterator>
		 			<s:if test="listMap==null||listMap.size()==0"> 
		 					<tr class="mailItem ">
		 						<td colspan="5" style="text-align:center">【空】</td>
		 						<td class="mailicon"></td>
		 					</tr>
		 			</s:if>
		 			
		 		</table>
            </div> 
            <div region="south" border="false" style="height: 32px;">
		<div id="pp" style="background: url('../iwork_img/engine/tools_nav_bg.jpg') repeat-x;border:1px solid #ccc;"></div>
	</div>
</body>
</html>
