<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>任务转发</title>
 
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/addressbook/radioaddressbookaction.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	 
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
var api = art.dialog.open.api, W = api.opener;
	 var arr=new Array();
        $(document).ready(function(){
           $(document).bind("contextmenu",function(e){
             	//return false;   
           });
           initTree();
       });
       function dosearch(){
			var searchOrg = $("#searchOrg").val();
			if(searchOrg==""){
				alert("请输入查询条件");
				return;
			} 
			var url = "multibook_search.action?parentDept=&currentDept=&startDept=&searchOrg="+searchOrg;
			var setting = { 
					check: {
							enable: false 
					}, 
					async: {
						enable: true, 
						url:url, 
						dataType:"json",
						autoParam:["id","nodeType"] 
					}, 
					callback: { 
					beforeAsync: beforeAsync,
						onAsyncSuccess: onAsyncSuccess,
						onAsyncError: onAsyncError,
						onClick:onClick
					} 
				};
			$.fn.zTree.init($("#forwardUser"), setting);//加载导航树 
		}
		//查询回车事件
	    function enterKey(){
			if (window.event.keyCode==13){
				 dosearch();
				return; 
			}
		} 
        function initTree(){
			var setting = {
						check: {
							enable: false
					    },
					    view: {
							selectedMulti: false
						},
						async: {
							enable: true, 
							url:"multibook_orgjson.action?parentDept=&currentDept=&startDept=", 
							dataType:"json",
							autoParam:["id","nodeType"] 
						},

						callback: {
							beforeAsync: beforeAsync,
							onAsyncSuccess: onAsyncSuccess,
							onAsyncError: onAsyncError,
							onClick:onClick
						} 
					};
				$.fn.zTree.init($("#forwardUser"), setting);//加载导航树 
		}
       function onClick(event, treeId, treeNode, clickFlag){
    	   var zTree = $.fn.zTree.getZTreeObj("forwardUser"); 
			if(treeNode.isParent){
				if(treeNode.open){
					zTree.expandNode(treeNode, false, null, null, true);
				}else{ 
					zTree.expandNode(treeNode, true, null, null, true);
				}
			}else{
				var str="<span class='selItem' id='"+treeNode.id+"'>"+treeNode.name+"</span>";
			 	$('#selectHtml').html(str);
			 	var address = treeNode.useraddress+",";
		    	$("#receiveUser").val(address);
			}
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
			var zTree = $.fn.zTree.getZTreeObj("forwardUser");
			if (asyncForAll) {
				$("#demoMsg").text(demoMsg.expandAll);
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
			var zTree = $.fn.zTree.getZTreeObj("forwardUser");
			zTree.expandAll(false);
		}

		function expandNodes(nodes) {
			if (!nodes) return;
			curStatus = "expand";
			var zTree = $.fn.zTree.getZTreeObj("forwardUser");
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
			var zTree = $.fn.zTree.getZTreeObj("forwardUser");
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
			var zTree = $.fn.zTree.getZTreeObj("forwardUser");
			for (var i=0, l=nodes.length; i<l; i++) {
				if (nodes[i].isParent && nodes[i].zAsync) {
					asyncNodes(nodes[i].children);
				} else {
					goAsync = true;
					zTree.reAsyncChildNodes(nodes[i], "refresh", true);
				}
			}
		}  
       function exexuteSendAction(){
      	 var receiveUser = $("#receiveUser").val();
       		if(receiveUser == ''){
       			art.dialog.tips("请选择您要转发人员信息",2);
       			return false;
       		}
       		 var obj = $('#ifromMain').serialize();
			     $.post("processRuntimeExecuteHandle.action",obj,function(data){
			            var arr=data;
						if(arr!=null){
							//alert(responseText);
							if(arr=="success"){
								art.dialog.tips("发送成功",2);
								//刷新办理列表
								try{ 
								}catch(err){}
								setTimeout('api.close();',2000);
							}else if(arr=="ERROR-10014"){
								art.dialog.tips("发送异常,任务接收人中包含了非法账号<br/>(错误编号:ERROR-00014)",3);
							}else if(arr=="ERROR-10015"){
								art.dialog.tips("发送异常,任务接收人地址异常<br/>(错误编号:ERROR-00015)",3); 
							}else if(arr=="ERROR-10005"){ 
								art.dialog.tips("发送异常,系统参数异常<br/>(错误编号:ERROR-00005)",3);
							}else if(arr=="ERROR-10017"){  
								art.dialog.tips("发送异常,接收人地址未填写完整，请检查<br/>(错误编号:ERROR-00017)",3);
							}else{
								art.dialog.tips("发送异常(错误编号:ERROR-00018)",3);
							}

						}else{
							art.dialog.tips("发送失败,返回值异常(错误编号:ERROR-10016)",3);
						}
			     });
       }
       //设置节点
       function setStepName(value){
       	$("#targetStepName").val(value);
       }
       //调用父页面脚本，关闭办理窗口
        function close(){
        	api.close();
       }
</script>
<style type="text/css">
.nextStep{
	font-size:14px;
	padding-left:10px;
}
.searchkey{
	font-size:12px;
	text-align:right;
}
.selItem{
	font-size:12px;
	padding:2px;
	background-color:#efefef;
	border:1px solid #efefef;
	margin-top:3px;
	margin-right:3px;
	cursor:pointer; 
}
#dept a{
	font-size:12px; 
}
</style>
</head>

