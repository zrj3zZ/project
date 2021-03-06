<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta http-equiv="Content-Type" content="text/html;" />
<title>任务转发</title>
 
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/> 
	<link href="iwork_css/button.css" rel="stylesheet" type="text/css"/>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<script language="javascript" src="iwork_js/commons.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
	 var arr=new Array();
	 var api,W;
		try{
			api=  art.dialog.open.api;
			W = api.opener;	
		}catch(e){}
        $(document).ready(function(){
           $(document).bind("contextmenu",function(e){
             	return false;   
           });
           initTree();
       });
       function dosearch(){
			var searchOrg = $("#searchOrg").val();
			if(searchOrg==""){
				alert("请输入查询条件");
				return;
			} 
			var url = encodeURI("multibook_search.action?parentDept=&currentDept=&startDept=&searchOrg="+searchOrg);
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
       	   $("#sendbtn").attr("disabled",true);
      	 var receiveUser = $("#receiveUser").val();
       		if(receiveUser == ''){
       			 $("#sendbtn").attr("disabled",false);
       			alert("请选择您要转发人员信息");
       			return false;
       		}
       		 var obj = $('#ifromMain').serialize();
			     $.post("processRuntimeExecuteForward.action",obj,function(data){
			            var arr=data;
			             try{
			            	showSysTips();
			            }catch(e){alert('消息提取异常');$("#sendbtn").attr("disabled",false);}
						if(arr!=null){
							//alert(responseText);
							if(arr=="success"){
								alert("发送成功");
								//转发成功,发送短信通知
								var instanceid = $("#instanceId").val();
								var userName = $("#receiveUser").val();
								var actdefid = $("#actDefId").val();
								var seachUrl = encodeURI("zqb_forward_msgsend.action?userName="+userName+"&instanceid="+instanceid+"&actdefid="+actdefid);
								$.post(seachUrl,function(result){});
								//刷新办理列表
								try{
									window.parent.opener.reloadWorkList();
								}catch(err){} 
								setTimeout('_close();',1000); 
							}else if(arr=="ERROR-10002"){
								$("#sendbtn").attr("disabled",false);
								alert("系统执行转发操作时不允许转发给本人，操作失败(错误号:ERROR-0002)");
							}else if(arr=="ERROR-10003"){
								$("#sendbtn").attr("disabled",false);
								alert("发送失败，请联系管理员(错误号:ERROR-10003)");
							}else{
								$("#sendbtn").attr("disabled",false);
								alert("转发失败，返回值异常(错误号:ERROR-10004)");
							}
						}else{
							$("#sendbtn").attr("disabled",false);
							alert("转发失败，返回值异常(错误号:ERROR-10004)");
						}
			     });
       }
       //调用父页面脚本，关闭办理窗口
        function _close(){
        	window.onbeforeunload = null;//禁用表单关闭浏览器询问
        	if(typeof(api) =="undefined"){
        		window.close();
        	}else{ 
        		api.close(); 
        	}
       }
</script>
<style type="text/css">
.nextStep{
	font-size:16px;
	font-family:黑体;
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
.ItemTitle{
	font-size:12px;
	font-family:宋体;
}
</style>
</head>
<body >
<div region="north" border="false" style="height:38px;"> 
	<table  width="100%"  border="0">
		<tr>
				<td >
					<table width="100%">
						<tr>
							<td  class="nextStep">请选择您要转发的人</td>
							<td class="searchkey" align="right"><input type="text" name="searchOrg"  id="searchOrg"  onclick="this.value=''"  onKeyDown="enterKey();if(window.event.keyCode==13) return false;"><a href="#" onclick="dosearch()" class="easyui-linkbutton" plain="false" iconCls="icon-search"></a></td>
						</tr>
					</table> 
				</td>
			</tr>
	</table>
</div>
<div region="center" border="false" style="border-left:1px solid #efefef;">
<s:form name="ifromMain" id="ifromMain" method="post" action="processRuntimeExecuteForward" theme="simple">
		<table width="100%"  border="0">
			<tr>
				<td >
						<div id="dept" style="height:330px; border:1px #C0C0C0 solid;text-align:left; overflow-x:auto; overflow-y:auto;">
							<div style="height:325px;width:110px;">
							<a href="javascript:expandAll();">全部展开</a>/<a href="javascript:unExpandAll();">全部收起</a>
							<ul id="forwardUser"  class="ztree"></ul>
							</div>
						</div>
				</td>
			</tr>
			<tr>
				<td>
				
					<s:if test="RemindTypeList!=null">
					<table border="0">
					<tr>
						<td class="ItemTitle">提醒方式:	</td><td  class="ItemTitle">
							 <s:checkboxlist name="remindType" id="remindType" list="remindTypeList" 
					         labelposition="top"
					         listKey="key"
					         listValue="value" 
					         value="remindTypeList"
					         >
					        </s:checkboxlist>
						</td>
					</tr>
					</table>
					</s:if>
				</td>
			</tr>
			</table>
			<div style="padding:5px;padding-left:10px;font-size:12px;text-align:left">已选择的转发人:</div>
						<div id="selectHtml" style="padding-left:30px;font-size:12px;text-align:left"></div>
			<!-- 办理参数 -->
			<s:hidden id ="title" name="title"/>
			<s:hidden id ="targetStepId" name="targetStepId"/>
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
		<div region="south" style="height:40px;text-align:right;padding-top:5px;border:0px">
						<input id="sendbtn" type="button" style="height:35px" onclick="exexuteSendAction()" value="发送"   class="button_ send"/>
					<input type="button"  onClick="_close()" value="取消" style="height:35px"  class="button_ close"/>
		</div>
</body>
</html>
<script>
	$("#sendbtn").focus();
</script>