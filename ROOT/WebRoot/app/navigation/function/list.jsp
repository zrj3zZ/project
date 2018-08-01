<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.iwork.app.navigation.directory.model.SysNavDirectory" %>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap" %>
<%@page import="com.iwork.app.navigation.sys.model.SysNavSystem" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Map" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<title>目录管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link href="iwork_css/navigation/function_list.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
  <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
<script language="javascript" src="iwork_js/navigation/function_list.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script>
    function add(){
    		var dirId = $("#function_list_directoryId").val();
    		var pageUrl = "function_add.action?directoryId="+dirId;
    		art.dialog.open(pageUrl,{
						id:'function_add',
						title:'新增',
						lock:true,
						background: '#999', // 背景色
						opacity: 0.87,	// 透明度
						width:600,
						height:410
					 });
			
	}
	function edit(id){
		var pageUrl = "function_edit.action?id="+id;
			art.dialog.open(pageUrl,{
						id:'function_edit',
						title:'编辑',
						lock:true,
						background: '#999', // 背景色
						opacity: 0.87,	// 透明度
						width:600,
						height:410
					 });
		}
	function confirm1(id){
			art.dialog.confirm('确认删除?', function(r){
				if (r){										
					location.href="function_delete.action?id="+id;
					return false;
				}
			});
	} 
	function showDir(){
		this.location="directory_list.action";
	}
    jQuery(document).bind('keydown',function (evt){		
		    if(evt.ctrlKey&&evt.shiftKey){
			return false;
		   }
		   else if(evt.ctrlKey && event.keyCode==82){ //Ctrl+r /刷新操作
			         this.location.reload(); return false;
		     } 
		  else if(evt.shiftKey && event.keyCode==78){ //Shift+n 新增操作
					 add(); return false;
				}
}); //快捷键
	</script>
</head>	
<body  class="easyui-layout" >
<div region="north" border="false" split="false"  style="height:45px;" id="layoutNorth">
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr>
	<td align="left" > 
		<div class="toolbar">
<a href="javascript:add();" class="easyui-linkbutton"  plain="true" iconCls="icon-add" >新增</a>
							<a href="javascript:this.location.reload();" class="easyui-linkbutton"  plain="true" iconCls="icon-reload" >刷新</a>
							<a href="javascript:showDir();" class="easyui-linkbutton"  plain="true" iconCls="icon-back" >返回上级</a>
	</div>	
	</td>
</tr>
</table>
</div>
<div region="center" border="false" style="border-left:1px solid #efefef;background:#fff" id="layoutCenter" >
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr><td align="center" >
	<table cellpadding="1" cellspacing="1" class="table3" >
	<thead>
    <tr class="cell">
        <th  width="39">序号</th>
        <th width="279"  >模块名称</th> 
        <th width="232" >ICON</th>
        <th width="240"  >URL</th>
        <th width="169"  >提交目标</th>
        <th width="163"  >操作</th>
    </tr>
    </thead>
    <tbody>
    <s:iterator value="list" status="status">
        <tr class="cell">
         <td class="RowData2">
            <s:property value="id"/>
          </td>
            <td class="RowData2">
            <a href='javascript:edit(<s:property value="id"/>);'>
            <s:property value="functionName"/>
            </a>
            </td> 
            <td class="RowData2"><s:property value="functionIcon"/></td>
            <td class="RowData2"><s:property value="functionUrl"/></td>
            <td class="RowData2"><s:property value="functionTarget" /></td>
            <td align="left" class="RowData2">
            	<a href='javascript:edit(<s:property value="id"/>);' > <img alt="编辑" src="iwork_img/but_edit.gif" border="0"></a> 
            <!-- 	<a href='javascript:confirm1(<s:property value="id"/>);'> <img alt="删除" src="iwork_img/but_delete.gif" border="0"></a> -->
            	<s:if test="#status.first==true">
            	    <a href='javascript:moveDown(<s:property value="id"/>)'> <img alt="向下" src="iwork_img/but_down.gif" border="0"></a>
            	</s:if>
            	<s:elseif test="#status.last==true" >
            		<a  href='javascript:moveUp(<s:property value="id"/>)'> <img alt="向上" src="iwork_img/btn_up.gif" border="0"></a>
            	</s:elseif>
            	<s:else>
            		<a href='javascript:moveUp(<s:property value="id"/>)'> <img alt="向上" src="iwork_img/btn_up.gif" border="0"></a>
            		<a  href='javascript:moveDown(<s:property value="id"/>)'> <img alt="向下" src="iwork_img/but_down.gif" border="0"></a>
            	</s:else>  
            </td>
        </tr>
    </s:iterator>
    </tbody>
</table>
</td></tr>
</table>
<s:form>
	<s:hidden name="directoryId"></s:hidden>
</s:form>
</div>
</body>
</html>
