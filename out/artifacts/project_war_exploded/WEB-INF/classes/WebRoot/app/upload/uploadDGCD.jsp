<!DOCTYPE html>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
	<title>上传附件</title>
	<meta http-equiv="description" content="底稿存档附件上传">
	<link rel="stylesheet" type="text/css" href="iwork_css/stream-v1.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>
	<script type="text/javascript" src="iwork_js/commons.js"   ></script>
	<script type="text/javascript" src="iwork_js/iwork/info/aloneupload.js"   ></script>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
        function closeWin(){
            var api = art.dialog.open.api, W = api.opener;
            api.close();
        }
        function fileCommit(uuid){
            $.ajax({
                type: 'POST',
                url: "zqb_file_uploadCommit.action",
                async: false,
                data: {fileUUID:uuid},
                success: function (data) {
                }
            });
        }
        function fileCommitDGCD(size,uuid,url){
            var XMQY_ID=$("#XMQY_ID").val();
            var DAJYLCB_ID=$("#DAJYLCB_ID").val();
            $.ajax({
                type: 'POST',
                url: "upfileDGCD.action",
                async: false,
                data: {fileUUID:uuid,filesize:size,XMQY_ID:XMQY_ID,DAJYLCB_ID:DAJYLCB_ID,DGCDURL:url},
                success: function (data) {
                }
            });
        }
	</script>
</head>
<body>
<div id="i_select_files"><button id="" class="btn btn-default" type="button">点击选择文件</button></div>
<div id="i_stream_files_queue">
</div>
<div region="south" border="false" style="border-top:1px solid #efefef;height:35px;text-align:right;padding:5px;">
	<a href="#" onclick="javascript:_t.upload();" class="easyui-linkbutton" plain="false" iconCls="icon-add">确定上传</a>
	<a href="#" onclick="closeWin();" class="easyui-linkbutton" plain="false" iconCls="icon-cancel">关闭</a>
</div>
<div id="i_stream_message_container" class="stream-main-upload-box" style="overflow: auto;height:200px; display: none;">
</div>
<div id="result"></div>
	<s:hidden id="XMQY_ID" name="XMQY_ID"></s:hidden>
	<s:hidden id="DAJYLCB_ID" name="DAJYLCB_ID"></s:hidden>
<br>
<script type="text/javascript" src="iwork_js/stream-v1.js"></script>
<script type="text/javascript">
    var config = {
        browseFileId : "i_select_files", /** 选择文件的ID, 默认: i_select_files */
        browseFileBtn : "<div></div>", /** 显示选择文件的样式, 默认: `<div>请选择文件</div>` */
        dragAndDropArea: "i_select_files", /** 拖拽上传区域，Id（字符类型"i_select_files"）或者DOM对象, 默认: `i_select_files` */
        dragAndDropTips: "<span>拖拽文件到这里可以直接上传(单选不支持拖拽)</span>", /** 拖拽提示, 默认: `<span>把文件(文件夹)拖拽到这里</span>` */
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
            "XMQY_ID": '<% out.print(request.getParameter("XMQY_ID"));%>',
            "DAJYLCB_ID": '<% out.print(request.getParameter("DAJYLCB_ID"));%>'
        },
        swfURL : "/swf/FlashUploader.swf", /** SWF文件的位置 */
        tokenURL : "/tk", /** 根据文件名、大小等信息获取Token的URI（用于生成断点续传、跨域的令牌） */
        frmUploadURL : "/fd;", /** Flash上传的URI */
        uploadURL : "/uploadDGCD", /** HTML5上传的URI */
        simLimit: 200, /** 单次最大上传文件个数 */
        onSelect: function(list) {}, /** 选择文件后的响应事件 */
        onMaxSizeExceed: function(size, limited, name) {alert('文件最大可上传100M。')}, /** 文件大小超出的响应事件 */
        onFileCountExceed: function(selected, limit) {alert('单次最大上传文件数为200。')}, /** 文件数量超出的响应事件 */
        onExtNameMismatch: function(name, filters) {alert('文件扩展名不匹配')}, /** 文件的扩展名不匹配的响应事件 */
        onCancel : function(file) {alert('取消上传')}, /** 取消上传文件的响应事件 */
        onComplete: function(file) {
            var resJson = strToJson(file.msg);
            if(resJson.success){
                if(file.size==0){
                    alert(file.name+"文件是空文件,无法上传！");
                }else{
                    fileCommit(resJson.uuid);
                    fileCommitDGCD(file.size,resJson.uuid,resJson.url);
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
        }, /** 所有文件上传完毕的响应事件 */
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
    var _t = new Stream(config);
</script>
</body>