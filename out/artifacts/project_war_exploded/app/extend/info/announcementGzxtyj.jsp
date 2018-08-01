<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>股转系统意见</title>
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
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
var mainFormValidator;
$().ready(function() {
	mainFormValidator =  $("#editForm").validate({});
	mainFormValidator.resetForm();
});
	$(function() {});
	function GZYJHF() {
		var gzxtyjhfdemid =$("#gzxtyjhfdemid").val();
		var gzxtyjhfformid =$("#gzxtyjhfformid").val();
		var khbh =$("#khbh").val();
		var gzyjid =$("#gzyjid").val();
		var pageUrl ="createFormInstance.action?formid="+gzxtyjhfformid+"&demId="+gzxtyjhfdemid+"&GZYJID="+gzyjid+"&KHBH="+khbh;
		var target = "_blank";
		var win_width = window.screen.width/2;
		var win_height = window.screen.height/2;
		var page = window.open('form/loader_frame.html',target,'width='+ win_width+ ',height='+ win_height+ ',top='+ win_height/2+ ',left='+ win_width/2+ ',location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
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
.hfdiv{
	float:right;
	font-size:16px;
	color:white;
	font-style:
	inherit;
	cursor:pointer;
	font-weight:bold;
	padding:4px;
	background-color:#c0c0c0;
	border-radius:3px;
}
.hfdiv:hover{
	background-color:gray;
}
</style>
</head>
<body class="easyui-layout">
	<div region="north" style="height:40px;" border="false">
		<div class="tools_nav">
			<table width="100%">
				<tr>
					<td align="left">
					<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
					<a href="javascript:pageClose();" class="easyui-linkbutton" plain="true" iconCls="icon-cancel">关闭</a></td>
					<td style="text-align:right;padding-right:10px"></td>
				</tr>
			</table>
		</div>
	</div>
	<div region="center" style="text-align:center;border-left:1px #999 dotted;border-right:1px #999 dotted;border-top:1px #999 dotted;border-bottom:0px #999 dotted;padding:2px;">
		<s:form id="editForm" name="editForm" theme="simple">
			<!--表单参数-->
			<div id="border">
				<table style="margin-bottom:5px;" class="ke-zeroborder" border="0"
					cellpadding="0" cellspacing="0" width="100%">
					<tbody>
						<tr>
							<td class="formpage_title">股转系统意见</td>
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
											<td class="td_title" id="title_GZYJLRR" width="180">录入人</td>
											<td class="td_data" id="data_GZYJLRR">${gzyjlrr}
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${gzyjlrsj}</td>
										</tr>
										<tr id="itemTr_1915">
											<td class="td_title" id="title_GZYJJSM" width="180">股转意见及说明</td>
											<td class="td_data" id="data_GZYJJSM"><pre>${gzyjjsm}</pre></td>
										</tr>
										<tr id="itemTr_1917">
											<td class="td_title" id="title_HFR" width="180">短信邮件通知人</td>
											<td class="td_data" id="data_HFR">
												<div id="DXYJTZRDIV">${dxyjtzrsb}</div>
											</td>
										</tr>
										<tr id="itemTr_1916">
											<td class="td_title" id="title_XGZL" width="180">说明文档</td>
											<td class="td_data" id="data_XGZL" >
												${gzyjsmwd}
											</td>
										</tr>
									</tbody>
								</table>
								<s:iterator value="gzxtyjlist" status="status">
									<table class="ke-zeroborder" border="0" cellpadding="0"
										cellspacing="0" width="100%">
										<tbody>
											<tr id="itemTr_1913">
												<td class="td_title" style="height:25px;color:red;font-size:16px;font-weight:bold;" width="180">第<s:property value="#status.count" />条回复信息</td>
												<td class="td_data" style="height:25px;"></td>
											</tr>
											<tr id="itemTr_1913">
												<td class="td_title" id="title_GZYJLRR" width="180">回复人</td>
												<td class="td_data" id="data_GZYJLRR"><s:property value="GZYJLRR" />
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<s:property value="GZYJLRSJ" /></td>
											</tr>
											<tr id="itemTr_1915">
												<td class="td_title" id="title_GZYJJSM" width="180">回复信息</td>
												<td class="td_data" id="data_GZYJJSM"><pre><s:property value="GZYJJSM" /></pre></td>
											</tr>
											<tr id="itemTr_1916">
												<td class="td_title" id="title_XGZL" width="180">回复资料</td>
												<td class="td_data" id="data_XGZL" >
													<s:property value="GZYJSMWD" escapeHtml="false" />
												</td>
											</tr>
											<tr id="itemTr_1917">
												<td class="td_title" id="title_HFR" width="180">短信邮件通知人</td>
												<td class="td_data" id="data_HFR">
													<div id="DXYJTZRDIV"><s:property value="DXYJTZRSB" escapeHtml="false"/></div>
												</td>
											</tr>
										</tbody>
									</table>
								</s:iterator>
								<tr id="itemTr_1919">
									<td class="td_data" id="data_HF" >
										<div class="hfdiv" onclick="GZYJHF();">回复</div>
									</td>
								</tr>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<input type="hidden" id="gzyjid" name="gzyjid" value="${gzyjid}" class="{string:true}"/>
			<input type="hidden" id="dxyjtzr" name="dxyjtzr" value="${dxyjtzr}" class="{string:true}"/>
			<input type="hidden" id="ggid" name="ggid" value="${ggid}" class="{string:true}"/>
			<input type="hidden" id="khbh" name="khbh" value="${khbh}" class="{string:true}"/>
			<input type="hidden" id="gzyjlrrid" name="gzyjlrrid" value="${gzyjlrrid}" class="{string:true}"/>
			<input type="hidden" id="gzxtyjhfdemid" name="gzxtyjhfdemid" value="${gzxtyjhfdemid}" class="{string:true}"/>
			<input type="hidden" id="gzxtyjhfformid" name="gzxtyjhfformid" value="${gzxtyjhfformid}" class="{string:true}"/>
		</s:form>
	</div>
</body>
</html>
<script language="JavaScript"> 
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