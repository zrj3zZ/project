<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>


<title>签到</title>




<link rel="stylesheet" type="text/css"
	href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css"
	href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_css/public.css" />
<link rel="stylesheet" type="text/css"
	href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
<link rel="stylesheet" type="text/css"
	href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
<link rel="stylesheet" type="text/css"
	href="iwork_css/engine/iformpage.css" />
<link rel="stylesheet" type="text/css"
	href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css"
	href="iwork_css/engine/sysenginemetadata.css">
<link rel="stylesheet" type="text/css"
	href="iwork_themes/easyui/gray/easyui.css">
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript"
	src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript"
	src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="iwork_js/jqueryjs/languages/grid.locale-cn.js"></script>
<script type="text/javascript"
	src="iwork_js/jqueryjs/jquery.jqGrid.min.js"> </script>
<script type="text/javascript"
	src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/engine/ifromworkbox.js"> </script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript"
	src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript"
	src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript"
	src="iwork_js/jqueryjs/languages/messages_cn.js"></script>
<script type="text/javascript" src="iwork_js/bindclick.js"></script>
<script type="text/javascript">
	var mainFormValidator;
	$().ready(function() {
		$.ajaxSetup({
			async: false
		});
		mainFormValidator =  $("#ifrmMain").validate({});
		mainFormValidator.resetForm();
	});
	
	function submitMsg(pageNumber,pageSize){
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#frmMain").submit();
		return ;
	}
	$(function(){
		//分页
		$('#pp').pagination({
		    total:<s:property value="totalNum"/>,  
		    pageNumber:<s:property value="pageNumber"/>,
		    pageSize:<s:property value="pageSize"/>,
		    onSelectPage:function(pageNumber, pageSize){
		    	submitMsg(pageNumber,pageSize);
		    }
		});
	    //查询
	    $("#search").click(function(){
	    	var valid = mainFormValidator.form(); //执行校验操作
	    	if(!valid){
	    		return;
	    	}
	    	var enddate = $("#ENDDATE").val();
			var startdate = $("#STARTDATE").val();
			var username = $("#username").val();
			
	        var seachUrl = encodeURI("zqb_dkindex_byuserid.action?startdate="+startdate+"&enddate="+enddate+"&username="+username);
	        window.location.href = seachUrl;
	    });
	   
    });
		
        function expPro(){
    		var totle = <s:property value="totalNum"/>;
    		if(totle==0){
    			alert("没有签到记录!");
    			return;
    		}
    		
    		var enddate = $("#ENDDATE").val();
			var startdate = $("#STARTDATE").val();
			var username = $("#username").val();
    		var pageUrl = encodeURI("zqb_exbyuserid.action?startdate="+startdate+"&enddate="+enddate+"&username="+username);
    		$("#editForm").attr("action", pageUrl);
    		$("#editForm").submit();
    	}
</script>
<style>
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
	background: url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left
		bottom;
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
	text-align:left;
	border-bottom: 1px dotted #eee;
	border-top: 1px dotted #fff;
	border-right: 1px dotted #eee;
}

.cell:hover {
	background-color: #F0F0F0;
}
</style>
</head>

