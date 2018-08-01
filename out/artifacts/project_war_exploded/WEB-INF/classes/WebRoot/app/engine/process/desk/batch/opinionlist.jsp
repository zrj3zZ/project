<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
   <title>意见审批窗口左边的iframe</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style type="text/css"> 
	#leftbox {
		font-weight:bolder; font-family:微软雅黑; font-size:12px; text-align:left;
		width:100%;
		height:100%;
	}  
	#leftdiv{
	    overflow-x:hidden;  
	    overflow-y:auto;
	    width:100%;
	    height:230px; 
	    border:1px #C0C0C0 solid;
	}
	.td_data {
		line-height: 23px;
		text-align: left;
		padding-left: 3px;
		font-size: 12px;
		font-family:"宋体";
		border-bottom:1px #999999 dotted;
		vertical-align:middle;
		word-wrap:break-word;
		word-break:break-all;
		font-weight:500;
		line-height:15px;
   }
	</style>
	<script type="text/javascript">
	var api = art.dialog.open.api, W = api.opener;
	var   currentActiveRow; //全局变量
	function clearActiveRow(){
	      if(currentActiveRow){
			  currentActiveRow.style.backgroundColor="";
			}
	}
    function mouseover(obj){
       if(currentActiveRow!=obj){
	      obj.style.background = "#D2E9FF";
	    } 
    }//鼠标到来
    function mouseout(obj){
        if(currentActiveRow!=obj){
           obj.style.background = "";
        }
    }//鼠标离开
    function chooseData(obj)
	{        
		  api.get("sendDialog").document.getElementById("opinion").value = obj.value; 
		  api.close();
	}//单击事件
	 String.prototype.trim=function()   
	{
	     return this.replace(/(^[\s]*)|([\s]*$)/g,"");
	}//去除字符串前后空格
   </script>
  </head>
  
  <body>
    <div id="leftbox">
            	<fieldset>
            	<legend id="leftlengendtitle"><b><font color="808080" >常用意见</font></b></legend>
                <div id="leftdiv"> 
	            	<table id="myTable" style="width:100%;" cellspacing="0" cellpadding="0">          	
			    		<s:iterator value="opinions" status="status">
						     <tr>
						        <td class="td_data">
						     <input name="defaultopins" onclick="chooseData(this)" onmouseover="mouseover(this);" onmouseout="mouseout(this);" style="border:0px;width:100%;height:100%;cursor:pointer;" onfocus="this.blur();" readonly type="text" title="<s:property value='content'/>" value="<s:property value='content'/>"/>
						        </td>
						     </tr>					
						</s:iterator> 
					</table>           	
	             </div>  
	            </fieldset>
	            	
     </div>
  </body>
  <script type="text/javascript">
  loadTip();
  lockRightCursor();
  function loadTip(){
        var myTable=document.getElementById("myTable");
        var rownum=myTable.rows.length;
        if(rownum==0){
	         var b =art.dialog.confirm("是否加载系统默认批注?",
			       function(){
			             location.href = 'process_opin_loadDefaultOpinions.action?code=1';
			       },function(){
			        
			       }//不加载
             );
         }
    }
  function lockRightCursor(){
       $(document).bind("contextmenu",function(){return false;});
  }//锁鼠标右键  
    
  </script>
</html>
