<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<title>流程模型定义</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
</head> 
	<frameset cols="200,*" border="0" frameBorder = "0" framespacing="0" bordercolor="#F4F4F4" id="d2">
		  <frame style="border:1px solid #efefef" src="sysFlowDef_tabList.action?edittype=tab&prcDefId=<s:property value='prcDefId' escapeHtml='false'/>&actdefId=<s:property value='actdefId'  escapeHtml='false'/>" name="sysDef_Designer_left"  scrolling="auto" id="sysDef_Designer_left" noresize="noresize"/>
		  <frame style="border:1px solid #efefef;border-left-style: none;" src="sysFlowDef_PropertyIndex!load.action?edittype=tab&prcDefId=<s:property value='prcDefId' escapeHtml='false'/>&actdefId=<s:property value='actdefId'  escapeHtml='false'/>" name="sysDef_Designer_right"  scrolling="auto" id="sysDef_Designer_right" noresize="noresize"/>
	</frameset>
</html>  