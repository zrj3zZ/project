<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html> 
<head>
  <meta charset="utf-8"> 
  <title>报表中心</title>
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=0">
  <link rel="stylesheet" href="iwork_css/weixin/process/style.css">
  	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/mobile/jquery.mobile-1.4.5.min.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqm-datebox.min.css" /> 
	<script src="iwork_js/jqueryjs/jquery-3.0.8.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.mobile-1.4.5.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.layout-latest.js"></script>
	<script  type="text/javascript" src="iwork_js/weixin/weixin_tools.js"></script> 
<script>
$(document).on("pageinit","#pageone",function(){
	  $("ul li").on("tap",function(){
	  		var reportId = $(this).attr("reportId");
	  		var type = $(this).attr("type"); 
	   		var pageurl ="ireport_rt_index.action?reportId="+reportId+"&reportType="+type;
	   		redirectUrl(pageurl); 
	  });                       
}); 

function redirectUrl(url){
	window.location = url;
}
</script>
<script src="iwork_js/weixin/weixin_tools.js"> </script>
</head>
<body style="padding:10px;">
<div  data-role="page" id="pageone" >
<!-- 
 <div data-role="header" style="height:45px;padding-top:10px;" data-position="fixed" data-theme="c">
   <a href="#" data-role="button" data-icon="info" data-iconpos="notext">Delete</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请选择您要查询的报表
</div> 
 -->
 <div data-role="content" >
    <s:property value="html" escapeHtml="false"/>
  </div>
</div>

</div>
</body>
</html>
<script>
function hideOption(){
	 wx.hideOptionMenu();
}
setTimeout("hideOption()",500);
</script>