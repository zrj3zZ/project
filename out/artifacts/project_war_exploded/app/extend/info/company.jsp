<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>项目管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css"
	href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css"
	href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="iwork_css/process_center.css" />
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript"
	src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript"
	src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript">
	function openInfo(instanceid) {
		var formid = 103;
		var demId = 32;
		var url = "loadVisitPage.action?formid=" + formid + "&instanceId="
				+ instanceid + "&demId=" + demId;
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window
				.open(
						'form/loader_frame.html',
						target,
						'width='
								+ win_width
								+ ',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
		return;
	}
</script>
<style type="text/css">
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}

.groupTitle {
	font-family: 黑体;
	font-size: 12px;
	text-align: left;
	color: #666;
	height-line: 20px;
	padding: 5px;
	padding-left: 15px;
}

.itemList {
	font-family: 宋体;
	font-size: 12px;
	padding-left: 15px;
}

.itemList td {
	list-style: none;
	height: 20px;
	padding: 2px;
	padding-left: 20px;
	border-bottom: 1px solid #efefef;
}

.itemList tr:hover {
	color: #0000ff;
	cursor: pointer;
}

.itemList  td {
	font-size: 12px;
}

.itemicon {
	padding-left: 25px;
	background: transparent url(iwork_img/pin.png) no-repeat scroll 0px 3px;
}

.selectBar {
	border: 1px solid #efefef;
	margin: 5px;
	background: #FDFDFD;
}

.selectBar td {
	vertical-align: middle;
	height: 20px;
}

.selectBar td linkbtn {
	color: #0000FF;
	text-decoration: none;
}
</style>
</head>
<body class="easyui-layout">
	<div region="north" border="false" split="false" style="height:40px;"
		id="layoutNorth">
		<div class="process_header">
			<div class="process_head_tab_box">
				<a class="process_head_tab_active" href="zqb_search_company.action">分子公司查询</a>
			</div>
			<div style="padding-top:3px;"></div>
		</div>
	</div>
	<div region="center" border="false" style="padding:5px;">
		<div>

			<table width="100%" class="itemList">

				<tr bgcolor="#E8E8E8">
					<td>编号</td>
					<td>公司名称</td>
					<td>公司类型</td>
					<td>法人代表</td>
					<td>备注信息</td>
				</tr>
				<s:iterator value="companyList" status="status">
					<tr>
						<TD><s:property value="#status.count" /></TD>
						<td><a
							href="javascript:openInfo(<s:property value="INSTANCEID"/>)"><s:property
									value="GSMC" /></a></td>
						<td><a
							href="javascript:openInfo(<s:property value="INSTANCEID"/>)"><s:property
									value="GSLX" /></a></td>
						<td><a
							href="javascript:openInfo(<s:property value="INSTANCEID"/>)"><s:property
									value="FRDB" /></a></td>
						<td><a
							href="javascript:openInfo(<s:property value="INSTANCEID"/>)"><s:property
									value="BZXX" /></a></td>
					</tr>
				</s:iterator>
			</table>
		</div>
	</div>
</body>
</html>












