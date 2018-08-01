<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/design_showlist.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
	<script type="text/javascript" src ="iwork_js/engine/metadata_showlist.js"></script> 
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style>
body {
	font-size:12px;
	padding:0px;
	margin:0px;
	border:0px
}
.content{
	padding-left:10px;
	margin-right:auto;
	text-align:center; 
}
.flowbox {
	width:210px;
	height:110px;
	background:#fff; 
	float:left;
	display:inline-block;
	margin:3px;
	padding:15px;
	position:relative;
	border:1px solid #ccc;
	color:#333;
	
} 
.flowbox:hover {
	width:210px;
	height:110px;
	float:left;
	display:inline-block;
	margin:3px;
	padding:15px;
	position:relative;
	border:1px solid #FF9900;
	color:#FF9900;
	font-weight:bold;
	background:#fcfcfc; 
}
.flowbox_title_bar{
	cursor:pointer;
	background-image:url(iwork_img/icon/database.png);
	background-repeat:no-repeat;
	background-position:top left;
	border-bottom:1px solid #efefef;
	padding-left:15px;
}
.flowbox_title {
	float:left;
	text-align:left;
	font-size:14px;
	
}
.flowbox_date {
	float:right;
	width:70px;
}
.flowbox_del {
	float:right;
	width:20px;
	height:20px;
	cursor:pointer
}
.flowbox_body {
	clear:both;
	display:block;
	padding-top:5px;
}
.flowbox_body h1 {
	font-weight:bold;
	font-size:12px;
	text-align:left;
	padding:5px;
	cursor:pointer
}
.flowbox_body h2 {
	color:#999;
	font-size:12px;
	text-align:left;
	padding:5px;
	cursor:pointer;
	font-weight:100;
}
.flowbox_btn {
	position:absolute;
	right:10px;
	bottom:10px;
	color:#00F
}
.flowbox_btn a {
	text-decoration:none
}
.flowbox_btn a:linked {
color:#00F;
}
.flowbox_btn a:visited {
	color:#00F;
}
.flowbox_btn a:hover {
	color:#00F;
	text-decoration:underline
}
.flowbox_btn a:active {
	color:#00F;
}

</style>
	 
</head>
<script type="text/javascript">
	$(document).bind("contextmenu",function(){return false;});
</script>
<body>
	<s:if test="list==null || list.size()==0">
	            <div class="none_item"><img src="iwork_img/metadata.gif" border="0"> 未找到存储模型</div>
	 </s:if> 
	 <div class="content">
				<s:iterator  value="list" status="status">
					<div class="flowbox"  >
					<div class="flowbox_title_bar"  onclick="openBaseInfo('<s:property value="entitytitle"/>',<s:property value="id"/>);" >
						<table width="100%">
							<tr><td class="flowbox_title">
							<s:property value="entitytitle"/>
							</td>
							<td class="flowbox_del"></td>
							</tr>
						</table>
					</div>
					  <div class="flowbox_body"  onclick="openBaseInfo('<s:property value="entitytitle"/>',<s:property value="id"/>);">
					    <h1>[<s:property value="entityname"/>]</h1>
					    <h2>
								<s:if test="descirption==null||descirption==\"\""> 
										    		<span style="width:300px;">未描述</span>
										    	</s:if>
										    	<s:else>
										    		<s:property value="descirption"/> 
										    	</s:else>
						</h2>
					  </div>
					 
					  <div class="flowbox_btn" ><a href="javascript:del(<s:property value="id"/>);" class="easyui-linkbutton" plain="true" >移除</a> </div>
					</div>

			</s:iterator>
</div>			<s:form name="editForm" id="editForm">
			</s:form>
</body>
</html>
