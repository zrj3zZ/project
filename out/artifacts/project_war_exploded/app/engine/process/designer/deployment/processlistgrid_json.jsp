<%@page pageEncoding="gb2312" contentType="text/html; charset=gb2312" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<% 
String treeData= "";
treeData=(String)request.getAttribute("sysProcessDefinition_DataGrid");
if(treeData!=null){
	if(!"".equals(treeData)){
		request.setAttribute("sysProcessDefinition_DataGrid","");
		response.getWriter().write(treeData);
	}
}
%> 