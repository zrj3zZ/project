<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>项目管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/easyui/themes/gray/easyui.css">
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<link href="iwork_css/message/sysmsgpage.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=default"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
	//加载导航树  
	window.onload = function() {
		$('#xm').pagination({
			total : <s:property value="xmlxListSize"/>,
			pageNumber : <s:property value="pageNumber"/>,
			pageSize : <s:property value="pageSize"/>,
			onSelectPage : function(pageNumber, pageSize) {
				submitXM(pageNumber, pageSize);
			}
		});
	};
	function submitXM(pageNumber, pageSize) {
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#frmMainXM").submit();
		return;
	}
	function expExcel() {
		//导入excel
		var pageUrl = "sx_doXMExcelExp.action";
		$("#ifrmMain").attr("action", pageUrl);
		$("#ifrmMain").submit();
	}
	function editUser(name) {
		var userid = name.substring(0, name.indexOf("["));
		var pageUrl = "zqb_project_getXM_List.action?userid=" + userid;
		art.dialog.open(pageUrl,{
			title : '个人信息',
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			rang : true,
			width : 1100,
			cache : false,
			lock : true,
			height : 180,
			iconTitle : false,
			extendDrag : true,
			autoSize : false
		});
	}
</script>
<style type="text/css">
.gridTitle {
	padding-left: 25px;
	height: 20px;
	font-size: 14px;
	font-family: 黑体;
	background: transparent url(iwork_img/table_multiple.png) no-repeat
		scroll 5px 1px;
}

.grid {
	padding: 5px;
	vertical-align: top;
}

.grid table {
	width: 100%;
	border: 1px solid #efefef;
}

.grid th {
	padding: 5px;
	font-size: 12px;
	font-weight: 500;
	height: 20px;
	background-color: #ffffee;
	border-bottom: 1px solid #ccc;
}

.grid tr:hover {
	background-color: #efefef;
}

.grid td {
	padding: 5px;
	line-height: 16px;
}

.cell td {
	margin: 0;
	padding: 3px 4px;
	height: 25px;
	font-size: 12px;
	white-space: nowrap;
	word-wrap: normal;
	overflow: hidden;
	text-align: left;
	border-bottom: 1px dotted #eee;
	border-top: 1px dotted #fff;
	border-right: 1px dotted #eee;
}

.cell:hover {
	background-color: #F0F0F0;
}

.header td {
	height: 30px;
	font-size: 12px;
	padding: 3px;
	white-space: nowrap;
	padding-left: 5px;
	background: url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left
		bottom;
	border-top: 1px dotted #ccc;
	border-right: 1px solid #eee;
}
</style>
</head>
<body class="easyui-layout">
	<div class="tools_nav" region="north" border="false">
		<span style="float:right;padding-right:32px">以项目为基准统计|<a href="sx_getXmlxCyrProject.action">以参与人为基准统计</a></span>
	</div>
	<div region="center" border="false">
		<table width="100%">
			<tr>
				<td class="gridTitle">以项目为基准统计</td>
				<td align="right" style="padding-right:27px">
					<a href="javascript:expExcel();" class="easyui-linkbutton" plain="true" iconCls="icon-excel-exp">导出</a>
				</td>
			</tr>
			<tr>
				<td class="grid" colspan="2">
					<form name='ifrmMain' id='ifrmMain' method="post">
						<table width="100%" style="border:1px solid #efefef">
							<tr class="header">
								<td style="width:10%;text-align: center;">公司名称</td>
								<td style="width:10%;text-align: center;">项目承揽人</td>
								<td style="width:10%;text-align: center;">大区负责人</td>
								<td style="width:10%;text-align: center;">项目负责人</td>
								<td style="width:10%;text-align: center;">督导安排</td>
								<td style="width:10%;text-align: center;">项目注会</td>
								<td style="width:10%;text-align: center;">项目行研</td>
								<td style="width:10%;text-align: center;">项目律师</td>
								<td style="width:10%;text-align: center;">最后修改时间</td>
							</tr>
							<s:iterator value="xmlxList">
								<tr class="cell">
									<td style="text-align: center;"><s:property value="CUSTOMERNAME"/></td>
									<td style="text-align: center;"><a href="javascript:editUser('<s:property value="ZCLR"/>')"><s:property value="ZCLR"/></a></td>
									<td style="text-align: center;"><a href="javascript:editUser('<s:property value="OWNER"/>')"><s:property value="OWNER"/></a></td>
									<td style="text-align: center;"><a href="javascript:editUser('<s:property value="MANAGER"/>')"><s:property value="MANAGER"/></a></td>
									<td style="text-align: center;"><a href="javascript:editUser('<s:property value="DDAP"/>')"><s:property value="DDAP"/></a></td>
									<td style="text-align: center;"><s:property value="XMZK"/></td>
									<td style="text-align: center;"><s:property value="XMHY"/></td>
									<td style="text-align: center;"><s:property value="XMLS"/></td>
									<td style="text-align: center;"><s:property value="ZHXGSJ"/></td>
								</tr>
							</s:iterator>
						</table>
					</form>
				</td>
			</tr>
		</table>
		<div style="padding:5px">
			<s:if test="xmlxListSize==0">

			</s:if>
			<s:else>
				<form action="sx_getXmlxProject.action" method="post" name="frmMainXM" id="frmMainXM">
					<div id="xm" style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
					<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
					<s:hidden name="pageSize" id="pageSize"></s:hidden>
				</form>
			</s:else>
		</div>
</body>
</html>