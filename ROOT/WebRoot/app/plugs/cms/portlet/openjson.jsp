<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<% 
String treeData= "";
treeData=(String)request.getAttribute("CmsPortlet");
if(treeData!=null){
	if(!"".equals(treeData)){
		request.setAttribute("CmsPortlet","");
		response.getWriter().write(treeData);
	}
}
%> 