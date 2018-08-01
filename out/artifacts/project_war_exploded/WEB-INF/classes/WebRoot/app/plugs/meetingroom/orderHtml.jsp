<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<title>IWORK综合应用管理系统</title>
		<link rel="stylesheet" type="text/css" href="iwork_css/meeting/ext-all.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/meeting/formpage.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/meeting/pos.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/meeting/url-route.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/public.css" />
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/portal.css">
		<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/plugins/jquery.portal.js"></script>
		<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
		<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
		<script type="text/javascript">
	var api = art.dialog.open.api, W = api.opener;
	function save(){
		var meetingtitleValue =$("#meetingtitle").val();
		var meetingpersonsValue =$("#meetingpersons").val();
		
		if($.trim(meetingtitleValue).length == 0){
			alert("请输入会议主题");
			return;
		}
		
		if($.trim(meetingpersonsValue).length == 0){
			alert("请选择会议人数");
			return;
		}
		
		$.post("/orderSubmit.action", {
				beizhu:$("#beizhu").val(),
				endtime:$("#endtime").val(),
				jssj:$("#jssj").val(),
				meetingdate:$("#meetingdate").val(),
				meetingpersons:$("#meetingpersons").val(),
				meetingtitle:$("#meetingtitle").val(),
				selectMeetingRoom:$("#selectMeetingRoom").val(),
				starttime:$("#starttime").val(),
				dqbh:$("#dqbh").val()
			},
			function(data) {
				alert(data);
				if(data == "会议室预订成功！"){
					setTimeout("cancel();",500);
				}
			},
			"text");
    }
	
	function changeTime(){
		
		var a = $("#starttime").val().split(":");
		var b = $("#endtime").val().split(":");
		var h = Number(a[0])+Number(b[0]);
		var m = Number(a[1])+Number(b[1]);
		
		if(m==0)m="00";
		if(m==60){h++;m="00";}
		if(m==75){h++;m="15";}
		if(h>="24"){h = Number(h)-24;}
		var changeTime = h + ":" + m;
		$("#jssj").val(changeTime);
	}
	
	function cancel(){
		api.close();
	}
</script>
	</head>
	<body>
		<form id="editForm" name="editForm" onsubmit="return true;"
			action="/orderSubmit.action" method="post">
			<table width=98% align=center border=0 cellspacing=0 cellpadding=0>
				<tr align=center>
					<td align=center>
						<b>请填写会议信息：</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
				</tr>
			</table>
			<table width=98% align=center border=0 cellspacing=0 cellpadding=0
				border="2">
				<tr height="30">
					<td align=right width=20%>
						会议室：
					</td>
					<td>
						<b><s:property value="meetingRoomName" />
						</b>
						<input type="hidden" name="selectMeetingRoom" id="selectMeetingRoom" 
							value="<s:property value="meetingRoomNo"/>">
					</td>
				</tr>
				<tr height="30">
					<td align=right width=20%>
						会议主题：
					</td>
					<td>
						<input type="text" name="meetingtitle" id="meetingtitle" width="100%" size="40" />
						<span style="color: red">*</span>
					</td>
				</tr>
				<tr height="30">
					<td align=right width=20%>
						开始时间：
					</td>
					<td>
						<input type="text" onfocus="calendar();" name="meetingdate" id="meetingdate" 
							maxlength=10 size="18" value="<s:property value="currdate"/>">
						<select name="starttime" id="starttime" onchange="changeTime();">
							<s:property value="dateOption" escapeHtml="false" />
						</select>
					</td>
				</tr>
				<tr height="30">
					<td align=right width=20%>
						持续时间：
					</td>
					<td>
						<select id="endtime" name="endtime" style="width: 90px;"
							onchange="changeTime();" onBlur="changeTime();">
							<option value="01:00" selected>
								1小时
							</option>
							<option value="01:30">
								1.5小时
							</option>
							<option value="02:00">
								2小时
							</option>
							<option value="02:30">
								2.5小时
							</option>
							<option value="03:00">
								3小时
							</option>
							<option value="03:30">
								3.5小时
							</option>
							<option value="04:00">
								4小时
							</option>
							<option value="04:30">
								4.5小时
							</option>
							<option value="05:00">
								5小时
							</option>
							<option value="06:00">
								6小时
							</option>
							<option value="07:00">
								7小时
							</option>
							<option value="08:00">
								8小时
							</option>
							<option value="09:00">
								9小时
							</option>
							<option value="10:00">
								10小时
							</option>
							<option value="11:00">
								11小时
							</option>
							<option value="12:00">
								12小时
							</option>
						</select>
						&nbsp;&nbsp;&nbsp;&nbsp;到&nbsp;
						<input type="text" id="jssj" name="jssj" readonly width="100%"
							size="12" style="border: 0px; background-color: transparent;"
							value="<s:property value="jssjValue"/>" />
					</td>
				</tr>
				<tr height="30">
					<td align=right width=20%>
						会议人数：
					</td>
					<td>
						<select name="meetingpersons" id="meetingpersons" style="width: 90px">
							<option value="">
								请选择..
							</option>
							<option value="5人以下">
								5人以下
							</option>
							<option value="10人以下">
								5-10人
							</option>
							<option value="10-15人">
								11-15人
							</option>
							<option value="15-20人">
								16-20人
							</option>
							<option value="20-25人">
								21-25人
							</option>
							<option value="25-30人">
								26-30人
							</option>
							<option value="30-40人">
								31-40人
							</option>
							<option value="40-100人">
								41-100人
							</option>
							<option value="100人以上">
								100人以上
							</option>
						</select>
						<span style="color: red">*</span>
					</td>
				</tr>
				<tr>
					<td align=right width=20%>
						备注说明：
					</td>
					<td>
						<textarea rows=4 cols=38 name="beizhu" id="beizhu"></textarea>
					</td>
				</tr>
			</table>
			<br />
			<br />
			
			<table width=98% align=center border=0 cellspacing=0 cellpadding=0>
				<tr align=center>
					<td align=center>
						
						<a href="javascript:save();" icon="icon-ok" class="easyui-linkbutton l-btn" id="btnEp">
							确定
						</a>
						&nbsp;&nbsp;&nbsp;
						<a href="javascript:cancel();" icon="icon-cancel" class="easyui-linkbutton l-btn" id="btnCancel">
							取消
						</a>
					</td>
				</tr>
			</table>
		</form>
		<s:hidden name="dqbh"/>
	</body>
</html>
