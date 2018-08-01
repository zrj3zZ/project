<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>意见</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/easyui/themes/gray/easyui.css"/> 
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css"/>
	
	
	<script type="text/javascript" src="iwork_js/commons.js"></script>
	
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"   charset="utf-8" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"  charset="utf-8" ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript" src="iwork_js/engine/iformpage.js"  charset="utf-8" ></script>
	<script type="text/javascript">

	function edityj(){
	
	var text=document.getElementById("text").value;
	var id=document.getElementById("id").value;
	
	
	$.ajax({
            	type : "post",
            	url : encodeURI("edityj.action?id="+id+"&text="+text),
           		success:function(data){
           				
           		}
          		});
          		
	
	close();
	
	
	}
	
	
	</script> 
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css"/>
  </head>
  
  <body>
  	<div id="blockPage" class="black_overlay" style="display:none"></div> 
	
		<div region="north" style="height:40px;" border="false" >
			<div  class="tools_nav">
			<span>
				<a href="#" id="savebtn" onclick='edityj()' class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>	
				<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
				<!--<a href="javascript:remove();" class="easyui-linkbutton" plain="true" iconCls="icon-print">打印</a>--> 
				<a id="close" href="javascript:pageClose();" class="easyui-linkbutton" plain="true" iconCls="icon-cancel">关闭</a>
				
				</span> 
				
				
				
			</div>
		</div>
		<div region="center" style="text-align:center;border-left:1px #999 dotted;border-right:1px #999 dotted;border-top:1px #999 dotted;border-bottom:0px #999 dotted;padding:2px;">
	<div class="form-wrapper"  >
	<form   id="iformMain" name="iformMain" method="post" action='#'>
		  意见： <textarea id="text" rows="10" cols="70" <s:if test="orgroleid!=5"> readonly="readonly" </s:if>><s:property value="text"/></textarea>
    <input type="hidden" name="id" id="id" value="<s:property value="id"/>"/>
    
    
	</form>
	
	</div>
	</div>
  
    
  </body>
</html>
