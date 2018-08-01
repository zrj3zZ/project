<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
<script type="text/javascript" src="iwork_js/bindclick.js"  ></script>
<script type="text/javascript">
	var mainFormValidator;
	$().ready(function() {
		mainFormValidator =  $("#ifrmMain").validate({});
		mainFormValidator.resetForm();
	});
	
	function openFormPage(instanceId,formId,demId){
		var url = 'openFormPage.action?formid='+formId+'&instanceId='+instanceId+'&demId='+demId;
		var target = "dem"+instanceId;
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url; 
	}
	
	function addFormPage(formId,demId,projectno,projectname,nhspr){
		var url = encodeURI("createNhData.action?nhformid="+formId+"&nhid="+demId+"&projectno="+projectno+"&projectname="+projectname+"&nhspr="+nhspr);
		$.ajax({
		    type:'POST',
		    async:false,
		    url:url,
		    success:function(data){
		    	if(data!=""){
		    		var json = (new Function("return " + data))();
		    		var instanceid = json.instanceid;
		    		var nhformid = json.formid;
		    		var nhdemId = json.demId;
		    		var openFormPageUrl='openFormPage.action?formid='+nhformid+'&instanceId='+instanceid+'&demId='+nhdemId;
					var target = "dem"+nhformid;
					var win_width = window.screen.width;
					var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
					page.location = openFormPageUrl;
		    	}
		    }
		});
	}
	
	function submitMsg(pageNumber,pageSize){
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#frmMain").submit();
		return ;
	}
	$(function(){
		//分页
		$('#pp').pagination({
		    total:<s:property value="nhlistsize"/>,  
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
	    	return false;
	    	}
	    	var pjname = $("#PJNAME").val();
	    	var jdmc = $("#jdmc").val();
	        var seachUrl = encodeURI("zqb_project_getNhList.action?pjname="+pjname+"&jdmc="+jdmc);
	        window.location.href = seachUrl;
	    });
	    $(document).bind('keydown', function(event) {
			if (event.keyCode == "13") {
				//禁用键盘按钮按下事件
				return false;
			}
		});
    });
	//导出
	function expNH(){
		var pjname = $("#pjname").val();
    	var jdmc = $("#jdmc").val();
		var pageUrl = encodeURI("zqb_project_expNh.action?pjname="+pjname+"&jdmc="+jdmc);
		window.location.href = pageUrl; 
	}
	
	function loadProject(lcbh,lcbs,taskid) {
		var url="loadProcessFormPage.action?actDefId="+ lcbh + "&instanceId="+ lcbs + "&excutionId="+ lcbs +"&taskId="+taskid;
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+ win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
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
			<a href="javascript:window.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
			<a href="javascript:expNH();" class="easyui-linkbutton" plain="true" iconCls="icon-excel-exp">导出</a>
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
										<td class="searchtitle">项目名称</td>
										<td class="searchdata"><input type='text'
											class='{maxlength:128,required:false,string:true}' style="width:100px"
											name='PJNAME' id='PJNAME' value='${pjname}'></td>
										<td class="searchtitle">阶段名称</td>
										<td class="searchdata">
											<s:select list="jdmcList" theme="simple" headerKey="" headerValue="-空-" name="jdmc" id="jdmc"></s:select>
										</td>
									</tr>
								</table>
							<td>
							<td valign='bottom' style='padding-bottom:5px;'><a
								id="search" class="easyui-linkbutton" icon="icon-search"
								href="javascript:void(0);">查询</a></td>
						<tr>
					</table>
				</div>
			</div>
		</form>
		<div style="padding:5px;text-align:center;">
			<table id='iform_grid' width="99%" style="border:1px solid #efefef">
				<tr class="header">
					<td style="width:2%;">序号</td>
					<td style="width:20%;">项目名称</td>
					<td style="width:5%;">部门负责人</td>
					<td style="width:5%;">阶段名称</td>
					<td style="width:5%;">提交人</td>
					<td style="width:5%;">呈报时间</td>
					<td style="width:5%;">内核日期</td>
					<td style="width:5%;">操作</td>
				</tr>
				<s:iterator value="nhlist" status="status">
					<tr class="cell">
						<td><s:property value="ROWNUM"/></td>
						<td>
							<a href="javascript:void(0)" onclick="loadProject('<s:property value='LCBH'/>',<s:property value='LCBS'/>,<s:property value='TASKID'/>)">
								<s:property value="PJNAME"/>
							</a>
						</td>
						<td><s:property value="OWNER" /></td>
						<td><s:property value="JDMC" /></td>
						<td><s:property value="LCCREATEUSER" /></td>
						<td><s:property value="NHCBSJ" /></td>
						<td><s:property value="NHDATE" /></td>
						<s:if test="NID==0">
							<td aria-describedby="iform_grid__OPERATE" title="设置内核日期" style="" role="gridcell">
								<a href="javascript:void(0)" style="color: #0000ff;" onclick="addFormPage(<s:property value='nhformid'/>,<s:property value='nhid'/>,'<s:property value='PJPRONO'/>','<s:property value='PJNAME'/>','<s:property value='SZNHSPR'/>')">
									设置内核日期
								</a>
							</td>
						</s:if>
						<s:else>
							<td aria-describedby="iform_grid__OPERATE" title="设置内核日期" style="" role="gridcell">
								<a href="javascript:void(0)" style="color: #0000ff;" onclick="openFormPage(<s:property value='NINSTANCEID'/>,<s:property value='nhformid'/>,<s:property value='nhid'/>)">
									设置内核日期
								</a>
							</td>
						</s:else>
					</tr>
				</s:iterator>
			</table>
			<form action="zqb_project_getNhList.action" method="post" name="frmMain" id="frmMain">
				<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
				<s:hidden name="pageSize" id="pageSize"></s:hidden>
				<s:hidden name="pjname" id="pjname"></s:hidden>
				<s:hidden name="jdmc" id="jdmc"></s:hidden>
			</form>
			<div id='prowed_ifrom_grid'></div>
		</div>
	</div>
	<div region="south" style="vertical-align:bottom;height:60px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;"	border="false">
		<div style="padding:5px">
			<s:if test="nhlistsize==0">
			</s:if>
			<s:else>
				<div id="pp" style="background:#efefef;text-align:right;border:1px solid #ccc;">
				</div>
			</s:else>
		</div>
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
