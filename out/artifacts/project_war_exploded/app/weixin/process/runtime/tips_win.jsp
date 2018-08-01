<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><s:property value="targetStepName"  escapeHtml="false"/></title>
 <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=0">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/mobile/jquery.mobile-1.4.5.min.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqm-datebox.min.css" /> 
	<script src="iwork_js/jqueryjs/jquery-3.0.8.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.mobile-1.4.5.min.js"></script>
	<script type="text/javascript" src="iwork_js/weixin/weixin_tools.js"></script> 
<script type="text/javascript">
       //调用父页面脚本，关闭办理窗口
        function back(){
        	window.history.back(); 
       }
</script>

</head>
 
<body >
	<div data-role="page">
		<div data-role="header" data-position="fixed"><h1>提示</h1></div>
		<div data-role="content"><s:property value="tipsInfo"/></div>
		<div data-role="footer" data-position="fixed" style="text-align:center">
				<a href="javascript:back();" class="easyui-linkbutton" plain="false" iconCls="icon-cancel">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;返&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;回&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>
		</div>
	</div>
</body>
</html>
