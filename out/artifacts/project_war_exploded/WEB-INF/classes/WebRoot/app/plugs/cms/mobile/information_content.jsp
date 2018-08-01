<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title></title>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jquery.mobile-1.3.2.min.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqm-datebox.min.css" />
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.8.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.mobile-1.3.2.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.layout-latest.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jqm-datebox.core.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jqm-datebox.mode.calbox.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.mobile.datebox.i8n.CHN-S.js"></script>
<script type="text/javascript"> 
	function openCms(infoid){
		var url = "cmsOpen.action?infoid="+infoid;
		window.FrameLink.loadLink("查看详细",url); 
	}
	
	function openLink(key){
		window.jumpTools.backTasklist(key);
	}
	
	</script>
<style>
.mb_title {
	color:#000;
	font-size:18px;
	padding-top:12px;
	padding-bottom:12px;
	text-align:center;
	font-family:黑体;
	font-weight:bold;
	
}
.mt_content {
	color:#000;
	font-size:14px;
	padding-left:10px;
	padding-right:10px;
	padding-top:12px;
	padding-bottom:12px;
	text-align:left;
	font-family:宋体;
	line-height:18px;
}
.mt_info {
	color:#ccc;
	font-size:12px;
	padding-top:2px;
	padding-bottom:2px;
	text-align:center;
}
.mt_img {
	margin-top:5px;
	text-align:center;
	padding:2px;
}
.mt_img img{
	padding:5px;
	border:1px solid #ccc;
}
</style>
</head>
<body> 
	<div class="mb_title"><s:property value="model.title" /></div>
	<div class="mt_info"><s:property value="model.source" /> <s:property value="model.releaseman" /> <s:property value="%{getText('{0,date,yyyy-MM-dd }',{model.releasedate})}" /></div>
	<div class="mt_img"><img style="width:280px" src="<s:property value="model.prepicture" />" onError="this.src=''"/></div>
	<div class="mt_content"><s:property value="model.content" escapeHtml="false"/></div>
</body> 
</html>