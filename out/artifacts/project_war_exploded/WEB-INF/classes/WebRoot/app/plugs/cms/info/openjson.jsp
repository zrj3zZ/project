<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<% 
String treeData= "";
treeData=(String)request.getAttribute("Cms_Info_Grid");
if(treeData!=null){
	if(!"".equals(treeData)){
		request.setAttribute("Cms_Info_Grid","");
		response.getWriter().write(treeData);
	}
}
%> 