﻿<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
		<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script> 
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
		var api = art.dialog.open.api, W = api.opener;;	
		function doUpfile(){
		 	var src = $('#upload').val();
		 	//var filename=src.replace(/.*(\/|\\)/, "");
			var fileExt=(/[.]/.exec(src)) ? /[^.]+$/.exec(src.toLowerCase()):'';
			if(fileExt=='xls'||fileExt=='et'){
				submitForm(); 
			}else{
				alert('您选择的文件格式不正确,请重新选择 .xls 文件');
				$('#upFile').val("");
				$('#upFile').click();
				return false;
			}
		}
		
		function submitForm(){
			var options = {
					error:errorFunc,
					success:successFunc 
			};
			$('#upfileForm').ajaxSubmit(options);
		}
		
		errorFunc=function(){
	           alert("导入失败！");
	    } 
	    successFunc=function(responseText, statusText, xhr, $form){
	           if(responseText=="success"){
	           		alert("导入成功！"); 
	           		parent.$('#subformCGSQSB').trigger("reloadGrid");
	                api.close();
	           }else if(responseText=="error"){
	              alert("导入失败！");
	           }else if(responseText=="typeerror"){
	              alert("数据模板匹配异常,导入失败！");
	           }else{
	        	   alert(responseText);
	           }
	      }
		  function shuaxin(){
		  	var subformkey = $("#subformkey").val();
		  	$("#"+subformkey,parent.document).trigger("reloadGrid"); 
		  }
	</script> 
</head> 
<body class="easyui-layout"> 
<div region="north" border="false" >
	选择您要上传的excel模板
	</div>
	<div region="center"border="false" >
		 <s:form  method ="POST" name="upfileForm" id="upfileForm"  action="dg_UploadFile.action" theme="simple"  enctype ="multipart/form-data">
			<input type="file" id ="upload" name ="upload" onChange="doUpfile();"  size=40 >
			<s:hidden name="COMPANYNAME" id="COMPANYNAME"></s:hidden> 
		</s:form>
	</div>
	<div region="south"border="false" >
		<input type="button" onclick="shuaxin();" name="btn" value="刷新" />
	</div>
</body>
</html>
