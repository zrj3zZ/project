<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="iwork" uri="/WEB-INF/sys-commonsTags.tld" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
	<link href="iwork_css/reset.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>

	<style type="text/css">
		.td_title {
				color:#004080;
				font-size: 12px;
				text-align: right;
				letter-spacing: 0.1em;
				padding-right:10px;
				white-space:nowrap;
				vertical-align:middle;
				font-family:"微软雅黑";
				height:15px;
				
			}
		.td_data{
			color:#0000FF;
			text-align:left;
			padding-left:3px;
			font-size: 12px;
			vertical-align:middle;
			word-wrap:break-word;
			word-break:break-all;
			font-weight:500;
			line-height:12px;
			padding-top:5px;
			font-family:"微软雅黑";
			height:15px;
		}
		.rpinput{
		    width:80px;
		}
		input {
		 color:#0000FF;
		}
		textarea{
		color:#0000FF;
		}
		body {
			margin-left: 0px;
			margin-top: 0px;
			margin-right: 0px;
			margin-bottom: 0px;
		}
		*{font-family:Verdana;font-size:96%;}
           label.error{float:none;color:red;padding-left:.5em;}
           p{clear:both;}
           .submit{margin-left:12em;}
           em{font-weight:bold;}
	</style>
	<script type="text/javascript">
	    //全局变量
    var api = art.dialog.open.api, W = api.opener;
		//表单提交
		function doSubmit(){ 
          var bool= validate();
          if(bool){
            var options = {
				error:errorFunc,
				success:successFunc 
			   };
			$('#editForm').ajaxSubmit(options);
           }
          else{
          		return ;
        	}
       }
      errorFunc=function(){
           art.dialog.tips("保存失败！",2);
      }
      successFunc=function(responseText, statusText, xhr, $form){
           if(responseText=="ok"){
              art.dialog.tips("保存成功！",2);
              setTimeout('cancel();',1000);
           }
           else if(responseText=="error"){
              art.dialog.tips("保存失败！",2);
           } 
      }
		//表单验证
		function validate(){
		     var bool1=$('#editForm_model_routeType0').attr('checked');
		     var bool2=$('#editForm_model_routeType1').attr('checked');
		     if(!bool1&!bool2){
		           art.dialog.tips("请选择路由方式！",2);
		           return false;
		     }
		     var routeSchema=$('#editForm_model_routeSchema').val();
		     if(routeSchema==""){
		           art.dialog.tips("请选择路由方案！",2);
		           $('#editForm_model_routeSchema').focus();
		           return false;
		     }
		     if(routeSchema<=3){
		          if($('#userType_3').attr('checked')){
		                 if($('#userTypeDiy').val()==""){
		                      art.dialog.tips("请选择自定义用户！",2);
		          			  $('#userTypeDiy').focus();
		           			  return false;
		                 }
		          } 
		     }
		     if(routeSchema==4||routeSchema==5||(routeSchema>=11&routeSchema<=14)){
		          var title=$('#routeParam_title').html();
		          var routeParam=$.trim($('#editForm_model_routeParam').val()); 
		          $('#editForm_model_routeParam').val(routeParam);
		          title=title.slice(0,title.length-1);
		          if(routeParam==""){
		                art.dialog.tips(title+"不能为空！",2);
		          	    $('#editForm_model_routeParam').focus();
		           	    return false;
		          }
		          if(length2(routeParam)>500){
		                art.dialog.tips(title+"过长！",2);
		          	    $('#editForm_model_routeParam').focus();
		           	    return false;
		          }
		     }
		     if(routeSchema>=7&routeSchema<=9){
		     	
		     }
		       if(routeSchema==10){
		     	  var title=$('#rolename_title').html();
		          var stepname = $('#editForm_model_routeParam').val();
		          if(stepname==""){
		         		title=title.slice(0,title.length-1);
		               art.dialog.tips(title+"不能为空！",2);
		          	   $('#editForm_model_routeParam').focus(); 
		           	   return false;
		          }	
		     	}
		     var routePurview=$.trim($('#editForm_model_routePurview').val());
		     $('#editForm_model_routePurview').val(routePurview);
		     if(length2(routePurview)>500){
		            art.dialog.tips("权限授权过长！",2);
		            $('#editForm_model_routeParam').focus();
		            return false;
		     }
		     var routeMemo=$.trim($('#editForm_model_routeMemo').val());
		     $('#editForm_model_routeMemo').val(routeMemo);
		     if(length2(routeMemo)>500){
		            art.dialog.tips("策略描述过长！",2);
		            $('#editForm_model_routeMemo').focus();
		            return false;
		     }
		     return true;
		}
		//执行退出
		function cancel(){
            api.close();
			//window.parent.location.reload();
		}
		//修改路由策略
		function showForm(obj){
		    $('form').attr('action','sysFlowDef_stepRoute!loadform.action');
		    $("#editForm").submit();
		}
		
		//选择策略
		function selectRoute(obj){
			if(obj.value!=''){
				$('form').attr('action','sysFlowDef_stepRoute!selectRoute.action');
			    $("#editForm").submit();
			}
		}
		function showMenu() {
				var cityObj = $("#routeMemo"); 
				var cityOffset = $("#routeMemo").offset();
				$("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");
				$("body").bind("mousedown", onBodyDown);
				return false;
		}
		
		
		//
		$(document).ready(function(){
              <s:if test="model.routeSchema==6||model.routeSchema==7">
              $('#editForm_model_rolename').combotree({
				    url: 'authorityAddressBookAction!groupTree.action',
				    onSelect:function(node){
			    		$('#editForm_model_routeParam').val(node.id);
			    		$('#editForm_model_rolename').combotree('setValue',node.text);
				    },
					onLoadSuccess:function(){
						var routeParam = $('#editForm_model_routeParam').val(); 
						if(routeParam!=''){//如果团队有初始值，给团队赋值
							var node = $(this).tree('find', routeParam);  
							if(node!=null){
								$('#editForm_model_rolename').combotree('setValue',node.text); 
							}
						}
					}  
			  });
              </s:if>
              <s:elseif test="model.routeSchema==8||model.routeSchema==9">
              	
              /**
              $('#editForm_model_rolename').combotree({
				    url: 'authorityAddressBookAction!roleTree.action',
				    multiple:true,
				    width:355,
				    onSelect:function(node){
				    	this.open();
				    },
				    onCheck:function(node){
				    	
				    	var nodes = $(this).tree('getNode');
				    	var txt = "";
				    	for(var i=0;i<nodes.length;i++){
				    		txt +=","+nodes[i].id;
				    	}
				    	$("#editForm_model_routeParam").val(txt.substring(1));
				    },onLoadSuccess: function () {
				    	var param = $("#editForm_model_routeParam").val();
				    	if(param!=''){
				    		var params = param.split(",");
				    		var nodes = $(this).tree('getRoots');
				    		alert(nodes);
					    	for(var i=0;i<nodes.length;i++){
					    		var id = nodes[i].id;
						    	alert(nodes[i].text);
					    	}
				    	}
                	}
			  });
			  var params = $("#routeParamTitle").val();
			  var title = params.split(","); 
			  $('#editForm_model_rolename').combotree('setValues',title);
			  **/
              </s:elseif>
              <s:elseif test="model.routeSchema==16">
              $('#stationid').combotree({
				    url: 'orgStationListTree.action',
				    multiple:true,
				    width:355,
				    onSelect:function(node){
				    	this.open();
				    },
				    onCheck:function(node){
				    	var nodes = $(this).tree('getChecked');
				    	var txt = "";
				    	for(var i=0;i<nodes.length;i++){
				    		txt +=","+nodes[i].id;
				    	}
				    	$("#editForm_model_routeParam").val(txt.substring(1));
				    }
			  });
			  var params = $("#routeParamTitle").val();
			  var title = params.split(","); 
			  $('#editForm_model_rolename').combotree('setValues',title);
              </s:elseif>
           });
           
		//单选地址簿
		function openRadioBook(isOrg, isRole, isGroup, parentDept, currentDept, startDept, targetUserId, targetUserName, targetDeptId, targetDeptName, defaultField) {	  	
			var userId = document.getElementById(defaultField).value;	
			var pageUrl = "radioAddressBookAction!index.action?userId=" + userId + "&parentDept=" + parentDept + "&currentDept=" + currentDept + "&startDept=" + startDept + "&isOrg=" + isOrg + "&isRole=" + isRole + "&isGroup=" + isGroup;
			   art.dialog.open(pageUrl,{
					id:'addressDialog', 
					title:"地址簿",
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:500,
				    height:510
				 });
			
	    }
		//多选地址簿
		function multi_book(defaultField) {
		var code = document.getElementById(defaultField).value;	
		var pageUrl = "multibook_index.action?input="+encodeURI(code)+"&defaultField="+defaultField;
			var obj = new Object();
			 art.dialog.open(pageUrl,{
					id:'addressDialog', 
					title:"地址簿",
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:500,
				    height:510
				 });
		}
		 function dept_book(targetDeptId, targetDeptName, defaultField) {
			var url = "deptbook_index.action?1=1";
				var obj = new Object();
			obj.dialogName = "routeStepDialog";
			obj.win = window;
			if(targetDeptId!=''){
				url+="&targetDeptId="+targetDeptId;
			}
			if(targetDeptName!=''){
				url+="&targetDeptName="+targetDeptName;
			}
			if(defaultField!=''){
				url+="&defaultField="+defaultField;
			}
			//获得input内容
			var v = document.getElementById(defaultField);
			if(v.value!=""){
				url+="&input="+v.value;
			} 
			 art.dialog.open(url,{
					id:"deptBookDialog",
					title: '部门地址簿',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:500,
				    height:510
				 });
				 
		}
		
		 function dept_multi_book(targetDeptId, targetDeptName, defaultField) {
			var url = "deptbook_multi_index.action?1=1";
				var obj = new Object();
			obj.dialogName = "routeStepDialog"; 
			obj.win = window;
			if(targetDeptId!=''){
				url+="&targetDeptId="+targetDeptId;
			}
			if(targetDeptName!=''){
				url+="&targetDeptName="+targetDeptName;
			}
			if(defaultField!=''){
				url+="&defaultField="+defaultField;
			}
			//获得input内容
			var v = document.getElementById(defaultField);
			if(v.value!=""){
				url+="&input="+v.value;
			} 
			 art.dialog.open(url,{
					id:"deptBookDialog",
					title: '多选部门地址簿',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:500,
				    height:510
				 });
		}
		//岗位列表
		 function role_book(targetId, targetName) {
			var url = "role_tree_book.action?1=1";
				var obj = new Object();
			obj.dialogName = "routeStepDialog"; 
			obj.win = window;
			if(targetId!=''){
				url+="&inputName="+targetId;
			}
			if(targetName!=''){
				url+="&inputTitle="+targetName;
			}
			 art.dialog.open(url,{
					id:"deptBookDialog",
					title: '角色地址簿',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:500,
				    height:510
				 });
		}
		//岗位列表
		 function station_book(targetId, targetName) {
			var url = "org_station_book.action?1=1";
				var obj = new Object();
			obj.dialogName = "routeStepDialog"; 
			obj.win = window;
			if(targetId!=''){
				url+="&inputName="+targetId;
			}
			if(targetName!=''){
				url+="&inputTitle="+targetName;
			}
			 art.dialog.open(url,{
					id:"deptBookDialog",
					title: '岗位地址簿',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:500,
				    height:510
				 });
		}
		
		//权限地址簿
		function openAuthorityBook(fieldName){
			var code = $("#"+fieldName).val();
			var url = "authorityAddressBookAction!index.action?target="+fieldName+"&code="+encodeURI(encodeURI(code));
			 art.dialog.open(url,{
					id:"selectrouteParam",
					title: '权限地址簿', 
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:'90%',
				    height:'90%'
				 });
		}
	</script>
