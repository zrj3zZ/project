<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<% 
String treeData= "";
treeData=(String)request.getAttribute("rmcontent");
if(treeData!=null){
	if(!"".equals(treeData)){
		request.setAttribute("rmcontent","");
		response.getWriter().write(treeData);
	}
}
%> 