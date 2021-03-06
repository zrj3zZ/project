﻿<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" href="iwork_js/kindeditor/themes/simple/simple.css" />
	<link rel="stylesheet" href="iwork_js/kindeditor/plugins/code/prettify.css" />
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link href="iwork_css/plugs/cmsrelease.css" rel="stylesheet" type="text/css" />
	<script charset="utf-8" src="iwork_js/kindeditor/kindeditor.js"></script>
	<script charset="utf-8" src="iwork_js/kindeditor/lang/zh_CN.js"></script>
	<script charset="utf-8" src="iwork_js/kindeditor/plugins/code/prettify.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" charset="gb2312"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
	  <script type="text/javascript" src="iwork_js/upload/swfobject.js"></script>
	<script type="text/javascript" src="iwork_js/upload/jquery.uploadify.v2.1.4.js"></script>
	 <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/plugs/cmsrelease.js"></script>
<script type="text/javascript">
var editor1 ;
var mainFormValidator;
	$().ready(function() {
		KindEditor.ready(function(K) {
				editor1 = K.create('textarea[name="model.content"]', { 
					cssPath : 'iwork_css/formstyle.css', 
					items : [
						  'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
						'italic', 'underline',  'lineheight',
						'indent', 'outdent',  
						 'link', 'unlink'
					],
					themeType:'simple',
					newlineTag:'br',
					height : '400px',
					width:'98%',
					filterMode:false 
				});
			});
			mainFormValidator =  $("#editForm").validate({
				debug:false
			 });
			 mainFormValidator.resetForm();
	});
</script>
<s:property value='pstrScript'  escapeHtml='false'/>
</head>
<body class="easyui-layout">
<div region="north" border="false" split="false">
<div class="tools_nav" style="text-align:right">
<a class="easyui-linkbutton" iconCls="icon-ok"  href="#" onClick="doDeploy();">发布</a>
<a class="easyui-linkbutton" iconCls="icon-save"  href="#" onClick="Save();">暂存</a>
<a class="easyui-linkbutton" iconCls="icon-close"  href="#" onClick="Remove();">关闭</a>
</div>
</div>
<div region="center"  border="false" split="false" style="text-align:center" >
<s:form  name="editForm" id="editForm"  action="cms_content_deploy" theme="simple">
<!-- TOP区 -->
<table  width="100%"  border="0" cellpadding="0" cellspacing="0">
<tr>
<td align="center" style="font-family: '黑体', 'Arial', 'Helvetica', 'sans-serif'; font-size:18px; color: #999999;">
首页资讯内容发布
</td>
</tr>
<tr>
	<td align="center">
		<table style="width:1000px;background:#fff;border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td align='right' style="width:200px;">所属栏目<span style='color:red'>*</span>：</td>
			<td ><s:select name="model.channelid"  list="portletList" listKey="id" listValue="portletname"  theme="simple"></s:select></td>
			</tr>
			<tr>
			<td align='right'>标题<span style='color:red'>*</span>：</td>
			<td ><s:textfield name="model.title"  cssClass="{maxlength:200,required:true}"  cssStyle="width:400px" theme="simple" ></s:textfield>&nbsp;</td>
			</tr>
			<tr>
			<td align='right'>显示标题<span style='color:red'>*</span>：</td>
			<td><s:textfield name="model.brieftitle"  cssClass="{maxlength:50,required:true}"  cssStyle="width:400px"  theme="simple"></s:textfield></td>
			</tr>
			<tr>
			<td align='right'>来源<span style='color:red'>*</span>：</td>
			<td ><s:textfield name="model.source"  cssClass="{maxlength:200,required:true}"   theme="simple"></s:textfield>&nbsp;</td>
			</tr>
			<tr>
			<td align='right'>导读图片：</td>
			<td >
					<s:file  name="prepicture" theme="simple"></s:file> 
			 </td>
			</tr>
			<tr>
			<td align='right'>导读内容：</td>
			<td ><s:textarea name="model.precontent" theme="simple"  cssStyle="width:400px;height:150px" cssClass="{maxlength:300,required:true}"  ></s:textarea>&nbsp;</td>
			</tr>
			<tr>
			<td align='right'>关键词：</td>
			<td ><s:textfield name="model.keyword"  theme="simple"></s:textfield>（多个关键词以逗号相隔）</td>
			</tr>  
			<tr>
			<td align='right'></td>
				<td>
			       <table width="100%">
			       	<tr>
			       	<td>是否支持讨论：<s:radio  name="model.istalk"  cssStyle="border:0px;" listKey="key" listValue="value"  list="#{'1':'是','0':'否'}" theme="simple"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			       	<td  align='left'>是否允许复制：<s:radio  name="model.iscopy"  cssStyle="border:0px;" listKey="key" listValue="value"  list="#{'1':'是','0':'否'}" theme="simple"/></td>
			       	</tr>
			       				<tr>
			       	<td align='left'>是否标红：<s:radio  name="model.markred"  cssStyle="border:0px;" listKey="key" listValue="value"  list="#{'1':'是','0':'否'}" theme="simple"/></td>
			       	<td>是否加粗：<s:radio  name="model.markbold"  cssStyle="border:0px;" listKey="key" listValue="value"  list="#{'1':'是','0':'否'}" theme="simple"/></td>
			       	<td></td>
			       	<td>是否置顶：<s:radio  name="model.marktop"  cssStyle="border:0px;" listKey="key" listValue="value"  list="#{'1':'是','0':'否'}" theme="simple"/></td>
			       	</tr>
			       </table> 
			    </td>
			</tr>
			<tr>
			<tr>
			<td align='right'>浏览权限：</td>
			<td ><s:textfield name="model.browse"  theme="simple"></s:textfield></td>
			</tr>
			 <tr>
			<td align='right'>附件：</td>
			<td ><input class="easyui-validatebox" type="text" id="archives" name="model.archives" size='45'></input></td>
			</tr>  
			<tr>
			<td  height='22' style='background-color:#efefef;'>&nbsp;&nbsp;正文</td>
			<td>
				<s:textarea name="model.content"  theme="simple"></s:textarea>
			</td>
			</tr>
			</table>
	</td>
</tr>
</table>
<!-- 内容区 -->

<s:hidden name="model.id"></s:hidden>
<s:hidden name="model.status"></s:hidden>
<s:hidden name="model.prepicture"  theme="simple"></s:hidden>&nbsp;
</s:form>
</div>
</body>
</html>
