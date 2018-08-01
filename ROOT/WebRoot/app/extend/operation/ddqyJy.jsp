<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>解约</title>
    
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

	<link rel="stylesheet" type="text/css" href="iwork_css/common.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/easyui/themes/gray/easyui.css"/> 
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css"/>
		<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/commons.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"   charset="utf-8" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"  charset="utf-8"  > </script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"  charset="utf-8"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"  charset="utf-8" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"  charset="utf-8"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript" src="iwork_js/engine/iformpage.js"  charset="utf-8" ></script>
<script type="text/javascript">
	function savejy(){
		//$("#jySave").submit();
		var jy=document.getElementById("jyyy").value;
		if(jy==null ||jy==""){
			alert("请输入解约原因！");
			
		}else{
			$.ajax({
				url : "jySave.action",
				type: "POST",
				data:$("#jySave").serialize(),// 你的formid
				dataType : "json",
				success : function(data) {
					alert("保存成功！");
					window.close();
				}
			});
		}
		
	}
</script>

  </head>
  <link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css"/>
  
  <body>
   <div id="blockPage" class="black_overlay" style="display:none"></div> 
	
		<div region="north" style="height:40px;" border="false" >
			<div  class="tools_nav">
			<span>
				<a href="javascript:void(0);" id="savebtn" onclick="savejy();" class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>	
				<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
				<!-- <a href="javascript:remove();" class="easyui-linkbutton" plain="true" iconCls="icon-print">打印</a> -->
				<a id="close" href="javascript:pageClose();" class="easyui-linkbutton" plain="true" iconCls="icon-cancel">关闭</a>
				</span> 
				<span style="float:right">
					<!-- <a href="#" onclick='setMenuLayout()'>布局设置</a>&nbsp;&nbsp; -->
					
				</span>
			</div>
		</div>
<div id="border">
	<table style="margin-bottom:5px;" class="ke-zeroborder" width="100%" cellspacing="0" cellpadding="0" border="0">
		<tbody>
			<tr>
				<td class="formpage_title">
					解约原因
				</td>
			</tr>
			<tr>
				<td id="help" align="right">
					<br />
				</td>
			</tr>
			<tr>
				<td class="line" align="right">
					<br />
				</td>
			</tr>
			<tr>
				<td align="left">
				<form name='jySave' id='jySave' method="post" action="jySave.action">
					<table class="ke-zeroborder" width="100%" cellspacing="0" cellpadding="0" border="0">
						<tbody>
							
							<tr id="itemTr_3213">
								<td class="td_title" id="title_DZJE" width="180">
									<span style="color:red;">*</span>原因
								</td>
								<td class="td_data" id="data_DZJE">
									<textarea  name="jyyy" id="jyyy" style="width:400px;height:120px;" maxlength="516" ></textarea>
									<input id="rzid" name="rzid" type="hidden"  value="<s:property value="rzid"/>" />
								</td>
							</tr>
							
						</tbody>
					</table>
					</form>
				</td>
			</tr>
		</tbody>
	</table>
</div>


  </body>
</html>
