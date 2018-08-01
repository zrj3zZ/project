<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<% 
String treeData= "";
treeData=(String)request.getAttribute("user_DataGrid");
if(treeData!=null){
	if(!"".equals(treeData)){
		request.setAttribute("user_DataGrid","");
		response.getWriter().write(treeData);
	}
}
%> 