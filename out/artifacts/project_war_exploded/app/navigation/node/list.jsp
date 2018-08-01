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
<head>
<title>目录管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/linkbutton.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link href="iwork_css/navigation/function_list.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="iwork_js/navigation/function_list.js"></script>
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script>
    function add(){
    		var pageUrl = ($('#addUrl').val());
    		 art.dialog.open(pageUrl,{
			  id:'dg_editInfo',  
			  title:'新建菜单',
				lock:true,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:600,
			    height:410,
		        close:function(){
       		 		window.location.reload();
		  		}
			 }); 
	}
	function edit(id){
			var pageUrl = "sysNode_edit.action?id="+id;
			art.dialog.open(pageUrl,{
			  id:'dg_editInfo',  
			  title:'编辑菜单',
				lock:true,
				background: '#999', // 背景色
			    opacity: 0.87,	// 透明度
			    width:600,
			    height:410,
		        close:function(){
       		 		window.location.reload();
		  		}
			 }); 
			 
		}
	function confirm1(id){
		art.dialog.confirm('确认删除?', function(r){
			if (r){	
					var url = "sysNode_delete.action";
					$.post(url,{id:id},function(data){
				         location.reload(true);
				    });
			}
		});
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
					 add(); return false;
				}
}); //快捷键
	</script>
	<style type="text/css">
		.header th{
			font-weight:bold;
			font-size:12px;
			padding:3px;
			white-space:nowrap;
			padding-left:5px;
			background:url('iwork_css/jquerycss/default/images/datagrid_header_bg.gif') repeat-x left bottom;
			border-bottom:1px dotted #ccc;
			border-top:1px dotted #fff;
			border-right:1px dotted #ccc;
		}
		.cell td{
					margin:0;
					padding:3px 4px;
					font-size:12px;
					overflow:hidden;
					text-align:left;
					border-bottom:1px dotted #eee;
					border-top:1px dotted #fff;
					border-right:1px dotted #ccc;
					font-family:宋体 avril;
				}
	</style>
	
</head>	
<body  class="easyui-layout">
<div region="north" border="false" style="height:40px;border-bottom:1px;">
<table  border="0" width="100%" class="tools_nav">
					<tr  >
						<td>
							<a href="javascript:add();" class="easyui-linkbutton"  plain="true" iconCls="icon-add" >新增</a>
							<a href="javascript:this.location.reload();" class="easyui-linkbutton"  plain="true" iconCls="icon-reload" >刷新</a>
						</td>
						
					</tr>
		</table>
</div>
<div region="center" style="padding:0px;border:0px;border-left:0px solid #efefef">
	<table  width="100%" cellspacing="0" cellpadding="0" border="0" style="border:1px solid #efefef">
    <tr class="header" >
        <th  width="30">序号</th>
        <th width="200"  >模块名称</th> 
        <th width="32" >ICON</th> 
        <th width="240"  >URL</th>
        <th width="69"  >提交目标</th>
        <th width="163"  >操作</th>
    </tr>
    <tbody>
    <s:iterator value="childenList" status="status">
        <tr  class="cell">
         <td>
            <s:property value="id"/>
          </td>
            <td class="itemlist" align="center">
            <a href='javascript:edit(<s:property value="id"/>);'>
            <s:property value="nodeName"/>
            </a>
            </td>
            <td class="itemlist" align="center">
           	<s:if test="nodeIcon!=null&&nodeIcon!=''">
         	  	<img src='iwork_img/adds.gif' border="0" onerror="this.src='iwork_img/adds.gif'">
           	</s:if>
           	<s:else>
           		<img src='iwork_img/adds.gif' border="0" >
           	</s:else>
            
            
            </td>
            <td class="itemlist" align="left"><s:property value="nodeUrl"/></td>
            <td class="itemlist" align="center"><s:property value="nodeTarget" /></td>
            <td align="left" class="itemlist">
            	<a href='javascript:edit(<s:property value="id"/>);' rel='gb_page_center[540, 380]'> <img alt="编辑" src="iwork_img/but_edit.gif" border="0"></a> 
            	<a href='javascript:confirm1(<s:property value="id"/>);'> <img alt="删除" src="iwork_img/but_delete.gif" border="0"></a>
            	<s:if test="#status.first==true">
            	    <a href='<s:url action="sysNode_moveDown" ><s:param name="id" value="id" /></s:url>'> <img alt="向下" src="iwork_img/but_down.gif" border="0"></a>
            	</s:if>
            	<s:elseif test="#status.last==true" >
            		<a href='<s:url action="sysNode_moveUp" ><s:param name="id" value="id" /></s:url>'> <img alt="向上" src="iwork_img/btn_up.gif" border="0"></a>
            	</s:elseif>
            	<s:else>
            		<a href='<s:url action="sysNode_moveUp" ><s:param name="id" value="id" /></s:url>'> <img alt="向上" src="iwork_img/btn_up.gif" border="0"></a>
            		<a href='<s:url action="sysNode_moveDown" ><s:param name="id" value="id" /></s:url>'> <img alt="向下" src="iwork_img/but_down.gif" border="0"></a>
            	</s:else>  
            </td>
        </tr>
    </s:iterator>
    	<tr align="right">
    	<td colspan="9">&nbsp;</td>
   		</tr>	
    </tbody>
</table>
<input type="hidden" value="<s:url action="sysNode_add" ><s:param name="systemId"/></s:url>" id="addUrl" > 
<s:hidden name="systemId"/>
</div>


</body>
</html>
