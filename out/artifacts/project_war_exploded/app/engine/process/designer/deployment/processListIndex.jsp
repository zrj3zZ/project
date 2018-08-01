<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" charset="gb2312"></script>
	<script type="text/javascript" src="iwork_js/processListIndex.js" charset="gb2312"></script>
	<script type="text/javascript" src="iwork_js/commons.js" charset="gb2312"></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js" charset="gb2312"></script>
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
	$(function(){
	/*
		$(document).bind("contextmenu",function(e){
              return false;   
        });*/
	});
		
	function refresh() {
		window.location.href='sysProcessDefinition!displayProcess.action?processId=' + document.uploadNewForm.sysProcDefId.value;
	}
	
	function prompt() {
		var s="<%=session.getAttribute("flag")%>"; 
		if (s != null && s.length > 0) {
			alert(s);
		}
	}
	function openProcessDef(title,prcDefId,actdefId){
		var pageUrl = 'sysFlowDef_index.action?prcDefId='+prcDefId+'&actdefId='+actdefId;
		  art.dialog.open(pageUrl,{
					id:'openProcessDefWinDiv',
					title:'流程模型设计['+title+']',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:'90%',
				    height:510
				 });
	}	
	function openStepDef(title,prcDefId,actdefId){
		var pageUrl = 'sysFlowDef_stepFrame.action?prcDefId='+prcDefId+'&actDefId='+actdefId;
		 art.dialog.open(pageUrl,{
					id:'openProcessDefWinDiv',
					title:'节点模型设计['+title+']',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:'90%',
				    height:510
				 });
	}	
	//加载流程图
	function showProcessImage(title,prcDefId,actdefId){
		var pageUrl = 'sysProcessDefinition!openProcessImage.action?sysProcDefId='+prcDefId+'&actProcId='+actdefId;
		 art.dialog.open(pageUrl,{
					id:'openProcessDefWinDiv',
					title:'流程图['+title+']',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:'90%',
				    height:510
				 });
	}	
	
	function showProcessDesigner(title,prcDefId,actdefId){
		var pageUrl = 'sysProcessDefinition!openProcessDesigner.action?sysProcDefId='+prcDefId+'&actProcId='+actdefId;
		 art.dialog.open(pageUrl,{
					id:'openProcessDefWinDiv',
					title:'流程设计器['+title+']',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:'95%',
				    height:'95'
				 });
	}
	</script>
	<style type="text/css">
		/* CSS Document */
		input {
		outline: none;
		}
		textarea {
		outline: none;
		}
		* {
			padding:0px;
			margin:0px;
			font-size:12px;
		}
		.box {
			width:99%;
		}
		.edition_frame{ padding:0px; background:url(iwork_img/edition_bg.jpg) repeat-x;}
		.edition_nav{ background:url(iwork_img/edition_nav.jpg) repeat-x;; height:32px; padding-left:0px; padding-right:0px;line-height:32px;}
		.edition_title{
			font-size:12px;
			color:#535353;
			padding-left:20px;
			line-height:30px;
		}
		.edition_article {
			font-size:12px;
			color:#6c6c6c;
			padding-left:30px;
			line-height:20px;
			margin-bottom:5px;
		}
		.edition_btn{ line-height:24px; height:24px;clear:both ;  width:100%; margin-bottom:10px;}
	</style>
