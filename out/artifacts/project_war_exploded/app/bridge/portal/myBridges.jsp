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
	var api = art.dialog.open.api, W = api.opener;
		function update(uuid){
			var pageUrl="syspersion_editMyBridge.action?uuid="+uuid;
			art.dialog.open(pageUrl,{
				id:'editMyBridge',
				title:'编辑连接',
				lock:true,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:'50%',
			    height:'50%',
			    close:function(){
			 		location.reload();
			    }
			 });
		}
		function del(uuid){
			art.dialog.confirm('确认删除?', function(data){
				if (data){
					$.post("syspersion_editMyBridge_delete.action",{uuid:uuid},function(){
						window.location.reload();
				    });
				}
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
						<a href="javascript:void(0);" onclick="update('<s:property value="uuid"/>');" style="color: #4169e1;">编辑参数</a>
						<a href="javascript:void(0);" onclick="del('<s:property value="uuid"/>')" style="color: #4169e1;">删除</a>
					</td>
				</tr>
			</s:iterator>
		</table>
	</div>
</body>
</html>
