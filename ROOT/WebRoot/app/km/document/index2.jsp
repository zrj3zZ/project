<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head><title>知识管理</title><meta http-equiv="Content-Type" content="text/html; charset=gb2312"><style type="text/css">
<!--
body { font-family:Verdana; font-size:14px; margin:0;}
#container {margin:0 auto; width:100%;}
#sidebar { float:left; width:200px; height:400px;}
#sidebar2 { float:right; width:200px; height:400px;}
#content { margin:0 205px !important; margin:0 202px; height:400px;}
.STYLE1 {
	color: #FFFFFF;
	font-weight: bold;
	font-size: 12px;
}
.STYLE8 {color: #000000; font-size: 12px; }
-->
</style></head>

<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link href="iwork_css/km_style.css" rel="stylesheet" type="text/css" />
<link href="iwork_css/km_menu.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="iwork_js/commons.js"></script>
		<script language="javascript" src="iwork_js/commons.js"></script>
<body>
<div id="container">
  <div id="sidebar">
  		<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
		  <tr>
		    <td height="25" bgcolor="#1078b5" style="padding-left:15px;"><span class="STYLE1">IWORK文档管理</span></td>
		  </tr>
		  
		      <tr>
		        <td valign="top"><table width="100%" height="100" border="0" cellpadding="0" cellspacing="0">
		          <tr>
		            <td height="24" background="iwork_img/km/bg1.gif" style="padding-left:20px; font-size:12px; border-bottom:solid 1px #8db6cf;border-top:solid 1px #8db6cf;">目录导航</td>
		          </tr>
		          <tr>
		            <td><div align="center"><img src="iwork_img/km/t2.gif" width="141" height="155"></div></td>
		          </tr>
		        </table></td>
		      </tr>
		      <tr>
		        <td height="40"><table width="100%" border="0" cellspacing="0" cellpadding="0">
		          <tr>
		            <td height="28" background="iwork_img/km/bg.gif" style="border-top:solid 1px #8db6cf; padding-left:10px; font-size:12px; "><table width="100%" border="0" cellspacing="0" cellpadding="0">
		              <tr>
		                <td width="25" class="STYLE8"><img src="iwork_img/km/mail.gif" width="19" height="19"></td>
		                <td class="STYLE8">目录导航</td>
		              </tr>
		            </table></td>
		          </tr>
		          <tr>
		            <td height="28" background="iwork_img/km/bg.gif" style="border-top:solid 1px #8db6cf;padding-left:10px; font-size:12px; "><table width="100%" border="0" cellspacing="0" cellpadding="0">
		              <tr>
		                <td width="25" class="STYLE8"><img src="../iwork_img/km/date.gif" width="19" height="19"></td>
		                <td class="STYLE8">共享文档</td>
		              </tr>
		            </table></td>
		          </tr>
		          <tr>
		            <td height="28" background="iwork_img/km/bg.gif" style="border-top:solid 1px #8db6cf; padding-left:10px;font-size:12px; "><table width="100%" border="0" cellspacing="0" cellpadding="0">
		              <tr>
		                <td width="25" class="STYLE8"><img src="iwork_img/km/book.gif" width="19" height="19"></td>
		                <td class="STYLE8">收藏夹</td>
		              </tr>
		            </table></td>
		          </tr>
		          <tr>
		            <td height="28" background="iwork_img/km/bg.gif" style="border-top:solid 1px #8db6cf; padding-left:10px; font-size:12px;"><table width="100%" border="0" cellspacing="0" cellpadding="0">
		              <tr>
		                <td width="25" class="STYLE8"><img src="iwork_img/km/work.gif" width="19" height="19"></td>
		                <td class="STYLE8">任务</td>
		              </tr>
		            </table></td>
		          </tr>
		          <tr>
		            <td height="28" background="iwork_img/km/bg.gif" style="border-top:solid 1px #8db6cf; padding-left:10px;font-size:12px; ">&nbsp;</td>
		          </tr>
		        </table></td>
		      </tr>
		    </table>
  </div>
  <div id="sidebar2">
		<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
			  <tr>
			    <td valign="top" bgcolor="#FFFFFF" style="border:solid 1px #5fabd9;"><table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
			      <tr>
			        <td height="100" style="border-bottom:solid 1px #c3d7e3;"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
			          <tr>
			            <td height="22"><span class="STYLE5">欢迎使用 Microsoft Office Outlook 2003</span></td>
			          </tr>
			          <tr>
			            <td height="22"><span class="STYLE3">Outlook 2003 工作组 [olteam@microsoft.com]</span></td>
			          </tr>
			          <tr>
			            <td height="22"><span class="STYLE3">收件人：Outlook 新用户</span></td>
			          </tr>
			        </table></td>
			      </tr>
			      <tr>
			        <td><table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
			          <tr>
			            <td style="line-height:20px; font-size:12px;">感谢您使用 Microsoft? Office Outlook? 2003！ 此版本的 Outlook 包含一些新增功能，旨在帮助您对通信和信息进行访问、确定优先级以及进行处理，以便您可更有效地安排时间，并且更轻松地管理所接收到的越来越多的电子邮件。<br>
			              为了让您了解 Outlook 2003 的功能，我们整理了一个最受欢迎的新功能列表。<br></td>
			          </tr>
			        </table></td>
			      </tr>
			    </table></td>
			  </tr>
			</table>
  </div>
  <div id="content">
  		<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
			  <tr>
			    <td height="25" bgcolor="#1078b5" style="padding-left:15px;"><span class="STYLE1">文档管理>></span></td>
			  </tr>
			  <tr>
			    <td valign="top" style="border:solid 1px #8db6cf;"><table width="100%" border="0" cellspacing="0" cellpadding="0">
			      <tr>
			        <td height="22" background="../iwork_img/km/bg1.gif"  style="border-bottom:solid 1px #8db6cf; padding-top:1px; padding-bottom:1px;"><table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
			          <tr>
			            <td width="9%" height="22" style=" border-right: solid 1px #6daed6; ">&nbsp;</td>
			            <td width="11%" style="padding-left:1px; border-right: solid 1px #6daed6; border-left:solid 1px #e7f4fc;"><div align="center"><span class="STYLE2"> 收件人</span></div></td>
			            <td width="56%" style="padding-left:1px; border-right: solid 1px #6daed6; border-left:solid 1px #e7f4fc;"><div align="center"><span class="STYLE2">主题</span></div></td>
			            <td width="12%" style="padding-left:1px; border-right: solid 1px #6daed6; border-left:solid 1px #e7f4fc;"><div align="center"><span class="STYLE2">时间</span></div></td>
			            <td width="12%" style="padding-left:1px; border-right: solid 1px #6daed6; border-left:solid 1px #e7f4fc;"><div align="center"><span class="STYLE2">大小</span></div></td>
			            </tr>
			        </table></td>
			      </tr>
			      <tr>
			        <td style="padding-top:10px;"><table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
			          <tr>
			            <td><img src="iwork_img/km/list.gif" width="11" height="11"> <span class="STYLE3">大小 中 （25-100kb）</span> </td>
			          </tr>
			          <tr>
			            <td style="padding-top:5px;"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
			              <tr>
			                <td width="9%" height="22" style="border-bottom:solid 1px #c3d7e3;"><div align="center"><img src="iwork_img/km/ml.gif" width="13" height="12"></div></td>
			                <td width="16%" style="border-bottom:solid 1px #c3d7e3;"class="STYLE2" ><div align="left"><span class="STYLE2"> outlook2003 </span></div></td>
			                <td width="51%" style="border-bottom:solid 1px #c3d7e3;"class="STYLE2" ><div align="left"><span class="STYLE2">欢迎使用outlook2003系统</span></div></td>
			                <td width="12%" style="border-bottom:solid 1px #c3d7e3;"class="STYLE2" ><div align="center">2008-08-22</div></td>
			                <td width="12%"style="border-bottom:solid 1px #c3d7e3;" class="STYLE2" ><div align="center">123kb</div></td>
			              </tr>
			              <tr>
			                <td height="22" style="border-bottom:solid 1px #c3d7e3;"><div align="center"><img src="iwork_img/km/ml.gif" width="13" height="12"></div></td>
			                <td class="STYLE2"style="border-bottom:solid 1px #c3d7e3;" ><div align="left">tiezhu0902@qq.com</div></td>
			                <td class="STYLE2" style="border-bottom:solid 1px #c3d7e3;">你好，请接收邮件</td>
			                <td class="STYLE2" style="border-bottom:solid 1px #c3d7e3;"><div align="center">2008-08-25</div></td>
			                <td class="STYLE2" style="border-bottom:solid 1px #c3d7e3;"><div align="center">10kb</div></td>
			              </tr>
			            </table></td>
			          </tr>
			        </table></td>
			      </tr>
			    </table></td>
			  </tr>
			</table>
  </div>
</div>

</body>
</html>
