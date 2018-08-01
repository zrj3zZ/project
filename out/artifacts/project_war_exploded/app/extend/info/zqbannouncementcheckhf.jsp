<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html style="height: 100%; overflow: hidden;"><head>
<meta content="text/html; charset=UTF-8" http-equiv="Content-Type">
<title>IWORK综合应用管理系统

</title>
<link href="iwork_css/common.css" type="text/css" rel="stylesheet">
<link href="iwork_css/jquerycss/process-icon.css" type="text/css" rel="stylesheet">
<link href="iwork_css/jquerycss/default/easyui.css" type="text/css" rel="stylesheet">
<link type="text/css" rel="stylesheet" href="iwork_css/public.css">
<link href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css" type="text/css" rel="stylesheet">
<link href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css" type="text/css" rel="stylesheet">
<link href="iwork_css/engine/iformpage.css" type="text/css" rel="stylesheet">
<script src="iwork_js/commons.js" language="javascript"></script>
<script src="iwork_js/jqueryjs/jquery-3.1.0.min.js" type="text/javascript"></script>
<script src="iwork_js/jqueryjs/jquery.easyui.min.js" type="text/javascript"></script>
<script src="iwork_js/jqueryjs/languages/grid.locale-cn.js" type="text/javascript"></script>
<script src="iwork_js/jqueryjs/jquery.jqGrid.min.js" type="text/javascript"></script>
<link href="iwork_js/jqueryjs/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" type="text/css">
<script src="iwork_js/engine/ifromworkbox.js" type="text/javascript"></script>
<script src="iwork_js/lhgdialog/lhgdialog.min.js" type="text/javascript"></script>
<link href="iwork_css/jquerycss/zTreeStyle.css" type="text/css" rel="stylesheet">
<link href="iwork_css/engine/sysenginemetadata.css" type="text/css" rel="stylesheet">
<link href="iwork_themes/easyui/gray/easyui.css" type="text/css" rel="stylesheet">
<script src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js" type="text/javascript"></script>
<script type="text/javascript" src="iwork_js/bindclick.js"  ></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
$(function() {
		$('#pp').pagination({
			total : 0,
			pageNumber : 1,
			pageSize : 10,
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
		$("#search").click(
			function() {
				var spzt = $("#SPZT").val();
				var id=$("#id").val();
				var seachUrl = encodeURI("zqb_announcement_checkHF.action?spzt=" + spzt+"&id="+id);
				window.location.href = seachUrl;
			});
		});

	function showGZSC(instanceid){
		var gzschfformid = $("#gzschfformid").val();
		var gzschfdemid = $("#gzschfdemid").val();
		var pageUrl = "openFormPage.action?formid="+gzschfformid+"&demId="+gzschfdemid+"&instanceId="+instanceid+"&isDialogDisabled=1"+"&isHFRandHFNRdiaplsy=0";
		art.dialog.open(pageUrl,{
			title:'工作备查回复查看',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:1100,
			cache:false,
			lock: true,
			height:580, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			zindex:3999,
			stack:true,
			modal:true,
			position:'absolute',
			close:function(){
				window.location.reload();
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
	height: 

28px;
	font-size: 12px;
	vertical-align: text-middle;
	padding-top: 2px;
}

.header td {
	height: 35px;
	font-size: 12px;
	padding: 

3px;
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
	border-bottom: 1px dotted 

#eee;
	border-top: 1px dotted #fff;
	border-right: 1px dotted #eee;
}

.cell:hover {
	background-color: #F0F0F0;
}
</style>
</head>
<body>
<div region="center" style="padding-left: 0px; padding-right: 0px; border: 0px none; background-position: center top; width: auto; height: 450px;" class="layout-body panel-body panel-body-noheader">
<form method="post" id="ifrmMain" name="ifrmMain">
	<div style="border:1px solid #ccc;background:#FFFFEE;width:97%;">
		<table width="100%" cellspacing="0" cellpadding="0" border="0">
			<tbody><tr>
			<td style="padding-top:5px;">
				<table width="100%" cellspacing="0" cellpadding="0" border="0">
					<tbody>
					<tr>
						<td class="searchtitle">状态</td>
						<td class="searchdata">
							<select id="SPZT" name="SPZT">
								<option value="">-空-</option>
								<option value="0">未反馈</option>
								<option value="1">已反馈</option>
							</select>
						</td>
					</tr>
					</tbody>
				</table>
			</td>
			<td></td>
			<td valign="bottom" style="padding-bottom:2px;">
			<a href="javascript:void(0);" icon="icon-search" class="easyui-linkbutton l-btn" id="search">
				<span style="padding-left: 20px;">查询</span>
			</a>
			</td>
			</tr>
			<tr></tr>
			</tbody>
		</table>
	</div>
</form>
<div align="left" style="padding-right: 5px;">共通知人数：<s:property value='totalNum'/> 未回复人数：<s:property value='gzscnothfnum'/></div>
<div style="padding:5px;width:98%;">
	<table width="98%" style="border:1px solid #efefef" id="iform_grid">
		<tbody><tr class="header">
			<!-- <td style="width:20%;">公司代码</td> -->
			<td style="width:10%;">部门/公司名称</td>
			<td style="width:15%;">姓名</td>
			<td style="width:10%;">手机号</td>
			<td style="width:10%;">状态</td>
			<td style="width:10%;">回复时间</td>
		</tr>
		<s:iterator value="List" status="status">
		<tbody><tr class="cell">
			<%-- <td style="width:20%;"><s:property value='ZQDM'/></td> --%>
			<td style="width:10%;"><s:property value='DEPARTMENTNAME'/></td>
			<td style="width:15%;"><s:property value='NAME'/></td>
			<td style="width:10%;"><s:property value='PHONE'/></td>
			<td style="width:10%;"><s:if test="FKZT==1"><a href="javascript:showGZSC('<s:property value='INSTANCEID'/>')">已反馈</a></s:if><s:else>未反馈</s:else></td>
			<td style="width:10%;"><s:property value='HFSJ'/></td>
		</tr>
		</s:iterator>
		</tbody>
	</table>

	<form action="zqb_announcement_checkHF.action" method=post name=frmMain id=frmMain>
		<s:hidden name="gzschfformid" id="gzschfformid"></s:hidden>
		<s:hidden name="gzschfdemid" id="gzschfdemid"></s:hidden>
		<s:hidden name="id" id="id"></s:hidden>
		<s:hidden name="spzt" id="spzt"></s:hidden>
	</form>
	<div id="prowed_ifrom_grid"></div>
</div>
</div>
	<script type="text/javascript">
		$(function(){
		$("#SPZT").val($("#spzt").val());
	});
	</script>
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