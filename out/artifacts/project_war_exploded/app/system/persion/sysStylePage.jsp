<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head><title>IWORK综合应用管理系统</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
<script type="text/javascript" src="iwork_js/system/syspersion_loadsysstylepage.js" ></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<style type="text/css">
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
   cursor:pointer;
	width:180px; 
	height:160px;
	background:#fcfcfc; 
	float:left;
	display:inline-block;
	margin:13px;
	position:relative;
	border:1px solid #ccc;
	color:#333;
	padding:10px;
	font-family:微软雅黑;
} 

.flowbox_select {
	width:180px; 
	height:160px;
	cursor:pointer;
	float:left;
	display:inline-block;
	margin:13px;
	position:relative;
	border:1px solid #ccc;
	color:#333;
	font-weight:bold;
	padding:10px;
	font-family:微软雅黑;
	background:transparent url(iwork_img/accept.png) no-repeat scroll 100% 100%;
	background-color:#ffffcc; 
}
.flowbox_select:hover {
	float:left;
	border:1px solid #FF9900;
	color:#FF9900;
	font-weight:bold;
}
.flowbox:hover {
	float:left;
	border:1px solid #FF9900;
	color:#FF9900;
	font-weight:bold;
	background:#fff; 
}
.flowbox_title_bar{
	cursor:pointer;
	background-repeat:repeat-x;
	border-bottom:1px solid #efefef;
	background-image:url(iwork_img/crumb_bg.png); 
	
}
.flowbox_title {
	float:left;
	text-align:left;
	font-size:12px;
	
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
	font-size:20px;
	text-align:left;
	padding:5px; 
	cursor:pointer;
	font-family:微软雅黑;
}
.flowbox_body h1 span{
	font-weight:bold;
	font-size:20px;
	text-align:left;
	padding:5px; 
	cursor:pointer;
	font-family:微软雅黑;
}
.flowbox_body h2 {
	color:#999;
	font-size:12px;
	text-align:left;
	padding:5px;
	cursor:pointer;
	font-weight:100;
	margin:5px;
	padding:5px;
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
.purview{
	padding:10px;
}
.purview img{
	padding:5px;
	border:1px solid #efefef;
}
.groupTitle{
	text-align:left;
	padding:10px;
	font-size:18px;
	font-family:微软雅黑;
	font-weight:bold
}
.feelTitle{
	padding:5px;
	
}
</style>
</head>	
<body>
<s:form id ="editForm" name="editForm" action="syspersion_saveSysStylePage"  theme="simple">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" >
	 <tr>
      <td>
  		<div  class="tools_nav">
	  		<a id="btnEp" class="easyui-linkbutton" plain="true" icon="icon-save" href="javascript:doSubmit();" >保存</a>
	  		<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
  		</div>
	  </td>
    </tr>
    <tr>
      <td align="center" valign="top" height="100%" >
		<div class="flowbox_body">
					<div class="groupTitle">表单布局</div>
					<div style="padding-left:80px" id="formLayoutDiv">
						<s:if test="formLayoutSet.equals('top')">
							<div id="formLayout_top"  onclick="setFormLayout(this,'top')"  class="flowbox_select">
								<div  class="purview"><img style="width:100px;" src="iwork_img/layout_Top.gif" "/></div>
								<div  class="feelTitle">顶部菜单</div>
							</div>
							</s:if>
							<s:else>
							<div  id="formLayout_top" onclick="setFormLayout(this,'top')" class="flowbox">
								<div  class="purview"><img style="width:100px;" src="iwork_img/layout_Top.gif" "/></div>
								<div  class="feelTitle">顶部菜单</div>
							</div>
							</s:else>
							
						<!--
						<s:if test="formLayoutSet.equals('bottom')">
							<div  id="formLayout_bottom"  onclick="setFormLayout(this,'bottom')"  class="flowbox_select">
								<div  class="purview"><img style="width:100px;" src="iwork_img/layout_Bottom.gif"/></div>
								<div  class="feelTitle">底部菜单</div>
							</div>
						</s:if>
						<s:else>
						<div  id="formLayout_bottom"  onclick="setFormLayout(this,'bottom')"  class="flowbox">
								<div  class="purview"><img style="width:100px;" src="iwork_img/layout_Bottom.gif"/></div>
								<div  class="feelTitle">底部菜单</div>
							</div>
						</s:else>
						-->
						<s:if test="formLayoutSet.equals('right')">
						<div  id="formLayout_right" onclick="setFormLayout(this,'right')"  class="flowbox">
								<div  class="purview"><img style="width:100px;" src="iwork_img/layout_Right.gif" /></div>
								<div  class="feelTitle">右侧菜单</div>
							</div>
						</s:if>
						<s:else>
						<div  id="formLayout_right" onclick="setFormLayout(this,'right')"  class="flowbox">
								<div  class="purview"><img style="width:100px;" src="iwork_img/layout_Right.gif"/></div>
								<div  class="feelTitle">右侧菜单</div>
							</div>
						</s:else>
						<s:if test="formLayoutSet.equals('left')">
						<div  id="formLayout_left" onclick="setFormLayout(this,'left')"  class="flowbox">
								<div  class="purview"><img style="width:100px;" src="iwork_img/layout_Left.gif" /></div>
								<div  class="feelTitle">左侧菜单</div>
							</div>
						</s:if>
						<s:else>
						<div  id="formLayout_left"  onclick="setFormLayout(this,'left')" class="flowbox">
								<div  class="purview"><img style="width:100px;" src="iwork_img/layout_Left.gif"/></div>
								<div  class="feelTitle">左侧菜单</div>
							</div>
						</s:else>
						</div>
					</div>
		
		
					<div class="flowbox_body">
					<div class="groupTitle">皮肤及主题</div>
					<div style="padding-left:80px" id="skinLayoutDiv">
						<s:iterator value="lookandfeel" status='st'>
						<s:if test="skinLayoutSet.equals(key)">
							<div class="flowbox_select" onclick="setLookAndFeel(this,'<s:property value="key"/>')"  >
							
								<div  class="purview"><img style="height:100px;" src="<s:property value="img"/>" /></div>
								<div  class="feelTitle"><s:property value="title"/></div>
							</div>
							</s:if> 
							<s:else>
							<div class="flowbox" onclick="setLookAndFeel(this,'<s:property value="key"/>')"  >
								<div  class="purview"><img style="height:100px" src="<s:property value="img"/>"/></div>
								<div  class="feelTitle"><s:property value="title"/></div>
							</div>
							</s:else>
						</s:iterator>
						</div>
					</div>
     </td>
    </tr>
	</table>
	<s:hidden name="formLayoutSet" id="formLayoutSet"></s:hidden>
	<s:hidden name="skinLayoutSet" id="skinLayoutSet"></s:hidden>
</s:form>
</body>	
</html>
