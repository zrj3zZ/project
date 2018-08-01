<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head><title>个人信息管理</title>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css"/>
<link rel="stylesheet" type="text/css" href="../iwork_css/public.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css"/>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>
<link href="iwork_css/base-090628.css" rel="stylesheet" type="text/css" />
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link href="iwork_css/reset.css" rel="stylesheet" type="text/css"/>
<script language="javascript" src="iwork_js/commons.js"></script>

<link href="iwork_css/system/persion_menuLayout.css" rel="stylesheet" type="text/css"/>

</head>	
<body class="opts" onload='checkMenuLayoutUpdated()'> 
<form name="menuLayoutEditForm"  action="persion_menuLayout.action"  method="post"  target='_self'>

<table width="100%" border="0" cellpadding="0" cellspacing="0" >
    <tr>
      <td rowspan="2" align="right" width="1%" style='padding-left:10px;padding-top:5px;'><img src="iwork_img/changepass.gif"></td>
      <td width="80%" align="left" valign="bottom" style='padding-top:5px;'><span style='font-size:18px;font-family:黑体'>修改菜单布局</span></td>
    </tr>
    <tr>
      <td><span style='color:gray;font-size:10px;font-family:Arial'>SystemConfigCenter</span></td>
    </tr>
	 <tr>
      <td colspan="3" align="center" style='padding-bottom:3px;'>
	  	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		  <tr>
			<td style="height:2px; border-top:3px #D2D2D2 solid;">&nbsp;</td>
		  </tr>
		</table> 
	  </td>
    </tr>
    <tr>
    	<td colspan="3">
    	<div class=mrgn>
    	<table width='100%' cellpadding="0" cellspacing="0">
<tr>
	<td >
		<table  width=100%>
			<tr align='center'>
				<td width='25%'>
					<img src='../iwork_img/layout_Top.gif' width='60' height='60'/><br/>
					<s:if test="menuLayoutType.equals('Top')">
						<input type=radio  id='menuLayoutType' name='menuLayoutType' value='Top' checked/>页面顶部
					</s:if>
					<s:else>
						<input type=radio  id='menuLayoutType' name='menuLayoutType' value='Top'/>页面顶部
					</s:else>
				</td>
				<td width='25%'>
					<img src='../iwork_img/layout_Bottom.gif' width='60' height='60'/><br/>
					<s:if test="menuLayoutType.equals('Bottom')">
					<input type=radio  id='menuLayoutType' name='menuLayoutType' value='Bottom' checked/>页面底部
					</s:if>
					<s:else>
						<input type=radio  id='menuLayoutType' name='menuLayoutType' value='Bottom'/>页面底部
					</s:else>
				</td>
				<td width='25%'>
					<img src='../iwork_img/layout_Right.gif' width='60' height='60'/><br/>
					<s:if test="menuLayoutType.equals('Right')">
					<input type=radio  id='menuLayoutType' name='menuLayoutType' value='Right' checked/>页面右部
					</s:if>
					<s:else>
						<input type=radio  id='menuLayoutType' name='menuLayoutType' value='Right'/>页面右部
					</s:else>
				</td>
				<td width='25%'>
					<img src='../iwork_img/layout_Left.gif' width='60' height='60'/><br/>
					<s:if test="menuLayoutType.equals('Left')">
					<input type=radio  id='menuLayoutType' name='menuLayoutType' value='Left' checked/>页面左部
					</s:if>
					<s:else>
						<input type=radio  id='menuLayoutType' name='menuLayoutType' value='Left'/>页面左部
					</s:else>
				</td>
			</tr>
			 <tr align=center>
		   		<td colspan=4>&nbsp;</td>
		    </tr>
		    <tr align=center>
		   		<td colspan=4>&nbsp;</td>
		    </tr>
		    <tr align=center>
		   		<td colspan=3>&nbsp;</td>
		    	<td ><a class="easyui-linkbutton" iconCls="icon-ok"  href="javascript:menuLayoutEdit();" >确定</a></td>
		    </tr>
		
		</table>
	</td>
</tr>
</table>
</div>
		</td>
    </tr>
  </table>
  <input type=hidden id='menuLayoutUpdateFlag' value="<s:property  value='menuLayoutUpdateFlag'/>"/>
 
 <script type="text/javascript" src="iwork_js/system/persion_menulayout.js"  ></script>
 
</form>
</body>	
</html>
