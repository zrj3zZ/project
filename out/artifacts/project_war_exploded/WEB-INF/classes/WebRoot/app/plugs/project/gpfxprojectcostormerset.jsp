<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.iwork.app.conf.SystemConfig"%>
<%@ page language="java"
	import="com.ibpmsoft.project.zqb.util.ConfigUtil"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta content="text/html; charset=GBK" http-equiv="Content-Type">
<title>IWORK综合应用管理系统</title>
<link href="iwork_css/jquerycss/process-icon.css" type="text/css" rel="stylesheet">
<link href="iwork_css/jquerycss/default/easyui.css" type="text/css" rel="stylesheet">
<link href="iwork_css/plugs/dictionary_runtime.css" type="text/css" rel="stylesheet">
<link href="iwork_css/jquerycss/validate/screen.css" media="screen" type="text/css" rel="stylesheet">
<link href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css" type="text/css" rel="stylesheet">
<link href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css" type="text/css" rel="stylesheet">

<script src="iwork_js/commons.js" type="text/javascript"></script>
<script src="iwork_js/jqueryjs/jquery-3.1.0.min.js" type="text/javascript"></script>
<script src="iwork_js/jqueryjs/jquery.easyui.min.js" type="text/javascript"></script>
<script src="iwork_js/jqueryjs/languages/grid.locale-cn.js" type="text/javascript"></script>
<script src="iwork_js/jqueryjs/jquery.jqGrid.src.js" type="text/javascript"> </script>
<script src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<link href="iwork_js/jqueryjs/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" type="text/css">
<script charset="utf-8" src="iwork_js/jqueryjs/jquery.validate.js" type="text/javascript"></script>
<script charset="utf-8" src="iwork_js/jqueryjs/jquery.metadata.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
	var api,W; 
	try{
		api=  art.dialog.open.api;
		W = api.opener;	
	}catch(e){}
	//关闭窗口
	function closeWin(){
	     api.close();
	}
	function doSubmit(){
		var checkvalue = $("input[name='chk_list']:checked").val();
		var chackval = checkvalue.split("//");
		if(checkvalue!=null&&checkvalue!=''){
			$("#PROJECTNAME",parent.document).val(chackval[0]);
			$("#CUSTOMERNAME",parent.document).val(chackval[0]);
			$("#CUSTOMERNO",parent.document).val(chackval[1]);
			$("#KHLXR",parent.document).val(chackval[2]);
			$("#KHLXDH",parent.document).val(chackval[3]);
			closeWin();
		}else{
			document.getElementById("setresult").innerHTML="请选择记录!";
		}
		closeWin();
	}
	function doSearch(){
		var customername = $("#CUSTOMERNAME").val();
		var customerno = $("#CUSTOMERNO").val();
		var seachUrl = encodeURI("zqb_gpfxproject_costormer_set.action?name="+ customername + "&no=" + customerno);
		window.location.href = seachUrl;
	}
	//关闭窗口
	function cancel(){
		closeWin();
	}
	function setBeckGroundYes(obj) {
		obj.style.backgroundColor='#a6c9e2';
	}
	function setBeckGroundNo(obj) {
		obj.style.backgroundColor='';
	}
</script>
<style>
#header {
	background: #6cf;
}

#title {
	height: 20px;
	background: #EFEFEF;
	border-bottom: 1px solid #990000;
	font: 12px;
	font-family: 宋体;
	padding-left: 5px;
	padding-top: 5px;
}

#baseframe {
	margin: 0px;
	background: #FFFFFF;
}

#baseinfo {
	background: #FFFFFF;
	padding: 5px;
	font: 12px;
	font-family: 宋体;
}

.toobar {
	border-bottom: 1px solid #990000;
	padding-bottom: 5px;
}
/*只读数据样式*/
.readonly_data {
	vertical-align: bottom;
	font-size: 12px;
	line-height: 20px;
	color: #888888;
	padding-right: 10px;
	border-bottom: 1px #999999 dotted;
	font-family: "宋体";
	line-height: 15px;
}

.table_form {
	font-family: "宋体";
	font-size: 12px;
}
/*数据字段标题样式*/
.td_title {
	color: #004080;
	line-height: 23px;
	font-size: 12px;
	text-align: right;
	letter-spacing: 0.1em;
	padding-right: 10px;
	padding-left: 10px;
	white-space: nowrap;
	border-bottom: 1px #999999 thick;
	vertical-align: middle;
	font-family: "宋体";
}

/*数据字段内容样式*/
.td_data {
	color: #0000FF;
	line-height: 23px;
	text-align: left;
	padding-left: 3px;
	font-size: 12px;
	font-family: "宋体";
	border-bottom: 1px #999999 dotted;
	vertical-align: middle;
	word-wrap: break-word;
	word-break: break-all;
	font-weight: 500;
	line-height: 15px;
	padding-top: 5px;
}

