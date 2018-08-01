<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>协议资料</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
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
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
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
	      	var seachUrl = encodeURI("zqb_project_showDaily.action?startdate="+startdate+"&enddate="+enddate+"&projectNo="+projectno);
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
	
	function addItem(instanceId){
		var formid=$("#xmzlformid").val();
		var id=$("#xmzlid").val();
		var projectno=$("#projectNo").val();
		var customername=$("#customername").val();
		var pageUrl = "url:getXMZLList.action?PROJECTNO="+projectno;
		$.dialog({ 
			title:'协议资料清单',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:550,
			cache:false,
			lock: true,
			stack:true,
			zIndex:10,
			top:100,
			height:480, 
			iconTitle:false,
			extendDrag:true,
			autoSize:true,
			content:pageUrl,
			close:function(){
				window.location.reload();
			}
		});
	}
	
	function editItem(instanceId){
		var formid=$("#xmzlxxformid").val();
		var id=$("#xmzlxxid").val();
		var projectno=$("#projectNo").val();
		var pageUrl = "url:createFormInstance.action?formid="+formid+"&demId="+id+"&PROJECTNO="+projectno+"&instanceId="+instanceId;
		$.dialog({ 
			title:'协议资料信息表单',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:900,
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
	
	function checkItem(instanceId){
		var formid=$("#xmzlxxckformid").val();
		var id=$("#xmzlxxckid").val();
		var projectno=$("#projectNo").val();
		var pageUrl = "url:createFormInstance.action?formid="+formid+"&demId="+id+"&PROJECTNO="+projectno+"&instanceId="+instanceId;
		$.dialog({ 
			title:'协议资料信息表单',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:900,
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
	
	function deleteItem(id){
		var pageUrl = encodeURI("delatexmxxzl.action?zlxxid="+id);
		$.messager.confirm("确认","确认删除资料？",function(result){ 
		 	if(result){
		 		$.post(pageUrl,function(data){ 
	       			window.location.reload();
	     		 });
			}
		});
	}
	
	function updateYGD(id){
		var pageUrl = encodeURI("updatexmxxzl.action?zlxxid="+id);
		$.messager.confirm("确认","确认已归档？",function(result){ 
		 	if(result){
		 		$.post(pageUrl,function(data){ 
	       			window.location.reload();
	     		 });
			}
		});
	}
	function expPro(){
		var pageUrl = "zqb_project_daily.action";
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
			<center>
				<h2 style="padding-top: 10px;font-style: inherit;">项目名称：<% response.setCharacterEncoding("UTF-8");out.print(request.getParameter("customername"));%></h2>
			</center>
			<div  class="tools_nav" style="float: right;margin-right: 20px;">
				<a href="javascript:addItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">选择项目反馈资料</a>
				<a href="javascript:window.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
			</div> 
	</div>
	<div style="clear: right;"></div>
	<div region="center"style="padding-left:15px;padding-right:15px;border:0px;background-position:top">
		<form name='ifrmMain' id='ifrmMain'  method="post" >
	<div style="padding:5px">
		<div style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE;">
</div>
	</div>
	</form>
	<s:form id="editForm" name="editForm" theme="simple"></s:form>
		<div style="padding:5px">
			<table id='iform_grid' width="100%" style="border:1px solid #efefef">
				<tr class="header">
					<td style="width:14.2%;">协议资料名称</td>
					<td style="width:14.2%;">资料份数</td>
					<td style="width:14.2%;">归档状态</td>
					<td style="width:14.2%;">合同签订日期</td>
					<td style="width:14.2%;">合同终止日期</td>
					<td style="width:14.2%;">负责人</td>
					<td style="width:14.2%;">协议资料管理</td>
				</tr>
				<s:iterator value="listxmzl" status="status">
				<s:if test="ZT!='已删除'">
					<tr class="cell">
						<td><s:property value="XYZLMC"/></td>
						<td><s:property value="ZLFS" /></td>
						<td>
							<s:if test="ZZHTSFYGD=='未归档'">
							<input type="button" value="确认已归档" onclick="updateYGD(<s:property value="ID" />);">
							</s:if>
							<s:else>
								<s:property value="ZZHTSFYGD" />
							</s:else>
						</td>
						<td><s:property value="HTQDRQ" /></td>
						<td><s:property value="HTZZRQ" /></td>
						<td><s:property value="HTFZR" /></td>
						<td>
							<a href="javascript:checkItem(<s:property value="INSTANCEID" />);">查看</a>&nbsp;|&nbsp;<s:if test="ZT=='已删除'"><s:property value="ZT" /></s:if><s:else><a href="javascript:editItem(<s:property value="INSTANCEID" />);">添加/编辑</a>&nbsp;|&nbsp;<a href="javascript:deleteItem(<s:property value="ID" />);">删除</a></s:else>
						</td>
						</td> 
					</tr>
				</s:if>
				</s:iterator>
			</table>
				<s:hidden name="xmzlid" id="xmzlid"></s:hidden>
				<s:hidden name="xmzlformid" id="xmzlformid"></s:hidden>
				<s:hidden name="projectNo" id="projectNo"></s:hidden>
				<s:hidden name="xmzlxxid" id="xmzlxxid"></s:hidden>
				<s:hidden name="xmzlxxformid" id="xmzlxxformid"></s:hidden>
				<s:hidden name="xmzlxxckformid" id="xmzlxxckformid"></s:hidden>
				<s:hidden name="xmzlxxckid" id="xmzlxxckid"></s:hidden>
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