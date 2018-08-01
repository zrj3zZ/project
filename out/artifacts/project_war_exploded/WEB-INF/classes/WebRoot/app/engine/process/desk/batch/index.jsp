<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>流程任务批量处理</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/process_center.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/process_batch.css">
    <link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyuis.css">
    <link rel="stylesheet" type="text/css" href="iwork_themes/easyui/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
    <script type="text/javascript">
    function selectAll(obj){
    	$("[name="+obj.name+"_]").each(function(){
     		 if($(obj).attr("checked")){
     		 	$(this).attr("checked",true);
     		 }else{
     		 	$(this).attr("checked",false);
     		 }
    	})
    }
    	function selectRow(taskId){
    		if($("#ck_"+taskId).attr("checked")){
    			$("#ck_"+taskId).attr("checked",false);
    		}else{
    			$("#ck_"+taskId).attr("checked",true);
    		}
    	}
    	function openProcess(actDefId,instanceId,excutionId,taskId) {
			var url = 'loadProcessFormPage.action?actDefId='+actDefId+'&instanceId='+instanceId+'&excutionId='+excutionId+'&taskId='+taskId;
			var target = instanceId;
			var page = window.open('form/loader_frame.html',target,'width='+screen.width-50+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location=url;
			return;
		}
		/* $(function(){ 
			$(".icon").click(
					function(){
						$(".icon").hide();
						$(".list-selected").show();
						window.location="process_desk_index.action";
			});	
		})*/ 
		
		function expand(prcDefId,actDefId,actStepId,taskId,formId){
			if($("#content_"+taskId).html()==""){
				var submitdata = {prcDefId:prcDefId,actDefId:actDefId,actStepId:actStepId,taskId:taskId,formId:formId};
				  $.post('processManagement_batch_expand.action',submitdata,function(data)
			            {
					    	$("#content_"+taskId).html(data);
					  });  
			}
			$("#row_"+taskId).show();
	    	$("#expandBtn"+taskId).hide();
	    	$("#unExpandBtn"+taskId).show();
			
		} 
		
		function unExpand(prcDefId,actDefId,actStepId,taskId,formId){
				    	$("#row_"+taskId).hide();
				    	$("#expandBtn"+taskId).show();
				    	$("#unExpandBtn"+taskId).hide();
		} 
		function printPage(key){
		 	var backup = window.document.body.innerHTML ;
			var content = $("#layoutCenter").html();
			 window.document.body.innerHTML = content;
			 window.print();
			 window.document.body.innerHTML = backup;
		}
		function batchHandle(actDefId,prcDefId,actStepId,formId,name){ 
			var count = 0;
			var chk_value =[];    
			$("[name='"+name+"_']").each(function(obj){
     		 	 if($(this).attr("checked")){ 
     		 	 	chk_value.push($(this).val());
     		 	 	count++;
     		 	 }
    		}) ;  
    		if(count>0){
    			var pageUrl = "processBatchHandleOperate.action?taskIds="+chk_value+"&actDefId="+actDefId+"&prcDefId="+prcDefId+"&actStepId="+actStepId+"&formId="+formId;
    			 parent.openWin("批量办理",500,600,pageUrl,window.document.location,'sendDialog');
    		}else{
    			alert('请勾选要批量办理的任务');
    		}
		}
		
		function batchManualJump(actDefId,prcDefId,actStepId,formId,name,mjId,targetStepId,targetStepName){ 
			var count = 0;
			var chk_value =[];    
			$("[name='"+name+"_']").each(function(obj){
     		 	 if($(this).attr("checked")){ 
     		 	 	chk_value.push($(this).val());
     		 	 	count++;
     		 	 }
    		}) ;  
    		if(count>0){
    			var pageUrl = "processBatchManualJumpOperate.action?taskIds="+chk_value+"&actDefId="+actDefId+"&prcDefId="+prcDefId+"&actStepId="+actStepId+"&formId="+formId+"&mjId="+mjId+"&targetStepId="+targetStepId+"&targetStepName="+encodeURI(targetStepName);
    			 parent.openWin("批量办理",500,600,pageUrl,window.document.location,'sendDialog');
    		}else{
    			alert('请勾选要批量办理的任务');
    		}
		}
		
		function executeBack(actDefId,prcDefId,actStepId,formId,name,backType){ 
			var count = 0;
			var chk_value =[];     
			$("[name='"+name+"_']").each(function(obj){
     		 	 if($(this).attr("checked")){ 
     		 	 	chk_value.push($(this).val());
     		 	 	count++;
     		 	 }
    		}) ;  
    		if(count>0){ 
    			var pageUrl = "processBatchBackOperate.action?taskIds="+chk_value+"&actDefId="+actDefId+"&prcDefId="+prcDefId+"&actStepId="+actStepId+"&formId="+formId+"&backType="+backType;
    			 parent.openWin("批量驳回",500,650,pageUrl,window.document.location,'sendDialog');
    		}else{
    			alert('请勾选要批量办理的任务'); 
    		}
		}
		
		 $(function () {
			$('#translist').menubutton({
				menu:'#translist'
			}
			)
		 });  
    </script>
</head>
<body class="easyui-layout" >
<div region="north" border="false" style="height:35px;background:#fafafa;" split="false" id="layoutNorth">
	<div class="process_header">
  <div class="process_head_tab_box"> 
 <a class="process_head_tab_active" title="显示所有待审批流程未读的抄送任务或通知" href="process_desk_index.action">待办流程</a>
     <a class="process_head_tab"  title="显示当前用户已办理，但未最终归档的流程任务"  href="process_desk_finish.action">已办跟踪</a>  
     <a class="process_head_tab"  title="显示所有接收的抄送及流程通知任务"  href="process_desk_notice.action"> 通知/抄送</a>
      <a class="process_head_tab"  title="显示当前用户已办理，所有办理过的任务历史及审批意见"  href="process_desk_history.action"> 办理日志</a>
      </div>
      <div style="padding-top:3px;float:right;padding-right:90px">
        <a  href="#" onclick="printPage()" text='Ctrl+s' class="easyui-linkbutton" plain="true" iconCls="icon-print">打印</a>
      
      </div>
       <!-- 
  <div class="process_head_tab_right">
  
    <form id="search" class="search">
      <input class="txt" type="text">
      </input>
      <button class="search-submit" data-ca="search-btn"></button>
    </form>
  </div> -->
  <div class="switch-view">
	<!-- <a href="#" class="list-selected"  title="切换到缩略图模式" style="display:none"></a> -->
		<a href="process_desk_index.action" class="icon" title="切换到列表模式" ></a>
 </div>
</div>
</div>
	<div region="center" border="false" style="border-left:1px solid #efefef;" id="layoutCenter" >
	<s:if test="content==''">
		<div class="tipsInfo">未发现批量办理任务！</div>
	</s:if>
  <s:property value="content" escapeHtml="false"/>
    </div>
    
</body>
</html>