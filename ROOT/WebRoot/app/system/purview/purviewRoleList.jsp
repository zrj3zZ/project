<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
	<title>权限组进行角色授权</title>
	<s:head/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="iwork_css/common.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"> 
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
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
var api = art.dialog.open.api, W = api.opener;
$(document).ready(function(){
			var setting = {
					check: {
						enable: true
					},
					view: { 
						selectedMulti: false
					},
					async: {
						enable: true, 
						url:"openpurviewToRole_json.action?purviewid=<s:property value='purviewid'/>",
						dataType:"json"
					},callback: {  
						onClick:onPurviewClick
					}
				};
			
			$.fn.zTree.init($("#purviewTree"), setting);
		});
		
		//点击权限组树
		function onPurviewClick(event, treeId, treeNode,clickFlag) {
		var zTree = $.fn.zTree.getZTreeObj("purviewTree");
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
					if($("#purgroup_save_purviewid").val()==""){
		 				art.dialog.tips('请选择您要授权的对象');
		 				return;
		 			}
					var zTree = $.fn.zTree.getZTreeObj("purviewTree");
		 			var nodes = zTree.getCheckedNodes(true);
		 			var str = ""; 
		 			for(var i=0;i<nodes.length;i++){
		 				var type = nodes[i].type;
		 				if(type=='group')continue;
		 				var tmp = nodes[i].id; 
		 				if(i<nodes.length-1){ 
		 					tmp+=","; 
		 				}
		 				str+=tmp;
		 			}
		 			$("#roleIds").val(str);  
					$.post('setpurviewRole_do.action',$("#editForm").serialize(),function(data){
				    	if(data=='success'){   
				    		art.dialog.tips("授权成功");
				    	}else{
				    		art.dialog.tips("授权异常,请稍后再试");
				    	}
				  }); 
				}
</script>
</head>
<body  class="easyui-layout">
<div region="north" border="false" split="false" >
<div class="tools_nav">
        <table width="100%" border="0" cellpadding="0" cellspacing="0" >
	      <tr>
          	<td align="left"><span id="title_span" style='font-size:14px;font-family:黑体'><img alt="权限组【用户】授权" src="iwork_img/shield.png" border="0">权限组【流程】授权</span></td>
          	<td style="text-align:right;padding:0px;padding-right:10px;padding-bottom:2px;">
          		<a href='javascript:setpurview();' class="easyui-linkbutton" iconCls="icon-process-purview" plain="false">授权</a>
				<a href='javascript:api.close();' class="easyui-linkbutton" iconCls="icon-cancel" plain="false">关闭</a>
          	</td>
           </tr>
	    </table>
	  </div>
</div>
<div region="west"   border="false"  region="west" icon="icon-reload"  split="true" style="width:200px;padding-left:5px;overflow:hidden;border-right:1px solid #efefef">
	<s:property value="tablist" escapeHtml="false"/>
</div>
<div region="center" border="false" >
<s:form name="editForm" id="editForm"  action="purgroup_save" >
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td>
	<table width="100%" border="0" cellspacing="0" cellpadding="2">
    <tr>  
      <td  width="85%" colspan="0" align="center"   valign="top""> 
        <div align="left"> 
         <ul id="purviewTree" class="ztree"></ul>
        </div></td>
    </tr>
  </table>
	</td>
  </tr>
</table>
		<s:if test="null == model">
		 	<s:hidden name="model.id" value="%{id}"/>
		 </s:if>	 	
	     <s:else>
		 	<s:hidden name="model.id" />
		 </s:else>
		 <s:hidden name="purviewid" /> 
		 <s:hidden name="roleIds" id="roleIds" /> 
</s:form>
</div>
</body>
</html>
