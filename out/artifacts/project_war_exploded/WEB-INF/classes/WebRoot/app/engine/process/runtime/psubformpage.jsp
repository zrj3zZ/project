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
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/subformstyle.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
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
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript" src="iwork_js/engine/subformpage.js" charset="utf-8" ></script>
	 
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
		<div  class="tools_nav">
		<table width="100%"><tr>
			<td align="left">
			<a href="#" onclick='saveForm()' class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>	
			<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
			<a href="javascript:pageClose();" class="easyui-linkbutton" plain="true" iconCls="icon-cancel">关闭</a>
			</td>
			<td style="text-align:right;padding-right:10px">
				<!-- <a href="#" onclick='setMenuLayout()'>布局设置</a>&nbsp;&nbsp; -->
				<s:if test="isShowLog==1&&isLog==1">
					<a href="#" onclick='showlog()'><img src="iwork_img/min_monitor.gif" border="0">修改日志</a>
				</s:if>
			</td>
		</tr></table>
		</div>
	</div>
	<div region="center" style="text-align:center;border-left:1px #999 dotted;border-right:1px #999 dotted;border-top:1px #999 dotted;border-bottom:0px #999 dotted;padding:2px;">
	<form   id="subformMain" name="subformMain" method="post" action='subformSaveItem.action'>
		<!--表单参数-->
		<span style="display:none"> 
			<s:hidden name="modelId"/>
			<s:hidden name="modelType"/> 
			<s:hidden name="actDefId"/>
			<s:hidden name="prcDefId"/> 
			<s:hidden name="actStepDefId"/> 
			<s:hidden name="isLog"/> 
			<s:hidden name="subformid"/>
			<s:hidden name="subformkey"/>
			<s:hidden name="instanceId"/> 
			<s:hidden name="oper"/> 
			<s:hidden name="id"/> 
			<s:hidden name="formContent"/>
			<input name='submitbtn' id='submitbtn' type="submit" />
			<s:property value='param' escapeHtml='false'/>
		</span>
		<s:property value='content' escapeHtml='false'/>
	</form>
	</div>
	
</body>
</html>
 <script language="JavaScript">  
  jQuery.validator.addMethod("string", function(value, element) {
          var sqlstr=[" and "," exec ", " count ", " chr ", " mid ", " master ", " or ", " truncate ", " char ", " declare ", " join ","insert", "select", "delete", "update","create","drop"]
    	  var patrn=/[`~!@#$%^&*()_+<>?:"{},\/;'[\]]/im;  
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
 
