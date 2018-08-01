<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
<link href="iwork_css/common.css" type="text/css" rel="stylesheet">
<link href="iwork_css/jquerycss/process-icon.css" type="text/css" rel="stylesheet">
<link href="iwork_css/jquerycss/default/easyui.css" type="text/css" rel="stylesheet">
<link type="text/css" rel="stylesheet" href="iwork_css/public.css">
<link href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css" type="text/css" rel="stylesheet">
<link href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css" type="text/css" rel="stylesheet">
<link href="iwork_css/jquerycss/validate/screen.css" media="screen" type="text/css" rel="stylesheet">
<link href="iwork_css/engine/iformpage.css" type="text/css" rel="stylesheet">
<link href="iwork_js/jqueryjs/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" type="text/css">
<script src="iwork_js/commons.js" language="javascript"></script>
<script src="iwork_js/jqueryjs/jquery-3.1.0.min.js" type="text/javascript"></script>
<script src="iwork_js/jqueryjs/jquery.easyui.min.js" type="text/javascript"></script>
<script src="iwork_js/jqueryjs/languages/grid.locale-cn.js" type="text/javascript"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script src="iwork_js/jqueryjs/jquery.jqGrid.min.js" type="text/javascript"></script>
<script src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" type="text/javascript" charset="utf-8"></script>
<script src="iwork_js/lhgdialog/lhgdialog.min.js" type="text/javascript"></script>
<script charset="utf-8" src="iwork_js/jqueryjs/jquery.validate.js" type="text/javascript"></script>
<script charset="utf-8" src="iwork_js/jqueryjs/jquery.metadata.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
function openFormPage(instanceid){
	var demid = $("#xmlxdemid").val();
	var formid = $("#xmlxformid").val();
	var pageUrl = "openFormPage.action?formid="+formid+"&instanceId="+instanceid+"&demId="+demid;
	art.dialog.open(pageUrl,{ 
		title:'项目阶段提交资料清单',
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
	});	
	
}
function addItem(){
	var demid = $("#xmlxdemid").val();
	var formid = $("#xmlxformid").val();
	var pageUrl = "createFormInstance.action?formid="+formid+"&demId="+demid;
	art.dialog.open(pageUrl,{  
		title:'项目阶段提交资料清单',
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
	});	
}
function checkAll() {
	$("[name=colname]:checkbox").attr("checked", this.checked);
}
//执行删除操作
function remove(){
	$.messager.confirm('确认','确认删除?',function(result){ 
		if(result){
			var list = $('[name=colname]').length;
			//alert(list);
			var a=0;
			var ids="";
			$("input[name='colname']:checkbox").each(function(){
			    if (true == $(this).attr("checked")) {
			    	ids += $(this).attr('value')+'-';
			    }
			});
			if(ids!=""){
				$.post("deletexmlx.action",{instanceids:ids},function(data){ 
			    	if(data=='success'){
				   		window.location.reload();
				   	}else{
				   		alert("有"+data+"项删除失败!");
			    	}
		     	});
			}else{
				alert("未勾选任何项!");
			}
		}
	});
}
</script>

	<style>
