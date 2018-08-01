<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>项目日志</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_css/public.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css" />
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
<script type="text/javascript">
	var mainFormValidator;
	$().ready(function() {
		mainFormValidator = $("#ifrmMain").validate({});
		mainFormValidator.resetForm();
	});
	$(function(){
		$(document).bind('keyup', function(event) {
			if (event.keyCode == "13") {
				//回车执行查询
				$("#ifrmMain").submit();
			}
		});
		//查询
	    $("#search").click(function(){
	    	$("#ifrmMain").submit();
	    });
    });
	
	function search(){
		var valid = mainFormValidator.form(); //执行校验操作
		if (!valid) {
			return false;
		}
		var projectname = $("#PROJECTNAME").val();
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var type = ("#type  option:selected").text();
		var seachUrl = encodeURI("zqb_pjLogList.action?projectname=" + projectname +"&startDate="+startDate+"&endDate="+endDate+"&type="+type);
		window.location.href = seachUrl;
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

.header td {
	height: 35px;
	font-size: 12px;
	padding: 3px;
	white-space: nowrap;
	padding-left: 5px;
	background: url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left
		bottom;
	border-top: 1px dotted #ccc;
	border-right: 1px solid #eee;
}

.cell td {
	margin: 0;
	padding: 3px 4px;
	height: 25px;
	font-size: 12px;
	white-space: nowrap;
	word-wrap: normal;
	overflow: hidden;
	text-align: left;
	border-bottom: 1px dotted #eee;
	border-top: 1px dotted #fff;
	border-right: 1px dotted #eee;
}

.cell:hover {
	background-color: #F0F0F0;
}
</style>
</head>
<body class="easyui-layout">
	<div region="center"
		style="padding-left:0px;padding-right:0px;border:0px;background-position:top">
		<form name='ifrmMain' id='ifrmMain' method="post" action="zqb_pjLogList.action">
			<div style="padding:5px;text-align:center;">
				<div style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE; width:99%">
					<table width='100%' border='0' cellpadding='0' cellspacing='0'>
					<tr>
						<td style='padding-top:10px;padding-bottom:10px;'>
							<table width='100%' border='0'>
								<tr>
									<td>
										<span style="padding-left:5px;">项目全称：</span>
										<input type='text' class='{maxlength:128,required:false,string:true}' style="width:300px" name='projectname' id='projectname' value='${projectname}'>
										<span style="padding-left:5px;">日期：</span>
										<input type='text' onfocus="WdatePicker();" class='{maxlength:64,required:false}' style="width:80px;" name='startDate' id='startDate' value='${startDate}'>
										到
										<input type='text' onfocus="WdatePicker();" class='{maxlength:64,required:false}' style="width:80px;" name='endDate' id='endDate' value='${endDate}'>
										<span style="padding-left:5px;">变更信息：</span>
										<select name='type' id='type' value="${type}">
											<option value=''>-空-</option>
											<option value='1' <s:if test="%{type==1}">selected</s:if>>项目管理信息</option>
											<option value='2' <s:if test="%{type==2}">selected</s:if>>项目阶段信息</option>
											<option value='3' <s:if test="%{type==3}">selected</s:if>>分配部门</option>
											<option value='4' <s:if test="%{type==4}">selected</s:if>>承揽人</option>
											<option value='5' <s:if test="%{type==5}">selected</s:if>>项目成员</option>
										</select>
										<a id="search" class="easyui-linkbutton" icon="icon-search" href="javascript:void(0);">查询</a>
									</td>
								</tr>
							</table>
						<td>
					<tr>
				</table>
				</div>
			</div>
		</form>
		<div style="padding:5px;text-align:center;">
			<table id='iform_grid' width="99%" style="border:1px solid #efefef">
				<tr class="header">
					<td style="width:20%;">项目全称</td>
					<td style="width:10%;">更新时间</td>
					<td style="width:10%;">操作人</td>
					<td style="width:10%;">变更信息</td>
					<td style="width:10%;">操作</td>
					<td style="width:35%;">编辑内容</td>
				</tr>
				<s:iterator value="logList" status="status">
					<tr class="cell">
						<td><s:property value="PROJECTNAME" /></td>
						<td><s:property value="CREATEDATE" /></td>
						<td><s:property value="USERNAME" /></td>
						<td><s:property value="TITLE" /></td>
						<s:if test="STATUS=='数据新增'">
							<td><span style="color:red;"><s:property value="STATUS" /></span></td>
						</s:if>
						<s:elseif test="STATUS=='数据更新'">
							<td><span style="color:green;"><s:property value="STATUS" /></span></td>
						</s:elseif>
						<s:elseif test="STATUS=='数据移除'">
							<td><span style="color:blue;"><s:property value="STATUS" /></span></td>
						</s:elseif>
						<td><s:property value="CONTENT" /></td>
					</tr>
				</s:iterator>
			</table>
			<div id='prowed_ifrom_grid'></div>
		</div>
	</div>
</body>
</html>
<!-- 新增查询过滤SQL注入关键字 -->
<script language="JavaScript">
	jQuery.validator.addMethod("string", function(value, element) {
		var sqlstr = [ " and ", " exec ", " count ", " chr ", " mid ",
				" master ", " or ", " truncate ", " char ", " declare ",
				" join ", "insert ", "select ", "delete ", "update ",
				"create ", "drop " ]
		var patrn = /[`~!#$%^&*+<>?"{},;'[\]]/im;
		if (patrn.test(value)) {
		} else {
			var flag = false;
			var tmp = value.toLowerCase();
			for (var i = 0; i < sqlstr.length; i++) {
				var str = sqlstr[i];
				if (tmp.indexOf(str) > -1) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				return "success";
			}
		}
	}, "包含非法字符!");
</script>
