<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title></title>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/mobile/jquery.mobile-1.4.5.min.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqm-datebox.min.css" /> 
	<script src="iwork_js/jqueryjs/jquery-3.0.8.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.mobile-1.4.5.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.layout-latest.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jqm-datebox.core.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jqm-datebox.mode.calbox.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.mobile.datebox.i8n.CHN-S.js"></script>
	
	<script type="text/javascript"> 
	 	function editSubForm(id){
	 		var pageUrl = 'process_mb_subForm_edit.action?taskId='+$("#taskId").val()+'&instanceId='+$("#instanceId").val()+'&subformid='+$("#formId").val()+'&id='+id;
	 		loadframeURL("编辑子表",pageUrl);
	 		//window.FrameLink.loadLink("编辑子表",pageUrl); 
	 	}  
	 	function delSubForm(id){
	 		if(confirm("确定要删除吗？")){
	 		var  formid = $("#formId").val(); 
	 		var  instanceId = $("#instanceId").val(); 
	 		var url = "process_mb_subForm_remove.action"; 
				 $.post(url,{id:id,instanceId:instanceId,subformid:formid},function(data){
			    	if(data=='true'){
			    		 this.location.reload();
			    	}else{
			    		alert('删除失败');
			    	}
			  	});  
			}
	 	}  
	 	function addSubForm(){ 
	 		var pageUrl = 'process_mb_subForm_add.action?taskId='+$("#taskId").val()+'&instanceId='+$("#instanceId").val()+'&subformid='+$("#formId").val();
	 	//	window.FrameLink.loadLink("新增行",pageUrl); 
	 		loadframeURL("新增行",pageUrl);
	 	}  
	 	function back(){ 
	 			backTodolist();
				//window.FrameLink.back();    
	 	}
	 	//打开子表数据字典
		function openSubGridDictionary(id,subformkey,subformid){
			var pageUrl = "weixin_dictionary_runtime_show.action?dictionaryId="+id+"&subformkey="+subformkey+"&subformid="+subformid;
			loadframeURL("",pageUrl);
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
<div >
	<!-- <ul data-role="listview" data-inset="true">  -->
			<s:property value="content" escapeHtml="false"/>
			<!-- </ul>  -->
</div>
<!-- 
	<div data-role="footer" style="padding:10px" data-fullscreen="true" class="ui-bar" data-position="fixed">
    	<a href="javascript:addSubForm();" style="width:130px;" data-role="button" data-icon="plus" data-mini="true" data-inline="true" >追加记录</a>
    	<a href="javascript:back();" style="width:130px;" data-role="button"  data-icon="back" data-mini="true" data-inline="true">返回</a>
	</div>
	 -->		
			<s:hidden name="subformid" id="subformid"></s:hidden>
			<s:hidden name="taskId" id="taskId"></s:hidden>
			<s:hidden name="instanceId"  id="instanceId" ></s:hidden>
			<s:hidden name="formId" id="formId" ></s:hidden>
			<s:hidden id ="deviceType" name="deviceType"/>
</body>
</html>