body {
	font-size:12px;
	padding:0px;
	margin:0px;
	border:0px
}
.content{
	padding-left:10px;
	margin-right:auto;
	text-align:center; 
}
.flowbox {
	width:210px;
	height:110px;
	background:#fff; 
	float:left;
	display:inline-block;
	margin:3px;
	padding:15px;
	position:relative;
	border:1px solid #ccc;
	color:#333;
	
} 
.flowbox:hover {
	width:210px;
	height:110px;
	float:left;
	display:inline-block;
	margin:3px;
	padding:15px;
	position:relative;
	border:1px solid #FF9900;
	color:#FF9900;
	font-weight:bold;
	background:#fcfcfc; 
}
.flowbox_title_bar{
	cursor:pointer;
	background-image:url(iwork_img/icon/database.png);
	background-repeat:no-repeat;
	background-position:top left;
	border-bottom:1px solid #efefef;
	padding-left:15px;
}
.flowbox_title {
	float:left;
	text-align:left;
	font-size:14px;
	
}
.flowbox_date {
	float:right;
	width:70px;
}
.flowbox_del {
	float:right;
	width:20px;
	height:20px;
	cursor:pointer
}
.flowbox_body {
	clear:both;
	display:block;
	padding-top:5px;
}
.flowbox_body h1 {
	font-weight:bold;
	font-size:12px;
	text-align:left;
	padding:5px;
	cursor:pointer
}
.flowbox_body h2 {
	color:#999;
	font-size:12px;
	text-align:left;
	padding:5px;
	cursor:pointer;
	font-weight:100;
}
.flowbox_btn {
	position:absolute;
	right:10px;
	bottom:10px;
	color:#00F
}
.flowbox_btn a {
	text-decoration:none
}
.flowbox_btn a:linked {
color:#00F;
}
.flowbox_btn a:visited {
	color:#00F;
}
.flowbox_btn a:hover {
	color:#00F;
	text-decoration:underline
}
.flowbox_btn a:active {
	color:#00F;
}
.searchtitle{
	text-align:right;
	padding:5px;
}
.ui-jqgrid tr.jqgrow td {
	white-space: normal !important;
	height:28px;
	font-size:12px;
	vertical-align:text-middle;
	padding-top:2px;
}
</style>
	 
</head>
<script type="text/javascript">
	$(document).bind("contextmenu",function(){return false;});
