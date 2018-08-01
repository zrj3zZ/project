<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
         <title>我的桌面</title> 
         <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
         <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
         <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" charset="gb2312"></script>
        <link href="iwork_css/portal/inettuts.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript">
        	function openDeskManage(){  
				parent.openWin("桌面设置",450,380,"pt_person_showset.action",this.location);
        	} 
        	function removePortlet(id){
        		var portletid = "";
        		if(id.indexOf("widget_")==0){
        			var start = id.indexOf("widget_");
        			portletid = id.substr(7);
        		}
        		if(portletid!=""){
        				$.post('pt_person_portlet_remove.action',{portletid:portletid},function(response){
    	 						if(response!='success'){
    	 							alert('移除错误，请稍候重试');
    	 						}
    					});
        		}
			}
        	function removePortlet(id){
        		var portletid = "";
        		if(id.indexOf("widget_")==0){
        			var start = id.indexOf("widget_");
        			portletid = id.substr(7);
        		}
        		if(portletid!=""){
        				$.post('pt_person_portlet_remove.action',{portletid:portletid},function(response){
    	 						if(response!='success'){
    	 							alert('移除错误，请稍候重试');
    	 						}
    					});
        		}
			}
			
        	function movePortlet(groupid,widgetid,orderlist){
        		var columnid = "";
        		var portletid = ""; 
        		if(widgetid.indexOf("widget_")==0){
        			var start = widgetid.indexOf("widget_");
        			portletid = widgetid.substr(7);
        		} 
        		if(groupid.indexOf("column_")==0){
        			var start = groupid.indexOf("column_");
        			columnid = groupid.substr(7);  
        		}  
        		if(portletid!=""&&columnid!=""){ 
        				$.post('pt_person_portlet_move.action',{columnid:columnid,portletid:portletid,orderlist:orderlist},function(response){
    	 						if(response!='success'){  
    	 							alert('移除错误，请稍候重试');
    	 						}
    					});
        		}
			}
			//更新设置
			function updateColumnSet(widgetid,title,style){ 
				var portletid = ""; 
        		if(widgetid.indexOf("widget_")==0){
        			var start = widgetid.indexOf("widget_");
        			portletid = widgetid.substr(7);
        		} 
				if(title==""&&style==""){
					return;
				}else{ 
					$.post('pt_person_portlet_update.action',{title:title,style:style,portletid:portletid},function(response){
	    	 						if(response!='success'){   
	    	 							alert('设置成功'); 
	    	 						}
	    			});
				}
			}
        </script>
</head> 
<body   >
    <div style="height:30px;overflow:hidden;text-align:right" border="false" split="false" >
         <div class="toolbar">
         	<a  href="#"  style="margin-left:1px;margin-right:1px" id="transBtn" onclick='openDeskManage()' text='Ctrl+Enter' class="easyui-linkbutton" plain="false" iconCls="icon-dictionary">管理桌面</a>
 		</div>
    </div>
   <div  border="false" split="false"  >
    <div id="columns">
           <s:property value="columnHTML" escapeHtml="false"/>
    </div>
    </div>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.2.min.js"   ></script>
    <script type="text/javascript" src="iwork_js/portal/jquery-ui-personalized-1.6rc2.min.js"></script>
    <script type="text/javascript" src="iwork_js/portal/inettuts.js"></script>
 
</body>
</html>
