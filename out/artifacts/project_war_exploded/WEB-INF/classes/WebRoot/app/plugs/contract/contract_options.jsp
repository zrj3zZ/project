<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
	<head>
	<title>IWORK综合应用管理系统</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
		<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
		<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css" />
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
		<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
		<script language="javascript" src="iwork_js/commons.js"></script>
<script>
function selectcreate(){
     var pageUrl = 'iwork_contract_getoptionsdemidandformid.action';
     $.post(pageUrl, null, function(json) {
			 var proInfo = JSON.parse(json);
			 if (proInfo!=null) {
			  var formid = proInfo.Formid;
			  var demid= proInfo.Demid;
			  var instanceId = $("#instanceId").val();
			     var pageUrl = "openFormPage.action?formid="+formid+"&instanceId="+instanceId+"&demId="+demid;
			     art.dialog.open(pageUrl,{
			    	id:"planlistDlg",
			    	title:"权限修改",
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:'90%',
				    height:'90%',
					close:function(){
					   location.reload();
					}
				 });
        }
     });
}
    
</script>
<style>
.topCss {
padding: 5px;
}

.topCss table {
border: 1px solid #efefef;
}

.topCss tr {
line-height: 20px;
padding: 2px;
}

.topCss td {
height: 30px;
padding: 2px;
padding-left: 10px;
}

.tableList th {
height: 30px;
padding: 2px;
padding-left: 10px;
background-color: #efefef;
text-align: left;
}

.tableList tr {
line-height: 20px;
padding: 2px;
border-bottom: 1px solid #efefef;
}

.tableList tr:hover {
background-color: #f6f6f6;
}

.tableList td {
height: 30px;
padding: 2px;
padding-left: 10px;
text-align: left;
}

.addItemBtn {
line-height: 30px;
text-align: left;
padding-left: 40px;
font-size: 14px;
font-family: 微软雅黑;
cursor: pointer;
}

.addItemBtn span {
background-image: url(iwork_img/add_obj.gif);
background-repeat: no-repeat;
background-position: 20px 5px;
padding: 5px;
padding-left: 40px;
}

.addItemBtn:hover span {
border: 1px solid #efefef;
color: red;
}

.attachDiv {
list-style-type: none;
}

.attachDiv li a {
color: #0000ff;
}
</style>
</head>
<body>
<div class="listCss">
<div class="tools_nav">
<div class="addItemBtn" onclick="javascript:selectcreate();"
style="float: left; width: 140px; height: 10px;">
<span>编辑权限</span>
</div>
</div>
<div class="tableList">
<table width="100%" border="0" align="center" cellpadding="0"
cellspacing="0">
<tr>
<td height="3">
<table width="100%" height="40" border="0" align="center"
cellpadding="0" cellspacing="0">
<tr>
<td width="51%" align="right" valign="top"
style="margin: 10px;">
<fieldset
style="padding: 8px; width: 90%; border: 1px solid #ACBCC9; margin-left: 10px; margin-right: 10px; line-height: 2.0; text-align: left;">
<legend style="vertical-align: middle; text-align: left">
<img src="iwork_img/table_multiple.png" width="16"
height="16" border="0">
设置权限 
</legend>
<table width='90%'>
<tr>
<td class="td_title1"
background='iwork_img/rszz/report-bg-blue2.gif'
width="22%" align="center">
合同管理员：
</td>
<td class="td_data1" align="left">
<s:property value="hashMap.ADMINNAME" />
</td>
</tr>
<tr>
<td class="td_title1"
background='iwork_img/rszz/report-bg-blue2.gif'
width="22%" align="center">
是否支持子合同：
</td>
<td class="td_data1">
<s:if test="hashMap.ISSUPPORT==1">支持</s:if>
<s:if test="hashMap.ISSUPPORT==2">不支持</s:if>

</td>
</tr>

<input name="instanceId" type="hidden" id="instanceId"
value="<s:property value="hashMap.INSTANCEID"/>" />

</table>
</fieldset>
</td>
</tr>
</table>
</td>
</tr>
</table>
</div>
</div>
</body>
</html>
