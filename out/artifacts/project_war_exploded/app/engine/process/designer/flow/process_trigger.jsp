<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/commons.js"></script>  
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<style> 
		<!--
			#header { background:#6cf;}
			#title { height:20px; border-bottom:1px solid #cdcdcd; font-size:12px; font-family:宋体; padding-left:10px; padding-top:5px;vertical-align:top}
			#baseframe { margin:0px;background:#FFFFFF; border:0px;}
			.toobar{ 
				 border-bottom:1px solid #990000; 
				 padding-bottom:5px; 
			}
			/*只读数据样式*/
			.readonly_data {
				vertical-align:bottom;
				font-size: 12px;
				line-height: 20px;
				color: #888888;
				padding-right:10px;
				border-bottom:1px #999999 dotted;
				font-family:"宋体";
				line-height:15px;
			}
			fieldset { 
				padding:10px; 
				margin:3px; 
				border:1px solid #cdcdcd; 
				background:#fff; 
				} 
				
				fieldset legend { 
				color:#1E7ACE; 
				font-weight:bold; 
				padding:3px 20px 3px 20px; 
				border:0px solid #1E7ACE; 
				background:#fff; 
				} 
				fieldset div { 
				clear:left; 
				margin-bottom:2px; 
			    } 
		--> 
</style>
<script type="text/javascript">
		function addItem(){
			var list= $('input:radio[name="model.eventType"]:checked').val();
            if(list==null){
               art.dialog.tips("请选择您要注册的事件类型",2);
                return false;
            }
		    var eventParam=$.trim($("#editForm_model_eventParam").val());
		    $("#editForm_model_eventParam").val(eventParam);
		     var eventMemo=$.trim($("#editForm_model_eventMemo").val());
		    $("#editForm_model_eventMemo").val(eventMemo);
			if(eventParam==''){
				art.dialog.tips("请选择您要注册的事件类",2);
				$("#editForm_model_eventParam").focus();
				return false;
			}
			if(length2(eventParam)>500){
				art.dialog.tips("事件类过长",2);
				$("#editForm_model_eventParam").focus();
				return false;
			}
			if(length2(eventMemo)>500){
				art.dialog.tips("事件描述过长",2);
				$("#editForm_model_eventMemo").focus();
				return false;
			}
			$('#editForm').attr('action','sysFlowDef_trigger!save.action');
			$("#editForm").submit();
		}
		function deleteItem(id){
			art.dialog.confirm('你确定要删除流程触发器设置吗？', function(){
			    $("#editForm_model_id").val(id);
               	$('#editForm').attr('action','sysFlowDef_trigger!delete.action');
               	$('#editForm').submit();
			}, function(){
			
			});
		}
	</script>
</head>
<body class="easyui-layout">
<div region="north" border="false" style="height:40px;">
		<div class="tools_nav">
			<a href="javascript:addItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加</a>
			 <a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		</div>
	</div>
	<div region="center" style="padding:5px;border:0px;">
		       <s:form  theme="simple" id="editForm" action="sysFlowDef_trigger!save.action">
		 			<fieldset>
			                <legend>触发器设置:</legend>
			                <table width="100%" border="0" cellspacing="0" cellpadding="5">
							  <tr>
							   <td  width="100">触发器类型:</td>
							    <td width="600">							        
							        <s:radio  name="model.trigerType" list="#{'0':'JAVA触发器'}" />	
							        <input type="radio" name="model.trigerType" id="editForm_model_trigerType1" disabled value="1"/><label for="editForm_model_trigerType1">WebService触发器</label>
							        <span style="color:red;">*</span>						            					                
								</td>
							  </tr>
							  <tr>
							   <td  width="100">&nbsp;&nbsp;事件触发类型:</td>
							    <td width="600">
							        <s:radio  name="model.eventType" list="#{'processAddSignFinish':'加签完毕时触发','processForward':'任务转发时触发','beforeProgressDelete':'任务[删除/取消]时触发','beforeProgressReturn':'任务归档时触发','processRebacBefore':'任务撤销时触发'}" />	
								    <span style="color:red;">*</span>
								</td>  
							  </tr>
							  <tr>
							    <td>&nbsp;&nbsp;事  件  类:</td>
							    <td><s:textfield name="model.eventParam" cssStyle="width:500px;"/><span style="color:red;">*</span></td>
							    </tr>
							     <tr> 
							    <td>&nbsp;&nbsp;</td>
							    <td style="color:#ccc">注：当触发器类别为“流程表单数据过滤触发器” 时，触发器类将实现FormDataFileterInterFace</td>
							    </tr>
							  <tr> 
							    <td>&nbsp;&nbsp;业务描述:</td>
							    <td><s:textarea name="model.eventMemo" cssStyle="width:300px;height:45px;"/></td>
							    </tr>
							   
							</table>
			                <s:hidden name="model.prcDefId"></s:hidden>
							<s:hidden name="model.actDefId"></s:hidden>
							<s:hidden name="model.id"></s:hidden>
			        </fieldset>
			   </s:form>     
			        <div id="title">注册事件列表</div>
			        <table width="100%">
			          	<s:iterator  value="list" status="status">
			        	<tr style="border-bottom:1px solid #efefef">
			        		<td width="100"><img src="iwork_img/javalogo.gif" style="border:1px solid #efefef;padding:3px;margin:5px;height:60px;"></td>
			        		<td valign = "top" >
			        			<table width="100%">
			        				<tr>
			        					<td width="100"><img src="iwork_img/page_lightning.png" style="border:0px solid #efefef;margin-right:3px;">事件类型:</td>
			        					<td>
			        					     <s:if test="eventType=='processAddSignFinish'">
			        					            任务加签完毕时触发
			        					     </s:if>
			        					     <s:elseif test="eventType=='processForward'">
			        					            任务转发时触发
			        					     </s:elseif>
			        					     <s:elseif test="eventType=='beforeProgressDelete'">
			        					           任务删除时触发
			        					     </s:elseif>
			        					    
			        					     <s:elseif test="eventType=='beforeProgressReturn'">
			        					        流程归档时触发
			        					     </s:elseif>
			        					    <s:elseif test="eventType=='processRebacBefore'">
			        					        任务撤销时触发
			        					     </s:elseif>
			        					</td>
			        				</tr>
			        				<tr>
			        					<td><img src="iwork_img/page_white_cup.png" style="border:0px solid #efefef;margin-right:3px;">事 件 类 :</td>
			        					<td><s:property value="eventParam"/></td>
			        				</tr>
			        				<tr>
			        					<td><img src="iwork_img/page_white_text.png" style="border:0px solid #efefef;margin-right:3px;">业务描述:</td>
			        					<td><s:property value="eventMemo"/></td>
			        				</tr>
			        			</table>
			        		</td>
			        		<td valign="top"><a href="javascript:deleteItem(<s:property value='id'/>)"><img src="iwork_img/close.gif" style="border:0px solid #efefef;margin-right:3px;margin-top:3px;">卸载</a></td>
			        	</tr>
			       </s:iterator>
			        </table>
					 
	</div>
</body>
</html>
