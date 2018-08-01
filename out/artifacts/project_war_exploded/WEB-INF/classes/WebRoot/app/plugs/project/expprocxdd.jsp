<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>项目汇总</title>
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
		var zjjguser = $("#zjjguser").is(":checked");
		var zjjgdh = $("#zjjgdh").is(":checked");
		var zjjgyx = $("#zjjgyx").is(":checked");
		if(zjjguser||zjjgdh||zjjgyx){$("#zjjgname").attr("checked",true);}
		var xmcyzw = $("#xmcyzw").is(":checked");
		var xmcydh = $("#xmcydh").is(":checked");
		var xmcysj = $("#xmcysj").is(":checked");
		var xmcyyx = $("#xmcyyx").is(":checked");
		var xmcybz = $("#xmcybz").is(":checked");
		if(xmcyzw||xmcydh||xmcysj||xmcyyx||xmcybz){$("#xmcyname").attr("checked",true);}
		var cljgfpbl = $("#cljgfpbl").is(":checked");
		var cljgtbr = $("#cljgtbr").is(":checked");
		var cljgtbsj = $("#cljgtbsj").is(":checked");
		if(cljgfpbl||cljgtbr||cljgtbsj){$("#cljgmc").attr("checked",true);}
		var clrfpbm = $("#clrfpbm").is(":checked");
		var clrfpbl = $("#clrfpbl").is(":checked");
		var clrtbr = $("#clrtbr").is(":checked");
		var clrtbsj = $("#clrtbsj").is(":checked");
		if(clrfpbm||clrfpbl||clrtbr||clrtbsj){$("#clr").attr("checked",true);}
		var temp=new Array();
		$("input[name='box']:checked").each(function(){
			temp.push($(this).val());
		});
		temp=temp.join(',');
		var pageUrl = "zqb_project_expprocxdd.action?zcPro="+temp;
		$("#editForm").attr("action", pageUrl);
		$("#editForm").submit();
	}
	$(function (){
		$('#all').click(function(){
	        //判断apple是否被选中
	        var bischecked=$('#all').is(':checked');
	        var fruit=$('input[name="box"]');
	        bischecked?fruit.attr('checked',true):fruit.attr('checked',false);
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
						icon="icon-save" plain="true" href="javascript:doSubmit();">确认导出</a>
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
							<td class="formpage_title">持续督导项目信息汇总</td>
						</tr>
						<tr>
							<td id="help" align="right"><br /></td>
						</tr>
						<tr>
							<td class="line" align="right"><br /></td>
						</tr>
						<tr>
							<td align="left">
								<input id="all" type="checkbox" name="all">&nbsp;全选
								<br />
								<fieldset>
									<legend>项目基本信息</legend>
									<table class="ke-zeroborder" border="0" cellpadding="0"
										cellspacing="0" width="100%">
										<tbody>
											<tr id="itemTr_1913">
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="projectname" type="checkbox" value="PROJECTNAME" name="box">&nbsp;项目名称
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="customername" type="checkbox" name="box" value="CUSTOMERNAME">&nbsp;客户名称
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="owner" type="checkbox" value="OWNER" name="box">&nbsp;项目负责人
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="manager" type="checkbox" value="MANAGER" name="box">&nbsp;项目现场负责人
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="startdate" type="checkbox" value="STARTDATE" name="box">&nbsp;项目开始时间
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="enddate" type="checkbox" value="ENDDATE" name="box">&nbsp;预计项目完成时间
												</td>
											</tr>
											<tr id="itemTr_1913">
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="khlxr" type="checkbox" value="KHLXR" name="box">&nbsp;客户联系人
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="khlxdh" type="checkbox" value="KHLXDH" name="box">&nbsp;客户联系电话
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="ggjzr" type="checkbox" value="GGJZR" name="box">&nbsp;股改基准日
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="sbjzr" type="checkbox" value="SBJZR" name="box">&nbsp;申报基准日
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="htje" type="checkbox" value="HTJE" name="box">&nbsp;合同金额
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
													<!-- <input id="memo" type="checkbox" value="MEMO" name="box">&nbsp;风险评估说明 -->
													<input id="xmbz" type="checkbox" value="XMBZ" name="box">&nbsp;项目备注
												</td>
											</tr>
											<tr id="itemTr_1913">
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="sssyb" type="checkbox" value="SSSYB" name="box">&nbsp;所属事业部
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="clslr" type="checkbox" value="CLSLR" name="box">&nbsp;材料受理日
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="shtgr" type="checkbox" value="SHTGR" name="box">&nbsp;审核通过日
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
													<s:if test="superman==true">
														<input id="a01" type="checkbox" value="A01" name="box">&nbsp;协议支出比例
													</s:if>
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
												</td>
												<!-- <td class="td_data" id="data_TZBT" width="150px">
													<input id="thcl" type="checkbox" value="THCL" name="box">&nbsp;是否投行承揽
												</td> -->
												<td class="td_data" id="data_TZBT" width="150px">
												</td>
											</tr>
										</tbody>
									</table>
									</fieldset>
									<br />
									<fieldset>
									<legend>项目中介机构</legend>
									<table class="ke-zeroborder" border="0" cellpadding="0"
										cellspacing="0" width="100%">
										<tbody>
											<tr id="itemTr_1913">
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="zjjgname" type="checkbox" value="ZJJGMC" name="box">&nbsp;中介机构名称
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="zjjguser" type="checkbox" value="LXR" name="box">&nbsp;联系人
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="zjjgdh" type="checkbox" value="LXDH" name="box">&nbsp;联系电话
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="zjjgyx" type="checkbox" value="LXYX" name="box">&nbsp;联系邮箱
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
												</td>
											</tr>
										</tbody>
									</table>
									</fieldset>
									<br />
									<fieldset>
									<legend>项目成员信息</legend>
									<table class="ke-zeroborder" border="0" cellpadding="0"
										cellspacing="0" width="100%">
										<tbody>
											<tr id="itemTr_1913">
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="xmcyname" type="checkbox" value="NAME" name="box">&nbsp;成员姓名
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="xmcyzw" type="checkbox" value="POSITION" name="box">&nbsp;成员职务
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="xmcydh" type="checkbox" value="TEL" name="box">&nbsp;成员联系电话
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="xmcysj" type="checkbox" value="PHONE" name="box">&nbsp;成员手机
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="xmcyyx" type="checkbox" value="EMAIL" name="box">&nbsp;成员邮箱
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="xmcybz" type="checkbox" value="CYMEMO" name="box">&nbsp;成员备注
												</td>
											</tr>
										</tbody>
									</table>
									</fieldset>
									<br />
									<s:if test="cwAndZkNh==true">
										<fieldset>
										<legend>项目质控内核人员信息</legend>
										<table class="ke-zeroborder" border="0" cellpadding="0" cellspacing="0" width="100%">
											<tbody>
												<tr id="itemTr_1913">
													<td class="td_data" id="data_TZBT" width="150px">
														<input id="zkryxx" type="checkbox" value="ZKRYXX" name="box">&nbsp;质控人员信息 
													</td>
													<td class="td_data" id="data_TZBT" width="150px">
														<input id="nhryxx" type="checkbox" value="NHRYXX" name="box">&nbsp;内核人员信息
													</td>
													<td class="td_data" id="data_TZBT" width="150px">
													</td>
													<td class="td_data" id="data_TZBT" width="150px">
													</td>
													<td class="td_data" id="data_TZBT" width="150px">
													</td>
													<td class="td_data" id="data_TZBT" width="150px">
													</td>
												</tr>
											</tbody>
										</table>
										</fieldset>
										<br />
									</s:if>
									<s:if test="superman==true">
										<fieldset>
										<legend>分配部门信息</legend>
										<table class="ke-zeroborder" border="0" cellpadding="0"
											cellspacing="0" width="100%">
											<tbody>
												<tr id="itemTr_1913">
													<td class="td_data" id="data_TZBT" width="150px">
														<input id="cljgmc" type="checkbox" value="CLJGMC" name="box">&nbsp;机构名称
													</td>
													<td class="td_data" id="data_TZBT" width="150px">
														<input id="cljgfpbl" type="checkbox" value="CLJGFPBL" name="box">&nbsp;分配比例
													</td>
													<td class="td_data" id="data_TZBT" width="150px">
														<input id="cljgtbr" type="checkbox" value="CLJGTBR" name="box">&nbsp;填报人
													</td>
													<td class="td_data" id="data_TZBT" width="150px">
														<input id="cljgtbsj" type="checkbox" value="CLJGTBSJ" name="box">&nbsp;填报时间
													</td>
													<td class="td_data" id="data_TZBT" width="150px">
													</td>
													<td class="td_data" id="data_TZBT" width="150px">
													</td>
												</tr>
											</tbody>
										</table>
										</fieldset>
										<br />
									</s:if>
									<s:if test="superman==true">
										<fieldset>
										<legend>承揽人信息</legend>
										<table class="ke-zeroborder" border="0" cellpadding="0"
											cellspacing="0" width="100%">
											<tbody>
												<tr id="itemTr_1913">
													<td class="td_data" id="data_TZBT" width="150px">
														<input id="clr" type="checkbox" value="CLR" name="box">&nbsp;承揽人
													</td>
													<td class="td_data" id="data_TZBT" width="150px">
														<input id="clrfpbm" type="checkbox" value="CLRFPBM" name="box">&nbsp;分配部门
													</td>
													<td class="td_data" id="data_TZBT" width="150px">
														<input id="clrfpbl" type="checkbox" value="CLRFPBL" name="box">&nbsp;分配比例
													</td>
													<td class="td_data" id="data_TZBT" width="150px">
														<input id="clrtbr" type="checkbox" value="CLRTBR" name="box">&nbsp;填报人
													</td>
													<td class="td_data" id="data_TZBT" width="150px">
														<input id="clrtbsj" type="checkbox" value="CLRTBSJ" name="box">&nbsp;填报时间
													</td>
													<td class="td_data" id="data_TZBT" width="150px">
													</td>
												</tr>
											</tbody>
										</table>
										</fieldset>
										<br />
									</s:if>
									<fieldset>
									<legend>项目阶段信息</legend>
									<table class="ke-zeroborder" border="0" cellpadding="0"
										cellspacing="0" width="100%">
										<tbody>
											<tr id="itemTr_1913">
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="taskname" type="checkbox" value="JDMC" name="box">&nbsp;阶段名称
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="taskmanager" type="checkbox" value="JDFZR" name="box">&nbsp;阶段负责人
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="taskssje" type="checkbox" value="SSJE" name="box">&nbsp;阶段实收金额
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="taskjdzl" type="checkbox" value="FILENAME" name="box">&nbsp;阶段上传资料
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="taskspjg" type="checkbox" value="SPZT" name="box">&nbsp;审批结果
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
												</td>
											</tr>
										</tbody>
									</table>
									</fieldset>
									<br />
									<fieldset>
									<legend>项目相关问题</legend>
									<table class="ke-zeroborder" border="0" cellpadding="0"
										cellspacing="0" width="100%">
										<tbody>
											<tr id="itemTr_1913">
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="questionname" type="checkbox" value="TWUSERNAME" name="box">&nbsp;提问人
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="question" type="checkbox" value="QUESTION" name="box">&nbsp;问题
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="questiondate" type="checkbox" value="TWCREATEDATE" name="box">&nbsp;提问时间
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="questioncontent" type="checkbox" value="CONTENT" name="box">&nbsp;回复信息
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
												</td>
											</tr>
										</tbody>
									</table>
									</fieldset>
									<br />
									<fieldset>
									<legend>项目评价</legend>
									<table class="ke-zeroborder" border="0" cellpadding="0"
										cellspacing="0" width="100%">
										<tbody>
											<tr id="itemTr_1913">
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="pjname" type="checkbox" value="PJNAME" name="box">&nbsp;阶段评价人
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="pjsj" type="checkbox" value="PJSJ" name="box">&nbsp;阶段评价时间
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="pjjg" type="checkbox" value="LDPJ" name="box">&nbsp;阶段评价结果
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
													<input id="pjsm" type="checkbox" value="PJSM" name="box">&nbsp;阶段评价说明
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
												</td>
												<td class="td_data" id="data_TZBT" width="150px">
												</td>
											</tr>
										</tbody>
									</table>
									</fieldset>
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