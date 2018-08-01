<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>公式规则编辑</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

		<link rel="stylesheet" href="iwork_css/common.css" type="text/css">
		<link rel="stylesheet" href="iwork_css/plugs/exam.css" type="text/css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
		<link rel="stylesheet" href="iwork_css/jquerycss/zTreeStyle.css" type="text/css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"  ></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"  > </script>
		<script charset="utf-8" src="iwork_js/kindeditor/lang/zh_CN.js"></script>
		<script charset="utf-8" src="iwork_js/kindeditor/plugins/code/prettify.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
		<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
		<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
		<script type="text/javascript">
		var api = art.dialog.open.api, W = api.opener;
		//设置ztree
		var setting = {
			view: {
				dblClickExpand: dblClickExpand
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				beforeClick: beforeClick
			}
		};
		
		function dblClickExpand(treeId, treeNode) {
			return treeNode.level > 0;
		}
		
		var log, className = "dark";
		
		//默认的点击展开 关闭方法
		function beforeClick(treeId, treeNode, clickFlag) {
			var zTree = $.fn.zTree.getZTreeObj("tree");
			zTree.expandNode(treeNode);
		}
		
		$(document).ready(function(){
			//根据参数初始化ztree 
			$.post("getTreeJsonStr.action",{eId:"<s:property  value='eId' escapeHtml='false'/>"},function(result){
				var zNodes = result;
				$.fn.zTree.init($("#tree"), setting, result);
				var defItemId = $("#defItemId").val();
				if(defItemId.length > 0){
					var treeObj = $.fn.zTree.getZTreeObj("tree");
					var oneNodes = treeObj.getNodesByParamFuzzy("methodName", defItemId, null);
					if (oneNodes.length > 0) {
						treeObj.selectNode(oneNodes[0],false);
					}
				}
				
			},"json");
			//根据参数初始化Grid
			openTreeItem(-1);
						
		});
		
		//打开一个节点
		function openTreeItem(itemId){
			//获取节点说明
			if(itemId > 0){
				$.post("openTreeItem.action",{itemId:itemId},function(data){
					$("#itemDesc").html(data);
				});
			}else{
				$.post("openTreeItem.action",{defItemId:"<s:property  value='defItemId' escapeHtml='false' />" ,itemId:itemId},function(data){
					$("#itemDesc").html(data);
				});
			}
			//打开节点Grid
			var lastsel;
			var postGridUrl = "openTreeItemGrid.action?eId=" + encodeURI("<s:property  value='eId' escapeHtml='false' />") 
								+ "&defItemId=" + "<s:property  value='defItemId' escapeHtml='false' />" + "&itemId=" + itemId;
			jQuery("#expression_grid").jqGrid({
		   		url:postGridUrl,
				datatype: "json",
				mtype: "POST",
			   	colNames:["参数名称","参数传入值","参数说明"],
			   	colModel:[
					{"index":1,"width":100,"name":"NAME","align":"left"},
					{"index":2,"width":100,"name":"VALUE","align":"left",editable:true}, 
					{"index":3,"width":190,"name":"DESC","align":"left"}
				],
			   	rowNum:5,
				jsonReader: {
					root: "dataRows",
					page: "curPageNo", 
					total: "totalPages", 
					records: "totalRecords",
					repeatitems: false,
					id: "id",
					userdata: "userdata"
				},
			    height: 150,
			    width: 507,
				onSelectRow: function(id){
					if(id && id!==lastsel){
						
						jQuery('#expression_grid').jqGrid('editRow',id);
						jQuery('#expression_grid').jqGrid('restoreRow',cl);
						lastsel=id; 
					} 
				}
			});
			doResize();
			var pageUrl = 'openTreeItemGrid.action?eId=&itemId=' + itemId;
			$("#expression_grid").setGridParam({page:1,url:pageUrl}).trigger("reloadGrid");
		}
		
		function doResize() {
			var winW = parseInt($("#topLeft").css("width"));
			var winH = parseInt($("#itemGrid").css("height"));
			$("#expression_grid").jqGrid('setGridWidth', winW - 20).jqGrid('setGridHeight', winH-30); 
		}
		
		function myelem (value, options) {
			var el = document.createElement("input");
			el.type="text";
			el.value = value;
			return el;
		}

		//获取
		function myvalue(elem) {
			return $(elem).val();
		}
		
		//搜索子节点
		function searchTree(){
			var searchStr = $("#searchStr").val();
			$.post("getTreeJsonStr.action",{searchStr:searchStr},function(result){
				var zNodes = result;
				$.fn.zTree.init($("#tree"), setting, result);
			},"json");
		}
		
		//重载ztree
		function reloadTree(){
			$("#searchStr").val("");
			$.post("getTreeJsonStr.action",{},function(result){
				var zNodes = result;
				$.fn.zTree.init($("#tree"), setting, result);
			},"json");
		
		}
		
		//关闭窗口
		function closeExpressionWindow(){
			window.close();
		}
		
		//向父页面插入结果
		function insertStr(){
			var treeObj = $.fn.zTree.getZTreeObj("tree");
			var selectedNodes = treeObj.getSelectedNodes();
			var methodName = "";
			if (selectedNodes.length > 0) {
				methodName = selectedNodes[0].methodName;
			}else{
				alert("请选择一个方法添加");
				return;
			}
			
			//获取grid字段信息
			var ids = jQuery("#expression_grid").jqGrid('getDataIDs');
			var tempStr = "";
			var cl = "";
			for(var i=0;i < ids.length;i++){ 
				cl = ids[i] + "";
				tempStr = $("#" + cl + "_VALUE").val();	
				jQuery('#expression_grid').jqGrid('restoreRow',cl);
				jQuery("#expression_grid").jqGrid('setRowData',cl,{VALUE:tempStr});
			} 

			//根据字表类型构建返回值
			$.post("getParamsType.action",{getParamsEId:methodName},function(data){
				if(data != "" && $.trim(data).length > 0){
					var col = $("#expression_grid").jqGrid('getCol',"VALUE",false) + "";//获取列名为value的列的
					var paramsTypes = data.split("|");
					var values = col.split(","); //字符分割
					
					if(values.length > 0){
						methodName = methodName + "(";
						for (i = 0; i < values.length; i++ ){
							if(i != 0){
								methodName = methodName + ",";
							}
							if($.trim(values[i].length) > 0){
								//参数加入
								if("String" == paramsTypes[i]){
									methodName = methodName + "'" + values[i] + "'";
								}else{
									methodName = methodName + values[i];
								}
							}else{
								methodName = methodName + "null";
								//alert("方法参数为必填项,请填写后再添加");
								return;
							}
						}
						methodName = methodName + ")";
					}
				}
				
				methodName = "%" + methodName + "%";
			//	window.opener.insertExpressionStr($.trim(methodName));
				var origin = artDialog.open.origin; 
					var input = origin.document.getElementById("editForm_model_fieldDefault");
				input.value = methodName;
				close();
			});
		}
		function close(){
			api.close();
		}
		</script>
		
		<style>
		    a{
		    	font-size:12px;
		    	color:#666;
		    }
			form {
				margin: 0;
			}
			
			textarea {
				display: block;
			}
			
			.ke-icon-puls {
				background-image: url(/iwork_img/add_obj.gif);
				width: 16px;
				height: 16px;
			}
			
			.ke-icon-reduction {
				background-image: url(/iwork_img/reduction.png);
				width: 16px;
				height: 16px;
			}
			
			.ke-toolbar-icon {
				background-image: url(../themes/default/default.gif);
				background-position: 0px -672px;
				width: 16px;
				height: 16px;
			}
			.desc{
			height:120px;overflow: auto;border:1px solid #efefef; margin-right: 17px; padding-left: 23px; padding-top: 10px;
			color:#666;
			}
			
		</style>
		
		
