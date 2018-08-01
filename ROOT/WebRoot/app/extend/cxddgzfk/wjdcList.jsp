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
	<script type="text/javascript" charset="utf-8">
		$(document).ready(function() {
			if($("#lockStatusForToolsNav").val()!='0'){
				$("#save").hide();
			}
		});
		function saveForm() {
			var ggid = $("#ggid").val();
			var khbh = $("#khbh").val();
			var roleid = $("#roleid").val();
			var bz;
			var pz;
			var url = "zqb_vote_updatewjdc.action?userId=" + khbh + "&TZGGID=" + ggid;
			for (var i = 1; i <= $("#forSize").val(); i++) {
				var as = $("input[name='AS" + i + "']:checked").val();
				var pl = $("input[name='PL" + i + "']:checked").val();
				bz = $("#BZ" + i).val();
				pz = $("#PZ" + i).val();
				if (as != '' && as != undefined) {
					url += ("&AS" + i + "=" + encodeURIComponent(as));
				}
				if (pl != '' && pl != undefined) {
					url += ("&PL" + i + "=" + encodeURIComponent(pl));
				}
				if (bz != '' && bz != undefined) {
					url += ("&BZ" + i + "=" + encodeURIComponent(bz));
				}
				if (pz != '' && pz != undefined) {
					url += ("&PZ" + i + "=" + encodeURIComponent(pz));
				}
				
			}
			url += ("&forSize=" + $("#forSize").val());
			var encodeUrl = url;
			$.post(encodeUrl,function(data){
				art.dialog.tips("保存成功!",2);
				window.location.reload();
			});
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
.content div{
	word-break:break-all;
	white-space:normal;
	width:860px;
	color:red;
}
.breakword{
	word-wrap:break-word;
	word-break:break-all;
	white-space: yes;
}
</style>
</head>
<body class="easyui-layout">
	<div region="center" style="text-align:center;border-left:1px #999 dotted;border-right:1px #999 dotted;border-top:1px #999 dotted;border-bottom:0px #999 dotted;padding:2px;">
	<div class="tools_nav" id="tools_nav">
		<span>
			<a id="save" plain="true" class="easyui-linkbutton l-btn l-btn-plain" onclick="saveForm()" href="#">
				<span class="l-btn-left">
					<span class="l-btn-text icon-save" style="padding-left: 20px;">保存</span>
				</span>
			</a>	
			<a plain="true" class="easyui-linkbutton l-btn l-btn-plain" href="javascript:this.location.reload();">
				<span class="l-btn-left">
					<span class="l-btn-text icon-reload" style="padding-left: 20px;">刷新</span>
				</span>
			</a>
			<!-- <a href="javascript:remove();" class="easyui-linkbutton" plain="true" iconCls="icon-print">打印</a> -->
			<a plain="true" class="easyui-linkbutton l-btn l-btn-plain" href="javascript:pageClose();" id="close">
				<span class="l-btn-left">
					<span class="l-btn-text icon-cancel" style="padding-left: 20px;">关闭</span>
				</span>
			</a>
		</span> 
		<span style="float:right">
		<!-- <a href="#" onclick='setMenuLayout()'>布局设置</a>&nbsp;&nbsp; -->
		</span>
	</div>
		<s:form id="editForm" name="editForm" theme="simple">
			<!--表单参数-->
			<div id="border">
				<table style="margin-bottom:5px;" class="ke-zeroborder" border="0"
					cellpadding="0" cellspacing="0" width="100%">
					<tbody>
						<tr>
							<td class="formpage_title">调查问卷</td>
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
										<table id='iform_grid'  width="100%" style="border:1px solid #efefef">
							                <s:iterator value="list" status="ll">
							                	<s:if test="#ll.index-1<0||CUSTOMERNO!=list[#ll.index-1].CUSTOMERNO">
									                	<tr class="cell">
									                		<td class="breakword"><s:property	value="QUESTION"/></td>
									                	</tr>
									                	<tr class="cell">
									                		<td><font color="red">
									                		<s:if test='list[#ll.index].ANSWER=="是"'>是否发生：
									                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="是" <s:if test="instanceid!=null">disabled="disabled"</s:if> checked="checked"/>是</label>
									                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="否" <s:if test="instanceid!=null">disabled="disabled"</s:if> />否</label>
									                		</s:if>
									                		<s:if test='list[#ll.index].ANSWER=="否"'>是否发生：
									                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="是" <s:if test="instanceid!=null">disabled="disabled"</s:if> />是</label>
									                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="否" <s:if test="instanceid!=null">disabled="disabled"</s:if> checked="checked"/>否</label>
									                		</s:if>
									                		&nbsp;&nbsp;&nbsp;&nbsp;
									                		<s:if test='list[#ll.index].PL=="是"'>是否披露：
									                		<label><input type="radio" name="PL<s:property value="COUNT"/>" id="PL<s:property value="COUNT"/>" value="是" <s:if test="instanceid!=null">disabled="disabled"</s:if> checked="checked"/>是</label>
									                		<label><input type="radio" name="PL<s:property value="COUNT"/>" id="PL<s:property value="COUNT"/>" value="否" <s:if test="instanceid!=null">disabled="disabled"</s:if> />否</label>
									                		</s:if>
									                		<s:if test='list[#ll.index].PL=="否"'>是否披露：
									                		<label><input type="radio" name="PL<s:property value="COUNT"/>" id="PL<s:property value="COUNT"/>" value="是" <s:if test="instanceid!=null">disabled="disabled"</s:if> />是</label>
									                		<label><input type="radio" name="PL<s:property value="COUNT"/>" id="PL<s:property value="COUNT"/>" value="否" <s:if test="instanceid!=null">disabled="disabled"</s:if> checked="checked"/>否</label>
										                	</s:if>
										                	</font>
									                	</tr>
									                	<s:if test="instanceid!=null">
									                		<tr class="cell">
									                			<td class="content">情况说明：<div id="BZ<s:property value="COUNT"/>" color="red"><s:property value="BZ"/></div></td>
									                		</tr>
									                	</s:if>
									                	<s:elseif test="instanceid==null">
									                		<tr class="cell">
									                			<td>情况说明：<textarea id="BZ<s:property value="COUNT"/>" class="{maxlength:256,required:false} " style="width:80%;height:50px;" value="" name="BZ<s:property value="COUNT"/>"><s:property value="BZ"/></textarea></td>
									                		</tr>
									                	</s:elseif>
									                	<s:if test="instanceid==null">
									                		<tr class="cell">
									                			<td class="content">批注：<div id="PZ<s:property value="COUNT"/>" color="red"><s:property value="PZ"/></div></td>
									                		</tr>
									                	</s:if>
									                	<s:elseif test="instanceid!=null">
									                		<tr class="cell">
									                			<td>批注：<textarea id="PZ<s:property value="COUNT"/>" class="{maxlength:256,required:false} " style="width:80%;height:50px;" value="" name="PZ<s:property value="COUNT"/>"><s:property value="PZ"/></textarea></td>
									                		</tr>
									                	</s:elseif>
												</s:if>
												<s:else>
								                	<tr class="cell">
								                		<td class="breakword"><s:property	value="QUESTION"/></td>
								                	</tr>
								                	<tr class="cell">
								                		<td>
									                		<font color="red">
									                			<s:if test='list[#ll.index].ANSWER=="是"'>是否发生：
										                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="是" <s:if test="instanceid!=null">disabled="disabled"</s:if> checked="checked"/>是</label>
										                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="否" <s:if test="instanceid!=null">disabled="disabled"</s:if> />否</label>
										                		</s:if>
										                		<s:if test='list[#ll.index].ANSWER=="否"'>是否发生：
										                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="是" <s:if test="instanceid!=null">disabled="disabled"</s:if> />是</label>
										                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="否" <s:if test="instanceid!=null">disabled="disabled"</s:if> checked="checked"/>否</label>
										                		</s:if>
										                		&nbsp;&nbsp;&nbsp;&nbsp;
										                		<s:if test='list[#ll.index].PL=="是"'>是否披露：
										                		<label><input type="radio" name="PL<s:property value="COUNT"/>" id="PL<s:property value="COUNT"/>" value="是" <s:if test="instanceid!=null">disabled="disabled"</s:if> checked="checked"/>是</label>
										                		<label><input type="radio" name="PL<s:property value="COUNT"/>" id="PL<s:property value="COUNT"/>" value="否" <s:if test="instanceid!=null">disabled="disabled"</s:if> />否</label>
										                		</s:if>
										                		<s:if test='list[#ll.index].PL=="否"'>是否披露：
										                		<label><input type="radio" name="PL<s:property value="COUNT"/>" id="PL<s:property value="COUNT"/>" value="是" <s:if test="instanceid!=null">disabled="disabled"</s:if> />是</label>
										                		<label><input type="radio" name="PL<s:property value="COUNT"/>" id="PL<s:property value="COUNT"/>" value="否" <s:if test="instanceid!=null">disabled="disabled"</s:if> checked="checked"/>否</label>
										                		</s:if>
															</font>
														</td>
								                	</tr>
								                	<s:if test="instanceid!=null">
									                	<tr class="cell">
									                		<td class="content">情况说明：<div id="BZ<s:property value="COUNT"/>" color="red"><s:property value="BZ"/></div></td>
									                	</tr>
									                </s:if>
									                <s:elseif test="instanceid==null">
									                	<tr class="cell">
									                		<td>情况说明：<textarea id="BZ<s:property value="COUNT"/>" class="{maxlength:256,required:false} " style="width:80%;height:50px;" value="" name="BZ<s:property value="COUNT"/>"><s:property value="BZ"/></textarea></td>
									                	</tr>
									                </s:elseif>
									                <s:if test="instanceid==null">
									                	<tr class="cell">
									                		<td class="content">批注：<div id="PZ<s:property value="COUNT"/>" color="red"><s:property value="PZ"/></div></td>
									                	</tr>
									                </s:if>
									                <s:elseif test="instanceid!=null">
									                		<tr class="cell">
									                			<td>批注：<textarea id="PZ<s:property value="COUNT"/>" class="{maxlength:256,required:false} " style="width:80%;height:50px;" value="" name="PZ<s:property value="COUNT"/>"><s:property value="PZ"/></textarea></td>
									                		</tr>
									                </s:elseif>
												</s:else>
												<input type="hidden" id="khbh" name="khbh" value="${userId}"/>
											</s:iterator>
							                <s:iterator value="feedBackList" status="ll">
							                <s:if test="#ll.index-1<0||CUSTOMERNO!=list[#ll.index-1].CUSTOMERNO">
					                			<s:if test="list.size()==0">
													<tr class="cell">
									                	<td><font size="2.5">
										                	<s:if test="CUSTOMERNAME!=null&&CUSTOMERNAME!=''">
									                			客户名称:<s:property	value="CUSTOMERNAME"/>
										                	</s:if>
									                	</font></td>
									                </tr>
												</s:if>
							                	<tr class="cell">
							                		<td class="breakword"><s:property	value="QUESTION"/></td>
							                	</tr>
							                	<tr class="cell">
							                		<td>
							                			<s:if test='feedBackList[#ll.index].ANSWER=="是"'>是否发生：
								                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="是" <s:if test="instanceid!=null">disabled="disabled"</s:if> checked="checked"/>是</label>
								                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="否" <s:if test="instanceid!=null">disabled="disabled"</s:if> />否</label>
								                		</s:if>
								                		<s:if test='feedBackList[#ll.index].ANSWER=="否"'>是否发生：
								                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="是" <s:if test="instanceid!=null">disabled="disabled"</s:if> />是</label>
								                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="否" <s:if test="instanceid!=null">disabled="disabled"</s:if> checked="checked"/>否</label>
								                		</s:if>
								                		&nbsp;&nbsp;&nbsp;&nbsp;
							                			<s:if test='feedBackList[#ll.index].PL=="是"'>是否披露：
								                		<label><input type="radio" name="PL<s:property value="COUNT"/>" id="PL<s:property value="COUNT"/>" value="是" <s:if test="instanceid!=null">disabled="disabled"</s:if> checked="checked"/>是</label>
								                		<label><input type="radio" name="PL<s:property value="COUNT"/>" id="PL<s:property value="COUNT"/>" value="否" <s:if test="instanceid!=null">disabled="disabled"</s:if> />否</label>
								                		</s:if>
								                		<s:if test='feedBackList[#ll.index].PL=="否"'>是否披露：
								                		<label><input type="radio" name="PL<s:property value="COUNT"/>" id="PL<s:property value="COUNT"/>" value="是" <s:if test="instanceid!=null">disabled="disabled"</s:if> />是</label>
								                		<label><input type="radio" name="PL<s:property value="COUNT"/>" id="PL<s:property value="COUNT"/>" value="否" <s:if test="instanceid!=null">disabled="disabled"</s:if> checked="checked"/>否</label>
								                		</s:if>
													</td>
							                	</tr>
							                	<s:if test="instanceid!=null">
							                		<tr class="cell">
							                			<td class="content">情况说明：<div id="BZ<s:property value="COUNT"/>" color="red"><s:property value="BZ"/></div></td>
							                		</tr>
							                	</s:if>
							                	<s:elseif test="instanceid==null">
							                		<tr class="cell">
							                			<td>情况说明：<textarea id="BZ<s:property value="COUNT"/>" class="{maxlength:256,required:false} " style="width:80%;height:50px;" value="" name="BZ<s:property value="COUNT"/>"><s:property value="BZ"/></textarea></td>
							                		</tr>
							                	</s:elseif>
							                	<s:if test="instanceid==null">
							                		<tr class="cell">
							                			<td class="content">批注：<div id="BZ<s:property value="COUNT"/>" color="red"><s:property value="PZ"/></div></td>
							                		</tr>
							                	</s:if>
							                	<s:elseif test="instanceid!=null">
							                		<tr class="cell">
							                			<td>批注：<textarea id="PZ<s:property value="COUNT"/>" class="{maxlength:256,required:false} " style="width:80%;height:50px;" value="" name="PZ<s:property value="COUNT"/>"><s:property value="PZ"/></textarea></td>
							                		</tr>
							                	</s:elseif>
							                </s:if>
							                <s:else>
						                	<tr class="cell">
						                		<td class="breakword"><s:property	value="QUESTION"/></td>
						                	</tr>
						                	<tr class="cell">
						                		<td>
						                			<s:if test='feedBackList[#ll.index].ANSWER=="是"'>是否发生：
							                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="是" <s:if test="instanceid!=null">disabled="disabled"</s:if> checked="checked"/>是</label>
							                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="否" <s:if test="instanceid!=null">disabled="disabled"</s:if> />否</label>
							                		</s:if>
							                		<s:if test='feedBackList[#ll.index].ANSWER=="否"'>是否发生：
							                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="是" <s:if test="instanceid!=null">disabled="disabled"</s:if> />是</label>
							                		<label><input type="radio" name="AS<s:property value="COUNT"/>" id="AS<s:property value="COUNT"/>" value="否" <s:if test="instanceid!=null">disabled="disabled"</s:if> checked="checked"/>否</label>
							                		</s:if>
							                		&nbsp;&nbsp;&nbsp;&nbsp;
						                			<s:if test='feedBackList[#ll.index].PL=="是"'>是否披露：
							                		<label><input type="radio" name="PL<s:property value="COUNT"/>" id="PL<s:property value="COUNT"/>" value="是" <s:if test="instanceid!=null">disabled="disabled"</s:if> checked="checked"/>是</label>
							                		<label><input type="radio" name="PL<s:property value="COUNT"/>" id="PL<s:property value="COUNT"/>" value="否" <s:if test="instanceid!=null">disabled="disabled"</s:if> />否</label>
							                		</s:if>
							                		<s:if test='feedBackList[#ll.index].PL=="否"'>是否披露：
							                		<label><input type="radio" name="PL<s:property value="COUNT"/>" id="PL<s:property value="COUNT"/>" value="是" <s:if test="instanceid!=null">disabled="disabled"</s:if> />是</label>
							                		<label><input type="radio" name="PL<s:property value="COUNT"/>" id="PL<s:property value="COUNT"/>" value="否" <s:if test="instanceid!=null">disabled="disabled"</s:if> checked="checked"/>否</label>
							                		</s:if>
												</td>
						                	</tr>
						                	<s:if test="instanceid!=null">
						                		<tr class="cell">
						                			<td class="content">情况说明：<div id="BZ<s:property value="COUNT"/>" color="red"><s:property value="BZ"/></div></td>
						                		</tr>
						                	</s:if>
						                	<s:elseif test="instanceid==null">
						                		<tr class="cell">
						                			<td>情况说明：<textarea id="BZ<s:property value="COUNT"/>" class="{maxlength:256,required:false} " style="width:80%;height:50px;" value="" name="BZ<s:property value="COUNT"/>"><s:property value="BZ"/></textarea></td>
						                		</tr>
						                	</s:elseif>
						                	<s:if test="instanceid==null">
						                		<tr class="cell">
						                			<td class="content">批注：<font id="BZ<s:property value="COUNT"/>" color="red"><s:property value="PZ"/></font></td>
						                		</tr>
						                	</s:if>
						                	<s:elseif test="instanceid!=null">
						                		<tr class="cell">
						                			<td>批注：<textarea id="PZ<s:property value="COUNT"/>" class="{maxlength:256,required:false} " style="width:80%;height:50px;" value="" name="PZ<s:property value="COUNT"/>"><s:property value="PZ"/></textarea></td>
						                		</tr>
						                	</s:elseif>
							                </s:else>
							                <input type="hidden" id="khbh" name="khbh" value="${userId}"/>
											</s:iterator>
										</table>
									</tbody>
								</table>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<input type="hidden" id="instanceid" name="instanceid" value="${instanceid}"/>
			<input type="hidden" id="ggid" name="ggid" value="${ggid}"/>
			<input type="hidden" id="forSize" name="forSize" value="${feedBackList.size()+list.size()}"/>
			<input type="hidden" id="lockStatusForToolsNav" name="lockStatusForToolsNav" value="${lockStatusForToolsNav}"/>
			<input type="hidden" id="roleid" name="roleid" value="${roleid}"/>
		</s:form>
	</div>
</body>
</html>