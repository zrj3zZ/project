<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<% 
String param= "";
param=(String)request.getAttribute("log");
if(param!=null){
	if(!"".equals(param)){request.setAttribute("log","");
		response.getWriter().write(param);
	}
}
%> 