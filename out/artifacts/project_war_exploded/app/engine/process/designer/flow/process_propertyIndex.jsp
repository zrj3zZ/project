<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title> 
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/portal.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<style> 
		<!--
			#header { background:#6cf;}
			.tab_title {padding-left:5px;font-weight:bold;line-height:20px;border-bottom:1px solid #990000;color:#666 }
			#baseframe { margin:0px;background:#FFFFFF; border:0px solid #CCCCCC;}
			#baseinfo {background:#FFFFFF; padding:5px;font:12px; font-family:宋体;}
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
			/*数据字段标题样式*/
			.td_title {
			color:#004080;
				line-height: 23px;
				font-size: 12px;
				height:15px;
				text-align: right;
				letter-spacing: 0.1em;
				padding-right:10px;
				padding-left:10px;
				white-space:nowrap;
				vertical-align:middle;
				font-family:"宋体";
			}
			/*数据字段内容样式*/
			.td_data {
				color:#0000FF;
				line-height: 23px;
				text-align: left;
				padding-left: 3px;
				font-size: 12px;
				font-family:"宋体";
				border-bottom:1px #efefef dotted;
				vertical-align:middle;
				word-wrap:break-word;
				word-break:break-all;
				font-weight:500;
				line-height:15px;
				padding-top:5px;
			}
			.td_memo{
				color:#999;
				line-height: 23px; 
				text-align: left;
				padding-left: 10px;
				font-size: 10px; 
				vertical-align:bottom;
				line-height:15px;
				font-family:"微软雅黑";  
				font-weight:100;
			}
		-->
</style>
	<script type="text/javascript">
	$(function(){
		selectNoticeType();
	});
		//提交保存
		function saveSubmit(){
			var url = "sysFlowDef_PropertyIndex!save.action";
		    $.post(url,$("#editForm").serialize(),function(data){
		    	 art.dialog.tips("保存成功",2);
		    });
		}
		function multi_book(defaultField) {
			var code = document.getElementById(defaultField).value;	
			var pageUrl = "multibook_index.action?input="+encodeURI(code)+"&defaultField="+defaultField;
			 art.dialog.open(pageUrl,{
					id:"selectUserParam",
					title: '多选地址簿',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width: 350,
					height: 550
				 });
			}
		function selectNoticeType(){
			var v = $('input:radio[name="model.noticeType"]:checked').val();
			if(v=="2"){
				$("#noticeAddressTr").show(); 
			}else{
				$("#noticeAddressTr").hide(); 
			}
		}
		
	</script>
</head>
<body class="easyui-layout">
	<div region="north" border="false" style="height:40px;">
		<div class="tools_nav">
				<a href="javascript:saveSubmit();" class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>
				<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>		
		</div>
	</div>
	<div region="center" style="padding:0px;border:0px;">
			 <s:form name="editForm" id="editForm" action="sysFlowDef_PropertyIndex!save.action"  theme="simple">
			 <div  class="tab_title"  style="display:none">模型说明</div>
					<div>
							<table>
							<tr>
										<td class = "td_title">流程任务标题默认设置:</td>
										<td class = "td_data"><s:textfield cssStyle="width:373px" name="model.defTitle"></s:textfield>
										<div class="td_memo">注:系统参数流程标题 “&#36;&#123;PROCESS_NAME&#125;”,单据编号“&#36;&#123;FORM_NO&#125;”,发起人“&#36;&#123;OWNER&#125;”,创建时间“&#36;&#123;CREATEDATE&#125;”<BR/>显示表单内容请使用%表单域名称%，例如：%APPNAME%,不设置则为系统默认标题</div>
										</td>
								</tr>
								<tr style="display:none">
										<td class = "td_title">描述</td>
										<td class = "td_data">
											<s:textarea name ="model.defMemo"   theme="simple" cols="44" rows="5">
											</s:textarea>
										</td>
								</tr>
								
							</table>
					</div>
				<div  class="tab_title">应用设置</div>
				<div>
							<table>
								<tr>
										<td class = "td_title">是否在发起中心显示:</td>
										<td class = "td_data">
											<s:radio  onclick="selectisCreate()" value="model.isCcenter" list="#{'1':'是','0':'否'}" name="model.isCcenter" theme="simple"/>
										</td>
										</tr>
										<tr> 
								<td></td>
										<td class = "td_memo" >注：当系统包含内置流程(被其他系统或功能调用的流程)时，选择“否”，则该流程将不在发起中心显示</td>
								</tr>
								<tr>
										<td class = "td_title">发起人取消任务设置:</td>
										<td class = "td_data">  
											<s:radio value="model.taskCancelType" list="#{'DEL':'允许删除','CLOSE':'允许关闭任务'}" name="model.taskCancelType" theme="simple"/>
										</td>
										</tr>
								<tr>
								<td></td> 
										<td class = "td_memo">注：任务起草或被驳回至发起人时，发起人可进行的操作  “关闭任务”：数据保留，“允许删除”则将任务及数据从系统中彻底清除</td>
								</tr> 
								
								<tr>
										<td class = "td_title">是否允许发起人撤销:</td>
										<td class = "td_data">
											<s:radio value="model.isCreaterCancel" list="#{'1':'是','0':'否'}" name="model.isCreaterCancel" theme="simple"/>
										</td>
										</tr>
								<tr>
								<td></td>
										<td class = "td_memo">注：开启此功能，未归档流程，流程发起人可在直接撤销单据，收回至发起人待办</td>
								</tr>
								
								<tr>
										<td class = "td_title">是否支持讨论区:</td>
										<td class = "td_data">
											<s:radio value="model.isTalk" list="#{'1':'是','0':'否'}" name="model.isTalk" theme="simple"/>
										</td>
										</tr>
								<tr>
								<td></td>
										<td class = "td_memo">注：表单中的讨论区功能，可添加讨论群组，讨论结果可作为单据的一部分进行存档</td>
								</tr>
								
								<tr>
										<td class = "td_title">是否开启绩效监控:</td>
										<td class = "td_data">
											<s:radio value="model.isMonitor" list="#{'1':'是','0':'否'}" name="model.isMonitor" theme="simple"/>
										</td>
										</tr>
								<tr>
								<td></td>
										<td class = "td_memo">注：开启绩效监控后，系统将定时获取系统中超出流程合理时间的任务，进行绩效监控</td>
								</tr>
								
								
							</table>
					</div> 
					<div  class="tab_title" >归档设置</div>
					<div style="height:60px;">
							<table width="100%">
								<tr>
										<td class = "td_title" style="width:120px">归档通知:</td>
										<td class = "td_data"> 
											<s:radio value="model.noticeType"  list="#{'0':'关闭','1':'通知发起人','2':'通知指定用户'}" onclick="selectNoticeType()" name="model.noticeType" theme="simple"/>
										</td> 
								</tr> 
								<tr id="noticeAddressTr" style="display:none">
										<td class = "td_title">归档通知人:</td>
										<td class = "td_data">
											<s:textfield name="model.archiveNotice" readonly="true" cssStyle="width:500px;margin-right:5px;color:#0000ff"></s:textfield><a href="###" onclick="multi_book('editForm_model_archiveNotice');" title="多选地址薄" class="easyui-linkbutton" plain="true" iconCls="icon-multibook"></a>
										</td> 
								</tr> 
								
							</table>
							</div>
					<div  class="tab_title" style="display:none">引擎设置</div>
					<div style="display:none">
							<table>
								<tr>
										<td class = "td_title">引擎类型:</td>
										<td class = "td_data">
											<s:radio value="model.engineType" disabled="true" list="#{'0':'普通流程','1':'自由流程','2':'动态表单流程'}" onclick="selectEngineType(this.value)" name="model.engineType" theme="simple"/>
										</td> 
								</tr> 
									<tr id = "tr_FormScope" style="display:none">
										<td class = "td_title">动态表单范围</td>
										<td class = "td_data">
										<s:radio name="sirnScope" value="0" list="#{'0':'全部','1':'自定义'}" onclick="selectFormScope(this.value)"  theme="simple"/>
											<s:textfield name="model.formList" theme="simple"></s:textfield>
										</td>
								</tr> 
								<tr>
										<td class = "td_title">访问类型:</td>
										<td class = "td_data">
											<s:radio value="model.visitType"  disabled="true" list="#{'1':'系统验证用户发起','0':'允许外部用户发起'}" name="model.visitType" theme="simple"/>
										</td>
								</tr>
							</table>
							</div>
					<div  class="tab_title">绩效设置</div>
					<div>
							<table>
								<tr>
										<td class = "td_title">合理流程时间设置:</td>
										<td class = "td_data">
											<s:textfield name="model.standardTime" theme="simple"></s:textfield>小时(工作日)
										</td>
								</tr>
								<tr>
										<td class = "td_title">流程办理预警时间设置:</td>
										<td class = "td_data">
											<s:textfield name="model.warningTime" theme="simple"></s:textfield>小时(工作日)
										</td>
								</tr>
							
							</table>
							<s:hidden name="model.prcDefId"></s:hidden>
							<s:hidden name="model.actDefId"></s:hidden>
							<s:hidden name="model.id"></s:hidden>
							<s:hidden name="pageindex"></s:hidden>
							<s:hidden name="model.prcAbstract"></s:hidden>
							<s:hidden name="model.prcHelp"></s:hidden>
					</div>
					</s:form>
	</div>
</body>
</html>
