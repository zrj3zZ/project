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
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css" />
<link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/easyui/themes/gray/easyui.css" />
<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/iwork/oaknow.css" />
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
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
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"   charset="utf-8" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"  charset="utf-8"  > </script>



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
	
	function doSubmit() {
		
		var ksrq=document.getElementById("ksrq").value;
		if(ksrq==""){
			alert("开始日期不能为空！");
			return;
		}
		var jzrq=document.getElementById("jzrq").value;
		if(jzrq==""){
			alert("截止日期不能为空！");
			return;
		}
		//判断结束日期大于等于开始日期
		$.post("checkdate.action", { startdate: ksrq,enddate:jzrq }, function (data) {
			if(data=='error'){
				alert("开始日期或截止日期不能为空！")
			}else if(data=='error1'){
				alert("开始日期不能大于截止日期！")
			}else{
				var options = {
						error:showRequest,
						success:showResponse 
					};
				setHref("btnEp");//移除onclick事件
				setTimeout("setQhref('btnEp')",5000);//onclick事件重置
				$("#editForm").ajaxSubmit(options);
				
				return false;
			}
		
		});
		
	}
	function showRequest(formData, jqForm, options) {
		alert("保存失败!")
		setTimeout("setQhref('btnEp')",500);//onclick事件重置
		 return true; 
	}
	function showResponse(responseText, statusText, xhr, $form)  { 
		alert("保存成功！");
		closees() 
		}
	
	function getAppointmentNum(){
		var yysxid=$("#editForm_appointmentNum_yysx").val();
		var seachUrl = encodeURI("appointmentSetNum.action?yysx="+yysxid);
        location.replace = seachUrl;
	}
	function closees(){
		api.close(); 
		window.close();
	}

	function setHref(idName){
		$("#"+idName).removeAttr("onclick");
	}

	function setQhref(idName){
		$("#"+idName).attr("onclick","doSubmit();");
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
					<td align="left"><a id="btnEp" class="easyui-linkbutton"
						icon="icon-save" plain="true" href="#" onclick="doSubmit();">保存</a>
						<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload" id="reloadButton">刷新</a>
					<td style="text-align:right;padding-right:10px"></td>
				</tr>
			</table>
		</div>
	</div>
	<div region="center"
		style="text-align:center;border-left:1px #999 dotted;border-right:1px #999 dotted;border-top:1px #999 dotted;border-bottom:0px #999 dotted;padding:2px;">
		<s:form id="editForm" name="editForm" theme="simple" action="saveAppointmentNum.action">
			<!--表单参数-->
			<!-- sxmsList -->
			<div>
				<table style="margin-bottom:5px;" class="ke-zeroborder" border="0"
					cellpadding="0" cellspacing="0" width="100%">
					<tbody>
						<tr>
							<td class="td_title" id="title_SXMC" width="180">事项名称</td>
							<td class="td_data" id="data_SXMC">
								<s:textfield id="sxms" name="appointmentYysx.sxms" cssStyle="width:180px;" cssClass="{maxlength:200,required:true,string:true}" theme="simple"></s:textfield>
							</td>
						</tr>
						
					<%-- 	<tr >
				<td align="right" width="100%">邀请回答人：</td>
				<td colspan="3" align="left">
					<table cellspacing="0" cellpadding="0">
						<tr valign="middle">
							<td><s:textfield name="question.answerbody"
									onkeyup="getuserbycode(this);" cssClass="actionsoftInput "
									cssStyle="width:360px;"
									onblur="dropFocus(this,'多个被邀请回答人请用逗号分隔');"
									onfocus="getFocus(this,'多个被邀请回答人请用逗号分隔');" /></td>
							<td>&nbsp;&nbsp;</td>
							<td><input type="button" value='地址簿' name='ANSWERBODY_Btn'
								class='Btn_s_a'
								onclick="multi_book('editForm_question_answerbody');" border='0'></td>
						</tr>
					</table>
				</td>
			</tr> --%>
						<tr>
							<td class="td_title" id="title_yydd" width="180">预约督导</td>
							<td class="td_data" id="data_yydd">
								<s:textfield id="yydd" name="appointmentYysx.yydd" cssStyle="width:180px;" cssClass="{maxlength:200,required:true}" theme="simple"></s:textfield>
							</td>
						</tr>
						
						<tr>
							<td class="td_title" id="title_ZCHFSJ" width="180">开始日期</td>
							<td class="td_data" id="ksrqd">
							<%-- <s:textfield id="ksrq" name="appointmentYysx.ksrq" cssStyle="width:160px;" onfocus="WdatePicker()"></s:textfield> --%>
									<input id="ksrq" type="text" value="<s:date name="appointmentYysx.ksrq" format="yyyy-MM-dd" />" name="appointmentYysx.ksrq" cssStyle="width:160px;" onfocus="WdatePicker()" />
							</td>
						</tr>
						<tr>
							<td class="td_title" id="title_ZCHFSJ" width="180">截止日期</td>
							<td class="td_data" id="jzrqd">
							<input id="jzrq" type="text" value="<s:date name="appointmentYysx.jzrq" format="yyyy-MM-dd" />" name="appointmentYysx.jzrq" cssStyle="width:160px;" onfocus="WdatePicker()" />
							</td>
						</tr>
						<tr>
							<td class="td_title" id="title_ZCHFSJ" width="180">开始预约时间</td>
							<td class="td_data">
							<input id="yysj" type="text" value="<s:property value='appointmentYysx.yysj'/>" name="appointmentYysx.yysj" cssStyle="width:160px;" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" />
							</td>
						</tr>
						<tr>
							<td class="td_title" id="title_YYS" width="180">每日最大预约数</td>
							<td class="td_data" id="data_YYS">
								<s:textfield id="szs" name="appointmentNum.szs" cssStyle="width:50px;" cssClass="{required:true,string:true}" theme="simple"></s:textfield>
							</td>
						</tr>
						<tr id="itemTr_1918">
							<td class="td_title" id="title_SFTZ" width="180">是否短信通知</td>
							<td class="td_data" id="data_SFTZ">
											<s:radio list="#{'1':'是','2':'否'}" name="sftz" value='2' /></td>
						</tr>
					</tbody>
				</table>
			</div>
			<s:hidden name="appointmentNum.szr"></s:hidden>
			<s:hidden name="appointmentNum.id"></s:hidden>
			<s:hidden name="appointmentYysx.id"></s:hidden>
			<s:hidden name="appointmentNum.yysx"></s:hidden>
			<s:hidden name="appointmentYysx.cjr"></s:hidden>
		</s:form>
	</div>
</body>
</html>
<!-- 新增查询过滤SQL注入关键字 -->
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