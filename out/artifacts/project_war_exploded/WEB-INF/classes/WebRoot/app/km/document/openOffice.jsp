<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>  
    <title>在线预览office文件</title>
    <%
       String mServerUrl="http://"+request.getServerName()+":"+request.getServerPort()+"/km_office_open.action";//取得OfficeServer文件的完整URL        
      //String mServerUrl="km_office_open.action";//取得OfficeServer文件的完整URL        
   //   String mServerUrl="http://127.0.0.1:8080/iWebOffice2009/starup.doc";//取得OfficeServer文件的完整URL        
     %>   
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>   
    <script type="text/javascript" src="iwork_js/km/iwebOffice2009Impl.js"></script>
   
  </head>
  <body>
       <table width="100%" border="0" cellpadding="0" cellspacing="0">   		
			<tr> 			
				<td height="1100"  align="left" valign="top">
					<div align="center"> 


						 <!--调用iWebPicture，注意版本号，可用于升级-->
			    		<script src="iwork_js/plugs/iWebOffice2009.js"></script>
					</div>
				</td> 		
			</tr> 
		</table>      
  </body>
</html>
 <script type="text/javascript">
     	var OFFICE_OBJ_FILENAME="<s:property value='fileRealName'/>";          
		var OFFICE_OBJ_USERNAME="<s:property value='openUser'/>";
		var OFFICE_OBJ_RECORDID="<s:property value='fileSaveName'/>";
		var OFFICE_OBJ_FILETYPE="<s:property value='fileType'/>";      //FileType:文档类型  .doc  .xls  .wps
		var OFFICE_OBJ_STATUS='WRITE';
		var OFFICE_OBJ_DIR='DOCUMENT';		 //额外传后台的数据
			
     var officeObj = document.getElementById('WebOffice');
			alert(officeObj);
     try{		    
			officeObj.WebUrl="<%=mServerUrl%>";        //WebUrl:系统服务器路径，与服务器文件交互操作，如保存、打开文档，重要文件
			officeObj.RecordID="1";	       //RecordID:本文档记录编号
			officeObj.FileName="temp";	     //FileName:文档名称
			officeObj.FileType=".doc";        //FileType:文档类型  .doc  .xls  .wps
			officeObj.UserName="ADMIN";        //UserName:操作用户名，痕迹保留需要
			officeObj.ShowToolBar='0';   //ShowToolBar:是否显示工具栏:1显示,0不显示
			officeObj.EditType="-1,1,0,0,0,0,1,0";              //A  必须为“-1”
			var bool = officeObj.WebOpen();
			//if(!bool) return;
			//officeObj.showType='1';                       //文档显示方式  1:表示文字批注  2:表示手写批注  0:表示文档核稿
		//	document.getElementById('OLEObject').style.display='';
			
		}catch(e){
			alert('加载远程文档失败，请尝试重新打开\n'+e.description);
			window.close();
		}		
    </script>