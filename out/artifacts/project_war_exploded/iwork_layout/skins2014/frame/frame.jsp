<!DOCTYPE html>  
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh">  
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title><s:property value="systemTitle"/></title>
	<s:include value="link.jsp"> </s:include>
</head>
<body >
<s:include value="topbar.jsp">
   </s:include>
<div id="page-wrap"> 
        <div class="qq-fixed">
            <a href="" class="qg-ico18 ico-qq" target="_blank"></a>
        </div>
        <div id="feedback" class="feedback-fixed">
            <a href="javascript:void(0);" event-node="add_feedback_box"  class="qg-ico18 ico-edit"></a>
        </div>
        <div id="main-wrap">
            <div id="st-index-grid" class="st-grid">
            <!-- 左侧菜单  -->
				<s:include value="leftmenu.jsp">
				   </s:include>
                    <!--右边-->
                  <div id="col8" class="st-section">
                  <div class="flexible clearfix">
                         <iframe src="<s:property value="navurl" escapeHtml="false"/>" id="main" width="100%" height="<s:property value="height" escapeHtml="false"/>" frameborder="0" scrolling="auto"></iframe>
             	 </div>
          </div>
            </div>
        </div>
    </div>
<!--=> ft <=-->
<div id="footer">
    <div class="client">
        <ul>
            <li><a href="javascript:getContentHeight()"><i class="aicon-android"></i>Android</a></li>
            <li><a href=""><i class="aicon-ios"></i>iPhone</a></li>
              </ul>
    </div>
    <div class="copyright">
        <p>Copyright(C) 2014 金鹰BPM</p>
    </div>
</div>
<!--=> ft End <=-->
</body>
</html>
<<script type="text/javascript">
$("#main").load(function(){
	var newHeight = $(this).contents().find('body').height();
	var mainheight = $("#col8").height()-5;
	if(newHeight<mainheight){
		newHeight = mainheight;
	}
	$(this).height(newHeight);
	}); 
</script>
