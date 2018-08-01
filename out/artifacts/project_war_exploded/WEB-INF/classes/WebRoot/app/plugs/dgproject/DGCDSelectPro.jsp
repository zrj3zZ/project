<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/public.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
	<script type="text/javascript" src="iwork_js/commons.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
	<script type="text/javascript" src="iwork_js/engine/ifromworkbox.js"></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"></script>
	<script type="text/javascript" src="iwork_js/bindclick.js"></script>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>

<script>
        function submitMsg(pageNumber, pageSize) {
            debugger;
            $("#pageNumber").val(pageNumber);
            $("#pageSize").val(pageSize);
            $("#frmMain2").submit();
            return;
        }
        function pp(){
            $('#pp').pagination({
                total:<s:property value="totalNum"/>,
                pageNumber:<s:property value="pageNumber"/>,
                pageSize:<s:property value="pageSize"/>,
                onSelectPage: function (pageNumber, pageSize) {
                    submitMsg(pageNumber, pageSize);
                }
            });
		}

</script>
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
        var ss = $('input[name="chk_list"]:checked ').val();
        var slice = ss.split(",");
        window.parent.$("#PROJECTNAME").val(slice[0]);
        window.parent.$("#PROJECTNO").val(slice[1]);
        cancel();
	}
	function doSearch(){
		var ProStatus = $("#ProStatus").val();
		var ProID = $("#ProID").val();
        var pageSize = $("#pageSize").val();
		var seachUrl = encodeURI("sx_dgcdInsert_selectPro.action?ProStatus="+ ProStatus + "&ProID=" + ProID+"&pageNumber=1&pageSize=20");
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
	function setCheck(){
		$("[name=chk_list]:checkbox").attr("checked", $("#chkAll").is(':checked'));
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
<body class="easyui-layout layout" style="height: 100%; overflow-x: hidden; border: medium none; margin: 0px; padding: 0px;"  onload="pp()" >
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
							<form method="post" onsubmit="return true;" name="editForm" id="editForm" style="padding-top:2px;margin-bottom:-2px">
								<table width="100%" cellspacing="0" cellpadding="2">
									<tbody>
										<tr>
											<td align="right" style="font-size: 12px;">项目名称</td>
											<td><input type="text" form-type="al_textbox" value="<s:property value="ProStatus"/>" id="ProStatus" name="ProStatus" style="width:150px;font-size:12px;" class="{required:false}"></td>
											<input type="hidden" value="<s:property value="ProID"/>" id="ProID" name="ProID" style="width:150px;font-size:12px;"></td>
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
											<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" style="width: 232px;">
												<span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
												<div  class="ui-jqgrid-sortable" style="font-size: 12px;">选择
													<span style="display:none" class="s-ico">
														<span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
														<span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
													</span>
												</div>
											</th>
											<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader"  style="width: 695px;">
												<span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
												<div  class="ui-jqgrid-sortable" style="font-size: 12px">项目名称
													<span style="display:none" class="s-ico">
														<span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
														<span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
													</span>
												</div>
											</th>
											<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader"  style="width: 231px;">
												<span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
												<div id="jqgh_dictionary_list_grid_1" class="ui-jqgrid-sortable" style="font-size: 12px">项目编号
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
											<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="dictionary_list_grid_rn" style="width: 232px;">
												<div id="jqgh_dictionary_list_grid_rn">
													<span style="display:none" class="s-ico">
														<span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
														<span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
													</span>
												</div>
											</th>
											<td style="height:0px;width:695px;" role="gridcell"></td>
											<td style="height:0px;width:231px;" role="gridcell"></td>
										</tr>
										<s:iterator value="list" status="ll">
										<tr class="ui-widget-content jqgrow ui-row-ltr" onmouseover="setBeckGroundYes(this);" onmouseout="setBeckGroundNo(this);">
											<td><input type="radio" value="<s:property value="PROJECTNAME"/>,<s:property value="PROJECTNO"/>" name="chk_list"></td>
											<td aria-describedby="dictionary_list_grid_MOBILE" title="" style="text-align:left;" role="gridcell"><s:property value="PROJECTNAME"/></td>
											<td aria-describedby="dictionary_list_grid_EMAIL" title="" style="text-align:left;" role="gridcell"><s:property value="PROJECTNO"/></td>
										</tr>
										</s:iterator>
									</tbody>
								</table>
								<form action="sx_dgcdInsert_selectPro.action" method="post" name="frmMain2"
									  id="frmMain2">
									<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
									<s:hidden name="pageSize" id="pageSize"></s:hidden>
									<s:hidden name="ProStatus"></s:hidden>
									<s:hidden name="ProID"></s:hidden>
								</form>
								<div region="south"
									 style="vertical-align:bottom;height:60px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;"
									 border="false">
									<div style="padding:5px">
										<div id="pp"
											 style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
									</div>
								</div>
							</div>

						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>