<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<% 
String treeData= "";
treeData=(String)request.getAttribute("SysEngineIformMapInfo");
if(treeData!=null){
	if(!"".equals(treeData)){
		request.setAttribute("SysEngineIformMapInfo","");
		response.getWriter().write(treeData);
	}
}
%> 