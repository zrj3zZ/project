<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8"> 
  <title>发起中心</title>
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=0">
  <link rel="stylesheet" href="iwork_css/weixin/process/style.css">
  	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/mobile/jquery.mobile-1.4.5.min.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqm-datebox.min.css" /> 
	<script src="iwork_js/jqueryjs/jquery-3.0.8.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.mobile-1.4.5.min.js"></script>
	<script  type="text/javascript" src="iwork_js/weixin/weixin_tools.js"></script> 
<script>
  $(document).on("pageinit","#pageone",function(){
	  $("ul li").on("tap",function(){
	  		var actDefId = $(this).attr("actDefId");
	   		window.location ="wechat_pr_instace.action?actDefId="+actDefId;
	  });                       
}); 

function redirectUrl(url){
	window.location = url;
}

</script>
<script src="iwork_js/weixin/weixin_tools.js"> </script>

</head>
<body >
<div  data-role="page" id="pageone" >
<h3  class="ui-bar ui-bar-a">请选择您要发起的流程</h3>

 <div data-role="content" >
     <s:property value="listHtml" escapeHtml="false"/>
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