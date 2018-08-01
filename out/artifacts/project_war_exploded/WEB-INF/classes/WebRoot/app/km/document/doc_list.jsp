<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head><title>知识目录管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">

<link href="iwork_css/km/opdoclist.css" rel="stylesheet" type="text/css" />

</head>	
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="iwork_js/commons.js"></script>
<body>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr align="center" >
	<td  align="left" height="30" class="pagetoolbar">
		<table  border="0">
					<tr  >
						<td align="left">
							<a href='<s:url action="km_dir_add" ></s:url>'><img alt="新建文件夹" border="0" src="../iwork_img/km/newFolder16.gif">新建文件夹</a>
						</td>
						
					</tr>
		</table>
	</td>
</tr>
<tr><td align="center" >
	<table cellpadding="1" cellspacing="1" width='100%' >
	<thead>
    <tr  class="tablehead" >
        <th  width="39" >序号</th>
        <th width="279"  >目录名称</th>
        <th width="232" >优先级</th>
        <th width="240"  >大小</th>
        <th width="169"  >创建人</th>
        <th width="163"  >创建时间</th>
    </tr>
    </thead>
    <s:iterator value="availableItems" status="status">
        <tr class="tr1">
	        <td class="RowData2">
	         <s:property value="#status.count"/>
	        </td>
            <td class="RowData2">
	            <a href='<s:url action="km_dir_load" ><s:param name="id" value="id" /></s:url>' rel="gb_page_center[540,300]">
	            <img alt="目录" src="iwork_img/km/treeimg/ftv2link01.gif" border="0">
	            <s:property value="directoryname"/>(<s:property value="id"/>)
	            </a>
            </td>
            <td class="RowData2"><s:property value="priority"/></td>
            <td class="RowData2"><s:property value="filesize"/></td>
            <td class="RowData2"><s:property value="createuser" /></td>
            <td class="RowData2"><s:property value="createdate" /></td>
            
        </tr>
    </s:iterator>
</table>
</td></tr>
</table>
</body>
</html>

