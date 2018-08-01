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
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
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
	<link href="iwork_js/designer/runtime.css" type="text/css" rel="stylesheet"/>
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
var processDefinitionId='<s:property value="actDefId"/>'; 
var actStepDefId = '<s:property value="actStepDefId"/>';
var instanceId = '<s:property value="instanceId"/>';
var excutionId = '<s:property value="excutionId"/>';
var taskId = '<s:property value="taskId"/>';
var tasklist = <s:property value="tasklist" escapeHtml='false'/>;
var currentid = <s:property value="currentStepId" escapeHtml='false'/>;

var processDefinitionName='1111111';
var proDefId='222222';
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
	}catch(e){
		alert(e.message);
	};
	//jq(window).unload( function () { 
		//window.opener._list_grid_obj.datagrid('reload');
	//} );
});
function openProcessDef(){
						jq.ajax({
							url:"processDeploy_showProcessXML.action?actProcId="+processDefinitionId,
							type: 'POST',
							/*
							data:{
										moduleId:"${moduleId}",
										_request_json_fields:json4params
								},
							*/
							error:function(e){
								alert(e);
								return "";
							},
							success:function(data){
								parseProcessDescriptor(data);	
							}
						}); 
					}
				
					function createCanvas(disabled){
						
						try{
							//initCanvas();
							workflow  = new draw2d.MyCanvas("paintarea");
							workflow.scrollArea=document.getElementById("designer-area");
							if(typeof processDefinitionId != "undefined" && processDefinitionId != null &&  processDefinitionId != "null" && processDefinitionId != "" && processDefinitionId != "NULL"){
								openProcessDef();
							}
						}catch(e){
							alert(e.message);
						}
					}
				
//-->
</script>

<body id="designer" class="easyui-layout">
	<div id="process-panel" region="center" split="true" >
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
						
						workflow.process.category=definitions.attr('targetNamespace');
						workflow.process.id=process.attr('id');
						workflow.process.name=process.attr('name');
							workflow.disabled = true;
							workflow.readOnly = true;
						var documentation = trim(descriptor.find('process > documentation').text());
						if(documentation != null && documentation != "")
							workflow.process.documentation=documentation;
						var extentsion = descriptor.find('process > extensionElements');
						if(extentsion != null){
							var listeners = extentsion.find('activiti\\:executionListener');
							var taskListeners = extentsion.find('activiti\\:taskListener');
							//var listeners =extentsion.find('activiti\\:taskListener');
							//workflow.process.setListeners(parseListeners(listeners,"draw2d.Process.Listener","draw2d.Process.Listener.Field"));
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
								//检查是否过该节点
								var isFinish = false;
								
								var isCurrent = false;
								for(var i=0;i<tasklist.length;i++){
									if(tasklist[i]==tid){
										isFinish = true;
										break;
									}
								}
								
								for(var i=0;i<currentid.length;i++){
									if(currentid[i]==tid){
										isCurrent = true;
										break;
									}
								}
								if(tid!= tname){
									if(isFinish){
										task.setContent("<span style='color:gray'>"+tname+"</span><img src='iwork_img/ui-check-box_2.png' title='已审批' style='margin-left:5px;margin-top:5px;'>");
									}else{
										task.setContent("<span style='color:#0000ff'>"+tname+"</span>");
									}
									
									if(isCurrent){
										task.setContent("<span style='color:red'>"+tname+"</span><img src='iwork_img/page_edit.png' title='办理中' style='margin-left:5px;margin-top:5px;'>");
									}
								}
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
							var gateway = new draw2d.ExclusiveGateway("iwork_js/designer/icons/type.gateway.parallel.png");
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
						//	printObject(listener);
							listener.event=jq(this).attr('event');
							var expression = jq(this).attr('expression'); 
							var clazz = jq(this).attr('class');
						//	alert(clazz);
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
								var string = jq(this).find('activiti\\:string').text();
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
							<div id="designer-area" title="画布" style="POSITION: absolute;width:100%;height:100%;padding: 0;border: none;overflow:auto;">
								<div id="paintarea" style="POSITION: absolute;WIDTH: 3000px; HEIGHT: 3000px" ></div>
							</div>
	</div>
	<!-- toolbar -->
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
	createCanvas(true);
//-->
</script>