<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head><title>IWORK综合应用管理系统</title>
<link rel="stylesheet" type="text/css" href="iwork_css/system/syspersion_loadmsremindpage.css">
</head>	
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link href="iwork_css/reset.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
<script type="text/javascript" src="iwork_js/system/syspersion_loadmsremindpage.js" ></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<body>
<s:form id ="editForm" name="editForm" action="syspersion_saveMsRemindPage"  theme="simple">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" >
	<tr>
      <td > 
		  		<div  class="tools_nav">
			  		<a id="btnEp" class="easyui-linkbutton" plain="true" icon="icon-save" href="javascript:doSubmit();" >保存</a>
			  		<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		  		</div>
	  </td>
    </tr>
    <tr>
      <td align="center" valign="top" height="100%" >
		<div id="editInfo" style="width:80%;">
			<table width="100%">
			<tr>
				<td>
					<table style="padding:10px;">
	    			<tr>
	    				<td style="padding:5px;font-size:14px;">系统通知/提醒类别设置:</td>
	    				<td  style="color:#0000ff;padding:5px;font-size:14px;"><s:radio name="remindType" list="#{0:'即时提醒',1:'定时提醒' }"></s:radio></td>
	    			</tr>
	    		</table>
				</td>
			</tr>
			<tr>
				<td> <br/><br/>
	    		<fieldset  style="padding:10px;background-color:#FFFFEE;color:#666;line-hieght:20px;border:1px solid #efefef">
	    			<legend>说明</legend>
	    			 1、选择“即时提醒”时，所有通知或邮件信息即时接收。<br/><br/>
	    			 2、选择“定时提醒”时,所有通知或邮件信息，每两小时向您推送新增的任务提醒,如果在流程办理过程中设置优先级为“紧急”时，则发送即时提醒邮件
	    		</fieldset>
				</td>
			</tr>
			<!-- 
				<tr>
					<td class="choose"><s:checkbox id="isMailRemind" name="isMailRemind" ></s:checkbox>是否接收邮件提醒</td>
				</tr>
				<tr>
					<td class="choose"><s:checkbox id="isIMRemind" name="isIMRemind" ></s:checkbox>是否接收IM提醒</td>
				</tr>
				<tr>
					<td class="choose"><s:checkbox id="isSMSRemind" name="isSMSRemind" ></s:checkbox>是否接收短信提醒</td>
				</tr>
				<tr>
					<td class="choose"><s:checkbox id="isSYSRemind" name="isSYSRemind" ></s:checkbox>是否接收系统消息提醒</td>
				</tr>
				<tr>
					<td>
						<div style="display:none">
							<s:textfield name="configID_1"/> 
							<s:textfield name="configID_2"/> 
							<s:textfield name="configID_3"/> 
							<s:textfield name="configID_4"/> 
						</div>
					</td>
				</tr>
				 -->
			</table>
		</div>
     </td>
    </tr>
	</table>
</s:form>
</body>	
</html>
