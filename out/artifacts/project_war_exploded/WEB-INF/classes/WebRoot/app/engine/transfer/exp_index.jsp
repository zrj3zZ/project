<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/systransfer.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/design_model.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/commons.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/engine/sysExpModule.js"></script>	  
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>   		
	<script type="text/javascript">  
		function expModule(){
			var expStr = "";
			for(var i=0;i<selectList.length;i++){ 
						expStr += selectList[i].typeId+"|"+selectList[i].id+",";
			}
			//$("#explist").val(expStr);
			var pageUrl = "sysExp_doExecute.action?explist="+expStr;
			art.dialog.open(pageUrl,{
				id:'sysImp_uploadModule',
				title:'模型导出',
				lock:true,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:550,
		        height:380,
		        close:function (){
		             window.location.reload();
		        }
			 });
			/*
			var url='sysExp_doExecute.action';
		    $.post(url,$("#editForm").serialize(),function(data){
		    });
		    */
		} 	
	</script>
	<style type="text/css">
		.selectArea{
			border:1px solid #efefef;
			padding:5px;
			font-family:微软雅黑;
			line-height:20px;
		}
		.expand{
			padding:5px;
			background-color:#efefef;
		}
	</style>
</head>
<body class="easyui-layout">
<!-- TOP区 -->
	<div region="north" border="false" style="padding:0px;overflow:no;scrolling:no;border-bottom:1px;">
		<div class="tools_nav"> 
		<table width="100%"  border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td> 
				<a href="javascript:expModule();" class="easyui-linkbutton" plain="true" iconCls="icon-expModule">导出模型</a>
				<a href="javascript:expModule();" class="easyui-linkbutton" plain="true" iconCls="icon-search">导出日志</a>
						<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
				</td> 
			</tr>
		</table> 
		 
		 </div>
	</div>
    <!-- 导航区 -->
	<div region="west" split="false" border="true"  style="height:100%;width:330px;padding-left:5px;overflow:auto; border-top:1px solid #F9FAFD">
					<div class="expand"><a href="javascript:expandAll();">全部展开</a>&nbsp;&nbsp;&nbsp;<a href="javascript:unExpandAll()">全部收回</a></div>
							<ul id="transferTree" class="ztree"></ul>
    </div>
	<div region="center" style="padding:5px;width:230px;border:0px;padding-left:5px;">
	<table  height="100%">
		<tr>
			<td style="display:none">
				<a href="javascript:selectModule();" class="easyui-linkbutton" plain="false" iconCls="icon-add">添加</a>
				<br><br>
				<a href="javascript:deleteItem();" class="easyui-linkbutton" plain="false" iconCls="icon-del">删除</a>
			</td>
			<td style="vertical-align:top">
			<div>
			    <form id="editForm" name="editForm" method="post">
			 		<select id="selectModule" class="selectArea" name='selectModule' size='10' multiple style='border:1px solid #efefef;width:310px;height:420px;margin-left:5px;background-color:#FFFFD9' ondblclick='deleteItem();'></select>
						 		<input type="hidden" id="explist" name="explist">
				</form>
             </div>
					<div style="padding-left:10px;">双击选中的模型取消</div>		
			</td>
		</tr>
	</table>
	
	</div>
	
</body>
</html>
