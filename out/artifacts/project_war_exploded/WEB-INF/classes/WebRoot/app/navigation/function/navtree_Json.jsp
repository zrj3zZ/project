<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<% 
String treeData= "";
treeData=(String)request.getAttribute("nav_Function_DataGrid");
if(treeData!=null){
	if(!"".equals(treeData)){
		request.setAttribute("nav_Function_DataGrid","");
		response.getWriter().write(treeData);
	}
}
%> 