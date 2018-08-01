<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>选择添加字段设置</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
    <script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=default"></script> 
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
		$(function(){
	             var setting = {
	             	check:{
						enable:true
					},
					view: {
						showLine: true, 
						selectedMulti: false
					},
					data: { 
						simpleData: {
							enable: true,
							idKey: "id",
							pIdKey: "pid",
							rootPId: 0
						}
					},  
					async: {
						enable:true,
						url:"ireport_designer_showFieldJson.action",
						dataType:"json",
						otherParam:{'reportId':'<s:property value='reportId' escapeHtml='false'/>','type':'<s:property value="showtype" escapeHtml='false'/>'}
					},
					callback: {
						onClick: onClick,
						onAsyncSuccess: onAsyncSuccess
					}
				};
				
	             function onClick(event, treeId, treeNode){
	             	var treeObj = $.fn.zTree.getZTreeObj(treeId);
	             	if(treeNode.checked){
	             		treeObj.checkNode(treeNode, false, true);
	             	}else{
	             		treeObj.checkNode(treeNode, true, true);
	             	}
	             } 
	             function onAsyncSuccess(event, treeId, treeNode, msg){
	                 var feildsChoosen = new Array();
	                 feildsChoosen = <s:property value='fieldsChoosen' escapeHtml='false'/>; //已经字段变灰色
	                 for(var i=0;i<feildsChoosen.length;i++){
	                     var treeObj = $.fn.zTree.getZTreeObj(treeId);
	                     var nodes=treeObj.getNodesByParam('fieldName',feildsChoosen[i],null);
	                     for(var j=0;j<nodes.length;j++){
	                     	  if(nodes[j].type=='field'){
	                     	  		treeObj.checkNode(nodes[j], true, true);
	                     	  		treeObj.setChkDisabled(nodes[j], true);
	                     	  }
	                     }
	                 }  
	             }
	             $.fn.zTree.init($("#fieldTree"), setting);
		});
		//添加字段
		function add(){
					var treeObj = $.fn.zTree.getZTreeObj("fieldTree");
					var nodes = treeObj.getCheckedNodes(true); 
					var fieldNameArray = new Array();  
					var fieldTitleArray = new Array();
					for(var i=0,j=0;i<nodes.length;i++){	
						if(nodes[i].type=='field'){  
							var fieldName = "";
							if(nodes[i].entityname!=""){
								fieldName = nodes[i].entityname+"."+nodes[i].fieldName;
							}else{
								fieldName = nodes[i].fieldName;
							}
							fieldNameArray[j] = fieldName;
							fieldTitleArray[j] =fieldName;  
							j++;
							//doAddItem(fieldName,nodes[i].fieldTitle);
						}
					}
					if(fieldNameArray.length==0){
					     $.dialog.tips("请选择要添加的字段",1);
					}
					else{
					     doAdd(fieldNameArray.join(),fieldTitleArray.join());
					}
					
		}
		function doAdd(fieldNameStr,fieldTitleStr){
		alert(fieldNameStr+"||||||"+fieldTitleStr);
			var reportId = $("#reportId").val();
			var type = $("#type").val();
			 var url = '';
			if(type==1){
			    url='ireport_designer_condition_quickadd.action';
			}
			else if(type==2){
			    url='ireport_designer_showField_quickadd.action';
			}
			else{
			    url='ireport_designer_orderField_quickadd.action';
			}
		    $.post(url,{reportId:reportId,fieldName:fieldNameStr,fieldTitle:fieldTitleStr},function(data){
		         if(data=='success'){
		             location.reload();		             
		         }else{
		             $.dialog.tips("保存失败",1);
		         }
		    });
		}   
   </script>
  </head>
  
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
	 	<s:hidden name="reportId" id="reportId"></s:hidden>
	 	<s:hidden name="reportType" id="reportType"></s:hidden>
	 	<s:hidden name="fieldsChoosen" id="fieldsChoosen"></s:hidden>
	 	<s:hidden name="type" id="type"></s:hidden>
  </body>
</html>
