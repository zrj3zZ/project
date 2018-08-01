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
<style type="text/css">
	body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	
	background-position:top left; 
	background-repeat:no-repeat;
} 
.signs_tips{
			margin-top:10px;
			padding:5px;
			font-size:14px;
			font-weight:bold;
			font-family:微软雅黑;
			text-align:left;
		}
		
		 li{
			list-style:none;
			font-size:14px;
			padding:5px;
			text-align:left;
			margin:5px;
			color:#424242;
			border:1px solid #efefef;
		}
		li:hover{
			list-style:none;
			font-size:14px;
			padding:5px;
			cursor:pointer; 
			border:1px solid #F5DA81;
		} 
		.signsTitle{
			font-size:12px;
			font-weight:bold;
			padding-left:15px;
			background-image: url(../../../iwork_img/arrow.png);
			background-repeat:no-repeat;
			background-position:3px 0px ;
			padding-bottom:10px;
		}
		.signsTitle span{
			float:right;
			font-weight:lighter;
			color:#999;
		}
		.signsEnd{
			text-align:right;
			color:#999;
			padding:5px;
			font-size:10px;
			border-top:1px solid #efefef;
		}
		.signsEnd span{
			padding-left:5px;
		}
		.signsDesc{
			border:1px solid #efefef;
			margin:5px;
			padding:15px;
			background-color:#FBFBEF;
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
	<s:property value="listHtml"  escapeHtml='false'/>	
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
