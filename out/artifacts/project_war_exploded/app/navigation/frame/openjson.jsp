<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<% 
String treeData= "";
treeData=(String)request.getAttribute("navtreeJson");
if(treeData!=null){
	if(!"".equals(treeData)){
		request.setAttribute("navtreeJson","");
		response.getWriter().write(treeData);
	}
}
%> 