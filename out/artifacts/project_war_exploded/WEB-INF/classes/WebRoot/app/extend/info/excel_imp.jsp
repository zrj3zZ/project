<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link href="iwork_css/upload/uploadify.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/upload/swfobject.js"></script>
	<script type="text/javascript" src="iwork_js/upload/jquery.uploadify.v2.1.4.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js"></script>
	<script type="text/javascript">
		var api = frameElement.api, W = api.opener;	
		function doUpfile(){
			var src = $('#uploadify').val();
			if(src!=''){
				var fileExt=(/[.]/.exec(src)) ? /[^.]+$/.exec(src.toLowerCase()):'';
				submitForm(src); 
			}
		}
		
		function submitForm(src){
			var options = {
					error:errorFunc,
					success:successFunc,
					data: {'fileName':src}
			};
			$('#upfileForm').ajaxSubmit(options);
		}
		
		errorFunc=function(){
	           alert("导入失败！");
	    } 
	    successFunc=function(responseText, statusText, xhr, $form){
	           if(responseText=="success"){
	           		alert("导入成功！"); 
	           		parent.$('#subformCGSQSB').trigger("reloadGrid");
	                api.close();
	           }else if(responseText=="error"){
	              alert("导入失败！");
	           }else if(responseText=="typeerror"){
	              alert("数据模板匹配异常,导入失败！");
	           }  
	      }
				function closeWin(){
					var api = frameElement.api;
					api.close(); 
				}
				function execute(){
					$('#uploadify').uploadifyUpload();
					return false;
				}
	</script> 
</head> 
<body class="easyui-layout"> 
<div region="center" style="text-align:center;border-left:1px #999 dotted;border-right:1px #999 dotted;border-top:1px #999 dotted;border-bottom:0px #999 dotted;padding:2px;">
		<form action="zqb_announcement_DoImp.action" id="upfileForm" name="upfileForm" method="post" enctype="multipart/form-data">
  		<input id="uploadify" name="uploadify" type="file" onChange="doUpfile();"  size=40/></td>	 
       	<s:hidden name="formid" id="formid"></s:hidden>
		<s:hidden name="demId" id="demId"></s:hidden>
        </table>
    </form>
    </div>
</body>
</html>
