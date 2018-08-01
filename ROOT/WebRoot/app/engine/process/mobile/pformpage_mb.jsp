<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title></title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jquery.mobile-1.3.2.min.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqm-datebox.min.css" /> 
	<script src="iwork_js/jqueryjs/jquery-3.0.8.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.mobile-1.3.2.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.layout-latest.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jqm-datebox.core.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jqm-datebox.mode.calbox.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.mobile.datebox.i8n.CHN-S.js"></script>
<script type="text/javascript" src="iwork_js/mobile/iOSJavascriptBridge.js"></script>
<script type="text/javascript" src="iwork_js/mobile/sys_mobile_adapter.js"></script> 

	<script type="text/javascript"> 
	 jQuery.extend(jQuery.mobile.datebox.prototype.options, {
       'overrideDateFormat': '%Y-%m-%d',
       'overrideHeaderFormat': '%Y-%m-%d' 
   });
		//保存脚本触发器事件
		function formSaveEventScript(){
			<s:property value='saveScriptEvent' escapeHtml='false'/>
		}
		//办理脚本触发器事件
		function formTransEventScript(){
			<s:property value='transScriptEvent' escapeHtml='false'/>
		} 
		 <s:property value='script' escapeHtml='false'/>
		jQuery(function($) { 
		// 使用 jQuery 异步提交表单 
			$('#iformMain').submit(function() { 
					var alertMsg='';
					$.ajax({ 
						url: 'processRuntimeFormSave.action', 
						data: $('#iformMain').serialize(), 
						type: "post", 
						cache : false, 
						success: function(data) 
						{
								var arr=data.split(',');
								if(arr!=null&&arr.length==5){
									if(arr[0]=='success'){
										  //更新instanceId和dataid
											document.getElementById('instanceId').value=arr[1];
											document.getElementById('dataid').value=arr[2];
											document.getElementById('taskId').value=arr[3];
											document.getElementById('excutionId').value=arr[4];
											alertMsg='保存成功';
									}
								
								}else{
									alertMsg='主表保存失败(错误编号:'+arr[0]+')';
								}
								showTip(alertMsg);
								//alert();
						} 
					}
				); 
				return false; 
			}); 
		});  
		 function saveForm(){
		 	formSaveEventScript();
			$('#iformMain').submit();
		 }
		//抢签任务，执行任务领用
			function claimTask(){
				  $.post('processManagement_claimTask.action',$("#iformMain").serialize(),function(data)
			            {
					    	if(data=='success'){
					    		showTip('任务已领取，请重新打开');
					    		backTodolist();
					    	}else{
					    		showTip('任务领取失败');
					    		backTodolist();
					    	}
					  }); 
			}
		 //执行顺序流转动作
		function executeHandle(){
				//===============执行办理动作的表单自定义控制脚本================
				try{
					var flag = formTransEventScript();
					if(flag==false){
						return flag;
					}
				}catch(e){}
				//==================END==================================
				var actDefId = $("#actDefId").val();
				var actStepDefId = $("#actStepDefId").val();
				var excutionId = $("#excutionId").val();
				var taskId = $("#taskId").val();
				var instanceId = $("#instanceId").val();
				var prcDefId = $("#prcDefId").val();
				var formId = $("#formId").val();
				var dataid = $("#dataid").val();
				var pageUrl = "process_mb_RuntimeHandle.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid;
				loadframeURL("流程办理",pageUrl);
				//window.FrameLink.loadLink(,pageUrl);  
			}
			function showSubform(taskId,instanceId,subformId,subtitle){
				var actDefId = $("#actDefId").val();
				var actStepDefId = $("#actStepDefId").val();
				var prcDefId = $("#prcDefId").val();
				var pageUrl = "process_mb_subForm_showpage.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+subformId;
				loadframeURL(subtitle,pageUrl);
			//	window.FrameLink.loadLink(subtitle,pageUrl);    
			}
			function addOpinion(){
				var actDefId = $("#actDefId").val();
				var actStepDefId = $("#actStepDefId").val();
				var excutionId = $("#excutionId").val();
				var taskId = $("#taskId").val();
				var instanceId = $("#instanceId").val();
				var prcDefId = $("#prcDefId").val();
				var pageUrl = "process_mb_addopinion.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId;
				loadframeURL("填写审核意见",pageUrl);
				//window.FrameLink.loadLink("填写审核意见",pageUrl);   
			}
			//执行审核跳转动作
		function executeManualJump(mjId,activityId,activityName){
				//=====================================================
				//===============执行办理动作的表单自定义控制脚本================
				//=====================================================
				try{
					var flag = formTransEventScript();
					if(flag==false){
						return flag;
					}
				}catch(e){}
				//==================END==================================
				var actDefId = $("#actDefId").val();
				var actStepDefId = $("#actStepDefId").val();
				var excutionId = $("#excutionId").val();
				var taskId = $("#taskId").val();
				var instanceId = $("#instanceId").val();
				var prcDefId = $("#prcDefId").val();
				var formId = $("#formId").val();  
				var dataid = $("#dataid").val();
				var pageUrl = "process_mb_RuntimeManualJump.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid+"&mjId="+mjId+"&targetStepId="+activityId;
				loadframeURL(activityName,pageUrl); 
				//window.FrameLink.loadLink(activityName,pageUrl); 
		}
		
		//驳回操作
		function executeBack(type){
				var actDefId = $("#actDefId").val();
				var actStepDefId = $("#actStepDefId").val();
				var excutionId = $("#excutionId").val();
				var taskId = $("#taskId").val();
				var instanceId = $("#instanceId").val();
				var prcDefId = $("#prcDefId").val();
				var formId = $("#formId").val();
				var dataid = $("#dataid").val();
				var pageUrl = "process_mb_RuntimeBack.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid+"&backType="+type;
				loadframeURL("任务驳回",pageUrl); 
				//window.FrameLink.loadLink("任务驳回",pageUrl); 
		}
		//加签操作
		function addSign(){
				var actDefId = $("#actDefId").val();
				var actStepDefId = $("#actStepDefId").val();
				var excutionId = $("#excutionId").val();
				var taskId = $("#taskId").val();
				var instanceId = $("#instanceId").val();
				var prcDefId = $("#prcDefId").val(); 
				var formId = $("#formId").val();
				var dataid = $("#dataid").val();
				var pageUrl = "process_mb_RuntimeAddSign.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid;
				//this.showSendWindow(pageUrl,"加签操作",600,400);
				loadframeURL("加签操作",pageUrl); 
				//window.FrameLink.loadLink("加签操作",pageUrl); 
		}
		
		//加签完毕操作
		function backAddSign(){
				var actDefId = $("#actDefId").val();
				var actStepDefId = $("#actStepDefId").val();
				var excutionId = $("#excutionId").val();
				var taskId = $("#taskId").val();
				var instanceId = $("#instanceId").val();
				var prcDefId = $("#prcDefId").val();
				var formId = $("#formId").val();
				var dataid = $("#dataid").val();
				var pageUrl = "process_mb_RuntimeBackAddSign.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid;
				//this.showSendWindow(pageUrl,"加签操作");
				loadframeURL("加签操作",pageUrl); 
				//window.FrameLink.loadLink("加签操作",pageUrl); 
		}
		function addOLWin(){
			var actDefId = $("#actDefId").val();
			var actStepDefId = $("#actStepDefId").val();
			var taskId = $("#taskId").val();
			var instanceId = $("#instanceId").val();
			var excutionId = $("#excutionId").val(); 
	        var pageurl='ProcessStepOL_add.action?actDefId='+actDefId+'&taskId='+taskId+'&actStepDefId='+actStepDefId+'&excutionId='+excutionId+'&instanceId='+instanceId;
	        loadframeURL("线下任务反馈",pageUrl); 
	      // window.FrameLink.loadLink("离线任务反馈",pageUrl); 
		}
		//执行审核跳转动作
		function selectStep(activityId){
				//==================END==================================
				var actDefId = $("#actDefId").val();
				var actStepDefId = $("#actStepDefId").val();
				var excutionId = $("#excutionId").val();
				var taskId = $("#taskId").val();
				var instanceId = $("#instanceId").val();
				var prcDefId = $("#prcDefId").val();
				var formId = $("#formId").val();  
				var dataid = $("#dataid").val();
				var pageUrl = "process_mb_RuntimeManualJump.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid+"&targetStepId="+activityId;
				loadframeURL("任务办理",pageUrl); 
				//window.FrameLink.loadLink("任务办理",pageUrl);
		} 
		//转发操作
		function execForward(){
				var actDefId = $("#actDefId").val();
				var actStepDefId = $("#actStepDefId").val();
				var excutionId = $("#excutionId").val();
				var taskId = $("#taskId").val();
				var instanceId = $("#instanceId").val();
				var prcDefId = $("#prcDefId").val();
				var formId = $("#formId").val();
				var dataid = $("#dataid").val();
				var pageUrl = "process_mb_RuntimeForward.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid;
				loadframeURL("任务转发",pageUrl); 
		}
		
		function dosearchAddress(fieldName,obj){
			if(obj.length>2){
				$.ajax({ 
						url: 'ifrom_mb_radio_address_search.action?fieldName='+fieldName+'&searchkey='+obj, 
						data: $('#iformMain').serialize(), 
						type: "post", 
						cache : false, 
						success: function(data)  
						{
							if(data==""){
								$("#searchLister").html("<li>未发现账户地址</li>");
								$("#searchLister").listview("refresh"); 
								$("#searchLister").trigger("create");
							}else{
								$("#searchLister").html(data);
								$("#searchLister").listview("refresh"); 
								$("#searchLister").trigger("create");
							}
						} 
					}
				);
			}
		}
		function dosearchMultiAddress(fieldName,obj){
			if(obj.length>2){
				$.ajax({ 
						url: 'ifrom_mb_multi_address_search.action?fieldName='+fieldName+'&searchkey='+obj, 
						type: "post", 
						cache : false, 
						success: function(data)  
						{
							if(data==""){
								$("#searchLister").html("<li>未发现账户地址</li>");
								$("#searchLister").listview("refresh"); 
								$("#searchLister").trigger("create");
							}else{
								$("#searchLister").html(data);
								$("#searchLister").listview("refresh"); 
								$("#searchLister").trigger("create");
							}
						} 
					}
				);
			}
		}
		
		function showPage(){
			var actDefId = $("#actDefId").val();
			var actStepDefId = $("#actStepDefId").val();
			var excutionId = $("#excutionId").val();
			var taskId = $("#taskId").val();
			var instanceId = $("#instanceId").val();
			var prcDefId = $("#prcDefId").val();
			var formId = $("#formId").val();
			var dataid = $("#dataid").val();
			var pageUrl = "loadProcessFormPage.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&excutionId="+excutionId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&dataid="+dataid;
			loadframeURL("原表单",pageUrl); 
		}
		function setAddress(fieldname,obj){
			$('#'+fieldname).val(obj); 
			 $('.ui-dialog').dialog('close'); 
		}
	</script>
	<style> 
		.mb_title{ 
			color:#999;
			font-size:12px;
			padding-top:2px;
			padding-bottom:2px;
		}
		.mb_data{
			padding-left:30px;
			font-size:16px;
			color:#0000FF;
			border-bottom:1px solid #999;
		} 
		<s:property value="style" escapeHtml="false"/>
	</style>
