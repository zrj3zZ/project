<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head><title>序列管理</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
    <link href="iwork_css/common.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/linkbutton.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/system/sequence_list.css">
    <link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
	<script language="javascript" src="iwork_js/commons.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="iwork_js/system/sequence_list.js"></script>
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
         <script type="text/javascript">

 //==========================装载快捷键===============================//快捷键
    jQuery(document).bind('keydown',function (evt){		
		    if(evt.ctrlKey&&evt.shiftKey){
			return false;
		   }
		   else if(evt.ctrlKey && event.keyCode==82){ //Ctrl+r /刷新操作
			         this.location.reload(); return false;
		     } 
		  else if(evt.shiftKey && event.keyCode==78){ //Shift+n 新增操作
					 addSequence(); return false;
				}
}); //快捷键
	</script>
</head>	
<body>
<table width="100%" border="0" align="left" cellpadding="0" cellspacing="0" >
<tr align="center" >
	<td  align="left" height="30" class="tools_nav">
		<table  border="0" >
			<tr>
				<td>
					<a href="javascript:addSequence();" class="easyui-linkbutton"  iconCls="icon-add" plain="true">增加</a>
					<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
				</td>
			</tr>
		</table>
	</td> 
</tr>
<tr>
  <td align="center" style="padding:5px;"> 
	<table cellpadding="1" cellspacing="1"  width="100%" border="0px" class="table3" >
	<tr class="tablehead" >
		<th norwap width="91">序号</th>
		<th width="353">序列键值</th> 
		<th width="91">序列ID</th>
		<th width="568">序列描述</th>
		<th width="273">操作</th>
	</tr>
    <s:iterator value="availableItems" status="status">
        <tr  class="tr1">
	        <td width="36" class="RowData2"><s:property value="#status.count"/></td>
            <td  valign="top" class="RowData2">
               <a href='javascript:editSequence(<s:property value="id"/>);'><s:property value="sequencekey" /></a>
            </td>
            <td  valign="top" class="RowData2"><s:property value="sequencevalue" /></td>
            <td  valign="top" class="RowData2"><s:property value="sequencedesc" /></td>
             <td align="left" class="RowData2">
            	<a href='javascript:editSequence(<s:property value="id"/>);'><img alt="编辑" src="iwork_img/but_edit.gif" border="0"></a> 
            	<a href='javascript:delConfirm(<s:property value="id"/>);'> <img alt="删除" src="iwork_img/but_delete.gif" border="0"></a>
            </td>
        </tr>
    </s:iterator>
    <tr align="right"> 
    <td colspan="6" class="STYLE1">
         共<s:property value="totalRows"/>行&nbsp;
    	第<s:property value="currentPage"/>页&nbsp;
    	共<s:property value="pager.getTotalPages()"/>页&nbsp;
    	<a href='<s:url value="sequence_list.action" ><s:param name="currentPage" value="currentPage"/><s:param name="pagerMethod" value="'first'"/></s:url>'>首页</a>
    	<a href='<s:url value="sequence_list.action"><s:param name="currentPage" value="currentPage"/><s:param name="pagerMethod" value="'previous'"/></s:url>'>上一页</a>
    	<a href='<s:url value="sequence_list.action"><s:param name="currentPage" value="currentPage"/><s:param name="pagerMethod" value="'next'"/></s:url>'>下一页</a>
    	<a href='<s:url value="sequence_list.action"><s:param name="currentPage" value="currentPage"/><s:param name="pagerMethod" value="'last'"/></s:url>'>尾页</a>    	
    </td>
    </tr>	
</table>
</td></tr>
</table>
</body>
</html>
