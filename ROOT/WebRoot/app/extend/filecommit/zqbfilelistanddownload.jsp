<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>上传资料下载</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link href="iwork_css/common.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/portal.css">
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/plugins/jquery.portal.js"></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script>
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.src.js"> </script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=default"></script>
<script type="text/javascript" src="iwork_js/engine/mailIformpage.js"></script>
<script type="text/javascript" src="iwork_js/json.js"></script>
<script type="text/javascript">
	$(function() {
		initTreeTo();
	});
	function initTreeTo() {
		var setting = {
			async : {
				enable : true,
				url : "zqb_file_showTongJiJsonTree.action",
				dataType : "json",
				autoParam : [ "id", "nodeType", "companyId","onclick"]
			},
			data : {
				simpleData : {
					enable : true
				}
			},
			callback : {
				onClick : onClick
			}
		};
		$.fn.zTree.init($("#phoneTree"), setting);
	}
	function onClick(event, treeId, treeNode, clickFlag) {
		$("#filename").val(treeNode.name);
	}
	function getValue(url) {
		$("iframe").attr("src",url);
	}
</script>
</head>
<body>
	<div region="west" style="width:15%;height:100%;background-color:#efefef;float:left;border:0px;padding:3px; PADDING:0px; LINE-HEIGHT: 20px; OVERFLOW: auto; " border="false">
			<ul id="phoneTree" class="ztree"></ul>
	</div>
	<iframe id="iframe" name="iframe" scrolling="auto" frameborder="0" style="width:85%;height:100%;float:left;" src=""></iframe>
	<input id="filename" value="" type="hidden"/>
</body>
</html>
