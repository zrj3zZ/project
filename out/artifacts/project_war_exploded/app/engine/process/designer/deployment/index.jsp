<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" charset="gb2312"></script>
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="iwork_js/sys_procdef.js" charset="gb2312"></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js" charset="gb2312"></script>
	<style type="text/css">
		.title{
			font-size:16px;
			font-weight:bold;
			padding:20px 10px;
			background:#eee;
			overflow:hidden;
			border-bottom:1px solid #ccc;
		}
		.t-list{
			padding:5px;
		}
		.formdata{
			padding-top:3px;
			padding-bottom:3px;
		}
	</style>
	<script type="text/javascript">
	$(function(){
				//加载导航树 
				$('#processtree').tree({   
	                 url: 'sysProcessDefinition_loadtree.action',   
	               	onClick:function(node){
	               		if(node.attributes.type=='process'){
	               			var url = 'sysProcessDefinition!displayProcess.action?processId='+node.id;
							parent.frames['processDefListFrame'].location = url; 
	               		}else if(node.attributes.type=='group'){
	               			var url = 'sysProcessDefinition!processlist.action?groupId='+node.id;
							parent.frames['processDefListFrame'].location = url; 
	               		}
	               },
	               onLoadSuccess:function(node,data){
	               		var groupId = "<s:property value='groupId'  escapeHtml='false'/>";
	               		var tnode = $('#processtree').tree('find',groupId);
	               		if(tnode!=null){
	               			$('#processtree').tree('select',tnode.target);
	               		}
	               }
	             });
				$('#editForm').form({  
				    onSubmit:function(){  
				        return $(this).form('validate');  
				    },  
				    success:function(data){  
				        alert(data);  
				    }  
				});
				$('#editForm').ajaxForm(); 
		})
		
		
		
		
	
	</script>
</head>
<body class="easyui-layout">
<!-- TOP区 -->
	<div region="north" border="false" style="padding:0px;overflow:no">
	<div style="padding:0px;border-bottom:1px solid #efefef">
	</div>
	</div>
    <!-- 导航区 -->
	<div region="west" title="流程模型导航" split="false" border="false"  style="width:200px;padding:0px;overflow:hidden; border-top:1px solid #F9FAFD">
		<div title="我的菜单" icon="icon-reload" closable="false" style="overflow:auto;padding:0px;">
						 <ul id="processtree">
						 </ul> 
					</div>
    </div>
	<div region="center" style="padding:3px;">
			
	</div>

</body>
</html>
