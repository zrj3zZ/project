<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
	<title>权限组授权</title>
	<s:head/>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<link href="iwork_css/common.css" rel="stylesheet" type="text/css" />
		<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
		<link href="iwork_css/system/limits.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/engine/design_model.css"> 
		<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
		<script language="javascript" src="iwork_js/system/openpurview_web.js"></script>
		<script language="javascript" src="iwork_js/sys_purviewList.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>	
		<script type="text/javascript" src="iwork_js/system/purgroup_list.js"></script>
		<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
		<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
		<body  class="easyui-layout">
		<div region="north" border="false" split="false" > 
			<div class="tools_nav">
				<table width="100%">
					<tr>
						<td>
							<a href='javascript:addInfo();' class="easyui-linkbutton" iconCls="icon-add" plain="true">新增</a>
							<a href='javascript:this.location.reload();' class="easyui-linkbutton" iconCls="icon-reload" plain="true">刷新</a>
						</td>
						<td>
						<s:form id="editForm" name="editForm" theme="simple">
							<span class="search_btn" id="search_btn">
									 <s:textfield name="searchTxt" id="searchTxt" theme="simple" value="%{searchkey}" cssClass="search_input" onkeydown="enterKey();if(window.event.keyCode==13) return false;" ></s:textfield>
									  <input  onclick="doSearch();" type="button" class="search_button" value="&nbsp;"/> 
							 </span> 
							 </s:form>
							
						</td>
					</tr>
				</table>
			        
			</div>
		</div>
		<div region="west"   border="false"  region="west" icon="icon-reload"  split="true" style="width:200px;padding-left:5px;overflow:hidden;border-right:1px solid #efefef">
			<s:property value='tablist' escapeHtml="false"/>
		</div> 
		<div region="center" border="false" >
		
		    <s:iterator value="availableItems" status="status">
		    <div class="limits_box" onmousemove="this.className='limits_box_selected'" onmouseout="this.className='limits_box'">
		        <div >
		        <table width="100%">
		        	<tr>
		        		<td class="limits_box_title"><s:property value="groupname" /></td>
		        		<td style="text-align:right;padding-right:5px;"><a href="javascript:editInfo(<s:property value="id"/>)">编辑</a>&nbsp;&nbsp;<a href="javascript:confirm1(<s:property value="id"/>)">删除</a></td>
		        	</tr>
		        </table></div>
				  <div class="limits_box_main"> 
				    <ul>
				      <li><a href='javascript:moduleAuthority(<s:property value="id"/>);'>▪ 授权模块</a></li>
				      <li><a  href='javascript:processAuthority(<s:property value="id"/>);'>▪ 授权流程</a></li>
				      <li><a href='javascript:roleAuthority(<s:property value="id"/>);'>▪ 授权角色</a></li>
				      <li><a href='javascript:userAuthority(<s:property value="id"/>);'>▪ 授权用户</a></li>
				      <li><a href='javascript:deptAuthority(<s:property value="id"/>);'>▪ 授权部门</a></li>
				      <li><a href='javascript:demAuthority(<s:property value="id"/>);'>▪ 授权数据维护</a></li> 
				      <li><a href='javascript:reportAuthority(<s:property value="id"/>);'>▪ 授权报表</a></li> 
				       
				    </ul>
				  </div>
				  </div>
		    </s:iterator>
		</div>
		</body>
</html>
<script type="text/javascript">
<!--
	setTimeout("expandAll()",200);
//-->
</script>
