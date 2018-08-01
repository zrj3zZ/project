<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
  <meta content="text/html; charset=UTF-8" http-equiv="content-type">
		  <title>流程设计器-<s:property value="processName"/></title>
	<!-- JQuery EasyUi CSS-->
	<link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/easyui/themes/gray/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
	<!-- JQuery validate CSS-->
	<link href="iwork_js/jqueryjs/validate/jquery.validate.extend.css" type="text/css" rel="stylesheet"/>
	<!-- JQuery AutoComplete -->
	<link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/autocomplete/jquery.autocomplete.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script> 
	<script src="iwork_js/jqueryjs/validate/jquery.validate.method.js" type="text/javascript"></script>
	<script src="iwork_js/jqueryjs/validate/jquery.validate.extend.js" type="text/javascript"></script>
<!-- JQuery form Plugin -->
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<!-- JSON JS-->
	<script src="iwork_js/designer/json2.js" type="text/javascript"></script>
<!-- JQuery AutoComplete -->
	<script type='text/javascript' src='iwork_js/jqueryjs/autocomplete/lib/jquery.bgiframe.min.js'></script>
	<script type='text/javascript' src='iwork_js/jqueryjs/autocomplete/lib/jquery.ajaxQueue.js'></script>
	<script type='text/javascript' src='iwork_js/jqueryjs/autocomplete/jquery.autocomplete.min.js'></script>
<!-- framework JS -->
	<script src="iwork_js/designer/skin.js" type="text/javascript"></script>
	<link href="iwork_js/designer/designer.css" type="text/css" rel="stylesheet"/>
        <!-- common, all times required, imports -->
        <SCRIPT src='iwork_js/designer/draw2d/wz_jsgraphics.js'></SCRIPT>          
        <SCRIPT src='iwork_js/designer/draw2d/mootools.js'></SCRIPT>          
        <SCRIPT src='iwork_js/designer/draw2d/moocanvas.js'></SCRIPT>                        
        <SCRIPT src='iwork_js/designer/draw2d/draw2d.js'></SCRIPT>
        <!-- example specific imports -->
        <SCRIPT src="iwork_js/designer/MyCanvas.js"></SCRIPT>
        <SCRIPT src="iwork_js/designer/ResizeImage.js"></SCRIPT>
		<SCRIPT src="iwork_js/designer/event/Start.js"></SCRIPT>
		<SCRIPT src="iwork_js/designer/event/End.js"></SCRIPT>
		<SCRIPT src="iwork_js/designer/connection/MyInputPort.js"></SCRIPT>
		<SCRIPT src="iwork_js/designer/connection/MyOutputPort.js"></SCRIPT>
		<SCRIPT src="iwork_js/designer/connection/DecoratedConnection.js"></SCRIPT>
		<SCRIPT src="iwork_js/designer/task/Task.js"></SCRIPT>
		<SCRIPT src="iwork_js/designer/task/UserTask.js"></SCRIPT>
		<SCRIPT src="iwork_js/designer/task/ManualTask.js"></SCRIPT>
		<SCRIPT src="iwork_js/designer/task/ServiceTask.js"></SCRIPT>
		<SCRIPT src="iwork_js/designer/task/ScriptTask.js"></SCRIPT>
		<SCRIPT src="iwork_js/designer/task/MailTask.js"></SCRIPT>
		<SCRIPT src="iwork_js/designer/task/ReceiveTask.js"></SCRIPT>
		<SCRIPT src="iwork_js/designer/task/BusinessRuleTask.js"></SCRIPT>
		<SCRIPT src="iwork_js/designer/task/CallActivity.js"></SCRIPT>
		<SCRIPT src="iwork_js/designer/gateway/ExclusiveGateway.js"></SCRIPT>
		<SCRIPT src="iwork_js/designer/gateway/ParallelGateway.js"></SCRIPT>
		<SCRIPT src="iwork_js/designer/designer.js"></SCRIPT>	
		<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
</head>

