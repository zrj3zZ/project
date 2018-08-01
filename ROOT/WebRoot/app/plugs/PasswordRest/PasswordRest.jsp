<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head><title>IWORK综合应用管理系统</title>

<link href="iwork_css/system/syspersion_pwd_update.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="iwork_js/system/syspersion_pwd_update.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link href="iwork_css/reset.css" rel="stylesheet" type="text/css"/>
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?skin=default"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
var mainFormValidator;
$().ready(function() {
mainFormValidator =  $("#frmMain").validate({
 });
 mainFormValidator.resetForm();
});

function doSubmit(){
	var dlyh = $("#DLYH").val();
	var cshmm = $("#CSHMM").val();
	var valid = mainFormValidator.form(); //执行校验操作
	var flag=false;
	$.ajax({
        type: "POST",
        async: false,
        url:  "zqb_password_check.action",
        data: {password:cshmm,userid:dlyh},
        dataType: "text",
        success: function(data){
        	if(data=='true'){
        		flag=true;
        	}else if(data=="htzq"){
        		lhgdialog.tips("1、密码必须包含字母、数字、特殊符号三类字符；2、密码长度不能少于12个字符；");
        		return  ;
        	}else if(data=="namePwd"){
        		lhgdialog.tips("密码不能与用户名相同");
        		return  ;
        	}else if(data=="oldPwd"){
        		lhgdialog.tips("新密码不能与旧密码相同");
        		return  ;
        	}else if(data=="hlzq"){
        		lhgdialog.tips("1、密码必须包含字母、数字；2、密码长度不能少于8个字符；");
        		return  ;
        	}else{
        		lhgdialog.tips("1、密码必须包含字母、数字、特殊符号三类字符；2、密码长度不能少于8个字符；");
        		return  ;
        	}
        	if(cshmm.indexOf("&")>=0||cshmm.indexOf("<")>=0||cshmm.indexOf(">")>=0){
        		lhgdialog.tips("1、新密码不能带&,<,>字符");
        		flag=false;
        		return  ;
        	}
        }
    });
	if(flag){
		frmMain.action='zqb_change_password.action';
		frmMain.submit();
	}
    return  ;
}
</script>
</head>	
<body class="opts"> 
<form name="frmMain" id="frmMain"  action="zqb_change_password.action"  method="post"  target='_self'>

<table width="100%" border="0" cellpadding="0" cellspacing="0" >
   <tr>
      <td >
		  		<div  class="tools_nav">
			  		<a id="btnEp" class="easyui-linkbutton" plain="true" icon="icon-save" href="javascript:doSubmit();" >保存</a>
			  		<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		  		</div>
	  </td>
    </tr>
    <tr>
    	<td align="left" valign="top" height="100%" >
			<div style="width:50%;">
				<table width="100%">
				<tr id="itemTr_12">
								<td class="td_title"  id="title_YHMM" width="15%">									
									当前登录用户密码：						
									</td>
									<td class="td_data"  id="data_YHMM"  width="35%"><input type="password" class = '{maxlength:64,required:true,string:false}'  style="width:100px;" name='YHMM' id='YHMM'  value=''   form-type='al_textbox'><font style="color:red;padding-left:5px">*</font>&nbsp;　</td>
							</tr>
				  			<tr id="itemTr_0">
								<td class="td_title" id="title_DLYH" width="50%">
									待修改用户：
								</td>
								<td class="td_data" id="data_DLYH" width="50%">
									<div style=" white-space:nowrap;vertical-align:bottom;"><input type='text'  readonly name='DLYH' class = '{maxlength:64,required:true}'  id='DLYH' value=''  style="width:100px;margin-right:5px;"  form-type="radioAddress" ><a id='radioBtnhtml' href="###" title="单选地址薄"  onclick="radio_book('false','false','','DLYH','','','','YHSSBM','DLYH');" class="easyui-linkbutton" plain="true" iconCls="icon-radiobook"></a>&nbsp;　
								</td>
							</tr>
							<tr id="itemTr_1">
								<td class="td_title" id="title_YHSSBM" width="50%">
									待修改用户所属部门：
								</td>
								<td class="td_data" id="data_YHSSBM" width="50%">
									<input type='text' class = '{maxlength:64,required:false,string:false}' readonly style="width:100px;" name='YHSSBM' id='YHSSBM'  value=''   form-type='al_textbox'>&nbsp;　
								</td>
							</tr>
							<tr id="itemTr_2">
								<td class="td_title" id="title_CSHMM" width="50%">
									初始化密码：
								</td>
								<td class="td_data" id="data_CSHMM" width="50%">
									<input type='password' class = '{maxlength:20,required:false,string:false}'  style="width:100px;" name='CSHMM' id='CSHMM'  value=''   form-type='al_textbox'>&nbsp;　
								</td>
							</tr>
							<tr>
								<td colspan="2"><s:actionmessage cssClass='errorInfo'/></td>
							</tr>
				</table>
			</div>
		</td>
    </tr>
  </table>
</form>
</body>	
</html>
<script language="JavaScript">
  jQuery.validator.addMethod("string", function(value, element) {
          var patrn=/(?=.[^“”`~!#$%^&*+<>?"{},;'（）—。[\]]+$)((?=.*\d)(?=.*\D)|(?=.*[a-zA-Z])(?=.*[^a-zA-Z]))^.{8,}$/im;
         // var patrn = /^(?!([a-zA-Z\d]*|[\d!@！￥；，、【】|·《》？‘’：]*|[a-zA-Z!@！￥；，、【】|·《》？‘’：]*)$)[a-zA-Z\d!@！￥；，、【】|·《》？‘’：]{8,20}$/;
          return patrn.test(value);
        }, "<br/>1、密码必须由数字、字符、特殊字符组成；2、密码长度不能少于8个字符，不能超过20；3、“”`~!#$%^&*+<>?\"{},;'[]（）—。\ 这些字符不能输入；");
  //@！￥；，、【】|·《》？‘’：“”`~!#$%^&*+<>?,;（）—。
</script>