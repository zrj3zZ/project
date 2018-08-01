<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort() + path + "/";

	StringBuffer uploadUrl = new StringBuffer("http://");
	uploadUrl.append(request.getHeader("Host"));
	uploadUrl.append(request.getContextPath());
	uploadUrl.append("/FileUploadServlet.htm");
%>
<html>
	<head>
		<base href="<%=basePath%>"> 
		<title>SWFUpload Demos</title>
		<link href="iwork_css/upload/default.css" rel="stylesheet" type="text/css" />
        <link href="iwork_css/base-090628.css" rel="stylesheet" type="text/css" />
		<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
		<link href="iwork_css/reset.css" rel="stylesheet" type="text/css"/>

		<link rel="stylesheet" type="text/css" href="ext/resources/css/ext-all.css" />
 		<script type="text/javascript" src="ext/adapter/ext/ext-base.js"></script>
    	<script type="text/javascript" src="ext/ext-all.js"></script>
  		<script type="text/javascript" src="ext/ext-lang-zh_CN.js"></script>
		<script language="javascript" src="iwork_js/commons.js"></script>
		<script type="text/javascript" src="iwork_js/upload/swfupload.js"></script>
		<script type="text/javascript" src="iwork_js/upload/swfupload.queue.js"></script>
		<script type="text/javascript" src="iwork_js/upload/handlers.js"></script>

		<script type="text/javascript">
			var swfu;
			window.onload = function () {
				swfu = new SWFUpload({
					upload_url: "<%=uploadUrl.toString()%>",
					post_params: {"name" : "huliang"},
					
					// File Upload Settings
					file_size_limit : "10 MB",	// 1000MB
					file_types : "*.*",
					file_types_description : "所有文件",
					file_upload_limit : "0",
									
					file_queue_error_handler : fileQueueError,
					file_dialog_complete_handler : fileDialogComplete,//选择好文件后提交
					file_queued_handler : fileQueued,
					upload_progress_handler : uploadProgress,
					upload_error_handler : uploadError,
					upload_success_handler : uploadSuccess,
					upload_complete_handler : uploadComplete,
	
					// Button Settings
					button_image_url : "iwork_img/upload/SmallSpyGlassWithTransperancy_17x18.png",
					button_placeholder_id : "spanButtonPlaceholder",
					button_width: 180,
					button_height: 18,
					button_text : '<span class="button">选择文件</span>',
					button_text_style : '.button { font-family: 黑体; font-size: 12pt; } .buttonSmall { font-size: 12pt; }',
					button_text_top_padding: 0,
					button_text_left_padding: 18,
					button_window_mode: SWFUpload.WINDOW_MODE.TRANSPARENT,
					button_cursor: SWFUpload.CURSOR.HAND,
					
					// Flash Settings
					flash_url : "iwork_js/upload/swfupload.swf",
	
					custom_settings : {
						upload_target : "divFileProgressContainer"
					},
					// Debug Settings
					debug: false  //是否显示调试窗口
				});
			};
			function startUploadFile(){
				swfu.startUpload();
			}
			var win = new Ext.Window({
				title : 'SwfUpload',
				closeAction : 'hide',
				width : 750,
				height : 360,
				resizable : false,
				modal : true,
				html : '<iframe src="index.jsp" width="100%" height="100%"></iframe>'
			});
			function showExtShow(){
				win.show();
			}
		</script>
	</head>
	<body style="padding: 2px;">
	<form>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	    <tr>
      <td align="right" width="1%" style='padding-left:10px;padding-top:5px;border-bottom:2px #D2D2D2 solid;'><img src='../iwork_img/menulogo_jgtongxunl.gif' height=25 border=0><span style='font-size:18px;font-family:黑体'>知识上传</span><span style='color:gray;font-size:10px;font-family:Arial;padding-left:4px;'>（KM Upload）</span>	</td>      
    </tr>
	
    <tr>
  		<tr>
    		<td style="background-color:#F8EDBC;padding-left:20px;padding-top:5px;padding-bottom:3px;"><span id="spanButtonPlaceholder"></span><input id="btnCancel" type="button" value="取消所有上传"
						onclick="cancelUpload();"  class='btn-bar' /></td>
  		</tr>
		  <tr>
		    <td>注意事项：每次上传不应超过600M和300个文件，每个文件不应大于60M</td>
		  </tr>
		  <tr>
		    <td>&nbsp;</td>
		  </tr>
		
		  <tr>
		    <td>
			<div id="divFileProgressContainer"></div>
			<div id="thumbnails">
				<table id="infoTable" border="0" width="530" style="display: inline; border: solid 1px #7FAAFF; background-color: #C5D9FF; padding: 2px;margin-top:8px;">
				</table>
			</div>
			</td>
		  </tr>
		    <tr>
		    <td><input id="btnUpload" type="button" value="上  传"
						onclick="startUploadFile();" class="btn3_mouseout" onMouseUp="this.className='btn3_mouseup'"
						onmousedown="this.className='btn3_mousedown'"
						onMouseOver="this.className='btn3_mouseover'"
						onmouseout="this.className='btn3_mouseout'"/>&nbsp;&nbsp; 关闭窗口</td>
		  </tr>
		</table>
		
		</form>
	</body>
</html>