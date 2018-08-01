<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	String lookandfeel = "_def";
	Long dataid = Long.valueOf(request.getParameter("id"));
 %>
<html>
<head>
<title>系统日历设置</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
</head>
  <frameset cols="150,*" frameborder="no" style='border:1px solid #efefef' border="0" framespacing="0" id="dd">
	 <frame style="border:1px solid #efefef"  scrolling="no"  src="iwork_sys_calendar_tab.action?id=<%=dataid %>" name="iformMapTabListFrame" frameborder="no" scrolling="auto" id="iformMapTabListFrame" />
	 <frame src="iwork_sys_calendar_update.action?id=<%=dataid %>" name="iformMapIndexFrame" frameborder="no" id="iformMapIndexFrame" />
</frameset> 
</html>