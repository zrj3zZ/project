<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>操作提示</title>
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
	var api = art.dialog.open.api, W = api.opener;
		$(document).ready(function(){
           $(document).bind("contextmenu",function(e){   
              return false;   
           });
           art.dialog.tips('<s:property value="tipsInfo"/>',2);
		   api.close();
       });
       //调用父页面脚本，关闭办理窗口
        function close(){
        	parent.hiddenSenWindow();
       }
</script>
<style type="text/css">
		.nextStep{
		font-size:16px;
		color:#0000FF;
		text-align: left;
		padding-left: 3px;
		padding-bottom: 10px;
		font-family:"黑体";
		border-bottom:1px #999999 dotted;
		vertical-align:middle;
		word-wrap:break-word;
		word-break:break-all;
		font-weight:500;
		line-height:12px;
		padding-top:5px;
	}
	.ItemTitle{
		font-size:12px;
		color:#0000FF;
		text-align: right;
		padding-left: 10px;
		font-family:"宋体";
		vertical-align:middle;
		word-wrap:break-word;
		word-break:break-all;
		font-weight:800;
		line-height:12px;
		 white-space:nowrap;
	}
	.pageInfo{
		font-size:12px;
		color:#0000FF;
		text-align: left;
		padding-left: 10px;
		font-family:"宋体";
		vertical-align:middle;
		word-wrap:break-word;
		word-break:break-all;
		font-weight:500;
		line-height:12px;
		padding-right:20px;
	}
	.button{
		margin-top:10px;
		border-top:1px solid #990000;
		text-align:right;
		padding-top:20px;
		padding-right:20px;
		height:60px;
		vertical-align:middle;
	}
	#div{ width:450px; padding-top:0px; }
	ul,li{ list-style:none; padding:0; }
	li{ width:80px; float:left; margin:-1px 0 0 -1px;padding:0 2px; border:0px solid #000;line-height:25px}
</style>
</head>
 
<body class="easyui-layout">
	<div region="north" border="false" style="background:#EFEFEF;padding-top:2px;text-align:left;padding-left:10px;">
	</div>
	<div region="center" style="text-align:center;border-bottom:0px #999 dotted;padding:2px;">
		<s:property value="tipsInfo"/>
	</div>
	<div region="south" border="false" style="padding-top:2px;text-align:left;padding:10px;text-align:right;">
			<a href="javascript:close();" class="easyui-linkbutton" plain="false" iconCls="icon-cancel">关闭窗口</a>
	</div>
</body>
</html>
