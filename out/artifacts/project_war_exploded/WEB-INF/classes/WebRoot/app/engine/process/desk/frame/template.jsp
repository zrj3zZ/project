<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/portal.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" charset="gb2312"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/plugins/jquery.portal.js"></script>
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="iwork_js/processManagement.js" charset="gb2312"></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js" charset="gb2312"></script>
	<style type="text/css">
		.b1,.b2,.b3,.b4,.b1b,.b2b,.b3b,.b4b,.b{display:block;overflow:hidden;}
		.b1,.b2,.b3,.b1b,.b2b,.b3b{height:1px;}
		.b2,.b3,.b4,.b2b,.b3b,.b4b,.b{border-left:1px solid #999;border-right:1px solid #999;}
		.b1,.b1b{margin:0 5px;background:#999;}
		.b2,.b2b{margin:0 3px;border-width:2px;}
		.b3,.b3b{margin:0 2px;}
		.b4,.b4b{height:2px;margin:0 1px;}
		.d1{background:#F7F8F9;}
		
	</style>
	<script type="text/javascript">
	$(function(){			
				
				$('#editForm').form({  
				    onSubmit:function(){  
				        return $(this).form('validate');  
				    },  
				    success:function(data){  
				        alert(data);  
				    }  
				});
				
				$('#editForm').ajaxForm(); 
				
		});	
		
	function doSubmit(){
		
		var taskId = document.forms[0].taskId.value;
		var cmd = document.forms[0].cmd.value;
		var nextUser = document.forms[0].nextUser.value;
		var hasUser = document.forms[0].hasUser.value;
		
		 if($("nextUserForm").form('validate')){
			 $.ajax({
					type : 'POST',
					url : 'processManagement!doCmd.action?taskId=' + taskId + '&cmd=' + cmd + '&nextUser=' + nextUser + '&hasUser=' + hasUser,
					success : function(msg) {
						msg = msg.replace(/^\s+|\s+$/g, "")
						if (msg == 'nouser') {
							$('#next_user_window').window('open');
						} else if (msg == 'complete'){
							$('#next_user_window').window('close');
							window.parent.$('#mainFrameTab').tabs("close",window.parent.$('#mainFrameTab').tabs('getSelected').panel('options').title);
						}
					}
				});
		 //	document.forms[0].submit();
		 }else{
			$("a[icon=pagination-load]").trigger('click');
		 }
	}		
	
	
				
	
	</script>
</head>
<body class="easyui-layout">
<!-- TOP区 -->
	<div region="north" border="false" style="padding:0px;overflow:no">
	<div style="padding:2px;background:#efefef;border-bottom:1px solid #efefef">
		流程处理
	</div>
	</div>
   
	<div id="mainzone" region="center" style="padding:3px;" >		
		
		
		<div id="formzone" style="width:80%;float:left;border-style:solid;border-width:1px;border-right-width:0px; border-color:#8db2e3">
			表单
		</div>
		<div id="opzone" style="float:left;border-style:solid; width:20%;border-width:1px;border-left-width:1px; border-color:#8db2e3">
			操作
			<br>
			<div>
				<s:property value="cmdListHtml" escapeHtml="false" />				
			</div>
		</div>		 
	</div>
	
	
	<!-- next task user -->
    <div id="next_user_window" class="easyui-window" title="选择办理人" modal="true" closed="true" collapsible="false" minimizable="true"
        maximizable="false" icon="icon-save"  style="width: 400px; height: 150px; padding: 5px;
        background: #fafafa;">
        <div class="easyui-layout" fit="true">
            <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <s:form id="nextUserForm" name="nextUserForm"  theme="simple"  enctype="multipart/form-data">
            	
		        <div>
		            <label for="message">&nbsp;&nbsp;&nbsp;&nbsp;办理人：</label>
		             <input class="easyui-validatebox" type="text" id="nextUser" name="nextUser" style="width:190px"></input>
		        </div>
                
                
                
                <s:hidden name="taskId" />
                <s:hidden name="cmd" />
                <s:hidden name="hasUser" />
                
               </s:form> 
            </div>
            <div region="south" border="false" style="text-align: right; height: 30px; line-height: 30px;padding-top:5px;">
                <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:doSubmit();" >
                    确定</a> <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:cancel()">取消</a>
            </div>
        </div>
    </div>
	
	
	<script>
		function setheight() {
			var mainzone = document.getElementById('mainzone');
        	var sidebar1=document.getElementById('formzone');
        	var sidebar2=document.getElementById('opzone');
        	
        	
        	
        	
        	sidebar1.style.height=document.body.clientHeight -29+'px';
			sidebar2.style.height=document.body.clientHeight -29+'px';
			
    	}
    	setheight();
		onresize=setheight;	
	</script>
	
   
</body>
</html>
