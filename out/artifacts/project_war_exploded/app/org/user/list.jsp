<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%><%@page import="com.iwork.app.navigation.directory.model.SysNavDirectory" %>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap" %>
<%@page import="com.iwork.app.navigation.sys.model.SysNavSystem" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Map" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head><title> 用户管理维护</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link href="iwork_css/org/user_list.css" rel="stylesheet" type="text/css" />

<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/linkbutton.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script language="javascript" src="iwork_js/commons.js"></script>
<script language="javascript" src="iwork_js/org/user_list.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script language="JavaScript">   		
		window.onload=delAlert;
		function delAlert(){
			var delMsg = '<s:property value="delInfo"  escapeHtml="false"/>';
			if(delMsg == "1"){
				return false;
			}else if(delMsg == "2"){
				art.dialog.tips('系统提示','您删除的用户由于还存在角色不能强制删除!',3);
			}else if(delMsg == "3"){
				art.dialog.tips('系统提示','您删除的信息不存在!',3);
			}else if(delMsg == "4"){
				art.dialog.tips('系统提示','参数异常，请联系管理员!',3);
			}else{
				return false;
			}
		}
 //==========================装载快捷键===============================//快捷键
		 jQuery(document).bind('keydown',function (evt){		
		    if(evt.ctrlKey&&evt.shiftKey){
			return false;
		   }
		   else if(evt.ctrlKey && event.keyCode==82){ //Ctrl+r /刷新操作
			         this.location.reload(); return false;
		     } 
		  else if(evt.shiftKey && event.keyCode==78){ //Shift+n 新增操作
					 this.location.href='user_add.action'; return false;
				}
}); //快捷键

	</script>
	<style type="text/css">
		.memo{
			color:#999;
		}
		.tablehead{
			text-align:left;
		}
	</style>
	</head>	
