<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>IWORK综合应用管理系统</title>
	<script type="text/javascript" src="iwork_js/metadatamap_index.js" ></script>
	<script type="text/javascript" src="iwork_js/commons.js" ></script>
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
</head>
<body class="easyui-layout">
			<s:form name="editForm" action="sysEngineMetadataMap_save.action">
				<table  width="98%"  style="margin:3px;">
						<s:iterator value="userOnlineList" status="status">
							<tr>
								<td  style="margin:3px;border-bottom:1px dotted #A4BED4;"><img src="iwork_img/user.png" style="margin-right:5px;" border="0"/><s:property value="_userModel.username"/>[<s:property value="_deptModel.departmentname"/>]</td>
							</tr>
						 </s:iterator>
				</table>
			</s:form>
</body>
</html>
