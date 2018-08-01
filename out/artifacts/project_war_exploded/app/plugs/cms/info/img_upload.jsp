<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>上传附件</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link href="iwork_css/upload/uploadify.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/upload/swfobject.js"></script>
	<script type="text/javascript" src="iwork_js/upload/jquery.uploadify.v2.1.4.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js"></script>
	<script type="text/javascript">
    var api= frameElement.api; W = api.opener; 
	//关闭窗口
	function closeWin(){
	     api.close();
	}
	</script>
	<script type="text/javascript">
		$(document).ready(function() {
		    var obj = api.data; 
		    var parentWin = obj.win;   
		    
			var parentHidId = $("#parentHidId").val();
			var parentFileDivId = $('#parentFileDivId').val();
			var  sizeLimit = $("#sizeLimit").val();
			var  multi = $("#multi").val();
			var  fileExt =$("#fileExt").val();
			var  fileDesc=$("#fileDesc").val();
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
			var fileArray=new Array();//用于存放已经上传成功的附件uuid
			var tempFileArray=new Array();//用于onComplete事件中临时存放uuid,如果tempFileArray中的uuid已经加入到fileArray，tempFileArray清空
			//定义文件对象结构
			function uploadifyFile(id,fileName,fileUrl,uuid){
				this.id=id;
				this.uuid=uuid;
				this.fileName=fileName;
				this.fileUrl=fileUrl;
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
		  $('#uploadify').uploadify({
		    'uploader'  : 'iwork_js/upload//uploadify.swf',
		    'script'    : 'cmsFileUpload.action;jsessionid=<%=session.getId()%>',
		    'cancelImg' : 'iwork_img/del3.gif',
		    'folder'    : 'uploads',
		    'fileDataName'   : 'uploadify',
		    'removeCompleted' : false,
			'sizeLimit' :sizeLimit,
			'queueID':'fileQueue',
			'simUploadLimit':10,
		    'multi'     : multi,
		    'fileExt': fileExt,
		    'fileDesc' : fileDesc,
		    'auto'      : false,
		    'scriptData'  : {'tableName':$("#parentTableName").val(),'rowId':$("#parentRowId").val()},
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

		    						//取父页面中现在的uuid值
		    		 				var oldparentUUIDs = $("#"+parentHidId,parentWin.document).attr("value");
		    						//把本次上传的文件的UUID拼成串
		    						var newUUIDs=fileArrToStr(tempFileArray);
		    						//把本次上传的文件的UUID串更新到父页面中
		    		    			$("#"+parentHidId,parentWin.document).attr("value",insertUUID(oldparentUUIDs,newUUIDs));
		    						//校验父页面中UUID字段是否超长，如果超长则把已经上传的附件及页面信息清除，避免产生垃圾数据
		    	                    var valid = parentWin.ValidateUUIDsLength();  
									if(!valid){
										alert("上传附件失败,有可能是附件提取码超长，如果您上传的附件过多，请打包成一个压缩文件再上传");
										//还原附件字段的值
										$("#"+parentHidId,parentWin.document).attr("value",oldparentUUIDs);
										//循环删除已经上传的附件和当前页面对应的文件列表
										for(var i=0;i<tempFileArray.length;i++){
												var obj = tempFileArray[i];
												uploadifyRemoveServer2(obj.uuid);
												$('#uploadify').uploadifyCancel(obj.id);												
										}
										//初始化tempFileArray，为了不影响后续的导入
										tempFileArray=new Array();
									}else{
										//如果字段校验成功，则更新父页面中的文件列表
										for(var i=0;i<tempFileArray.length;i++){
												var obj = tempFileArray[i];
												var html = buildFileElementHtml2(parentHidId,obj.uuid,obj.fileName,obj.uuid,obj.fileUrl,true);
												$("#"+parentFileDivId,parentWin.document).append(html);
										}										
										
										//把已经上传的附件信息合并到fileArray，并初始化tempFileArray
										fileArray = fileArray.concat(tempFileArray);
										tempFileArray=new Array();
									}
								
		    				},
		    'onCancel'    : function(event,ID,fileObj,data) {
		    					var flag = true;
		    					//循环已经上传的附件数组，找到对应的ID的附件，执行删除
    							for(var i=0;i<fileArray.length;i++){
										var obj = fileArray[i];
										if(ID==obj.id){
											//从后台删除附件
											flag = uploadifyRemoveServer2(obj.uuid);
											if(flag){
												//如果删除成功，则更新父页面中UUID的值
							 					var oldparentUUIDs = $("#"+parentHidId,parentWin.document).attr("value");
		    									$("#"+parentHidId,parentWin.document).attr("value",removeUUID(oldparentUUIDs,obj.uuid));
		    									//移除父页面中对应的文件列表
		    									if($("#"+obj.uuid,parentWin.document)!=null){
													$("#"+obj.uuid,parentWin.document).remove();
												}												
												//从fileArray数组中移除已经删除的附件
												fileArray.splice(i,1);
							 				}
							 				break;
										}
								}
								if(!flag){
									//如果删除失败，则提示
							   		alert("删除失败");
							   }
							   return flag;
    						},	
    		'onComplete'  : function(event, ID, fileObj, response, data) {
    							if(response!=null&&response.length>0){
    								//把后台传回的值转换成json格式
    				 				var resJson = strToJson(response);
									$("#cmsFileUploadUrl",parentWin.document).attr("src",resJson.url);
									$("#prepicture",parentWin.document).val(resJson.url);
									$("#cmsFileUploadUrl",parentWin.document).css("display","block");
    				  				var flag = resJson.flag;
    								if(flag==true||flag=='true'){
    									//如果上传成功，则把生成该附件对象，存放到tempFileArray中
    									var file = new uploadifyFile(ID,fileObj.name,resJson.url,resJson.uuid);
    									tempFileArray.push(file);
    								}
									closeWin();
    							}
						    }						
		  });
		});
	</script>
	
  </head>
  
  <body>
  	<form action="uploadifyUpload.action" method="post" enctype="multipart/form-data">
  		<table width='100%'>	
  			<tr>
  				<td><input id="uploadify" name="uploadify" type="file" /></td>
  				<td>&nbsp;</td>
  			</tr>
			<tr>
  				<td colspan='2'><div style="height:300px;overflow:auto"><div id="fileQueue" style="height:250px"></div></div></td>
  			</tr>
  			<tr>
  				<td align='right'><input type=button value="确定上传" onclick="javascript:jQuery('#uploadify').uploadifyUpload();"/></td>
  				<td><input type=button value="关闭窗口" onclick="closeWin()"/></td>
  			</tr>	
        </table>
        <div id="result"></div>
        <s:hidden id="parentHidId" name="parentHidId"></s:hidden>
        <s:hidden id="parentFileDivId" name="parentFileDivId"></s:hidden>
        <s:hidden id="fileExt" name="fileExt"></s:hidden>
        <s:hidden id="fileDesc" name="fileDesc"></s:hidden>
        <s:hidden id="sizeLimit" name="sizeLimit"></s:hidden>
        <s:hidden id="multi" name="multi"></s:hidden>
    </form>
  </body>
</html>
