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
<head><title>部门管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link href="iwork_css/org/department_list.css" rel="stylesheet" type="text/css" />	
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/linkbutton.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script language="javascript" src="iwork_js/commons.js"></script>
<script language="javascript" src="iwork_js/org/department_list.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script language="JavaScript">   
		window.onload=delAlert;
		function delAlert(){
			var delMsg = '<s:property value="delInfo"  escapeHtml="false"/>';
			if(delMsg == "1"){
				return false;
			}else if(delMsg == "2"){
				art.dialog.tips('您删除的部门由于还存在子部门不能强制删除!');
			}else if(delMsg == "3"){
				art.dialog.tips('您删除的信息不存在!');
			}else if(delMsg == "4"){
				art.dialog.tips('参数异常，请联系管理员!');
			}else if(delMsg == "5"){
				art.dialog.tips('您删除的部门由于还存在用户不能强制删除!');
			}else{
				return false;
			}
		}
	</script>
</head>
<body>
	<table cellpadding="0" cellspacing="0" class="table3" >
    <tr>
        <th  width="10"   align="left"  >ID</th>
        <th width="200"   align="left" >名称</th> 
        <th width="10"   align="left"  >类型</th> 
         <th width="100" align="left"  >编号</th>
        <th width="163"  align="left"  >操作</th>
    </tr>
    
     <s:iterator value="companyList" status="status">
      <tr class="tr1">
        <td  width="10"><s:property value="id"/></td>
       
        <td width="200"  > 
         <a href='javascript:editcompany(<s:property value="id"/>);'  class="easyui-linkbutton" plain="true" iconCls="icon-max">
        <s:property value="companyname"/>
        </a>
        </td> 
         <td  width="10">组织</td>
         <td width="100" ><s:property value="companyno"/></td>
        <td width="163"  >
            	<a href="javascript:deleteCompany(<s:property value="id"/>);"> 删除 </a> 
            	<s:if test="#status.first==true">
           			<a href='<s:url action="company_movedown" ><s:param name="id" value="id" /></s:url>'> <img alt="向下" src="iwork_img/but_down.gif" border="0"></a>
           		</s:if>
           		<s:elseif test="#status.last==true" >
           			<a href='<s:url action="company_moveup" ><s:param name="id" value="id" /></s:url>'> <img alt="向上" src="iwork_img/btn_up.gif" border="0"></a>
           		</s:elseif>
           		<s:else>
	           		<a href='<s:url action="company_moveup" ><s:param name="id" value="id" /></s:url>'> <img alt="向上" src="iwork_img/btn_up.gif" border="0"></a> 
	            	<a href='<s:url action="company_movedown" ><s:param name="id" value="id" /></s:url>'> <img alt="向下" src="iwork_img/but_down.gif" border="0"></a> 
           		</s:else>
        </td>
    </tr>
     </s:iterator>
    <s:iterator value="availableItems" status="status">
       <s:if test="departmentstate==0"> <!--判断是否为注销部门  -->
        <tr class="tr1">
         <td  >
           <a href='javascript:editDept(<s:property value="id"/>);'>
           <s:property value="id"/>
            </a>
            </td>
            <td  >
            <a href='javascript:editDept(<s:property value="id"/>);'  class="easyui-linkbutton" plain="true" iconCls="icon-deptbook">
            <s:property value="departmentname"/>
            </a>
            </td>
             <td  width="70">部门</td>
             <td  >
           <a href='javascript:editDept(<s:property value="id"/>);' >
           <s:property value="departmentno"/>
            </a>
            </td>
            <td align="left" >
            	<a href='javascript:confirm1(<s:property value="id"/>);'> 删除</a> 
           	<s:if test="#status.first==true">
            	    <a href='<s:url action="department_movedown" ><s:param name="id" value="id" /></s:url>'> <img alt="向下" src="iwork_img/but_down.gif" border="0"></a>
            	</s:if>
            	<s:elseif test="#status.last==true" >
            		<a href='<s:url action="department_moveup" ><s:param name="id" value="id" /></s:url>'> <img alt="向上" src="iwork_img/btn_up.gif" border="0"></a>
            	</s:elseif>
            	<s:else>
            		<a href='<s:url action="department_moveup" ><s:param name="id" value="id" /></s:url>'> <img alt="向上" src="iwork_img/btn_up.gif" border="0"></a>
            		<a href='<s:url action="department_movedown" ><s:param name="id" value="id" /></s:url>'> <img alt="向下" src="iwork_img/but_down.gif" border="0"></a>
            	</s:else> 	
           	</td>
        </tr>
        </s:if>
        <s:else>
        	<tr class="tr1" title="部门已注销" style="background:#efefef">
          <td  >
           <a href='javascript:editDept(<s:property value="id"/>);' style="color:#999999">
           <s:property value="id"/>
            </a>
            </td>
              <td  style="color:#999999" >
            <a href='javascript:editDept(<s:property value="id"/>);'  class="easyui-linkbutton" plain="true" iconCls="icon-deptbook" style="color:#999999">
            <s:property value="departmentname"/>
            </a>
            </td>
            <td  width="70">部门</td>
           <td  >
           <a href='javascript:editDept(<s:property value="id"/>);'  style="color:#999999">
           <s:property value="departmentno"/>
            </a>
            </td>
          
            <td align="left" > 
            	<a href='javascript:confirm1(<s:property value="id"/>);'>删除</a> 
           	<s:if test="#status.first==true">
            	    <a href='<s:url action="department_movedown" ><s:param name="id" value="id" /></s:url>'> <img alt="向下" src="iwork_img/but_down.gif" border="0"></a>
            	</s:if>
            	<s:elseif test="#status.last==true" >
            		<a href='<s:url action="department_moveup" ><s:param name="id" value="id" /></s:url>'> <img alt="向上" src="iwork_img/btn_up.gif" border="0"></a>
            	</s:elseif>
            	<s:else>
            		<a href='<s:url action="department_moveup" ><s:param name="id" value="id" /></s:url>'> <img alt="向上" src="iwork_img/btn_up.gif" border="0"></a>
            		<a href='<s:url action="department_movedown" ><s:param name="id" value="id" /></s:url>'> <img alt="向下" src="iwork_img/but_down.gif" border="0"></a>
            	</s:else> 	
           	</td>
        </tr>
        </s:else>
    </s:iterator>
