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
			var info = "确认传输当前模型吗?"
			if(confirm(info)){
			var spaceId = $("#spaceId").val()
				var url='sysImp_doExecute.action';
				alert(spaceId);
			    $.post(url,{spaceId:spaceId},function(data){
			    
			    });
			}
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
				<a href='javascript:dotransfer();' class="easyui-linkbutton" iconCls="icon-transfer_run" plain="true">传输</a>
				<a href='javascript:showlog();' class="easyui-linkbutton" iconCls="icon-transfer_log" plain="true">传输日志</a>
				<a href='javascript:remove();' class="easyui-linkbutton" iconCls="icon-transfer_del" plain="true">移除当前模型</a>
				<a href='javascript:this.location.reload();' class="easyui-linkbutton" iconCls="icon-reload" plain="true">刷新</a>
		</div>
	</div>
	<div region="center" border="false" split="false">
			<s:form name="editForm" id="editForm" theme="simple">
	           <div class="transferInfo">
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
	           		<div>
	           			<table  width="95%" cellspacing="0" cellpadding="0" border="0" style="border:1px solid #efefef">
						<tr class="header">
							<td  width="1%"><input type="checkbox" name="selectAll" onClick="selectCheck()" id="selectAll" ></td>
							<td   >类型</td>
							<td  >名称</td>
							<td >传输状态</td>
							<td >操作</td>
						</tr>
						 <s:iterator value="list" status="status">
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
	           </div>
	           <s:hidden name="spaceId" id="spaceId"></s:hidden>
	           	</s:form>
	</div>
	</s:else>

		
</body>
</html>
