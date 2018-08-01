<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>操作提示</title>
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript">
var p=8;
var isBreakAuto=0;
	function onFocus(){
		setInterval("showProcess()",150);
	}
	function showProcess(){
	if(isBreakAuto==0){
		p--;
		//alert('');
		if(p<1){
			isBreakAuto=1;
			parent.reloadParentPage();
			parent.window.close();
			//parent.parent.GB_hide();
		}else{
			try{
				showProcessTitle.innerText=showProcessTitle.innerText+"》";
			}catch(e){}
		}
	}
} 
</script>
</head>

<body onLoad="onFocus();">

<table width="100%" height="100%">
<tr>
<td align="center" valign="middle">
<table border="0" align="center" cellpadding="0" cellspacing="0" width="460" valign="middle">
  <tr>
    <td class="onleft"> </td>
	 <td class="onbg">

			<table border="0" width="400">
			<tr>
			<td class="onyeslogo"></td>
			<td class="onyestext">已驳回成功!
			</td>
			</tr>
			</table>

<div id="showProcessTitle" align="right" style="color:red;">&nbsp;</div>
			<table border="0" align="center" cellpadding="0" cellspacing="0">
			  <tr>
				<td><input  type="button" onclick="showProcess();" class="btn_2" id="btnOK" value="关闭" />
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
