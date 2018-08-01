<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><s:property value='title' escapeHtml='false' /></title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css" />
<link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/easyui/themes/gray/easyui.css" />
<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css" />
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=default"></script>
<script type="text/javascript" src="iwork_js/engine/iformpage.js" charset="utf-8"></script>
<script type="text/javascript">
	var mainFormValidator;
	$().ready(function() {
		mainFormValidator =  $("#editForm").validate({});
	 	mainFormValidator.resetForm();
	});
	function expPro() {
		var valid = mainFormValidator.form(); //执行校验操作
    	if(!valid){
    		return ;
    	}
    	var nums = 0;
		var chk = document.getElementsByName('box');
		for (var i = 0; i < chk.length; i++) {
			if(chk[i].checked){
				nums++;
			}
		}
		if(nums<=0){
			alert("请勾选项目类型后导出。");
		}else{
			var temp=new Array();
			$("input[name='box']:checked").each(function(){
				temp.push($(this).val());
			});
			temp=temp.join(',');
			var pageUrl = "zqb_project_expxmjs.action?xmlx="+temp;
			$("#editForm").attr("action", pageUrl);
			$("#editForm").submit();
		}
	}
	function pageClose() {
		if (typeof (api) == "undefined") {
			window.close();
		} else {
			api.close();
		}
	}
</script>
<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css" />
</head>
<body class="easyui-layout">
	<div id="blockPage" class="black_overlay" style="display:none"></div>
	<div region="north" style="height:40px;" border="false">
		<div class="tools_nav">
			<table width="100%">
				<tr>
					<td align="left"><a id="btnEp" class="easyui-linkbutton"
						icon="icon-save" plain="true" href="javascript:expPro();">确认导出</a>
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
							<td class="formpage_title">项目结算信息管理</td>
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
										<s:if test="status==1">
											<tr id="itemTr_1913">
												<td class="td_title" id="title_TZBT">月份:</td>
												<td class="td_data" id="data_TZBT"><input type="text" name="prodate" id="prodate"
													class="{required:true}"
													onfocus="WdatePicker({dateFmt:'yyyy-MM'})" />&nbsp;&nbsp;&nbsp;&nbsp;
												</td>
											</tr>
										</s:if>
										<tr id="itemTr_1914">
											<td class="td_title" id="title_TZBT">类型:</td>
											<td class="td_data" id="data_TZBT">
												<table class="ke-zeroborder" border="0" cellpadding="0"
													cellspacing="0" width="100%">
													<tbody>
														<tr id="itemTr_1913">
															<td class="td_data" id="data_TZBT" width="50px">
																<input id="tjgp" type="checkbox" value="TJGP" checked="checked" name="box">&nbsp;推荐挂牌
															</td>
															<td class="td_data" id="data_TZBT" width="50px">
																<input id="dxzf" type="checkbox" value="DXZF" name="box">&nbsp;定向增发
															</td>
															<td class="td_data" id="data_TZBT" width="50px">
																<input id="bgcz" type="checkbox" value="BGCZ" name="box">&nbsp;并购重组
															</td>
															<td class="td_data" id="data_TZBT" width="50px">
																<input id="cxdd" type="checkbox" value="CXDD" name="box">&nbsp;持续督导
															</td>
															<td class="td_data" id="data_TZBT" width="50px">
																<input id="qt" type="checkbox" value="QT" name="box">&nbsp;其他
															</td>
														</tr>
													</tbody>
												</table>
											</td>
										</tr>
									</tbody>
								</table>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<s:hidden id="status" name="status"></s:hidden>
		</s:form>
	</div>
</body>
</html>