<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title></title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jquery.mobile-1.3.2.min.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqm-datebox.min.css" /> 
	<script src="iwork_js/jqueryjs/jquery-3.0.8.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.mobile-1.3.2.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.layout-latest.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jqm-datebox.core.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jqm-datebox.mode.calbox.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.mobile.datebox.i8n.CHN-S.js"></script>
<script type="text/javascript" src="iwork_js/mobile/iOSJavascriptBridge.js"></script>
<script type="text/javascript" src="iwork_js/mobile/sys_mobile_adapter.js"></script> 
	<script type="text/javascript"> 
	 	function dosave(){ 
	 		$.ajax({ 
						url: 'subformSaveItem.action?oper=edit', 
						data: $('#editForm').serialize(), 
						type: "post", 
						cache : false, 
						success: function(data) 
						{
									if(data=='true'){
											alertMsg='保存成功';
									}else{
									alertMsg='保存失败(错误编号:'+arr[0]+')';
								}
								showTip(alertMsg);
								 back();
						} 
					});
	 	}
	 	function back(){ 
	 		backTodolist();   
	 	}
	 	function del(){ 
				$.ajax({ 
						url: 'subformItem_remove.action', 
						data: $('#editForm').serialize(), 
						type: "post", 
						cache : false, 
						success: function(data) 
						{
									if(data=='true'){
											alertMsg='保存成功';
									}else{
									alertMsg='保存失败(错误编号:'+arr[0]+')';
								}
									showTip(alertMsg);
								 back();
						} 
					});    
	 	}
	</script>
	<style> 
		.mb_title{
			color:#999;
			font-size:12px;
			padding-top:2px;
			padding-bottom:2px;
		}
		.mb_data{
			padding-left:30px;
			font-size:16px;
			color:#0000FF;
			border-bottom:1px solid #efefef;
		} 
		<s:property value="style" escapeHtml="false"/>
	</style>
</head> 
<body> 

<s:form name="editFrom" id="editForm" action="subformSaveItem.action" >
				<s:property value="content" escapeHtml="false"/>
				<s:hidden name="taskId" id="taskId"></s:hidden>
				<s:hidden name="instanceId" id="instanceId"></s:hidden>
				<s:hidden name="subformid" id="subformid"></s:hidden>
				<s:hidden name="id" id="id"></s:hidden> 
</s:form>
<div data-role="footer" data-position="fixed" style="padding:10px;text-align:center">
    <a href="javascript:back();" style="width:130px;"  data-role="button"  data-icon="back" data-mini="true" data-inline="true">返回</a>
    <a href="javascript:dosave();" style="width:130px;" data-role="button" data-icon="check" data-mini="true" data-inline="true" data-theme="a">保&nbsp;&nbsp;存</a>
</div>

</body>
</html>
