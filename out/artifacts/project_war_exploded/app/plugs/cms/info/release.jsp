<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>IWORK综合应用管理系统</title>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" href="iwork_js/kindeditor/themes/simple/simple.css" />
	<link rel="stylesheet" href="iwork_js/kindeditor/plugins/code/prettify.css" />
	<script charset="utf-8" src="iwork_js/kindeditor/kindeditor.js"></script>
	<script charset="utf-8" src="iwork_js/kindeditor/lang/zh_CN.js"></script>
	<script charset="utf-8" src="iwork_js/kindeditor/plugins/code/prettify.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" charset="gb2312"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/easyui-lang-zh_CN.js"></script>
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link href="iwork_css/plugs/cmsrelease.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="iwork_js/plugs/cmsrelease.js"></script>
<script type="text/javascript">
var editor1 ;
	$().ready(function() {
		KindEditor.ready(function(K) {
				editor1 = K.create('textarea[name="content"]', { 
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
					filterMode:false 
				});
			});
	});
</script>
<s:property value='pstrScript'  escapeHtml='false'/>
</head>
<body class="easyui-layout">
<div region="north" border="false" split="false" >
<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
<td align="left" >
<a class="easyui-linkbutton" iconCls="icon-ok"  href="#" onClick="Add();">发布</a>
<a class="easyui-linkbutton" iconCls="icon-save"  href="#" onClick="Save();">暂存</a>
<!--<a class="easyui-linkbutton" iconCls="icon-save"  href="#" onClick="SaveUp();">保存</a>-->
<a class="easyui-linkbutton" iconCls="icon-cancel"  href="#" onClick="Remove();">作废</a>
</td>	
</tr>  
</table>
</div>
<div region="center"  border="false" split="false" >
<!-- TOP区 -->
<table  width="100%"  border="0" cellpadding="0" cellspacing="0" class="tb_release">
<tr>
<td align="center" style="font-family: '黑体', 'Arial', 'Helvetica', 'sans-serif'; font-size:18px; color: #999999;">
内容发布
</td>
</tr>
</table>
<!-- 内容区 -->
<div style="width:100%;margin-top:5px;padding:10px;background:#fff;border:1px solid #ccc;">
<form id="releaseform" method="post">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb_release">
<tr>
<td class="td_title" width='16%'>内容来自：</td>
<td><s:property value='release' escapeHtml='false'/></td>
</tr>
<tr>
<td colspan='2' height='22' style='background-color:#efefef;'>&nbsp;&nbsp;基本信息</td>
</tr>
<tr>
<td class="td_title">所属栏目：</td>
<td  class="td_data"><input class="easyui-combotree" id="portletid" name="portletid" url="cmsInfoAction!cmsChannel.action"  editable='false' required="true">&nbsp;<span style='color:red'>*</span></td>
</tr>
<tr>
<td class="td_title">标题：</td>
<td  class="td_data"><input class="easyui-validatebox" type="text" id="infotitle" name="infotitle" size='45' required="true"></input>&nbsp;<span style='color:red'>*</span></td>
</tr>
<tr>
<td class="td_title">显示标题：</td>
<td class="td_data"><input class="easyui-validatebox" type="text" id="brieftitle" name="brieftitle" size='45' required="true"></input>&nbsp;<span style='color:red'>*</span></td>
</tr>
<tr>
<td class="td_title">来源：</td>
<td  class="td_data"><input class="easyui-validatebox" type="text" id="source" name="source" size='20' required="true"></input>&nbsp;<span style='color:red'>*</span></td>
</tr>
<tr>
<td class="td_title">导读图片：</td>
<td  class="td_data"><input class="easyui-validatebox" type="text" id="prepicture" name="prepicture" size='45' required="true"></input>&nbsp;<span style='color:red'>*</span></td>
</tr>
<tr>
<td class="td_title">导读内容：</td>
<td  class="td_data"><textarea id="precontent" name="precontent" style="height:60px;width:327px;"></textarea>&nbsp;<span style='color:red'>*</span></td>
</tr>
<tr>
<td class="td_title">是否支持讨论：</td>
<td class="td_data"><input type="radio" checked id="istalk" name="istalk"  value='0'>是<input type="radio" id="istalk" name="istalk" value='1'>否&nbsp;<span style='color:red'>*</span>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;是否允许复制：<input type="radio" checked id="iscopy" name="iscopy"  value='0'>是<input type="radio" id="iscopy" name="iscopy" value='1'>否&nbsp;<span style='color:red'>*</span></td>
</tr>
<tr>
<td class="td_title">属性：</td>
<td  class="td_data"><input type='checkbox' id='markred' name='markred'>标红<input type='checkbox' id='markbold' name='markbold'>加粗<input type='checkbox' id='marktop' name='marktop'>置顶&nbsp;<span style='color:red'>*</span></td>
</tr>
<tr>
<td class="td_title">浏览权限：</td>
<td class="td_data" ><input class="easyui-validatebox" type="text" id="browse" name="browse" size='45'></input></td>
</tr>
<tr>
<td class="td_title">关键词：</td>
<td  class="td_data"><input class="easyui-validatebox" type="text" id="keyword" name="keyword" size='45'></input>&nbsp;（多个关键词以逗号相隔）</td>
</tr>    
<tr>
<td colspan='2' height='22' style='background-color:#efefef;'>&nbsp;&nbsp;正文</td>
</tr>
<tr>
<td colspan='2'><textarea id="content" name="content" style="height:100px;width:100%"></textarea></td>
</tr>
<tr>
<td colspan='2' height='22' style='background-color:#efefef;' class="td_data">&nbsp;&nbsp;附件</td>
</tr>
<tr>
<td class="td_title">附件：</td>
<td ><input class="easyui-validatebox" type="text" id="archives" name="archives" size='45'></input></td>
</tr>   
</table>
<input type='hidden' id='infoid' name='infoid' value=''>
<input type='hidden' id='status' name='status'>
<input type='hidden' id='type' name='type'>
</form>	 
</div>
<iframe name='hidden_frame' id="hidden_frame" width= "0"  height= "0" style="VISIBILITY: hidden"></iframe>
</body>
</html>
<script type="text/javascript">	
$(function(){
   $('#portletid').combotree('setValue','<s:property value='portletid' escapeHtml='false'/>'+'_channel');	
});	
</script>
