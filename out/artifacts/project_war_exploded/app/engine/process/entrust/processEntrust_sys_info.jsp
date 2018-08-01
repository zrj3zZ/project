<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
<link rel="stylesheet" type="text/css"	href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css"	href="iwork_css/jquerycss/icon.css">
<linkrel="stylesheet" type="text/css"  href="iwork_css/reset.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
<script type="text/javascript"	src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript"	src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>

<style type="text/css">
.td_title {
	color: #004080;
	font-size: 12px;
	text-align: right;
	letter-spacing: 0.1em;
	padding-right: 10px;
	white-space: nowrap;
	vertical-align: middle;
}

.td_data {
	color: #0000FF;
	text-align: left;
	padding-left: 3px;
	font-size: 12px;
	vertical-align: middle;
	word-wrap: break-word;
	word-break: break-all;
	font-weight: 500;
	line-height: 12px;
	padding-top: 5px;
	height: 15px;
}
.textinput {
	padding: 5px;
	border: 1px solid #ABADB3;
}
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
</style>
</script>
	</head>
	<body>
		<div align="left">
		<s:form id="editForm" name="editForm" action="processEntrust_sys_save.action" theme="simple">
			<table border="0" cellspacing="5" cellpadding="0" style="margin: 15px;">
				<tr>
					<td class="td_title">委托时间:</td>
					<td class="td_data">
						<s:property value="entrustStart" /> 至 
							<s:property value="entrustEnd" /> 
					</td>
				</tr>
				<tr>
					<td class="td_title">委托人:</td>
					<td class="td_data">
					 	<s:property value="model.entrusetUserid" /> 
					</td>
				</tr>
				<tr>
					<td class="td_title">被委托人:</td>
					<td class="td_data">
						<s:property value="model.entrusetedUserid" /> 
					</td>
				</tr>
				<tr>
					<td class="td_title">授权类型:</td>
					<td class="td_data">
						<s:if test="model.itemOption==1">
							委托办理
						</s:if>
						<s:else>
							授权代发起
						</s:else>
					</td>
				</tr>
				<tr>
					<td class="td_title">是否委托全部流程:</td>
					<td class="td_data">
						<s:if test="model.isentrusetall==1">
							是
						</s:if>
						<s:else>
							否
						</s:else>
					</td>
				</tr>
				<tr>
					<td class="td_title">委托原因:</td>
					<td class="td_data">
						<s:property value="model.entrusetReason" /> 
					</td>
				</tr>
					<tr>
						<td class="td_title">委托流程:</td>
						<td class="td_data">
							<s:property value="processInfo" /> 
						</td>
					</tr>
				<tr>
					<td class="td_title">委托状态:</td>
					<td class="td_data">
						<s:if test="model.entrusetStatus==0">
							待确认
						</s:if>
						<s:else>
							已确认
						</s:else>
					</td>
				</tr>
			</table>
			<s:hidden name="model.id" />
			<s:hidden name="model.entrusetStatus" />
		</s:form>
		</div>
	</body>
</html>
