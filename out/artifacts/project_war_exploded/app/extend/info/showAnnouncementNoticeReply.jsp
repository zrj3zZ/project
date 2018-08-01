<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>通知公告回复</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css" />
<link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/easyui/themes/gray/easyui.css" />
<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/iwork/oaknow.css" />
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" charset="utf-8"></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript" src="iwork_js/engine/iformpage.js" charset="utf-8"></script>
<style type="text/css">
.searchtitle {
	text-align: right;
	padding: 5px;
}
.ui-jqgrid tr.jqgrow td {
	white-space: normal !important;
	height: 28px;
	font-size: 12px;
	vertical-align: text-middle;
	padding-top: 2px;
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
	height: 35px;
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
	<div id="blockPage" class="black_overlay" style="display:none"></div>
	<div region="north" style="height:40px;" border="false">
		<div class="tools_nav">
			<table width="100%">
				<tr>
					<td align="left">
						<a href="javascript:this.location.reload();"
						class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
						<a href="javascript:pageClose();" class="easyui-linkbutton"
						plain="true" iconCls="icon-cancel">关闭</a></td>
					<td style="text-align:right;padding-right:10px"></td>
				</tr>
			</table>
		</div>
	</div>
	<div region="center"
		style="text-align:center;border-left:1px #999 dotted;border-right:1px #999 dotted;border-top:1px #999 dotted;border-bottom:0px #999 dotted;padding:2px;">
		<s:form id="editForm" name="editForm" theme="simple">
			<!--表单参数-->
			<div id="border">
				<table style="margin-bottom:5px;" class="ke-zeroborder" border="0"
					cellspacing="0" cellpadding="0" width="100%">
					<tbody>
						<tr>
							<td class="formpage_title">通知公告回复信息</td>
						</tr>
						<tr>
							<td id="help" align="right"></td>
						</tr>
						<tr>
							<td class="line" align="right"></td>
						</tr>
						<tr>
							<td align="left">
								<table class="ke-zeroborder" border="0" cellspacing="0"
									cellpadding="0" width="100%">
									<tbody>
										<tr>
											<td colspan="2">
											<s:iterator value="list" status="status">
													<table class="ke-zeroborder" border="0" cellpadding="0"
														cellspacing="0" width="100%">
														<tbody>
															<tr id="itemTr_1913">
																<td class="td_title" id="title_TZBT" width="180">通知标题</td>
																<td class="td_data" id="data_TZBT"><s:property
																		value="TZBT" /></td>
															</tr>
															<tr id="itemTr_1914">
																<td class="td_title" id="title_ZCHFSJ" width="180">
																	最迟回复时间</td>
																<td class="td_data" id="data_ZCHFSJ"><s:property
																		value="ZCHFSJ" /></td>
															</tr>
															<tr id="itemTr_1915">
																<td class="td_title" id="title_TZNR" width="180">通知内容</td>
																<td class="td_data" id="data_TZNR"><s:property
																		value="TZNR" /></td>
															</tr>
															
															<tr id="itemTr_1916">
																<td class="td_title" id="title_XGZL" width="180">相关资料</td>
																<td class="td_data" id="data_XGZL" colspan="3"><div class="ui-jqgrid-hbox"><s:property value="XGZL" escapeHtml="false" /></div><%-- <a
																	href="<s:property value="URL" />"
																	style="color: #0000ff;"><s:property value="XGZL" /></a> --%></td>
															</tr>
															<tr id="itemTr_1918">
																<td class="td_title" id="title_SFTZ" width="180">
																	是否通知回复人</td>
																<td class="td_data" id="data_SFTZ"><s:property
																		value="SFTZ" /></td>
															</tr>
															<%-- <tr id="itemTr_1917">
																<td class="td_title" id="title_HFR" width="180">回复人</td>
																<td class="td_data" id="data_HFR"><s:property
																		value="HFR" /></td>
															</tr> --%>
														</tbody>
													</table>
												</s:iterator></td>
										</tr><!-- hflist -->
										<tr>
											<td colspan="2">
											<s:iterator value="hflist" status="status">
													<table class="ke-zeroborder" border="0" cellpadding="0"
														cellspacing="0" width="100%">
														<tbody>
															<s:if test="EXTEND1!=null&&EXTEND1!=''">
															<tr id="itemTr_1920">
																<td class="td_title" id="title_EXTEND1" width="180">是否查询</td>
																<td class="td_data" id="data_EXTEND1"><s:if test="EXTEND1==0">否</s:if><s:else>是</s:else></td>
															</tr>
															</s:if>
															<tr id="itemTr_1920">
																<td class="td_title" id="title_CONTENT" width="180">回复内容</td>
																<td class="td_data" id="data_CONTENT"><s:property
																		value="CONTENT" /></td>
															</tr>
															<tr>
																<td class="td_title" id="title_xgzl" width="180">相关资料</td>
																<td class="td_data" id="data_xgzl" colspan="3"><div class="ui-jqgrid-hbox"><s:property value="XGZL" escapeHtml="false" /></div>
																
																</td>
															</tr>
														
														</tbody>
													</table>
												</s:iterator></td>
										</tr><!-- hflist -->
										
									</tbody>
								</table>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<input type="hidden" name="ggid" id="ggid" value="${ggid }" class="{string:true}" />
		</s:form>
	</div>
</body>
</html>
