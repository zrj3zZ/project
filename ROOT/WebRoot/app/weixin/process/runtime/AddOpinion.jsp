<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
  <head>
    <title>添加批注</title>
		<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jquery.mobile-1.3.2.min.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqm-datebox.min.css" /> 
	<script src="iwork_js/jqueryjs/jquery-3.0.8.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.mobile-1.3.2.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.layout-latest.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jqm-datebox.core.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jqm-datebox.mode.calbox.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.mobile.datebox.i8n.CHN-S.js"></script>
	<script type="text/javascript" src="iwork_js/mobile/iOSJavascriptBridge.js"></script>
<script type="text/javascript" src="iwork_js/mobile/sys_mobile_adapter.js"></script> 
	<script type="text/javascript">
	  window.onload=clearEditor;
	  function clearEditor(){
	     document.getElementById('ta').value='';
	     setCursor();      
	     }    
	function setCursor() {   
	    var obj=document.getElementById('ta');
	    var start=obj.value.length;
		if(obj.setSelectionRange) {
			obj.focus();
			obj.setSelectionRange(start,start);
		} else {
			if(obj.createTextRange) {
				range=obj.createTextRange();
				range.collapse(true);
				range.moveStart("character",start);
				range.select();
	        }
       }
   }//textarea聚焦
   function check(obj){ 
             var maxText = obj.getAttribute("maxlength"); 
             if(obj.value.length > maxText){ 
                     obj.value = obj.value.substring(0,maxText);                       
                     showTip("添加的批注不能超过"+maxText+"个字符!");                       
               } 
   } //文本域输入的字符不能超过100字符
   String.prototype.trim=function()   
	{
	     return this.replace(/(^[\s]*)|([\s]*$)/g,"");
	}//去除字符串前后空格
	  function ok(){
	   var str=document.getElementById("ta").value.trim();
	   if(str==""||str==null){
		   showTip("添加的批注不能为空!");
	         document.getElementById("ta").value=str;
	         document.getElementById("ta").focus();
	         return false;
	   }
	   $.post('process_opin_isOpinExist.action',{opin:str},function(data)
				    {
					    	if(data=='1'){
					    		document.getElementById("addform").action="process_opin_addOpinion.action"; 
                                document.getElementById("addform").submit();
					    	}//该批注不存在，可以插入数据库。
					        else{
					        	showTip("该批注已经存在!");
					            document.getElementById("ta").value=str;
	                            setCursor();
					        }//该批注已经存在。
			});
       
     }
     function cancel(){
       location.href="process_opin_addOpinion.action?code=0";
     }
     function add(data){
     	document.getElementById("ta").value = data;
     }
	</script>
	<style type="text/css">
	textarea{
	    overflow-x:hidden;
	    overflow-y:auto;
	    width:100%;
	    height:220px; 
	    border:1px #C0C0C0 solid;
	}
	a{
      text-decoration:none;
    }
    #all {
	padding:10px;	
	align:center;
    }
   #top {
	font-weight:bolder; font-family:微软雅黑; font-size:16px;
	text-align: left;
	margin-left:3%;
	margin-top:3px;
	align:left;
    }
     #content {
	     margin-top:25px;
	     font-weight:bolder; font-family:微软雅黑; font-size:12px; text-align:left;
		 width:99%;
		 float:left;
    }
	</style>
	<script type="text/javascript">
			function setOpinion(opin){
				$("#opinion").val(opin);
				$("#popupMenu1").popup("close");
			}
			
			function dosave(){
				if($("#opinion").val()==""){
					showTip("请填写意见");
					return ;
				}
				var obj = $('#iformMain').serialize();
				$.post('process_opin_sendOpinion.action',obj,function(data){
					backPrevious();
				});
			}
	</script>
	</head>
  
  <body>
  <form id="iformMain" method="post" >
    <!--添加小窗口  -->
    <div id="all">
        <div id="content">
				<fieldset>
	            	<legend id="leftlengendtitle"><b><font color="808080" >批注内容（100字以内)</font></b></legend>
	            	<s:textarea id="opinion" name="model.content" onKeyUp="check(this)" onKeyDown="check(this)" cssStyle="height:80px;text-align:left"  ></s:textarea>
	            </fieldset>
       </div>
       <div style="text-align:right"> 
       <a  href="#popupMenu1" data-rel="popup" data-role="button" data-inline="true" data-transition="slideup"  data-icon="gear" data-theme="d">常用意见</a>
		<div data-role="popup" id="popupMenu1" data-theme="d">
		        <ul data-role="listview" data-inset="true" style="min-width:210px;" data-theme="d">
		            <li data-role="divider" data-theme="e">常用意见</li>
		            <s:iterator value="cyOpinionlist">
		            	<li data-rel="back" ><a href="javascript:setOpinion('<s:property value="value"/>')"><s:property value="value"/></a></li>
		            </s:iterator> 
		        </ul>
		</div>
       </div>
       <div id="top">
               <a id="saveBtn" href="javascript:dosave();" data-role="button" data-icon="check" data-theme="a">保&nbsp;&nbsp;存</a>
        </div>
  </div>
 			<s:hidden id ="actDefId" name="model.actDefId" value="%{actDefId}"/>
			<s:hidden  id ="prcDefId" name="model.prcDefId" value="%{prcDefId}"/> 
			<s:hidden id ="actStepDefId" name="model.actStepId" value="%{actStepDefId}"/>
			<s:hidden id ="taskId" name="model.taskid" value="%{taskId}"/> 
			<s:hidden id ="instanceId" name="model.instanceid" value="%{instanceId}"/>
			<s:hidden id ="excutionId" name="model.executionid" value="%{excutionId}"/>
			<s:hidden id ="deviceType" name="deviceType"/>
<!--添加小窗口结束  -->
</form>
  </body>
</html>
