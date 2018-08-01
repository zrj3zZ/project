<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/portal.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<style> 
		<!--
			#header { background:#6cf;}
			#tab_title {padding-left:5px;margin-left:5px;font-weight:bold;line-height:20px;border-bottom:1px solid #990000;color:#666 }
			#baseframe { height:320px;margin:0px;background:#FFFFFF; border:0px solid #CCCCCC;}
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
				border-bottom:1px #999999 dotted;
				vertical-align:middle;
				word-wrap:break-word;
				word-break:break-all;
				font-weight:500;
				line-height:15px;
				padding-top:5px;
			}
		-->
</style>
	<script type="text/javascript">		
	//提交
	function doSubmit(){
		var url = "sysFlowDef_actionSave.action";
		    $.post(url,$("#editForm").serialize(),function(data){
		    	 art.dialog.tips("保存成功",2);
		    });
	}
	//刷新
	function refresh(){
	    var prcDefId=$('#editForm_model3_prcDefId').val();
	    var actdefId=$('#editForm_model3_actDefId').val();
	    location.href="sysFlowDef_actionLoad.action?prcDefId="+encodeURI(prcDefId)+"&actdefId="+encodeURI(actdefId); 
	}
	</script>
</head>
<body class="easyui-layout">
    <div region="north" border="false" style="height:40px;">
		<div class="tools_nav">
			<a href="javascript:doSubmit();" class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>
			<a href="javascript:refresh();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		</div>
    </div>
	<div region="center" style="padding:5px;border:0px;border:0px;">		 
	<s:form action="sysFlowDef_actionSave.action" id="editForm" theme="simple">
		<div id="baseframe">
				  <!-- 加签设置 -->
				    <div style="float:left;width:50%;height:150px;">
				      <div id="tab_title">加签设置</div>
				      <div>
							<table>
								<tr>
										<td class = "td_title">是否允许意见审核：</td>
										<td class = "td_data">
											<s:radio list="#{'1':'是','0':'否'}" name="model1.isOpinion" theme="simple"/>
										</td>
								</tr>
								<tr>
										<td class = "td_title">是否支持讨论区：</td>
										<td class = "td_data">
											<s:radio  list="#{'1':'是','0':'否'}" name="model1.isTalk" theme="simple"/>
										</td>
								</tr>
								<tr>
										<td class = "td_title">是否允许打印：</td>
										<td class = "td_data">
											<s:radio  list="#{'1':'是','0':'否'}" name="model1.isPrint" theme="simple"/>
										</td>
								</tr>
								<tr>
										<td class = "td_title">是否允许转发：</td>
										<td class = "td_data">
											<s:radio  list="#{'1':'是','0':'否'}" name="model1.isForward" theme="simple"/>
										</td>
								</tr>
								<s:hidden name="model1.id"/>
								<s:hidden name="model1.prcDefId"/>
								<s:hidden name="model1.actDefId"/>
								<s:hidden name="model1.settingType"/>
							</table>
					</div>
				 </div>	
				<!-- 表单浏览设置 -->
				    <div style="float:left;width:50%;height:150px;">
				      <div id="tab_title">表单浏览设置</div>
				      <div>
							<table>
								<tr>
										<td class = "td_title">是否允许意见审核：</td>
										<td class = "td_data">
											<s:radio  list="#{'1':'是','0':'否'}"  name="model2.isOpinion" theme="simple"/>
										</td>
								</tr>
								<tr>
										<td class = "td_title">是否支持讨论区：</td>
										<td class = "td_data">
											<s:radio  list="#{'1':'是','0':'否'}" name="model2.isTalk" theme="simple"/>
										</td>
								</tr>
								<tr>
										<td class = "td_title">是否允许打印：</td>
										<td class = "td_data">
											<s:radio  list="#{'1':'是','0':'否'}" name="model2.isPrint" theme="simple"/>
										</td>
								</tr>
								<tr>
										<td class = "td_title">是否允许转发：</td>
										<td class = "td_data">
											<s:radio  list="#{'1':'是','0':'否'}" name="model2.isForward" theme="simple"/>
										</td>
								</tr>
								<s:hidden name="model2.id"/>
								<s:hidden name="model2.prcDefId"/>
								<s:hidden name="model2.actDefId"/>
								<s:hidden name="model2.settingType"/>
							</table>
					</div>
				 </div>	
				 <!-- 会签设置 -->
				    <div style="display:none">
				      <div id="tab_title">抢签设置</div>
				      <div>
							<table>
								<tr>
										<td class = "td_title">是否允许意见审核：</td>
										<td class = "td_data">
											<s:radio list="#{'1':'是','0':'否'}" name="model3.isOpinion" theme="simple"/>
										</td>
								</tr>
								<tr>
										<td class = "td_title">是否支持讨论区：</td>
										<td class = "td_data">
											<s:radio list="#{'1':'是','0':'否'}" name="model3.isTalk" theme="simple"/>
										</td>
								</tr>
								<tr>
										<td class = "td_title">是否允许打印：</td>
										<td class = "td_data">
											<s:radio  list="#{'1':'是','0':'否'}" name="model3.isPrint" theme="simple"/>
										</td>
								</tr>
								<tr>
										<td class = "td_title">是否允许转发：</td>
										<td class = "td_data">
											<s:radio  list="#{'1':'是','0':'否'}" name="model3.isForward" theme="simple"/>
										</td>
								</tr>
								<s:hidden name="model3.id"/>
								<s:hidden name="model3.prcDefId"/>
								<s:hidden name="model3.actDefId"/>
								<s:hidden name="model3.settingType"/>
							</table>
					</div>
				 </div>	
				 <!-- 通知传阅设置
				    <div style="float:left;width:50%;height:150px;">
				      <div id="tab_title">通知传阅设置</div>
				      <div>
							<table>
								<tr>
										<td class = "td_title">是否允许意见审核：</td>
										<td class = "td_data">
											<s:radio  list="#{'1':'是','0':'否'}" name="model4.isOpinion" theme="simple"/>
										</td>
								</tr>
								<tr>
										<td class = "td_title">是否支持讨论区：</td>
										<td class = "td_data">
											<s:radio list="#{'1':'是','0':'否'}" name="model4.isTalk" theme="simple"/>
										</td>
								</tr>
								<tr>
										<td class = "td_title">是否允许打印：</td>
										<td class = "td_data">
											<s:radio list="#{'1':'是','0':'否'}" name="model4.isPrint" theme="simple"/>
										</td>
								</tr>
								<tr>
										<td class = "td_title">是否允许转发：</td>
										<td class = "td_data">
											<s:radio list="#{'1':'是','0':'否'}" name="model4.isForward" theme="simple"/>
										</td>
								</tr>
								<s:hidden name="model4.id"/>
								<s:hidden name="model4.prcDefId"/>
								<s:hidden name="model4.actDefId"/>
								<s:hidden name="model4.settingType"/>
							</table>
					</div>
				 </div>		
			 -->					
		</div> 
		</s:form> 
    </div>
</body>
</html>
