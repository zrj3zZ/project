<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>IWORK综合应用管理系统</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_css/public.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"> </script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/engine/ifromworkbox.js"> </script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"></script>
<script type="text/javascript" src="iwork_js/bindclick.js"></script>
<script type="text/javascript">
	var mainFormValidator;
	$().ready(function() {
		var rqtype=<s:property value="rqtype"/>;
		if(rqtype=="1"){
			$("#QYRQBEGIN").focus(function(){
				WdatePicker({dateFmt:"yyyy-MM"});
			});
			$("#QYRQEND").focus(function(){
				WdatePicker({dateFmt:"yyyy-MM"});
			});
		}else if(rqtype=="0"){
			$("#QYRQBEGIN").focus(function(){
				WdatePicker({dateFmt:"yyyy"});
			});
			$("#QYRQEND").focus(function(){
				WdatePicker({dateFmt:"yyyy"});
			});
		}
	    $.ajaxSetup({async: false});
		mainFormValidator =  $("#ifrmMain").validate({});
		mainFormValidator.resetForm();
	});
	
	$(function(){
	    //查询
	    $("#search").click(function(){
	    	var valid = mainFormValidator.form(); //执行校验操作
	    	if(!valid){
	    		return false;
	    	}
	        var gsmc = $("#GSMC").val();
	        var ssbm = $("#SSBM").val();
	        var qyrqbegin = $("#QYRQBEGIN").val();
	        var qyrqend = $("#QYRQEND").val();
	        var xylx = $("#xylx option:selected").val();
	    	var xmlx = $("#xmlx option:selected").val();
	    	var rqtype = $('input[name="rqtype"]:checked').val();
	        var seachUrl = encodeURI("sx_loadQianYue.action?gsmc="+gsmc+"&ssbm="+ssbm+"&qyrqbegin="+qyrqbegin+"&qyrqend="+qyrqend+"&xylx="+xylx+"&xmlx="+xmlx+"&rqtype="+rqtype);
	        window.location.href = seachUrl;
	    });
    });
		
	function edit(instanceid){
		var pageUrl = "openFormPage.action?formid=88&demId=21&instanceId="+instanceid;
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
	}
		
	function addItem(formId,demId){
		var pageUrl = "createFormInstance.action?formid="+formId+"&demId="+demId;
   		var target = "_blank";
   		var win_width = window.screen.width;
   		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
   		page.location = pageUrl;
	}
		
	function remove(instanceid){
		if(confirm("确定执行删除操作吗?")) {
			var pageUrl = "sx_removeQianYue.action";
			$.post(pageUrl,{instanceid:instanceid},function(data){ 
		       			if(data=='success'){
		       				window.location.reload();
		       			}else{
		       				alert("删除失败。");
		       			} 
		     }); 
		}
	}
	
	function addGugai(actDefId,customername,customerno){
   		var url = 'processRuntimeStartInstance.action?actDefId='+actDefId+'&YGSMC='+encodeURI(customername)+'&CUSTOMERNO='+encodeURI(customerno);
   		var target = "_blank";
   		var win_width = window.screen.width;
   		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
   		page.location = url;
   		return;
	}
	
	function editGugai(lcbh,lcbs,taskid) {
		var url="loadProcessFormPage.action?actDefId="+lcbh+"&instanceId="+lcbs+"&excutionId="+lcbs+"&taskId="+taskid;
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width=' + win_width +',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
	}
	
	function addFormPage(formId,demId){
		var url = 'createFormInstance.action?formid=' + formId + '&demId=' + demId;
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width +',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
	}
	
	function editFormPage(instanceId,formId,demId){
		var url = 'openFormPage.action?instanceId=' + instanceId + '&formid='+ formId + '&demId=' + demId;
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width +',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
	}
	
	function addProcessFormPage(instanceId,projectNo){
		var url="";
		var win_width = window.screen.width;
		if (confirm('是否提交项目,提交后项目数据不可更改。')) {
			$.ajax({
		        url : 'zqb_project_commitProject.action',
		        async : false,
		        type : "POST",
		        data: {
		        	instanceid:instanceid,
		        	projectNo:projectNo
                },
		        dataType : "json",
		        success : function(data) {  
		        	var executionId=data.executionId;
					var taskid=data.taskid;
					var actDefId=data.actDefId;
					var instanceId=data.instanceId;
					url = 'url:loadProcessFormPage.action?actDefId='+actDefId+'&instanceId='+instanceId+'&excutionId='+executionId+'&taskId='+taskid;
		        }  
		    });
			var target = "_blank";
			var win_width = window.screen.width;
			var page = window.open('form/loader_frame.html',target,'width='+win_width +',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location = url;
		}
	}
	
	function edit(instanceid,formId,demId){
		var pageUrl = "openFormPage.action?formid="+formId+"&demId="+demId+"&instanceId="+instanceid;
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
	}
	
	function expQianYue(){
		var valid = mainFormValidator.form(); //执行校验操作
    	if(!valid){
    		return false;
    	}
        var gsmc = $("#GSMC").val();
        var ssbm = $("#SSBM").val();
        var qyrqbegin = $("#QYRQBEGIN").val();
        var qyrqend = $("#QYRQEND").val();
        var xylx = $("#xylx option:selected").val();
    	var xmlx = $("#xmlx option:selected").val();
    	var rqtype = $('input[name="rqtype"]:checked').val();
        var seachUrl = encodeURI("sx_expQianYue.action?gsmc="+gsmc+"&ssbm="+ssbm+"&qyrqbegin="+qyrqbegin+"&qyrqend="+qyrqend+"&xylx="+xylx+"&xmlx="+xmlx+"&rqtype="+rqtype);
        window.location.href = seachUrl;
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

.header td {
	height: 35px;
	font-size: 12px;
	padding: 3px;
	white-space: nowrap;
	padding-left: 5px;
	background: url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;
	border-top: 1px dotted #ccc;
	border-right: 1px solid #eee;
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
</style>
<style> 
	pre {
		width:200;
   		overflow: hidden; 
		white-space: pre-wrap; /* css-3 */
		white-space: -moz-pre-wrap; /* Mozilla, since 1999 */
		white-space: -pre-wrap; /* Opera 4-6 */
		white-space: -o-pre-wrap; /* Opera 7 */
		word-wrap: break-word; /* Internet Explorer 5.5+ */
	}
</style>
</head>
<body class="easyui-layout">
	<div region="north" border="false">
		<div class="tools_nav">
			<a href="javascript:addItem(<s:property value="xmqyFormId" />,<s:property value="xmqyDemId" />);" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加</a>
			<a href="javascript:expQianYue();" class="easyui-linkbutton" plain="true" iconCls="icon-edit">导出</a>
			<a href="javascript:window.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		</div>
	</div>
	<div region="center" style="padding-left:0px;padding-right:0px;border:0px;background-position:top;">
			<form name='ifrmMain' id='ifrmMain' method="post">
				<div style="padding:5px;text-align:center;">
					<div style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE; width:99%">
						<table width='100%' border='0' cellpadding='0' cellspacing='0'>
							<tr>
								<td style='padding-top:10px;padding-bottom:10px;'>
									<table width='100%' border='0' cellpadding='0' cellspacing='0'>
										<tr>
											<td class="searchtitle">公司名称</td>
											<td class="searchdata">
												<input type='text' class='{maxlength:128,required:false,string:true}' style="width:250px" name='GSMC' id='GSMC' value='${gsmc}'>
											</td>
											<td class="searchtitle">所属部门</td>
											<td class="searchdata">
												<input type='text' class='{maxlength:64,required:false,string:true}' style="width:100px" name='SSBM' id='SSBM' value='${ssbm}'>
											</td>
											<td class="searchtitle">签约日期</td>
											<td class="searchdata"><input type='text'
												class='{maxlength:64,required:false,string:true}' style="width:80px;"
												name='QYRQBEGIN' id='QYRQBEGIN' value='${qyrqbegin}'> 到 <input
												type='text' 
												class='{maxlength:64,required:false,string:true}' style="width:80px;"
												name='QYRQEND' id='QYRQEND' value='${qyrqend}'/>
												<s:if test="rqtype==0">
													<input type="radio" name="rqtype" value="0" checked="checked"/>年&nbsp;&nbsp;
												</s:if>
												<s:else>
													<input type="radio" name="rqtype" value="0" />年&nbsp;&nbsp;
												</s:else>
												<s:if test="rqtype==1">
													<input type="radio" name="rqtype" value="1" checked="checked"/>月&nbsp;&nbsp;
												</s:if>
												<s:else>
													<input type="radio" name="rqtype" value="1" />月&nbsp;&nbsp;
												</s:else>
											</td>
										</tr>
										<tr>
											<td class="searchtitle">协议类型</td>
											<td class="searchdata">
												<s:select list="{'改制融资推荐挂牌财务顾问协议','保密协议','定增财务顾问协议','并购财务顾问协议','财务顾问协议','推荐挂牌并持续督导协议书','推荐挂牌并持续督导协议书之补充协议','其他'}" theme="simple" headerKey="" headerValue="-空-" name="xylx" id="xylx"></s:select>
											</td>
											<td class="searchtitle">项目类型</td>
											<td class="searchdata">
												<s:select list="{'挂牌项目','督导项目','并购项目','定增项目','其他'}" theme="simple" headerKey="" headerValue="-空-" name="xmlx" id="xmlx"></s:select>
											</td>
										</tr>
									</table>
								<td>
								<td valign='bottom' style='padding-bottom:5px;'>
									<a id="search" class="easyui-linkbutton" icon="icon-search" href="javascript:void(0);">
										查询
									</a>
								</td>
							<tr>
						</table>
					</div>
				</div>
			</form>
		<div style="padding:5px;text-align:center;">
			<table id='iform_grid' width="99%" style="border:1px solid #efefef;">
				<tr class="header">
					<td style="width:2%;text-align: center;">序号</td>
					<td style="width:4%;text-align: center;">月份</td>
					<td style="width:10%;text-align: center;">公司名称</td>
					<td style="width:10%;text-align: center;">协议类型</td>
					<td style="width:5%;text-align: center;">项目类型</td>
					<td style="width:5%;text-align: center;">协议金额(万元)</td>
					<td style="width:5%;text-align: center;">督导金额(万元)</td>
					<td style="width:5%;text-align: center;">收到协议日期</td>
					<td style="width:5%;text-align: center;">签署协议日期</td>
					<td style="width:5%;text-align: center;">流程结束日期</td>
					<td style="width:5%;text-align: center;">部门</td>
					<td style="width:5%;text-align: center;">经办人</br>(流程提交人)</td>
					<td style="width:5%;text-align: center;"">付款节点</td>
					<td style="width:4%;text-align: center;">操作</td>
				</tr>
				<s:iterator value="contractList" status="status">
					<s:if test="MONTHRQ!=contractList[#status.index-1].MONTHRQ">
						<tr class="cell">
							<td colspan="5" style="text-align: center;font-weight: bold;"><s:property value="contractList[#status.index-1].MONTHRQ" />月合计</td>
							<td style="text-align: center;"><s:property value="contractList[#status.index-1].MXYJE" /></td>
							<td style="text-align: center;"></td>
							<td style="text-align: center;"></td>
							<td style="text-align: center;"></td>
							<td style="text-align: center;"></td>
							<td style="text-align: center;"></td>
							<td style="text-align: center;"></td>
							<td style="text-align: center;"></td>
							<td style="text-align: center;"></td>
						</tr>
					</s:if>
					<s:if test="YEAR!=contractList[#status.index-1].YEAR">
						<tr class="cell">
							<td colspan="5" style="text-align: center;font-weight: bold;"><s:property value="contractList[#status.index-1].YEAR" />年度合计</td>
							<td style="text-align: center;"><s:property value="contractList[#status.index-1].YXYJE" /></td>
							<td style="text-align: center;"></td>
							<td style="text-align: center;"></td>
							<td style="text-align: center;"></td>
							<td style="text-align: center;"></td>
							<td style="text-align: center;"></td>
							<td style="text-align: center;"></td>
							<td style="text-align: center;"></td>
							<td style="text-align: center;"></td>
						</tr>
					</s:if>
					<tr class="cell">
							<td style="text-align: center;"><s:property value="#status.count" /></td>
							<td style="text-align: center;"><s:property value="MONTHRQ" /></td>
							<td style="text-align: center;"><s:property value="GSMC" /></td>
							<td style="text-align: center;"><s:property value="XYLX" /></td>
							<td style="text-align: center;"><s:property value="XMLX" /></td>
							<td style="text-align: center;"><s:property value="XYJE" /></td>
							<td style="text-align: center;"><s:property value="DDJE" /></td>
							<td style="text-align: center;"><s:property value="SDXYRQ" /></td>
							<td style="text-align: center;"><s:property value="QSXYRQ" /></td>
							<td style="text-align: center;"><s:property value="LCJSRQ" /></td>
							<td style="text-align: center;"><s:property value="SSBM" /></td>
							<td style="text-align: center;"><s:property value="JBR" /></td>
							<td style="text-align: left;"><pre><s:property value="FKJD" /></pre></td>
							<td style="text-align: center;"><a href="javascript:edit(<s:property value="INSTANCEID" />,<s:property value="xmqyFormId" />,<s:property value="xmqyDemId" />);">编辑</a>|<a href="javascript:remove(<s:property value="INSTANCEID" />);">删除</a></td>
					</tr>
					<s:if test="#status.index==contractList.size()-1">
						<tr class="cell">
							<td colspan="5" style="text-align: center;font-weight: bold;"><s:property value="MONTHRQ" />月合计</td>
							<td style="text-align: center;"><s:property value="MXYJE" /></td>
							<td style="text-align: center;"></td>
							<td style="text-align: center;"></td>
							<td style="text-align: center;"></td>
							<td style="text-align: center;"></td>
							<td style="text-align: center;"></td>
							<td style="text-align: center;"></td>
							<td style="text-align: center;"></td>
							<td style="text-align: center;"></td>
						</tr>
						<tr class="cell">
							<td colspan="5" style="text-align: center;font-weight: bold;"><s:property value="YEAR" />年度合计</td>
							<td style="text-align: center;"><s:property value="YXYJE" /></td>
							<td style="text-align: center;"></td>
							<td style="text-align: center;"></td>
							<td style="text-align: center;"></td>
							<td style="text-align: center;"></td>
							<td style="text-align: center;"></td>
							<td style="text-align: center;"></td>
							<td style="text-align: center;"></td>
							<td style="text-align: center;"></td>
						</tr>
					</s:if>
				</s:iterator>
			</table>

			<form action="sx_loadCustomer.action" method="post" name="frmMain" id="frmMain">
				<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
				<s:hidden name="pageSize" id="pageSize"></s:hidden>
				<s:hidden name="customerno" id="customerno"></s:hidden>
				<s:hidden name="zqdm" id="zqdm"></s:hidden>
				<s:hidden name="zqjc" id="zqjc"></s:hidden>
				<s:hidden name="customername" id="customername"></s:hidden>
				<s:hidden name="type" id="type"></s:hidden>
				<s:hidden name="zwmc" id="zwmc"></s:hidden>
				<s:hidden name="zczbbegin" id="zczbbegin"></s:hidden>
				<s:hidden name="zczbend" id="zczbend"></s:hidden>
				<s:hidden name="gfgsrqbegin" id="gfgsrqbegin"></s:hidden>
				<s:hidden name="gfgsrqend" id="gfgsrqend"></s:hidden>
			</form>
			<div id='prowed_ifrom_grid'></div>
		</div>
	</div>
	<div region="south"
		style="vertical-align:bottom;height:60px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;"
		border="false">
		<div style="padding:5px">
			<s:if test="totalNum==0">

			</s:if>
			<s:else>
				<div id="pp" style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
			</s:else>
		</div>
	</div>
</body>
</html>
<!-- 新增查询过滤SQL注入关键字 -->
<script language="JavaScript"> 
$().ready(function() {
	$(":radio").click(function(){
		var value = $(this).val();
		if(value=='1'){
			$("#QYRQBEGIN").unbind();
			$("#QYRQBEGIN").val("");
			$("#QYRQEND").unbind();
			$("#QYRQEND").val("");
			$("#QYRQBEGIN").focus(function(){
				WdatePicker({dateFmt:"yyyy-MM"});
			});
			$("#QYRQEND").focus(function(){
				WdatePicker({dateFmt:"yyyy-MM"});
			});
		}else if(value=='0'){
			$("#QYRQBEGIN").unbind();
			$("#QYRQBEGIN").val("");
			$("#QYRQEND").unbind();
			$("#QYRQEND").val("");
			$("#QYRQBEGIN").focus(function(){
				WdatePicker({dateFmt:"yyyy"});
			});
			$("#QYRQEND").focus(function(){
				WdatePicker({dateFmt:"yyyy"});
			});
		}
	});
});
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