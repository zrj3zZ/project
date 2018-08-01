<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
	<script type="text/javascript">
		//	$(document).bind("contextmenu",function(){return false;});
			$(function() { 
					$('.left-nav a').click(function(ev) {
						$('.left-nav a.selected').removeClass('selected');
						$(this).addClass('selected');
					});
			});
	</script>
	<script type="text/javascript">
		function showStepDef(actDefId,prcDefId,stepid){
			var url = "sysFlowDef_stepMap!load.action?actDefId="+actDefId+"&prcDefId="+prcDefId+"&actStepDefId="+stepid;
			window.open(url,"stepDefFrame",""); 
		}
	</script>
	<style type="text/css">
		.listTitle{
			border-bottom:1px solid #efefef;
			background-color:#efefef;
			padding-left:5px;
		}
	</style>
</head>
<body>
				<div class="listTitle"><img  src="iwork_img/gif53_033.gif" style="margin-right:3px;" border=0>用户节点列表&nbsp;<a href="javascript:this.location.reload();"><img  src="iwork_img/refresh2.gif" border=0></a></div>
					<table class="layout-grid" cellspacing="0" cellpadding="0" style="width:170px;" >
						<tr>
							<td class="left-nav">
								<s:property value='stepList' escapeHtml="false"/>
							</td>
						</tr>
					</table>
</body>
</html>
