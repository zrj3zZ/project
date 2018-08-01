<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%><%@ taglib prefix="s" uri="/struts-tags" %>
<% 
String treeData= "";
treeData=(String)request.getAttribute("SysEngineIForm");
if(treeData!=null){
	if(!"".equals(treeData)){
		request.setAttribute("SysEngineIForm","");
		response.getWriter().write(treeData);
	}
}
%> 