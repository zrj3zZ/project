<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
	<style type="text/css">
		 div#contain {
			width: 1000em;
			background: none;
			font-family:verdan;
			font-size:12px;
		}
		 ul#map {
			float: none;
			margin: 0 auto;
		}
		 ul {
			clear: left;
			margin: 2em 0 0 0;
			padding: 0;
			background: #fff;
		}
		 ul ul {
			border-top: 1px solid #000;
			width: auto;
		}
		 ul.solo {
			border-top: 0;
		}
		 li {
			float: left;
			list-style: none;
			position: relative;
		}
		 li li {
			margin: -1px 0 0 0;
		}
		 div {
			background: url(iwork_img/navmap/vLine.gif) 50% repeat-y;
			padding: 2em 5px 0 5px;
			margin: 0 .3em -2em .3em;
		}
		 div.section {
			padding: 2em 5px 2em 5px;
		}
		 div.first {
			background: url(iwork_img/navmap/first.gif) 50% repeat-y;
			margin-left: 0;
		}
		 div.last {
			background: url(iwork_img/navmap/last.gif) 50% repeat-y;
			margin-right: 0;
		}
		 div.root {
			padding-top: 0;
		}
		 a {
			display: block;
			background: #fff;
			border: 1px solid #000;
			padding: .25em .2em .2em .2em;
			color: #222;
			text-decoration: none;
			margin: 0 auto;
			width: 10em;
			line-height: 2em;
			text-align: center;
			font-size: 12px;
		}
		 a:hover {
			background: #ccc;
		}
		/*IE 6 (when comma-separated, IE6 didn't work, so these are duped for IE7)*/
		*html  {text-align: center;}
		*html  a {margin: 0; position: relative;}
		/*IE 7*/
		*:first-child+html  {text-align: center;}
		*:first-child+html  a {margin: 0; position: relative;}
	</style>
</head>

<body style="background: #EFEFEF;">
<!-- TOP区 -->
			<div id="contain">  
			  <ul id="map" class="solo">  
			      <li><div class="root section"><a href="#">KBPM流程引擎</a></div>  
			          <ul>  
			              <li><div class="first"><a href="#">数据地图</a></div></li>  
			              <li><div class="section"><a href="#">表单引擎</a></div>  
			                      <ul>  
			                          <li><div class="first"><a href="#">Department1</a></div></li>  
			                          <li><div><a href="#">Department2</a></div></li>  
			                          <li><div><a href="#">Department3</a></div></li>  
			                          <li><div class="last"><a href="#">Department4</a></div></li>  
			                      </ul>  
			              </li>  
			              <li><div class="last"><a href="#">流程引擎</a></div></li>  
			          </ul>  
			      </li>  
			  </ul>  
			</div>
			
</body>
</html>
