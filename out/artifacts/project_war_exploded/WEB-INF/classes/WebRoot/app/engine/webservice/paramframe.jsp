<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
</head> 
	<frameset cols="200,*" border="0" frameBorder = "0" framespacing="0" bordercolor="#F4F4F4" id="d2">
	<frame style="border:1px solid #efefef" src="web_service_param_tabs.action?id=<s:property value='id'/>" name="left"  scrolling="auto" id="left" noresize="noresize"/>
	<frame style="border:1px solid #efefef;border-left-style: none;"  src="web_service_param_baseinfo.action?id=<s:property value='id'/>" name="right"  scrolling="auto" id="right" noresize="noresize"/>
		</frameset>
</html>   