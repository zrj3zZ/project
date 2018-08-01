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
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script> 
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	 
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.excheck-3.4.min.js"></script>	 
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   charset="utf-8"  ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"  charset="utf-8"   ></script>
<script type="text/javascript">
var api = art.dialog.open.api, W = api.opener;
var mainFormValidator;
$().ready(function() {
	window.alert = function(str) {
		return ;
}
mainFormValidator =  $("#editForm").validate({
 });
 mainFormValidator.resetForm();
});
        $(document).ready(function(){
           var setting2 = {
					check: {
						enable: true
					},
					view: { 
						selectedMulti: false
					},
					async: {
						enable: true, 
						url:"ProcessStepOL_showJSON.action?actDefId=<s:property value="actDefId"/>&actStepDefId=<s:property value="actStepDefId"/>&instanceId=<s:property value="instanceId"/>&excutionId=<s:property value="excutionId"/>&taskId=<s:property value="taskId"/>",
						dataType:"json", 
						autoParam:["id","type"]
					},callback: { 
						onClick:onClick
					}
				}; 
			$.fn.zTree.init($("#treeMenu"), setting2);
       });
		//点击权限组树
		function onClick(event, treeId, treeNode,clickFlag) {
				var zTree = $.fn.zTree.getZTreeObj("treeMenu");
				if(!treeNode.checked){
	 				zTree.checkNode(treeNode, true, true, clickFlag);
		 		}else{
	 				zTree.checkNode(treeNode, false, true, false);
		 		}
		}

				//添加跟踪记录
				function doSubmit(){
					var valid = mainFormValidator.form(); //执行校验操作
					if(!valid){
						return false;
					}
				var zTree = $.fn.zTree.getZTreeObj("treeMenu");
				var nodes = zTree.getCheckedNodes(true);
	 			var str = "";  
	 			for(var i=0;i<nodes.length;i++){
	 				var tmp = nodes[i].id; 
	 				var type = nodes[i].type; 
	 				var chkDisabled = nodes[i].chkDisabled; 
	 				
	 				if(type=='category'||chkDisabled){
	 					continue; 
	 				}
	 				if(i<nodes.length-1){
	 					tmp+=","; 
	 				}
	 				str+=tmp;
	 			}
					if(str==''){
						alert("请选择操作");
						return;
					}
				$("#olids").val(str); 
					$.post('ProcessStepOL_save.action',$("#editForm").serialize(),function(data){
				    	if(data=='success'){
				    		alert("添加成功");
				    		api.close();
				    	}else{
				    		alert("添加异常,请稍后再试");
				    	}
				  }); 
				}
</script>
<style type="text/css">
	.form_title{  
			font-family:黑体;
			font-size:14px;
			text-align:right;
			vertical-align:top;
		}
		.form_data{
			font-family:宋体;
			font-size:12px;
			text-align:left;
			color:0000FF; 
		}
</style>
</head>

<body class="easyui-layout">
<div region="north" border="false" style="font-size:16px;font-family:黑体;border-bottom:1px solid #efefef;padding-bottom:5px;height:50px;">
	<img alt="线下任务反馈" src="iwork_img/menulogo_yingyun.gif" border="0" >线下任务反馈
</div> 
<div region="center" border="false" style="padding:10px;margin-bottom:5px;">
<s:form name="editForm" id="editForm" action="km_dir_save.action" theme="simple">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
        <td class='form_title'>线下任务列表:</td>
        <td class='form_data'>
       		 <div style="overflow:auto; width:180px;height: 100px;border:0px solid #ccc;">
        	 <ul id="treeMenu" class="ztree" style="margin-top:0;"></ul>
        	 </div>
        </td>
        </tr>
         <tr>
                      <td class="form_title">备注说明:</td>
                      <td class="form_data">
                           <s:textarea name="model.memo" cssStyle="width:300px;height:80px;" class="{string:true}"></s:textarea>
                      </td>
        </tr> 
    </table>
   <s:hidden   name="olids" id = "olids"/>
   <s:hidden   name="model.id"/>
   <s:hidden   name="model.actDefId" />
   <s:hidden   name="model.prcDefId" />
   <s:hidden   name="model.actStepId" />
   <s:hidden   name="model.taskid" />
   <s:hidden   name="model.instanceid" />
   <s:hidden   name="model.executionid" />
</s:form>
</div> 
<div region="south" border="false" style="text-align: right; height: 50px; line-height: 30px;padding-top:5px;padding-right:15px;">
	<a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:doSubmit();" >确认</a> 
	<a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:api.close();">关闭</a>
</div>
</body>
</html>
<script language="JavaScript">
	jQuery.validator.addMethod("string", function(value, element) {
		var sqlstr=[" and "," exec ", " count ", " chr ", " mid ", " master ", " or ", " truncate ", " char ", " declare ", " join ","insert ", "select ", "delete ", "update ","create ","drop "]
		var patrn=/[“”`~!#$%^&*+<>?"{},;'[]（）—。[\]]/im;
		if(patrn.test(value)){
    	}else{
    		var flag = false;
    		var tmp = value.toLowerCase();
    		for(var i=0;i<sqlstr.length;i++){
    			var str = sqlstr[i];
    			if(tmp.indexOf(str)>-1){
    				flag = true;
    				break;
    			}
    		}
    		if(!flag){
    			return "success";
    		}
    	}
    }, "包含非法字符!");
</script>
