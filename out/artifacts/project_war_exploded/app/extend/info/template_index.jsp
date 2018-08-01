<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html> 
<head> 
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>WBS</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link href="iwork_css/common.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	
	<style type="text/css">
		.memoTitle{
			font-size:14px;
			padding:5px;
			color:#666;
		}
		.memoTitle a{
			font-size:12px;
			padding:5px;
		}
	</style>
	<script type="text/javascript">
		//加载导航树  
		$(function(){
			initTree();
		});
		
		function initTree(){
			var setting = {
						async: {
							enable: true, 
							url:"zqb_templateJSON.action",  
							dataType:"json",
							autoParam:["id","nodeType"]
						},
						callback: {
							onClick:onClick
						} 
					};
				$.fn.zTree.init($("#templateTree"), setting);//加载导航树 
		}
		
		//点击事件
		function onClick(event, treeId, treeNode, clickFlag){
			var id = treeNode.id;
			var nodeType = treeNode.nodeType;
			if(!treeNode.open){
				var zTree = $.fn.zTree.getZTreeObj("templateTree");
				zTree.expandNode(treeNode, true, null, null, true);
		    }
			showOperateButton(nodeType);
			
			var pageUrl = "zqb_template_list.action?id="+id+"&nodeType="+nodeType;
			$("#mainFrame").attr("src",pageUrl);
		}
		
		//判断是否显示各编辑按钮
		function showOperateButton(nodeType){
			$("#operationButton").show();
			$("#editTemplateType").show();
			$("#delTemplateType").show();
			$("#addTemplateType").show();
			$("#addTemplate").show();
			
			if(nodeType=="root"){
				$("#operationButton").hide();
			}
		//	if(nodeType=="company"){
		//		$("#editTemplateType").hide();
		//		$("#delTemplateType").hide();
		//	}
		}
		
		//全部展开
	    function expandAll() {
			var zTree = $.fn.zTree.getZTreeObj("templateTree");
				zTree.expandAll(true);
		}

	   	//全部折叠
		function unExpandAll() {
			var zTree = $.fn.zTree.getZTreeObj("templateTree");
			zTree.expandAll(false);
		}
		
		// 新增模板分类
		function addTemplateType(){
			var zTree = $.fn.zTree.getZTreeObj("templateTree");
			var nodes = zTree.getSelectedNodes();
			if(nodes==null || nodes==''){
				alert('请选择模板类别');
				return;
			}
			var id = nodes[0].id;
			
			if(nodes.length==1){
				var pageUrl = "createFormInstance.action?formid=134&demId=57&FID="+id;
				art.dialog.open(pageUrl,{ 
						id:'Category_show',
						cover:true,
						title:'新增模板分类',
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true,
						width:1000,
						cache:false,
						lock: true,
						height:480, 
						iconTitle:false,
						extendDrag:true,
						autoSize:false
					});
			}else{
				alert('请选择模板类别'); 
			}
			
		}
		
		// 新增模板
		function addTemplate(){
			var zTree = $.fn.zTree.getZTreeObj("templateTree");
			var nodes = zTree.getSelectedNodes();
			if(nodes==null || nodes==''){
				alert('请选择模板类别');
				return;
			}
			var id = nodes[0].id;
			
			if(nodes.length==1){
				var pageUrl = "createFormInstance.action?formid=129&demId=52";
				art.dialog.open(pageUrl,{ 
						id:'Category_show',
						cover:true, 
						title:'新增模板',
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true,
						width:1000,
						cache:false,
						lock: true,
						height:480, 
						iconTitle:false,
						extendDrag:true,
						autoSize:false
					});
			}else{
				alert('请选择模板类别'); 
			}
		}
		
		function batchTemplateType(){
			var zTree = $.fn.zTree.getZTreeObj("templateTree");
			var nodes = zTree.getSelectedNodes();
			if(nodes==null || nodes==''){
				alert('请选择模板类别');
				return;
			}
			var id = nodes[0].id;
			
			if(nodes.length==1){
				var pageUrl = "zqb_template_batch.action?TYPE="+id;
				art.dialog.open(pageUrl,{ 
						id:'Category_show',
						cover:true, 
						title:'新增模板',
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true,
						width:1000,
						cache:false,
						lock: true,
						height:550, 
						iconTitle:false,
						extendDrag:true,
						autoSize:false
					});
			}else{
				alert('请选择模板类别'); 
			}
		}
		
		// 编辑模板分类
		function editTemplateType(){
			var zTree = $.fn.zTree.getZTreeObj("templateTree");
			var nodes = zTree.getSelectedNodes();
			if(nodes==null || nodes==''){
				alert('请选择模板类别');
				return;
			}
			var id = nodes[0].id;
			
			var instanceid = nodes[0].instanceid;
			if(id=='999999'){
				alert("根节点无法编辑,请选择其他节点！");
				return;
			}
			if(nodes.length==1){
				var pageUrl = "openFormPage.action?formid=134&demId=57&instanceId="+instanceid;
				art.dialog.open(pageUrl,{ 
						id:'Category_show',
						cover:true,
						title:'编辑模板分类',
						loadingText:'正在加载中,请稍后...',
						bgcolor:'#999',
						rang:true,
						width:1000,
						cache:false,
						lock: true,
						height:480, 
						iconTitle:false,
						extendDrag:true,
						autoSize:false
					});
			}else{
				alert('请选择要编辑模板类别！'); 
			}
		}
		
		// 删除模板分类
		function deleteTemplateType(){
			var zTree = $.fn.zTree.getZTreeObj("templateTree");
			var nodes = zTree.getSelectedNodes();
			if(nodes==null || nodes==''){
				alert('请选择模板类别');
				return;
			}
			var id = nodes[0].id;
			var instanceid = nodes[0].instanceid;
			if(id=='999999'){
				alert("根节点无法删除,请选择其他节点！");
				return;
			}
			if(confirm("确定要删除模板类别吗？")){
				var pageUrl="zqb_templatetype_delete.action";
				$.post(pageUrl,{id:id,instanceid:instanceid},function(data){
			         if(data=='success'){
			             window.location.reload();
			         }else{
			         	alert("删除失败,模板类别下存在子类别或存在模板!");
			         }
			    });
			}
		}
	</script>
