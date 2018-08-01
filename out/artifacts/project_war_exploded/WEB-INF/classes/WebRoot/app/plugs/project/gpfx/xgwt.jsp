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
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/validate/screen.css" />
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
	function showQuestion(title, taskid, questionid) {
		var projectNo = $("#projectNo").val();
		var pageUrl = "url:zqb_gpfx_project_question.action?projectNo=" + projectNo
				+ "&taskid=" + taskid + "&questionId=" + questionid;
		$.dialog({
			id : 'projectTask',
			cover : true,
			title : "[问题反馈]" + title,
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			rang : true,
			width : 650,
			cache : false,
			lock : true,
			height : 650,
			iconTitle : false,
			extendDrag : true,
			autoSize : false,
			content : pageUrl
		});
	}
	function editQuestion(instanceid) {
		var projectNo = $("#projectNo").val();
		var projectName = $("#projectName").val();
		var formid = $("#formid").val();
		var demId = $("#demId").val();
		var pageUrl = encodeURI("openFormPage.action?formid="+formid+"&demId="+demId+"&XMBH="
				+ projectNo
				+ "&XMMC="
				+ projectName
				+ "&instanceId="
				+ instanceid);
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
	}
	
	function addQuestion() {
		var projectNo = $("#projectNo").val();
		var projectName = $("#projectName").val();
		var instanceId = $("#instanceId").val();
		if (instanceId == "0") {
			alert("请先保存项目信息！");
			return;
		}
		var formid = $("#formid").val();
		var demId = $("#demId").val();
		var pageUrl = encodeURI("url:createFormInstance.action?formid="+formid+"&demId="+demId+"&XMBH="
				+ projectNo + "&XMMC=" + projectName);
		/* var pageUrl = encodeURI("url:createFormInstance.action?formid=97&demId=27&XMBH="
				+ projectNo + "&XMMC=" + projectName); */
		 $.dialog({
			id : 'projectGroup',
			cover : true,
			title : '问题',
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			rang : true,
			width : 800,
			top:-500,
			cache : false,
			lock : true,
			height : 450,
			iconTitle : false,
			extendDrag : true,
			autoSize : false,
			content : pageUrl,
			close : function() {
				window.location.reload();
			}
		}); 
	}
	//删除评价
	function removeXGWT() {
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
										var deleteUrl = "zqb_gpfx_project_deleteXgwt.action";
										$.post(deleteUrl,{instanceid : $('[name=colname]')[n].id},function(data) {
											if (data == 'success') {
												$.dialog.tips(data,2,'success.gif');
												window.location.reload();
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
	<div class="tabs-panels" style="width:850px;height:769px;">
		<div class="panel" style="width:850px;display:block;">
			<div title="" class="panel-body panel-body-noheader panel-body-noborder" style="border:0px solid #EFEFEF;border-image:none;width:850px;height:769px;" border="true" cache="false">
				<div class="subReportTable" id="subTableDiv129">
					<div class="tools_nav" id="subTableTools129">
						<a class="easyui-linkbutton l-btn l-btn-plain" id="newRow129" href="#" iconcls="icon-add" plain="true" onclick="javascript:addQuestion();return;">新增</a>
						<a class="easyui-linkbutton l-btn l-btn-plain" id="deleteRows129" href="#" iconcls="icon-cancel" plain="true" onclick="javascript:removeXGWT();return ;">删除</a>
					</div>
					<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_subformSUBFORM_BGSRZXX" style="width:850px;" dir="ltr">
						<div class="ui-jqgrid-view" id="gview_subformSUBFORM_BGSRZXX" style="width:800px;">
							<div class="ui-state-default ui-jqgrid-hdiv"
								style="width:850px;">
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
	<s:hidden name="formid"></s:hidden>
	<s:hidden name="demId"></s:hidden>
</body>
</html>