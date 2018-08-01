<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*,java.text.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<style type="text/css">
		.gridTable td{
			line-height:25px;
		}
	</style>
	<script type="text/javascript">
	var api,W;
		try{
			api= frameElement.api;
			W = api.opener;	
		}catch(e){}
		
		
		$(document).ready(function(){
		var isWriter = $("input[name='isWriter']:checked").val();
			setStatus(isWriter);
			
		});
		function pageClose(){ 
			if(typeof(api) =="undefined"){
				window.close();
			}else{ 
				api.close(); 
			}
		}
function doSetting(){
	var pageUrl = "schCalendarDoSetting.action"; 
			$.post(pageUrl,$("#editForm").serialize(),function(data){
				var alertMsg = "";
       			if(data=='success'){
       				alertMsg =  "设置成功";
       			}
       			W.$.dialog.tips(alertMsg,2,'success.gif');
     		});
}

function showMultBook(){
	 var obj = new Object();
	 var pageurl  = "url:multibook_index.action?1=1&defaultField=purviewUser";
	 obj.dialogName = "CalendarSettingMode";
	 W.$.dialog({  
		 id:"multbook", 
		 cover:true,  
		 title:"aaa",  
		 loadingText:'正在加载中,请稍后...',
		 bgcolor:'#999',
		 width:350,
		 cache:false, 
		 lock: true,
		 top:10,
		 esc: true,
		 height:480,   
		 iconTitle:false,  
		 extendDrag:true,
		 autoSize:true,
		 content:pageurl,
		 data:obj
	 });

}

function setStatus(obj){
	if(obj==1){
		$("#puviewSel").show();
		var meettype = $("input[name='purviewType']:checked").val();
	setIndex(meettype);
	}else{
		$("#puviewSel").hide();
		$("#purviewUserTr").hide();
	}
	
}

function setIndex(obj){
	if(obj=="all"){
		$("#purviewUserTr").hide();
	}else{
			$("#purviewUserTr").show();
	}
}
	</script>
</head>
<body  class="easyui-layout">
		<div region="north" border="false" style="padding:0px;overflow:no;scrolling:no;border-bottom:1px;">
			<div class="tools_nav"> 
				<table width="100%"  border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td> 
							<a href="javascript:doSetting();" class="easyui-linkbutton" plain="true" iconCls="icon-save">设置</a>
								<a href="javascript:pageClose();" class="easyui-linkbutton" plain="true" iconCls="icon-cancel">关闭</a>
						</td>
					</tr>
				</table> 
		 </div>
		</div>
		<div region="center" style="width:300px;height:100px;padding:3px;border:0px;padding-left:10px;padding-top:10px;padding-right:10px;">
            	<s:form id ="editForm" name="editForm" action="saveSchCalendar"  theme="simple">
	            	<table border="0"  class="gridTable"  cellspacing="0" cellpadding="0" width="100%">
	            		<tr>
	            			<td class="td_title" width="17%">:默认视图</td>
	            			<td class="td_data" width="83%" > 
	            				<s:radio name="defaultView" listKey="key" listValue="value" list="#{'month':'月视图','agendaWeek':'周视图','agendaDay':'日视图'}"></s:radio>
	            			</td>
	            		</tr>
	            		<tr> 
	            			<td class="td_title" width="17%">是否共享日程</td>
	            			<td class="td_data" width="83%" > 
	            				<s:radio name="isShare" listKey="key" listValue="value" list="#{'1':'是','0':'否'}"></s:radio>
	            			</td>
	            		</tr>
	            		<tr> 
	            			<td class="td_title" width="17%">是否允许他人<br>定制我的日程</td>
	            			<td class="td_data" width="83%" > 
	            				<s:radio name="isWriter" listKey="key"   onclick="setStatus(this.value)"  listValue="value" list="#{'1':'允许','0':'禁止'}"></s:radio>
	            			</td>
	            		</tr>
	            		<!-- 
	            		<tr> 
	            			<td class="td_title" width="17%">提醒方式</td>
	            			<td class="td_data" width="83%" >
	            				系统消息：<s:radio value="sendSysmsg" list="#{'1':'开启','0':'关闭'}" name="sendSysmsg" theme="simple"/><br/>
											邮　　件：<s:radio value="sendEmail" list="#{'1':'开启','0':'关闭'}" name="sendEmail" theme="simple"/><br/>
											短　　信：<s:radio value="sendSms" list="#{'1':'开启','0':'关闭'}" name="sendSms" theme="simple"/><br/>
	            			</td>
	            		</tr>
	            		-->
	            		<tr id="puviewSel">
	            			<td  class="td_title" width="17%">权限</td> 
	            			<td class="td_data" width="83%" > 
	            				<s:radio name="purviewType"  listKey="key"   onclick="setIndex(this.value)" listValue="value" list="#{'all':'全员可访问','user':'指定用户可访问'}"></s:radio>
	            			</td>
	            		</tr>
	            		<tr id="purviewUserTr">
	            			<td class="td_title" width="17%"></td>
	            			<td class="td_data" width="83%" > 
	            				<s:textarea name="purviewUser" id="purviewUser"  cssStyle="width:260px;height:60px;"></s:textarea><a href="javascript:showMultBook();" class="easyui-linkbutton" plain="true" iconCls="icon-multibook"></a>
	            			</td>
	            		</tr> 
	            		
	            	</table>
	          </s:form> 
	     </div>
</body>
</html>