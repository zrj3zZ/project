<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java"%>
<%@ taglib prefix="webUpload" uri="/WEB-INF/tld/upload/upload.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/upload/c.tld"%>
<html>
<script type="text/javascript" src="../iwork_js/upload/upload.js"></script>
<link href="iwork_css/base-090628.css" rel="stylesheet" type="text/css" />
<link href="iwork_css/reset.css" rel="stylesheet" type="text/css" />
<link href="iwork_css/tab.css" rel="stylesheet" type="text/css" />
<script >
function uploadThis(){
    document.uploadForm.submit();  
}
</script>
<body>
<form name="uploadForm" action="upload-uploadFile" method="post" enctype="multipart/form-data"  target='hidden_frame'>
		<div class="padding6-14">
			  <table align="center" width="100%" border="0" cellspacing="0" class="table06">
			     <tr>
			      <th colspan="2">带进度条上传</th>
			      </tr>  
			       <tr>
			          <td class="color table-right" >
			                 信息输入:
			          </td>
			          <td>
			              &nbsp;<input type="text" size="19" class="input-def" name="" value="" id=""/>&nbsp;
			          </td>
			       </tr>
			       <tr>      
			       <webUpload:upload/>
			       </tr>
			</table>                   
		 </div>
</form>
</body>
</html>