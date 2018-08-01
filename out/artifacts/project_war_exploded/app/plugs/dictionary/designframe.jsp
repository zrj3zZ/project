<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
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
		  <frame style="border:1px solid #efefef" src="sys_dictionary_design_tablist.action?dictionaryId=<s:property value='dictionaryId'/>&dicType=<s:property value='dicType'/>" name="dictionary_Designer_left"  scrolling="auto" id="dictionary_Designer_left" noresize="noresize"/>
		  <frame style="border:1px solid #efefef;border-left-style: none;"  src="sys_dictionary_design_baseInfo.action?dictionaryId=<s:property value='dictionaryId'/>&dicType=<s:property value='dicType'/>" name="dictionary_Designer_right"  scrolling="auto" id="dictionary_Designer_right" noresize="noresize"/>
	</frameset>
</html>  