<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=8" >
<title><s:property value='title' escapeHtml='false'/></title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/bootstrap/easyui.css">
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/autocomplete/jquery.autocomplete.css"/>
	<link href="iwork_css/reset.css" rel="stylesheet" type="text/css"/>
	<link href="iwork_css/pformpage.css" rel="stylesheet" type="text/css"/> 
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css" /> 
	<link rel="stylesheet" href="iwork_js/kindeditor/themes/simple/simple.css" />
	<link rel="stylesheet" href="iwork_js/kindeditor/plugins/code/prettify.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/slide.css"/>
	<script charset="utf-8" src="iwork_js/kindeditor/kindeditor.js"></script>
	<script charset="utf-8" src="iwork_js/kindeditor/lang/zh_CN.js"></script>
	<script charset="utf-8" src="iwork_js/kindeditor/plugins/code/prettify.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js"   ></script>
	<script type="text/javascript" src="iwork_js/webuploader.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"  > </script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/autocomplete/jquery.autocomplete.min.js"  charset="utf-8" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript" src="iwork_js/pformpage.js"></script>
	<script type="text/javascript" src="iwork_js/json.js"></script>
	<script type="text/javascript" src="iwork_js/autolink.js"  charset="utf-8" ></script>
	<style>
	.readonlyTxt{
		color:red;
	}
	</style>
	<script type="text/javascript"> 
		//表单初始化页面
		window.onload=function(){
		 var jdmc=document.getElementById("jdmc")==null?"":document.getElementById("jdmc").value;
		
		
		}
		function formInitEventScript(){
			<s:property value='initScriptEvent' escapeHtml='false'/>
		}
		//保存脚本触发器事件
		function formSaveEventScript(){
			<s:property value='saveScriptEvent' escapeHtml='false'/>
		}
		//办理脚本触发器事件
		function formTransEventScript(){
			<s:property value='transScriptEvent' escapeHtml='false'/>
		}
		function sumField(val, opts, rowdata){
				alert(rowdata);
		}
		window.onbeforeunload = function() {
			if(is_form_changed()) {
			}
			}
			function is_form_changed() {
			var t_save = jQuery("#savebtn"); //检测页面是否要保存按钮

			if(t_save.length>0) { //检测到保存按钮,继续检测元素是否修改
			var is_changed = false;
			jQuery("#border input, #border textarea, #border select").each(function() {
			var _v = jQuery(this).attr('_value');
			if(typeof(_v) == 'undefined') _v = '';
			if(_v != jQuery(this).val()) is_changed = true;
			});
			return is_changed;
			}
			return false;
			}
			jQuery(document).ready(function(){
			jQuery("#border input, #border textarea, #border select").each(function() {
			jQuery(this).attr('_value', jQuery(this).val());
			});
			}); 
		 <s:property value='script' escapeHtml='false'/>
		 //==========================装载快捷键===============================//快捷键
		 <s:property value='processShortcutsButton' escapeHtml='false'/>
		 /* 
		 
			function webUploadifyDialog(dialogId,fieldName,divId,sizeLimit,multi,fileExt,fileDesc){
				
				var pageUrl = 'showWebUploaderPage.action?test=1';
				
				art.dialog.open(pageUrl,{
					id:dialogId,
					title: '上传附件',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:650,
					height:510
				}); 
				return ;
			} */
	</script>
	
	<style>
        .opinion_title{
       	 	background:url(iwork_img/engine/tools_nav_bg.jpg) repeat-x;
        	font-size:12px;
        	padding:2px;
        	padding-left:15px;
        	padding-right:3px;
        	background-color:#fff;
        	width:160px;
        	height:20px;
        	color:#333;
        	text-align:left;
        	font-family:黑体;
        	border-right:1px solid #efefef;
        	border-bottom:1px solid #efefef;
        }
        .opinion_step{
        	font-size:12px;
        	padding:2px;
        	padding-left:15px;
        	padding-right:3px;
        	background-color:#fff;
        	width:160px;
        	height:30px;
        	color:#333;
        	text-align:left;
        	font-family:黑体;
        	border-right:1px solid #efefef;
        	border-bottom:1px solid #efefef;
        }
        .opinion_content{
        	padding:2px;
        	padding-left:15px;
        	padding-right:3px;
        	font-size:14px;
        	color:red;
        	width:300px;
        	text-align:left;
        	border-bottom:1px solid #efefef;
        	border-right:1px solid #efefef;
        }
        /*td div{margin-bottom:-20px;}xlj update 2018年6月12日19:07:47 因为流程页面显示多行输入内容时总是位置偏上，故更改*/
		<s:property value="style" escapeHtml="false"/>
	</style>
