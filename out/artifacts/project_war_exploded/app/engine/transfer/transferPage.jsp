<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/systransfer_page.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/design_showlist.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script> 
	<script type="text/javascript" src="iwork_js/checklist.js"></script> 
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
		
		function selectCheck(){
			if($("[name='selectAll']").attr("checked")){
				$("[type='checkbox']").attr("checked",'true');
			}else{
			 	$("[type='checkbox']").removeAttr("checked");//取消全选 
			}
		}
		function dotransfer(){
			var spaceId = $("#spaceId").val();
			var pageUrl = "sysImp_dialog.action?spaceId="+spaceId;
			art.dialog.open(pageUrl,{
				id:'sysImp_uploadModule',
				title:'上传模型库', 
				lock:true,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:450,
		        height:380,
		        close:function (){
		             window.location.reload();
		        }
			 });
		}
		function showlog(){
			var spaceId = $("#spaceId").val();
			var pageUrl = "sysImp_showLog.action?spaceId="+spaceId;
			art.dialog.open(pageUrl,{
				id:'sysImp_uploadModule',
				title:'模型传输日志',
				lock:true,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:680,
		        height:480
			 });
		}
		function removeModule(){
			var spaceId = $("#spaceId").val();
			var url='systransfer_module_del.action';
			    $.post(url,{spaceId:spaceId},function(data){
				    if(data=='success'){
				    	alert('删除成功');
				    	this.location.reload();
					}
				});
		}
	</script> 
</head>
<script type="text/javascript">
	$(document).bind("contextmenu",function(){return false;});
</script> 
<body class="easyui-layout">
	<s:if test="spacemodel==null">
		<img src="iwork_img/engine/transfer.gif" border=""/>请选择左侧传输请求
	</s:if>
	<s:else> 
	<div region="north" border="false" split="false">
		<div class="toolbar">
				<s:if test="spacemodel.status=='未传输'">
					<a href='javascript:dotransfer();' class="easyui-linkbutton" iconCls="icon-transfer_run" plain="true">传输</a>
					<a href='javascript:removeModule();' class="easyui-linkbutton" iconCls="icon-transfer_del" plain="true">移除当前模型</a>
				</s:if>
				<a href='javascript:showlog();' class="easyui-linkbutton" iconCls="icon-transfer_log" plain="true">传输日志</a>
				<a href='javascript:this.location.reload();' class="easyui-linkbutton" iconCls="icon-reload" plain="true">刷新</a>
		</div>
		<table width="90%">
	           			<tr >
	           				<td  class="infoTitle">模型名称</td>
	           				<td class="infoData"><s:property value="spacemodel.title"/></td>
	           				<td class="infoTitle">来源</td>
	           				<td class="infoData"><s:property value="spacemodel.source"/></td>
	           			</tr>
	           			<tr >
	           				<td class="infoTitle">版本</td>
	           				<td class="infoData"><s:property value="spacemodel.version"/></td>
	           				<td class="infoTitle">上传人/上传时间</td> 
	           				<td class="infoData"><s:property value="spacemodel.impUser"/>/<s:date name="spacemodel.impDate" format="yyyy-MM-dd HH:mm" /></td>
	           			</tr>
	           			<tr  class="infoData">
	           			<td class="infoTitle">功能描述</td> 
	           				<td colspan="3">
								<s:property value="spacemodel.memo"/>
	           				</td>
	           			</tr>
	           		</table>
	</div>
	<div region="center" border="false" split="false">
	           			<div id="mainFrameTab"  class="easyui-tabs" fit="true" border="false">
	           			<s:if test="spacemodel.status=='未传输'">
							<div title="模型信息" style="width:100%;height:100%;padding:5px;">
			           			<table  width="95%" cellspacing="0" cellpadding="0" border="0" style="border:1px solid #efefef">
								<tr class="header">
									<td  width="1%"><input type="checkbox" name="selectAll" onClick="selectCheck()" id="selectAll" ></td>
									<td   >类型</td>
									<td  >名称</td>
									<td >传输状态</td>
									<td >操作</td>
								</tr>
								 <s:iterator value="list">
									<tr class="cell" >
											<td><input type="checkbox" name="tags" value="<s:property value="id"/>" /></td>					
														<td><s:property value="type"/></td>					
											<td><s:property value="title"/></td>					
										<td><s:property value="status"/></td>
										<td><a href="javascript:edit(3);">查看传输文本</a>&nbsp;<a href="javascript:edit(3);">查看传输日志</a></td>				
									</tr>
								</s:iterator>
			           			</table>
		           			</div>
		           			</s:if>
		           			<s:if test="spacemodel.status=='已传输'">
							<div title="导入日志" style="width:100%;height:100%;padding:5px;">
			           			<table  width="95%" cellspacing="0" cellpadding="0" border="0" style="border:1px solid #efefef">
								<tr class="header">
									<td  width="1%"><input type="checkbox" name="selectAll" onClick="selectCheck()" id="selectAll" ></td>
									<td   >类型</td>
									<td  >名称</td>
									<td >传输状态</td>
									<td >操作</td>
								</tr>
								 <s:iterator value="list">
									<tr class="cell" >
										<td><input type="checkbox" name="tags" value="<s:property value="id"/>" /></td>					
										<td><s:property value="type"/></td>					
										<td><s:property value="title"/></td>					
										<td><s:property value="status"/></td>					
										<td><a href="javascript:edit(3);">查看传输文本</a>&nbsp;<a href="javascript:edit(3);">查看传输日志</a></td>					
									</tr>
								</s:iterator>
			           			</table>
		           			</div>
		           			</s:if>
	           			</div>
	           		</div>
	           <s:form name="editForm" id="editForm" theme="simple">
	           <s:hidden name="spaceId" id="spaceId"></s:hidden>
	           	</s:form>
	</s:else>

		
</body>
</html>
