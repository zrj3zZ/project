<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/public.css"/>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style type="text/css"> 
		.header{
			height:25px;
			line-height: 25px;
			background-color:#f2f2f2;
		}
		.cell{
			padding:10px;
			height:25px;
			line-height: 25px;
		}
		.cell td{
			vertical-align:top;
		}
		TR TH{
			text-align:left;
		}
		.clickMore{
			height:30px;
			line-height:30px;
		}
	</style>

	<script type="text/javascript">
		function detail(instanceid){
			var pageUrl = "loadVisitPage.action?formid=182&demId=88&instanceId="+instanceid;
			art.dialog.open(pageUrl,{
				title:'查看会议',
				loadingText:'正在加载中,请稍后...',
				bgcolor:'#999',
				rang:true,
				width:1100,
				cache:false,
				lock: true,
				height:580, 
				iconTitle:false,
				extendDrag:true,
			});
		}
		
		function hyzl(instanceid,meetid,meettype){
			var pageUrl = "hna_sanhui_meeting_getFileInfo.action?meetId="+meetid;
			$.post(pageUrl,{id:meetid,meettype:meettype,instanceId:instanceid},function(data){
				if(data.trim()== ""){
					// 数据库没有记录
					alert("该会议暂无会议资料！");
				}else{
					// 数据库有对应记录
					//var url = "loadVisitPage.action?formid=235&demId=117&instanceId="+data.trim();
					var url = "loadVisitPage.action?formid=235&demId=117&instanceId="+data.trim();
					<%--art.dialog.open(url,{
						title:'查看会议资料',
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true,
						width:1100,
						cache:false,
						lock: true,
						height:580, 
						iconTitle:false,
						extendDrag:true,
						autoSize:false
					});--%>
					window.top.addTab("查看会议资料",url,"icon-user-group");
				}
			});
			
		}
		
		function clickMore(){
			var title="会议管理";
			var icon="icon-user-group";
			var url="hna_sanhui_meeting.action";
			//window.praent.addTab(title,url,icon);
			window.top.addTab(title,url,icon);
		}
	
		$(function(){
			var tr=document.getElementsByTagName("tr");
			for(var i=0;i<tr.length;i++)
			{
				tr[i].style.background=tr[i].rowIndex % 2==0?"#F9F9F9":"white";
			}
		});
	</script>	
  </head>
  
  <body class="bg">
    <div region="center" border="false"  align="center" style="border:0px solid #C0C0C0;text-align:left;padding:5px;">
    <div class="grid">
    	<table WIDTH="100%" style="border:1px solid #efefef">
			<TR  class="header" align="left">
				<TH style="width:15%;">公司名称</TH>
				<TH style="width:20%;">会议名称</TH>
				<TH style="width:15%;">议案</TH>
				<TH style="width:15%;">召开时间</TH>
				<TH style="width:15%;">会议资料</TH>
			</TR>
			<s:iterator value="meetList"  status="status">
			<TR class="cell" align="left">
				<TD><s:property value="companyname"/></TD>
				<TD><a href="javascript:void(0)" onclick="detail('<s:property value="instanceid"/>')"><s:property value="meetname"/></a></TD>
				<TD title="<s:property value='hyyaAll'/>"><s:property value="hyya"/></TD>
				<TD><s:property value="meettime"/></TD>
				<TD><a href="javascript:void(0)" onclick="hyzl('<s:property value="instanceid"/>','<s:property value="id"/>','<s:property value="meettype"/>')">查看会议资料</a></TD>
			</TR>
			</s:iterator>
			<TR class="clickMore">
				<TD></TD>
				<TD></TD>
				<TD></TD>
				<TD></TD>
				<TD><a href="javascript:void(0)" onclick="clickMore()">>>更多...</a></TD>
			</TR>
		</table>
    </div>
    </div>
  </body>
</html>
