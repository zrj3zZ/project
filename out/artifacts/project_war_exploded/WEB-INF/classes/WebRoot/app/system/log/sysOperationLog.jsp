<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<% 
String treeData= "";
treeData=(String)request.getAttribute("sysOperationLogJson");
if(treeData!=null){
	if(!"".equals(treeData)){
		request.setAttribute("sysOperationLogJson","");
		response.getWriter().write(treeData);
	}
}
%> 