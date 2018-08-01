<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>IWORK综合应用管理系统</title>

<link href="iwork_css/news_list.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.KinSlideshow-1.2.1.min.js"></script>


<script type="text/javascript">

	function openCms(infoid){
	    var url = "cmsOpen.action?infoid="+infoid;
	    var newWin="newVikm"+infoid;
	    var page = window.open('form/waiting.html',newWin,'width=700,height=500,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
	    page.location=url;
	}
	
</script>
		<style>
		
		.news_list_box table tr {
			height: 18px;
			border-bottom:0px;
		}
		
		.data{ 
			width:40px;
			padding-right: 0px;
		}
		.text-overflow {
			overflow: hidden;
			text-overflow: ellipsis;
			white-space: nowrap;
			width: 150px;
			word-break: keep-all;
		}
		.text-overflow a{
			color:#003399;
			line-height:22px;height:22px;
		}		
		.text-overflow a hover{
			color:#A60000;
			line-height:22px;height:22px;
		}		
		.weatherTitle{
			width:50px; 
		}
		</style>
		
</head>
<body>
    <!-- 内容区 -->
	
	<div class="news_list_box">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<s:property value="htmlShowStr" escapeHtml="false"/> 
			</table>
	</div>
	
	
	
</body>
</html>
