<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>上传附件</title>
	<meta http-equiv="description" content="邮件附件上传">
	<link href="iwork_css/upload/uploadify.css" type="text/css" rel="stylesheet" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/commons.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>
	<script type="text/javascript" src="iwork_js/upload/swfobject.js"></script>
	<script type="text/javascript" src="iwork_js/upload/jquery.uploadify.v2.1.4.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
	var api = art.dialog.open.api, W = api.opener;
	$(document).ready(function() { 
		var parentColId = "parentHidId";
		var parentFileDivId = "parentHidDivId";
		var  sizeLimit = 10240000;
		var  multi = 'true';
		var  fileExt = null;
		var  fileDesc=null;
		var fileArray=new Array();//用于存放已经上传成功的附件uuid
		var tempFileArray=new Array();//用于onComplete事件中临时存放uuid,如果tempFileArray中的uuid已经加入到fileArray，tempFileArray清空
		
	  $('#uploadify').uploadify({
	    'uploader'  : 'iwork_js/upload/uploadify.swf',
	    'script'    : 'iwork_email_doUpload.action;jsessionid=<%=session.getId()%>',
	    'cancelImg' : 'iwork_img/del3.gif',
	    'folder'    : 'uploads',
	    'fileDataName'   : 'uploadify',
	    'removeCompleted' : false,
		'sizeLimit' :sizeLimit,
		'queueID':'fileQueue',
		'simUploadLimit':10,
	    'multi'     : multi,
	    'fileExt': '',
	    'buttonImg':'iwork_img/upload/select.gif',
	    'fileDesc' : fileDesc,
	    'auto'      : false,
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
	  					  try{
	  					  		var origin = artDialog.open.origin; 
	    						//取父页面中现在的uuid值
	    						var oldParentUUIDs =origin.document.getElementById(parentColId).value;
	    						//把本次上传的文件的UUID拼成串
	    						var newUUIDs=fileArrToStr(tempFileArray);
	    						
	    						//把本次上传的文件的UUID串更新到父页面中 
	    						var inputV = origin.document.getElementById(parentColId);
	    						inputV.value = insertUUID(oldParentUUIDs,newUUIDs);
	    							var htmlstr = "";
									//如果字段校验成功，则更新父页面中的文件列表
									for(var i=0;i<tempFileArray.length;i++){
											var obj = tempFileArray[i];
											var html = buildFileElementHtml(parentColId,obj.uuid,obj.fileName,obj.uuid,obj.fileUrl,true);
											htmlstr+=html;
									}
								var inputDiv = origin.document.getElementById(parentFileDivId);
								$(inputDiv).append(htmlstr);
								fileArray = fileArray.concat(tempFileArray);
								tempFileArray=new Array();
								//关闭当前窗口
								closeWin();
							}catch(e){}
							
							return;
	    				},	
		'onComplete'  : function(event, ID, fileObj, response, data) {
							if(response!=null&&response.length>0){
								//把后台传回的值转换成json格式
								var resJson = strToJson(response);
								var flag = resJson.flag;
								if(flag==true||flag=='true'){
									//如果上传成功，则把生成该附件对象，存放到tempFileArray中
									var file = new uploadifyFile(ID,fileObj.name,resJson.url,resJson.uuid);
									tempFileArray.push(file);
								}
							}
					    }						
	  });
	});
	//定义文件对象结构
	function uploadifyFile(id,fileName,fileUrl,uuid){
		this.id=id;
		this.uuid=uuid;
		this.fileName=fileName;
		this.fileUrl=fileUrl;
		return false;
	}
	//把文件对象数组转换成UUID字符串
	function fileArrToStr(arr){
		var str="";
		if(arr!=null&&arr.length>0){
			for(var i=0;i<arr.length;i++){
				var file = arr[i];
				if(str==""){
					str=file.uuid;
				}else{
					str+=","+file.uuid;
				}
			}
		}
		return str;
	}
	//往原有的UUID串中增加uuid串
	function insertUUID(srcUUIDS,newUUIDS){
		if(srcUUIDS==""){
			srcUUIDS=newUUIDS;
		}else{
			srcUUIDS+=","+newUUIDS;
		}
		return srcUUIDS;
	}
	//每个附件在页面上的显示样式
	function buildFileElementHtml(colName,fileDivId,fileName,fileUUID,fileSrc,removeFlag){
			if (fileName.length > 20) {
					fileName = fileName.substr(0,15) + '...';
			} 
			var html = '<span  id="'+fileDivId+'" class="attachItem">';
			
			html 	+= '	<span><a href="iwork_email_download.action?fileUUID='+fileUUID+'" target="_blank"><img src="/iwork_img/attach.png"/>'+fileName+'</a></span>';
			if(removeFlag){
				html 	+= '		<a href="javascript:uploadifyReomve(\''+colName+'\',\''+fileUUID+'\',\''+fileDivId+'\');"><img src="/iwork_img/del3.gif"/></a>';
			}
			html 	+= '</span>';
			return html;
	}
		function closeWin(){
			api.close(); 
		}
		function execute(){
			$('#uploadify').uploadifyUpload();
			return false;
		}
	</script>
	<style type="text/css">
		.tab{
			border-top:1px solid #999;
			border-left:1px solid #999;
			border-right:1px solid #999;
			border-bottom:1px solid #999;
			background:#ccc;
			padding:5px;
			width:100px;
			margin-top:100px;
			cursor:pointer;
		}
		.sel{
			background:#fff; 
			cursor:auto;
			border-bottom:1px solid #fff;
		}
		
	</style>
  </head>
  
 <body class="easyui-layout">
 <div region="north" border="false" style="border-top:1px solid #efefef;padding-top:20px;overflow:hidden;text-align:left;">
 		<div>
  		</div>
    </div>
 <div region="center" style="text-align:center;border-left:1px #999 solid;border-right:1px #999solid;border-top:1px #fff solid;border-bottom:0px #999 solid;padding:2px;">
  	<form action="iwork_email_doUpload.action" method="post" enctype="multipart/form-data">
  		<table width='100%'>	
  			<tr>
  				<td><input id="uploadify" name="uploadify" type="file" /></td>
  				<td>&nbsp;</td>
  			</tr>
			<tr>
  				<td colspan='2'><div style="height:300px;overflow:auto"><div id="fileQueue" style="height:250px"></div></div></td>
  			</tr>
        </table>
        <div id="result"></div> 
    </form>
    </div>
    <div region="south" border="false" style="border-top:1px solid #efefef;height:35px;text-align:right;padding:5px;">
    	<a href="#" onclick="execute()" class="easyui-linkbutton" plain="false" iconCls="icon-add">确定上传</a>
  		<a href="#" onclick="closeWin();" class="easyui-linkbutton" plain="false" iconCls="icon-cancel">关闭</a>
  		
    </div>
  </body>
</html>
