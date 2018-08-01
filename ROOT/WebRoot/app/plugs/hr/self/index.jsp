<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html> 
<head> 
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>单选地址簿</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script> 	 
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script> 
	<script type="text/javascript">
		//加载导航树 
		var api = art.dialog.open.api, W = api.opener;
		$(function(){ 
			initTree();
		});
		function initTree(){
			var setting = {
						check: {
							enable: true
					    },
					    view: {
							selectedMulti: false
						},
						async: {
							enable: true,  
							url:"iwork_hr_self_json.action", 
							dataType:"json"
						},
						callback: {
							onClick:onClick
						}  
					};
				$.fn.zTree.init($("#multitree"), setting);//加载导航树 
		}
		//点击事件
			function onClick(event, treeId, treeNode, clickFlag){
			var zTree = $.fn.zTree.getZTreeObj("multitree"); 
					if(treeNode.isParent){
						if(treeNode.open){
							zTree.expandNode(treeNode, false, null, null, true);
						}else{ 
							zTree.expandNode(treeNode, true, null, null, true);
						}
					}else{
						if(!treeNode.checked){ 
							zTree.checkNode(treeNode, true, true,true);
				 		}else{  
			 				zTree.checkNode(treeNode,false, true,true);
				 		}
					}
			}
			//获得选择节点
	//设置权限

		function seportlet(){
		//===装载数据
			var zTree = $.fn.zTree.getZTreeObj("multitree");
 			var nodes = zTree.getCheckedNodes(true);
 			var str = "";  
 			for(var i=0;i<nodes.length;i++){
 				var type = nodes[i].type;
 				if(nodes[i].chkDisabled)continue;
 				var tmp = nodes[i].key;
 				if(i<nodes.length-1){
 					tmp+=","; 
 				}
 				str+=tmp;
 			}
 			$("#ids").val(str);
 			//===请求服务器==
			$.post('iwork_hr_self_doset.action',$("#editForm").serialize(),function(data){
		    	if(data=='success'){ 
		    		alert("设置成功");
		    		api.close();
		    	}else{ 
		    		alert("设置异常,请稍后再试");
		    	}

		  }); 
		}


		//查询回车事件
	    function enterKey(){
			if (window.event.keyCode==13){
				return; 
			}
		} 
	</script> 
</head>
<body class="easyui-layout">
	<div region="north" style="border-bottom:1px solid #efefef" border="false" >
		<div class="tools_nav">
			<a href="#" onclick="seportlet()" class="easyui-linkbutton" plain="true" iconCls="icon-add">发布到我的员工自助</a>	
			<a href="javascript:api.close();" class="easyui-linkbutton" plain="true" iconCls="icon-cancel">关闭</a>
		</div>	
		<div nowrap style="padding: 5px;text-align:center">
			
		</div>
	</div>
    <div region="center" border="false"  align="center" style="border:0px solid #C0C0C0;">
    <div style="text-align:left;padding:3px;"><a href="javascript:expandAll();">全部展开</a>/<a href="javascript:unExpandAll();">全部收起</a></div>
	    <s:form name="editForm" id="editForm" theme="simple">
	    	<div>
	         <ul id="multitree" class="ztree"></ul>
	        </div>
	    	<s:hidden name="ids" id="ids"></s:hidden>
	    </s:form> 
    </div>
    <div region="south" style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;" border="false" >
		已选择：<span id="selectinfo"><s:property value="input"/></span>
	</div>
</body>
</html>