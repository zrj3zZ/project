<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/process_group_center.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" charset="gb2312"></script>
	<script type="text/javascript" src="iwork_js/commons.js" charset="gb2312"></script>
	<script type="text/javascript" src="iwork_js/engine/process_launcher_center.js"></script>
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" /> 
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
	var api = art.dialog.open.api, W = api.opener;
	$(function(){
	  if(api.get("iformBaseWinDiv")!=null){
			api.get("iformBaseWinDiv",1).close();
		}
		
		$(document).bind("contextmenu",function(e){
              return false;   
        });
	}); 
	</script>
</head>
<body >
<div class="layout_bg">
  <div class="layout_title">选择您要发起的流程</div>
   <s:iterator value="processList">
			<div class="layout_box" onclick="startProcess('<s:property value="actDefId"/>','<s:property value="title"/>');" onmouseover="this.className='layout_box_hover'" onmouseout="this.className='layout_box'">
			    <table width="100%" border="0" cellspacing="0" cellpadding="0">
			      <tr>
			        <td width="29%" rowspan="2"><img src="iwork_img/engine/process_model.png" onerror="this.src='iwork_img/engine/process_model.png'" width="82" height="82" /></td>
			        <td width="71%" style="font-weight:bold"><s:property value="title"/></td>
			      </tr>
			      <tr> 
			        <td valign="top" style="color:#b5b5b5"><s:property value="memo"/></td>
			      </tr>
			    </table>
			  </div>
 </s:iterator>
 
 <s:form name="editForm" id="editForm">
 <s:hidden name="templateId" id="templateId"></s:hidden>
 <s:hidden name="formid" id="formid"></s:hidden> 
 <s:hidden name="metadataid" id="metadataid"></s:hidden>
 </s:form>
</div>
</body>
</html>
