<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<title>个人信息管理</title>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link href="iwork_css/base-090628.css" rel="stylesheet" type="text/css" />
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link href="iwork_css/reset.css" rel="stylesheet" type="text/css"/>
<link href="iwork_css/jquerycss/Jcrop/jquery.Jcrop.css" rel="stylesheet" type="text/css"/>
<link href="iwork_css/upload/uploadify.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/upload/swfobject.js"></script>
<script type="text/javascript" src="iwork_js/upload/jquery.uploadify.v2.1.4.js"></script>
<script type="text/javascript" src="iwork_js/commons.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/Jcrop/jquery.Jcrop.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<style type="text/css">
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
td {
 text-align: center;
} 
</style>
<script type="text/javascript">
	var api = art.dialog.open.api, W = api.opener;
	//预览
	jQuery(function($){

      // Create variables (in this scope) to hold the API and image size
      var jcrop_api, boundx, boundy;
      
      $('#target').Jcrop({
        onChange: updatePreview,
        onSelect: updatePreview,
        aspectRatio: 1
      },function(){
        // Use the API to get the real image size
        var bounds = this.getBounds();
        boundx = bounds[0];
        boundy = bounds[1];
        // Store the API in the jcrop_api variable
        jcrop_api = this;
      });

      function updatePreview(c)
      {
        if (parseInt(c.w) > 0)
        {
          var rx = 100 / c.w;
          var ry = 100 / c.h;

          $('#preview').css({
            width: Math.round(rx * boundx) + 'px',
            height: Math.round(ry * boundy) + 'px',
            marginLeft: '-' + Math.round(rx * c.x) + 'px',
            marginTop: '-' + Math.round(ry * c.y) + 'px'
          });
        }
        $('#imgInfo').val(boundx+"_"+c.x+"_"+c.y+"_"+c.w+"_"+c.h);
      };

    });
    function saveImage(){
    	var imgInfo = $('#imgInfo').val();
    	if(null== imgInfo){
    		imgInfo=="";
    	}
	   	var options = {
			error:errorFunc,
			success:showResponse 
			};
			$("#saveImageForm").ajaxSubmit(options);
	}
	errorFunc = function(){
		art.dialog.tips("头像保存失败，返回值异常(错误号:ERROR-1001)",2);
	}
	showResponse = function(responseText, statusText, xhr, $form){
		if(responseText=="success"){
			art.dialog.tips("头像保存成功",2);
			setTimeout('api.close();',2000);
		}else if(responseText=="ERROR-1002"){
			art.dialog.tips("原图大小小于截取图片大小,保存失败(错误号:ERROR-1002)",2);
		}else if(responseText=="ERROR-1003"){
			art.dialog.tips("为保证头像显示效果,请勿截取过小的图片(错误号:ERROR-1003)",2);
		}else{
			art.dialog.tips("头像保存失败，返回值异常(错误号:ERROR-1001)",2);
		}
	}
	
	$(document).ready(function() {
		$('#myFile').uploadify({
			'uploader'  : 'iwork_js/upload//uploadify.swf',
		    'script'    : 'syspersion_photo_UpFile.action;jsessionid=<%=session.getId()%>',
		    'scriptData'     : {'userid': '<s:property value="userid" escapeHtml="false"/>', 'isUserImageExists': '<s:property value="isUserImageExists" escapeHtml="false"/>'},
		    'cancelImg' : 'iwork_img/del3.gif',
		    'fileDataName'   : 'myFile',
		    'removeCompleted' : false,
            'folder'         : 'uploads',
            'queueID'        : 'fileQueue',
            'auto'           : true,
            'multi'          : false,
            'fileExt'        : '*.gif;*.jpg;*.png',
            'fileDesc'       : '*.gif;*.jpg;*.png',
            'sizeLimit'      : 2*1024*1024,
            'buttonImg'     : 'iwork_img/upload/select.gif',
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
		  			art.dialog.tips("上传成功");
		  			setTimeout('window.location.reload();',1000);
		  					}
		  });
	});
</script>
</head>
<body> 
<form name="uploadForm"  action="syspersion_photo_UpFile"  method="post" enctype="multipart/form-data" >
	<table cellpadding="0" cellspacing="0" align="center" width="80%">
		<tr><td colspan="2" style="border-bottom:1px solid #efefef; "><span><font style="font-size: 14px;color: #707070">仅支持JPG、GIF、PNG图片格式,且文件小于2M</font></span></td></tr>
		<tr>
			<td width="65%" style="height: 250px;border-right: 1px solid #efefef;padding-right: 10px;">
				<div id="b_image">
					<img width="230" id="target" src='<s:property value='userImgPath'/>' alt='用户照片' title='用户照片' name='photoUpload' id='photoUpload' style="border:1px solid #e5e5e5;margin:3px;padding:5px;">
				</div>
			</td>
			<td width="35%" style="height: 250px;border-left: 1px solid #efefef;padding-left: 10px;">
				<div id="s_image" style="width:100px;height:100px;overflow:hidden;">
					<img id="preview" hspace="5" src='<s:property value='userImgPath'/>' title='用户照片' name='photoUpload' id='photoUpload' style="border:1px solid #e5e5e5;margin:3px;">
				</div>
			</td>
		</tr> 
		<tr>
			<td width="100%" style='padding-left:20px;height: 50px' colspan="2" >
             <table width="100%" cellspacing="0" cellpadding="0" >
             	<tr>
    				<td align="right">
      					<s:file name ="myFile" label ="上传头像"  theme="simple" cssStyle="width:200px"  cssClass='input-def'/>
      				</td>
	      			<td>
	      				<a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:saveImage();" >保存</a>
	      			</td>
      			</tr>
      			<tr>
	  				<td colspan='2'><div style="height:50px;overflow:hidden;"><div id="fileQueue" style="height:50px"></div></div></td>
	  			</tr>
      		  </table>
      		 </td>
		</tr>
	</table>
</form>
<s:form name="saveImageForm" id="saveImageForm" action="syspersion_photo_saveImage" method="post">
	 <table>
	 	<div style="display:none ">
			imgInfo<input type="text" id="imgInfo" name="imgInfo" value=""/>
			<s:textfield name="userid" id="userid" cssStyle="display:none"/>
			<s:textfield name="isUserImageExists" cssStyle="display:none"/>
		</div>
     </table>
</s:form>
</body>	
</html>
