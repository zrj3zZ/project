<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
  <head>
    <title>文件上传</title>
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/commons.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
	var api = art.dialog.open.api, W = api.opener;
	$().ready(function() {
		var origin = artDialog.open.origin; 
	    						//取父页面中现在的uuid值
	    						var oldParentUUIDs = origin.document.getElementById("attach").value;
	    						//把本次上传的文件的UUID拼成串
	    						var newUUIDs= $("#fileUUID").val();
	    						//把本次上传的文件的UUID串更新到父页面中 
	    						origin.document.getElementById("attach").value = insertUUID(oldParentUUIDs,newUUIDs);
	    						//校验父页面中UUID字段是否超长，如果超长则把已经上传的附件及页面信息清除，避免产生垃圾数据
									//如果字段校验成功，则更新父页面中的文件列表
									var input = origin.document.getElementById("DIVATTACHMENT");
									var html = buildFileElementHtml("attach",$("#fileUUID").val(),$("#srcFileName").val(),$("#fileUUID").val(),$("#url").val(),true);
									input.innerHTML=input.innerHTML+html;
									api.close();
		
	}); 
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
	</script>
  </head>
  <body>
	  上传成功！
	  <s:form name="editForm">
	  	<s:hidden name="parentColId" id="parentColId"></s:hidden>
	  	<s:hidden name="parentDivId" id="parentDivId"></s:hidden>
	  	<s:hidden name="fileUUID" id="fileUUID"></s:hidden>
	  	<s:hidden name="url" id="url"></s:hidden>
	  	<s:hidden name="srcFileName" id="srcFileName"></s:hidden>
	  </s:form>
  </body>
</html>
