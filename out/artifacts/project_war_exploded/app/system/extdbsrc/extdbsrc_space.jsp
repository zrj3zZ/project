<%@ page language="java" import="java.util.*"  pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>  
    <title>外部数据源管理</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
    <link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
    <script type="text/javascript">
     //==========================装载快捷键===============================//快捷键

    jQuery(document).bind('keydown',function (evt){		
		    if(evt.ctrlKey&&evt.shiftKey){
			return false;
		   }
		   else if(evt.ctrlKey && event.keyCode==82){ //Ctrl+r /刷新操作
			         this.location.reload(); return false;
		     } 
		  else if(evt.shiftKey && event.keyCode==78){ //Shift+n 新增操作
					 addDsrc(); return false;
				}

      }); //快捷键
    
    //新建数据源窗口
     function addDsrc(){
     	var pageUrl = 'sys_extdbsrc_add.action';
     	art.dialog.open(pageUrl,{
			    	id:'addDbsrc',  
	        		title:'新建数据源',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:600,
				    height:410,
		            close:function(){
		                location.reload();
		            }   
				 });
     }
   //编辑数据源窗口 
     function editDsrc(id){
     	var pageUrl = 'sys_extdbsrc_edit.action?id='+id;
     	art.dialog.open(pageUrl,{
			    	id:'addDbsrc',  
	        		title:'新建数据源',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:600,
				    height:410,
		            close:function(){
		                location.reload();
		            }   
				 });
     }
    //删除数据源窗口
    function delDsrc(id){
          art.dialog.confirm("确定删除？",function(){
               location.href="sys_extdbsrc_delete.action?id="+id;
          },function(){});
    }      
    </script>
    <style type="text/css">
        .title{font-weight:bolder; font-family:"微软雅黑"; font-size:12px; text-align:left;}
        .td_title{width:10%;}
        .td_data{width:80%;}
        .td_edit{text-align:right;width:10%;}
        .eachdiv{margin:5px;margin-bottom:10px; border:1px solid #CCCCCC;width:99%;}
        .eachdiv table td{
        	padding:5px;
        }
        .pagetoolbar{
			width:100%;
			background-image:url(../iwork_img/bg_bg1.gif);
			background-color:#E3E3E3;
			margin-top:0px;
 			height:28px;
 			border:1px #FFFFFF solid;
 			border-bottom:1px #efefef solid;
    		}
         body{
    		position:relative;
			overflow-x:hidden;
			overflow-y:auto;
			margin:0;
			padding:0;
			}
    </style>
  </head>
  
  <body>
	       <div class="tools_nav">
			   <a href="javascript:addDsrc();" class="easyui-linkbutton" plain="true" iconCls="icon-add">新增数据源</a>
			   <a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		   </div>
	   
	    <div>
	       
	        <div class="eachdiv">	          	            
	            <table  width="100%" align="center" cellpadding="0" cellspacing="0" border="0">
	             <s:iterator value="list">
	                 <tr style="background-color:#F5F8FA">
	                     <td class="td_title"><a href="javascript:editDsrc(<s:property value='id'/>);"><s:property value="dsrcTitle"/></a></td>  
	                     <td>&nbsp;</td>
	                    
	                     <td class="td_edit">
	                      <s:if test="dsrcTitle!=\"本地数据源\"">
	                          <a><img align='top' style="cursor:hand" alt="编辑数据源" src='iwork_img/but_edit.gif' onClick="editDsrc(<s:property value='id'/>)"></a>
	                          <a><img align='top' style="cursor:hand" alt="删除数据源" src='iwork_img/but_delete.gif' onClick="delDsrc(<s:property value='id'/>)"></a>
	                          </s:if>
	                     </td>
	                 </tr>
	                 <tr>
	                     <td class="td_title">驱动名称：</td>
	                     <td class="td_data"><s:property value="driverName"/></td>
	                  </tr>
	                 <tr>
	                     <td class="td_title">链接地址：</td>
	                     <td class="td_data"><s:property value="dsrcUrl"/></td>
	                  </tr>
	                 <tr>
	                     <td class="td_title">用户名：</td>
	                     <td class="td_data"><s:property value="username"/></td>
	                  </tr>	
	                 <tr>
	                     <td class="td_title">唯一标识：</td>
	                     <td class="td_data"><s:property value="uuid"/></td>
	                  </tr>	
	                   </s:iterator>	                                    	                  
	            </table>	               
	        </div>
	           
    </div> 
  </body>
</html>