<body class="easyui-layout">
	<!-- <div region="north" border="false">
		<div class="tools_nav">
			<a href="javascript:addItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加</a>
			<a href="javascript:remove();" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除</a>
			<a href="javascript:window.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		</div>
	</div> -->
	<div region="center" style="padding-left:0px;padding-right:0px;border:0px;background-position:top">
	<s:form id="editForm" name="editForm" theme="simple"></s:form>
		<form name='ifrmMain' id='ifrmMain' method="post">
			<div style="padding:5px;text-align:center;">
				<div style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE; width:99%">
				<table width='100%' border='0' cellpadding='0' cellspacing='0'>
					<tr>
						<td style='padding-top:10px;padding-bottom:10px;'>
							<table width='100%' border='0' cellpadding='0' cellspacing='0'>
								<tr>
									
									<td > </td> 
									<td   >
										签到时间:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type='text' onfocus="var ENDDATE=$dp.$('ENDDATE');WdatePicker({onpicked:function(){ENDDATE.focus();},maxDate:'#F{$dp.$D(\'ENDDATE\')}'})"  style="width:100px" name='STARTDATE' id='STARTDATE'  value='' >
										&nbsp;&nbsp;到&nbsp;&nbsp;<input type='text' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'STARTDATE\')}'})" onchange="checkRQ()" style="width:80px" name='ENDDATE' id='ENDDATE'  value=''  >
									</td>
									<%-- <td class="searchtitle">在岗状态</td>
									<td class="searchdata">
										<select name='WORKSTATUS' id='WORKSTATUS'>
												<option value=''>空</option>
												<option value='在岗'>在岗</option>
												<option value='离岗'>离岗</option>
										</select>
									</td> --%>
									
								</tr>
							</table>
						<td>
						<td valign='bottom' style='padding-bottom:5px;'>
							<a id="search" class="easyui-linkbutton" icon="icon-search" href="javascript:void(0);">查询</a>
							<!-- <a href="javascript:expPro();" class="easyui-linkbutton" plain="true" iconCls="icon-excel-exp">导出</a> -->
						</td>
					<tr>
				</table>
				</div>
			</div>
		</form>
		<div style="padding:5px;text-align:center;">
			<table id='iform_grid' width="99%" style="border:1px solid #efefef">
				<tr class="header">
					<!-- <td style="width:5%;" class="header"><input type="checkbox" name="colname" id="chkAll" /></td> -->
					<td style="width:10%;">姓名</td>
					<td style="width:15%;">日期</td>
					<td style="width:15%;">上班签到</td>
					<td style="width:25%;">下班签到</td>
					<td style="width:35%;">备注说明</td>
					
				</tr>
				
					<s:iterator value="pagedList" status="status">
					<tr class="cell">
						
						<td>
							<s:property value='USERNAME'/>
						</td>
						<td>
							<s:property value='checkintime'/>
						</td>
						<s:if test="exception_typei=='时间异常'">
						<td><span style="color:red;"><s:property value="checkin_timei" /><span></td>
						</s:if><s:else>
						<td><s:property value="checkin_timei" /></td>
						</s:else>
						<s:if test="exception_typej=='时间异常'">
						<td ><span style="color:red;"><s:property value="checkin_timej"  /></span></td>
						</s:if><s:else>
						<td><s:property value="checkin_timej" /></td>
						</s:else>
						<td><s:property value="notes" /></td>
						
						
					</tr>
				</s:iterator>
			</table>

			
			<form action="zqb_dkindex_byuserid.action" method=post name=frmMain id=frmMain>
				<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
				<s:hidden name="pageSize" id="pageSize"></s:hidden>
				<s:hidden name="enddate" id="enddate"></s:hidden>
				<s:hidden name="startdate" id="startdate"></s:hidden>
				<s:hidden name="username" id="username"></s:hidden>
				<s:hidden name="departmentname" id="departmentname"></s:hidden>
				
				<s:hidden name="userid" id="userid"></s:hidden>
			</form>
			<div id='prowed_ifrom_grid'></div>
		</div>
		</div>
	</div>
	<div region="south" style="vertical-align:bottom;height:60px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;" border="false">
		<div style="padding:5px">
			<s:if test="totalNum==0">
			</s:if>
			<s:else>
				<div id="pp" style="background:#efefef;text-align:right;border:1px solid #ccc;">
				</div>
			</s:else>
		</div>
	</div>
	
	<script type="text/javascript">
	$(function(){
 		$("#STARTDATE").val($("#startdate").val());
		$("#ENDDATE").val($("#enddate").val());
		
	});
	</script>

	
	



	

</body>

</html>
