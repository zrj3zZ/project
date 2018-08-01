<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!--
User interface templet,HTML
 
AWS is a application middleware	for BPM	system,Powered by actionsoft,China.
Copyright(C)2001,2002,2003,2004,2005,2006 Actionsoft Co.,Ltd
-->
 
 
<html>
<head>
<title>资产领用车</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
   <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
    <script type="text/javascript" src="iwork_js/commons.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
<style type="text/css"> 
.aButton {
 	font-family: "Arial", "Helvetica", "sans-serif"; 
	background-image:url("iwork_img/formbutton.gif"); 
	font-size: 11px; 
	text-align: center; 
	background-color:#EFEFEF; 
	border-color: #FFFFFF #FFFFFF #8FBCBF; 
	background-position: middle; 
	cursor:pointer;
	padding-top:2px;
	padding-left:3px;
	cursor:hand;
	border-left:1px #999999 solid;
	border-right:1px #999999 solid;
	border-top:1px #999999 solid;
	border-bottom:1px #999999 solid;
}
 
</style>
<script type="text/javascript"> 
function carclose(){
	$(window.parent.document).find("div#FloatDiv").css("visibility","hidden");
	}
function removeItem(id){
			$("#id").val(id);
			$("#editForm").attr("action","iwork_bgyp_shopRemove.action");
			$("#editForm").submit();
}
function openPage(id){
			var url = 'iwork_bgyp_showPage.action';
			var target = "iform_"+id;
			var page = window.open('form/loader_frame.html',target,'width='+screen.width-50+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location=url;
} 
</script>
</head>
<body >
<s:form name="editForm" id="editForm" theme="simple">
  <table align=center style="border:1px solid #ccc" cellpadding=0 cellspacing=0 width="200px"  bordercolorlight="#CCCCCC" bordercolordark="#FFFFFF">
      <tr>
        <td style="background-color:#F2F2F2; color:#990000; padding:3px; font-size:12px; font:黑体">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			  <tr>
				<td nowrap="nowrap" class="form_title"><img src="iwork_img/gw_car_ico.gif" border="0">我的领用列表</td>
				<td align="right"><img src="iwork_img/close.gif"  onClick="carclose();return false;" border='0'  style="cursor:pointer"></td>
			  </tr>
			</table>	
		</td>
      </tr> 
 
      <tr>
        <td  style="background-color:#FDFCDF; padding:3px; font-size:12px;">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center" bordercolorlight="#CCCCCC" bordercolordark="#FFFFFF">
			<s:iterator  value="shoplist" status="status">
			<tr><td style='border-bottom:1px dotted #cccccc'  class="form_data"><span title="<s:property value="name"/>">
				<s:if test="name.length()>10">
  					  <s:property value='name.substring(0,10)'/>...
 				</s:if> 
 				 <s:else>      
				     <s:property value='name'/>
				 </s:else><b>[<s:property value="num"/>]</b></span></td><td style='border-bottom:1px dotted #cccccc' align='right' width='10%'><a href='' onClick="removeItem(<s:property value="id"/>);return false;" class="form_data">[×]</a></td></tr>
			</s:iterator> 
		</table> 
 
      </td></tr>
	   <tr>
        <td align="right"  style="background-color:#F2F2F2; color:#990000; padding:3px; font-size:12px; font:黑体">
      <a href='#' onClick="openPage(<s:property value="id"/>);return false;" border='0'><img src=iwork_img/sub_left_bullet_08.gif style='margin-top:0px' border=0>生成领用单</a>&nbsp;&nbsp;
		</td>
      </tr>
  </table>
 
<input type=hidden name=id id="id">  
</s:form>
</body>
</html>

