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
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/portal.css">
		<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>	
		<script type="text/javascript" src="iwork_js/jqueryjs/plugins/jquery.portal.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" ></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery-ui-1.8.24.custom.min.js" ></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.scrollfollow.js" ></script>
		<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
		<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
			
		<script type="text/javascript">
	
	function openDialog(pageUrl){
	art.dialog.open(pageUrl,{
					 id:'rebackWinDiv',  
					title:'预订会议室',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:520,
				    height:400,
				    close:function(){
				    	window.location.reload(true);
				    }
				 });
	}
	
	function getOneRoom(j,date,assemblyInfo){
		var selectMeetingZone = $("#Select_MeetingZone").val();
		var pageUrl = "meetingRoomTimeOrder.action?j=" + j + "&date=" + date + "&assemblyno=" + assemblyInfo + "&dqbh=" + selectMeetingZone;
		openDialog(pageUrl);
	}
	
	function openUpdateOrderHtml(orderId,currdate){
		
		var selectMeetingZone = $("#Select_MeetingZone").val();
		$.post("/checkUser.action", {
				orderId:orderId,
				currdate:currdate
			},
			function(data) {
				if(data == "ok"){
					var pageUrl = "meetingRoomOrderOpen.action?orderId=" + orderId + "&currdate=" + currdate + "&dqbh=" + selectMeetingZone;;
					openDialog(pageUrl)
				}else{
					alert(data);
				}
			},
			"text");
	}
	
	function selectMeetingRoomOrder(beforORafter){
		
		var selectMeetingZone = $("#Select_MeetingZone").val();
		var selectDate = $("#SelectDate").val();
		location.href = "./meetingRoomOrderList.action?dqbh=" + selectMeetingZone + "&date=" + selectDate + "&beforORafter=" + beforORafter;
		
	}

	function marginMeetingRoomHead(){
		var bodyWidth = $(document.body).width();
		var marginValue = 0;
		if(bodyWidth < 925){
			marginValue = -(bodyWidth/2 - 9);
		}else{
			marginValue = -320;
		}
		$("#meetingRoomHead").css("margin-left", marginValue + "px");
	}
	
	$(document).ready(function(){
		//$("#meetingRoomHead").scrollFollow();
		//marginMeetingRoomHead();
		//$("#meetingRoomHead").css("top","43px");
	});
	
	$(window).resize(function() {
		marginMeetingRoomHead();
	});

	
	</script>
	<style type="text/css">
	
	.meetingRoomHead{
		position: absolute; 
		top: 42px; 
		width: 925px;
		left: 50%;
		margin-left: -463px;
		margin-left: -462px;\9;  /*all ie*/

	}
	
	.fixedHeaderTr {
		top: expression(this.offsetParent.scrollTop);
	}
	
	.mainDiv {
		overflow: auto;
		scrollbar-face-color: 9999ff;
	}
	
	.tocssDiv {
		border: 1ps #999999 solid;
		overflow: auto;
		scrollbar-face-color: 9999ff;
		height: expression((   document.body.clientHeight-this.offsetTop-40 > 
			 this.children [   0 ].   offsetHeight) ?(   this.children [   0 ]. 
			 offsetHeight +40 ) :       ( 
			 document.body.clientHeight-this.offsetTop-40 ) );
		border-top: 1px solid #666666;
	}
	
	.topDiv {
		height: expression((   document.body.clientHeight-this.offsetTop-60 > 
			 this.children [   0 ].   offsetHeight) ?(   this.children [   0 ]. 
			 offsetHeight +40 ) :       ( 
			 document.body.clientHeight-this.offsetTop-40 ) );
	}
	
	body {
		line-height: 20px;
		font-size: 12px;
	}
	
	img {
		border: 0;
	}
	
	.bg1 {
		width: 62;
		colspan: 4;
		align: center;
	}
	
	.bg {
		width: 63;
		colspan: 4;
		align: center;
	}
	
	.bg2 {
		display: inline;
		width: 15;
		height: 19;
		align: center;
		valign: top;
		background-color: #CCCCCC;
		border-right: 1px solid #CCCCCC;
		float: left;
	}
	
	.input {
		border: 1px solid #ACBCC9;
		width: 100px;
	}
	
	.s1 {
		background: #FFFFFF url(../iwork_img/xxx.gif) no-repeat center;
		border: 0px;
		width: 15px;
		height: 19px;
	}
	
	.s2 {
		background: #F7F8FA url(../iwork_img/xyl.gif) no-repeat center;
		border: 0px;
		width: 15px;
		height: 19px;
	}
	
	.s3 {
		background: #FFFFFF;
		border-left: #FFF 1px solid;
		border-top: #FFF 1px solid;
		border-bottom: #FFF 1px solid;
		border-right: #FFF 1px solid;
		width: 15px;
		height: 19px;
	}
	
	.s4 {
		background: #F7F8FA;
		border-left: #FFF 1px solid;
		border-top: #FFF 1px solid;
		border-bottom: #F7F8FA 1px solid;
		border-right: #F7F8FA 1px solid;
		width: 15px;
		height: 19px;
	}
	
	.STYLE1 {
		font-size: 12px;
	}
	
	a:link,a:visited {
		color: #484347;
		text-decoration: none;
	}
	
	a:hover {
		color: #777176;
		text-decoration: none;
	}
	
	.dragTable {
		font-size: 12px;
		border: 1px solid #DCE0E1;
		background: url(../iwork_img/lft_tit_bg.jpg) repeat-x
			top left;
		margin: 2px;
		padding-top: 10px;
		padding-bottom: 5px;
		width: 925;
		text-align: center;
	}
	
	.main_sub {
		font-size: 12px;
		width: 70px;
		height: 21px;
		line-height: 21px;
		text-align: center;
		border: none;
		background: url(../iwork_img/baisearch.gif) no-repeat;
	}
	
	.main_sub2 {
		font-size: 12px;
		width: 108px;
		height: 21px;
		line-height: 21px;
		text-align: center;
		border: none;
		background: url(../iwork_img/baibutton.jpg) no-repeat;
	}
	
	.myTaskTitle1{
		font-size:12px;
		font-weight:bold;
	   color:#003366;
	   text-align:center;
		padding-left:3px;
	   padding-right:3px; 
	   padding-top:1px;
	   padding-bottom:1px;
	   height:20;
	   white-space:nowrap;
	   border-right:1px #CCCCCC solid;
	   border-bottom:1px #CCCCCC solid;
	   border-left:1px #CCCCCC solid;
	   background-image:url(../aws_skins/_def2011/images/lft_tit_bg1.jpg);
		background-repeat:repeat-x;
		background-position:top left;
		
	}
	.myTaskTitle{
		font-size:12px;
		font-weight:bold;
	   color:#003366;
	   text-align:left;
		padding-left:3px;
	   padding-right:3px; 
	   padding-top:1px;
	   padding-bottom:1px;
	   height:20;
	   white-space:nowrap;
	   border-right:1px #CCCCCC solid;
	   border-bottom:1px #CCCCCC solid;
	   background-image:url(../aws_skins/_def2011/images/lft_tit_bg1.jpg);
		background-repeat:repeat-x;
		background-position:top left;
		
	}
	
	</style>
	
	</head>
	<body style="min-width: 820px;">
		<form action="./login.wf" method=post name=frmMain>
			<center>
				<div class='dragTable' id="test" style="width:<s:property value='tableWidth' />px">
					<table border="0" width='<s:property value='tableWidth' />px' align="center" cellpadding="0"
						cellspacing="0" >
						<tr>
							<td>
								&nbsp;&nbsp;&nbsp;
									<s:property value='meetingSelectZone' escapeHtml="false"/>
								&nbsp;&nbsp;&nbsp;
									<a href="###" onclick="selectMeetingRoomOrder('befor')"><img width="15" height="15" src="../iwork_img/go_back.gif"></a>
										<input type="text" id="SelectDate" name="SelectDate" 
										onfocus="WdatePicker({dateFmt:'yyyy-MM-dd DD'})" class="Wdate" 
										value="<s:property value='meetingSelectDate' escapeHtml='false'/>"/>
									<a href="###" onclick="selectMeetingRoomOrder('after')"><img width="15" height="15" src="../iwork_img/go_forward.gif"></a>
								&nbsp;&nbsp;&nbsp;
									<input type="button" name="selectbutton" class="main_sub"
										value="查询"
										onClick="selectMeetingRoomOrder('')">
							</td>
							<td align='right'>
								<img src=../iwork_img/bell.gif border=0
									style='position: relative; top: 1px;'>
								<font color='brown'>&nbsp;提示：预订人双击预订区可修改或取消预订&nbsp;&nbsp;&nbsp;</font>
							</td>
						</tr>
					</table>
				</div>
			</center>
			
			<table width="<s:property value='tableWidth' />px" align="center" cellpadding="0" cellspacing="0" 
				bordercolor="#CCCCCC" border="1" style="border-collapse: collapse">
				<tr id="meetingRoomHead">
					<td align="center" class="myTaskTitle1" style="width:150px">
						会议室名称
					</td>
					<s:property value='meetingTimeView' escapeHtml="false"/>
				</tr>
				<!-- 
				<tr style="border: 0;">
					<td width="640px" height="19" colspan="29" style="border: 0;"></td>
				</tr>
				-->
				<s:property value='mettingView' escapeHtml="false"/>
			</table>
			<div class="mainDiv" id="mytabletop">
			</div>
		</form>
	</body>
</html>
