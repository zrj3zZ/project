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
	   .title{
	   	padding:5px;
	   	border-bottom:1px solid #333;
	   }
		.content{
			padding:5px;
		}
	.grid{
		padding:5px;
		width:90%
	}
	</style>
</head>
<body>
<div data-role="page">
<div data-role="header" data-position="fixed" data-theme="c">
		<div data-role="controlgroup" data-type="horizontal">
			<a  data-role="button" href="javascript:showList(0)">根目录</a> 
			<s:property value="path" escapeHtml="false"/>
		</div>
	</div> 
	<div data-role="content">
	<div class="title">
	<strong><s:property value="kmDoc.docName"/></strong></div>
	</div>
	<div class="grid">
		<fieldset data-role="controlgroup">
	    <legend>创建人:</legend>
	    <h1><s:property value="createUser" escapeHtml="false"/></h1>
	    </fieldset>
	    <fieldset data-role="controlgroup">
	    <legend>创建时间:</legend>
	    <h1><s:property value="kmDoc.createDate" escapeHtml="false"/></h1>
	    </fieldset>
	    <fieldset data-role="controlgroup">
	      <legend>文件:</legend>
	    <s:property value="fileurl" escapeHtml="false"/>
	    </fieldset>
	     <fieldset data-role="controlgroup">
	      <legend>描述:</legend>
	        <h1><s:property value="kmDoc.memo" escapeHtml="false"/></h1>
	    </fieldset>
	    <fieldset data-role="controlgroup">
	    <legend>关键词:</legend>
	    <h1><s:property value="kmDoc.keyword" escapeHtml="false"/></h1>
	    </fieldset>
	</div>
</div><!-- /page -->
</body>
</html>