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
	href="iwork_themes/easyui/gray/easyui.css">
<link rel="stylesheet" type="text/css"
	href="iwork_css/process_center.css" />
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript"
	src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript"
	src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript">
	$(function() {
		$('#mainFrameTab').tabs({});
	});

	function openInfo(instanceid) {
		var formid = 105;
		var demId = 34;
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
	font-family: verdana, arial, sans-serif;
	font-size: 11px;
	color: #333333;
	border-width: 1px;
	border-color: #666666;
	border-collapse: collapse;
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
	background-color: #efefef;
}

.itemList  td {
	font-size: 12px;
	vertical-align: top;
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

.pic {
	width: 35px;
	border: 1px solid #efefef;
	padding: 3px;
}
</style>
</head>
<body class="easyui-layout">
	<div region="north" border="false" split="false" style="height:40px;"
		id="layoutNorth">
		<div class="process_header">
			<div class="process_head_tab_box">
				<a class="process_head_tab_active" href="###">股份管理</a>
			</div>
			<div style="padding-top:3px;"></div>
		</div>
	</div>
	<div region="center" border="false" style="padding:5px;">
		<div id="mainFrameTab" class="easyui-tabs" fit="true">
			<div title="股份转让情况" border="false" iconCls="icon-home" cache="false">
				<div>
					<table width="100%" class="itemList">
					
				<tr bgcolor="#E8E8E8">
					<td>编号</td>
					<td>转让人</td>
					<td>转让时间</td>
					<td>转让数量</td>
					<td>转让价格</td>
					<td>受让人</td>
				</tr>
						<s:iterator value="stocklist4" status="status">
							<tr>
								<TD><s:property value="#status.count" /></TD>
								<td><a
									href="javascript:openInfo(<s:property value="INSTANCEID"/>)"><s:property
											value="ZRR" />
										<s:property value="CW" /></a></td>
								<td><a
									href="javascript:openInfo(<s:property value="INSTANCEID"/>)"><s:property
											value="ZRSJ" /></a></td>
								<td><a
									href="javascript:openInfo(<s:property value="INSTANCEID"/>)"><s:property
											value="ZRSL" /></a></td>
								<td><a
									href="javascript:openInfo(<s:property value="INSTANCEID"/>)"><s:property
											value="ZRJG" /></a></td>
								<td><a
									href="javascript:openInfo(<s:property value="INSTANCEID"/>)"><s:property
											value="SRR" /></a></td>
							</tr>
						</s:iterator>
					</table>
				</div>
			</div>
			<div title="股份解限售情况" border="false" iconCls="icon-home" cache="false">
				<div>
					<table width="100%" class="itemList">
					<tr bgcolor="#E8E8E8">
					<td>编号</td>
					<td>人员</td>
					<td>类型</td>
					<td>时间</td>
					<td>解限售前限售股份数量(万股)</td>
				    <td>本次解限售后无限售股份数量(万股)</td>
					</tr>
						<s:iterator value="stocklist3" status="status">
							<tr>
								<TD><s:property value="#status.count" /></TD>
								<td><a
									href="javascript:openInfo(<s:property value="INSTANCEID"/>)"><s:property
											value="USERID" /></a></td>
								<td><a
									href="javascript:openInfo(<s:property value="INSTANCEID"/>)"><s:property
											value="TYPE" /></a></td>
								<td><a
									href="javascript:openInfo(<s:property value="INSTANCEID"/>)"><s:property
											value="JXS_DATE" /></a></td>
								<td><a
									href="javascript:openInfo(<s:property value="INSTANCEID"/>)"><s:property
											value="JXSQ_XSSL" /></a></td>
								<td><a
									href="javascript:openInfo(<s:property value="INSTANCEID"/>)"><s:property
											value="JXSQ_WXSSL" /></a></td>
							</tr>
						</s:iterator>
					</table>
				</div>
			</div>
			<%-- <div title="股东及管理层持股现状" border="false" iconCls="icon-home"
				cache="false">
				<div>
					<table width="100%" class="itemList">
						<tr bgcolor="#E8E8E8">
					<td>编号</td>
					<td>名称</td>
					<td>职务</td>
					<td>持股数（万股）</td>
					<td>持股比例（%）</td>
					<td>限售股份数(万股)</td>
					<td>无限售股份数(万股)</td>
				</tr>
						<s:iterator value="stocklist2" status="status">
							<tr>
								<TD><s:property value="#status.count" /></TD>
								<td><a
									href="javascript:openInfo(<s:property value="INSTANCEID"/>)"><s:property
											value="NAME" /></a></td>
								<td><a
									href="javascript:openInfo(<s:property value="INSTANCEID"/>)"><s:property
											value="ZHIWU" /></a></td>
								<td><a
									href="javascript:openInfo(<s:property value="INSTANCEID"/>)"><s:property
											value="CGS" /></a></td>
								<td><a
									href="javascript:openInfo(<s:property value="INSTANCEID"/>)"><s:property
											value="CGBL" /></a></td>
								<td><a
									href="javascript:openInfo(<s:property value="INSTANCEID"/>)"><s:property
											value="XSGFS" /></a></td>
								<td><a
									href="javascript:openInfo(<s:property value="INSTANCEID"/>)"><s:property
											value="WXSGF" /></a></td>
							</tr>
						</s:iterator>
					</table>
				</div>
			</div>
			<div title="挂牌时股东及管理层持股情况" border="false" iconCls="icon-home"
				cache="false">
				<div>
					<table width="100%" class="itemList">
							<tr bgcolor="#E8E8E8">
					<td>编号</td>
					<td>名称</td>
					<td>职务</td>
					<td>持股数（万股）</td>
					<td>持股比例（%）</td>
					<td>限售股份数(万股)</td>
					<td>无限售股份数(万股)</td>
				</tr>
						<s:iterator value="stocklist1" status="status">
							<tr>
								<TD><s:property value="#status.count" /></TD>
								<td><a
									href="javascript:openInfo(<s:property value="INSTANCEID"/>)"><s:property
											value="NAME" /></a></td>
								<td><a
									href="javascript:openInfo(<s:property value="INSTANCEID"/>)"><s:property
											value="ZHIWU" /></a></td>
								<td><a
									href="javascript:openInfo(<s:property value="INSTANCEID"/>)"><s:property
											value="CGS" /></a></td>
								<td><a
									href="javascript:openInfo(<s:property value="INSTANCEID"/>)"><s:property
											value="CGBL" /></a></td>
								<td><a
									href="javascript:openInfo(<s:property value="INSTANCEID"/>)"><s:property
											value="XSGFS" /></a></td>
								<td><a
									href="javascript:openInfo(<s:property value="INSTANCEID"/>)"><s:property
											value="WXSGF" /></a></td>
							</tr>
						</s:iterator>
					</table>
				</div>
			</div> --%>
		</div>

	</div>
</body>
</html>












