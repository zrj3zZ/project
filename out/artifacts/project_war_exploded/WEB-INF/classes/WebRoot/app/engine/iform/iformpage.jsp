<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title><s:property value='title' escapeHtml='false'/></title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/easyui/themes/gray/easyui.css"/> 
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/commons.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.src.js"  > </script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
		 <s:property value='script' escapeHtml='false'/>
	</script>
	
	<script type="text/javascript" src="iwork_js/engine/openformpage.js"  ></script>
	
	<style> 
		<s:property value="style" escapeHtml="false"/>
	</style>
	
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/openformpage.css"/>
	
</head>
<body class="easyui-layout">
	<div id="blockPage" class="black_overlay" style="display:none"></div> 
	<div region="north" border="false" style="height:30px;background:#EFEFEF;padding-top:2px;text-align:center;padding-left:10px;">
	</div>
	<div region="east" split="false" title="工具栏" style="width:220px;padding:0px;border:0px;">
		<div id="iforminfo">
							<div id="infotitle">基本操作<font color="red"><s:property value='info' escapeHtml='false'/></font></div>
								<div class="iformContent">
									<a href="#" onclick='saveForm()' class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>
									<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
									<a href="javascript:remove();" class="easyui-linkbutton" plain="true" iconCls="icon-print">打印</a>
									<a href="javascript:pageClose();" class="easyui-linkbutton" plain="true" iconCls="icon-cancel">关闭</a>
								</div>
							</div>
						<div id="infotitle">维护日志</div> 
							<div class="iformContent">
							</div>
	</div> 
	
	<div region="south"  style="height:15px;background:#A9FACD;border:0px;">
	</div>
	<div region="center" style="text-align:center;border-left:1px #999 dotted;border-right:1px #999 dotted;border-top:1px #999 dotted;border-bottom:0px #999 dotted;padding:2px;">
	<form   id="iformMain" name="iformMain" method="post" action='saveIform.action'>
		<!--表单参数-->
		<span style="display:none">
			<s:hidden name="modelId"/>
			<s:hidden name="modelType"/> 
			<s:hidden name="isLog"/> 
			<s:hidden name="formid"/>
			<s:hidden name="instanceId"/> 
			<s:hidden name="dataid"/> 
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
     window.opener.location.href=window.opener.location.href;  
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
