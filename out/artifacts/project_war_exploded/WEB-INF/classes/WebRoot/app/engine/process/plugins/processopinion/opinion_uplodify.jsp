<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>意见上传附件</title>
	<meta http-equiv="description" content="意见附件上传">
	<link href="iwork_css/upload/uploadify.css" type="text/css" rel="stylesheet" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>
	<script type="text/javascript" src="iwork_js/upload/swfobject.js"></script>
	<script type="text/javascript" src="iwork_js/upload/jquery.uploadify.v2.1.4.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js"   ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
	var api = art.dialog.open.api, W = api.opener;
	$(document).ready(function() {
	    //跳转到单文件上传，暂时屏蔽多文件上传
		setBaseUpload();
		
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
		
		
	  $('#uploadify').uploadify({
	    'uploader'  : 'iwork_js/upload/uploadify.swf',
	    'script'    : 'uploadifyUpload.action;jsessionid=<%=session.getId()%>',
	    'cancelImg' : 'iwork_img/del3.gif',
	    'folder'    : 'uploads',
	    'fileDataName'   : 'uploadify',
	    'removeCompleted' : false,
		'sizeLimit' :sizeLimit,
		'queueID':'fileQueue',
		'simUploadLimit':10,
	    'multi'     : multi,
	    'fileExt': '<s:property value="fileExt"/>',
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
	    				var origin = artDialog.open.origin; 
	    						//取父页面中现在的uuid值
	    						var oldParentUUIDs = origin.document.getElementById("attach").value;
	    						//把本次上传的文件的UUID拼成串
	    						var newUUIDs=fileArrToStr(tempFileArray);
	    						//把本次上传的文件的UUID串更新到父页面中 
	    						origin.document.getElementById("attach").value = insertUUID(oldParentUUIDs,newUUIDs);
	    						//校验父页面中UUID字段是否超长，如果超长则把已经上传的附件及页面信息清除，避免产生垃圾数据
									//如果字段校验成功，则更新父页面中的文件列表
									var input = origin.document.getElementById("DIVATTACHMENT");
									var resultHtml = "";
									for(var i=0;i<tempFileArray.length;i++){
											var obj = tempFileArray[i];
											var html = buildFileElementHtml(parentColId,obj.uuid,obj.fileName,obj.uuid,obj.fileUrl,true);
											var origin = artDialog.open.origin; 
											if(typeof(input)=="object"){//判断编辑窗口是否打开
												resultHtml+=html;
											}
									}
									input.innerHTML=resultHtml;
									fileArray = fileArray.concat(tempFileArray);
									tempFileArray=new Array();
									close();
							//关闭当前窗口
							return;
	    				},
	    'onCancel': function(event,ID,fileObj,data) {
	    					var origin = artDialog.open.origin; 
	    					var flag = true;
	    					//循环已经上传的附件数组，找到对应的ID的附件，执行删除
							for(var i=0;i<fileArray.length;i++){
									var obj = fileArray[i];
									if(ID==obj.id){
										//从后台删除附件
										flag = uploadifyRemoveServer(obj.uuid);
										if(flag){
											//如果删除成功，则更新父页面中UUID的值
						 					var oldParentUUIDs = origin.document.getElementById("attach").value;
	    									origin.document.getElementById("attach").value = removeUUID(oldParentUUIDs,obj.uuid);
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
       //调用父页面脚本，关闭办理窗口
        function close(){ 
        	api.close();
        	return; 
       }
       
		function execute(){
			$('#uploadify').uploadifyUpload();
			
			return;
		}
		function setBaseUpload(){
			var divId = $("#parentDivId").val();
			var fieldName = $("#parentColId").val();
			this.location.href="process_opin_baseAttach.action?parentColId="+fieldName+"&parentDivId="+divId;
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
  			<span class="tab sel">多文件上传</span>  <span class="tab" onclick="c">单文件上传</span> 
  		</div>
    </div>
 <div region="center" style="text-align:center;border-left:1px #999 solid;border-right:1px #999solid;border-top:1px #fff solid;border-bottom:0px #999 solid;padding:2px;">
  	<form action="uploadifyUpload.action" method="post" enctype="multipart/form-data">
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
        <s:hidden id="parentColId" name="parentColId"></s:hidden>
        <s:hidden id="parentDivId" name="parentDivId"></s:hidden>
        <s:hidden id="fileExt" name="fileExt"></s:hidden>
        <s:hidden id="fileDesc" name="fileDesc"></s:hidden>
        <s:hidden id="sizeLimit" name="sizeLimit"></s:hidden>
        <s:hidden id="multi" name="multi"></s:hidden>
    </form>
    </div>
    <div region="south" border="false" style="border-top:1px solid #efefef;height:35px;text-align:right;padding:5px;">
    	<a href="###" onclick="execute()" class="easyui-linkbutton" plain="false" iconCls="icon-add">确定上传</a>
  		<a  href="javascript:close();" class="easyui-linkbutton" plain="false" iconCls="icon-cancel">关闭</a>
  		
    </div>
  </body>
</html>
