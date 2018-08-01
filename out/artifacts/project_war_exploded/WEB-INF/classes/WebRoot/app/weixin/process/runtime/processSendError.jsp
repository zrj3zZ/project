<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><s:property value="targetStepName"  escapeHtml="false"/></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/mobile/jquery.mobile-1.4.5.min.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqm-datebox.min.css" /> 
	<script src="iwork_js/jqueryjs/jquery-3.0.8.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.mobile-1.4.5.min.js"></script>
	<script type="text/javascript" src="iwork_js/weixin/weixin_tools.js"></script> 
	<script>
		function back(){
			window.history.go(-1);
		}
	</script>
</head>
<body>
<div data-role="page" class="jqm-demos jqm-demos-home">
	<div data-role="header" data-position="fixed" style="text-align:center;padding:10px;">
				流程办理异常提示
		</div>
	<div data-role="content" class="jqm-content">
		<s:property value="tipsInfo"></s:property>
	</div><!-- /content -->

	<div data-role="footer" data-position="fixed" style="text-align:center;padding:10px;">
			   <a href="javascript:back();" style="width:230px;"  data-role="button"  data-icon="back" data-inline="true" data-theme="b">返回</a>
		</div>

</div><!-- /page -->
</body>
</html>
	