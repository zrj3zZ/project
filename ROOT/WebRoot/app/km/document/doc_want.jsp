<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>   
    <title>猜你想要</title>
    <link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
    <script type="text/javascript">
    function openOrDloadFile(id){
        $.post('km_doc_getTypeAndEnum.action',{id:id},function(data){  
             var json = strToJson(data);
        	 var type = json.type;  
        	 var docEnum = json.docEnum; 
        	 if(type == 'DOC'){
    	    	 if(docEnum == null || docEnum == ''){
    	        		art.dialog.tips("该文档没有绑定附件",2);
		        		return;
    	    		}
    	    	 var dloadUrl = 'km_file_uploadifyDownload.action?fileUUID='+docEnum;
	        	 location.href = dloadUrl;
    	  	 }
    		else if(type == 'LINK'){
    	     
    		}
    		else if(type == 'FORM'){
    	    
    		}
        });    	
    }
    </script>
  </head>
  
  <body>
  <div class="div_des">
    <div class="report_top">
   
      <s:iterator value='wantList' id='list'>  
      <s:iterator value='#list' id='map'> 
      <s:iterator value='#map.key' id='key'>
      <div class="cnxy_main">
        <div style="clear:both;">
          <table width="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td class="cnxy_title"><a href='#' onclick="openOrDloadFile(<s:property value='id'/>);return false;"><s:property value='docname' escapeHtml='false'/></a></td>
              <td class="cnxy_path">路径:<s:property value='#map.value' escapeHtml='false'/></td>
            </tr>
          </table>
        </div>
        <div class="cnxy_content"><s:property value='content' escapeHtml='false'/></div>
      </div>
      </s:iterator>
      </s:iterator>
      </s:iterator>
      
    </div>
  </div>    
  </body>
</html>