</head>
<body>
 

	<div data-role="page" class="type-interior"> 
	<div data-role="content"> 
		<form   id="iformMain" name="iformMain" method="post"  data-ajax="true" action='processRuntimeFormSave.action' >
		<div>
			<s:property value='content' escapeHtml='false'/> 
			<!-- 
			<a href="javascript:executeHandle();" data-role="button" data-icon="check">办理</a>
			<a href="#popupBasic" data-rel="popup">Open Popup</a> -->
		</div> 
			<s:property value='opinionList' escapeHtml='false'/> 
		<!--表单参数-->
		<span style="display:none">
			<s:hidden id ="actDefId" name="actDefId"/>
			<s:hidden  id ="prcDefId" name="prcDefId"/>
			<s:hidden id ="actStepDefId" name="actStepDefId"/>
			<s:hidden id ="formId" name="formId"/>
			<s:hidden id ="taskId" name="taskId"/>
			<s:hidden id ="instanceId" name="instanceId"/>
			<s:hidden id ="excutionId" name="excutionId"/>
			<s:hidden id ="dataid" name="dataid"/>
			<s:hidden id ="deviceType" name="deviceType"/>
			<input name='submitbtn' id='submitbtn' type="submit" />
		</span> 
	</form>
 
	</div><!-- /content --> 
</div><!-- /page --> 
</body>
</html>
