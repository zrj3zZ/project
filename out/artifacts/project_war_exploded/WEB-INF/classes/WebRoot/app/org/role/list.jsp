<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head><title>角色管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">

<link href="iwork_css/org/role_list.css" rel="stylesheet" type="text/css" />

</head>	
<link href="iwork_css/common.css" rel="stylesheet" type="text/css" />
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/linkbutton.css">
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script language="javascript" src="iwork_js/commons.js"></script>	
<script language="javascript" src="iwork_js/org/role_list.js"></script>	
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>

<body  class="easyui-layout">
<div region="north" border="false" split="false" >
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr align="center" >
	<td  align="left" height="30" class="tools_nav">
		<table  border="0">
					<tr  >
						<td>
						<a href="javascript:void(0)" id="mb2" class="easyui-menubutton" menu="#mm2" iconCls="icon-edit">分组维护</a>
							<a href="javascript:addRole();" class="easyui-linkbutton"  plain="true" iconCls="icon-add" >新增</a>
							<a href="javascript:this.location.reload();" class="easyui-linkbutton"  plain="true" iconCls="icon-reload" >刷新</a>
						</td>
						
					</tr>
		</table>
	</td>
</tr>
</table>
</div>
<div region="west"  region="west" icon="icon-reload" border="false" split="false"  style="width:200px;padding:0px;padding-left:10px;overflow:hidden;border-right:1px solid #efefef">
	<s:property value="tab_html" escapeHtml="false"/> 
</div>
<div region="center" border="false" split="false">
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr><td align="center" >
	<table cellpadding="0" cellspacing="1" class="table3" >
	<thead>
    <tr  class="tablehead" >
        <th  width="67">ID</th>
        <th width="251"  >角色名称</th>
        <th width="169"  >皮肤样式</th>
        <th width="169"  >角色类别</th>
        <th width="163"  >操作</th>
    </tr>
    </thead>
    <tbody>
    <s:iterator value="availableItems" status="status">
        <tr class="tr1">
         <td align="center" class="RowData2">
            <s:property value="id"/>
          </td>
            <td align="center" class="RowData2">
            <a href='javascript:editRole(<s:property value="id"/>)'>
            <s:property value="rolename"/>
            </a>
            </td>
            <td align="center" class="RowData2"><s:property value="lookandfeel"/></td>
            <td align="center" class="RowData2"><s:property value="roletype"/></td>
            <td align="center" align="left" class="RowData2">
            	<a href='javascript:editRole(<s:property value="id"/>)'> <img alt="编辑" src="iwork_img/but_edit.gif" border="0"></a> 
            	<a href='javascript:confirm1(<s:property value="id"/>);'> <img alt="删除" src="iwork_img/but_delete.gif" border="0"></a> 
            	<s:if test="#status.first==true">
            		<a href='<s:url action="role_movedown" ><s:param name="id" value="id" /></s:url>'> <img alt="向下" src="iwork_img/but_down.gif" border="0"></a>
            	</s:if>
            	<s:elseif test="#status.last==true" >
            		<a href='<s:url action="role_moveup" ><s:param name="id" value="id" /></s:url>'> <img alt="向上" src="iwork_img/btn_up.gif" border="0"></a> 
            	</s:elseif>
            	<s:else>
	            	<a href='<s:url action="role_moveup" ><s:param name="id" value="id" /></s:url>'> <img alt="向上" src="iwork_img/btn_up.gif" border="0"></a> 
	            	<a href='<s:url action="role_movedown" ><s:param name="id" value="id" /></s:url>'> <img alt="向下" src="iwork_img/but_down.gif" border="0"></a> 
            	</s:else>
            </td>
        </tr>
    </s:iterator>
   
    </tbody>
</table>
</td></tr>
</table>
<div id="mm2" style="width:100px;">
	<div iconCls="icon-process-trans" onClick="addgroup();">新增分组</div>
	<div class="menu-sep"></div>
	<div iconCls="icon-edit" onClick="editgroup();">编辑分组</div>
	<div class="menu-sep"></div>
	<div iconCls="icon-remove" onClick="delgroup();">删除</div>
</div>
<s:hidden name="groupId" id="groupId"></s:hidden>
</div>
</body>
</html>
