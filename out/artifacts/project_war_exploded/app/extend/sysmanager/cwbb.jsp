<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html style="height: 100%; overflow: hidden;"><head>
<title>财务报表</title>
<meta content="text/html; charset=UTF-8" http-equiv="Content-Type">
<link href="iwork_js/jqueryjs/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" type="text/css">
<link href="iwork_js/lhgdialog/skins/default.css" rel="stylesheet" id="lhgdialoglink">
<link href="iwork_css/common.css" type="text/css" rel="stylesheet">
<link href="iwork_css/jquerycss/icon.css" type="text/css" rel="stylesheet">
<link href="iwork_js/jqueryjs/easyui/themes/gray/easyui.css" type="text/css" rel="stylesheet">
<link href="iwork_css/message/sysmsgpage.css" type="text/css" rel="stylesheet">
<link href="iwork_css/jquerycss/process-icon.css" type="text/css" rel="stylesheet">
<link href="iwork_css/jquerycss/zTreeStyle.css" type="text/css" rel="stylesheet">
<link href="iwork_css/engine/sysenginemetadata.css" type="text/css" rel="stylesheet">
<link href="iwork_themes/easyui/gray/easyui.css" type="text/css" rel="stylesheet">
<link href="iwork_css/public.css" type="text/css" rel="stylesheet">
<link href="iwork_css/common.css" type="text/css" rel="stylesheet">
<link href="iwork_css/jquerycss/process-icon.css" type="text/css" rel="stylesheet">
<link href="iwork_js/jqueryjs/easyui/themes/gray/easyui.css" type="text/css" rel="stylesheet">
<link href="iwork_css/jquerycss/validate/screen.css" media="screen" type="text/css" rel="stylesheet">
<link href="iwork_css/formstyle.css" type="text/css" rel="stylesheet">
<link href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css" type="text/css" rel="stylesheet">
<link href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css" type="text/css" rel="stylesheet">
<link href="iwork_js/jqueryjs/autocomplete/jquery.autocomplete.css" type="text/css" rel="stylesheet">
<script charset="utf-8" src="iwork_js/commons.js" type="text/javascript"></script>
<script charset="utf-8" src="iwork_js/jqueryjs/jquery-3.1.0.min.js" type="text/javascript"></script>
<script charset="utf-8" src="iwork_js/jqueryjs/jquery.easyui.min.js" type="text/javascript"></script>
<script charset="utf-8" src="iwork_js/jqueryjs/languages/grid.locale-cn.js" type="text/javascript"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script charset="utf-8" src="iwork_js/jqueryjs/jquery.jqGrid.min.js" type="text/javascript"></script>
<script charset="utf-8" src="iwork_js/jqueryjs/jquery.validate.js" type="text/javascript"></script>
<script charset="utf-8" src="iwork_js/jqueryjs/jquery.metadata.js" type="text/javascript"></script>
<script charset="utf-8" src="iwork_js/jqueryjs/jquery.form.js" type="text/javascript"></script>
<script charset="utf-8" src="iwork_js/jqueryjs/autocomplete/jquery.autocomplete.min.js" type="text/javascript"></script>
<script charset="utf-8" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script charset="utf-8" src="iwork_js/jqueryjs/languages/messages_cn.js" type="text/javascript"></script>
<script charset="utf-8" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&amp;skin=default" type="text/javascript"></script>
<script charset="utf-8" src="iwork_js/engine/iformpage.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
	function addTask() {
		if(parent.document.getElementById("instanceId").value==0){
			alert("请先保存主表!");
		}else{
			var cwbbformid = $("#cwbbformid").val();
			var cwbbdemid = $("#cwbbdemid").val();
			var khbh = $("#khbh").val();
			var fzgsmc = $("#fzgsmc").val();
			var fzgsid = $("#id").val();
			var pageUrl = encodeURI("createFormInstance.action?formid="+cwbbformid+"&demId="+cwbbdemid+"&KHBH="+khbh+"&FZGSID="+fzgsid+"&FZGSMC="+fzgsmc);
			var target = "_blank";
    		var win_width = window.screen.width;
    		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
    		page.location = pageUrl;
			/* art.dialog.open(pageUrl,{ 
				title:'财务报表表单',
				loadingText:'正在加载中,请稍后...',
				bgcolor:'#999',
				rang:true,
				width:1100,
				cache:false,
				lock: true,
				height:580, 
				iconTitle:false,
				extendDrag:true,
				autoSize:false,
				close:function(){
					window.location.reload();
				}
			}); */
		}
	}
	function upload(instanceId) {
		var cwbbformid = $("#cwbbformidsc").val();
		var cwbbdemid = $("#cwbbdemidsc").val();
		var pageUrl = encodeURI("openFormPage.action?formid="+cwbbformid+"&instanceId=" + instanceId + "&demId="+cwbbdemid);
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
		/* art.dialog.open(pageUrl,{
			id : 'projectTask',
			cover : true,
			title : '报表上传',
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			rang : true,
			width : 1100,
			cache : false,
			lock : true,
			height : 580,
			iconTitle : false,
			extendDrag : true,
			autoSize:false,
			close:function(){
				window.location.reload();
			}
		}); */
	}
	function check(instanceId) {
		var cwbbformid = $("#cwbbformid").val();
		var cwbbdemid = $("#cwbbdemid").val();
		var pageUrl = encodeURI("openFormPage.action?formid="+cwbbformid+"&instanceId=" + instanceId + "&demId="+cwbbdemid);
		art.dialog.open(pageUrl,{
			id : 'projectTask',
			cover : true,
			title : '财务报表查看',
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			rang : true,
			width : 1100,
			cache : false,
			lock : true,
			height : 580,
			iconTitle : false,
			extendDrag : true,
			autoSize:false,
			close:function(){
				window.location.reload();
			}
		});
	}
	function deleteThis(bid){
		var name=$("#fzgsmc").val();
		$.messager.confirm('确认','确认删除报表?',function(result){
			
				
 			
			if(result){
				$.post("zqb_fzgs_updateDeleteStatus.action",{bid:bid},function(data){
					$.post("delCubb.action",{name:encodeURI(name)},function(data){
					alert("删除成功!")
	   				window.location.reload();
					});
		 		});
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

</head>
<body class="easyui-layout layout" style="height: 100%; overflow: hidden; border: medium none; margin: 0px; padding: 0px;">
	<div cache="false" border="true" style="border:0px solid #EFEFEF;border-image:none;height:769px;" title=财务报表">
		<div style="width:855px;height:769px;" class="tabs-panels">
			<div style="width:855px;display:block;" class="panel">
				<div cache="false" border="true" style="border:0px solid #EFEFEF;border-image:none;width:855px;height:769px;" class="panel-body panel-body-noheader panel-body-noborder" title="">
					<div class="subReportTable">
						<div class="tools_nav">
							<a class="easyui-linkbutton l-btn l-btn-plain" plain="true" onclick="javascript:addTask();">
								<span class="l-btn-text icon-add" style="padding-left: 20px;">新增</span>
							</a>
							<a class="easyui-linkbutton l-btn l-btn-plain" plain="true" onclick="javascript:window.location.reload();">
								<span class="l-btn-text icon-reload" style="padding-left: 20px;">刷新</span>
							</a>
						</div>
						<div dir="ltr" style="width:100%;" class="ui-jqgrid ui-widget ui-widget-content ui-corner-all">
							<div style="width:850px;" class="ui-jqgrid-view">
								<div style="display:none;" class="ui- jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
									<a href="javascript:void(0)" style="right:0px;" role="link" class="ui-jqgrid-titlebar-close HeaderButton"><span class="ui-icon ui-icon-circle-triangle-n"></span></a><span class="ui-jqgrid-title"></span>
								</div>
								<div style="width:100%;" class="ui-state-default ui-jqgrid-hdiv">
									<div class="ui-jqgrid-hbox">
										<s:property value="cwbbcontent" escapeHtml="false" />
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<s:hidden id="khbh" name="khbh"></s:hidden>
	<s:hidden id="khmc" name="khmc"></s:hidden>
	<s:hidden id="id" name="id"></s:hidden>
	<s:hidden id="fzgsmc" name="fzgsmc"></s:hidden>
	<s:hidden id="cwbbformid" name="cwbbformid"></s:hidden>
	<s:hidden id="cwbbdemid" name="cwbbdemid"></s:hidden>
	<s:hidden id="cwbbformidsc" name="cwbbformidsc"></s:hidden>
	<s:hidden id="cwbbdemidsc" name="cwbbdemidsc"></s:hidden>
<div class="layout-split-proxy-h"></div>
<div class="layout-split-proxy-v"></div>
</body>
</html>
