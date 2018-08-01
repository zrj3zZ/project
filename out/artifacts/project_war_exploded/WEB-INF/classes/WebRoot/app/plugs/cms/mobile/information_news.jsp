<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title></title>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jquery.mobile-1.2.0.min.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqm-datebox.min.css" />
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.8.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.mobile-1.3.2.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.layout-latest.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jqm-datebox.core.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jqm-datebox.mode.calbox.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.mobile.datebox.i8n.CHN-S.js"></script>
<script type="text/javascript"> 
	function openCms(infoid){
		var url = "cmsOpen.action?infoid="+infoid;
		window.FrameLink.loadLink("查看详细",url); 
	}
	
	function openLink(key){
		window.jumpTools.backTasklist(key);
	}
	
	</script>
<style>
.mb_title {
	color:#999;
	font-size:12px;
	padding-top:2px;
	padding-bottom:2px;
}
.mb_data {
	padding-left:30px;
	font-size:16px;
	color:#0000FF;
	border-bottom:1px solid #999;
}
 <s:property value="style" escapeHtml="false"/>
</style>
</head>
<body>
<div data-role="page" class="type-interior">
  <div data-role="content">
    <form id="iformMain" name="iformMain" method="post" data-ajax="true">
      <div>
        <s:property value='JsonData' escapeHtml='false'/>
      </div>
      <!--表单参数-->
      <span style="display:none">
      <input name='submitbtn' id='submitbtn' type="submit" />
      </span>
    </form>
  </div>
  <!-- /content -->
</div>
<!-- /page -->
</body>
</html>