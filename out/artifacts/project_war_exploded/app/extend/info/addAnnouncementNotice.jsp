<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>公告统计111</title>
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
		$.ajax({
	        url : 'zqb_tzggproject_costormer_set.action',
	        async : false,
	        type : "POST",
	        data: {
	        	ggid:id
            },
	        dataType : "json",
	        success : function(data) {
	        }
	    });
	});
	$(function() {
		$('#pp').pagination({
			total : <s:property value="totalNum"/>,
			pageNumber : <s:property value="pageNumber"/>,
			pageSize : <s:property value="pageSize"/>,
			onSelectPage : function(pageNumber, pageSize) {
				submitMsg(pageNumber, pageSize);
			}
		});
	});
	function submitMsg(pageNumber, pageSize) {
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#frmMain").submit();
		return;
	}
	$(function() {
	
		//查询
		$("#search")
				.click(
						function() {
							var khmc = $("#KHMC").val();
							var ggzy = $("#GGZY").val();
							var spzt = $("#SPZT").val();
							var noticename = $("#NOTICENAME").val();
							var gpdm = $("#GPDM").val();
							var startdate = $("#STARTDATE").val();
							var enddate = $("#ENDDATE").val();
							var seachUrl = encodeURI("zqb_announcement_count.action?khmc="
									+ khmc
									+ "&ggzy="
									+ ggzy
									+ "&spzt="
									+ spzt
									+ "&noticename="
									+ noticename
									+ "&zqdm="
									+ gpdm
									+ "&startdate="
									+ startdate
									+ "&enddate=" + enddate);
							window.location.href = seachUrl;
						});
	});

	function expExcel() {
		//导入excel
		var pageUrl = "zqb_announcement_exp.action";
		$("#ifrmMain").attr("action", pageUrl);
		$("#ifrmMain").submit();
	}
	function addItem() {
		var pageUrl = "createFormInstance.action?formid=157&demId=72";
		art.dialog.open(pageUrl,{
			title : '客户信息维护表单',
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			rang : true,
			width : 1100,
			cache : false,
			lock : true,
			height : 580,
			iconTitle : false,
			extendDrag : true,
			autoSize : false,
			close : function() {
				window.location.reload();
			}
		});

	}
	
	function multi_book(parentDept, currentDept, startDept, targetUserId,targetUserNo, targetUserName, targetDeptId, targetDeptName, defaultField) {
		var url = "multibook_index.action?1=1";
		if(parentDept!=''){
			url+="&parentDept="+parentDept;
		}
		if(currentDept!=''){
			url+="&currentDept="+currentDept;
		}
		if(startDept!=''){
			url+="&startDept="+startDept;
		} 
		if(targetUserId!=''){
			url+="&targetUserId="+targetUserId;
		}
		if(targetUserNo!=''){
			url+="&targetUserNo="+targetUserNo;
		}
		if(targetUserName!=''){
			url+="&targetUserName="+targetUserName;
		}
		if(targetDeptId!=''){
			url+="&targetDeptId="+targetDeptId;
		}
		if(targetDeptName!=''){
			url+="&targetDeptName="+targetDeptName;
		}
		if(defaultField!=''){
			url+="&defaultField="+defaultField;
		}
		//获得input内容
		var v = document.getElementById(defaultField);
		if(v.value!=""){
			url+="&input="+v.value;
		} 
		art.dialog.open(url,{
			id:"multiBookDialog",
			title: '多选地址簿',
			pading: 0,
			lock: true,
			width: 400,
			height: 450
		});
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
						<s:if test="fszt!='完成'&&tzggGrantCUD">
							<a id="btnEpForSave" class="easyui-linkbutton" icon="icon-save" plain="true" href="javascript:void(0);" onclick="doSubmit();" >保存</a>
						</s:if>
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
							<td class="formpage_title">通知公告</td>
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
										<tr id="itemTr_1913">
											<td class="td_title" id="title_TZBT" width="180">通知标题</td>
											<td class="td_data" id="data_TZBT"><input type="text"<s:if test="sfyhf==1"> readonly="readonly"</s:if>
												id="tzbt" name="tzbt" style="width:500px;height:22px;" class="{maxlength:128,required:true,string:true}" value="${tzbt}"/>
												<font style="color:red">*</font>
											</td>
										</tr>
										<tr id="itemTr_2862">
											<td id="title_EXTEND5" class="td_title" width="180"> 首页滚动显示 </td>
											<td id="data_EXTEND5" class="td_data">
												<input id="EXTEND50" type="radio" value="是" <s:if test='extend5=="是"'> checked="checked"</s:if> name="EXTEND5">
												<label id="lbl_EXTEND50" for="EXTEND50">是</label>
												<input id="EXTEND51" type="radio" value="否" <s:if test='extend5=="否"'> checked="checked"</s:if> name="EXTEND5">
												<label id="lbl_EXTEND51" for="EXTEND51">否</label>
											</td>
										</tr>
										<tr id="itemTr_1914">
											<td class="td_title" id="title_ZCHFSJ" width="180">最迟回复时间</td>
											<td class="td_data" id="data_ZCHFSJ"><input id="zchfsj" class="{required:true,string:true}" type="text" value="${zchfsj}" name="zchfsj"
												style="width:160px;height:22px;"
												onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})">
												<font style="color:red">*</font>
												<font style="color: #333;font-family:'微软雅黑';">通知类型</font> 
												<s:if test="tzlx=='培训通知'">
													<select name='NOTICETYPE'  id='NOTICETYPE' class=required  >
															<option value=''>-空-</option>
															<option value='其他通知'>其他通知</option>
															<option value='培训通知' selected>培训通知</option>
															<s:if test="zqServer=='hlzq' || zqServer=='dgzq'"><option value='股东质押查询'>股东质押查询</option></s:if>
															<option value='问卷调查'>问卷调查</option>
															<s:if test="zqServer=='dgzq'"><option value='弹窗通知'>弹窗通知</option></s:if>
													</select><font color=red>*</font>&nbsp;
												</s:if>
												<s:elseif test="tzlx=='问卷调查'">
													<select name='NOTICETYPE'  id='NOTICETYPE' class=required>
															<option value=''>-空-</option>
															<option value='其他通知'>其他通知</option>
															<option value='培训通知'>培训通知</option>
															<s:if test="zqServer=='hlzq' || zqServer=='dgzq'"><option value='股东质押查询'>股东质押查询</option></s:if>
															<option value='问卷调查' selected>问卷调查</option>
															<s:if test="zqServer=='dgzq'"><option value='弹窗通知'>弹窗通知</option></s:if>
													</select><font color=red>*</font>&nbsp;
												</s:elseif>
												<s:elseif test="tzlx=='股东质押查询' && (zqServer=='hlzq' || zqServer=='dgzq')">
													<select name='NOTICETYPE'  id='NOTICETYPE' class=required  >
															<option value=''>-空-</option>
															<option value='其他通知'>其他通知</option>
															<option value='培训通知'>培训通知</option>
															<option value='股东质押查询' selected>股东质押查询</option>
															<option value='问卷调查'>问卷调查</option>
															<s:if test="zqServer=='dgzq'"><option value='弹窗通知'>弹窗通知</option></s:if>
													</select><font color=red>*</font>&nbsp;
												</s:elseif>
												<s:elseif test="tzlx=='弹窗通知' && zqServer=='dgzq' ">
													<select name='NOTICETYPE'  id='NOTICETYPE' class=required  >
															<option value=''>-空-</option>
															<option value='其他通知'>其他通知</option>
															<option value='培训通知'>培训通知</option>
															<option value='股东质押查询' >股东质押查询</option>
															<option value='问卷调查'>问卷调查</option>
															<s:if test="zqServer=='dgzq'"><option value='弹窗通知' selected>弹窗通知</option></s:if>
													</select><font color=red>*</font>&nbsp;
												</s:elseif>
												<s:else>
													<select name='NOTICETYPE'  id='NOTICETYPE' class=required  >
															<option value=''>-空-</option>
															<option value='其他通知' selected>其他通知</option>
															<option value='培训通知'>培训通知</option>
															<s:if test="zqServer=='hlzq' || zqServer=='dgzq'"><option value='股东质押查询'>股东质押查询</option></s:if>
															<option value='问卷调查'>问卷调查</option>
															<s:if test="zqServer=='dgzq'"><option value='弹窗通知'>弹窗通知</option></s:if>
													</select><font color=red>*</font>&nbsp;
												</s:else>
											</td>
										</tr>
										<s:if test="tzggGrantCUD">
										<tr id="itemTr_1911">
											<td id="title_DCTM" class="td_title" width="90px">问卷操作步骤</td>
											<td id="data_DCTM" class="td_data" width="130px">
												
												<div id="DIVwjfile">
													<div><input type="hidden" value="${wjfile}" name="wjfile" class="valid" id="wjfile" size="100"></div>
													<div>
														<input type="text" id="dctm" name="dctm" style="width:300px;height:22px;display:none;" class="{maxlength:128,required:true,string:true}" value="${dctm}"/>
														<a style="margin-left:15px;color:blue;" href="iwork_file/问卷调查问题导入模板.xls" download="问卷调查问题导入模板.xls">1、下载问卷模板</a>
														<a style="margin-left:15px;color:blue;" href="javascript:showUploadifyPagehfr();">2、选择问卷模板文件</a>
														<a style="margin-left:15px;color:blue;" href="javascript:impWjdc();">3、导入问卷模板文件</a>
														<a style="margin-left:15px;color:blue;" href="javascript:readWjdc();">4、问卷预览</a>
													</div>
												</div>
												<script>
													function showUploadifyPagehfr(){
														uploadifyDialog('wjfile','wjfile','DIVwjfile','','true','','');
													}
													function impWjdc(){
														var tzbz = $("#tzbz").val();
														var zchfsj = $("#zchfsj").val();
														var NOTICETYPE = $("#NOTICETYPE").val();
														var tznr = $("#tznr").val();
														if(tzbz==''||zchfsj==''||NOTICETYPE==''||tznr==''){
															art.dialog.tips("请添加必填信息!",2);
															return;
														}
														$("#NOTICETYPE").val("问卷调查");
														var wjfile = $("#wjfile").val();
														var arr = wjfile.split(",");
														if(arr.length>1){
															$("#wjfile").val(arr[arr.length-1])
														}
														var uuid = $("#wjfile").val();
														$.post("zqb_vote_impwjdc.action",{uuid:uuid},function(data){
															window.onbeforeunload = null;
															doSubmitImp();
														});
													}
													function readWjdc(){
														var demId = $("#demId").val();
														var formid = $("#formid").val();
														var pageUrl = "createFormInstance.action?formid="+formid+"&demId="+demId+"&isDialogDisabled="+1+"&TZGGID=" + $("#ggid").val();
														art.dialog.open(pageUrl,{
															title : '问卷调查',
															loadingText : '正在加载中,请稍后...',
															bgcolor : '#999',
															rang : true,
															width : 1100,
															cache : false,
															lock : true,
															height : 580,
															iconTitle : false,
															extendDrag : true,
															autoSize : false
														});
													}
												</script>
												
											</td>
										</tr>
										</s:if>
										<tr id="itemTr_1915">
											<td class="td_title" id="title_TZNR" width="180">通知内容</td>
											<td class="td_data" id="data_TZNR">
											<s:textarea rows="8" cols="92" id="tznr" name="tznr" cssClass="{maxlength:3000,required:true,string:true}"></s:textarea>
												<font style="color:red">*</font>
											</td>
										</tr>
										<tr id="itemTr_1916">
											<td class="td_title" id="title_XGZL" width="180">相关资料</td>
											<td class="td_data" id="data_XGZL" colspan="3">
													<div id="DIVXGZL">
														<div><input type="hidden" value="${XGZL}" name="XGZL {string:true}" class="valid" id="XGZL" size="100"></div>
														<div><s:if test="tzggGrantCUD"><button onclick="showUploadifyPageXGZL();return false;">附件上传</button></s:if></div>
														<s:property value="count" escapeHtml="false" />
													</div>
													<script>
														function showUploadifyPageXGZL(){
															uploadifyDialog('XGZL','XGZL','DIVXGZL','','true','','');
														}
													</script>
											</td>
										</tr>
										<%-- <tr id="itemTr_1918">
											<td class="td_title" id="title_SFTZ" width="180">
												通知事务办理人</td>
											<td class="td_data" id="data_SFTZ">
											<s:radio list="#{'是':'是','否':'否'}" name="sftz" value="#{sftz}"/>
											<font style="color:red">*</font>
											<label for="sftz" id="sftzd" generated="true" class="error" style="display: none;">必选字段</label>
										</tr> --%>
										<s:if test="tzggGrantCUD">
										<tr id="itemTr_1917">
											<td class="td_title" id="title_HFR" width="180">事务办理人</td>
											<td class="td_data" id="data_HFR">
												<input type="hidden" form-type="radioAddress" style="width:100px;margin-right:5px;" value="${hfr}" id="hfr" name="hfr" class="{string:true}" >
												<a href="javascript:openTzggcx();"  style="margin-left:5px;" class="easyui-linkbutton" plain="true" iconCls="icon-dictionary">选择</a><!-- 需通知初审人员 -->
												<!-- <a iconcls="icon-multibook" plain="true" class="easyui-linkbutton l-btn l-btn-plain" title="多选地址薄" onclick="multi_book('','','','','','','','','hfr');" href="###">选择</a> -->
												<s:if test="roleid!=3">
													<a iconcls="icon-multibook" plain="true" class="easyui-linkbutton l-btn l-btn-plain" title="" onclick="doSubmitOne();" href="###">选择我督导的公司</a><!-- 需通知初审人员 -->
													<a iconcls="icon-multibook" plain="true" class="easyui-linkbutton l-btn l-btn-plain" title="" onclick="doSubmitAll();" href="###">选择所有公司</a><!-- 需通知初审人员 -->
													<a iconcls="icon-multibook" plain="true" class="easyui-linkbutton l-btn l-btn-plain" title="" onclick="doSubmitWGP();" href="###">选择未挂牌公司</a>
													<a href="javascript:openXmjbr();"  style="margin-left:5px;" class="easyui-linkbutton" plain="true" iconCls="icon-dictionary">选择项目经办人</a>
												</s:if>
												</td>
										</tr>
										</s:if>
										<tr id="itemTr_1919">
											<td colspan="2" style="border:1px solid #eee; height: 520px;" >
											<iframe id="iframe" name="iframe" width='100%'  height='98%' src='announcement_notice_list.action?ggid=${ggid}' frameborder=0 scrolling=no marginheight=0 marginwidth=0></iframe>
											</td>
										</tr>
									</tbody>
								</table>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<input type="hidden" id="ggid" name="ggid" value="${ggid}" class="{string:true}"/>
			<input type="hidden" id="demId" name="demId" value="${demId}"/>
			<input type="hidden" id="formid" name="formid" value="${formid}"/>
		</s:form>
	</div>
