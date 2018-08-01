<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>意见历史</title>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
<link href="iwork_css/common.css" type="text/css" rel="stylesheet">
<link href="iwork_css/plugs/timeline/css/timerline.css" type="text/css" rel="stylesheet">
<script src="iwork_js/jqueryjs/jquery-3.0.7.min.js" type="text/javascript"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>
<style type="text/css">
* {
	PADDING-BOTTOM: 0px; MARGIN: 0px; PADDING-LEFT: 0px; PADDING-RIGHT: 0px; PADDING-TOP: 0px
}
		.view {
			background:url(iwork_img/engine/view1.png);
			display:block;
			height:26px;
			width:61px;
			margin-right:10px;
			margin-top:3px;
			float:right;
			cursor:pointer
		}
		.view2 {
			background:url(iwork_img/engine/view2.png);
			display:block;
			height:26px;
			width:61px;
			margin-right:10px;
			margin-top:3px;
			float:right;
			cursor:pointer
		}
		.showWinTitle{
	font-size:12px;
	text-align:right;
	color:#003399;
}
.showWinDate{
font-size:12px;
	text-align:left;
	color:#0000FF;
}
.opinion_title{
       	 background:url(../iwork_img/engine/tools_nav_bg.jpg) repeat-x;
        	font-size:12px;
        	padding:2px;
        	padding-left:15px;
        	padding-right:3px;
        	background-color:#fff;
        	width:160px;
        	height:20px;
        	color:#333;
        	text-align:left;
        	font-family:黑体;
        	border-right:1px solid #efefef;
        	border-bottom:1px solid #efefef;
        }
        .opinion_step{
        	font-size:12px;
        	padding:2px;
        	padding-left:15px;
        	padding-right:3px;
        	background-color:#fff;
        	width:160px;
        	height:30px;
        	color:#333;
        	text-align:left;
        	font-family:黑体;
        	border-right:1px solid #efefef;
        	border-bottom:1px solid #efefef;
        }
        .opinion_content{
        	padding:2px;
        	padding-left:15px;
        	padding-right:3px;
        	font-size:12px;
        	color:red;
        	width:300px;
        	text-align:left;
        	border-bottom:1px solid #efefef;
        	border-right:1px solid #efefef;
        }
         .opinion_item{
        	text-align:left;
        	padding:2px;
        	font-size:12px;
        	padding-left:3px;
        	padding-right:10px;
        	border-bottom:1px solid #efefef;
        	border-right:1px solid #efefef;
        	color:#efefef;
        }
        
</style>
<script type="text/javascript">
	function setView(){
		//	$("#view").attr("class","view2");
			var url ="processInstanceMornitor_showOpinion.action?type=grid&actDefId="+$("#actDefId").val()+"&prcDefId="+$("#prcDefId").val()+"&actStepDefId="+$("#actStepDefId").val()+"&instanceId="+$("#instanceId").val();
			this.location = url; 
	}
</script>
</head>
<body class="easyui-layout">
<div region="north" border="false" >
<div class="tools_nav">
	<span class="view" id="view" onclick="setView()"></span>
</div>
</div>
<div region="center" border="false"  >
<!--ShowMsg-->
<div id="Container">
  <div class="timeline_container">
    <div class="timeline">
      <div class="plus"></div>
      <div class="plus2"></div>
    </div>
  </div>
 <s:property value="opinionList" escapeHtml="false"/>
</div>
<s:form id="editForm" name="editForm">
	<s:hidden name="actDefId" id="actDefId"></s:hidden>
	<s:hidden name="prcDefId" id="prcDefId"></s:hidden>
	<s:hidden name="actStepDefId" id="actStepDefId"></s:hidden>
	<s:hidden name="instanceId" id="instanceId"></s:hidden>
	<s:hidden name="type" id="type"></s:hidden>
</s:form>
<!--/ShowMsg-->

<script type="text/javascript" src="iwork_js/plugs/Smohan.blog.plug.js"></script>
<script type="text/javascript">
$(document).ready(function() {
  /*时间轴*/
  $('#Container').masonry({itemSelector : '.item'});
	function Arrow_Points(){
	  var s = $("#Container").find(".item");
	  $.each(s,function(i,obj){
		var posLeft = $(obj).css("left");
		if(posLeft == "0px"){
		  html = "<span class='rightCorner'></span>";
		  $(obj).prepend(html);
		} else {
		  html = "<span class='leftCorner'></span>";
		  $(obj).prepend(html);
		}
	  });
	}
	Arrow_Points();
});
</script>
</div>
</body>
</html>
