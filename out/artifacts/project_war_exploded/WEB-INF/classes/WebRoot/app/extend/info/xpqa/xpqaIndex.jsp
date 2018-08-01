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
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
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
<script type="text/javascript" src="iwork_js/plugs/tableSort.js"></script>
<script type="text/javascript">
	var mainFormValidator;
	$().ready(function() {
		mainFormValidator =  $("#ifrmMain").validate({});
		mainFormValidator.resetForm();
	});
	
	$(function(){
		$(document).bind('keyup', function(event) {
			if (event.keyCode == "13") {
				//回车执行查询
				var valid = mainFormValidator.form(); //执行校验操作
		    	if(!valid){
		    	return false;
		    	}
				$("#ifrmMain").submit();
			}
		});
	    //查询
	    $("#search").click(function(){
	    	var valid = mainFormValidator.form(); //执行校验操作
	    	if(!valid){
	    	return false;
	    	}
	    	$("#ifrmMain").submit();
	    });
	    
	    function number(){ 
			for(var i=0;i< $(".numberClass").length;i++){ 
				$(".numberClass").get(i).innerHTML = i+1;
			} 
		}
		number();
    });

	function openFormPage(customerno,zqdm,zqjc){
		var startdate=$("#startdate").val();
		var enddate=$("#enddate").val();
		var url = 'zqb_notice_detail.action?customerno='+encodeURI(customerno)+'&zqdm='+encodeURI(zqdm)+'&zqjc='+encodeURI(zqjc)+'&startdate='+encodeURI(startdate)+'&enddate='+encodeURI(enddate);
		var target = "_blank";
		var win_width = window.screen.width;
		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = url;
	}
	function uploadItem(){
		
		  var zqdm = $("#zqdm").val();
	        var zqjc = $("#zqjc").val();
	        var startdate = $("#startdate").val();
	        var enddate = $("#enddate").val();
		 var seachUrl =encodeURI("zqb_XPQA_excel.action?zqdm="+zqdm+"&zqjc="+zqjc+"&startdate="+startdate+"&enddate="+enddate);
		window.location.href = seachUrl;
		
	 }
</script>
<style type="text/css">
pre {
	overflow: auto;
	white-space: pre-wrap; /* css-3 */
	white-space: -moz-pre-wrap; /* Mozilla, since 1999 */
	white-space: -pre-wrap; /* Opera 4-6 */
	white-space: -o-pre-wrap; /* Opera 7 */
	word-wrap: break-word; /* Internet Explorer 5.5+ */
}

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
	font-size: 12px;
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
	<div region="center"
		style="padding-left:0px;padding-right:0px;border:0px;background-position:top">
		<form name='ifrmMain' id='ifrmMain' method="post" action="zqb_XPQA_list.action">
			<div style="padding:5px;text-align:center;">
				<div
					style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE; width:99%">
					<table width='100%' border='0' cellpadding='0' cellspacing='0'>
						<tr>
							<td style='padding-top:10px;padding-bottom:10px;'>
								<table width='100%' border='0' cellpadding='0' cellspacing='0'>
									<tr>
										<td class="searchtitle">证券代码</td>
										<td class="searchdata">
											<input type='text' class='{maxlength:6,required:false,string:true}' style="width:100px" name='zqdm' id='zqdm' value="${zqdm}">
										</td>
										<td class="searchtitle">证券简称</td>
										<td class="searchdata">
											<input type='text' class='{maxlength:12,required:false,string:true}' style="width:100px" name='zqjc' id='zqjc' value="${zqjc}">
										</td>
										<td class="searchtitle">起始时间</td>
										<td id="title_STARTDATE" class="searchdata">
											<input type='text' onfocus="var ENDDATE=$dp.$('enddate');WdatePicker({onpicked:function(){ENDDATE.focus();},maxDate:'#F{$dp.$D(\'enddate\')}'})" style="width:100px" name='startdate' id='startdate' value='${startdate}'>
											到
											<input type='text' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startdate\')}'})" style="width:100px" name='enddate' id='enddate' value='${enddate}'>
										</td>
									</tr>
								</table>
							<td>
							<td valign='bottom' style='padding-bottom:5px;'><a
								id="search" class="easyui-linkbutton" icon="icon-search"
								href="javascript:void(0);">查询</a>&nbsp;</td>	<td valign='bottom' style='padding-bottom:5px;'><a
								 class="easyui-linkbutton" icon="icon-add"
								href="javascript:uploadItem();">导出</a></td>
						<tr>
					</table>
				</div>
			</div>
		</form>
		<div style="padding:5px;text-align:center;">
			<table id='iform_grid' width="99%"
				style="border:1px solid #efefef;table-layout:fixed;">
				<thead>
					<tr class="header">
						<td style="width:2%;">序号</td>
						<td style="width:10%;">证券代码</td>
						<td style="width:20%;">证券简称</td>
						<td style="width:5%;" onclick="sort('iform_grid',4,'float');">信披评分</td>
						<td style="width:5%;text-align: center;">操作</td>
					</tr>
				</thead>
				<tbody>
					<s:iterator value="qaList" status="status">
						<tr class="cell">
							<td><%-- <s:property value="#status.count" /> --%><span class="numberClass"></span></td>
							<td><s:property value="ZQDM" /></td>
							<td><s:property value="ZQJC" /></td>
							<td style="text-align: right;"><s:property value="XPPF" /></td>
							<td style="text-align: center;">&nbsp; <a href="javascript:void(0)"
								style="color: #0000ff;"
								onclick="openFormPage('<s:property value='KHBH'/>','<s:property value='ZQDM'/>','<s:property value='ZQJC'/>')">查看公告评分详情
							</a> &nbsp;
							</td>
						</tr>
					</s:iterator>
				</tbody>
			</table>
			<div id='prowed_ifrom_grid'></div>
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
