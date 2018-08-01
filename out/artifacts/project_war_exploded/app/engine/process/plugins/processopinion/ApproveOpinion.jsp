<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head> 
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">  
    <title>test</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" charset="gb2312"></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true"></script>
	<script type="text/javascript">
    
	function openWindow(){
	     var param='actDefId=caigoushenqing:2:13510&proDefId=1&actStepId=节点ID&actStepName=节点名称&instanceid=1&taskid=1&excutionid=1';
	     var html='<iframe name="Lframe" frameborder="0" style="width:34%;height:370px;margin-left:2%;float:left;" src="process_opin_loadUserDefinedOpinions.action?code=1"></iframe><iframe name="Rframe" frameborder="0" style="width:60%;height:370px;margin-right:2%;float:right;" src="process_opin_editOpinion.action?'+param+'"></iframe>';
		  var a = $.dialog({       
	      id: "w" ,
	      title: "发表意见",
	      content: html,   
	      width: '550px',
	      height: '400px',
	      cancel:function(){
            },   
          cancelVal:"取消"
         });
	}	
	</script>
  </head>
  
  <body> 
<input type="button" value="发表意见" onclick="openWindow();"/>
  </body>
</html>
