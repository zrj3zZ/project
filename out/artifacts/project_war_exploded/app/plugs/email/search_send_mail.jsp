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
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
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
	<style>
		.leftDiv{ 
			background:#f5f5f5;
			width:150px;
		} 
		.rigetDiv{
			width:230px;
			border-left:1px solid #efefef;
			vertical-align:top
		} 
		input[type~=text] {
			width:500px;
			height:20px;
			padding:2px;
			padding-left:5px;
			color:#666;
			font-family:微软雅黑;
		}
		.headTitle{
			text-align:right;
			padding:2px;
			font-size:20px;
			padding-right:10px;
			font-family:微软雅黑;
			color:#285586;
			font-werght:bold;
			line-height:28px;
		}
		.headData{
			padding:2px;
			font-size:12px;
			padding-right:5px;
			font-family:微软雅黑;
			color:red;
			text-align:left;
			font-werght:bold;
			line-height:28px;
		}
	</style>
	</style>
					<script type="text/javascript">
	//发送动作
	function sentLetter(){
   	var form =  document.getElementById('editForm');
   	var flag = checkValidate();
   	if(flag==true){
   		form.action ="iwork_email_dosend.action";
   		form.submit();
   		window.opener.location.href="app/plugs/email/send_tips.jsp";
		window.close();
   	}else{
   		return false;
   	}
   }
   //存草稿箱
   function savetounsend(){
   	var form =  document.getElementById('editForm');
   	var flag = checkValidate();
   	if(flag==true){
   		form.action ="iwork_mail_savetounsend.action";
   		form.submit();
   		window.opener.location.href="app/plugs/email/save_tips.jsp";
		window.close();
   	}else{
   		return false;
   	}
   }
	
	   //校验输入内容
   function checkValidate(){
  	var letterContent = document.getElementById('htmlEditor');
  	var letterTitle = document.getElementById('_title');
  	var receiveUserName = document.getElementById('_to');
  	if(receiveUserName.value==''){
  		art.dialog.tips('请选择收信人!');
  		return false;
  	}
  	if(letterTitle.value==''){
  		art.dialog.tips('请填标题!');
  		return false;
  	}
  	if(letterContent.value==''){
  		art.dialog.tips('请填写信内容!');
  		return false;
  	}
  	return true;
  }
  //关闭提示是否保存
  function message(){
  $.messager.confirm('删除草稿提醒', '</br>是否保存?</br></br>',function(r){ 
  if(r){ 
  savetounsend();
  }
  }); 
}

    //转发邮件
   function toWrite(){
   var forwardid = $("#ids").val();
   var taskid = $("#taskid").val();
            window.location.href="iwork_email_forward.action?forwardid="+forwardid+"&taskid="+taskid;
       }


    //返回
   function returnHistory(){
              
            window.location.href="iwork_mail_send_index.action";
       }

	</script>
	
</head>
<body>
<s:form name="eidtForm" id="editForm" theme="simple">
<table width="100%">
<div region="north" border="false">

			<div class="tools_nav" style="padding-left: 2px;">
				
				<input type="button" name="removeBtn"
					onclick="javascript:toWrite();" value="转发" />
				<input type="button" name="removeBtn"
					onclick="javascript:returnHistory();" value="返回" />
				
			</div>
		</div>
	
	<tr>
		<td>
			<div>
		 		<table width="100%" cellspacing="0" cellpadding="0" border="0">
		 		
		 		 <tr>
		 			<td class="headTitle" width="10%">发件人:</td>
		 			<td align="left"><s:property value="mailModel._mailFrom"/></td>
		 		</tr>
		 		<tr>
		 			<td class="headTitle">收件人:</td>
		 			<td class="headData"><s:property value="mailModel._to"/></td>
		 		</tr> 
		 		
		 		<tr>
		 			<td class="headTitle">时&nbsp;&nbsp;&nbsp;间:</td>
		 			<td class="headData"><s:property value="%{getText('{0,date,yyyy-MM-dd hh:mm:ss}',{mailModel._createDate})}" /> </td>
		 		</tr>
		 		<tr>
		 			<td class="headTitle">主&nbsp;&nbsp;&nbsp;题:</td>
		 			<td class="headData"> <s:property value="mailModel._title"/>
					</td>
		 		</tr>
		 		<tr>
		 			<td class="headTitle">正&nbsp;&nbsp;&nbsp;文:</td>
		 			<td class="headData"><s:property value="mailModel._title" />
					</td>
		 		</tr>
		 	 <s:hidden id="ids" name ="ids"></s:hidden>
	         <s:hidden id="taskid" name ="taskid"></s:hidden>
		 		</table>
		 	</div>
		</td>
	</tr>
</table>
</s:form>
</body>
</html>

