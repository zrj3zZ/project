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
	
	function doSubmit(){

		if($("#CUSTOMERNAME").val()==''||$("#CUSTOMERNO").val()==''||$("#SQRQ").val()==''||$("#NSRLX").val()==''){
			art.dialog.tips("请添加必填信息!",2);
			return;
		}
		
		
		var CUSTOMERNAME=document.getElementById("CUSTOMERNAME").value;
		var CUSTOMERNO=document.getElementById("CUSTOMERNO").value;
		var SQRQ=document.getElementById("SQRQ").value;
		var NSRLX=document.getElementById("NSRLX").value;
		var MEMO=document.getElementById("MEMO").value;
	
		 var value="";
		 var radio=document.getElementsByName("STATE");
		 for(var i=0;i<radio.length;i++){
			if(radio[i].checked==true){
			  value=radio[i].value;
			  break;
			}
		 }
		var ggid=$("#ggid").val();
		
		
	//	zqb_announcement_save
		 	var pageUrl="zqb_updsave_index.action";
		 	$.post(pageUrl,{khid:ggid,customername:CUSTOMERNAME,customerno:CUSTOMERNO,state:value,nsrlx:NSRLX,sqrq:SQRQ,memo:MEMO},function(data){
				
		       
				if(data!='error'){
					$("#ggid").val(ggid);
					art.dialog.tips("保存成功",2);
					setTimeout('api.zindex().focus()', 500);
					window.location.href="zqb_upd_index.action?khid="+ggid;
				}else{
					art.dialog.tips("保存失败",2);
					$("#iframe").reload();
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
					<s:if test="fszt!='完成'">
					<a id="btnEpForSave" class="easyui-linkbutton" icon="icon-save" plain="true" href="javascript:void(0);" onclick="doSubmit();">保存</a>
					</s:if>
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
			<div id="border">
				<table style="margin-bottom:5px;" class="ke-zeroborder" border="0"
					cellpadding="0" cellspacing="0" width="100%">
					<tbody>
						<tr>
							<td class="formpage_title">工作底稿归档</td>
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
										<tr id="itemTr_2745">
								<td class="td_title" id="title_CUSTOMERNAME" width="180">
									<span style="color:red;"></span>项目名称
								</td>
								<td class="td_data" id="data_CUSTOMERNAME">
									<input type='text' class = '{maxlength:64,required:true,string:true}'  style="width:300px;" name='CUSTOMERNAME' id='CUSTOMERNAME'  value="${invoiceKpList[0].customername}"  ><font style="color:red;padding-left:5px">*</font>&nbsp;
								</td>
							</tr>
							<tr id="itemTr_2746">
								<td class="td_title" id="title_CUSTOMERNO" width="180">
									归档人员
								</td>
								<td class="td_data" id="data_CUSTOMERNO">
									<input type='text' class = '{maxlength:64,required:true,string:true}'  style="width:300px;" name='CUSTOMERNO' id='CUSTOMERNO'  value='${invoiceKpList[0].customerno}'   form-type='al_textbox'><font style="color:red;padding-left:5px">*</font>&nbsp;
								</td>
							</tr>
							<tr id="itemTr_2752">
								<td class="td_title" id="title_STATE" width="180">
									<span style="color:red;"></span>归档状态
								</td>
								<td class="td_data" id="data_STATE">
									<input type=radio  id='state0' name='STATE' <c:if test="${invoiceKpList[0].state=='已归档'}"> checked="checked"</c:if> value='已归档'  class='required'><label  id="lbl_STATE0" for="STATE0">已归档</label>&nbsp;
									<input type=radio  id='STATE1' name='STATE' <c:if test="${invoiceKpList[0].state=='延期归档'}"> checked="checked"</c:if> value='延期归档'   class='required'><label  id="lbl_STATE1" for="STATE1">延期归档</label>&nbsp;
									<input type=radio   id='STATE2' name='STATE' <c:if test="${invoiceKpList[0].state=='未归档'}"> checked="checked"</c:if> value='未归档' ><label  id="lbl_STATE2"  for="STATE2">未归档</label>&nbsp;
								<font style='color:red'>*</font>&nbsp;
								</td>
							</tr>
							<tr id="itemTr_2750">
								<td class="td_title" id="title_SQRQ" width="180">
									<span style="color:red;"></span>归档时间
								</td>
								<td class="td_data" id="data_SQRQ">
									<input type='text' onfocus="WdatePicker()" class = "{required:true}"  style="width:300px" name='SQRQ' id='SQRQ'  value='${invoiceKpList[0].sqrq}' ><font color=red>*</font>&nbsp;
								</td>
							</tr>
							<tr id="itemTr_2775">
								<td class="td_title" id="title_NSRLX" width="180">
									<span style="color:red;"></span>归档位置
								</td>
								<td class="td_data" id="data_NSRLX">
									<textarea  class="{maxlength:128,required:true} "  name='NSRLX' id='NSRLX'  style="width:280px;height:30px;"  >${invoiceKpList[0].nsrlx}</textarea><font color=red>*</font>&nbsp;
								</td>
							</tr>
							<tr id="itemTr_2751">
								<td class="td_title" id="title_MEMO" width="180">
									归档内容
								</td>
								<td class="td_data" id="data_MEMO">
									<textarea  class="{maxlength:512,required:false} "  name='MEMO' id='MEMO'  style="width:300px;height:50px;"  >${invoiceKpList[0].memo}</textarea>&nbsp;
								</td>
							</tr>

									</tbody>
								</table>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<input type="hidden" id="ggid" name="ggid" value="${invoiceKpList[0].id}" class="{string:true}"/>
			<input type="hidden" id="demId" name="demId" value="${demId}"/>
			<input type="hidden" id="formid" name="formid" value="${formid}"/>
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