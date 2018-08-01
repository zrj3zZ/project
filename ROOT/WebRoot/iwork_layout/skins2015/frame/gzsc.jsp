<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>流程处理中心</title>
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/process/process_desk.js"></script> 
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_css/process_center.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/message/sysmsgpage.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css"/>
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css"/>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
	$(function() {
		$('#pp').pagination({
			total : <s:property value="gzsccount"/>,
			pageNumber : <s:property value="pageNumber"/>,
			pageSize : <s:property value="pageSize"/>,
			onSelectPage : function(pageNumber, pageSize) {
				submitMsg(pageNumber, pageSize);
			}
		});
	});
	
	function submitMsg(pageNumber, pageSize) {
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#editForm").submit();
		return;
	}
	
	function reloadPage() {
		window.location.reload();
	}
	
	function updateGzsc(cid,did,instanceid) {
		var gzscformid = $("#gzscformid").val();
		var gzscdemid = $("#gzscdemid").val();
		var pageUrl = "openFormPage.action?formid="+gzscformid+"&demId="+gzscdemid+"&instanceId="+instanceid+"&cid="+cid+"&did="+did+"&isDialogDisabled=1"+"&isHFRandHFNRdiaplsy=0";
		art.dialog.open(pageUrl,{
			title:'部门通知',
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
			close:function(){
				window.location.reload();
			}
		});
	}
	
	function addGzschf(cid,did){
		var gzschfformid = $("#gzschfformid").val();
		var gzschfdemid = $("#gzschfdemid").val();
		var pageUrl = "createFormInstance.action?formid="+gzschfformid+"&demId="+gzschfdemid+"&CID="+cid+"&DID="+did;
		art.dialog.open(pageUrl,{
			title:'部门通知',
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
			close:function(){
				window.location.reload();
			}
		}); 
	}
	
	function checkGzschf(eins){
		var gzschfformid = $("#gzschfformid").val();
		var gzschfdemid = $("#gzschfdemid").val();
		var pageUrl = "openFormPage.action?formid="+gzschfformid+"&demId="+gzschfdemid+"&instanceId="+eins;
		art.dialog.open(pageUrl,{
			title:'查看',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:1100,
			cache:false,
			lock: true,
			height:480, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			close:function(){
				window.location.reload();
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
	align:center;
	valign:middle;
}
</style>
</head>
<body class="easyui-layout">
	<div region="north" border="false" split="false" style="height:35px;"
		id="layoutNorth">
		<div class="process_header">
			<div class="process_head_tab_box">
				<a class="process_head_tab"	href="fsp_zydd_index.action">披露信息更新(<font color="red"><s:property value="sxcount" /></font>)</a>
				<a class="process_head_tab" title="显示所有会议计划未处理的事宜" href="fsp_hy_index.action">会议计划(<font color="red"><s:property value="hycount" /></font>)</a>
				<a class="process_head_tab_active" title="显示所有部门通知未处理的事宜" href="fsp_gzsc_index.action">部门通知(<font color="red"><s:property value="gzsccount"/></font>)</a>
			</div>
			<div style="padding-top:3px;"></div>
		</div>
	</div>
	<div region="center" border="false" id="layoutCenter" style="overflow:auto">
		<div class="wr_table">
			<table border="0" cellspacing="0" cellpadding="0">
				<thead>
					<tr class="header" style="border:1px solid #efefef">
						<td align="left" style="width:20%;"><font style="color:#000000;">标题</font></td>
						<td align="left" style="width:5%;"><font style="color:#000000;">发送人</font></td>
						<td align="left" style="width:5%;"><font style="color:#000000;">通知日期</font></td>
						<td align="left" style="width:45%;"><font style="color:#000000;">内容</font></td>
						<td align="left" style="width:5%;"><font style="color:#000000;">操作</font></td>
					</tr>
				</thead>
				<s:iterator value="list" status="st">
					<tbody>
						<tr class="content" height="36px">
							<td align="left" style="width:20%;"><%-- <a href="javascript:showGZSC('<s:property value="INSTANCEID"/>')"> --%><s:property value="TZBT" /><!-- </a> --></td>
							<td align="left" style="width:5%;"><s:property value="FSR"/></td>
							<td align="left" style="width:5%;"><s:property value="FSSJ.substring(0,10)"/></td>
							<td align="left" style="width:45%;">
								<s:if test="TZNR.length() > 60">
									<s:property value="TZNR.substring(0,60)"/>...
								</s:if>
								<s:else>
									<s:property value="TZNR"/>
								</s:else>
							</td>
							<td align="left" style="width:5%;">
								<s:if test="FKZT==1">
									<a href="javascript:updateGzsc('<s:property value="CID"/>','<s:property value="DID"/>','<s:property value="INSTANCEID"/>')">查看</a>&nbsp;|&nbsp;<a href="javascript:checkGzschf('<s:property value="EINS"/>')">已反馈</a>
								</s:if>
								<s:else>
									<a href="javascript:updateGzsc('<s:property value="CID"/>','<s:property value="DID"/>','<s:property value="INSTANCEID"/>')">查看</a>&nbsp;|&nbsp;<a href="javascript:addGzschf('<s:property value="CID"/>','<s:property value="DID"/>')">回复</a>
								</s:else>
							</td>
						</tr>
					</tbody>
				</s:iterator>
			</table>
		</div>
	</div>
	<s:form id="editForm" name="editForm" action="fsp_gzsc_index.action">
		<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
		<s:hidden name="pageSize" id="pageSize"></s:hidden>
		<s:hidden name="gzscformid" id="gzscformid"></s:hidden>
		<s:hidden name="gzscdemid" id="gzscdemid"></s:hidden>
		<s:hidden name="gzschfformid" id="gzschfformid"></s:hidden>
		<s:hidden name="gzschfdemid" id="gzschfdemid"></s:hidden>
	</s:form>
	<div region="south" style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;" border="false">
		<div style="padding:5px">
			<s:if test="gzsccount>0">
				<div id="pp"
					style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
			</s:if>
		</div>
	</div>
</body>
</html>