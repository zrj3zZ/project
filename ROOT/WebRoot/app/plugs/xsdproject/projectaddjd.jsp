<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title></title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/bootstrap/easyui.css">
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css"/>
	<link href="iwork_css/reset.css" rel="stylesheet" type="text/css"/> 
	<link href="iwork_css/pformpage.css" rel="stylesheet" type="text/css"/>
	<script type="text/javascript" src="iwork_js/commons.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.src.js"  > </script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=default"></script>
	<script type="text/javascript" src="iwork_js/pformpage.js"></script>
	<script type="text/javascript" src="iwork_js/json.js"></script>
	<script type="text/javascript"> 
/* 	//表单初始化页面
		function formInitEventScript(){
			return true;
		}
		//保存脚本触发器事件
		function formSaveEventScript(){
			return true;
		}
		
		//办理脚本触发器事件
		function formTransEventScript(){
			return true;
		} */
		 //快捷键
		 jQuery(document).bind('keydown',function (evt){		if(evt.ctrlKey&&evt.ShiftKey){
			return false;
		}
		else if(evt.ctrlKey && event.keyCode==83){ //Ctrl+s /保存操作
			saveForm(); return false;
		} 
}); //快捷键
		 $(function () {
var groupid=$("#GROUPID").val();
 $("#URL").attr("src", "xsd_zqb_project_loadXmrw.action?groupid="+groupid);
});	
function changeZl() {
	    $.ajaxSetup({
	        async: false
	    });
	    $("#JDZL").val("");
	    //$("#zlinfo").html("");
	    var obj = document.getElementById('GROUPID');
	    // 定位id
	    var index = obj.selectedIndex;
	    // 选中的文本
	    var text = obj.options[index].text;
	    // 值	    
	    var value = obj.options[index].value;
	    var projectNo = $("#PROJECTNO").val();
	    //改变判断是否存在上一级任务，不存在不可进行该级的保存
	    $.post("xsd_zqb_project_checkrwid.action", {
	        "groupid": value,
	        "projectNo": projectNo
	    }, function (data) {
	        var dataJson = eval("(" + data + ")");
	        var id = dataJson[0].GROUPID;
	        var jdzl = dataJson[0].JDZL;
	        var sxzlmb = dataJson[0].SXZLMB;
	        var uuid = dataJson[0].UUID;
	        var d = parseInt(id);
	        if (d < value && d != 0) {
	            alert("请先添加上一任务阶段！");
	            $("#GROUPID").val(d);
	            $("#JDZL").val(jdzl);
	            $("#labelSXZLMB").append("<div  id=\"eb266039238b422b9922fa3d49ae881f\" style=\"background-color: #F5F5F5;vertical-align:middle;border-bottom: 1px solid #E5E5E5;font: 11px Verdana, Geneva, sans-serif;padding: 5px;width: 200px\"><span><a href=\"uploadifyDownload.action?fileUUID=" + uuid + "\" target=\"_blank\"><img style=\"margin:3px\"/>" + sxzlmb + "</a></span>");
	            return;
	        } else if (d > value && d != 0) {
	            alert("该阶段已存在！");
	            $("#GROUPID").val(d);
	            $("#JDZL").val(jdzl);
	            $("#labelSXZLMB").append("<div  id=\"eb266039238b422b9922fa3d49ae881f\" style=\"background-color: #F5F5F5;vertical-align:middle;border-bottom: 1px solid #E5E5E5;font: 11px Verdana, Geneva, sans-serif;padding: 5px;width: 200px\"><span><a href=\"uploadifyDownload.action?fileUUID=" + uuid + "\" target=\"_blank\"><img style=\"margin:3px\"/>" + sxzlmb + "</a></span>");
	            return;
	        } else {
	            $("#JDZL").val(jdzl);
	            $("#labelSXZLMB").append("<div  id=\"eb266039238b422b9922fa3d49ae881f\" style=\"background-color: #F5F5F5;vertical-align:middle;border-bottom: 1px solid #E5E5E5;font: 11px Verdana, Geneva, sans-serif;padding: 5px;width: 200px\"><span><a href=\"uploadifyDownload.action?fileUUID=" + uuid + "\" target=\"_blank\"><img style=\"margin:3px\"/>" + sxzlmb + "</a></span>");
	        }
	    });
	}
	</script>
	<style> 
		
	</style>
