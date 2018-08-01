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
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<script type="text/javascript" src="iwork_js/iform_index.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script> 
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript"> 
	/* var api = frameElement.api, W = api.opener;  */
		var setting = {
				async: {
						enable: true, 
						url:"sysEngineGroup!openjson.action",
						dataType:"json"
					}, 
			view: {   
				dblClickExpand: false
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				//beforeClick: beforeClick,
				onClick: onClick
			}
		};
		function saveBaseInfo(){
			var url = "sysFlowDef_baseInfo!save.action";
		    $.post(url,$("#editForm").serialize(),function(data){
		    	 art.dialog.tips("保存成功",2);
		    });
		}
		function beforeClick(treeId, treeNode) {
			var check = (treeNode && !treeNode.isParent);
			if (!check) art.dialog.tips("请选择目录...");
			return check;
		}
		
		function onClick(e, treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
			nodes = zTree.getSelectedNodes(),
			v = "";
			var id = "";
			nodes.sort(function compare(a,b){return a.id-b.id;});
			for (var i=0, l=nodes.length; i<l; i++) {
				v += nodes[i].name + ",";
				id+= nodes[i].id + ",";
			}
			if (v.length > 0 ) v = v.substring(0, v.length-1);
			if (id.length > 0 ) id = id.substring(0, id.length-1);
			var cityObj = $("#citySel");
			cityObj.attr("value", v);
			$("#groupid").val(id); 
			hideMenu();
		}
		function showMenu() {
			var cityObj = $("#citySel"); 
			var cityOffset = $("#citySel").offset();
			$("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");
 
			$("body").bind("mousedown", onBodyDown);
			return false;
		}
		function hideMenu() {
			$("#menuContent").fadeOut("fast");
			$("body").unbind("mousedown", onBodyDown);
		}
		function onBodyDown(event) {
			if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
				hideMenu();
			}
		}
		$(document).ready(function(){
			$.fn.zTree.init($("#treeDemo"), setting);
		});
	</script>
</head>
<body class="easyui-layout">
	<div region="north" border="false" style="height:40px;">
		<div class="tools_nav">
		        <a href="javascript:saveBaseInfo();" class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>
				<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>				
		 </div>
	</div>
	<div region="center" style="padding:0px;border:0px;">
				<s:form name="editForm" id="editForm" action="sysFlowDef_baseInfo!save.action" theme="simple">
							<table class = "table_form">
								<tr>
										<td class = "td_title">流程名称:</td>
										<td class = "td_data"><s:textfield name="model.title" theme ="simple"/></td>
								</tr>
								<tr>
										<td class = "td_title">流程管理员:</td>
										<td class = "td_data"><s:textfield name="model.uploader" theme ="simple"/></td>
								</tr>
								<tr>
										<td class = "td_title">流程分类:</td>
										<td class = "td_data">
										<input id="citySel" type="text" disabled value="<s:property value="groupname"/>" style="width:120px;border:1px #999 solid;background-color:#efefef"/> 
		&nbsp;<a id="menuBtn" href="#" onclick="showMenu()">选择</a>
										<s:hidden  id="groupid"  name="model.groupid" theme="simple"/></td>
								</tr>
								
								<tr>
										<td class = "td_title">流程状态:</td>
										<td class = "td_data"><s:radio  name="model.status"  listKey="key" listValue="value"  list="#{'1':'开启','0':'关闭'}" theme="simple"/></td>
								</tr>
								<tr>
										<td class = "td_title">流程版本:</td>
										<td class = "td_data">[<s:property value="model.versionType"/>] V<s:property value="model.version"/></td>
								</tr>
								<tr>
										<td class = "td_title">流程标识:</td>
										<td class = "td_data"><s:property value="model.actDefId"/>
										</td>
								</tr>
								<tr> 
										<td class = "td_title">流程创建人/流程创建时间:</td>
										<td class = "td_data"><s:property value="model.uploader"/> /<s:date name="model.uploadDate" format="yyyy-MM-dd hh:mm:ss"/> </td>
								</tr>
								<tr >
										<td class = "td_title">流程说明:</td>
										<td class = "td_data"><s:textarea  name="model.memo" cssStyle="width:300px;height:100px"  ></s:textarea></td>
								</tr> 
								<tr >
										<td class = "td_title">排序:</td>
										<td class = "td_data"><s:textfield name="model.orderIndex" theme ="simple"/></td>
								</tr> 
							</table>
							<s:hidden name="model.id"></s:hidden>
							<s:hidden name="model.deployId"></s:hidden>
							<s:hidden name="model.uploadDate"></s:hidden>
							<s:hidden name="model.version"></s:hidden>
							<s:hidden name="model.versionType"></s:hidden>
							<s:hidden name="model.prcKey" theme ="simple"/> 
							<s:hidden name="model.actDefId"></s:hidden>
							<s:hidden name="model.prcHelp"></s:hidden>
							</s:form>
							<div id="menuContent" class="menuContent" style="display:none; background-color:#fff;border:1px solid #efefef; position: absolute;"> 
								<ul id="treeDemo" class="ztree" style="margin-top:0; width:160px;"></ul> 
							</div>
	</div>
</body>
</html>
