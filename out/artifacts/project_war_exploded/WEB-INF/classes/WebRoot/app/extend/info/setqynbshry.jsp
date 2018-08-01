<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>公告统计</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css" />
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/engine/ifromworkbox.js"></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>

<script type="text/javascript">
	function updateFP(instanceid) {
		var demId = $("#nbshrDemId").val();
		var formid = $("#nbshrFormid").val();
		var pageUrl = "openFormPage.action?formid="+formid+"&demId="+demId+"&instanceId="+instanceid;
		art.dialog.open(pageUrl,{
			title : '企业内部人员审核表单',
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			rang : true,
			width : 1100,
			cache : false,
			lock : true,
			height : 580,
			iconTitle : false,
			extendDrag : true,
			autoSize : false,
			close : function() {
				window.location.reload();
			}
		});
	}
</script>
<style type="text/css">
.searchtitle {
	text-align: right;
	padding: 5px;
}

.ui-jqgrid tr.jqgrow td {
	white-space: normal !important;
	height: 28px;
	font-size: 12px;
	vertical-align: text-middle;
	padding-top: 2px;
}

.cell td{
			margin:0;
			padding:3px 4px;
			height:25px;
			font-size:12px;
			white-space:nowrap;
			word-wrap:normal;
			overflow:hidden;
			text-align:left;
			border-bottom:1px dotted #eee;
			border-top:1px dotted #fff;
			border-right:1px dotted #eee;
		}
		.cell:hover{
			background-color:#F0F0F0;
		}
 .header td{
			height:35px;
			font-size:12px;
			padding:3px;
			white-space:nowrap;
			padding-left:5px;
			background:url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;
			border-top:1px dotted #ccc;
			border-right:1px solid #eee;
		} 
.nbshrspan{
			margin-left:10px;
			font-size:20px;
			font-style:oblique;
}
.nbshrbutton{
			margin-left:45px;
			margin-top:10px;
}
.nbshrinput{
			margin-top:10px;
			text-align:center;
}
</style>
</head>
<body class="easyui-layout">
	<div region="north" border="false">
		<div class="tools_nav"></div>
	</div>
	<div region="center" style="padding-left:0px;padding-right:0px;border:0px;background-position:top">
		<!-- 内部审批人可分配人员 -->
		<div style="padding:5px;width:auto;">
			<table id='iform_grid' width="100%" style="border:1px solid #efefef">
				<tr class="header">
					<td style="width:15%;">公司名称</td>
					<td style="width:10%;">企业内部人员审核</td>
					<td style="width:10%;">持续督导专员</td>
					<td style="width:10%;">投行审核人员</td>
					<td style="width:10%;">专职持续督导</td>
					<td style="width:10%;">质控部负责人</td>
					<td style="width:10%;">场外市场部负责人1</td>
					<td style="width:10%;">场外市场部负责人2</td>
					<td style="width:10%;">公告发布人</td>
					<td style="width:20%;">操作</td>
				</tr>
				<s:iterator value="nbshrlist" status="status">
					<tr class="cell">
						<td><s:property value="KHMC" /></td>
						<td><s:property value="QYNBRYSH" /></td>
						<td><s:property value="KHFZR" /></td>
						<td><s:property value="ZZCXDD" /></td>
						<td><s:property value="FHSPR" /></td>
						<td><s:property value="ZZSPR" /></td> 
						<td><s:property value="CWSCBFZR2" /></td>
						<td><s:property value="CWSCBFZR3" /></td>
						<td><s:property value="GGFBR" /></td>
						<td>
			<a href="javascript:updateFP('<s:property value="INSTANCEID"/>')" style="color:blue;"><u>设置公告内核人员</u></a></td>
					</tr>
				</s:iterator>
			</table>
		</div>
	</div>
	<div region="south" style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;" border="false">
	<s:hidden name="nbshrFormid" id="nbshrFormid"></s:hidden>
    <s:hidden name="nbshrDemId" id="nbshrDemId"></s:hidden>
	<script type="text/javascript">
	$(function(){
		$("#").val($("#").val());
		$("#").val($("#").val());
	});
	</script>
</body>
</html>