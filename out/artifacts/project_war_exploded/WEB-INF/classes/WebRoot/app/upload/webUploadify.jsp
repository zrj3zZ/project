<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>使用webuploader上传</title>
 <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
<!-- 1.引入文件 -->

<%-- <link rel="stylesheet" type="text/css" href="iwork_js/webuploader/webuploader.css"  />
	<link rel="stylesheet" type="text/css" href="assets/css/bootstrap.min.css">
 <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="iwork_js/webuploader/webuploader.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript" src="iwork_js/iwork/info/aloneupload.js"   ></script> --%>

<link rel="stylesheet" type="text/css" href="assets/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="iwork_js/webuploader/webuploader.css"  />
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="iwork_js/webuploader/webuploader.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript" src="iwork_js/iwork/info/aloneupload.js"   ></script> 
<script type="text/javascript">
var api = art.dialog.open.api, W = api.opener;


function fileCommit(uuid,filesize,filename){
	if($("#parentColId").val()=='fileListId'){
		var instanceid = parent.document.getElementById("instanceid").value;
		aert(instanceid);
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
.uploader-list{
	width: 100%;
	height: 70%;
	overflow:auto;
	font-size: 12px
}
#picker{
 float: left;
}
</style>
  </head>
  <body>
  <div id="i_select_draganddroparea" style="display:none;"></div>
		<div id="dndArea" style="height:70px;width:100%;border:2px dashed black;border-radius:6px;margin-bottom:10px;">
			<div id="picker" style="margin-top:15px;display:block;margin-left:41%;margin-right:41%;">选择上传文件</div>
			<span style="text-align:center;display:block;margin-bottom:10px;height:15px;"></br></span>
		</div>
		
 			<div id="thelist" class="uploader-list"></div>
		
		
	<div region="south" border="false" style="border-top:1px solid #efefef;height:35px;text-align:right;padding:5px;">
    	<!-- <div id="picker">选择文件</div> -->
    </div>
		
	
	 <s:hidden id="parentColId" name="parentColId"></s:hidden>
        <s:hidden id="parentDivId" name="parentDivId"></s:hidden>
        <s:hidden id="fileExt" name="fileExt"></s:hidden>
        <s:hidden id="fileDesc" name="fileDesc"></s:hidden>
        <s:hidden id="sizeLimit" name="sizeLimit"></s:hidden>
        <s:hidden id="multi" name="multi"></s:hidden>
        <s:if test="parentColId=='kmDocId'">
        	<input value="<% out.print(request.getParameter("directoryId"));%>" id="directoryId" type="hidden"/>
        </s:if>
      <input type="hidden" value="0" id="flag">
        
	<script type="text/javascript">
	function uuid() {
	    var s = [];
	    var hexDigits = "0123456789abcdef";
	    for (var i = 0; i < 36; i++) {
	        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
	    }
	    s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
	    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
	    s[8] = s[13] = s[18] = s[23] = "-";
	 
	    var uuid = s.join("");
	    return uuid;
	}
		var fileMd5;
		var $list=$("#thelist");
		var state = 'pending';//初始按钮状态
		var $btn=$("#btn");
		//监听分块上传过程中的三个时间点  
		WebUploader.Uploader.register({
			"before-send-file" : "beforeSendFile",
			"before-send" : "beforeSend",
			"after-send-file" : "afterSendFile"
		}, {
			//时间点1：所有分块进行上传之前调用此函数  
			beforeSendFile : function(file) {
				var deferred = WebUploader.Deferred();
				//1、计算文件的唯一标记，用于断点续传  
				(new WebUploader.Uploader()).md5File(file, 0, 10 * 1024 * 1024)
						.progress(function(percentage) {
							$('#' + file.id).find("p.state").text("正在读取文件信息...");
						}).then(function(val) {
							fileMd5 = uuid();
							$('#' + file.id).find("p.state").text("成功获取文件信息...");
							//获取文件信息后进入下一步  
							
							$.ajax({
								type : "POST",
								url : "webUploadyz.action",
								data : {
									filename :encodeURI(file.name)
								},
								dataType : "json",
								success : function(data) {
									if(data.success=="success"){
										deferred.resolve();
									}else{
										alert(data.illegalChar);
										 uploader.cancelFile( file );
										 deferred.resolve();
									} 
								}
							}); 
						});
				return deferred.promise();
			},
			//时间点2：如果有分块上传，则每个分块上传之前调用此函数  
			beforeSend : function(block) {
				var deferred = WebUploader.Deferred();

				$.ajax({
					type : "POST",
					url : "/servlet/WebFileUploadServlet?action=checkChunk",
					data : {
						//文件唯一标记  
						fileMd5 : fileMd5,
						//当前分块下标  
						chunk : block.chunk,
						//当前分块大小  
						chunkSize : block.end - block.start
					},
					dataType : "json",
					success : function(response) {
						if (response.ifExist) {
							//分块存在，跳过  
							deferred.reject();
						} else {
							//分块不存在或不完整，重新发送该分块内容  
							deferred.resolve();
						}
					}
				});

				this.owner.options.formData.fileMd5 = fileMd5;
				deferred.resolve();
				return deferred.promise();
			},
			//时间点3：所有分块上传成功后调用此函数  
			afterSendFile : function(file) {
				//如果分块上传成功，则通知后台合并分块  
				$.ajax({
					type : "POST",
					url : "/servlet/WebFileUploadServlet?action=mergeChunks",
					data : {
						fileMd5 : fileMd5,
						exts : file.ext,
						filenames : file.name
					},
					dataType: 'json',
					 beforeSend: function(){
						 $('#' + file.id).find('p.state').text('文件处理中，请稍后...');
						 $('#' + file.id).find('p.state').css('color','red');
					  },
					complete: function(){
						 $('#' + file.id).find('p.state').text('文件处理完成');
						 $('#' + file.id).find('p.state').css('color','black');
					},
					success : function(data) {
						if(data.success=='true'){
							if($("#parentColId").val()=='ZDFJ'||$("#parentColId").val()=='newHidId'||$("#parentColId").val()=='wjfile'){
								if(file.size==0){
		 							alert(file.name+"文件是空文件,无法上传！");
			 					}else{
			 						fileCommit(data.uuid,file.size,file.fileName);
									var parentColId = $("#parentColId").val();
									var parentFileDivId = $("#parentDivId").val();
									if($("#submitbtn",window.parent.document)!=null){
			 							//取父页面中现在的uuid值
			 							var oldParentUUIDs = $("#"+parentColId,artDialog.open.origin.document).attr("value");
			 							/*if(oldParentUUIDs==undefined){
											var oldParentUUIDs = art.dialog.data("parentColId");
										}*/
			 							var str=oldParentUUIDs;
			 							if(str==""){
			 								str=data.uuid;
			 							}else{
			 								str+=","+data.uuid;
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
			 							html = buildFileElementHtml(parentColId,data.uuid,file.name,data.uuid,data.url,true);
			 							$("#"+parentFileDivId,artDialog.open.origin.document).append(html);
			 							if($("#parentColId").val()!='wjfile'){
			 								$("button",window.parent.document).hide();
			 							}
			 							$("#ZDMC",window.parent.document).val(zdmc.substring(0, zdmc.lastIndexOf(".")));
			 						}
			 					}
							}else if($("#parentColId").val()=='YJFS'){
								if(file.size==0){
		 							alert(file.name+"文件是空文件,无法上传！");
		 						}else{
		 							fileCommit(data.uuid,file.size,file.fileName);
									var parentColId = $("#parentColId").val();
									var parentFileDivId = $("#parentDivId").val();
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
		 								html = buildFileElementHtml(parentColId,data.uuid,file.name,data.uuid,data.url,true);
		 								var obj = window.parent.document.getElementById("ifm").contentWindow;
										var ifmObj=obj.$("#"+parentFileDivId);
										ifmObj.append(html);
		 							}
		 						}
							}else{
								fileCommit(data.uuid,file.size,file.fileName);
								var parentColId = $("#parentColId").val();
								var parentFileDivId = $("#parentDivId").val();
								if($("#submitbtn",window.parent.document)!=null){
									//取父页面中现在的uuid值
									var oldParentUUIDs = $("#"+parentColId,artDialog.open.origin.document).attr("value");
									/*if(oldParentUUIDs==undefined){
									var oldParentUUIDs = art.dialog.data("parentColId");
									}*/
									//alert("已有文件："+oldParentUUIDs)
									var str=oldParentUUIDs;
									if(str==""||str==undefined){
										str=data.uuid;
									}else{
										str+=","+data.uuid;
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
										html=alonebuildFileElementHtml(parentColId,data.uuid,file.name,data.uuid,data.url,true);
									}else if(parentColId=="SXBCFILE"){
										html=sxbcfilebuildFileElementHtml(parentColId,data.uuid,file.name,data.uuid,data.url,true);
									}else{
										html = buildFileElementHtml(parentColId,data.uuid,file.name,data.uuid,data.url,true);
									}
									$("#"+parentFileDivId,artDialog.open.origin.document).append(html);
									//$("#"+parentFileDivId,window.parent.document).append(html);
								}
							}
							
						}else if(data.success=='fname'){
							$("#flag").val("1");
							//alert(data.illegalChar);
							$('#' + file.id).find('p.state').text('上传出错:'+data.illegalChar);
							$('#' + file.id).find('p.state').css('color','red');
							$('#' + file.id).find('p.info').css('color','red');
							return false;
						}else if(data.success=='wjyc'){
							$("#flag").val("1");
						//	alert(data.illegalChar);
							$('#' + file.id).find('p.state').text('上传出错:'+data.illegalChar);
							$('#' + file.id).find('p.state').css('color','red');
							$('#' + file.id).find('p.info').css('color','red');
						}
						//
					
					}
				});
			}
		});
		var allMaxSize = 10;
		var uploader = WebUploader.create({
				//	 auto:false, //是否自动上传
					//拖拽
					 dnd:"#dndArea",
					// swf文件路径  
					 swf: '<%=basePath %>iwork_js/webuploader/Uploader.swf', 
					// 文件接收服务端。  
					server : '/servlet/WebuploaderServlet',
					fileSingleSizeLimit: 1073741824,//限制大小GB，单文件
					// 选择文件的按钮。可选。  
					// 内部根据当前运行是创建，可能是input元素，也可能是flash.  
					pick : {
						id : '#picker',//这个id是你要点击上传文件的id
						multiple : true
					},
					// 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！  
					resize : true,
					auto : true,
					//开启分片上传  
					chunked : true,
					chunkSize : 10 * 1024 * 1024,
					chunkRetry : 5,   //如果某个分片由于网络问题出错，允许自动重传多少次
					threads : 1,  // [默认值：3]上传并发数。允许同时最大上传进程数。
					accept : {
						extensions : "txt,jpg,jpeg,bmp,png,zip,rar,war,pdf,doc,docx,ppt,pptx,xls,xlsx,xml,gif,7z",
						mimeTypes : '.txt,.jpg,.jpeg,.bmp,.png,.zip,.rar,.war,.pdf,.doc,.docx,.ppt,.pptx,.xls,.xlsx,.xml,.gif,.7z'
					},
					//fileNumLimit:50,//验证文件总数量, 超出则不允许加入队列
			          // runtimeOrder: 'flash',  
			          // 禁掉全局的拖拽功能。这样不会出现图片拖进页面的时候，把图片打开。  
			        disableGlobalDnd: true
				});
		
		uploader.on("error",function (type){ 
			
	         if(type == "F_DUPLICATE"){
	              alert("请不要重复选择文件！");
	         }else if(type == "Q_EXCEED_SIZE_LIMIT"){
	              alert("所选附件总大小不可超过" + allMaxSize + "M");
	         }else if(type == "F_EXCEED_SIZE"){
	        	 alert("所选单个文件不小不可超过1GB")
	         }

	     });
		//所有文件上传完成事件
		uploader.on('uploadFinished', function () {
			var bs=$("#flag").val();
		/* 	if(bs!='1'){
				 $btn.text('开始上传'); 
				function closeWin(){
					var api = art.dialog.open.api;
					api.close(); 
				}
				setTimeout(function () { 
					closeWin();
			    }, 2000);
			} */
			
		//	
			return false;
		});
		 uploader.on('beforeFileQueued', function (file) {
			
		 }); 
		// 当有文件被添加进队列的时候  
		uploader.on('fileQueued', function(file) {
			$list.append(
					'<div id="' + file.id + '" class="item">'
							+ '<p class="info">' + file.name + '</p>'
							+ '<p class="state">等待上传...</p></div>');
		});
		
		/* // 文件上传过程中创建进度条实时显示。  
		uploader.on('uploadProgress', function(file, percentage) {
			$('#' + file.id).find('p.state').text(
					'上传中 ' + Math.round(percentage * 100) + '%');
		}); */
		uploader.on( 'uploadProgress', function( file, percentage ) {
	        var $li = $( '#'+file.id ),
	            $percent = $li.find('.progress .progress-bar');

	        // 避免重复创建
	        if ( !$percent.length ) {
	            $percent = $('<div class="progress progress-striped active">' +
	              '<div class="progress-bar" role="progressbar" style="width: 0%">' +
	              '</div>' +
	            '</div>').appendTo( $li ).find('.progress-bar');
	        }

	        $li.find('p.state').text('上传中' + Math.round(percentage * 100) + '%');

	        $percent.css( 'width', percentage * 100 + '%' );
	    });
		uploader.on('uploadSuccess', function(file,data) {
			//$('#' + file.id).find('p.state').text('已上传');
		});

		uploader.on('uploadError', function(file) {
			$('#' + file.id).find('p.state').text('上传出错');
		});

		uploader.on('uploadComplete', function(file) {
			$('#' + file.id).find('.progress').fadeOut();
		});
		
		
		uploader.on('all', function(type) {
			if (type === 'startUpload') {
				state = 'uploading';
			} else if (type === 'stopUpload') {
				state = 'paused';
			} else if (type === 'uploadFinished') {
				state = 'done';
			}

			if (state === 'uploading') {
				$btn.text('暂停上传');
			} else {
				$btn.text('开始上传');
			}
		});
		
		$btn.on('click', function(){  
	        if (state === 'uploading'){  
	            uploader.stop(true);  
	        } else {  
	        	uploader.upload();
	        }
		});
	</script>
</body>
</html>