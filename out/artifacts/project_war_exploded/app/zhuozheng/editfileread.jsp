<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*,com.zhuozhengsoft.pageoffice.*" pageEncoding="UTF-8"%>
<html>
<head>
	<script type="text/javascript">
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
        	 
			</script>	
	

</head>
<body class="easyui-layout" > 
<div region="north" style="padding:0px;border:0px;">
      </div>
      <div region="center">
			<% 
			PageOfficeCtrl poCtrl1 = new PageOfficeCtrl(request);
			poCtrl1.setServerPage("poserver.zz");
			poCtrl1.addCustomToolButton("全屏切换", "SwitchFullScreen()", 4);
			poCtrl1.addCustomToolButton("关闭", "fileClose()", 21); 
			Object data=request.getAttribute("fileurl");
			String fileurl=null;
			if(data!=null){
		    fileurl=data.toString();
			}
			poCtrl1.setAllowCopy(false);//禁止拷贝
			poCtrl1.setMenubar(false);//隐藏菜单栏
			poCtrl1.setOfficeToolbars(false);//隐藏Office工具条
			poCtrl1.setCustomToolbar(true);//隐藏自定义工具栏
			poCtrl1.setJsFunction_AfterDocumentOpened("AfterDocumentOpened");
			poCtrl1.webOpen(request.getContextPath()+"/"+fileurl, OpenModeType.docAdmin, ""); 
			poCtrl1.setTagId("PageOfficeCtrl1");
			%>
			<input type="hidden" id="FILEURL" name="FILEURL"   value="<%=fileurl %>" />
      </div>
      		<div style="width:auto;height:800px;" >
			<%=poCtrl1.getHtmlCode("PageOfficeCtrl1")%>
 			</div> 
</body>
</html> 
