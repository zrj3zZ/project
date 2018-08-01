<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>
    <title>上传附件</title>
	<meta http-equiv="description" content="表单附件上传">
	<link rel="stylesheet" type="text/css" href="iwork_css/stream-v1.css"/>
	
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>
	<script type="text/javascript" src="iwork_js/commons.js"   ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript" src="iwork_js/iwork/info/aloneupload.js"   ></script>
	<script type="text/javascript">
		var api = art.dialog.open.api, W = api.opener;
		function closeWin(){
			api.close(); 
		}
		
		
		function fileCommit(uuid,filesize,filename){
			if($("#parentColId").val()=='fileListId'){
				var instanceid = parent.document.getElementById("instanceid").value;
				$.ajax({
	                type: 'POST',
	                url: "zqb_meeting_doCommitFile.action",
	                async: false,
	                data: {uuid:uuid,instanceid:instanceid},
	                success: function (data) {}
	            });
			}else if($("#parentColId").val()=='kmDocId'){
				var directoryId = $("#directoryId").val();
				$.ajax({
	                type: 'POST',
	                url: "zqb_km_savekmdoc.action",
	                async: false,
	                data: {uuid:uuid,directoryId:directoryId,filesize:filesize,filename:filename},
	                success: function (data) {}
	            });
			}
			$.ajax({
                type: 'POST',
                url: "zqb_file_uploadCommit.action",
                async: false,
                data: {fileUUID:uuid},
                success: function (data) {}
            });
		}
		
		function sendMeetMSG(){
			var instanceid = parent.document.getElementById("instanceid").value;
			$.ajax({
                type: 'POST',
                url: "zqb_meeting_sendmeetmsg.action",
                async: false,
                data: {instanceid:instanceid},
                success: function (data) {}
            });
		}
	</script>
	<style type="text/css">
		.btn-group > button {width: 100px;}
	</style>
  </head>
<body>
	<s:if test="parentColId=='ZDFJ'||parentColId=='newHidId'||parentColId=='wjfile'">
		
		
		<div id="i_select_files"><button id="i_select_files" class="btn btn-default" type="button">点击选择文件</button></div>
		<div id="i_stream_files_queue">
	</s:if>
	<s:else>
		<div id="i_select_files"><button id="i_select_files" class="btn btn-default" type="button">点击选择文件</button></div>
		<div id="i_stream_files_queue">
	</s:else>
	</div>
	<div region="south" border="false" style="border-top:1px solid #efefef;height:35px;text-align:right;padding:5px;">
    	<a href="#" onclick="javascript:_t.upload();" class="easyui-linkbutton" plain="false" iconCls="icon-add">确定上传</a>
  		<a href="#" onclick="closeWin();" class="easyui-linkbutton" plain="false" iconCls="icon-cancel">关闭</a>
    </div>
	<div id="i_stream_message_container" class="stream-main-upload-box" style="overflow: auto;height:200px; display: none;">
	</div>
	<div id="result"></div> 
        <s:hidden id="parentColId" name="parentColId"></s:hidden>
        <s:hidden id="parentDivId" name="parentDivId"></s:hidden>
        <s:hidden id="fileExt" name="fileExt"></s:hidden>
        <s:hidden id="fileDesc" name="fileDesc"></s:hidden>
        <s:hidden id="sizeLimit" name="sizeLimit"></s:hidden>
        <s:hidden id="multi" name="multi"></s:hidden>
        <s:if test="parentColId=='kmDocId'">
        	<input value="<% out.print(request.getParameter("directoryId"));%>" id="directoryId" type="hidden"/>
        </s:if>
