<%@page pageEncoding="GBK" contentType="text/html; charset=GBK" %>
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