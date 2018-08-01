<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css" />
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/engine/ifromworkbox.js"></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
	$(function() {
		$('#pp').pagination({
			total : <s:property value="totalNum"/>,
			pageNumber : <s:property value="pageNumber"/>,
			pageSize : <s:property value="pageSize"/>,
			onSelectPage : function(pageNumber, pageSize) {
				submitMsg(pageNumber, pageSize);
			}
		});
	});

	function submitMsg(pageNumber, pageSize) {
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#frmMain").submit();
		return;
	}
	$(function() {
		//查询
		$("#search")
				.click(
						function() {
							var customerName = $("#CUSTOMERNAME").val();
							var zqdm = $("#ZQDM").val();
							var ygp = $("#YGP").val();
							var status = $("#STATUS").val();
							var seachUrl = encodeURI("loadCustomer.action?customername="
									+ customerName
									+ "&zqdm="
									+ zqdm
									+ "&ygp="
									+ ygp + "&status=" + status);
							window.location.href = seachUrl;
						});
		$("#chkAll").bind("click", function() {
			$("[name =colname]:checkbox").attr("checked", this.checked);
		});

	});

	function edit(instanceid) {
		var pageUrl = "openFormPage.action?formid=88&demId=21&instanceId="
				+ instanceid;
		art.dialog.open(pageUrl,{
			title : '客户信息维护表单',
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			rang : true,
			width : 1100,
			cache : false,
			lock : true,
			height : 580,
			iconTitle : false,
			extendDrag : true,
			autoSize : false,
			close : function() {
				window.location.reload();
			}
		});
	}
	function addItem() {
		var pageUrl = "createFormInstance.action?formid=88&demId=21";
		art.dialog.open(pageUrl,{
			title : '客户信息维护表单',
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			rang : true,
			width : 1100,
			cache : false,
			lock : true,
			height : 580,
			iconTitle : false,
			extendDrag : true,
			autoSize : false,
			close : function() {
				window.location.reload();
			}
		});

	}
	function remove() {
		$.messager
				.confirm(
						'确认',
						'确认删除?',
						function(result) {

							if (result) {
								var list = $('[name=colname]').length;
								var a = 0;

								for (var n = 0; n < list; n++) {
									if ($('[name=colname]')[n].checked == false
											&& $('[name=colname]')[n].id != 'chkAll') {
										a++;
										if (a == list) {
											$.messager.alert('提示信息',
													'请选择您要删除的行项目!', 'info');
											return;
										}
									}
									//console.log(typeof ($('[name=colname]')[n].id)!=String('chkAll')+","+typeof ($('[name=colname]')[n].id));
									if ($('[name=colname]')[n].checked == true
											&& String($('[name=colname]')[n].id) != String('chkAll')) {
										var deleteUrl = "deleteCustomer.action";
										$
												.post(
														deleteUrl,
														{
															instanceid : $('[name=colname]')[n].id
														},
														function(data) {
															if (data == 'success') {
																window.location
																		.reload();
															} else {
																alert(data);
															}
														});
									}
								}
							}
						});
	}
</script>
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
	<div region="center"  style="padding-left:0px;padding-right:0px;border:0px;background-position:top;height:100%;">
		<form id="editForm" name="editForm">
			<!--表单参数-->
				<table style="margin-bottom:5px;" class="ke-zeroborder" border="0"
					cellpadding="0" cellspacing="0" width="100%">
					<tbody>
						<tr>
							<td align="left">
							<s:iterator value="list" status="status">
								<table class="ke-zeroborder" border="0" cellpadding="0"
									cellspacing="0" width="100%">
									<tbody>
										<tr id="itemTr_1913">
											<td class="td_title" id="title_TZBT" width="180">通知标题</td>
											<td class="td_data" id="data_TZBT"><s:property value="TZBT" /></td>
										</tr>
										<tr id="itemTr_1914">
											<td class="td_title" id="title_ZCHFSJ" width="180">
												最迟回复时间</td>
											<td class="td_data" id="data_ZCHFSJ"><s:property value="ZCHFSJ" /></td>
										</tr>
										<tr id="itemTr_1915">
											<td class="td_title" id="title_TZNR" width="180">通知内容</td>
											<td class="td_data" id="data_TZNR">
											<textarea style="width:550px;height:100px;" readonly><s:property value="TZNR" escapeHtml="false" /></textarea>
											</td>
										</tr>
										<tr id="itemTr_1916">
											<td class="td_title" id="title_XGZL" width="180">相关资料</td>
											<td class="td_data" id="data_XGZL" colspan="3"><div class="ui-jqgrid-hbox"><s:property value="COUNT" escapeHtml="false" /></div><%-- <a href="<s:property value="URL" />" style="color: #0000ff;"><s:property value="XGZL"/> --%></a></td>
										</tr>
										<tr id="itemTr_1918">
											<td class="td_title" id="title_SFTZ" width="180">
												是否通知回复人</td>
											<td class="td_data" id="data_SFTZ"><s:property value="SFTZ" /></td>
										</tr>
										<!--  <tr id="itemTr_1917">
											<td class="td_title" id="title_HFR" width="180">回复人</td>
											<td class="td_data" id="data_HFR">
												<s:property value="HFR" /></td>
										</tr>-->
									</tbody>
								</table>
							</s:iterator>
							</td>
						</tr>
					</tbody>
				</table>
		</form>
</body>
</html>