</head> 
<body class="easyui-layout">
	<!-- <div region="north" style="height:50px;font-size:20px;font-family:黑体;padding:5px;border-bottom:1px solid #efefef;" border="false" >
		<div class="tools_nav">
			<table width="100%"  border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td> 
						<a id="addTemplateType" href="javascript:addTemplateType();" class="easyui-linkbutton" plain="true" iconCls="icon-add">新增模板分类</a>
						<label id="operationButton">
						<a id="editTemplateType" href="javascript:editTemplateType();" class="easyui-linkbutton" plain="true" iconCls="icon-edit">编辑模板分类</a>
						<a id="addTemplate" href="javascript:addTemplate();" class="easyui-linkbutton" plain="true" iconCls="icon-add">新增模板</a>
						<a id="delTemplateType" href="javascript:deleteTemplateType();" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除模板分类</a>
						<a id="batchTemplateType" href="javascript:batchTemplateType();" class="easyui-linkbutton" plain="true" iconCls="icon-add">批量插入</a>
						</label>
						<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div region="west" style="width:250px;background-color:#efefef" border="false" >
		<ul id="templateTree" class="ztree"></ul>
	</div> -->
    <div region="center" border="false"  align="center" style="border:0px solid #C0C0C0;">
		<iframe id="mainFrame" name="mainFrame" scrolling="no" frameborder="0"  src="zqb_template_list.action" style="width:100%;height:100%;"></iframe> 
    </div>
    <div region="south" style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;" border="false" >
	</div>
</body>
</html>