</body>
</html>
<script language="JavaScript"> 

window.onbeforeunload = function() {
	if(is_form_changed()) {
	}
	}
	function is_form_changed() {
	var t_save = jQuery("#savebtn"); //检测页面是否要保存按钮

	if(t_save.length>0) { //检测到保存按钮,继续检测元素是否修改
	var is_changed = false;
	jQuery("#border input, #border textarea, #border select").each(function() {
	var _v = jQuery(this).attr('_value');
	if(typeof(_v) == 'undefined') _v = '';
	if(_v != jQuery(this).val()) is_changed = true;
	});
	return is_changed;
	}
	return false;
	}
	jQuery(document).ready(function(){
	jQuery("#border input, #border textarea, #border select").each(function() {
	jQuery(this).attr('_value', jQuery(this).val());
	});
	}); 
function doSubmitImp(){
	if($("#tzbz").val()==''||$("#zchfsj").val()==''||$("#NOTICETYPE").val()==''||$("#tznr").val()==''){
		art.dialog.tips("请添加必填信息!",2);
		return;
	}	
	var tzbt=document.getElementById("tzbt").value;
	var zchfsj=document.getElementById("zchfsj").value;
	var tznr=document.getElementById("tznr").value;
	var XGZL=document.getElementById("XGZL").value;
	var hfr=document.getElementById("hfr").value;
	var wjfile=document.getElementById("wjfile").value;
	var dctm=document.getElementById("dctm").value;
	var extend5 = $("input[name='EXTEND5']:checked").val();
	var tzlx = $("select").find("option:selected").val();	
	var sftz="";	
	var ggid=$("#ggid").val();	
	sftz='否';
 	var pageUrl="zqb_announcement_save.action";
 	$.post(pageUrl,{tzbt:tzbt,zchfsj:zchfsj,tznr:tznr,XGZL:XGZL,hfr:hfr,sftz:sftz,ggid:ggid,tzlx:tzlx,wjfile:wjfile,dctm:dctm,extend5:extend5},function(data){
		var dataJson = eval("(" + data + ")");
        var flag = dataJson[0].flag;
        var ggid = dataJson[0].ggid;
        var hfr = dataJson[0].HFR;
		if(flag!='error'){
			$("#ggid").val(ggid);
			$("#hfr").val(hfr);
			art.dialog.tips("保存成功",2);
			setTimeout('api.zindex().focus()', 500);
			window.location.href="zqb_announcement_notice_add.action?ggid="+ggid;
		}else{
			art.dialog.tips("保存失败",2);
			$("#iframe").reload();
		}
	});
}
function doSubmit(){
	if($("#tzbz").val()==''||$("#zchfsj").val()==''||$("#NOTICETYPE").val()==''||$("#tznr").val()==''){
		art.dialog.tips("请添加必填信息!",2);
		return;
	}
	/* var valid = mainFormValidator.form(); //执行校验操作
	if(!valid){
		return false;
	} */
	var tzbt=document.getElementById("tzbt").value;
	var zchfsj=document.getElementById("zchfsj").value;
	var tznr=document.getElementById("tznr").value;
	var XGZL=document.getElementById("XGZL").value;
	var hfr=document.getElementById("hfr").value;
	var wjfile=document.getElementById("wjfile").value;
	var dctm=document.getElementById("dctm").value;
	var tzlx = $("select").find("option:selected").val();
	var extend5 = $("input[name='EXTEND5']:checked").val();
	
	var sftz="";
	/* var radio=document.getElementsByName("sftz"); */
	if(XGZL.length>=4000){
		alert("最多支持上传100个文件！");
		return;
	}
	var ggid=$("#ggid").val();
	/* for(var i=0;i<radio.length;i++){
		if(radio[i].checked==true){
			sftz=radio[i].value;
		}
	} */
	/* if(sftz==""){
		document.getElementById("sftzd").style.display="inline";
		return  ;
	}else{
		document.getElementById("sftzd").style.display="none";
	} */
	$.messager.defaults = { ok: "是", cancel: "否" };
	$.messager.confirm('操作提示','是否邮件、短信通知事务办理人?',function(result){
	 	if(result){
			sftz='是';
		}else{
			sftz='否';
		}
	 	var pageUrl="zqb_announcement_save.action";
	 	$("#btnEpForSave").removeAttr("onclick");
	 	$.post(pageUrl,{tzbt:tzbt,zchfsj:zchfsj,tznr:tznr,XGZL:XGZL,hfr:hfr,sftz:sftz,ggid:ggid,tzlx:tzlx,wjfile:wjfile,dctm:dctm,extend5:extend5},function(data){
			var dataJson = eval("(" + data + ")");
	        var flag = dataJson[0].flag;
	        var ggid = dataJson[0].ggid;
	        var hfr = dataJson[0].HFR;
			if(flag!='error'){
				$("#ggid").val(ggid);
				$("#hfr").val(hfr);
				art.dialog.tips("保存成功",2);
				setTimeout('api.zindex().focus()', 500);
				window.location.href="zqb_announcement_notice_add.action?ggid="+ggid;
				/* var url="announcement_notice_list.action?ggid="+ggid;
				$("#iframe").attr("src",url);
				$("#iframe").reload(); */
			}else{
				art.dialog.tips("保存失败",2);
				$("#iframe").reload();
			}
		});
	});
}

