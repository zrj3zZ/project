<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head><title>流程处理中心</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">

<link rel="stylesheet" type="text/css" href="../../iwork_css/flow/wfcreate.css">

</head>	
<body>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" >

<tr><td align="center" >
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" >
<thead>
    <tr  style="background-color:#efefef;">
        <th  width="39" >序号</th>
        <th width="279"  >流程名称</th>
        <th width="279"  >主键</th>
        <th width="232" >流程描述</th>
    </tr>
    </thead>
    <s:iterator value="processList" status="status">
        <tr class="tr1">
	        <td class="RowData2">
	         <s:property value="#status.count"/>
	        </td> 
            <td class="RowData2"><s:property value="name"/></td>
            <td class="RowData2"><s:property value="key"/></td>
            <td class="RowData2"><a href='<s:url action="startProcessInstance.action" ><s:param name="startProcessInstanceKey" value="key" /></s:url>'>启动</a></td>
        </tr>
    </s:iterator>
   
</table>
</td></tr>
</table>
</body>
</html>

