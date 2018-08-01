<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
  <head>
    
    <title>添加批注</title>
    
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" charset="gb2312"></script> 
	
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
                     alert("添加的批注不能超过"+maxText+"个字符!");                       
               } 
   } //文本域输入的字符不能超过100字符
   String.prototype.trim=function()   
	{
	     return this.replace(/(^[\s]*)|([\s]*$)/g,"");
	}//去除字符串前后空格
	  function ok(){
	   var str=document.getElementById("ta").value.trim();
	   if(str==""||str==null){
	         alert("添加的批注不能为空!");
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
					            window.alert("该批注已经存在!");
					            document.getElementById("ta").value=str;
	                            setCursor();
					        }//该批注已经存在。
			});
       
     }
     function cancel(){
       location.href="process_opin_addOpinion.action?code=0";
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
	padding-top:0px;	
	width:300px; height:300px;	
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
		 width:94%;
		 margin-left:3%;
		 float:left;
    }
	</style>
	</head>
  
  <body>
    <!--添加小窗口  -->
    <div id="all">
    	<div id="top">
                <a href="#" onclick="ok();">确认</a>|               
                <a href="#" onclick="cancel();">取消</a>           
        </div>
        
        <div style="border:1px solid #C0C0C0; width:auto clear:both; margin-left:2px; margin-right:2px"></div>
        <form id="addform" method="post" >
        <div id="content">
				<fieldset>
	            	<legend id="leftlengendtitle"><b><font color="808080" >批注内容（100字以内)</font></b></legend>
					<textarea id="ta" name="addopin" maxlength="100" onKeyUp="check(this)" onKeyDown="check(this)">
		            </textarea> 
	            </fieldset>
       </div>
       </form>
  </div>     
<!--添加小窗口结束  -->
  </body>
</html>
