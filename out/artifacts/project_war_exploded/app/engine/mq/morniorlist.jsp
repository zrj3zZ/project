<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>  
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
<title>IWORK综合应用管理系统</title> 
	<script type="text/javascript">
		function exeSend(){
		 	$("#sendContent").val($("#sendContent").val()+1);
			$.post('sysmq_test_sender.action',$("#iformMain").serialize(),function(data)
		            {
				    	if(data=='success'){ 
				    		//alert('成功');
				    	}else{
				    		alert('失败');
				    	}
				  }); 
		}
		function exeReciver(){
			$.post('sysmq_test_receiver.action',$("#iformMain").serialize(),function(data)
		            { 
		            $("#reciverContent").val(data+">"+$("#reciverContent").val());
				    	if(data=='success'){
				    		//alert('成功');
				    	}else{
				    		//alert('失败');
				    	}
				  }); 
		}
	</script>
</head>

<body style="padding:20px;">
<s:form name="iformMain" id="iformMain" theme="simple">
		<table border="0" width="100%">
			<tr>
				<td>发送消息</td>
				<td><s:textarea cssStyle="width:300px;height:100px" name="sendContent" id="sendContent"></s:textarea></td>
				<td><input type="button"  style="width:150px;height:60px;" value="发送" onclick="exeSend()"></td>
			</tr>
			<tr>
				<td>接收消息</td>
				<td><s:textarea  cssStyle="width:300px;height:100px;"  name="reciverContent" id="reciverContent"></s:textarea></td>
				<td><input type="button" style="width:150px;height:60px;"  value="接收" onclick="exeReciver()"></td>
			</tr>
		</table>
		</s:form>
</body>
</html>
