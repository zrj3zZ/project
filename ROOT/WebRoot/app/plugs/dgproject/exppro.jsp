<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>项目汇总</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css" />
<link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/easyui/themes/gray/easyui.css" />
<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css" />
<link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/autocomplete/jquery.autocomplete.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/iwork/oaknow.css" />
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.src.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js" charset="utf-8"></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript" src="iwork_js/engine/iformpage.js" charset="utf-8"></script>
<script type="text/javascript">
	function doSubmit() {
		var ptype = $("#ptype").val();
		var temp=new Array();
		var type = $("#type").val();
		if(ptype==null||ptype==""){
			art.dialog.tips("请勾选导出项!");
			return;
		}
		<s:if test="type=='xsb'">
			if(ptype=="推荐挂牌"){
				var projectbaseinfo = $("projectbaseinfo").is(":checked");
				var projectjoinersinfo = $("projectjoinersinfo").is(":checked");
				var projectprocessinfo = $("projectprocessinfo").is(":checked");
				var projectchargeinfo = $("projectchargeinfo").is(":checked");
				var projecterrorrecord = $("projecterrorrecord").is(":checked");
				var projectstagesinfo = $("projectstagesinfo").is(":checked");
				var projectmemberinfo = $("projectmemberinfo").is(":checked");
				var projectagencyinfo = $("projectagencyinfo").is(":checked");
				var financedata = $("financedata").is(":checked");
				$("input[name='box']:checked").each(function(){temp.push($(this).val());});
			}else if(ptype=="定增"){
				var projectbaseinfo = $("projectbaseinfo").is(":checked");
				var projecterrorrecord = $("projecterrorrecord").is(":checked");
				var projectstagesinfo = $("projectstagesinfo").is(":checked");
				var projectplacementinfo = $("projectplacementinfo").is(":checked");
				var projectagencyinfo = $("projectagencyinfo").is(":checked");
				var financedata = $("financedata").is(":checked");
				$("input[name='box2']:checked").each(function(){temp.push($(this).val());});
			}else if(ptype=="并购重组"){
				var projectbaseinfo = $("projectbaseinfo").is(":checked");
				var projectstagesinfo = $("projectstagesinfo").is(":checked");
				var projectjoinersinfo = $("projectjoinersinfo").is(":checked");
				var projectagencyinfo = $("projectagencyinfo").is(":checked");
				var financedata = $("financedata").is(":checked");
				var financedataopponent = $("financedataopponent").is(":checked");
				var projecterrorrecord = $("projecterrorrecord").is(":checked");
				$("input[name='box3']:checked").each(function(){temp.push($(this).val());});
			}else if(ptype=="其他"){
				var projectbaseinfo = $("projectbaseinfo").is(":checked");
				var projectstagesinfo = $("projectstagesinfo").is(":checked");
				var projectmemberinfo = $("projectmemberinfo").is(":checked");
				var projectagencyinfo = $("projectagencyinfo").is(":checked");
				var financedata = $("financedata").is(":checked");
				var projecterrorrecord = $("projecterrorrecord").is(":checked");
				$("input[name='box4']:checked").each(function(){temp.push($(this).val());});
			}
		</s:if>
		<s:elseif test="type=='ssgs'">
			if(ptype=="首次公开发行股票"){
				var projectbaseinfo = $("projectbaseinfo").is(":checked");
				var projectstagesinfo = $("projectstagesinfo").is(":checked");
				var projectmemberinfo = $("projectmemberinfo").is(":checked");
				var projectagencyinfo = $("projectagencyinfo").is(":checked");
				var financedata = $("financedata").is(":checked");
				var projecterrorrecord = $("projecterrorrecord").is(":checked");
				$("input[name='box']:checked").each(function(){temp.push($(this).val());});
			}else if(ptype=="再融资"){
				var projectbaseinfo = $("projectbaseinfo").is(":checked");
				var projectstagesinfo = $("projectstagesinfo").is(":checked");
				var projectmemberinfo = $("projectmemberinfo").is(":checked");
				var projectagencyinfo = $("projectagencyinfo").is(":checked");
				var financedata = $("financedata").is(":checked");
				var projecterrorrecord = $("projecterrorrecord").is(":checked");
				$("input[name='box2']:checked").each(function(){temp.push($(this).val());});
			}else if(ptype=="并购重组"){
				var projectbaseinfo = $("projectbaseinfo").is(":checked");
				var projectstagesinfo = $("projectstagesinfo").is(":checked");
				var projectjoinersinfo = $("projectjoinersinfo").is(":checked");
				var projectagencyinfo = $("projectagencyinfo").is(":checked");
				var financedata = $("financedata").is(":checked");
				var financedataopponent = $("financedataopponent").is(":checked");
				var projecterrorrecord = $("projecterrorrecord").is(":checked");
				$("input[name='box3']:checked").each(function(){temp.push($(this).val());});
			}else if(ptype=="其他"){
				var projectbaseinfo = $("projectbaseinfo").is(":checked");
				var projectstagesinfo = $("projectstagesinfo").is(":checked");
				var projectmemberinfo = $("projectmemberinfo").is(":checked");
				var projectagencyinfo = $("projectagencyinfo").is(":checked");
				var financedata = $("financedata").is(":checked");
				var projecterrorrecord = $("projecterrorrecord").is(":checked");
				$("input[name='box4']:checked").each(function(){temp.push($(this).val());});
			}
		</s:elseif>
		<s:elseif test="type=='zq'">
		var projectbaseinfo = $("projectbaseinfo").is(":checked");
		var projectstagesinfo = $("projectstagesinfo").is(":checked");
		var projectmemberinfo = $("projectmemberinfo").is(":checked");
		var projectagencyinfo = $("projectagencyinfo").is(":checked");
		var financedata = $("financedata").is(":checked");
		var projecterrorrecord = $("projecterrorrecord").is(":checked");
			if(ptype=="公司债"){
				$("input[name='box']:checked").each(function(){temp.push($(this).val());});
			}else if(ptype=="企业债"){
				$("input[name='box2']:checked").each(function(){temp.push($(this).val());});
			}else if(ptype=="可交换债"){
				$("input[name='box3']:checked").each(function(){temp.push($(this).val());});
			}else if(ptype=="其他"){
				$("input[name='box4']:checked").each(function(){temp.push($(this).val());});
			}
		</s:elseif>
		
		temp=temp.join(',');
		if(temp==null||temp==""){
			art.dialog.tips("请勾选导出项!");
			return;
		}
		var pageUrl = encodeURI("dg_zqb_project_exppj.action?expfields="+temp+"&type="+type+"&ptype="+ptype);
		window.onbeforeunload = null;//禁用表单关闭浏览器询问
		window.location.href = pageUrl;
		/* $("#editForm").attr("action", pageUrl);
		$("#editForm").submit(); */
	}
	function setXmlx(ptype){
		$("#ptype").val(ptype);
	}
	$(function (){
		$('#all').click(function(){
	        //判断apple是否被选中
	        var bischecked=$('#all').is(':checked');
	        var fruit=$('input[name="box"]');
	        bischecked?fruit.attr('checked',true):fruit.attr('checked',false);
		});
		$('#all2').click(function(){
	        //判断apple是否被选中
	        var bischecked=$('#all2').is(':checked');
	        var fruit=$('input[name="box2"]');
	        bischecked?fruit.attr('checked',true):fruit.attr('checked',false);
		});
		$('#all3').click(function(){
	        //判断apple是否被选中
	        var bischecked=$('#all3').is(':checked');
	        var fruit=$('input[name="box3"]');
	        bischecked?fruit.attr('checked',true):fruit.attr('checked',false);
		});
		$('#all4').click(function(){
	        //判断apple是否被选中
	        var bischecked=$('#all4').is(':checked');
	        var fruit=$('input[name="box4"]');
	        bischecked?fruit.attr('checked',true):fruit.attr('checked',false);
		});
	});
	function pageClose(){
		window.onbeforeunload = null;//禁用表单关闭浏览器询问
		if(typeof(api) =="undefined"){
			window.close();
		}else{
			api.close();
		}
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
</style>
</head>
<body class="easyui-layout">
	<div id="blockPage" class="black_overlay" style="display:none"></div>
	<div region="north" style="height:40px;" border="false">
		<div class="tools_nav">
			<table width="100%">
				<tr>
					<td align="left">
						<a id="btnEp" class="easyui-linkbutton" icon="icon-save" plain="true" href="javascript:doSubmit();">确认导出</a>
						<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
						<a href="javascript:pageClose();" class="easyui-linkbutton" plain="true" iconCls="icon-cancel">关闭</a>
					</td>
					<td style="text-align:right;padding-right:10px">
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div region="center" style="text-align:center;border-left:1px #999 dotted;border-right:1px #999 dotted;border-top:1px #999 dotted;border-bottom:0px #999 dotted;padding:2px;">
		<div id="mainFrameTab" style="border:0px" class="easyui-tabs" fit="true">
			<s:if test="type=='xsb'">
				<div onclick="setXmlx('推荐挂牌');" title="推荐挂牌" border="false" style="border:0px" iconCls="icon-search" cache="false">
					<s:form id="editForm" name="editForm" theme="simple">
						<!--表单参数-->
						<div id="border">
							<table style="margin-bottom:5px;" class="ke-zeroborder" border="0"
								cellpadding="0" cellspacing="0" width="100%">
								<tbody>
									<tr>
										<td class="formpage_title">推荐挂牌</td>
									</tr>
									<tr>
										<td id="help" align="right"><br /></td>
									</tr>
									<tr>
										<td class="line" align="right"><br /></td>
									</tr>
									<tr>
										<td align="left">
											<input id="all" type="checkbox" name="all">&nbsp;全选
											<br />
											<fieldset>
												<legend>项目信息</legend>
												<table class="ke-zeroborder" border="0" cellpadding="0"
													cellspacing="0" width="100%">
													<tbody>
														<tr id="itemTr_1913">
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectbaseinfo" type="checkbox" value="PROJECTBASEINFO" name="box">&nbsp;项目基本情况
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectjoinersinfo" type="checkbox" name="box" value="PROJECTJOINERSINFO">&nbsp;项目人员安排
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectprocessinfo" type="checkbox" value="PROJECTPROCESSINFO" name="box">&nbsp;项目进度时间
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectchargeinfo" type="checkbox" value="PROJECTCHARGEINFO" name="box">&nbsp;项目预计收费情况
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projecterrorrecord" type="checkbox" value="PROJECTERRORRECORD" name="box">&nbsp;项目异常情况记录
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectstagesinfo" type="checkbox" value="PROJECTSTAGESINFO" name="box">&nbsp;项目阶段信息
															</td>
														</tr>
														<tr id="itemTr_1913">
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectmemberinfo" type="checkbox" value="PROJECTMEMBERINFO" name="box">&nbsp;项目成员列表
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectagencyinfo" type="checkbox" value="PROJECTAGENCYINFO" name="box">&nbsp;项目中介机构
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="financedata" type="checkbox" value="FINANCEDATA" name="box">&nbsp;财务数据
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box">&nbsp; -->
															</td>
														</tr>
													</tbody>
												</table>
												</fieldset>
												<br />
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</s:form>
				</div>
				<div onclick="setXmlx('定增');" title="定增" border="false" style="border:0px" iconCls="icon-search" cache="false">
					<s:form id="editForm" name="editForm" theme="simple">
						<!--表单参数-->
						<div id="border">
							<table style="margin-bottom:5px;" class="ke-zeroborder" border="0"
								cellpadding="0" cellspacing="0" width="100%">
								<tbody>
									<tr>
										<td class="formpage_title">定增</td>
									</tr>
									<tr>
										<td id="help" align="right"><br /></td>
									</tr>
									<tr>
										<td class="line" align="right"><br /></td>
									</tr>
									<tr>
										<td align="left">
											<input id="all2" type="checkbox" name="all2">&nbsp;全选
											<br />
											<fieldset>
												<legend>项目信息</legend>
												<table class="ke-zeroborder" border="0" cellpadding="0"
													cellspacing="0" width="100%">
													<tbody>
														<tr id="itemTr_1913">
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectbaseinfo" type="checkbox" value="PROJECTBASEINFO" name="box2">&nbsp;项目基本情况
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projecterrorrecord" type="checkbox" value="PROJECTERRORRECORD" name="box2">&nbsp;项目异常情况记录
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectstagesinfo" type="checkbox" value="PROJECTSTAGESINFO" name="box2">&nbsp;项目阶段
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectplacementinfo" type="checkbox" name="box2" value="PROJECTPLACEMENTINFO">&nbsp;定增对象
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectagencyinfo" type="checkbox" value="PROJECTAGENCYINFO" name="box2">&nbsp;中介机构
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="financedata" type="checkbox" value="FINANCEDATA" name="box2">&nbsp;财务数据
															</td>
														</tr>
														<tr id="itemTr_1913">
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box2">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box2">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box2">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box2">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box2">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box2">&nbsp; -->
															</td>
														</tr>
													</tbody>
												</table>
												</fieldset>
												<br />
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</s:form>
				</div>
				<div onclick="setXmlx('并购重组');" title="并购重组" border="false" style="border:0px" iconCls="icon-search" cache="false">
					<s:form id="editForm" name="editForm" theme="simple">
						<!--表单参数-->
						<div id="border">
							<table style="margin-bottom:5px;" class="ke-zeroborder" border="0"
								cellpadding="0" cellspacing="0" width="100%">
								<tbody>
									<tr>
										<td class="formpage_title">并购重组</td>
									</tr>
									<tr>
										<td id="help" align="right"><br /></td>
									</tr>
									<tr>
										<td class="line" align="right"><br /></td>
									</tr>
									<tr>
										<td align="left">
											<input id="all3" type="checkbox" name="all3">&nbsp;全选
											<br />
											<fieldset>
												<legend>项目信息</legend>
												<table class="ke-zeroborder" border="0" cellpadding="0"
													cellspacing="0" width="100%">
													<tbody>
														<tr id="itemTr_1913">
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectbaseinfo" type="checkbox" value="PROJECTBASEINFO" name="box3">&nbsp;项目基本情况
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectstagesinfo" type="checkbox" value="PROJECTSTAGESINFO" name="box3">&nbsp;项目阶段信息
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectjoinersinfo" type="checkbox" name="box3" value="PROJECTJOINERSINFO">&nbsp;项目参与人
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectagencyinfo" type="checkbox" value="PROJECTAGENCYINFO" name="box3">&nbsp;中介机构
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="financedata" type="checkbox" value="FINANCEDATA" name="box3">&nbsp;交易方财务情况
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="financedataopponent" type="checkbox" value="FINANCEDATAOPPONENT" name="box3">&nbsp;交易对手方财务情况
															</td>
														</tr>
														<tr id="itemTr_1913">
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projecterrorrecord" type="checkbox" value="PROJECTERRORRECORD" name="box3">&nbsp;项目异常情况记录
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box3">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box3">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box3">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box3">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box3">&nbsp; -->
															</td>
														</tr>
													</tbody>
												</table>
												</fieldset>
												<br />
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</s:form>
				</div>
				<div onclick="setXmlx('其他');" title="其他" border="false" style="border:0px" iconCls="icon-search" cache="false">
					<s:form id="editForm" name="editForm" theme="simple">
						<!--表单参数-->
						<div id="border">
							<table style="margin-bottom:5px;" class="ke-zeroborder" border="0"
								cellpadding="0" cellspacing="0" width="100%">
								<tbody>
									<tr>
										<td class="formpage_title">其他</td>
									</tr>
									<tr>
										<td id="help" align="right"><br /></td>
									</tr>
									<tr>
										<td class="line" align="right"><br /></td>
									</tr>
									<tr>
										<td align="left">
											<input id="all4" type="checkbox" name="all4">&nbsp;全选
											<br />
											<fieldset>
												<legend>项目信息</legend>
												<table class="ke-zeroborder" border="0" cellpadding="0"
													cellspacing="0" width="100%">
													<tbody>
														<tr id="itemTr_1913">
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectbaseinfo" type="checkbox" value="PROJECTBASEINFO" name="box4">&nbsp;项目基本情况
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectstagesinfo" type="checkbox" value="PROJECTSTAGESINFO" name="box4">&nbsp;项目阶段信息
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectmemberinfo" type="checkbox" value="PROJECTMEMBERINFO" name="box4">&nbsp;项目成员列表
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectagencyinfo" type="checkbox" value="PROJECTAGENCYINFO" name="box4">&nbsp;项目中介机构
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="financedata" type="checkbox" value="FINANCEDATA" name="box4">&nbsp;财务数据
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projecterrorrecord" type="checkbox" value="PROJECTERRORRECORD" name="box4">&nbsp;项目异常情况记录
															</td>
														</tr>
														<tr id="itemTr_1913">
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box4">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box4">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box4">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box4">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box4">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box4">&nbsp; -->
															</td>
														</tr>
													</tbody>
												</table>
												</fieldset>
												<br />
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</s:form>
				</div>
			</s:if>
			<s:elseif test="type=='ssgs'">
				<div onclick="setXmlx('首次公开发行股票');" title="首次公开发行股票" border="false" style="border:0px" iconCls="icon-search" cache="false">
					<s:form id="editForm" name="editForm" theme="simple">
						<!--表单参数-->
						<div id="border">
							<table style="margin-bottom:5px;" class="ke-zeroborder" border="0"
								cellpadding="0" cellspacing="0" width="100%">
								<tbody>
									<tr>
										<td class="formpage_title">首次公开发行股票</td>
									</tr>
									<tr>
										<td id="help" align="right"><br /></td>
									</tr>
									<tr>
										<td class="line" align="right"><br /></td>
									</tr>
									<tr>
										<td align="left">
											<input id="all" type="checkbox" name="all">&nbsp;全选
											<br />
											<fieldset>
												<legend>项目信息</legend>
												<table class="ke-zeroborder" border="0" cellpadding="0"
													cellspacing="0" width="100%">
													<tbody>
														<tr id="itemTr_1913">
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectbaseinfo" type="checkbox" value="PROJECTBASEINFO" name="box">&nbsp;项目基本情况
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectstagesinfo" type="checkbox" value="PROJECTSTAGESINFO" name="box">&nbsp;项目阶段信息
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectmemberinfo" type="checkbox" value="PROJECTMEMBERINFO" name="box">&nbsp;项目成员列表
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectagencyinfo" type="checkbox" value="PROJECTAGENCYINFO" name="box">&nbsp;项目中介机构
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="financedata" type="checkbox" value="FINANCEDATA" name="box">&nbsp;财务数据
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projecterrorrecord" type="checkbox" value="PROJECTERRORRECORD" name="box">&nbsp;项目异常情况记录
															</td>
														</tr>
														<tr id="itemTr_1913">
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box">&nbsp; -->
															</td>
														</tr>
													</tbody>
												</table>
												</fieldset>
												<br />
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</s:form>
				</div>
				<div onclick="setXmlx('再融资');" title="再融资" border="false" style="border:0px" iconCls="icon-search" cache="false">
					<s:form id="editForm" name="editForm" theme="simple">
						<!--表单参数-->
						<div id="border">
							<table style="margin-bottom:5px;" class="ke-zeroborder" border="0"
								cellpadding="0" cellspacing="0" width="100%">
								<tbody>
									<tr>
										<td class="formpage_title">再融资</td>
									</tr>
									<tr>
										<td id="help" align="right"><br /></td>
									</tr>
									<tr>
										<td class="line" align="right"><br /></td>
									</tr>
									<tr>
										<td align="left">
											<input id="all2" type="checkbox" name="all2">&nbsp;全选
											<br />
											<fieldset>
												<legend>项目信息</legend>
												<table class="ke-zeroborder" border="0" cellpadding="0"
													cellspacing="0" width="100%">
													<tbody>
														<tr id="itemTr_1913">
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectbaseinfo" type="checkbox" value="PROJECTBASEINFO" name="box2">&nbsp;项目基本情况
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectstagesinfo" type="checkbox" value="PROJECTSTAGESINFO" name="box2">&nbsp;项目阶段信息
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectmemberinfo" type="checkbox" value="PROJECTMEMBERINFO" name="box2">&nbsp;项目成员列表
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectagencyinfo" type="checkbox" value="PROJECTAGENCYINFO" name="box2">&nbsp;项目中介机构
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="financedata" type="checkbox" value="FINANCEDATA" name="box2">&nbsp;财务数据
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projecterrorrecord" type="checkbox" value="PROJECTERRORRECORD" name="box2">&nbsp;项目异常情况记录
															</td>
														</tr>
														<tr id="itemTr_1913">
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box2">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box2">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box2">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box2">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box2">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box2">&nbsp; -->
															</td>
														</tr>
													</tbody>
												</table>
												</fieldset>
												<br />
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</s:form>
				</div>
				<div onclick="setXmlx('并购重组');" title="并购重组" border="false" style="border:0px" iconCls="icon-search" cache="false">
					<s:form id="editForm" name="editForm" theme="simple">
						<!--表单参数-->
						<div id="border">
							<table style="margin-bottom:5px;" class="ke-zeroborder" border="0"
								cellpadding="0" cellspacing="0" width="100%">
								<tbody>
									<tr>
										<td class="formpage_title">并购重组</td>
									</tr>
									<tr>
										<td id="help" align="right"><br /></td>
									</tr>
									<tr>
										<td class="line" align="right"><br /></td>
									</tr>
									<tr>
										<td align="left">
											<input id="all3" type="checkbox" name="all3">&nbsp;全选
											<br />
											<fieldset>
												<legend>项目信息</legend>
												<table class="ke-zeroborder" border="0" cellpadding="0"
													cellspacing="0" width="100%">
													<tbody>
														<tr id="itemTr_1913">
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectbaseinfo" type="checkbox" value="PROJECTBASEINFO" name="box3">&nbsp;项目基本情况
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectstagesinfo" type="checkbox" value="PROJECTSTAGESINFO" name="box3">&nbsp;项目阶段信息
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectjoinersinfo" type="checkbox" name="box3" value="PROJECTJOINERSINFO">&nbsp;项目参与人
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectagencyinfo" type="checkbox" value="PROJECTAGENCYINFO" name="box3">&nbsp;中介机构
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="financedata" type="checkbox" value="FINANCEDATA" name="box3">&nbsp;交易方财务情况
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="financedataopponent" type="checkbox" value="FINANCEDATAOPPONENT" name="box3">&nbsp;交易对手方财务情况
															</td>
														</tr>
														<tr id="itemTr_1913">
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projecterrorrecord" type="checkbox" value="PROJECTERRORRECORD" name="box3">&nbsp;项目异常情况记录
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box3">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box3">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box3">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box3">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box3">&nbsp; -->
															</td>
														</tr>
													</tbody>
												</table>
												</fieldset>
												<br />
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</s:form>
				</div>
				<div onclick="setXmlx('其他');" title="其他" border="false" style="border:0px" iconCls="icon-search" cache="false">
					<s:form id="editForm" name="editForm" theme="simple">
						<!--表单参数-->
						<div id="border">
							<table style="margin-bottom:5px;" class="ke-zeroborder" border="0"
								cellpadding="0" cellspacing="0" width="100%">
								<tbody>
									<tr>
										<td class="formpage_title">其他</td>
									</tr>
									<tr>
										<td id="help" align="right"><br /></td>
									</tr>
									<tr>
										<td class="line" align="right"><br /></td>
									</tr>
									<tr>
										<td align="left">
											<input id="all4" type="checkbox" name="all4">&nbsp;全选
											<br />
											<fieldset>
												<legend>项目信息</legend>
												<table class="ke-zeroborder" border="0" cellpadding="0"
													cellspacing="0" width="100%">
													<tbody>
														<tr id="itemTr_1913">
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectbaseinfo" type="checkbox" value="PROJECTBASEINFO" name="box4">&nbsp;项目基本情况
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectstagesinfo" type="checkbox" value="PROJECTSTAGESINFO" name="box4">&nbsp;项目阶段信息
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectmemberinfo" type="checkbox" value="PROJECTMEMBERINFO" name="box4">&nbsp;项目成员列表
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectagencyinfo" type="checkbox" value="PROJECTAGENCYINFO" name="box4">&nbsp;项目中介机构
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="financedata" type="checkbox" value="FINANCEDATA" name="box4">&nbsp;财务数据
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projecterrorrecord" type="checkbox" value="PROJECTERRORRECORD" name="box4">&nbsp;项目异常情况记录
															</td>
														</tr>
														<tr id="itemTr_1913">
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box4">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box4">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box4">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box4">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box4">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box4">&nbsp; -->
															</td>
														</tr>
													</tbody>
												</table>
												</fieldset>
												<br />
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</s:form>
				</div>
			</s:elseif>
			<s:elseif test="type=='zq'">
				<div onclick="setXmlx('公司债');" title="公司债" border="false" style="border:0px" iconCls="icon-search" cache="false">
					<s:form id="editForm" name="editForm" theme="simple">
						<!--表单参数-->
						<div id="border">
							<table style="margin-bottom:5px;" class="ke-zeroborder" border="0"
								cellpadding="0" cellspacing="0" width="100%">
								<tbody>
									<tr>
										<td class="formpage_title">公司债</td>
									</tr>
									<tr>
										<td id="help" align="right"><br /></td>
									</tr>
									<tr>
										<td class="line" align="right"><br /></td>
									</tr>
									<tr>
										<td align="left">
											<input id="all" type="checkbox" name="all">&nbsp;全选
											<br />
											<fieldset>
												<legend>项目信息</legend>
												<table class="ke-zeroborder" border="0" cellpadding="0"
													cellspacing="0" width="100%">
													<tbody>
														<tr id="itemTr_1913">
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectbaseinfo" type="checkbox" value="PROJECTBASEINFO" name="box">&nbsp;项目基本情况
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectstagesinfo" type="checkbox" value="PROJECTSTAGESINFO" name="box">&nbsp;项目阶段信息
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectmemberinfo" type="checkbox" value="PROJECTMEMBERINFO" name="box">&nbsp;项目成员列表
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectagencyinfo" type="checkbox" value="PROJECTAGENCYINFO" name="box">&nbsp;项目中介机构
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="financedata" type="checkbox" value="FINANCEDATA" name="box">&nbsp;财务数据
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projecterrorrecord" type="checkbox" value="PROJECTERRORRECORD" name="box">&nbsp;项目异常情况记录
															</td>
														</tr>
														<tr id="itemTr_1913">
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box">&nbsp; -->
															</td>
														</tr>
													</tbody>
												</table>
												</fieldset>
												<br />
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</s:form>
				</div>
				<div onclick="setXmlx('企业债');" title="企业债" border="false" style="border:0px" iconCls="icon-search" cache="false">
					<s:form id="editForm" name="editForm" theme="simple">
						<!--表单参数-->
						<div id="border">
							<table style="margin-bottom:5px;" class="ke-zeroborder" border="0"
								cellpadding="0" cellspacing="0" width="100%">
								<tbody>
									<tr>
										<td class="formpage_title">企业债</td>
									</tr>
									<tr>
										<td id="help" align="right"><br /></td>
									</tr>
									<tr>
										<td class="line" align="right"><br /></td>
									</tr>
									<tr>
										<td align="left">
											<input id="all2" type="checkbox" name="all2">&nbsp;全选
											<br />
											<fieldset>
												<legend>项目信息</legend>
												<table class="ke-zeroborder" border="0" cellpadding="0"
													cellspacing="0" width="100%">
													<tbody>
														<tr id="itemTr_1913">
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectbaseinfo" type="checkbox" value="PROJECTBASEINFO" name="box2">&nbsp;项目基本情况
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectstagesinfo" type="checkbox" value="PROJECTSTAGESINFO" name="box2">&nbsp;项目阶段信息
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectmemberinfo" type="checkbox" value="PROJECTMEMBERINFO" name="box2">&nbsp;项目成员列表
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectagencyinfo" type="checkbox" value="PROJECTAGENCYINFO" name="box2">&nbsp;项目中介机构
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="financedata" type="checkbox" value="FINANCEDATA" name="box2">&nbsp;财务数据
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projecterrorrecord" type="checkbox" value="PROJECTERRORRECORD" name="box2">&nbsp;项目异常情况记录
															</td>
														</tr>
														<tr id="itemTr_1913">
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box2">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box2">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box2">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box2">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box2">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box2">&nbsp; -->
															</td>
														</tr>
													</tbody>
												</table>
												</fieldset>
												<br />
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</s:form>
				</div>
				<div onclick="setXmlx('可交换债');" title="可交换债" border="false" style="border:0px" iconCls="icon-search" cache="false">
					<s:form id="editForm" name="editForm" theme="simple">
						<!--表单参数-->
						<div id="border">
							<table style="margin-bottom:5px;" class="ke-zeroborder" border="0"
								cellpadding="0" cellspacing="0" width="100%">
								<tbody>
									<tr>
										<td class="formpage_title">可交换债</td>
									</tr>
									<tr>
										<td id="help" align="right"><br /></td>
									</tr>
									<tr>
										<td class="line" align="right"><br /></td>
									</tr>
									<tr>
										<td align="left">
											<input id="all3" type="checkbox" name="all3">&nbsp;全选
											<br />
											<fieldset>
												<legend>项目信息</legend>
												<table class="ke-zeroborder" border="0" cellpadding="0"
													cellspacing="0" width="100%">
													<tbody>
														<tr id="itemTr_1913">
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectbaseinfo" type="checkbox" value="PROJECTBASEINFO" name="box3">&nbsp;项目基本情况
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectstagesinfo" type="checkbox" value="PROJECTSTAGESINFO" name="box3">&nbsp;项目阶段信息
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectmemberinfo" type="checkbox" value="PROJECTMEMBERINFO" name="box3">&nbsp;项目成员列表
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectagencyinfo" type="checkbox" value="PROJECTAGENCYINFO" name="box3">&nbsp;项目中介机构
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="financedata" type="checkbox" value="FINANCEDATA" name="box3">&nbsp;财务数据
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projecterrorrecord" type="checkbox" value="PROJECTERRORRECORD" name="box3">&nbsp;项目异常情况记录
															</td>
														</tr>
														<tr id="itemTr_1913">
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box3">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box3">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box3">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box3">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box3">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box3">&nbsp; -->
															</td>
														</tr>
													</tbody>
												</table>
												</fieldset>
												<br />
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</s:form>
				</div>
				<div onclick="setXmlx('其他');" title="其他" border="false" style="border:0px" iconCls="icon-search" cache="false">
					<s:form id="editForm" name="editForm" theme="simple">
						<!--表单参数-->
						<div id="border">
							<table style="margin-bottom:5px;" class="ke-zeroborder" border="0"
								cellpadding="0" cellspacing="0" width="100%">
								<tbody>
									<tr>
										<td class="formpage_title">其他</td>
									</tr>
									<tr>
										<td id="help" align="right"><br /></td>
									</tr>
									<tr>
										<td class="line" align="right"><br /></td>
									</tr>
									<tr>
										<td align="left">
											<input id="all4" type="checkbox" name="all4">&nbsp;全选
											<br />
											<fieldset>
												<legend>项目信息</legend>
												<table class="ke-zeroborder" border="0" cellpadding="0"
													cellspacing="0" width="100%">
													<tbody>
														<tr id="itemTr_1913">
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectbaseinfo" type="checkbox" value="PROJECTBASEINFO" name="box4">&nbsp;项目基本情况
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectstagesinfo" type="checkbox" value="PROJECTSTAGESINFO" name="box4">&nbsp;项目阶段信息
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectmemberinfo" type="checkbox" value="PROJECTMEMBERINFO" name="box4">&nbsp;项目成员列表
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projectagencyinfo" type="checkbox" value="PROJECTAGENCYINFO" name="box4">&nbsp;项目中介机构
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="financedata" type="checkbox" value="FINANCEDATA" name="box4">&nbsp;财务数据
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<input id="projecterrorrecord" type="checkbox" value="PROJECTERRORRECORD" name="box4">&nbsp;项目异常情况记录
															</td>
														</tr>
														<tr id="itemTr_1913">
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box4">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box4">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box4">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box4">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box4">&nbsp; -->
															</td>
															<td class="td_data" id="data_TZBT" width="150px">
																<!-- <input id="" type="checkbox" value="" name="box4">&nbsp; -->
															</td>
														</tr>
													</tbody>
												</table>
												</fieldset>
												<br />
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</s:form>
				</div>
			</s:elseif>
			
		</div>
	</div>
	<div style="display:none;">
		<input id="ptype" type="hidden" value="" name="ptype">
		<s:hidden name="type"></s:hidden>
	</div>
</body>
</html>
<!-- 新增查询过滤SQL注入关键字 -->
<script language="JavaScript"> 
  jQuery.validator.addMethod("string", function(value, element) {
          var sqlstr=[" and "," exec ", " count ", " chr ", " mid ", " master ", " or ", " truncate ", " char ", " declare ", " join ","insert ", "select ", "delete ", "update ","create ","drop "]
          var patrn=/[`~!#$%^&*+<>?"{},;'[\]]/im;
    	    if(patrn.test(value)){
        	}else{
            	var flag = false;
            	var tmp = value.toLowerCase();
            	for(var i=0;i<sqlstr.length;i++){
                	var str = sqlstr[i];
					if(tmp.indexOf(str)>-1){
						flag = true;
						break;
					}
                }
                if(!flag){
                	return "success";
                }
            }
        }, "包含非法字符!");
</script>