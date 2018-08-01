<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<% 
String treeData= "";
treeData=(String)request.getAttribute("sysEngineMetadata_DataGrid");
if(treeData!=null){
	if(!"".equals(treeData)){
		request.setAttribute("sysEngineMetadata_DataGrid","");
		response.getWriter().write(treeData);
	}
}
%> 