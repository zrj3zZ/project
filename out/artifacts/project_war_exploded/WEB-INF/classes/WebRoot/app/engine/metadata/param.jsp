<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<% 
String param= "";
param=(String)request.getAttribute("param");
if(param!=null){
	if(!"".equals(param)){request.setAttribute("param","");
		response.getWriter().write(param);
	}
}
%> 