</head>
<body >
	<div id="blockPage" class="black_overlay" style="display:none"></div> 
	<div  border="false" >
		<div class="tools_nav">
				<a  href="#" style="margin-left:1px;margin-right:1px"  onclick='saveForm()' text='Ctrl+s' class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>
				<a href="#" style="margin-left:1px;margin-right:1px"  onclick='location.reload();' class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		</div>
	</div>
	<div  id="fpcontent" style="text-align:center;border-left:1px #999 dotted;border-right:1px #999 dotted;border-top:1px #999 dotted;border-bottom:1px #999 solid;padding:2px;">
	<p id="back-top"> <a href="#top"><span></span></a> </p>
	<form   id="iformMain" name="iformMain" method="post" action='processRuntimeFormSave.action'>
			<table width="100%" border="0" cellpadding="0" cellspacing="0" >
    <tr>
      <td align="right"  class=PY_tag  valign="bottom" nowrap="nowrap" width='100%'>
<UL style="text-align:right" >
<LI  onclick='openSubForm(183)'><DIV class=PY_tagn ><SPAN class=bold><img src='iwork_img/min_form.gif' style='margin-right:5px;' border='0'>项目任务管理流程表单</SPAN></DIV></LI>
<LI  onclick='openMonitorPage();'><DIV class=PY_tagn><SPAN class=bold><img src='iwork_img/min_monitor.gif' style='margin-right:5px;' border='0'>流程跟踪</SPAN></DIV></LI>
        </UL>      </td>
    </tr>
  </table>

		<div>
			<div id="border">
	<table style="margin-bottom:5px;" class="ke-zeroborder" border="0" cellpadding="0" cellspacing="0" width="100%">
		<tbody>
			<tr>
				<td class="formpage_title">
					项目任务管理流程表单
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
					<table class="ke-zeroborder" border="0" cellpadding="0" cellspacing="0" width="100%">
						<tbody>
							<tr id="itemTr_1641">
								<td class="td_title" id="title_GROUPID" width="180">
									<span style="color:red;">*</span>任务阶段
								</td>
								<td class="td_data" id="data_GROUPID">
									<select   name='GROUPID'  id='GROUPID' class=required  ><option value=''>-空-</option>
<option value="53" selected >项目开发</option>
<option value="54">签署协议</option>
<option value="55">股改</option>
<option value="56">尽职调查</option>
<option value="57">申报材料</option>
<option value="58">内核</option>
<option value="59">内核反馈</option>
<option value="60">申报</option>
<option value="61">申报反馈</option>
<option value="62">持续督导</option>
</select><font color=red>*</font>&nbsp;
								</td>
							</tr>
							<tr id="itemTr_1640">
								<td class="td_title" id="title_MANAGER" width="180">
									<span style="color:red;">*</span>任务负责人
								</td>
								<td class="td_data" id="data_MANAGER">
									<input type='text' class = '{maxlength:128,required:true}'   style="width:100px"  name='MANAGER' id='MANAGER'  value='LH[刘辉]' ><a href="javascript:openDictionary('dc45c80ce13f42a0bdf149796c7b8bbe');"  style="margin-left:5px;" class="easyui-linkbutton" plain="true" iconCls="icon-dictionary"></a><font color=red>*</font>&nbsp;
								</td>
							</tr>
							<tr id="itemTr_1647">
								<td class="td_title" id="title_SSJE" width="180">
									实收金额
								</td>
								<td class="td_data" id="data_SSJE">
									<input type='text' class = {maxlength:11,number:true,required:false} onchange="check();" style="width:100px"  name='SSJE' id='SSJE'  value='' >&nbsp;
								</td>
							</tr>
							<tr id="itemTr_1648">
								<td class="td_title" id="title_JDZL" width="180">
									资料说明
								</td>
								<td class="td_data" id="data_JDZL">
									<textarea  class="{maxlength:512,required:false} "  name='JDZL' id='JDZL'  style="width:200px;height:50px;"  ></textarea>&nbsp;
								</td>
							</tr>
							<tr id="itemTr_1648">
								<td colspan="2">
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
								</td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
		</tbody>
	</table>
