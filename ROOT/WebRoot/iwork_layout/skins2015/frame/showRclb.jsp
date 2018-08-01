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
	function delAll(id){
		if(window.confirm("确认删除该事项么？")){
			$.post('rctx_del.action',{id:id},function(data){
				alert("删除成功!");
				window.location.reload();
       		}); 
		}
	}
	//显示已提交的流程页面
    function showInfo(lcbh,steptid,lcinstanceId,taskik){
	
		if(lcbh=="" || steptid=="" || lcinstanceId==""  ||taskik==""){
		
			alert("当前业务流程暂未提交！");
			
			window.location.reload();
			return false;
		} 
			var pageUrl = "loadProcessFormPage.action?actDefId="+lcbh+"&actStepDefId="+steptid+"&instanceId="+lcinstanceId+"&excutionId="+lcinstanceId+"&taskId="+taskik;
			art.dialog.open(pageUrl,{
						id:'projectItem',
						cover:true, 
						title:'',
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true,
						width:1000,
						cache:false,
						lock: true,
						height:600, 
						iconTitle:false,
						extendDrag:true,
						autoSize:false,
						 close:function(){
                           window.location.reload();
                           }
					});
    
			// parent.openWin(title,height,width,pageurl,null,dialogId);
		}				//processInstanceMornitor
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
		
	</div>
	<div region="center" border="false" id="layoutCenter" style="overflow:auto">
		<div class="wr_table">
			<table border="0" cellspacing="0" cellpadding="0">
				<thead>
					<tr class="header" style="border:1px solid #efefef">
						
						
						<td align="center" style="width:8%;"><font style="color:#000000;">公司简称</font></td>
						<td align="center" style="width:8%;"><font style="color:#000000;">公司代码</font></td>
						<td align="center" style="width:32%;"><font style="color:#000000;">事项标题</font></td>
						<td align="center" style="width:32%;"><font style="color:#000000;">提醒内容</font></td>
						<td align="center" style="width:6%;"><font style="color:#000000;">设置人</font></td>
						<td align="center" style="width:9%;"><font style="color:#000000;">设置时间</font></td>
						<td align="center" style="width:5%;"><font style="color:#000000;">操作</font></td>
					</tr>
				</thead>
				<s:iterator value="list" status="st">
					<tbody>
						<tr class="content" height="36px">
							<td align="center" style="width:8%;"><s:property value="zqjc" /></td>
							<td align="center" style="width:8%;"><s:property value="zqdm"/></td>
							<td align="left" style="width:32%;">
							
							<a href="javascript:showInfo('<s:property value="lcbh"/>','<s:property value="stepid"/>','<s:property value="subid"/>','<s:property value="taskid"/>')"><s:property value="noticename"/></a>
						</td>
							<td align="left" style="width:32%;"><a href="javascript:addAdvance('<s:property value="id"/>')"><s:property value="title" /></a></td>
							<td align="center" style="width:6%;"><s:property value="username" /></td>
							<td align="center" style="width:9%;"><s:property value="cjsj" /></td>
							<td align="center" style="width:5%;">
								<s:if test="del==0">
								<a href="javascript:delAll('<s:property value="id"/>')">删除</a>
								</s:if><s:else>
									
								
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
	 <s:form id="editForm" name="editForm" action="zqb_sxzqtxgl.action">
			<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
			<s:hidden name="pageSize" id="pageSize"></s:hidden>
			

   </s:form>
  
</body>
</html>