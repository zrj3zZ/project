<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js" ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style>
		
		#itemlist{
			padding:0px;
		}
		
		#itemlist li{
			line-height:35px;
			border-bottom:#efefef 1px solid; 
			list-style:none;
			width:80%;
			padding-left:30px;
			font-size:14px;
			font-family:微软雅黑;
		}
		#itemlist li:hover{
			background-color:#efefef;
			cursor:pointer;
			color:0000ff;
		}
		.leftMiddleItemCurr{
			background:#efefef;
			color:red;
		}
		.tablelist{
			width:100%;
		}
		.tablelist th{
			background:#f2f4f6;
			border-bottom:1px solid #c6c9ca;
			border-right:1px solid #c6c9ca;
			padding:2px;
			padding-left:5px;
			font-family:微软雅黑;
		}
		.tablelist td{
			height:30px;
			color:#666;
			border-bottom:1px solid #efefef;
			font-family:微软雅黑;
		}
		.addBtn{
			text-align:center;
		}
		.tablist .current{
			  background:#efefef;
			   color:#0000ff;
			 border-right:1px solid #fff;
		}
		
	</style>
	<script type="text/javascript">
	$(function(){
		$('.tablist li').click(function(obj){
			 $(".tablist li").each(function(item){
				    $(this).removeClass("current");
			  });
			 $(this).addClass("current");
				  var label = $(this).attr('id');
				  showInfo(label);
				  $("#groupid").val(label);
	    });
	});
		function addGroup(){
			 var pageUrl = "iwork_mail_booklist_add.action";
			 art.dialog.open(pageUrl,{
			    	id:"planlistDlg",
			    	title:"新建分组 ",  
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:500,
				    height:'90%',
					close:function(){
						location.reload();
					}
				 });
			
		}
		function editGroup(groupid){
			 var pageUrl = "iwork_mail_booklist_add.action?id="+groupid;
			 art.dialog.open(pageUrl,{
			    	id:"planlistDlg",
			    	title:"新建分组 ",  
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:500,
				    height:'90%',
					close:function(){
						location.reload();
					}
				 });
			
		}
		function showInfo(id){
			$.post('iwork_mail_booklist_sub.action',{id:id},function(data){
		    	$("#sublistDiv").html(data);
		  }); 
		}
		function remove(id){
		 if(confirm('确认删除当前分组成员吗？')){
			$.post('iwork_mail_booklist_subremove.action',{id:id},function(data){
				if(data=='success'){
					$("#tr_"+id).remove();
				}else{
					art.dialog.tips('权限不足');
				}
		    	
		  }); 
		  }
		}
		function removeGroup(){
		    if(confirm('确认删除当前分组吗？')){
		    	var groupid = $("#groupid").val();
				$.post('iwork_mail_booklist_remove.action',{id:groupid},function(data){
					if(data=='success'){
						location.reload();
					}else{
						art.dialog.tips('分组中包含成员，无法删除');
					}
			    	
			  }); 
		    }
			
		}
		
		
	</script>
</head>
<body class="easyui-layout">
		 <div region="north" border="false" > 
		 	<div class="tools_nav" style="padding-left:2px;">
		 		<input type="button" onclick="javascript:addGroup();"  name="removeBtn" value="新增分组 "/>
		 		<input type="button" onclick="javascript:removeGroup();"  value="删除"/>
		 	</div>
        </div>  
       	 <div region="west" border="false" style="width:200px;border:1px solid #efefef;padding-left:5px">
            	<s:property value="html" escapeHtml="false"/>
            </div> 
            <div region="center" border="false" style="background:#fff; border:1px solid #fff">
            	<table class="tablelist">
            		<tr>
            			<th>姓名</th>
            			<th>账号</th>
            			<th>部门</th>
            			<th>组织</th>
            			<th>操作</th>
            		</tr>
            		<tbody  id="sublistDiv">
            		
            		</tbody>
            	</table>
            
            </div> 
            <div region="south" border="false" style="height: 32px;">
            	<s:hidden name="groupid" id="groupid"></s:hidden>
			</div>
</body>
</html>
