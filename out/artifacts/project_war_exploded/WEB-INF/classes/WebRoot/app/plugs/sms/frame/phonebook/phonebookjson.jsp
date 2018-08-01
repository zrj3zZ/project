<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<% 
String treeData= "";
treeData=(String)request.getAttribute("PhonebookType");
if(treeData!=null){
	if(!"".equals(treeData)){
		request.setAttribute("PhonebookType","");
		response.getWriter().write(treeData);
	}
}
%> 