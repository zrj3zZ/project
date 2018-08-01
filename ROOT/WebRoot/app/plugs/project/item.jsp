<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
   "http://www.w3.org/TR/html4/strict.dtd">
    <%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html lang="zh">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>甘特图</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="iwork_js/plugs/gantt2/jquery-ui-1.8.4.css" />
	<link rel="stylesheet" type="text/css" href="iwork_js/plugs/gantt2/reset.css" />
	<link rel="stylesheet" type="text/css" href="iwork_js/plugs/gantt2/jquery.ganttView.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.contextmenu.r2.js"></script>
	<script type="text/javascript" src="iwork_js/plugs/gantt2/date.js"></script>
	<script type="text/javascript" src="iwork_js/plugs/gantt2/jquery-ui-1.8.4.js"></script>
	<script type="text/javascript" src="iwork_js/plugs/gantt2/jquery.ganttView.js"></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=default"></script> 
	<script type="text/javascript">
	function removeTask(projectNo,id,instanceId){
		if(confirm("确认删除当前任务吗？")){
    		removeItem(projectNo,id,instanceId);
    		this.location.reload();
    	}
	}
	function updateTaskItem(projectName,projectNo,taskid,startdate,enddate,instanceid){
		var pageUrl = "zqb_project_update_item.action"; 
		$.post(pageUrl,{instanceid:instanceid,projectName:projectName,projectNo:projectNo,taskid:taskid,startDate:startdate,endDate:enddate},function(data){
			var alertMsg = "";
   			if(data=='success'){
   				alertMsg =  "变更成功";
   			}else{
   				alertMsg = "变更异常，请稍后再试";
   			}
   			$.dialog.tips(alertMsg,2,'success.gif');
   			
 		});
	}
	
	/* function removeItem(projectNo,taskid,instanceid){
	var pageUrl = "zqb_project_remove_item.action"; 
		$.post(pageUrl,{instanceid:instanceid,projectNo:projectNo,taskid:taskid},function(data){
			var alertMsg = "";
   			if(data=='success'){
   				alertMsg =  "删除成功";
   			}
   			$.dialog.tips(alertMsg,2,'success.gif');
   			
 		});
	
	} */
	function removeItem(projectNo,taskid){
		var pageUrl = "zqb_project_remove_item.action"; 
			$.post(pageUrl,{projectNo:projectNo,taskid:taskid},function(data){
				var alertMsg = "";
       			if(data=='success'){
       				alertMsg =  "删除成功";
       			}
       			$.dialog.tips(alertMsg,2,'success.gif');
       			
     		});
		
		}
	
	function loadItem(instanceId){
		var projectNo = $("#PROJECTNO").val();
        		var projectName = $("#projectName").val();
//         		var readwrite=$("#readwrite").val();
        		var url="";
//         		if(readwrite=="false"){
//         			url=encodeURI("url:openFormPage.action?formid=94&instanceId="+instanceId+"&demId=24&PROJECTNO="+projectNo+"&PROJECTNAME="+projectName);
//         		}else{
        			url=encodeURI("url:loadVisitPage.action?formid=94&demId=24&PROJECTNO="+projectNo+"&PROJECTNAME="+projectName);
//         		}
		$.dialog({
					id:'projectTask',
					cover:true,
					title:'项目任务', 
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					rang:true,
					width:600,
					cache:false,
					lock: true,
					height:500, 
					iconTitle:false,
					extendDrag:true,
					autoSize:false,
					content:url,
					close:function(){
						window.location.reload();
					}
				});
		}
	 	function addGroup(formid,demId){
    		var projectNo = $("#projectNo").val();
    		var projectName = $("#projectName").val();
			var pageUrl = encodeURI("url:createFormInstance.action?formid="+formid+"&demId="+demId+"&PROJECTNO="+projectNo+"&PROJECTNAME="+projectName); 
			$.dialog({
				id:'projectGroup',
				cover:true,
				title:'项目阶段', 
				loadingText:'正在加载中,请稍后...',
				bgcolor:'#999',
				rang:true,
				width:450,
				cache:false,
				lock: true,
				height:300, 
				iconTitle:false,
				extendDrag:true,
				autoSize:false,
				content:pageUrl,
				close:function(){
					window.location.reload();
				}
				
			});
    	}
    	function addQuestion(taskNo){ 
    		var projectNo = $("#projectNo").val();
    		var projectName = $("#projectName").val();
			var pageUrl = encodeURI("url:createFormInstance.action?formid=97&demId=27&XMBH="+projectNo+"&XMMC="+projectName+"&TASKNO="+taskNo);  
			$.dialog({
				id:'projectGroup',
				cover:true,
				title:'任务反馈', 
				loadingText:'正在加载中,请稍后...',
				bgcolor:'#999',
				rang:true,
				width:800,
				cache:false,
				lock: true,
				height:450, 
				iconTitle:false,
				extendDrag:true,
				autoSize:false,
				content:pageUrl,
				close:function(){
					window.location.reload();
				}
			});
    	}
    	function addTask(xmlcServer,formid,demId){
    		var projectNo = $("#projectNo").val();
    		var projectName = $("#projectName").val();
			var pageUrl = encodeURI("url:processRuntimeStartInstance.action?actDefId="+xmlcServer+"&PROJECTNO="+projectNo+"&PROJECTNAME="+projectName);  
			$.dialog({
				id:'projectTask',
				cover:true,
				title:'项目任务', 
				loadingText:'正在加载中,请稍后...',
				bgcolor:'#999',
				rang:true,
				width:600,
				cache:false,
				lock: true,
				height:500, 
				iconTitle:false,
				extendDrag:true,
				autoSize:false,
				content:pageUrl,
				close:function(){
					window.location.reload();
				}
			});
    	}
    	function showQuestion(title,projectNo,taskid){
    	var pageUrl = "url:zqb_project_question.action?projectNo="+projectNo+"&taskid="+taskid;  
			$.dialog({
				id:'projectTask',
				cover:true,
				title:"[问题反馈]"+title, 
				loadingText:'正在加载中,请稍后...',
				bgcolor:'#999',
				rang:true,
				width:650,
				cache:false,
				lock: true,
				height:650, 
				iconTitle:false,
				extendDrag:true,
				autoSize:false,
				content:pageUrl
			});
    	}
    	
    	function showQuestion1(title,projectNo,taskid){
        	var pageUrl = "url:zqb_project_showquestion.action?projectNo="+projectNo+"&taskid="+taskid;  
    			$.dialog({
    				id:'projectTask',
    				cover:true,
    				title:"", 
    				loadingText:'正在加载中,请稍后...',
    				bgcolor:'#999',
    				rang:true,
    				width:850,
    				cache:false,
    				lock: true,
    				height:500, 
    				iconTitle:false,
    				extendDrag:true,
    				autoSize:false,
    				content:pageUrl
    			});
        	}
    	
    	function setTaskFinish(){
    		if(confirm("您确定将当前项目转入【持续督导】状态吗？转入持续督导状态后，将由持续督导人员进行后期维护。")){
    			var projectNo = $("#projectNo").val();
    			var instanceid = $("#instanceid").val();
    			var pageUrl = "zqb_project_finish_item.action"; 
				$.post(pageUrl,{instanceid:instanceid,projectNo:projectNo},function(data){
					var alertMsg = "";
	       			if(data=='success'){
	       				alertMsg =  "转入成功";
	       			}
	       			$.dialog.tips(alertMsg,2,'success.gif');
	       			
	     		});
			
    		}
    	}
	</script>
	<style type="text/css">
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
	td{
		text-align:center; /*设置水平居中*/
    	vertical-align:middle;/*设置垂直居中*/
    	border: #efefef 1px solid;
	}
		body {
			font-family: tahoma, verdana, helvetica;
			font-size: 0.8em;
			padding: 0px;
		}
		.dropDown2 {
	z-index: 6;
	position: absolute;
	text-align: left;
	vertical-align: top;
}
.dropDown2 .dropMenu {
	margin-left: 18px;
}
.dropDown .dropMenu, .dropDown2 .dropMenu {
	min-width: 72px;
	margin-top: -1px;
	border-top-left-radius: 0px;
}
.dropDown2 .dropMenu {
	position: relative;
}
.sel{
	border:1px solid #999; 
	padding:5px;
	color:#666; 
	
}
.sel:hover{
	background-color:#efefef;
}
	</style>

