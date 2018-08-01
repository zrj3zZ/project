<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head><title>查看AWS服务器线程池状态</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
		<script language="javascript" src="iwork_js/commons.js"></script>
	    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/linkbutton.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
        <link href="iwork_css/system/purgroup_list.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="iwork_js/system/purgroup_list.js"></script>
        <script type="text/javascript">
 //==========================装载快捷键===============================//快捷键
    jQuery(document).bind('keydown',function (evt){		
		    if(evt.ctrlKey&&evt.shiftKey){
			return false;
		   }
		   else if(evt.ctrlKey && event.keyCode==82){ //Ctrl+r /刷新操作
			         this.location.reload(); return false;
		     }
}); //快捷键
</script>
<style type="text/css">
	.toolbar td{
			background:url(../iwork_img/engine/tools_nav_bg.jpg) repeat-x;
			height:34px;
			line-height:30px;
			padding-left:10px;
			vertical-align:middle;
			padding-top:2px;
			padding-bottom:2px; 
			border-right:1px #efefef;
	}
	.item td{
			height:24px;
			line-height:20px;
			padding-left:10px;
			vertical-align:middle;
			padding-top:2px;
			padding-bottom:2px; 
			border-right:1px #efefef;
	}
</style>
</head>	
<body class="easyui-layout">
<div region="north" border="false" split="false" >
<table width="100%" border="0" cellspacing="0" cellpadding="0" style="border-bottom:1px solid #efefef;">
  <tr > 
  <td style='height: 35px;' align="left"><span id="title_span" style='font-size:18px;font-family:黑体'>查看系统线程状态</span></td>
  <td  style="padding-left:10px;padding-right:10px;font-size:12px;color:#999;text-align:right">
  	信息采集时间(APP服务器):<s:property value ="currentTime"/><input type=button value='刷新列表'  class ='actionsoftButton' onClick="document.location.reload();"  border='0'>
  </td></tr>
</table>
</div>
<div region="center" border="false" style="border-left:1px solid #efefef">
  <table align=center border=0 cellpadding=0 cellspacing=0 width="100%">
    <tr > 
      <td colspan="2"   style="padding-left:10px;padding-right:10px;"> 
        <table width="100%" border="1" cellspacing="0" cellpadding="3" align="center" bordercolorlight="#CCCCCC" bordercolordark="#FFFFFF">
          <tr class="toolbar"> 
            <td class=actionsoftReportTitle width=5%>序号</td>
            <td class=actionsoftReportTitle width=10%>线程组</td>
            <td class=actionsoftReportTitle width=65%>线程名</td>     
            <td class=actionsoftReportTitle width=15%>标记</td>      
            <td class=actionsoftReportTitle width=15%>状态</td>
          </tr>
          <s:property value="list"  escapeHtml='false'/>
        </table>
      </td>
    </tr>
  </table>       
</div>
</body>
</html>
