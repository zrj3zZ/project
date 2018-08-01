			var swfu;
			window.onload = function () {
				swfu = new SWFUpload({
					upload_url: "persion_photo_UpFile.action",
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
					button_image_url : "iwork_img/upload/select.gif",
					button_placeholder_id : "spanButtonPlaceholder",
					button_width: 100,
					button_height: 28,
					button_text : '<span class="button"></span>',
					button_text_style : '.button { font-family: 黑体; font-size: 14pt; } .buttonSmall { font-size: 12pt; }',
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