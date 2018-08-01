<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page contentType="text/html; charset=utf-8"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><s:property value='title' escapeHtml='false'/></title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/easyui/themes/gray/easyui.css"/>
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css"/>
	<script type="text/javascript" src="iwork_js/commons.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"   charset="utf-8" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.src.js"  charset="utf-8"  > </script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"  charset="utf-8"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"  charset="utf-8" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"  charset="utf-8"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true"  ></script>
	<script type="text/javascript" src="iwork_js/engine/iformpage.js"  charset="utf-8" ></script>
	 
	<script type="text/javascript">
		//==========================装载快捷键===============================//快捷键
		jQuery(document).bind('keydown',function (evt){		
			if(evt.ctrlKey&&evt.ShiftKey){
				return false;
			}
			else if(evt.ctrlKey && event.keyCode==83){ //Ctrl+s /保存操作
				saveForm(); return false;
			} 
			else if(evt.ctrlKey && event.keyCode==13){  //Ctrl+回车 顺序办理操作
					executeHandle(); return false;
			}
		}); //快捷键
		
		 <s:property value='script' escapeHtml='false'/>
	</script>
	
	<style> 
		<s:property value="style" escapeHtml="false"/>
	</style>
	
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css"/>
	
</head>
<body class="easyui-layout">
	<div id="blockPage" class="black_overlay" style="display:none"></div> 
	<div region="north" style="height:40px;" border="false" >
		
	</div>
	<div region="center" style="text-align:center;border-left:1px #999 dotted;border-right:1px #999 dotted;border-top:1px #999 dotted;border-bottom:0px #999 dotted;padding:2px;">
	<form   id="iformMain" name="iformMain" method="post" action='saveIform.action'>
		<!--表单参数-->
		
		<div>
	<table style="margin-bottom:5px;" class="ke-zeroborder" border="0" cellspacing="0" cellpadding="0" width="100%">
		<tbody>
			<tr>
				<td align="left">
					<table class="ke-zeroborder" border="0" cellspacing="0" cellpadding="0" width="100%">
						<tbody>
							<tr id="itemTr_1230">
								<td id="title_MEETNAME" class="td_title" width="180">
									<span style="color:red;">*</span>会议名称
								</td>
								<td id="data_MEETNAME" class="td_data">
									 <s:property value="hash.MEETNAME" escapeHtml="false"/> 
								</td>
							</tr>
							<tr id="itemTr_1179">
								<td id="title_PLANTIME" class="td_title" width="180">
									<span style="color:red;">*</span>召开时间
								</td>
								<td id="data_PLANTIME" class="td_data">
                                       <s:date name="hash.PLANTIME" format="yyyy-MM-dd HH:mm:ss"/>
								</td>
							</tr>
							<tr id="itemTr_1233">
							<td id="title_PLANTIME" class="td_title" width="180">
									<span style="color:red;">*</span>议案资料上传
								</td>
								<td id="data_PLANTIME" class="td_data">
									 <div class="fileListArea">
								     	<s:property value="fileHtml" escapeHtml="false"/>
								     </div>
								</td>
							
								
							</tr>
							<tr id="itemTr_1235">
							<td id="title_PLANTIME" class="td_title" width="180">
									<span style="color:red;">*</span>其他资料
								</td>
								<td id="data_PLANTIME" class="td_data">
									 <div class="fileListArea">
								     	<s:property value="html" escapeHtml="false"/>
								     </div>
								</td>
							
								
							</tr>
							<%-- <tr id="itemTr_1181">
							<td id="title_HYDD" class="td_title" width="180">
									会议地点
								</td>
								<td id="data_HYDD" class="td_data">
									 <div class="fileListArea">
								     	 <s:property value="hash.HYDD" escapeHtml="false"/> 
								     </div>
								</td>	
							</tr> --%>
							
						
							
						</tbody>
					</table>
				</td>
			</tr>
		</tbody>
	</table>
</div>
<div style="display:none;">
	<input type=radio   id='STATUS0' name='STATUS' value='未召开' checked="checked"><label  id="lbl_STATUS0"  for="STATUS0">未召开</label>&nbsp;
<input type=radio  id='STATUS1' name='STATUS' value='已召开' ><label  id="lbl_STATUS1" for="STATUS1">已召开</label>&nbsp;
<input type='hidden' name='JHCJR'  id='JHCJR'  value='A01' ><input type='hidden' name='JHCJSJ'  id='JHCJSJ'  value='2014-12-02' ><input type='hidden' name='CUSTOMERNO'  id='CUSTOMERNO'  value='CNO2014-11-561' ><input type='hidden' name='CUSTOMERNAME'  id='CUSTOMERNAME'  value='A公司' >
</div>
	</form>
	</div>
	
</body>

</html>
 
