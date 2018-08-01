<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>操作提示</title>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">

<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/ui/base/jquery.ui.autocomplete.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/ui/jquery-ui-1.8.16.custom.js" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/ui/jquery.ui.autocomplete.js" ></script> 
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	 
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>	 

<script type="text/javascript">
        $(document).ready(function(){
           $(document).bind("contextmenu",function(e){
              //return false;   
           });
			var setting = {
						check: {
							enable: true
					    },
					    view: {
							selectedMulti: false
						},
						async: {
							enable: true, 
							url:"multibook_orgjson.action?parentDept=&currentDept=&startDept=&input=<s:property value="ccUsers" escapeHtml="false"/> ", 
							dataType:"json",
							autoParam:["id","nodeType"] 
						},
						callback: {
						beforeAsync: beforeAsync,
							onAsyncSuccess: onAsyncSuccess,
							onAsyncError: onAsyncError,
							onClick:onClick,
							onCheck:onCheck
						} 
				 };
				$.fn.zTree.init($("#multitree"), setting);//加载导航树 
			
			$("#receiveUser").bind( "keydown", function( event ) {
				if ( event.keyCode === $.ui.keyCode.TAB &&
						$( this ).data( "autocomplete" ).menu.active ) {
					event.preventDefault();
				}
			}).autocomplete({
				source: function( request, response ) {
					$.getJSON( "user_load_autocomplete_json.action", {
						term: extractLast( request.term )
					}, response );
				},
				search: function() {
					var term = extractLast( this.value );
					if ( term.length < 2 ) {
						return false;
					}
				},
				focus: function() {
					return false;
				},
				select: function( event, ui ) {
					var terms = split( this.value );
					terms.pop();
					terms.push( ui.item.value );
					terms.push( "" );
					this.value = terms.join( ", " );
					return false;
				}
			});
       });
       	//录入提示
		   	function split( val ) {
				return val.split( /,\s*/ );
			}
			function extractLast( term ) {
				return split( term ).pop();
			}
       function lockPage(){
		}
       function unLockPage(){
			return true;	
	   }
       function failResponse(){
      	 art.dialog.tips("发送失败",3);
			return true;
		}
		function successResponse(responseText, statusText, xhr, $form){
			var arr=responseText;
		//	alert(responseText);
			if(arr!=null){
				if(arr=="success"){
					 art.dialog.tips("抄送人添加成功",2);
					 setTimeout('parent.$.dialog.list["ccDialog"].close();',1000);
				}else{
					art.dialog.tips("抄送人添加失败",1);
				}
			}else{
				art.dialog.tips("抄送人添加失败,请联系管理员",3);
			}
			
			return true;
		}
       function exexuteSendAction(){
       	var receiveUser = $("#receiveUser").val();
       	if(typeof(parent.$.dialog.list["sendDialog"])=="object"){//判断编辑窗口是否打开
			parent.$.dialog.list["sendDialog"].get("sendDialog","window").setCCList(receiveUser);
			close();
		}
       }
       //调用父页面脚本，关闭办理窗口
        function close(){
        	parent.$.dialog.list["ccDialog"].close();
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
 		function onCheck(event, treeId, treeNode){
	 		var zTree = $.fn.zTree.getZTreeObj("multitree");
 			var nodes = zTree.getCheckedNodes(true);
 			var str = "";  
 			for(var i=0;i<nodes.length;i++){
 				if(nodes[i].nodeType=='dept'||nodes[i].nodeType=='com'){
	 					continue;
	 			}
 				var tmp = nodes[i].userId+"["+nodes[i].name+"]";
 				if(i<nodes.length-1){
 					tmp+=","; 
 				}
 				str+=tmp;
 			}
 			
 			var ru =  $("#receiveUser").val();
 			$("#receiveUser").val(str);
 			
 		}
			function insertData(){
				//关闭窗口
				api.close();
			}
			//全部展开
	var demoMsg = {
			async:"正在进行异步加载，请等一会儿再点击...",
			expandAllOver: "全部展开完毕",
			asyncAllOver: "后台异步加载完毕",
			asyncAll: "已经异步加载完毕，不再重新加载",
			expandAll: "已经异步加载完毕，使用 expandAll 方法"
		}
		function beforeAsync() {
			curAsyncCount++;
		}
		function onAsyncSuccess(event, treeId, treeNode, msg) {
			curAsyncCount--;
			if (curStatus == "expand") {
				expandNodes(treeNode.children);
			} else if (curStatus == "async") {
				asyncNodes(treeNode.children);
			}
			if (curAsyncCount <= 0) {
				if (curStatus != "init" && curStatus != "") {
					$("#demoMsg").text((curStatus == "expand") ? demoMsg.expandAllOver : demoMsg.asyncAllOver);
					asyncForAll = true;
				}
				curStatus = "";
			}
		}
		function onAsyncError(event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {
			curAsyncCount--;
			if (curAsyncCount <= 0) {
				curStatus = "";
				if (treeNode!=null) asyncForAll = true;
			}
		}

		var curStatus = "init", curAsyncCount = 0, asyncForAll = false,
		goAsync = false;
		function expandAll() {
			var zTree = $.fn.zTree.getZTreeObj("multitree");
			if (asyncForAll) {
				$("#demoMsg").text(demoMsg.expandAll)
				zTree.expandAll(true);
			} else {
				expandNodes(zTree.getNodes());
				if (!goAsync) {
					$("#demoMsg").text(demoMsg.expandAll);
					curStatus = "";
				}
			}
		}
		//全部折叠
		function unExpandAll() {
			var zTree = $.fn.zTree.getZTreeObj("multitree");
			zTree.expandAll(false);
		}
		function expandNodes(nodes) {
			if (!nodes) return;
			curStatus = "expand";
			var zTree = $.fn.zTree.getZTreeObj("multitree")
			for (var i=0, l=nodes.length; i<l; i++) {
				zTree.expandNode(nodes[i], true, false, false);
				if (nodes[i].isParent && nodes[i].zAsync) {
					expandNodes(nodes[i].children);
				} else {
					goAsync = true;
				}
			}
		}
		function asyncAll() {
			var zTree = $.fn.zTree.getZTreeObj("multitree");
			if (asyncForAll) {
				$("#demoMsg").text(demoMsg.asyncAll);
			} else {
				asyncNodes(zTree.getNodes());
				if (!goAsync) {
					$("#demoMsg").text(demoMsg.asyncAll);
					curStatus = "";
				}
			}
		}
		function asyncNodes(nodes) {
			if (!nodes) return;
			curStatus = "async";
			var zTree = $.fn.zTree.getZTreeObj("multitree");
			for (var i=0, l=nodes.length; i<l; i++) {
				if (nodes[i].isParent && nodes[i].zAsync) {
					asyncNodes(nodes[i].children);
				} else {
					goAsync = true;
				zTree.reAsyncChildNodes(nodes[i], "refresh", true);
				}
			}
		}
</script>
<style type="text/css">
		.ui-menu-item{
		font-size:10px;
		color:#0000FF;
		background-color:#FFF;
		border-left:1px solid #f2f2f2;
		border-right:1px solid #f2f2f2;
		border-bottom:1px solid #f2f2f2;
		text-decoration:none;
		display:block;
		padding:.2em .4em;
		line-height:1.5;
		zoom:1;
		float: left;
		clear: left;
		width: 100%;
	}
	.ui-state-hover{
		font-size:10px;
		color:#0000FF;
		background-color:#EEEEEE;
		text-decoration:none;
		display:block;
		padding:.2em .4em;
		line-height:1.5;
		zoom:1;
		float: left;
		clear: left;
		width: 100%;
	}

.ui-menu {
	list-style:none;
	padding: 2px;
	margin: 0;
	display:block;
	float: left;
}
	.nextStep{
		font-size:14px;
		color:#0000FF;
		text-align: left;
		padding-left: 3px;
		padding-bottom: 10px;
		font-family:"宋体";
		border-bottom:1px #999999 dotted;
		vertical-align:middle;
		word-wrap:break-word;
		word-break:break-all;
		font-weight:500;
		line-height:12px;
		padding-top:5px;
	}
	.StepTitle{
		font-size:13px;
		color:#666;
		text-align: right;
		padding-left: 10px;
		font-family:"宋体";
		vertical-align:middle;
		word-wrap:break-word;
		word-break:break-all;
		line-height:16px;
		font-weight:800;
		white-space:nowrap;
		background-color:#F5F8F7;
		 text-align:left;
		 border-bottom:1px solid #EFEFEF
	}
	.ItemTitle{
		font-size:12px;
		color:#0000FF;
		text-align: right;
		padding: 10px;
		font-family:"宋体";
		vertical-align:middle;
		word-wrap:break-word;
		word-break:break-all;
		font-weight:800;
		line-height:12px;
		 white-space:nowrap;
	}
	.pageInfo{
		font-size:12px;
		color:#0000FF;
		text-align: left;
		padding-left: 10px;
		font-family:"宋体";
		vertical-align:middle;
		word-wrap:break-word;
		word-break:break-all;
		font-weight:500;
		line-height:12px;
		padding-right:20px;
	}
	#div{ width:450px; padding-top:0px; }
</style>
</head>

<body >
<s:form name="ifromMain" id="ifromMain" method="post" action="processRuntimeExecuteAddCC" theme="simple">
<!-- TOP区 -->
		<table width="100%"  border="0">
			<tr>
				<td class="nextStep">请选择抄送人：<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="receiveUser" value="<s:property value="ccUsers"/>" style="color:#0000FF;" size="60" id="receiveUser"></td>
			</tr>
			<tr>
				<td >
						<div id="dept" style="height:370px; border:1px #C0C0C0 solid; overflow-x:auto; overflow-y:auto;">
							<ul id="multitree" class="ztree"></ul> 
						</div>  
						<div style="text-align:right;padding-top:5px;vertical-align:bottom">
						<a href="#" onclick="exexuteSendAction();" class="easyui-linkbutton" plain="false" iconCls="icon-ok">添加</a>
						<a href="javascript:close();" class="easyui-linkbutton" plain="false" iconCls="icon-cancel">取消</a>
						</div> 
				</td>
			</tr>
			
			</table>
			<!-- 办理参数 -->
			<s:hidden id ="targetStepId" name="targetStepId"/>
			<s:hidden id ="targetStepName" name="targetStepName"/>
					<!--表单参数-->
		<span style="display:none">
			<s:hidden id ="actDefId" name="actDefId"/>
			<s:hidden  id ="prcDefId" name="prcDefId"/> 
			<s:hidden id ="actStepDefId" name="actStepDefId"/>
			<s:hidden id ="formId" name="formId"/>
			<s:hidden id ="taskId" name="taskId"/>
			<s:hidden id ="instanceId" name="instanceId"/>
			<s:hidden id ="excutionId" name="excutionId"/>
			<s:hidden id ="dataid" name="dataid"/>
			<input name='submitbtn' id='submitbtn' type="submit" />
		</span>
		</s:form>
</body>
</html>
