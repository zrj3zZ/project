<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>财务数据对比</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
		<link rel="stylesheet" type="text/css"
			href="iwork_css/jquerycss/process-icon.css" />
		<link rel="stylesheet" type="text/css"
			href="iwork_js/jqueryjs/easyui/themes/gray/easyui.css" />
		<link rel="stylesheet" type="text/css" media="screen"
			href="iwork_css/jquerycss/validate/screen.css" />
		<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css" />
		<link rel="stylesheet" type="text/css"
			href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css" />
		<link rel="stylesheet" type="text/css"
			href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css" />
		<link rel="stylesheet" type="text/css"
			href="iwork_css/engine/iformpage.css" />
		<link rel="stylesheet" type="text/css"
			href="iwork_css/iwork/oaknow.css" />
		<script type="text/javascript" src="iwork_js/commons.js"></script>
		<script type="text/javascript"
			src="iwork_js/jqueryjs/jquery-3.1.0.min.js" charset="utf-8"></script>
		<script type="text/javascript"
			src="iwork_js/jqueryjs/jquery.easyui.min.js" charset="utf-8"></script>
		<script type="text/javascript"
			src="iwork_js/jqueryjs/languages/grid.locale-cn.js" charset="utf-8"></script>
		<script type="text/javascript"
			src="iwork_js/jqueryjs/jquery.jqGrid.src.js" charset="utf-8"> </script>
		<script type="text/javascript"
			src="iwork_js/jqueryjs/jquery.validate.js" charset="utf-8"></script>
		<script type="text/javascript"
			src="iwork_js/jqueryjs/jquery.metadata.js" charset="utf-8"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"
			charset="utf-8"></script>
		<script type="text/javascript"
			src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
		<script type="text/javascript"
			src="iwork_js/jqueryjs/languages/messages_cn.js" charset="utf-8"></script>
		<script type="text/javascript"
			src="iwork_js/lhgdialog/lhgdialog.min.js?self=true"></script>
		<script type="text/javascript" src="iwork_js/engine/iformpage.js"
			charset="utf-8"></script>
		<script type="text/javascript">
	function doSubmit(){
		var FILETEXT=document.getElementById("FILETEXT").value;
		if(FILETEXT==""){
			alert("请选择文件后进行数据对比。");
			return;
		}
		var pageUrl="zqb_file_compare.action";
		$.post(pageUrl,{FILETEXT:FILETEXT},function(data){
			var divshow = $("#fileCompareDiv");
            divshow.text("");// 清空数据
            divshow.append(data);
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
					<td align="left"><a id="btnEp" class="easyui-linkbutton"
						icon="icon-add" plain="true" href="#" onclick="doSubmit();">对比</a>
					</td>
					<td style="text-align:right;padding-right:10px"></td>
				</tr>
			</table>
		</div>
	</div>
	<%-- <div region="center"
		style="text-align:center;border-left:1px #999 dotted;border-right:1px #999 dotted;border-top:1px #999 dotted;border-bottom:0px #999 dotted;padding:2px;">
		<s:form id="editForm" name="editForm" theme="simple">
			<div id="border">
				<table style="margin-bottom:5px;" class="ke-zeroborder" border="0"
					cellpadding="0" cellspacing="0" width="100%">
					<tbody>
						<tr>
							<td class="formpage_title">数据对比表单</td>
						</tr>
						<tr>
							<td id="help" align="right"><br /></td>
						</tr>
						<tr>
							<td class="line" align="right"><br /></td>
						</tr>
						<tr>
							<td align="left">
								<table class="ke-zeroborder" border="0" cellpadding="0"
									cellspacing="0" width="100%">
									<tbody>
										<tr id="itemTr_1916">
											<td class="td_title" id="title_FILETEXT" width="180">对比文件</td>
											<td class="td_data" id="data_FILETEXT" colspan="3">
												<div id="DIVFILETEXT">
													<div>
														<input type="hidden" name="FILETEXT" class="valid"
															id="FILETEXT" size="100">
													</div>
													<div>
														<button
															onclick="showUploadifyPageFILETEXT();return false;">附件上传</button>
													</div>
													<s:property value="count" escapeHtml="false" />
												</div> <script>
																function showUploadifyPageFILETEXT(){
																	uploadifyDialog('FILETEXT','FILETEXT','DIVFILETEXT','','true','','');
																}
															</script>
											</td>
										</tr>
									</tbody>
								</table>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div id="fileCompareDiv">
			
			</div>
		</s:form>
	</div> --%>
	<div region="center" style="padding-left:0px;padding-right:0px;border:0px;background-position:top">
	<form name='ifrmMain' id='ifrmMain'  method="post" >
	<div style="padding:5px">
		<div style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE;">
<table class="ke-zeroborder" border="0" cellpadding="0"
									cellspacing="0" width="100%">
									<tbody>
										<tr id="itemTr_1916">
											<td class="td_title" id="title_FILETEXT" width="180">对比文件</td>
											<td class="td_data" id="data_FILETEXT" colspan="3">
												<div id="DIVFILETEXT">
													<div>
														<input type="hidden" name="FILETEXT" class="valid"
															id="FILETEXT" size="100">
													</div>
													<div>
														<button
															onclick="showUploadifyPageFILETEXT();return false;">附件上传</button>
													</div>
													<s:property value="count" escapeHtml="false" />
												</div> <script>
																function showUploadifyPageFILETEXT(){
																	uploadifyDialog('FILETEXT','FILETEXT','DIVFILETEXT','','true','','');
																}
															</script>
											</td>
										</tr>
									</tbody>
								</table>
</div>
	</div>
	</form>
			<div id="fileCompareDiv">
			
			</div>
</body>
</html>