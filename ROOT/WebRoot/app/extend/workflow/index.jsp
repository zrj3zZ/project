<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>  
    <title>项目管理</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="iwork_css/message/sysmsgpage.css" rel="stylesheet" type="text/css" />
	<link href="iwork_css/common.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">	
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
    <link href="iwork_css/message/sysmsgpage.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js" charset="gb2312"></script>
    <script type="text/javascript" src="iwork_js/message/sysmsgpage.js"></script>
	<script type="text/javascript" src="iwork_js/message/sysmsglist.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"  charset="utf-8"   ></script>
    <script type="text/javascript">
	$(function(){
		$('#pp').pagination({  
		    total:<s:property value="totalNum"/>,  
		    pageNumber:<s:property value="pageNumber"/>,
		    pageSize:<s:property value="pageSize"/>,
		    onSelectPage:function(pageNumber, pageSize){
		    	submitMsg(pageNumber,pageSize);
		    }
		});
	});
	function submitMsg(pageNumber,pageSize){
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#frmMain").submit();
		return ;
	}
    	function addItem(){
			var formid = 115; 
			var demId = 37; 
			
			var pageurl = 'createFormInstance.action?formid='+formid+'&demId='+demId; 
			 var title = "";
			 var height=580;
			 var width = 900;
			 var dialogId = "WorkflowItem";
			 parent.openWin(title,height,width,pageurl,this.location,dialogId);
			return;    
		} 
    	function editItem(instanceid){
			var pageurl = 'openFormPage.action?formid=115&instanceId='+instanceid+'&demId=37'; 
			 var title = ""; 
			 var height=580;
			 var width = 900;
			 var dialogId = "WorkflowItem";
			 parent.openWin(title,height,width,pageurl,this.location,dialogId);
			return;    
		}
		
		function setDesign(instanceid){
			var pageurl = 'zqb_workflow_showDesigner.action?instanceid='+instanceid; 
				 var title = "";
				 var height=580;
				 var width = 900;
				 var dialogId = "WorkflowDesigner";
				 parent.openWin(title,height,width,pageurl,this.location,dialogId);
			return;   
		}
		function delItem(instanceid){
			 if(confirm("确定执行删除操作吗?")) {
				var pageUrl = "zqb_workflow_remove.action";
				$.post(pageUrl,{instanceid:instanceid},function(data){ 
			       			if(data=='success'){
			       				window.location.reload();
			       			}else{
			       				alert("删除失败");;
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
		.memoTitle{
			font-size:14px;
			padding:5px;
			color:#666;
		}
		.memoTitle a{
			font-size:12px;
			padding:5px;
		}
		.TD_TITLE{
			padding:5px;
			width:200px;
			background-color:#efefef;
			text-align:right;
			
		}
		.TD_DATA{
			padding:5px;
			padding-left:15px;
			padding-right:30px;
			background-color:#fff;
			width:500px;
			text-align:left;
			border-bottom:1px solid #efefef;
		}
		.header td{
			height:30px;
			font-size:12px;
			padding:3px;
			white-space:nowrap;
			padding-left:5px;
			background:url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;
			border-top:1px dotted #ccc;
			border-right:1px solid #eee;
		} 
		.cell:hover{
			background-color:#F0F0F0;
		}
		.cell td{
					margin:0;
					font-size:12px;
					padding:3px 4px;
					white-space:nowrap;
					word-wrap:normal;
					overflow:hidden;
					text-decoration:none;
					text-align:left;
					border-bottom:1px dotted #eee;
					border-top:1px dotted #fff;
					border-right:1px dotted #ccc;
				}
		.selectCheck{
			border:0px;
			text-align:right;
		}
		
	</style>	
  </head> 
    <body class="easyui-layout">
  
    <div region="center" border="false"  align="center" style="border:0px solid #C0C0C0;text-align:left;padding:5px;">
     	<div class="tools_nav">
         	<a href="javascript:addItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加事物指引</a>
			<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
        </div>
<table width="100%" WIDTH="100%" style="border:1px solid #efefef" cellpadding="0" cellspacing="0"> 
      		<tr class="header">
      			<td>事务描述</td>
      			<td>分类</td> 
      		    <td>创建时间</td>
      			<td>状态</td>
      			<td>操作</td>
      		</tr>
      		<s:iterator value="runList">	
      				<tr class="cell">
      				<td ><s:property value="SWMS"/></td>
      				<td ><s:property value="TYPE"/></td>
      				<td ><s:property value="CJSJ"/></td>		
      					<td >
      					<s:if test="STATUS==1">
      						开启
      					</s:if> 
      					<s:else>
      						关闭
      					</s:else>
      					</td>
      					<td>
      						<a href="javascript:setDesign(<s:property value="INSTANCEID"/>)">流程设计</a>&nbsp;&nbsp;
      						<a href="javascript:editItem(<s:property value="INSTANCEID"/>)">属性</a>&nbsp;&nbsp;      						
      						<!-- xlj update 2017年1月11日11:29:58 此方法调用的action与通讯录重复，请重写方法 -->
      						<%-- javascript:delItem('<s:property value="INSTANCEID"/>') --%>
      					</td>
      				</tr> 
      		</s:iterator>
      		</table>

			<form action="zqb_workflow_index.action" method=post name=frmMain id=frmMain >
			<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
			<s:hidden name="pageSize" id="pageSize"></s:hidden>
		</form>	
	 <s:form id="editForm" name="editForm">
  	
   </s:form>
  </div>
 <div region="south" style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;" border="false" >
	<div style = "padding:5px">
			<s:if test="totalNum==0">
			
			</s:if><s:else>
			<div id="pp" style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
			</s:else>
			</div>
	</div>
  </body>
</html>
		
		
		
		
		
		
		
		
		
		
		
		
		