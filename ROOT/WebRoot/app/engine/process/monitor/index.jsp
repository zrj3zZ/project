<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title> 
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<link href="iwork_css/eagles_searcher.css" rel="stylesheet" type="text/css"/>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<style type="text/css">
	
	</style>
<script>

</script>
<style type="text/css">
a {
 font-size: 10pt; text-decoration: none; color: #0000ff;
 }
.app_box_item{
	 position:relative;
 	border-bottom:1px solid #e5e5e5;
 }
 .app_box_title{
 	position: absolute;
    color: #777;
    left: 20px;
    top: 20px;
    font-family:微软雅黑;
    font-size:14px;
 }
 .app-box_count{
 	overflow: hidden;
    padding: 60px 0 20px 30px;
 }
 ul{
 	list-style-type: none;
 	}
 .app-box_count li {
 	list-style-type: none;
 	text-decoration:none;
    text-align: center;
    width: 32%;
    float: left;
    }
    .app-box_count li.li-border-right {
   		 border-right: 1px solid #e5e5e5;
	}
	.app-box_li_width {
  	  width: 155px;
    	margin: 0 auto;
	}
	.app-box_count h5 {
		  font-size: 14px;
	    color: #777;
	    font-weight: 400;
	    font-family:微软雅黑;
	}
	.mod-basic-data__rep-num {
	    font-size: 50px;
	    color: #222;
	    line-height: 60px;
	    font-family:微软雅黑;
	}
relative
</style> 
</head> 
<body class="easyui-layout">
	<div region="north" style="text-align:left;border:0px">
				<div  class="tools_nav">
			  		<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		  		</div>	
		  		<div class="app_box_item clearfix">
			    <span class="app_box_title">流程运行指标<a class="app_message_more" href="#analysis/chart/qydata">查看详情</a></span>
			    <ul class="js_user_data_list app-box_count"><li class="li-border-right">
			    <div class="app-box_li_width">
			        <h5>待办流程总数</h5>
			        <span class="mod-basic-data__rep-num"><s:property value="model.todoNum"/></span>
			    </div>
			</li>
			<li class="li-border-right">
			    <div class="app-box_li_width">
			        <h5>归档流程总数</h5>
			        <span class="mod-basic-data__rep-num"><s:property value="model.finishNum"/></span>
			    </div>
			</li>
			<li>
			    <div class="app-box_li_width">
			        <h5>办理任务总数</h5>
			        <span class="mod-basic-data__rep-num"><s:property value="model.taskNum"/></span>
			    </div>
			</li>
			</ul>
			</div>	
	</div>
	<div region="center" style="text-align:left;border:0px">
			
		</div>
</body>
</html>
