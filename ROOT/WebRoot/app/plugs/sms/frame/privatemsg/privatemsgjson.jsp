<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%><%@ taglib prefix="s" uri="/struts-tags" %>
<% 
String param= "";
param=(String)request.getAttribute("privatemsg");
if(param!=null){
	if(!"".equals(param)){request.setAttribute("privatemsg","");
		response.getWriter().write(param);
	}
}
%> 