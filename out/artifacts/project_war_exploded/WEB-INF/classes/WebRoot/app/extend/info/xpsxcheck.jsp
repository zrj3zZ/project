<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>信披事项</title>
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
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
var api,W;
try{
	api=  art.dialog.open.api;
	W = api.opener;	
}catch(e){}
var mainFormValidator;
$().ready(function() {
	mainFormValidator =  $("#editForm").validate({
});
mainFormValidator.resetForm();
});
$(function() {
	$("#txrq").html(getNowFormatDate());
});
function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate + " " + date.getHours() + seperator2 + date.getMinutes() + seperator2 + date.getSeconds();
    return currentdate;
}
function doSubmit(){
	var valid = mainFormValidator.form(); //执行校验操作
	if(!valid){
		return false;
	}
	var xpsxname = document.getElementById("xpsxname").value;
	var txrq = $("#txrq").html();
	var id=$("#id").val();
	 	var pageUrl="zqb_xpsxt_save.action";
		$.post(pageUrl,{xpsxname:xpsxname,id:id,txrq:txrq},function(data){
			if(data!='0'){
				W.lhgdialog.tips("保存成功",2);
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
						<s:if test="xpsxname.indexOf('其他事项')!=-1">
							<a id="btnEp" class="easyui-linkbutton" icon="icon-save" plain="true" href="#" onclick="doSubmit();" >保存</a>
						</s:if>
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
							<td class="formpage_title">信披事项表单</td>
						</tr>
						<tr>
							<td id="help" align="right"><br /></td>
						</tr>
						<tr>
							<td class="line" align="right"><br /></td>
						</tr>
						<tr>
							<td>
								<table align="center" class="ke-zeroborder" border="0" cellpadding="0" cellspacing="0" width="100%">
									<tbody>
										<%-- <tr id="itemTr_1913">
											<td class="td_title" id="title_SXMC" width="180">事项类型</td>
											<td class="td_data" id="data_SXMC">${sxmc}
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;最后修改人:${username}
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="txrq"></span></td>
										</tr> --%>
										<tr id="itemTr_1914">
											<td class="td_title" id="title_XPSXNAME" width="180">事项名称</td>
											<td class="td_data" id="data_XPSXNAME">
											${sxmc}
											<%-- <s:if test="xpsxname.indexOf('其他事项')!=-1">
											<input type="text" id="xpsxname" name="xpsxname" style="width:500px;height:22px;" class="{maxlength:256,required:true,string:true}" value="${xpsxname}"/>
											<font style="color:red">*</font>
											</s:if>
											<s:else>
											${xpsxname}
											</s:else> --%>
											</td>
										</tr>
										<tr id="itemTr_1915">
											<td class="td_title" id="title_SYGZ" width="180px">适用规则</td>
											<td class="td_data" id="data_SYGZ">${sygz}</td>
										</tr>
										<tr id="itemTr_1916">
											<td class="td_title" id="title_PLYQ" width="180">披露要求</td>
											<td class="td_data" id="data_PLYQ">${plyq}</td>
										</tr>
										<tr id="itemTr_1917">
											<td class="td_title" id="title_BZNR" width="180">办事流程</td>
											<td class="td_data" id="data_BZNR">${bznr}</td>
										</tr>
										<tr id="itemTr_1918">
											<td class="td_title" id="title_NR" width="180">内容</td>
											<td class="td_data" id="data_NR">${bnr}</td>
										</tr>
										<tr id="itemTr_1919">
											<td class="td_title" id="title_BC" width="180">备查</td>
											<td class="td_data" id="data_BC">${bbc}</td>
										</tr>
									</tbody>
								</table>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<input type="hidden" id="id" name="id" value="${id}" class="{string:true}"/>
		</s:form>
	</div>
</body>
</html>
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