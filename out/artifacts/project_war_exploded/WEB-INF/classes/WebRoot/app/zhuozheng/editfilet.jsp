<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*,com.zhuozhengsoft.pageoffice.*" pageEncoding="UTF-8"%>

<html>
<head>
	
	<script type="text/javascript" src="<%=request.getContextPath()%>/jquery.min.js"></script>
	<script type="text/javascript">
			 var st=setInterval("WSave()",1000*60*10);//每十分钟保存一次
         function WSave(){
         var flag=document.getElementById("PageOfficeCtrl1").IsDirty;
        	
         if (flag){
				
           Save();
          
          }
        }
	
			function fileClose() {
			
				
               window.external.close();
               
        	} 
        	function AfterDocumentOpened() {
            SwitchFullScreen();
            document.getElementById("PageOfficeCtrl1").SetEnableFileCommand(3, false); //禁止保存
       	 }
	
			function SwitchFullScreen() {
         	document.getElementById("PageOfficeCtrl1").FullScreen = !document.getElementById("PageOfficeCtrl1").FullScreen;
     		}
			function Save() {
			 	var NOTICEFILE=document.getElementById("NOTICEFILE").value;
			 	var fileurl=document.getElementById("FILEURL").value;
			 	var username=document.getElementById("username").value; 
            	document.getElementById("PageOfficeCtrl1").WebSave();
            	$.ajax({
            	type : "post",
            	url : encodeURI("pagesave.action?newfilepath="+fileurl+"&username="+username+"&NOTICEFILE="+NOTICEFILE),
           		success:function(){
           				
           		}
          		});
            	 
            
       		 }
        	function BeforeBrowserClosed(){ 
        	   var flag=document.getElementById("PageOfficeCtrl1").IsDirty;
        	  /* 关闭前提示，点击确定保存关闭文档，点击取消不保存关闭文档 */
            if (document.getElementById("PageOfficeCtrl1").IsDirty){
                if(confirm("提示：文档已被修改，确定保存修改吗 ？"))
                {
                    Save();
                    return  true;
                    
                }else{
                	
                    return  false;
                }
	         	
            }
        }
           
	</script>	
	

</head>
<body class="easyui-layout" > 
<div region="north" style="padding:0px;border:0px;">
<div class="title">

</div>
	
      </div>
      <div region="center">
      		  
			<% 
			
			PageOfficeCtrl poCtrl1 = new PageOfficeCtrl(request);
			Object o=request.getAttribute("map");
			
					
			Map map=new HashMap();
			if(o!=null){
			map=(HashMap) o;
			}
			String username=map.get("username")==null?"":map.get("username").toString(); 
			
			String noticefile=map.get("NOTICEFILE")==null?"":map.get("NOTICEFILE").toString();
			
			
			poCtrl1.setServerPage("poserver.zz");
			poCtrl1.setMenubar(false);
			poCtrl1.setOfficeToolbars(true);
			
			
			poCtrl1.addCustomToolButton("保存", "Save()", 1);
			poCtrl1.addCustomToolButton("全屏切换", "SwitchFullScreen()", 4);
			poCtrl1.addCustomToolButton("关闭", "fileClose()", 21); 
			Object data=map.get("fileurl");
			String fileurl=null;
			if(data!=null){
		    fileurl=data.toString();
			}
			Object data2=map.get("startpath");
			String startpath=null;
			if(data2!=null){
		    startpath=data2.toString();
			}
			Object data1=map.get("endpath");
			String endpath=null;
			if(data1!=null){
		    endpath=data1.toString();
			}
			request.getSession().setAttribute("startpath", startpath);
		    poCtrl1.setJsFunction_AfterDocumentOpened("AfterDocumentOpened");
			poCtrl1.setSaveFilePage("/app/zhuozheng/savefile.jsp?endpath="+endpath);
			poCtrl1.webOpen(request.getContextPath()+"/"+fileurl, OpenModeType.docAdmin, ""); 
		
			poCtrl1.setTagId("PageOfficeCtrl1");
				
			%>
			<input type="hidden" id="username" name="username"   value="<%=username %>" />
			<input type="hidden" id="FILEURL" name="FILEURL"   value="<%=fileurl %>" />
			<input type="hidden" id="NOTICEFILE" name="NOTICEFILE"  value="<%=noticefile %>" />
      </div>
      		
      		<div style=" width:auto; height:700px;" >
			<%=poCtrl1.getHtmlCode("PageOfficeCtrl1")%>
 			</div> 
		
</body>
<script type="text/javascript">
			
</script>
</html> 
