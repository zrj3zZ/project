<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
<title>目录管理</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
        <link href="iwork_css/navigation/directory_list.css" rel="stylesheet" type="text/css" />
        <link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
        <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
		<script language="javascript" src="iwork_js/commons.js"></script> 
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	    <script type="text/javascript" src="iwork_js/navigation/directory_list.js"></script>
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
					 add(); return false;
				}
}); //快捷键
    function add(){
    	var pageUrl = "directory_add.action";
    		art.dialog.open(pageUrl,{
						id:'directory_add',
						title:'增加',
						lock:true,
						background: '#999', // 背景色
						opacity: 0.87,	// 透明度
						width:600,
						height:410
					 });
	}
	function edit(id){
			var pageUrl = "directory_edit.action?id="+id;
			art.dialog.open(pageUrl,{
						id:'directory_add',
						title:'编辑',
						lock:true,
						background: '#999', // 背景色
						opacity: 0.87,	// 透明度
						width:600,
						height:410
					 });
		}
	function showfunction(id){
			var url = "function_list.action?directoryId="+id;
			this.location = url;
		}
	function confirm1(id){
			var s = ($('#addUrl').val());
    		ss = s.split("=");
			art.dialog.confirm('确认删除?', function(r){
				if (r){										
					location.href="directory_delete.action?queryName=SYSTEM_ID&queryValue="+ss[2]+"&id="+id;
					return false;
				}
			}); 
		}
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
				}
	</style>
</head>	
<body  class="easyui-layout" >
<div region="north" border="false" split="false"  style="height:45px;" id="layoutNorth">
	<div class="toolbar">
							<a href="javascript:add();" class="easyui-linkbutton"  plain="true" iconCls="icon-add" >新增</a>
							<a href="javascript:this.location.reload();" class="easyui-linkbutton"  plain="true" iconCls="icon-reload" >刷新</a>
	</div>				
</div>
<div region="center" border="false" style="border-left:1px solid #efefef;background:#fff" id="layoutCenter" >
	<table cellpadding="1" cellspacing="1" class="table3" style="width:96%" >
	<thead>
    <tr  class="header" >
        <th  width="39">序号</th>
        <th width="279"  >目录名称1</th> 
        <th width="232" >ICON</th>
        <th width="240"  >URL</th>
        <th width="169"  >提交目标</th>
        <th width="163"  >操作</th>
    </tr>
    </thead>
    <tbody>
    <s:iterator value="availableItems" status="status">
        <tr class="cell">
         <td class="RowData2">
            <s:property value="#status.count"/>
          </td>
            <td class="RowData2">
            <a href='javascript:showfunction(<s:property value="id"/>);'>
            <s:property value="directoryName"/>
            </a>
            </td>
            <td class="RowData2"><s:property value="directoryIcon"/></td>
            <td class="RowData2"><s:property value="directoryUrl"/></td>
            <td class="RowData2"><s:property value="directoryTarget" /></td>
            <td align="left" class="RowData2">
            	<a href='javascript:edit(<s:property value="id"/>);'> <img alt="编辑" src="iwork_img/but_edit.gif" border="0"></a> 
           <!--  	<a href='javascript:confirm1(<s:property value="id"/>);'> <img alt="删除" src="iwork_img/but_delete.gif" border="0"></a> -->
            	<s:if test="#status.first==true">
            	    <a href='<s:url action="directory_movedown" ><s:param name="id" value="id" /></s:url>'> <img alt="向下" src="iwork_img/but_down.gif" border="0"></a>
            	</s:if>
            	<s:elseif test="#status.last==true" >
            		<a href='<s:url action="directory_moveup" ><s:param name="id" value="id" /></s:url>'> <img alt="向上" src="iwork_img/btn_up.gif" border="0"></a>
            	</s:elseif>
            	<s:else>
            		<a href='<s:url action="directory_moveup" ><s:param name="id" value="id" /></s:url>'> <img alt="向上" src="iwork_img/btn_up.gif" border="0"></a>
            		<a href='<s:url action="directory_movedown" ><s:param name="id" value="id" /></s:url>'> <img alt="向下" src="iwork_img/but_down.gif" border="0"></a>
            	</s:else> 
            </td>
        </tr>
    </s:iterator>
    <tr align="right">
    	<td colspan="9">
    		共<s:property value="totalRows"/>行&nbsp;
    		第<s:property value="currentPage"/>页&nbsp;
    		共<s:property value="pager.getTotalPages()"/>页&nbsp;
    		<a href="<s:url value="directory_list.action">
    			<s:param name="currentPage" value="currentPage"/>
    			<s:param name="pagerMethod" value="'first'"/>
    			
    		</s:url>">首页</a>
    		<a href="<s:url value="directory_list.action">
    			<s:param name="currentPage" value="currentPage"/>
    			<s:param name="pagerMethod" value="'previous'"/>
    		</s:url>">上一页</a>
    		<a href="<s:url value="directory_list.action">
    			<s:param name="currentPage" value="currentPage"/>
    			<s:param name="pagerMethod" value="'next'"/>
    		</s:url>">下一页</a>
    		<a href="<s:url value="directory_list.action">
    			<s:param name="currentPage" value="currentPage"/>
    			<s:param name="pagerMethod" value="'last'"/>
    		</s:url>">尾页</a>
    	</td>
    </tr>	
    </tbody>
</table>
<input type="text" value="<s:url action="directory_add" ></s:url>" id="addUrl" style="display:none">
</div>
</body>
</html>
