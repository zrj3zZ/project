<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>通知公告回复</title>
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
	function doSubmit() {
		/* var valid = mainFormValidator.form(); //执行校验操作
		if(!valid){
		return false;
		} */
		if($("#xgzl").val().length>=3000){
			alert("最多上传80个文件！");
		}else{
			var hfcontent = document.getElementById("HFCONTENT").value;
			var xgzl = document.getElementById("xgzl").value;
			var gonggaoID = document.getElementById("ggid").value;
			var hfqkid = document.getElementById("hfqkid").value;
			var sfcx = $("input[name='sfcx']:checked").val();
			var pageUrl = "zqb_announcement_reply_save.action";
			$.post(pageUrl, {hfcontent:hfcontent,xgzl:xgzl,gonggaoID:gonggaoID,hfqkid:hfqkid,sfcx:sfcx}, function(data) {
				if(data=='success'){
					art.dialog.tips("保存成功!",2);
					window.top.frames["ifm"].location.reload();
					setTimeout('cancel();',1000);
				}
			});
		}
		
	}
	function cancel(){
        api.zindex().focus();
    }
	function uploadifyDialog(dialogId,fieldName,divId,sizeLimit,multi,fileExt,fileDesc){
		if(dialogId==null||dialogId==""||fieldName==null||fieldName==""||divId==null||divId==""){
			alert('参数不正确');
			return ;
		}
		var url = 'showUploadifyPageTwo.action?parentColId='+fieldName+'&parentDivId='+divId+'&sizeLimit='+sizeLimit+'&multi='+multi+'&fileExt='+fileExt+'&fileDesc='+fileDesc+'';
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
	window.onbeforeunload = function() {
		if (is_form_changed()) {
			
		}
	}
	function is_form_changed() {
		var t_save = jQuery("#savebtn"); //检测页面是否要保存按钮

		if (t_save.length > 0) { //检测到保存按钮,继续检测元素是否修改
			var is_changed = false;
			jQuery("#border input, #border textarea, #border select").each(
					function() {
						var _v = jQuery(this).attr('_value');
						if (typeof (_v) == 'undefined')
							_v = '';
						if (_v != jQuery(this).val())
							is_changed = true;
					});
			return is_changed;
		}
		return false;
	}
	jQuery(document).ready(
			function() {
				jQuery("#border input, #border textarea, #border select")
						.each(
								function() {
									jQuery(this).attr('_value',
											jQuery(this).val());
								});
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
</style>
</head>
<body class="easyui-layout">
	<div id="blockPage" class="black_overlay" style="display:none"></div>
	<div region="north" style="height:40px;" border="false">
		<div class="tools_nav">
			<table width="100%">
				<tr>
					<td align="left"><a id="btnEp" class="easyui-linkbutton"
						icon="icon-save" plain="true" href="javascript:doSubmit();">保存</a>
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
					cellspacing="0" cellpadding="0" width="100%">
					<tbody>
						<tr>
							<td class="formpage_title">通知公告回复信息</td>
						</tr>
						<tr>
							<td id="help" align="right"></td>
						</tr>
						<tr>
							<td class="line" align="right"></td>
						</tr>
						<tr>
							<td align="left">
								<table class="ke-zeroborder" border="0" cellspacing="0"
									cellpadding="0" width="100%">
									<tbody>
										<tr>
											<td colspan="2"><s:iterator value="list" status="status">
													<table class="ke-zeroborder" border="0" cellpadding="0"
														cellspacing="0" width="100%">
														<tbody>
															<tr id="itemTr_1913">
																<td class="td_title" id="title_TZBT" width="180">通知标题</td>
																<td class="td_data" id="data_TZBT"><s:property
																		value="TZBT" /></td>
															</tr>
															<tr id="itemTr_1914">
																<td class="td_title" id="title_ZCHFSJ" width="180">
																	最迟回复时间</td>
																<td class="td_data" id="data_ZCHFSJ"><s:property
																		value="ZCHFSJ" /></td>
															</tr>
															<tr id="itemTr_1912">
																<td class="td_title" id="title_TZNR" width="180">通知类型</td>
																<td class="td_data" id="data_TZLX"><s:property
																		value="TZLX" /></td>
															</tr>
															<tr id="itemTr_1915">
																<td class="td_title" id="title_TZNR" width="180">通知内容</td>
																<td class="td_data" id="data_TZNR">
																<textarea
																		 name='TZNR'
																		id='TZNR' style="width:550px;height:100px;" readonly><s:property value="TZNR" escapeHtml="false" /></textarea>
																
															</tr>
															<tr id="itemTr_1916">
																<td class="td_title" id="title_XGZL" width="180">相关资料</td>
																<td class="td_data" id="data_XGZL" colspan="3"><div class="ui-jqgrid-hbox"><s:property value="count" escapeHtml="false" /></div><%-- <a
																	href="<s:property value="URL" />"
																	style="color: #0000ff;"><s:property value="XGZL" /></a> --%></td>
															</tr>
															<tr id="itemTr_1918">
																<td class="td_title" id="title_SFTZ" width="180">
																	是否通知回复人</td>
																<td class="td_data" id="data_SFTZ"><s:property
																		value="SFTZ" /></td>
															</tr>
															<%-- <tr id="itemTr_1917">
																<td class="td_title" id="title_HFR" width="180">回复人</td>
																<td class="td_data" id="data_HFR"><s:property
																		value="HFR" /></td>
															</tr> --%>
														</tbody>
													</table>
												</s:iterator></td>
										</tr>
										<s:iterator value="hflist" status="status">
													<table class="ke-zeroborder" border="0" cellpadding="0"
														cellspacing="0" width="100%">
														<tbody>
															<s:if test="tzlx=='股东质押查询'">
															<tr id="itemTr_1916">
																<td class="td_title" id="title_XGZL" width="180">是否查询</td>
																<td class="td_data" id="data_XGZL" colspan="3">
																	<s:radio id="sfcx" name="sfcx" value="EXTEND1" list="%{#{'1':'是','0':'否'}}" theme="simple"/>
																</td>
															</tr>
															</s:if>
															<tr id="itemTr_1932">
																<td id="title_CONTENT" class="td_title" width="180">
																	回复内容</td>
																<td id="data_CONTENT" class="td_data"><textarea
																		class="{maxlength:512,required:false,string:true} " name='HFCONTENT'
																		id='HFCONTENT' style="width:550px;height:60px;"><s:property value="CONTENT" escapeHtml="false" /></textarea>&nbsp;
																</td>
															</tr>
															<tr id="itemTr_1916">
																<td class="td_title" id="title_xgzl" width="180">相关资料</td>
																<td class="td_data" id="data_xgzl" colspan="3">
																	<div id="DIVxgzl">
																		<div>
																			<input type="hidden" value="${XGZL}" name="xgzl"	class="{maxlength:3000,string:true} valid" id="xgzl" size="100">
																		</div>
																		<div>
																			<button onclick="showUploadifyPagexgzl();return false;">附件上传</button>
																		</div>
																	</div> 
																	<script>
																		function showUploadifyPagexgzl() {
																			uploadifyDialog('xgzl',
																					'xgzl',
																					'DIVxgzl', '',
																					'true', '', '');
																		}
																	</script>
																</td>
															</tr>
															<tr>
															<td class="td_title" id="title_xgzl" width="180"></td>
															<td class="td_data" id="data_xgzl"><div class="ui-jqgrid-hbox"><s:property value="XGZLCOUNT" escapeHtml="false" /></div>
															</td>
															</tr>
														</tbody>
													</table>
										</s:iterator>
									</tbody>
								</table>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<input type="hidden" name="ggid" id="ggid" value="${ggid}" class="{string:true}" />
			<input type="hidden" name="hfqkid" id="hfqkid" value="${hfqkid}" class="{string:true}" />
			<input type="hidden" name="tzlx" id="tzlx" value="${tzlx}" class="{string:true}" />
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