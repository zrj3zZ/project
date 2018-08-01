<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>项目日报</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css"
	href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css"
	href="iwork_css/jquerycss/default/easyui.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css"
	href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
<link rel="stylesheet" type="text/css"
	href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
<link rel="stylesheet" type="text/css"
	href="iwork_css/engine/iformpage.css" />
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript"
	src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript"
	src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="iwork_js/jqueryjs/languages/grid.locale-cn.js"></script>
<script type="text/javascript"
	src="iwork_js/jqueryjs/jquery.jqGrid.min.js">
	
</script>
<script type="text/javascript"
	src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/engine/ifromworkbox.js">
	
</script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css"
	href="iwork_css/engine/sysenginemetadata.css">
<link rel="stylesheet" type="text/css"
	href="iwork_themes/easyui/gray/easyui.css">
<script type="text/javascript"
	src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>

	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/bindclick.js"  ></script>

<script type="text/javascript">
	$(function() {
		$('#pp').pagination({
			total : <s:property value="totalNum"/>,
			pageNumber : <s:property value="pageNumber"/>,
			pageSize : <s:property value="pageSize"/>,
			onSelectPage : function(pageNumber, pageSize) {
				submitMsg(pageNumber, pageSize);
			}
		});
		//查询事件
	    $("#search").click(function(){
	    	var projectno = $("#projectNo").val();
	        var startdate = $("#STARTDATE").val();
	        var enddate = $("#ENDDATE").val();
	      	var seachUrl = encodeURI("xsd_zqb_project_showDaily.action?startdate="+startdate+"&enddate="+enddate+"&projectNo="+projectno);
	        window.location.href = seachUrl;
	      });
	});
	function submitMsg(pageNumber, pageSize) {
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#frmMain").submit();
		return;
	}
	function showVote(ggid){
		var pageUrl = "url:zqb_vote_goList.action?ggid="+ggid;
		$.dialog({
			title:'自查反馈统计',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:1100,
			cache:false,
			lock: true,
			height:780, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			content:pageUrl,
			close:function(){
				window.location.reload();
			}
		});
	}
	
	function addItem(){
		var formid=$("#formid").val();
		var id=$("#xmrbid").val();
		var projectno=$("#projectNo").val();
		var pageUrl = "url:createFormInstance.action?formid="+formid+"&demId="+id+"&PROJECTNO="+projectno;
		$.dialog({ 
			title:'项目日报表单',
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
			content:pageUrl,
			close:function(){
				window.location.reload();
			}
		});
	}
	
	function expPro(){
		var totle = <s:property value="totalNum"/>;
		if(totle==0){
			alert("无项目日报!");
			return;
		}
		var projectno=$("#projectNo").val();
		var pageUrl = "xsd_zqb_project_daily.action?projectNo="+projectno;
		$("#editForm").attr("action", pageUrl);
		$("#editForm").submit();
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

.cell td{
			margin:0;
			padding:3px 4px;
			height:25px;
			font-size:12px;
			white-space:nowrap;
			word-wrap:normal;
			overflow:hidden;
			text-align:left;
			border-bottom:1px dotted #eee;
			border-top:1px dotted #fff;
			border-right:1px dotted #eee;
		}
		.cell:hover{
			background-color:#F0F0F0;
		}
 .header td{
			height:35px;
			font-size:12px;
			padding:3px;
			white-space:nowrap;
			padding-left:5px;
			background:url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;
			border-top:1px dotted #ccc;
			border-right:1px solid #eee;
		} 
</style>
</head>
<body class="easyui-layout">
<div region="north" border="false" >
			<div  class="tools_nav">
				<a href="javascript:addItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加</a>
				<a href="javascript:window.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
				<a href="javascript:expPro();" class="easyui-linkbutton" plain="true" iconCls="icon-excel-exp">导出</a>
			</div> 
	</div>
	<div region="center"
		style="padding-left:0px;padding-right:0px;border:0px;background-position:top">
		<form name='ifrmMain' id='ifrmMain'  method="post" >
	<div style="padding:5px">
		<div style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE;">
<table width='100%' border='0' cellpadding='0' cellspacing='0'> 
<tr> 
  <td style='padding-top:10px;padding-bottom:10px;'> 
<table width='100%' border='0' cellpadding='0' cellspacing='0'> 
<tr> 
	<td class= "searchtitle">日报时间 </td> 
	<td class= "searchdata"><input type='text' onfocus="WdatePicker()" class = "{required:true}"  style="width:100px" name='STARTDATE' id='STARTDATE'  value='' >到<input type='text' onfocus="WdatePicker()" class = "{required:true}" onchange="checkRQ()" style="width:100px" name='ENDDATE' id='ENDDATE'  value='' ></td> 
</tr> 
</table> 
  <td></td>
  <td valign='bottom' style='padding-bottom:5px;'> <a id="search" class="easyui-linkbutton" icon="icon-search" href="javascript:void(0);" >查询</a></td>
<tr> 
</table> 
</div>
	</div>
	</form>
	<s:form id="editForm" name="editForm" theme="simple"></s:form>
		<div style="padding:5px">
			<table id='iform_grid' width="100%" style="border:1px solid #efefef">
				<tr class="header">
					<td style="width:20%;">日期</td>
					<td style="width:20%;">进展情况</td>
					<td style="width:20%;">对方联系人</td>
					<td style="width:20%;">联系电话</td>
					<td style="width:20%;">跟踪进度</td>
				</tr>
				<s:iterator value="list" status="status">
					<tr class="cell">
						<td><s:property value="PROJECTDATE"/></td>
						<td><s:property value="PROGRESS" /></td>
						<td><s:property value="USERNAME" /></td>
						<td><s:property value="TEL" /></td>
						<td><s:property value="TRACKING" /></td>
						</td> 
					</tr>
				</s:iterator>
			</table>
			<form action="xsd_zqb_project_showDaily.action" method=post name=frmMain id=frmMain>
				<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
				<s:hidden name="pageSize" id="pageSize"></s:hidden>
				<s:hidden name="projectNo" id="projectNo"></s:hidden>
				<s:hidden name="startdate" id="startdate"></s:hidden>
				<s:hidden name="enddate" id="enddate"></s:hidden>
			</form>
				<s:hidden name="formid" id="formid"></s:hidden>
				<s:hidden name="xmrbid" id="xmrbid"></s:hidden>
			<div id='prowed_ifrom_grid'></div>
		</div>
	</div>
	<div region="south"
		style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;"
		border="false">
		<div style="padding:5px">
			<s:if test="totalNum==0">

			</s:if>
			<s:else>
				<div id="pp"
					style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
			</s:else>
		</div>
		<script type="text/javascript">
	$(function(){
		$("#STARTDATE").val($("#startdate").val());
		$("#ENDDATE").val($("#enddate").val());
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