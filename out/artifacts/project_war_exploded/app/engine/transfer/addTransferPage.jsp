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
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/upload/swfobject.js"></script>
	<script type="text/javascript" src="iwork_js/upload/jquery.uploadify.v2.1.4.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script> 
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
    
	<style type="text/css">
		.form_title{  
			font-family:黑体;
			font-size:14px;
			text-align:right;
		}
		.form_data{
			font-family:宋体;
			font-size:12px;
			text-align:left;
			color:0000FF; 
		}
	</style>	
	<script type="text/javascript">
	var api = art.dialog.open.api, W = api.opener;
		$(document).ready(function() {
			$('#impModule').uploadify({
				'uploader'  : 'iwork_js/upload/uploadify.swf',
			    'script'    : 'sysImp_doUploadModule.action;jsessionid=<%=session.getId()%>',
			  //  'scriptData'     : {"title":$('#title').val(),"memo":$('#memo').val()},
			    'cancelImg' : 'iwork_img/del3.gif',
			    'fileDataName'   : 'impModule',
			    'removeCompleted' : false,
	            'folder'         : 'uploads',
	            'method'         : 'GET',
	            'queueID'        : 'fileQueue',
	            'auto'           : false,
	            'multi'          : false,
	            'fileExt'        : '*.zip',
	            'fileDesc'       : '*.zip',
	            'sizeLimit'      : 2*1024*1024,
	            buttonText:escape('上传'),//上传按钮的文字
	           // 'buttonImg'     : 'iwork_img/upload/select.gif',
	            'onError'     : function (event,ID,fileObj,errorObj) {
			    					if(errorObj.type=="File Size"){
			    						var byteSize  = Math.round(errorObj.info/ 1024 * 100) * .01;
			    						var suffix = 'KB';
										if (byteSize >= 1024) {
											byteSize  = Math.round(byteSize/ 1024 * 100) * .01;
											suffix = 'MB';
										}
			    						errorObj.type="文件大小不能超过"+byteSize+suffix;
			    					}else if(errorObj.type=="HTTP"){
			    						errorObj.type="文件上传失败";
			    					}
			    				},
			  'onAllComplete':function(event,data) {
			 		 alert('上传成功');
			 		 cancel();
			 		 W.reloadTree();
			  }
			  });
		});
		
		function doSubmit(){
			if($('#title').val()==''){
				alert('标题不允许为空');
				$('#title').focus();
				return;
			}
			if($('#memo').val()==''){
				alert('详细描述不允许为空');
				$('#memo').focus();
				return;
			}
			var trans_title = $('#title').val();
			var trans_memo = $('#memo').val(); 
			$("#impModule").uploadifySettings('scriptData',{'title':trans_title,'memo':trans_memo});
			jQuery('#impModule').uploadifyUpload();
			//document.forms[0].submit();
		}
		//关闭窗口
		function cancel(){
			api.close();
		}
	</script>
</head>
<body class="easyui-layout">
            <div region="center" border="false" style="padding: 15px; background: #fff; border:0px solid #ccc;">
            <s:form name="editForm" id="editForm" action="sysImp_doExecute.action" method="post" enctype="multipart/form-data"  theme="simple">
            	<table width="100%"  border="0" cellpadding="5" cellspacing="0"> 
            		 <tr>  
            			<td class="form_title"><span style="color:red;">*</span>导入模型标题:</td>
            			<td class="form_data">
            				<s:textfield id="title" name="title"  theme="simple" ></s:textfield>
            			</td>
            		</tr>
            		<tr>  
            			<td class="form_title"><span style="color:red;">*</span>详细描述:</td>
            			<td class="form_data"><s:textarea  id="memo"  name="memo"  cssStyle="width:230px;height:100px" theme="simple" ></s:textarea></td>
            		</tr> 

            		<tr>
            			<td class="form_title"><span style="color:red;">*</span>上传模型:</td>
            			<td  class="form_data">  
            					<s:file id ="impModule" name ="impModule" label ="上传模型"  theme="simple" cssStyle="width:200px"  cssClass='input-def'/>
            					<div id="fileQueue">
            			</td> 
            		</tr>
            	</table>
               </s:form>  
            </div> 
            <div region="south" border="false" style="text-align: right; height: 50px; line-height: 30px;padding-top:5px;padding-right:5px;">
                <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:doSubmit();" >
                    确定</a> <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:cancel()">关闭</a>
            </div> 
</body>
</html>
