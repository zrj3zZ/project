<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="iwork" uri="/WEB-INF/sys-commonsTags.tld" %>
<html>
<head>

<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/org/user_edit.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<script type="text/javascript" src="iwork_js/orguser_form.js"></script>
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
var mainFormValidator;
var api = art.dialog.open.api, W = api.opener;
	$(document).ready(function(){
		mainFormValidator =  $("#editForm").validate({
			 errorPlacement: function (error, element) {
	             error.appendTo(element.parent());    //将错误信息添加当前元素的父结点后面               
	        }
	   }); 
	    mainFormValidator.resetForm();
    });
    function send(){
    	 var obj = $('#editForm').serialize(); 
	     $.post("user_pwd_send.action",obj,function(data){	           
           	if(data=='success'){
	             alert("已将初始化的登录密码发送至用户");
	             if(typeof(api) =="undefined"){
	            	 window.close();
	         	}else{ 
	         		api.close(); 
	         	}
	         }else{
	         	alert("初始化操作异常！");
	         }	        	
	     });
    }
</script>

<style type="text/css">
		.form_title{  
			font-family:黑体;
			font-size:15px;
			text-align:right;
		}
		.form_data{
			font-family:宋体;
			font-size:12px;
			text-align:left;
			color:0000FF; 
			padding:3px; 
		}
		.title{
			font-size:14px;
			padding-top:20px;
			padding-bottom:20px;
			padding-left:10px;
		}
		.select_type{
			font-size:14px;
			font-weight:bold;
			padding-bottom:20px;
			padding-left:100px;
		}
	</style>
	</head>
<body class="easyui-layout" >
<div region="north" border="false" split="false" style="font-size:18px;height:30px;font-family:黑体">
	<img alt="账户口令初始化" src="iwork_img/icon_key.gif" border="0">账户登录口令初始化 
</div>
<div region="center" border="false" split="false" >
<s:form name="editForm" id="editForm" action="user_pwd_send"  theme="simple">
	<div class="title">请选择初始化登录口令的接收方式：</div>
	<div class="select_type"><input type="radio" name="sendType" id="editForm_sendTypeemail"  checked="checked"  value="email" class="{maxlength:32,required:true}"/><label for="editForm_sendTypeemail">邮件</label>
<!-- <input type="radio" name="sendType" id="editForm_sendTypesms" value="sms" class="{maxlength:32,required:true}"/><label for="editForm_sendTypesms">短信</label> -->
</div> 
	<div class="select_type"> 
  	 <a href="javascript:send()" class="easyui-linkbutton"  plain="false" iconCls="icon-ok" >发送</a> 
   </div>
   <s:hidden name="userid" id="userid"></s:hidden>
</s:form>
</body>
</html>
