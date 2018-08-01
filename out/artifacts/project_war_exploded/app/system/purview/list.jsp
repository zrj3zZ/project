<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head><title>权限组管理</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<link href="iwork_css/common.css" rel="stylesheet" type="text/css" />
		<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
        <link href="iwork_css/system/purgroup_list.css" rel="stylesheet" type="text/css" />
        <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/linkbutton.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
		<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
		<script language="javascript" src="iwork_js/commons.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>	    
        <script type="text/javascript" src="iwork_js/system/purgroup_list.js"></script>
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
					 addInfo(); return false;
				}
}); //快捷键

	</script>
</head>	
<body>
<table width="100%" border="0" align="left" cellpadding="0" cellspacing="0" >

<tr align="center" >
	<td  align="left" height="30" class="tools_nav">
		<table  border="0" >
					<tr  >
						<td>
							<a href='javascript:addInfo();' class="easyui-linkbutton" iconCls="icon-add" plain="true">增加</a>
							<a href='javascript:this.location.reload();' class="easyui-linkbutton" iconCls="icon-reload" plain="true">刷新</a>
						</td>
					</tr>
		</table>
	</td> 
</tr>
<tr><td align="center" > 
	<table cellpadding="1" cellspacing="1"  width="100%" border="0px" class="table3" >
    <s:iterator value="availableItems" status="status">
        <tr  class="tr1">
	        <td width="36" class="RowData2">
	         <s:property value="#status.count"/>
	        </td>
	        <td  valign="top" class="RowData2"><s:property value="categoryname" /></td>
            <td  valign="top" class="RowData2">
            <a  href='javascript:editInfo(<s:property value="id"/>);' title='<s:property value="groupdesc" />' >
           	 <img alt="权限组" src="iwork_img/shield.png" border="0"><s:property value="groupname" />
            </a> 
            </td>
            </tr>
            <tr>
            <td  valign="top" class="RowData2"><a  href='javascript:moduleAuthority(<s:property value="id"/>);'><img alt="授权模块" src="iwork_img/shield_go.png" border="0">授权模块</a>
            &nbsp;&nbsp;&nbsp;<a  href='javascript:operationAuthority(<s:property value="id"/>);'><img alt="授权操作" src="iwork_img/shield_go.png" border="0">授权操作</a>
            &nbsp;&nbsp;&nbsp;<a  href='javascript:processAuthority(<s:property value="id"/>);'><img alt="授权流程" src="iwork_img/shield_go.png" border="0">授权流程</a>
            &nbsp;&nbsp;&nbsp;<a  href='javascript:roleAuthority(<s:property value="id"/>);'><img alt="授权角色" src="iwork_img/shield_go.png" border="0">授权角色</a>
            &nbsp;&nbsp;&nbsp;<a  href='javascript:userAuthority(<s:property value="id"/>);'><img alt="授权用户" src="iwork_img/shield_go.png" border="0">授权用户</a>
            &nbsp;&nbsp;&nbsp;<a  href='javascript:deptAuthority(<s:property value="id"/>);'><img alt="授权部门" src="iwork_img/shield_go.png" border="0">授权部门</a>
</td> 
             <td align="left" class="RowData2">
            	<a  href='javascript:editInfo(<s:property value="id"/>);'  > <img alt="编辑" src="iwork_img/but_edit.gif" border="0"></a> 
            	<a href='javascript:confirm1(<s:property value="id"/>);'> <img alt="删除" src="iwork_img/but_delete.gif" border="0"></a>
            	<s:if test="#status.first==true">
            	    <a href='<s:url action="purgroup_movedown" ><s:param name="id" value="id" /></s:url>'> <img alt="向下" src="iwork_img/but_down.gif" border="0"></a>
            	</s:if>
            	<s:elseif test="#status.last==true" >
            		<a href='<s:url action="purgroup_moveup" ><s:param name="id" value="id" /></s:url>'> <img alt="向上" src="iwork_img/btn_up.gif" border="0"></a>
            	</s:elseif>
            	<s:else>
            		<a href='<s:url action="purgroup_moveup" ><s:param name="id" value="id" /></s:url>'> <img alt="向上" src="iwork_img/btn_up.gif" border="0"></a>
            		<a href='<s:url action="purgroup_movedown" ><s:param name="id" value="id" /></s:url>'> <img alt="向下" src="iwork_img/but_down.gif" border="0"></a>
            	</s:else>  
            	
            </td>
        </tr>
    </s:iterator>
    <tr align="right"> 
    	<td colspan="6" class="STYLE1">
    		共<s:property value="totalRows"/>行&nbsp;
    		第<s:property value="currentPage"/>页&nbsp;
    		共<s:property value="pager.getTotalPages()"/>页&nbsp;
    		<a href="<s:url value="purgroup_list.action" >
    			<s:param name="currentPage" value="currentPage"/>
    			<s:param name="pagerMethod" value="'first'"/>
    			
    		</s:url>">首页</a>
    		<a href="<s:url value="purgroup_list.action">
    			<s:param name="currentPage" value="currentPage"/>
    			<s:param name="pagerMethod" value="previous"/>
    		</s:url>">上一页</a>
    		<a href="<s:url value="purgroup_list.action">
    			<s:param name="currentPage" value="currentPage"/>
    			<s:param name="pagerMethod" value="next"/>
    		</s:url>">下一页</a>
    		<a href="<s:url value="purgroup_list.action">
    			<s:param name="currentPage" value="currentPage"/>
    			<s:param name="pagerMethod" value="last"/>
    		</s:url>">尾页</a>    	</td>
    </tr>	
</table>
</td></tr>
</table>
</body>
</html>
