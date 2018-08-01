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
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />   
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/design_model.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.7.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
	<script type="text/javascript" src="iwork_js/jquery.form.js" ></script>
	<script type="text/javascript" src="iwork_js/process/design_index.js"></script>	
	<script type="text/javascript" src="iwork_js/process/design_showlist.js"></script> 	 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
		
	</script>
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
			line-height:30px;
			padding-bottom:3px;
		}
	</style>
</head>
<body class="easyui-layout">
<!-- TOP区 -->
	<div region="north" border="false" style="padding:0px;overflow:no;padding:0px;">
	<div class="tools_nav">
		<table width="100%"  border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td>
					<a href="javascript:addProcess();" class="easyui-linkbutton" plain="true"  iconCls="icon-add">新建流程</a>
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
	<div region="west"  split="false" border="false"  style="padding-left:5px;width:230px;overflow:hidden; border-top:1px solid #F9FAFD;border-right:1px solid #efefef">
		<table style="height:100%;" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="tree_top"></td> 
					</tr>
					<tr>
						<td class="tree_main">
							<div style="height:430px;width:210px;position:absolute; overflow-y:auto">
								<ul id="processtree" class="ztree"></ul>
							</div>
						</td>
					</tr>
					<tr>
						<td class="tree_bottom">
						<s:form id="editForm" name = "editForm" theme="simple">
				<input type="hidden" id="searchkey" name="searchkey">
						 		<input type="hidden" id="list_showtype" name="list_showtype">
			</s:form>
						</td>
					</tr>
				</table>
    </div>
    
	<div region="center" style="padding:0px;border:0px">
	
				<iframe  name="processFrame" id="processFrame" src="sysEngineProcess_Help.action"  frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="auto" allowtransparency="yes"   width="100%" height="100%"></iframe>
			
	</div>

</body>
</html>