</head>
<body>
<div class="tools_nav"> 
<s:if test="readwrite!=true&&readonly!=true">
<!-- 
         	<a href="javascript:addGroup(93,23);" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加项目阶段</a> -->
         	<a href="javascript:addTask('<s:property value='xmlcServer'/>',94,24);" class="sel" >添加任务</a>
         	<a href="javascript:setTaskFinish();" class="sel" >转入持续督导阶段</a>
</s:if>

			<a href="javascript:this.location.reload();"  class="sel" >刷新</a>
        </div>
      <div style="padding:5px;">
		<div id="ganttChart" ></div>
	</div>
	<table text-align:center;>
	<tr  class="header" height="30px" style="border-bottom: #efefef 1px solid; border-left: #efefef 1px solid; border-top: #efefef 1px solid; border-right: #efefef 1px solid;">
                  <td width="15%"; style="text-align: center;">任务名称</td>
                  <td width="15%"; style="text-align: center;">所属阶段</td>
                  <td width="15%"; style="text-align: center;">任务开始日期</td>
                  <td width="15%"; style="text-align: center;">预计完成日期</td>
                  <td width="15%"; style="text-align: center;">负责人</td>
                  <td width="15%"; style="text-align: center;">操作</td>
                </tr>
	<s:iterator value="taskList">
	<tr height="30px" style="border-bottom: #efefef 1px solid; border-left: #efefef 1px solid; border-top: #efefef 1px solid; border-right: #efefef 1px solid;">
	<td><a href="javascript:loadItem('<s:property value="INSTANCEID"/>')"><s:property value="TASK_NAME"/></a></td>
	<td><s:property value="GROUPNAME"/></td>
	<td><s:property value="STARTDATE"/></td>
	<td><s:property value="ENDDATE"/></td>
	<td><s:property value="MANAGER"/></td>
	<td>
	<a href="javascript:addQuestion('<s:property value="ID"/>')">提问</a>
	<a href="javascript:showQuestion1('<s:property value="TASK_NAME"/>','<s:property value="PROJECTNO"/>','<s:property value="ID"/>')">回复</a>
	<s:if test="readwrite!=true">
	<a href="javascript:loadItem('<s:property value="INSTANCEID"/>')">编辑</a>
	<a href="javascript:removeTask('<s:property value="PROJECTNO"/>','<s:property value="ID"/>','<s:property value="INSTANCEID"/>')">删除</a>
	</s:if>
	</td>
	</tr>
	</s:iterator>
	<tr>
	<td></td>
	</tr>
	</table>
	<br/><br/>
	<div id="eventMessage"></div>
	 <s:hidden name="projectNo"></s:hidden>
	 <s:hidden name="instanceid"></s:hidden>
    <input type="hidden" name="projectName" value="<s:property value="hash.PROJECTNAME"/>" id="projectName"/>
    <input type="hidden" name="readwrite" value="<s:property value="readwrite"/>" id="readwrite"/>
	
	<%-- <script type="text/javascript">
		$(function () {
			debugger;
			var selectedObj = null;
			var ganttData = <s:property value="json" escapeHtml="false"/>;
			$("#ganttChart").ganttView({ 
				showWeekends: true,
				data: ganttData,
				slideWidth: 640,
				readonly:<s:property value="readonly"/>,
				excludeWeekends: true,
        		showDayOfWeek: true,
				behavior: {
					onClick: function (o) { 
					},
					onDblClick: function (data) { 
						loadItem(data.instanceid);
					},
					onResize: function (data) { 
						if(confirm("确认调整时间区间吗?")){
							updateTaskItem($("#projectName").val(),$("#projectNo").val(),data.id,data.start.toString("yyyy-M-d"),data.end.toString("yyyy-M-d"),data.instanceid);
						}else{ 
							this.location.reload();
						}
					},
					onDrag: function (data) { 
						if(confirm("确认移动当前任务的开始和结束时间吗")){
							updateTaskItem($("#projectName").val(),$("#projectNo").val(),data.id,data.start.toString("yyyy-M-d"),data.end.toString("yyyy-M-d"),data.instanceid);
						}else{
							this.location.reload();
						}
					},onContextMenu:function (data) { 
						$(this).contextMenu('myMenu', 
					     {
					      //菜单样式
					      menuStyle: {
					        border: '4px solid #efefef'
					      },
					      //菜单项样式
					      itemStyle: {
					        fontFamily : 'verdana',
					        backgroundColor : '#efefef',
					        color: '#666',
					        border: '1px',
					        padding: '5px'
				
					      },
					      //菜单项鼠标放在上面样式
					      itemHoverStyle: {
					        color: 'blue',
					        backgroundColor:'#fff',
					        border: 'none'
					      },
						          bindings: 
						          {
						            'edit': function(t) {
						         		     addQuestion(data.id);
						            },
						            'look': function(t) {
						            	loadItem(data.instanceid);
						            },
						            'quit': function(t) {
						            	if(confirm("确认删除当前任务吗？")){
						            		removeItem($("#projectNo").val(),data.id,data.instanceid);
						            		this.location.reload();
						            	}
						            }
						          }
						    });
					}
				}
			});
			
			// $("#ganttChart").ganttView("setSlideWidth", 600);
		});
		
		function updateTaskItem(projectName,projectNo,taskid,startdate,enddate,instanceid){
			var pageUrl = "zqb_project_update_item.action"; 
			$.post(pageUrl,{instanceid:instanceid,projectName:projectName,projectNo:projectNo,taskid:taskid,startDate:startdate,endDate:enddate},function(data){
				var alertMsg = "";
       			if(data=='success'){
       				alertMsg =  "变更成功";
       			}else{
       				alertMsg = "变更异常，请稍后再试";
       			}
       			$.dialog.tips(alertMsg,2,'success.gif');
       			
     		});
		}
		
		/* function removeItem(projectNo,taskid,instanceid){
		var pageUrl = "zqb_project_remove_item.action"; 
			$.post(pageUrl,{instanceid:instanceid,projectNo:projectNo,taskid:taskid},function(data){
				var alertMsg = "";
       			if(data=='success'){
       				alertMsg =  "删除成功";
       			}
       			$.dialog.tips(alertMsg,2,'success.gif');
       			
     		});
		
		} */
		function removeItem(projectNo,taskid){
			var pageUrl = "zqb_project_remove_item.action"; 
				$.post(pageUrl,{projectNo:projectNo,taskid:taskid},function(data){
					var alertMsg = "";
	       			if(data=='success'){
	       				alertMsg =  "删除成功";
	       			}
	       			$.dialog.tips(alertMsg,2,'success.gif');
	       			
	     		});
			
			}
		
		function loadItem(instanceId){
			var projectNo = $("#projectNo").val();
	        		var projectName = $("#projectName").val();
	        		var readwrite=$("#readwrite").val();
	        		var url="";
	        		if(readwrite=="false"){
	        			url=encodeURI("url:openFormPage.action?formid=94&instanceId="+instanceId+"&demId=24&PROJECTNO="+projectNo+"&PROJECTNAME="+projectName);
	        		}else{
	        			url=encodeURI("url:loadVisitPage.action?formid=94&demId=22&instanceId="+instanceId+"&PROJECTNO="+projectNo+"&PROJECTNAME="+projectName);
	        		}
			$.dialog({
						id:'projectTask',
						cover:true,
						title:'项目任务', 
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true,
						width:600,
						cache:false,
						lock: true,
						height:500, 
						iconTitle:false,
						extendDrag:true,
						autoSize:false,
						content:url,
						close:function(){
							window.location.reload();
						}
					});
			}
		 	function addGroup(formid,demId){
        		var projectNo = $("#projectNo").val();
        		var projectName = $("#projectName").val();
				var pageUrl = encodeURI("url:createFormInstance.action?formid="+formid+"&demId="+demId+"&PROJECTNO="+projectNo+"&PROJECTNAME="+projectName); 
				$.dialog({
					id:'projectGroup',
					cover:true,
					title:'项目阶段', 
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					rang:true,
					width:450,
					cache:false,
					lock: true,
					height:300, 
					iconTitle:false,
					extendDrag:true,
					autoSize:false,
					content:pageUrl,
					close:function(){
						window.location.reload();
					}
					
				});
        	}
        	function addQuestion(taskNo){ 
        		var projectNo = $("#projectNo").val();
        		var projectName = $("#projectName").val();
				var pageUrl = encodeURI("url:createFormInstance.action?formid=97&demId=27&XMBH="+projectNo+"&XMMC="+projectName+"&TASKNO="+taskNo);  
				$.dialog({
					id:'projectGroup',
					cover:true,
					title:'任务反馈', 
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					rang:true,
					width:450,
					cache:false,
					lock: true,
					height:300, 
					iconTitle:false,
					extendDrag:true,
					autoSize:false,
					content:pageUrl,
					close:function(){
						window.location.reload();
					}
				});
        	}
        	function addTask(formid,demId){
        		var projectNo = $("#projectNo").val();
        		var projectName = $("#projectName").val();
				var pageUrl = encodeURI("url:processRuntimeStartInstance.action?actDefId=XMRWLC:1:73504&PROJECTNO="+projectNo+"&PROJECTNAME="+projectName);  
				$.dialog({
					id:'projectTask',
					cover:true,
					title:'项目任务', 
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					rang:true,
					width:600,
					cache:false,
					lock: true,
					height:500, 
					iconTitle:false,
					extendDrag:true,
					autoSize:false,
					content:pageUrl,
					close:function(){
						window.location.reload();
					}
				});
        	}
        	function showQuestion(title,projectNo,taskid){
        	var pageUrl = "url:zqb_project_question.action?projectNo="+projectNo+"&taskid="+taskid;  
				$.dialog({
					id:'projectTask',
					cover:true,
					title:"[问题反馈]"+title, 
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					rang:true,
					width:650,
					cache:false,
					lock: true,
					height:450, 
					iconTitle:false,
					extendDrag:true,
					autoSize:false,
					content:pageUrl
				});
        	}
        	function setTaskFinish(){
        		if(confirm("您确定将当前项目转入【持续督导】状态吗？转入持续督导状态后，将由持续督导人员进行后期维护。")){
        			var projectNo = $("#projectNo").val();
        			var instanceid = $("#instanceid").val();
        			var pageUrl = "zqb_project_finish_item.action"; 
					$.post(pageUrl,{instanceid:instanceid,projectNo:projectNo},function(data){
						var alertMsg = "";
		       			if(data=='success'){
		       				alertMsg =  "转入成功";
		       			}
		       			$.dialog.tips(alertMsg,2,'success.gif');
		       			
		     		});
				
        		}
        	}
	</script>
	<!--右键菜单的源-->  
     <div class="contextMenu" id="myMenu">
		  <ul>
		    <li id="edit"><img src="iwork_img/comment_add.png" />问题反馈</li>
		    <li id="look"><img src="iwork_img/note_edit.png" />编辑</li>
		    <s:if test="readwrite!=true">
		    <li id="quit"><img src="iwork_img/delete.png" />删除</li>
		    </s:if>
		  </ul>
		</div> --%>
</body>
</html>
<!-- 新增查询过滤SQL注入关键字 -->
<script language="JavaScript"> 
  jQuery.validator.addMethod("string", function(value, element) {
          var sqlstr=[" and "," exec ", " count ", " chr ", " mid ", " master ", " or ", " truncate ", " char ", " declare ", " join ","insert ", "select ", "delete ", "update ","create ","drop "]
          var patrn=/[`~!#$%^&*+<>?"{},;'[\]]/im;
    	    if(patrn.test(value)){
        	}else{
            	var flag = false;
            	var tmp = value.toLowerCase();
            	for(var i=0;i<sqlstr.length;i++){
                	var str = sqlstr[i];
					if(tmp.indexOf(str)>-1){
						flag = true;
						break;
					}
                }
                if(!flag){
                	return "success";
                }
            }
        }, "包含非法字符!");
</script>