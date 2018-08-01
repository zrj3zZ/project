<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>

<%@ page import="java.util.*"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	Long dataid = Long.valueOf(request.getParameter("id"));
 %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>系统日历设置</title>
    <style type="text/css">
    
    </style>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
	<script type="text/javascript">
			$(document).bind("contextmenu",function(){return false;});
			$(function() { 
					$('.left-nav a').click(function(ev) {
						$('.left-nav a.selected').removeClass('selected');
						$(this).addClass('selected');
					});
			});
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
					<table class="layout-grid" cellspacing="0" cellpadding="0" style="width:170px;" >
						<tr>
							<td class="left-nav">
								<dl class="demos-nav">
									<dd><a id="sysCalendar_base" class="selected" href="iwork_sys_calendar_update.action?id=<%=dataid %>" target="iformMapIndexFrame">日历基本设置</a></dd>
									<dd><a id="sysCalendar_history" href="iwork_sys_get_holidays_map.action?id=<%=dataid %>" target="iformMapIndexFrame">节假日设置</a></dd>
									<dd><a id="sysCalendar_api" href="app/plugs/sysworkcalendar/SysWorkCalendarAPI.jsp" target="iformMapIndexFrame">日历API说明</a></dd>
								</dl> 
							</td> 
						</tr>
					</table>
</body>
</html>