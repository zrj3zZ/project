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
		total : <s:property value="totalNum"/>,
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

	
	function addAdvance(id){
		
		var pageUrl ="open_Advance.action?id="+id;
			art.dialog.open(pageUrl,{
				title:'查看事项',
				loadingText:'正在加载中,请稍后...',
				rang:true,
				width:550, 
				cache:false,
				lock: true,
				height:500, 
				
			
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
	//日程提醒完成事件
		function rctxWc(id,type,flag,sj){
				if(flag==1){
					if(window.confirm("确认完成后该日程将不显示，确认完成吗？")){
						$.post('rctx_wcsj.action',{type:type},function(data){
							window.location.reload();
			       		}); 
					}
				}else{
					if(window.confirm("确认完成后该日程将不显示，确认完成吗？")){
						$.post('rctx_zqwcsj.action',{type:id,sj:sj},function(data){
							window.location.reload();
			       		}); 
					}
				}
				
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
				
				<a class="process_head_tab_active" title="显示所有工作审查未处理的事宜" href="#">事项提醒</a>
			</div>
			<div style="padding-top:3px;"></div>
		</div>
	</div>
	<div region="center" border="false" id="layoutCenter" style="overflow:auto">
		<div class="wr_table">
			<table border="0" cellspacing="0" cellpadding="0">
				<thead>
					<tr class="header" style="border:1px solid #efefef">
						<td align="center" style="width:30%;"><font style="color:#000000;">事项描述</font></td>
						<td align="center" style="width:5%;"><font style="color:#000000;">开始时间</font></td>
						<td align="center" style="width:5%;"><font style="color:#000000;">公司简称</font></td>
						<td align="center" style="width:5%;"><font style="color:#000000;">公司代码</font></td>
						<td align="center" style="width:48%;"><font style="color:#000000;">备注</font></td>
						
						<td align="center" style="width:7%;"><font style="color:#000000;">操作</font></td>
					</tr>
				</thead>
				<s:iterator value="list" status="st">
					<tbody>
						<tr class="content" height="36px">
							<td align="left" ><s:property value="title" /></td>
							<td align="center" ><s:property value="sj"/></td>
							<td align="center" ><s:property value="zqjc"/></td>
							<td align="center" ><s:property value="zqdm"/></td>
							<td align="left" ><s:property value="remark"/></td>
							
							<td align="center" >
								<s:if test="flag==2">
									<a href="javascript:addAdvance('<s:property value="tid"/>')">查看</a>|
									<a href="javascript:rctxWc('<s:property value="id"/>','<s:property value="tid"/>','<s:property value="flag"/>','<s:property value="sj"/>')">确认完成</a>
								</s:if>
								<s:else>
									<a href="javascript:addAdvance('<s:property value="id"/>')">查看</a>|
									<a href="javascript:rctxWc('<s:property value="id"/>','<s:property value="tid"/>','<s:property value="flag"/>','<s:property value="sj"/>')">确认完成</a>
								</s:else>
							
								
							</td>
						</tr>
					</tbody>
				</s:iterator>
			</table>
		</div>
		 <div region="south" style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;" border="false" >
				<div style = "padding:5px">
			<s:if test="totalNum==0">
			
			</s:if><s:else>
			<div id="pp" style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
			</s:else>
			</div>
	</div>
	</div>
	 <s:form id="editForm" name="editForm" action="rx_index.action">
			<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
			<s:hidden name="pageSize" id="pageSize"></s:hidden>
			

   </s:form>
	
</body>
</html>