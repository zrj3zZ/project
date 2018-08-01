<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>意见历史</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css"/>
<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
<link href="iwork_css/jquerycss/cluetip/jquery.cluetip.css" rel="stylesheet" type="text/css"/>
<link href="iwork_css/reset.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="iwork_js/commons.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"  ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.src.js"  > </script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/cluetip/jquery.cluetip.js" ></script>
<style type="text/css">
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	
	background-position:top left; 
	background-repeat:no-repeat;
} 
.opinion_toolbar{
	border:1px solid #efefef;
}
.opinion_tb{
	border-top:1px solid #efefef;
}
.opinion_title{
       	 background:url(../iwork_img/engine/tools_nav_bg.jpg) repeat-x;
        	font-size:12px;
        	padding:2px;
        	padding-left:15px;
        	padding-right:3px;
        	background-color:#fff;
        	width:160px;
        	height:30px;
        	color:#333;
        	text-align:left;
        	font-family:黑体;
        	border-right:1px solid #efefef;
        	border-bottom:1px solid #efefef;
        }
        .opinion_step{
        	font-size:12px;
        	padding:2px;
        	padding-left:15px;
        	padding-right:3px;
        	background-color:#fff;
        	width:160px;
        	height:30px;
        	color:#333;
        	text-align:left;
        	font-family:黑体;
        	border-right:1px solid #efefef;
        	border-bottom:1px solid #efefef;
        }
        .opinion_content{
        	padding:2px;
        	padding-left:15px;
        	padding-right:3px;
        	font-size:12px;
        	color:red;
        	width:300px;
        	text-align:left;
        	border-bottom:1px solid #efefef;
        	border-right:1px solid #efefef;
        }
         .opinion_item{
        	text-align:left;
        	padding:2px;
        	font-size:12px;
        	padding-left:3px;
        	padding-right:10px;
        	border-bottom:1px solid #efefef;
        	border-right:1px solid #efefef;
        	color:#666;
        }
</style>
</head>
<body class="easyui-layout">
<div region="center" border="false"  >
<!--ShowMsg-->
<div id="Container">
 <s:property value="opinionList" escapeHtml="false"/>
</div>
<!--/ShowMsg-->
</div>
</body>
</html>
