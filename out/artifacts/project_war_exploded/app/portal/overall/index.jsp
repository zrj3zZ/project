<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>IWORK综合应用管理系统</title> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link href="iwork_css/global_frame.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript"> 
 //==========================装载快捷键===============================//快捷键
		$(document).ready(function(){
			
		});
		function add(){
			var pageUrl = 'pt_overall_add.action';
			art.dialog.open(pageUrl,{
					id:'pt_overall_add',  
		       	 title:'新增门户',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:500,
		            height:350,
		            close:function(){
		            	window.location.reload();
			        }
				 });
		}
		function edit(id){
			var url = 'pt_overall_edit.action?id='+id;
			art.dialog.open(pageUrl,{
		        id:'pt_overall_edit',  
		        title:'更新门户',
		       	iconTitle:false, 
	            width:500,
	            height:350,
	            close:function(){
	            window.location.reload();
		        }
			});    
		}
		function del(id){
			if(confirm("确认删除当前模型吗?")){
				var pageurl = "pt_overall_remove.action?id="+id;
				$.ajax({ 
		            type:'POST',
		            url:pageurl,
		            success:function(msg){ 
		            	  if(msg=="success"){
		                  	alert("移除成功!");
		                  	 window.location.reload();
		                  }  
		                  else if(responseText=="error"){
		                     alert("移除失败!");
		                  } 
		            }
		        });
			}
		}
	</script>
	<style type="text/css">
			.header td{
			font-weight:bold;
			font-size:12px;
			padding:3px;
			white-space:nowrap;
			padding-left:5px;
			background:#fafafa url('../iwork_css/jquerycss/default/images/datagrid_header_bg.gif') repeat-x left bottom;
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
<body class="easyui-layout">
<!-- TOP区 -->
	<div region="north" border="false" style="height:40px;border-bottom:1px;">
		<div class="tools_nav">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" >
	      <tr>
          	<td style="text-align:left;padding:0px;padding-right:10px;padding-bottom:2px;">
          		<a href='javascript:add();' class="easyui-linkbutton" iconCls="icon-add" plain="true">新增</a>
				<a href='javascript:this.location.reload();' class="easyui-linkbutton" iconCls="icon-reload" plain="true">刷新</a>
          	</td>
           </tr>
	    </table>

	  </div>
		 
	</div>
    <!-- 导航区 -->
	<div region="west"  split="false" border="false"  style="height:100%;width:180px;padding-left:5px;overflow:auto; border-top:1px solid #F9FAFD">
				<s:property value="tablist" escapeHtml="false"/> 
    </div>
	<div region="center" style="padding:3px;border:0px;border-left:1px solid #efefef">
		<table  width="100%" cellspacing="0" cellpadding="0" border="0" style="border:1px solid #efefef">
		<tr class="header">
							<td   width="20%">标题</td>
							<td   width="15%" >类型</td>
							<td   width="20%" >栏目高度</td>
							<td   width="10%">接口</td>
							<td  width="10%" >链接</td>
							<td  width="20%" >操作<br></td>
						</tr>
			<s:iterator value="list" status="status">
					<tr class="cell">
							<td><s:property value="title"/></td>					
							<td><s:property value="type"/></td>					
							<td><s:property value="height"/></td>					
							<td><s:property value="interface_"/></td>					
							<td><s:property value="urlLink"/></td>					
							<td><a href="javascript:edit(<s:property value="id"/>);">编辑</a>&nbsp;<a href="javascript:del(<s:property value="id"/>);">删除</a></td>					
					</tr>
			</s:iterator>
		</table>
	</div>
	
</body>
</html>
