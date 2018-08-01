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
	<script type="text/javascript" >
	function openSubForm(stepformId){
				var actDefId = $("#actDefId").val();
				var prcDefId = $("#prcDefId").val();
				var excutionId = $("#excutionId").val();
				var instanceId = $("#instanceId").val();
				var taskId = $("#taskId").val();
				var url = "wechat_pr_formpage.action?actDefId="+actDefId+"&prcDefId="+prcDefId+"&instanceId="+instanceId+"&excutionId="+excutionId+"&stepformId="+stepformId+"&taskId="+taskId;
				this.location = url;
		}
	function exexuteSendAction(){
		var formId = $("#formId").val();
		openSubForm(formId);
	}
	</script>
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
	<div data-role="header" class="ui-body-d ui-body"　style="overflow:hidden;" data-position="fixed">
        <div data-role="controlgroup" data-type="horizontal">
		     <s:property value="pageTab" escapeHtml="false"/>
		</div>
	</div>
	<div data-role="content"> 
		<form   id="iformMain" name="iformMain" method="post"  data-ajax="true" action='processRuntimeFormSave.action' >
		<div>
			<s:property value='content' escapeHtml='false'/> 
		</div> 
		<div data-role="footer" data-position="fixed" style="text-align:center;padding-left:10px;">
				<a id="sendBtn"   href="javascript:exexuteSendAction();"  data-iconpos="left" data-inline="true" data-role="button" data-icon="back" ><span style="font-size:20px;height:30px;">　　　返　　　回　　　</span></a>
		</div>
		<!--表单参数-->
		<span style="display:none">
			<s:form name="iformMain" id="iformMain">
			<s:hidden id ="actDefId" name="actDefId"/>
			<s:hidden  id ="prcDefId" name="prcDefId"/>
			<s:hidden id ="actStepDefId" name="actStepDefId"/>
			<s:hidden id ="formId" name="formId"/>
			<s:hidden id ="taskId" name="taskId"/>
			<s:hidden id ="instanceId" name="instanceId"/>
			<s:hidden id ="excutionId" name="excutionId"/>
			<s:hidden id ="dataid" name="dataid"/>
			<s:hidden id ="deviceType" name="deviceType"/>
			</s:form>
			<input name='submitbtn' id='submitbtn' type="submit" />
		</span> 
	</form>
 
	</div><!-- /content --> 
</div><!-- /page --> 
</body>
</html>
<script>
function hideOption(){
	 wx.hideOptionMenu();
}
setTimeout("hideOption()",500);
   
</script>
