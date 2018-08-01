<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

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
		
	$(document).ready(function(){
	
		var winW, winH; 
		if(window.innerHeight) {// all except IE 
			winW = window.innerWidth; 
			winH = window.innerHeight; 
		} else if (document.documentElement && document.documentElement.clientHeight) {// IE 6 Strict Mode 
			winW = document.documentElement.clientWidth; 
			winH = document.documentElement.clientHeight; 
		} else if (document.body) { // other 
			winW = document.body.clientWidth; 
			winH = document.body.clientHeight; 
		}  // for small pages with total size less then the viewport  
		
		$("#KSS_moveBox").css("width",winW + "px");
		$("#KSS_moveBox").css("height",210 + "px");
		$("img").attr("width",winW);
		$("img").attr("height",210);
		
		$("#KinSlideshow").KinSlideshow();
		$("#KinSlideshow").css("padding-right",1);
		$("#KinSlideshow").css("height",winH - 1);
		$("#KinSlideshow").css("padding-top",1);
		$("#KinSlideshow").css("width","99%");
	});
	
	
</script>
</head>
<body>
	<s:property value="htmlShowStr" escapeHtml="false"/>
</body>
</html>
