<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page pageEncoding="gb2312" contentType="text/html; charset=gb2312" %>
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