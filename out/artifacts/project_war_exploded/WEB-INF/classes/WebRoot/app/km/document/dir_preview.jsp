<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>  
    <title>文件夹预览</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/ui/base/jquery.ui.autocomplete.css">
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
    <script type="text/javascript">
    function openFile(type,rowindex,docEnum){ 
    	if(type == 'DIR'){ 
    	    var selectedId = $("#kmlist_grid").jqGrid("getGridParam", "selrow");
    	    gridReloadFlag = true;
    	    gridSelectId = rowindex;
    	    clickIcon(selectedId);
    	  //  setTimeout('afterWait2('+rowindex+');',1000);   	    
    	}
    	else if(type == 'DOC'){
    	    if(docEnum == null || docEnum == ''){
    	        art.dialog.tips("该文档没有绑定附件",2);
		        return;
    	    }
    	    var tabPanel = $('#property').tabs('getTab','预览');   
    	    var dloadUrl = 'km_doc_view.action?id='+rowindex;
    	    $('#property').tabs('update', {
				tab: tabPanel,
				options:{
					href:'km_doc_view.action?id='+rowindex
				}
			});
    	}
    	else if(type == 'LINK'){
    	     
    	}
    	else if(type == 'FORM'){
    	    
    	}
    }  
    </script>
  </head>
  
  <body>
<s:if test="previewList == null || previewList.size()==0">
   <div style='padding-left:10px;padding-top:10px;'>
      空文件夹
   </div>   
</s:if>  

<s:else>
<div class="div_des">
    <div class="content">
     <s:iterator value='previewList' status='status'>   
      <div class="icon_box">
        <div class="icon_images"><a href='#' onclick="openFile('<s:property value='type'/>',<s:property value='#status.count'/>,'<s:property value='docEnum'/>');return false;"><img onerror="this.src='iwork_img/file/big/attach.png'" src="iwork_img/file/big/attach.png" width="64" height="64"/></a></div>
        <div class="icon_text" title="<s:property value='name'/>"><s:property value='name'/></div>
      </div>   
    </s:iterator>
   </div>
</div> 
</s:else>  

  </body>
</html>