<body class="easyui-layout">
<div region="center" border="false" style="border-left:1px solid #efefef;">
<s:form name="ifromMain" id="ifromMain" method="post" action="processRuntimeExecuteForward" theme="simple">
	<table  width="100%"  border="0">
		<tr>
				<td >
					<table width="100%">
						<tr>
							<td  class="nextStep">请选择跳转的节点</td>
							<td  align="left"><s:select id="targetStepId"  name="targetStepId" onchange="setStepName($('#targetStepId').find('option:selected').text())"  headerKey="" headerValue="--请选择要跳转的节点--" listKey="targetStepId" listValue="targetStepName"  list="targetList"  theme="simple"/></td>
						</tr>
					</table> 
				</td>
			</tr>
	</table>

		<table width="100%"  border="0">
			<tr>
				<td >
				
						<div id="dept" style="height:350px; border:1px #C0C0C0 solid; overflow-x:auto; overflow-y:auto;">	
						<a href="javascript:expandAll();">全部展开</a>/<a href="javascript:unExpandAll();">全部收起</a>
						<input type="text" name="searchOrg"  id="searchOrg"  onclick="this.value=''" style="width:120px" onKeyDown="enterKey();if(window.event.keyCode==13) return false;">&nbsp;<a href="#" onclick="dosearch()" class="easyui-linkbutton" plain="false" iconCls="icon-search"></a>
							<ul id="forwardUser"  class="ztree"></ul>
						</div>
				</td>
			</tr>
			</table>
			<div style="padding:5px;padding-left:10px">已选择的发送人:</div>
						<div id="selectHtml" style="padding-left:30px"></div>
			<!-- 办理参数 -->
			<s:hidden id ="title" name="title"/>
			<s:hidden id ="targetStepName" name="targetStepName"/>
			<s:hidden id ="action" name="action"/>
					<!--表单参数-->
		<span style="display:none">
			<s:hidden id ="receiveUser" name="receiveUser"/>
			<s:hidden id ="actDefId" name="actDefId"/>
			<s:hidden  id ="prcDefId" name="prcDefId"/> 
			<s:hidden id ="actStepDefId" name="actStepDefId"/>
			<s:hidden id ="formId" name="formId"/>
			<s:hidden id ="currentUser" name="currentUser"/>
			<s:hidden id ="taskId" name="taskId"/>
			<s:hidden id ="instanceId" name="instanceId"/>
			<s:hidden id ="excutionId" name="excutionId"/>
			<s:hidden id ="dataid" name="dataid"/>
			<input name='submitbtn' id='submitbtn' type="submit" />
		</span>
		</s:form>
</div>		
		<div region="south" style="height:35px;text-align:right;padding-top:5px;border:0px">
						<a href="#" onclick="exexuteSendAction()" class="easyui-linkbutton" plain="false" iconCls="icon-ok">发送</a>
						<a href="javascript:close();" class="easyui-linkbutton" plain="false" iconCls="icon-cancel">取消</a>
		</div>
</body>
</html>