</script>
<body class="easyui-layout layout" style="height: 100%; overflow: hidden; border: medium none; margin: 0px; padding: 0px;">
	<div class="panel layout-panel layout-panel-north" style="left: 0px; top: 0px; width: 100%;">
		<div border="false" region="north" class="layout-body panel-body panel-body-noheader panel-body-noborder" title="" style="width: 100%; height: 38px;">
			<div class="tools_nav">
				<a plain="true" class="easyui-linkbutton l-btn l-btn-plain" href="javascript:addItem();">
					<span class="l-btn-text icon-add" style="padding-left: 20px;">新增信息</span>
				</a>
				<a plain="true" class="easyui-linkbutton l-btn l-btn-plain" href="javascript:remove();">
					<span class="l-btn-text icon-remove" style="padding-left: 20px;">删除</span>
				</a>
				<a plain="true" class="easyui-linkbutton l-btn l-btn-plain" href="javascript:window.location.reload();">
					<span class="l-btn-text icon-reload" style="padding-left: 20px;">刷新</span>
				</a>
			</div> 
		</div>
	</div> 
	<div class="panel layout-panel layout-panel-center" style="left: 0px; top: 38px; width: 100%;">
	<div style="padding-left: 0px; padding-right: 0px; border: 0px none; background-position: center top; width: 100%; height: auto;" region="center" class="layout-body panel-body panel-body-noheader" title="">
	<form method="post" id="ifrmMain" name="ifrmMain">
	<div style="padding:5px">
	<div style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE;display:none;">
		<table width="100%" cellspacing="0" cellpadding="0" border="0"> 
		<tbody>
			<tr> 
				<td style="padding-top:10px;padding-bottom:10px;"> 
					<table width="100%" cellspacing="0" cellpadding="0" border="0"> 
						<tbody>
							<tr> 
							<td class="searchtitle">阶段名称 </td> 
							<td class="searchdata">
							<input type="text" form-type="al_textbox" value="" id="JDMC_152" name="JDMC_152" style="width:100px;" class="{maxlength:32,required:false,string:true}">
							</td> 
							<td class="searchtitle"></td> 
							<td class="searchdata">&nbsp;</td> 
							<td class="searchtitle"></td> 
							<td class="searchdata">&nbsp;</td> 
							</tr> 
						</tbody>
					</table> 
				</td> 
				<td valign="bottom" style="padding-bottom:5px;">
					<a href="javascript:doSearch();" class="easyui-linkbutton l-btn" id="btnEp"><span class="l-btn-text icon-search" style="padding-left: 20px;">查询</span></a>
				</td>
			</tr>
			<tr> 
			</tr>
		</tbody>
	</table> 
	</div>

	</div>
	<span style="disabled:none">
			<input type="hidden" id="formid" value="128" name="formid"> 
			<input type="hidden" id="demId" value="51" name="demId">
			<input type="hidden" id="init_params" value="" name="init_params">  
			<input type="hidden" value="11" id="idlist" name="idlist">
		
	</span>
	</form>
	<div style="padding:5px">
		<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_iform_grid" dir="ltr" style="width: 100%;">
			<div id="lui_iform_grid" class="ui-widget-overlay jqgrid-overlay"></div>
			<div class="ui-jqgrid-view" id="gview_iform_grid" style="width: 100%;">
			<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix" style="display: none;">
				<a href="javascript:void(0)" role="link" class="ui-jqgrid-titlebar-close HeaderButton" style="right: 0px;">
					<span class="ui-icon ui-icon-circle-triangle-n"></span>
				</a>
				<span class="ui-jqgrid-title"></span>
			</div>
			<div style="width: 100%;" class="ui-state-default ui-jqgrid-hdiv">
				<div class="ui-jqgrid-hbox">
					<table cellspacing="0" cellpadding="0" border="0" aria-labelledby="gbox_iform_grid" role="grid" style="width:100%" class="ui-jqgrid-htable">
						<thead>
							<tr role="rowheader" class="ui-jqgrid-labels">
								<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="iform_grid_cb" style="width: 5%;">
									<input type="checkbox" class="cbox" onclick="checkAll();" role="checkbox">
								</th>
								<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="iform_grid_ROWNUM" style="width: 5%;">
								序号
								</th>
								<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="iform_grid_JDMC" style="width: 10%;">
								阶段名称
								</th>
								<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="iform_grid__OPERATE" style="width: 10%;">
									操作
								</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
			<div style="position:relative;">
				<table cellspacing="0" cellpadding="0" border="0" id="iform_grid" tabindex="1" role="grid" aria-multiselectable="true" aria-labelledby="gbox_iform_grid" class="ui-jqgrid-btable" style="width: 100%;">
					<tbody>
						<tr style="height:auto" role="row" class="jqgfirstrow">
							<td style="height:0px;width:5%;"></td>
							<td style="height:0px;width:5%;"></td>
							<td style="height:0px;width:10%;"></td>
							<td style="height:0px;width:10%;"></td>
						</tr>
						<s:iterator value="sysdemMdmXmlxList" status="status">
						<tr class="ui-widget-content jqgrow ui-row-ltr" role="row">
							<td aria-describedby="iform_grid_cb" style="text-align:center;width:5%;">
								<input class="cbox" role="checkbox" type="checkbox" name="colname" value="<s:property value="INSTANCEID" />">
							</td>
							<td title="<s:property value="#status.count" />" style="text-align:center;width:5%;"><s:property value="#status.count" /></td>
							<td title="<s:property value="JDMC" />" style="text-align:center;width:10%;"><s:property value="LXMC" /></td>
							<td title="查看详情" style="text-align:center;width:10%;">
								<a onclick="openFormPage('<s:property value="INSTANCEID" />');" href="###"><img border="0" style="">查看详情</a>
							</td>
						</tr>
						</s:iterator>
					</tbody>
				</table>
			</div>
			</div>
			</div>
	</div>
	</div>
	</div> 
	<form action="sysDem_Mdm_xmjdzl.action" method=post name=frmMain id=frmMain>
		<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
		<s:hidden name="pageSize" id="pageSize"></s:hidden>
		<s:hidden name="xmlxformid" id="xmlxformid"></s:hidden>
		<s:hidden name="xmlxdemid" id="xmlxdemid"></s:hidden>
	</form>
</body>
</html>
