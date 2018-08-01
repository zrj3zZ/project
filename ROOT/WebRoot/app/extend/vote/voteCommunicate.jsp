<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>信披自查反馈</title>
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
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"   charset="utf-8" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.src.js"  charset="utf-8"  > </script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"  charset="utf-8"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"  charset="utf-8" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"  charset="utf-8"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true"  ></script>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript" src="iwork_js/engine/iformpage.js"  charset="utf-8" ></script>
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
<script type="text/javascript">
var mainFormValidator;
$().ready(function() {
		mainFormValidator =  $("#editForm").validate({
		 });
		 mainFormValidator.resetForm();
	});
function doSubmit(){
	var valid = mainFormValidator.form(); //执行校验操作
	/* if(!valid){
		return false;
	} */
	var content=document.getElementById("content").value;
	var xpgtFile=document.getElementById("XPGTFILE").value;
	var customerno=document.getElementById("customerno").value;
	var ggid=document.getElementById("ggid").value;
	var gtid=document.getElementById("gtid").value;
	var pageUrl="zqb_vote_communicate_save.action";
	$.post(pageUrl,{content:content,ggid:ggid,customerno:customerno,gtid:gtid,xpgtFile:xpgtFile},function(data){
		if(data!='error'){
			art.dialog.tips("保存成功!",10);
			setTimeout('api.zindex().focus()', 500);
			pageClose();
		}
  		});
	}
</script>
<script>
	function showUploadifyPageXPGTFILE(){
		mainFormAlertFlag=false;
		saveSubReportFlag=false;
		var valid = mainFormValidator.form();
		if(!valid){
			return false;
		}
		mainFormAlertFlag=false;
		saveSubReportFlag=false;
		uploadifyDialog('XPGTFILE','XPGTFILE','DIVXPGTFILE','','true','','');
	}
	function uploadifyDialog(dialogId,fieldName,divId,sizeLimit,multi,fileExt,fileDesc){
		if(dialogId==null||dialogId==""||fieldName==null||fieldName==""||divId==null||divId==""){
			alert('参数不正确');
			return ;
		}
		var url = 'showUploadifyPage.action?parentColId='+fieldName+'&parentDivId='+divId+'&sizeLimit='+sizeLimit+'&multi='+multi+'&fileExt='+fileExt+'&fileDesc='+fileDesc+'';
		art.dialog.open(url,{
			id:dialogId,
			title: '上传附件',
			pading: 0,
			lock: true,
			width: 650,
			height: 500, 
			close:function(){
				this.focus();
			}
		}); 
		return ;
	}
</script>
</head>
<body class="easyui-layout">
	<div region="north" style="height:40px;" border="false">
		<div class="tools_nav">
			<table width="100%">
				<tr>
					<td align="left">
					<a id="btnEp" class="easyui-linkbutton" icon="icon-save" plain="true" href="#" onclick="doSubmit();" >保存</a>
					<a href="javascript:this.location.reload();"
						class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
						<a href="javascript:pageClose();" class="easyui-linkbutton"
						plain="true" iconCls="icon-cancel">关闭</a></td>
					<td style="text-align:right;padding-right:10px"></td>
				</tr>
			</table>
		</div>
	</div>
	<div region="center"
		style="text-align:center;border-left:1px #999 dotted;border-right:1px #999 dotted;border-top:1px #999 dotted;border-bottom:0px #999 dotted;padding:2px;">
		<s:form id="editForm" name="editForm" theme="simple">
			<!--表单参数-->
			<div>
				<table border="0" cellpadding="0" cellspacing="0" style="margin-bottom:5px;" width="100%">
					<tbody>
						<tr>
							<td align="left">
								<table border="0" cellpadding="0" cellspacing="0" width="100%" class="ke-zeroborder">
									<tbody>
										<tr id="itemTr_2261">
											<td class="td_title" id="title_CONTENT" width="180">
												沟通情况
											</td>
											<td class="td_data" id="data_CONTENT">
												<%-- ${CONTENT} --%>
												<textarea class='{maxlength:256}' type="text" id="content" name="content" value="${content}" style="width:600px;height:80px;">${content}</textarea>
											</td>
										</tr>
										<tr id="itemTr_2262">
											<td class="td_title" id="title_FJSC" width="180">
												确认盖章扫描件
											</td>
											<td class="td_data" id="data_FJSC">
												<%-- ${CONTENT} --%>
												<div id="DIVXPGTFILE">
												<button onclick="showUploadifyPageXPGTFILE();return false;">附件上传</button>
												<input type="hidden" id="XPGTFILE"  name="XPGTFILE" value="${xpgtFile}" style="width:200px;height:22px;"/>&nbsp;
												</div>
											</td>
										</tr>
										<tr id="itemTr_2263">
											<td class="td_title" id="title_FJ" width="180">
											</td>
											<td class="td_data" id="data_FJ">
												${filecontent}
											</td>
										</tr>
									</tbody>
								</table>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<%-- <div style="display:none;">
				${USERNAME}${CUSTOMERNO}${STATUS}${GGID}${DATA}
			</div> --%>
			<s:hidden id="customerno" name="customerno"></s:hidden>
			<s:hidden id="gtid" name="gtid"></s:hidden>
			<input type="hidden" id="ggid" name="ggid" value="${ggid}" class="{string:true}"/>
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