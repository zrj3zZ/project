<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<title>流程模型定义</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
</head> 
	<frameset cols="200,*" border="0" frameBorder = "0" framespacing="0" bordercolor="#F4F4F4" id="d2">
	<frame style="border:1px solid #efefef" src="conn_bridge_param_tabs.action?id=<s:property value='id'/>" name="conn_left"  scrolling="auto" id="conn_left" noresize="noresize"/>
		  <frame style="border:1px solid #efefef;border-left-style: none;"  src="conn_bridge_param_baseinfo.action?id=<s:property value='id'/>" name="conn_right"  scrolling="auto" id="conn_right" noresize="noresize"/>
		</frameset>
</html>   