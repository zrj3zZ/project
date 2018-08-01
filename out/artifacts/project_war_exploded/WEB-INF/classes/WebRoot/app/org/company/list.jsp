<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="iwork" uri="/WEB-INF/tld/linkbtn.tld"%>
<%@ taglib prefix="cache"  uri="/oscache"%>

<html>
<head><title>子系统管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link href="iwork_css/common.css" rel="stylesheet" type="text/css" />
<link href="iwork_css/org/company_list.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="iwork_js/commons.js"></script>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/linkbutton.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/org/company_list.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
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
					 this.location.href='company_add.action'; return false;
		}
}); //快捷键
		$(document).ready(function(){
			 var setting = {
				async: {
						enable: true, 
						url:"company_tree_json.action?id=999999",
						dataType:"json",
						autoParam:["id","nodeType"] 
					},
				data: {
					simpleData: {
						enable: true
					}
				},
				callback: {
					onClick: onClick
				} 
			};
			$.fn.zTree.init($("#companyTree"), setting); 
		});
		function onClick(e, treeId, treeNode) {
			var url= "company_list.action?parentid="+treeNode.id;
			if(treeNode.nodeType=='root'){
				var url= "company_list.action";
			}
			var zTree = $.fn.zTree.getZTreeObj("companyTree");
			zTree.expandNode(treeNode, true, null, null, true);
			$('#editForm').attr('action',url); 
			$('#editForm').attr('target',"_self");
			$('#editForm').submit(); 
		}
	</script>
</head>	
<body  class="easyui-layout">
<!-- <div class="menubackground"style="padding-left:5px;padding-top:2px;padding-bottom:2px;"> -->
<div region="north" border="false" split="false"   class="nav" id="layoutNorth">
<div class="tools_nav"> 
		<s:property value="toolbar"  escapeHtml="false"/>
		<a href="javascript:addcompany();" class="easyui-linkbutton"  plain="true" iconCls="icon-add" >新增</a>
		<a href="javascript:location.reload();" class="easyui-linkbutton"  plain="true" iconCls="icon-reload" >刷新</a>
	</div>
</div>
<div region="center" border="false" split="false"   class="nav" id="layoutNorth">

	<table id="tt" cellpadding="1" cellspacing="1" class="table3" width="100%">
    <tr >
        <th  width="67" >单位ID</th>
        <th width="251"  >单位名称</th>
        <th width="232"  >单位编号</th>
        <th width="240"  >单位类型</th>
        <th width="169"  >皮肤样式</th>
        <th width="163"  >操作</th>
    </tr>
    <tbody>
   
    <s:iterator value="availableItems" status="status">
        <tr >
         <td align="center">
            <s:property value="id"/>
          </td>
            <td align="center">
            <a href='javascript:editcompany(<s:property value="id"/>)'>
            <s:property value="companyname"/>
            </a>
            </td>
            <td align="center"><s:property value="companyno"/></td>
            <td align="center"><s:property value="companytype"/></td>
            <td align="center"><s:property value="lookandfeel"/></td>
            <td align="left">
            	<a href='javascript:editcompany(<s:property value="id"/>)'>编辑</a> 
            	<a href="javascript:confirm1(<s:property value="id"/>);"> 删除 </a> 
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
   
    </tbody>
</table>
<div class="page">
共<s:property value="totalRows"/>行&nbsp;
    		第<s:property value="currentPage"/>页&nbsp;
    		共<s:property value="pager.getTotalPages()"/>页&nbsp;
    		<a href="<s:url value="company_list.action">
    			<s:param name="currentPage" value="currentPage"/>
    			<s:param name="pagerMethod" value="'first'"/>
    			
    		</s:url>">首页</a>
    		<a href="<s:url value="company_list.action">
    			<s:param name="currentPage" value="currentPage"/>
    			<s:param name="pagerMethod" value="'previous'"/>
    		</s:url>">上一页</a>
    		<a href="<s:url value="company_list.action">
    			<s:param name="currentPage" value="currentPage"/>
    			<s:param name="pagerMethod" value="'next'"/>
    		</s:url>">下一页</a>
    		<a href="<s:url value="company_list.action">
    			<s:param name="currentPage" value="currentPage"/>
    			<s:param name="pagerMethod" value="'last'"/>
    		</s:url>">尾页</a>
</div>
<form id="editForm" method="post">
<input type='hidden' id='id' name='id' value=<s:property value="id"/>>
</form>
</div>
<div region="west" border="false" split="false"  style="width:230px;border-right:1px solid #efefef"  id="layoutNorth">
	<ul id="companyTree" class="ztree"></ul>
</div>
</body>
</html>
