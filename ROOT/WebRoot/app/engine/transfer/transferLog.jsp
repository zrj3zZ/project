<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/systransfer_page.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/design_showlist.css"> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script> 
	<script type="text/javascript" src="iwork_js/checklist.js"></script> 
</head>
<script type="text/javascript">
	$(document).bind("contextmenu",function(){return false;});
</script> 
<body class="easyui-layout">
	<div region="north" border="false" split="false" style="height:120px">
		<div style="margin:10px;padding:10px;border:1px solid #efefef;background-color:#FFFFCC">
		<table width="100%" >
	           			<tr >
	           				<td  class="infoTitle">模型名称</td>
	           				<td class="infoData"><s:property value="spacemodel.title"/></td>
	           				<td class="infoTitle">来源</td>
	           				<td class="infoData"><s:property value="spacemodel.source"/></td>
	           			</tr>
	           			<tr >
	           				<td class="infoTitle">版本</td>
	           				<td class="infoData"><s:property value="spacemodel.version"/></td>
	           				<td class="infoTitle">上传人/上传时间</td> 
	           				<td class="infoData"><s:property value="spacemodel.impUser"/>/<s:date name="spacemodel.impDate" format="yyyy-MM-dd HH:mm" /></td>
	           			</tr>
	           			<tr  class="infoData">
	           			<td class="infoTitle">功能描述</td> 
	           				<td colspan="3">
								<s:property value="spacemodel.memo"/>
	           				</td>
	           			</tr>
	           		</table>
	           		</div>
	</div>
	<div region="center" border="false" split="false">
	<div style="margin:10px;">
							<table  width="100%" cellspacing="0" cellpadding="0" border="0" style="border:1px solid #efefef">
								<tr class="header">
									<td >传输对象</td>
									<td >传输状态</td>
									<td >操作人</td>
									<td >时间</td>
								</tr>
	           			 <s:iterator value="loglist">
									<tr class="cell">
										<td  style="color:#999"><s:property value="title"/></td>					
										<td style="color:#999"><s:property value="status"/></td>					
										<td style="color:#999"><s:property value="oprUser"/></td>
										<td style="color:#999"><s:date name="tranDate" format="yyyy-MM-dd HH:mm" /></td>
									</tr>
						</s:iterator>
						</table>
	           		</div>
	           		</div>
</body>
</html>
