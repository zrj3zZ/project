<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css"/>
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link href="iwork_css/jquerycss/cluetip/jquery.cluetip.css" rel="stylesheet" type="text/css"/>
	<link href="iwork_css/reset.css" rel="stylesheet" type="text/css"/>
	<script type="text/javascript" src="iwork_js/commons.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.src.js"  > </script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/cluetip/jquery.cluetip.js" ></script>
<title>流程跟踪</title>
<head> 
<script type="text/javascript">
var api = frameElement.api, W = api.opener;
$(function(){
			$(".acss").cluetip({
				cluetipClass: 'rounded', 
				dropShadow: false, 
				arrows:true,
				positionBy: 'mouse',
				showTitle: false
			});
});

function showPopWin(actStepDefId){
	$('#mainFrameTab').tabs('select',"离线任务跟踪");
	var actDefId = $("#actDefId").val();
	var instanceId = $("#instanceId").val();
	var excutionId = $("#excutionId").val();
	var pageurl = "processStepOL_showList.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&instanceId="+instanceId+"&excutionId="+excutionId
	$('#offlineFrame').attr("src",pageurl);   
}

</script>
 

<style type="text/css">
	body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	
	background-position:top left; 
	background-repeat:no-repeat;
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
.opinion_toolbar{
	border:1px solid #efefef;
}
.opinion_tb{
	border-top:1px solid #efefef;
}
.opinion_title{
       	 background:url(../iwork_img/engine/tools_nav_bg.jpg) repeat-x;
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
        	color:#666;
        }
</style>
<script type="text/javascript">
	function setView(){
			//$("#view").attr("class","view");
			var url ="processInstanceMornitor_showOpinion.action?type=timerLine&actDefId="+$("#actDefId").val()+"&prcDefId="+$("#prcDefId").val()+"&actStepDefId="+$("#actStepDefId").val()+"&instanceId="+$("#instanceId").val();
			this.location = url;
	}
</script>
</head>
<body   class="easyui-layout">
<div region="north" border="false" >
<div class="tools_nav"> 
	<span class="view2" id="view" onclick="setView()"></span> 
	 </div>
	 </div>
<div region="center"  border="false" split="false" style="paading:5px;">
	<s:property value="opinionList"  escapeHtml='false'/>	
	<s:form id="editForm" name="editForm">
	<s:hidden name="actDefId" id="actDefId"></s:hidden>
	<s:hidden name="prcDefId" id="prcDefId"></s:hidden>
	<s:hidden name="actStepDefId" id="actStepDefId"></s:hidden>
	<s:hidden name="instanceId" id="instanceId"></s:hidden>
	<s:hidden name="type" id="type"></s:hidden>
</s:form>	
</div>
</body>
</html>
