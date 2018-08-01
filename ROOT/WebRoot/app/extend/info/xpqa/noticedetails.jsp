<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>公告评分详情</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_css/public.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css" />
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/engine/ifromworkbox.js"></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
var mainFormValidator;
$().ready(function() {
		mainFormValidator =  $("#ifrmMain").validate({
		 });
		 mainFormValidator.resetForm();
	});
	$(function() {
		$(document).bind('keyup', function(event) {
			if (event.keyCode == "13") {
				var valid = mainFormValidator.form(); //执行校验操作
		    	if(!valid){
		    		return false;
		    	}
				//回车执行查询
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
	});
	
	function getOpinion(lcbs,lcbh,stepid){
		var pageUrl = "zqb_notice_getOpinion.action?lcbs="+lcbs+"&lcbh="+lcbh+"&stepid="+stepid;
		art.dialog.open(pageUrl,{
			id:'Category_show',
			cover:true, 
			title:'反馈意见',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:1000,
			cache:false,
			lock: true,
			height:580, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,			
			close:function(){
				window.location.reload();
			}
		});
	}
	function getGgxq(instanceId,zqjcxs,zqdmxs){
		var url=encodeURI("loadPage.action?instanceId="+instanceId+"&zqjcxs="+zqjcxs+"&zqdmxs="+zqdmxs);
		var target = "_blank";
    	var win_width = window.screen.width;
    	var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
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

.cell td {
	margin: 0;
	padding: 3px 4px;
	font-size: 12px;
	word-wrap: break-word;
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
#iform_grid {
table-layout: fixed;
 }


#iform_grid td {
overflow: hidden;
white-space: nowrap;
text-overflow: ellipsis;
}
.tools_nav {
	background:url(../iwork_img/engine/tools_nav_bg.jpg) repeat-x;
	height:34px;
	line-height:30px;
	padding-left:20px;
	vertical-align:middle;
	padding-top:2px;
	padding-bottom:2px; 
	text-align:left;
}
</style>
</head>
<body class="easyui-layout">
	<div region="center"
		style="padding-left:0px;padding-right:0px;border:0px;background-position:top">
		<form name='ifrmMain' id='ifrmMain' method="post" action="zqb_notice_detail.action">
			<div style="padding:5px">
				<div
					style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE;">
					<table width='100%' border='0' cellpadding='0' cellspacing='0'>
						<tr>
							<td style='padding-top:10px;padding-bottom:10px;'>
								<table width='100%' border='0' cellpadding='0' cellspacing='0'>
									<tr>
										<td class="searchtitle">证券简称</td>
										<td class="searchdata"><input type='text'
											class='{maxlength:200,required:false,string:true}' style="width:100px;background-color: #ccc;"
											name='zqjc' id='zqjc' value='${zqjc}'  form-type='al_textbox'></td>
										<td class="searchtitle">证券代码</td>
										<td class="searchdata"><input type='text'
											class='{maxlength:200,required:false,string:true}'
											style="width:100px;background-color: #ccc;" name='zqdm' id='zqdm' readonly="readonly" value='${zqdm}'
											form-type='al_textbox'></td>
										<td class="searchtitle" style="text-align:right;">公告类型</td>
										<td class="searchdata">
											<select name='noticetype' id='noticetype'>
											<option value=''>-空-</option>
											<option value='临时报告' <s:if test="%{noticetype=='临时报告'}">selected</s:if>>临时报告</option>
											<option value='定期报告' <s:if test="%{noticetype=='定期报告'}">selected</s:if>>定期报告</option>
											<option value='券商公告' <s:if test="%{noticetype=='券商公告'}">selected</s:if>>券商公告</option>
											<option value='风险提示性公告' <s:if test="%{noticetype=='风险提示性公告'}">selected</s:if>>风险提示性公告</option>
											</select>
										</td>
										<td class="searchtitle">公告名称</td>
										<td class="searchdata"><input type='text'
											class='{maxlength:64,required:false,string:true}' style="width:100px;"
											name='noticename' id='noticename' value='${noticename}'
											form-type='al_textbox'></td>
										<td id="title_STARTDATE" class="searchtitle" width="400">公告时间
											<input type='text' onfocus="WdatePicker()"
											style="width:100px" name='startdate'
											id='startdate' value='${startdate}'> 到 <input type='text'
											onfocus="WdatePicker()" style="width:100px" name='enddate'
											id='enddate' value='${enddate}'>
										</td>
									</tr>
								</table>
							</td>
							<td valign='bottom' style='padding-bottom:15px;'><a
								id="search" class="easyui-linkbutton" icon="icon-search"
								href="javascript:void(0);">查询</a></td>
						<tr>
					</table>
				</div>

			</div>
			<span style="disabled:none">
				<s:hidden id="customerno" name="customerno"></s:hidden>
			</span>
		</form>
		<div style="padding:5px">
			<table id='iform_grid'
				style="TABLE-LAYOUT: fixed;border:1px solid #efefef" border="1"
				cellspacing="0" cellpadding="0" width="100%">
				<tbody>
					<tr class="header">
						<td style="width:5%;">序号</td>
						<td style="width:5%;">公告类型</td>
						<td style="width:5%;">证券代码</td>
						<td style="width:10%;">证券简称</td>
						<td style="width:15%;">公告编号</td>
						<td style="width:30%;">公告名称</td>
						<td style="width:10%;">公告日期</td>
						<td style="width:5%;">公告评分</td>
						<td style="width:10%;text-align: center;">操作</td>
					</tr>
					<s:iterator value="detailList" status="status">
						<tr class="cell">
							<td><s:property value="#status.count"/></td>
							<td><s:property value="NOTICETYPE" /></td>
							<td><s:property value="ZQDMXS" /></td>
							<td><s:property value="ZQJCXS" /></td>
							<td><s:property value="NOTICENO" /></td>
							<td><a href="javascript:void(0)"  onclick="getGgxq(<s:property value="INSTANCEID" />,'<s:property value='ZQJCXS'/>','<s:property value='ZQDMXS'/>')"> <s:property value="NOTICENAME" /></a></td>
							<td><s:property value="NOTICEDATE" /></td>
							<td text-align: right;"><s:property value="GGDF" /></td>
							<td style="text-align: center;">&nbsp;
								<a href="javascript:void(0)" style="color: #0000ff;" onclick="getOpinion(<s:property value='LCBS'/>,'<s:property value='LCBH'/>','<s:property value='STEPID'/>')"> 查看反馈意见</a> &nbsp;
							</td>
						</tr>
					</s:iterator>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>
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