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
<head><title> 用户管理维护</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"> 
<link href="iwork_css/org/usermap_list.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="iwork_js/orguser_form.js"></script>
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
	function add(){
	
		var userid = encodeURI(document.getElementById('userid').value);
		var pageUrl = 'usermap_add.action?userid='+userid; 
		art.dialog.open(pageUrl,{
			id:'usermapAddDialog', 
			title:"新增",
			lock:true,
			background: '#999', // 背景色
		    opacity: 0.87,	// 透明度
		    width: 500,
			height: 510,
			close:function(){
			 location.reload();
			}
		 });
	}
	function edit(id){
		var pageUrl = 'usermap_edit.action?id='+id;
		art.dialog.open(pageUrl,{
			id:'usermapEditDialog', 
			title:"修改",
			lock:true,
			background: '#999', // 背景色
		    opacity: 0.87,	// 透明度
		    width: 500,
			height: 510,
			close:function(){
			 location.reload();
			}
		 });
	}
	function removeItem(id){
		var r=confirm("是否删除用户兼任")
		  if (r==true){
		  	var pageUrl = 'usermap_delete.action'; 
			 $.post(pageUrl,{id:id},function(msg){ 
		    		if(msg=='success'){
		    			  location.reload();
		    		}
			 }); 
		  }
	}
	function showDlg(url){
		var dg =parent.$.dialog({   
			id:'dg_userMap', 
			cover:true, 
			title:'兼任职位设置',  
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:500,
			cache:false, 
			lock: true,
			esc: true,
			height:450,  
			iconTitle:false, 
			extendDrag:true,
			autoSize:true,
			content:url
		});
		dg.ShowDialog();
	}
	function refreshPage(){
		var userid = document.getElementById('userid').value;
		window.location.href= "usermap_list.action?userid="+userid; 
	}
</script>

</head>	
<body class="easyui-layout" >
<div region="north" border="false" style="height:40px" split="false" >
	<div class="tools_nav"> 
			<a href="javascript:add()" class="easyui-linkbutton"  plain="true" iconCls="icon-add" >新增</a>
			<a href="javascript:refreshPage();" class="easyui-linkbutton"  plain="true" iconCls="icon-reload" >刷新</a>
	</div>
</div>
<div region="center" border="false" split="false" > 
	<table border="0" cellpadding="0" cellspacing="0" class="table3" >
    <tr  >
        <th  nowrap="nowrap" >ID</th>
        <th  nowrap  >兼任公司</th>
        <th  nowrap  >兼任部门编号</th>
        <th nowrap  >兼任部门名称</th> 
        <th  nowrap  >兼任角色ID</th>
        <th  nowrap   >是否是部门管理者</th>
        <th  nowrap  >兼任类型</th>
        <th  nowrap  >操作</th>
    </tr>
    <s:iterator value="availableItems">
        <tr  >
         <td >
            <s:property value="id"/>
          </td>
            <td  onclick='edit(<s:property  value="id" />)'> 
          	 <s:property value="companyname"/>[<s:property value="companyno"/>]
            </td>
            <td  onclick='edit(<s:property  value="id" />)'> 
          	 <s:property value="departmentid"/>
            </td>
            <td   onclick='edit(<s:property  value="id" />)'>
            <s:property value="departmentname"/>
            </td>
            <td   onclick='edit(<s:property  value="id" />)'>
            <s:property value="orgroleid"/>
            </td>
           
            <td >
            <s:if test="ismanager == 0">
					<s:label theme="simple">否</s:label>									
			 </s:if>	 	
			 <s:else>
					<s:label theme="simple">是</s:label>	 
			 </s:else>
            </td>
            <td >
             <s:if test="usermaptype == 0">
					<s:label theme="simple">行政兼任</s:label>									
			 </s:if>	 	
			 <s:else>
					<s:label theme="simple">系统兼任</s:label>	 
			 </s:else>
            </td>
            <td >
            	<a href="javascript:edit('<s:property value="id" />')" class="easyui-linkbutton"  plain="true"> <img alt="编辑" src="iwork_img/but_edit.gif" border="0"></a>&nbsp;
            	<a href="javascript:removeItem('<s:property value="id" />')"  class="easyui-linkbutton"  plain="true"> <img alt="删除" src="iwork_img/but_delete.gif" border="0"></a> 
            </td>
        </tr>
    </s:iterator> 
</table>

</div>
<s:textfield name="userid" id="userid" cssStyle="display:none;"/>
</body>
</html>
