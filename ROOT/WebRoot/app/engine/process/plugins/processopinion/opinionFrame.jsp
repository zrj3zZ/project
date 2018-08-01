<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
   <title>意见审批窗口右边的iframe</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" charset="gb2312"></script>	
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
    <script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style type="text/css"> 	
	#rightbox {
		font-weight:bolder; font-family:微软雅黑; font-size:12px; text-align:left;
		width:100%;
		height:250px;
	}
	textarea{
	    overflow-x:hidden;
	    overflow-y:auto;  
	    line-height:18px;
		font-family:"Arial";
		font-size:16px;
		border:1px solid #999999;
		color: #0000FF;
		width:380px;height:290px;background:#FFFFCC
	}
	
	#optionlist{
		padding:5px;
		background:#fff; 
		border:1px solid #efefef;
		font-weight:bolder; font-family:微软雅黑; font-size:12px; text-align:left;
	}
	#optionlist li{
		list-style:none; 
		line-height:18px;
		padding:4px;
		padding-left:10px;
		color:#0000ff;
		border-bottom:1px solid #efefef;
		
		cursor:pointer;
	}
	#optionlist li:hover{
		background:#efefef;
	}
	.tools_nav{
		background: #efefef;
		height:50px; 
		padding-top:10px;
		border-top:1px solid #ccc;
		text-align:right; 
		vertical-align:middle;
	}
	</style>
	<script type="text/javascript">
   var   currentActiveRow; //全局变量
   $(document).ready(function(){
	   document.getElementById('ta').value = art.dialog.data('opinionContent');// 读取A页面的数据
   })
   function removeItem(id){
   		art.dialog.confirm('你确定要删除这掉消息吗？',function () {
		 	    $.post('process_opin_delOpinion.action',{rowindex:id},function(data){
				    		$("#item_"+id).remove();
				        	art.dialog.tips("常用意见删除成功",2);
				});
		});
   }
   function chooseData(val)
	{        
          document.getElementById('ta').value =  val;
	}
	//去除字符串前后空格
   function check(obj){ 
             var maxText = obj.getAttribute("maxlength"); 
             if(obj.value.length > maxText){ 
                     obj.value = obj.value.substring(0,maxText);                       
                     art.dialog.tips("添加的常用意见不能超过"+maxText+"个字符!",2);                      
               } 
   } //文本域输入的字符不能超过100字符
    function addOpinion(){
       var opin=document.getElementById('ta').value.trim();
       document.getElementById('ta').value=opin;
	   if(opin==""||opin==null){
		   art.dialog.tips("添加的常用意见不能为空!",2);
	         return false;
	   } 
	   
	   
      $.post('process_opin_isOpinExist.action',{opin:opin},function(data)
      {
	    	if(data=='1'){
	    		 $.post('process_opin_addOpin.action',{addopin:opin},function(data)
                    {
                    		$("#optionlist").append("<li id='item_"+data+"' onclick=\"chooseData('"+opin+"')\">"+opin+"<a href=\"javascript:removeItem("+data+");\"><img style=\"float:right\" src=\"iwork_img/close.gif\"/></a></li>");
	    	          		art.dialog.tips("常用意见添加成功",2);
                    });
	    	}//该常用意见不存在，可以插入数据库。
	        else{
	        	art.dialog.tips("该常用意见已经存在",2);
	        }//该常用意见已经存在。
		  });
    }//添加常用意见 
	function sendMsg(){
	   var opinionContent=document.getElementById('ta').value.trim();
	   if(opinionContent==""||opinionContent==null){
	         art.dialog.tips("审核意见不能为空", 2);
	         return false;
	   }
	   var win = art.dialog.open.origin;
	   win.document.getElementById('opinion').value=opinionContent;// 存储数据
	   art.dialog.open.api.close();
	}
	
	//发表意见
	String.prototype.trim=function()   
	{
	     return this.replace(/(^[\s]*)|([\s]*$)/g,"");
	}//去除字符串前后空格
	
	</script>
  </head>
  
  <body class="easyui-layout">
   <div region="west"  border="false" title="常用意见" style="width:200px;background: #fff;Overflow:auto;padding:2px; border:1px solid #fff">
	            	<ul id="optionlist">    	
			    		<s:iterator value="opinions" status="status">
						 	<li id="item_<s:property value='id'/>" onclick="chooseData('<s:property value='content'/>')"><s:property value='content'/>
						 	<a href="javascript:removeItem(<s:property value='id'/>);"><img style="float:right" src="iwork_img/close.gif"/></a></li>					
						</s:iterator> 
					</ul>           	
   </div>
    <div region="center" border="false" title="编辑意见(100字以内)"  style="background: #fff; border:1px solid #fff">
    	<form id="opinform">
			       <div id="rightbox">
			            	<textarea  id="ta" name="model.content" maxlength="100" onKeyUp="check(this)" onKeyDown="check(this)"></textarea>           		           	
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
   </div>
     	  <div region="south" border="false" class="tools_nav" >
     	  			<input style="width:112px;height:25px;font-size:12px;margin-right:20px" type="button" onclick="addOpinion();" value="加入常用意见列表"/>
			            			<a id="sendbtn" href="###" onclick="sendMsg();" class="easyui-linkbutton" plain="false" iconCls="icon-ok">提交我的审核意见</a>
     	  </div>
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
     //  $(document).bind("contextmenu",function(){return false;});
  }//锁鼠标右键
  
  </script>
</html>
