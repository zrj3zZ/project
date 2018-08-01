<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
  response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
  String newLocn = "/cms/index.action";
  response.setHeader("Location",newLocn);
%>