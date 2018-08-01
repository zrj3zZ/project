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
<link rel="stylesheet" type="text/css" href="iwork_css/message/sysmsgpage.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_css/public.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css" />
<link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/easyui/themes/gray/easyui.css" />
<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css" />
<link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/autocomplete/jquery.autocomplete.css" />
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/autocomplete/jquery.autocomplete.min.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=default"></script>
<script type="text/javascript" src="iwork_js/engine/iformpage.js" charset="utf-8"></script>
<script type="text/javascript">
	// 全选、全清功能
		function selectAll(){
			if($("input[name='colname']").attr("checked")){
				$("input[name='colname']").attr("checked",true);
			}else{
				$("input[name='colname']").attr("checked",false);
			}
		}
	function newPJ(groupid, pjinsid) {
		var projectNo = $("#projectNo").val();
		var projectName = $("#projectName").val();
		var instanceId = $("#instanceId").val();
		if (instanceId == "0") {
			alert("请先保存项目信息！");
			return;
		}
		if (groupid == "") {
			alert("请选择阶段！");
			return;
		}
		var pageUrl;
		if (pjinsid == '0' || pjinsid == '') {
			/* pageUrl = encodeURI("url:createFormInstance.action?formid=151&demId=65&PROJECTNO="
					+ projectNo + "&GROUPID=" + groupid); */
			return;
		} else {

			pageUrl = encodeURI("openFormPage.action?formid=150&demId=65&instanceId="
					+ pjinsid + "&GROUPID=" + groupid);
		}
			var target = "_blank";
		var win_width = window.screen.width;
		var page = window
				.open(
						'form/loader_frame.html',
						target,
						'width='
								+ win_width
								+ ',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
		/* $.dialog({
			id : 'projectTask',
			cover : true,
			title : '项目评价',
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			rang : true,
			width : 600,
			cache : false,
			lock : true,
			height : 500,
			iconTitle : false,
			extendDrag : true,
			autoSize : false,
			content : pageUrl,
			close : function() {
				window.location.reload();
			}
		}); */
	}

	function addPJ() {
	  var projectNo = $("#projectNo").val();
		//var projectName = $("#projectName").val();
		var instanceId = $("#instanceId").val();
		if (instanceId == "0") {
			alert("请先保存项目信息！");
			return;
		}
		var pageUrl = "url:createFormInstance.action?formid=150&demId=65&PROJECTNO="
				+ projectNo;
		/* var target = "_blank";
		var win_width = window.screen.width;
		var page = window
				.open(
						'form/loader_frame.html',
						target,
						'width='
								+ win_width
								+ ',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
		var projectNo = $("#projectNo").val();
		var url = encodeURI("createFormInstance.action?formid=151&demId=65&PROJECTNO="
				+ projectNo);
					var target = "_blank";
		var win_width = window.screen.width;
		var page = window
				.open(
						'form/loader_frame.html',
						target,
						'width='
								+ win_width
								+ ',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl; */
		 $.dialog({
			id : 'projectTask',
			cover : true,
			title : '项目任务评价',
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			rang : true,
			width : 600,
			top:-500,
			cache : false,
			lock : true,
			content : pageUrl,
			height : 500,
			iconTitle : false,
			extendDrag : true,
			close : function() {
				window.location.reload();
			}
		}); 
	}
	//删除评价
	function removePJ() {
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
										if (a === list) {
											$.messager.alert('提示信息',
													'请选择您要删除的行项目!', 'info');
											return;
										}
									}
									//console.log(typeof ($('[name=colname]')[n].id)!=String('chkAll')+","+typeof ($('[name=colname]')[n].id));
									if ($('[name=colname]')[n].checked == true
											&& String($('[name=colname]')[n].id) != String('chkAll')) {
										var deleteUrl = "deletePj.action";
										$
												.post(
														deleteUrl,
														{
															instanceid : $('[name=colname]')[n].id
														},
														function(data) {
															if (data == 'success') {
																$.dialog
																		.tips(
																				data,
																				2,
																				'success.gif');
																window.location
																		.reload();
															} else {
																$.dialog
																		.tips(
																				data,
																				2,
																				'error.gif');
															}
														});
									}
								}
							}
						});

	}
	function removeItem(projectNo, taskid, instanceId) {
		var pageUrl = "zqb_project_remove_pj.action";
		$.post(pageUrl, {
			projectNo : projectNo,
			taskid : taskid,
			instanceid : instanceId
		}, function(data) {
			var alertMsg = "";
			if (data == 'success') {
				alertMsg = "删除成功";
			}
			$.dialog.tips(alertMsg, 2, 'success.gif');

		});

	}
</script>
<style type="text/css">
.title {
	font-size: 30px;
	text-align: center;
}

.grid {
	width: 90%;
	margin-left: auto;
	margin-right: auto;
	border: 1px solid #CCCCCC;
}

.grid th {
	height: 22px;
	background-color: #efefef;
	padding: 5px;
}

.grid td {
	height: 22px;
	padding: 5px;
	border-bottom: 1px solid #efefef;
}
</style>
</style>
</head>
<body class="easyui-layout">
	<div class="tabs-panels" style="width:855px;height:769px;">
		<div class="panel" style="width:855px;display:block;">
			<div title="" class="panel-body panel-body-noheader panel-body-noborder" style="border:0px solid #EFEFEF;border-image:none;width:855px;height:769px;" border="true" cache="false">
				<div class="subReportTable" id="subTableDiv129">
					<div class="tools_nav" id="subTableTools129">
						<a class="easyui-linkbutton l-btn l-btn-plain" id="newRow129" href="#" iconcls="icon-add" plain="true" onclick="javascript:addPJ();return;">新增</a>
						<a class="easyui-linkbutton l-btn l-btn-plain" id="deleteRows129" href="#" iconcls="icon-cancel" plain="true" onclick="javascript:removePJ();return ;">删除</a>
					</div>
					<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_subformSUBFORM_BGSRZXX" style="width:850px;" dir="ltr">
						<div class="ui-widget-overlay jqgrid-overlay" id="lui_subformSUBFORM_BGSRZXX" style="display:none;"></div>
						<div class="ui-jqgrid-view" id="gview_subformSUBFORM_BGSRZXX" style="width:800px;">
							<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix" style="display:none;">
								<a class="ui-jqgrid-titlebar-close HeaderButton" role="link" style="right:0px;" href="javascript:void(0)">
									<span class="ui-icon ui-icon-circle-triangle-n"></span>
								</a>
								<span class="ui-jqgrid-title"></span>
							</div>
							<div class="ui-state-default ui-jqgrid-hdiv" style="width:850px;">
								<div class="ui-jqgrid-hbox">
									<s:property value="content" escapeHtml="false" />
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<s:hidden name="instanceId"></s:hidden>
	<s:hidden name="projectNo"></s:hidden>
	<s:hidden name="projectName"></s:hidden>
	<s:hidden name="readwrite"></s:hidden>
</body>
</html>