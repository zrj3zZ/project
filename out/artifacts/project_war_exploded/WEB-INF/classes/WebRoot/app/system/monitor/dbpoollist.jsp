<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head><title>查看数据库连接池信息</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
	.title{
		font-family:微软雅黑;
		font-size:18px;
		padding:10px;
	}
	th{
		line-height:30px;
		background:#efefef;
		font-family:微软雅黑;
	}
	.td_title{
			width:200px;
			line-height:30px;
		padding:5px;
		font-size:14px;
		font-family:微软雅黑;
	}
	.td_data{
			width:200px;
			line-height:30px;
		padding:5px;
		font-size:16px;
	}
	.toolbar{
			background:url(../iwork_img/engine/tools_nav_bg.jpg) repeat-x;
			height:34px;
			line-height:30px;
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
<div class="title">当前数据库连接池状态</div>
<div class="toolbar">信息采集时间(APP服务器):<s:property value ="currentTime"/><input type=button value='刷新列表'  class ='actionsoftButton' onClick="document.location.reload();"  border='0'></div>

</div>
<div region="center" border="false" style="border-left:1px solid #efefef">
  <table align=center border=0 cellpadding=0 cellspacing=0 width="100%">
  
    <tr> 
      <td colspan="2"   style="padding-left:10px;padding-right:10px;"> 
        <table border="1" cellspacing="0" cellpadding="3" align="center" bordercolorlight="#CCCCCC" bordercolordark="#FFFFFF">
           <tr> 
            <th>标题</th>
            <th>参数值</th>         
            <th>说明</th>         
          </tr>
          <s:property value="list"  escapeHtml='false'/>
        </table>
      </td>
    </tr>
    <tr> <td  style="padding-left:10px;padding-right:10px;font-size:10px;color:#999">
  	
  <br /></td></tr>
  </table>       
</div>
</body>
</html>
