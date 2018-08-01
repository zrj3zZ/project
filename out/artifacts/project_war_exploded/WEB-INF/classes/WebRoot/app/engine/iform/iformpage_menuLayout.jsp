<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title><s:property value='title' escapeHtml='false'/></title>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css"/>
<link rel="stylesheet" type="text/css" href="../iwork_css/public.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css"/>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>

<script type="text/javascript" src="iwork_js/engine/iformpage_menulayout.js"  ></script>
</head>
<body onload='checkMenuLayoutUpdated()'  class="easyui-layout">
<div region="center" style="text-align:center;border-left:1px #999 dotted;border-right:1px #999 dotted;border-top:1px #999 dotted;border-bottom:0px #999 dotted;padding:2px;">
	<form   id="menuLayoutEditForm" name="menuLayoutEditForm" method="post" action='menuLayoutEdit.action'>
		<div>
		<table width=100%>
			
			<tr align='right'>
				<td colspan='4'>&nbsp;</td>
			</tr>
			<tr align='right'>
				<td colspan='4'>&nbsp;</td>
			</tr>
			<tr align='center'>
				<td width='25%'>
				<label>
					<img src='../iwork_img/layout_Top.gif' width='60' height='60'/><br/>
					<s:if test="menuLayoutType.equals('Top')">
						<input type=radio  id='menuLayoutType' name='menuLayoutType' value='Top' checked/>页面顶部
					</s:if>
					<s:else>
						<input type=radio  id='menuLayoutType' name='menuLayoutType' value='Top'/>页面顶部
					</s:else>
					</label>
				</td>
				
				<td width='25%'>
				<label>
					<img src='../iwork_img/layout_Right.gif' width='60' height='60'/><br/>
					<s:if test="menuLayoutType.equals('Right')">
					<input type=radio  id='menuLayoutType' name='menuLayoutType' value='Right' checked/>页面右部
					</s:if>
					<s:else>
						<input type=radio  id='menuLayoutType' name='menuLayoutType' value='Right'/>页面右部
					</s:else>
					</label>
				</td>
				<td width='25%'>
				<label>
					<img src='../iwork_img/layout_Left.gif' width='60' height='60'/><br/>
					<s:if test="menuLayoutType.equals('Left')">
					<input type=radio  id='menuLayoutType' name='menuLayoutType' value='Left' checked/>页面左部
					</s:if>
					<s:else>
						<input type=radio  id='menuLayoutType' name='menuLayoutType' value='Left'/>页面左部
					</s:else>
					</label>
				</td>
				<td width='25%'>
				<label>
					<img src='../iwork_img/layout_Bottom.gif' width='60' height='60'/><br/>
					<s:if test="menuLayoutType.equals('Bottom')">
					<input type=radio  id='menuLayoutType' name='menuLayoutType' value='Bottom' checked/>页面底部
					</s:if>
					<s:else>
						<input type=radio  id='menuLayoutType' name='menuLayoutType' value='Bottom'/>页面底部
					</s:else>
					</label>
				</td>
			</tr>
			
		</table>
		</div>
		<input type=hidden id='menuLayoutUpdateFlag' value="<s:property  value='menuLayoutUpdateFlag'/>"/>
	</form>
	</div>
	<div region="south" border="false" style="border-top:1px solid #efefef;padding:5px;">
	<table width="100%">
		 <tr align='right'>
				<td>
					<a class="easyui-linkbutton" plain="false" iconCls="icon-ok"  href="javascript:menuLayoutEdit();" >确定</a>
                    <a class="easyui-linkbutton" plain="false" iconCls="icon-cancel" href="javascript:closeWin()">取消</a>
                </td>
			</tr>
	</table>
	</div>
</body>
</html>
 
