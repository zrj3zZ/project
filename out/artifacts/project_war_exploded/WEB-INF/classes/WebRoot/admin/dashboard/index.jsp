<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

 


 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
         <title>我的桌面</title> 
         <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
		<script type="text/javascript" src="iwork_js/jqueryjs/easyui/jquery.easyui.min.js" ></script>
        <link href="admin/css/dashboard.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript">
        	function openDeskManage(){
				parent.openWin("首页面板设置",450,380,"admin_portlet_design.action",this.location);
        	} 
        	function removePortlet(id){ 
        		var portletid = "";
        		if(id.indexOf("widget_")==0){
        			var start = id.indexOf("widget_");
        			portletid = id.substr(7);
        		}
        		if(portletid!=""){
        			var orderlist = iNettuts.showOrderlist();
        				$.post('admin_portlet_design_remove.action',{portletid:portletid,orderlist:orderlist},function(response){
    	 						if(response!='success'){ 
    	 							alert('移除错误，请稍候重试');
    	 						}
    					});
        		}
			}
			
        	function movePortlet(groupid,widgetid,orderlist){
        		var orderlist = iNettuts.showOrderlist();
        		if(orderlist!=""){ 
        				$.post('admin_portlet_design_move.action',{orderlist:orderlist},function(response){
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
<body   class="easyui-layout" >
    <div region="north" border="false" split="false" >
    <div id="head">
         <div class="toolbar"><a href="javascript:openDeskManage();">管理桌面</a>&nbsp;&nbsp;<a href="javascript:openDeskManage();">恢复默认</a></div>
    </div>
    </div>
   <div region="center" border="false" split="false"  >
    <div id="columns">
    <s:property value="html" escapeHtml="false"/>
    </div>
    </div>
   <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.2.min.js"   ></script>
    <script type="text/javascript" src="iwork_js/portal/jquery-ui-personalized-1.6rc2.min.js"></script>
    <script type="text/javascript" src="iwork_js/portal/inettuts.js"></script>

 
</body>
</html>
