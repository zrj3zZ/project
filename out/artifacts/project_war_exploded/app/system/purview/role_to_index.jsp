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
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
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
			var setting = {
					check: {
						enable: false
					},
					view: {
						selectedMulti: false
					},
					async: {
						enable: true, 
						url:"showPurviewRoleTree.action",
						dataType:"json"
					},callback: {
						onClick:onClick
					}
				};
			var setting2 = {
					check: {
						enable: true
					},
					view: { 
						selectedMulti: false
					},
					async: {
						enable: true, 
						url:"openUnUserPurviewList.action",
						dataType:"json", 
						autoParam:["id","type"]
					},callback: { 
						onClick:onPurviewClick
					}
				};
			$.fn.zTree.init($("#purviewTree"), setting);
			$.fn.zTree.init($("#purviewListTree"), setting2);
		});
	
 		function onClick(event, treeId, treeNode, clickFlag){
 			
 			var zTree = $.fn.zTree.getZTreeObj("purviewTree");
 			var type = treeNode.type;
	 			var url = "openUnRolePurviewList.action?roleId="+treeNode.id;
				var setting2 = {
					check: { 
						enable: true
					},
					view: { 
						selectedMulti: false
					},
					async: {
						enable: true, 
						url:url,
						dataType:"json"
					},callback: { 
						onClick:onPurviewClick
					}
				};
				$("#roleId").val(treeNode.id);
				$.fn.zTree.init($("#purviewListTree"), setting2); 
 		} 
		
		//点击权限组树
		function onPurviewClick(event, treeId, treeNode,clickFlag) {
		var zTree = $.fn.zTree.getZTreeObj("purviewListTree");
			var type = treeNode.type;
			if(type=='category'){
				zTree.expandNode(treeNode, true, null, null, true);
			}else{
				if(!treeNode.checked){
	 				zTree.checkNode(treeNode, true, true, clickFlag);
		 		}else{
	 				zTree.checkNode(treeNode, false, true, false);
		 		}
			}
		}
				//设置权限
				function setpurview(){
					if($("#roleId").val()==""){
		 				art.dialog.tips('请选择您要授权的对象');
		 				return;
		 			}
					var zTree = $.fn.zTree.getZTreeObj("purviewListTree");
		 			var nodes = zTree.getCheckedNodes(true);
		 			var str = ""; 
		 			for(var i=0;i<nodes.length;i++){
		 				var type = nodes[i].type;
		 				if(type=='category')continue;
		 				var tmp = nodes[i].id;
		 				if(i<nodes.length-1){ 
		 					tmp+=","; 
		 				}
		 				str+=tmp;
		 			}
		 			
		 			$("#purviewIds").val(str);
					$.post('openPurviewRole_setRolelist.action',$("#editForm").serialize(),function(data){
				    	if(data=='success'){ 
				    		art.dialog.tips("授权成功");
				    	}else{
				    		art.dialog.tips("授权异常,请稍后再试");
				    	}
				  }); 
				}
	
	</script>
</head>
<body class="easyui-layout">
<!-- TOP区 -->
	<div region="north" border="false" style="height:40px;border-bottom:1px;">
		<div class="tools_nav">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" >
	      <tr>
          	<td align="left"><span id="title_span" style='font-size:14px;font-family:黑体'><img alt="用户-->权限组授权" src="iwork_img/shield.png" border="0"/>用户-->权限组授权</span></td>
          	<td style="text-align:right;padding:0px;padding-right:10px;padding-bottom:2px;">
          		<a href='javascript:setpurview();' class="easyui-linkbutton" iconCls="icon-process-purview" plain="false">授权</a>
				<a href='javascript:this.location.reload();' class="easyui-linkbutton" iconCls="icon-reload" plain="false">刷新</a>
          	</td>
           </tr>
	    </table>

	  </div>
		 </div>
    <!-- 导航区 -->
	<div region="west"  split="false" border="false"  style="height:100%;width:180px;padding-left:5px;overflow:auto; border-top:1px solid #F9FAFD">
				<s:property value="tablist" escapeHtml="false"/> 
    </div>
	<div region="center" style="padding:3px;border:0px;">
	<table style="height:100%;" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td  style="vertical-align:top">
				<table style="height:100%;" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="tree_top"><a href="" onClick="expandAll();return false;"><strong  style='color:#990033'>展开全部</strong></a>
        <a href="" onClick="unExpandAll();return false;"><strong  style='color:#990033'>全部折叠</strong></a> </td>
					</tr>
					<tr>
						<td class="tree_main">
							<ul id="purviewTree" class="ztree"></ul>
						</td>
					</tr>
					<tr>
						<td class="tree_bottom"></td>
					</tr>
					
					
				</table>
			</td>
			<td  style="vertical-align:top">
			<table style="height:100%;" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="tree_top"></td>
					</tr>
					<tr>
						<td class="tree_main">
							<ul id="purviewListTree" class="ztree"></ul>
						</td>
					</tr>
					<tr>
						<td class="tree_bottom"></td>
					</tr>
					
					
				</table>
			</td>
			<td style="vertical-align:top">
			<fieldset style="width:150px;margin-left:5px;padding:5px;">

								<legend >功能提示</legend>
								 【说明】当前显示的用户对应的权限组，仅为用户自身对应的权限组信息，不包含用户对应部门、角色所绑定的权限组。<br><br>
								 【建议】基于通过管理的需求,建议个性化授权时使用此授权。<br><br>
							</fieldset>
			
			</td>
		</tr>
	</table>
	<s:form name="editForm" id="editForm" theme="simple">
		 <s:hidden name="purviewIds" id="purviewIds" />
		 <s:hidden name="roleId" id="roleId"/>
</s:form>
	</div>
	
</body>
</html>
