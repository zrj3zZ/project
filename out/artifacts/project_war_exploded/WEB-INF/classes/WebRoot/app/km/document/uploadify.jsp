<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>上传附件</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/upload/uploadify.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/upload/swfobject.js"></script>
	<script type="text/javascript" src="iwork_js/upload/jquery.uploadify.v2.1.4.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
    var api = art.dialog.open.api;
		$(document).ready(function() {
		    var delUrl = 'km_file_uploadifyRemove.action';      //删除地址
			var uploadUrl = 'km_file_uploadifyUpload.action';   //上传地址
			var dloadUrl = 'km_file_uploadifyDownload.action';    //下载地址 
					    
			var  sizeLimit = $("#sizeLimit").val();
			var  multi = $("#multi").val();
			var  fileExt =$("#fileExt").val();
			var  fileDesc=$("#fileDesc").val();
			var  fileMaxNum=$("#fileMaxNum").val();
			var  parentid = $("#parentid").val();
			
			if(sizeLimit==null||sizeLimit==""||parseInt(sizeLimit)!=sizeLimit){
				sizeLimit=10240000;
			}
			if(multi=='true'){
				multi=true;
			}else if(multi=='false'){
				multi=false;
			}else{
				multi=true;
			}
			if(fileExt==null||fileExt==""||fileDesc==null||fileDesc==""){
				fileDesc=null;
				fileExt=null;
			}
			
			//var selectOnce={totalNum:'0',totalSize:'0'};//用于存放每选一次，所选文件的个数和大小
			var fileArray=new Array();//用于存放已经上传成功的附件uuid
			var tempFileArray=new Array();//用于onComplete事件中临时存放uuid,如果tempFileArray中的uuid已经加入到fileArray，tempFileArray清空
			//定义文件对象结构
			function uploadifyFile(id,fileName,fileSize,fileUrl,uuid){
				this.id=id;
				this.uuid=uuid;
				this.fileName=fileName;
				this.fileSize=fileSize;
				this.fileUrl=fileUrl;
			}
			//type=0把文件对象数组（存放uploadifyFile）转换成UUID字符串，type=1把文件对象数组转换成文件大小字符串
			function fileArrToStr(arr,type){
				var str="";
				if(arr!=null&&arr.length>0){
				    if(type==0){
				        	for(var i=0;i<arr.length;i++){
								var file = arr[i];
								if(str==""){
									str=file.uuid;
								}else{
									str+=","+file.uuid;
								}
					 		 }
				    }
				    else if(type==1){
				           for(var i=0;i<arr.length;i++){
								var file = arr[i];
								if(str==""){
									str=file.fileSize;
								}else{
									str+=","+file.fileSize;
								}
					 		 }
				    }					
				}
				return str;
			}
		   //获得数组（uploadifyFile）中文件大小之和
		   function getTotalSize(arr){
		        var totalSize= 0;    
		        if(arr!=null&&arr.length>0){
		             for(var i=0;i<arr.length;i++){
								var file = arr[i];
								totalSize = totalSize+file.fileSize;
					 }
		        }
		        return totalSize;
		   }
		   
		  $('#uploadify').uploadify({
		    'uploader'  : 'iwork_js/upload/uploadify.swf',
		    'script'    :  uploadUrl+';jsessionid=<%=session.getId()%>',
		    'cancelImg' : 'iwork_img/del3.gif',
		    'folder'    : 'uploads', 
		    'fileDataName'   : 'uploadify',
		    'removeCompleted' : false,
			'sizeLimit' :sizeLimit,
			'simUploadLimit':10,
			'queueSizeLimit': fileMaxNum,
			'queueID':'fileQueue',			
		    'multi'     : multi,
		    'fileExt': fileExt,
		    'fileDesc' : fileDesc,
		    'buttonImg':'iwork_img/upload/select.gif',
		    'auto'      : false,
		    'scriptData'  : {'tableName':$("#parentTableName").val(),'rowId':$("#parentRowId").val()},
		    'onQueueFull' : function(event,quequeSizeLimit){
		              		art.dialog.tips('最多只能上传'+quequeSizeLimit+"个文件！",2);
		                },
		    'onSelectOnce' : function(event,data){
		                        var onceFileNum = data.fileCount;
		    					var onceTotalSize = data.allBytesTotal; 
		    					//selectOnce={totalNum:fileNum,totalSize:totalSize};
		    					var hasUploadSize = getTotalSize(fileArray);  
		    					$('#fileTotal').html(onceFileNum+fileArray.length);
		    					$('#sizeTotal').html(onceTotalSize+hasUploadSize);
		    				}, 
		    'onAllComplete':function(event,data) {
	    						//取父页面中现在的uuid值
	    						var parentColId = "docEnum";
	    						var parentFileDivId = "parentFileDivId";
	    						var origin = artDialog.open.origin; 
	    						var oldParentUUIDs = art.dialog.data('oldUUIDs');
	    						//把本次上传的文件的UUID拼成串
	    						var newUUIDs=fileArrToStr(tempFileArray,0);
	    						//把本次上传的文件的UUID串更新到父页面中 
	    						var uuids = insertUUID(oldParentUUIDs,newUUIDs);
	    						//校验父页面中UUID字段是否超长，如果超长则把已经上传的附件及页面信息清除，避免产生垃圾数据
								//如果字段校验成功，则更新父页面中的文件列表
								var uploadDiv = "";
								for(var i=0;i<tempFileArray.length;i++){
										var obj = tempFileArray[i];
										var html = buildFileElementHtml(parentColId,obj.uuid,obj.fileName,obj.uuid,obj.fileUrl,true);
										uploadDiv+=html; 
								}
								art.dialog.data('newUUID', newUUIDs);
								art.dialog.data('fileName', obj.fileName);
								art.dialog.data('bValue', uuids);
								art.dialog.data('bHtml', uploadDiv);
							//关闭当前窗口
							api.close();
							return;
		    /*								        
		    						var fileUUID = fileArrToStr(tempFileArray,0); 
		    						var fileSize = fileArrToStr(tempFileArray,1); 
		    						$.post('file_uploadMany_save.action',{fileUUID:fileUUID,fileSize:fileSize,parentid:parentid},function(data){
		    						        if(data=='ok'){
		    						              fileArray = fileArray.concat(tempFileArray);
									              tempFileArray=new Array();
		    						        }
		    						});
		    						closeWin();		*/
		    				},	
    		'onComplete'  : function(event, ID, fileObj, response, data) { 
    							if(response!=null&&response.length>0){
    								//把后台传回的值转换成json格式
    				 				var resJson = strToJson(response);
    				  				var flag = resJson.flag;  
    								if(flag==true||flag=='true'){    						   								        
    									    var file = new uploadifyFile(ID,fileObj.name,fileObj.size,resJson.url,resJson.uuid);
    									    tempFileArray.push(file);									        									
    								}
    							}
						    }						
		  });
		});
		
	//关闭窗口
	function closeWin(){
		//点击取消时，将两个值置空
		art.dialog.data('bHtml','');
		art.dialog.data('bValue','');
	    api.close();
	}
	//批量上传	
	function uploadify_many(){ 
	    var fileTotal = $('#fileTotal').html();   fileTotal = parseInt(fileTotal);
	    var sizeTotal = $('#sizeTotal').html();   sizeTotal = parseInt(sizeTotal);
	    var limitFileTotalNum = $('#fileMaxNum').val();   limitFileTotalNum = parseInt(limitFileTotalNum);
	    var limitFileTotalSize = $('#fileMaxSize').val(); limitFileTotalSize = parseInt(limitFileTotalSize);
	    if(fileTotal>limitFileTotalNum){
	        art.dialog.tips('最多只能上传'+limitFileTotalNum+"个文件！",2);
	        return ;
	    }
	    if(sizeTotal>limitFileTotalSize){
	        art.dialog.tips('最多只能上传'+limitFileTotalSize+"B文件！",2);
	    	return ;
	    }
	    jQuery('#uploadify').uploadifyUpload();
	    return;
	}	
	</script>
  </head>
  
  <body class="easyui-layout">
  <div region="center"  style="border:0px;padding:0px;background:#gegege" >
  	<form action="uploadifyUpload.action" method="post" enctype="multipart/form-data">
  		<table width='100%'>	
  			<tr>
  				<td><input id="uploadify" name="uploadify" type="file" /></td>
  				<td>&nbsp;</td>
  			</tr>
			<tr>
  				<td colspan='2'><div style="height:260px;overflow:auto"><div id="fileQueue" style="height:255px;"></div></div></td>
  			</tr>	
        </table>
        
        <s:hidden id="fileExt" name="fileExt"></s:hidden>
        <s:hidden id="fileDesc" name="fileDesc"></s:hidden>
        <s:hidden id="sizeLimit" name="sizeLimit"></s:hidden>       <!-- 每个文件大小限制 -->
        <s:hidden id="multi" name="multi"></s:hidden>
        <s:hidden id="fileMaxNum" name="fileMaxNum"></s:hidden>      <!-- 文件总个数限制 -->
        <s:hidden id="fileMaxSize" name="fileMaxSize"></s:hidden>    <!-- 文件总大小限制 byte为单位 -->
        <s:hidden id="parentid" name="parentid"></s:hidden>
    </form>
</div>
<div region="south" split="true" style="border:0px;height:60px;text-align:right;color:#666"> 
    <div style='text-align:left;align:center;color:#ff0000;margin-left:5px;'>
        已选择&nbsp;<span id="fileTotal" style="font-weight:bold;">0</span>&nbsp;个文件，所选文件共&nbsp;<span id="sizeTotal" style="font-weight:bold;">0</span>&nbsp;<br/>
        注意事项：每次上传文件数量不大于<s:property value='fileMaxNum'/>，且每个文件不超过<s:property value='sizeLimit'/>B
    </div> 
    <a class="easyui-linkbutton" icon="icon-ok" href="javascript:uploadify_many();">确定上传</a> 
	<a class="easyui-linkbutton" icon="icon-cancel" href="javascript:closeWin();">关闭</a>      			
</div>    
  </body>
</html>
