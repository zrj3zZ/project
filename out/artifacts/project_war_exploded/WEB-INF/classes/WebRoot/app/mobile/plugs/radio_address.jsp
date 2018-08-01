<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<div data-role="page" data-overlay-theme="e"> 
		<div data-role="header" data-theme="b"> 
			<h1>单选地址簿</h1> 
		</div> 
		<div data-role="content" data-theme="d"> 
			<label for="search-mini">输入您查询的地址信息</label> 
				<input type="search" name="search-mini" id="search-mini" value="" data-mini="true"  onchange="dosearchAddress('<s:property value="fieldName" escapeHtml="false"/>',this.value)"/>
		</div>
		<div>
			<ul data-role="listview" id="searchLister">   
			</ul>
		</div> 
		<div>  
			<a data-role="button" data-rel="search"  onclick="dosearchAddress('<s:property value="fieldName" escapeHtml="false"/>',$('search-mini').val())" data-theme="a">查询</a>   
		</div>  
	</div>
 