</head>
<body class="easyui-layout"> 
       <div region="west" border="false" style="width:280px;padding:5px; background: #fff;">
            	<div class="easyui-layout" fit="true">
				<div region="center"  border="false" style="border:1px solid #efefef;padding-bottom:3px;" >
					<ul id="tree" class="ztree"></ul>
				</div>
				<div region="north" split="true" style="height:28px;border:0px;padding-bottom:3px;">
					<input type="text" id="searchStr" onkeyup="searchTree()" onkeydown="searchTree()" style="margin-left: 8px; margin-top: 3px;" />
				<a href="javascript:reloadTree();" >重置</a>
				</div>
		</div>
	</div>
           <div region="center" border="false" style="background: #fff; border:0px solid #ccc;">
           		  <div  style="padding-right:15px;margin-bottom:10px;background: #fff; border:0px solid #ccc;">
					<table id='expression_grid'></table>
					</div>
				  <div class="desc" id="itemDesc"></div>
            </div>
	
	<div region="south" border="false" style="text-align: right; height: 40px; line-height: 30px;padding-top:5px;padding-right:15px; margin-right: 10px;">
		<a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:insertStr();" >确定</a> 
		<a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:close();">取消</a>
	</div>
	
	<s:hidden name="eId" id="eId" />
	<s:hidden name="defItemId" id="defItemId" />
</body>
</html>