function openTzggcx(){
	if($("#tzbz").val()==''||$("#zchfsj").val()==''||$("#NOTICETYPE").val()==''||$("#tznr").val()==''){
		art.dialog.tips("请添加必填信息!",2);
		return;
	}
	$("#btnEpForSave").removeAttr("onclick");
	var pageUrl="zqb_tzggproject_costormer_set.action";
		art.dialog.open(pageUrl,{
		title:'通知人员选择页面',
		loadingText:'正在加载中,请稍后...',
		bgcolor:'#999',
		rang:true,
		width:1200,
		cache:false,
		lock: true,
		height:600, 
		iconTitle:false,
		extendDrag:true,
		autoSize:false,
		close:function(){
			if($("#ggid").val()!=''){
				window.location.href="zqb_announcement_notice_add.action?ggid="+$("#ggid").val();
			}
		}
	});
}

function openXmjbr(){
	if($("#tzbz").val()==''||$("#zchfsj").val()==''||$("#NOTICETYPE").val()==''||$("#tznr").val()==''){
		art.dialog.tips("请添加必填信息!",2);
		return;
	}
	$("#btnEpForSave").removeAttr("onclick");
	var pageUrl="zqb_tzggxmjbr_set.action";
		art.dialog.open(pageUrl,{
		title:'项目经办人选择',
		loadingText:'正在加载中,请稍后...',
		bgcolor:'#999',
		rang:true,
		width:900,
		cache:false,
		lock: true,
		height:600, 
		iconTitle:false,
		extendDrag:true,
		autoSize:false,
		close:function(){
			if($("#ggid").val()!=''){
				window.location.href="zqb_announcement_notice_add.action?ggid="+$("#ggid").val();
			}
		}
	});
}
function doSubmitOne(){
	if($("#tzbz").val()==''||$("#zchfsj").val()==''||$("#NOTICETYPE").val()==''||$("#tznr").val()==''){
		art.dialog.tips("请添加必填信息!",2);
		return;
	}
	$("#btnEpForSave").removeAttr("onclick");
//	$("#btnEpForSave").removeAttr("href");
	/* var valid = mainFormValidator.form(); //执行校验操作
	if(!valid){
		return false;
	} */
	var tzbt=document.getElementById("tzbt").value;
	var zchfsj=document.getElementById("zchfsj").value;
	var tznr=document.getElementById("tznr").value;
	var XGZL=document.getElementById("XGZL").value;
	var hfr=document.getElementById("hfr").value;	
	var wjfile=document.getElementById("wjfile").value;
	var dctm=document.getElementById("dctm").value;
	var extend5 = $("input[name='EXTEND5']:checked").val();
	var ggid=$("#ggid").val();	
	var tzlx = $("select").find("option:selected").val();
	$.messager.defaults = { ok: "确认", cancel: "取消" };
	$.messager.confirm('操作提示','确认选择自己督导的公司?',function(result){
	 	if(result){
	 		var pageUrl="zqb_announcement_save_one.action";
			$.post(pageUrl,{tzbt:tzbt,zchfsj:zchfsj,tznr:tznr,XGZL:XGZL,hfr:hfr,ggid:ggid,tzlx:tzlx,wjfile:wjfile,dctm:dctm,extend5:extend5},function(data){
				var dataJson = eval("(" + data + ")");
		        var flag = dataJson[0].flag;
		        var ggid = dataJson[0].ggid;
		        var hfr = dataJson[0].HFR;
		      //  $("#btnEpForSave").attr("href","javascript:doSubmit();");
				if(flag!='error'){
					$("#ggid").val(ggid);
					setTimeout('api.zindex().focus()', 500);
					window.location.href="zqb_announcement_notice_add.action?ggid="+ggid;
					/* var url="announcement_notice_list.action?ggid="+ggid;
					$("#iframe").attr("src",url);
					$("#iframe").reload(); */
				}
	   		}); 
			}
	});
}

