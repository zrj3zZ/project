<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>债券时间区间</title>
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
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true"></script>
<script type="text/javascript" src="iwork_js/engine/iformpage.js" charset="utf-8"></script>
<script type="text/javascript">
	var api = frameElement.api, W = api.opener;
	function doSubmit() {
		var startdate = $("#STARTDATE").val();
		var enddate = $("#ENDDATE").val();
		var pageUrl = "zqb_bond_expzq.action?startdate="+startdate+"&enddate="+enddate;
		window.location.href = pageUrl
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
						<a href="javascript:pageClose();" class="easyui-linkbutton" plain="true" iconCls="icon-cancel">关闭</a></td>
					<td style="text-align:right;padding-right:10px"></td>
				</tr>
			</table>
		</div>
	</div>
	<div region="center"
		style="text-align:center;border-left:1px #999 dotted;border-right:1px #999 dotted;border-top:1px #999 dotted;border-bottom:0px #999 dotted;padding:2px;">
		<s:form id="editForm" name="editForm" theme="simple">
			<!--表单参数-->
			<div id="border">
				<table style="margin-bottom:5px;" class="ke-zeroborder" border="0"
					cellpadding="0" cellspacing="0" width="100%">
					<tbody>
						<tr>
							<td class="formpage_title">付息时间区间</td>
						</tr>
						<tr>
							<td id="help" align="right"><br /></td>
						</tr>
						<tr>
							<td class="line" align="right"><br /></td>
						</tr>
						<tr id="itemTr_0">
							<td class="td_title" id="title_CREATEUSER" width="20%">
								<table class="ke-zeroborder" width="100%" cellspacing="0" cellpadding="0" border="0">
									<tbody>
										<tr id="itemTr_1913">
											<td id="title_TZBT" class="td_title">付息时间</td>
											<td class="td_data" id="data_CREATEUSER" width="80%">
												<span class="td_title">从</span>
												<input id="STARTDATE" type="text" name="STARTDATE" style="width:200px" onfocus="WdatePicker()">
												<span class="td_title">到</span>
												<input id="ENDDATE" type="text" name="ENDDATE" style="width:200px" onfocus="WdatePicker()">
											</td>
										</tr>
									</tbody>
								</table>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</s:form>
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