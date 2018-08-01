<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html>
<head>
 <meta name="viewport" content="width=device-width, initial-scale=0.8, user-scalable=0">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>跟踪记录</title>
	 <link rel="stylesheet" href="iwork_css/weixin/process/style.css">
  	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/mobile/jquery.mobile-1.4.5.min.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqm-datebox.min.css" /> 
	<script src="iwork_js/jqueryjs/jquery-3.0.8.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.mobile-1.4.5.min.js"></script>
	<script  type="text/javascript" src="iwork_js/weixin/weixin_tools.js"></script> 
	
<style>
   td{
   	padding:5px;
   }
.opinionContent{
	background:#ffffcc;
	border:1px solid #efefef;
	padding:5px;
	color:#993333;
}
.hr{
	border-bottom:1px dotted #ccc;
	text-align:left;
	color:#999;
}
.opinion_tb{
	border:1px solid #efefef;
	background:#fff;
}
</style>
</head>
<body>
	<div data-role="page" class="type-interior"> 
	<div data-role="content"> 
		<form   id="iformMain" name="iformMain" method="post"  data-ajax="true" >
					<ul data-role="listview" data-inset="true" class="ui-icon-alt">
						<s:iterator value="opinions" status="status">
						 	<li id="item_<s:property value='id'/>" >
						 	<a href="javascript:setAddress('opinion','<s:property value='content'/>')" ><s:property value='content'/></a>
						 	</li>
						</s:iterator> 
					</ul>
	</form>
	</div><!-- /content --> 
</div><!-- /page --> 
</body>
</html>
