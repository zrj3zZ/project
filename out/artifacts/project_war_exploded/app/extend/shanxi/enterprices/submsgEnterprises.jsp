<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=8" >
<title>员工绩效详情</title>
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
	    	return false;
	    	}
	        var fullname = $("#FULLNAME").val();
	        var model = $("#MODEL").val();
	        var orderby = $("#orderby").val();
	        var starttime = $("#STARTTIME").val();
	        var endtime = $("#ENDTIME").val();
	        var departmentname = $("#departmentname").val();
	        var seachUrl = encodeURI("zqb_company_getsubmsg.action?fullname="+fullname+"&model="+model+"&orderby="+orderby+"&starttime="+starttime+"&endtime="+endtime+"&departmentname="+departmentname);
	        window.location.href = seachUrl;
	    });
    });
	function loadItem(url){
		var target = "_blank";
    	var win_width = window.screen.width;
    	var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
    	page.location = encodeURI(url);
	}
		
	function expkh(){
		var fullname = $("#FULLNAME").val();
        var model = $("#MODEL").val();
        var orderby = $("#orderby").val();
        var starttime = $("#STARTTIME").val();
        var endtime = $("#ENDTIME").val();
        var departmentname = $("#departmentname").val();
        var pageUrl = encodeURI("zqb_submsg_expcurrentlist.action?fullname="+fullname+"&model="+model+"&orderby="+orderby+"&starttime="+starttime+"&endtime="+endtime+"&departmentname="+departmentname);
		window.location.href = pageUrl;
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
			<a href="javascript:expkh();" class="easyui-linkbutton" plain="true" iconCls="icon-edit">导出</a>
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
											<input type='text' class='{maxlength:64,required:false,string:true}' style="width:150px" name='FULLNAME' id='FULLNAME' value=''>
										</td>
										<td class="searchtitle">发生模块</td>
										<td class="searchdata">
											<select name='MODEL' id='MODEL'>
												<option value=''>-空-</option>
												<option value='公告'>公告</option>
												<option value='日常业务呈报'>日常业务呈报</option>
												<s:if test="flag=='show'"><option value='项目'>项目</option></s:if>
											</select>
										</td>
									</tr>
									<tr>
										<td class="searchtitle">开始时间</td>
										<td class="searchdata">
											<input type='text' onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" class='{maxlength:20,required:false}' style="width:150px;" name='STARTTIME' id='STARTTIME' value=''>
										</td>
										<td class="searchtitle">截止时间</td>
										<td class="searchdata">
											<input type='text' onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" class='{maxlength:20,required:false}' style="width:150px;" name='ENDTIME' id='ENDTIME' value=''>
										</td>
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
					<td style="width:15%;">姓名</td>
					<td style="width:20%;">所属部门</td>
					<td style="width:15%;">减分</td>
					<td style="width:20%;">发生模块</td>
					<td style="width:10%;">扣分日期</td>
					<td style="width:20%;">查看详情</td>
				</tr>
				<s:iterator value="list" status="status">
					<tr class="cell">
						<td><s:property value="FULLNAME" /></td>
						<td><s:property value="DEPARTMENTNAME" /></td>
						<td><s:property value="SUMFS" /></td>
						<td><s:property value="MODEL" /></td>
						<td><s:property value="KFSJ" /></td>
						<td><a href="javascript:loadItem('<s:property value="URL" />');"><s:property value="ITEMNAME" /></a></td>
					</tr>
				</s:iterator>
			</table>
			<form action="zqb_company_getsubmsg.action" method=post name=frmMain id=frmMain>
				<s:hidden name="pageSize" id="pageSize"></s:hidden>
				<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
				<s:hidden name="fullname" id="fullname"></s:hidden>
				<s:hidden name="model" id="model"></s:hidden>
				<s:hidden name="orderby" id="orderby"></s:hidden>
				<s:hidden name="starttime" id="starttime"></s:hidden>
				<s:hidden name="endtime" id="endtime"></s:hidden>
				<s:hidden name="departmentname" id="departmentname"></s:hidden>
			</form>
			<div id='prowed_ifrom_grid'></div>
		</div>
	</div>
	<div region="south" style="vertical-align:bottom;height:60px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;" border="false">
		<div style="padding:5px">
			<s:if test="totalNum==0">

			</s:if>
			<s:else>
				<div id="pp"
					style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
			</s:else>
		</div>
	</div>
	<script type="text/javascript">
	$(function(){
		$("#FULLNAME").attr("value",$("#fullname").val());
		$("#MODEL").attr("value",$("#model").val());
		$("#ORDERBY").attr("value",$("#orderby").val());
		$("#STARTTIME").attr("value",$("#starttime").val());
		$("#ENDTIME").attr("value",$("#endtime").val());
	});
	</script>
</body>
</html>
<!-- 新增查询过滤SQL注入关键字 -->
<script language="JavaScript"> 
  jQuery.validator.addMethod("string", function(value, element) {
          var sqlstr=[" and "," exec ", " count ", " chr ", " mid ", " master ", " or ", " truncate ", " char ", " declare ", " join ","insert ", "select ", "delete ", "update ","create ","drop "]
          var patrn=/[`~!#$%^&*+<>?"{},;']/im;
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