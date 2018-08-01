<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 	<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=0">
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css"/>
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link href="iwork_css/jquerycss/cluetip/jquery.cluetip.css" rel="stylesheet" type="text/css"/>
	<link href="iwork_css/reset.css" rel="stylesheet" type="text/css"/>
	<link href="iwork_css/pformpage.css" rel="stylesheet" type="text/css"/>
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
        	color:#666;
        }
        .processingImg{
        	background-image:url(processInstanceMornitor_showImg.action?actDefId=<s:property value="actDefId"/>&actStepDefId=<s:property value="actStepDefId"/>&instanceId=<s:property value="instanceId"/>);
	        background-image:url;
			background-repeat:no-repeat;
			background-position:left top
        }
</style>
</head>
<body   class="easyui-layout">
<div region="center" style="padding-top:10px;" border="false" split="false" >
	<div id="mainFrameTab"  class="easyui-tabs" fit="true" border="false">
				<div title="流程图" class="processingImg" style="width:100%;height:100%;padding:5px;">
					<s:property value="hotArea" escapeHtml="false"/>
				</div>
				<s:if test="isCC==1">
					<div title="抄送记录"   style="width:100%;height:100%;padding:5px;"> 
					<s:property value="ccList"  escapeHtml='false'/>
					</div>
				</s:if>
				<s:if test="isOffLine==1">
					<div title="线下任务跟踪"  style="width:100%;height:100%;"> 
						<iframe width='100%' height='100%' name="offlineFrame" id="offlineFrame" src='processStepOL_showList.action?actDefId=<s:property value="actDefId"/>&actStepDefId=<s:property value="actStepDefId"/>&instanceId=<s:property value="instanceId"/>&excutionId=<s:property value="excutionId"/>&taskId=<s:property value="taskId"/>' frameborder=0  scrolling=yes  marginheight=0 marginwidth=0></iframe>
					</div>
				</s:if>
				<s:if test="isShowLog==1">
					<div title="数据变更历史"  style="width:100%;height:100%;"> 
					<iframe width='100%' height='100%' name="offlineFrame" id="offlineFrame" src='sysForm_showlog.action?modelId=<s:property value="prcDefId"/>&instanceId=<s:property value="instanceId"/>' frameborder=0  scrolling=yes  marginheight=0 marginwidth=0></iframe>
				</div>
				</s:if>
				
				<div title="意见历史" style="width:100%;height:100%;padding:5px;"> 
						<iframe width='100%' height='100%' name="timerLineFrame" id="timerLineFrame" src='processInstanceMornitor_showOpinion.action?actDefId=<s:property value="actDefId"/>&prcDefId=<s:property value="prcDefId"/>&actStepDefId=<s:property value="actStepDefId"/>&instanceId=<s:property value="instanceId"/>&excutionId=<s:property value="excutionId"/>&taskId=<s:property value="taskId"/>' frameborder=0  scrolling=yes  marginheight=0 marginwidth=0></iframe>
				</div>
			</div>
	<s:form name = "editForm" id = "editForm" theme="simple">
		<s:hidden name="actDefId" id="actDefId"></s:hidden> 
		<s:hidden name="actStepDefId" id="actStepDefId"></s:hidden>
		<s:hidden name="instanceId"  id="instanceId"></s:hidden>
		<s:hidden name="excutionId" id="excutionId"></s:hidden>
		<s:hidden name="taskId" id="taskId"></s:hidden>
	</s:form>
</div>
</body>
</html>
