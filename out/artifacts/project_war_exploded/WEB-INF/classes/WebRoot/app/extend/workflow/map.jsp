<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>  
    <title>项目管理</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
   
    <script type="text/javascript" src="iwork_js/commons.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
   
    <link rel="stylesheet" type="text/css" href="iwork_plugs/gooflow/GooFlow2.css"/>
	<script type="text/javascript" src="iwork_plugs/gooflow/jquery.min.js"></script>
	<script type="text/javascript" src="iwork_plugs/gooflow/GooFunc.js"></script>
	<script type="text/javascript" src=".iwork_plugs/gooflow/json2.js"></script>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/gooflow/default.css"/>
	<script type="text/javascript" src="iwork_plugs/gooflow/GooFlow.js"></script>
    <script type="text/javascript">
    
		var demo;
		$(document).ready(function(){
			setTimeout("buildflow()",500);
		});
		
		function buildflow(){
			var width1 = document.body.clientWidth-100;
		var height1 = document.body.clientHeight-100;
			var property={
				width:width1, 
				height:height1,
				haveHead:true,
				initLabelText:"",
				toolBtns:[],
				haveHead:false,
				headBtns:["save","undo","redo","reload"],//如果haveHead=true，则定义HEAD区的按钮
				haveTool:true,
				haveGroup:true,
				useOperStack:true,
			}; 
			var remark={
				cursor:"选择指针",
				direct:"转换连线",
				start:"开始结点",
				"end round":"结束结点",
				"task round":"任务结点",
				node:"自动结点",
				chat:"决策结点",
				state:"状态结点",
				plug:"附加插件",
				fork:"分支结点",
				"join":"联合结点",
				"complex mix":"复合结点",
				group:"组织划分框编辑开关"
			}; 
			demo=$.createGooFlow($("#demo"),
				property
			);
			demo.setNodeRemarks(remark);
			//demo.onItemDel=function(id,type){
			//	return confirm("确定要删除该单元吗?");
			//}
			jsondata=<s:property value="json" escapeHtml="false"/>;
			demo.loadData(jsondata);
			demo.onBtnSaveClick=function(){
				var expData = JSON.stringify(demo.exportData());
				var instanceid = $("#instanceid").val();
				var pageUrl = "zqb_workflow_saveDesigner.action";
				$.post(pageUrl,{instanceid:instanceid,json:expData},function(data){ 
			       			if(data=='success'){
			       				alert('保存成功');
			       			}else{
			       				alert("关闭项目失败");;
			       			} 
			     }); 
			}
		}
		
</script>
    <style type="text/css">
		 body {
		margin-left: 0px;
		margin-top: 0px;
		margin-right: 0px;
		margin-bottom: 0px;
	}
	</style>	
  </head> 
    <body class="easyui-layout">
      <div region="center"  border="false" >
      	<div id="demo"></div>
	 <s:form id="editForm" name="editForm">
  	<s:hidden id="instanceid" name="instanceid"></s:hidden>
   </s:form>
  </div>
 
  </body>
</html>

		
		
		
		
		
		
		
		
		
		
		
		