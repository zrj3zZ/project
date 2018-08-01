<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>操作提示</title>
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
<script type="text/javascript">
	function closeWin(){
		try{
			window.parent.opener.$("#task_list_grid").trigger('reloadGrid'); 
		}catch(err){}
		setTimeout('window.close();',1000);
	}
</script>
</head>

<body>
<table width="100%" height="100%">
<tr>
<td align="center" valign="middle">
<table border="0" align="center" cellpadding="0" cellspacing="0" width="460" valign="middle">
  <tr>
    <td class="onleft"></td>
	 <td class="onbg">

			<table border="0" width="400">
			<tr>
			<td class="onnologo"></td>
			<td class="onyestext">流程任务打开时异常，可能此任务已办理完毕。
			</td>
			</tr>
			</table>
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" >
			  <tr>
				<td align="right">
				<a href="#" onclick='closeWin();' class="easyui-linkbutton" plain="false" iconCls="icon-cancel">关闭</a>
			</td>
			  </tr>
			</table>
</td>
  <td class="onright"></td>
  </tr>
</table>
</td>
</tr>
</table>

</body>
</html>
