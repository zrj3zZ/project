<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=0.8, user-scalable=0">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title><s:property value="title"/></title>
	  	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/mobile/jquery.mobile-1.4.5.min.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqm-datebox.min.css" /> 
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css" /> 
	<script src="iwork_js/jqueryjs/jquery-3.0.8.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.mobile-1.4.5.min.js"></script>
	<script type="text/javascript" src="iwork_js/weixin/weixin_formpage.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script type="text/javascript"> 
	 	function editSubForm(id){
	 		var pageUrl = 'weixin_process_subForm_edit.action?taskId='+$("#taskId").val()+'&instanceId='+$("#instanceId").val()+'&formId='+$("#formId").val()+'&subformid='+$("#subformid").val()+'&id='+id;
	 		this.showSendWindow(pageUrl); 
	 	}  
	 	function delSubForm(id){
	 		if(confirm("确定要删除吗？")){
	 		var  formid = $("#subformid").val(); 
	 		var  instanceId = $("#instanceId").val(); 
	 		var url = "weixin_process_subForm_remove.action"; 
				 $.post(url,{id:id,instanceId:instanceId,subformid:formid},function(data){ 
			    	if(data=='true'){
			    		 window.location.reload();
			    	}else{ 
			    		alert('删除失败');
			    	}
			  	});  
			}
	 	}  
	 	function addSubForm(){ 
	 		var pageUrl = 'weixin_process_subForm_add.action?taskId='+$("#taskId").val()+'&instanceId='+$("#instanceId").val()+'&formId='+$("#formId").val()+'&subformid='+$("#subformid").val();
	 		this.showSendWindow(pageUrl); 
	 	}  
	 	function back(){ 
	 			backTodolist();
	 	}
	 	
	 	//打开数据字典
		function openDictionary(uuid){
			var pageUrl = "weixin_dictionary_runtime_show.action?dictionaryUUID="+uuid;
			art.dialog.open(pageUrl,{
				id:'dg_dictionary',  
			    title:'数据字典',
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:'100%', 
	            height:'100%'   
			 });
		}
	 	
	 	
	 	//打开子表数据字典
		function openSubGridDictionary(id,subformkey,subformid){
			var instanceid = $("#instanceId").val(); 
			var pageUrl = "weixin_dictionary_runtime_show.action?dictionaryId="+id+"&subformkey="+subformkey+"&instanceId="+instanceid+"&subformid="+subformid;
			this.showSendWindow(pageUrl); 
		}
	</script>
	<style> 
		.subGridDiv{
			padding:5px;
			border-bottom:1px dotted #999;
		}
		.subGridItemDiv td{
			padding:5px;
			
		}
		.mb_title{
			color:#999;
			font-size:12px;
			padding-top:2px;
			padding-bottom:2px;
			white-space:nowrap;
		}
		.mb_data{
			padding-left:30px;
			font-size:16px;
			color:#0000FF;
			border-bottom:1px solid #efefef;
			word-wrap: break-word; 
			word-break: normal; 
		} 
		<s:property value="style" escapeHtml="false"/>
	</style>
</head>
<body>
<div data-role="page" class="type-interior"> 
<div data-role="content"> 
<s:property value="content" escapeHtml="false"/>

<s:hidden name="subformid" id="subformid"></s:hidden>
			<s:hidden name="taskId" id="taskId"></s:hidden>
			<s:hidden name="instanceId"  id="instanceId" ></s:hidden>
			<s:hidden name="formId" id="formId" ></s:hidden>
			<s:hidden id ="deviceType" name="deviceType"/>
</div>
<s:property value="toolbar" escapeHtml="false"/> 
</div>
</body>
</html>
<script>
function hideOption(){
	 wx.hideOptionMenu();
}
setTimeout("hideOption()",500);
</script>