function doSubmitAll(){
	if($("#tzbz").val()==''||$("#zchfsj").val()==''||$("#NOTICETYPE").val()==''||$("#tznr").val()==''){
		art.dialog.tips("请添加必填信息!",2);
		return;
	}
	$("#btnEpForSave").removeAttr("onclick");
	/* var valid = mainFormValidator.form(); //执行校验操作
	if(!valid){
		return false;
	} */
	var tzbt=document.getElementById("tzbt").value;
	var zchfsj=document.getElementById("zchfsj").value;
	var tznr=document.getElementById("tznr").value;
	var XGZL=document.getElementById("XGZL").value;
	var hfr=document.getElementById("hfr").value;	
	var wjfile=document.getElementById("wjfile").value;
	var dctm=document.getElementById("dctm").value;
	var extend5 = $("input[name='EXTEND5']:checked").val();
	var ggid=$("#ggid").val();	
	var tzlx = $("select").find("option:selected").val();
	$.messager.defaults = { ok: "确认", cancel: "取消" };
	$.messager.confirm('操作提示','确认选择所有公司?',function(result){ 
	 	if(result){
	 		var pageUrl="zqb_announcement_save_all.action";
			$.post(pageUrl,{tzbt:tzbt,zchfsj:zchfsj,tznr:tznr,XGZL:XGZL,hfr:hfr,ggid:ggid,tzlx:tzlx,wjfile:wjfile,dctm:dctm,extend5:extend5},function(data){
				var dataJson = eval("(" + data + ")");
		        var flag = dataJson[0].flag;
		        var ggid = dataJson[0].ggid;
		        var hfr = dataJson[0].HFR;
		     //   $("#btnEpForSave").attr("href","javascript:doSubmit();");
				if(flag!='error'){
					$("#ggid").val(ggid);
					setTimeout('api.zindex().focus()', 500);
					window.location.href="zqb_announcement_notice_add.action?ggid="+ggid;
					/* var url="announcement_notice_list.action?ggid="+ggid;
					$("#iframe").attr("src",url);
					$("#iframe").reload(); */
				}
	   		});
			}
	});
}
function doSubmitWGP(){
	if($("#tzbz").val()==''||$("#zchfsj").val()==''||$("#NOTICETYPE").val()==''||$("#tznr").val()==''){
		art.dialog.tips("请添加必填信息!",2);
		return;
	}
	$("#btnEpForSave").removeAttr("onclick");
	/* var valid = mainFormValidator.form(); //执行校验操作
	if(!valid){
		return false;
	} */
	var tzbt=document.getElementById("tzbt").value;
	var zchfsj=document.getElementById("zchfsj").value;
	var tznr=document.getElementById("tznr").value;
	var XGZL=document.getElementById("XGZL").value;
	var hfr=document.getElementById("hfr").value;	
	var wjfile=document.getElementById("wjfile").value;
	var dctm=document.getElementById("dctm").value;
	var extend5 = $("input[name='EXTEND5']:checked").val();
	var ggid=$("#ggid").val();	
	var tzlx = $("select").find("option:selected").val();
	$.messager.defaults = { ok: "确认", cancel: "取消" };
	$.messager.confirm('操作提示','确认选择所有未挂牌公司?',function(result){ 
	 	if(result){
	 		var pageUrl="zqb_announcement_save_wgp.action";
			$.post(pageUrl,{tzbt:tzbt,zchfsj:zchfsj,tznr:tznr,XGZL:XGZL,hfr:hfr,ggid:ggid,tzlx:tzlx,wjfile:wjfile,dctm:dctm,extend5:extend5},function(data){
				var dataJson = eval("(" + data + ")");
		        var flag = dataJson[0].flag;
		        var ggid = dataJson[0].ggid;
		        var hfr = dataJson[0].HFR;
		      //  $("#btnEpForSave").attr("href","javascript:doSubmit();");
				if(flag!='error'){
					$("#ggid").val(ggid);
					setTimeout('api.zindex().focus()', 500);
					window.location.href="zqb_announcement_notice_add.action?ggid="+ggid;
					/* var url="announcement_notice_list.action?ggid="+ggid;
					$("#iframe").attr("src",url);
					$("#iframe").reload(); */
				}
	   		});
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