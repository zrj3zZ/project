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
<link href="iwork_css/public.css" type="text/css" rel="stylesheet">
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
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
function openFormPage(instanceid){
	var demid = $("#xpzcwtdemid").val();
	var formid = $("#xpzcwtformid").val();
	var pageUrl = "openFormPage.action?formid="+formid+"&instanceId="+instanceid+"&demId="+demid;
	art.dialog.open(pageUrl,{
		title:'信披自查问题表单',
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
function changeColTwo(instanceid){
	
	$("tr[id='"+instanceid+"'] td").css('font-weight','bolder');
	$("tr[id='"+instanceid+"'] td").css('font-size','18px');
	$("tr[id='"+instanceid+"'] td").css('color','red');
}
function changeCol(instanceid){
		
	$("tr[id='"+instanceid+"'] td").css('font-weight','normal');
	$("tr[id='"+instanceid+"'] td").css('font-size','12px');
	$("tr[id='"+instanceid+"'] td").css('color','black');
	
}
function addItem(){
	$.post("xpwttjyz.action",function(data){
		if(data=='true'){
			var demid = $("#xpzcwtdemid").val();
			var formid = $("#xpzcwtformid").val();
			var pageUrl = "createFormInstance.action?formid="+formid+"&demId="+demid;
			art.dialog.open(pageUrl,{ 
				title:'信披自查问题表单',
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
		}else{
			alert('最多可以添加50个问题!');
		}
	});
	
}

function checkAll(){
	if($("input[name='colname']").attr("checked")){
		$("input[name='colname']").attr("checked",true);
	}else{
		$("input[name='colname']").attr("checked",false);
	}
}

//执行删除操作
function remove(){
	$.messager.confirm('确认','确认删除?',function(result){ 
		if(result){
			var list = $('[name=chk_list]').length;
			//alert(list);
			var a=0;
			var ids="";
			$("input[name='colname']:checkbox").each(function(){
			    if (true == $(this).attr("checked")) {
			    	ids += $(this).val()+',';
			    }
			});
			if(ids!=""){
				$.post("deletexpzcwt.action",{instanceids:ids},function(data){ 
			    	if(data=='success'){
			    		window.location.href = "sysDem_Mdm_xpzcwt.action";
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

function down() {
	var down="";
	$("input[name='colname']:checkbox").each(function(){ 
        if($(this).attr("checked")){
        	down += ($(this).attr("alt")+","+$(this).parent().parent().next().children().children().attr("alt"));
        }
    });
	var downs = down.split(",");
	if(downs.length>2){
		lhgdialog.tips("选择行过多",1);
	}else if(downs.length==1){
		lhgdialog.tips("请选择要移动的行记录",1);
	}else{
		window.location.href = "downxpzcwt.action?downids="+down;
	}
}
function up() {
	var up="";
	$("input[name='colname']:checkbox").each(function(){ 
        if($(this).attr("checked")){
        	up += ($(this).attr("alt")+","+$(this).parent().parent().prev().children().children().attr("alt"));
        }
    });
	var ups = up.split(",");
	if(ups.length>2){
		lhgdialog.tips("选择行过多",1);
	}else if(ups.length==1){
		lhgdialog.tips("请选择要移动的行记录",1);
	}else{
		window.location.href = "upxpzcwt.action?upids="+up;
	}
}
function topup() {
	var uptop="";
	$("input[name='colname']:checkbox").each(function(){ 
        if($(this).attr("checked")){
        	uptop += $(this).attr("alt")+",";
        }
    });
	var uptops = uptop.split(",");
	if(uptops.length>2){
		lhgdialog.tips("选择行过多",1);
	}else if(uptops.length==1){
		lhgdialog.tips("请选择要移动的行记录",1);
	}else{
		window.location.href = "uptopxpzcwt.action?uptopid="+uptop;
	}
}
function bottomdown() {
	var downbottom="";
	$("input[name='colname']:checkbox").each(function(){ 
        if($(this).attr("checked")){
        	downbottom += $(this).attr("alt")+",";
        }
    });
	var dowbottoms = downbottom.split(",");
	if(dowbottoms.length>2){
		lhgdialog.tips("选择行过多",1);
	}else if(dowbottoms.length==1){
		lhgdialog.tips("请选择要移动的行记录",1);
	}else{
		window.location.href = "downbottomxpzcwt.action?downbottomid="+downbottom;
	}
}
function fresh() {
	window.location.href = "sysDem_Mdm_xpzcwt.action";
}
window.onload=function(){ 
	var checkid=$("#checkid").val(); 
	$("input[name='colname']:checkbox").each(function(){ 
        if($(this).attr("alt")==checkid){
        	$(this).attr("checked","checked");
        }
    });
} 
function uploadXxzcMb(){
	 var seachUrl ="xxzcMbupload.action";
	window.location.href = seachUrl;
}
function xxzcDr(){
	
	var pageUrl = "xxzcDr.action";
	art.dialog.open(pageUrl,{
		id:'ExcelImpDialog',
		title:"数据导入",
		lock:true,
		background: '#999', // 背景色
	    opacity: 0.87,	// 透明度
	    width:500,
	    height:300
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
.icon-up{
	background:url('../../iwork_img/icon/arrow_up_blue.gif') no-repeat; 
}
.icon-down{
	background:url('../../iwork_img/icon/arrow_down_blue.gif') no-repeat; 
}
.icon-top{
	background:url('../../iwork_img/icon/arrow_first.gif') no-repeat; 
}
.icon-bottom{
	background:url('../../iwork_img/icon/arrow_last.gif') no-repeat; 
}
</style>
</head>
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
			<a plain="true" class="easyui-linkbutton l-btn l-btn-plain"  href="javascript:uploadXxzcMb();">
					<span class="l-btn-text icon-add" style="padding-left: 20px;">模板下载</span>
				</a>
		<a plain="true" class="easyui-linkbutton l-btn l-btn-plain"  href="javascript:xxzcDr();">
					<span class="l-btn-text icon-add" style="padding-left: 20px;">导入</span>
				</a>
				<a plain="true" class="easyui-linkbutton l-btn l-btn-plain" href="javascript:fresh();">
					<span class="l-btn-text icon-reload" style="padding-left: 20px;">刷新</span>
				</a>
				<a plain="true" class="easyui-linkbutton l-btn l-btn-plain" href="javascript:bottomdown();">
					<span class="l-btn-text icon-bottom" style="padding-left: 20px;">置底</span>
				</a>
				<a plain="true" class="easyui-linkbutton l-btn l-btn-plain" href="javascript:down();">
					<span class="l-btn-text icon-down" style="padding-left: 20px;">下移</span>
				</a>
				<a plain="true" class="easyui-linkbutton l-btn l-btn-plain" href="javascript:up();">
					<span class="l-btn-text icon-up" style="padding-left: 20px;">上移</span>
				</a>
				<a plain="true" class="easyui-linkbutton l-btn l-btn-plain" href="javascript:topup();">
					<span class="l-btn-text icon-top" style="padding-left: 20px;">置顶</span>
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
	</form>
	<div style="padding:5px;">
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
									<input type="checkbox" class="cbox" onclick="checkAll();" role="checkbox" name="colname">
								</th>
								<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="iform_grid_ROWNUM" style="width: 5%;">
									序号
								</th>
								<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="iform_grid_JDMC" style="width: 45%;">
									信披问题
								</th>
								<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="iform_grid_JDMC" style="width: 10%;">
									问题类型
								</th>
								<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="iform_grid_JDMC" style="width: 10%;">
									默认答案
								</th>
								<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="iform_grid_CONTENT" style="width: 10%;">
									有效状态
								</th>
								<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" id="iform_grid__OPERATE" style="width: 10%;">
									操作
								</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
			<div style="position:relative;overflow:auto;height:500px">
				<table cellspacing="0" cellpadding="0" border="0" id="iform_grid" tabindex="1" role="grid" aria-multiselectable="true" aria-labelledby="gbox_iform_grid" class="ui-jqgrid-btable" style="width: 100%;">
					<tbody>
						<tr style="height:auto" class="jqgfirstrow">
							<td style="height:0px;width:5%;"></td>
							<td style="height:0px;width:5%;"></td>
							<td style="height:0px;width:45%;"></td>
							<td style="height:0px;width:10%;"></td>
							<td style="height:0px;width:10%;"></td>
							<td style="height:0px;width:10%;"></td>
							<td style="height:0px;width:10%;"></td>
						</tr>
						<s:iterator value="sysdemMdmXpzcwtList" status="status">
						<tr class="ui-widget-content jqgrow ui-row-ltr" role="row" id="<s:property value="INSTANCEID" />">
							<td aria-describedby="iform_grid_cb" style="text-align:center;width: 5%;">
								<input name="colname" alt="<s:property value="ID" />" value="<s:property value="INSTANCEID" />" title="<s:property value="ID" />" class="cbox" role="checkbox" type="checkbox">
							</td>
							<td title="<s:property value="#status.count" />" style="text-align:center;width:5%"><s:property value="#status.count" /></td>
							<td title="<s:property value="QUESTION" />" style="text-align:center;width:45%;"><s:property value="QUESTION" /></td>
							<td title="<s:property value="TYPE" />" style="text-align:center;width:10%;"><s:property value="TYPE" /></td>
							<td title="<s:property value="DEFULTANSWER" />" style="text-align:center;width:10%;"><s:property value="DEFULTANSWER" /></td>
							<td title="<s:property value="STATUS" />" style="text-align:center;width:10%;"><s:property value="STATUS" /></td>
							<td title="查看详情" style="text-align:center;" role="gridcell">
							<a onclick="openFormPage('<s:property value="INSTANCEID" />');" onmouseout="changeCol('<s:property value="INSTANCEID" />')" onmouseover="changeColTwo('<s:property value="INSTANCEID" />')"   href="#">查看详情</a>
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
	<form action="sysDem_Mdm_xpzcwt.action" method=post name=frmMain id=frmMain>
		<s:hidden name="xpzcwtformid" id="xpzcwtformid"></s:hidden>
		<s:hidden name="xpzcwtdemid" id="xpzcwtdemid"></s:hidden>
		<s:hidden name="checkid" id="checkid"></s:hidden>
	</form>
</body>
</html>