<body>
<form  method="post">
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr><td align="center" >
	<table cellpadding="1" cellspacing="1" class="table3" >
	<thead>
    <tr  class="tablehead" >
        <th  width="5%"  align="left" >ID</th>
        <th width="20%"  align="left"  >名称</th> 
         <th width="30%" align="left"  >类型</th>
         <th width="30%" align="left"  >描述</th>
        <th width="20%"  align="left"  >操作</th>
    </tr>
    </thead>
    <tbody>
     <s:iterator value="deptList" status="status">
	        <s:if test="departmentstate==0"> <!--判断是否为注销部门  -->
        <tr class="tr1">
         <td  >
           <a href='javascript:editDept(<s:property value="id"/>);'>
           <s:property value="id"/>
            </a>
            </td>
            <td  >
            <a href='javascript:editDept(<s:property value="id"/>);'  class="easyui-linkbutton" plain="true" iconCls="icon-deptbook">
            <s:property value="departmentname"/><span> [<s:property value="departmentno"/>]</span>
            </a>
            </td>
             <td  width="70">部门</td>
             <td  class="memo">
           			部门编号:<s:property value="departmentno"/>
           			部门描述:<s:property value="departmentdesc"/>
            </td>
            <td align="left" >
            	<a href='javascript:deleteDept(<s:property value="id"/>);'>删除</a> 
           	<s:if test="#status.first==true">
            	    <a href='javascript:moveDownDept(<s:property value="id"/>)'> <img alt="向下" src="iwork_img/but_down.gif" border="0"></a>
            	</s:if>
            	<s:elseif test="#status.last==true" >
            		<a href='javascript:moveUpDept(<s:property value="id"/>)'> <img alt="向上" src="iwork_img/btn_up.gif" border="0"></a>
            	</s:elseif>
            	<s:else>
            		<a href='javascript:moveUpDept(<s:property value="id"/>)'> <img alt="向上" src="iwork_img/btn_up.gif" border="0"></a>
            		<a href='javascript:moveDownDept(<s:property value="id"/>)'> <img alt="向下" src="iwork_img/but_down.gif" border="0"></a>
            	</s:else> 	
           	</td>
        </tr>
        </s:if>
        <s:else>
        	<tr class="tr1" title="部门已注销" style="background:#efefef">
         <td  >
            <s:property value="#status.count"/>
          </td>
          <td  >
          <a href='javascript:editDept(<s:property value="id"/>);'  class="easyui-linkbutton" plain="true" iconCls="icon-deptbook">
           <s:property value="id"/>
            </a>
            <td  style="color:#999999" >
           <a href='javascript:editDept(<s:property value="id"/>);'  class="easyui-linkbutton" plain="true" iconCls="icon-deptbook">
            <s:property value="departmentname"/>[<s:property value="departmentno"/>]
            </a>
            </td>
            
            <td align="left" > 
            	<a href='javascript:deleteDept(<s:property value="id"/>);'>删除</a> 
           			<%-- <s:if test="#status.first==true">
	           			<a href='<s:url action="user_movedown" ><s:param name="id" value="id" /></s:url>'> <img alt="向下" src="iwork_img/icon/arrow_down_blue.gif" border="0"></a>
	           			<a href='<s:url action="user_moveBottem" ><s:param name="id" value="id" /></s:url>'> <img alt="置底" src="iwork_img/icon/arrow_last.gif" border="0"></a>
	           		</s:if>
	           		<s:elseif test="#status.last==true" >
	           			<a href='<s:url action="user_moveup" ><s:param name="id" value="id" /></s:url>'> <img alt="向上" src="iwork_img/icon/arrow_up_blue.gif" border="0"></a>
	           			<a href='<s:url action="user_moveTop" ><s:param name="id" value="id" /></s:url>'> <img alt="置顶" src="iwork_img/icon/arrow_first.gif" border="0"></a>
	           		</s:elseif>
	           		<s:else>
		           		<a href='<s:url action="user_moveup" ><s:param name="id" value="id" /></s:url>'> <img alt="向上" src="iwork_img/icon/arrow_up_blue.gif" border="0"></a> 
		            	<a href='<s:url action="user_movedown" ><s:param name="id" value="id" /></s:url>'> <img alt="向下" src="iwork_img/icon/arrow_down_blue.gif" border="0"></a> 
		           		<a href='<s:url action="user_moveTop" ><s:param name="id" value="id" /></s:url>'> <img alt="置顶" src="iwork_img/icon/arrow_first.gif" border="0"></a>
		            	<a href='<s:url action="user_moveBottem" ><s:param name="id" value="id" /></s:url>'> <img alt="置底" src="iwork_img/icon/arrow_last.gif" border="0"></a>
	           		</s:else>	 --%>
           	</td>
        </tr>
        </s:else>
    </s:iterator>
    <s:iterator value="availableItems" status="status">
   		<s:if test="id!=''"> 
   			<s:if test="userstate==0"> <!--判断是否为注销用户  -->
	        <tr >
	         <td align="center" >
	            <s:property value="id"/>
	          </td>
	            <td align="left" >
	            <s:if test="ismanager==0">
	             <a href="javascript:editUser(<s:property value="departmentid"/>,'<s:property value="userid"/>')"  class="easyui-linkbutton" plain="true" iconCls="icon-org-user">
	           		 <s:property value="username"/> <span> [<s:property value="userid"/>]</span>
	            </a> 
	            </s:if>
	            <s:else>
	             <a href="javascript:editUser(<s:property value="departmentid"/>,'<s:property value="userid"/>')" class="easyui-linkbutton" plain="true" iconCls="icon-org-manager">
	            <s:property value="username"/> <span> [<s:property value="userid"/>]</span>
	            </a>
	            </s:else>
	            </td>
	                  <td  width="70">人员</td>
	            <td  class="memo">
	          		员工编号[<s:property value="userno"/>]
	          		有效期[<s:property value="%{getText('{0,date,yyyy-MM-dd }',{startdate})}"/>至<s:property value="%{getText('{0,date,yyyy-MM-dd }',{enddate})}"/>]
	            </td>
	            <td align="left" >
	            	<a href='javascript:deleteUser(<s:property value="id"/>);'>删除</a> 
	           		<%-- <s:if test="#status.first==true">
	           			<a href='<s:url action="user_movedown" ><s:param name="id" value="id" /></s:url>'> <img alt="向下" src="iwork_img/icon/arrow_down_blue.gif" border="0"></a>
	           			<a href='<s:url action="user_moveBottem" ><s:param name="id" value="id" /></s:url>'> <img alt="置底" src="iwork_img/icon/arrow_last.gif" border="0"></a>
	           		</s:if>
	           		<s:elseif test="#status.last==true" >
	           			<a href='<s:url action="user_moveup" ><s:param name="id" value="id" /></s:url>'> <img alt="向上" src="iwork_img/icon/arrow_up_blue.gif" border="0"></a>
	           			<a href='<s:url action="user_moveTop" ><s:param name="id" value="id" /></s:url>'> <img alt="置顶" src="iwork_img/icon/arrow_first.gif" border="0"></a>
	           		</s:elseif>
	           		<s:else>
		           		<a href='<s:url action="user_moveup" ><s:param name="id" value="id" /></s:url>'> <img alt="向上" src="iwork_img/icon/arrow_up_blue.gif" border="0"></a> 
		            	<a href='<s:url action="user_movedown" ><s:param name="id" value="id" /></s:url>'> <img alt="向下" src="iwork_img/icon/arrow_down_blue.gif" border="0"></a> 
		           		<a href='<s:url action="user_moveTop" ><s:param name="id" value="id" /></s:url>'> <img alt="置顶" src="iwork_img/icon/arrow_first.gif" border="0"></a>
		            	<a href='<s:url action="user_moveBottem" ><s:param name="id" value="id" /></s:url>'> <img alt="置底" src="iwork_img/icon/arrow_last.gif" border="0"></a>
	           		</s:else> --%>
	            </td>
	        </tr>
        </s:if>
        <s:else>
	        <tr  style="background-color:#efefef">
		         <td align="center" >
		            <s:property value="id"/>
		          </td>
		            <td align="left" >
		            <a title="注销用户" href="javascript:editUser(<s:property value="departmentid"/>,'<s:property value="userid"/>')" class="easyui-linkbutton" plain="true" iconCls="icon-remove">
		            <s:property value="username"/><span> [<s:property value="userid"/>]</span>[注销]
		            </a> 
		            </td>
	             <td  width="70">人员</td>
	              <td  class="memo">
	          		员工编号[<s:property value="userno"/>]
	          		有效期[<s:property value="%{getText('{0,date,yyyy-MM-dd }',{startdate})}"/>至<s:property value="%{getText('{0,date,yyyy-MM-dd }',{enddate})}"/>]
	            </td>
		            <td align="left" >  
	            	<a href='javascript:deleteUser(<s:property value="id"/>);'>删除</a> 
	           		<%-- <s:if test="#status.first==true">
	           			<a href='<s:url action="user_movedown" ><s:param name="id" value="id" /></s:url>'> <img alt="向下" src="iwork_img/icon/arrow_down_blue.gif" border="0"></a>
	           			<a href='<s:url action="user_moveBottem" ><s:param name="id" value="id" /></s:url>'> <img alt="置底" src="iwork_img/icon/arrow_last.gif" border="0"></a>
	           		</s:if>
	           		<s:elseif test="#status.last==true" >
	           			<a href='<s:url action="user_moveup" ><s:param name="id" value="id" /></s:url>'> <img alt="向上" src="iwork_img/icon/arrow_up_blue.gif" border="0"></a>
	           			<a href='<s:url action="user_moveTop" ><s:param name="id" value="id" /></s:url>'> <img alt="置顶" src="iwork_img/icon/arrow_first.gif" border="0"></a>
	           		</s:elseif>
	           		<s:else>
		           		<a href='<s:url action="user_moveup" ><s:param name="id" value="id" /></s:url>'> <img alt="向上" src="iwork_img/icon/arrow_up_blue.gif" border="0"></a> 
		            	<a href='<s:url action="user_movedown" ><s:param name="id" value="id" /></s:url>'> <img alt="向下" src="iwork_img/icon/arrow_down_blue.gif" border="0"></a> 
		           		<a href='<s:url action="user_moveTop" ><s:param name="id" value="id" /></s:url>'> <img alt="置顶" src="iwork_img/icon/arrow_first.gif" border="0"></a>
		            	<a href='<s:url action="user_moveBottem" ><s:param name="id" value="id" /></s:url>'> <img alt="置底" src="iwork_img/icon/arrow_last.gif" border="0"></a>
	           		</s:else> --%>
	            </td>
		        </tr>
        </s:else>
   		</s:if>
    </s:iterator>
    
    <s:iterator value="userMapList" status="status">
    	<s:if test="userstate==0"> <!--判断是否为注销用户  -->
	        <tr >
	         <td align="center" >
	            <s:property value="id"/>
	          </td>
	            <td align="left" >
		            <a title="兼职用户" href="javascript:editUser(<s:property value="departmentid"/>,'<s:property value="userid"/>')"  class="easyui-linkbutton" plain="true" iconCls="icon-radiobook">
		            <s:property value="username"/><span> [<s:property value="userid"/>]</span>
		            </a>
	            </td>
	                <td  width="70">人员[兼职]</td>
	             <td  class="memo">
	          		员工编号[<s:property value="userno"/>]
	          		有效期[<s:property value="%{getText('{0,date,yyyy-MM-dd }',{startdate})}"/>至<s:property value="%{getText('{0,date,yyyy-MM-dd }',{enddate})}"/>]
	            </td>
	            <td>
	            </td>
	        </tr>
        </s:if>
        <s:else>
	        <tr  style="background-color:#efefef">
		         <td align="center" >
		            <s:property value="id"/>
		          </td>
		            <td align="left" >
		             <a  style="color:#999999" title="兼职用户" href="javascript:editUser(<s:property value="departmentid"/>,'<s:property value="userid"/>')">
		            <IMG alt="兼职用户" src="iwork_img/user_silhouette.png" border=0/>
		             <s:property value="username"/><span> [<s:property value="userid"/>]</span>[已注销]
		            </a>
		            </td>
		             <td  width="70">人员[兼职]</td>
		            <td  class="memo">
	          		员工编号[<s:property value="userno"/>]
	          		有效期[<s:property value="%{getText('{0,date,yyyy-MM-dd }',{startdate})}"/>至<s:property value="%{getText('{0,date,yyyy-MM-dd }',{enddate})}"/>]
	            </td>
		        </tr>
        </s:else>
    </s:iterator>
 
    </tbody>
</table>
</form>
</body>
</html>
