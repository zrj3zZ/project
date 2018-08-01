<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>显示权限设置</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/portal.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
    
    <style type="text/css">
    #rightbox {
		font-weight:bold; font-family:微软雅黑; font-size:12px; text-align:left;
		width:250px;
		height:auto;    	
		margin-right:20px;
		overflow:hidden;
    }
    </style>
    <script type="text/javascript">
    $(function(){
                var api = frameElement.api;
		   		var W = api.opener;
				var obj = api.data;   
				//加载导航树
				$('#formTree').tree({   
	                 url:"sysEngineIForm!openjson.action",
	               	onClick:function(node){
	               		if(node.attributes.type=='group'){
	               			
	               		}
	               		else{
	               		    var id=node.id;
	               		    obj.value=id;
	               		    api.close();
	               		}
	               }, 
	               onLoadSuccess:function(node,data){
	               		var groupid = "<s:property value='formId'  escapeHtml='false'/>";
	               		var tnode = $('#formTree').tree('find',groupid);  
	               		if(tnode!=null){
	               			$('#formTree').tree('select',tnode.target);
	               		}
	               }
	             });
	  });
    </script>
  </head>
  
  <body>

    	<div id="rightbox">
	        <fieldset>
	            	<legend><b><font color="808080" >表单</font></b></legend>
	            	<div style="padding-left:5px; width:auto; height:400px; overflow-y:auto; border:1px #C0C0C0 solid;">
	            			<ul id="formTree">
						   </ul> 
	                </div>	
	       </fieldset>
	   </div>
	 
  </body>
</html>
