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
			var pageUrl = "loadVisitPage.action?formid=227&demId=115&instanceId="+instanceid;
			art.dialog.open(pageUrl,{ 
				title:'查看项目',
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
		
		function jinZhanDetail(xmbh){
			var pageUrl = "hna_historyReport_getList.action?projectno="+xmbh;
			art.dialog.open(pageUrl,{ 
				title:'进展反馈',
				loadingText:'正在加载中,请稍后...',
				bgcolor:'#999',
				rang:true,
				width:1100,
				zIndex:1005,
				cache:false,
				lock: true,
				height:580, 
				iconTitle:false,
				extendDrag:true,
				autoSize:false,
				content:pageUrl
			});
		}
		
		function piShi(xmbh,fkbh){
			if(fkbh==""){
				alert("该项目无最新进展！");
				return;
			}
			var pageUrl = "createFormInstance.action?formid=237&demId=121&XMBH="+encodeURI(xmbh)+"&FKXXBH="+encodeURI(fkbh);
			art.dialog.open(pageUrl,{
				title:'意见留言',
				loadingText:'正在加载中,请稍后...',
				bgcolor:'#999',
				rang:true,
				width:1000,
				cache:false,
				lock: true,
				height:450, 
				zIndex:1100,
				iconTitle:false,
				extendDrag:true,
				autoSize:false,
				content:pageUrl,
				close:function(){
					window.location.reload();	
				}
			});
		}	
		
		function clickMore(){
			var title="资本运作项目管理 ";
			var icon="icon-model";
			var url="hna_project_querylist.action";
			window.top.addTab(title,url,icon);
		}
		
		//领导意见留言
		function history(xmbh){
			var pageUrl = "hna_project_getLeaderOpinionList.action?projectno="+xmbh;
			art.dialog.open(pageUrl,{ 
				title:'领导意见留言',
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
		
		$(function(){
			var tr=document.getElementsByTagName("tr");
			for(var i=0;i<tr.length;i++)
			{
				tr[i].style.background=tr[i].rowIndex % 2==0?"#F9F9F9":"white";
			}
		});
	</script>	
  </head>
  
  <body >
    <div class="grid">
    	<table WIDTH="100%" style="border:1px solid #efefef">
			<TR  class="header" >
				<TH style="width:30%;">项目名称</TH>
				<TH style="width:20%;">最新进展</TH>
				<TH style="width:20%;">最后更新时间</TH>
				<TH style="width:15%;">领导批示</TH>
				<TH style="width:15%;">历史</TH>
			</TR>
			<s:iterator value="zbyzList"  status="status">
			<TR class="cell" align="left">
				<s:if test="rowAuthority == 'rowAdmin'">
					<TD><a href="javascript:void(0)" onclick="detail(<s:property value="INSTANCEID"/>)"><s:property value="PROJECTNAME"/></a></TD>
			    </s:if>
                <s:else>
                	<TD><a><s:property value="PROJECTNAME"/></a></TD>
                </s:else>
				<TD><a href="javascript:void(0)" onclick="jinZhanDetail('<s:property value="PROJETNO"/>')">详细</a></TD>
				<TD><s:property value="FKRQ"/></TD>
				<TD><a href="javascript:void(0)" onclick="piShi('<s:property value="PROJETNO"/>','<s:property value="fkInstanceid"/>')">批示</a></TD>
				<TD><a href="javascript:void(0)" onclick="history('<s:property value="PROJETNO"/>','<s:property value="fkInstanceid"/>')">查看</a></TD>
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
