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
		var obj=document.getElementsByName('chk_list'); //选择所有name="'test'"的对象，返回数组
		var s=''; 
		//取到对象数组后，我们来循环检测它是不是被选中
		for(var i=0; i<obj.length; i++){ 
				if(obj[i].value!="")
				s+=obj[i].value+","+obj[i].checked+"|"; //如果选中，将value添加到变量s中 
			}
		s = s.substring(0, s.length-1);
		$.messager.defaults = { ok: "是", cancel: "否" };
		$.messager.confirm('操作提示','是否确定修改?',function(result){
		if(result){
			//修改未签到的数据
			var seachUrl = "updateSignin.action?"
			$.post(seachUrl,{cyrid:s},function(data){ 
				if(data=="success"){
				     alert("修改成功");
				     window.location.reload();
				     }else{
				     alert("当前公司并未回复或并未添加参与培训人员，无法修改签到状态！");
				     }
			       });
			}
		});
	}
	//查询
	function doSearch(){
		
		var ggid=$('#ggid', window.parent.document).val();
	    var username=$("#USERNAME").val();
	    var szbm=$("#SZBM").val();
	    var startdate = $("#STARTDATE").val();
		var enddate = $("#ENDDATE").val();
		var sfqd= $("select").find("option:selected").val();
		var seachUrl = encodeURI("Signin.action?ggid="+ggid+"&username="+ username + "&szbm=" + szbm+ "&startdate=" + startdate+"&enddate=" + enddate+"&sfqd=" + sfqd);
		window.location.href = seachUrl;
	}
	//导出
	function downloadThisNoticeFile(){
		var ggid=$('#ggid', window.parent.document).val();
		var tzbt=$('#tzbt', window.parent.document).val();
	    var username=$("#USERNAME").val();
	    var szbm=$("#SZBM").val();
	    var startdate = $("#STARTDATE").val();
		var enddate = $("#ENDDATE").val();
		var sfqd= $("select").find("option:selected").val();
		var seachUrl = "SigninupExp.action?ggid="+ggid+"&username="+ username + "&szbm=" + szbm+ "&startdate=" + startdate+"&enddate=" + enddate+"&sfqd=" + sfqd+"&tzbt="+tzbt;
		window.location.href = encodeURI(seachUrl);
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
				<a iconcls="icon-ok" plain="true" class="easyui-linkbutton l-btn l-btn-plain" href="javascript:doSubmit()">确认</a>
				<a iconcls="icon-cancel" plain="true" class="easyui-linkbutton l-btn l-btn-plain" href="javascript:cancel();">取消</a>
				<a iconcls="icon-reload" plain="true" class="easyui-linkbutton l-btn l-btn-plain" href="javascript:this.location.reload();">刷新</a>
				<a href="javascript:void(0);" class="easyui-linkbutton" plain="true" iconcls="icon-search" onclick="downloadThisNoticeFile();">导出数据</a>
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
											<td align="right" style="font-size: 12px;">参与人姓名</td>
											<td><input type="text" form-type="al_textbox" value="<s:property value="username"/>" id="USERNAME" name="USERNAME" style="width:150px;font-size:12px;" class="{required:false}"></td>
											<td align="right" style="font-size: 12px;">部门/公司</td>
											<td><input type="text" form-type="al_textbox" value="<s:property value="szbm"/>" id="SZBM" name="SZBM" style="width:150px;font-size:12px;" class="{required:false}"></td>
											<td align="right" style="font-size: 12px;">参与日期</td>
											<td><input type='text' onfocus="WdatePicker()"
											style="width:100px" name='STARTDATE'
											id='STARTDATE' value='<s:property value="startdate"/>'> 到 <input type='text'
											onfocus="WdatePicker()"
											onchange="checkRQ()" style="width:100px" name='ENDDATE'
											id='ENDDATE' value='<s:property value="enddate"/>'></td>
											<td align="right" style="font-size: 12px;">是否签到</td>
											<td class="searchdata">
											<s:if test='sfqd=="是"'>
												<select name='SFQD' id='SFQD' >
												<option value=''>-空-</option>
												<option value='是' selected>是</option>
												<option value='否'>否</option>
												</select>
											</s:if>
											<s:elseif test='sfqd=="否"'>
												<select name='SFQD' id='SFQD' >
												<option value=''>-空-</option>
												<option value='是' >是</option>
												<option value='否' selected>否</option>
												</select>
											</s:elseif>
											<s:else>
												<select name='SFQD' id='SFQD' >
												<option value=''>-空-</option>
												<option value='是' >是</option>
												<option value='否' >否</option>
											</s:else>
											</td>
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
											<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="dictionary_list_grid_CUSTOMERNO" style="width: 30%;">
												<span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
												<div id="jqgh_dictionary_list_grid_SZBM" class="ui-jqgrid-sortable" style="font-size: 12px;"> 部门/公司
												<span style="display:none" class="s-ico">
													<span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
													<span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
												</span>
												</div>
											</th>
											<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="dictionary_list_grid_CUSTOMERNAME" style="width: 12%;">
												<span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
												<div id="jqgh_dictionary_list_grid_USERNAME" class="ui-jqgrid-sortable" style="font-size: 12px;"> 通知反馈人
												<span style="display:none" class="s-ico">
													<span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
													<span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
												</span>
												</div>
											</th>
											
											<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="dictionary_list_grid_USERNAME" style="width: 12%;">
												<span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
												<div id="jqgh_dictionary_list_grid_SS" class="ui-jqgrid-sortable" style="font-size: 12px;"> 参与培训人员
													<span style="display:none" class="s-ico">
														<span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
														<span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
													</span>
												</div>
											</th>
											<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="dictionary_list_grid_USERNAME" style="width: 12%;">
												<span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
												<div id="jqgh_dictionary_list_grid_gpsj" class="ui-jqgrid-sortable" style="font-size: 12px;"> 参与日期
													<span style="display:none" class="s-ico">
														<span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
														<span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
													</span>
												</div>
											</th>
											<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="dictionary_list_grid_TEL" style="width: 12%;">
												<span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
												<div id="jqgh_dictionary_list_grid_MOBILE" class="ui-jqgrid-sortable" style="font-size: 12px;">电话
													<span style="display:none" class="s-ico">
														<span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
														<span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
													</span>
												</div>
											</th>
											<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="dictionary_list_grid_CUSTOMERDESC" style="width: 15%;">
												<span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
												<div id="jqgh_dictionary_list_grid_EMAIL" class="ui-jqgrid-sortable" style="font-size: 12px;">邮箱
													<span style="display:none" class="s-ico">
														<span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
														<span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
													</span>
												</div>
											</th>
											<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="dictionary_list_grid_USERNAME" style="width: 15%;">
												<span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
												<div id="jqgh_dictionary_list_grid_qd" class="ui-jqgrid-sortable" style="font-size: 12px;"> 签到
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
											<td style="height:0px;width:30%;" role="gridcell"></td>
											<td style="height:0px;width:12%;" role="gridcell"></td>
											<td style="height:0px;width:12%;" role="gridcell"></td>
											<td style="height:0px;width:12%;" role="gridcell"></td>
											<td style="height:0px;width:12%;" role="gridcell"></td>
											<td style="height:0px;width:15%;" role="gridcell"></td>
											<td style="height:0px;width:15%;" role="gridcell"></td>
										</tr>
										<s:iterator value="ydry" status="ll">
										<tr class="ui-widget-content jqgrow ui-row-ltr" onmouseover="setBeckGroundYes(this);" onmouseout="setBeckGroundNo(this);">
											<td aria-describedby="dictionary_list_grid_SZBM" title="" style="text-align:left;" role="gridcell"><s:if test="SZBM.length()>16"><s:property value="SZBM.substring(0,16)"/>...</s:if><s:else><s:property value="SZBM"/></s:else></td>
											<td aria-describedby="dictionary_list_grid_USERNAME" title="" style="text-align:left;" role="gridcell"><s:property value="USERNAME"/></td>
											<td aria-describedby="dictionary_list_grid_SS" title="" style="text-align:left;" role="gridcell"><s:property value="PXRY"/></td>
											<td aria-describedby="dictionary_list_grid_gpsj" title="" style="text-align:left;" role="gridcell"><s:property value="CYRQ"/></td>
											<td aria-describedby="dictionary_list_grid_MOBILE" title="" style="text-align:left;" role="gridcell"><s:property value="MOBILE"/></td>
											<td aria-describedby="dictionary_list_grid_EMAIL" title="" style="text-align:left;" role="gridcell"><s:property value="EMAIL"/></td>
											<td aria-describedby="dictionary_list_grid_qd" title="" style="text-align:left;" role="gridcell"><s:if test='SFQD=="是"'><input type="checkbox" name="chk_list" value="<s:property value="ID"/>" checked>是</s:if><s:else><input type="checkbox" name="chk_list" value="<s:property value="ID"/>">是</s:else> </td>
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