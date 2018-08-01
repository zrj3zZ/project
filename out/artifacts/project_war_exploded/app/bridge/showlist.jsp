<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/design_showlist.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/design_model.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/bridge/conn_bridge.js" ></script>	
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style type="text/css">
		.memo span {
			display:block;
			width:350px;
			overflow: hidden;
			white-space: nowrap;
			text-overflow: ellipsis;
			}
		.tabcss{
			 border: 1px solid #efefef;
    		 width: 100%;
    		 line-height: 30px;
    		 
		}
		.tabcss tr th{
			background: #efefef;
			border-bottom: 1px solid #efefef;
			height: 15px;
			padding-left: 5px;
			font-size: 14px;
			color:  #585858;
		}
		.tabcss tr td{
			text-align:center;
			border-bottom: 1px solid #efefef;
			height: 15px;
			font-size: 14px;
			color:  #585858;
		}
	</style>
	<script type="text/javascript">
		$(function(){
		})
		function addBridge(){ 
			var pageUrl = "conn_bridge_add.action?groupid=1"; 
			 art.dialog.open(pageUrl,{
				    id:'iformBaseWinDiv',
					title:'新建连接桥',  
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:550,
				    height:410,
				    close:function(){
				 		location.reload();
				    }
			  });

		}
		//模拟登陆
		function showLogin(uuid){
			var pageUrl = 'conn_bridge_portal_run.action?uuid='+uuid;
			art.dialog.open(pageUrl,{
				id:'showLogin',
				title:'模拟登陆',
				lock:true,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:'90%',
			    height:'90%'
			 });
		}
	</script>
</head> 
<body class="easyui-layout">  
	<!-- TOP区 -->
	
	<div region="north" border="false" style="padding:0px;overflow:no;scrolling:no;border-bottom:1px;">
		<div class="tools_nav"> 
		<table width="100%"  border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td> 
					<a href="javascript:addBridge();" class="easyui-linkbutton" plain="true" iconCls="icon-add">新增</a>
						<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
				</td>
				<td>
				 
		  		</td>
			</tr>
		</table> 
		 
		 </div>
	</div>
	<div region="center" style="padding:3px;border:0px;">
		<table class="tabcss">
			<tr>
				<th>连接名称</th>
				<th>连接状态</th>
				<th>唯一标识</th>
				<th>操作</th>
			</tr>
			<s:iterator value="list" >
				<tr>
					<td><s:property value="bridgeName"/></td>
					<td><s:property value="status"/></td>
					<td><s:property value="uuid"/></td>
					<td>
						<a href="#" class="easyui-linkbutton" onclick="showLogin('<s:property value="uuid"/>')">模拟</a>
						<a href="#" class="easyui-linkbutton" onclick="showParamsDlg(<s:property value="id"/>)"   plain="false" >参数设置</a>
						<a href="javascript:del(<s:property value="id"/>)" class="easyui-linkbutton">删除</a>
					</td>
				</tr>
			</s:iterator>
		</table>
	</div>
	
</body>
</html>
