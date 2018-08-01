<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>流程处理中心</title>
<link href="iwork_css/process_center.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<link href="iwork_css/message/sysmsgpage.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script src="iwork_js/process/process_desk.js"></script> 
<script>
	$(function(){
		$('#pp').pagination({  
			total:<s:property value="hycount"/>,  
		    pageNumber:<s:property value="pageNumber"/>,
		    pageSize:<s:property value="pageSize"/>,
		    onSelectPage:
		    	function(pageNumber, pageSize){
		    		submitMsg(pageNumber,pageSize);
		    	}
		});
	});
	
	function submitMsg(pageNumber,pageSize){
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#editForm").submit();
		return ;
	}
	
	function showInfo(taskId){
		if($("#more"+taskId).hasClass("more")){
			$("#more"+taskId).removeClass("more"); 
			$("#more"+taskId).addClass("js");
			$.post('process_desk_getsummary.action',{taskId:taskId},function(msg){ 
				$("#content"+taskId).find(".summary_main").html(msg);
			});
		}else{
			$("#more"+taskId).removeClass("js"); 
			$("#more"+taskId).addClass("more");
		}
		$("#content"+taskId).animate({height: 'toggle', opacity: 'toggle'});
	}
	
	function reloadPage(){
		window.location.reload();
	}
	
	function showItem(id) {
		var title = "";
		var height = 580;
		var width = 900;
		var pageurl = "loadVisitPage.action?formid=96&instanceId=" + id + "&demId=26";
		var dialogId = "meetItem";
		parent.parent.openWin(title, height, width, pageurl, this.location,dialogId);
	}
	
	function updateClflag(instanceid,id){
		$.messager.confirm('确认','确认更新为“已处理”?',function(result) {
			if (result) {
				var updateUrl = "updateClFlag.action";
				$.post(updateUrl,{instanceid : instanceid,id:id},function(data) {
					alert(data);
					window.location.reload();
				});
			}
		});
	}
</script>
<style>
.header td {
	height: 30px;
	font-size: 12px;
	padding: 3px;
	white-space: nowrap;
	padding-left: 5px;
	background: url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left
		bottom;
	border-top: 1px dotted #ccc;
	border-right: 1px solid #eee;
}
.content td {
	border: 1px solid #efefef;
}
.tablerow {
width:80px;
text-align:center;

}
</style>
</head>
<body  class="easyui-layout" >
<div region="north" border="false" split="false"  style="height:35px;" id="layoutNorth">
<div class="process_header">
	<div class="process_head_tab_box"> 
		<a class="process_head_tab" href="fsp_zydd_index.action">披露信息更新(<font color="red"><s:property value="sxcount"/></font>)</a>  
		<a class="process_head_tab_active" title="显示所有会议计划未处理的事宜" href="fsp_hy_index.action">会议计划(<font color="red"><s:property value="hycount"/></font>)</a>
		<a class="process_head_tab" title="显示所有部门通知未处理的事宜" href="fsp_gzsc_index.action">部门通知(<font color="red"><s:property value="gzsccount"/></font>)</a>
	</div>
	<div style="padding-top:3px;">
	</div>
</div>
</div>
<div region="center" border="false" id="layoutCenter" style="overflow:auto">
	<div class="wr_table">
		<table border="0" cellspacing="0" cellpadding="0">
			<thead>
				<tr class="header" style="border:1px solid #efefef">
					<td class="tablerow"><font style="color:#000000;">公司名称</></td>
					<td class="tablerow"><font style="color:#000000;">会议名称</font></td>
					<td class="tablerow"><font style="color:#000000;">会议时间</font></td>
					<td class="tablerow"><font style="color:#000000;">会议状态</font></td>
					<td class="tablerow"><font style="color:#000000;">操作</font></td>
				</tr>
			</thead>
			<s:iterator value="list" status="st">
				<tbody>
					<tr class="content" id="notice_<s:property value="taskId"/>" height="36px">
						<td class="row3">
							<a href="javascript:showItem('<s:property value="INSTANCEID"/>','<s:property value="ID"/>')"><s:property value="CUSTOMERNAME" /></a>
						</td>
						<td class="row1">
							<a href="javascript:showItem('<s:property value="INSTANCEID"/>','<s:property value="ID"/>')"><s:property value="MEETNAME" /></a>
						</td>
						<td class="row1">
							<s:property value="PLANTIME" />
						</td>
						<td class="row2">
							<s:property value="STATUS" />
						</td>
						<td class="row1">
							<a href="javascript:updateClflag('<s:property value="INSTANCEID"/>','<s:property value="ID"/>')">处理</a>
						</td>
					</tr>
				</tbody>
			</s:iterator>
		</table>
	</div>
</div>
<s:form id="editForm" name="editForm" action="fsp_hy_index.action">
	<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
	<s:hidden name="pageSize" id="pageSize"></s:hidden>
</s:form>
<div region="south" style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;" border="false" >
	<div style = "padding:5px">
		<s:if test="hycount>0">
			<div id="pp" style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
		</s:if>
	</div>
</div>
</body>
</html>