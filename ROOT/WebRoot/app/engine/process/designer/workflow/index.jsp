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
    <script type="text/javascript">
   		
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
			var pageurl = 'process_workflow_showDesigner.action?instanceid='+instanceid; 
				 var title = "";
				 var height=580;
				 var width = 900;
				 var dialogId = "WorkflowDesigner";
				 parent.openWin(title,height,width,pageurl,this.location,dialogId);
			return;   
		}
		function delItem(instanceid){
			 if(confirm("确定执行删除操作吗?")) {
				var pageUrl = "process_workflow_remove.action";
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
		.groupTitle{
			font-family:黑体;
			font-size:12px;
			text-align:left;
			color:#666;
			height-line:20px;
			padding:5px;
			padding-left:15px;
		}
		.itemList{  
			font-family:宋体;
			font-size:12px;
			height:200px;
			padding-left:15px;
		}
		.itemList td{
			list-style:none;
			height:20px;
			padding:2px;
			padding-left:20px;
			
		}
		.itemList tr:hover{
			color:#0000ff;
			cursor:pointer;
		}
		.itemList  td{
			font-size:12px;
		}
		
		.itemicon{
			padding-left:25px;
			background:transparent url(iwork_img/pin.png) no-repeat scroll 0px 3px;
		}
		.selectBar{
			border:1px solid #efefef;
			margin:5px;
			background:#FDFDFD;
		}
		.selectBar td{
			vertical-align:middle;
			height:20px;
		}
		
		.selectBar td linkbtn{
			color:#0000FF;
			text-decoration: none;
		}
	</style>	
  </head> 
    <body class="easyui-layout">
  
      <div region="north" border="false" >
      	<div class="tools_nav">
         	<a href="javascript:addItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加事物指引</a>
			<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
        </div>
        
      </div>
      <div region="center"  border="false" >
      	<div  class="itemList" id="itemList">
      		<table width="100%"> 
      		<s:iterator value="runList">
      				<tr>
      					<td class="itemicon" ><s:property value="LCSWBH"/> </td>
      					<td ><s:property value="TYPE"/></td>
      					<td ><s:property value="SWMS"/></td>
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
      						<a href="javascript:delItem('<s:property value="INSTANCEID"/>')">删除</a>&nbsp;&nbsp;
      					</td>
      				</tr> 
      		</s:iterator>
      		</table>
      	</div>
	 <s:form id="editForm" name="editForm">
  	
   </s:form>
  </div>
 
  </body>
</html>
		
		
		
		
		
		
		
		
		
		
		
		
		