</head>
<body >
	<div id="blockPage" class="black_overlay" style="display:none"></div> 
	<div  border="false" >
		<div class="tools_nav">
				<s:property value='processButton' escapeHtml='false'/>
				<s:property value='processExtendButton' escapeHtml='false'/>
		</div>
	</div>
	<div  id="fpcontent" style="text-align:center;border-left:1px #999 dotted;border-right:1px #999 dotted;border-top:1px #999 dotted;border-bottom:1px #999 solid;padding:2px;">
<!-- 	<a href="javascript:webUploadifyDialog();" class="easyui-linkbutton" plain="true" iconCls="icon-add">导出</a>
 -->	<p id="back-top"> <a href="#top"><span></span></a> </p>
	<form   id="iformMain" name="iformMain" method="post" action='processRuntimeFormSave.action' >
			<s:property value='pageTab' escapeHtml='false'/>
		<div>
		<!--startprint1-->
			<s:property value='content' escapeHtml='false' /> 
		<!--endprint1-->
		</div>
		<!--表单参数-->
		<span style="display:none">
			<s:hidden name="modelId"/>
			<s:hidden name="modelType"/> 
			<s:hidden name="taskType"/> 
			<s:hidden name="formIsModify"/>
			<s:hidden name="isLog"/> 
			<s:hidden id ="actDefId" name="actDefId"/>
			<s:hidden  id ="prcDefId" name="prcDefId"/>
			<s:hidden id ="actStepDefId" name="actStepDefId"/>
			<s:hidden id ="XMJD" name="XMJD"/>
			<s:hidden id ="JDMC" name="JDMC"/>
			<s:hidden id ="formId" name="formId"/>
			<s:hidden id ="taskId" name="taskId"/>
			<s:hidden id ="instanceId" name="instanceId"/>
			<s:hidden id ="excutionId" name="excutionId"/>
			<s:hidden id ="dataid" name="dataid"/>
			<input name='submitbtn' id='submitbtn' type="submit" />
		</span> 
	</form>
	<div style="width:880px;margin-left:auto;margin-right:auto">
			<s:property value='opinionList' escapeHtml='false'/>
	</div>
	
	</div>
	<!-- 办理菜单 -->
		<s:property value='processTransButton' escapeHtml='false'/>
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
	  //xlj 去掉_，因为流程节点命名step_
          var sqlstr=[" and "," exec ", " count ", " chr ", " mid ", " master ", " or ", " truncate ", " char ", " declare ", " join ","insert", "select", "delete", "update","create","drop"]
    	  var patrn=/[`~!@#$%^&*()+<>?"{},\/;']/im;  
    	    if(patrn.test(value)){
        	}else{
            	var flag = false;
            	var tmp = value.toLowerCase();
            	for(var i=0;i<sqlstr.length;i++){
                	var str = sqlstr[i];
					if(tmp.indexOf(str)>-1){
						flag = true;
						alert("str:"+tmp+"s:"+str);
						break;
					}
                }
                if(!flag){
                	return "success";
                }
            }
        }, "包含非法字符!");
</script> 
