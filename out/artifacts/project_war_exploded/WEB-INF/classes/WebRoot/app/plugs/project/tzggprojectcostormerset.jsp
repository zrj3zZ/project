<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.iwork.app.conf.SystemConfig"%>
<%@ page language="java"
	import="com.ibpmsoft.project.zqb.util.ConfigUtil"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
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
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"></script>
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
		var tzbt=$("#tzbt", parent.document).val();
		var zchfsj=$("#zchfsj", parent.document).val();
		var tznr=$("#tznr", parent.document).val();
		var XGZL=$("#XGZL", parent.document).val();
		var hfr=$("#hfr", parent.document).val();
		var ggid=$("#ggid", parent.document).val();
		var wjfile=$("#wjfile", parent.document).val();
		var dctm=$("#dctm", parent.document).val();
        var tzlx = window.parent.document.getElementById("NOTICETYPE").value;
        var extend5 = $("input[name='EXTEND5']:checked", parent.document).val();
        var sftz = "否";
		var obj = document.getElementsByName("chk_list");
		var s='';
				//取到对象数组后，我们来循环检测它是不是被选中
				for(var i=0; i<obj.length; i++){ 
					if(obj[i].checked) 
					s+="\'"+obj[i].value+'\','; //如果选中，将value添加到变量s中 
				}
				s = s.substring(0, s.length-1);
		if(s!=null&&s!=''){
			var pageUrl="zqb_announcement_save_YXZ.action";
			$.post(pageUrl,{tzbt:tzbt,zchfsj:zchfsj,tznr:tznr,XGZL:XGZL,hfr:hfr,sftz:sftz,ggid:ggid,USERID:s,tzlx:tzlx,wjfile:wjfile,dctm:dctm,extend5:extend5},function(data){
				var dataJson = eval("(" + data + ")");
		        var flag = dataJson[0].flag;
		        var ggid = dataJson[0].ggid;
		        var hfr = dataJson[0].HFR;
		        $("#btnEpForSave", parent.document).attr("href","javascript:doSubmit();");
				if((flag+"")!='error'){
					$("#ggid", parent.document).val(ggid);
					setTimeout('api.zindex().focus()', 500);
					var url="announcement_notice_list.action?ggid="+ggid;
					$("#ggid", parent.document).val(ggid); 
					$("#iframe",artDialog.open.origin.document).attr("src",url);
					closeWin();
				}
	   		});
		}else{
			document.getElementById("setresult").innerHTML="请选择记录!";
		}
		//closeWin();
	}
	function doSearch(){
		var username = $("#USERNAME").val();
		var sszjj = $("#SSZJJ").val();
		var szbm = $("#SZBM").val();
		var szdq = $("#GPDQ").val();
		var gprqks = $("#GPRQKS").val();
		var gprqjs = $("#GPRQJS").val();
		var ssbm = $("#SSBM").val();
		var gpzt = $("#GPZT").val();
		var zczt = $("#ZCZT").val();
		var seachUrl = encodeURI("zqb_cxtzggproject_costormer_set.action?name="+ username + "&sszjj=" + sszjj+ "&szbm=" + szbm+ "&gpdq=" + szdq+ "&gprqks=" + gprqks+ "&gprqjs=" + gprqjs+ "&ssbm=" + ssbm + "&gpzt=" + gpzt + "&zczt=" + zczt);
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
											<td align="right" style="font-size: 12px;">姓名</td>
											<td><input type="text" form-type="al_textbox" value="<s:property value="name"/>" id="USERNAME" name="USERNAME" style="width:150px;font-size:12px;" class="{required:false}"></td>
											<td align="right" style="font-size: 12px;">所属证监局</td>
											<td><input type="text" form-type="al_textbox" value="<s:property value="sszjj"/>" id="SSZJJ" name="SSZJJ" style="width:150px;font-size:12px;" class="{required:false}"></td>
											<td align="right" style="font-size: 12px;">部门/公司</td>
											<td><input type="text" form-type="al_textbox" value="<s:property value="szbm"/>" id="SZBM" name="SZBM" style="width:100px;font-size:12px;" class="{required:false}"></td>
										</tr>
										<tr>
											<td align="right" style="font-size: 12px;">挂牌地区</td>
											<td><input type="text" form-type="al_textbox" value="<s:property value="gpdq"/>" id="GPDQ" name="GPDQ" style="width:150px;font-size:12px;" class="{required:false}"></td>
											<td align="right" style="font-size: 12px;">挂牌日期</td>
											<td colspan="1" style="font-size: 12px;">
												 <input type="text" form-type="al_textbox" value="<s:property value="gprqks"/>" id="GPRQKS" name="GPRQKS" style="width:100px;font-size:12px;" class="{required:false}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})">
												至<input type="text" form-type="al_textbox" value="<s:property value="gprqjs"/>" id="GPRQJS" name="GPRQJS" style="width:100px;font-size:12px;" class="{required:false}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})">
											</td>
											<td align="right" style="font-size: 12px;">所属部门</td>
											<td><input type="text" form-type="al_textbox" value="<s:property value="ssbm"/>" id="SSBM" name="SSBM" style="width:100px;font-size:12px;" class="{required:false}"></td>
										
										</tr>
										<tr>
											<td align="right" style="font-size: 12px;">挂牌状态</td>
											<td><input type="text" form-type="al_textbox" value="<s:property value="gpzt"/>" id="GPZT" name="GPZT" style="width:150px;font-size:12px;" class="{required:false}"></td>
											<td align="right" style="font-size: 12px;">转出状态</td>
											<td><input type="text" form-type="al_textbox" value="<s:property value="zczt"/>" id="ZCZT" name="ZCZT" style="width:150px;font-size:12px;" class="{required:false}"></td>
											
										
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
											<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="dictionary_list_grid_rn" style="width: 2%;">
												<div id="jqgh_dictionary_list_grid_rn"><input type="checkbox" name="chk_list" id="chkAll" onclick="setCheck();">
													<span style="display:none" class="s-ico">
														<span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
														<span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
													</span>
												</div>
											</th>
											<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="dictionary_list_grid_CUSTOMERNAME" style="width: 6%;">
												<span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
												<div id="jqgh_dictionary_list_grid_USERNAME" class="ui-jqgrid-sortable" style="font-size: 12px;"> 姓名
												<span style="display:none" class="s-ico">
													<span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
													<span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
												</span>
												</div>
											</th>
											<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="dictionary_list_grid_CUSTOMERNO" style="width: 22%;">
												<span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
												<div id="jqgh_dictionary_list_grid_SZBM" class="ui-jqgrid-sortable" style="font-size: 12px;"> 部门/公司
												<span style="display:none" class="s-ico">
													<span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
													<span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
												</span>
												</div>
											</th>
											<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="dictionary_list_grid_USERNAME" style="width: 8%;">
												<span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
												<div id="jqgh_dictionary_list_grid_SSZJJ" class="ui-jqgrid-sortable" style="font-size: 12px;"> 所属证监局
													<span style="display:none" class="s-ico">
														<span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
														<span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
													</span>
												</div>
											</th>
											<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="dictionary_list_grid_USERNAME" style="width: 8%;">
												<span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
												<div id="jqgh_dictionary_list_grid_SSBM" class="ui-jqgrid-sortable" style="font-size: 12px;"> 所属部门
													<span style="display:none" class="s-ico">
														<span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
														<span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
													</span>
												</div>
											</th>
											<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="dictionary_list_grid_USERNAME" style="width: 10%;">
												<span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
												<div id="jqgh_dictionary_list_grid_SS" class="ui-jqgrid-sortable" style="font-size: 12px;"> 挂牌地区
													<span style="display:none" class="s-ico">
														<span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
														<span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
													</span>
												</div>
											</th>
											<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="dictionary_list_grid_USERNAME" style="width: 10%;">
												<span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
												<div id="jqgh_dictionary_list_grid_gpsj" class="ui-jqgrid-sortable" style="font-size: 12px;"> 挂牌日期
													<span style="display:none" class="s-ico">
														<span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
														<span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
													</span>
												</div>
											</th>
											<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="dictionary_list_grid_TEL" style="width: 10%;">
												<span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
												<div id="jqgh_dictionary_list_grid_MOBILE" class="ui-jqgrid-sortable" style="font-size: 12px;">电话
													<span style="display:none" class="s-ico">
														<span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
														<span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
													</span>
												</div>
											</th>
											<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="dictionary_list_grid_CUSTOMERDESC" style="width: 10%;">
												<span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
												<div id="jqgh_dictionary_list_grid_EMAIL" class="ui-jqgrid-sortable" style="font-size: 12px;">邮箱
													<span style="display:none" class="s-ico">
														<span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
														<span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
													</span>
												</div>
											</th>
											<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="dictionary_list_grid_CUSTOMERDESC" style="width: 5%;">
												<span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
												<div id="jqgh_dictionary_list_grid_2" class="ui-jqgrid-sortable" style="font-size: 12px;">挂牌状态
													<span style="display:none" class="s-ico">
														<span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
														<span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
													</span>
												</div>
											</th>
											<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="dictionary_list_grid_CUSTOMERDESC" style="width: 5%;">
												<span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
												<div id="jqgh_dictionary_list_grid_1" class="ui-jqgrid-sortable" style="font-size: 12px;">转出状态
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
											<td style="height:0px;width:2%;" role="gridcell"></td>
											<td style="height:0px;width:6%;" role="gridcell"></td>
											<td style="height:0px;width:22%;" role="gridcell"></td>
											<td style="height:0px;width:8%;" role="gridcell"></td>
											<td style="height:0px;width:8%;" role="gridcell"></td>
											<td style="height:0px;width:10%;" role="gridcell"></td>
											<td style="height:0px;width:10%;" role="gridcell"></td>
											<td style="height:0px;width:13%;" role="gridcell"></td>
											<td style="height:0px;width:10%;" role="gridcell"></td>
											<td style="height:0px;width:5%;" role="gridcell"></td>
											<td style="height:0px;width:5%;" role="gridcell"></td>
										</tr>
										<s:iterator value="listxjxzq" status="ll">
										<tr class="ui-widget-content jqgrow ui-row-ltr" onmouseover="setBeckGroundYes(this);" onmouseout="setBeckGroundNo(this);">
											<td><input type="checkbox" value="<s:property value="USERID"/>" name="chk_list"></td>
											<td aria-describedby="dictionary_list_grid_USERNAME" title="" style="text-align:left;" role="gridcell"><s:property value="USERNAME"/></td>
											<td aria-describedby="dictionary_list_grid_SZBM" title="" style="text-align:left;" role="gridcell"><s:if test="SZBM.length()>35"><s:property value="SZBM.substring(0,33)"/>...</s:if><s:else><s:property value="SZBM"/></s:else></td>
											<td aria-describedby="dictionary_list_grid_SSZJJ" title="" style="text-align:left;" role="gridcell"><s:property value="SSZJJ"/></td>
											<td aria-describedby="dictionary_list_grid_SSBM" title="" style="text-align:left;" role="gridcell"><s:property value="SSBM"/></td>
											<td aria-describedby="dictionary_list_grid_SS" title="" style="text-align:left;" role="gridcell"><s:property value="GPDQ"/></td>
											<td aria-describedby="dictionary_list_grid_GPSJ" title="" style="text-align:left;" role="gridcell"><s:property value="GPSJ"/></td>
											<td aria-describedby="dictionary_list_grid_MOBILE" title="" style="text-align:left;" role="gridcell"><s:property value="MOBILE"/></td>
											<td aria-describedby="dictionary_list_grid_EMAIL" title="" style="text-align:left;" role="gridcell"><s:property value="EMAIL"/></td>
											<td aria-describedby="dictionary_list_grid_MOBILE" title="" style="text-align:left;" role="gridcell"><s:property value="ygp"/></td>
											<td aria-describedby="dictionary_list_grid_EMAIL" title="" style="text-align:left;" role="gridcell"><s:property value="cxddbg"/></td>
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