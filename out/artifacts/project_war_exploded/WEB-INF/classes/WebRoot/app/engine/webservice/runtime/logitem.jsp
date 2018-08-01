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
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/public.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script> 
	<style type="text/css">
		.form_title{  
			font-family:黑体; 
			font-size:14px;
			text-align:right;
		}
		.form_data{
			font-family:宋体;
			font-size:12px;
			text-align:left;
			color:0000FF; 
		}
		.td_memo{
			border:1px solid #efefef;
			padding:5px;
			
		}
		.ptable{
			width:100%;
			border:1px solid #efefef;
		}
		.ptable th{
			background-color:#efefef;
			padding:2px;
		}
		.ptable td{
			padding:2px;
			line-height:20px;
			border-bottom:1px solid #efefef;
			border-right:1px solid #efefef;
			color:#0000ff;
		}
		
	</style>	
</head>
<body class="easyui-layout">
            <div region="center" border="false" style="padding: 5px; background:#fff; border:0px solid #ccc;">
            	<fieldset  class="td_memo">
								<legend class="td_memo_title">输入参数</legend>
								 <table class="ptable">
								 	<tr>
								 		<th>参数标题</th>
								 		<th>参数名</th>
								 		<th>参数值</th>
								 	</tr>
								 	<s:iterator value="inparamList">
								 		<tr>
								 			<td><s:property value="title"/></td>
								 			<td><s:property value="name"/></td>
								 			<td><s:property value="value"/></td>
								 		</tr>
								 	</s:iterator>
								 </table>
							</fieldset>
							<br>
            	<fieldset  class="td_memo">
								<legend class="td_memo_title">输出参数</legend>
								 <table class="ptable">
								 <tr>
								 		<th>参数标题</th>
								 		<th>参数名</th>
								 		<th>参数值</th>
								 	</tr>
								 <s:iterator value="outparamList">
								 		<tr>
								 			<td><s:property value="title"/></td>
								 			<td><s:property value="name"/></td>
								 			<td><s:property value="value"/></td>
								 		</tr>
								 	</s:iterator>
								 	</table>
							</fieldset>
            </div> 
            <div region="north" border="false" style="padding: 5px; background:#FCFCF4; border:0px solid #ccc;">
            	<table width="100%"  border="0" cellpadding="0" cellspacing="0"> 
            	<tr>   
            			<td class="form_title">调用时间:</td>
            			<td class="form_data"><s:property value="log.createdate"/></td>
            			<td class="form_title">调用状态:</td>
            			<td class="form_data">
            				<s:if test="log.status==1">
            					成功
            				</s:if>
            				<s:else>
            					失败
            				</s:else>
            			</td>
            		</tr> 
            	<tr>   
            			<td class="form_title">响应时长:</td>
            			<td class="form_data"><s:property value="log.showtime"/></td>
            			<td class="form_title">日志信息:</td>
            			<td class="form_data"><s:property value="log.loginfo"/></td>
            		</tr> 
            	</table>
            </div> 
</body>
</html>