.dict_type {
	float: right;
}
</style>
</head>
<body class="easyui-layout layout" style="height: 100%; overflow-x: hidden; border: medium none; margin: 0px; padding: 0px;">
	<div class="panel layout-panel layout-panel-north" style="left: 0px; top: 0px; width: 100%;">
		<div style="padding: 5px; border: 0px none; overflow-x: hidden; width: 98%; height: 26px;" region="north" class="layout-body panel-body panel-body-noheader" title="">
			<div class="tools_nav">
				<a iconcls="icon-ok" plain="true" class="easyui-linkbutton l-btn l-btn-plain" href="javascript:doSubmit()">确认选择</a>
				<a iconcls="icon-cancel" plain="true" class="easyui-linkbutton l-btn l-btn-plain" href="javascript:cancel();">取消</a>
				<a iconcls="icon-reload" plain="true" class="easyui-linkbutton l-btn l-btn-plain" href="javascript:this.location.reload();">刷新</a>
			</div>
		</div>
	</div>
	<div class="panel layout-panel layout-panel-center" style="left: 0px; top: 36px; width: 100%;">
		<div style="padding: 5px; border: 0px none; overflow-x: hidden; width: 98%; height: 474px;" region="center" class="layout-body panel-body panel-body-noheader" title="">
			<table width="100%" cellspacing="0" cellpadding="0" style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE;">
				<tbody>
					<tr>
						<td width="100%">
							<form method="post" onsubmit="return true;" name="editForm" id="editForm">
								<table width="100%" cellspacing="0" cellpadding="2">
									<tbody>
										<tr>
											<td align="right" style="font-size: 12px;">客户名称</td>
											<td><input type="text" form-type="al_textbox" value="${name}" id="CUSTOMERNAME" name="CUSTOMERNAME" style="width:150px;font-size:12px;" class="{required:false}"></td>
											<td><span id="setresult" style="font-size:12px;color:red;"></span><td>
											<td align="right" style="font-size: 12px;">客户编号</td>
											<td><input type="text" form-type="al_textbox" value="${no}" id="CUSTOMERNO" name="CUSTOMERNO" style="width:100px;font-size:12px;" class="{required:false}"></td>
										</tr>
									</tbody>
								</table>
							</form>
						</td>
						<td width="5%" style="vertical-align:bottom;padding:3px;">
							<a iconcls="icon-search" plain="false" class="easyui-linkbutton l-btn" href="javascript:doSearch();"></a>
						</td>
					</tr>
				</tbody>
			</table>

			<div id="baseframe">
				<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_dictionary_list_grid" dir="ltr" style="width: 100%;">
					<div id="lui_dictionary_list_grid" class="ui-widget-overlay jqgrid-overlay" style="display: none;"></div>
					<div id="load_dictionary_list_grid" class="loading ui-state-default ui-state-active" style="display: none;">读取中...</div>
					<div class="ui-jqgrid-view" id="gview_dictionary_list_grid" style="width: 100%;">
						<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix" style="display: none;">
							<a href="javascript:void(0)" role="link" class="ui-jqgrid-titlebar-close HeaderButton" style="right: 0px;">
								<span class="ui-icon ui-icon-circle-triangle-n"></span>
							</a>
							<span class="ui-jqgrid-title"></span>
						</div>
						<div style="width: 100%;" class="ui-state-default ui-jqgrid-hdiv">
							<div class="ui-jqgrid-hbox">
								<table cellspacing="0" cellpadding="0" border="0" aria-labelledby="gbox_dictionary_list_grid" role="grid" style="width:100%" class="ui-jqgrid-htable">
									<thead>
										<tr role="rowheader" class="ui-jqgrid-labels">
											<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="dictionary_list_grid_rn" style="width: 3%;">
												<div id="jqgh_dictionary_list_grid_rn">
													<span style="display:none" class="s-ico">
														<span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
														<span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
													</span>
												</div>
											</th>
											<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="dictionary_list_grid_CUSTOMERNAME" style="width: 23%;">
												<span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
												<div id="jqgh_dictionary_list_grid_CUSTOMERNAME" class="ui-jqgrid-sortable" style="font-size: 12px;"> 客户名称
												<span style="display:none" class="s-ico">
													<span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
													<span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
												</span>
												</div>
											</th>
											<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="dictionary_list_grid_CUSTOMERNO" style="width: 14%;">
												<span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
												<div id="jqgh_dictionary_list_grid_CUSTOMERNO" class="ui-jqgrid-sortable" style="font-size: 12px;"> 客户编号
												<span style="display:none" class="s-ico">
													<span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
													<span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
												</span>
												</div>
											</th>
											<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="dictionary_list_grid_USERNAME" style="width: 13%;">
												<span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
												<div id="jqgh_dictionary_list_grid_USERNAME" class="ui-jqgrid-sortable" style="font-size: 12px;"> 客户联系人
													<span style="display:none" class="s-ico">
														<span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
														<span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
													</span>
												</div>
											</th>
											<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="dictionary_list_grid_TEL" style="width: 13%;">
												<span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
												<div id="jqgh_dictionary_list_grid_TEL" class="ui-jqgrid-sortable" style="font-size: 12px;">客户联系电话
													<span style="display:none" class="s-ico">
														<span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
														<span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
													</span>
												</div>
											</th>
											<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="dictionary_list_grid_CUSTOMERDESC" style="width: 34%;">
												<span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
												<div id="jqgh_dictionary_list_grid_CUSTOMERDESC" class="ui-jqgrid-sortable" style="font-size: 12px;">公司概况
													<span style="display:none" class="s-ico">
														<span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
														<span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
													</span>
												</div>
											</th>
										</tr>
									</thead>
								</table>
							</div>
						</div>
						<div class="ui-jqgrid-bdiv" style="width: 100%;">
							<div style="position:relative;">
								<div></div>
								<table cellspacing="0" cellpadding="0" border="0" id="dictionary_list_grid" tabindex="1" role="grid" aria-multiselectable="false" aria-labelledby="gbox_dictionary_list_grid" class="ui-jqgrid-btable" style="width: 100%;font-size: 12px;">
									<tbody>
										<tr style="height:auto" role="row" class="jqgfirstrow">
											<td style="height:0px;width:3%;" role="gridcell"></td>
											<td style="height:0px;width:23%;" role="gridcell"></td>
											<td style="height:0px;width:14%;" role="gridcell"></td>
											<td style="height:0px;width:13%;" role="gridcell"></td>
											<td style="height:0px;width:13%;" role="gridcell"></td>
											<td style="height:0px;width:34%;" role="gridcell"></td>
										</tr>
										<s:iterator value="listxjxzq" status="ll">
										<tr class="ui-widget-content jqgrow ui-row-ltr" onmouseover="setBeckGroundYes(this);" onmouseout="setBeckGroundNo(this);">
											<td><input type="radio" value="<s:property value="CUSTOMERNAME"/>//<s:property value="CUSTOMERNO"/>//<s:property value="USERNAME"/>//<s:property value="TEL"/>" name="chk_list"></td>
											<td aria-describedby="dictionary_list_grid_CUSTOMERNAME" title="<s:property value="CUSTOMERNAME"/>" style="text-align:left;" role="gridcell"><s:if test="CUSTOMERNAME.length()>35"><s:property value="CUSTOMERNAME.substring(0,33)"/>...</s:if><s:else><s:property value="CUSTOMERNAME"/></s:else></td>
											<td aria-describedby="dictionary_list_grid_CUSTOMERNO" title="" style="text-align:left;" role="gridcell"><s:property value="CUSTOMERNO"/></td>
											<td aria-describedby="dictionary_list_grid_USERNAME" title="" style="text-align:left;" role="gridcell"><s:property value="USERNAME"/></td>
											<td aria-describedby="dictionary_list_grid_TEL" title="" style="text-align:left;" role="gridcell"><s:property value="TEL"/></td>
											<td aria-describedby="dictionary_list_grid_CUSTOMERDESC" title="<s:property value="CUSTOMERDESC"/>" style="text-align:left;" role="gridcell"><s:if test="CUSTOMERDESC.length()>50"><s:property value="CUSTOMERDESC.substring(0,48)"/>...</s:if><s:else><s:property value="CUSTOMERDESC"/></s:else></td>
										</tr>
										</s:iterator>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					<div id="rs_mdictionary_list_grid" class="ui-jqgrid-resize-mark">&nbsp;</div>
					<div id="dictionary_list_prowed" style="width: 825px;" class="ui-state-default ui-jqgrid-pager ui-corner-bottom" dir="ltr">
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="layout-split-proxy-h"></div>
	<div class="layout-split-proxy-v"></div>
	<div class="ui-widget ui-widget-content ui-corner-all ui-jqdialog" id="alertmod" dir="ltr" style="width: 200px; height: auto; z-index: 950; overflow: hidden; top: 50px; left: 50px;" tabindex="-1" role="dialog" aria-labelledby="alerthd" aria-hidden="true">
		<div class="ui-jqdialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix" id="alerthd" style="cursor: move;">
			<span class="ui-jqdialog-title" style="float: left;">注意</span>
			<a class="ui-jqdialog-titlebar-close ui-corner-all" href="javascript:void(0)" style="right: 0.3em;">
				<span class="ui-icon ui-icon-closethick"></span>
			</a>
		</div>
		<div class="ui-jqdialog-content ui-widget-content" id="alertcnt">
			<div>请选择记录</div>
			<span tabindex="0"><span id="jqg_alrt" tabindex="-1"></span></span>
		</div>
		<div
			class="jqResize ui-resizable-handle ui-resizable-se ui-icon ui-icon-gripsmall-diagonal-se ui-icon-grip-diagonal-se"></div>
	</div>
</body>
</html>