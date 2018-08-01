<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java"%>
<%@ taglib prefix="webUpload" uri="/WEB-INF/tld/upload/upload.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/upload/c.tld"%>
<html>
<head>
<link href="iwork_css/base-090628.css" rel="stylesheet" type="text/css" />
<link href="iwork_css/reset.css" rel="stylesheet" type="text/css" />
<link href="iwork_css/tab.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
-->
</style>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/kmjs/km_upload.js"></script>


<script type="text/javascript">
function uploadThis(){
    document.uploadForm.submit();  
}
</script>
<link href="../iwork_css/public.css" rel="stylesheet" type="text/css" />
</head> 
<body>
<form name="uploadForm" action="upload-uploadFile" method="post" enctype="multipart/form-data"  target='hidden_frame'>
	<!-- 	<div class="padding6-14"> -->
			  <table align="center" width="100%" border="0" cellspacing="0" class="table06">
			       <tr>      
			       <webUpload:kmupload/>
			       </tr>
			</table>                   
		<!--  </div>-->
<!-- 
<table border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td class="td3">
    <table width="100%" border="0" cellpadding="2" cellspacing="0">
    <tr>
        <td>目录ID</td>
        <td><s:textfield cssClass="txtInput"  theme="simple" label="目录ID" disabled="true" name="model.id"/></td>
        </tr>
    </table>
	</td>
  </tr>
  <tr>
  		<td>
  			<table>
  					<tr>
  						<td width="150"></td>
  					<td>
  						<table>
  						<s:submit value="保存" cssClass="save_btn"/>
  						</table>
  					</td></tr>
  					
  			</table>
  		</td>
  </tr>
</table>  
 -->   
</form>
</body>
</html>
