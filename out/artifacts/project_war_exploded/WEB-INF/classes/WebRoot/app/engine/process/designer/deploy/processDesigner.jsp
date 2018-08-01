<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" /> 
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style type="text/css">
		.header td{ 
			font-weight:bold;
			font-size:12px;
			padding:3px;
			white-space:nowrap;
			padding-left:5px;
			background:#fafafa url('iwork_css/jquerycss/default/images/datagrid_header_bg.gif') repeat-x left bottom;
			border-bottom:1px dotted #ccc;
			border-top:1px dotted #fff;
			border-right:1px dotted #ccc;
		}
		.cell td{
			margin:0;
			padding:3px 4px;
			white-space:nowrap;
			word-wrap:normal;
			overflow:hidden;
			text-align:left;
			border-bottom:1px dotted #eee;
			border-top:1px dotted #fff;
			border-right:1px dotted #ccc;
		}
	</style>
	<script type="text/javascript">
	 var api = frameElement.api, W = api.opener; 
	$(function(){  
		if(api.get("newProcessDg")!=null){
			api.get("newProcessDg",1).close();
		}
	})
	
		function setActDefId(actDefid){
			$('#actProcId').val(actDefid);
			art.dialog.tips('保存成功');
		}
		function setGlobal(msg){
			var actdefId = $('#actProcId').val();  
			var prcDefId = $('#id').val();  
			var title =    $('#processName').val();  
			var pageUrl = 'sysFlowDef_index.action?prcDefId='+prcDefId+'&actdefId='+actdefId;
			
			art.dialog.open(pageUrl,{
						id:'ProcessDefWinDiv',
						title:'全局设置['+title+']',  
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width:'90%',
					    height:'90%'
					 });
					 
		}
		function openDialog(id,type,name,tab){ 
			var pageUrl=""; 
			var actdefId = $('#actProcId').val();  
			var prcDefId = $('#id').val();  
			var title =    $('#processName').val(); 
		    if(tab=="BaseMap"){
		    	pageUrl = 'sysFlowDef_stepMap!load.action?actDefId='+actdefId+'&prcDefId='+prcDefId+'&actStepDefId=usertask'+id;
		    }else if(tab=="Route"){
		    	pageUrl = 'sysFlowDef_stepRoute!load.action?pageindex=1&actDefId='+actdefId+'&prcDefId='+prcDefId+'&actStepDefId=usertask'+id;
		    }else if(tab=="form"){
		  		pageUrl = 'sysFlowDef_stepFormSet!load.action?pageindex=2&actDefId='+actdefId+'&prcDefId='+prcDefId+'&actStepDefId=usertask'+id;
		    }else if(tab=="MJ"){
		    	pageUrl = 'sysFlowDef_stepManualJump!load.action?pageindex=3&actDefId='+actdefId+'&prcDefId='+prcDefId+'&actStepDefId=usertask'+id;
		    }else if(tab=="OffLine"){
		    	pageUrl = 'sysFlowDef_stepOffLine_load!load.action?pageindex=4&actDefId='+actdefId+'&prcDefId='+prcDefId+'&actStepDefId=usertask'+id;
		    }else if(tab=="SJ"){
		    	pageUrl = 'sysFlowDef_stepSysJump!load.action?pageindex=5&actDefId='+actdefId+'&prcDefId='+prcDefId+'&actStepDefId=usertask'+id;
		    }else if(tab=="Supervise"){
		    	pageUrl = 'sysFlowDef_stepSupervise!load.action?pageindex=6&actDefId='+actdefId+'&prcDefId='+prcDefId+'&actStepDefId=usertask'+id;
		    }else if(tab=="trigger"){
		    	pageUrl = 'sysFlowDef_stepTrigger!load.action?pageindex=7&actDefId='+actdefId+'&prcDefId='+prcDefId+'&actStepDefId=usertask'+id;
		    }else if(tab=="jsTrigger"){
		    	pageUrl = 'sysFlowDef_stepScriptTrigger_load!load.action?pageindex=8&actDefId='+actdefId+'&prcDefId='+prcDefId+'&actStepDefId=usertask'+id;
		    }else if(tab=="diybtn"){
		    	pageUrl = 'sysFlowDef_stepDIYBtn_load.action?pageindex=9&actDefId='+actdefId+'&prcDefId='+prcDefId+'&actStepDefId=usertask'+id;
		    }else if(tab=="Summary"){
		    	pageUrl = 'process_step_summary.action?pageindex=10&actDefId='+actdefId+'&prcDefId='+prcDefId+'&actStepDefId=usertask'+id;
		    }else if(tab=="Help"){ 
		    	pageUrl = 'sysFlowDef_stepMap!load.action?pageindex=11&actDefId='+actdefId+'&prcDefId='+prcDefId+'&actStepDefId=usertask'+id;
		    }
			if(tab==""){art.dialog.tips('参数异常');return false;}
			//window.open(pageUrl,'','height=600p,width:800px;center:no'); 
			//var url; //转向网页的地址;
		var name = ""; //网页名称，可为空;
		var iWidth = 800; //弹出窗口的宽度;
		var iHeight = 600; //弹出窗口的高度; 
		var iTop = (window.screen.availHeight-30-iHeight)/2; //获得窗口的垂直位置;
		var iLeft = (window.screen.availWidth-10-iWidth)/2; //获得窗口的水平位置;
		art.dialog.open(pageUrl,{
						id:'ProcessDefWinDiv',
						title:'设置',  
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width:'90%',
					    height:'90%'
					 });
					 
	//	window.open(pageUrl,name,'height='+iHeight+',,innerHeight='+iHeight+',width='+iWidth+',innerWidth='+iWidth+',top='+iTop+',left='+iLeft+',toolbar=no,menubar=no,scrollbars=auto,resizeable=no,location=no,status=no');
			/* 
			art.dialog.open(pageUrl,{
				id:'ProcessDefStepWinDiv',
		    	cover:true,
				title:title,
				loadingText:'正在加载中,请稍后...',
				bgcolor:'#999',
				rang:true, 
				width:850,
				cache:false, 
				lock: true,
				height:500,
				iconTitle:false,
				extendDrag:true,
				autoSize:false
			});
			dg.zindex();
			dg.ShowDialog();*/
		}
		function openInfo(id,type,name){
			var actdefId = $('#actProcId').val();  
			var prcDefId = $('#id').val();  
			var title =    $('#processName').val();  
			var pageUrl = "";
			//if(type=="Switch2ow2Figure"){
				pageUrl = 'process_step_service_index.action?edittype=notab&pageindex=4&actDefId='+actdefId+'&prcDefId='+prcDefId+'&actStepDefId=serviceTask'+id;
			
			alert(pageUrl);
			art.dialog.open(pageUrl,{
						id:'ProcessDefWinDiv',
						title:'设置',  
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
	<div region="center" style="padding:3px;border-top:0px;height:97%" >
		<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0" width="100%" height="100%" id="FlashVars" align="middle">
			<param name="allowscrīptAccess" value="sameDomain" />
			<param name="movie" value="designer/kbpmDesigner.swf" />
			<param name="FlashVars" value="status=<s:property value="processShowType"/>&processKey=<s:property value="processKey"/>&proId=<s:property value="id"/>&processName=<s:property value="processName"/>&actProcId=<s:property value="actProcId"/>&ip=<s:property value="currentIp"/>&port=<s:property value="currentPort"/>" />
			<param name="quality" value="high" />
			<param name="wmode" value="window" />
			<param name="bgcolor" value="#ffffff" />  
		<embed src="designer/kbpmDesigner.swf" FlashVars="status=<s:property value="processShowType"/>&processKey=<s:property value="processKey"/>&proId=<s:property value="id"/>&processName=<s:property value="processName"/>&actProcId=<s:property value="actProcId"/>&ip=<s:property value="currentIp"/>&port=<s:property value="currentPort"/>" width="100%" height="99%"   quality="high" bgcolor="#ffffff" width="550" height="400" name="FlashVars" align="middle" allowscrīptAccess="sameDomain" FlashVars="foo=happy2005&program=flash&language=简体中文-中国" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />
		</OBJECT>
		</div>
	    <form id="mainFrom" name="mainFrom">
	    	<s:hidden  id="id" name="id"></s:hidden> 
	    	<s:hidden  id="actProcId" name="actProcId"></s:hidden>
	    	<s:hidden  id="processName" name="processName"></s:hidden>  
   		 </form>  
</body> 
</html>
