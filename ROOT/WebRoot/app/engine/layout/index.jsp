<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>IFORM Designer</title>
	<meta content="text/html; charset=UTF-8" http-equiv="content-type"/>
	<link rel="stylesheet" href="iwork_js/kindeditor/themes/simple/simple.css" />
	<link rel="stylesheet" href="iwork_js/kindeditor/plugins/code/prettify.css" />
	<link rel="stylesheet" href="iwork_css/formstyle.css" />
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script charset="utf-8" src="iwork_js/kindeditor/kindeditor.js"></script>
	<script charset="utf-8" src="iwork_js/kindeditor/lang/zh_CN.js"></script>
	<script charset="utf-8" src="iwork_js/kindeditor/plugins/code/prettify.js"></script>
	<s:if test="formType==2">
	<script type="text/javascript" src="iwork_js/engine/mibileformdesginer_show.js"></script>
	</s:if>
	<s:else>
	<script type="text/javascript" src="iwork_js/engine/sysengineiformdesginer_show.js"></script>
	</s:else>
	
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	
	<style>
			form {    
				margin: 0;
			}
			textarea {
				display: block;
			}
			.ke-icon-save {
				background-position: 0px -1248px;
				width: 16px;
				height: 16px;
			}
			.ke-icon-rebuildtemplate {
				background-position: 0px -1264px; 
				width: 16px;
				height:16px;
			}
			.ke-icon-refresh {
				background-position: 0px -1264px; 
				width: 16px;
				height:16px;
			}
			.ke-icon-source {
				background-position: 0px -1280px; 
				width: 16px;
				height:16px;
			}
			.ke-icon-refresh {
				background-position: 0px -1296px; 
				width: 16px;
				height:16px;
			}
			.ke-icon-lookupformmap {
				background-position: 0px -1312px; 
				width: 16px;
				height:16px;
			}
			.ke-icon-lookupmetadatamap {
				background-position: 0px -1328px; 
				width: 16px;
				height:16px;
			} 
			.ke-icon-selectfield {
				background-position: 0px -560px; 
				width: 16px;
				height:16px;
			} 
		</style>
	</head>
<body> 
	<s:form name="editform"  id="editform" action="sysEngineIformDesginer_save.action" method="post">
		<textarea name="htmlEditor" id="htmlEditor"  cols="80" rows="12" ><s:property value='iformPage'/></textarea>
		<s:hidden name="formid" id="formid"></s:hidden>
		<s:hidden name="metadataid" id="metadataid"></s:hidden>  
		<s:hidden name="formType" id="formType"></s:hidden>  
		</s:form> 
</body>
</html>