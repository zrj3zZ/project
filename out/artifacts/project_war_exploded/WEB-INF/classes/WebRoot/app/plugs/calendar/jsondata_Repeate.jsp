<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<% 
String treeData= "";
treeData=(String)request.getAttribute("schCalendardata_Repeate_DataGrid");
if(treeData!=null){
	if(!"".equals(treeData)){
		request.setAttribute("schCalendardata_Repeate_DataGrid","");
		response.getWriter().write(treeData);
	}
}
%> 