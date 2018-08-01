<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head><title>OA知道-分类管理</title>
  <link href="iwork_css/public.css" rel="stylesheet" type="text/css" />				
        <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
        <link href="iwork_css/public.css" rel="stylesheet" type="text/css" />		
        <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/linkbutton.css">
        <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
		<script language="javascript" src="iwork_js/commons.js"></script>
	    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
        <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	    <script type="text/javascript" src="iwork_js/km/know_bigclass_manager.js"></script>
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
	</script>
	<style type="text/css">
		.tablehead{
			background:url(../iwork_img/desk/process_nav_bg.png) repeat-x;
			line-height:32px;
			height:32px;
			clear:both;
			width:100%;
			border-bottom:1px solid #ccc;
		}
		.tablehead th{
			text-align:left;
			padding:2px;
		}
		.tr1{
			line-height:30px;
			padding:2px;
			border-bottom:1px solid #efefef;
		}
		.tr1:hover{
			background-color:#f6f6f6;
			cursor: pointer;
		}
	</style>
</head>	
<body>
<table width="100%" border="0" align="left" cellpadding="0" cellspacing="0" >
<tr align="center" >
	<td  align="left" height="30" class="tools_nav">
		<a href="javascript:add();" class="easyui-linkbutton"  iconCls="icon-add" plain="true">增加</a>
					<a href="javascript:this.location.reload();" class="easyui-linkbutton"  iconCls="icon-reload" plain="true">刷新</a>
	</td> 
</tr>
<tr>
  <td align="center"> 
	<table cellpadding="1" cellspacing="1"  width="100%"  class="table3" >
	<tr  class="tablehead"  >
		<th width="91" style="text-align: left;">序号</th>
		<th width="353" style="text-align: left;">分类名称</th> 
		<!--<th width="91">分类排序</th>-->
		<th width="568" style="text-align: left;">分类状态</th>
		<th width="273" style="text-align: left;">操作</th>
	</tr>
    <s:iterator value="classList" status="status">
        <tr  class="tr1">
	        <td width="36" class="RowData2" style="text-align: left;"><s:property value="#status.count"/></td>
            <td valign="top" class="RowData2" style="text-align: left;">
               <a href='javascript:edit(<s:property value="id"/>);'><s:property value="cname" /></a>
            </td>
            <!--<td  valign="top" class="RowData2"><s:property value="corder" /></td>-->
            <td valign="top" class="RowData2" style="text-align: left;">
            	<s:if test="ctype=='可用'">
        	        <a href="javascript:stop_confirm(<s:property value='id'/>);"><img title="当前可用状态" src="iwork_img/osprc_obj.gif" border="0"></a>
        	      </s:if>
        	      <s:elseif test="ctype=='停用'">
        	        <a href="javascript:start_confirm(<s:property value='id'/>);"><img title="当前停用状态" src="iwork_img/osprct_obj.gif" border="0"></a>
        	      </s:elseif> 
            </td>
             <td class="RowData2" style="text-align: left;">
        	      	 <a href='javascript:edit(<s:property value="id"/>);'><img alt="编辑" title="编辑" src="iwork_img/but_edit.gif" border="0"></a>
        	      	 <a href='javascript:delConfirm(<s:property value="id"/>);'><img alt="删除" title="删除" src="iwork_img/but_delete.gif" border="0"></a>
        	      	 <a href='javascript:showExperts(<s:property value="id"/>);'><img alt="查看分类专家" title="查看分类专家" src="iwork_img/shield_go.png" border="0"></a>
        	         
        	      <s:if test="#status.first==true">
            	      <a href='<s:url action="know_bigclass_orderDown" ><s:param name="bcid" value="id"/></s:url>'> <img alt="下移" title="下移" src="iwork_img/but_down.gif" border="0"></a>
            	  </s:if>
            	  <s:elseif test="#status.last==true" >
            		  <a href='<s:url action="know_bigclass_orderUp"  ><s:param name="bcid" value="id"/></s:url>'> <img alt="上移"  title="上移" src="iwork_img/btn_up.gif" border="0"></a>
            	  </s:elseif>
            	  <s:else>
            		  <a href='<s:url action="know_bigclass_orderUp"  ><s:param name="bcid" value="id"/></s:url>'> <img alt="上移" title="上移" src="iwork_img/btn_up.gif" border="0"></a>
            		  <a href='<s:url action="know_bigclass_orderDown"><s:param name="bcid" value="id"/></s:url>'> <img alt="下移" title="下移" src="iwork_img/but_down.gif" border="0"></a>
            	  </s:else> 
            </td>
        </tr>
    </s:iterator>
    <tr align="right"> 
    <td colspan="6" class="STYLE1">
    <div  class="tools_nav" style="text-align:right">
     共<s:property value="totalRows"/>行&nbsp;
    	第<s:property value="currentPage"/>页&nbsp;
    	共<s:property value="pager.getTotalPages()"/>页&nbsp;
    	<a href='<s:url value="know_bigclass_manager.action" ><s:param name="currentPage" value="currentPage"/><s:param name="pagerMethod" value="'first'"/></s:url>'>首页</a>
    	<a href='<s:url value="know_bigclass_manager.action"><s:param name="currentPage" value="currentPage"/><s:param name="pagerMethod" value="'previous'"/></s:url>'>上一页</a>
    	<a href='<s:url value="know_bigclass_manager.action"><s:param name="currentPage" value="currentPage"/><s:param name="pagerMethod" value="'next'"/></s:url>'>下一页</a>
    	<a href='<s:url value="know_bigclass_manager.action"><s:param name="currentPage" value="currentPage"/><s:param name="pagerMethod" value="'last'"/></s:url>'>尾页</a>    	
    </div>
        
    </td>
    </tr>	
</table>
</td></tr>
</table>
</body>
</html>
