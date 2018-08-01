<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">




<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 
<title>项目资料表单</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/easyui/themes/gray/easyui.css"/> 
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/autocomplete/jquery.autocomplete.css"/>
	<script type="text/javascript" src="iwork_js/commons.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"   charset="utf-8" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"  charset="utf-8"  > </script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"  charset="utf-8"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"  charset="utf-8" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/autocomplete/jquery.autocomplete.min.js"  charset="utf-8" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"  charset="utf-8"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=default"  ></script>
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
		
		 	//点击附件上传按钮调用的方法，用于弹出上传附件窗口
	function uploadifyDialog(dialogId,fieldName,divId,sizeLimit,multi,fileExt,fileDesc){
		if(dialogId==null||dialogId==""||fieldName==null||fieldName==""||divId==null||divId==""){
			alert('参数不正确');
			return ;
		}
		$.dialog({
			id:dialogId,
			title: '上传附件',
			content: 'url:showUploadifyPage.action?parentColId='+fieldName+'&parentDivId='+divId+'&sizeLimit='+sizeLimit+'&multi='+multi+'&fileExt='+fileExt+'&fileDesc='+fileDesc+'',
			pading: 0,
			lock: true,
			width: 500,
			height: 500, 
			close:function(){
				this.focus();
			}
		}); 
		return ;
	}
	</script>
	
	<style> 
		
	</style>
	
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css"/>
	
</head>
<body>
	
	<div region="center" >
	<div class="form-wrapper"  >
	<form   id="iformMain" name="iformMain" method="post" action='saveIform.action'>
		
		<div >
	<table width="100%" class="ke-zeroborder" style="margin-bottom:5px;" border="0" cellspacing="0" cellpadding="0">
		<tbody>
		
			<tr>
				<td align="left">
					<table width="100%" class="ke-zeroborder" border="0" cellspacing="0" cellpadding="0">
						<tbody>
							<tr id="itemTr_1850">
								<td width="180" class="td_title" id="title_JDZL">
									阶段资料
								</td>
								<td class="td_data" id="data_JDZL">
									<div id='DIVJDZL' style='width:100px'>
	<div><input type=hidden size=100 id='JDZL'  class = '{maxlength:1024}'  name='JDZL' value=''/></div>
	<div><button   onclick='showUploadifyPageJDZL("DIVJDZL","JDZL");return false;' >附件上传</button></div>
</div>
	<script>
		function showUploadifyPageJDZL(divjdzl,jdzl){
			mainFormAlertFlag=false;
			saveSubReportFlag=false;
			var valid = mainFormValidator.form();
			if(!valid){
				return false;
			}
			mainFormAlertFlag=false;
			saveSubReportFlag=false;
			uploadifyDialog(jdzl,jdzl,divjdzl,'','true','','');
		}
	</script>
&nbsp;
								</td>
							</tr>
						<tr id="itemTr_1851">
								<td width="180" class="td_title" id="title_JDZL1">
									阶段资料
								</td>
								<td class="td_data" id="data_JDZL1">
									<div id='DIVJDZL1' >
	<div><input type=hidden size=100 id='JDZL1'  class = '{maxlength:1024}'  name='JDZL1' value=''/></div>
	<div><button onclick='showUploadifyPageJDZL("DIVJDZL1","JDZL1");return false;' >附件上传</button></div>
</div>
	
&nbsp;
								</td>
							</tr>		
						</tbody>
					</table>
				</td>
			</tr>
		</tbody>
	</table>
</div>
	</form>
	</div>
	</div>
</body>
</html>
 