</table>
<div class="page">
    		共<s:property value="totalRows"/>行&nbsp;
    		第<s:property value="currentPage"/>页&nbsp;
    		<s:if  test="pager!=null">
    			共<s:property value="pager.getTotalPages()"/>页&nbsp;
    		</s:if>
    		<a href="<s:url value="department_list.action">
    			<s:param name="currentPage" value="currentPage"/>
    			<s:param name="pagerMethod" value="'first'"/>
    			<s:param name="companyId" value="companyId"/>
    			<s:param name="parentdeptid" value="parentdeptid"/>
    			
    		</s:url>">首页</a>
    		<a href="<s:url value="department_list.action">
    			<s:param name="currentPage" value="currentPage"/>
    			<s:param name="pagerMethod" value="'previous'"/>
    			<s:param name="companyId" value="companyId"/>
    			<s:param name="parentdeptid" value="parentdeptid"/>
    		</s:url>">上一页</a>
    		<a href="<s:url value="department_list.action">
    			<s:param name="currentPage" value="currentPage"/>
    			<s:param name="pagerMethod" value="'next'"/>
    			<s:param name="companyId" value="companyId"/>
    			<s:param name="parentdeptid" value="parentdeptid"/>
    		</s:url>">下一页</a>
    		<a href="<s:url value="department_list.action">
    			<s:param name="currentPage" value="currentPage"/>
    			<s:param name="pagerMethod" value="'last'"/>
    			<s:param name="companyId" value="companyId"/>
    			<s:param name="parentdeptid" value="parentdeptid"/>
    		</s:url>">尾页</a>
</div>
</body>
</html>
