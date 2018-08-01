<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
	<title>目录管理维护</title>
	<s:head/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script language="javascript" src="iwork_js/commons.js"></script>
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="iwork_js/kmjs/km_common.js"></script>

<link href="iwork_css/km/km_dir_info.css" rel="stylesheet" type="text/css" />

<link href="../iwork_css/public.css" rel="stylesheet" type="text/css" />
<body>
<s:form name="editForm" action="km_dir_save" validate="true">
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td class="td3">
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td class="td_title" width=30%>创建人</td>
        <td class="td_data" width=70%><s:property value="model.createuser"  escapeHtml="false"/></td>
        </tr>
    <tr>
     <tr>
        <td class="td_title">创建时间</td>
        <td class="td_data"><s:property value="model.createdate"  escapeHtml="false"/></td>
        </tr>
    <tr>
        <td class="td_title">目录ID</td>
        <td class="td_data"><s:property value="model.id"  escapeHtml="false"/></td>
        </tr>
    <tr style="display:none">
    <td>父目录ID</td>
        <td ><s:property value="model.parentid"  escapeHtml="false"/></td>
        </tr>
      <tr>
      <td class="td_title">目录名称</td>
        <td class="td_data"><s:property value="model.directoryname" escapeHtml="false"/></td>
        </tr>
      <tr>
      <td class="td_title">优先级</td>
        <td class="td_data"><s:property value="model.priority" escapeHtml="false"/></td>
        </tr>
      <tr>
        <td class="td_title">模块描述</td> 
        <td class="td_data">
			<s:property value="model.memo" escapeHtml="false"/>
		</td>
        </tr>
        <tr>
        <td colspan=2>
			<hr>
		</td> 
        </tr>
        <tr>
        <td colspan=2 align="right">
			<s:property value="securityBtn" escapeHtml="false"/>
		</td> 
        </tr>
    </table>
</s:form>
</body>
</html>
