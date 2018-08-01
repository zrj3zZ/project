<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
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
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
	//加载导航树  
	$(function() {
		$('#xm').pagination({
			total : <s:property value="xmlxCyrListSize"/>,
			pageNumber : <s:property value="pageNumber"/>,
			pageSize : <s:property value="pageSize"/>,
			onSelectPage : function(pageNumber, pageSize) {
				submitXM(pageNumber, pageSize);
			}
		});
	});
	function submitXM(pageNumber, pageSize) {
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#frmMainXM").submit();
		return;
	}

	function expExcel() {
		//导入excel
		var pageUrl = "sx_doCYExcelExp.action";
		$("#ifrmMain").attr("action", pageUrl);
		$("#ifrmMain").submit();
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
</style>
</head>
<body class="easyui-layout">
	<div region="north" border="false">
		<div class="tools_nav" style="overflow-y: hidden">
			<span style="float:right;padding-right:32px"><a
				href="sx_getXmlxProject.action">以项目为基准统计</a>|以参与人为基准统计</span>
		</div>
	</div>
	<div region="center" border="false">
		<table width="100%">
			<tr>
				<td class="gridTitle">以参与人为基准统计</td>
				<td align="right" style="padding-right:27px">
					<a href="javascript:expExcel();" class="easyui-linkbutton" plain="true" iconCls="icon-excel-exp">导出</a></td>
			</tr>
			<tr>
				<td class="grid" colspan="2">
					<form name='ifrmMain' id='ifrmMain' method="post">
						<table width="100%" style="border:1px solid #efefef">
							<tr class="header">
								<td>参与人姓名</td>
								<td>项目数量</td>
								<td>公司名称</td>
								<td>职务</td>
								<td>最后修改时间</td>
							</tr>
							<s:iterator value="xmlxCyrList" status="ll">
								<tr class="cell">
									<s:if test="#ll.index-1<0||NAME!=xmlxCyrList[#ll.index-1].NAME">
										<td rowspan="<s:property value="CNUM"/>">
											<s:property value="NAME" />
										</td>
										<td rowspan="<s:property value="CNUM"/>">
											<s:property value="NUM" />
										</td>
									</s:if>
									<td><s:property value="CUSTOMERNAME" /></td>
									<td><s:property value="TYPE" /></td>
									<td><s:property value="ZHXGSJ" /></td>
								</tr>
							</s:iterator>
						</table>
					</form>
				</td>
			</tr>
		</table>
		<div style="padding:5px">
			<s:if test="xmlxCyrListSize==0">

			</s:if>
			<s:else>
				<form action="sx_getXmlxCyrProject.action" method="post" name="frmMainXM" id="frmMainXM">
					<div id="xm" style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
					<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
					<s:hidden name="pageSize" id="pageSize"></s:hidden>
				</form>
			</s:else>
		</div>
	</div>
</body>
</html>