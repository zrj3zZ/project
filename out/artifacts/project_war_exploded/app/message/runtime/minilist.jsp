<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js" charset="gb2312"></script>
    <script type="text/javascript" src="iwork_js/message/sysmsgpage.js"></script>
	<script type="text/javascript" src="iwork_js/message/sysmsglist.js"></script>
	<script type="text/javascript">
	
	
	function submitMsg(pageNumber,pageSize){
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#frmMain").submit();
	}

	function openInfo(id){ 
		var pageUrl = "sysmsg_showItem.action?id="+id;
		parent.parent.openWin("系统消息",450,380,pageUrl,this.location);
	}
	function showMsg(){
		parent.location = "sysmsg_index.action";
	}
	</script>
	<style type="text/css">
		.msgTb tr td{
			line-height:22px;
			font-size:10px;
			color:#0000ff;
		}
		.msgTb tr:hover{
			background-color:#efefef;
			cursor:pointer;
		}
		.text-overflow{
			overflow: hidden;
			width: 220px;
			text-overflow: ellipsis;
			white-space: nowrap;
			word-break: keep-all;
			font-size:10px;
		}
	</style>
</head>
<body>
<!-- TOP区 -->
 <table width="100%" border="0" cellspacing="0" cellpadding="0" class="msgTb">
<s:iterator value="list" >
<tbody id="div_item_<s:property value="id"/>">
      <tr> 
      <td>
      <s:if test="status==null||status==0">
			<img src="iwork_img/logomail.gif" alt="未读消息" />
	</s:if>
	<s:else>
		<img src="iwork_img/read.gif" alt="已读消息" />
	</s:else>
      </td>
        <td  onclick="openInfo(<s:property value="id"/>);">
        <div class="text-overflow" title=" <s:property value='title'/>">
		     <s:property value='title'/></div>
        </td> 
        <td> <s:property value="sendDate"/></td>
        <td><img align='top' style="cursor:hand" alt="删除消息" src='iwork_img/sysmsg/msg_close.gif' onClick="deleteit(<s:property value="id"/>)"></td>
      </tr>
  </tbody>
</s:iterator>
<tr>
	<td colspan="2" style="background-color:#fff;color:#999">
	<s:if test="status==0">
	<a href="sysmsg_mini_unread_list.action">全选</a>|<a href="sysmsg_mini_unread_list.action?status=1">已读</a>|未读
	</s:if>
	<s:elseif test="status==null">
		全选|<a href="sysmsg_mini_unread_list.action?status=1">已读</a>|<a href="sysmsg_mini_unread_list.action?status=0">未读</a>
	</s:elseif>
	<s:else>
	<a href="sysmsg_mini_unread_list.action?status=0">全选</a>|已读|<a href="sysmsg_mini_unread_list.action?status=0">未读</a>
	</s:else>
	</td>
	<td colspan="2"  style="background-color:#fff;color:#999"onclick="showMsg()"  style="background-color:#fff;color:#999">点击查看全部...</td>
</tr>
<tr>
	
</tr>
</table>
			<form action="sysmsg_index.action" method=post name="editForm" id="editForm">
			<s:hidden name="status" id="status"></s:hidden>
			<s:hidden name="type" id="type"></s:hidden> 
			<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
			<s:hidden name="pageSize" id="pageSize"></s:hidden>
		</form>	
</body>
</html>
