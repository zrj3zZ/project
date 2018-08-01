<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head><title>服务管理</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
        <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
        <link href="iwork_css/public.css" rel="stylesheet" type="text/css" />      
        <link href="iwork_css/system/sysservice_list.css" rel="stylesheet" type="text/css" />
        <link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
		<script language="javascript" src="iwork_js/commons.js"></script>
        <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
        <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
        <script language="javascript" src="iwork_js/system/sysservice_list.js"></script>
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
					 service_add(); return false;
				}
}); //快捷键
	</script>
</head>	

<body>
<s:form name="editForm" action="sysService_save" >
<table width="99%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr align="center" >
	<td align="left" class="title"><img alt="服务管理" src="iwork_img/server.gif">服务管理<br><img  height="3" width="60%" src="iwork_img/AuditingToolbar.gif" border="0"></td>
</tr>
<tr>
	<td align="left" valign="top"></td>
</tr>
<tr align="center" >
	<td  align="left" height="30" class="pagetoolbar">
		  <a href="javascript:service_add();" class="easyui-linkbutton"  iconCls="icon-add" plain="true">新增服务模型</a>						
	</td> 
</tr>
<tr>
<td align="center" > 
	<table cellpadding="0" cellspacing="0" width="100%" border="1px" class="Acolor">
	<tr>
		<th class="servicetitle"> 
		<!-- 超级管理检查 -->
		<s:if test="superManager==true">
			<a href='<s:url action="service_add" ></s:url>'>
			<img alt="增加服务" src="iwork_img/add_obj.gif" border="0"></a>
		</s:if>
		</th>
		<th class="servicetitle">服务名 </th> 
		<th class="servicetitle">描述</th>
		<th class="servicetitle">有效日期</th>
		<th class="servicetitle">状态</th>		
		<th class="servicetitle">操作</th>
	</tr>
    <s:iterator value="availableItems" status="status">
        <tr >
	        <td width="20" class="servicename">
	         <s:property value="#status.count"/>
	        </td>
	        
            <td  class="servicename" >
            <a href='javascript:service_edit(<s:property value="id"/>);' > <img src="iwork_img/server_25.gif" border="0">&nbsp;&nbsp;<s:property value="servicename"/>(<s:property value="servicekey"/>)
            </a>
            </td>
            <td class="servicecontent" valign="top"><IMG alt="说明 " src="iwork_img/rect.gif" border="0">&nbsp;<s:property value="servicedesc" /></td>
            <td class="servicecontent" valign="top"><s:date name="startdate" format="yyyy-MM-dd" />&nbsp;至&nbsp;<s:date name="enddate" format="yyyy-MM-dd" /></td>
            <td class="servicename">
                  <s:if test="status==1">
        	        <a href="javascript:stop_confirm(<s:property value='id'/>);"><img title="当前启动状态" src="iwork_img/osprc_obj.gif" border="0"></a>
        	      </s:if>
        	      <s:elseif test="status==0">
        	        <a href="javascript:start_confirm(<s:property value='id'/>);"><img title="当前停止状态" src="iwork_img/osprct_obj.gif" border="0"></a>
        	      </s:elseif>  
        	 </td>
        	 <td class="servicename">
        	 	 <s:if test="servicekey!='SYSTEM'">
        	      	 <a href="javascript:del_confirm(<s:property value='id'/>);"><img alt="删除" src="iwork_img/but_delete.gif" border="0"></a>
        	      	 <a href='javascript:service_edit(<s:property value="id"/>);'><img alt="编辑" src="iwork_img/but_edit.gif" border="0"></a>
        	      </s:if>
        	      <s:if test="#status.first==true">
            	      <a href='<s:url action="sysService_movedown" ><s:param name="id" value="id"/></s:url>'> <img alt="向下" src="iwork_img/but_down.gif" border="0"></a>
            	  </s:if>
            	  <s:elseif test="#status.last==true" >
            		  <a href='<s:url action="sysService_moveup"  ><s:param name="id" value="id"/></s:url>'> <img alt="向上" src="iwork_img/btn_up.gif" border="0"></a>
            	  </s:elseif>
            	  <s:else>
            		  <a href='<s:url action="sysService_moveup"  ><s:param name="id" value="id"/></s:url>'> <img alt="向上" src="iwork_img/btn_up.gif" border="0"></a>
            		  <a href='<s:url action="sysService_movedown"><s:param name="id" value="id"/></s:url>'> <img alt="向下" src="iwork_img/but_down.gif" border="0"></a>
            	  </s:else>           	               
        	 </td>        
        </tr>
    </s:iterator>
    <tr align="right">
    	<td colspan="6" class="servicetitle STYLE1">
    		共<s:property value="totalRows"/>行&nbsp;
    		第<s:property value="currentPage"/>页&nbsp;
    		共<s:property value="pager.getTotalPages()"/>页&nbsp;
    		<a href="<s:url value="sysService_list.action" ><s:param name="currentPage" value="currentPage"/><s:param name="pagerMethod" value="'first'"/></s:url>">首页</a>
    		<a href="<s:url value="sysService_list.action"><s:param name="currentPage" value="currentPage"/><s:param name="pagerMethod" value="'previous'"/></s:url>">上一页</a>
    		<a href="<s:url value="sysService_list.action"><s:param name="currentPage" value="currentPage"/><s:param name="pagerMethod" value="'next'"/></s:url>">下一页</a>
    		<a href="<s:url value="sysService_list.action"><s:param name="currentPage" value="currentPage"/><s:param name="pagerMethod" value="'last'"/></s:url>">尾页</a>    	
    	</td>
    </tr>	
</table>
</td></tr>
</table>
</s:form>
</body>
</html>
