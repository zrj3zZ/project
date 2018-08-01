<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head><title>知识管理</title><meta http-equiv="Content-Type" content="text/html; charset=utf-8"><style type="text/css">
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
<link rel="stylesheet" type="text/css" href="iwork_css/extcss/ext-all.css" />
    <link rel="stylesheet" type="text/css" href="iwork_css/extcss/xtheme-blue.css" /> 
    <script type="text/javascript" src="iwork_js/extjs/ext-base.js"  charset="utf-8"></script>
    <script type="text/javascript" src="iwork_js/extjs/ext-all-debug.js"charset="utf-8"></script>
<!-- Theme includes -->
    <link rel="stylesheet" type="text/css" title="blue"      href="iwork_css/extcss/xtheme-blue.css" /> 
    <link rel="stylesheet" type="text/css" title="gray"      href="iwork_css/extcss/xtheme-gray.css" />
    <link rel="stylesheet" type="text/css" title="yourtheme" href="iwork_css/extcss/yourtheme.css" />
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
		            <td><div align="center">目录树</div></td>
		          </tr>
		        </table></td>
		      </tr>
		      <tr>
		        <td height="40"><table width="100%" border="0" cellspacing="0" cellpadding="0">
		          <tr>
		            <td height="28" background="iwork_img/km/bg.gif" style="border-top:solid 1px #8db6cf; padding-left:10px; font-size:12px; "><table width="100%" border="0" cellspacing="0" cellpadding="0">
		              <tr>
		                <td width="25" class="STYLE8"><img src="iwork_img/process_file.gif" ></td>
		                <td class="STYLE8">目录导航</td>
		              </tr>
		            </table></td>
		          </tr>
		          <tr>
		            <td height="28" background="iwork_img/km/bg.gif" style="border-top:solid 1px #8db6cf;padding-left:10px; font-size:12px; "><table width="100%" border="0" cellspacing="0" cellpadding="0">
		              <tr>
		                <td width="25" class="STYLE8"><img src="iwork_img/km/date.gif" width="19" height="19"></td>
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
		          
		        </table></td>
		      </tr>
		    </table>
  </div>
  <div id="sidebar2">
		<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
		  <tr>
		    <td height="25" bgcolor="#1078b5" style="padding-left:15px;"><span class="STYLE1">IWORK文档管理</span></td>
		  </tr>
		  
		      <tr>
		        <td valign="top"><table width="100%" height="100" border="0" cellpadding="0" cellspacing="0">
		          <tr>
		            <td height="24" background="iwork_img/km/bg1.gif" style="padding-left:20px; font-size:12px; border-bottom:solid 1px #8db6cf;border-top:solid 1px #8db6cf;">基本信息</td>
		          </tr>
		          <tr>
		            <td><div align="center">目录树</div></td>
		          </tr>
		        </table></td>
		      </tr>
		      <tr>
		        <td height="40"><table width="100%" border="0" cellspacing="0" cellpadding="0">
		          <tr>
		            <td height="28" background="iwork_img/km/bg.gif" style="border-top:solid 1px #8db6cf; padding-left:10px; font-size:12px; "><table width="100%" border="0" cellspacing="0" cellpadding="0">
		              <tr>
		                <td width="25" class="STYLE8"><img src="iwork_img/process_file.gif" ></td>
		                <td class="STYLE8">基本信息</td>
		              </tr>
		            </table></td>
		          </tr>
		          <tr>
		            <td height="28" background="iwork_img/km/bg.gif" style="border-top:solid 1px #8db6cf;padding-left:10px; font-size:12px; "><table width="100%" border="0" cellspacing="0" cellpadding="0">
		              <tr>
		                <td width="25" class="STYLE8"><img src="iwork_img/km/date.gif" width="19" height="19"></td>
		                <td class="STYLE8">历史版本</td>
		              </tr>
		            </table></td>
		          </tr>
		          <tr>
		            <td height="28" background="iwork_img/km/bg.gif" style="border-top:solid 1px #8db6cf; padding-left:10px;font-size:12px; "><table width="100%" border="0" cellspacing="0" cellpadding="0">
		              <tr>
		                <td width="25" class="STYLE8"><img src="iwork_img/km/book.gif" width="19" height="19"></td>
		                <td class="STYLE8">关联文档</td>
		              </tr>
		            </table></td>
		          </tr>
		          <tr>
		            <td height="28" background="iwork_img/km/bg.gif" style="border-top:solid 1px #8db6cf; padding-left:10px; font-size:12px;"><table width="100%" border="0" cellspacing="0" cellpadding="0">
		              <tr>
		                <td width="25" class="STYLE8"><img src="iwork_img/km/work.gif" width="19" height="19"></td>
		                <td class="STYLE8">用户评论</td>
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
			        <td height="22" background="iwork_img/km/bg1.gif"  style="border-bottom:solid 1px #8db6cf; padding-top:1px; padding-bottom:1px;"><table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
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
			        <td style="padding-top:10px;"> 文档正文</td>
		
			  </tr>
			</table>
			</td>
			</tr>
			</table>
			
  </div>
</div>

</body>
</html>
