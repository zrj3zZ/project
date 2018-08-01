<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=8" >
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
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
<script type="text/javascript" src="iwork_js/bindclick.js"  ></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
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
	    	var username = $("#USERNAME").val();
			var departmentname = $("#DEPARTMENTNAME").val();
			var workstatus = $("#WORKSTATUS").val();
	        var seachUrl = encodeURI("zqb_leave_index.action?username="+username+"&departmentname="+departmentname+"&workstatus="+workstatus);
	        window.location.href = seachUrl;
	    });
	    //复选/全选
        $("#chkAll").bind("click", function () {
        	$("[name =colname]:checkbox").attr("checked", this.checked);
        });
    });
		
	function checkLeave(instanceId,excutionId,taskId){
		if(instanceId==''||excutionId==''||taskId==''){
			art.dialog.tips("流程信息出错,请联系管理员!",2);
			return;
		}
		var qjlcServer = $("#qjlcServer").val();
   		var pageUrl = "loadProcessFormPage.action?actDefId="+qjlcServer+"&instanceId="+instanceId+"&excutionId="+excutionId+"&taskId="+taskId;
   		var target = "_blank";
   		var win_width = window.screen.width;
   		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
   		page.location = pageUrl;
	}
	function sickLeave(instanceId){
		var url = "zqb_sickleave_dialog.action?instanceid="+instanceId;
		art.dialog.open(url,{
			title:'录入销假日期',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:1000,
			cache:false,
			lock: true,
			height:300, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			close:function(){
				window.location.reload();
			}
		});	
	}
	
	function openFormPage(instanceId){
		$.messager.confirm('确认','确认销假?',function(result){
			if(result){
				var url = "zqb_sick_leave.action";
				$.post(url,{instanceid:instanceId},function(data){
					if(data=='success'){
						window.location.reload();
					}else{
						art.dialog.tips("销假失败,请联系管理员!",2);
					}
				});
			}
		});
	}
	
	function addItem(){
		var qjlcServer = $("#qjlcServer").val();
		var pageUrl = "processRuntimeStartInstance.action?actDefId="+qjlcServer;
   		var target = "_blank";
   		var win_width = window.screen.width;
   		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
   		page.location = pageUrl;
	}
	function listLeaveByUserid(userid,username){
		if(userid==null||userid==''){
			art.dialog.tips(username+"还未请过假!",2);
			return;
		}
		var pageUrl = "zqb_leave_indexbyuserid.action?userid="+userid;
   		var target = "_blank";
   		var win_width = window.screen.width;
   		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
   		page.location = pageUrl;
	}
	function delLeave(instanceid){
		$.messager.confirm('确认','确认删除?',function(result){
			if(result){
				$.post("zqb_leave_del.action",{instanceid:instanceid},function(data){
					if(data=='success'){
						art.dialog.tips("删除成功!",2);
						window.location.reload();
					}else{
						alert('删除失败,请联系管理员!');
					}
				});
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
	text-align: left;
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
	<div region="north" border="false">
		<div class="tools_nav">
			<a href="javascript:addItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加</a>
			<!-- <a href="javascript:remove();" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除</a> -->
			<a href="javascript:window.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		</div>
	</div>
	<div region="center" style="padding-left:0px;padding-right:0px;border:0px;background-position:top">
		<form name='ifrmMain' id='ifrmMain' method="post">
			<div style="padding:5px;text-align:center;">
				<div style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE; width:99%">
				<table width='100%' border='0' cellpadding='0' cellspacing='0'>
					<tr>
						<td style='padding-top:10px;padding-bottom:10px;'>
							<table width='100%' border='0' cellpadding='0' cellspacing='0'>
								<tr>
									<td class="searchtitle">姓名</td>
									<td class="searchdata">
										<input type='text' class='{maxlength:128,required:false,string:true}' style="width:100px" name='USERNAME' id='USERNAME' value=''>
									</td>
									<td class="searchtitle">部门</td>
									<td class="searchdata">
										<input type='text' class='{maxlength:64,required:false,string:true}' style="width:100px" name='DEPARTMENTNAME' id='DEPARTMENTNAME' value=''>
									</td>
									<td class="searchtitle">在岗状态</td>
									<td class="searchdata">
										<select name='WORKSTATUS' id='WORKSTATUS'>
												<option value=''>空</option>
												<option value='在岗'>在岗</option>
												<option value='离岗'>离岗</option>
										</select>
									</td>
									<td class="searchtitle"></td>
									<td class="searchdata"></td>
								</tr>
							</table>
						<td>
						<td valign='bottom' style='padding-bottom:5px;'>
							<a id="search" class="easyui-linkbutton" icon="icon-search" href="javascript:void(0);">查询</a>
						</td>
					<tr>
				</table>
				</div>
			</div>
		</form>
		<div style="padding:5px;text-align:center;">
			<table id='iform_grid' width="99%" style="border:1px solid #efefef">
				<tr class="header">
					<td style="width:5%;" class="header"><input type="checkbox" name="colname" id="chkAll" /></td>
					<td style="width:5%;">姓名</td>
					<td style="width:10%;">联系方式</td>
					<td style="width:10%;">部门名称</td>
					<td style="width:12.5%;">部门负责人及电话</td>
					<td style="width:12.5%;">交接人及电话</td>
					<td style="width:10%;">请假时间</td>
					<td style="width:10%;">审批状态</td>
					<td style="width:10%;">在岗状态</td>
					<td style="width:15%;">操作</td>
				</tr>
				<s:iterator value="qjlcList" status="status">
					<tr class="cell">
						<td style="width:05%;">
							<input type="checkbox" id="<s:property value='INSTANCEID'/>" name="colname" value="<s:property value='INSTANCEID'/>" />
						</td>
						<td>
							<a href="javascript:listLeaveByUserid('<s:property value="PROJECTNO"/>','<s:property value="USERNAME"/>')" style="color:blue;"><s:property value='USERNAME'/>[<s:property value='USERID'/>]</a>
						</td>
						<td><s:property value='MOBILE'/></td>
						<td><s:property value="DEPARTMENTNAME" /></td>
						<td><s:property value="DUTIER" escapeHtml="false" /></td>
						<td>
							<s:if test="(MANAGER==null||MANAGER=='')&&(SXYZLMB==null||SXYZLMB=='')"></s:if>
							<s:else>
								<s:property value="MANAGER" /><s:property value="SXYZLMB" />
							</s:else>
						</td>
						<td>
							<s:if test="STARTDATE!=null&&STARTDATE!=''&&SCALE!=null&&SCALE!=''&&ENDDATE!=null&&ENDDATE!=''&&GROUPID!=null&&GROUPID!=''">
								<s:property value="STARTDATE" /> <s:property value="SCALE" />--<s:property value="ENDDATE" /> <s:property value="GROUPID" />
							</s:if>
						</td>
						<td><s:property value="SPZT" /></td>
						<td>
						<s:if test="TASK_NAME==''||TASK_NAME==null">
							<s:if test="SPZT=='审批通过'">
								离岗
							</s:if><s:else>
								在岗
							</s:else>
						</s:if>
						<s:else>
							在岗
						</s:else>
						</td>
						<td>
							<s:if test="INSTANCEID!=0">
								<s:if test="SHOWBUTTON==true&&SPZT=='审批通过'"><!-- 自己不能销假 -->
									<s:if test="TASK_NAME!=null&&TASK_NAME!=''">
										已销假
									</s:if>
									<s:else>
										<a href="javascript:sickLeave('<s:property value="INSTANCEID"/>')" style="color:blue;"><u>销假</u></a>
									</s:else>
									&nbsp;|&nbsp;
								</s:if>
								<a href="javascript:checkLeave('<s:property value="INSTANCEID"/>','<s:property value="LCBH"/>','<s:property value="RWID"/>')" style="color:blue;"><u>查看详情</u></a>
								<s:if test="SPZT!='审批通过'&&SHOWBUTTON==false">
									&nbsp;|&nbsp;<a href="javascript:delLeave('<s:property value="INSTANCEID"/>')" style="color:blue;"><u>删除</u></a>
								</s:if>
							</s:if>
						</td>
					</tr>
				</s:iterator>
			</table>

			<form action="zqb_leave_index.action" method=post name=frmMain id=frmMain>
				<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
				<s:hidden name="pageSize" id="pageSize"></s:hidden>
				<s:hidden name="qjlcServer" id="qjlcServer"></s:hidden>
				<s:hidden name="username" id="username"></s:hidden>
				<s:hidden name="departmentname" id="departmentname"></s:hidden>
				<s:hidden name="workstatus" id="workstatus"></s:hidden>
				<s:hidden name="userid" id="userid"></s:hidden>
			</form>
			<div id='prowed_ifrom_grid'></div>
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
		$("#USERNAME").val($("#username").val());
		$("#DEPARTMENTNAME").val($("#departmentname").val());
		$("#WORKSTATUS").val($("#workstatus").val());
	});
	</script>
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