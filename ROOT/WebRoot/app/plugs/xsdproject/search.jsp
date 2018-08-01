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
			var formid = 91;
			var demId = 22;
			var url = 'createFormInstance.action?formid='+formid+'&demId='+demId; 
			var target = "_blank";
			var win_width = window.screen.width;
			var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location = url;
			return;   
		}
		function showInfo(id,projectName){
			 var title = "";
			 var height=580;
			 var width = 900;
			 var pageurl = "loadVisitPage.action?formid=91&demId=22&instanceId="+id+"&PROJECTNAME="+encodeURI(projectName);
			 var dialogId = "projectItem";
			 parent.openWin(title,height,width,pageurl,this.location,dialogId);
		}
		
		function showItem(id){
			 var title = "";
			 var height=580;
			 var width = 900;
			 var pageurl = "xsd_zqb_project_item.action?projectNo="+id;
			 var dialogId = "projectItem";
			 parent.openWin(title,height,width,pageurl,this.location,dialogId);
		}
		
		function closeItem(instanceid,projectNo){
			if(confirm('确定关闭当前项目吗？')){
				var pageUrl = "xsd_zqb_project_closeitem.action?instanceid="+instanceid+"&projectNo="+projectNo; 
				$.post(pageUrl,{},function(data){
	       			if(data=='success'){
	       				this.location.reload();
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
		.groupTitle{
			font-family:黑体;
			font-size:12px;
			text-align:left;
			color:#666;
			height-line:20px;
			padding:5px;
			padding-left:15px;
			border-bottom:1px solid #efefef;
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
			background:transparent url(iwork_img/application_view_list.gif) no-repeat scroll 0px 3px;
		}
			
	</style>	
  </head>
    <body class="easyui-layout">
      <div region="north" border="false" >
      	<div class="tools_nav">
         	<!-- <a href="javascript:addItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加项目</a> -->
			<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
        </div>
      </div>
      <div region="center"  border="false" >
      	<div class="groupTitle">项目列表</div>
      	<div  class="itemList">
      		<table width="100%"> 
      		<tr bgcolor="#E8E8E8"><td>项目名称</td><td>客户联系人</td><td>项目负责人</td><td>开始时间</td><td>结束时间</td><td>详细情况</td></tr>
      		<s:iterator value="runList">
      				<tr>
      					<td class="itemicon" onclick="showInfo('<s:property value="INSTANCEID"/>','<s:property value="PROJECTNAME"/>')"><s:property value="PROJECTNAME"/> </td>
      					<td  onclick="showInfo('<s:property value="INSTANCEID"/>','<s:property value="PROJECTNAME"/>')"><s:property value="KHLXR"/></td>
      					<td  onclick="showInfo('<s:property value="INSTANCEID"/>','<s:property value="PROJECTNAME"/>')"><s:property value="OWNER"/></td>
      					<td  onclick="showInfo('<s:property value="INSTANCEID"/>','<s:property value="PROJECTNAME"/>')"><s:property value="STARTDATE"/></td>
      					<td  onclick="showInfo('<s:property value="INSTANCEID"/>','<s:property value="PROJECTNAME"/>')"><s:property value="ENDDATE"/></td>
      					<td><a href="javascript:showInfo('<s:property value="INSTANCEID"/>','<s:property value="PROJECTNAME"/>')">详情</a>&nbsp;&nbsp;
      					<!-- <a href="javascript:showItem('<s:property value="PROJECTNO"/>')">进度</a>&nbsp;&nbsp; -->
      					<s:if test="ISPURVIEW==true">
      						<a href="javascript:closeItem(<s:property value="INSTANCEID"/>,'<s:property value="PROJECTNO"/>')">关闭</a>
      					</s:if>
      					</td>
      				</tr>
      		</s:iterator>
      		</table>
      	</div>
	</div>
  </body>
</html>