</head>
<body class="easyui-layout">
	<div region="center" class="edition_nav" style="border:0px;border-left:1px solid #efefef">
			<s:form name="editForm" action="sysEngineMetadataMap_save.action" theme="simple">
			<div style="border-bottom:1px solid #efefef;margin-bottom:3px;padding-left:10px;float:left">
			<a href="javascript:delProcess();" class="easyui-linkbutton" plain="true" iconCls="icon-remove">移除当前流程</a>
				<a href="javascript:delDeployments();" class="easyui-linkbutton" plain="true" iconCls="icon-remove">移除版本</a>
				
				<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
			</div>
			<div  align="right" style="border-bottom:1px solid #efefef;margin-bottom:3px; padding-right:10px; ">
				<a href="javascript:uploadNewDefFile();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">上传部署文件</a>
				<a href="javascript:queryDeployLog();"  class="easyui-linkbutton" plain="true" iconCls="icon-remove">查看部署日志</a>
			</div>
			<div class="box">
						<s:iterator value="processMapList" status="status">
						  <div class="edition_frame">
						    <div class="edition_title"> 
						      <input type="checkbox" name="id" value="<s:property value="actDefId"/>"/>
						      <label for="checkbox" style="color:#535353; font-size:14px; font-weight:bold; font-family:"黑体"><s:property value="defTitle"/></label>
						      <span style="color:#535353; font-size:16px; font-weight:bold; font-family:宋体"> 
						      <s:if test="model.actProcDefId==actDefId&&model.state==1">
								<a href="sysProcessDefinition!turnOnOrOff.action?sysProcDefId=<s:property value="model.id"/>&actProcId=<s:property value="actDefId"/>&turnFlag=close"><img src="iwork_img/server_go.png" style="padding-left:50px;" border="0"/>开启</a>
								</s:if>
								<s:else>
									<a href="sysProcessDefinition!turnOnOrOff.action?sysProcDefId=<s:property value="model.id"/>&actProcId=<s:property value="actDefId"/>&turnFlag=open"><img src="iwork_img/server_delete.png" style="padding-left:50px;"  border="0"/>关闭</a>
								</s:else>
						       <span style="color:#535353; font-size:12px;padding-right:5px; float:right; font-family:宋体">管理员:<s:property value="model.master"/></span> </div>
						
						    <div class="edition_article"><s:property value="defMemo"/></div>
						    <div class="edition_btn">
						       <span >
						      <input type="button" name="designerBtn" id="designerBtn" value=""  onclick="showProcessImage('<s:property value="defTitle"/>',<s:property value="model.id"/>,'<s:property value="actDefId"/>')" style=" background:url(iwork_img/btn_3.jpg); height:24px; width:99px; border:0 none; cursor:pointer; margin-left:10px; float:right"/>
						      </span> <span >
						      <input type="button" name="stepBtn" id="stepBtn" value=" " onclick="showProcessDesigner('<s:property value="defTitle"/>',<s:property value="model.id"/>,'<s:property value="actDefId"/>')"  style=" background:url(iwork_img/btn_1.jpg); height:24px; width:99px; border:0 none; cursor:pointer; margin-left:10px; float:right"/>
						      </span> 
						      <span >
						      <input type="button" name="globalBtn" id="globalBtn" value=" " onclick="openProcessDef('<s:property value="defTitle"/>',<s:property value="model.id"/>,'<s:property value="actDefId"/>')" style=" background:url(iwork_img/btn_2.jpg); height:24px; width:99px; border:0 none; cursor:pointer; margin-left:10px; float:right"/>
						      </span></div>
						  </div>
						 </s:iterator>
				</div>		
			<s:hidden name="sysProcDefId"/>
			</s:form>
		</div>
		
		 <!-- upload deploy file -->
    <div id="upload_proc_def_window" class="easyui-window" title="上传部署文件" modal="true" closed="true" collapsible="false" minimizable="true"
        maximizable="false" icon="icon-save"  style="width: 400px; height: 150px; padding: 5px;
        background: #fafafa;">
        <div class="easyui-layout" fit="true">
            <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <s:form id="uploadNewForm" name="uploadNewForm" action="sysProcessDefinition!uploadNewDefFile.action"  theme="simple"  enctype="multipart/form-data">
            	
		        <div>
		        	<label for="upload">上传部署文件：</label>
		        	<s:file name="upload" label="上传的文件" required="true" />
		        </div>
                <s:hidden name="model.id"></s:hidden>                
                <s:hidden name="sysProcDefId"/>
                <s:hidden name="fileName"/>
                <s:hidden name="uploader"/>
               </s:form> 
            </div>
            <div region="south" border="false" style="text-align: right; height: 30px; line-height: 30px;padding-top:5px;">
                <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:doSubmit();" >
                    确定</a> <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:cancel()">取消</a>
            </div>
        </div>
    </div>
    
    <!-- deploy log -->
    <div id="deploy_log_window" class="easyui-window" title="部署日志" modal="true" closed="true" collapsible="false" minimizable="true"
        maximizable="false" icon="icon-save"  style="width: 800px; padding: 5px;
        background: #fafafa;">
        <table  width="98%" style="border:1px solid #A4BED4;">
					<thead>
						<tr class="header">
							<td   width="40" nowrap="nowrap">序号</td>
							<td   width="70"  nowrap="nowrap">流程名称<br></td>
							<td   width="100" nowrap="nowrap">部署日期<br></td>
							<td   width="15%" nowrap="nowrap">上传人<br></td>
							<td   width="15%" nowrap="nowrap">模型管理员<br></td>
							<td   width="15%" nowrap="nowrap">配置文件名<br></td>							
						</tr>
						</thead>
						<tbody>
						<s:iterator value="deployLogList" status="status">
							<tr class="cell">
								
								<td> <s:property value="#status.count"/></td>
								<td><s:property value="title"/></td>
								<td><s:property value="uploadDate"/></td>
								<td><s:property value="uploader"/></td>
								<td><s:property value="master"/></td>
								
									
								<td><s:property value="fileName"/></td>							
								<td></td>
							</tr>
						 </s:iterator>
						</tbody>
						
			</table>
        
    </div>    
			
		
    
</body>
<script type="text/javascript">
	//prompt();
</script>
</html>
