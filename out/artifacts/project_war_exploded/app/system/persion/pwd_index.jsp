<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head><title>IWORK综合应用管理系统</title>

<link href="iwork_css/system/syspersion_pwd_update.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="iwork_js/system/syspersion_pwd_update.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link href="iwork_css/reset.css" rel="stylesheet" type="text/css"/>
<script language="javascript" src="iwork_js/commons.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?skin=default"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
/* var mainFormValidator;
$().ready(function() {
mainFormValidator =  $("#frmMain").validate({
 });
 mainFormValidator.resetForm();
}); */
</script>
<script type="text/javascript" src="iwork_js/system/syspersion_pwd_update.js"></script>
</head>	
<body class="opts"> 
<form name="frmMain" id="frmMain"  action="syspersion_exec_pwd_update"  method="post"  target='_self'>

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
					<tr>
						<td class="td_title" width="50%">旧密码:</td>
						<td class="td_data" width="50%"><input type="password" name="txtOldPwd"></td>
					</tr>
					<tr>
						<td class="td_title" width="50%">新密码:</td>
						<td class="td_data" width="50%"><input type="password" name="txtNewPwd" class="{maxlength:20,string:true}"></td>
					</tr>
					<tr>
						<td class="td_title" width="50%">确认新密码:</td>
						<td class="td_data" width="50%"><input type="password" id="txtConfirmPwd" name="txtConfirmPwd" class="{maxlength:20,string:true}"></td>
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
 /*  jQuery.validator.addMethod("string", function(value, element) {
          var patrn=/(?=.[^“”`~!#$%^&*+<>?"{},;'（）—。[\]]+$)((?=.*\d)(?=.*\D)|(?=.*[a-zA-Z])(?=.*[^a-zA-Z]))^.{8,}$/im;
          return patrn.test(value);
        }, "<br/>1、密码必须由数字、字符、特殊字符三种中的两种组成；2、密码长度不能少于8个字符；3、“”`~!#$%^&*+<>?\"{},;'[]（）—。\ 这些字符不能输入；"); */
</script>