</div>
<div style="display:none;">
	<input type='hidden' name='TASK_NAME'  id='TASK_NAME'  value='' ><input type='hidden' name='STARTDATE'  id='STARTDATE'  value='' ><input type='hidden'onchange="checkRQ()" name='ENDDATE'  id='ENDDATE'  value='' ><input type='hidden' name='SCALE'  id='SCALE'  value='' ><input type='hidden' name='HTJE'  id='HTJE'  value='' ><input type='hidden' name='SXZLMB'  id='SXZLMB'  value='' ><input type='hidden' name='ATTACH'  id='ATTACH'  value='' ><input type='hidden' name='MEMO'  id='MEMO'  value='' ><input type='hidden' name='PROJECTNO'  id='PROJECTNO'  value='PM2015-04-1143' ><input type='hidden' name='PROJECTNAME'  id='PROJECTNAME'  value='测试' ><input type='hidden' name='ORDERINDEX'  id='ORDERINDEX'  value='1258' ><input type='hidden' name='PRIORITY'  id='PRIORITY'  value='' ><input type='hidden' name='GXSJ'  id='GXSJ'  value='2015-04-07 15:51:02' >
</div>
		</div> 
		<div style="width:880px;margin-left:auto;margin-right:auto">
			
		</div>
		<!--表单参数-->
		<span style="display:none">
			<input type="hidden" name="modelId" value="30" id="modelId"/>
			<input type="hidden" name="modelType" value="PROCESS" id="modelType"/> 
			<input type="hidden" name="taskType" value="0" id="taskType"/> 
			<input type="hidden" name="formIsModify" value="1" id="formIsModify"/> 
			<input type="hidden" name="isLog" value="0" id="isLog"/>
			<input type="hidden" name="actDefId" value="XMRWLC:1:73504" id="actDefId"/>
			<input type="hidden" name="prcDefId" value="30" id="prcDefId"/>
			<input type="hidden" name="actStepDefId" value="usertask18" id="actStepDefId"/>
			<input type="hidden" name="formId" value="137" id="formId"/>
			<input type="hidden" name="taskId" value="0" id="taskId"/>
			<input type="hidden" name="instanceId" value="0" id="instanceId"/>
			<input type="hidden" name="excutionId" value="0" id="excutionId"/>
			<input type="hidden" name="dataid" value="0" id="dataid"/>
			<input name='submitbtn' id='submitbtn' type="submit" />
		</span>
	</form>
	</div>
	<!-- 办理菜单 -->
		
		
	<!-- 操作窗口 -->
	<!--添加分类窗口-->
		<div id="formWinDiv"  style="display:none">
	    <div id="formwindow" class="easyui-window"  modal="true" closed="true" collapsible="false" minimizable="true"
	        maximizable="false" icon="icon-save"  style="width: 500px; height: 400px; padding: 5px;
	        background: #fafafa;">
	        	<iframe id="formInfoFrame"  name="formInfoFrame" width="473" style="border:1px solid #ccc;padding:3px;" height="330" frameborder=0  scrolling=auto  marginheight=0 marginwidth=0 border="0" ></iframe>
	        </div>
    	</div>
</body>
</html>

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