<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	String lookandfeel = "_def";
 %>
<html>
<head>
<title>1</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
</head>
  <frameset cols="190,*" frameborder="no" style='border:1px solid #efefef' border="0" framespacing="0" id="dd">
	  <frame style="border:1px solid #efefef"  scrolling="no"  src="sysDem_tablist.action?id=<s:property value='id'  escapeHtml='false'/>" name="SysDemTabListFrame" frameborder="no" scrolling="auto" id="SysDemTabListFrame" />
	  <frame src="sysDem_edit.action?id=<s:property value='id'  escapeHtml='false'/>" name="SysDemFrame" frameborder="no" id=SysDemFrame" />
</frameset> 
</html>