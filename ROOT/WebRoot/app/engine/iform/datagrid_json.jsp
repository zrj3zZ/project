<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<% 
String treeData= ""; 
treeData=(String)request.getAttribute("sysEngineIform_DataGrid");
if(treeData!=null){
	if(!"".equals(treeData)){
		request.setAttribute("sysEngineIform_DataGrid","");
		response.getWriter().write(treeData);
	}
}
%> 