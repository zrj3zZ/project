<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="iwork" uri="/WEB-INF/sys-commonsTags.tld" %>
<html>
  <head>
    <title>选择添加字段设置</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>

    <style type="text/css">  
    #rightbox {
		font-weight:bold; font-family:微软雅黑; font-size:12px; text-align:left;
		width:250px;
		height:auto;    	
		margin-right:20px;
		overflow:hidden;
    }
    </style>
   <script type="text/javascript">
		var api = art.dialog.open.api, W = api.opener;
		$(function(){
			var setting = {
					check: {
						enable: true
					},
					async: {
						enable: true, 
						url:"process_step_summary_showfieldJSON.action?prcDefId=<s:property value="prcDefId"/>&actDefId=<s:property value="actDefId"/>&actStepDefId=<s:property value="actStepDefId"/>",
						dataType:"json"
					},
					data: {
						simpleData: {
							enable: true
						}
					},
					callback: {
						onClick: onClick,
						onAsyncSuccess: onAsyncSuccess
					}
				};
				$.fn.zTree.init($("#fieldTree"), setting);
				
				//添加字段
 			function onAsyncSuccess(event, treeId, treeNode, msg){
 			
	                 var feildsChoosen = new Array();
	                  feildsChoosen = <s:property value='fieldsChoosen' escapeHtml='false'/>;
	                 for(var i=0;i<feildsChoosen.length;i++){
	                     var treeObj = $.fn.zTree.getZTreeObj(treeId);
	                     var nodes=treeObj.getNodesByParam('fieldname',feildsChoosen[i],null);
	                     for(var j=0;j<nodes.length;j++){
	                     	  if(nodes[j].type=='field'){
	                     	  		treeObj.checkNode(nodes[j], true, true);
	                     	  		treeObj.setChkDisabled(nodes[j], true);
	                     	  }
	                     }
	                 } 
	             }
		});
		function onClick(event, treeId, treeNode){
	             	var treeObj = $.fn.zTree.getZTreeObj(treeId);
	             	if(treeNode.checked){
	             		treeObj.checkNode(treeNode, false, true);
	             	}else{
	             		treeObj.checkNode(treeNode, true, true);
	             	}
	             } 
	
		
		function add(){
					var treeObj = $.fn.zTree.getZTreeObj("fieldTree");
					var nodes = treeObj.getCheckedNodes(true); 
					if(nodes.length<1){
						art.dialog.tips("请选择您要显示的字段");
						return;
					}
					
					var datas=[]; 
					for(var i=0;i<nodes.length;i++){	
						if(nodes[i].type=="group"){continue;}				    				   
						if(nodes[i].chkDisabled==true){continue;}				    				   
						var data = {};
						data["formid"]=nodes[i].formid;
						data["metadataid"]=nodes[i].metadataid;
						data["fieldName"]=nodes[i].fieldname;
						data["fieldTitle"]=nodes[i].name;
						datas.push(data);
					} 
					var jsonString = JSON.stringify(datas);
					
					$("#fieldNames").val(jsonString);
						var options = {
							error:function(){
					           art.dialog.tips("保存失败！");
					        },
							success:function(responseText, statusText, xhr, $form){
					           if(responseText=="success"){
					               art.dialog.tips("保存成功！");
					               api.close();
					           }
					           else if(responseText=="error"){
					              art.dialog.tips("保存失败！");
					           }else{
					          	 //art.dialog.tips(responseText);
					           } 
					        } 
				        }						
				  	$('#editForm').ajaxSubmit(options); 
					
		}
   </script>
  </head>
  <s:property value=""/>
  <body>
  <div style="border-bottom:1px solid #efefef;margin-bottom:3px;padding-bottom:8px;text-align:left;padding-right:20px;backgroud-color:#efefef">
					<div style="float:left;width:200px;">
						<a href="javascript:add();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加</a>
						<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
						</div>
						<br>
					</div>
					<div style="text-align:left;padding:5px;color='#808080';font-size:10px;">
	        				请选择您要添加的条件字段
	        		</div>
	            	<div style="padding-left:5px;margin-left:30px; width:400px;  overflow-y:auto; border:1px #C0C0C0 solid;">
	            			<ul id="fieldTree"  class="ztree"> 
						   </ul> 
	                </div>
	      <form id="editForm" name="editForm" action="process_step_summary_save.action" method="post">	 
	 		<s:hidden name="actDefId"></s:hidden>
	 		<s:hidden name="actStepDefId"></s:hidden> 
	 		<s:hidden name="prcDefId"></s:hidden>
	 		<input type="hidden" name="fieldNames"  id="fieldNames"/> 
	 	</form>




  </body>
</html>



