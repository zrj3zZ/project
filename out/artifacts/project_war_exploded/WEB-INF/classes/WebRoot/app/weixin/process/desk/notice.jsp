<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>传阅/通知</title>
  <meta name="viewport" content="width=device-width, initial-scale=0.8, user-scalable=0">
  <link rel="stylesheet" href="iwork_css/weixin/process/style.css">
  	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/mobile/jquery.mobile-1.4.5.min.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqm-datebox.min.css" /> 
	<script src="iwork_js/jqueryjs/jquery-3.0.8.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.mobile-1.4.5.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.layout-latest.js"></script>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script>
  <s:property value="initWeiXinScript" escapeHtml="false"/>
  $(document).on("pageinit","#pageone",function(){
		  $("#searchInput").on( "change", function(event, ui) {
		  		var key = $("#searchInput").val();
		  		var pageUrl = "weixin_processdeskNotice.action?key="+key;
		  		redirectUrl(pageUrl);
		  });
		  $("#itemList li").on("tap",function(){
	  		var taskId = $(this).attr("id");
	  		var actDefId = $(this).attr("actDefId");
	  		var instanceId = $(this).attr("instanceId");
	  		var excutionId = $(this).attr("excutionId");
	  		var taskType = $(this).attr("taskType");
	  		window.location ="weixin_NoticeFormPage.action?actDefId="+actDefId+"&instanceId="+instanceId+"&excutionId="+excutionId+"&taskId="+taskId;
	  }); 
  });
  
  function redirectUrl(url){
	window.location = url;
  }
  
  function showMore(pageNo,key){
  	if(key==''){
	  	 key = $("#searchInput").val();
	  }
  	$.post('weixin_processdeskNoticeHtml.action',{pageNo:pageNo,key:key}, function (text, status) {
  			$("#moreLi").remove();
  			$("#itemList").append(text).listview('refresh').trigger("create");;
	   });
  }
</script>
<script src="iwork_js/weixin/weixin_tools.js"> </script>

</head>
<body ontouchstart="">

<div data-role="page" id="pageone">
<!-- /header --> 
<div data-role="header" data-position="fixed">
<input type="search" name="searchInput" id="searchInput" value="" placeholder="请输入查询标题...">
</div>
      <s:property value="listHtml" escapeHtml="false"/>
    <div data-role="footer"  data-position="fixed">
	   <div data-role="navbar">
        <ul>
            <li><a href="javascript:redirectUrl('weixin_processdeskIndex.action')" data-icon="grid" >待办流程</a></li>
            <li><a href="javascript:redirectUrl('weixin_processdeskHistory.action')" data-icon="star">已办跟踪</a></li>
            <li><a href="#" data-icon="gear"  class="ui-btn-active">传阅/通知</a></li>
        </ul>
    </div><!-- /navbar -->  
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