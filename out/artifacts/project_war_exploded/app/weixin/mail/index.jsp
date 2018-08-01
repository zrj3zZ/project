<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8"> 
  <title>内部邮箱</title>
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=0">
  <link rel="stylesheet" href="iwork_css/weixin/process/style.css">
   	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/mobile/jquery.mobile-1.4.5.min.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqm-datebox.min.css" /> 
	<script src="iwork_js/jqueryjs/jquery-3.0.8.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.mobile-1.4.5.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.layout-latest.js"></script>
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
	<div data-role="header" data-position="fixed"  >
		  <input type="search" name="searchInput"  id="searchInput" value="" placeholder="请输入查询标题...">
	</div>
<!-- /header --> 
 <div data-role="content" >
    <ul data-role="listview"data-inset="true">
	    <li><a href="#">收件箱 <span class="ui-li-count">12</span></a></li>
	    <li><a href="#">标星邮件 <span class="ui-li-count">0</span></a></li>
	    <li><a href="#">已发送 <span class="ui-li-count">4</span></a></li>
	    <li><a href="#">草稿箱 <span class="ui-li-count">328</span></a></li>
	    <li><a href="#">已删除 <span class="ui-li-count">62</span></a></li>
	</ul> 
	<a href="#" data-role="button" data-icon="edit"  data-theme="c">写邮件</a>		
</div>	 
    <div data-role="footer"  data-position="fixed" >
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