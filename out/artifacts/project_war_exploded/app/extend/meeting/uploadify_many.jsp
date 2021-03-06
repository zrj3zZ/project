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
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link href="iwork_css/upload/uploadify.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/upload/swfobject.js"></script>
	<script type="text/javascript" src="iwork_js/upload/jquery.uploadify.v2.1.4.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
	<script type="text/javascript">
    var api= frameElement.api; W = api.opener; 
	//关闭窗口
	function closeWin(){
	     
	     api.close();
	}
	
	function fileCommit(uuid){
		$.ajax({
            type: 'POST',
            url: "zqb_file_uploadCommit.action",
            async: false,
            data: {fileUUID:uuid},
            success: function (data) {}
        });
	}
	
	function removeFileObject(uuid){
		$.ajax({
			   type: "POST",
			   async : false,
			   url: "zqb_file_remove.action",
			   data: {fileUUID:uuid},
			   success: function(msg){
				 if(msg!=null&&msg!=""){
					 if(msg=='success'){
						flag=true;
					 }else{
						flag=false;
					 }
				 }
			   },
			   error:function(msg){
					flag=false;
			   }
		});
	}
	</script>
	<script type="text/javascript">
		$(document).ready(function() {
		    var delUrl = 'km_file_uploadifyRemove.action';      //删除地址
			var uploadUrl = 'zqb_meeting_upload.action;jsessionid=<%=session.getId()%>'+'?parentid='+parentid;   //上传地址 
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
				return false;
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
		    'script'    :  uploadUrl,
		    'cancelImg' : 'iwork_img/del3.gif',
		    'folder'    : 'uploads',
		    'fileDataName'   : 'uploadify',
		    'removeCompleted' : false,
			'sizeLimit' :sizeLimit,
			'buttonImg':'iwork_img/upload/select.gif',
			'simUploadLimit':1,
			'queueSizeLimit': fileMaxNum,
			'queueID':'fileQueue',			
		    'multi'     : multi,
		    'fileExt'	: '*.txt;*.xls;*.xlsx;*.doc;*.docx;*.pdf;*.ppt;*.pptx;*.zip;*.rar;*.bmp;*.jpg;*.jpeg;*.png;*.gif;*.xml',
		    'fileDesc' 	: '*.txt;*.xls;*.xlsx;*.doc;*.docx;*.pdf;*.ppt;*.pptx;*.zip;*.rar;*.bmp;*.jpg;*.jpeg;*.png;*.gif;*.xml',
		    'auto'      : false,
		    'scriptData'  : {'parentid':$("#parentid").val(),'tableName':$("#parentTableName").val(),'rowId':$("#parentRowId").val()},
		    'onQueueFull' : function(event,quequeSizeLimit){
		              		W.lhgdialog.tips('最多只能上传'+quequeSizeLimit+"个文件！",2);
		                },
		    'onSelectOnce' : function(event,data){
		                        var onceFileNum = data.fileCount;
		    					var onceTotalSize = data.allBytesTotal; 
		    					//selectOnce={totalNum:fileNum,totalSize:totalSize};
		    					var hasUploadSize = getTotalSize(fileArray);  
		    					$('#fileTotal').html(onceFileNum+fileArray.length);
		    					$('#sizeTotal').html(onceTotalSize+hasUploadSize);
		    				},            
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
		    							api.close();																										
		    				},
		    'onCancel'    : function(event,ID,fileObj,data) { 
		    					var flag = true;
		    					var flag2 = false; //默认已上传队列，即fileArray中没有要删除的文件
		    					//循环已经上传的附件数组，找到对应的ID的附件，执行删除
    							for(var i=0;i<fileArray.length;i++){
										var obj = fileArray[i];
										if(ID==obj.id){
										    flag2 = true;//已上传队列中存在要删除的文件
											//从后台删除附件
											flag = uploadifyRemoveServerCom(obj.uuid,delUrl);
											if(flag){
											    $.post('file_uploadMany_del.action',{fileUUID:obj.uuid},function(response){  
		    						        		if(response=='ok'){
		    						              		fileArray.splice(i,1);
		    						              		var FileNumBeLeft = data.fileCount;         //剩下没有上传的
		    											var TotalSizeBeLeft = data.allBytesTotal;   //剩下没有上传的   
		    											//selectOnce={totalNum:fileNum,totalSize:totalSize};
		    											var hasUploadSize = getTotalSize(fileArray);
		    											$('#fileTotal').html(FileNumBeLeft+fileArray.length);  
		    											$('#sizeTotal').html(TotalSizeBeLeft+hasUploadSize);
		    						        		}
		    									});																			
							 				}
							 				break;
										}
								}
								if(!flag){
									//如果删除失败，则提示
							   		alert("删除失败");
							   		
							   		var FileNumBeLeft = data.fileCount;         //剩下没有上传的
		    						var TotalSizeBeLeft = data.allBytesTotal;   //剩下没有上传的   
		    						//selectOnce={totalNum:fileNum,totalSize:totalSize};
		    						var hasUploadSize = getTotalSize(fileArray);
		    						$('#fileTotal').html(FileNumBeLeft+fileArray.length);  
		    						$('#sizeTotal').html(TotalSizeBeLeft+hasUploadSize);
							   }
							   if(!flag2){
							        var FileNumBeLeft = data.fileCount;         //剩下没有上传的
		    						var TotalSizeBeLeft = data.allBytesTotal;   //剩下没有上传的   
		    						//selectOnce={totalNum:fileNum,totalSize:totalSize};
		    						var hasUploadSize = getTotalSize(fileArray);
		    						$('#fileTotal').html(FileNumBeLeft+fileArray.length);  
		    						$('#sizeTotal').html(TotalSizeBeLeft+hasUploadSize);
							   }	    					
							   return flag;
    						},	
    		'onComplete'  : function(event, ID, fileObj, response, data) { 
    							if(response!=null&&response.length>0){
    								//把后台传回的值转换成json格式
    				 				var resJson = strToJson(response);
    				  				var flag = resJson.flag;  
    								if(flag==true||flag=='true'){    		
    									    var file = new uploadifyFile(ID,fileObj.name,fileObj.size,resJson.url,resJson.uuid);
    									    fileCommit(resJson.uuid);
    									    tempFileArray.push(file);									        									
    								}
    							}
						    }						
		  });
		});
	//批量上传	
	function uploadify_many(){ 
	    var fileTotal = $('#fileTotal').html();   fileTotal = parseInt(fileTotal);
	    var sizeTotal = $('#sizeTotal').html();   sizeTotal = parseInt(sizeTotal);
	    var limitFileTotalNum = $('#fileMaxNum').val();   limitFileTotalNum = parseInt(limitFileTotalNum);
	    var limitFileTotalSize = $('#fileMaxSize').val(); limitFileTotalSize = parseInt(limitFileTotalSize);
	    if(fileTotal>limitFileTotalNum){
	        W.lhgdialog.tips('最多只能上传'+limitFileTotalNum+"个文件！",2);
	        return ;
	    }
	    if(sizeTotal>limitFileTotalSize){
	        W.lhgdialog.tips('最多只能上传'+limitFileTotalSize+"B文件！",2);
	    	return ;
	    }
	    jQuery('#uploadify').uploadifyUpload();
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
        注意事项：每次上传不应超过60M和50个文件，每个文件不应大于30M
    </div> 
    <a class="easyui-linkbutton" icon="icon-ok" href="#" onclick="uploadify_many();return false;">确定上传</a> 
	<a class="easyui-linkbutton" icon="icon-cancel" href="#" onclick="closeWin();return false;">关闭窗口</a>      			
</div>    
  </body>
</html>
<!-- 新增查询过滤SQL注入关键字 -->
<script language="JavaScript"> 
  jQuery.validator.addMethod("string", function(value, element) {
          var sqlstr=[" and "," exec ", " count ", " chr ", " mid ", " master ", " or ", " truncate ", " char ", " declare ", " join ","insert ", "select ", "delete ", "update ","create ","drop "]
          var patrn=/[`~!#$%^&*+<>?"{},;'[\]]/im;
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