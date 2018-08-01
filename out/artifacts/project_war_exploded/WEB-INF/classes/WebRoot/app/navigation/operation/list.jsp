<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.iwork.app.navigation.directory.model.SysNavDirectory" %>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap" %>
<%@page import="com.iwork.app.navigation.sys.model.SysNavSystem" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Map" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
	<head><title>操作管理</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<link href="iwork_css/navigation/operation_list.css" rel="stylesheet" type="text/css" />	
	<link href="iwork_css/common.css" rel="stylesheet" type="text/css" />
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script language="javascript" src="iwork_js/commons.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="iwork_js/navigation/operation_list.js"></script>
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
					 this.location.href='operation_add.action'; return false;
				}
	}); //快捷键

	</script>
</head>	    
<body>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr align="center" >
	<td  align="left" height="30" class="tools_nav">
		<table  border="0">
					<tr  >
						<td>
							<a href="javascript:addOperation();" class="easyui-linkbutton"  plain="true" iconCls="icon-add" >新增</a>
							<a href="javascript:this.location.reload();" class="easyui-linkbutton"  plain="true" iconCls="icon-reload" >刷新</a>
						</td>
						
					</tr>
		</table>
	</td> 
</tr>

<tr><td align="center" >
	<table cellpadding="1" cellspacing="1" class="table3" >
	<thead>
    <tr  class="tablehead" >
        <th  width="39">序号</th>
        <th width="179"  >操作名称</th> 
        <th width="179"  >ACTION命令</th> 
        <th width="150" >关联类型/关联ID</th>
        <th width="400"  >操作描述</th>
        <th width="163"  >操作</th>
    </tr>
    </thead>
    <tbody>
    <s:iterator value="availableItems" status="status">
        <tr class="tr1">
         <td class="RowData2">
            <s:property value="id"/>
          </td>
            <td class="RowData2">
            <a href='javascript:editOperation(<s:property value="id"/>)'>
            <s:property value="oname"/>
            </a>
            </td>
            <td class="RowData2"><s:property value="actionurl"/></td>
            <td class="RowData2"><s:property value="ptype"/>/<s:property value="pid"/></td>
            <td class="RowData2"><s:property value="odesc" /></td>
            <td align="left" class="RowData2">
            <s:property value="id"/> 
            	<a href='javascript:editOperation(<s:property value="id"/>)'> <img alt="编辑" src="iwork_img/but_edit.gif" border="0"></a> 
            	<a href='javascript:deleteOperation(<s:property value="id"/>)'> <img alt="删除" src="iwork_img/but_delete.gif" border="0"></a>
            	<s:if test="#status.first==true">
            	    <a href='<s:url action="operation_movedown" ><s:param name="id" value="id" /></s:url>'> <img alt="向下" src="iwork_img/but_down.gif" border="0"></a>
            	</s:if>
            	<s:elseif test="#status.last==true" >
            		<a href='<s:url action="operation_moveup" ><s:param name="id" value="id" /></s:url>'> <img alt="向上" src="iwork_img/btn_up.gif" border="0"></a>
            	</s:elseif>
            	<s:else>
            		<a href='<s:url action="operation_moveup" ><s:param name="id" value="id" /></s:url>'> <img alt="向上" src="iwork_img/btn_up.gif" border="0"></a>
            		<a href='<s:url action="operation_movedown" ><s:param name="id" value="id" /></s:url>'> <img alt="向下" src="iwork_img/but_down.gif" border="0"></a>
            	</s:else>  
            </td>
        </tr>
    </s:iterator>
    <tr align="right">
    	<td colspan="9">
    		共<s:property value="totalRows"/>行&nbsp;
    		第<s:property value="currentPage"/>页&nbsp;
    		共<s:property value="pager.getTotalPages()"/>页&nbsp;
    		<a href="<s:url value="operation_list.action">
    			<s:param name="currentPage" value="currentPage"/>
    			<s:param name="pagerMethod" value="'first'"/>
    			
    		</s:url>">首页</a>
    		<a href="<s:url value="operation_list.action">
    			<s:param name="currentPage" value="currentPage"/>
    			<s:param name="pagerMethod" value="'previous'"/>
    		</s:url>">上一页</a>
    		<a href="<s:url value="operation_list.action">
    			<s:param name="currentPage" value="currentPage"/>
    			<s:param name="pagerMethod" value="'next'"/>
    		</s:url>">下一页</a>
    		<a href="<s:url value="operation_list.action">
    			<s:param name="currentPage" value="currentPage"/>
    			<s:param name="pagerMethod" value="'last'"/>
    		</s:url>">尾页</a>
    	</td>
    </tr>	
    </tbody>
</table>
</td></tr>
<tr>
	<td>
		<s:form name="ifrmMain">
			<s:hidden id="ptype" name="ptype" />	     
		 	<s:hidden id="pid"  name="pid" />	   
		</s:form>
	</td>
</tr>
</table>
</body>
</html>
