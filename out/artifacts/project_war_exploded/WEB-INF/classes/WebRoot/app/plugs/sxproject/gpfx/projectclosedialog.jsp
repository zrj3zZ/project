<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>股票发行项目关闭</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/easyui/themes/gray/easyui.css"/>
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/iwork/oaknow.css"/>
	<script type="text/javascript" src="iwork_js/commons.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.src.js"> </script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true"  ></script>
	<script type="text/javascript" src="iwork_js/engine/iformpage.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
		var api = art.dialog.open.api, W = api.opener; 
		var mainFormValidator;
		$().ready(function() {
			mainFormValidator =  $("#editForm").validate({});
			mainFormValidator.resetForm();
		});
		function pageClose() {
			if (typeof (api) == "undefined") {
				window.close();
			} else {
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
	<div region="north" style="height:40px;width:40%;" border="false">
		<div class="tools_nav">
			<table width="100%">
				<tr>
					<td align="left">
						<a class="easyui-linkbutton" plain="true" icon="icon-save" href="javascript:doSubmit();">保存</a>
						<a class="easyui-linkbutton" plain="true" iconCls="icon-reload" href="javascript:this.location.reload();">刷新</a>
						<a class="easyui-linkbutton" plain="true" iconCls="icon-cancel" href="javascript:pageClose();">关闭</a>
					</td>
					<td style="text-align:right;padding-right:10px"></td>
				</tr>
			</table>
		</div>
	</div>
	<div region="center" style="width:40%;text-align:center;border-left:1px #999 dotted;border-right:1px #999 dotted;border-top:1px #999 dotted;border-bottom:0px #999 dotted;padding:2px;">
		<s:form id="editForm" name="editForm" theme="simple">
			<!--表单参数-->
			<div id="border" style="width:600px;height:400px;">
				<table style="margin-bottom:5px;" class="ke-zeroborder" border="0" cellpadding="0" cellspacing="0" width="100%">
					<tbody>
						<tr>
							<td class="formpage_title">股票发行项目关闭</td>
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
										<tr id="itemTr_1913">
											<td class="td_title" id="title_TZBT" width="180">*项目名称</td>
											<td class="td_data" id="data_TZBT">
												<label>${commonstr}</label>
											</td>
										</tr>
										<tr id="itemTr_1914">
											<td class="td_title" id="title_ZCHFSJ" width="180">*关闭原因</td>
											<td class="td_data" id="data_ZCHFSJ">
												<s:textarea rows="6" cols="62" id="JSYY" name="JSYY" cssClass="{maxlength:3000,required:true,string:true}"></s:textarea>
												<font style="color:red">*</font>
											</td>
										</tr>
										<tr id="itemTr_1915">
											<td class="td_title" id="title_TZNR" width="180">备注</td>
											<td class="td_data" id="data_TZNR">
												<s:textarea rows="5" cols="62" id="MEMO" name="MEMO" cssClass="{maxlength:3000,string:true}"></s:textarea>
											</td>
										</tr>
									</tbody>
								</table>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<input type="hidden" id="INSTANCEIDS" name="instanceids" value="${instanceids}" class={string:true} />
		</s:form>
	</div>
</body>
</html>
<script language="JavaScript">
function doSubmit(){
	/* alert(1);
	var valid = mainFormValidator.form(); //执行校验操作
	alert(2);
	if(!valid){
		return false;
	} */
	var pageUrl="zqb_gpfxproject_sx_closeitem.action";
	var jsyy=document.getElementById("JSYY").value;
	var memo=document.getElementById("MEMO").value;
	var instanceids=document.getElementById("INSTANCEIDS").value;
	$.post(pageUrl,{jsyy:jsyy,memo:memo,instanceids:instanceids},function(data){
		if(data=='success'){
			art.dialog.tips("关闭成功",2);
			setTimeout('api.zindex().focus()', 500);
			pageClose();
		}else{
			art.dialog.tips("关闭失败",2);
		}
	});
}
jQuery.validator.addMethod("string", function(value, element) {
	var sqlstr=[" and "," exec ", " count ", " chr ", " mid ", " master ", " or ", " truncate ", " char ", " declare ", " join ","insert ", "select ", "delete ", "update ","create ","drop "]
	var patrn=/[`~!#$%^&*+<>?"{};'[\]]/im;
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