</head>

<body  class="easyui-layout">
		<div region="center" style="padding:3px;border:0px">
            	<s:form id ="editForm" name="editForm" action="sysFlowDef_stepRoute!save.action"  theme="simple">
	            	<table border="0"  cellspacing="0" cellpadding="0">
	            		<tr>
	            			<td class="td_title"><span style='color:red'>*</span>路由方式:</td>
	            			<td class="td_data">
	            				<span id="div_routeType">
	            				<s:radio value="model.routeType"  list="#{'0':'由办理人选择','1':'设置固定办理人'}"  name="model.routeType" theme="simple"/>
	            				</span>
	            			</td>
	            		</tr>
	            		<tr>
	            			<td class="td_title"><span style='color:red'>*</span>路由方案:</td>
	            			<td class="td_data">
								<s:select onchange="selectRoute(this);"  headerKey="" headerValue="-----请选择路由策略----" name="model.routeSchema" list="%{routeSchemalist}" cssStyle="font-size:12px;color:#0000FF;" listKey="key" listValue="title" theme="simple"></s:select>
								
							</td>
	            		</tr>
	            		<tr>
	            			
	            			<s:if test="model.routeSchema<=3">
	            				<td class="td_title" style="vertical-align:top;padding-top:8px;"><span style='color:red'>*</span>指定用户类型:</td>
	            				<td class="td_data">
	            					<s:if test="model.routeParam=='%userid%'">
		            					<input type="radio" name="userType" id="userType_1" checked onclick="$('#editForm_model_routeParam').val(this.value);$('#routeParamSpan').hide();" value="%userid%"/><label for="userType_1">发送人</label>
		            					<input type="radio" name="userType" id="userType_2" onclick="$('#editForm_model_routeParam').val(this.value);$('#routeParamSpan').hide();" value="%createUser%"/><label for="userType_2">申请人</label>
										<input type="radio" name="userType" id="userType_3"onclick="$('#routeParamSpan').show();$('#editForm_model_routeParam').val('');$('#userTypeDiy').val('');"  value="3"/><label for="userType_3">自定义</label>
										<br/><br/><span id="routeParamSpan"  style='display:none'><s:textarea name = "model.routeParam"    cssStyle="width:320px;height:50px;"></s:textarea>&nbsp;<a href="###" onclick="multi_book('editForm_model_routeParam');" title="多选地址薄" class="easyui-linkbutton" plain="true" iconCls="icon-multibook"></a></span>
	            					</s:if>
	            					<s:elseif test="model.routeParam=='%createUser%'">
		            					<input type="radio" name="userType" id="userType_1"  onclick="$('#editForm_model_routeParam').val(this.value);$('#routeParamSpan').hide();" value="%userid%"/><label for="userType_1">发送人</label>
		            					<input type="radio" name="userType" id="userType_2" checked onclick="$('#editForm_model_routeParam').val(this.value);$('#routeParamSpan').hide();" value="%createUser%"/><label for="userType_2">申请人</label>
										<input type="radio" name="userType" id="userType_3"onclick="$('#routeParamSpan').show();$('#editForm_model_routeParam').val('');$('#userTypeDiy').val('');"  value="3"/><label for="userType_3">自定义</label>
										<br/><br/><span id="routeParamSpan"  style='display:none'><s:textarea name = "model.routeParam"  cssStyle="width:320px;height:50px;"></s:textarea>&nbsp;<a href="###" onclick="multi_book('editForm_model_routeParam');" title="多选地址薄" class="easyui-linkbutton" plain="true" iconCls="icon-multibook"></a></span>
	            					</s:elseif>
	            					<s:else>
	            					<input type="radio" name="userType" id="userType_1"  onclick="$('#editForm_model_routeParam').val(this.value);$('#routeParamSpan').hide();" value="%userid%"/><label for="userType_1">发送人</label>
		            					<input type="radio" name="userType" id="userType_2"  onclick="$('#editForm_model_routeParam').val(this.value);$('#routeParamSpan').hide();" value="%createUser%"/><label for="userType_2">申请人</label>
										<input type="radio" name="userType" id="userType_3" checked onclick="$('#routeParamSpan').show();$('#editForm_model_routeParam').val('');$('#userTypeDiy').val('');"  value="3"/><label for="userType_3">自定义</label>
										<br/><br/><span id="routeParamSpan"><s:textarea name = "model.routeParam"    cssStyle="width:320px;height:50px;"></s:textarea>&nbsp;<a href="###" onclick="multi_book('editForm_model_routeParam');" title="多选地址薄" class="easyui-linkbutton" plain="true" iconCls="icon-multibook"></a></span>
	            					</s:else>						
								</td>
	            			</s:if>
	            			<s:elseif test="model.routeSchema==4||model.routeSchema==5">
	            				<td class="td_title" id="routeParam_title"><span style='color:red'>*</span>设置部门范围:</td>
	            				<td class="td_data">
									<s:textfield name = "model.routeParam"></s:textfield><a href="###" onclick="dept_book('editForm_model_routeParam','','showTitle');" title="部门地址薄" class="easyui-linkbutton" plain="true" iconCls="icon-deptbook"></a>
								</td>
	            			</s:elseif>
	            			<s:elseif test="model.routeSchema==6||model.routeSchema==7">
	            				<td class="td_title" id="rolename_title">选择团队:</td>
	            				<td class="td_data">
	            					<input type="text" name="editForm_model_rolename" id="editForm_model_rolename" value="">
									<s:textfield name = "model.routeParam" cssStyle="display:none;"></s:textfield>
									<span style='color:red'>*</span>
								</td>
	            			</s:elseif>
	            			<s:elseif test="model.routeSchema==8||model.routeSchema==9">
	            				<td class="td_title" id="rolename_title">选择角色:</td>
	            				<td class="td_data">
	            				<s:textfield name = "title" id="editForm_model_stationname" cssStyle="background:#efefef;border:1px solid #ccc;width:350px" readonly="true"  ></s:textfield>
										<s:textfield id="editForm_model_routeParam" name = "model.routeParam" cssStyle="display:none;"></s:textfield>
									<a href="###" onclick="role_book('editForm_model_routeParam','editForm_model_stationname');" title="角色列表" class="easyui-linkbutton" plain="true" iconCls="icon-hrbook"></a>
									<span style='color:red'>*</span> 
								</td>
	            			</s:elseif>
	            			<s:elseif test="model.routeSchema==10">
	            				<td class="td_title" id="rolename_title">选择节点:</td>
	            				<td class="td_data">
	            				<s:select list="activityList" name="model.routeParam" headerKey="" headerValue="----请选择节点---" listKey="actStepId" listValue="stepTitle" theme="simple"  ></s:select>
									<span style='color:red'>*</span> 
								</td>
	            			</s:elseif>
	            			<s:elseif test="model.routeSchema==11">
	            				<td class="td_title" id="routeParam_title">设置SQL语句:</td>
	            				<td class="td_data">
									<s:textarea name = "model.routeParam" cssStyle="width:320px;height:50px;"></s:textarea>
									<span style='color:red'>*</span>
								</td> 
	            			</s:elseif>
	            			<s:elseif test="model.routeSchema==12">
	            				<td class="td_title" id="routeParam_title">设置存储表.字段ID:</td>
	            				<td class="td_data">
									<s:textfield name = "model.routeParam" cssStyle="width:320px;"></s:textfield><BR>例如：BD_HR_BASEINFO.USERID
								<span style='color:red'>*</span>
								</td>
	            			</s:elseif>
	            			<s:elseif test="model.routeSchema==13">
	            				<td class="td_title" id="routeParam_title">设置路由表路径:</td>
	            				<td class="td_data">
									<s:textfield name = "model.routeParam" cssStyle="width:320px;"></s:textfield>
								    <span style='color:red'>*</span>
								</td>
	            			</s:elseif>
	            			<s:elseif test="model.routeSchema==14">
	            				<td class="td_title" id="routeParam_title">设置触发器路径:</td>
	            				<td class="td_data">
									<s:textfield name = "model.routeParam" cssStyle="width:320px;"></s:textfield>
								    <span style='color:red'>*</span>
								</td>
	            			</s:elseif>
	            			<s:elseif test="model.routeSchema==15">
	            				<td class="td_title" id="routeParam_title">设置部门:</td>
	            				<td class="td_data">
									<s:hidden name = "model.routeParam"></s:hidden><s:textfield name = "routeParamTitle" cssStyle="background:#efefef;border:1px solid #ccc;width:350px" readonly="true"  id = "routeParamTitle"></s:textfield><a href="###" onclick="dept_multi_book('editForm_model_routeParam','','showTitle');" title="部门地址薄" class="easyui-linkbutton" plain="true" iconCls="icon-deptbook"></a>
								</td>
	            			</s:elseif>
	            			<s:elseif test="model.routeSchema==16">
	            				<td class="td_title" id="routeParam_title"><span style='color:red'>*</span> 设置岗位:</td>
	            				<td class="td_data">
	            				<s:textfield name = "title" id="editForm_model_stationname" cssStyle="background:#efefef;border:1px solid #ccc;width:350px" readonly="true"  ></s:textfield>
										<s:textfield name = "model.routeParam" cssStyle="display:none;"></s:textfield>
									<a href="###" onclick="station_book('editForm_model_routeParam','editForm_model_stationname');" title="岗位列表" class="easyui-linkbutton" plain="true" iconCls="icon-hrbook"></a>
									
								</td>
	            			</s:elseif>
	            		</tr>
	            		<s:if test="model.routeSchema==16">
	            				<td class="td_title" id="routeParam_title"><span style='color:red'>*</span> 判断依据:</td>
	            				<td class="td_data">
	            					<s:radio list="#{'owner':'根据【发起人】查找岗位','currentUser':'根据【当前办理人】查找岗位'}"  name = "model.routeParam2"></s:radio>
								</td>
	            			</s:if>
	            		<tr>
	            			<td class="td_title">权限授权:</td>
	            			<td class="td_data">
								<s:textarea name = "model.routePurview"  cssStyle="width:320px;height:50px;"></s:textarea>
								<a href="javascript:openAuthorityBook('editForm_model_routePurview');" class="easyui-linkbutton" plain="true" iconCls="icon-add">权限地址簿</a>								
							</td>
	            		</tr>
	            		<tr>
	            			<td class="td_title">策略描述:</td><td class="td_data">
									<s:textarea name = "model.routeMemo" cssStyle="width:320px;height:50px;"></s:textarea>
							</td>
	            		</tr>
	            	</table>
	                <s:hidden name="routeParamTitle" id="routeParamTitle"/>
	                <s:hidden name="model.actDefId"/>
	                <s:hidden name="model.actStepId"/>
	                <s:hidden name="model.prcDefId"/>
	                <s:hidden name="model.orderIndex"/>
	                <s:hidden name="model.id"/>
	                <s:hidden name="actDefId"/>
	                <s:hidden name="actStepDefId"/>
	                <s:hidden name="prcDefId"/>
	                <s:hidden name="id"/>
	                <s:hidden name = "showTitle" id="showTitle"></s:hidden>
	                 </s:form> 
	     </div>
	                <div region="south" border="false" style="text-align: right; height: 50px; line-height: 30px;padding-top:5px;padding-right:15px;margin-bottom:20px;border-top:1px solid #efefef">
		                <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:doSubmit();" >
		                    保存</a> <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:cancel()">关闭</a>
		            </div>
           
</body>
</html>
