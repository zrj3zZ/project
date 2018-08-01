<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8"> 
  <title>知识管理</title>
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=0">
  <link rel="stylesheet" href="iwork_css/weixin/process/style.css">
  	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/mobile/jquery.mobile-1.4.5.min.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqm-datebox.min.css" /> 
	<script src="iwork_js/jqueryjs/jquery-3.0.8.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.mobile-1.4.5.min.js"></script>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script  type="text/javascript" src="iwork_js/weixin/weixin_tools.js"></script> 
	<script type="text/javascript">
		   <s:property value="initWeiXinScript" escapeHtml="false"/>
		   function showList(parentid){
		   		var pageUrl = "weixin_km_index.action?parentid="+parentid;
		   		selectUrl(pageUrl); 
		   }
		   function selectUrl(url){
				window.location.href = url;
			}
	</script>
</head>
<body ontouchstart="">
<div data-role="page" id="pageone">
<!-- /header --> 
	<div data-role="header" data-position="fixed" >
		<div data-role="controlgroup" data-type="horizontal">
			<a  data-role="button" href="javascript:showList(0)">根目录</a> 
			<s:property value="path" escapeHtml="false"/>
		</div>
	</div> 
	 <div data-role="content" >
    <ul data-role="listview" data-inset="true">
    	<s:property value="html" escapeHtml="false"/>
	   
	</ul>
	</div>
    <div data-role="footer"  data-position="fixed">
	   	
	</div><!-- /footer -->
</div> 
</body>
</html>
<script>
function hideOption(){
	 wx.hideOptionMenu();
}
setTimeout("hideOption()",500);
   
</script>