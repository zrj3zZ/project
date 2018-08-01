<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/public.css" />
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js" ></script>
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformsearch_index.css">
	<script type="text/javascript" src="iwork_js/engine/iformsearch_index.js" ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style>
		.tbLable td{
			font-size:16px;
		}
	</style>
	<script type="text/javascript">
	function clearData(){
		var width=500;
		var height=300;
		
		var content = "确认清除所有待办及一般人物吗？删除该数据将无法恢复"; 
		art.dialog.confirm('你确认删除操作？', function(topWin){
		    		this.content('开始执行清理操作 ...').time(2);
					doClear();
		}, function(){
		   this.close();
		});
	}
		function doClear(){
			$.post('system_process_doclear.action',{},function(data)
            {
		    	if(data=='success'){ 
		    		 alert("清理完毕");
		    		 window.location.reload();
		    	}else{
		    		 alert("清理异常请稍后");
		    	}
		  });  
		
		}
	</script>
</head>
<body class="easyui-layout">
<!-- TOP区 -->
	<div region="north" border="false" style="height:70px;border:1px solid #efefef;font-size:20px;font-family:微软雅黑">
	<img src="iwork_img/gear3.gif" style="width:50px"/>数据初始化 
   </div>
 
	<div region="center" style="padding:5px;border:0px;height:300px"> 
		<a href="javascript:clearData();" class="easyui-linkbutton" plain="false" iconCls="icon-remove">清理流程数据信息</a><br /><br />
			点击清理全部待办及一般流程
	</div>
</body> 
</html>