<script type="text/javascript">
<!--
var workflow;
var processDefinitionId='<s:property value="actProcId"/>'; 
var processDefinitionName='<s:property value="processName"/>';
var proDefId='<s:property value="proDefId"/>';
var processDefinitionVariables="";
var _process_def_provided_listeners="";
var is_open_properties_panel = false;
var task;
var line;
jq(function(){
	try{
		_task_obj = jq('#task');
		_designer = jq('#designer');
		_properties_panel_obj = _designer.layout('panel','east');
		_properties_panel_obj.panel({
			onOpen:function(){
				is_open_properties_panel = true;
			},
			onClose:function(){
				is_open_properties_panel = false;
			}
		});
		_process_panel_obj = _designer.layout('panel','center');
		_task_context_menu = jq('#task-context-menu').menu({});
		_designer.layout('collapse','east');
		
		jq('.easyui-linkbutton').draggable({
					proxy:function(source){
						var n = jq('<div class="draggable-model-proxy"></div>');
						n.html(jq(source).html()).appendTo('body');
						return n;
					},
					deltaX:0,
					deltaY:0,
					revert:true,
					cursor:'auto',
					onStartDrag:function(){
						jq(this).draggable('options').cursor='not-allowed';
					},
					onStopDrag:function(){
						jq(this).draggable('options').cursor='auto';
					}	
		});
		jq('#paintarea').droppable({
					accept:'.easyui-linkbutton',
					onDragEnter:function(e,source){
						jq(source).draggable('options').cursor='auto';
					},
					onDragLeave:function(e,source){
						jq(source).draggable('options').cursor='not-allowed';
					},
					onDrop:function(e,source){
						//jq(this).append(source)
						//jq(this).removeClass('over');
						var wfModel = jq(source).attr('wfModel');
						var shape = jq(source).attr('iconImg');
						if(wfModel){
							var x=jq(source).draggable('proxy').offset().left;
							var y=jq(source).draggable('proxy').offset().top;
							var xOffset    = workflow.getAbsoluteX();
		                    var yOffset    = workflow.getAbsoluteY();
		                    var scrollLeft = workflow.getScrollLeft();
		                    var scrollTop  = workflow.getScrollTop();
		                  //alert(xOffset+"|"+yOffset+"|"+scrollLeft+"|"+scrollTop);
		                  //alert(shape);
		                    addModel(wfModel,x-xOffset+scrollLeft,y-yOffset+scrollTop,shape);
						}
					}
				});
		//jq('#paintarea').bind('contextmenu',function(e){
			//alert(e.target.tagName);
		//});
	}catch(e){
		alert(e.message);
	};
	jq(window).unload( function () { 
		window.opener._list_grid_obj.datagrid('reload');
	} );
});
function addModel(name,x,y,icon){
	var model = null;
	if(icon!=null&&icon!=undefined){
		model = eval("new draw2d."+name+"('"+icon+"')");
	}else{
		model = eval("new draw2d."+name+"(openTaskProperties)");
	}
	//userTask.setContent("DM Approve");
	model.generateId();
	//var id= task.getId();
	//task.id=id;
	//task.setId(id);
	//task.taskId=id;
	//task.taskName=id;
	//var parent = workflow.getBestCompartmentFigure(x,y);
	//workflow.getCommandStack().execute(new draw2d.CommandAdd(workflow,task,x,y,parent));
	workflow.addModel(model,x,y);
}

