<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head><title>IWORK综合应用管理系统</title>

<link rel="stylesheet" type="text/css" href="iwork_css/system/syspersion_loadotherparampage.css">

</head>	
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link href="iwork_css/reset.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" href="iwork_css/demos.css">
<link rel="stylesheet" href="iwork_css/jquerycss/ui/base/jquery.ui.all.css">
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/ui/jquery.ui.core.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/ui/jquery.ui.widget.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/ui/jquery.ui.mouse.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/ui/jquery.ui.slider.js"></script>
<script type="text/javascript" src="iwork_js/system/syspersion_loadotherparampage.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	
<script type="text/javascript">
//Slider
	$(function() {
		$('#listFormLineSet').attr("readonly","readonly")
		$('#reportFormLineSet').attr("readonly","readonly")
		$('#windowTimeOut').attr("readonly","readonly")
		$( "#slider-listFormLineSet" ).slider({
			range: "max",
			min: 10,
			max: 100,
			step: 10,
			value: <s:property value="listFormLineSet" escapeHtml="false"/>,
			slide: function( event, ui ) {
				$( "#listFormLineSet" ).val( ui.value );
			}
		});
		$( "#listFormLineSet" ).val( $( "#slider-listFormLineSet" ).slider( "value" ) );
		
		$( "#slider-reportFormLineSet" ).slider({
			range: "max",
			min: 10,
			max: 100,
			step: 10,
			value: <s:property value="reportFormLineSet" escapeHtml="false"/>,
			slide: function( event, ui ) {
				$( "#reportFormLineSet" ).val( ui.value );
			}
		});
		$( "#reportFormLineSet" ).val( $( "#slider-reportFormLineSet" ).slider( "value" ) );
		
		$( "#slider-windowTimeOut" ).slider({
			range: "max",
			min: 1,
			max: 100,
			value: <s:property value="windowTimeOut" escapeHtml="false"/>,
			slide: function( event, ui ) {
				$( "#windowTimeOut" ).val( ui.value );
			}
		});
		$( "#windowTimeOut" ).val( $( "#slider-windowTimeOut" ).slider( "value" ) );
	});
</script>
<body>
<s:form id ="editForm" name="editForm" action="syspersion_saveOtherParamPage"  theme="simple">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" >
	<tr>
	<td>
     <div  class="tools_nav">
			  		<a id="btnEp" class="easyui-linkbutton" plain="true" icon="icon-save" href="javascript:doSubmit();" >保存</a>
			  		<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		  		</div>
	  </td>
    </tr>
    <tr>
      <td align="center" valign="top" height="100%" >
		<div id="editInfo" style="width:80%;">
			<table width="100%">
				<tr>
					<td class='td_title' width="10%">列表显示行设置:</td>
					<td width="4%"><s:textfield id="listFormLineSet" name="listFormLineSet" cssStyle="width:30px;"/></td>
					<td width="86%">行&nbsp;</td>
				</tr>
				<tr>
					<td colspan="3" width="100%" style="padding-bottom: 30px;padding-top:5px;padding-left: 80px;"><div id="slider-listFormLineSet"></div></td>
				</tr>
				<tr>
					<td class='td_title' width="10%">报表显示行设置:</td>
					<td width="4%"><s:textfield id="reportFormLineSet" name="reportFormLineSet" cssStyle="width:30px;"/></td>
					<td width="86%">行&nbsp;</td>
				</tr>
				<tr>
					<td colspan="3" width="100%" style="padding-bottom: 30px;padding-top:5px;padding-left: 80px;"><div id="slider-reportFormLineSet"></div></td>
				</tr>
				<tr>
					<td class='td_title' width="10%">窗口失效时长设置:</td>
					<td width="4%"><s:textfield id="windowTimeOut" name="windowTimeOut" cssStyle="width:30px;"/></td>
					<td width="86%">分钟&nbsp;</td>
				</tr>
				<tr>
					<td colspan="3" width="100%" style="padding-bottom: 30px;padding-top:5px;padding-left: 80px;"><div id="slider-windowTimeOut"></div></td>
				</tr>
				<tr>
					<td>
						<div style="display:none">
							<s:textfield name="configID_1"/> 
							<s:textfield name="configID_2"/> 
							<s:textfield name="configID_3"/> 
						</div>
					</td>
				</tr>
			</table>
		</div>
     </td>
    </tr>
	</table>
</s:form>
</body>	
</html>