<br>
<script type="text/javascript" src="iwork_js/stream-v1.js"></script>
<script type="text/javascript">
/**
 * 配置文件（如果没有默认字样，说明默认值就是注释下的值）
 * 但是，on*（onSelect， onMaxSizeExceed...）等函数的默认行为
 * 是在ID为i_stream_message_container的页面元素中写日志
 */
 	if($("#parentColId").val()=='ZDFJ'||$("#parentColId").val()=='newHidId'||$("#parentColId").val()=='wjfile'){
 		var config = {
 				browseFileId : "i_select_files", /** 选择文件的ID, 默认: i_select_files */
 				browseFileBtn : "<div></div>", /** 显示选择文件的样式, 默认: `<div>请选择文件</div>` */
 				dragAndDropArea: "i_select_files", /** 拖拽上传区域，Id（字符类型"i_select_files"）或者DOM对象, 默认: `i_select_files` */
 				dragAndDropTips: "拖拽文件到这里可以直接上传(单选不支持拖拽)", /** 拖拽提示, 默认: `<span>把文件(文件夹)拖拽到这里</span>` */
 				filesQueueId : "i_stream_files_queue", /** 文件上传容器的ID, 默认: i_stream_files_queue */
 				filesQueueHeight : 300, /** 文件上传容器的高度（px）, 默认: 450 */
 				extFilters : [".txt",".xls",".xlsx",".doc",".docx",".pdf",".ppt",".pptx",".zip",".rar",".bmp",".jpg",".jpeg",".png",".gif",".xml"],
 				messagerId : "i_stream_message_container", /** 消息显示容器的ID, 默认: i_stream_message_container */
 				multipleFiles: false, /** 多个文件一起上传, 默认: false */
 				autoUploading: false, /** 选择文件后是否自动上传, 默认: true */
 				autoRemoveCompleted : true, /** 是否自动删除容器中已上传完毕的文件, 默认: false */
 				maxSize: 104857600, /** 单个文件的最大大小，默认:2G */
 				retryCount : 5, /** HTML5上传失败的重试次数 */
 				postVarsPerFile : { /** 上传文件时传入的参数，默认: {} */
 					param1: "val1",
 					param2: "val2"
 				},
 				swfURL : "/swf/FlashUploader.swf", /** SWF文件的位置 */
 				tokenURL : "/tk", /** 根据文件名、大小等信息获取Token的URI（用于生成断点续传、跨域的令牌） */
 				frmUploadURL : "/fd;", /** Flash上传的URI */
 				uploadURL : "/upload", /** HTML5上传的URI */
 				simLimit: 200, /** 单次最大上传文件个数 */
 				onSelect: function(list) {
 				
 				}, /** 选择文件后的响应事件 */
 				onMaxSizeExceed: function(size, limited, name) {alert('文件最大可上传100M。')}, /** 文件大小超出的响应事件 */
 				onFileCountExceed: function(selected, limit) {alert('最大上传文件数为200。')}, /** 文件数量超出的响应事件 */
 				onExtNameMismatch: function(name, filters) {}, /** 文件的扩展名不匹配的响应事件 */
 				onCancel : function(file) {}, /** 取消上传文件的响应事件 */
 				onComplete: function(file) {
 					var resJson = strToJson(file.msg);
 					if(resJson.success){
 						if(file.size==0){
 							alert(file.name+"文件是空文件,无法上传！");
	 					}else{
	 						var resJson = strToJson(file.msg);
	 						var parentColId = $("#parentColId").val();
	 						var parentFileDivId = $("#parentDivId").val();
	 						fileCommit(resJson.uuid,file.size,file.name);
	 						if($("#submitbtn",window.parent.document)!=null){
	 							//取父页面中现在的uuid值
	 							var oldParentUUIDs = $("#"+parentColId,artDialog.open.origin.document).attr("value");
	 							/*if(oldParentUUIDs==undefined){
									var oldParentUUIDs = art.dialog.data("parentColId");
								}*/
	 							var str=oldParentUUIDs;
	 							if(str==""){
	 								str=resJson.uuid;
	 							}else{
	 								str+=","+resJson.uuid;
	 							}
	 							var newUUIDs=str;
	 							//把本次上传的文件的UUID串更新到父页面中
	 							//$("#"+parentColId,window.parent.document).attr("value",newUUIDs); 原有
	 							$("#"+parentColId,artDialog.open.origin.document).attr("value",newUUIDs);
	 							//校验父页面中UUID字段是否超长，如果超长则把已经上传的附件及页面信息清除，避免产生垃圾数据
	 							var html="";
	 							var zdmc = "";
	 							function buildFileElementHtml(colName,fileDivId,fileName,fileUUID,fileSrc,removeFlag){
	 								zdmc=fileName;
	 								if (fileName.length > 20) {
	 										fileName = fileName.substr(0,15) + '...';
	 								} 
	 								var html = '<div  id="'+fileDivId+'" style="background-color: #F5F5F5;border-bottom: 1px solid #E5E5E5;font: 11px Verdana, Geneva, sans-serif;padding: 5px;width: 200px">';
	 								html 	+= '	<div style="align:right;float: right;">';
	 								if(removeFlag){
	 									html 	+= '		<a href="javascript:uploadifyReomve(\''+colName+'\',\''+fileUUID+'\',\''+fileDivId+'\');"><img src="/iwork_img/del3.gif"/></a>';
	 								}
	 								html 	+= '	</div>';
	 								html 	+= '	<span><a href="uploadifyDownload.action?fileUUID='+fileUUID+'" target="_blank"><img src="/iwork_img/attach.png"/>'+fileName+'</a></span>';
	 								html 	+= '</div>';
	 								return html;
	 							}
	 							html = buildFileElementHtml(parentColId,resJson.uuid,file.name,resJson.uuid,resJson.url,true);
	 							$("#"+parentFileDivId,artDialog.open.origin.document).append(html);
	 							if($("#parentColId").val()!='wjfile'){
	 								$("button",window.parent.document).hide();
	 							}
	 							/*$("#ZDMC",window.parent.document).val(zdmc.substring(0, zdmc.lastIndexOf(".")));*/
	 						}
 						}
	 				}else{
						alert("文件上传失败，下列文件中带有非法字符:"+"\n"+resJson.illegal);
					}
 				}, /** 单个文件上传完毕的响应事件 */
 				onQueueComplete: function() {
 					function closeWin(){
 						var api = art.dialog.open.api;
 						api.close(); 
 					}
 					closeWin();
 					return false;
 				}, /** 所以文件上传完毕的响应事件 */
 				onUploadError: function(status, msg) {
 					var resJson = strToJson(msg);
					var message = resJson.message;
					if(message=='1'){
						alert("文件上传失败，下列文件中带有非法字符:"+"\n"+resJson.illegal);
					}else{
						alert("文件上传失败!");
					}
				} /** 文件上传出错的响应事件 */
 			};
 	}else if($("#parentColId").val()=='YJFS'){
 		var config = {
 				browseFileId : "i_select_files", /** 选择文件的ID, 默认: i_select_files */
 				browseFileBtn : "<div></div>", /** 显示选择文件的样式, 默认: `<div>请选择文件</div>` */
 				dragAndDropArea: "i_select_files", /** 拖拽上传区域，Id（字符类型"i_select_files"）或者DOM对象, 默认: `i_select_files` */
 				dragAndDropTips: "拖拽文件到这里可以直接上传(单选不支持拖拽)", /** 拖拽提示, 默认: `<span>把文件(文件夹)拖拽到这里</span>` */
 				filesQueueId : "i_stream_files_queue", /** 文件上传容器的ID, 默认: i_stream_files_queue */
 				filesQueueHeight : 300, /** 文件上传容器的高度（px）, 默认: 450 */
 				extFilters : [".txt",".xls",".xlsx",".doc",".docx",".pdf",".ppt",".pptx",".zip",".rar",".bmp",".jpg",".jpeg",".png",".gif",".xml"],
 				messagerId : "i_stream_message_container", /** 消息显示容器的ID, 默认: i_stream_message_container */
 				multipleFiles: true, /** 多个文件一起上传, 默认: false */
 				autoUploading: false, /** 选择文件后是否自动上传, 默认: true */
 				autoRemoveCompleted : true, /** 是否自动删除容器中已上传完毕的文件, 默认: false */
 				maxSize: 104857600, /** 单个文件的最大大小，默认:2G */
 				retryCount : 5, /** HTML5上传失败的重试次数 */
 				postVarsPerFile : { /** 上传文件时传入的参数，默认: {} */
 					param1: "val1",
 					param2: "val2"
 				},
 				swfURL : "/swf/FlashUploader.swf", /** SWF文件的位置 */
 				tokenURL : "/tk", /** 根据文件名、大小等信息获取Token的URI（用于生成断点续传、跨域的令牌） */
 				frmUploadURL : "/fd;", /** Flash上传的URI */
 				uploadURL : "/upload", /** HTML5上传的URI */
 				simLimit: 200, /** 单次最大上传文件个数 */
 				onSelect: function(list) {}, /** 选择文件后的响应事件 */
 				onMaxSizeExceed: function(size, limited, name) {alert('文件最大可上传100M。')}, /** 文件大小超出的响应事件 */
 				onFileCountExceed: function(selected, limit) {alert('最大上传文件数为200。')}, /** 文件数量超出的响应事件 */
 				onExtNameMismatch: function(name, filters) {}, /** 文件的扩展名不匹配的响应事件 */
 				onCancel : function(file) {}, /** 取消上传文件的响应事件 */
 				onComplete: function(file) {
 					var resJson = strToJson(file.msg);
 					if(resJson.success){
 						if(file.size==0){
 							alert(file.name+"文件是空文件,无法上传！");
 						}else{
 							var resJson = strToJson(file.msg);
 							var parentColId = $("#parentColId").val();
 							var parentFileDivId = $("#parentDivId").val();
 							fileCommit(resJson.uuid,file.size,file.name);
 							if($("#submitbtn",window.parent.document)!=null){
 								//取父页面中现在的uuid值
 								var obj = window.parent.document.getElementById("ifm").contentWindow;
 								var ifmObj = obj.$("#"+parentColId);
 								var oldParentUUIDs = ifmObj.attr("value");
 								var str=oldParentUUIDs;
 								if(str==""){
 									str=resJson.uuid;
 								}else{
 									str+=","+resJson.uuid;
 								}
 								var newUUIDs=str;
 								//把本次上传的文件的UUID串更新到父页面中
 								ifmObj.attr("value",newUUIDs);
 								//校验父页面中UUID字段是否超长，如果超长则把已经上传的附件及页面信息清除，避免产生垃圾数据
 								var html="";
 								function buildFileElementHtml(colName,fileDivId,fileName,fileUUID,fileSrc,removeFlag){
 									if (fileName.length > 20) {
 											fileName = fileName.substr(0,15) + '...';
 									} 
 									var html = '<div  id="'+fileDivId+'" style="background-color: #F5F5F5;border-bottom: 1px solid #E5E5E5;font: 11px Verdana, Geneva, sans-serif;padding: 5px;width: 200px">';
 									html 	+= '	<div style="align:right;float: right;">';
 									if(removeFlag){
 										html 	+= '		<a href="javascript:uploadifyReomve(\''+colName+'\',\''+fileUUID+'\',\''+fileDivId+'\');"><img src="/iwork_img/del3.gif"/></a>';
 									}
 									html 	+= '	</div>';
 									html 	+= '	<span><a href="uploadifyDownload.action?fileUUID='+fileUUID+'" target="_blank"><img src="/iwork_img/attach.png"/>'+fileName+'</a></span>';
 									html 	+= '</div>';
 									return html;
 								}
 								html = buildFileElementHtml(parentColId,resJson.uuid,file.name,resJson.uuid,resJson.url,true);
 								var obj = window.parent.document.getElementById("ifm").contentWindow;
								var ifmObj=obj.$("#"+parentFileDivId);
								ifmObj.append(html);
 							}
 						}
 					}else{
 						alert("文件上传失败，下列文件中带有非法字符:"+"\n"+resJson.illegal);
 					}
 				}, /** 单个文件上传完毕的响应事件 */
 				onQueueComplete: function() {
 					function closeWin(){
 						var api = art.dialog.open.api;
 						api.close(); 
 					}
 					closeWin();
 					return false;
 				}, /** 所以文件上传完毕的响应事件 */
 				onUploadError: function(status, msg) {
 					var resJson = strToJson(msg);
 					var message = resJson.message;
 					if(message=='1'){
 						alert("文件上传失败，下列文件中带有非法字符:"+"\n"+resJson.illegal);
 					}else{
 						alert("文件上传失败!");
 					}
 				} /** 文件上传出错的响应事件 */
 			};
 	 	}else{
 		var config = {
			browseFileId : "i_select_files", /** 选择文件的ID, 默认: i_select_files */
			browseFileBtn : "<div></div>", /** 显示选择文件的样式, 默认: `<div>请选择文件</div>` */
			dragAndDropArea: "i_select_files", /** 拖拽上传区域，Id（字符类型"i_select_files"）或者DOM对象, 默认: `i_select_files` */
			dragAndDropTips: "拖拽文件到这里可以直接上传(单选不支持拖拽)", /** 拖拽提示, 默认: `<span>把文件(文件夹)拖拽到这里</span>` */
			filesQueueId : "i_stream_files_queue", /** 文件上传容器的ID, 默认: i_stream_files_queue */
			filesQueueHeight : 300, /** 文件上传容器的高度（px）, 默认: 450 */
			extFilters : [".txt",".xls",".xlsx",".doc",".docx",".pdf",".ppt",".pptx",".zip",".rar",".bmp",".jpg",".jpeg",".png",".gif",".xml"],
			messagerId : "i_stream_message_container", /** 消息显示容器的ID, 默认: i_stream_message_container */
			multipleFiles: true, /** 多个文件一起上传, 默认: false */
			autoUploading: false, /** 选择文件后是否自动上传, 默认: true */
			autoRemoveCompleted : true, /** 是否自动删除容器中已上传完毕的文件, 默认: false */
			maxSize: 104857600, /** 单个文件的最大大小，默认:2G */
			retryCount : 5, /** HTML5上传失败的重试次数 */
			postVarsPerFile : { /** 上传文件时传入的参数，默认: {} */
				param1: "val1",
				param2: "val2"
			},
			swfURL : "/swf/FlashUploader.swf", /** SWF文件的位置 */
			tokenURL : "/tk", /** 根据文件名、大小等信息获取Token的URI（用于生成断点续传、跨域的令牌） */
			frmUploadURL : "/fd;", /** Flash上传的URI */
			uploadURL : "/upload", /** HTML5上传的URI */
			simLimit: 200, /** 单次最大上传文件个数 */
			onSelect: function(list) {}, /** 选择文件后的响应事件 */
			onMaxSizeExceed: function(size, limited, name) {alert('文件最大可上传100M。')}, /** 文件大小超出的响应事件 */
			onFileCountExceed: function(selected, limit) {alert('最大上传文件数为200。')}, /** 文件数量超出的响应事件 */
			onExtNameMismatch: function(name, filters) {}, /** 文件的扩展名不匹配的响应事件 */
			onCancel : function(file) {}, /** 取消上传文件的响应事件 */
			onComplete: function(file) {
				
				var resJson = strToJson(file.msg);
				if(resJson.success){
					if(file.size==0){
						alert(file.name+"文件是空文件,无法上传！");
					}else{
						var resJson = strToJson(file.msg);
						var parentColId = $("#parentColId").val();
						var parentFileDivId = $("#parentDivId").val();
						
						fileCommit(resJson.uuid,file.size,file.name);

						if($("#submitbtn",window.parent.document)!=null){
							//取父页面中现在的uuid值
							var oldParentUUIDs = $("#"+parentColId,artDialog.open.origin.document).attr("value");
							/*if(oldParentUUIDs==undefined){
							var oldParentUUIDs = art.dialog.data("parentColId");
							}*/
							//alert("已有文件："+oldParentUUIDs)
							var str=oldParentUUIDs;
							if(str==""||str==undefined){
								str=resJson.uuid;
							}else{
								str+=","+resJson.uuid;
							}
							var newUUIDs=str;
							//把本次上传的文件的UUID串更新到父页面中
							//$("#"+parentColId,window.parent.document).attr("value",newUUIDs); 原有
							$("#"+parentColId,artDialog.open.origin.document).attr("value",newUUIDs);
							//artDialog.data("parentColId",newUUIDs);
							//校验父页面中UUID字段是否超长，如果超长则把已经上传的附件及页面信息清除，避免产生垃圾数据
							var html="";
							function alonebuildFileElementHtml(colName,fileDivId,fileName,fileUUID,fileSrc,removeFlag){
								var flag = false;
								var pos = fileName.lastIndexOf('.');
								if(pos!=-1){
									var extName=fileName.substring(pos + 1,fileName.length);
									if(extName=="doc"||extName=="docx"){
										flag=true;
									}
								}
								/* if (fileName.length > 20) {
										fileName = fileName.substr(0,15) + '...';
								} */ 
								var html = '';//'<div  id="'+fileDivId+'" style="background-color: #F5F5F5;border-bottom: 1px solid #E5E5E5;font: 11px Verdana, Geneva, sans-serif;padding: 5px;">';
								/* if(removeFlag){
									html 	+= '		<table id="aloneTable"><tr id="'+fileUUID+'" align="center"><td style="white-space:nowrap; padding: 5px;"><a href="uploadifyDownload.action?fileUUID='+fileUUID+'" target="_blank"><img src="/iwork_img/attach.png"/>'+fileName+'</a></td><td style="white-space:nowrap; padding-top: 5px;"><a style="text-decoration:none;" href="javascript:updateUploadify(\''+fileUUID+'\',\''+fileName+'\');">【修改】</a>&nbsp;&nbsp;&nbsp;&nbsp;<a style="text-decoration:none;" href="javascript:aloneUploadifyReomve(\''+colName+'\',\''+fileUUID+'\',\''+fileDivId+'\');">【删除】</a></td></tr></table>';
								}
								html 	+= '</div>'; */
								
							
								html+='<tr id="'+fileDivId+'">';
								html+=	'<td width="230px;" align="left" style="border-right:1px solid #eee;border-bottom:1px solid #eee;">';
								html+=		'<a target="_blank" href="uploadifyDownload.action?fileUUID='+fileUUID+'">';
								html+=		'<img src="/iwork_img/attach.png" style="margin:3px">';
								html+=		''+fileName+'';
								html+=		'</a>';
								html+=	'</td>';
								html+=	'<td width="180px;" align="center" style="border-right:1px solid #eee;border-bottom:1px solid #eee;">';
								if(removeFlag){
									if(flag){
										html+='<a style="text-decoration:none;" href="javascript:updateUploadify(\''+fileUUID+'\',\''+fileName+'\');">【修改】</a>';
									}
								html+=		'<a href="javascript:aloneUploadifyReomve(\''+colName+'\',\''+fileUUID+'\',\''+fileDivId+'\');">【删除】</a>';
								}
								html+=	'</td>';
								html+=	'<td width="135px;" align="center" style="border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;"></td>';
								html+=	'<td width="90px;" align="center" style="border-bottom:1px solid #eee;color:#004080;"></td>';
								html+='</tr>';
								var rownum = oldParentUUIDs.split(",").length+2;
								$("#rowspantd"+parentColId,window.parent.document).attr("rowspan",rownum);
								return html;
							}
							function sxbcfilebuildFileElementHtml(colName,fileDivId,fileName,fileUUID,fileSrc,removeFlag){
								if (fileName.length > 20) {
										fileName = fileName.substr(0,15) + '...';
								} 
								var html = '';//'<div  id="'+fileDivId+'" style="background-color: #F5F5F5;border-bottom: 1px solid #E5E5E5;font: 11px Verdana, Geneva, sans-serif;padding: 5px;">';
								/* if(removeFlag){
									html 	+= '		<table id="aloneTable"><tr id="'+fileUUID+'" align="center"><td style="white-space:nowrap; padding: 5px;"><a href="uploadifyDownload.action?fileUUID='+fileUUID+'" target="_blank"><img src="/iwork_img/attach.png"/>'+fileName+'</a></td><td style="white-space:nowrap; padding-top: 5px;"><a style="text-decoration:none;" href="javascript:updateUploadify(\''+fileUUID+'\',\''+fileName+'\');">【修改】</a>&nbsp;&nbsp;&nbsp;&nbsp;<a style="text-decoration:none;" href="javascript:aloneUploadifyReomve(\''+colName+'\',\''+fileUUID+'\',\''+fileDivId+'\');">【删除】</a></td></tr></table>';
								}
								html 	+= '</div>'; */
								
							
								html+='<tr id="'+fileDivId+'">';
								html+=	'<td width="230px;" align="left" style="border-right:1px solid #eee;border-bottom:1px solid #eee;">';
								html+=		'<a target="_blank" href="uploadifyDownload.action?fileUUID='+fileUUID+'">';
								html+=		'<img src="/iwork_img/attach.png" style="margin:3px">';
								html+=		''+fileName+'';
								html+=		'</a>';
								html+=	'</td>';
								html+=	'<td width="180px;" align="center" style="border-right:1px solid #eee;border-bottom:1px solid #eee;">';
								if(removeFlag){
								html+=		'<a href="javascript:aloneUploadifyReomve(\''+colName+'\',\''+fileUUID+'\',\''+fileDivId+'\');">【删除】</a>';
								}
								html+=	'</td>';
								html+=	'<td width="135px;" align="center" style="border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;"></td>';
								html+=	'<td width="90px;" align="center" style="border-bottom:1px solid #eee;color:#004080;"></td>';
								html+='</tr>';
								var rownum = oldParentUUIDs.split(",").length+2;
								$("#rowspantdsxbc",window.parent.document).attr("rowspan",rownum);
								return html;
							}
							function buildFileElementHtml(colName,fileDivId,fileName,fileUUID,fileSrc,removeFlag){
								if (fileName.length > 20) {
										fileName = fileName.substr(0,15) + '...';
								} 
								var html = '<div  id="'+fileDivId+'" style="background-color: #F5F5F5;border-bottom: 1px solid #E5E5E5;font: 11px Verdana, Geneva, sans-serif;padding: 5px;width: 200px">';
								html 	+= '	<div style="align:right;float: right;">';
								if(removeFlag){
									html 	+= '		<a href="javascript:uploadifyReomve(\''+colName+'\',\''+fileUUID+'\',\''+fileDivId+'\');"><img src="/iwork_img/del3.gif"/></a>';
								}
								html 	+= '	</div>';
								html 	+= '	<span><a href="uploadifyDownload.action?fileUUID='+fileUUID+'" target="_blank"><img src="/iwork_img/attach.png"/>'+fileName+'</a></span>';
								html 	+= '</div>';
								return html;
							}
							if(parentColId=="NOTICEFILE"||parentColId=="SXFJ"||parentColId=="COMPANYNAME"){
								html=alonebuildFileElementHtml(parentColId,resJson.uuid,file.name,resJson.uuid,resJson.url,true);
							}else if(parentColId=="SXBCFILE"){
								html=sxbcfilebuildFileElementHtml(parentColId,resJson.uuid,file.name,resJson.uuid,resJson.url,true);
							}else{
								html = buildFileElementHtml(parentColId,resJson.uuid,file.name,resJson.uuid,resJson.url,true);
							}
							$("#"+parentFileDivId,artDialog.open.origin.document).append(html);
							//$("#"+parentFileDivId,window.parent.document).append(html);
						}
					}
				}else{
					alert("文件上传失败，下列文件中带有非法字符:"+"\n"+resJson.illegal);
				}
			}, /** 单个文件上传完毕的响应事件 */
			onQueueComplete: function() {
				artDialog.open.origin.saveForm();
				function closeWin(){
					var api = art.dialog.open.api;
					api.close(); 
				}
				closeWin();
				return false;
			}, /** 所以文件上传完毕的响应事件 */
			onUploadError: function(status, msg) {
				var resJson = strToJson(msg);
				var message = resJson.message;
				if(message=='1'){
					alert("文件上传失败，下列文件中带有非法字符:"+"\n"+resJson.illegal);
				}else{
					alert("文件上传失败!");
				}
			} /** 文件上传出错的响应事件 */
		};
 	}
	var _t = new Stream(config);
</script>
</body>