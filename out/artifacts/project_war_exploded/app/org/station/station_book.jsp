<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="iwork" uri="/WEB-INF/tld/linkbtn.tld"%>
<%@ taglib prefix="cache"  uri="/oscache"%>

<html>
<head><title>子系统管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link href="iwork_css/common.css" rel="stylesheet" type="text/css" />
<link href="iwork_css/org/company_list.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="iwork_js/commons.js"></script>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/linkbutton.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/org/company_list.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style type="text/css">
		ul{
		
		}
		li{
			cursor:pointer;
			text-decoration:none;
			list-style-type:decimal;
			padding:5px;
			color:#333;
			border-bottom:1px solid #efefef;
			font-family:微软雅黑;
		}
		li:hover{
			background:#efefef;
			color:#0000ff;
		}
	</style>
	<script type="text/javascript">
		//装载数据对象
		  var api = art.dialog.open.api, W = api.opener;
			function rebackData(value,title){
				var inputName = $("#inputName").val();
				var inputTitle = $("#inputTitle").val();
				if(inputName!=""&&inputTitle!=""){
					var origin = artDialog.open.origin; 
					var v = origin.document.getElementById(inputName);
					var titleInput = origin.document.getElementById(inputTitle);
					if(v!=null){
						v.value = value;
					}else if(api.get(api.data.dialogName)!=null){
						v = api.get(api.data.dialogName).document.getElementById(inputName);
					}
					if(v!=null){
							v.value = value;
					}
					
					if(titleInput!=null){
						titleInput.value = title;
					}else if(api.get(api.data.dialogName)!=null){
						titleInput = api.get(api.data.dialogName).document.getElementById(inputTitle);
					}
					if(titleInput!=null){
							titleInput.value = title;
					}
				}
				api.close();
			}
		
	</script>
</head>	
<body  class="easyui-layout">
<!-- <div class="menubackground"style="padding-left:5px;padding-top:2px;padding-bottom:2px;"> -->
<div region="north" border="false" split="false"   class="nav" id="layoutNorth">
<div class="tools_nav"> 
		<s:textfield name=""></s:textfield>
		<a href="javascript:location.reload();" class="easyui-linkbutton"  plain="true" iconCls="icon-search" >查询</a>
	</div>
</div>
<div region="center" border="false" split="false"   class="nav" id="layoutNorth">
	<ul>
	 <s:iterator value="station_list" status="status"> 
       <li onclick="rebackData(<s:property value="id"/>,'<s:property value="stationName"/>')"><s:property value="stationName"/></li>  
    </s:iterator>
	</ul>
<form id="editForm" method="post">
<s:hidden  id='inputName' name='inputName'></s:hidden>
<s:hidden  id='inputTitle' name='inputTitle'></s:hidden>
<s:hidden  id='stationId' name='stationId'></s:hidden>
</form>
</div>
</body>
</html>
