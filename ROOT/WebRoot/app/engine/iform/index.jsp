<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/design_model.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/engine/form_index.js" ></script>	 
	<script type="text/javascript" src="iwork_js/engine/form_showlist.js"></script>	
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>	
	<script type="text/javascript">  
 //==========================装载快捷键===============================//快捷键
 		var status = 1;
	    jQuery(document).bind('keydown',function (evt){	 	
			    if(evt.ctrlKey&&evt.shiftKey){
				return false;
			   }
			   else if(evt.ctrlKey && event.keyCode==82){ //Ctrl+r /刷新操作
				         this.location.reload(); return false;
			     } 
			  else if(evt.shiftKey && event.keyCode==78){ //Shift+n 新增存储
						 addMetaData(); return false;
					}
			  else if(evt.ctrlKey && event.keyCode==83){ //ctrl+s保存
						doSubmit(); return false;
					}		
		}); //快捷键
		$(function(){
			  $("#view").click(function(){ 
					$("#view").toggleClass("view2");
					var parentid = $("#list_showtype").val();
					if(status==0){
						submitList(parentid,status);
						status = 1;
					}else{
						submitList(parentid,status);
						status = 0; 
					}
			  });
	    })
	    //查询回车事件
	    function enterKey(){
			if (window.event.keyCode==13){
				 doSearch();
				return;
			}
		} 
	     function doSearch(){ 
			  		  var searchTxt = $("#searchTxt").val(); 
			  		   $("#search_btn").toggleClass("search_btn_onclick");
					  if(searchTxt==''){
					  		alert('请填写查询条件');
					  		return;
					  }else{ 
					   $("#searchkey").val(searchTxt); 
					  	var url = "sysEngineIForm_search.action?showtype="+status;
						$('form').attr('action',url);
						$('form').attr('target',"iformListFrame");
						$('form').submit();  
					  } 
			  }
	</script>
</head>
<body class="easyui-layout">
<!-- TOP区 -->
	<div region="north" border="false" style="padding:0px;overflow:no;scrolling:no;border-bottom:1px;">
		<div class="tools_nav"> 
		<table width="100%"  border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td> 
					<a href="javascript:addForm();" class="easyui-linkbutton" plain="true" iconCls="icon-add">新增表单</a>
						<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
				</td>
				<td>
				 <span class="view" id="view"></span> <span class="search_btn" id="search_btn">
				  <input  type="text" name="searchTxt" id="searchTxt" onKeyDown="enterKey();" class="search_input"/>
				  <input  onclick="doSearch();" type="button" class="search_button" value="&nbsp;"/> 
				  </span> 
		  		</td>
			</tr>
		</table> 
		 
		 </div>
	</div>
    <!-- 导航区 -->
	<div region="west"  split="false" border="false"  style="height:100%;width:230px;padding-left:5px;overflow:hidden; border-top:1px solid #F9FAFD">
				<table style="height:100%;" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="tree_top"></td>
					</tr>
					<tr>
						<td class="tree_main">
						<div style="height:430px;width:210px;position:absolute; overflow-y:auto">
							<ul id="formtree" class="ztree"></ul>
						</div>
						</td>
					</tr>
					<tr>
						<td class="tree_bottom"></td>
					</tr>
					
					
				</table>
						 
						 <form id="editForm" name="editForm"  action="/processDeploy_index.action" method="post">
						 		<input type="hidden" id="searchkey" name="searchkey">
						 		<input type="hidden" id="list_showtype" name="list_showtype">
						 </form>
    </div>
	<div region="center" style="padding:3px;border:0px;">
			<iframe id="iformListFrame" name="iformListFrame"  src="sysEngineIFrom_Help.action" frameborder="no" border="0" marginwidth="0″ marginheight="0″ scrolling="auto" allowtransparency="yes"  id="metadataFrame" name="metadataFrame" width="100%" height="100%"></iframe>
			<!-- <table id="metadata_grid" style="margin:2px;"></table> -->
	</div>
	
</body>
</html>
