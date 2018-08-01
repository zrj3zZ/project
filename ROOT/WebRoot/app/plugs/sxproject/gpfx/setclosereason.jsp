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
	<script type="text/javascript">
		var api = frameElement.api, W = api.opener; 
		var mainFormValidator;
		$().ready(function() {
			mainFormValidator =  $("#editForm").validate({});
			mainFormValidator.resetForm();
		});
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
	.formpage_title {
		text-align: center;
		vertical-align:bottom;
		height:50px;
		font-size: 22px;
		color:#000;
		margin-bottom:20px;
	}
	.line {
		background-color: #ccc;
		height:15px;
		margin-bottom:20px;
	}
	td {
		line-height: 30px;
		padding-left: 3px;
		font-size: 12px;
		border-bottom:1px #efefef dotted;
		vertical-align:middle;
		word-wrap:break-word;
		word-break:break-all;
		font-weight:100;
		line-height:15px;
		padding-top:5px;
		text-align:left;
		
	}
	legend {
		font-size: 16px;
	}
	label {
		color: blue;
	}
	</style>
</head>
<body>
	<div region="north" style="height:40px;" border="false">
		<div class="tools_nav">
			<span>
				<a href="javascript:void(0);" onclick="doSubmit();" class="easyui-linkbutton l-btn l-btn-plain" plain="true" iconcls="icon-save">保存</a>	
				<a href="javascript:this.location.reload();" class="easyui-linkbutton l-btn l-btn-plain" plain="true" iconcls="icon-reload">刷新</a>
				<a id="close" href="javascript:pageClose();" class="easyui-linkbutton l-btn l-btn-plain" plain="true" iconcls="icon-cancel">关闭</a>
			</span>
		</div>
	</div>
	<div region="center" style="text-align:center;border-left:1px #999 dotted;border-right:1px #999 dotted;border-top:1px #999 dotted;border-bottom:0px #999 dotted;padding:2px;">
		<div class="form-wrapper">
			<form id="editForm" name="editForm" method="post">
		<s:iterator value="closelist" status="ll">
		<div id="border">
			<table style="margin-bottom:5px;" width="100%">
				<tbody>
					<tr>
						<td class="formpage_title">
							股票发行项目信息
						</td>
					</tr>
					<tr>
						<td align="right" id="help" style="text-align:right;">
							<p style="margin-left:15px;float:right;">填报时间:：<label>${CREATEDATE}</label></p>
							<p style="margin-left:15px;float:right;">填报人:：<label>${CREATEUSER}</label>[<label>${CREATEUSERID}</label>]</p>
						</td>
					</tr>
					<tr>
						<td align="right" class="line"></td>
					</tr>
				</tbody>
			</table>
			<fieldset style="margin-top:15px;color:#004080;width:100%;border:1px dashed #CCCCCC">
				<legend algin="right" style="color:#004080;">项目基本情况</legend>
				<table width="100%">
				<tr id="itemTr_0" colspan="2">
					<td id="data_CUSTOMERNAME" width="50%" colspan="2">
						客户名称：<label>${CUSTOMERNAME}</label>
					</td>
					<td width="50%" colspan="2">
						项目名称：<label>${PROJECTNAME}</label>
					</td>
				</tr>
				<tr id="itemTr_1" colspan="2">
					<td width="50%" colspan="2">
						项目负责人：<label>${OWNER}</label>
					</td>
					<td width="50%" colspan="2">
						项目现场负责人：<label>${MANAGER}</label>
					</td>
				</tr>
				<tr id="itemTr_2" colspan="2">
					<td width="50%" colspan="2">
						客户联系人：<label>${KHLXR}</label>
					</td>
					<td width="50%" colspan="2">
						承揽部门：<label>${CLBM}</label>　
					</td>
				</tr>
				<tr id="itemTr_3" colspan="2">
					<td width="50%" colspan="2">
						客户联系电话：<label>${KHLXDH}
					</td>
					<td width="50%" colspan="2">
						承做部门：<label>${CZBM}</label>　
					</td>
				</tr>
				<tr id="itemTr_4" colspan="2">
					<td width="50%" colspan="2">
						股票发行进展：<label>${GPFXJZ}</label>
					</td>
					<td width="50%" colspan="2">
						项目发起日期：<label>${STARTDATE}</label>
					</td>
				</tr>
				</table>
			</fieldset>
			<fieldset style="margin-top:15px;color:#004080;width:100%;border:1px dashed #CCCCCC">
				<legend algin="right" style="color:#004080;">拟发行情况</legend>
				<table width="100%">
				<tr id="itemTr_5" colspan="2">
					<td width="50%" colspan="2">
						拟股票发行数量不超过（万股）：<label>${GPFXSL}</label>
					</td>
					<td width="50%" colspan="2">
						拟发行日期：<label>${NFXRQ}</label>
					</td>
				</tr>
				<tr id="itemTr_6" colspan="2">
					<td width="50%" colspan="2">
						拟募集资金总额不超过人民币（万元）：<label>${MJZJZE}</label>
					</td>
					<td width="50%" colspan="2" rowspan="2">
						发行目的阐述：<label>${FXMDCS}</label>
					</td>
				</tr>
				<tr id="itemTr_7" colspan="2">
					<td width="50%" colspan="2">
						拟发行股票的价格为人民币（元/股）：<label>${FXGPJG}</label>
					</td>
				</tr>
				</table>
			</fieldset>
			<fieldset style="margin-top:15px;color:#004080;width:100%;border:1px dashed #CCCCCC">
				<legend algin="right" style="color:#004080;">实际发行情况</legend>
				<table width="100%">
				<tr id="itemTr_8" colspan="2">
					<td width="50%" colspan="2">
						股票发行数量（万股）：<label>${SJGPFXSL}</label>
					</td>
					<td width="50%" colspan="2">
						发行日期：<label>${FXRQ}</label>&nbsp;&nbsp;&nbsp;&nbsp;市盈率：<label>${SYL}</label>
					</td>
				</tr>
				<tr id="itemTr_9" colspan="2">
					<td width="50%" colspan="2">
						发行总额人民币（元）：<label>${SJFXZE}</label>
					</td>
					<td width="50%" colspan="2">
						<label>
							<s:if test="RSSFCG!=''">
								<input type="radio" checked="checked" disabled="disabled">${RSSFCG}
							</s:if>
							<s:else>
								<input type="radio" disabled="disabled">本次股票发行完成后公司股东人数是否超过200人
							</s:else>
						</label>
					</td>
				</tr>
				<tr id="itemTr_10" colspan="2">
					<td width="50%" colspan="2">
						发行股票的价格为人民币（元/股）：<label>${SJFXGPJG}</label>
					</td>
					<td width="50%" colspan="2">
						<label>
							<s:if test="SFZYXS!=''">
								<input type="radio" checked="checked" disabled="disabled">${SFZYXS}
							</s:if>
							<s:else>
								<input type="radio" disabled="disabled">是否自愿限售 
							</s:else>
						</label>
						&nbsp;&nbsp;&nbsp;&nbsp;发行状态：<label>${FXZT}
						</label>
					</td>
				</tr>
				</table>
			</fieldset>
			<fieldset style="margin-top:15px;color:#004080;width:100%;border:1px dashed #CCCCCC">
				<legend algin="right" style="color:#004080;">定增对象</legend>
				<table style="width:838px">
					<tr class="header">
						<td role="columnheader" style="width: 388px;">
							姓名公司名
						</td>
						<td role="columnheader" style="width: 388px;">
							股东类型
						</td>
						<td role="columnheader" style="width: 388px;">
							获取渠道
						</td>
					</tr>
				</table>
				<table style="width: 838px;">
				<tbody>
					<s:iterator value="commonlist" status="var">
					<tr>
							<td style="width: 388px;"><label><s:property value="XMGSM"/></label></td>
							<td style="width: 388px;"><label><s:property value="GDLX"/></label></td>
							<td style="width: 388px;"><label><s:property value="HQQD"/></label></td>
					</tr>
					</s:iterator>
				</tbody></table>
			</fieldset>
			<fieldset style="margin-top:15px;color:#004080;width:100%;border:1px dashed #CCCCCC">
				<legend algin="right" style="color:#004080;">相关资料</legend>
				<div id="XGZL" style="float:left;align:left;">
					<label><s:property value="commonstr" escapeHtml="false" /></label>
				</div>
			</fieldset>
			<fieldset style="margin-top:15px;color:#004080;width:100%;border:1px dashed #CCCCCC">
				<legend algin="right" style="color:#004080;">关闭原因</legend>
				<table width="100%">
				<tbody><tr id="txtAreaTr_2888">
					<td id="title_JSYY">
						<textarea class="{maxlength:500,required:false,string:true}" name="JSYY" id="JSYY" style="width: 850px; height: 50px; border: 0px;">${JSYY}</textarea>
					</td>
				</tr>
				</tbody></table>
			</fieldset>
			<fieldset style="margin-top:15px;color:#004080;width:100%;border:1px dashed #CCCCCC">
				<legend algin="right" style="color:#004080;">备注</legend>
				<table width="100%">
				<tbody><tr id="txtAreaTr_2888">
					<td id="title_MEMO">
						<textarea class="{maxlength:500,required:false,string:true}" name="MEMO" id="MEMO" style="width: 850px; height: 50px; border: 0px;">${MEMO}</textarea>
					</td>
				</tr>
				</tbody></table>
			</fieldset>
		<div style="display:none;">
			<s:hidden id="ENDDATE" name="ENDDATE"></s:hidden>
			<s:hidden id="CUSTOMERNO" name="CUSTOMERNO"></s:hidden>
			<s:hidden id="XMJD" name="XMJD"></s:hidden>
			<s:hidden id="STATUS" name="STATUS"></s:hidden>
			<s:hidden id="PROJECTNO" name="PROJECTNO"></s:hidden>
			<s:hidden id="ID" name="ID"></s:hidden>
			<s:hidden id="instanceid" name="instanceid"></s:hidden>
		</div>
		</div>
		</s:iterator>
		</form>
	</div>
</div>
</body>
</html>
<script language="JavaScript"> 
function doSubmit(){
	var valid = mainFormValidator.form(); //执行校验操作
	if(!valid){
		return false;
	}
	var jsyy=document.getElementById("JSYY").value;
	var memo=document.getElementById("MEMO").value;
	var id=document.getElementById("ID").value;
	var instanceid=document.getElementById("instanceid").value;
	 	var pageUrl="zqb_gpfxproject_sx_setclosereasonupdate.action";
		$.post(pageUrl,{memo:memo,jsyy:jsyy,id:id,instanceid:instanceid},function(data){
			if(data=='true'){
				W.lhgdialog.tips("保存成功",2);
				setTimeout('api.zindex().focus()', 500);
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