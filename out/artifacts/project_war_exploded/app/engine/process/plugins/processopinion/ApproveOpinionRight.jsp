<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
   <title>意见审批窗口右边的iframe</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" charset="gb2312"></script>	
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true"></script>
	<style type="text/css"> 	
	#rightbox {
		font-weight:bolder; font-family:微软雅黑; font-size:12px; text-align:left;
		width:100%;
		height:250px;
	}
	textarea{
	    overflow-x:hidden;
	    overflow-y:auto;  
	    width:100%;
	    height:230px; 	  
	    line-height:18px;
		font-family:"Arial";
		font-size:16px;
		border:1px solid #999999;
		color: #0000FF;
	}
	</style>
	<script type="text/javascript">
	
   function check(obj){ 
             var maxText = obj.getAttribute("maxlength"); 
             if(obj.value.length > maxText){ 
                     obj.value = obj.value.substring(0,maxText);                       
                     window.parent.parent.lhgdialog.tips("添加的批注不能超过"+maxText+"个字符!",2);                      
               } 
   } //文本域输入的字符不能超过100字符
    function addOpinion(){
       var opin=document.getElementById('ta').value.trim();
       document.getElementById('ta').value=opin;
	   if(opin==""||opin==null){
	         window.parent.parent.lhgdialog.tips("添加的批注不能为空!",2);
	         return false;
	   } 
      $.post('process_opin_isOpinExist.action',{opin:opin},function(data)
      {
	    	if(data=='1'){
	    		 $.post('process_opin_addOpin.action',{addopin:opin},function(data)
                    {
	    	          if(data=='0'){
	    		          window.parent.parent.Lframe.location.reload();
	    	           }//插入成功,刷新右边的iframe。
                    });
	    	}//该批注不存在，可以插入数据库。
	        else{
	            window.parent.parent.lhgdialog.tips("该批注已经存在",2);
	        }//该批注已经存在。
		  });
    }//添加批注 
	function sendMsg(){
	   var opinionContent=document.getElementById('ta').value.trim();
	   if(opinionContent==""||opinionContent==null){
	         window.parent.parent.lhgdialog.tips("审核意见不能为空!",2);
	         return false;
	   }
	   document.getElementById('ta').value=opinionContent;
	   $.post('process_opin_sendOpinion.action',$("#opinform").serialize(),function(data)
            {
					    	if(data=='1'){
					    		 window.parent.parent.lhgdialog.tips("审核意见发表成功",2);
					    		 window.parent.parent.$.dialog.list["dg_addAuditOpinion"].close();
					    	}
		  });
       
	}//发表意见
	 String.prototype.trim=function()   
	{
	     return this.replace(/(^[\s]*)|([\s]*$)/g,"");
	}//去除字符串前后空格
	
	</script>
  </head>
  
  <body>
       <form id="opinform">
       <div id="rightbox">
            	<fieldset>
            	<legend id="rightlegendtitle"><b><font color="808080" >编辑意见(100字以内)</font></b></legend>
            	<textarea id="ta" name="model.content" maxlength="100" onKeyUp="check(this)" onKeyDown="check(this)"></textarea>           		           	
            	</fieldset>
            	<div style="margin-top:10px;margin-left:4px;margin-right:4px;">
            	<table width="">
            		<tr>
            			<td><input style="float:left;width:112px;height:25px;font-size:12px;text-align:center" type="button" onclick="addOpinion();" value="加入常用意见列表"/></td>
            			<td><a id="sendbtn" href="#" onclick="sendMsg();" class="easyui-linkbutton" plain="false" iconCls="icon-ok">提交我的审核意见</a> </td>
            		</tr>
            	</table>           	            	
            	
            	            	          	
            	
            	</div>
       </div> 
       
       <input id="hid0" type="hidden" value="<s:property value='userOpin'/>">
       <input id="hid1" type="hidden" name="model.actDefId" value="<s:property value='actDefId'/>">
       <input id="hid2" type="hidden" name="model.prcDefId" value="<s:property value='proDefId'/>">
       <input id="hid3" type="hidden" name="model.actStepId" value="<s:property value='actStepId'/>">
       <input id="hid4" type="hidden" name="model.actStepName" value="<s:property value='actStepName'/>">
       <input id="hid5" type="hidden" name="model.instanceid" value="<s:property value='instanceid'/>">
       <input id="hid6" type="hidden" name="model.taskid" value="<s:property value='taskid'/>">
       <input id="hid7" type="hidden" name="model.executionid" value="<s:property value='excutionid'/>">
       <input id="hid8" type="hidden" name="model.status" value="0">
       
       </form>
  </body>
  <script type="text/javascript">
     getUserOpin();
     setCursor();
     lockRightCursor();
     
     function getUserOpin(){
         var userOpin=document.getElementById('hid0').value;
         document.getElementById('ta').value=userOpin;
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
   
   function lockRightCursor(){
       $(document).bind("contextmenu",function(){return false;});
  }//锁鼠标右键
  
  </script>
</html>