function openTaskProperties(t){
	if(!is_open_properties_panel)
		_designer.layout('expand','east');
	task=t;
	if(task.type=="draw2d.UserTask")
		_properties_panel_obj.panel('refresh','userTaskProperties.html');
	else if(task.type=="draw2d.ManualTask")
		_properties_panel_obj.panel('refresh','manualTaskProperties.html');
	else if(task.type=="draw2d.ServiceTask")
		_properties_panel_obj.panel('refresh','serviceTaskProperties.html');
	else if(task.type=="draw2d.ScriptTask")
		_properties_panel_obj.panel('refresh','scriptTaskProperties.html');
	else if(task.type=="draw2d.ReceiveTask")
		_properties_panel_obj.panel('refresh','receiveTaskProperties.html');
	else if(task.type=="draw2d.MailTask")
		_properties_panel_obj.panel('refresh','mailTaskProperties.html');
	else if(task.type=="draw2d.BusinessRuleTask")
		_properties_panel_obj.panel('refresh','businessRuleTaskProperties.html');
	else if(task.type=="draw2d.CallActivity")
		_properties_panel_obj.panel('refresh','callActivityProperties.html');
}
function openProcessProperties(id){
	//alert(id);
	if(!is_open_properties_panel)
		_designer.layout('expand','east');
	_properties_panel_obj.panel('refresh','processProperties.html');
}
function openFlowProperties(l){
	//alert(id);
	//if(!is_open_properties_panel)
		//_designer.layout('expand','east');
	line=l;
	_properties_panel_obj.panel('refresh','flowProperties.html');
}
function deleteModel(id){
	var task = workflow.getFigure(id);
	workflow.removeFigure(task);
}
function redo(){
	workflow.getCommandStack().redo();
}
function undo(){
	workflow.getCommandStack().undo();
}
function saveProcessDef(){
	var xml = workflow.toXML();
	var processName = workflow.process.name;
	var processKey = workflow.process.id;
	var proId = jq('#proDefId').val(); 
	jq.ajax({
		url:"${ctx}/wf/procdef/processDeploy_saveProcessXML.action",
		type: 'POST',
		data:{
			processDescXml:xml,processName:processName,processKey:processKey,proId:proId
		},
		dataType:'json',
		error:function(){
			//$.messager.alert("<s:text name='label.common.error'></s:text>","<s:text name='message.common.save.failure'></s:text>","error");
			return "";
		},
		success:function(data){
			alert('保存成功'+data);
		}	
	}); 
	alert('保存成功');
}
function exportProcessDef(obj){
	//obj.href="${ctx}/wf/procdef/procdef!exportProcessDef.action?procdefId="+processDefinitionId+"&processName="+processDefinitionName;
}
function setGlobal(){

			var actdefId = processDefinitionId;  
			var prcDefId =proDefId;  
			var title =   processDefinitionName; 
			var pageUrl = 'sysFlowDef_index.action?prcDefId='+prcDefId+'&actdefId='+actdefId;
			art.dialog.open(pageUrl,{
						id:'ProcessDefWinDiv',
						title:'全局设置['+title+']',  
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width:'90%',
					    height:'90%'
					 });
					 
		}
