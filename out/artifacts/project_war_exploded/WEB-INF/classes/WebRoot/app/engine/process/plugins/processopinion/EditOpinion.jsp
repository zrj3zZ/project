<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>常用批注编辑</title> 
     <base target="_self"/>  
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" charset="gb2312"></script>
		
	<script type="text/javascript">
	var  currentActive; //全局变量
	window.onload=clearActiveRow;
	function clearActiveRow(){
	      if(currentActive){  
			  currentActive.style.backgroundColor="";
			}
	}
	   
	function mouseover(obj){
	    if(currentActive!=obj){
	      obj.style.background = "#D2E9FF";
	    }       
    }//鼠标到来
    function mouseout(obj){
        if(currentActive!=obj){
         obj.style.background = "";
        }
    }//鼠标离开
	 function getRowIndex(obj)
	 {  
	     return obj.parentNode.parentNode.rowIndex;
	 }//得到行号
    function editData(obj)
	{
		 if(currentActive)   {currentActive.style.backgroundColor="";}   
           currentActive=obj; 
           currentActive.style.backgroundColor="#FFF0AC";  
	}//单击事件
	function add(){
	      location.href='process_opin_openAddWin.action';
	}//添加批注
	function modify(){
		  if(currentActive){
		     var rowindex=getRowIndex(currentActive);
		     var str=encodeURI(currentActive.value);
		     location.href='process_opin_openModWin.action?str='+str+'&rowindex='+rowindex;
		  }else{
		     alert("请选中要修改的批注!");
		  }   
	}//修改批注
	function del(){
	      if(currentActive){
					if (window.confirm('删除该批注?')){
						rowindex=getRowIndex(currentActive);
	                    location.href='process_opin_delOpinion.action?rowindex='+rowindex;
					}//删除
					else{
					    
					}//不删除
	      }
	      else{
	         alert("请选中要删除的批注!");
	      }
	}//删除批注
   function ok(){
       //window.returnValue = addressCode;
       window.close(); 
   }
	</script>
	<style type="text/css"> 
    #content {
	     margin-top:10px;	
	     width:325px; height:300px;	
	     align:center;
    }
	#upbox {
		font-weight:bolder; font-family:微软雅黑; font-size:12px; text-align:left;
		width:256px;
		height:100%;
		margin-left:3px;
		float:left;
	}
	#updiv{
	    overflow-x:hidden;
	    overflow-y:auto;
	    width:100%;
	    height:250px; 
	    border:1px #C0C0C0 solid;
	}
	#upbox2 {
		font-weight:bolder; font-family:微软雅黑; font-size:12px; text-align:left;
		width:60px;
		height:50%;
		margin-left:3px;
		float:left;
		margin-top:100px;	
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
	cursor:pointer;
   }
   .dwnbuttons{
    width:60px;
    height:25px;
    font-size:12px;
    text-align:center;
   }
   A:LINK, A:VISITED, A:ACTIVE, A:HOVER
    {
	  color:#006699;
      font-size: 12px;
      padding-left: 3px;
      TEXT-DECORATION: NONE;
    }
   </style>
  </head>
 
  <body>
    	  <div id="content">	
				<div id="upbox">
	            	<fieldset>
	            	<legend id="leftlengendtitle"><b><font color="808080" >常用意见</font></b></legend>
	            	<div id="updiv"> 
	            	<table id="editable" style="width:100%" cellspacing="0" cellpadding="0">
	            	    <s:iterator value="opinions" status="status">
						     <tr>
						        <td class="td_data">
						     <input onclick="editData(this)" onmouseover="mouseover(this);" onmouseout="mouseout(this);" style="border:0px;width:100%;height:100%;cursor:pointer;" onfocus="this.blur();" readonly type="text" title="<s:property value='value'/>" value="<s:property value='value'/>"/>
						        </td>
						     </tr>					
						</s:iterator> 
					</table> 					         	
	               </div> 
	              </fieldset>
	           
	           
	                 <div style="float:left;margin-top:30px;">
		                <input class="dwnbuttons" type="button" onclick="add();" value="添加"/>
		                <input class="dwnbuttons" type="button" onclick="modify();" value="修改"/>               
		                <input class="dwnbuttons" type="button" onclick="del();" value="删除"/>
		             </div>
	            </div>
               <div id='upbox2'>
                  <a href='#' title='置顶' onClick='moveUp(true);'><img src=iwork_img/arr_up.gif border='0'>&nbsp;&nbsp;置顶</a><br>
	              <a href='#' title='上移' onClick='moveUp(false);'><img src=iwork_img/arr_u.gif border='0'>&nbsp;&nbsp;上移</a><br>
	              <a href='#' title='下移' onClick='moveDown(false);'><img src=iwork_img/arr_d.gif border='0'>&nbsp;&nbsp;下移</a><br>
	              <a href='#' title='置底' onClick='moveDown(true);'><img src=iwork_img/arr_down.gif border='0'>&nbsp;&nbsp;置底</a>                  
               </div>
             
         </div>   

  </body>
  <script type="text/javascript">
  loadTip();
  function loadTip(){
     var myTable=document.getElementById("editable");
     var rownum=myTable.rows.length;
     if(rownum==0){
             if(window.confirm('是否加载系统默认批注?')){
                location.href = 'process_opin_loadDefaultOpinions.action?code=2';
             }
      }
  }
  if (typeof Element != 'undefined') Element.prototype.moveRow = function (sourceRowIndex, targetRowIndex) {//执行扩展操作
        if (!/^(table|tbody|tfoot|thead)$/i.test(this.tagName) || sourceRowIndex === targetRowIndex) return false;
        var pNode = this;
        if (this.tagName == 'TABLE') pNode = this.getElementsByTagName('tbody')[0]; //firefox会自动加上tbody标签，所以需要取tbody，直接table.insertBefore会error
        var sourceRow = pNode.rows[sourceRowIndex], targetRow = pNode.rows[targetRowIndex];
        if (sourceRow == null || targetRow == null) return false;
        var targetRowNextRow = sourceRowIndex > targetRowIndex ? false : getTRNode(targetRow, 'nextSibling');
        if (targetRowNextRow === false) pNode.insertBefore(sourceRow, targetRow); //后面行移动到前面，直接insertBefore即可
        else {//移动到当前行的后面位置，则需要判断要移动到的行的后面是否还有行，有则insertBefore，否则appendChild
            if (targetRowNextRow == null) pNode.appendChild(sourceRow);
            else pNode.insertBefore(sourceRow, targetRowNextRow);
        }
    }//table两行交换
  //Firefox下表格里面的空白，回车也算一个节点，所以需要过滤一下节点类型。
    function getTRNode(nowTR, sibling) {
       while (nowTR = nowTR[sibling]) if (nowTR.tagName == 'TR') break; return nowTR; 
      }
 //上移以及置顶
 function moveUp(bool){
      if(!currentActive){
         alert("请选择要移动的批注");
         return false;
      }
      var now_row=currentActive.parentNode.parentNode;
      if(!bool){        
         var pre_row=getTRNode(now_row,'previousSibling');
         if (pre_row==null) {alert('已经是第一条批注！');return false;}
         var now_index=now_row.rowIndex;
         var pre_index=pre_row.rowIndex;
          $.post('process_opinlist_moveUp.action',{now_index:now_index,pre_index:pre_index},function(data)
				    {
					    	if(data=='1'){
					    		document.getElementById('editable').moveRow(now_index,pre_index);
					    	}//数据库中orderindex交换之后，交换页面上的两行记录
					       
			});
         
      }else{
         var up_row=getTRNode(now_row,'previousSibling');
         if (up_row==null) {alert('已经是第一条批注！');return false;}
         while(true){
            if(getTRNode(up_row,'previousSibling')==null) break;
            up_row=getTRNode(up_row,'previousSibling');
          }
        var now_index=now_row.rowIndex;
        var up_index=up_row.rowIndex; 
        $.post('process_opinlist_moveTop.action',{now_index:now_index,up_index:up_index},function(data)
				    {
					    	if(data=='1'){
					    		document.getElementById('editable').moveRow(now_index,up_index);
					    	}//数据库中orderindex交换之后，交换页面上的两行记录
					       
			});
        
      }
      
 }
 //下移以及置底
 function moveDown(bool){
      if(!currentActive){
         alert("请选择要移动的批注");
         return false;
      }
      var now_row=currentActive.parentNode.parentNode;
      if(!bool){       
         var next_row=getTRNode(now_row,'nextSibling');
         if (next_row==null) {alert('已经是最后一条批注！');return false;}
         var now_index=now_row.rowIndex;
         var next_index=next_row.rowIndex;
         $.post('process_opinlist_moveDown.action',{now_index:now_index,next_index:next_index},function(data)
				    {
					    	if(data=='1'){
					    		document.getElementById('editable').moveRow(now_index,next_index);
					    	}//数据库中orderindex交换之后，交换页面上的两行记录
					       
			});
         
      }else{
         var down_row=getTRNode(now_row,'nextSibling');
         if (down_row==null) {alert('已经是最后一条批注！');return false;}
         while(true){
            if(getTRNode(down_row,'nextSibling')==null) break;
            down_row=getTRNode(down_row,'nextSibling');
          }
         var now_index=now_row.rowIndex;
         var down_index=down_row.rowIndex;
         $.post('process_opinlist_moveLow.action',{now_index:now_index,down_index:down_index},function(data)
				    {
					    	if(data=='1'){
					    		document.getElementById('editable').moveRow(now_index,down_index);
					    	}//数据库中orderindex交换之后，交换页面上的两行记录
					       
			});
        
      }
      
 }    
  </script>
</html>
