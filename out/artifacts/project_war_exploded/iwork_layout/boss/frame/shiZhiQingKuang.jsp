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
	</style>

	<script type="text/javascript">
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
			<TR  class="header" align="left">
				<TH style="width:15%;">公司名称</TH>
				<TH style="width:20%;">昨日市值(万元)</TH>
				<TH style="width:15%;">股价</TH>
				<TH style="width:20%;">前日市值(万元)</TH>
				<TH style="width:15%;">股价</TH>
			</TR>
			<s:iterator value="shizhiList"  status="status">
			<TR class="cell">
				<TD><s:property value="gsjc"/></TD>
				<TD name="ZRSZ"><s:property value="zrsz"/></TD>
				<TD><s:property value="zrgj"/></TD>
				<TD><s:property value="qrsz"/></TD>
				<TD><s:property value="qrgj"/></TD>
			</TR>
			</s:iterator>
			<TR class="cell">
				<TD>集团总市值</TD>
				<TD><s:property value="jtzsz"/></TD>
				<TD></TD>
				<TD></TD>
			</TR>
		</table>
    </div>
  </body>
</html>
