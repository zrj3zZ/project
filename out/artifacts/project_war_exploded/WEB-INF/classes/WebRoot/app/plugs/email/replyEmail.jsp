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
	<script type="text/javascript" src="iwork_js/commons.js"></script>
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
			font-size:12px;
			padding-right:5px;
			font-family:微软雅黑;
			color:#285586;
			font-werght:bold;
			line-height:28px;
		}
		.headData{
			text-align:left;
			padding:2px;
			font-size:12px;
			padding-right:5px;
			font-family:微软雅黑;
			color:red;
			font-werght:bold;
			line-height:28px;
		}
	</style>
	<script type="text/javascript">
	//发送动作
	function sentLetter(){
   	var form =  document.getElementById('editForm');
   	var taskid = $("#taskid").val();
   	var flag = checkValidate();
   	if(flag==true){
   		form.action ="iwork_email_dosend.action";
   		form.submit();
   		//window.opener.location.href="app/plugs/email/send_tips.jsp";
		//window.close();
   	}else{
   		return false;
   	}
   }
   //
  
   //存草稿箱
  
	
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
  //回复邮件
	</script>
</head>
<body>
<s:form name="eidtForm" id="editForm" theme="simple">
<table width="100%">
	<tr>
		<td colspan="2">
			<div class="tools_nav" style="padding-left:5px;">
		 		<input type="button" onclick="sentLetter()" name="removeBtn" value="回复"/>
		 		<input type="button" onclick ="message()" name="removeBtn"  value="关闭"/>
		 	</div>
		</td>
	</tr>
	<tr>
		<td>
			<div>
		 		<table width="100%" cellspacing="0" cellpadding="0" border="0">
		 		<tr>
		 			<td class="headTitle">收件人</td>
		 			<td class="headData"><s:textfield tabindex="1" name="model._to" id="_to" theme="simple" 

cssClass="{maxlength:1000,required:true}" ></s:textfield> <img src="iwork_img/mans.gif"/><a href="###" onclick="multi_book

('','','','','','','','','_to');" title="多选地址薄" class="easyui-linkbutton" plain="true"></a></td>
		 		    
		 		</tr> 
		 		<tr>
		 			<td class="headTitle">抄送</td>
		 			<td class="headData"><s:textfield tabindex="2"  name="model._cc" id="_cc" theme="simple" 

cssClass="{maxlength:1000,required:false}"></s:textfield> <a href="###" onclick="multi_book('','','','','','','','','_cc');" title="多选地址薄" 

class="easyui-linkbutton" plain="true"><img src="iwork_img/mans.gif"/></a></a></td>
		 		</tr>
		 		<tr>
		 			<td class="headTitle">主题</td>
		 			<td class="headData">回复：<s:textfield tabindex="3"  name="model._title" id="_title" theme="simple" 

cssClass="{maxlength:200,required:true}"></s:textfield> </td>
		 		</tr>
		 		<tr>
		 			<td class="headTitle">附件</td>
		 			<td><a href="#">添加附件</a></td>
		 		</tr>
		 		<tr>
		 			<td class="headTitle">正文</td>
		 			<td class="headData"> 
						<textarea name="model._content" id="htmlEditor"  cols="80" rows="12" >aaaaa<s:property value="_content"/></textarea>
					</td>
		 		</tr>
		 		</table>
		 	</div>
		</td>
		<td class="rigetDiv" >
			<div>通讯录</div>
            	<div></div>
		</td>
  	    <input type="hidden" id="receiveUserId" name="receiveUserId"/>
  	    <input type="hidden" id="receiveUser" name="receiveUser"></input>
  	    <s:hidden id="id" name ="id"></s:hidden>
  	    <s:hidden id="taskid" name ="taskid"></s:hidden>
	</tr>
	
	
</table>
</s:form>
</body>
</html>
