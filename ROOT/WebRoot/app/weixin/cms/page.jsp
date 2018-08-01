<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title><s:property value='model.title' escapeHtml='false'/></title> 
  <meta name="viewport" content="width=device-width, initial-scale=0.8, user-scalable=0">
  <link rel="stylesheet" href="iwork_css/weixin/process/style.css">
  	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/mobile/jquery.mobile-1.4.5.min.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqm-datebox.min.css" /> 
	<script src="iwork_js/jqueryjs/jquery-3.0.8.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.mobile-1.4.5.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.layout-latest.js"></script>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script  type="text/javascript" src="iwork_js/weixin/weixin_tools.js"></script> 
	<style type="text/css">
		.content{
			padding:5px;
		}
	</style>
</head>
<body>
<div  data-role="page" id="pageone" >
 <div data-role="content" >
	<div><h2><s:property value="model.title"/></h2></div>
		<div><s:property value='cmsdate' escapeHtml='false'/></div>
			 <s:if test="model.prepicture!=null&&model.prepicture!=\"\""> 
				<div align="center"><img id="cmsFileUploadUrl" width="60%" src="<s:property value='model.prepicture' escapeHtml='false'/>" class="news_img"></img></div>
	       </s:if>
	       <div><s:property value='precontent' escapeHtml='false'/></div>
		
		<div class="content">  
			<s:property value='model.content' escapeHtml='false'/>
		</div>
		<div>
			<s:property value='archives' escapeHtml='false'/>
		</div>
		<div>
			关键词：<s:property value='model.keyword' escapeHtml='false'/>
		</div>
		<div><p><s:property value='model.releaseman' escapeHtml='false'/> 　 来源: <s:property value='model.source' escapeHtml='false'/></p></div>
	</div><!-- /content --> 
</div><!-- /page -->
</body>
</html>