<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<% 
String data= "";
data=(String)request.getAttribute("json");
if(data!=null){
	if(!"".equals(data)){
		request.setAttribute("json","");
		response.getWriter().write(data);
	}
}
%> 