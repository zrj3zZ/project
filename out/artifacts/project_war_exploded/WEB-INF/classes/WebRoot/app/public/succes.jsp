<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>保存成功</title>
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script>
	var api = art.dialog.open.api, W = api.opener;
	function closeWin(){
    	api.close();
    }
	</script>
<style type="text/css">
	body{
		background-img:url(../../../iwork_img/background/securityLayer.gif); 
	}
	.title{
		padding:5px;
		font-size:22px; 
		font-family:黑体;
		line-height:30px;
		color:#f77215;
	}
	.info{
		background-color:#fff; 
		padding:5px;
	}
	.content li{
	line-height:20px;
	color:#f77215;
	font-size:12px;
	}
</style>
</head> 

<body>
<table width="100%" height="60%">
<tr>
<td align="center" valign="middle">
	<table cellpadding="0" cellspacing="0" >
		<tr>
		<td><img width="100" src="../../../iwork_img/success.jpg"/></td>
		<td class="info"> 
			<div class="title">保存成功!</div>
		</td>
		</tr>
	</table>
</td>
</tr>
</table>

</body>
</html>
<script>
	setTimeout("closeWin()",1000);
</script>