function openDialog(id,tab){ 
			var pageUrl=""; 
			var actdefId = processDefinitionId;  
			var prcDefId =proDefId;  
			var title =   processDefinitionName; 
			
		    if(tab=="BaseMap"){
		    	pageUrl = 'sysFlowDef_stepMap!load.action?actDefId='+actdefId+'&prcDefId='+prcDefId+'&actStepDefId='+id;
		    }else if(tab=="Route"){
		    	pageUrl = 'sysFlowDef_stepRoute!load.action?pageindex=1&actDefId='+actdefId+'&prcDefId='+prcDefId+'&actStepDefId='+id;
		    }else if(tab=="form"){
		  		pageUrl = 'sysFlowDef_stepFormSet!load.action?pageindex=2&actDefId='+actdefId+'&prcDefId='+prcDefId+'&actStepDefId='+id;
		    }else if(tab=="MJ"){
		    	pageUrl = 'sysFlowDef_stepManualJump!load.action?pageindex=3&actDefId='+actdefId+'&prcDefId='+prcDefId+'&actStepDefId='+id;
		    }else if(tab=="OffLine"){
		    	pageUrl = 'sysFlowDef_stepOffLine_load!load.action?pageindex=4&actDefId='+actdefId+'&prcDefId='+prcDefId+'&actStepDefId='+id;
		    }else if(tab=="SJ"){
		    	pageUrl = 'sysFlowDef_stepSysJump!load.action?pageindex=5&actDefId='+actdefId+'&prcDefId='+prcDefId+'&actStepDefId='+id;
		    }else if(tab=="Supervise"){
		    	pageUrl = 'sysFlowDef_stepSupervise!load.action?pageindex=6&actDefId='+actdefId+'&prcDefId='+prcDefId+'&actStepDefId='+id;
		    }else if(tab=="trigger"){
		    	pageUrl = 'sysFlowDef_stepTrigger!load.action?pageindex=7&actDefId='+actdefId+'&prcDefId='+prcDefId+'&actStepDefId='+id;
		    }else if(tab=="jsTrigger"){
		    	pageUrl = 'sysFlowDef_stepScriptTrigger_load!load.action?pageindex=8&actDefId='+actdefId+'&prcDefId='+prcDefId+'&actStepDefId='+id;
		    }else if(tab=="diybtn"){
		    	pageUrl = 'sysFlowDef_stepDIYBtn_load.action?pageindex=9&actDefId='+actdefId+'&prcDefId='+prcDefId+'&actStepDefId='+id;
		    }else if(tab=="Summary"){
		    	pageUrl = 'process_step_summary.action?pageindex=10&actDefId='+actdefId+'&prcDefId='+prcDefId+'&actStepDefId='+id;
		    }else if(tab=="Help"){ 
		    	pageUrl = 'sysFlowDef_stepMap!load.action?pageindex=11&actDefId='+actdefId+'&prcDefId='+prcDefId+'&actStepDefId='+id;
		    }
		var name = ""; //网页名称，可为空;
		var iWidth = 800; //弹出窗口的宽度;
		var iHeight = 600; //弹出窗口的高度; 
		var iTop = (window.screen.availHeight-30-iHeight)/2; //获得窗口的垂直位置;
		var iLeft = (window.screen.availWidth-10-iWidth)/2; //获得窗口的水平位置;
		art.dialog.open(pageUrl,{
						id:'ProcessDefWinDiv',
						title:'设置',  
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width:'90%',
					    height:'90%'
					 });
		}
//-->
</script>

<body id="designer" class="easyui-layout">
	<div region="west" split="true" iconCls="palette-icon" style="width:150px;">
		<div class="easyui-accordion" fit="true" border="false">
