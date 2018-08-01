<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=0">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>新增行</title>
<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/mobile/jquery.mobile-1.4.5.min.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqm-datebox.min.css" /> 
		<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css" /> 
	<script src="iwork_js/jqueryjs/jquery-3.0.8.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.mobile-1.4.5.min.js"></script>
	<script type="text/javascript" src="iwork_js/weixin/weixin_formpage.js"></script>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>  
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
	<script  type="text/javascript" src="iwork_js/weixin/weixin_tools.js"></script> 
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script>
	var mainFormValidator;
		 $().ready(function() {
			 mainFormValidator =  $("#iformMain").validate(); 
				mainFormValidator.resetForm();
			});
	 	function dosave(){ 
	 	var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){
				return false;
			}else{
					$.ajax({ 
						url: 'subformSaveItem.action?oper=add', 
						data: $('#iformMain').serialize(), 
						type: "post", 
						cache : false, 
						success: function(data) 
						{
									if(data=='true'){
											alertMsg='保存成功';
									}else{
										alertMsg='保存失败';
										
									}
								showTip(alertMsg);
								showSysTips();
								 back();
						} 
					});
			
			}
	 		
	 	}
	 	//打开数据字典
		function openDictionary(id){
			var pageUrl = "weixin_dictionary_runtime_show.action?dictionaryUUID="+id;
			art.dialog.open(pageUrl,{
				id:'dg_dictionary',  
			    title:'数据字典',
				background: '#999', // 背景色 
			    opacity: 0.87,	// 透明度
			    width:'100%', 
	            height:'100%'   
			 });
		}
		//打开数据字典
		function openSubDictionary(id){
			var pageUrl = "weixin_dictionary_runtime_show.action?dictionaryId="+id;
			art.dialog.open(pageUrl,{
				id:'dg_dictionary',  
			    title:'数据字典',
				background: '#999', // 背景色 
			    opacity: 0.87,	// 透明度
			    width:'100%', 
	            height:'100%'   
			 });
		}
	 	function back(){ 
	 	    var actDefId = $("#actDefId").val();
			var actStepDefId = $("#actStepDefId").val();
			var prcDefId = $("#prcDefId").val();
			var taskId = $("#taskId").val();
			var instanceId = $("#instanceId").val();
			var formId = $("#formId").val(); 
			var pageUrl = "weixin_subForm_showpage.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId;
	 		redirectUrl(pageUrl);  
	 	}
	 	function del(){ 
				$.ajax({ 
						url: 'subformItem_remove.action', 
						data: $('#iformMain').serialize(), 
						type: "post", 
						cache : false, 
						success: function(data) 
						{
									if(data=='true'){
											alertMsg='保存成功';
									}else{
									}
						} 
					});    
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
			border-bottom:1px solid #efefef;
		} 
		<s:property value="style" escapeHtml="false"/>
	</style>
</head> 
<body> 
<div  data-role="page" id="pageone" >
 <div data-role="content" >
<form id="iformMain" name="iformMain" method="post" >
				<s:property value="content" escapeHtml="false"/>
				<s:hidden name="actDefId" id="actDefId"></s:hidden>
				<s:hidden name="actStepDefId" id="actStepDefId"></s:hidden>
				<s:hidden name="prcDefId" id="prcDefId"></s:hidden>
				<s:hidden name="taskId" id="taskId"></s:hidden>
				<s:hidden name="instanceId" id="instanceId"></s:hidden>
				<s:hidden name="subformid" id="subformid"></s:hidden>
				<s:hidden name="formId" id="formId"></s:hidden>
				<s:hidden name="modelType" id="modelType"></s:hidden>
				<s:hidden name="id" id="id"></s:hidden> 
</form>
</div>
<div data-role="footer" data-position="fixed" style="text-align:center;height:40px;padding:5px;">
	<s:property value="toolbar" escapeHtml="false"/> 
</div>
</div>
</body>
</html>
<script>
function hideOption(){
	 wx.hideOptionMenu();
}
setTimeout("hideOption()",500);
</script>