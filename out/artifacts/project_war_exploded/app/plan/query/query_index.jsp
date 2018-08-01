<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>IWORK综合应用管理系统</title> 
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link href="iwork_css/common.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=default"></script>     		
	<script type="text/javascript"> 
	var selectedNodes;
		$(function(){ 
			
		})
		
		function showht(instanceId){
			// var url = 'openFormPage.action?formid='+formId+'&instanceId='+instanceId+'&demId='+demId;
			var url = 'loadVisitPage.action?formid=92&instanceId='+instanceId+'&demId=22';
			var target = "dem"+instanceId;

			var win_width = window.screen.width;
			var page = window.open(url,target,'width='+win_width+',height=800,top=10,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location = url;
		}
		
		function showht1(htid){
			var url = 'xhht_loadHtInfo_action.action?htid='+htid;
			var target = "dem"+htid;

			var win_width = window.screen.width;
			var page = window.open(url,target,'width='+win_width+',height=800,top=10,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location = url;
		}
		
		function showAuditInfo(instanceid,id){
			//var pageUrl = 'xhht_loadHtInfo_action.action?htid='+htid;
			var url = 'show_audit_info_action.action?instanceid='+instanceid+'&rowid='+id;
			var target = "dem"+instanceid;
			var win_width = window.screen.width;
			var page = window.open(url,target,'width=600,height=350,top=200,left=350,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location = url;
				
		}
		
		// 年度
		function setnd(obj){
			var nd = obj.value;
			var pageUrl="sanbu_plan_xhrc_queryYsbb.action?nd="+nd;
			window.location.href = pageUrl;
	       
		}
		function query(){
			var lx = $("#lx option:selected").text();
			if(lx=="请选择"){
				alert("请选择编报类型");
				return;
			}
			var nd = $("#nd option:selected").text();
			if(nd=="请选择"){
				alert("请选择年度");
				return;
			}
			var yf = $("#yf option:selected").text();
			if(yf=="请选择"){
				alert("请选择月份");
				return;
			}
			if(lx=="项目日常"){
				lx = "XHRC";
			}
			if(lx=="其他合同"){
				lx = "QTHT";
			}
			if(lx=="部门费用"){
				lx = "BMFY";
			}
			if(lx=="项目合同"){
				lx = "XHHT";
			}
			
			if(lx=="XHRC"){
				var pageUrl="plan_query_xhrc.action?year="+nd+"&month="+yf+"&bblx="+lx;
				window.location.href = pageUrl;
			}else if(lx=="QTHT"){
				var pageUrl="plan_query_qtht.action?year="+nd+"&month="+yf+"&bblx="+lx;
				window.location.href = pageUrl;
			}else if(lx=="BMFY"){
				var pageUrl="plan_query_bmfy.action?year="+nd+"&month="+yf+"&bblx="+lx;
				window.location.href = pageUrl;
			}else if(lx=="XHHT"){
				var pageUrl="plan_query_xhht.action?year="+nd+"&month="+yf+"&bblx="+lx;
				window.location.href = pageUrl;
			}else{
			 	var pageUrl="plan_query_xhrc.action?year="+nd+"&month="+yf+"&bblx="+lx;
				window.location.href = pageUrl;
				/*
				var pageUrl="plan_query_xhrc.action";
	       		$.post(pageUrl,{year:nd,month:yf,bblx:lx},function(data){
		       		if(data=='success'){
			        	window.location.reload();
			        }else{
			         	alert("操作异常");
			        }
		   	 	});*/
			 }
		}
	</script>
	<style>
		.cell:hover{
			background-color:#F0F0F0;
		}
		* {
			margin:0px;
			padding:0px;
			font-size:12px;
		}
		
		img {
			border: 0 none;
		}
		.header td{
			font-weight:bold;
			font-size:12px;
			padding:3px;
			white-space:nowrap;
			padding-left:5px;
			background:#fafafa url('../../iwork_css/jquerycss/default/images/datagrid_header_bg.gif') repeat-x left bottom;
			border-bottom:1px dotted #ccc;
			border-top:1px dotted #fff;
			border-right:1px dotted #ccc;
		} 
		.cell td{
			margin:0;
			padding:3px 4px;
			white-space:nowrap;
			word-wrap:normal; 
			overflow:hidden;
			text-align:left;
			line-height:30x;
			border-top:1px dotted #fff;
			border-right:1px dotted #ccc;
		}
		
		.cell td{
			margin:0;
			padding:3px 4px;
			white-space:nowrap;
			word-wrap:normal;
			overflow:hidden;
			text-align:left;
			border-bottom:1px dotted #eee;
			border-top:1px dotted #fff;
			border-right:1px dotted #ccc;
			line-hieght:30px;
		}
		
		.cellBtn td{
			border-bottom:1px solid #efefef;
			padding:5px;
		}
		.selectCheck{
			border:0px;
			text-align:right;
		}
		.cellTop td{
				margin:0;
			padding:3px 4px;
			white-space:nowrap;
			word-wrap:normal;
			overflow:hidden;
			text-align:left;
			border-bottom:1px dotted #eee;
			border-top:1px dotted #fff;
			border-right:1px dotted #ccc;
			line-hieght:30px;
		}
		.searchAre{
			padding:10px;
			border:1px solid #efefef;
			margin-bottom:5px;
			background-color:#FFFFDD;
		}
		.searchAre table td{
			padding:5px;
		}
	</style>
</head>
<body class="easyui-layout">
<!-- TOP区 -->
	<div region="center" style="padding:10px;border:0px;">
	<div class="searchAre">
		<table WIDTH="100%" >
			<tr >
				<TD style="text-align:right">编报类型</TD>
				<TD><s:select list="listlx" name="lx" id="lx" theme="simple" listKey="lx" listValue="lx" value="lx" headerKey="0" headerValue="请选择"></s:select></TD>
				<TD style="text-align:right">年度</TD>
				<TD><s:select list="listnd" name="nd" id="nd" theme="simple" listKey="nd" listValue="nd" value="nd" headerKey="0" headerValue="请选择"></s:select></TD>
				<TD style="text-align:right">月份</TD>
				<TD><s:select list="listyf" name="yf" id="yf" theme="simple" listKey="yf" listValue="yf" value="yf" headerKey="0" headerValue="请选择"></s:select></TD>
				<TD>
					<a href="javascript:query();" class="easyui-linkbutton" plain="false" iconCls="icon-search">查询</a>
				</TD>
			</tr>
		</table> 
	</div>
		<s:if test="mainData.BBLX=='XHRC'">
			<table WIDTH="100%" style="border:1px solid #efefef">
				<TR  class="header">
					<TD style="width:30px;text-align:center">序号</TD>
					<TD style="width:100px">项目名称</TD>
					<TD style="width:100px">项目编号</TD>
					<TD style="width:100px">年度支出成本指标</TD>
					<TD style="width:80px">可用余额</TD>
					<td style="width:100px">截至上月已支出</TD>
					<td style="width:80px">备注</td>
					<td style="width:80px">本月预算</td>
					<td style="width:80px">付款理由</td>
					<td style="width:80px">审批状态</td>
					<td style="width:80px">操作</td>
				</TR>
				<s:iterator value="list"  status="status">
					<TR class="cell">
						<TD style="text-align:center"><s:property value="#status.count"/></TD>
						<TD><s:property value="XMMC"/></TD>
						<TD><s:property value="XMBH"/></TD>
						<TD><s:property value="NDZCCBZB"/></TD>
						<TD><s:property value="KYYE"/></TD>
						<TD><s:property value="SYYZC"/></TD>
						<TD><s:property value="BZ"/></TD>
						<TD><s:property value="BYYS"/></TD>
						<TD><s:property value="FKLY"/></TD>
						<TD><s:property value="SPZT"/></TD>
						<TD><a href="###" onclick="showAuditInfo('<s:property value="INSTANCEID"/>','<s:property value="ID"/>')">流程跟踪</a></TD>
					</TR>
				</s:iterator>
			</table>
		</s:if>
		<s:elseif test="mainData.BBLX=='QTHT'">
			<table WIDTH="100%" style="border:1px solid #efefef">
				<TR  class="header">
					<TD style="width:30px;text-align:center">序号</TD>
					<TD style="width:100px">合同名称</TD>
					<TD style="width:100px">合同编号</TD>
					<TD style="width:90px">合同金额</TD>
					<TD style="width:90px">合同未付金额</TD>
					<td style="width:80px">累计执行</TD>
					<td style="width:80px">累计执行比例</TD>
					<td style="width:60px">预算科目</td>
					<td style="width:80px">年度付款指标</td>
					<td style="width:60px">本月预算</td>
					<td style="width:60px">付款理由</td>
					<td style="width:80px">审批状态</td>
					<td style="width:80px">操作</td>
				</TR>
				<s:iterator value="list"  status="status">
					<TR class="cell">
						<TD style="text-align:center"><s:property value="#status.count"/></TD>
						<TD><a href="###" onclick="showht('<s:property value="HTINSTANCEID"/>')"><s:property value="HTMC"/></a></TD>
						<TD><s:property value="HTBH"/></TD>
						<TD><s:property value="HTZJE"/></TD>
						<TD><s:property value="HTWFKJE"/></TD>
						<TD><s:property value="LJZXJE"/></TD>
						<TD><s:property value="ZXBL"/>%</TD>
						<TD><s:property value="YSKMMC"/></TD>
						<TD><s:property value="NDFKZB"/></TD>
						<TD><s:property value="BYYS"/></TD>
						<TD><s:property value="FKLY"/></TD>
						<TD><s:property value="SPZT"/></TD>
						<TD><a href="###" onclick="showAuditInfo('<s:property value="INSTANCEID"/>','<s:property value="ID"/>')">流程跟踪</a></TD>
					</TR>
				</s:iterator>
			</table>
		</s:elseif>
		<s:elseif test="mainData.BBLX=='BMFY'">
			<table WIDTH="100%" style="border:1px solid #efefef">
				<TR class="header">
					<TD style="width:30px;text-align:center">序号</TD>
					<TD style="width:160px">预算名称</TD>
					<TD style="width:110px">期间费用年度预算额</TD>
					<TD style="width:110px">期间费用可用余额</TD>
					<TD style="width:100px">本月预算</TD>
					<TD style="width:100px">备注</TD>
					<td style="width:80px">审批状态</td>
					<td style="width:80px">操作</td>
				</TR>
				<s:iterator value="list"  status="status">
					<TR class="cell">
						<TD style="text-align:center"><s:property value="#status.count"/></TD>
						<TD><s:property value="YSMC"/></TD>
						<TD><s:property value="NDYSE"/></TD>
						<TD><s:property value="KYYE"/></TD>
						<TD><s:property value="BYYS"/></TD>
						<TD><s:property value="BZ"/></TD>
						<TD><s:property value="SPZT"/></TD>
						<TD><a href="###" onclick="showAuditInfo('<s:property value="INSTANCEID"/>','<s:property value="ID"/>')">流程跟踪</a></TD>
					</TR>
				</s:iterator>
			</table>
		</s:elseif>
		<s:elseif test="mainData.BBLX=='XHHT'">
			<table WIDTH="100%" style="border:1px solid #efefef">
				<TR class="header">
					<TD style="width:30px;text-align:center">序号</TD>
					<TD style="width:100px">名称</TD>
					<TD style="width:100px">编号</TD>
					<TD style="width:90px">金额</TD>
					<TD style="width:90px">未付金额</TD>
					<td style="width:80px">累计执行</TD>
					<td style="width:80px">累计执行比例</TD>
					<td style="width:80px">年度付款指标</td>
					<td style="width:60px">本年已执行</td>
					<td style="width:60px">本月预算</td>
					<td style="width:60px">付款理由</td>
					<td style="width:60px">部门名称</td>
					<td style="width:80px">审批状态</td>
					<td style="width:80px">操作</td>
				</TR>
				<s:iterator value="list"  status="status">
					<TR class="cellTop">
						<TD style="text-align:center"><s:property value="#status.count"/></TD>
						<TD><a href="###" onclick="showht1('<s:property value="XMBH"/>')"><s:property value="XMMC"/></a></TD>
						<TD><s:property value="XMBH"/></TD>
						<TD><s:property value="HTZJE"/></TD>
						<TD><s:property value="HTWFKJE"/></TD>
						<TD><s:property value="LJZXJE"/></TD>
						<TD><s:property value="LJZX_RATE"/>%</TD>
						<TD><s:property value="NDFKZB"/></TD>
						<TD><s:property value="BYYZX"/></TD>
						<TD><s:property value="BYYS"/></TD>
						<TD style="display:none"><s:property value="BBZT"/></TD>
						<TD><s:property value="FKLY"/></TD>
						<TD><s:property value="CJBMMC"/></TD>
						<TD></TD>
						<TD></TD>
						<TD></TD>
					</TR>
					<s:iterator value="children"  id="inner" >
						<TR class="cell">
							<td></td>
							<TD style="text-align:left;padding-left:20px;">
							<img src="iwork_img/arrow.png" alt="" /><s:property value="#inner.XMMC"/>
							<td><s:property value="#inner.XMBH"/></TD></td>
							<TD><s:property value="#inner.HTZJE"/></TD>
							<TD><s:property value="#inner.HTWFKJE"/></TD>
							<TD><s:property value="#inner.LJZXJE"/></TD>
							<TD><s:property value="#inner.LJZX_RATE"/>%</TD>
							<TD><s:property value="#inner.NDFKZB"/></TD>
							<TD><s:property value="#inner.BYYZX"/></TD>
							<TD><s:property value="#inner.BYYS"/></TD>
							<TD><s:property value="#inner.FKLY"/></TD>
							<TD><s:property value="#inner.CJBMMC"/></TD>
							<TD><s:property value="#inner.SPZT"/></TD>
							<TD><a href="###" onclick="showAuditInfo('<s:property value="INSTANCEID"/>','<s:property value="id"/>')">流程跟踪</a></TD>
						</TR>
					</s:iterator>
				</s:iterator>
			</table>
		</s:elseif>
	</div>
</body>
</html>
