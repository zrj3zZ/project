<%@page pageEncoding="GBK" contentType="text/html; charset=GBK" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<% 
String treeData= ""; 
treeData=(String)request.getAttribute("subFormDataGrid");
if(treeData!=null){
	if(!"".equals(treeData)){
		request.setAttribute("subFormDataGrid","");
		response.getWriter().write(treeData);
	}
}
%> 