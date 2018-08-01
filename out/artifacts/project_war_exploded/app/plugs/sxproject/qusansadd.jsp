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
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/easyui/themes/gray/easyui.css"/>
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css" />
	<script type="text/javascript" src="iwork_js/commons.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.src.js"> </script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>	
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
	<script type="text/javascript">
	var api,W;
	try{
		api=  art.dialog.open.api;
		W = api.opener;	
	}catch(e){} 
	var mainFormValidator;
	$().ready(function(){
		mainFormValidator = $("#editForm").validate({});
		mainFormValidator.resetForm();
	});
	function doSubmit(){
		var valid = mainFormValidator.form(); //执行校验操作
		if(!valid){
			return false;
		}
		var question = document.getElementById("question").value;
		var content = document.getElementById("content").value;
		var date = document.getElementById("date").value;
		
		var qins = document.getElementById("qins").value;
		var ains = document.getElementById("ains").value;
		if(qins!=null&&qins!=""&&ains!=null&&ains!=""){
			var pageUrl="zqb_project_sx_qusansupdate.action";
			$.post(pageUrl,{date:date,question:question,content:content,qins:qins,ains:ains},function(data){
				if(data!='error'){
					$("#btnEp").attr("onclick","");
					art.dialog.tips("保存成功",2);
					pageClose();
				}else{
					art.dialog.tips("保存失败",2);
				}
			});
		}else{
			var projectno = document.getElementById("projectno").value;
			var pageUrl="zqb_project_sx_qusanssave.action";
			$.post(pageUrl,{date:date,question:question,content:content,projectno:projectno},function(data){
				if(data!='error'){
					$("#btnEp").attr("onclick","");
					art.dialog.tips("保存成功",2);
					pageClose();
				}else{
					art.dialog.tips("保存失败",2);
				}
			});
		}
	}
	function pageClose(){ 
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
	<div region="north" style="height:40px;" border="false">
		<div class="tools_nav">
			<table width="100%">
				<tr>
					<td align="left">
						<a id="btnEp" class="easyui-linkbutton" icon="icon-save" plain="true" href="#" onclick="doSubmit();" >保存</a>
						<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
						<a href="javascript:pageClose();" class="easyui-linkbutton" plain="true" iconCls="icon-cancel">关闭</a>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div region="center" style="text-align:center;border-left:1px #999 dotted;border-right:1px #999 dotted;border-top:1px #999 dotted;border-bottom:0px #999 dotted;padding:2px;">
		<s:form id="editForm" name="editForm" theme="simple">
			<!--表单参数-->
			<div id="border" style="width:90%;">
				<table style="margin-bottom:5px;" class="ke-zeroborder" border="0" cellpadding="0" cellspacing="0" width="100%">
					<tbody>
						<tr>
							<td class="formpage_title">异常情况及解决方案</td>
						</tr>
						<tr>
							<td id="help" align="right"></td>
						</tr>
						<tr>
							<td class="line" align="right"></td>
						</tr>
						<tr>
							<td align="center">
								<table class="ke-zeroborder" border="0" cellpadding="0"
									cellspacing="0" width="100%">
									<tbody>
										<tr>
											<td class="td_title" width="80">日期</td>
											<td class="td_data">
											<s:textfield rows="8" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" cols="78" id="date" name="date" cssClass="{maxlength:512,required:true,string:true}"></s:textfield>
												<font style="color:red">*</font>
											</td>
										</tr>
										<tr>
											<td class="td_title" width="80">事项描述</td>
											<td class="td_data">
											<s:textarea rows="8" cols="78" id="question" name="question" cssClass="{maxlength:512,required:true,string:true}"></s:textarea>
												<font style="color:red">*</font>
											</td>
										</tr>
										<tr>
											<td class="td_title" width="80">处理结果</td>
											<td class="td_data">
											<s:textarea rows="8" cols="78" id="content" name="content" cssClass="{maxlength:1024,required:true,string:true}"></s:textarea>
												<font style="color:red">*</font>
											</td>
										</tr>
									</tbody>
								</table>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<s:hidden name="projectno" id="projectno"></s:hidden>
			<s:hidden name="qins" id="qins"></s:hidden>
			<s:hidden name="ains" id="ains"></s:hidden>
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