<!--				<div id="connection" title="Connection" iconCls="palette-menu-icon" class="palette-menu">-->
<!--					<a href="##" class="easyui-linkbutton" plain="true" iconCls="sequence-flow-icon">SequenceFlow</a><br>-->
<!--				</div>-->
				<div id="event" title="Event" iconCls="palette-menu-icon" class="palette-menu">
					<a href="##" class="easyui-linkbutton" plain="true" iconCls="start-event-icon">开始</a><br>
					<a href="##" class="easyui-linkbutton" plain="true" iconCls="end-event-icon">结束</a><br>
					<a href="##" class="easyui-linkbutton" plain="true" iconCls="user-task-icon" wfModel="UserTask">用户活动</a><br>
					
					<!-- 
					<a href="##" class="easyui-linkbutton" plain="true" iconCls="service-task-icon" wfModel="ServiceTask">Service Task</a><br>
					<a href="##" class="easyui-linkbutton" plain="true" iconCls="script-task-icon" wfModel="ScriptTask">Script Task</a><br>
					
					<a href="##" class="easyui-linkbutton" plain="true" iconCls="mail-task-icon" wfModel="MailTask">Mail Task</a><br>
					 -->
					<!-- 
					<a href="##" class="easyui-linkbutton" plain="true" iconCls="business-rule-task-icon" wfModel="BusinessRuleTask">Business Rule Task</a><br>
					<a href="##" class="easyui-linkbutton" plain="true" iconCls="subprocess-icon">SubProcess</a><br>
					<a href="##" class="easyui-linkbutton" plain="true" iconCls="callactivity-icon" wfModel="CallActivity">CallActivity</a><br>
					 -->
					<a href="##" class="easyui-linkbutton" plain="true" iconCls="parallel-gateway-icon" wfModel="ParallelGateway" iconImg="iwork_js/designer/icons/type.gateway.parallel.png">并行分支</a><br>
					<a href="##" class="easyui-linkbutton" plain="true" iconCls="exclusive-gateway-icon" wfModel="ExclusiveGateway" iconImg="iwork_js/designer/icons/type.gateway.exclusive.png">单一分支</a><br>
				</div>
		</div>
	</div>
	<div id="process-panel" region="center" split="true"  iconCls="process-icon">
		
				<script>
					function parseProcessDescriptor(data){
						var descriptor = jq(data);
						var definitions = descriptor.find('definitions');
						var process = descriptor.find('process');
						var startEvent = descriptor.find('startEvent');
						var endEvent = descriptor.find('endEvent');
						var userTasks = descriptor.find('userTask');
						var exclusiveGateway = descriptor.find('exclusiveGateway');
						var parallelGateway = descriptor.find('parallelGateway');
						var lines = descriptor.find('sequenceFlow');
						var shapes = descriptor.find('bpmndi\\:BPMNShape');
						var edges = descriptor.find('bpmndi\\:BPMNEdge');
						workflow.process.category= 'http://www.activiti.org/test';
						workflow.process.id=process.attr('id');
						workflow.process.name=process.attr('name');
						var documentation = trim(descriptor.find('process > documentation').text());
						if(documentation != null && documentation != "")
							workflow.process.documentation=documentation;
						var extentsion = descriptor.find('process > extensionElements');
						if(extentsion != null){
							var listeners = extentsion.find('activiti\\:executionListener');
							var taskListeners = extentsion.find('activiti\\:taskListener');
							workflow.process.setListeners(parseListeners(listeners,"draw2d.Process.Listener","draw2d.Process.Listener.Field"));
						}
						jq.each(processDefinitionVariables,function(i,n){
								var variable = new draw2d.Process.variable();
								variable.name=n.name;
								variable.type=n.type;
								variable.scope=n.scope;
								variable.defaultValue=n.defaultValue;
								variable.remark=n.remark;
								workflow.process.addVariable(variable);
							});
						startEvent.each(function(i){
								var start = new draw2d.Start("iwork_js/designer/icons/type.startevent.none.png");
								start.id=jq(this).attr('id');
								start.eventId=jq(this).attr('id');
								start.eventName=jq(this).attr('name');
								shapes.each(function(i){
									var id = jq(this).attr('bpmnElement');
									if(id==start.id){
										var x=parseInt(jq(this).find('omgdc\\:Bounds').attr('x'));
										var y=parseInt(jq(this).find('omgdc\\:Bounds').attr('y'));
										workflow.addFigure(start,x,y);
										return false;
									}
								});
							});
						endEvent.each(function(i){
								var end = new draw2d.End("iwork_js/designer/icons/type.endevent.none.png");
								end.id=jq(this).attr('id');
								end.eventId=jq(this).attr('id');
								end.eventName=jq(this).attr('name');
								shapes.each(function(i){
									var id = jq(this).attr('bpmnElement');
									if(id==end.id){
										var x=parseInt(jq(this).find('omgdc\\:Bounds').attr('x'));
										var y=parseInt(jq(this).find('omgdc\\:Bounds').attr('y'));
										workflow.addFigure(end,x,y);
										return false;
									}
								});
							});
						
						userTasks.each(function(i){
								var task = new draw2d.UserTask();
								var tid = jq(this).attr('id');
								task.id=tid;
								
								var tname = jq(this).attr('name');
								var assignee=jq(this).attr('activiti:assignee');
								var candidataUsers=jq(this).attr('activiti:candidateUsers');
								var candidataGroups=jq(this).attr('activiti:candidateGroups');
								var formKey=jq(this).attr('activiti:formKey');
								if(assignee!=null&&assignee!=""){
									task.isUseExpression=true;
									task.performerType="assignee";
									task.expression=assignee;
								}else if(candidataUsers!=null&&candidataUsers!=""){
									task.isUseExpression=true;
									task.performerType="candidateUsers";
									task.expression=candidataUsers;
								}else if(candidataGroups!=null&&candidataGroups!=""){
									task.isUseExpression=true;
									task.performerType="candidateGroups";
									task.expression=candidataGroups;
								}
								if(formKey!=null&&formKey!=""){
									task.formKey=formKey;
								}
								var documentation = trim(jq(this).find('documentation').text());
								if(documentation != null && documentation != "")
									task.documentation=documentation;
								task.taskId=tid;
								task.taskName=tname;
								if(tid!= tname)
									task.setContent(tname);
								var listeners = jq(this).find('extensionElements').find('activiti\\:taskListener');
								
								
								task.setListeners(parseListeners(listeners,"draw2d.Task.Listener","draw2d.Task.Listener.Field"));
								var performersExpression = jq(this).find('potentialOwner').find('resourceAssignmentExpression').find('formalExpression').text();
								if(performersExpression.indexOf('user(')!=-1){
									task.performerType="candidateUsers";
								}else if(performersExpression.indexOf('group(')!=-1){
									task.performerType="candidateGroups";
								}
								var performers = performersExpression.split(',');
								jq.each(performers,function(i,n){
									var start = 0;
									var end = n.lastIndexOf(')');
									if(n.indexOf('user(')!=-1){
										start = 'user('.length;
										var performer = n.substring(start,end);
										task.addCandidateUser({
												sso:performer
										});
									}else if(n.indexOf('group(')!=-1){
										start = 'group('.length;
										var performer = n.substring(start,end);
										task.addCandidateGroup(performer);
									}
								});
								shapes.each(function(i){
									var id = jq(this).attr('bpmnElement');
									if(id==task.id){
										var x=parseInt(jq(this).find('omgdc\\:Bounds').attr('x'));
										var y=parseInt(jq(this).find('omgdc\\:Bounds').attr('y'));
										workflow.addModel(task,x,y);
										return false;
									}
								});
							});
						exclusiveGateway.each(function(i){
								var gateway = new draw2d.ExclusiveGateway("iwork_js/designer/icons/type.gateway.exclusive.png");
								var gtwid = jq(this).attr('id');
								var gtwname = jq(this).attr('name');
								gateway.id=gtwid;
								gateway.gatewayId=gtwid;
								gateway.gatewayName=gtwname;
								shapes.each(function(i){
									var id = jq(this).attr('bpmnElement');
									if(id==gateway.id){
										var x=parseInt(jq(this).find('omgdc\\:Bounds').attr('x'));
										var y=parseInt(jq(this).find('omgdc\\:Bounds').attr('y'));
										workflow.addModel(gateway,x,y);
										return false;
									}
								});
							});
						parallelGateway.each(function(i){
							var gateway = new draw2d.ParallelGateway("iwork_js/designer/icons/type.gateway.parallel.png");
							var gtwid = jq(this).attr('id');
							var gtwname = jq(this).attr('name');
							gateway.id=gtwid;
							gateway.gatewayId=gtwid;
							gateway.gatewayName=gtwname;
							shapes.each(function(i){
								var id = jq(this).attr('bpmnElement');
								if(id==gateway.id){
									var x=parseInt(jq(this).find('omgdc\\:Bounds').attr('x'));
									var y=parseInt(jq(this).find('omgdc\\:Bounds').attr('y'));
									workflow.addModel(gateway,x,y);
									return false;
								}
							});
						});
						lines.each(function(i){
						
								var lid = jq(this).attr('id');
								var name = jq(this).attr('name');
								var condition=jq(this).find('conditionExpression').text();
								var sourceRef = jq(this).attr('sourceRef');
								var targetRef = jq(this).attr('targetRef');
								var source = workflow.getFigure(sourceRef);
								var target = workflow.getFigure(targetRef);
								edges.each(function(i){
										var eid = jq(this).attr('bpmnElement');
										if(eid==lid){
											var startPort = null;
											var endPort = null;
											var points = jq(this).find('omgdi\\:waypoint');
											var startX = jq(points[0]).attr('x');
											var startY = jq(points[0]).attr('y');
											var endX = jq(points[1]).attr('x');
											var endY = jq(points[1]).attr('y');
											
											var sports = source.getPorts();
											for(var i=0;i<sports.getSize();i++){
												var defaultPort = null;
												var s = sports.get(i);
												var x = s.getAbsoluteX();
												var y = s.getAbsoluteY();
												if(i==2){
													defaultPort = s;
												}
												//alert("[startX:"+startX+"][startY:"+startY+"][endX:"+endX+"][endY:"+endY+"]\n"+"[x:"+x+"][y:"+y+"]");
												//alert("x:"+x+"y:"+y+"startX:"+startX+"startY:"+startY);
												if(x == startX&&y==startY){ 
													startPort = s;
													break;
												}
												if(startPort==null){
													startPort = defaultPort;
												}
											}
											var tports = target.getPorts();
											for(var i=0;i<tports.getSize();i++){
												var t = tports.get(i);
											//	printObject(t);
												var defaultPort = null;
												defaultPort = t;
												var x = t.getAbsoluteX();
												var y = t.getAbsoluteY();
												if(x==endX&&y==endY){
													endPort = t;
													break;
												}
												if(endPort==null){
													endPort = defaultPort;
												}
											}
										//	alert("1-4"+startPort+">>>"+endPort);
											if(startPort != null&&endPort!=null){
											//alert(';');
												var cmd=new draw2d.CommandConnect(workflow,startPort,endPort);
												var connection = new draw2d.DecoratedConnection();
												connection.id=lid;
												connection.lineId=lid;
												connection.lineName=name;
												if(lid!=name)
													connection.setLabel(name);
												if(condition != null && condition!=""){
													connection.condition=condition;
												}
												cmd.setConnection(connection);
												workflow.getCommandStack().execute(cmd);
												
											}
											return false;
										}
									});
							});
						if(typeof setHightlight != "undefined"){
							setHightlight();
						}
					}
					function parseListeners(listeners,listenerType,fieldType){
						var parsedListeners = new draw2d.ArrayList();
						listeners.each(function(i){
							var listener = eval("new "+listenerType+"()");
							
							listener.event=jq(this).attr('event');
							var expression = jq(this).attr('expression');
							var clazz = jq(this).attr('class');
						
							if(expression != null && expression!=""){
								listener.serviceType='expression';
								listener.serviceExpression=expression;
							}else if(clazz != null&& clazz!=""){
								listener.serviceType='javaClass';
								listener.serviceClass=clazz;
							}
							var fields = jq(this).find('activiti\\:field');
							fields.each(function(i){
								var field = eval("new "+fieldType+"()");
								field.name=jq(this).attr('name');
								//alert(field.name);
								var string = jq(this).finsd('activiti\\:string').text();
								var expression = jq(this).find('activiti\\:expression').text();
								//alert("String="+string.text()+"|"+"expression="+expression.text());
								if(string != null && string != ""){
									field.type='string';
									field.value=string;
								}else if(expression != null && expression!= ""){
									field.type='expression';
									field.value=expression;
								}
								listener.setField(field);
							});
							parsedListeners.add(listener);
						});
						return parsedListeners;
					}
				</script>
				<div id="process-definition-tab">
							<div id="designer-area" title="画布" style="POSITION: absolute;width:100%;height:100%;padding: 0;border: none;overflow:auto;">
								<div id="paintarea" style="POSITION: absolute;WIDTH: 3000px; HEIGHT: 3000px" ></div>
							</div>
							<div id="xml-area" title="XML" style="width:100%;height:100%;overflow:hidden;overflow-x:hidden;overflow-y:hidden;">
								<textarea id="descriptorarea" rows="38" style="width: 100%;height:100%;padding: 0;border: none;" readonly="readonly"></textarea>
							</div>
				</div>
				<script type="text/javascript">
					<!--
					jq('#process-definition-tab').tabs({
						fit:true,
						onSelect:function(title){
							if(title=='Diagram'){
								
							}else if(title=='XML'){
								jq('#descriptorarea').val(workflow.toXML());
								/*
								if(document.body.innerText)
									jq('#xml-area').get(0).innerText=workflow.toXML();
								else if(document.body.textContent)
									jq('#xml-area').get(0).textContent=workflow.toXML();
								*/
							}
						}
					});
					function openProcessDef(){
					var pageurl = "processDeploy_showProcessXML.action?actProcId="+processDefinitionId;
						jq.ajax({
							url:pageurl,
							type: 'POST',
							/*
							data:{
										moduleId:"${moduleId}",
										_request_json_fields:json4params
								},
							*/
							error:function(){
								alert('xml加载错误');
								return "";
							},
							success:parseProcessDescriptor	
						}); 
					}
				
					function createCanvas(disabled){
						try{
							//initCanvas();
							workflow  = new draw2d.MyCanvas("paintarea");
							workflow.scrollArea=document.getElementById("designer-area");
							if(disabled)
								workflow.setDisabled();
							if(typeof processDefinitionId != "undefined" && processDefinitionId != null &&  processDefinitionId != "null" && processDefinitionId != "" && processDefinitionId != "NULL"){
								openProcessDef();
							}else{
									var id =document.getElementById("processKey").value;
									//var id = workflow.getId();
									workflow.process.category='demo_wf_process_def';
									workflow.process.id=id;
									workflow.process.name=document.getElementById("processName").value;
								// Add the start,end,connector to the canvas
								  var startObj = new draw2d.Start("iwork_js/designer/icons/type.startevent.none.png");
								  //startObj.setId("start");
								  workflow.addFigure(startObj, 200,50);
								  
								  var endObj   = new draw2d.End("iwork_js/designer/icons/type.endevent.none.png");
								  //endObj.setId("end");
								  workflow.addFigure(endObj,200,400);
							} 
						}catch(e){
							alert(e.message);
						}
					}
					//-->
				</script>
	</div>
	<div id="properties-panel" region="east" split="true" iconCls="properties-icon" title="Properties" style="display:none">
		
	</div>
	
	<!-- toolbar -->
	<div id="toolbar-panel" region="north" border="false" style="height:36px;background:#E1F0F2;">
		<div style="background:#E1F0F2;padding:5px;">
			<input type="button" name="saveBtn" id="saveBtn" value="保存" onclick="saveProcessDef()"/>
			<input type="button" name="saveBtn" id="saveBtn" value="前进" onclick="undo()"/>
			<input type="button" name="saveBtn" id="saveBtn" value="后退" onclick="redo()"/>
			<input type="button" name="saveBtn" id="saveBtn" value="刷新" onclick="window.location.reload();"/>
			<input type="button" name="saveBtn" id="saveBtn" value="流程全局设置" onclick="setGlobal();"/>
		</div>
	</div>
	<!-- task context menu -->
	<div id="task-context-menu" class="easyui-menu" style="width:120px;">
		<div id="properties-task-context-menu" iconCls="properties-icon">Properties</div>
		<div id="delete-task-context-menu" iconCls="icon-remove">Delete</div>
	</div>
	<!-- form configuration window -->
	<div id="form-win" title="Form Configuration" style="width:750px;height:500px;">
	</div>
	<!-- listener configuration window -->
	<div id="listener-win" title="Listener Configuration" style="width:750px;height:500px;">
	</div>
	<!-- candidate configuration window -->
	<div id="task-candidate-win" title="" style="width:750px;height:500px;">
	</div>
	<s:hidden  id="id" name="id"></s:hidden>
	    	<s:hidden  id="proDefId" name="proDefId"></s:hidden>
	    	<s:hidden  id="actProcId" name="actProcId"></s:hidden>
	    	<s:hidden  id="processShowType" name="processShowType"></s:hidden>
	    	<s:hidden  id="processName" name="processName"></s:hidden>
	    	<s:hidden  id="processKey" name="processKey"></s:hidden>
</body>
</html>
<script type="text/javascript">
<!--
	createCanvas(false);
//-->
</script>