<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">




<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 
<title>é¡¹ç®èµæè¡¨å</title>
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
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
		//==========================è£è½½å¿«æ·é®===============================//å¿«æ·é®
		jQuery(document).bind('keydown',function (evt){		
			if(evt.ctrlKey&&evt.ShiftKey){
				return false;
			}
			else if(evt.ctrlKey && event.keyCode==83){ //Ctrl+s /ä¿å­æä½
				saveForm(); return false;
			} 
			else if(evt.ctrlKey && event.keyCode==13){  //Ctrl+åè½¦ é¡ºåºåçæä½
					executeHandle(); return false;
			}
		}); //å¿«æ·é®
		
		 	//ç¹å»éä»¶ä¸ä¼ æé®è°ç¨çæ¹æ³ï¼ç¨äºå¼¹åºä¸ä¼ éä»¶çªå£
	function uploadifyDialog(dialogId,fieldName,divId,sizeLimit,multi,fileExt,fileDesc){
		if(dialogId==null||dialogId==""||fieldName==null||fieldName==""||divId==null||divId==""){
			alert('åæ°ä¸æ­£ç¡®');
			return ;
		}
		var url = 'showUploadifyPage.action?parentColId='+fieldName+'&parentDivId='+divId+'&sizeLimit='+sizeLimit+'&multi='+multi+'&fileExt='+fileExt+'&fileDesc='+fileDesc+'';
		art.dialog.open(pageUrl,{
			id:dialogId,
			title: 'ä¸ä¼ éä»¶',
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
									é¶æ®µèµæ
								</td>
								<td class="td_data" id="data_JDZL">
									<div id='DIVJDZL' style='width:100px'>
	<div><input type=hidden size=100 id='JDZL'  class = '{maxlength:1024}'  name='JDZL' value=''/></div>
	<div><button   onclick='showUploadifyPageJDZL("DIVJDZL","JDZL");return false;' >éä»¶ä¸ä¼ </button></div>
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
									é¶æ®µèµæ
								</td>
								<td class="td_data" id="data_JDZL1">
									<div id='DIVJDZL1' >
	<div><input type=hidden size=100 id='JDZL1'  class = '{maxlength:1024}'  name='JDZL1' value=''/></div>
	<div><button onclick='showUploadifyPageJDZL("DIVJDZL1","JDZL1");return false;' >éä»¶ä¸ä¼ </button></div>
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
<!-- 新增查询过滤SQL注入关键字 -->
<script language="JavaScript"> 
  jQuery.validator.addMethod("string", function(value, element) {
          var sqlstr=[" and "," exec ", " count ", " chr ", " mid ", " master ", " or ", " truncate ", " char ", " declare ", " join ","insert ", "select ", "delete ", "update ","create ","drop "]
          var patrn=/[`~!#$%^&*+<>?"{},;'[\]]/im;
    	    if(patrn.test(value)){
        	}else{
            	var flag = false;
            	var tmp = value.toLowerCase();
            	for(var i=0;i<sqlstr.length;i++){
                	var str = sqlstr[i];
					if(tmp.indexOf(str)>-1){
						flag = true;
						break;
					}
                }
                if(!flag){
                	return "success";
                }
            }
        }, "包含非法字符!");
</script>