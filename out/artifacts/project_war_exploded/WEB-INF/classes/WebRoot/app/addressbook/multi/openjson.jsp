<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<% 
String treeData= "";
treeData=(String)request.getAttribute("addressbook");
if(treeData!=null){
	if(!"".equals(treeData)){
		request.setAttribute("OrgGroup","");
		response.getWriter().write(treeData);
	}
}
%> 