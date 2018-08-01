<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title> 
<link rel="stylesheet" type="text/css" href="iwork_css/common.css"> 

	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<style type="text/css">
		.Itemtitle{
			height:25px;
			width:150px;
			line-height:25px;
			font-size:12px;
			border-bottom:1px solid #efefef;
			background:#f5f5f5;
			padding-left:5px;
		}
		.itemdata{
			height:25px;
			line-height:25px;
			font-size:12px; 
			border-bottom:1px solid #efefef;
			padding-left:5px;
			color:#0000FF;
		}
	</style>
</head>
<body>  
	<s:if test="actionItemList==null || actionItemList.size()==0">
	            <div class="none_item"><img src="iwork_img/metadata.gif" border="0"> 未发现日志信息</div>
	 </s:if> 
	 	<table style="border:1px solid #efefef;width:540px;" cellspacing="1" cellpadding="0">
	 			<tr>
						<td  class ="Itemtitle" colspan="4" style="font-weight:bold"><s:property value="formIcon" escapeHtml="false"/>&nbsp;<s:property value="formTitle"/></td>
						</tr>
	 			<tr>
						<td  class ="Itemtitle">字段标题</td>
						<td  class ="Itemtitle">字段名称</td> 
						<td class = "Itemtitle" style="width:200px;">原值</td> 
						<td class = "Itemtitle" style="width:200px;">修改值</td> 
						</tr>
				<s:iterator  value="actionItemList" status="status">
					<tr>
						<td class="itemdata"><s:property value="fieldTitle"/></td>
						<td class="itemdata"><s:property value="fieldName" /></td> 
						<td class="itemdata" style="color:#999;width:200px;"><s:property value="oldValue" /></td> 
						<td class="itemdata" style="color:red;width:200px;" ><s:property value="newValue" /></td> 
						</tr>
			</s:iterator>
			</table>
			<s:form name="editForm" id="editForm">
			</s:form>
</body>
</html>
