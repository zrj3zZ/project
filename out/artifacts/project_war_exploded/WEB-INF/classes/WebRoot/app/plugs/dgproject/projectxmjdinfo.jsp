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
		instanceid=$("#instanceId").val();
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
		
		var dxfxlxformid = $("#dxfxlxformid").val();
		var dxfxlxdemid = $("#dxfxlxdemid").val();
		
		var dxfxgkformid = $("#dxfxgkformid").val();
		var dxfxgkdemid = $("#dxfxgkdemid").val();
		
		var dxfxbaformid = $("#dxfxbaformid").val();
		var dxfxbademid = $("#dxfxbademid").val();
		
		var dxfxjyformid = $("#dxfxjyformid").val();//--
		var dxfxjydemid = $("#dxfxjydemid").val();//--
		
		var dxfxrgformid = $("#dxfxrgformid").val();//--
		var dxfxrgdemid = $("#dxfxrgdemid").val();//--
		
		var dxfxjgformid = $("#dxfxjgformid").val();
		var dxfxjgdemid = $("#dxfxjgdemid").val();
		
		var dxfxzzformid = $("#dxfxzzformid").val();
		var dxfxzzdemid = $("#dxfxzzdemid").val();
		
		var dxfxfkformid = $("#dxfxfkformid").val();
		var dxfxfkdemid = $("#dxfxfkdemid").val();
		
		var dgqtwjformid = $("#dgqtwjformid").val();
		var dgqtwjdemid = $("#dgqtwjdemid").val();
		var dgxmzjuformid = $("#dgxmzjuformid").val();
		var dgxmzjudemid = $("#dgxmzjudemid").val();
		
		var formid;
		var demid;
		if(task_name=='立项'){
			formid=dxfxlxformid;
			demid=dxfxlxdemid;
		}else if(task_name=='公告发行方案'){
			formid=dxfxgkformid;
			demid=dxfxgkdemid;
		}else if(task_name=='股东大会决议'){
			formid=dxfxjyformid;
			demid=dxfxjydemid;
		}else if(task_name=='认购起始日'){
			formid=dxfxrgformid;
			demid=dxfxrgdemid;
		}else if(task_name=='发行备案'){
			formid=dxfxbaformid;
			demid=dxfxbademid;
		}else if(task_name=='反馈'){
			formid=dxfxfkformid;
			demid=dxfxfkdemid;
		}else if(task_name=='新增股份挂牌转让'){
			formid=dxfxjgformid;
			demid=dxfxjgdemid;
		}else if(task_name=='项目终止'){
			formid=dxfxzzformid;
			demid=dxfxzzdemid;
		}else if(task_name=='项目组工作记录'){
			formid=dgxmzjuformid;
			demid=dgxmzjudemid;
		}else if(task_name=='其他文件'){
			formid=dgqtwjformid;
			demid=dgqtwjdemid;
		}
		var pageUrl = encodeURI("createFormInstance.action?formid="+formid+"&demId="+ demid + "&PROJECTNO=" + projectno + "&PROJECTNAME=" + projectname + "&GROUPID=" + groupid+"&JDMC="+task_name);
		
		/* var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl; */
		art.dialog.open(pageUrl,{
			id : 'projectTask',
			cover : true,
			title : '项目阶段添加',
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
		});
	}
	function submitXfx(instanceId,task_name){
		var dxfxlxformid = $("#dxfxlxformid").val();
		var dxfxlxdemid = $("#dxfxlxdemid").val();
		
		var dxfxgkformid = $("#dxfxgkformid").val();
		var dxfxgkdemid = $("#dxfxgkdemid").val();
		
		var dxfxgkdemid = $("#dxfxgkdemid").val();
		var dxfxgkdemid = $("#dxfxgkdemid").val();
		
		var dxfxbaformid = $("#dxfxbaformid").val();
		var dxfxbademid = $("#dxfxbademid").val();
		
		var dxfxjyformid = $("#dxfxjyformid").val();//--
		var dxfxjydemid = $("#dxfxjydemid").val();//--
		
		var dxfxrgformid = $("#dxfxrgformid").val();//--
		var dxfxrgdemid = $("#dxfxrgdemid").val();//--
		
		var dxfxjgformid = $("#dxfxjgformid").val();
		var dxfxjgdemid = $("#dxfxjgdemid").val();
		
		var dxfxzzformid = $("#dxfxzzformid").val();
		var dxfxzzdemid = $("#dxfxzzdemid").val();
		
		var dxfxfkformid = $("#dxfxfkformid").val();
		var dxfxfkdemid = $("#dxfxfkdemid").val();
		
		var dgqtwjformid = $("#dgqtwjformid").val();
		var dgqtwjdemid = $("#dgqtwjdemid").val();
		var dgxmzjuformid = $("#dgxmzjuformid").val();
		var dgxmzjudemid = $("#dgxmzjudemid").val();
		
		var formid;
		var demid;
		if(task_name=='立项'){
			formid=dxfxlxformid;
			demid=dxfxlxdemid;
		}else if(task_name=='公告发行方案'){
			formid=dxfxgkformid;
			demid=dxfxgkdemid;
		}else if(task_name=='股东大会决议'){
			formid=dxfxjyformid;
			demid=dxfxjydemid;
		}else if(task_name=='认购起始日'){
			formid=dxfxrgformid;
			demid=dxfxrgdemid;
		}else if(task_name=='发行备案'){
			formid=dxfxbaformid;
			demid=dxfxbademid;
		}else if(task_name=='反馈'){
			formid=dxfxfkformid;
			demid=dxfxfkdemid;
		}else if(task_name=='新增股份挂牌转让'){
			formid=dxfxjgformid;
			demid=dxfxjgdemid;
		}else if(task_name=='项目终止'){
			formid=dxfxzzformid;
			demid=dxfxzzdemid;
		}else if(task_name=='项目组工作记录'){
			formid=dgxmzjuformid;
			demid=dgxmzjudemid;
		}else if(task_name=='其他文件'){
			formid=dgqtwjformid;
			demid=dgqtwjdemid;
		}
		var pageUrl = encodeURI("openFormPage.action?formid="+formid+"&demId="+demid+"&instanceId="+instanceId);
		/* var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl; */
		art.dialog.open(pageUrl,{
			id : 'projectTask',
			cover : true,
			title : '项目阶段编辑',
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			rang : true,
			width : 1100,
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
			extendDrag : true,
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
						<table id="iform_grid1" cellspacing="0" cellpadding="0" border="0" aria-labelledby="gbox_subformSUBFORM_DZDX" role="grid" style="width:858px" class="ui-jqgrid-htable">
							<thead>
								<tr role="rowheader" class="ui-jqgrid-labels">
									<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" style="width: 100px;">
										<div class="ui-jqgrid-sortable">阶段名称</div></th>
									<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" style="width: 100px;">
										<div class="ui-jqgrid-sortable">填报人</div></th>
									<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" style="width: 100px;">
										<div class="ui-jqgrid-sortable">日期</div></th>
									<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" style="width: 370px;">
										<div class="ui-jqgrid-sortable">阶段资料</div></th>
									<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" style="width: 40px;">
										<div class="ui-jqgrid-sortable">操作</div></th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
				<div class="ui-jqgrid-bdiv" style="height: 100%; width: 898px;">
					<div style="position:relative;height: 502px;overflow: scroll;">
						<table id="iform_grid" cellspacing="0" cellpadding="0" border="0" tabindex="1" role="grid" aria-multiselectable="true" aria-labelledby="gbox_subformSUBFORM_DZDX" class="ui-jqgrid-btable" style="width: 858px;">
							<tbody>
								<tr style="height:auto" role="row" class="jqgfirstrow">
									<td style="height:0px;width:100px;" role="gridcell"></td>
									<td style="height:0px;width:100px;" role="gridcell"></td>
									<td style="height:0px;width:100px;" role="gridcell"></td>
									<td style="height:0px;width:370px;" role="gridcell"></td>
									<td style="height:0px;width:40px;" role="gridcell"></td>
								</tr>
								<s:property value='content' escapeHtml="false"/>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<s:hidden name="dxfxlxformid"></s:hidden>
<s:hidden name="dxfxlxdemid"></s:hidden>
<s:hidden name="dxfxgkformid"></s:hidden>
<s:hidden name="dxfxgkdemid"></s:hidden>
<s:hidden name="dxfxbaformid"></s:hidden>
<s:hidden name="dxfxbademid"></s:hidden>

<s:hidden name="dxfxjyformid"></s:hidden>
<s:hidden name="dxfxjydemid"></s:hidden>
<s:hidden name="dxfxrgformid"></s:hidden>
<s:hidden name="dxfxrgdemid"></s:hidden>

<s:hidden name="dxfxjgformid"></s:hidden>
<s:hidden name="dxfxjgdemid"></s:hidden>
<s:hidden name="dxfxzzformid"></s:hidden>
<s:hidden name="dxfxzzdemid"></s:hidden>
<s:hidden name="dxfxfkformid"></s:hidden>
<s:hidden name="dxfxfkdemid"></s:hidden>
<s:hidden name="instanceId"></s:hidden>
<s:hidden name="projectno"></s:hidden>
<s:hidden name="projectname"></s:hidden>

<s:hidden name="dgqtwjformid"></s:hidden>
<s:hidden name="dgqtwjdemid"></s:hidden>
<s:hidden name="dgxmzjuformid"></s:hidden>
<s:hidden name="dgxmzjudemid"></s:hidden>
</body>
</html>