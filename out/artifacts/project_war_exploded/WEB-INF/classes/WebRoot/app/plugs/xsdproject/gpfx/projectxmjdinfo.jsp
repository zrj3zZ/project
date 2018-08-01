<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>股票发行项目信息阶段</title>
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
<script type="text/javascript" src="iwork_js/engine/iformpage.js" charset="utf-8"></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
	function reportXfx(groupid,task_name) {
		instanceid=$("#instanceid").val();
		if (instanceid == "0") {
			alert("请先保存项目信息!");
			return;
		}
		if(task_name==null||task_name==''){
			alert("阶段配置信息错误!");
			return;
		}
		var projectno = $("#projectno").val();
		var projectname = $("#projectname").val();
		var instanceid = $("#instanceid").val();
		var xmlxnfxServer = $("#xmlxnfxServer").val();
		var zlgdsjfxServer = $("#zlgdsjfxServer").val();
		var sbzlServer = $("#sbzlServer").val();
		var fazlbsServer = $("#fazlbsServer").val();
		var actDefId;
		if(task_name=='项目立项'){
			actDefId = xmlxnfxServer;
		}else if(task_name=='方案资料报审'){
			actDefId = fazlbsServer;
		}else if(task_name=='申报资料'){
			actDefId = sbzlServer;
		}else if(task_name=='资料归档'){
			actDefId = zlgdsjfxServer;
		}
		var pageUrl = encodeURI("processRuntimeStartInstance.action?actDefId=" + actDefId + "&PROJECTNO=" + projectno + "&PROJECTNAME=" + projectname + "&GROUPID=" + groupid+"&JDMC="+task_name);
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
		/* art.dialog.open(pageUrl,{
			id : 'projectTask',
			cover : true,
			title : '股票发行阶段信息',
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			rang : true,
			width : 1200,
			cache : false,
			lock : true,
			height : 800,
			iconTitle : false,
			extendDrag : true,
			autoSize : false,
			content : pageUrl,
			close : function() {
				window.location.reload();
			}
		}); */
	}
	function submitXfx(actDefId,instanceId,excutionId,taskId,task_name){
		var pageUrl = encodeURI("loadProcessFormPage.action?actDefId="+actDefId+"&instanceId="+instanceId+"&excutionId="+excutionId+"&taskId="+taskId+"&JDMC="+task_name);
		art.dialog.open(pageUrl,{
			id : 'projectTask',
			cover : true,
			title : '拟发行信息',
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			rang : true,
			width : 1000,
			cache : false,
			lock : true,
			height : 700,
			iconTitle : false,
			drag : false,//禁用拖拽
			autoSize : false,
			content : pageUrl,
			close : function() {
				window.location.reload();
			}
		});
	}
	function tj(url) {
		var pageUrl = encodeURI(url);
		art.dialog.open(pageUrl,{
			id : 'projectTask',
			cover : true,
			title : '股票发行项目任务',
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			rang : true,
			width : 800,
			cache : false,
			lock : true,
			height : 600,
			iconTitle : false,
			drag : false,//禁用拖拽
			autoSize : false,
			content : pageUrl,
			close : function() {
				window.location.reload();
			}
		});
	} 
	function readLc(lcbh, lcbs, yxid, rwid, prcid, stepid) {
		var pageUrl = "processInstanceMornitor.action?actDefId=" + lcbh + "&actStepDefId=" + stepid + "&prcDefId=" + prcid + "&taskId=" + rwid + "&instanceId=" + lcbs + "&excutionId=" + yxid;
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width=' + win_width+ ',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
	}
	function loadItem(instanceId, groupid) {
		var formid = $("#formid").val();
		var demId = $("#demId").val();
		var projectNo = $("#projectNo").val();
		var projectName = $("#projectName").val();
		var readwrite = $("#readwrite").val();
		var url = encodeURI("loadVisitPage.action?formid="+formid+"&instanceId=" + instanceId + "&demId="+demId+"&PROJECTNO=" + projectNo + "&PROJECTNAME=" + projectName + "&GROUPID=" + groupid);
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width=' + win_width + ',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
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
<div cache="false" border="true" title="" style="border-width: 0px; border-style: solid; border-color: rgb(239, 239, 239); border-image: none; width: 898px; height: 832px;" class="panel-body panel-body-noheader panel-body-noborder">
	<div class="subReportTable" style="width: 99.5%;">
		<div class="tools_nav">
			<!-- <a iconcls="icon-add" plain="true" class="easyui-linkbutton l-btn l-btn-plain" onclick="javascript: newRow160();return;">新增</a>
			<a iconcls="icon-save" plain="true" class="easyui-linkbutton l-btn l-btn-plain" onclick="javascript:saveSubReportData160();return;">保存</a>
			<a iconcls="icon-cancel" plain="true" class="easyui-linkbutton l-btn l-btn-plain" onclick="javascript:deleteRows160();return ;">删除</a> -->
			<a iconCls="icon-reload" plain="true" class="easyui-linkbutton l-btn l-btn-plain" onclick="javascript:window.location.reload();">刷新</a>
		</div>
		<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" dir="ltr" style="width: 898px;">
			<div class="ui-jqgrid-view" style="width: 898px;">
				<div style="width: 898px;" class="ui-state-default ui-jqgrid-hdiv">
					<div class="ui-jqgrid-hbox">
						<table cellspacing="0" cellpadding="0" border="0" aria-labelledby="gbox_subformSUBFORM_DZDX" role="grid" style="width:858px" class="ui-jqgrid-htable">
							<thead>
								<tr role="rowheader" class="ui-jqgrid-labels">
									<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" style="width: 150px;">
										<div class="ui-jqgrid-sortable">阶段名称</div></th>
									<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" style="width: 100px;">
										<div class="ui-jqgrid-sortable">呈报人</div></th>
									<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" style="width: 200px;">
										<div class="ui-jqgrid-sortable">资料内容</div></th>
									<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" style="width: 130px;">
										<div class="ui-jqgrid-sortable">状态</div></th>
									<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" style="width: 130px;">
										<div class="ui-jqgrid-sortable">操作</div></th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
				<div class="ui-jqgrid-bdiv" style="height: 100%; width: 898px;">
					<div style="position:relative;">
						<table cellspacing="0" cellpadding="0" border="0" tabindex="1" role="grid" aria-multiselectable="true" aria-labelledby="gbox_subformSUBFORM_DZDX" class="ui-jqgrid-btable" style="width: 858px;">
							<tbody>
								<tr style="height:auto" role="row" class="jqgfirstrow">
									<td style="height:0px;width:150px;" role="gridcell"></td>
									<td style="height:0px;width:100px;" role="gridcell"></td>
									<td style="height:0px;width:200px;" role="gridcell"></td>
									<td style="height:0px;width:130px;" role="gridcell"></td>
									<td style="height:0px;width:130px;" role="gridcell"></td>
								</tr>
								<s:property value='commonstr' escapeHtml="false"/>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<s:hidden name="xmlxnfxServer"></s:hidden>
<s:hidden name="zlgdsjfxServer"></s:hidden>
<s:hidden name="fazlbsServer"></s:hidden>
<s:hidden name="sbzlServer"></s:hidden>
<s:hidden name="instanceid"></s:hidden>
<s:hidden name="projectno"></s:hidden>
<s:hidden name="projectname"></s:hidden>
</body>
</html>