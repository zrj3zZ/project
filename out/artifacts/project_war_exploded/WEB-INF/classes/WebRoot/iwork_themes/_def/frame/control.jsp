<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>ÎÞ±êÌâÎÄµµ</title>
<link href="/iwork_css/public.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/iwork_js/tree/custom_UI.js"></script>

<script type="text/JavaScript">
<!--
function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
//-->
</script>
</head>

<body onload="MM_preloadImages('../../../iwork_img/control_left2.gif')">

<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" class="centrolbackground">
<tr>
<td>
<div  style="z-index: 0;left: 0;top: 1;position: absolute">
					<table border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td >
								<a href="#" onClick="hide_left()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('_timg','','../../../iwork_img/control_right.gif',1)"> <img src="../../../iwork_img/control_right2.gif"  name="_timg"  border="0" id="_timg" />
								</a>
							</td>
						</tr>

					</table>
				</div>
	
	<div id="div_left" style="display: block;z-index: 0;left: 0;top: 1;position: absolute">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<a href="#" onClick="hide_left()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('timg','','../../../iwork_img/control_left2.gif',1)"> <img src="../../../iwork_img/control_left.gif" name="timg" border="0" id="timg" /> </a>
					</td>
				</tr>
			</table>
		</div>

</td>
</tr>
</table>

<!--
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" class="centrolbackground">
  <tr>
    <td><a href="#" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image1','','images/control_left2.gif',1)"><img src="images/control_left.gif" name="Image1" width="8" height="51" border="0" id="Image1" /></a></td>
  </tr>
</table>
-->
</body>
</html>
<script>
	<%
	String status = request.getParameter("status");
	if(status!=null){
		if(status.equals("0")){
		%>
			hide_left();
		<%}else{
		%>
			show_left();
	<%
		}
	}else{
		%>show_left();<%
	}
	%>
</script>
