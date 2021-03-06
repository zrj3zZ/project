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
		.clickMore{
			height:30px;
			line-height:30px;
		}
		TR TH{
			text-align:left;
		}
	</style>

	<script type="text/javascript">
	
		function detail(instanceid){
			var pageUrl = "loadVisitPage.action?formid=211&demId=104&instanceId="+instanceid;
			art.dialog.open(pageUrl,{ 
				title:'查看基金',
				loadingText:'正在加载中,请稍后...',
				bgcolor:'#999',
				rang:true,
				width:1100,
				cache:false,
				lock: true,
				height:580, 
				iconTitle:false,
				extendDrag:true,
				autoSize:false,
				content:pageUrl
			});
		}
		
		function clickMore(){
			var title="临时公告列表";
			var icon="icon-model";
			var url="sysDem_DataList.action?demUUID=<s:property value="TEMP_NOTICE_LIST_demUUID"/>&COMPANYNO=<s:property value="companyno"/>&init_params=COMPANYNO=<s:property value="companyno"/>";
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
  
  <body>
    <div border="false"  align="center" class="grid">
    	<table WIDTH="100%" style="border:1px solid #efefef">
			<TR  class="header" align="left">
				<TH style="width:10%;text-align: center;">序号</TH>	
				<TH style="width:20%;">公告编号</TH>
				<TH style="width:20%;">公告名称</TH>
				<TH style="width:20%;">公告日期</TH>
				<TH style="width:30%;">隶属会议名称</TH>
			</TR>
			<s:iterator value="tempNoticeList"  status="status">
			<TR class="cell"  align="left">
				<TD style="text-align:center"><s:property value="#status.count"/></TD>
				<TD><s:property value="NOTICENO"/></TD>
				<TD><s:property value="NOTICENAME"/></TD>
				<TD><s:property value="NOTICEDATE"/></TD>
				<TD><s:property value="MEETNAME"/></TD>
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
  </body>
</html>
