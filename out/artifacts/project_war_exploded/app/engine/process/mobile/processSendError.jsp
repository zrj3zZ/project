<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>操作提示</title>
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript">
	 /*   $(document).ready(function(){
	  	 
           $(document).bind("contextmenu",function(e){   
              return false;   
           });
             
       });*/ 
</script>
<style type="text/css">
	.nextStep{
		font-size:16px;
		color:#0000FF;
		text-align: left;
		padding-left: 3px;
		font-family:"黑体";
		border-bottom:1px #999999 dotted;
		vertical-align:middle;
		word-wrap:break-word;
		word-break:break-all;
		font-weight:500;
		line-height:12px;
		padding-top:5px;
	}
	.ItemTitle{
		font-size:12px;
		color:#0000FF;
		text-align: right;
		padding-left: 10px;
		font-family:"宋体";
		vertical-align:middle;
		word-wrap:break-word;
		word-break:break-all;
		font-weight:800;
		line-height:12px;
	}
	.pageInfo{
		font-size:12px;
		color:#0000FF;
		text-align: left;
		padding-left: 10px;
		font-family:"宋体";
		vertical-align:middle;
		word-wrap:break-word;
		word-break:break-all;
		font-weight:500;
		line-height:12px;
	}
</style>
</head>
<body >
<table width="100%" height="100%">
<tr>
<td align="center" valign="middle">
	<table border="0" align="center" cellpadding="0" cellspacing="0" width="460" valign="middle">
	  <tr>
	    <td class="onleft"> </td>
		 <td class="onbg">
					<table border="0" width="400">
					<tr>
					<td class="onnologo"></td>
					<td class="onyestext">办理异常，当前流程已结束，或者是当前用户无办理权限。
					</td>
					</tr>
					</table>
		</td>
	  <td class="onright"></td>
	  </tr>
	</table>
</td>
</tr>
</table>
</body>
</html>
