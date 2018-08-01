<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page pageEncoding="gb2312" contentType="text/html; charset=gb2312" %>
<% 
String treeData= "";
treeData=(String)request.getAttribute("orgUserTreeJson");
if(treeData!=null){
	if(!"".equals(treeData)){
		request.setAttribute("orgUserTreeJson","");
		response.getWriter().write(treeData);
	}
}
%> 